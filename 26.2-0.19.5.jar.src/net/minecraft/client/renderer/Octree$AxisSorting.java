/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ enum AxisSorting {
/* 235 */   XYZ(4, 2, 1),
/* 236 */   XZY(4, 1, 2),
/* 237 */   YXZ(2, 4, 1),
/* 238 */   YZX(1, 4, 2),
/* 239 */   ZXY(2, 1, 4),
/* 240 */   ZYX(1, 2, 4);
/*     */   
/*     */   private final int xShift;
/*     */   
/*     */   private final int yShift;
/*     */   
/*     */   private final int zShift;
/*     */   
/*     */   AxisSorting(int xShift, int yShift, int zShift) {
/* 247 */     this.xShift = xShift;
/* 248 */     this.yShift = yShift;
/* 249 */     this.zShift = zShift;
/*     */   }
/*     */   
/*     */   public static AxisSorting getAxisSorting(int absXDiff, int absYDiff, int absZDiff) {
/* 253 */     if (absXDiff > absYDiff && absXDiff > absZDiff) {
/* 254 */       if (absYDiff > absZDiff)
/* 255 */         return XYZ; 
/* 257 */       return XZY;
/*     */     } 
/* 259 */     if (absYDiff > absXDiff && absYDiff > absZDiff) {
/* 260 */       if (absXDiff > absZDiff)
/* 261 */         return YXZ; 
/* 263 */       return YZX;
/*     */     } 
/* 266 */     if (absXDiff > absYDiff)
/* 267 */       return ZXY; 
/* 269 */     return ZYX;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\Octree$AxisSorting.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */