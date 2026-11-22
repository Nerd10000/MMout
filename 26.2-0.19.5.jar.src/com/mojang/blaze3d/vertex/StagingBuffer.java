/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import java.nio.ByteBuffer;
/*     */ import net.minecraft.client.renderer.MappableRingBuffer;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public abstract class StagingBuffer implements AutoCloseable {
/*     */   private int nextWriteOffset;
/*     */   
/*     */   private int usedBufferCount;
/*     */   
/*     */   public static StagingBuffer create(String name, GpuDevice gpuDevice, int bufferSize) {
/*  25 */     if (gpuDevice.getDeviceInfo().hintsAndWorkarounds().writeToBufferIsSlow() && gpuDevice.getDeviceInfo().features().persistentMapping())
/*  26 */       return new PersistentlyMapped(name, bufferSize); 
/*  28 */     return new Cpu(bufferSize);
/*     */   }
/*     */   
/*     */   public BufferHandle tryAppend(ByteBuffer buffer) {
/*  33 */     int writeOffset = this.nextWriteOffset;
/*  34 */     int bufferSize = buffer.remaining();
/*  35 */     ByteBuffer writeBuffer = getWriteBuffer();
/*  36 */     if (bufferSize > writeBuffer.capacity())
/*  37 */       throw new IllegalArgumentException("Cannot fit allocation of size " + bufferSize + " into staging buffer of size " + writeBuffer.capacity()); 
/*  39 */     if (bufferSize > writeBuffer.capacity() - writeOffset)
/*  40 */       return null; 
/*  42 */     MemoryUtil.memCopy(buffer, writeBuffer.position(writeOffset));
/*  43 */     this.nextWriteOffset += bufferSize;
/*  44 */     this.usedBufferCount++;
/*  45 */     return new BufferHandle(writeOffset, bufferSize);
/*     */   }
/*     */   
/*     */   protected abstract ByteBuffer getWriteBuffer();
/*     */   
/*     */   protected abstract void copyTo(CommandEncoder paramCommandEncoder, GpuBuffer paramGpuBuffer, long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   protected void rotateBuffer() {}
/*     */   
/*     */   public Uploader startUploading(CommandEncoder encoder) {
/*  59 */     return new Uploader(encoder);
/*     */   }
/*     */   
/*     */   private void tryClearAndRotate() {
/*  64 */     if (this.nextWriteOffset > 0 && this.usedBufferCount == 0) {
/*  65 */       rotateBuffer();
/*  66 */       this.nextWriteOffset = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract void close();
/*     */   
/*     */   private static class Cpu extends StagingBuffer {
/*     */     private final ByteBuffer stagingBuffer;
/*     */     
/*     */     private Cpu(int bufferSize) {
/*  77 */       this.stagingBuffer = MemoryUtil.memAlloc(bufferSize);
/*     */     }
/*     */     
/*     */     protected ByteBuffer getWriteBuffer() {
/*  82 */       return this.stagingBuffer;
/*     */     }
/*     */     
/*     */     protected void copyTo(CommandEncoder encoder, GpuBuffer dstBuffer, long dstOffset, long stagingBufferOffset, long copySize) {
/*  87 */       encoder.writeToBuffer(dstBuffer.slice(dstOffset, copySize), this.stagingBuffer.slice((int)stagingBufferOffset, (int)copySize));
/*     */     }
/*     */     
/*     */     public void close() {
/*  92 */       MemoryUtil.memFree(this.stagingBuffer);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class PersistentlyMapped extends StagingBuffer {
/*     */     private final MappableRingBuffer mappableRingBuffer;
/*     */     
/*     */     private GpuBufferSlice.MappedView currentMappedView;
/*     */     
/*     */     private GpuBuffer currentGPUBuffer;
/*     */     
/*     */     private ByteBuffer currentBuffer;
/*     */     
/*     */     private PersistentlyMapped(String name, int bufferSize) {
/* 103 */       this.mappableRingBuffer = new MappableRingBuffer(() -> name + " staging buffer", 18, bufferSize / 2);
/* 108 */       this.currentGPUBuffer = this.mappableRingBuffer.currentBuffer();
/* 109 */       this.currentMappedView = this.currentGPUBuffer.map(false, true);
/* 110 */       this.currentBuffer = this.currentMappedView.data();
/*     */     }
/*     */     
/*     */     protected ByteBuffer getWriteBuffer() {
/* 115 */       return this.currentBuffer;
/*     */     }
/*     */     
/*     */     protected void copyTo(CommandEncoder encoder, GpuBuffer dstBuffer, long dstOffset, long stagingBufferOffset, long copySize) {
/* 120 */       encoder.copyToBuffer(this.currentGPUBuffer.slice(stagingBufferOffset, copySize), dstBuffer.slice(dstOffset, copySize));
/*     */     }
/*     */     
/*     */     protected void rotateBuffer() {
/* 125 */       this.currentMappedView.close();
/* 126 */       this.mappableRingBuffer.rotate();
/* 127 */       this.currentGPUBuffer = this.mappableRingBuffer.currentBuffer();
/* 128 */       this.currentMappedView = this.currentGPUBuffer.map(false, true);
/* 129 */       this.currentBuffer = this.currentMappedView.data();
/*     */     }
/*     */     
/*     */     public void close() {
/* 134 */       this.currentMappedView.close();
/* 135 */       this.mappableRingBuffer.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public class Uploader implements AutoCloseable {
/*     */     private final CommandEncoder encoder;
/*     */     
/*     */     public Uploader(CommandEncoder encoder) {
/* 143 */       this.encoder = encoder;
/*     */     }
/*     */     
/*     */     public void copyTo(StagingBuffer.BufferHandle srcBuffer, GpuBuffer dstBuffer, long dstOffset) {
/* 147 */       srcBuffer.checkValidFor(StagingBuffer.this);
/* 148 */       StagingBuffer.this.copyTo(this.encoder, dstBuffer, dstOffset, srcBuffer.offset, srcBuffer.size);
/*     */     }
/*     */     
/*     */     public void close() {
/* 153 */       StagingBuffer.this.tryClearAndRotate();
/*     */     }
/*     */     
/*     */     public void checkValidFor(StagingBuffer stagingBuffer) {
/* 157 */       if (stagingBuffer != StagingBuffer.this)
/* 158 */         throw new IllegalArgumentException("Uploader is not valid for " + String.valueOf(stagingBuffer)); 
/*     */     }
/*     */   }
/*     */   
/*     */   public class BufferHandle implements AutoCloseable {
/*     */     private final int offset;
/*     */     
/*     */     private final int size;
/*     */     
/*     */     private boolean closed;
/*     */     
/*     */     public BufferHandle(int offset, int size) {
/* 169 */       this.offset = offset;
/* 170 */       this.size = size;
/*     */     }
/*     */     
/*     */     private void checkValidFor(StagingBuffer stagingBuffer) {
/* 174 */       if (this.closed)
/* 175 */         throw new IllegalStateException("Buffer has already been closed"); 
/* 176 */       if (stagingBuffer != StagingBuffer.this)
/* 177 */         throw new IllegalArgumentException("Buffer is not valid for " + String.valueOf(stagingBuffer)); 
/*     */     }
/*     */     
/*     */     public long size() {
/* 182 */       return this.size;
/*     */     }
/*     */     
/*     */     public void close() {
/* 187 */       if (this.closed)
/*     */         return; 
/* 190 */       this.closed = true;
/* 191 */       StagingBuffer.this.usedBufferCount--;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\StagingBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */