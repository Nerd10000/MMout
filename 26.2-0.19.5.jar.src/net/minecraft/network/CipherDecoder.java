/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import java.util.List;
/*    */ import javax.crypto.Cipher;
/*    */ 
/*    */ public class CipherDecoder extends MessageToMessageDecoder<ByteBuf> {
/*    */   private final CipherBase cipher;
/*    */   
/*    */   public CipherDecoder(Cipher cipher) {
/* 14 */     this.cipher = new CipherBase(cipher);
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
/* 19 */     out.add(this.cipher.decipher(ctx, msg));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\CipherDecoder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */