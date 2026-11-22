/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface OutboundConfigurationTask {
/*    */   void run(ChannelHandlerContext paramChannelHandlerContext);
/*    */   
/*    */   default OutboundConfigurationTask andThen(OutboundConfigurationTask otherTask) {
/* 79 */     return ctx -> {
/*    */         run(ctx);
/*    */         otherTask.run(ctx);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\UnconfiguredPipelineHandler$OutboundConfigurationTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */