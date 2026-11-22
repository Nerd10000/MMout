/*    */ package net.minecraft.util.profiling.metrics.profiling;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.TimeUtil;
/*    */ import net.minecraft.util.profiling.ActiveProfiler;
/*    */ import net.minecraft.util.profiling.ProfileCollector;
/*    */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*    */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*    */ 
/*    */ public class ProfilerSamplerAdapter {
/* 15 */   private final Set<String> previouslyFoundSamplerNames = (Set<String>)new ObjectOpenHashSet();
/*    */   
/*    */   public Set<MetricSampler> newSamplersFoundInProfiler(Supplier<ProfileCollector> profiler) {
/* 21 */     Set<MetricSampler> newSamplers = (Set<MetricSampler>)((ProfileCollector)profiler.get()).getChartedPaths().stream().filter(pathAndCategory -> !this.previouslyFoundSamplerNames.contains(pathAndCategory.getFirst())).map(pathAndCategory -> samplerForProfilingPath(profiler, (String)pathAndCategory.getFirst(), (MetricCategory)pathAndCategory.getSecond())).collect(Collectors.toSet());
/* 23 */     for (MetricSampler sampler : newSamplers)
/* 24 */       this.previouslyFoundSamplerNames.add(sampler.getName()); 
/* 27 */     return newSamplers;
/*    */   }
/*    */   
/*    */   private static MetricSampler samplerForProfilingPath(Supplier<ProfileCollector> profiler, String profilerPath, MetricCategory category) {
/* 31 */     return MetricSampler.create(profilerPath, category, () -> {
/*    */           ActiveProfiler.PathEntry entry = ((ProfileCollector)profiler.get()).getEntry(profilerPath);
/*    */           return (entry == null) ? 0.0D : (entry.getMaxDuration() / TimeUtil.NANOSECONDS_PER_MILLISECOND);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\metrics\profiling\ProfilerSamplerAdapter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */