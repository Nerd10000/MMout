/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ public interface ShiftNoise extends DensityFunction {
/*     */   DensityFunction.NoiseHolder offsetNoise();
/*     */   
/*     */   default double minValue() {
/* 793 */     return -maxValue();
/*     */   }
/*     */   
/*     */   default double maxValue() {
/* 798 */     return offsetNoise().maxValue() * 4.0D;
/*     */   }
/*     */   
/*     */   default double compute(double localX, double localY, double localZ) {
/* 802 */     return offsetNoise().getValue(localX * 0.25D, localY * 0.25D, localZ * 0.25D) * 4.0D;
/*     */   }
/*     */   
/*     */   default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 807 */     contextProvider.fillAllDirectly(output, this);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\DensityFunctions$ShiftNoise.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */