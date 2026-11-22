/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ interface PureTransformer extends DensityFunction {
/*     */   DensityFunction input();
/*     */   
/*     */   default double compute(DensityFunction.FunctionContext context) {
/* 280 */     return transform(input().compute(context));
/*     */   }
/*     */   
/*     */   default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 285 */     input().fillArray(output, contextProvider);
/* 286 */     for (int i = 0; i < output.length; i++)
/* 287 */       output[i] = transform(output[i]); 
/*     */   }
/*     */   
/*     */   double transform(double paramDouble);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$PureTransformer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */