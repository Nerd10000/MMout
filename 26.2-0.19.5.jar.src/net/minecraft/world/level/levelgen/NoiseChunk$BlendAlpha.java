/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ 
/*     */ class BlendAlpha implements NoiseChunk.NoiseChunkDensityFunction {
/*     */   public DensityFunction wrapped() {
/* 773 */     return DensityFunctions.BlendAlpha.INSTANCE;
/*     */   }
/*     */   
/*     */   public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 778 */     return visitor.apply(wrapped());
/*     */   }
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 783 */     return NoiseChunk.this.getOrComputeBlendingOutput(context.blockX(), context.blockZ()).alpha();
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 789 */     contextProvider.fillAllDirectly(output, this);
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 794 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 799 */     return 1.0D;
/*     */   }
/*     */   
/*     */   public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 804 */     return DensityFunctions.BlendAlpha.CODEC;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk$BlendAlpha.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */