/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RateKickingConnection;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ 
/*     */ class null extends ChannelInitializer<Channel> {
/*     */   protected void initChannel(Channel ch) {
/* 134 */     int rateLimitPacketsPerSecond = ServerConnectionListener.this.server.getRateLimitPacketsPerSecond();
/* 137 */     Connection connection = (rateLimitPacketsPerSecond > 0) ? (Connection)new RateKickingConnection(rateLimitPacketsPerSecond) : new Connection(PacketFlow.SERVERBOUND);
/* 140 */     ChannelPipeline pipeline = ch.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30));
/* 142 */     Connection.configureSerialization(pipeline, PacketFlow.SERVERBOUND, false, null);
/* 143 */     connection.configurePacketHandler(pipeline);
/* 144 */     connection.setListenerForServerboundHandshake((PacketListener)new ServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, connection));
/* 145 */     connection.setIntendedProfileId(profileId);
/* 146 */     ServerConnectionListener.this.connections.add(connection);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerConnectionListener$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */