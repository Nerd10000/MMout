/*    */ package com.mojang.blaze3d.vulkan;
/*    */ 
/*    */ final class VulkanAllocation extends Record {
/*    */   private final long vkBuffer;
/*    */   
/*    */   private final long vmaAllocation;
/*    */   
/*    */   private final long hostPtr;
/*    */   
/*    */   private final long size;
/*    */   
/*    */   private VulkanAllocation(long vkBuffer, long vmaAllocation, long hostPtr, long size) {
/* 48 */     this.vkBuffer = vkBuffer;
/* 48 */     this.vmaAllocation = vmaAllocation;
/* 48 */     this.hostPtr = hostPtr;
/* 48 */     this.size = size;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #48	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanTransientMemory$VulkanAllocation;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long vkBuffer() {
/* 48 */     return this.vkBuffer;
/*    */   }
/*    */   
/*    */   public long vmaAllocation() {
/* 48 */     return this.vmaAllocation;
/*    */   }
/*    */   
/*    */   public long hostPtr() {
/* 48 */     return this.hostPtr;
/*    */   }
/*    */   
/*    */   public long size() {
/* 48 */     return this.size;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanTransientMemory$VulkanAllocation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */