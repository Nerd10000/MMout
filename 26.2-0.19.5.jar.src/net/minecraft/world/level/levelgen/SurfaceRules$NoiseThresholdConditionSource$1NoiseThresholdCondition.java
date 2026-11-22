/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.function.DoubleSupplier;
/*     */ 
/*     */ class NoiseThresholdCondition implements SurfaceRules.Condition {
/*     */   public boolean test() {
/* 732 */     double value = noise.getAsDouble();
/* 733 */     return (value >= SurfaceRules.NoiseThresholdConditionSource.this.minThreshold && value <= SurfaceRules.NoiseThresholdConditionSource.this.maxThreshold);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceRules$NoiseThresholdConditionSource$1NoiseThresholdCondition.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */