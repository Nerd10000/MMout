/*   */ package net.minecraft.network;
/*   */ 
/*   */ import io.netty.channel.ChannelHandlerContext;
/*   */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*   */ 
/*   */ public class LocalFrameDecoder extends ChannelInboundHandlerAdapter {
/*   */   public void channelRead(ChannelHandlerContext ctx, Object msg) {
/* 9 */     ctx.fireChannelRead(HiddenByteBuf.unpack(msg));
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\LocalFrameDecoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */