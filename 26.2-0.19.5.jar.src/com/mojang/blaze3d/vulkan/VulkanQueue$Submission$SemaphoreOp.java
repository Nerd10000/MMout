/*    */ package com.mojang.blaze3d.vulkan;
/*    */ 
/*    */ final class SemaphoreOp extends Record {
/*    */   private final long vkSemaphore;
/*    */   
/*    */   private final long value;
/*    */   
/*    */   private final long stageMask;
/*    */   
/*    */   private SemaphoreOp(long vkSemaphore, long value, long stageMask) {
/* 38 */     this.vkSemaphore = vkSemaphore;
/* 38 */     this.value = value;
/* 38 */     this.stageMask = stageMask;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #38	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public long vkSemaphore() {
/* 38 */     return this.vkSemaphore;
/*    */   }
/*    */   
/*    */   public long value() {
/* 38 */     return this.value;
/*    */   }
/*    */   
/*    */   public long stageMask() {
/* 38 */     return this.stageMask;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanQueue$Submission$SemaphoreOp.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */