/*     */ package com.mojang.blaze3d;
/*     */ 
/*     */ public enum GpuFormat {
/*   4 */   R8_UNORM(ComponentType.UNORM_8, 1),
/*   5 */   R8_SNORM(ComponentType.SNORM_8, 1),
/*   6 */   RG8_UNORM(ComponentType.UNORM_8, 2),
/*   7 */   RG8_SNORM(ComponentType.SNORM_8, 2),
/*   8 */   RGB8_UNORM(ComponentType.UNORM_8, 3),
/*   9 */   RGB8_SNORM(ComponentType.SNORM_8, 3),
/*  10 */   RGBA8_UNORM(ComponentType.UNORM_8, 4),
/*  11 */   RGBA8_SNORM(ComponentType.SNORM_8, 4),
/*  13 */   R16_UNORM(ComponentType.UNORM_16, 1),
/*  14 */   R16_SNORM(ComponentType.SNORM_16, 1),
/*  15 */   RG16_UNORM(ComponentType.UNORM_16, 2),
/*  16 */   RG16_SNORM(ComponentType.SNORM_16, 2),
/*  17 */   RGB16_UNORM(ComponentType.UNORM_16, 3),
/*  18 */   RGB16_SNORM(ComponentType.SNORM_16, 3),
/*  19 */   RGBA16_UNORM(ComponentType.UNORM_16, 4),
/*  20 */   RGBA16_SNORM(ComponentType.SNORM_16, 4),
/*  22 */   R8_UINT(ComponentType.UINT_8, 1),
/*  23 */   R8_SINT(ComponentType.SINT_8, 1),
/*  24 */   RG8_UINT(ComponentType.UINT_8, 2),
/*  25 */   RG8_SINT(ComponentType.SINT_8, 2),
/*  26 */   RGB8_UINT(ComponentType.UINT_8, 3),
/*  27 */   RGB8_SINT(ComponentType.SINT_8, 3),
/*  28 */   RGBA8_UINT(ComponentType.UINT_8, 4),
/*  29 */   RGBA8_SINT(ComponentType.SINT_8, 4),
/*  31 */   R16_UINT(ComponentType.UINT_16, 1),
/*  32 */   R16_SINT(ComponentType.SINT_16, 1),
/*  33 */   RG16_UINT(ComponentType.UINT_16, 2),
/*  34 */   RG16_SINT(ComponentType.SINT_16, 2),
/*  35 */   RGB16_UINT(ComponentType.UINT_16, 3),
/*  36 */   RGB16_SINT(ComponentType.SINT_16, 3),
/*  37 */   RGBA16_UINT(ComponentType.UINT_16, 4),
/*  38 */   RGBA16_SINT(ComponentType.SINT_16, 4),
/*  40 */   R32_UINT(ComponentType.UINT_32, 1),
/*  41 */   R32_SINT(ComponentType.SINT_32, 1),
/*  42 */   RG32_UINT(ComponentType.UINT_32, 2),
/*  43 */   RG32_SINT(ComponentType.SINT_32, 2),
/*  44 */   RGB32_UINT(ComponentType.UINT_32, 3),
/*  45 */   RGB32_SINT(ComponentType.SINT_32, 3),
/*  46 */   RGBA32_UINT(ComponentType.UINT_32, 4),
/*  47 */   RGBA32_SINT(ComponentType.SINT_32, 4),
/*  49 */   R16_FLOAT(ComponentType.FLOAT_16, 1),
/*  50 */   RG16_FLOAT(ComponentType.FLOAT_16, 2),
/*  51 */   RGB16_FLOAT(ComponentType.FLOAT_16, 3),
/*  52 */   RGBA16_FLOAT(ComponentType.FLOAT_16, 4),
/*  54 */   R32_FLOAT(ComponentType.FLOAT_32, 1),
/*  55 */   RG32_FLOAT(ComponentType.FLOAT_32, 2),
/*  56 */   RGB32_FLOAT(ComponentType.FLOAT_32, 3),
/*  57 */   RGBA32_FLOAT(ComponentType.FLOAT_32, 4),
/*  59 */   RGB10A2_UNORM(ComponentType.OPAQUE_32, 1),
/*  60 */   RGB10A2_UINT(ComponentType.OPAQUE_32, 1),
/*  61 */   RG11B10_FLOAT(ComponentType.OPAQUE_32, 1),
/*  63 */   D32_FLOAT(ComponentType.OPAQUE_32, 1),
/*  64 */   D32_FLOAT_S8_UINT(ComponentType.OPAQUE_64, 1),
/*  65 */   D24_UNORM_S8_UINT(ComponentType.OPAQUE_32, 1),
/*  66 */   D16_UNORM(ComponentType.OPAQUE_16, 1),
/*  67 */   S8_UINT(ComponentType.OPAQUE_8, 1);
/*     */   
/*     */   private final ComponentType componentType;
/*     */   
/*     */   private final int componentCount;
/*     */   
/*     */   GpuFormat(ComponentType componentType, int componentCount) {
/*  74 */     this.componentType = componentType;
/*  75 */     this.componentCount = componentCount;
/*     */   }
/*     */   
/*     */   public int blockSize() {
/*  83 */     return this.componentType.byteSize() * this.componentCount;
/*     */   }
/*     */   
/*     */   public int byteAlignment() {
/*  91 */     return this.componentType.byteSize();
/*     */   }
/*     */   
/*     */   public ComponentType componentType() {
/*  95 */     return this.componentType;
/*     */   }
/*     */   
/*     */   public int componentCount() {
/*  99 */     return this.componentCount;
/*     */   }
/*     */   
/*     */   public boolean hasColorAspect() {
/* 106 */     return (!hasDepthAspect() && !hasStencilAspect());
/*     */   }
/*     */   
/*     */   public boolean hasDepthAspect() {
/* 113 */     return (this == D32_FLOAT || this == D32_FLOAT_S8_UINT || this == D24_UNORM_S8_UINT || this == D16_UNORM);
/*     */   }
/*     */   
/*     */   public boolean hasStencilAspect() {
/* 120 */     return (this == S8_UINT || this == D32_FLOAT_S8_UINT || this == D24_UNORM_S8_UINT);
/*     */   }
/*     */   
/*     */   public enum ComponentType {
/* 124 */     UNORM_8(1),
/* 125 */     SNORM_8(1),
/* 126 */     UINT_8(1),
/* 127 */     SINT_8(1),
/* 129 */     UNORM_16(2),
/* 130 */     SNORM_16(2),
/* 131 */     UINT_16(2),
/* 132 */     SINT_16(2),
/* 133 */     FLOAT_16(2),
/* 135 */     UINT_32(4),
/* 136 */     SINT_32(4),
/* 137 */     FLOAT_32(4),
/* 139 */     OPAQUE_8(1),
/* 140 */     OPAQUE_16(2),
/* 141 */     OPAQUE_32(4),
/* 142 */     OPAQUE_64(8);
/*     */     
/*     */     private final int byteSize;
/*     */     
/*     */     ComponentType(int byteSize) {
/* 148 */       this.byteSize = byteSize;
/*     */     }
/*     */     
/*     */     public int byteSize() {
/* 152 */       return this.byteSize;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\GpuFormat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */