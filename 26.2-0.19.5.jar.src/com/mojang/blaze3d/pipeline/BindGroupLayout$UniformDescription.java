/*     */ package com.mojang.blaze3d.pipeline;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.shaders.UniformType;
/*     */ 
/*     */ public final class UniformDescription extends Record {
/*     */   private final String name;
/*     */   
/*     */   private final UniformType type;
/*     */   
/*     */   private final GpuFormat gpuFormat;
/*     */   
/*     */   public UniformDescription(String name, UniformType type, GpuFormat gpuFormat) {
/*  99 */     this.name = name;
/*  99 */     this.type = type;
/*  99 */     this.gpuFormat = gpuFormat;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #99	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #99	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #99	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String name() {
/*  99 */     return this.name;
/*     */   }
/*     */   
/*     */   public UniformType type() {
/*  99 */     return this.type;
/*     */   }
/*     */   
/*     */   public GpuFormat gpuFormat() {
/*  99 */     return this.gpuFormat;
/*     */   }
/*     */   
/*     */   public UniformDescription(String name, UniformType type) {
/* 101 */     this(name, type, null);
/* 102 */     if (type == UniformType.TEXEL_BUFFER)
/* 103 */       throw new IllegalArgumentException("Texel buffer needs a texture format"); 
/*     */   }
/*     */   
/*     */   public UniformDescription(String name, GpuFormat gpuFormat) {
/* 108 */     this(name, UniformType.TEXEL_BUFFER, gpuFormat);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\BindGroupLayout$UniformDescription.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */