/*     */ package net.minecraft.util.profiling.metrics;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ 
/*     */ public class MetricSamplerBuilder<T> {
/*     */   private final String name;
/*     */   
/*     */   private final MetricCategory category;
/*     */   
/*     */   private final DoubleSupplier sampler;
/*     */   
/*     */   private final T context;
/*     */   
/* 201 */   private MetricSampler.SamplingPhase samplingPhase = MetricSampler.SamplingPhase.END_TICK;
/*     */   
/*     */   private Runnable beforeTick;
/*     */   
/*     */   private MetricSampler.ThresholdTest thresholdTest;
/*     */   
/*     */   public MetricSamplerBuilder(String name, MetricCategory category, ToDoubleFunction<T> sampler, T context) {
/* 206 */     this.name = name;
/* 207 */     this.category = category;
/* 208 */     this.sampler = (() -> sampler.applyAsDouble(context));
/* 209 */     this.context = context;
/*     */   }
/*     */   
/*     */   public MetricSamplerBuilder<T> withBeforeTick(Consumer<T> beforeTick) {
/* 213 */     this.beforeTick = (() -> beforeTick.accept(this.context));
/* 214 */     return this;
/*     */   }
/*     */   
/*     */   public MetricSamplerBuilder<T> withThresholdAlert(MetricSampler.ThresholdTest thresholdTest) {
/* 218 */     this.thresholdTest = thresholdTest;
/* 219 */     return this;
/*     */   }
/*     */   
/*     */   public MetricSamplerBuilder<T> withSamplingPhase(MetricSampler.SamplingPhase samplingPhase) {
/* 223 */     this.samplingPhase = samplingPhase;
/* 224 */     return this;
/*     */   }
/*     */   
/*     */   public MetricSampler build() {
/* 228 */     return new MetricSampler(this.name, this.samplingPhase, this.category, this.sampler, this.beforeTick, this.thresholdTest);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\MetricSampler$MetricSamplerBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */