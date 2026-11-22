/*    */ package com.mojang.blaze3d.vulkan;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
/*    */ import java.util.List;
/*    */ import org.lwjgl.vulkan.VkCommandBuffer;
/*    */ 
/*    */ final class SubmitStage extends Record {
/*    */   private final List<VulkanQueue.Submission.SemaphoreOp> waits;
/*    */   
/*    */   private final List<VkCommandBuffer> commandBuffers;
/*    */   
/*    */   private final List<VulkanQueue.Submission.SemaphoreOp> signals;
/*    */   
/*    */   public List<VulkanQueue.Submission.SemaphoreOp> signals() {
/* 42 */     return this.signals;
/*    */   }
/*    */   
/*    */   public List<VkCommandBuffer> commandBuffers() {
/* 42 */     return this.commandBuffers;
/*    */   }
/*    */   
/*    */   public List<VulkanQueue.Submission.SemaphoreOp> waits() {
/* 42 */     return this.waits;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #42	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;
/*    */   }
/*    */   
/*    */   private SubmitStage(List<VulkanQueue.Submission.SemaphoreOp> waits, List<VkCommandBuffer> commandBuffers, List<VulkanQueue.Submission.SemaphoreOp> signals) {
/* 42 */     this.waits = waits;
/* 42 */     this.commandBuffers = commandBuffers;
/* 42 */     this.signals = signals;
/*    */   }
/*    */   
/*    */   public SubmitStage() {
/* 45 */     this((List<VulkanQueue.Submission.SemaphoreOp>)new ReferenceArrayList(), (List<VkCommandBuffer>)new ReferenceArrayList(), (List<VulkanQueue.Submission.SemaphoreOp>)new ReferenceArrayList());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanQueue$Submission$SubmitStage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */