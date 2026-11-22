/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import java.io.BufferedInputStream;
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.ByteBuffer;
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ 
/*    */ public class LoopingAudioStream implements AudioStream {
/*    */   private final AudioStreamProvider provider;
/*    */   
/*    */   private AudioStream stream;
/*    */   
/*    */   private final BufferedInputStream bufferedInputStream;
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface AudioStreamProvider {
/*    */     AudioStream create(InputStream param1InputStream) throws IOException;
/*    */   }
/*    */   
/*    */   private static class NoCloseBuffer extends FilterInputStream {
/*    */     private NoCloseBuffer(InputStream in) {
/* 18 */       super(in);
/*    */     }
/*    */     
/*    */     public void close() {}
/*    */   }
/*    */   
/*    */   public LoopingAudioStream(AudioStreamProvider provider, InputStream originalInputStream) throws IOException {
/* 28 */     this.provider = provider;
/* 29 */     this.bufferedInputStream = new BufferedInputStream(originalInputStream);
/* 30 */     this.bufferedInputStream.mark(2147483647);
/* 31 */     this.stream = provider.create(new NoCloseBuffer(this.bufferedInputStream));
/*    */   }
/*    */   
/*    */   public AudioFormat getFormat() {
/* 36 */     return this.stream.getFormat();
/*    */   }
/*    */   
/*    */   public ByteBuffer read(int expectedSize) throws IOException {
/* 41 */     ByteBuffer result = this.stream.read(expectedSize);
/* 42 */     if (!result.hasRemaining()) {
/* 43 */       this.stream.close();
/* 44 */       this.bufferedInputStream.reset();
/* 45 */       this.stream = this.provider.create(new NoCloseBuffer(this.bufferedInputStream));
/* 46 */       result = this.stream.read(expectedSize);
/*    */     } 
/* 49 */     return result;
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 54 */     this.stream.close();
/* 55 */     this.bufferedInputStream.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\LoopingAudioStream.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */