/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.thread.BlockableEventLoop;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ImposterProtoChunk;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*     */ 
/*     */ public abstract class GenerationChunkHolder {
/*  26 */   private static final List<ChunkStatus> CHUNK_STATUSES = ChunkStatus.getStatusList();
/*     */   
/*  28 */   private static final ChunkResult<ChunkAccess> NOT_DONE_YET = ChunkResult.error("Not done yet");
/*     */   
/*  29 */   public static final ChunkResult<ChunkAccess> UNLOADED_CHUNK = ChunkResult.error("Unloaded chunk");
/*     */   
/*  30 */   public static final CompletableFuture<ChunkResult<ChunkAccess>> UNLOADED_CHUNK_FUTURE = CompletableFuture.completedFuture(UNLOADED_CHUNK);
/*     */   
/*     */   protected final ChunkPos pos;
/*     */   
/*     */   private volatile ChunkStatus highestAllowedStatus;
/*     */   
/*  37 */   private final AtomicReference<ChunkStatus> startedWork = new AtomicReference<>();
/*     */   
/*  44 */   private final AtomicReferenceArray<CompletableFuture<ChunkResult<ChunkAccess>>> futures = new AtomicReferenceArray<>(CHUNK_STATUSES.size());
/*     */   
/*  49 */   private final AtomicReference<ChunkGenerationTask> task = new AtomicReference<>();
/*     */   
/*  51 */   private final AtomicInteger generationRefCount = new AtomicInteger();
/*     */   
/*  53 */   private volatile CompletableFuture<Void> generationSaveSyncFuture = CompletableFuture.completedFuture(null);
/*     */   
/*     */   public GenerationChunkHolder(ChunkPos pos) {
/*  56 */     this.pos = pos;
/*  57 */     if (!pos.isValid())
/*  58 */       throw new IllegalStateException("Trying to create chunk out of reasonable bounds: " + String.valueOf(pos)); 
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkResult<ChunkAccess>> scheduleChunkGenerationTask(ChunkStatus status, ChunkMap scheduler) {
/*  64 */     if (isStatusDisallowed(status))
/*  65 */       return UNLOADED_CHUNK_FUTURE; 
/*  67 */     CompletableFuture<ChunkResult<ChunkAccess>> future = getOrCreateFuture(status);
/*  68 */     if (future.isDone())
/*  69 */       return future; 
/*  71 */     ChunkGenerationTask task = this.task.get();
/*  72 */     if (task == null || status.isAfter(task.targetStatus))
/*  73 */       rescheduleChunkTask(scheduler, status); 
/*  75 */     return future;
/*     */   }
/*     */   
/*     */   CompletableFuture<ChunkResult<ChunkAccess>> applyStep(ChunkStep step, GeneratingChunkMap chunkMap, StaticCache2D<GenerationChunkHolder> cache) {
/*  81 */     if (isStatusDisallowed(step.targetStatus()))
/*  82 */       return UNLOADED_CHUNK_FUTURE; 
/*  86 */     if (acquireStatusBump(step.targetStatus()))
/*  87 */       return chunkMap.applyStep(this, step, cache).handle((chunk, exception) -> {
/*     */             if (exception != null) {
/*     */               CrashReport report = CrashReport.forThrowable(exception, "Exception chunk generation/loading");
/*     */               BlockableEventLoop.relayDelayCrash(report);
/*     */             } else {
/*     */               completeFuture(step.targetStatus(), chunk);
/*     */             } 
/*     */             return ChunkResult.of(chunk);
/*     */           }); 
/*  98 */     return getOrCreateFuture(step.targetStatus());
/*     */   }
/*     */   
/*     */   protected void updateHighestAllowedStatus(ChunkMap scheduler) {
/* 103 */     ChunkStatus oldStatus = this.highestAllowedStatus;
/* 104 */     ChunkStatus newStatus = ChunkLevel.generationStatus(getTicketLevel());
/* 105 */     this.highestAllowedStatus = newStatus;
/* 106 */     boolean statusDropped = (oldStatus != null && (newStatus == null || newStatus.isBefore(oldStatus)));
/* 107 */     if (statusDropped) {
/* 108 */       failAndClearPendingFuturesBetween(newStatus, oldStatus);
/* 110 */       if (this.task.get() != null)
/* 111 */         rescheduleChunkTask(scheduler, findHighestStatusWithPendingFuture(newStatus)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void replaceProtoChunk(ImposterProtoChunk chunk) {
/* 118 */     CompletableFuture<ChunkResult<ChunkAccess>> imposterFuture = CompletableFuture.completedFuture((ChunkResult)ChunkResult.of(chunk));
/* 120 */     for (int i = 0; i < this.futures.length() - 1; i++) {
/* 121 */       CompletableFuture<ChunkResult<ChunkAccess>> future = this.futures.get(i);
/* 122 */       Objects.requireNonNull(future);
/* 124 */       ChunkAccess maybeProtoChunk = ((ChunkResult<ChunkAccess>)future.getNow(NOT_DONE_YET)).orElse(null);
/* 125 */       if (maybeProtoChunk instanceof net.minecraft.world.level.chunk.ProtoChunk) {
/* 126 */         if (!this.futures.compareAndSet(i, future, imposterFuture))
/* 127 */           throw new IllegalStateException("Future changed by other thread while trying to replace it"); 
/*     */       } else {
/* 130 */         throw new IllegalStateException("Trying to replace a ProtoChunk, but found " + String.valueOf(maybeProtoChunk));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   void removeTask(ChunkGenerationTask task) {
/* 138 */     this.task.compareAndSet(task, null);
/*     */   }
/*     */   
/*     */   private void rescheduleChunkTask(ChunkMap scheduler, ChunkStatus status) {
/*     */     ChunkGenerationTask newTask;
/* 144 */     if (status != null) {
/* 145 */       newTask = scheduler.scheduleGenerationTask(status, getPos());
/*     */     } else {
/* 147 */       newTask = null;
/*     */     } 
/* 149 */     ChunkGenerationTask oldTask = this.task.getAndSet(newTask);
/* 150 */     if (oldTask != null)
/* 151 */       oldTask.markForCancellation(); 
/*     */   }
/*     */   
/*     */   private CompletableFuture<ChunkResult<ChunkAccess>> getOrCreateFuture(ChunkStatus status) {
/* 157 */     if (isStatusDisallowed(status))
/* 158 */       return UNLOADED_CHUNK_FUTURE; 
/* 161 */     int index = status.getIndex();
/* 162 */     CompletableFuture<ChunkResult<ChunkAccess>> future = this.futures.get(index);
/* 163 */     while (future == null) {
/* 164 */       CompletableFuture<ChunkResult<ChunkAccess>> newValue = new CompletableFuture<>();
/* 165 */       future = this.futures.compareAndExchange(index, null, newValue);
/* 166 */       if (future == null) {
/* 167 */         if (isStatusDisallowed(status)) {
/* 168 */           failAndClearPendingFuture(index, newValue);
/* 169 */           return UNLOADED_CHUNK_FUTURE;
/*     */         } 
/* 171 */         return newValue;
/*     */       } 
/*     */     } 
/* 174 */     return future;
/*     */   }
/*     */   
/*     */   private void failAndClearPendingFuturesBetween(ChunkStatus fromExclusive, ChunkStatus toInclusive) {
/* 179 */     int start = (fromExclusive == null) ? 0 : (fromExclusive.getIndex() + 1);
/* 180 */     int end = toInclusive.getIndex();
/* 181 */     for (int i = start; i <= end; i++) {
/* 182 */       CompletableFuture<ChunkResult<ChunkAccess>> previous = this.futures.get(i);
/* 183 */       if (previous != null)
/* 186 */         failAndClearPendingFuture(i, previous); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void failAndClearPendingFuture(int index, CompletableFuture<ChunkResult<ChunkAccess>> previous) {
/* 193 */     if (previous.complete(UNLOADED_CHUNK) && 
/* 194 */       !this.futures.compareAndSet(index, previous, null))
/* 195 */       throw new IllegalStateException("Nothing else should replace the future here"); 
/*     */   }
/*     */   
/*     */   private void completeFuture(ChunkStatus status, ChunkAccess chunk) {
/* 202 */     ChunkResult<ChunkAccess> result = ChunkResult.of(chunk);
/* 203 */     int index = status.getIndex();
/*     */     while (true) {
/* 207 */       CompletableFuture<ChunkResult<ChunkAccess>> future = this.futures.get(index);
/* 208 */       if (future == null) {
/* 209 */         if (this.futures.compareAndSet(index, null, CompletableFuture.completedFuture(result)))
/*     */           return; 
/*     */         continue;
/*     */       } 
/* 213 */       if (future.complete(result))
/*     */         return; 
/* 216 */       if (((ChunkResult)future.getNow(NOT_DONE_YET)).isSuccess())
/* 217 */         throw new IllegalStateException("Trying to complete a future but found it to be completed successfully already"); 
/* 220 */       Thread.yield();
/*     */     } 
/*     */   }
/*     */   
/*     */   private ChunkStatus findHighestStatusWithPendingFuture(ChunkStatus newStatus) {
/* 227 */     if (newStatus == null)
/* 228 */       return null; 
/* 230 */     ChunkStatus highestStatus = newStatus;
/* 231 */     ChunkStatus alreadyStarted = this.startedWork.get();
/* 232 */     while (alreadyStarted == null || highestStatus.isAfter(alreadyStarted)) {
/* 234 */       if (this.futures.get(highestStatus.getIndex()) != null)
/* 236 */         return highestStatus; 
/* 238 */       if (highestStatus == ChunkStatus.EMPTY)
/*     */         break; 
/* 241 */       highestStatus = highestStatus.getParent();
/*     */     } 
/* 243 */     return null;
/*     */   }
/*     */   
/*     */   private boolean acquireStatusBump(ChunkStatus status) {
/* 248 */     ChunkStatus parent = (status == ChunkStatus.EMPTY) ? null : status.getParent();
/* 250 */     ChunkStatus previousStarted = this.startedWork.compareAndExchange(parent, status);
/* 251 */     if (previousStarted == parent)
/* 252 */       return true; 
/* 255 */     if (previousStarted == null || status.isAfter(previousStarted))
/* 256 */       throw new IllegalStateException("Unexpected last startedWork status: " + String.valueOf(previousStarted) + " while trying to start: " + String.valueOf(status)); 
/* 259 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isStatusDisallowed(ChunkStatus status) {
/* 263 */     ChunkStatus highestAllowedStatus = this.highestAllowedStatus;
/* 264 */     return (highestAllowedStatus == null || status.isAfter(highestAllowedStatus));
/*     */   }
/*     */   
/*     */   public void increaseGenerationRefCount() {
/* 271 */     if (this.generationRefCount.getAndIncrement() == 0) {
/* 272 */       this.generationSaveSyncFuture = new CompletableFuture<>();
/* 273 */       addSaveDependency(this.generationSaveSyncFuture);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void decreaseGenerationRefCount() {
/* 279 */     CompletableFuture<Void> future = this.generationSaveSyncFuture;
/* 280 */     int newValue = this.generationRefCount.decrementAndGet();
/* 281 */     if (newValue == 0)
/* 282 */       future.complete(null); 
/* 284 */     if (newValue < 0)
/* 285 */       throw new IllegalStateException("More releases than claims. Count: " + newValue); 
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunkIfPresentUnchecked(ChunkStatus status) {
/* 290 */     CompletableFuture<ChunkResult<ChunkAccess>> future = this.futures.get(status.getIndex());
/* 291 */     return (future == null) ? null : ((ChunkResult<ChunkAccess>)future.getNow(NOT_DONE_YET)).orElse(null);
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunkIfPresent(ChunkStatus status) {
/* 295 */     if (isStatusDisallowed(status))
/* 296 */       return null; 
/* 298 */     return getChunkIfPresentUnchecked(status);
/*     */   }
/*     */   
/*     */   public ChunkAccess getLatestChunk() {
/* 303 */     ChunkStatus status = this.startedWork.get();
/* 304 */     if (status == null)
/* 305 */       return null; 
/* 307 */     ChunkAccess chunk = getChunkIfPresentUnchecked(status);
/* 308 */     if (chunk != null)
/* 309 */       return chunk; 
/* 311 */     return getChunkIfPresentUnchecked(status.getParent());
/*     */   }
/*     */   
/*     */   public ChunkStatus getPersistedStatus() {
/* 315 */     CompletableFuture<ChunkResult<ChunkAccess>> future = this.futures.get(ChunkStatus.EMPTY.getIndex());
/* 316 */     ChunkAccess chunkAccess = (future == null) ? null : ((ChunkResult<ChunkAccess>)future.getNow(NOT_DONE_YET)).orElse(null);
/* 317 */     return (chunkAccess == null) ? null : chunkAccess.getPersistedStatus();
/*     */   }
/*     */   
/*     */   public ChunkPos getPos() {
/* 321 */     return this.pos;
/*     */   }
/*     */   
/*     */   public FullChunkStatus getFullStatus() {
/* 325 */     return ChunkLevel.fullStatus(getTicketLevel());
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public List<Pair<ChunkStatus, CompletableFuture<ChunkResult<ChunkAccess>>>> getAllFutures() {
/* 335 */     List<Pair<ChunkStatus, CompletableFuture<ChunkResult<ChunkAccess>>>> result = new ArrayList<>();
/* 337 */     for (int i = 0; i < CHUNK_STATUSES.size(); i++)
/* 338 */       result.add(Pair.of(CHUNK_STATUSES.get(i), this.futures.get(i))); 
/* 340 */     return result;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public ChunkStatus getLatestStatus() {
/* 346 */     ChunkStatus status = this.startedWork.get();
/* 347 */     if (status == null)
/* 348 */       return null; 
/* 350 */     ChunkAccess chunk = getChunkIfPresentUnchecked(status);
/* 351 */     if (chunk != null)
/* 352 */       return status; 
/* 354 */     return status.getParent();
/*     */   }
/*     */   
/*     */   protected abstract void addSaveDependency(CompletableFuture<?> paramCompletableFuture);
/*     */   
/*     */   public abstract int getTicketLevel();
/*     */   
/*     */   public abstract int getQueueLevel();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\GenerationChunkHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */