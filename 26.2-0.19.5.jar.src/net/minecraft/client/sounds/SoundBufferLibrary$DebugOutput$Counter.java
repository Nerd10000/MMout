/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import javax.sound.sampled.AudioFormat;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class Counter implements SoundBufferLibrary.DebugOutput {
/*    */   private int totalCount;
/*    */   
/*    */   private long totalSize;
/*    */   
/*    */   public void accountBuffer(Identifier id, int size, AudioFormat format) {
/* 79 */     this.totalCount++;
/* 80 */     this.totalSize += size;
/*    */   }
/*    */   
/*    */   public int totalCount() {
/* 84 */     return this.totalCount;
/*    */   }
/*    */   
/*    */   public long totalSize() {
/* 88 */     return this.totalSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\SoundBufferLibrary$DebugOutput$Counter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */