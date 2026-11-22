/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.core.QuartPos;
/*     */ 
/*     */ class FlatCache implements NoiseChunk.NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */   private final DensityFunction noiseFiller;
/*     */   
/*     */   private final double[] values;
/*     */   
/*     */   private final int sizeXZ;
/*     */   
/*     */   private FlatCache(DensityFunction noiseFiller, boolean fill) {
/* 429 */     this.noiseFiller = noiseFiller;
/* 430 */     this.sizeXZ = paramNoiseChunk.noiseSizeXZ + 1;
/* 431 */     this.values = new double[this.sizeXZ * this.sizeXZ];
/* 432 */     if (fill)
/* 433 */       for (int x = 0; x <= paramNoiseChunk.noiseSizeXZ; x++) {
/* 434 */         int quartX = paramNoiseChunk.firstNoiseX + x;
/* 435 */         int blockX = QuartPos.toBlock(quartX);
/* 437 */         for (int z = 0; z <= paramNoiseChunk.noiseSizeXZ; z++) {
/* 438 */           int quartZ = paramNoiseChunk.firstNoiseZ + z;
/* 439 */           int blockZ = QuartPos.toBlock(quartZ);
/* 441 */           this.values[x + z * this.sizeXZ] = noiseFiller.compute(new DensityFunction.SinglePointContext(blockX, 0, blockZ));
/*     */         } 
/*     */       }  
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 449 */     int quartX = QuartPos.fromBlock(context.blockX());
/* 450 */     int quartZ = QuartPos.fromBlock(context.blockZ());
/* 452 */     int x = quartX - NoiseChunk.this.firstNoiseX;
/* 453 */     int z = quartZ - NoiseChunk.this.firstNoiseZ;
/* 455 */     if (x >= 0 && z >= 0 && x < this.sizeXZ && z < this.sizeXZ)
/* 456 */       return this.values[x + z * this.sizeXZ]; 
/* 458 */     return this.noiseFiller.compute(context);
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 464 */     contextProvider.fillAllDirectly(output, this);
/*     */   }
/*     */   
/*     */   public DensityFunction wrapped() {
/* 469 */     return this.noiseFiller;
/*     */   }
/*     */   
/*     */   public DensityFunctions.Marker.Type type() {
/* 474 */     return DensityFunctions.Marker.Type.FlatCache;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$FlatCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */