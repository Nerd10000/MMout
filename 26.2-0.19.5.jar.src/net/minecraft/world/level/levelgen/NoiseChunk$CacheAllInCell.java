/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ class CacheAllInCell implements NoiseChunk.NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */   private final DensityFunction noiseFiller;
/*     */   
/*     */   private final double[] values;
/*     */   
/*     */   private CacheAllInCell(DensityFunction noiseFiller) {
/* 483 */     this.noiseFiller = noiseFiller;
/* 484 */     this.values = new double[paramNoiseChunk.cellWidth * paramNoiseChunk.cellWidth * paramNoiseChunk.cellHeight];
/* 485 */     paramNoiseChunk.cellCaches.add(this);
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 491 */     if (context != NoiseChunk.this)
/* 492 */       return this.noiseFiller.compute(context); 
/* 494 */     if (!NoiseChunk.this.interpolating)
/* 495 */       throw new IllegalStateException("Trying to sample interpolator outside the interpolation loop"); 
/* 497 */     int x = NoiseChunk.this.inCellX;
/* 498 */     int y = NoiseChunk.this.inCellY;
/* 499 */     int z = NoiseChunk.this.inCellZ;
/* 501 */     if (x >= 0 && y >= 0 && z >= 0 && x < NoiseChunk.this.cellWidth && y < NoiseChunk.this.cellHeight && z < NoiseChunk.this.cellWidth)
/* 502 */       return this.values[((NoiseChunk.this.cellHeight - 1 - y) * NoiseChunk.this.cellWidth + x) * NoiseChunk.this.cellWidth + z]; 
/* 504 */     return this.noiseFiller.compute(context);
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 510 */     contextProvider.fillAllDirectly(output, this);
/*     */   }
/*     */   
/*     */   public DensityFunction wrapped() {
/* 515 */     return this.noiseFiller;
/*     */   }
/*     */   
/*     */   public DensityFunctions.Marker.Type type() {
/* 520 */     return DensityFunctions.Marker.Type.CacheAllInCell;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$CacheAllInCell.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */