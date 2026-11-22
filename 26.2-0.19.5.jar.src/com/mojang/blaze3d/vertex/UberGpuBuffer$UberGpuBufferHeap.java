/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.systems.GpuDevice;
/*    */ 
/*    */ public class UberGpuBufferHeap extends TlsfAllocator.Heap {
/*    */   private final GpuBuffer gpuBuffer;
/*    */   
/*    */   public UberGpuBufferHeap(long size, GpuDevice gpuDevice, int usage, String name) {
/* 60 */     super(size);
/* 61 */     this.gpuBuffer = gpuDevice.createBuffer(() -> name, usage | 0x8 | 0x10, size);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vertex\UberGpuBuffer$UberGpuBufferHeap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */