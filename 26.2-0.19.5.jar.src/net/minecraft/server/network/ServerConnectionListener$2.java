/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ 
/*     */ class null extends ChannelInitializer<Channel> {
/*     */   protected void initChannel(Channel channel) {
/* 105 */     Connection connection = new Connection(PacketFlow.SERVERBOUND);
/* 106 */     connection.setListenerForServerboundHandshake((PacketListener)new MemoryServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, connection));
/* 107 */     ServerConnectionListener.this.connections.add(connection);
/* 109 */     ChannelPipeline pipeline = channel.pipeline();
/* 111 */     Connection.configureInMemoryPipeline(pipeline, PacketFlow.SERVERBOUND);
/* 113 */     if (SharedConstants.DEBUG_FAKE_LATENCY_MS > 0)
/* 114 */       pipeline.addLast("latency", (ChannelHandler)new ServerConnectionListener.LatencySimulator(SharedConstants.DEBUG_FAKE_LATENCY_MS, SharedConstants.DEBUG_FAKE_JITTER_MS)); 
/* 116 */     connection.configurePacketHandler(pipeline);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerConnectionListener$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */