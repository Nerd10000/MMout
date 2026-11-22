/*    */ package net.minecraft.server.network;
/*    */ 
/*    */ import io.netty.channel.Channel;
/*    */ import io.netty.channel.ChannelException;
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelInitializer;
/*    */ import io.netty.channel.ChannelOption;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*    */ import net.minecraft.network.Connection;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RateKickingConnection;
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.server.ServerInfo;
/*    */ 
/*    */ class null extends ChannelInitializer<Channel> {
/*    */   protected void initChannel(Channel channel) {
/*    */     try {
/* 69 */       channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/* 70 */     } catch (ChannelException channelException) {}
/* 74 */     ChannelPipeline pipeline = channel.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30));
/* 76 */     if (ServerConnectionListener.this.server.repliesToStatus())
/* 77 */       pipeline.addLast("legacy_query", (ChannelHandler)new LegacyQueryHandler((ServerInfo)ServerConnectionListener.this.getServer())); 
/* 80 */     Connection.configureSerialization(pipeline, PacketFlow.SERVERBOUND, false, null);
/* 82 */     int rateLimitPacketsPerSecond = ServerConnectionListener.this.server.getRateLimitPacketsPerSecond();
/* 83 */     Connection connection = (rateLimitPacketsPerSecond > 0) ? (Connection)new RateKickingConnection(rateLimitPacketsPerSecond) : new Connection(PacketFlow.SERVERBOUND);
/* 84 */     ServerConnectionListener.this.connections.add(connection);
/* 85 */     connection.configurePacketHandler(pipeline);
/* 86 */     connection.setListenerForServerboundHandshake((PacketListener)new ServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, connection));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerConnectionListener$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */