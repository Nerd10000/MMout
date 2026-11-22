/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*    */ import io.netty.channel.ChannelPromise;
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import io.netty.util.ReferenceCountUtil;
/*    */ 
/*    */ public class Outbound extends ChannelOutboundHandlerAdapter {
/*    */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 46 */     if (msg instanceof net.minecraft.network.protocol.Packet) {
/* 47 */       ReferenceCountUtil.release(msg);
/* 48 */       throw new EncoderException("Pipeline has no outbound protocol configured, can't process packet " + String.valueOf(msg));
/*    */     } 
/* 49 */     if (msg instanceof UnconfiguredPipelineHandler.OutboundConfigurationTask) {
/* 49 */       UnconfiguredPipelineHandler.OutboundConfigurationTask configurationTask = (UnconfiguredPipelineHandler.OutboundConfigurationTask)msg;
/*    */       try {
/* 51 */         configurationTask.run(ctx);
/*    */       } finally {
/* 53 */         ReferenceCountUtil.release(msg);
/*    */       } 
/* 55 */       promise.setSuccess();
/*    */     } else {
/* 57 */       ctx.write(msg, promise);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\UnconfiguredPipelineHandler$Outbound.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */