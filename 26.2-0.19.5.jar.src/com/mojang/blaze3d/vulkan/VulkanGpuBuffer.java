/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.function.Supplier;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.util.vma.Vma;
/*     */ 
/*     */ public abstract class VulkanGpuBuffer extends GpuBuffer implements Destroyable {
/*     */   private final long vkBuffer;
/*     */   
/*     */   public VulkanGpuBuffer(long vkBuffer, int usage, long size) {
/*  22 */     super(usage, size);
/*  23 */     this.vkBuffer = vkBuffer;
/*     */   }
/*     */   
/*     */   public long vkBuffer() {
/*  27 */     return this.vkBuffer;
/*     */   }
/*     */   
/*     */   public static class Direct extends VulkanGpuBuffer {
/*     */     private boolean closed;
/*     */     
/*     */     protected final VulkanDevice device;
/*     */     
/*     */     private final long vmaAllocation;
/*     */     
/*     */     private int mappingRefCount;
/*     */     
/*     */     public Direct(VulkanDevice device, Supplier<String> label, int usage, long size, boolean forceHostVisibleAllocation) {
/*  84 */       super(vkBuffer, usage, size);
/*     */       long vkBuffer;
/*     */       this.closed = false;
/*     */       this.mappingRefCount = 0;
/*     */     }
/*     */     
/*     */     public void destroy() {
/*  89 */       Vma.vmaDestroyBuffer(this.device.vma(), vkBuffer(), this.vmaAllocation);
/*     */     }
/*     */     
/*     */     public boolean isClosed() {
/*  94 */       return this.closed;
/*     */     }
/*     */     
/*     */     public void close() {
/*  99 */       if (this.closed)
/*     */         return; 
/* 102 */       this.closed = true;
/* 103 */       if (this.mappingRefCount != 0)
/* 104 */         throw new IllegalStateException("Attempt to close a mapped buffer"); 
/* 106 */       this.device.createCommandEncoder().queueForDestroy(this);
/*     */     }
/*     */     
/*     */     public GpuBufferSlice.MappedView map(long offset, long length, boolean read, boolean write) {
/* 113 */       if (isClosed())
/* 114 */         throw new IllegalStateException("Buffer already closed"); 
/* 116 */       if (!read && !write)
/* 117 */         throw new IllegalArgumentException("At least read or write must be true"); 
/* 119 */       if (read && (usage() & 0x1) == 0)
/* 120 */         throw new IllegalStateException("Buffer is not readable"); 
/* 122 */       if (write && (usage() & 0x2) == 0)
/* 123 */         throw new IllegalStateException("Buffer is not writable"); 
/* 125 */       if (offset + length > size())
/* 126 */         throw new IllegalArgumentException("Cannot map more data than this buffer can hold (attempting to map " + length + " bytes at offset " + offset + " from " + size() + " size buffer)"); 
/* 128 */       if (length > 2147483647L)
/* 129 */         throw new IllegalArgumentException("Mapping buffer slice larger than 2GB is not supported"); 
/* 131 */       if (offset < 0L || length < 0L)
/* 132 */         throw new IllegalArgumentException("Offset or length must be positive integer values"); 
/* 135 */       this.mappingRefCount++;
/* 136 */       MemoryStack stack = MemoryStack.stackPush();
/*     */       try {
/* 137 */         PointerBuffer pointer = stack.callocPointer(1);
/* 138 */         VulkanUtils.crashIfFailure(this.device, Vma.vmaMapMemory(this.device.vma(), this.vmaAllocation, pointer), "Failed to map buffer");
/* 139 */         ByteBuffer byteBuffer = MemoryUtil.memByteBuffer(pointer.get(0) + offset, (int)length);
/* 140 */         GpuBufferSlice.MappedView mappedView = new GpuBufferSlice.MappedView(slice(offset, length), byteBuffer, new Runnable() {
/*     */               private boolean closed = false;
/*     */               
/*     */               public void run() {
/* 145 */                 if (this.closed)
/*     */                   return; 
/* 148 */                 this.closed = true;
/* 149 */                 VulkanGpuBuffer.Direct.this.mappingRefCount--;
/* 150 */                 Vma.vmaUnmapMemory(VulkanGpuBuffer.Direct.this.device.vma(), VulkanGpuBuffer.Direct.this.vmaAllocation);
/*     */               }
/*     */             });
/* 153 */         if (stack != null)
/* 153 */           stack.close(); 
/*     */         return mappedView;
/*     */       } catch (Throwable throwable) {
/*     */         if (stack != null)
/*     */           try {
/*     */             stack.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements Runnable {
/*     */     private boolean closed;
/*     */     
/*     */     null() {
/*     */       this.closed = false;
/*     */     }
/*     */     
/*     */     public void run() {
/*     */       if (this.closed)
/*     */         return; 
/*     */       this.closed = true;
/*     */       VulkanGpuBuffer.Direct.this.mappingRefCount--;
/*     */       Vma.vmaUnmapMemory(VulkanGpuBuffer.Direct.this.device.vma(), VulkanGpuBuffer.Direct.this.vmaAllocation);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanGpuBuffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */