/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface InboundConfigurationTask {
/*    */   void run(ChannelHandlerContext paramChannelHandlerContext);
/*    */   
/*    */   default InboundConfigurationTask andThen(InboundConfigurationTask otherTask) {
/* 67 */     return ctx -> {
/*    */         run(ctx);
/*    */         otherTask.run(ctx);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\UnconfiguredPipelineHandler$InboundConfigurationTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */