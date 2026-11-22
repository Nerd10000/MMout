/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.thread.BlockableEventLoop;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.LocalMobCapCalculator;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.TicketStorage;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
/*     */ import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.SavedDataStorage;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerChunkCache extends ChunkSource {
/*  59 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final DistanceManager distanceManager;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final Thread mainThread;
/*     */   
/*     */   private final ThreadedLevelLightEngine lightEngine;
/*     */   
/*     */   private final MainThreadExecutor mainThreadProcessor;
/*     */   
/*     */   public final ChunkMap chunkMap;
/*     */   
/*     */   private final SavedDataStorage savedDataStorage;
/*     */   
/*     */   private final TicketStorage ticketStorage;
/*     */   
/*     */   private long lastInhabitedUpdate;
/*     */   
/*     */   private boolean spawnEnemies = true;
/*     */   
/*     */   private static final int CACHE_SIZE = 4;
/*     */   
/*  77 */   private final long[] lastChunkPos = new long[4];
/*     */   
/*  78 */   private final ChunkStatus[] lastChunkStatus = new ChunkStatus[4];
/*     */   
/*  79 */   private final ChunkAccess[] lastChunk = new ChunkAccess[4];
/*     */   
/*  80 */   private final List<LevelChunk> spawningChunks = (List<LevelChunk>)new ObjectArrayList();
/*     */   
/*  81 */   private final Set<ChunkHolder> chunkHoldersToBroadcast = (Set<ChunkHolder>)new ReferenceOpenHashSet();
/*     */   
/*     */   @VisibleForDebug
/*     */   private NaturalSpawner.SpawnState lastSpawnState;
/*     */   
/*     */   public ServerChunkCache(ServerLevel level, LevelStorageSource.LevelStorageAccess levelStorage, DataFixer fixerUpper, StructureTemplateManager structureTemplateManager, Executor executor, ChunkGenerator generator, int viewDistance, int simulationDistance, boolean syncWrites, ChunkStatusUpdateListener chunkStatusListener, Supplier<SavedDataStorage> overworldDataStorage) {
/*  87 */     this.level = level;
/*  88 */     this.mainThreadProcessor = new MainThreadExecutor(level);
/*  89 */     this.mainThread = Thread.currentThread();
/*  91 */     Path dataFolder = levelStorage.getDimensionPath(level.dimension()).resolve("data");
/*     */     try {
/*  93 */       FileUtil.createDirectoriesSafe(dataFolder);
/*  94 */     } catch (IOException e) {
/*  95 */       LOGGER.error("Failed to create dimension data storage directory", e);
/*     */     } 
/*  98 */     this.savedDataStorage = new SavedDataStorage(dataFolder, fixerUpper, (HolderLookup.Provider)level.registryAccess());
/*  99 */     this.ticketStorage = (TicketStorage)this.savedDataStorage.computeIfAbsent(TicketStorage.TYPE);
/* 101 */     this.chunkMap = new ChunkMap(level, levelStorage, fixerUpper, structureTemplateManager, executor, this.mainThreadProcessor, (LightChunkGetter)this, generator, chunkStatusListener, overworldDataStorage, this.ticketStorage, viewDistance, syncWrites);
/* 102 */     this.lightEngine = this.chunkMap.getLightEngine();
/* 103 */     this.distanceManager = this.chunkMap.getDistanceManager();
/* 104 */     this.distanceManager.updateSimulationDistance(simulationDistance);
/* 105 */     clearCache();
/*     */   }
/*     */   
/*     */   public ThreadedLevelLightEngine getLightEngine() {
/* 110 */     return this.lightEngine;
/*     */   }
/*     */   
/*     */   private ChunkHolder getVisibleChunkIfPresent(long key) {
/* 114 */     return this.chunkMap.getVisibleChunkIfPresent(key);
/*     */   }
/*     */   
/*     */   private void storeInCache(long pos, ChunkAccess chunk, ChunkStatus status) {
/* 118 */     for (int i = 3; i > 0; i--) {
/* 119 */       this.lastChunkPos[i] = this.lastChunkPos[i - 1];
/* 120 */       this.lastChunkStatus[i] = this.lastChunkStatus[i - 1];
/* 121 */       this.lastChunk[i] = this.lastChunk[i - 1];
/*     */     } 
/* 123 */     this.lastChunkPos[0] = pos;
/* 124 */     this.lastChunkStatus[0] = status;
/* 125 */     this.lastChunk[0] = chunk;
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunk(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/* 130 */     if (Thread.currentThread() != this.mainThread)
/* 131 */       return CompletableFuture.<ChunkAccess>supplyAsync(() -> getChunk(x, z, targetStatus, loadOrGenerate), (Executor)this.mainThreadProcessor).join(); 
/* 133 */     ProfilerFiller profiler = Profiler.get();
/* 134 */     profiler.incrementCounter("getChunk");
/* 136 */     long pos = ChunkPos.pack(x, z);
/* 137 */     for (int i = 0; i < 4; i++) {
/* 138 */       if (pos == this.lastChunkPos[i] && targetStatus == this.lastChunkStatus[i]) {
/* 139 */         ChunkAccess chunkAccess = this.lastChunk[i];
/* 140 */         if (chunkAccess != null || !loadOrGenerate)
/* 141 */           return chunkAccess; 
/*     */       } 
/*     */     } 
/* 146 */     profiler.incrementCounter("getChunkCacheMiss");
/* 147 */     CompletableFuture<ChunkResult<ChunkAccess>> serverFuture = getChunkFutureMainThread(x, z, targetStatus, loadOrGenerate);
/* 148 */     Objects.requireNonNull(serverFuture);
/* 148 */     this.mainThreadProcessor.managedBlock(serverFuture::isDone);
/* 150 */     ChunkResult<ChunkAccess> chunkResult = serverFuture.join();
/* 151 */     ChunkAccess chunk = chunkResult.orElse(null);
/* 152 */     if (chunk == null && 
/* 153 */       loadOrGenerate)
/* 154 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Chunk not there when requested: " + chunkResult.getError())); 
/* 158 */     storeInCache(pos, chunk, targetStatus);
/* 159 */     return chunk;
/*     */   }
/*     */   
/*     */   public LevelChunk getChunkNow(int x, int z) {
/* 164 */     if (Thread.currentThread() != this.mainThread)
/* 166 */       return null; 
/* 168 */     Profiler.get().incrementCounter("getChunkNow");
/* 170 */     long pos = ChunkPos.pack(x, z);
/* 171 */     for (int i = 0; i < 4; i++) {
/* 172 */       if (pos == this.lastChunkPos[i] && this.lastChunkStatus[i] == ChunkStatus.FULL) {
/* 173 */         ChunkAccess chunkAccess = this.lastChunk[i];
/* 174 */         LevelChunk levelChunk = (LevelChunk)chunkAccess;
/* 174 */         return (chunkAccess instanceof LevelChunk) ? levelChunk : null;
/*     */       } 
/*     */     } 
/* 178 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(pos);
/* 179 */     if (chunkHolder == null)
/* 180 */       return null; 
/* 182 */     ChunkAccess chunk = chunkHolder.getChunkIfPresent(ChunkStatus.FULL);
/* 183 */     if (chunk != null) {
/* 184 */       storeInCache(pos, chunk, ChunkStatus.FULL);
/* 185 */       if (chunk instanceof LevelChunk) {
/* 185 */         LevelChunk levelChunk = (LevelChunk)chunk;
/* 186 */         return levelChunk;
/*     */       } 
/*     */     } 
/* 189 */     return null;
/*     */   }
/*     */   
/*     */   private void clearCache() {
/* 193 */     Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
/* 194 */     Arrays.fill((Object[])this.lastChunkStatus, (Object)null);
/* 195 */     Arrays.fill((Object[])this.lastChunk, (Object)null);
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkResult<ChunkAccess>> getChunkFuture(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/*     */     CompletableFuture<ChunkResult<ChunkAccess>> serverFuture;
/* 199 */     boolean isMainThread = (Thread.currentThread() == this.mainThread);
/* 201 */     if (isMainThread) {
/* 202 */       serverFuture = getChunkFutureMainThread(x, z, targetStatus, loadOrGenerate);
/* 203 */       Objects.requireNonNull(serverFuture);
/* 203 */       this.mainThreadProcessor.managedBlock(serverFuture::isDone);
/*     */     } else {
/* 205 */       serverFuture = CompletableFuture.supplyAsync(() -> getChunkFutureMainThread(x, z, targetStatus, loadOrGenerate), (Executor)this.mainThreadProcessor).thenCompose(chunk -> chunk);
/*     */     } 
/* 207 */     return serverFuture;
/*     */   }
/*     */   
/*     */   private CompletableFuture<ChunkResult<ChunkAccess>> getChunkFutureMainThread(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/* 212 */     ChunkPos pos = new ChunkPos(x, z);
/* 213 */     long key = pos.pack();
/* 214 */     int targetTicketLevel = ChunkLevel.byStatus(targetStatus);
/* 216 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
/* 217 */     if (loadOrGenerate) {
/* 219 */       addTicket(new Ticket(TicketType.UNKNOWN, targetTicketLevel), pos);
/* 221 */       if (chunkAbsent(chunkHolder, targetTicketLevel)) {
/* 222 */         ProfilerFiller profiler = Profiler.get();
/* 223 */         profiler.push("chunkLoad");
/* 224 */         runDistanceManagerUpdates();
/* 225 */         chunkHolder = getVisibleChunkIfPresent(key);
/* 226 */         profiler.pop();
/* 227 */         if (chunkAbsent(chunkHolder, targetTicketLevel))
/* 228 */           throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("No chunk holder after ticket has been added")); 
/*     */       } 
/*     */     } 
/* 233 */     if (chunkAbsent(chunkHolder, targetTicketLevel))
/* 234 */       return GenerationChunkHolder.UNLOADED_CHUNK_FUTURE; 
/* 237 */     return chunkHolder.scheduleChunkGenerationTask(targetStatus, this.chunkMap);
/*     */   }
/*     */   
/*     */   private boolean chunkAbsent(ChunkHolder chunkHolder, int targetTicketLevel) {
/* 241 */     return (chunkHolder == null || chunkHolder.getTicketLevel() > targetTicketLevel);
/*     */   }
/*     */   
/*     */   public boolean hasChunk(int x, int z) {
/* 246 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent((new ChunkPos(x, z)).pack());
/* 247 */     int targetTicketLevel = ChunkLevel.byStatus(ChunkStatus.FULL);
/* 249 */     return !chunkAbsent(chunkHolder, targetTicketLevel);
/*     */   }
/*     */   
/*     */   public LightChunk getChunkForLighting(int x, int z) {
/* 255 */     long key = ChunkPos.pack(x, z);
/* 256 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
/* 257 */     if (chunkHolder == null)
/* 258 */       return null; 
/* 261 */     return (LightChunk)chunkHolder.getChunkIfPresentUnchecked(ChunkStatus.INITIALIZE_LIGHT.getParent());
/*     */   }
/*     */   
/*     */   public Level getLevel() {
/* 266 */     return this.level;
/*     */   }
/*     */   
/*     */   public boolean pollTask() {
/* 270 */     return this.mainThreadProcessor.pollTask();
/*     */   }
/*     */   
/*     */   boolean runDistanceManagerUpdates() {
/* 275 */     boolean updated = this.distanceManager.runAllUpdates(this.chunkMap);
/* 276 */     boolean promoted = this.chunkMap.promoteChunkMap();
/* 277 */     this.chunkMap.runGenerationTasks();
/* 278 */     if (updated || promoted) {
/* 279 */       clearCache();
/* 280 */       return true;
/*     */     } 
/* 282 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPositionTicking(long chunkKey) {
/* 286 */     if (!this.level.shouldTickBlocksAt(chunkKey))
/* 287 */       return false; 
/* 289 */     ChunkHolder holder = getVisibleChunkIfPresent(chunkKey);
/* 290 */     if (holder == null)
/* 291 */       return false; 
/* 293 */     return ((ChunkResult)holder.getTickingChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).isSuccess();
/*     */   }
/*     */   
/*     */   public void save(boolean flushStorage) {
/* 297 */     runDistanceManagerUpdates();
/* 298 */     this.chunkMap.saveAllChunks(flushStorage);
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 303 */     save(true);
/* 304 */     this.savedDataStorage.close();
/* 305 */     this.lightEngine.close();
/* 306 */     this.chunkMap.close();
/*     */   }
/*     */   
/*     */   public void tick(BooleanSupplier haveTime, boolean tickChunks) {
/* 311 */     ProfilerFiller profiler = Profiler.get();
/* 312 */     profiler.push("purge");
/* 313 */     if (this.level.tickRateManager().runsNormally() || !tickChunks)
/* 315 */       this.ticketStorage.purgeStaleTickets(this.chunkMap); 
/* 317 */     runDistanceManagerUpdates();
/* 318 */     profiler.popPush("chunks");
/* 319 */     if (tickChunks) {
/* 320 */       tickChunks();
/* 321 */       this.chunkMap.tick();
/*     */     } 
/* 323 */     profiler.popPush("unload");
/* 324 */     this.chunkMap.tick(haveTime);
/* 325 */     profiler.pop();
/* 326 */     clearCache();
/*     */   }
/*     */   
/*     */   private void tickChunks() {
/* 330 */     long time = this.level.getGameTime();
/* 331 */     long timeDiff = time - this.lastInhabitedUpdate;
/* 332 */     this.lastInhabitedUpdate = time;
/* 334 */     if (this.level.isDebug())
/*     */       return; 
/* 338 */     ProfilerFiller profiler = Profiler.get();
/* 339 */     profiler.push("pollingChunks");
/* 341 */     if (this.level.tickRateManager().runsNormally()) {
/* 342 */       profiler.push("tickingChunks");
/* 343 */       tickChunks(profiler, timeDiff);
/* 344 */       profiler.pop();
/*     */     } 
/* 347 */     broadcastChangedChunks(profiler);
/* 349 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void broadcastChangedChunks(ProfilerFiller profiler) {
/* 353 */     profiler.push("broadcast");
/* 355 */     for (ChunkHolder chunkHolder : this.chunkHoldersToBroadcast) {
/* 356 */       LevelChunk chunk = chunkHolder.getTickingChunk();
/* 357 */       if (chunk != null)
/* 358 */         chunkHolder.broadcastChanges(chunk); 
/*     */     } 
/* 361 */     this.chunkHoldersToBroadcast.clear();
/* 363 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void tickChunks(ProfilerFiller profiler, long timeDiff) {
/*     */     List<MobCategory> spawningCategories;
/* 367 */     profiler.push("naturalSpawnCount");
/* 368 */     int chunkCount = this.distanceManager.getNaturalSpawnChunkCount();
/* 369 */     NaturalSpawner.SpawnState spawnCookie = NaturalSpawner.createState(chunkCount, this.level.getAllEntities(), this::getFullChunk, new LocalMobCapCalculator(this.chunkMap));
/* 370 */     this.lastSpawnState = spawnCookie;
/* 372 */     boolean doMobSpawning = ((Boolean)this.level.getGameRules().get(GameRules.SPAWN_MOBS)).booleanValue();
/* 373 */     int tickSpeed = ((Integer)this.level.getGameRules().get(GameRules.RANDOM_TICK_SPEED)).intValue();
/* 376 */     if (doMobSpawning) {
/* 377 */       boolean spawnPersistent = (this.level.getGameTime() % 400L == 0L);
/* 378 */       spawningCategories = NaturalSpawner.getFilteredSpawningCategories(spawnCookie, this.spawnEnemies, spawnPersistent);
/*     */     } else {
/* 380 */       spawningCategories = List.of();
/*     */     } 
/* 383 */     List<LevelChunk> spawningChunks = this.spawningChunks;
/*     */     try {
/* 385 */       profiler.popPush("filteringSpawningChunks");
/* 386 */       this.chunkMap.collectSpawningChunks(spawningChunks);
/* 387 */       profiler.popPush("shuffleSpawningChunks");
/* 389 */       Util.shuffle(spawningChunks, this.level.getRandom());
/* 391 */       profiler.popPush("tickSpawningChunks");
/* 392 */       for (LevelChunk chunk : spawningChunks)
/* 393 */         tickSpawningChunk(chunk, timeDiff, spawningCategories, spawnCookie); 
/*     */     } finally {
/* 396 */       spawningChunks.clear();
/*     */     } 
/* 399 */     profiler.popPush("tickTickingChunks");
/* 400 */     this.chunkMap.forEachBlockTickingChunk(chunk -> this.level.tickChunk(chunk, tickSpeed));
/* 402 */     if (doMobSpawning) {
/* 403 */       profiler.popPush("customSpawners");
/* 404 */       this.level.tickCustomSpawners(this.spawnEnemies);
/*     */     } 
/* 406 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void tickSpawningChunk(LevelChunk chunk, long timeDiff, List<MobCategory> spawningCategories, NaturalSpawner.SpawnState spawnCookie) {
/* 410 */     ChunkPos chunkPos = chunk.getPos();
/* 411 */     chunk.incrementInhabitedTime(timeDiff);
/* 412 */     if (this.distanceManager.inEntityTickingRange(chunkPos.pack()))
/* 413 */       this.level.tickThunder(chunk); 
/* 415 */     if (spawningCategories.isEmpty())
/*     */       return; 
/* 418 */     if (this.level.canSpawnEntitiesInChunk(chunkPos))
/* 419 */       NaturalSpawner.spawnForChunk(this.level, chunk, spawnCookie, spawningCategories); 
/*     */   }
/*     */   
/*     */   private void getFullChunk(long chunkKey, Consumer<LevelChunk> output) {
/* 424 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(chunkKey);
/* 426 */     if (chunkHolder != null)
/* 427 */       ((ChunkResult<LevelChunk>)chunkHolder.getFullChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).ifSuccess(output); 
/*     */   }
/*     */   
/*     */   public String gatherStats() {
/* 433 */     return Integer.toString(getLoadedChunksCount());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int getPendingTasksCount() {
/* 438 */     return this.mainThreadProcessor.getPendingTasksCount();
/*     */   }
/*     */   
/*     */   public ChunkGenerator getGenerator() {
/* 442 */     return this.chunkMap.generator();
/*     */   }
/*     */   
/*     */   public ChunkGeneratorStructureState getGeneratorState() {
/* 446 */     return this.chunkMap.generatorState();
/*     */   }
/*     */   
/*     */   public RandomState randomState() {
/* 450 */     return this.chunkMap.randomState();
/*     */   }
/*     */   
/*     */   public int getLoadedChunksCount() {
/* 455 */     return this.chunkMap.size();
/*     */   }
/*     */   
/*     */   public void blockChanged(BlockPos pos) {
/* 459 */     int xc = SectionPos.blockToSectionCoord(pos.getX());
/* 460 */     int zc = SectionPos.blockToSectionCoord(pos.getZ());
/* 461 */     ChunkHolder chunk = getVisibleChunkIfPresent(ChunkPos.pack(xc, zc));
/* 462 */     if (chunk != null && chunk.blockChanged(pos))
/* 463 */       this.chunkHoldersToBroadcast.add(chunk); 
/*     */   }
/*     */   
/*     */   public void onLightUpdate(LightLayer layer, SectionPos pos) {
/* 469 */     this.mainThreadProcessor.execute(() -> {
/*     */           ChunkHolder chunk = getVisibleChunkIfPresent(pos.chunk().pack());
/*     */           if (chunk != null && chunk.sectionLightChanged(layer, pos.y()))
/*     */             this.chunkHoldersToBroadcast.add(chunk); 
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean hasActiveTickets() {
/* 478 */     return this.ticketStorage.shouldKeepDimensionActive();
/*     */   }
/*     */   
/*     */   public void addTicket(Ticket ticket, ChunkPos pos) {
/* 482 */     this.ticketStorage.addTicket(ticket, pos);
/*     */   }
/*     */   
/*     */   public CompletableFuture<?> addTicketAndLoadWithRadius(TicketType type, ChunkPos pos, int radius) {
/* 486 */     if (!type.doesLoad())
/* 487 */       throw new IllegalStateException("Ticket type " + String.valueOf(type) + " does not trigger chunk loading"); 
/* 488 */     if (type.canExpireIfUnloaded())
/* 489 */       throw new IllegalStateException("Ticket type " + String.valueOf(type) + " can expire before it loads, cannot fetch asynchronously"); 
/* 491 */     addTicketWithRadius(type, pos, radius);
/* 492 */     runDistanceManagerUpdates();
/* 493 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(pos.pack());
/* 494 */     Objects.requireNonNull(chunkHolder, "No chunk was scheduled for loading");
/* 495 */     return this.chunkMap.getChunkRangeFuture(chunkHolder, radius, distance -> ChunkStatus.FULL);
/*     */   }
/*     */   
/*     */   public void addTicketWithRadius(TicketType type, ChunkPos pos, int radius) {
/* 499 */     this.ticketStorage.addTicketWithRadius(type, pos, radius);
/*     */   }
/*     */   
/*     */   public void removeTicketWithRadius(TicketType type, ChunkPos pos, int radius) {
/* 503 */     this.ticketStorage.removeTicketWithRadius(type, pos, radius);
/*     */   }
/*     */   
/*     */   public boolean updateChunkForced(ChunkPos pos, boolean forced) {
/* 508 */     return this.ticketStorage.updateChunkForced(pos, forced);
/*     */   }
/*     */   
/*     */   public LongSet getForceLoadedChunks() {
/* 513 */     return this.ticketStorage.getForceLoadedChunks();
/*     */   }
/*     */   
/*     */   public void move(ServerPlayer player) {
/* 520 */     if (!player.isRemoved()) {
/* 521 */       this.chunkMap.move(player);
/* 522 */       if (player.isReceivingWaypoints())
/* 523 */         this.level.getWaypointManager().updatePlayer(player); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasEntityWithId(int id) {
/* 531 */     return this.chunkMap.hasEntityWithId(id);
/*     */   }
/*     */   
/*     */   public void removeEntity(Entity entity) {
/* 535 */     this.chunkMap.removeEntity(entity);
/*     */   }
/*     */   
/*     */   public void addEntity(Entity entity) {
/* 539 */     this.chunkMap.addEntity(entity);
/*     */   }
/*     */   
/*     */   public void sendToTrackingPlayersAndSelf(Entity entity, Packet<? super ClientGamePacketListener> packet) {
/* 543 */     this.chunkMap.sendToTrackingPlayersAndSelf(entity, packet);
/*     */   }
/*     */   
/*     */   public void sendToTrackingPlayers(Entity entity, Packet<? super ClientGamePacketListener> packet) {
/* 547 */     this.chunkMap.sendToTrackingPlayers(entity, packet);
/*     */   }
/*     */   
/*     */   public void setViewDistance(int newDistance) {
/* 551 */     this.chunkMap.setServerViewDistance(newDistance);
/*     */   }
/*     */   
/*     */   public void setSimulationDistance(int simulationDistance) {
/* 555 */     this.distanceManager.updateSimulationDistance(simulationDistance);
/*     */   }
/*     */   
/*     */   public void setSpawnSettings(boolean spawnEnemies) {
/* 560 */     this.spawnEnemies = spawnEnemies;
/*     */   }
/*     */   
/*     */   public String getChunkDebugData(ChunkPos pos) {
/* 564 */     return this.chunkMap.getChunkDebugData(pos);
/*     */   }
/*     */   
/*     */   public SavedDataStorage getDataStorage() {
/* 568 */     return this.savedDataStorage;
/*     */   }
/*     */   
/*     */   public PoiManager getPoiManager() {
/* 572 */     return this.chunkMap.getPoiManager();
/*     */   }
/*     */   
/*     */   public ChunkScanAccess chunkScanner() {
/* 576 */     return this.chunkMap.chunkScanner();
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public NaturalSpawner.SpawnState getLastSpawnState() {
/* 581 */     return this.lastSpawnState;
/*     */   }
/*     */   
/*     */   public void deactivateTicketsOnClosing() {
/* 585 */     this.ticketStorage.deactivateTicketsOnClosing();
/*     */   }
/*     */   
/*     */   public void onChunkReadyToSend(ChunkHolder chunk) {
/* 589 */     if (chunk.hasChangesToBroadcast())
/* 590 */       this.chunkHoldersToBroadcast.add(chunk); 
/*     */   }
/*     */   
/*     */   private final class MainThreadExecutor extends BlockableEventLoop<Runnable> {
/*     */     private MainThreadExecutor(Level level) {
/* 596 */       super("Chunk source main thread executor for " + String.valueOf(level.dimension().identifier()), false);
/*     */     }
/*     */     
/*     */     public Runnable wrapRunnable(Runnable runnable) {
/* 601 */       return runnable;
/*     */     }
/*     */     
/*     */     protected boolean shouldRun(Runnable task) {
/* 606 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean scheduleExecutables() {
/* 612 */       return true;
/*     */     }
/*     */     
/*     */     protected Thread getRunningThread() {
/* 617 */       return ServerChunkCache.this.mainThread;
/*     */     }
/*     */     
/*     */     protected void doRunTask(Runnable task) {
/* 622 */       Profiler.get().incrementCounter("runTask");
/* 623 */       super.doRunTask(task);
/*     */     }
/*     */     
/*     */     protected boolean pollTask() {
/* 628 */       if (ServerChunkCache.this.runDistanceManagerUpdates())
/* 629 */         return true; 
/* 631 */       ServerChunkCache.this.lightEngine.tryScheduleUpdate();
/* 632 */       return super.pollTask();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerChunkCache.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */