/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
/*     */ import java.util.List;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.vulkan.KHRSynchronization2;
/*     */ import org.lwjgl.vulkan.VkCommandBuffer;
/*     */ import org.lwjgl.vulkan.VkCommandBufferSubmitInfo;
/*     */ import org.lwjgl.vulkan.VkSemaphoreSubmitInfo;
/*     */ import org.lwjgl.vulkan.VkSubmitInfo2;
/*     */ 
/*     */ public class Submission implements AutoCloseable {
/*     */   private static final class SemaphoreOp extends Record {
/*     */     private final long vkSemaphore;
/*     */     
/*     */     private final long value;
/*     */     
/*     */     private final long stageMask;
/*     */     
/*     */     private SemaphoreOp(long vkSemaphore, long value, long stageMask) {
/*  38 */       this.vkSemaphore = vkSemaphore;
/*  38 */       this.value = value;
/*  38 */       this.stageMask = stageMask;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SemaphoreOp;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public long vkSemaphore() {
/*  38 */       return this.vkSemaphore;
/*     */     }
/*     */     
/*     */     public long value() {
/*  38 */       return this.value;
/*     */     }
/*     */     
/*     */     public long stageMask() {
/*  38 */       return this.stageMask;
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class SubmitStage extends Record {
/*     */     private final List<VulkanQueue.Submission.SemaphoreOp> waits;
/*     */     
/*     */     private final List<VkCommandBuffer> commandBuffers;
/*     */     
/*     */     private final List<VulkanQueue.Submission.SemaphoreOp> signals;
/*     */     
/*     */     public List<VulkanQueue.Submission.SemaphoreOp> signals() {
/*  42 */       return this.signals;
/*     */     }
/*     */     
/*     */     public List<VkCommandBuffer> commandBuffers() {
/*  42 */       return this.commandBuffers;
/*     */     }
/*     */     
/*     */     public List<VulkanQueue.Submission.SemaphoreOp> waits() {
/*  42 */       return this.waits;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #42	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanQueue$Submission$SubmitStage;
/*     */     }
/*     */     
/*     */     private SubmitStage(List<VulkanQueue.Submission.SemaphoreOp> waits, List<VkCommandBuffer> commandBuffers, List<VulkanQueue.Submission.SemaphoreOp> signals) {
/*  42 */       this.waits = waits;
/*  42 */       this.commandBuffers = commandBuffers;
/*  42 */       this.signals = signals;
/*     */     }
/*     */     
/*     */     public SubmitStage() {
/*  45 */       this((List<VulkanQueue.Submission.SemaphoreOp>)new ReferenceArrayList(), (List<VkCommandBuffer>)new ReferenceArrayList(), (List<VulkanQueue.Submission.SemaphoreOp>)new ReferenceArrayList());
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean closed = false;
/*     */   
/*  50 */   private final ReferenceArrayList<SubmitStage> stages = new ReferenceArrayList();
/*     */   
/*     */   private Submission() {
/*  53 */     this.stages.add(new SubmitStage());
/*     */   }
/*     */   
/*     */   public void waitSemaphore(long semaphore, long value, long stageMask) {
/*  57 */     if (this.closed)
/*  58 */       throw new IllegalStateException("Attempt to use closed Submission"); 
/*  60 */     if (!((SubmitStage)this.stages.top()).commandBuffers.isEmpty() || !((SubmitStage)this.stages.top()).signals.isEmpty())
/*  61 */       this.stages.add(new SubmitStage()); 
/*  63 */     ((SubmitStage)this.stages.top()).waits.add(new SemaphoreOp(semaphore, value, stageMask));
/*     */   }
/*     */   
/*     */   public void executeCommands(VkCommandBuffer commandBuffer) {
/*  67 */     if (this.closed)
/*  68 */       throw new IllegalStateException("Attempt to use closed Submission"); 
/*  70 */     if (!((SubmitStage)this.stages.top()).signals.isEmpty())
/*  71 */       this.stages.add(new SubmitStage()); 
/*  73 */     ((SubmitStage)this.stages.top()).commandBuffers.add(commandBuffer);
/*     */   }
/*     */   
/*     */   public void signalSemaphore(long semaphore, long value, long stageMask) {
/*  77 */     if (this.closed)
/*  78 */       throw new IllegalStateException("Attempt to use closed Submission"); 
/*  80 */     ((SubmitStage)this.stages.top()).signals.add(new SemaphoreOp(semaphore, value, stageMask));
/*     */   }
/*     */   
/*     */   public void close() {
/*  87 */     MemoryStack stack = MemoryStack.stackPush();
/*     */     try {
/*  88 */       VkSubmitInfo2.Buffer submits = VkSubmitInfo2.calloc(this.stages.size(), stack);
/*  89 */       for (int i = 0; i < this.stages.size(); i++) {
/*  90 */         SubmitStage stage = (SubmitStage)this.stages.get(i);
/*  91 */         ((VkSubmitInfo2.Buffer)submits.position(i)).sType$Default();
/*  92 */         if (!stage.waits.isEmpty()) {
/*  93 */           VkSemaphoreSubmitInfo.Buffer waits = VkSemaphoreSubmitInfo.calloc(stage.waits.size(), stack);
/*  94 */           submits.pWaitSemaphoreInfos(waits);
/*  95 */           for (int j = 0; j < stage.waits.size(); j++) {
/*  96 */             SemaphoreOp wait = stage.waits.get(j);
/*  97 */             ((VkSemaphoreSubmitInfo.Buffer)waits.position(j)).sType$Default();
/*  98 */             waits.semaphore(wait.vkSemaphore);
/*  99 */             waits.value(wait.value);
/* 100 */             waits.stageMask(wait.stageMask);
/*     */           } 
/*     */         } 
/* 103 */         if (!stage.commandBuffers.isEmpty()) {
/* 104 */           VkCommandBufferSubmitInfo.Buffer buffers = VkCommandBufferSubmitInfo.calloc(stage.commandBuffers.size(), stack);
/* 105 */           submits.pCommandBufferInfos(buffers);
/* 106 */           for (int j = 0; j < stage.commandBuffers.size(); j++) {
/* 107 */             VkCommandBuffer vkCommandBuffer = stage.commandBuffers.get(j);
/* 108 */             ((VkCommandBufferSubmitInfo.Buffer)buffers.position(j)).sType$Default();
/* 109 */             buffers.commandBuffer(vkCommandBuffer);
/*     */           } 
/*     */         } 
/* 112 */         if (!stage.signals.isEmpty()) {
/* 113 */           VkSemaphoreSubmitInfo.Buffer signals = VkSemaphoreSubmitInfo.calloc(stage.signals.size(), stack);
/* 114 */           submits.pSignalSemaphoreInfos(signals);
/* 115 */           for (int j = 0; j < stage.signals.size(); j++) {
/* 116 */             SemaphoreOp signal = stage.signals.get(j);
/* 117 */             ((VkSemaphoreSubmitInfo.Buffer)signals.position(j)).sType$Default();
/* 118 */             signals.semaphore(signal.vkSemaphore);
/* 119 */             signals.value(signal.value);
/* 120 */             signals.stageMask(signal.stageMask);
/*     */           } 
/*     */         } 
/*     */       } 
/* 124 */       submits.position(0);
/* 125 */       KHRSynchronization2.vkQueueSubmit2KHR(VulkanQueue.this.vkQueue, submits, 0L);
/* 126 */       this.closed = true;
/* 127 */       if (stack != null)
/* 127 */         stack.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (stack != null)
/*     */         try {
/*     */           stack.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanQueue$Submission.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */