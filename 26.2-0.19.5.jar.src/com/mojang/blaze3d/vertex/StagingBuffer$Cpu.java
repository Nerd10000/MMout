/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.systems.CommandEncoder;
/*    */ import java.nio.ByteBuffer;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ 
/*    */ class Cpu extends StagingBuffer {
/*    */   private final ByteBuffer stagingBuffer;
/*    */   
/*    */   private Cpu(int bufferSize) {
/* 77 */     this.stagingBuffer = MemoryUtil.memAlloc(bufferSize);
/*    */   }
/*    */   
/*    */   protected ByteBuffer getWriteBuffer() {
/* 82 */     return this.stagingBuffer;
/*    */   }
/*    */   
/*    */   protected void copyTo(CommandEncoder encoder, GpuBuffer dstBuffer, long dstOffset, long stagingBufferOffset, long copySize) {
/* 87 */     encoder.writeToBuffer(dstBuffer.slice(dstOffset, copySize), this.stagingBuffer.slice((int)stagingBufferOffset, (int)copySize));
/*    */   }
/*    */   
/*    */   public void close() {
/* 92 */     MemoryUtil.memFree(this.stagingBuffer);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\StagingBuffer$Cpu.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */