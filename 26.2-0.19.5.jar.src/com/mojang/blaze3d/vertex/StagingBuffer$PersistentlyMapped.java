/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import java.nio.ByteBuffer;
/*     */ import net.minecraft.client.renderer.MappableRingBuffer;
/*     */ 
/*     */ class PersistentlyMapped extends StagingBuffer {
/*     */   private final MappableRingBuffer mappableRingBuffer;
/*     */   
/*     */   private GpuBufferSlice.MappedView currentMappedView;
/*     */   
/*     */   private GpuBuffer currentGPUBuffer;
/*     */   
/*     */   private ByteBuffer currentBuffer;
/*     */   
/*     */   private PersistentlyMapped(String name, int bufferSize) {
/* 103 */     this.mappableRingBuffer = new MappableRingBuffer(() -> name + " staging buffer", 18, bufferSize / 2);
/* 108 */     this.currentGPUBuffer = this.mappableRingBuffer.currentBuffer();
/* 109 */     this.currentMappedView = this.currentGPUBuffer.map(false, true);
/* 110 */     this.currentBuffer = this.currentMappedView.data();
/*     */   }
/*     */   
/*     */   protected ByteBuffer getWriteBuffer() {
/* 115 */     return this.currentBuffer;
/*     */   }
/*     */   
/*     */   protected void copyTo(CommandEncoder encoder, GpuBuffer dstBuffer, long dstOffset, long stagingBufferOffset, long copySize) {
/* 120 */     encoder.copyToBuffer(this.currentGPUBuffer.slice(stagingBufferOffset, copySize), dstBuffer.slice(dstOffset, copySize));
/*     */   }
/*     */   
/*     */   protected void rotateBuffer() {
/* 125 */     this.currentMappedView.close();
/* 126 */     this.mappableRingBuffer.rotate();
/* 127 */     this.currentGPUBuffer = this.mappableRingBuffer.currentBuffer();
/* 128 */     this.currentMappedView = this.currentGPUBuffer.map(false, true);
/* 129 */     this.currentBuffer = this.currentMappedView.data();
/*     */   }
/*     */   
/*     */   public void close() {
/* 134 */     this.currentMappedView.close();
/* 135 */     this.mappableRingBuffer.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\StagingBuffer$PersistentlyMapped.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */