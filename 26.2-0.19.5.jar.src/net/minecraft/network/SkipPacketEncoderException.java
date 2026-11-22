/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.handler.codec.EncoderException;
/*    */ import net.minecraft.network.codec.IdDispatchCodec;
/*    */ 
/*    */ public class SkipPacketEncoderException extends EncoderException implements IdDispatchCodec.DontDecorateException, SkipPacketException {
/*    */   public SkipPacketEncoderException(String message) {
/*  8 */     super(message);
/*    */   }
/*    */   
/*    */   public SkipPacketEncoderException(Throwable cause) {
/* 12 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\SkipPacketEncoderException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */