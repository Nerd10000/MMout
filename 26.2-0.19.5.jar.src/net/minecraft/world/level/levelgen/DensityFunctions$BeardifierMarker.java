/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ enum BeardifierMarker implements DensityFunctions.BeardifierOrMarker {
/* 367 */   INSTANCE;
/*     */   
/*     */   public double compute(DensityFunction.FunctionContext context) {
/* 371 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 376 */     Arrays.fill(output, 0.0D);
/*     */   }
/*     */   
/*     */   public double minValue() {
/* 381 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public double maxValue() {
/* 386 */     return 0.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$BeardifierMarker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */