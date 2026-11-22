/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ 
/*     */ public class Uploader implements AutoCloseable {
/*     */   private final CommandEncoder encoder;
/*     */   
/*     */   public Uploader(CommandEncoder encoder) {
/* 143 */     this.encoder = encoder;
/*     */   }
/*     */   
/*     */   public void copyTo(StagingBuffer.BufferHandle srcBuffer, GpuBuffer dstBuffer, long dstOffset) {
/* 147 */     srcBuffer.checkValidFor(StagingBuffer.this);
/* 148 */     StagingBuffer.this.copyTo(this.encoder, dstBuffer, dstOffset, srcBuffer.offset, srcBuffer.size);
/*     */   }
/*     */   
/*     */   public void close() {
/* 153 */     StagingBuffer.this.tryClearAndRotate();
/*     */   }
/*     */   
/*     */   public void checkValidFor(StagingBuffer stagingBuffer) {
/* 157 */     if (stagingBuffer != StagingBuffer.this)
/* 158 */       throw new IllegalArgumentException("Uploader is not valid for " + String.valueOf(stagingBuffer)); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\StagingBuffer$Uploader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */