/*     */ package net.minecraft.util.profiling.metrics.profiling;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.base.Ticker;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.util.profiling.ProfileCollector;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsRegistry;
/*     */ import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
/*     */ import org.slf4j.Logger;
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ 
/*     */ public class ServerMetricsSamplersProvider implements MetricsSamplerProvider {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  28 */   private final Set<MetricSampler> samplers = (Set<MetricSampler>)new ObjectOpenHashSet();
/*     */   
/*  29 */   private final ProfilerSamplerAdapter samplerFactory = new ProfilerSamplerAdapter();
/*     */   
/*     */   public ServerMetricsSamplersProvider(LongSupplier wallTimeSource, boolean isDedicatedServer) {
/*  32 */     this.samplers.add(tickTimeSampler(wallTimeSource));
/*  34 */     if (isDedicatedServer)
/*  35 */       this.samplers.addAll(runtimeIndependentSamplers()); 
/*     */   }
/*     */   
/*     */   public static Set<MetricSampler> runtimeIndependentSamplers() {
/*  42 */     ImmutableSet.Builder<MetricSampler> result = ImmutableSet.builder();
/*     */     try {
/*  45 */       CpuStats cpuStats = new CpuStats();
/*  48 */       Objects.requireNonNull(result);
/*  48 */       IntStream.range(0, cpuStats.nrOfCpus).mapToObj(i -> MetricSampler.create("cpu#" + i, MetricCategory.CPU, ())).forEach(result::add);
/*  49 */     } catch (Throwable t) {
/*  50 */       LOGGER.warn("Failed to query cpu, no cpu stats will be recorded", t);
/*     */     } 
/*  53 */     result.add(MetricSampler.create("heap MiB", MetricCategory.JVM, () -> SystemReport.sizeInMiB(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
/*  54 */     result.addAll(MetricsRegistry.INSTANCE.getRegisteredSamplers());
/*  55 */     return (Set<MetricSampler>)result.build();
/*     */   }
/*     */   
/*     */   public Set<MetricSampler> samplers(Supplier<ProfileCollector> profiler) {
/*  60 */     this.samplers.addAll(this.samplerFactory.newSamplersFoundInProfiler(profiler));
/*  61 */     return this.samplers;
/*     */   }
/*     */   
/*     */   public static MetricSampler tickTimeSampler(final LongSupplier timeSource) {
/*  65 */     Stopwatch stopwatch = Stopwatch.createUnstarted(new Ticker() {
/*     */           public long read() {
/*  68 */             return timeSource.getAsLong();
/*     */           }
/*     */         });
/*  72 */     ToDoubleFunction<Stopwatch> timeSampler = watch -> {
/*     */         if (watch.isRunning())
/*     */           watch.stop(); 
/*     */         long deltaTime = watch.elapsed(TimeUnit.NANOSECONDS);
/*     */         watch.reset();
/*     */         return deltaTime;
/*     */       };
/*  81 */     MetricSampler.ValueIncreasedByPercentage thresholdAlerter = new MetricSampler.ValueIncreasedByPercentage(2.0F);
/*  83 */     return MetricSampler.builder("ticktime", MetricCategory.TICK_LOOP, timeSampler, stopwatch)
/*  84 */       .withBeforeTick(Stopwatch::start)
/*  85 */       .withThresholdAlert((MetricSampler.ThresholdTest)thresholdAlerter)
/*  86 */       .build();
/*     */   }
/*     */   
/*     */   public static class CpuStats {
/*  92 */     private final SystemInfo systemInfo = new SystemInfo();
/*     */     
/*  93 */     private final CentralProcessor processor = this.systemInfo.getHardware().getProcessor();
/*     */     
/*  94 */     public final int nrOfCpus = this.processor.getLogicalProcessorCount();
/*     */     
/*  96 */     private long[][] previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/*     */     
/*  97 */     private double[] currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/*     */     
/*     */     private long lastPollMs;
/*     */     
/*     */     public double loadForCpu(int i) {
/* 101 */       long now = System.currentTimeMillis();
/* 102 */       if (this.lastPollMs == 0L || this.lastPollMs + 501L < now) {
/* 103 */         this.currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/* 104 */         this.previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/* 105 */         this.lastPollMs = now;
/*     */       } 
/* 108 */       return this.currentLoad[i] * 100.0D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\profiling\ServerMetricsSamplersProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */