/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ class BlendOffset implements NoiseChunk.NoiseChunkDensityFunction {
/*     */   public DensityFunction wrapped() {
/* 811 */     return DensityFunctions.BlendOffset.INSTANCE;
/*     */   }
/*     */   
/*     */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 816 */     return visitor.apply(wrapped());
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 821 */     return NoiseChunk.this.getOrComputeBlendingOutput(context.blockX(), context.blockZ()).blendingOffset();
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 827 */     contextProvider.fillAllDirectly(output, this);
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 833 */     return Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 839 */     return Double.POSITIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 844 */     return DensityFunctions.BlendOffset.CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$BlendOffset.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */