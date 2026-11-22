/*     */ package com.mojang.blaze3d.vulkan.glsl;
/*     */ 
/*     */ import com.mojang.blaze3d.vulkan.VulkanBindGroupLayout;
/*     */ 
/*     */ public final class CompiledModules extends Record {
/*     */   private final long vertex;
/*     */   
/*     */   private final long fragment;
/*     */   
/*     */   private final VulkanBindGroupLayout layout;
/*     */   
/*     */   public CompiledModules(long vertex, long fragment, VulkanBindGroupLayout layout) {
/* 142 */     this.vertex = vertex;
/* 142 */     this.fragment = fragment;
/* 142 */     this.layout = layout;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #142	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #142	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #142	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/vulkan/glsl/GlslCompiler$CompiledModules;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public long vertex() {
/* 142 */     return this.vertex;
/*     */   }
/*     */   
/*     */   public long fragment() {
/* 142 */     return this.fragment;
/*     */   }
/*     */   
/*     */   public VulkanBindGroupLayout layout() {
/* 142 */     return this.layout;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\glsl\GlslCompiler$CompiledModules.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */