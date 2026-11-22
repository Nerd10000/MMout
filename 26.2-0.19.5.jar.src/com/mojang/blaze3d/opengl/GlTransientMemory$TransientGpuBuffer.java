/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ 
/*     */ class TransientGpuBuffer extends GlBuffer {
/*     */   private boolean closed = false;
/*     */   
/*     */   private final long bufferSubmitIndex;
/*     */   
/*     */   protected TransientGpuBuffer(int handle, int usage, long size) {
/* 427 */     super(usage, size, handle, true);
/* 428 */     this.bufferSubmitIndex = paramGlTransientMemory.encoder.currentSubmitIndex();
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 433 */     if (this.closed)
/* 434 */       return true; 
/* 436 */     this.closed = (this.bufferSubmitIndex < GlTransientMemory.this.encoder.currentSubmitIndex());
/* 437 */     return this.closed;
/*     */   }
/*     */   
/*     */   public void close() {
/* 442 */     this.closed = true;
/*     */   }
/*     */   
/*     */   public GpuBufferSlice.MappedView map(long offset, long length, boolean read, boolean write) {
/* 447 */     throw new IllegalStateException("Cannot map Transient buffer");
/*     */   }
/*     */   
/*     */   public GpuBufferSlice slice(long offset, long length) {
/* 452 */     throw new IllegalStateException("Cannot slice transient buffer");
/*     */   }
/*     */   
/*     */   public GpuBufferSlice slice() {
/* 457 */     throw new IllegalStateException("Cannot slice transient buffer");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\GlTransientMemory$TransientGpuBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */