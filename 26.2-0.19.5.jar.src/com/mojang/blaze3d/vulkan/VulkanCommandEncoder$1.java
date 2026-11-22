/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuFence;
/*     */ 
/*     */ class null implements GpuFence {
/* 576 */   private final long submitIndex = VulkanCommandEncoder.this.currentSubmitIndex;
/*     */   
/*     */   private boolean completed = false;
/*     */   
/*     */   public boolean awaitCompletion(long timeoutMs) {
/* 581 */     if (!this.completed)
/* 582 */       this.completed = VulkanCommandEncoder.this.awaitSubmitCompletion(this.submitIndex, timeoutMs); 
/* 584 */     return this.completed;
/*     */   }
/*     */   
/*     */   public void close() {
/* 589 */     this.completed = true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanCommandEncoder$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */