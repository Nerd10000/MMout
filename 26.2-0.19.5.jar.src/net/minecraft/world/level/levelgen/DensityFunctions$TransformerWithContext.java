/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ interface TransformerWithContext extends DensityFunction {
/*     */   DensityFunction input();
/*     */   
/*     */   default double compute(DensityFunction.FunctionContext context) {
/* 261 */     return transform(context, input().compute(context));
/*     */   }
/*     */   
/*     */   default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 266 */     input().fillArray(output, contextProvider);
/* 267 */     for (int i = 0; i < output.length; i++)
/* 268 */       output[i] = transform(contextProvider.forIndex(i), output[i]); 
/*     */   }
/*     */   
/*     */   double transform(DensityFunction.FunctionContext paramFunctionContext, double paramDouble);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$TransformerWithContext.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */