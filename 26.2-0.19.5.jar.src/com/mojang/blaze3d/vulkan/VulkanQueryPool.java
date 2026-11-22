/*    */ package com.mojang.blaze3d.vulkan;
/*    */ 
/*    */ import com.mojang.blaze3d.systems.GpuQueryPool;
/*    */ import java.nio.LongBuffer;
/*    */ import java.util.OptionalLong;
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ import org.lwjgl.vulkan.VK12;
/*    */ import org.lwjgl.vulkan.VkQueryPoolCreateInfo;
/*    */ 
/*    */ public class VulkanQueryPool implements GpuQueryPool, Destroyable {
/*    */   private final VulkanDevice device;
/*    */   
/*    */   private final int size;
/*    */   
/*    */   private final long vkQueryPool;
/*    */   
/*    */   public VulkanQueryPool(VulkanDevice device, int size) {
/* 17 */     this.device = device;
/* 18 */     this.size = size;
/* 19 */     MemoryStack stack = MemoryStack.stackPush();
/*    */     try {
/* 20 */       VkQueryPoolCreateInfo createInfo = VkQueryPoolCreateInfo.calloc(stack).sType$Default();
/* 21 */       createInfo.queryType(2);
/* 22 */       createInfo.queryCount(size);
/* 23 */       LongBuffer pointer = stack.callocLong(1);
/* 24 */       VulkanUtils.crashIfFailure(device, VK12.vkCreateQueryPool(device.vkDevice(), createInfo, null, pointer), "Cannot create query pool");
/* 25 */       this.vkQueryPool = pointer.get(0);
/* 26 */       VK12.vkResetQueryPool(device.vkDevice(), this.vkQueryPool, 0, size);
/* 27 */       if (stack != null)
/* 27 */         stack.close(); 
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
/*    */   public int size() {
/* 32 */     return this.size;
/*    */   }
/*    */   
/*    */   public OptionalLong getValue(int index) {
/* 37 */     return getValues(index, 1)[0];
/*    */   }
/*    */   
/*    */   public OptionalLong[] getValues(int index, int count) {
/* 42 */     if (index + count > this.size)
/* 43 */       throw new IndexOutOfBoundsException("getValues would read out-of-bounds for an array of " + count + " starting at " + index + ", when total size is " + this.size); 
/* 45 */     OptionalLong[] result = new OptionalLong[count];
/* 46 */     MemoryStack stack = MemoryStack.stackPush();
/*    */     try {
/* 47 */       LongBuffer values = stack.callocLong(2 * count);
/* 48 */       VulkanUtils.crashIfFailure(this.device, VK12.vkGetQueryPoolResults(this.device.vkDevice(), this.vkQueryPool, index, count, values, 16L, 5), "Cannot fetch query results");
/* 51 */       for (int i = 0; i < count; i++) {
/* 52 */         if (values.get(i * 2 + 1) != 0L) {
/* 53 */           result[i] = OptionalLong.of(values.get(i * 2));
/*    */         } else {
/* 55 */           result[i] = OptionalLong.empty();
/*    */         } 
/*    */       } 
/* 58 */       if (stack != null)
/* 58 */         stack.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (stack != null)
/*    */         try {
/*    */           stack.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/* 59 */     return result;
/*    */   }
/*    */   
/*    */   protected long vkQueryPool() {
/* 63 */     return this.vkQueryPool;
/*    */   }
/*    */   
/*    */   public void close() {
/* 68 */     this.device.createCommandEncoder().queueForDestroy(this);
/*    */   }
/*    */   
/*    */   public void destroy() {
/* 73 */     VK12.vkDestroyQueryPool(this.device.vkDevice(), this.vkQueryPool, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanQueryPool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */