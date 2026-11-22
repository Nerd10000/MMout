/*     */ package net.minecraft.network;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ 
/*     */ class null extends ChannelInitializer<Channel> {
/*     */   protected void initChannel(Channel channel) {
/* 549 */     ChannelPipeline pipeline = channel.pipeline();
/* 550 */     Connection.configureInMemoryPipeline(pipeline, PacketFlow.CLIENTBOUND);
/* 551 */     connection.configurePacketHandler(pipeline);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\Connection$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */