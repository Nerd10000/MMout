/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ public class BufferHandle implements AutoCloseable {
/*     */   private final int offset;
/*     */   
/*     */   private final int size;
/*     */   
/*     */   private boolean closed;
/*     */   
/*     */   public BufferHandle(int offset, int size) {
/* 169 */     this.offset = offset;
/* 170 */     this.size = size;
/*     */   }
/*     */   
/*     */   private void checkValidFor(StagingBuffer stagingBuffer) {
/* 174 */     if (this.closed)
/* 175 */       throw new IllegalStateException("Buffer has already been closed"); 
/* 176 */     if (stagingBuffer != StagingBuffer.this)
/* 177 */       throw new IllegalArgumentException("Buffer is not valid for " + String.valueOf(stagingBuffer)); 
/*     */   }
/*     */   
/*     */   public long size() {
/* 182 */     return this.size;
/*     */   }
/*     */   
/*     */   public void close() {
/* 187 */     if (this.closed)
/*     */       return; 
/* 190 */     this.closed = true;
/* 191 */     StagingBuffer.this.usedBufferCount--;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\StagingBuffer$BufferHandle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */