/*    */ package net.minecraft.client.profiling;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.function.LongSupplier;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.LevelRenderer;
/*    */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*    */ import net.minecraft.client.renderer.extract.LevelExtractor;
/*    */ import net.minecraft.util.profiling.ProfileCollector;
/*    */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*    */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*    */ import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
/*    */ import net.minecraft.util.profiling.metrics.profiling.ProfilerSamplerAdapter;
/*    */ import net.minecraft.util.profiling.metrics.profiling.ServerMetricsSamplersProvider;
/*    */ 
/*    */ public class ClientMetricsSamplersProvider implements MetricsSamplerProvider {
/*    */   private final LevelRenderer levelRenderer;
/*    */   
/*    */   private final LevelExtractor levelExtractor;
/*    */   
/* 22 */   private final Set<MetricSampler> samplers = (Set<MetricSampler>)new ObjectOpenHashSet();
/*    */   
/* 23 */   private final ProfilerSamplerAdapter samplerFactory = new ProfilerSamplerAdapter();
/*    */   
/*    */   public ClientMetricsSamplersProvider(LongSupplier wallTimeSource, LevelRenderer levelRenderer, LevelExtractor levelExtractor) {
/* 26 */     this.levelRenderer = levelRenderer;
/* 27 */     this.levelExtractor = levelExtractor;
/* 28 */     this.samplers.add(ServerMetricsSamplersProvider.tickTimeSampler(wallTimeSource));
/* 29 */     registerStaticSamplers();
/*    */   }
/*    */   
/*    */   private void registerStaticSamplers() {
/* 33 */     this.samplers.addAll(ServerMetricsSamplersProvider.runtimeIndependentSamplers());
/* 35 */     Objects.requireNonNull(this.levelExtractor);
/* 35 */     this.samplers.add(MetricSampler.createExtractSampler("totalChunks", MetricCategory.CHUNK_RENDERING, this.levelExtractor::totalSections));
/* 36 */     Objects.requireNonNull(this.levelExtractor);
/* 36 */     this.samplers.add(MetricSampler.createExtractSampler("renderedChunks", MetricCategory.CHUNK_RENDERING, this.levelExtractor::countRenderedSections));
/* 37 */     Objects.requireNonNull(this.levelExtractor);
/* 37 */     this.samplers.add(MetricSampler.createExtractSampler("lastViewDistance", MetricCategory.CHUNK_RENDERING, this.levelExtractor::lastViewDistance));
/* 39 */     SectionRenderDispatcher sectionRenderDispatcher = this.levelRenderer.sectionRenderDispatcher();
/* 40 */     if (sectionRenderDispatcher != null) {
/* 41 */       Objects.requireNonNull(sectionRenderDispatcher);
/* 41 */       this.samplers.add(MetricSampler.createExtractSampler("freeBufferCount", MetricCategory.CHUNK_RENDERING_DISPATCHING, sectionRenderDispatcher::getFreeBufferCount));
/* 42 */       Objects.requireNonNull(sectionRenderDispatcher);
/* 42 */       this.samplers.add(MetricSampler.createExtractSampler("compileQueueSize", MetricCategory.CHUNK_RENDERING_DISPATCHING, sectionRenderDispatcher::getCompileQueueSize));
/*    */     } 
/* 46 */     Objects.requireNonNull(Minecraft.getInstance());
/* 46 */     this.samplers.add(MetricSampler.createExtractSampler("gpuUtilization", MetricCategory.GPU, Minecraft.getInstance()::getGpuUtilization));
/*    */   }
/*    */   
/*    */   public Set<MetricSampler> samplers(Supplier<ProfileCollector> singleTickProfiler) {
/* 51 */     this.samplers.addAll(this.samplerFactory.newSamplersFoundInProfiler(singleTickProfiler));
/* 52 */     return this.samplers;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\profiling\ClientMetricsSamplersProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */