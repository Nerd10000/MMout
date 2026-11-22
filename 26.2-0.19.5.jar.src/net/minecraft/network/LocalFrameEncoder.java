/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelOutboundHandlerAdapter;
/*    */ import io.netty.channel.ChannelPromise;
/*    */ 
/*    */ public class LocalFrameEncoder extends ChannelOutboundHandlerAdapter {
/*    */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
/* 10 */     ctx.write(HiddenByteBuf.pack(msg), promise);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\LocalFrameEncoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */