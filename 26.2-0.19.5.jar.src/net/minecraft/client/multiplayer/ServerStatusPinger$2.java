/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.status.ServerStatus;
/*     */ 
/*     */ class null extends ChannelInitializer<Channel> {
/*     */   null(ServerStatusPinger this$0) {}
/*     */   
/*     */   protected void initChannel(Channel channel) {
/*     */     try {
/* 193 */       channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/* 194 */     } catch (ChannelException channelException) {}
/* 197 */     channel.pipeline().addLast(new ChannelHandler[] { (ChannelHandler)new LegacyServerPinger(this.val$rawAddress, (protocolVersion, gameVersion, motd, players, maxPlayers) -> {
/*     */               data.setState(ServerData.State.INCOMPATIBLE);
/*     */               data.version = (Component)Component.literal(gameVersion);
/*     */               data.motd = (Component)Component.literal(motd);
/*     */               data.status = ServerStatusPinger.formatPlayerCount(players, maxPlayers);
/*     */               data.players = new ServerStatus.Players(maxPlayers, players, List.of());
/*     */             }) });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\ServerStatusPinger$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */