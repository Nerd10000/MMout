/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import org.lwjgl.util.vma.Vma;
/*     */ 
/*     */ class null implements Runnable {
/*     */   private boolean closed = false;
/*     */   
/*     */   public void run() {
/* 145 */     if (this.closed)
/*     */       return; 
/* 148 */     this.closed = true;
/* 149 */     VulkanGpuBuffer.Direct.this.mappingRefCount--;
/* 150 */     Vma.vmaUnmapMemory(VulkanGpuBuffer.Direct.this.device.vma(), VulkanGpuBuffer.Direct.this.vmaAllocation);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanGpuBuffer$Direct$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */