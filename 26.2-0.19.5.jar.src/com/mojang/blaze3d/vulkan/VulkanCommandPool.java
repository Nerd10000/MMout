/*    */ package com.mojang.blaze3d.vulkan;
/*    */ 
/*    */ import java.nio.LongBuffer;
/*    */ import org.lwjgl.PointerBuffer;
/*    */ import org.lwjgl.system.CustomBuffer;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ import org.lwjgl.vulkan.VK12;
/*    */ import org.lwjgl.vulkan.VkCommandBuffer;
/*    */ import org.lwjgl.vulkan.VkCommandBufferAllocateInfo;
/*    */ import org.lwjgl.vulkan.VkCommandPoolCreateInfo;
/*    */ 
/*    */ public class VulkanCommandPool implements Destroyable {
/*    */   private static final int BUFFER_ALLOC_COUNT = 32;
/*    */   
/*    */   private static final int HANDLE_BUFFER_BLOCK_SIZE = 512;
/*    */   
/*    */   private final VulkanDevice device;
/*    */   
/*    */   private final long commandPool;
/*    */   
/*    */   private PointerBuffer allocatedBuffers;
/*    */   
/*    */   public VulkanCommandPool(VulkanDevice device, VulkanQueue queue) {
/* 24 */     this.device = device;
/* 25 */     MemoryStack stack = MemoryStack.stackPush();
/*    */     try {
/* 26 */       VkCommandPoolCreateInfo commandPoolCreateInfo = VkCommandPoolCreateInfo.calloc(stack).sType$Default();
/* 27 */       commandPoolCreateInfo.flags(1);
/* 28 */       commandPoolCreateInfo.queueFamilyIndex(queue.queueFamilyIndex());
/* 29 */       LongBuffer commandPoolHandlePtr = stack.callocLong(1);
/* 30 */       VulkanUtils.crashIfFailure(device, VK12.vkCreateCommandPool(device.vkDevice(), commandPoolCreateInfo, null, commandPoolHandlePtr), "Failed to create VkCommandPool");
/* 31 */       this.commandPool = commandPoolHandlePtr.get(0);
/* 32 */       if (stack != null)
/* 32 */         stack.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (stack != null)
/*    */         try {
/*    */           stack.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/* 33 */     this.allocatedBuffers = MemoryUtil.memAllocPointer(512);
/* 34 */     this.allocatedBuffers.limit(0);
/*    */   }
/*    */   
/*    */   public void destroy() {
/* 39 */     release();
/* 40 */     this.allocatedBuffers.free();
/* 41 */     VK12.vkDestroyCommandPool(this.device.vkDevice(), this.commandPool, null);
/*    */   }
/*    */   
/*    */   public void release() {
/* 45 */     this.allocatedBuffers.rewind();
/* 46 */     if (this.allocatedBuffers.hasRemaining()) {
/* 47 */       VK12.vkFreeCommandBuffers(this.device.vkDevice(), this.commandPool, this.allocatedBuffers);
/* 48 */       this.allocatedBuffers.clear();
/* 49 */       MemoryUtil.memSet((CustomBuffer)this.allocatedBuffers, 0);
/*    */     } 
/* 51 */     VK12.vkResetCommandPool(this.device.vkDevice(), this.commandPool, 1);
/* 52 */     this.allocatedBuffers.limit(0);
/*    */   }
/*    */   
/*    */   public void reset() {
/* 56 */     VK12.vkResetCommandPool(this.device.vkDevice(), this.commandPool, 0);
/* 57 */     this.allocatedBuffers.rewind();
/*    */   }
/*    */   
/*    */   private void allocateMoreBuffers() {
/* 61 */     MemoryStack stack = MemoryStack.stackPush();
/*    */     try {
/* 63 */       if (this.allocatedBuffers.capacity() - this.allocatedBuffers.limit() < 32) {
/* 64 */         PointerBuffer newBuffer = MemoryUtil.memRealloc(this.allocatedBuffers, this.allocatedBuffers.capacity() + 512);
/* 65 */         newBuffer.limit(this.allocatedBuffers.limit());
/* 66 */         this.allocatedBuffers = newBuffer;
/*    */       } 
/* 69 */       VkCommandBufferAllocateInfo allocateInfo = VkCommandBufferAllocateInfo.calloc(stack).sType$Default();
/* 70 */       allocateInfo.commandPool(this.commandPool);
/* 71 */       allocateInfo.level(0);
/* 72 */       allocateInfo.commandBufferCount(32);
/* 73 */       this.allocatedBuffers.limit(this.allocatedBuffers.limit() + 32);
/* 74 */       PointerBuffer buffers = (PointerBuffer)this.allocatedBuffers.slice(0, 32);
/* 75 */       VulkanUtils.crashIfFailure(this.device, VK12.vkAllocateCommandBuffers(this.device.vkDevice(), allocateInfo, buffers), "Failed to allocate VkCommandBuffers");
/* 76 */       if (stack != null)
/* 76 */         stack.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (stack != null)
/*    */         try {
/*    */           stack.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */   
/*    */   public VkCommandBuffer allocateBuffer() {
/* 80 */     if (!this.allocatedBuffers.hasRemaining())
/* 81 */       allocateMoreBuffers(); 
/* 83 */     return new VkCommandBuffer(this.allocatedBuffers.get(), this.device.vkDevice());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanCommandPool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */