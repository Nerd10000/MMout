/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ class Cache2D implements NoiseChunk.NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */   private final DensityFunction function;
/*     */   
/* 722 */   private long lastPos2D = ChunkPos.INVALID_CHUNK_POS;
/*     */   
/*     */   private double lastValue;
/*     */   
/*     */   private Cache2D(DensityFunction function) {
/* 726 */     this.function = function;
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 731 */     int blockX = context.blockX();
/* 732 */     int blockZ = context.blockZ();
/* 733 */     long pos2D = ChunkPos.pack(blockX, blockZ);
/* 734 */     if (this.lastPos2D == pos2D)
/* 735 */       return this.lastValue; 
/* 737 */     this.lastPos2D = pos2D;
/* 738 */     double value = this.function.compute(context);
/* 739 */     this.lastValue = value;
/* 740 */     return value;
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 745 */     this.function.fillArray(output, contextProvider);
/*     */   }
/*     */   
/*     */   public DensityFunction wrapped() {
/* 750 */     return this.function;
/*     */   }
/*     */   
/*     */   public DensityFunctions.Marker.Type type() {
/* 755 */     return DensityFunctions.Marker.Type.Cache2D;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$Cache2D.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */