/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ 
/*    */ public interface TransientMemory {
/*    */   default ByteBuffer allocateCpu(long size, long alignment) {
/* 12 */     return allocateCpu(size, alignment, size, 1L);
/*    */   }
/*    */   
/*    */   ByteBuffer allocateCpu(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*    */   
/*    */   default GpuBufferSlice.MappedView allocateStaging(long size, long alignment, int usage) {
/* 18 */     return allocateStaging(size, alignment, usage, size, 1L);
/*    */   }
/*    */   
/*    */   GpuBufferSlice.MappedView allocateStaging(long paramLong1, long paramLong2, int paramInt, long paramLong3, long paramLong4);
/*    */   
/*    */   default GpuBufferSlice allocateGpu(long size, long alignment, int usage) {
/* 24 */     return allocateGpu(size, alignment, usage, size, 1L);
/*    */   }
/*    */   
/*    */   GpuBufferSlice allocateGpu(long paramLong1, long paramLong2, int paramInt, long paramLong3, long paramLong4);
/*    */   
/*    */   default GpuBufferSlice.MappedView allocateGpuMapped(long size, long alignment, int usage) {
/* 30 */     return allocateGpuMapped(size, alignment, usage, size, 1L);
/*    */   }
/*    */   
/*    */   GpuBufferSlice.MappedView allocateGpuMapped(long paramLong1, long paramLong2, int paramInt, long paramLong3, long paramLong4);
/*    */   
/*    */   default GpuBufferSlice uploadStaging(ByteBuffer data, long alignment, int usage) {
/* 36 */     return uploadStaging(data, alignment, usage, data.remaining(), 1L);
/*    */   }
/*    */   
/*    */   default GpuBufferSlice uploadStaging(ByteBuffer data, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 40 */     return uploadStaging(List.of(data), alignment, usage, minimumAllocation, elementSize);
/*    */   }
/*    */   
/*    */   default GpuBufferSlice uploadStaging(List<ByteBuffer> data, long alignment, int usage) {
/* 44 */     long totalSize = 0L;
/* 45 */     for (ByteBuffer buffer : data)
/* 46 */       totalSize += buffer.remaining(); 
/* 48 */     return uploadStaging(data, alignment, usage, totalSize, 1L);
/*    */   }
/*    */   
/*    */   GpuBufferSlice uploadStaging(List<ByteBuffer> paramList, long paramLong1, int paramInt, long paramLong2, long paramLong3);
/*    */   
/*    */   default GpuBufferSlice uploadGpu(ByteBuffer data, long alignment, int usage) {
/* 54 */     return uploadGpu(data, alignment, usage, data.remaining(), 1L);
/*    */   }
/*    */   
/*    */   default GpuBufferSlice uploadGpu(ByteBuffer data, long alignment, int usage, long minimumAllocation, long elementSize) {
/* 58 */     return uploadGpu(List.of(data), alignment, usage, minimumAllocation, elementSize);
/*    */   }
/*    */   
/*    */   default GpuBufferSlice uploadGpu(List<ByteBuffer> data, long alignment, int usage) {
/* 62 */     long totalSize = 0L;
/* 63 */     for (ByteBuffer buffer : data)
/* 64 */       totalSize += buffer.remaining(); 
/* 66 */     return uploadGpu(data, alignment, usage, totalSize, 1L);
/*    */   }
/*    */   
/*    */   GpuBufferSlice uploadGpu(List<ByteBuffer> paramList, long paramLong1, int paramInt, long paramLong2, long paramLong3);
/*    */   
/*    */   List<GpuBufferSlice> multiUploadStaging(List<ByteBuffer> paramList, long paramLong, int paramInt);
/*    */   
/*    */   List<GpuBufferSlice> multiUploadGpu(List<ByteBuffer> paramList, long paramLong, int paramInt);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\TransientMemory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */