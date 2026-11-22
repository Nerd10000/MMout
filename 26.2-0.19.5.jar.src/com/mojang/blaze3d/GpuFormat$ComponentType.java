/*     */ package com.mojang.blaze3d;
/*     */ 
/*     */ public enum ComponentType {
/* 124 */   UNORM_8(1),
/* 125 */   SNORM_8(1),
/* 126 */   UINT_8(1),
/* 127 */   SINT_8(1),
/* 129 */   UNORM_16(2),
/* 130 */   SNORM_16(2),
/* 131 */   UINT_16(2),
/* 132 */   SINT_16(2),
/* 133 */   FLOAT_16(2),
/* 135 */   UINT_32(4),
/* 136 */   SINT_32(4),
/* 137 */   FLOAT_32(4),
/* 139 */   OPAQUE_8(1),
/* 140 */   OPAQUE_16(2),
/* 141 */   OPAQUE_32(4),
/* 142 */   OPAQUE_64(8);
/*     */   
/*     */   private final int byteSize;
/*     */   
/*     */   ComponentType(int byteSize) {
/* 148 */     this.byteSize = byteSize;
/*     */   }
/*     */   
/*     */   public int byteSize() {
/* 152 */     return this.byteSize;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\GpuFormat$ComponentType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */