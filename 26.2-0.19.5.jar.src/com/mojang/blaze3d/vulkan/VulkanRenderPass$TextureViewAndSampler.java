/*     */ package com.mojang.blaze3d.vulkan;
/*     */ 
/*     */ public final class TextureViewAndSampler extends Record {
/*     */   private final VulkanGpuTextureView view;
/*     */   
/*     */   private final VulkanGpuSampler sampler;
/*     */   
/*     */   protected TextureViewAndSampler(VulkanGpuTextureView view, VulkanGpuSampler sampler) {
/* 387 */     this.view = view;
/* 387 */     this.sampler = sampler;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #387	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #387	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #387	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/VulkanRenderPass$TextureViewAndSampler;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public VulkanGpuTextureView view() {
/* 387 */     return this.view;
/*     */   }
/*     */   
/*     */   public VulkanGpuSampler sampler() {
/* 387 */     return this.sampler;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\VulkanRenderPass$TextureViewAndSampler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */