/*    */ package net.minecraft.util.profiling.metrics;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.WeakHashMap;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class MetricsRegistry {
/* 12 */   public static final MetricsRegistry INSTANCE = new MetricsRegistry();
/*    */   
/* 15 */   private final WeakHashMap<ProfilerMeasured, Void> measuredInstances = new WeakHashMap<>();
/*    */   
/*    */   public void add(ProfilerMeasured profilerMeasured) {
/* 21 */     this.measuredInstances.put(profilerMeasured, null);
/*    */   }
/*    */   
/*    */   public List<MetricSampler> getRegisteredSamplers() {
/* 27 */     Map<String, List<MetricSampler>> samplersByName = (Map<String, List<MetricSampler>>)this.measuredInstances.keySet().stream().flatMap(measuredInstance -> measuredInstance.profiledMetrics().stream()).collect(Collectors.groupingBy(MetricSampler::getName));
/* 29 */     return aggregateDuplicates(samplersByName);
/*    */   }
/*    */   
/*    */   private static List<MetricSampler> aggregateDuplicates(Map<String, List<MetricSampler>> potentialDuplicates) {
/* 33 */     return (List<MetricSampler>)potentialDuplicates.entrySet().stream()
/* 34 */       .map(entry -> {
/*    */           String samplerName = (String)entry.getKey();
/*    */           List<MetricSampler> duplicateSamplers = (List<MetricSampler>)entry.getValue();
/*    */           return (duplicateSamplers.size() > 1) ? new AggregatedMetricSampler(samplerName, duplicateSamplers) : duplicateSamplers.get(0);
/* 39 */         }).collect(Collectors.toList());
/*    */   }
/*    */   
/*    */   private static class AggregatedMetricSampler extends MetricSampler {
/*    */     private final List<MetricSampler> delegates;
/*    */     
/*    */     private AggregatedMetricSampler(String name, List<MetricSampler> delegates) {
/* 49 */       super(name, MetricSampler.SamplingPhase.END_TICK, ((MetricSampler)delegates.get(0)).getCategory(), () -> averageValueFromDelegates(delegates), () -> beforeTick(delegates), thresholdTest(delegates));
/* 50 */       this.delegates = delegates;
/*    */     }
/*    */     
/*    */     private static MetricSampler.ThresholdTest thresholdTest(List<MetricSampler> delegates) {
/* 54 */       return value -> delegates.stream().anyMatch(());
/*    */     }
/*    */     
/*    */     private static void beforeTick(List<MetricSampler> delegates) {
/* 63 */       for (MetricSampler delegate : delegates)
/* 64 */         delegate.onStartTick(); 
/*    */     }
/*    */     
/*    */     private static double averageValueFromDelegates(List<MetricSampler> delegates) {
/* 69 */       double aggregatedValue = 0.0D;
/* 71 */       for (MetricSampler delegate : delegates)
/* 72 */         aggregatedValue += delegate.getSampler().getAsDouble(); 
/* 75 */       return aggregatedValue / delegates.size();
/*    */     }
/*    */     
/*    */     public boolean equals(Object o) {
/* 80 */       if (this == o)
/* 81 */         return true; 
/* 83 */       if (o == null || getClass() != o.getClass())
/* 84 */         return false; 
/* 86 */       if (!super.equals(o))
/* 87 */         return false; 
/* 89 */       AggregatedMetricSampler that = (AggregatedMetricSampler)o;
/* 90 */       return this.delegates.equals(that.delegates);
/*    */     }
/*    */     
/*    */     public int hashCode() {
/* 95 */       return Objects.hash(new Object[] { Integer.valueOf(super.hashCode()), this.delegates });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\MetricsRegistry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */