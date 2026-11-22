/*     */ package net.minecraft.util.profiling.metrics.profiling;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Instant;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.LongSupplier;
/*     */ import net.minecraft.util.profiling.ActiveProfiler;
/*     */ import net.minecraft.util.profiling.ContinuousProfiler;
/*     */ import net.minecraft.util.profiling.EmptyProfileResults;
/*     */ import net.minecraft.util.profiling.InactiveProfiler;
/*     */ import net.minecraft.util.profiling.ProfileCollector;
/*     */ import net.minecraft.util.profiling.ProfileResults;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
/*     */ import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
/*     */ import net.minecraft.util.profiling.metrics.storage.RecordedDeviation;
/*     */ 
/*     */ public class ActiveMetricsRecorder implements MetricsRecorder {
/*     */   public static final int PROFILING_MAX_DURATION_SECONDS = 10;
/*     */   
/*  33 */   private static Consumer<Path> globalOnReportFinished = null;
/*     */   
/*  35 */   private final Map<MetricSampler, List<RecordedDeviation>> deviationsBySampler = (Map<MetricSampler, List<RecordedDeviation>>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private final ContinuousProfiler taskProfiler;
/*     */   
/*     */   private final Executor ioExecutor;
/*     */   
/*     */   private final MetricsPersister metricsPersister;
/*     */   
/*     */   private final Consumer<ProfileResults> onProfilingEnd;
/*     */   
/*     */   private final Consumer<Path> onReportFinished;
/*     */   
/*     */   private final MetricsSamplerProvider metricsSamplerProvider;
/*     */   
/*     */   private final LongSupplier wallTimeSource;
/*     */   
/*     */   private final long deadlineNano;
/*     */   
/*     */   private int currentTick;
/*     */   
/*     */   private ProfileCollector singleTickProfiler;
/*     */   
/*     */   private volatile boolean killSwitch;
/*     */   
/*  49 */   private Set<MetricSampler> thisTickSamplers = (Set<MetricSampler>)ImmutableSet.of();
/*     */   
/*     */   private ActiveMetricsRecorder(MetricsSamplerProvider metricsSamplerProvider, LongSupplier timeSource, Executor ioExecutor, MetricsPersister metricsPersister, Consumer<ProfileResults> onProfilingEnd, Consumer<Path> onReportFinished) {
/*  52 */     this.metricsSamplerProvider = metricsSamplerProvider;
/*  53 */     this.wallTimeSource = timeSource;
/*  54 */     this.taskProfiler = new ContinuousProfiler(timeSource, () -> this.currentTick, () -> false);
/*  55 */     this.ioExecutor = ioExecutor;
/*  56 */     this.metricsPersister = metricsPersister;
/*  57 */     this.onProfilingEnd = onProfilingEnd;
/*  58 */     this.onReportFinished = (globalOnReportFinished == null) ? onReportFinished : onReportFinished.andThen(globalOnReportFinished);
/*  59 */     this.deadlineNano = timeSource.getAsLong() + TimeUnit.NANOSECONDS.convert(10L, TimeUnit.SECONDS);
/*  60 */     this.singleTickProfiler = (ProfileCollector)new ActiveProfiler(this.wallTimeSource, () -> this.currentTick, () -> true);
/*  61 */     this.taskProfiler.enable();
/*     */   }
/*     */   
/*     */   public static ActiveMetricsRecorder createStarted(MetricsSamplerProvider metricsSamplerProvider, LongSupplier timeSource, Executor ioExecutor, MetricsPersister metricsPersister, Consumer<ProfileResults> onProfilingEnd, Consumer<Path> onReportFinished) {
/*  65 */     return new ActiveMetricsRecorder(metricsSamplerProvider, timeSource, ioExecutor, metricsPersister, onProfilingEnd, onReportFinished);
/*     */   }
/*     */   
/*     */   public synchronized void end() {
/*  70 */     if (!isRecording())
/*     */       return; 
/*  73 */     this.killSwitch = true;
/*     */   }
/*     */   
/*     */   public synchronized void cancel() {
/*  78 */     if (!isRecording())
/*     */       return; 
/*  82 */     this.singleTickProfiler = (ProfileCollector)InactiveProfiler.INSTANCE;
/*  83 */     this.onProfilingEnd.accept(EmptyProfileResults.EMPTY);
/*  85 */     cleanup(this.thisTickSamplers);
/*     */   }
/*     */   
/*     */   public void startTick() {
/*  90 */     verifyStarted();
/*  91 */     this.thisTickSamplers = this.metricsSamplerProvider.samplers(() -> this.singleTickProfiler);
/*  92 */     for (MetricSampler sampler : this.thisTickSamplers)
/*  93 */       sampler.onStartTick(); 
/*  95 */     this.currentTick++;
/*     */   }
/*     */   
/*     */   public void sampleDuringExtract() {
/* 100 */     sample(MetricSampler.SamplingPhase.EXTRACT);
/*     */   }
/*     */   
/*     */   public void endTick() {
/* 105 */     sample(MetricSampler.SamplingPhase.END_TICK);
/* 107 */     if (this.killSwitch || this.wallTimeSource.getAsLong() > this.deadlineNano) {
/* 108 */       this.killSwitch = false;
/* 109 */       ProfileResults results = this.taskProfiler.getResults();
/* 110 */       this.singleTickProfiler = (ProfileCollector)InactiveProfiler.INSTANCE;
/* 111 */       this.onProfilingEnd.accept(results);
/* 112 */       scheduleSaveResults(results);
/*     */       return;
/*     */     } 
/* 116 */     this.singleTickProfiler = (ProfileCollector)new ActiveProfiler(this.wallTimeSource, () -> this.currentTick, () -> true);
/*     */   }
/*     */   
/*     */   private void sample(MetricSampler.SamplingPhase samplingPhase) {
/* 120 */     verifyStarted();
/* 121 */     if (this.currentTick == 0)
/*     */       return; 
/* 125 */     for (MetricSampler sampler : this.thisTickSamplers) {
/* 126 */       if (sampler.samplingPhase() != samplingPhase)
/*     */         continue; 
/* 129 */       sampler.onEndTick(this.currentTick);
/* 130 */       if (sampler.triggersThreshold()) {
/* 131 */         RecordedDeviation recordedDeviation = new RecordedDeviation(Instant.now(), this.currentTick, this.singleTickProfiler.getResults());
/* 132 */         ((List<RecordedDeviation>)this.deviationsBySampler.computeIfAbsent(sampler, ignored -> Lists.newArrayList())).add(recordedDeviation);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isRecording() {
/* 139 */     return this.taskProfiler.isEnabled();
/*     */   }
/*     */   
/*     */   public ProfilerFiller getProfiler() {
/* 144 */     return ProfilerFiller.combine(this.taskProfiler.getFiller(), (ProfilerFiller)this.singleTickProfiler);
/*     */   }
/*     */   
/*     */   private void verifyStarted() {
/* 148 */     if (!isRecording())
/* 149 */       throw new IllegalStateException("Not started!"); 
/*     */   }
/*     */   
/*     */   private void scheduleSaveResults(ProfileResults profilerResults) {
/* 154 */     HashSet<MetricSampler> metricSamplers = new HashSet<>(this.thisTickSamplers);
/* 155 */     this.ioExecutor.execute(() -> {
/*     */           Path pathToLogs = this.metricsPersister.saveReports(metricSamplers, this.deviationsBySampler, profilerResults);
/*     */           cleanup(metricSamplers);
/*     */           this.onReportFinished.accept(pathToLogs);
/*     */         });
/*     */   }
/*     */   
/*     */   private void cleanup(Collection<MetricSampler> metricSamplers) {
/* 164 */     for (MetricSampler sampler : metricSamplers)
/* 165 */       sampler.onFinished(); 
/* 168 */     this.deviationsBySampler.clear();
/* 169 */     this.taskProfiler.disable();
/*     */   }
/*     */   
/*     */   public static void registerGlobalCompletionCallback(Consumer<Path> onFinished) {
/* 173 */     globalOnReportFinished = onFinished;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\profiling\ActiveMetricsRecorder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */