/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuFence;
/*    */ 
/*    */ public class GlFence implements GpuFence {
/*    */   private final GlCommandEncoder encoder;
/*    */   
/*    */   private final long submitIndex;
/*    */   
/*    */   private boolean closedOrCompleted;
/*    */   
/*    */   GlFence(GlCommandEncoder encoder) {
/* 13 */     this.encoder = encoder;
/* 14 */     this.submitIndex = encoder.currentSubmitIndex();
/*    */   }
/*    */   
/*    */   public void close() {
/* 19 */     this.closedOrCompleted = true;
/*    */   }
/*    */   
/*    */   public boolean awaitCompletion(long timeoutNS) {
/* 24 */     if (this.closedOrCompleted)
/* 25 */       return true; 
/* 28 */     this.closedOrCompleted = this.encoder.awaitSubmit(this.submitIndex, timeoutNS);
/* 29 */     return this.closedOrCompleted;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlFence.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */