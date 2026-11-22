/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ class BlendDensity implements NoiseChunk.NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */   private final DensityFunction input;
/*     */   
/*     */   private BlendDensity(DensityFunction input) {
/* 852 */     this.input = input;
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 857 */     return NoiseChunk.this.blender.blendDensity(context, this.input.compute(context));
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 862 */     this.input.fillArray(output, contextProvider);
/* 863 */     for (int i = 0; i < output.length; i++) {
/* 864 */       DensityFunction.FunctionContext context = contextProvider.forIndex(i);
/* 865 */       output[i] = NoiseChunk.this.blender.blendDensity(context, output[i]);
/*     */     } 
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 872 */     return Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 878 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public DensityFunction wrapped() {
/* 883 */     return this.input;
/*     */   }
/*     */   
/*     */   public DensityFunctions.Marker.Type type() {
/* 888 */     return DensityFunctions.Marker.Type.BlendDensity;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$BlendDensity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */