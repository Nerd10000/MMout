/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.screens.ConnectScreen;
/*     */ import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.ResolutionContext;
/*     */ import net.minecraft.network.chat.contents.objects.ObjectInfo;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*     */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*     */ import net.minecraft.network.protocol.status.ClientStatusPacketListener;
/*     */ import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
/*     */ import net.minecraft.network.protocol.status.ServerStatus;
/*     */ import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerStatusPinger {
/*  46 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  47 */   private static final Component CANT_CONNECT_MESSAGE = (Component)Component.translatable("multiplayer.status.cannot_connect").withColor(-65536);
/*     */   
/*     */   private static final ResolutionContext DESCRIPTION_SANITIZE_CONTEXT;
/*     */   
/*     */   static {
/*  53 */     DESCRIPTION_SANITIZE_CONTEXT = ResolutionContext.builder().withObjectInfoValidator(description -> !(description instanceof net.minecraft.network.chat.contents.objects.PlayerSprite)).setDepthLimit(16).setDepthLimitBehavior(ResolutionContext.LimitBehavior.DISCARD_REMAINING).build();
/*     */   }
/*     */   
/*  55 */   private final List<Connection> connections = Collections.synchronizedList(Lists.newArrayList());
/*     */   
/*     */   public void pingServer(final ServerData data, final Runnable onPersistentDataChange, final Runnable onPongResponse, final EventLoopGroupHolder eventLoopGroupHolder) throws UnknownHostException {
/*  58 */     final ServerAddress rawAddress = ServerAddress.parseString(data.ip);
/*  61 */     Optional<InetSocketAddress> resolvedAddress = ServerNameResolver.DEFAULT.resolveAddress(rawAddress).map(ResolvedServerAddress::asInetSocketAddress);
/*  62 */     if (resolvedAddress.isEmpty()) {
/*  63 */       onPingFailed(ConnectScreen.UNKNOWN_HOST_MESSAGE, data);
/*     */       return;
/*     */     } 
/*  67 */     final InetSocketAddress address = resolvedAddress.get();
/*  69 */     final Connection connection = Connection.connectToServer(address, eventLoopGroupHolder, null);
/*  71 */     this.connections.add(connection);
/*  73 */     data.motd = (Component)Component.translatable("multiplayer.status.pinging");
/*  74 */     data.playerList = Collections.emptyList();
/*  76 */     ClientStatusPacketListener listener = new ClientStatusPacketListener() {
/*     */         private boolean success;
/*     */         
/*     */         private boolean receivedPing;
/*     */         
/*     */         private long pingStart;
/*     */         
/*     */         public void handleStatusResponse(ClientboundStatusResponsePacket packet) {
/*  83 */           if (this.receivedPing) {
/*  84 */             connection.disconnect((Component)Component.translatable("multiplayer.status.unrequested"));
/*     */             return;
/*     */           } 
/*  87 */           this.receivedPing = true;
/*  88 */           ServerStatus status = packet.status();
/*  89 */           data.motd = sanitizeDescription(status.description());
/*  91 */           status.version().ifPresentOrElse(version -> {
/*     */                 data.version = (Component)Component.literal(version.name());
/*     */                 data.protocol = version.protocol();
/*     */               }() -> {
/*     */                 data.version = (Component)Component.translatable("multiplayer.status.old");
/*     */                 data.protocol = 0;
/*     */               });
/*  99 */           status.players().ifPresentOrElse(players -> {
/*     */                 data.status = ServerStatusPinger.formatPlayerCount(players.online(), players.max());
/*     */                 data.players = players;
/*     */                 if (!players.sample().isEmpty()) {
/*     */                   List<Component> playerNames = new ArrayList<>(players.sample().size());
/*     */                   for (NameAndId profile : players.sample()) {
/*     */                     MutableComponent mutableComponent;
/*     */                     if (profile.equals(MinecraftServer.ANONYMOUS_PLAYER_PROFILE)) {
/*     */                       mutableComponent = Component.translatable("multiplayer.status.anonymous_player");
/*     */                     } else {
/*     */                       mutableComponent = Component.literal(profile.name());
/*     */                     } 
/*     */                     playerNames.add(mutableComponent);
/*     */                   } 
/*     */                   if (players.sample().size() < players.online())
/*     */                     playerNames.add(Component.translatable("multiplayer.status.and_more", new Object[] { Integer.valueOf(players.online() - players.sample().size()) })); 
/*     */                   data.playerList = playerNames;
/*     */                 } else {
/*     */                   data.playerList = List.of();
/*     */                 } 
/*     */               }() -> data.status = (Component)Component.translatable("multiplayer.status.unknown").withStyle(ChatFormatting.DARK_GRAY));
/* 125 */           status.favicon().ifPresent(newIcon -> {
/*     */                 if (!Arrays.equals(newIcon.iconBytes(), data.getIconBytes())) {
/*     */                   data.setIconBytes(ServerData.validateIcon(newIcon.iconBytes()));
/*     */                   onPersistentDataChange.run();
/*     */                 } 
/*     */               });
/* 132 */           this.pingStart = Util.getMillis();
/* 133 */           connection.send((Packet)new ServerboundPingRequestPacket(this.pingStart));
/* 134 */           this.success = true;
/*     */         }
/*     */         
/*     */         private static Component sanitizeDescription(Component original) {
/*     */           try {
/* 139 */             return (Component)ComponentUtils.resolve(ServerStatusPinger.DESCRIPTION_SANITIZE_CONTEXT, original);
/* 140 */           } catch (CommandSyntaxException e) {
/* 141 */             ServerStatusPinger.LOGGER.warn("Failed to sanitize status {}", original, e);
/* 142 */             return (Component)Component.empty();
/*     */           } 
/*     */         }
/*     */         
/*     */         public void handlePongResponse(ClientboundPongResponsePacket packet) {
/* 148 */           long then = this.pingStart;
/* 149 */           long now = Util.getMillis();
/* 150 */           data.ping = now - then;
/* 152 */           connection.disconnect((Component)Component.translatable("multiplayer.status.finished"));
/* 154 */           onPongResponse.run();
/*     */         }
/*     */         
/*     */         public void onDisconnect(DisconnectionDetails details) {
/* 159 */           if (!this.success) {
/* 160 */             ServerStatusPinger.this.onPingFailed(details.reason(), data);
/* 161 */             ServerStatusPinger.this.pingLegacyServer(address, rawAddress, data, eventLoopGroupHolder);
/*     */           } 
/*     */         }
/*     */         
/*     */         public boolean isAcceptingMessages() {
/* 167 */           return connection.isConnected();
/*     */         }
/*     */       };
/*     */     try {
/* 172 */       connection.initiateServerboundStatusConnection(rawAddress.getHost(), rawAddress.getPort(), listener);
/* 173 */       connection.send((Packet)ServerboundStatusRequestPacket.INSTANCE);
/* 174 */     } catch (Throwable t) {
/* 175 */       LOGGER.error("Failed to ping server {}", rawAddress, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onPingFailed(Component reason, ServerData data) {
/* 180 */     LOGGER.error("Can't ping {}: {}", data.ip, reason.getString());
/* 181 */     data.motd = CANT_CONNECT_MESSAGE;
/* 182 */     data.status = CommonComponents.EMPTY;
/*     */   }
/*     */   
/*     */   private void pingLegacyServer(InetSocketAddress resolvedAddress, final ServerAddress rawAddress, final ServerData data, EventLoopGroupHolder eventLoopGroupHolder) {
/* 186 */     ((Bootstrap)((Bootstrap)((Bootstrap)(new Bootstrap())
/* 187 */       .group(eventLoopGroupHolder.eventLoopGroup()))
/* 188 */       .handler((ChannelHandler)new ChannelInitializer<Channel>(this) {
/*     */           protected void initChannel(Channel channel) {
/*     */             try {
/* 193 */               channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/* 194 */             } catch (ChannelException channelException) {}
/* 197 */             channel.pipeline().addLast(new ChannelHandler[] { (ChannelHandler)new LegacyServerPinger(this.val$rawAddress, (protocolVersion, gameVersion, motd, players, maxPlayers) -> {
/*     */                       data.setState(ServerData.State.INCOMPATIBLE);
/*     */                       data.version = (Component)Component.literal(gameVersion);
/*     */                       data.motd = (Component)Component.literal(motd);
/*     */                       data.status = ServerStatusPinger.formatPlayerCount(players, maxPlayers);
/*     */                       data.players = new ServerStatus.Players(maxPlayers, players, List.of());
/*     */                     }) });
/*     */           }
/* 208 */         })).channel(eventLoopGroupHolder.channelCls()))
/* 209 */       .connect(resolvedAddress.getAddress(), resolvedAddress.getPort());
/*     */   }
/*     */   
/*     */   public static Component formatPlayerCount(int curPlayers, int maxPlayers) {
/* 213 */     MutableComponent mutableComponent1 = Component.literal(Integer.toString(curPlayers)).withStyle(ChatFormatting.GRAY);
/* 214 */     MutableComponent mutableComponent2 = Component.literal(Integer.toString(maxPlayers)).withStyle(ChatFormatting.GRAY);
/* 215 */     return (Component)Component.translatable("multiplayer.status.player_count", new Object[] { mutableComponent1, mutableComponent2 }).withStyle(ChatFormatting.DARK_GRAY);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 219 */     synchronized (this.connections) {
/* 220 */       Iterator<Connection> iterator = this.connections.iterator();
/* 221 */       while (iterator.hasNext()) {
/* 222 */         Connection connection = iterator.next();
/* 224 */         if (connection.isConnected()) {
/* 225 */           connection.tick();
/*     */           continue;
/*     */         } 
/* 227 */         iterator.remove();
/* 228 */         connection.handleDisconnection();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void removeAll() {
/* 235 */     synchronized (this.connections) {
/* 236 */       Iterator<Connection> iterator = this.connections.iterator();
/* 237 */       while (iterator.hasNext()) {
/* 238 */         Connection connection = iterator.next();
/* 240 */         if (connection.isConnected()) {
/* 241 */           iterator.remove();
/* 242 */           connection.disconnect((Component)Component.translatable("multiplayer.status.cancelled"));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ServerStatusPinger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */