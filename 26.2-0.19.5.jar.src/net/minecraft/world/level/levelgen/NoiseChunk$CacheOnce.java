/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ class CacheOnce implements NoiseChunk.NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */   private final DensityFunction function;
/*     */   
/*     */   private long lastCounter;
/*     */   
/*     */   private long lastArrayCounter;
/*     */   
/*     */   private double lastValue;
/*     */   
/*     */   private double[] lastArray;
/*     */   
/*     */   private CacheOnce(DensityFunction function) {
/* 673 */     this.function = function;
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 678 */     if (context != NoiseChunk.this)
/* 679 */       return this.function.compute(context); 
/* 681 */     if (this.lastArray != null && this.lastArrayCounter == NoiseChunk.this.arrayInterpolationCounter)
/* 682 */       return this.lastArray[NoiseChunk.this.arrayIndex]; 
/* 684 */     if (this.lastCounter == NoiseChunk.this.interpolationCounter)
/* 685 */       return this.lastValue; 
/* 687 */     this.lastCounter = NoiseChunk.this.interpolationCounter;
/* 688 */     double value = this.function.compute(context);
/* 689 */     this.lastValue = value;
/* 690 */     return value;
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 695 */     if (this.lastArray != null && this.lastArrayCounter == NoiseChunk.this.arrayInterpolationCounter) {
/* 696 */       System.arraycopy(this.lastArray, 0, output, 0, output.length);
/*     */       return;
/*     */     } 
/* 699 */     wrapped().fillArray(output, contextProvider);
/* 700 */     if (this.lastArray != null && this.lastArray.length == output.length) {
/* 701 */       System.arraycopy(output, 0, this.lastArray, 0, output.length);
/*     */     } else {
/* 703 */       this.lastArray = (double[])output.clone();
/*     */     } 
/* 705 */     this.lastArrayCounter = NoiseChunk.this.arrayInterpolationCounter;
/*     */   }
/*     */   
/*     */   public DensityFunction wrapped() {
/* 710 */     return this.function;
/*     */   }
/*     */   
/*     */   public DensityFunctions.Marker.Type type() {
/* 715 */     return DensityFunctions.Marker.Type.CacheOnce;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$CacheOnce.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */