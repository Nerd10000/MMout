/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ 
/*     */ class TransientGpuBuffer extends VulkanGpuBuffer {
/*     */   private boolean closed = false;
/*     */   
/*     */   private final long bufferSubmitIndex;
/*     */   
/*     */   public TransientGpuBuffer(long vkBuffer, int usage, int size, long bufferSubmitIndex) {
/* 363 */     super(vkBuffer, usage, size);
/* 364 */     this.bufferSubmitIndex = bufferSubmitIndex;
/*     */   }
/*     */   
/*     */   public void destroy() {}
/*     */   
/*     */   public GpuBufferSlice.MappedView map(long offset, long length, boolean read, boolean write) {
/* 373 */     throw new IllegalStateException("Cannot map transient buffer");
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 378 */     if (this.closed)
/* 379 */       return true; 
/* 381 */     this.closed = (this.bufferSubmitIndex < VulkanTransientMemory.this.submitIndex);
/* 382 */     return this.closed;
/*     */   }
/*     */   
/*     */   public void close() {
/* 387 */     this.closed = true;
/*     */   }
/*     */   
/*     */   public GpuBufferSlice slice(long offset, long length) {
/* 392 */     throw new IllegalStateException("Cannot slice transient buffer");
/*     */   }
/*     */   
/*     */   public GpuBufferSlice slice() {
/* 397 */     throw new IllegalStateException("Cannot slice transient buffer");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanTransientMemory$TransientGpuBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */