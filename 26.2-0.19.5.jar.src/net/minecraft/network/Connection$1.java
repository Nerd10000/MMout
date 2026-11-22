/*     */ package net.minecraft.network;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ 
/*     */ class null extends ChannelInitializer<Channel> {
/*     */   protected void initChannel(Channel channel) {
/*     */     try {
/* 454 */       channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.valueOf(true));
/* 455 */     } catch (ChannelException channelException) {}
/* 459 */     ChannelPipeline pipeline = channel.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30));
/* 461 */     Connection.configureSerialization(pipeline, PacketFlow.CLIENTBOUND, false, connection.bandwidthDebugMonitor);
/* 463 */     connection.configurePacketHandler(pipeline);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\Connection$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */