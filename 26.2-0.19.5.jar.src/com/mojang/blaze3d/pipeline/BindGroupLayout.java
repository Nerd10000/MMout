/*     */ package com.mojang.blaze3d.pipeline;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.shaders.UniformType;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class BindGroupLayout {
/*     */   private final List<String> samplers;
/*     */   
/*     */   private final List<UniformDescription> uniforms;
/*     */   
/*     */   private BindGroupLayout(List<String> samplers, List<UniformDescription> uniforms) {
/*  17 */     this.samplers = samplers;
/*  18 */     this.uniforms = uniforms;
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/*  22 */     return new Builder();
/*     */   }
/*     */   
/*     */   public List<String> getSamplers() {
/*  26 */     return this.samplers;
/*     */   }
/*     */   
/*     */   public List<UniformDescription> getUniforms() {
/*  30 */     return this.uniforms;
/*     */   }
/*     */   
/*     */   public static List<String> flattenSamplers(List<BindGroupLayout> bindGroupLayouts) {
/*  34 */     List<String> flattened = new ArrayList<>();
/*  35 */     for (BindGroupLayout bindGroupLayout : bindGroupLayouts)
/*  36 */       flattened.addAll(bindGroupLayout.getSamplers()); 
/*  38 */     return flattened;
/*     */   }
/*     */   
/*     */   public static List<UniformDescription> flattenUniforms(List<BindGroupLayout> bindGroupLayouts) {
/*  42 */     List<UniformDescription> flattened = new ArrayList<>();
/*  43 */     for (BindGroupLayout bindGroupLayout : bindGroupLayouts)
/*  44 */       flattened.addAll(bindGroupLayout.getUniforms()); 
/*  46 */     return flattened;
/*     */   }
/*     */   
/*     */   public static void ensureCompatible(List<BindGroupLayout> bindGroupLayouts) {
/*  50 */     Set<String> names = new HashSet<>();
/*  51 */     for (int layoutIndex = 0; layoutIndex < bindGroupLayouts.size(); layoutIndex++) {
/*  52 */       BindGroupLayout bindGroupLayout = bindGroupLayouts.get(layoutIndex);
/*  53 */       for (UniformDescription uniform : bindGroupLayout.getUniforms()) {
/*  54 */         if (!names.add(uniform.name()))
/*  55 */           throw new IllegalArgumentException("Duplicate bind name '" + uniform.name() + "' in bind group layout " + layoutIndex); 
/*     */       } 
/*  58 */       for (String sampler : bindGroupLayout.getSamplers()) {
/*  59 */         if (!names.add(sampler))
/*  60 */           throw new IllegalArgumentException("Duplicate bind name '" + sampler + "' in bind group layout " + layoutIndex); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class Builder {
/*  67 */     private final List<String> samplers = new ArrayList<>();
/*     */     
/*  68 */     private final List<BindGroupLayout.UniformDescription> uniforms = new ArrayList<>();
/*     */     
/*     */     public Builder withSampler(String sampler) {
/*  74 */       this.samplers.add(sampler);
/*  75 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withUniform(String name, UniformType type) {
/*  79 */       if (type == UniformType.TEXEL_BUFFER)
/*  80 */         throw new IllegalArgumentException("Cannot use texel buffer without specifying texture format"); 
/*  82 */       this.uniforms.add(new BindGroupLayout.UniformDescription(name, type));
/*  83 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withUniform(String name, UniformType type, GpuFormat format) {
/*  87 */       if (type != UniformType.TEXEL_BUFFER)
/*  88 */         throw new IllegalArgumentException("Only texel buffer can specify texture format"); 
/*  90 */       this.uniforms.add(new BindGroupLayout.UniformDescription(name, format));
/*  91 */       return this;
/*     */     }
/*     */     
/*     */     public BindGroupLayout build() {
/*  95 */       return new BindGroupLayout(List.copyOf(this.samplers), List.copyOf(this.uniforms));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class UniformDescription extends Record {
/*     */     private final String name;
/*     */     
/*     */     private final UniformType type;
/*     */     
/*     */     private final GpuFormat gpuFormat;
/*     */     
/*     */     public UniformDescription(String name, UniformType type, GpuFormat gpuFormat) {
/*  99 */       this.name = name;
/*  99 */       this.type = type;
/*  99 */       this.gpuFormat = gpuFormat;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #99	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #99	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #99	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/BindGroupLayout$UniformDescription;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String name() {
/*  99 */       return this.name;
/*     */     }
/*     */     
/*     */     public UniformType type() {
/*  99 */       return this.type;
/*     */     }
/*     */     
/*     */     public GpuFormat gpuFormat() {
/*  99 */       return this.gpuFormat;
/*     */     }
/*     */     
/*     */     public UniformDescription(String name, UniformType type) {
/* 101 */       this(name, type, null);
/* 102 */       if (type == UniformType.TEXEL_BUFFER)
/* 103 */         throw new IllegalArgumentException("Texel buffer needs a texture format"); 
/*     */     }
/*     */     
/*     */     public UniformDescription(String name, GpuFormat gpuFormat) {
/* 108 */       this(name, UniformType.TEXEL_BUFFER, gpuFormat);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\BindGroupLayout.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */