/*    */ package net.minecraft.util.profiling.metrics;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ class AggregatedMetricSampler extends MetricSampler {
/*    */   private final List<MetricSampler> delegates;
/*    */   
/*    */   private AggregatedMetricSampler(String name, List<MetricSampler> delegates) {
/* 49 */     super(name, MetricSampler.SamplingPhase.END_TICK, ((MetricSampler)delegates.get(0)).getCategory(), () -> averageValueFromDelegates(delegates), () -> beforeTick(delegates), thresholdTest(delegates));
/* 50 */     this.delegates = delegates;
/*    */   }
/*    */   
/*    */   private static MetricSampler.ThresholdTest thresholdTest(List<MetricSampler> delegates) {
/* 54 */     return value -> delegates.stream().anyMatch(());
/*    */   }
/*    */   
/*    */   private static void beforeTick(List<MetricSampler> delegates) {
/* 63 */     for (MetricSampler delegate : delegates)
/* 64 */       delegate.onStartTick(); 
/*    */   }
/*    */   
/*    */   private static double averageValueFromDelegates(List<MetricSampler> delegates) {
/* 69 */     double aggregatedValue = 0.0D;
/* 71 */     for (MetricSampler delegate : delegates)
/* 72 */       aggregatedValue += delegate.getSampler().getAsDouble(); 
/* 75 */     return aggregatedValue / delegates.size();
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 80 */     if (this == o)
/* 81 */       return true; 
/* 83 */     if (o == null || getClass() != o.getClass())
/* 84 */       return false; 
/* 86 */     if (!super.equals(o))
/* 87 */       return false; 
/* 89 */     AggregatedMetricSampler that = (AggregatedMetricSampler)o;
/* 90 */     return this.delegates.equals(that.delegates);
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 95 */     return Objects.hash(new Object[] { Integer.valueOf(super.hashCode()), this.delegates });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\MetricsRegistry$AggregatedMetricSampler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */