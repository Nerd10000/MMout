/*     */ package net.minecraft.util.profiling.metrics;
/*     */ 
/*     */ public class ValueIncreasedByPercentage implements MetricSampler.ThresholdTest {
/*     */   private final float percentageIncreaseThreshold;
/*     */   
/* 174 */   private double previousValue = Double.MIN_VALUE;
/*     */   
/*     */   public ValueIncreasedByPercentage(float percentageIncreaseThreshold) {
/* 177 */     this.percentageIncreaseThreshold = percentageIncreaseThreshold;
/*     */   }
/*     */   
/*     */   public boolean test(double value) {
/*     */     boolean result;
/* 184 */     if (this.previousValue == Double.MIN_VALUE || value <= this.previousValue) {
/* 185 */       result = false;
/*     */     } else {
/* 187 */       result = ((value - this.previousValue) / this.previousValue >= this.percentageIncreaseThreshold);
/*     */     } 
/* 190 */     this.previousValue = value;
/* 191 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\MetricSampler$ValueIncreasedByPercentage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */