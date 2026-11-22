/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkDependencies;
/*     */ import net.minecraft.world.level.chunk.status.ChunkPyramid;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ public class ChunkGenerationTask {
/*     */   private final GeneratingChunkMap chunkMap;
/*     */   
/*     */   private final ChunkPos pos;
/*     */   
/*  21 */   private ChunkStatus scheduledStatus = null;
/*     */   
/*     */   public final ChunkStatus targetStatus;
/*     */   
/*     */   private volatile boolean markedForCancellation;
/*     */   
/*  26 */   private final List<CompletableFuture<ChunkResult<ChunkAccess>>> scheduledLayer = new ArrayList<>();
/*     */   
/*     */   private final StaticCache2D<GenerationChunkHolder> cache;
/*     */   
/*     */   private boolean needsGeneration;
/*     */   
/*     */   private ChunkGenerationTask(GeneratingChunkMap chunkMap, ChunkStatus targetStatus, ChunkPos pos, StaticCache2D<GenerationChunkHolder> cache) {
/*  32 */     this.chunkMap = chunkMap;
/*  33 */     this.targetStatus = targetStatus;
/*  34 */     this.pos = pos;
/*  35 */     this.cache = cache;
/*     */   }
/*     */   
/*     */   public static ChunkGenerationTask create(GeneratingChunkMap chunkMap, ChunkStatus targetStatus, ChunkPos pos) {
/*  40 */     int worstCaseRadius = ChunkPyramid.GENERATION_PYRAMID.getStepTo(targetStatus).getAccumulatedRadiusOf(ChunkStatus.EMPTY);
/*  41 */     StaticCache2D<GenerationChunkHolder> cache = StaticCache2D.create(pos.x(), pos.z(), worstCaseRadius, (x, z) -> chunkMap.acquireGeneration(ChunkPos.pack(x, z)));
/*  43 */     return new ChunkGenerationTask(chunkMap, targetStatus, pos, cache);
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> runUntilWait() {
/*     */     while (true) {
/*  48 */       CompletableFuture<?> waitingFor = waitForScheduledLayer();
/*  49 */       if (waitingFor != null)
/*  50 */         return waitingFor; 
/*  52 */       if (this.markedForCancellation || this.scheduledStatus == this.targetStatus) {
/*  54 */         releaseClaim();
/*  55 */         return null;
/*     */       } 
/*  57 */       scheduleNextLayer();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void scheduleNextLayer() {
/*     */     ChunkStatus statusToSchedule;
/*  63 */     if (this.scheduledStatus == null) {
/*  64 */       statusToSchedule = ChunkStatus.EMPTY;
/*  65 */     } else if (!this.needsGeneration && this.scheduledStatus == ChunkStatus.EMPTY && !canLoadWithoutGeneration()) {
/*  66 */       this.needsGeneration = true;
/*  68 */       statusToSchedule = ChunkStatus.EMPTY;
/*     */     } else {
/*  70 */       statusToSchedule = ChunkStatus.getStatusList().get(this.scheduledStatus.getIndex() + 1);
/*     */     } 
/*  72 */     scheduleLayer(statusToSchedule, this.needsGeneration);
/*  73 */     this.scheduledStatus = statusToSchedule;
/*     */   }
/*     */   
/*     */   public void markForCancellation() {
/*  77 */     this.markedForCancellation = true;
/*     */   }
/*     */   
/*     */   private void releaseClaim() {
/*  81 */     GenerationChunkHolder chunkHolder = (GenerationChunkHolder)this.cache.get(this.pos.x(), this.pos.z());
/*  83 */     chunkHolder.removeTask(this);
/*  84 */     Objects.requireNonNull(this.chunkMap);
/*  84 */     this.cache.forEach(this.chunkMap::releaseGeneration);
/*     */   }
/*     */   
/*     */   private boolean canLoadWithoutGeneration() {
/*  88 */     if (this.targetStatus == ChunkStatus.EMPTY)
/*  89 */       return true; 
/*  91 */     ChunkStatus highestGeneratedStatus = ((GenerationChunkHolder)this.cache.get(this.pos.x(), this.pos.z())).getPersistedStatus();
/*  92 */     if (highestGeneratedStatus == null || highestGeneratedStatus.isBefore(this.targetStatus))
/*  93 */       return false; 
/*  95 */     ChunkDependencies dependencies = ChunkPyramid.LOADING_PYRAMID.getStepTo(this.targetStatus).accumulatedDependencies();
/*  96 */     int range = dependencies.getRadius();
/*  97 */     for (int x = this.pos.x() - range; x <= this.pos.x() + range; x++) {
/*  98 */       for (int z = this.pos.z() - range; z <= this.pos.z() + range; z++) {
/*  99 */         int distance = this.pos.getChessboardDistance(x, z);
/* 100 */         ChunkStatus requiredStatus = dependencies.get(distance);
/* 101 */         ChunkStatus persistedStatus = ((GenerationChunkHolder)this.cache.get(x, z)).getPersistedStatus();
/* 102 */         if (persistedStatus == null || persistedStatus.isBefore(requiredStatus))
/* 103 */           return false; 
/*     */       } 
/*     */     } 
/* 107 */     return true;
/*     */   }
/*     */   
/*     */   public GenerationChunkHolder getCenter() {
/* 111 */     return (GenerationChunkHolder)this.cache.get(this.pos.x(), this.pos.z());
/*     */   }
/*     */   
/*     */   private void scheduleLayer(ChunkStatus status, boolean needsGeneration) {
/* 115 */     Zone zone = Profiler.get().zone("scheduleLayer");
/*     */     try {
/* 116 */       Objects.requireNonNull(status);
/* 116 */       zone.addText(status::getName);
/* 117 */       int radius = getRadiusForLayer(status, needsGeneration);
/* 118 */       for (int x = this.pos.x() - radius; x <= this.pos.x() + radius; x++) {
/* 119 */         for (int z = this.pos.z() - radius; z <= this.pos.z() + radius; z++) {
/* 120 */           GenerationChunkHolder chunkHolder = (GenerationChunkHolder)this.cache.get(x, z);
/* 121 */           if (this.markedForCancellation || !scheduleChunkInLayer(status, needsGeneration, chunkHolder)) {
/* 126 */             if (zone != null)
/* 126 */               zone.close(); 
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       } 
/* 126 */       if (zone != null)
/* 126 */         zone.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (zone != null)
/*     */         try {
/*     */           zone.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getRadiusForLayer(ChunkStatus status, boolean needsGeneration) {
/* 131 */     ChunkPyramid pyramid = needsGeneration ? ChunkPyramid.GENERATION_PYRAMID : ChunkPyramid.LOADING_PYRAMID;
/* 132 */     return pyramid.getStepTo(this.targetStatus).getAccumulatedRadiusOf(status);
/*     */   }
/*     */   
/*     */   private boolean scheduleChunkInLayer(ChunkStatus status, boolean needsGeneration, GenerationChunkHolder chunkHolder) {
/* 136 */     ChunkStatus persistedStatus = chunkHolder.getPersistedStatus();
/* 137 */     boolean generate = (persistedStatus != null && status.isAfter(persistedStatus));
/* 138 */     ChunkPyramid pyramid = generate ? ChunkPyramid.GENERATION_PYRAMID : ChunkPyramid.LOADING_PYRAMID;
/* 139 */     if (generate && !needsGeneration)
/* 140 */       throw new IllegalStateException("Can't load chunk, but didn't expect to need to generate"); 
/* 144 */     CompletableFuture<ChunkResult<ChunkAccess>> future = chunkHolder.applyStep(pyramid.getStepTo(status), this.chunkMap, this.cache);
/* 145 */     ChunkResult<ChunkAccess> now = future.getNow(null);
/* 146 */     if (now == null) {
/* 147 */       this.scheduledLayer.add(future);
/* 148 */       return true;
/*     */     } 
/* 151 */     if (now.isSuccess())
/* 152 */       return true; 
/* 155 */     markForCancellation();
/* 156 */     return false;
/*     */   }
/*     */   
/*     */   private CompletableFuture<?> waitForScheduledLayer() {
/* 161 */     while (!this.scheduledLayer.isEmpty()) {
/* 162 */       CompletableFuture<ChunkResult<ChunkAccess>> lastFuture = this.scheduledLayer.getLast();
/* 163 */       ChunkResult<ChunkAccess> resultNow = lastFuture.getNow(null);
/* 164 */       if (resultNow == null)
/* 165 */         return lastFuture; 
/* 167 */       this.scheduledLayer.removeLast();
/* 168 */       if (!resultNow.isSuccess())
/* 169 */         markForCancellation(); 
/*     */     } 
/* 173 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkGenerationTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */