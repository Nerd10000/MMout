/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.InputStream;
/*    */ 
/*    */ class NoCloseBuffer extends FilterInputStream {
/*    */   private NoCloseBuffer(InputStream in) {
/* 18 */     super(in);
/*    */   }
/*    */   
/*    */   public void close() {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\LoopingAudioStream$NoCloseBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */