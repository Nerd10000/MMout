/*    */ package com.mojang.blaze3d.vulkan.init;
/*    */ 
/*    */ import org.lwjgl.system.MemoryStack;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ import org.lwjgl.vulkan.VkPhysicalDeviceFeatures2;
/*    */ 
/*    */ public final class VulkanFeature extends Record {
/*    */   private final VulkanPNextStruct struct;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final long offset;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/init/VulkanFeature;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/init/VulkanFeature;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/init/VulkanFeature;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/init/VulkanFeature;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/init/VulkanFeature;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/init/VulkanFeature;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public VulkanPNextStruct struct() {
/*  8 */     return this.struct;
/*    */   }
/*    */   
/*    */   public String name() {
/*  8 */     return this.name;
/*    */   }
/*    */   
/*    */   public long offset() {
/*  8 */     return this.offset;
/*    */   }
/*    */   
/*    */   public VulkanFeature(VulkanPNextStruct struct, String name, long offset) {
/* 11 */     this.name = name;
/* 12 */     this.struct = struct;
/* 14 */     if (struct.sType() == 1000059000) {
/* 15 */       this.offset = offset + VkPhysicalDeviceFeatures2.FEATURES;
/*    */     } else {
/* 17 */       this.offset = offset;
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean get(VkPhysicalDeviceFeatures2 features2) {
/* 22 */     return get(features2.address());
/*    */   }
/*    */   
/*    */   public boolean get(long pNextChain) {
/* 26 */     long structAddr = this.struct.findStructInPNextChain(pNextChain);
/* 27 */     if (structAddr == 0L)
/* 29 */       return false; 
/* 33 */     return (MemoryUtil.memGetInt(structAddr + this.offset) != 0);
/*    */   }
/*    */   
/*    */   public boolean set(VkPhysicalDeviceFeatures2 features2, boolean value) {
/* 37 */     return set(features2.address(), value);
/*    */   }
/*    */   
/*    */   private boolean set(long pNextChain, boolean value) {
/* 41 */     long structAddr = this.struct.findStructInPNextChain(pNextChain);
/* 42 */     if (structAddr == 0L)
/* 44 */       return false; 
/* 47 */     MemoryUtil.memPutInt(structAddr + this.offset, value ? 1 : 0);
/* 48 */     return true;
/*    */   }
/*    */   
/*    */   public void set(VkPhysicalDeviceFeatures2 features2, boolean value, MemoryStack stack) {
/* 52 */     set(features2.address(), value, stack);
/*    */   }
/*    */   
/*    */   public void set(long pNextChain, boolean value, MemoryStack stack) {
/* 56 */     long structAddr = this.struct.findOrCreateStructInPNextChain(pNextChain, stack);
/* 58 */     MemoryUtil.memPutInt(structAddr + this.offset, value ? 1 : 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\init\VulkanFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */