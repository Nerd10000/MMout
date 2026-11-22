/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.GpuFormat;
/*    */ import com.mojang.blaze3d.pipeline.BindGroupLayout;
/*    */ import com.mojang.blaze3d.shaders.UniformType;
/*    */ 
/*    */ public class BindGroupLayouts {
/*  8 */   public static final BindGroupLayout DYNAMIC_TRANSFORMS = BindGroupLayout.builder()
/*  9 */     .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
/* 10 */     .build();
/*    */   
/* 12 */   public static final BindGroupLayout PROJECTION = BindGroupLayout.builder()
/* 13 */     .withUniform("Projection", UniformType.UNIFORM_BUFFER)
/* 14 */     .build();
/*    */   
/* 16 */   public static final BindGroupLayout MATRICES_PROJECTION = BindGroupLayout.builder()
/* 17 */     .withUniform("DynamicTransforms", UniformType.UNIFORM_BUFFER)
/* 18 */     .withUniform("Projection", UniformType.UNIFORM_BUFFER)
/* 19 */     .build();
/*    */   
/* 21 */   public static final BindGroupLayout CHUNK_SECTION = BindGroupLayout.builder()
/* 22 */     .withUniform("ChunkSection", UniformType.UNIFORM_BUFFER)
/* 23 */     .build();
/*    */   
/* 25 */   public static final BindGroupLayout FOG = BindGroupLayout.builder()
/* 26 */     .withUniform("Fog", UniformType.UNIFORM_BUFFER)
/* 27 */     .build();
/*    */   
/* 29 */   public static final BindGroupLayout GLOBALS = BindGroupLayout.builder()
/* 30 */     .withUniform("Globals", UniformType.UNIFORM_BUFFER)
/* 31 */     .build();
/*    */   
/* 33 */   public static final BindGroupLayout LIGHTING = BindGroupLayout.builder()
/* 34 */     .withUniform("Lighting", UniformType.UNIFORM_BUFFER)
/* 35 */     .build();
/*    */   
/* 37 */   public static final BindGroupLayout SAMPLER0 = BindGroupLayout.builder()
/* 38 */     .withSampler("Sampler0")
/* 39 */     .build();
/*    */   
/* 41 */   public static final BindGroupLayout SAMPLER1 = BindGroupLayout.builder()
/* 42 */     .withSampler("Sampler1")
/* 43 */     .build();
/*    */   
/* 45 */   public static final BindGroupLayout SAMPLER2 = BindGroupLayout.builder()
/* 46 */     .withSampler("Sampler2")
/* 47 */     .build();
/*    */   
/* 49 */   public static final BindGroupLayout SAMPLER0_SAMPLER2 = BindGroupLayout.builder()
/* 50 */     .withSampler("Sampler0")
/* 51 */     .withSampler("Sampler2")
/* 52 */     .build();
/*    */   
/* 54 */   public static final BindGroupLayout SAMPLER0_SAMPLER1 = BindGroupLayout.builder()
/* 55 */     .withSampler("Sampler0")
/* 56 */     .withSampler("Sampler1")
/* 57 */     .build();
/*    */   
/* 59 */   public static final BindGroupLayout SAMPLER0_SAMPLER1_SAMPLER2 = BindGroupLayout.builder()
/* 60 */     .withSampler("Sampler0")
/* 61 */     .withSampler("Sampler1")
/* 62 */     .withSampler("Sampler2")
/* 63 */     .build();
/*    */   
/* 65 */   public static final BindGroupLayout CLOUD_INFO = BindGroupLayout.builder()
/* 66 */     .withUniform("CloudInfo", UniformType.UNIFORM_BUFFER)
/* 67 */     .withUniform("CloudFaces", UniformType.TEXEL_BUFFER, GpuFormat.R8_SINT)
/* 68 */     .build();
/*    */   
/* 70 */   public static final BindGroupLayout DISSOLVE_MASK_SAMPLER = BindGroupLayout.builder()
/* 71 */     .withSampler("DissolveMaskSampler")
/* 72 */     .build();
/*    */   
/* 74 */   public static final BindGroupLayout IN_SAMPLER = BindGroupLayout.builder()
/* 75 */     .withSampler("InSampler")
/* 76 */     .build();
/*    */   
/* 78 */   public static final BindGroupLayout LIGHTMAP_INFO = BindGroupLayout.builder()
/* 79 */     .withUniform("LightmapInfo", UniformType.UNIFORM_BUFFER)
/* 80 */     .build();
/*    */   
/* 82 */   public static final BindGroupLayout SPRITE_ANIMATION_INFO = BindGroupLayout.builder()
/* 83 */     .withUniform("SpriteAnimationInfo", UniformType.UNIFORM_BUFFER)
/* 84 */     .build();
/*    */   
/* 86 */   public static final BindGroupLayout SPRITE = BindGroupLayout.builder()
/* 87 */     .withSampler("Sprite")
/* 88 */     .build();
/*    */   
/* 90 */   public static final BindGroupLayout CURRENT_SPRITE_NEXT_SPRITE = BindGroupLayout.builder()
/* 91 */     .withSampler("CurrentSprite")
/* 92 */     .withSampler("NextSprite")
/* 93 */     .build();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\BindGroupLayouts.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */