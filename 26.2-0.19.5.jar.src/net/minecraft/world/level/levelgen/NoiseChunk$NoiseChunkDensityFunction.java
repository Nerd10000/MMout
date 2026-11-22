/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ interface NoiseChunkDensityFunction extends DensityFunction {
/*     */   DensityFunction wrapped();
/*     */   
/*     */   default double minValue() {
/* 414 */     return wrapped().minValue();
/*     */   }
/*     */   
/*     */   default double maxValue() {
/* 419 */     return wrapped().maxValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$NoiseChunkDensityFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */