/*     */ package net.minecraft.util.profiling.metrics;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ 
/*     */ public class MetricSampler {
/*     */   private final String name;
/*     */   
/*     */   private final SamplingPhase samplingPhase;
/*     */   
/*     */   private final MetricCategory category;
/*     */   
/*     */   private final DoubleSupplier sampler;
/*     */   
/*     */   private final ByteBuf ticks;
/*     */   
/*     */   private final ByteBuf values;
/*     */   
/*     */   private volatile boolean isRunning;
/*     */   
/*     */   private final Runnable beforeTick;
/*     */   
/*     */   final ThresholdTest thresholdTest;
/*     */   
/*     */   private double currentValue;
/*     */   
/*     */   protected MetricSampler(String name, SamplingPhase samplingPhase, MetricCategory category, DoubleSupplier sampler, Runnable beforeTick, ThresholdTest thresholdTest) {
/*  32 */     this.name = name;
/*  33 */     this.samplingPhase = samplingPhase;
/*  34 */     this.category = category;
/*  35 */     this.beforeTick = beforeTick;
/*  36 */     this.sampler = sampler;
/*  37 */     this.thresholdTest = thresholdTest;
/*  38 */     this.values = ByteBufAllocator.DEFAULT.buffer();
/*  39 */     this.ticks = ByteBufAllocator.DEFAULT.buffer();
/*  40 */     this.isRunning = true;
/*     */   }
/*     */   
/*     */   public static MetricSampler create(String name, MetricCategory category, DoubleSupplier sampler) {
/*  44 */     return new MetricSampler(name, SamplingPhase.END_TICK, category, sampler, null, null);
/*     */   }
/*     */   
/*     */   public static MetricSampler createExtractSampler(String name, MetricCategory category, DoubleSupplier sampler) {
/*  48 */     return new MetricSampler(name, SamplingPhase.EXTRACT, category, sampler, null, null);
/*     */   }
/*     */   
/*     */   public static <T> MetricSamplerBuilder<T> builder(String metricName, MetricCategory category, ToDoubleFunction<T> sampler, T context) {
/*  52 */     if (sampler == null)
/*  53 */       throw new IllegalStateException(); 
/*  55 */     return new MetricSamplerBuilder<>(metricName, category, sampler, context);
/*     */   }
/*     */   
/*     */   public void onStartTick() {
/*  59 */     if (!this.isRunning)
/*  60 */       throw new IllegalStateException("Not running"); 
/*  62 */     if (this.beforeTick != null)
/*  63 */       this.beforeTick.run(); 
/*     */   }
/*     */   
/*     */   public void onEndTick(int currentTick) {
/*  72 */     verifyRunning();
/*  73 */     this.currentValue = this.sampler.getAsDouble();
/*  74 */     this.values.writeDouble(this.currentValue);
/*  75 */     this.ticks.writeInt(currentTick);
/*     */   }
/*     */   
/*     */   public void onFinished() {
/*  79 */     verifyRunning();
/*  80 */     this.values.release();
/*  81 */     this.ticks.release();
/*  82 */     this.isRunning = false;
/*     */   }
/*     */   
/*     */   private void verifyRunning() {
/*  86 */     if (!this.isRunning)
/*  87 */       throw new IllegalStateException(String.format(Locale.ROOT, "Sampler for metric %s not started!", new Object[] { this.name })); 
/*     */   }
/*     */   
/*     */   public DoubleSupplier getSampler() {
/*  92 */     return this.sampler;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  96 */     return this.name;
/*     */   }
/*     */   
/*     */   public SamplingPhase samplingPhase() {
/* 100 */     return this.samplingPhase;
/*     */   }
/*     */   
/*     */   public MetricCategory getCategory() {
/* 104 */     return this.category;
/*     */   }
/*     */   
/*     */   public SamplerResult result() {
/* 108 */     Int2DoubleOpenHashMap int2DoubleOpenHashMap = new Int2DoubleOpenHashMap();
/* 109 */     int firstTick = Integer.MIN_VALUE;
/* 110 */     int lastTick = Integer.MIN_VALUE;
/* 112 */     while (this.values.isReadable(8)) {
/* 113 */       int currentTick = this.ticks.readInt();
/* 114 */       if (firstTick == Integer.MIN_VALUE)
/* 115 */         firstTick = currentTick; 
/* 117 */       int2DoubleOpenHashMap.put(currentTick, this.values.readDouble());
/* 118 */       lastTick = currentTick;
/*     */     } 
/* 120 */     return new SamplerResult(firstTick, lastTick, (Int2DoubleMap)int2DoubleOpenHashMap);
/*     */   }
/*     */   
/*     */   public boolean triggersThreshold() {
/* 124 */     return (this.thresholdTest != null && this.thresholdTest.test(this.currentValue));
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 129 */     if (this == o)
/* 130 */       return true; 
/* 132 */     if (o == null || getClass() != o.getClass())
/* 133 */       return false; 
/* 135 */     MetricSampler that = (MetricSampler)o;
/* 136 */     return (this.name.equals(that.name) && this.category.equals(that.category));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 141 */     return this.name.hashCode();
/*     */   }
/*     */   
/*     */   public static class SamplerResult {
/*     */     private final Int2DoubleMap recording;
/*     */     
/*     */     private final int firstTick;
/*     */     
/*     */     private final int lastTick;
/*     */     
/*     */     public SamplerResult(int firstTick, int lastTick, Int2DoubleMap recording) {
/* 154 */       this.firstTick = firstTick;
/* 155 */       this.lastTick = lastTick;
/* 156 */       this.recording = recording;
/*     */     }
/*     */     
/*     */     public double valueAtTick(int tick) {
/* 160 */       return this.recording.get(tick);
/*     */     }
/*     */     
/*     */     public int getFirstTick() {
/* 164 */       return this.firstTick;
/*     */     }
/*     */     
/*     */     public int getLastTick() {
/* 168 */       return this.lastTick;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ValueIncreasedByPercentage implements ThresholdTest {
/*     */     private final float percentageIncreaseThreshold;
/*     */     
/* 174 */     private double previousValue = Double.MIN_VALUE;
/*     */     
/*     */     public ValueIncreasedByPercentage(float percentageIncreaseThreshold) {
/* 177 */       this.percentageIncreaseThreshold = percentageIncreaseThreshold;
/*     */     }
/*     */     
/*     */     public boolean test(double value) {
/*     */       boolean result;
/* 184 */       if (this.previousValue == Double.MIN_VALUE || value <= this.previousValue) {
/* 185 */         result = false;
/*     */       } else {
/* 187 */         result = ((value - this.previousValue) / this.previousValue >= this.percentageIncreaseThreshold);
/*     */       } 
/* 190 */       this.previousValue = value;
/* 191 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MetricSamplerBuilder<T> {
/*     */     private final String name;
/*     */     
/*     */     private final MetricCategory category;
/*     */     
/*     */     private final DoubleSupplier sampler;
/*     */     
/*     */     private final T context;
/*     */     
/* 201 */     private MetricSampler.SamplingPhase samplingPhase = MetricSampler.SamplingPhase.END_TICK;
/*     */     
/*     */     private Runnable beforeTick;
/*     */     
/*     */     private MetricSampler.ThresholdTest thresholdTest;
/*     */     
/*     */     public MetricSamplerBuilder(String name, MetricCategory category, ToDoubleFunction<T> sampler, T context) {
/* 206 */       this.name = name;
/* 207 */       this.category = category;
/* 208 */       this.sampler = (() -> sampler.applyAsDouble(context));
/* 209 */       this.context = context;
/*     */     }
/*     */     
/*     */     public MetricSamplerBuilder<T> withBeforeTick(Consumer<T> beforeTick) {
/* 213 */       this.beforeTick = (() -> beforeTick.accept(this.context));
/* 214 */       return this;
/*     */     }
/*     */     
/*     */     public MetricSamplerBuilder<T> withThresholdAlert(MetricSampler.ThresholdTest thresholdTest) {
/* 218 */       this.thresholdTest = thresholdTest;
/* 219 */       return this;
/*     */     }
/*     */     
/*     */     public MetricSamplerBuilder<T> withSamplingPhase(MetricSampler.SamplingPhase samplingPhase) {
/* 223 */       this.samplingPhase = samplingPhase;
/* 224 */       return this;
/*     */     }
/*     */     
/*     */     public MetricSampler build() {
/* 228 */       return new MetricSampler(this.name, this.samplingPhase, this.category, this.sampler, this.beforeTick, this.thresholdTest);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum SamplingPhase {
/* 233 */     EXTRACT, END_TICK;
/*     */   }
/*     */   
/*     */   public static interface ThresholdTest {
/*     */     boolean test(double param1Double);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\MetricSampler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */