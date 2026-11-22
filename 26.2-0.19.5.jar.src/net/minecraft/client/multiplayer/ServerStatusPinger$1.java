/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*     */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*     */ import net.minecraft.network.protocol.status.ClientStatusPacketListener;
/*     */ import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
/*     */ import net.minecraft.network.protocol.status.ServerStatus;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ class null implements ClientStatusPacketListener {
/*     */   private boolean success;
/*     */   
/*     */   private boolean receivedPing;
/*     */   
/*     */   private long pingStart;
/*     */   
/*     */   public void handleStatusResponse(ClientboundStatusResponsePacket packet) {
/*  83 */     if (this.receivedPing) {
/*  84 */       connection.disconnect((Component)Component.translatable("multiplayer.status.unrequested"));
/*     */       return;
/*     */     } 
/*  87 */     this.receivedPing = true;
/*  88 */     ServerStatus status = packet.status();
/*  89 */     data.motd = sanitizeDescription(status.description());
/*  91 */     status.version().ifPresentOrElse(version -> {
/*     */           data.version = (Component)Component.literal(version.name());
/*     */           data.protocol = version.protocol();
/*     */         }() -> {
/*     */           data.version = (Component)Component.translatable("multiplayer.status.old");
/*     */           data.protocol = 0;
/*     */         });
/*  99 */     status.players().ifPresentOrElse(players -> {
/*     */           data.status = ServerStatusPinger.formatPlayerCount(players.online(), players.max());
/*     */           data.players = players;
/*     */           if (!players.sample().isEmpty()) {
/*     */             List<Component> playerNames = new ArrayList<>(players.sample().size());
/*     */             for (NameAndId profile : players.sample()) {
/*     */               MutableComponent mutableComponent;
/*     */               if (profile.equals(MinecraftServer.ANONYMOUS_PLAYER_PROFILE)) {
/*     */                 mutableComponent = Component.translatable("multiplayer.status.anonymous_player");
/*     */               } else {
/*     */                 mutableComponent = Component.literal(profile.name());
/*     */               } 
/*     */               playerNames.add(mutableComponent);
/*     */             } 
/*     */             if (players.sample().size() < players.online())
/*     */               playerNames.add(Component.translatable("multiplayer.status.and_more", new Object[] { Integer.valueOf(players.online() - players.sample().size()) })); 
/*     */             data.playerList = playerNames;
/*     */           } else {
/*     */             data.playerList = List.of();
/*     */           } 
/*     */         }() -> data.status = (Component)Component.translatable("multiplayer.status.unknown").withStyle(ChatFormatting.DARK_GRAY));
/* 125 */     status.favicon().ifPresent(newIcon -> {
/*     */           if (!Arrays.equals(newIcon.iconBytes(), data.getIconBytes())) {
/*     */             data.setIconBytes(ServerData.validateIcon(newIcon.iconBytes()));
/*     */             onPersistentDataChange.run();
/*     */           } 
/*     */         });
/* 132 */     this.pingStart = Util.getMillis();
/* 133 */     connection.send((Packet)new ServerboundPingRequestPacket(this.pingStart));
/* 134 */     this.success = true;
/*     */   }
/*     */   
/*     */   private static Component sanitizeDescription(Component original) {
/*     */     try {
/* 139 */       return (Component)ComponentUtils.resolve(ServerStatusPinger.DESCRIPTION_SANITIZE_CONTEXT, original);
/* 140 */     } catch (CommandSyntaxException e) {
/* 141 */       ServerStatusPinger.LOGGER.warn("Failed to sanitize status {}", original, e);
/* 142 */       return (Component)Component.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handlePongResponse(ClientboundPongResponsePacket packet) {
/* 148 */     long then = this.pingStart;
/* 149 */     long now = Util.getMillis();
/* 150 */     data.ping = now - then;
/* 152 */     connection.disconnect((Component)Component.translatable("multiplayer.status.finished"));
/* 154 */     onPongResponse.run();
/*     */   }
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails details) {
/* 159 */     if (!this.success) {
/* 160 */       ServerStatusPinger.this.onPingFailed(details.reason(), data);
/* 161 */       ServerStatusPinger.this.pingLegacyServer(address, rawAddress, data, eventLoopGroupHolder);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isAcceptingMessages() {
/* 167 */     return connection.isConnected();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ServerStatusPinger$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */