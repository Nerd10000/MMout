/*    */ package com.mojang.blaze3d.pipeline;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import com.mojang.blaze3d.shaders.UniformType;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Builder {
/* 67 */   private final List<String> samplers = new ArrayList<>();
/*    */   
/* 68 */   private final List<BindGroupLayout.UniformDescription> uniforms = new ArrayList<>();
/*    */   
/*    */   public Builder withSampler(String sampler) {
/* 74 */     this.samplers.add(sampler);
/* 75 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withUniform(String name, UniformType type) {
/* 79 */     if (type == UniformType.TEXEL_BUFFER)
/* 80 */       throw new IllegalArgumentException("Cannot use texel buffer without specifying texture format"); 
/* 82 */     this.uniforms.add(new BindGroupLayout.UniformDescription(name, type));
/* 83 */     return this;
/*    */   }
/*    */   
/*    */   public Builder withUniform(String name, UniformType type, GpuFormat format) {
/* 87 */     if (type != UniformType.TEXEL_BUFFER)
/* 88 */       throw new IllegalArgumentException("Only texel buffer can specify texture format"); 
/* 90 */     this.uniforms.add(new BindGroupLayout.UniformDescription(name, format));
/* 91 */     return this;
/*    */   }
/*    */   
/*    */   public BindGroupLayout build() {
/* 95 */     return new BindGroupLayout(List.copyOf(this.samplers), List.copyOf(this.uniforms));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\pipeline\BindGroupLayout$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */