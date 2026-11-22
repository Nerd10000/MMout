/*    */ package com.mojang.blaze3d.vulkan.init;
/*    */ 
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ import org.lwjgl.system.Pointer;
/*    */ import org.lwjgl.vulkan.VkDeviceCreateInfo;
/*    */ import org.lwjgl.vulkan.VkPhysicalDeviceFeatures2;
/*    */ import org.lwjgl.vulkan.VkPhysicalDeviceProperties2;
/*    */ 
/*    */ public final class VulkanPNextStruct extends Record {
/*    */   private final int sType;
/*    */   
/*    */   private final int structSize;
/*    */   
/*    */   public VulkanPNextStruct(int sType, int structSize) {
/* 10 */     this.sType = sType;
/* 10 */     this.structSize = structSize;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/init/VulkanPNextStruct;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/init/VulkanPNextStruct;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/init/VulkanPNextStruct;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/init/VulkanPNextStruct;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/init/VulkanPNextStruct;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/init/VulkanPNextStruct;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int sType() {
/* 10 */     return this.sType;
/*    */   }
/*    */   
/*    */   public int structSize() {
/* 10 */     return this.structSize;
/*    */   }
/*    */   
/*    */   public long findOrCreateStructInPNextChain(VkPhysicalDeviceProperties2 properties2, MemoryStack stack) {
/* 13 */     return findOrCreateStructInPNextChain(properties2.address(), stack);
/*    */   }
/*    */   
/*    */   public long findOrCreateStructInPNextChain(VkPhysicalDeviceFeatures2 features2, MemoryStack stack) {
/* 17 */     return findOrCreateStructInPNextChain(features2.address(), stack);
/*    */   }
/*    */   
/*    */   public long findOrCreateStructInPNextChain(long pNextChain, MemoryStack stack) {
/* 21 */     long foundStruct = findStructInPNextChain(pNextChain, this.sType);
/* 22 */     if (foundStruct != 0L)
/* 23 */       return foundStruct; 
/* 25 */     long newStruct = stack.ncalloc(Pointer.POINTER_SIZE, 1, this.structSize);
/* 26 */     VkPhysicalDeviceProperties2.nsType(newStruct, this.sType);
/* 28 */     VkPhysicalDeviceProperties2.npNext(newStruct, VkPhysicalDeviceProperties2.npNext(pNextChain));
/* 29 */     VkPhysicalDeviceProperties2.npNext(pNextChain, newStruct);
/* 30 */     return newStruct;
/*    */   }
/*    */   
/*    */   public long findStructInPNextChain(long pNextChain) {
/* 34 */     return findStructInPNextChain(pNextChain, this.sType);
/*    */   }
/*    */   
/*    */   private static long findStructInPNextChain(long pNextChain, int sType) {
/* 39 */     while (pNextChain != 0L) {
/* 40 */       if (VkPhysicalDeviceProperties2.nsType(pNextChain) == sType)
/* 41 */         return pNextChain; 
/* 43 */       pNextChain = MemoryUtil.memGetAddress(pNextChain + VkDeviceCreateInfo.PNEXT);
/*    */     } 
/* 46 */     return 0L;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\init\VulkanPNextStruct.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */