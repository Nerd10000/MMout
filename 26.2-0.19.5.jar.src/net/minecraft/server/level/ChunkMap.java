/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Queues;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2LongMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*      */ import it.unimi.dsi.fastutil.longs.LongLinkedOpenHashSet;
/*      */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*      */ import it.unimi.dsi.fastutil.longs.LongSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.nio.file.Path;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.CancellationException;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.CompletionException;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.IntConsumer;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.IntSupplier;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.HolderGetter;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ClientboundChunksBiomesPacket;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.network.ServerPlayerConnection;
/*      */ import net.minecraft.util.CsvOutput;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.StaticCache2D;
/*      */ import net.minecraft.util.TriState;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.datafix.DataFixTypes;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.thread.BlockableEventLoop;
/*      */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*      */ import net.minecraft.util.thread.TaskScheduler;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelHeightAccessor;
/*      */ import net.minecraft.world.level.TicketStorage;
/*      */ import net.minecraft.world.level.chunk.ChunkAccess;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*      */ import net.minecraft.world.level.chunk.ProtoChunk;
/*      */ import net.minecraft.world.level.chunk.UpgradeData;
/*      */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*      */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*      */ import net.minecraft.world.level.chunk.status.ChunkType;
/*      */ import net.minecraft.world.level.chunk.status.WorldGenContext;
/*      */ import net.minecraft.world.level.chunk.storage.ChunkIOErrorReporter;
/*      */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*      */ import net.minecraft.world.level.chunk.storage.SerializableChunkData;
/*      */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*      */ import net.minecraft.world.level.entity.ChunkStatusUpdateListener;
/*      */ import net.minecraft.world.level.entity.EntityAccess;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
/*      */ import net.minecraft.world.level.levelgen.RandomState;
/*      */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.level.storage.SavedDataStorage;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class ChunkMap extends SimpleRegionStorage implements ChunkHolder.PlayerProvider, GeneratingChunkMap {
/*  114 */   private static final ChunkResult<List<ChunkAccess>> UNLOADED_CHUNK_LIST_RESULT = ChunkResult.error("Unloaded chunks found in range");
/*      */   
/*  115 */   private static final CompletableFuture<ChunkResult<List<ChunkAccess>>> UNLOADED_CHUNK_LIST_FUTURE = CompletableFuture.completedFuture(UNLOADED_CHUNK_LIST_RESULT);
/*      */   
/*      */   private static final byte CHUNK_TYPE_REPLACEABLE = -1;
/*      */   
/*      */   private static final byte CHUNK_TYPE_UNKNOWN = 0;
/*      */   
/*      */   private static final byte CHUNK_TYPE_FULL = 1;
/*      */   
/*  120 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int CHUNK_SAVED_PER_TICK = 200;
/*      */   
/*      */   private static final int CHUNK_SAVED_EAGERLY_PER_TICK = 20;
/*      */   
/*      */   private static final int EAGER_CHUNK_SAVE_COOLDOWN_IN_MILLIS = 10000;
/*      */   
/*      */   private static final int MAX_ACTIVE_CHUNK_WRITES = 128;
/*      */   
/*      */   public static final int MIN_VIEW_DISTANCE = 2;
/*      */   
/*      */   public static final int MAX_VIEW_DISTANCE = 32;
/*      */   
/*  131 */   public static final int FORCED_TICKET_LEVEL = ChunkLevel.byStatus(FullChunkStatus.ENTITY_TICKING);
/*      */   
/*  134 */   private final Long2ObjectLinkedOpenHashMap<ChunkHolder> updatingChunkMap = new Long2ObjectLinkedOpenHashMap();
/*      */   
/*  135 */   private volatile Long2ObjectLinkedOpenHashMap<ChunkHolder> visibleChunkMap = this.updatingChunkMap.clone();
/*      */   
/*  137 */   private final Long2ObjectLinkedOpenHashMap<ChunkHolder> pendingUnloads = new Long2ObjectLinkedOpenHashMap();
/*      */   
/*  140 */   private final List<ChunkGenerationTask> pendingGenerationTasks = new ArrayList<>();
/*      */   
/*      */   private final ServerLevel level;
/*      */   
/*      */   private final ThreadedLevelLightEngine lightEngine;
/*      */   
/*      */   private final BlockableEventLoop<Runnable> mainThreadExecutor;
/*      */   
/*      */   private final RandomState randomState;
/*      */   
/*      */   private final ChunkGeneratorStructureState chunkGeneratorState;
/*      */   
/*      */   private final TicketStorage ticketStorage;
/*      */   
/*      */   private final PoiManager poiManager;
/*      */   
/*  150 */   private final LongSet toDrop = (LongSet)new LongOpenHashSet();
/*      */   
/*      */   private boolean modified;
/*      */   
/*      */   private final ChunkTaskDispatcher worldgenTaskDispatcher;
/*      */   
/*      */   private final ChunkTaskDispatcher lightTaskDispatcher;
/*      */   
/*      */   private final ChunkStatusUpdateListener chunkStatusListener;
/*      */   
/*      */   private final DistanceManager distanceManager;
/*      */   
/*      */   private final String storageName;
/*      */   
/*  161 */   private final PlayerMap playerMap = new PlayerMap();
/*      */   
/*  162 */   private final Int2ObjectMap<TrackedEntity> entityMap = (Int2ObjectMap<TrackedEntity>)new Int2ObjectOpenHashMap();
/*      */   
/*  164 */   private final Long2ByteMap chunkTypeCache = (Long2ByteMap)new Long2ByteOpenHashMap();
/*      */   
/*  165 */   private final Long2LongMap nextChunkSaveTime = (Long2LongMap)new Long2LongOpenHashMap();
/*      */   
/*  167 */   private final LongSet chunksToEagerlySave = (LongSet)new LongLinkedOpenHashSet();
/*      */   
/*  168 */   private final Queue<Runnable> unloadQueue = Queues.newConcurrentLinkedQueue();
/*      */   
/*  169 */   private final AtomicInteger activeChunkWrites = new AtomicInteger();
/*      */   
/*      */   private int serverViewDistance;
/*      */   
/*      */   private final WorldGenContext worldGenContext;
/*      */   
/*      */   public ChunkMap(ServerLevel level, LevelStorageSource.LevelStorageAccess levelStorage, DataFixer dataFixer, StructureTemplateManager structureManager, Executor executor, BlockableEventLoop<Runnable> mainThreadExecutor, LightChunkGetter chunkGetter, ChunkGenerator generator, ChunkStatusUpdateListener chunkStatusListener, Supplier<SavedDataStorage> overworldDataStorage, TicketStorage ticketStorage, int serverViewDistance, boolean syncWrites) {
/*  189 */     super(new RegionStorageInfo(levelStorage
/*  190 */           .getLevelId(), level.dimension(), "chunk"), levelStorage
/*  191 */         .getDimensionPath(level.dimension()).resolve("region"), dataFixer, syncWrites, DataFixTypes.CHUNK);
/*  196 */     Path storageFolder = levelStorage.getDimensionPath(level.dimension());
/*  197 */     this.storageName = storageFolder.getFileName().toString();
/*  198 */     this.level = level;
/*  199 */     RegistryAccess registryAccess = level.registryAccess();
/*  200 */     long levelSeed = level.getSeed();
/*  201 */     if (generator instanceof NoiseBasedChunkGenerator) {
/*  201 */       NoiseBasedChunkGenerator noiseGenerator = (NoiseBasedChunkGenerator)generator;
/*  202 */       this.randomState = RandomState.create((NoiseGeneratorSettings)noiseGenerator.generatorSettings().value(), (HolderGetter)registryAccess.lookupOrThrow(Registries.NOISE), levelSeed);
/*      */     } else {
/*  206 */       this.randomState = RandomState.create(NoiseGeneratorSettings.dummy(), (HolderGetter)registryAccess.lookupOrThrow(Registries.NOISE), levelSeed);
/*      */     } 
/*  208 */     this.chunkGeneratorState = generator.createState((HolderLookup)registryAccess.lookupOrThrow(Registries.STRUCTURE_SET), this.randomState, levelSeed);
/*  209 */     this.mainThreadExecutor = mainThreadExecutor;
/*  211 */     ConsecutiveExecutor worldgen = new ConsecutiveExecutor(executor, "worldgen");
/*  212 */     this.chunkStatusListener = chunkStatusListener;
/*  213 */     ConsecutiveExecutor light = new ConsecutiveExecutor(executor, "light");
/*  215 */     this.worldgenTaskDispatcher = new ChunkTaskDispatcher((TaskScheduler<Runnable>)worldgen, executor);
/*  216 */     this.lightTaskDispatcher = new ChunkTaskDispatcher((TaskScheduler<Runnable>)light, executor);
/*  218 */     this.lightEngine = new ThreadedLevelLightEngine(chunkGetter, this, this.level.dimensionType().hasSkyLight(), light, this.lightTaskDispatcher);
/*  220 */     this.distanceManager = new DistanceManager(ticketStorage, executor, (Executor)mainThreadExecutor);
/*  221 */     this.ticketStorage = ticketStorage;
/*  222 */     this.poiManager = new PoiManager(new RegionStorageInfo(levelStorage.getLevelId(), level.dimension(), "poi"), storageFolder.resolve("poi"), dataFixer, syncWrites, registryAccess, (ChunkIOErrorReporter)level.getServer(), (LevelHeightAccessor)level);
/*  224 */     setServerViewDistance(serverViewDistance);
/*  226 */     this.worldGenContext = new WorldGenContext(level, generator, structureManager, this.lightEngine, (Executor)mainThreadExecutor, this::setChunkUnsaved);
/*      */   }
/*      */   
/*      */   private void setChunkUnsaved(ChunkPos chunkPos) {
/*  230 */     this.chunksToEagerlySave.add(chunkPos.pack());
/*      */   }
/*      */   
/*      */   protected ChunkGenerator generator() {
/*  234 */     return this.worldGenContext.generator();
/*      */   }
/*      */   
/*      */   protected ChunkGeneratorStructureState generatorState() {
/*  238 */     return this.chunkGeneratorState;
/*      */   }
/*      */   
/*      */   protected RandomState randomState() {
/*  242 */     return this.randomState;
/*      */   }
/*      */   
/*      */   public boolean isChunkTracked(ServerPlayer player, int chunkX, int chunkZ) {
/*  246 */     return (player.getChunkTrackingView().contains(chunkX, chunkZ) && !player.connection.chunkSender.isPending(ChunkPos.pack(chunkX, chunkZ)));
/*      */   }
/*      */   
/*      */   private boolean isChunkOnTrackedBorder(ServerPlayer player, int chunkX, int chunkZ) {
/*  251 */     if (!isChunkTracked(player, chunkX, chunkZ))
/*  252 */       return false; 
/*  254 */     for (int dx = -1; dx <= 1; dx++) {
/*  255 */       for (int dz = -1; dz <= 1; dz++) {
/*  256 */         if (dx != 0 || dz != 0)
/*  259 */           if (!isChunkTracked(player, chunkX + dx, chunkZ + dz))
/*  260 */             return true;  
/*      */       } 
/*      */     } 
/*  264 */     return false;
/*      */   }
/*      */   
/*      */   protected ThreadedLevelLightEngine getLightEngine() {
/*  268 */     return this.lightEngine;
/*      */   }
/*      */   
/*      */   public ChunkHolder getUpdatingChunkIfPresent(long key) {
/*  272 */     return (ChunkHolder)this.updatingChunkMap.get(key);
/*      */   }
/*      */   
/*      */   protected ChunkHolder getVisibleChunkIfPresent(long key) {
/*  276 */     return (ChunkHolder)this.visibleChunkMap.get(key);
/*      */   }
/*      */   
/*      */   public ChunkStatus getLatestStatus(long key) {
/*  280 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
/*  281 */     return (chunkHolder != null) ? chunkHolder.getLatestStatus() : null;
/*      */   }
/*      */   
/*      */   protected IntSupplier getChunkQueueLevel(long pos) {
/*  285 */     return () -> {
/*      */         ChunkHolder chunk = getVisibleChunkIfPresent(pos);
/*      */         return (chunk == null) ? (ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT - 1) : Math.min(chunk.getQueueLevel(), ChunkTaskPriorityQueue.PRIORITY_LEVEL_COUNT - 1);
/*      */       };
/*      */   }
/*      */   
/*      */   public String getChunkDebugData(ChunkPos pos) {
/*  295 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(pos.pack());
/*  296 */     if (chunkHolder == null)
/*  297 */       return "null"; 
/*  299 */     String result = "" + chunkHolder.getTicketLevel() + "\n";
/*  300 */     ChunkStatus status = chunkHolder.getLatestStatus();
/*  301 */     ChunkAccess chunk = chunkHolder.getLatestChunk();
/*  302 */     if (status != null)
/*  303 */       result = result + "St: §" + result + status.getIndex() + "§r\n"; 
/*  305 */     if (chunk != null)
/*  306 */       result = result + "Ch: §" + result + chunk.getPersistedStatus().getIndex() + "§r\n"; 
/*  308 */     FullChunkStatus fullStatus = chunkHolder.getFullStatus();
/*  309 */     result = result + result + String.valueOf('§') + fullStatus.ordinal();
/*  310 */     return result + "§r";
/*      */   }
/*      */   
/*      */   CompletableFuture<ChunkResult<List<ChunkAccess>>> getChunkRangeFuture(ChunkHolder centerChunk, int range, IntFunction<ChunkStatus> distanceToStatus) {
/*  315 */     if (range == 0) {
/*  316 */       ChunkStatus status = distanceToStatus.apply(0);
/*  317 */       return centerChunk.scheduleChunkGenerationTask(status, this).thenApply(r -> r.map(List::of));
/*      */     } 
/*  320 */     int chunkCount = Mth.square(range * 2 + 1);
/*  321 */     List<CompletableFuture<ChunkResult<ChunkAccess>>> deps = new ArrayList<>(chunkCount);
/*  322 */     ChunkPos centerPos = centerChunk.getPos();
/*  323 */     for (int z = -range; z <= range; z++) {
/*  324 */       for (int x = -range; x <= range; x++) {
/*  325 */         int distance = Math.max(Math.abs(x), Math.abs(z));
/*  326 */         long chunkNode = ChunkPos.pack(centerPos.x() + x, centerPos.z() + z);
/*  327 */         ChunkHolder chunk = getUpdatingChunkIfPresent(chunkNode);
/*  328 */         if (chunk == null)
/*  329 */           return UNLOADED_CHUNK_LIST_FUTURE; 
/*  331 */         ChunkStatus depStatus = distanceToStatus.apply(distance);
/*  332 */         deps.add(chunk.scheduleChunkGenerationTask(depStatus, this));
/*      */       } 
/*      */     } 
/*  335 */     return Util.sequence(deps).thenApply(chunkResults -> {
/*      */           List<ChunkAccess> chunks = new ArrayList<>(chunkResults.size());
/*      */           for (ChunkResult<ChunkAccess> chunkResult : (Iterable<ChunkResult<ChunkAccess>>)chunkResults) {
/*      */             if (chunkResult == null)
/*      */               throw debugFuturesAndCreateReportedException(new IllegalStateException("At least one of the chunk futures were null"), "n/a"); 
/*      */             ChunkAccess chunk = chunkResult.orElse(null);
/*      */             if (chunk == null)
/*      */               return UNLOADED_CHUNK_LIST_RESULT; 
/*      */             chunks.add(chunk);
/*      */           } 
/*      */           return ChunkResult.of(chunks);
/*      */         });
/*      */   }
/*      */   
/*      */   public ReportedException debugFuturesAndCreateReportedException(IllegalStateException exception, String details) {
/*  352 */     StringBuilder sb = new StringBuilder();
/*  353 */     Consumer<ChunkHolder> addToDebug = holder -> holder.getAllFutures().forEach(());
/*  362 */     sb.append("Updating:").append(System.lineSeparator());
/*  363 */     this.updatingChunkMap.values().forEach(addToDebug);
/*  365 */     sb.append("Visible:").append(System.lineSeparator());
/*  366 */     this.visibleChunkMap.values().forEach(addToDebug);
/*  368 */     CrashReport report = CrashReport.forThrowable(exception, "Chunk loading");
/*  369 */     CrashReportCategory category = report.addCategory("Chunk loading");
/*  370 */     category.setDetail("Details", details);
/*  371 */     category.setDetail("Futures", sb);
/*  372 */     return new ReportedException(report);
/*      */   }
/*      */   
/*      */   public CompletableFuture<ChunkResult<LevelChunk>> prepareEntityTickingChunk(ChunkHolder chunk) {
/*  376 */     return getChunkRangeFuture(chunk, 2, distance -> ChunkStatus.FULL)
/*  377 */       .thenApply(chunkResult -> chunkResult.map(()));
/*      */   }
/*      */   
/*      */   private ChunkHolder updateChunkScheduling(long node, int level, ChunkHolder chunk, int oldLevel) {
/*  381 */     if (!ChunkLevel.isLoaded(oldLevel) && !ChunkLevel.isLoaded(level))
/*  382 */       return chunk; 
/*  385 */     if (chunk != null)
/*  386 */       chunk.setTicketLevel(level); 
/*  389 */     if (chunk != null)
/*  390 */       if (!ChunkLevel.isLoaded(level)) {
/*  391 */         this.toDrop.add(node);
/*      */       } else {
/*  393 */         this.toDrop.remove(node);
/*      */       }  
/*  397 */     if (ChunkLevel.isLoaded(level) && 
/*  398 */       chunk == null) {
/*  399 */       chunk = (ChunkHolder)this.pendingUnloads.remove(node);
/*  401 */       if (chunk != null) {
/*  402 */         chunk.setTicketLevel(level);
/*      */       } else {
/*  404 */         chunk = new ChunkHolder(ChunkPos.unpack(node), level, (LevelHeightAccessor)this.level, this.lightEngine, this::onLevelChange, this);
/*      */       } 
/*  406 */       this.updatingChunkMap.put(node, chunk);
/*  407 */       this.modified = true;
/*      */     } 
/*  410 */     return chunk;
/*      */   }
/*      */   
/*      */   private void onLevelChange(ChunkPos pos, IntSupplier oldLevel, int newLevel, IntConsumer setQueueLevel) {
/*  414 */     this.worldgenTaskDispatcher.onLevelChange(pos, oldLevel, newLevel, setQueueLevel);
/*  415 */     this.lightTaskDispatcher.onLevelChange(pos, oldLevel, newLevel, setQueueLevel);
/*      */   }
/*      */   
/*      */   public void close() throws IOException {
/*      */     try {
/*  421 */       this.worldgenTaskDispatcher.close();
/*  422 */       this.lightTaskDispatcher.close();
/*  423 */       this.poiManager.close();
/*      */     } finally {
/*  425 */       super.close();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void saveAllChunks(boolean flushStorage) {
/*  431 */     if (flushStorage) {
/*  435 */       List<ChunkHolder> chunksToSave = this.visibleChunkMap.values().stream().filter(ChunkHolder::wasAccessibleSinceLastSave).peek(ChunkHolder::refreshAccessibility).toList();
/*  438 */       MutableBoolean didWork = new MutableBoolean();
/*      */       do {
/*  440 */         didWork.setFalse();
/*  441 */         chunksToSave.stream()
/*  442 */           .map(chunk -> {
/*      */               Objects.requireNonNull(chunk);
/*      */               this.mainThreadExecutor.managedBlock(chunk::isReadyForSaving);
/*      */               return chunk.getLatestChunk();
/*  446 */             }).filter(chunkAccess -> (chunkAccess instanceof net.minecraft.world.level.chunk.ImposterProtoChunk || chunkAccess instanceof LevelChunk))
/*  447 */           .filter(this::save)
/*  448 */           .forEach(c -> didWork.setTrue());
/*  449 */       } while (didWork.isTrue());
/*  451 */       this.poiManager.flushAll();
/*  452 */       processUnloads(() -> true);
/*  453 */       synchronize(true).join();
/*      */     } else {
/*  456 */       this.nextChunkSaveTime.clear();
/*  457 */       long now = Util.getMillis();
/*  458 */       for (ObjectIterator<ChunkHolder> objectIterator = this.visibleChunkMap.values().iterator(); objectIterator.hasNext(); ) {
/*  458 */         ChunkHolder chunk = objectIterator.next();
/*  459 */         saveChunkIfNeeded(chunk, now);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void tick(BooleanSupplier haveTime) {
/*  465 */     ProfilerFiller profiler = Profiler.get();
/*  466 */     profiler.push("poi");
/*  467 */     this.poiManager.tick(haveTime);
/*  468 */     profiler.popPush("chunk_unload");
/*  469 */     if (!this.level.noSave())
/*  470 */       processUnloads(haveTime); 
/*  472 */     profiler.pop();
/*      */   }
/*      */   
/*      */   public boolean hasWork() {
/*  476 */     return (this.lightEngine.hasLightWork() || 
/*  477 */       !this.pendingUnloads.isEmpty() || 
/*  478 */       !this.updatingChunkMap.isEmpty() || this.poiManager
/*  479 */       .hasWork() || 
/*  480 */       !this.toDrop.isEmpty() || 
/*  481 */       !this.unloadQueue.isEmpty() || this.worldgenTaskDispatcher
/*  482 */       .hasWork() || this.lightTaskDispatcher
/*  483 */       .hasWork() || this.distanceManager
/*  484 */       .hasTickets());
/*      */   }
/*      */   
/*      */   private void processUnloads(BooleanSupplier haveTime) {
/*  488 */     LongIterator iterator = this.toDrop.iterator();
/*  489 */     while (iterator.hasNext()) {
/*  490 */       long pos = iterator.nextLong();
/*  491 */       ChunkHolder chunkHolder = (ChunkHolder)this.updatingChunkMap.get(pos);
/*  492 */       if (chunkHolder != null) {
/*  493 */         this.updatingChunkMap.remove(pos);
/*  494 */         this.pendingUnloads.put(pos, chunkHolder);
/*  495 */         this.modified = true;
/*  496 */         scheduleUnload(pos, chunkHolder);
/*      */       } 
/*  498 */       iterator.remove();
/*      */     } 
/*  503 */     int minimalNumberOfChunksToProcess = Math.max(0, this.unloadQueue.size() - 2000);
/*      */     Runnable unloadTask;
/*  504 */     while ((minimalNumberOfChunksToProcess > 0 || haveTime.getAsBoolean()) && (unloadTask = this.unloadQueue.poll()) != null) {
/*  505 */       minimalNumberOfChunksToProcess--;
/*  507 */       unloadTask.run();
/*      */     } 
/*  510 */     saveChunksEagerly(haveTime);
/*      */   }
/*      */   
/*      */   private void saveChunksEagerly(BooleanSupplier haveTime) {
/*  515 */     long now = Util.getMillis();
/*  516 */     int eagerlySavedCount = 0;
/*  517 */     LongIterator iterator = this.chunksToEagerlySave.iterator();
/*  518 */     while (eagerlySavedCount < 20 && this.activeChunkWrites
/*      */       
/*  520 */       .get() < 128 && haveTime
/*  521 */       .getAsBoolean() && iterator
/*  522 */       .hasNext()) {
/*  524 */       long chunkPos = iterator.nextLong();
/*  525 */       ChunkHolder chunkHolder = (ChunkHolder)this.visibleChunkMap.get(chunkPos);
/*  528 */       ChunkAccess latestChunk = (chunkHolder != null) ? chunkHolder.getLatestChunk() : null;
/*  529 */       if (latestChunk == null || !latestChunk.isUnsaved()) {
/*  530 */         iterator.remove();
/*      */         continue;
/*      */       } 
/*  534 */       if (saveChunkIfNeeded(chunkHolder, now)) {
/*  535 */         eagerlySavedCount++;
/*  536 */         iterator.remove();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void scheduleUnload(long pos, ChunkHolder chunkHolder) {
/*  542 */     CompletableFuture<?> saveSyncFuture = chunkHolder.getSaveSyncFuture();
/*  567 */     Objects.requireNonNull(this.unloadQueue);
/*      */     saveSyncFuture.thenRunAsync(() -> {
/*      */           CompletableFuture<?> currentFuture = chunkHolder.getSaveSyncFuture();
/*      */           if (currentFuture != saveSyncFuture) {
/*      */             scheduleUnload(pos, chunkHolder);
/*      */             return;
/*      */           } 
/*      */           ChunkAccess chunk = chunkHolder.getLatestChunk();
/*      */           if (this.pendingUnloads.remove(pos, chunkHolder) && chunk != null) {
/*      */             if (chunk instanceof LevelChunk) {
/*      */               LevelChunk levelChunk = (LevelChunk)chunk;
/*      */               levelChunk.setLoaded(false);
/*      */             } 
/*      */             save(chunk);
/*      */             if (chunk instanceof LevelChunk) {
/*      */               LevelChunk levelChunk = (LevelChunk)chunk;
/*      */               this.level.unload(levelChunk);
/*      */             } 
/*      */             this.lightEngine.updateChunkStatus(chunk.getPos());
/*      */             this.lightEngine.tryScheduleUpdate();
/*      */             this.nextChunkSaveTime.remove(chunk.getPos().pack());
/*      */           } 
/*  567 */         }this.unloadQueue::add).whenComplete((ignored, throwable) -> {
/*      */           if (throwable != null)
/*      */             LOGGER.error("Failed to save chunk {}", chunkHolder.getPos(), throwable); 
/*      */         });
/*      */   }
/*      */   
/*      */   protected boolean promoteChunkMap() {
/*  575 */     if (!this.modified)
/*  576 */       return false; 
/*  579 */     this.visibleChunkMap = this.updatingChunkMap.clone();
/*  580 */     this.modified = false;
/*  581 */     return true;
/*      */   }
/*      */   
/*      */   private CompletableFuture<ChunkAccess> scheduleChunkLoad(ChunkPos pos) {
/*  589 */     CompletableFuture<Optional<SerializableChunkData>> chunkDataFuture = readChunk(pos).thenApplyAsync(chunkData -> chunkData.map(()), 
/*      */         
/*  595 */         Util.backgroundExecutor().forName("parseChunk"));
/*  596 */     CompletableFuture<?> poiFuture = this.poiManager.prefetch(pos);
/*  598 */     return chunkDataFuture.thenCombine(poiFuture, (chunkData, ignored) -> chunkData)
/*  599 */       .thenApplyAsync(chunkData -> {
/*      */           Profiler.get().incrementCounter("chunkLoad");
/*      */           if (chunkData.isPresent()) {
/*      */             ProtoChunk protoChunk = ((SerializableChunkData)chunkData.get()).read(this.level, this.poiManager, storageInfo(), pos);
/*      */             markPosition(pos, protoChunk.getPersistedStatus().getChunkType());
/*      */             return (ChunkAccess)protoChunk;
/*      */           } 
/*      */           return createEmptyChunk(pos);
/*  608 */         }(Executor)this.mainThreadExecutor).exceptionallyAsync(throwable -> handleChunkLoadFailure(throwable, pos), (Executor)this.mainThreadExecutor);
/*      */   }
/*      */   
/*      */   private ChunkAccess handleChunkLoadFailure(Throwable throwable, ChunkPos pos) {
/*  612 */     CompletionException e = (CompletionException)throwable;
/*  612 */     Throwable unwrapped = (throwable instanceof CompletionException) ? e.getCause() : throwable;
/*  613 */     ReportedException reportedException = (ReportedException)unwrapped;
/*  613 */     Throwable cause = (unwrapped instanceof ReportedException) ? reportedException.getCause() : unwrapped;
/*  614 */     boolean alwaysThrow = cause instanceof Error;
/*  615 */     boolean ioException = (cause instanceof IOException || cause instanceof net.minecraft.nbt.NbtException);
/*  617 */     if (!alwaysThrow) {
/*  617 */       if (!ioException);
/*      */     } else {
/*  618 */       CrashReport report = CrashReport.forThrowable(throwable, "Exception loading chunk");
/*  619 */       CrashReportCategory chunkBeingLoaded = report.addCategory("Chunk being loaded");
/*  620 */       chunkBeingLoaded.setDetail("pos", pos);
/*  621 */       markPositionReplaceable(pos);
/*  622 */       throw new ReportedException(report);
/*      */     } 
/*  624 */     this.level.getServer().reportChunkLoadFailure(cause, storageInfo(), pos);
/*  625 */     return createEmptyChunk(pos);
/*      */   }
/*      */   
/*      */   private ChunkAccess createEmptyChunk(ChunkPos pos) {
/*  629 */     markPositionReplaceable(pos);
/*  630 */     return (ChunkAccess)new ProtoChunk(pos, UpgradeData.EMPTY, (LevelHeightAccessor)this.level, this.level.palettedContainerFactory(), null);
/*      */   }
/*      */   
/*      */   private void markPositionReplaceable(ChunkPos pos) {
/*  634 */     this.chunkTypeCache.put(pos.pack(), (byte)-1);
/*      */   }
/*      */   
/*      */   private byte markPosition(ChunkPos pos, ChunkType type) {
/*  638 */     return this.chunkTypeCache.put(pos.pack(), (type == ChunkType.PROTOCHUNK) ? -1 : 1);
/*      */   }
/*      */   
/*      */   public GenerationChunkHolder acquireGeneration(long chunkNode) {
/*  646 */     ChunkHolder chunkHolder = (ChunkHolder)this.updatingChunkMap.get(chunkNode);
/*  647 */     chunkHolder.increaseGenerationRefCount();
/*  648 */     return chunkHolder;
/*      */   }
/*      */   
/*      */   public void releaseGeneration(GenerationChunkHolder chunkHolder) {
/*  654 */     chunkHolder.decreaseGenerationRefCount();
/*      */   }
/*      */   
/*      */   public CompletableFuture<ChunkAccess> applyStep(GenerationChunkHolder chunkHolder, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache) {
/*  660 */     ChunkPos pos = chunkHolder.getPos();
/*  661 */     if (step.targetStatus() == ChunkStatus.EMPTY)
/*  662 */       return scheduleChunkLoad(pos); 
/*      */     try {
/*  666 */       GenerationChunkHolder holder = (GenerationChunkHolder)cache.get(pos.x(), pos.z());
/*  667 */       ChunkAccess centerChunk = holder.getChunkIfPresentUnchecked(step.targetStatus().getParent());
/*  668 */       if (centerChunk == null)
/*  669 */         throw new IllegalStateException("Parent chunk missing"); 
/*  671 */       return step.apply(this.worldGenContext, cache, centerChunk);
/*  672 */     } catch (Exception e) {
/*  673 */       e.getStackTrace();
/*  674 */       CrashReport report = CrashReport.forThrowable(e, "Exception generating new chunk");
/*  675 */       CrashReportCategory category = report.addCategory("Chunk to be generated");
/*  677 */       category.setDetail("Status being generated", () -> step.targetStatus().getName());
/*  678 */       category.setDetail("Location", String.format(Locale.ROOT, "%d,%d", new Object[] { Integer.valueOf(pos.x()), Integer.valueOf(pos.z()) }));
/*  679 */       category.setDetail("Position hash", Long.valueOf(ChunkPos.pack(pos.x(), pos.z())));
/*  680 */       category.setDetail("Generator", generator());
/*  682 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */   
/*      */   public ChunkGenerationTask scheduleGenerationTask(ChunkStatus targetStatus, ChunkPos pos) {
/*  689 */     ChunkGenerationTask task = ChunkGenerationTask.create(this, targetStatus, pos);
/*  690 */     this.pendingGenerationTasks.add(task);
/*  691 */     return task;
/*      */   }
/*      */   
/*      */   private void runGenerationTask(ChunkGenerationTask task) {
/*  695 */     GenerationChunkHolder chunk = task.getCenter();
/*  702 */     Objects.requireNonNull(chunk);
/*      */     this.worldgenTaskDispatcher.submit(() -> {
/*      */           CompletableFuture<?> future = task.runUntilWait();
/*      */           if (future == null)
/*      */             return; 
/*      */           future.thenRun(());
/*  702 */         }chunk.getPos().pack(), chunk::getQueueLevel);
/*      */   }
/*      */   
/*      */   public void runGenerationTasks() {
/*  708 */     this.pendingGenerationTasks.forEach(this::runGenerationTask);
/*  709 */     this.pendingGenerationTasks.clear();
/*      */   }
/*      */   
/*      */   public CompletableFuture<ChunkResult<LevelChunk>> prepareTickingChunk(ChunkHolder chunk) {
/*  713 */     CompletableFuture<ChunkResult<List<ChunkAccess>>> future = getChunkRangeFuture(chunk, 1, distance -> ChunkStatus.FULL);
/*  715 */     return future.thenApplyAsync(listResult -> listResult.map(()), (Executor)this.mainThreadExecutor);
/*      */   }
/*      */   
/*      */   private void onChunkReadyToSend(ChunkHolder chunkHolder, LevelChunk chunk) {
/*  730 */     ChunkPos chunkPos = chunk.getPos();
/*  731 */     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
/*  732 */       if (player.getChunkTrackingView().contains(chunkPos))
/*  733 */         markChunkPendingToSend(player, chunk); 
/*      */     } 
/*  736 */     this.level.getChunkSource().onChunkReadyToSend(chunkHolder);
/*  737 */     this.level.debugSynchronizers().registerChunk(chunk);
/*      */   }
/*      */   
/*      */   public CompletableFuture<ChunkResult<LevelChunk>> prepareAccessibleChunk(ChunkHolder chunk) {
/*  741 */     return getChunkRangeFuture(chunk, 1, ChunkLevel::getStatusAroundFullChunk)
/*  742 */       .thenApply(chunkResult -> chunkResult.map(()));
/*      */   }
/*      */   
/*      */   Stream<ChunkHolder> allChunksWithAtLeastStatus(ChunkStatus status) {
/*  747 */     int level = ChunkLevel.byStatus(status);
/*  748 */     return this.visibleChunkMap.values().stream().filter(chunk -> (chunk.getTicketLevel() <= level));
/*      */   }
/*      */   
/*      */   private boolean saveChunkIfNeeded(ChunkHolder chunk, long now) {
/*  752 */     if (!chunk.wasAccessibleSinceLastSave() || !chunk.isReadyForSaving())
/*  753 */       return false; 
/*  756 */     ChunkAccess chunkAccess = chunk.getLatestChunk();
/*  757 */     if (chunkAccess instanceof net.minecraft.world.level.chunk.ImposterProtoChunk || chunkAccess instanceof LevelChunk) {
/*  758 */       if (!chunkAccess.isUnsaved())
/*  759 */         return false; 
/*  761 */       long chunkPos = chunkAccess.getPos().pack();
/*  762 */       long nextSaveTime = this.nextChunkSaveTime.getOrDefault(chunkPos, -1L);
/*  763 */       if (now < nextSaveTime)
/*  764 */         return false; 
/*  766 */       boolean saved = save(chunkAccess);
/*  767 */       chunk.refreshAccessibility();
/*  768 */       if (saved)
/*  769 */         this.nextChunkSaveTime.put(chunkPos, now + 10000L); 
/*  771 */       return saved;
/*      */     } 
/*  773 */     return false;
/*      */   }
/*      */   
/*      */   private boolean save(ChunkAccess chunk) {
/*  777 */     this.poiManager.flush(chunk.getPos());
/*  779 */     if (!chunk.tryMarkSaved())
/*  780 */       return false; 
/*  783 */     ChunkPos pos = chunk.getPos();
/*      */     try {
/*  785 */       ChunkStatus status = chunk.getPersistedStatus();
/*  787 */       if (status.getChunkType() != ChunkType.LEVELCHUNK) {
/*  788 */         if (isExistingChunkFull(pos))
/*  790 */           return false; 
/*  794 */         if (status == ChunkStatus.EMPTY && chunk.getAllStarts().values().stream().noneMatch(StructureStart::isValid))
/*  795 */           return false; 
/*      */       } 
/*  799 */       Profiler.get().incrementCounter("chunkSave");
/*  800 */       this.activeChunkWrites.incrementAndGet();
/*  801 */       SerializableChunkData data = SerializableChunkData.copyOf(this.level, chunk);
/*  802 */       Objects.requireNonNull(data);
/*  802 */       CompletableFuture<CompoundTag> encodedData = CompletableFuture.supplyAsync(data::write, (Executor)Util.backgroundExecutor());
/*  803 */       Objects.requireNonNull(encodedData);
/*  803 */       write(pos, encodedData::join).handle((ignored, throwable) -> {
/*      */             if (throwable != null)
/*      */               this.level.getServer().reportChunkSaveFailure(throwable, storageInfo(), pos); 
/*      */             this.activeChunkWrites.decrementAndGet();
/*      */             return null;
/*      */           });
/*  810 */       markPosition(pos, status.getChunkType());
/*  811 */       return true;
/*  812 */     } catch (Exception e) {
/*  813 */       this.level.getServer().reportChunkSaveFailure(e, storageInfo(), pos);
/*  815 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isExistingChunkFull(ChunkPos pos) {
/*      */     CompoundTag currentTag;
/*  819 */     byte cachedChunkType = this.chunkTypeCache.get(pos.pack());
/*  820 */     if (cachedChunkType != 0)
/*  821 */       return (cachedChunkType == 1); 
/*      */     try {
/*  826 */       currentTag = ((Optional<CompoundTag>)readChunk(pos).join()).orElse(null);
/*  827 */       if (currentTag == null) {
/*  828 */         markPositionReplaceable(pos);
/*  829 */         return false;
/*      */       } 
/*  831 */     } catch (Exception e) {
/*  832 */       LOGGER.error("Failed to read chunk {}", pos, e);
/*  833 */       markPositionReplaceable(pos);
/*  834 */       return false;
/*      */     } 
/*  837 */     ChunkType chunkType = SerializableChunkData.getChunkStatusFromTag(currentTag).getChunkType();
/*  838 */     return (markPosition(pos, chunkType) == 1);
/*      */   }
/*      */   
/*      */   protected void setServerViewDistance(int newViewDistance) {
/*  842 */     int actualNewDistance = Mth.clamp(newViewDistance, 2, 32);
/*  843 */     if (actualNewDistance != this.serverViewDistance) {
/*  844 */       this.serverViewDistance = actualNewDistance;
/*  845 */       this.distanceManager.updatePlayerTickets(this.serverViewDistance);
/*  846 */       for (ServerPlayer player : this.playerMap.getAllPlayers())
/*  847 */         updateChunkTracking(player); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private int getPlayerViewDistance(ServerPlayer player) {
/*  853 */     return Mth.clamp(player.requestedViewDistance(), 2, this.serverViewDistance);
/*      */   }
/*      */   
/*      */   private void markChunkPendingToSend(ServerPlayer player, ChunkPos pos) {
/*  857 */     LevelChunk chunk = getChunkToSend(pos.pack());
/*  858 */     if (chunk != null)
/*  859 */       markChunkPendingToSend(player, chunk); 
/*      */   }
/*      */   
/*      */   private static void markChunkPendingToSend(ServerPlayer player, LevelChunk chunk) {
/*  864 */     player.connection.chunkSender.markChunkPendingToSend(chunk);
/*      */   }
/*      */   
/*      */   private static void dropChunk(ServerPlayer player, ChunkPos pos) {
/*  868 */     player.connection.chunkSender.dropChunk(player, pos);
/*      */   }
/*      */   
/*      */   public LevelChunk getChunkToSend(long key) {
/*  872 */     ChunkHolder chunkHolder = getVisibleChunkIfPresent(key);
/*  873 */     if (chunkHolder == null)
/*  874 */       return null; 
/*  876 */     return chunkHolder.getChunkToSend();
/*      */   }
/*      */   
/*      */   public int size() {
/*  880 */     return this.visibleChunkMap.size();
/*      */   }
/*      */   
/*      */   public DistanceManager getDistanceManager() {
/*  884 */     return this.distanceManager;
/*      */   }
/*      */   
/*      */   void dumpChunks(Writer output) throws IOException {
/*  906 */     CsvOutput csvOutput = CsvOutput.builder().addColumn("x").addColumn("z").addColumn("level").addColumn("in_memory").addColumn("status").addColumn("full_status").addColumn("accessible_ready").addColumn("ticking_ready").addColumn("entity_ticking_ready").addColumn("ticket").addColumn("spawning").addColumn("block_entity_count").addColumn("ticking_ticket").addColumn("ticking_level").addColumn("block_ticks").addColumn("fluid_ticks").build(output);
/*  908 */     for (ObjectBidirectionalIterator<Long2ObjectMap.Entry<ChunkHolder>> objectBidirectionalIterator = this.visibleChunkMap.long2ObjectEntrySet().iterator(); objectBidirectionalIterator.hasNext(); ) {
/*  908 */       Long2ObjectMap.Entry<ChunkHolder> entry = objectBidirectionalIterator.next();
/*  909 */       long posKey = entry.getLongKey();
/*  910 */       ChunkPos pos = ChunkPos.unpack(posKey);
/*  911 */       ChunkHolder holder = (ChunkHolder)entry.getValue();
/*  912 */       Optional<ChunkAccess> chunk = Optional.ofNullable(holder.getLatestChunk());
/*  913 */       Optional<LevelChunk> fullChunk = chunk.flatMap(chunkAccess -> {
/*      */             LevelChunk levelChunk = (LevelChunk)chunkAccess;
/*      */             return (chunkAccess instanceof LevelChunk) ? Optional.<LevelChunk>of(levelChunk) : Optional.empty();
/*      */           });
/*  914 */       csvOutput.writeRow(new Object[] { 
/*  915 */             Integer.valueOf(pos.x()), 
/*  916 */             Integer.valueOf(pos.z()), 
/*  917 */             Integer.valueOf(holder.getTicketLevel()), 
/*  918 */             Boolean.valueOf(chunk.isPresent()), chunk
/*  919 */             .map(ChunkAccess::getPersistedStatus).orElse(null), fullChunk
/*  920 */             .map(LevelChunk::getFullStatus).orElse(null), 
/*  921 */             printFuture(holder.getFullChunkFuture()), 
/*  922 */             printFuture(holder.getTickingChunkFuture()), 
/*  923 */             printFuture(holder.getEntityTickingChunkFuture()), this.ticketStorage
/*  924 */             .getTicketDebugString(posKey, false), 
/*  925 */             Boolean.valueOf(anyPlayerCloseEnoughForSpawning(pos)), fullChunk
/*  926 */             .<Integer>map(c -> Integer.valueOf(c.getBlockEntities().size())).orElse(Integer.valueOf(0)), this.ticketStorage
/*  927 */             .getTicketDebugString(posKey, true), 
/*  928 */             Integer.valueOf(this.distanceManager.getChunkLevel(posKey, true)), fullChunk
/*  929 */             .<Integer>map(levelChunk -> Integer.valueOf(levelChunk.getBlockTicks().count())).orElse(Integer.valueOf(0)), fullChunk
/*  930 */             .<Integer>map(levelChunk -> Integer.valueOf(levelChunk.getFluidTicks().count())).orElse(Integer.valueOf(0)) });
/*      */     } 
/*      */   }
/*      */   
/*      */   private static String printFuture(CompletableFuture<ChunkResult<LevelChunk>> future) {
/*      */     try {
/*  937 */       ChunkResult<LevelChunk> result = future.getNow(null);
/*  938 */       if (result != null)
/*  939 */         return result.isSuccess() ? "done" : "unloaded"; 
/*  941 */       return "not completed";
/*  943 */     } catch (CompletionException e) {
/*  944 */       return "failed " + e.getCause().getMessage();
/*  945 */     } catch (CancellationException e) {
/*  946 */       return "cancelled";
/*      */     } 
/*      */   }
/*      */   
/*      */   private CompletableFuture<Optional<CompoundTag>> readChunk(ChunkPos pos) {
/*  951 */     return read(pos).thenApplyAsync(chunkTag -> chunkTag.map(this::upgradeChunkTag), Util.backgroundExecutor().forName("upgradeChunk"));
/*      */   }
/*      */   
/*      */   private CompoundTag upgradeChunkTag(CompoundTag tag) {
/*  955 */     return upgradeChunkTag(tag, -1, getChunkDataFixContextTag(this.level.dimension(), generator().getTypeNameForDataFixer()), SharedConstants.getCurrentVersion().dataVersion().version());
/*      */   }
/*      */   
/*      */   public static CompoundTag getChunkDataFixContextTag(ResourceKey<Level> dimension, Optional<Identifier> generatorIdentifier) {
/*  959 */     CompoundTag contextTag = new CompoundTag();
/*  960 */     contextTag.putString("dimension", dimension.identifier().toString());
/*  961 */     generatorIdentifier.ifPresent(identifier -> contextTag.putString("generator", identifier.toString()));
/*  962 */     return contextTag;
/*      */   }
/*      */   
/*      */   public void collectSpawningChunks(List<LevelChunk> output) {
/*  966 */     LongIterator spawnCandidateChunks = this.distanceManager.getSpawnCandidateChunks();
/*  967 */     while (spawnCandidateChunks.hasNext()) {
/*  968 */       ChunkHolder holder = (ChunkHolder)this.visibleChunkMap.get(spawnCandidateChunks.nextLong());
/*  969 */       if (holder == null)
/*      */         continue; 
/*  972 */       LevelChunk chunk = holder.getTickingChunk();
/*  973 */       if (chunk == null)
/*      */         continue; 
/*  976 */       if (anyPlayerCloseEnoughForSpawningInternal(holder.getPos()))
/*  977 */         output.add(chunk); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void forEachBlockTickingChunk(Consumer<LevelChunk> tickingChunkConsumer) {
/*  983 */     this.distanceManager.forEachEntityTickingChunk(chunkPos -> {
/*      */           ChunkHolder holder = (ChunkHolder)this.visibleChunkMap.get(chunkPos);
/*      */           if (holder == null)
/*      */             return; 
/*      */           LevelChunk chunk = holder.getTickingChunk();
/*      */           if (chunk == null)
/*      */             return; 
/*      */           tickingChunkConsumer.accept(chunk);
/*      */         });
/*      */   }
/*      */   
/*      */   public boolean anyPlayerCloseEnoughForSpawning(ChunkPos pos) {
/*  997 */     TriState triState = this.distanceManager.hasPlayersNearby(pos.pack());
/*  998 */     if (triState == TriState.DEFAULT)
/*  999 */       return anyPlayerCloseEnoughForSpawningInternal(pos); 
/* 1001 */     return triState.toBoolean(true);
/*      */   }
/*      */   
/*      */   public boolean anyPlayerCloseEnoughTo(BlockPos pos, int maxDistance) {
/* 1005 */     Vec3 target = new Vec3((Vec3i)pos);
/* 1006 */     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
/* 1007 */       if (playerIsCloseEnoughTo(player, target, maxDistance))
/* 1008 */         return true; 
/*      */     } 
/* 1011 */     return false;
/*      */   }
/*      */   
/*      */   private boolean anyPlayerCloseEnoughForSpawningInternal(ChunkPos pos) {
/* 1015 */     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
/* 1016 */       if (playerIsCloseEnoughForSpawning(player, pos))
/* 1017 */         return true; 
/*      */     } 
/* 1020 */     return false;
/*      */   }
/*      */   
/*      */   public List<ServerPlayer> getPlayersCloseForSpawning(ChunkPos pos) {
/* 1024 */     long key = pos.pack();
/* 1027 */     if (!this.distanceManager.hasPlayersNearby(key).toBoolean(true))
/* 1028 */       return List.of(); 
/* 1031 */     ImmutableList.Builder<ServerPlayer> builder = ImmutableList.builder();
/* 1032 */     for (ServerPlayer player : this.playerMap.getAllPlayers()) {
/* 1033 */       if (playerIsCloseEnoughForSpawning(player, pos))
/* 1034 */         builder.add(player); 
/*      */     } 
/* 1037 */     return (List<ServerPlayer>)builder.build();
/*      */   }
/*      */   
/*      */   private boolean playerIsCloseEnoughForSpawning(ServerPlayer player, ChunkPos pos) {
/* 1041 */     if (player.isSpectator())
/* 1042 */       return false; 
/* 1044 */     double distanceToChunk = euclideanDistanceSquared(pos, player.position());
/* 1045 */     return (distanceToChunk < 16384.0D);
/*      */   }
/*      */   
/*      */   private boolean playerIsCloseEnoughTo(ServerPlayer player, Vec3 pos, int maxDistance) {
/* 1049 */     if (player.isSpectator())
/* 1050 */       return false; 
/* 1052 */     double distanceToPos = player.position().distanceTo(pos);
/* 1053 */     return (distanceToPos < maxDistance);
/*      */   }
/*      */   
/*      */   private static double euclideanDistanceSquared(ChunkPos chunkPos, Vec3 pos) {
/* 1057 */     double xPos = SectionPos.sectionToBlockCoord(chunkPos.x(), 8);
/* 1058 */     double zPos = SectionPos.sectionToBlockCoord(chunkPos.z(), 8);
/* 1060 */     double xd = xPos - pos.x;
/* 1061 */     double zd = zPos - pos.z;
/* 1063 */     return xd * xd + zd * zd;
/*      */   }
/*      */   
/*      */   private boolean skipPlayer(ServerPlayer player) {
/* 1067 */     return (player.isSpectator() && !((Boolean)this.level.getGameRules().get(GameRules.SPECTATORS_GENERATE_CHUNKS)).booleanValue());
/*      */   }
/*      */   
/*      */   private void updatePlayerStatus(ServerPlayer player, boolean added) {
/* 1071 */     boolean ignored = skipPlayer(player);
/* 1072 */     boolean wasIgnored = this.playerMap.ignoredOrUnknown(player);
/* 1073 */     if (added) {
/* 1074 */       this.playerMap.addPlayer(player, ignored);
/* 1075 */       updatePlayerPos(player);
/* 1077 */       if (!ignored)
/* 1078 */         this.distanceManager.addPlayer(SectionPos.of((EntityAccess)player), player); 
/* 1080 */       player.setChunkTrackingView(ChunkTrackingView.EMPTY);
/* 1081 */       updateChunkTracking(player);
/*      */     } else {
/* 1083 */       SectionPos lastPos = player.getLastSectionPos();
/* 1084 */       this.playerMap.removePlayer(player);
/* 1085 */       if (!wasIgnored)
/* 1086 */         this.distanceManager.removePlayer(lastPos, player); 
/* 1088 */       applyChunkTrackingView(player, ChunkTrackingView.EMPTY);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updatePlayerPos(ServerPlayer player) {
/* 1093 */     SectionPos pos = SectionPos.of((EntityAccess)player);
/* 1094 */     player.setLastSectionPos(pos);
/*      */   }
/*      */   
/*      */   public void move(ServerPlayer player) {
/* 1098 */     for (ObjectIterator<TrackedEntity> objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) {
/* 1098 */       TrackedEntity trackedEntity = objectIterator.next();
/* 1099 */       if (trackedEntity.entity == player) {
/* 1100 */         trackedEntity.updatePlayers(this.level.players());
/*      */         continue;
/*      */       } 
/* 1102 */       trackedEntity.updatePlayer(player);
/*      */     } 
/* 1106 */     SectionPos oldSection = player.getLastSectionPos();
/* 1107 */     SectionPos newSection = SectionPos.of((EntityAccess)player);
/* 1109 */     boolean wasIgnored = this.playerMap.ignored(player);
/* 1110 */     boolean ignored = skipPlayer(player);
/* 1111 */     boolean positionChanged = (oldSection.asLong() != newSection.asLong());
/* 1112 */     if (positionChanged || wasIgnored != ignored) {
/* 1113 */       updatePlayerPos(player);
/* 1115 */       if (!wasIgnored)
/* 1116 */         this.distanceManager.removePlayer(oldSection, player); 
/* 1119 */       if (!ignored)
/* 1120 */         this.distanceManager.addPlayer(newSection, player); 
/* 1123 */       if (!wasIgnored && ignored)
/* 1124 */         this.playerMap.ignorePlayer(player); 
/* 1127 */       if (wasIgnored && !ignored)
/* 1128 */         this.playerMap.unIgnorePlayer(player); 
/* 1131 */       updateChunkTracking(player);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateChunkTracking(ServerPlayer player) {
/* 1136 */     ChunkPos chunkPos = player.chunkPosition();
/* 1137 */     int playerViewDistance = getPlayerViewDistance(player);
/* 1138 */     ChunkTrackingView chunkTrackingView = player.getChunkTrackingView();
/* 1138 */     if (chunkTrackingView instanceof ChunkTrackingView.Positioned) {
/* 1138 */       ChunkTrackingView.Positioned view = (ChunkTrackingView.Positioned)chunkTrackingView;
/* 1138 */       if (view.center().equals(chunkPos) && view.viewDistance() == playerViewDistance)
/*      */         return; 
/*      */     } 
/* 1141 */     applyChunkTrackingView(player, ChunkTrackingView.of(chunkPos, playerViewDistance));
/*      */   }
/*      */   
/*      */   private void applyChunkTrackingView(ServerPlayer player, ChunkTrackingView next) {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: invokevirtual level : ()Lnet/minecraft/server/level/ServerLevel;
/*      */     //   4: aload_0
/*      */     //   5: getfield level : Lnet/minecraft/server/level/ServerLevel;
/*      */     //   8: if_acmpeq -> 12
/*      */     //   11: return
/*      */     //   12: aload_1
/*      */     //   13: invokevirtual getChunkTrackingView : ()Lnet/minecraft/server/level/ChunkTrackingView;
/*      */     //   16: astore_3
/*      */     //   17: aload_2
/*      */     //   18: instanceof net/minecraft/server/level/ChunkTrackingView$Positioned
/*      */     //   21: ifeq -> 89
/*      */     //   24: aload_2
/*      */     //   25: checkcast net/minecraft/server/level/ChunkTrackingView$Positioned
/*      */     //   28: astore #4
/*      */     //   30: aload_3
/*      */     //   31: instanceof net/minecraft/server/level/ChunkTrackingView$Positioned
/*      */     //   34: ifeq -> 59
/*      */     //   37: aload_3
/*      */     //   38: checkcast net/minecraft/server/level/ChunkTrackingView$Positioned
/*      */     //   41: astore #5
/*      */     //   43: aload #5
/*      */     //   45: invokevirtual center : ()Lnet/minecraft/world/level/ChunkPos;
/*      */     //   48: aload #4
/*      */     //   50: invokevirtual center : ()Lnet/minecraft/world/level/ChunkPos;
/*      */     //   53: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */     //   56: ifne -> 89
/*      */     //   59: aload_1
/*      */     //   60: getfield connection : Lnet/minecraft/server/network/ServerGamePacketListenerImpl;
/*      */     //   63: new net/minecraft/network/protocol/game/ClientboundSetChunkCacheCenterPacket
/*      */     //   66: dup
/*      */     //   67: aload #4
/*      */     //   69: invokevirtual center : ()Lnet/minecraft/world/level/ChunkPos;
/*      */     //   72: invokevirtual x : ()I
/*      */     //   75: aload #4
/*      */     //   77: invokevirtual center : ()Lnet/minecraft/world/level/ChunkPos;
/*      */     //   80: invokevirtual z : ()I
/*      */     //   83: invokespecial <init> : (II)V
/*      */     //   86: invokevirtual send : (Lnet/minecraft/network/protocol/Packet;)V
/*      */     //   89: aload_3
/*      */     //   90: aload_2
/*      */     //   91: aload_0
/*      */     //   92: aload_1
/*      */     //   93: <illegal opcode> accept : (Lnet/minecraft/server/level/ChunkMap;Lnet/minecraft/server/level/ServerPlayer;)Ljava/util/function/Consumer;
/*      */     //   98: aload_1
/*      */     //   99: <illegal opcode> accept : (Lnet/minecraft/server/level/ServerPlayer;)Ljava/util/function/Consumer;
/*      */     //   104: invokestatic difference : (Lnet/minecraft/server/level/ChunkTrackingView;Lnet/minecraft/server/level/ChunkTrackingView;Ljava/util/function/Consumer;Ljava/util/function/Consumer;)V
/*      */     //   107: aload_1
/*      */     //   108: aload_2
/*      */     //   109: invokevirtual setChunkTrackingView : (Lnet/minecraft/server/level/ChunkTrackingView;)V
/*      */     //   112: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #1148	-> 0
/*      */     //   #1149	-> 11
/*      */     //   #1151	-> 12
/*      */     //   #1152	-> 17
/*      */     //   #1153	-> 59
/*      */     //   #1155	-> 89
/*      */     //   #1160	-> 107
/*      */     //   #1161	-> 112
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   43	16	5	from	Lnet/minecraft/server/level/ChunkTrackingView$Positioned;
/*      */     //   30	59	4	to	Lnet/minecraft/server/level/ChunkTrackingView$Positioned;
/*      */     //   0	113	0	this	Lnet/minecraft/server/level/ChunkMap;
/*      */     //   0	113	1	player	Lnet/minecraft/server/level/ServerPlayer;
/*      */     //   0	113	2	next	Lnet/minecraft/server/level/ChunkTrackingView;
/*      */     //   17	96	3	previous	Lnet/minecraft/server/level/ChunkTrackingView;
/*      */   }
/*      */   
/*      */   public List<ServerPlayer> getPlayers(ChunkPos pos, boolean borderOnly) {
/* 1165 */     Set<ServerPlayer> allPlayers = this.playerMap.getAllPlayers();
/* 1167 */     ImmutableList.Builder<ServerPlayer> result = ImmutableList.builder();
/* 1168 */     for (ServerPlayer player : allPlayers) {
/* 1169 */       if ((borderOnly && isChunkOnTrackedBorder(player, pos.x(), pos.z())) || (!borderOnly && 
/* 1170 */         isChunkTracked(player, pos.x(), pos.z())))
/* 1171 */         result.add(player); 
/*      */     } 
/* 1174 */     return (List<ServerPlayer>)result.build();
/*      */   }
/*      */   
/*      */   public boolean hasEntityWithId(int id) {
/* 1178 */     return this.entityMap.containsKey(id);
/*      */   }
/*      */   
/*      */   protected void addEntity(Entity entity) {
/* 1183 */     if (entity instanceof net.minecraft.world.entity.boss.enderdragon.EnderDragonPart)
/*      */       return; 
/* 1186 */     EntityType<?> type = entity.getType();
/* 1187 */     int range = type.clientTrackingRange() * 16;
/* 1188 */     if (range == 0)
/*      */       return; 
/* 1191 */     int updateInterval = type.updateInterval();
/* 1192 */     if (this.entityMap.containsKey(entity.getId()))
/* 1193 */       throw (IllegalStateException)Util.pauseInIde(new IllegalStateException("Entity is already tracked!")); 
/* 1195 */     TrackedEntity trackedEntity = new TrackedEntity(entity, range, updateInterval, type.trackDeltas());
/* 1196 */     this.entityMap.put(entity.getId(), trackedEntity);
/* 1197 */     trackedEntity.updatePlayers(this.level.players());
/* 1199 */     if (entity instanceof ServerPlayer) {
/* 1199 */       ServerPlayer player = (ServerPlayer)entity;
/* 1200 */       updatePlayerStatus(player, true);
/* 1201 */       for (ObjectIterator<TrackedEntity> objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) {
/* 1201 */         TrackedEntity e = objectIterator.next();
/* 1202 */         if (e.entity != player)
/* 1203 */           e.updatePlayer(player); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void removeEntity(Entity entity) {
/* 1210 */     if (entity instanceof ServerPlayer) {
/* 1210 */       ServerPlayer player = (ServerPlayer)entity;
/* 1211 */       updatePlayerStatus(player, false);
/* 1212 */       for (ObjectIterator<TrackedEntity> objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) {
/* 1212 */         TrackedEntity trackedEntity1 = objectIterator.next();
/* 1213 */         trackedEntity1.removePlayer(player);
/*      */       } 
/*      */     } 
/* 1216 */     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.remove(entity.getId());
/* 1217 */     if (trackedEntity != null)
/* 1218 */       trackedEntity.broadcastRemoved(); 
/*      */   }
/*      */   
/*      */   protected void tick() {
/* 1223 */     for (ServerPlayer player : this.playerMap.getAllPlayers())
/* 1224 */       updateChunkTracking(player); 
/* 1228 */     List<ServerPlayer> movedPlayers = Lists.newArrayList();
/* 1229 */     List<ServerPlayer> players = this.level.players();
/*      */     ObjectIterator<TrackedEntity> objectIterator;
/* 1231 */     for (objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) {
/* 1231 */       TrackedEntity trackedEntity = objectIterator.next();
/* 1232 */       SectionPos oldPos = trackedEntity.lastSectionPos;
/* 1233 */       SectionPos newPos = SectionPos.of((EntityAccess)trackedEntity.entity);
/* 1234 */       boolean sectionPosChanged = !Objects.equals(oldPos, newPos);
/* 1235 */       if (sectionPosChanged) {
/* 1236 */         trackedEntity.updatePlayers(players);
/* 1237 */         Entity entity = trackedEntity.entity;
/* 1238 */         if (entity instanceof ServerPlayer) {
/* 1238 */           ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 1239 */           movedPlayers.add(serverPlayer);
/*      */         } 
/* 1241 */         trackedEntity.lastSectionPos = newPos;
/*      */       } 
/* 1243 */       if (sectionPosChanged || trackedEntity.entity.needsSync || this.distanceManager.inEntityTickingRange(newPos.chunk().pack()))
/* 1244 */         trackedEntity.serverEntity.sendChanges(); 
/*      */     } 
/* 1248 */     if (!movedPlayers.isEmpty())
/* 1249 */       for (objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) {
/* 1249 */         TrackedEntity trackedEntity = objectIterator.next();
/* 1250 */         trackedEntity.updatePlayers(movedPlayers);
/*      */       }  
/*      */   }
/*      */   
/*      */   public void sendToTrackingPlayers(Entity entity, Packet<? super ClientGamePacketListener> packet) {
/* 1256 */     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
/* 1257 */     if (trackedEntity != null)
/* 1258 */       trackedEntity.sendToTrackingPlayers(packet); 
/*      */   }
/*      */   
/*      */   public void sendToTrackingPlayersFiltered(Entity entity, Packet<? super ClientGamePacketListener> packet, Predicate<ServerPlayer> targetPredicate) {
/* 1263 */     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
/* 1264 */     if (trackedEntity != null)
/* 1265 */       trackedEntity.sendToTrackingPlayersFiltered(packet, targetPredicate); 
/*      */   }
/*      */   
/*      */   protected void sendToTrackingPlayersAndSelf(Entity entity, Packet<? super ClientGamePacketListener> packet) {
/* 1270 */     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
/* 1271 */     if (trackedEntity != null)
/* 1272 */       trackedEntity.sendToTrackingPlayersAndSelf(packet); 
/*      */   }
/*      */   
/*      */   public boolean isTrackedByAnyPlayer(Entity entity) {
/* 1277 */     TrackedEntity trackedEntity = (TrackedEntity)this.entityMap.get(entity.getId());
/* 1278 */     if (trackedEntity != null)
/* 1279 */       return !trackedEntity.seenBy.isEmpty(); 
/* 1281 */     return false;
/*      */   }
/*      */   
/*      */   public void forEachEntityTrackedBy(ServerPlayer player, Consumer<Entity> consumer) {
/* 1285 */     for (ObjectIterator<TrackedEntity> objectIterator = this.entityMap.values().iterator(); objectIterator.hasNext(); ) {
/* 1285 */       TrackedEntity entity = objectIterator.next();
/* 1286 */       if (entity.seenBy.contains(player.connection))
/* 1287 */         consumer.accept(entity.entity); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void resendBiomesForChunks(List<ChunkAccess> chunks) {
/* 1293 */     Map<ServerPlayer, List<LevelChunk>> chunksForPlayers = new HashMap<>();
/* 1294 */     for (ChunkAccess chunkAccess : chunks) {
/*      */       LevelChunk chunk;
/* 1295 */       ChunkPos pos = chunkAccess.getPos();
/* 1297 */       if (chunkAccess instanceof LevelChunk) {
/* 1297 */         LevelChunk levelChunk = (LevelChunk)chunkAccess;
/* 1298 */         chunk = levelChunk;
/*      */       } else {
/* 1300 */         chunk = this.level.getChunk(pos.x(), pos.z());
/*      */       } 
/* 1302 */       for (ServerPlayer player : getPlayers(pos, false))
/* 1303 */         ((List<LevelChunk>)chunksForPlayers.computeIfAbsent(player, p -> new ArrayList())).add(chunk); 
/*      */     } 
/* 1307 */     chunksForPlayers.forEach((player, chunkList) -> player.connection.send((Packet)ClientboundChunksBiomesPacket.forChunks(chunkList)));
/*      */   }
/*      */   
/*      */   protected PoiManager getPoiManager() {
/* 1311 */     return this.poiManager;
/*      */   }
/*      */   
/*      */   public String getStorageName() {
/* 1315 */     return this.storageName;
/*      */   }
/*      */   
/*      */   void onFullChunkStatusChange(ChunkPos pos, FullChunkStatus status) {
/* 1320 */     this.chunkStatusListener.onChunkStatusChange(pos, status);
/*      */   }
/*      */   
/*      */   public void waitForLightBeforeSending(ChunkPos centerChunk, int chunkRadius) {
/* 1324 */     int affectedLightChunkRadius = chunkRadius + 1;
/* 1325 */     ChunkPos.rangeClosed(centerChunk, affectedLightChunkRadius).forEach(chunkPos -> {
/*      */           ChunkHolder chunkHolder = getVisibleChunkIfPresent(chunkPos.pack());
/*      */           if (chunkHolder != null)
/*      */             chunkHolder.addSendDependency(this.lightEngine.waitForPendingTasks(chunkPos.x(), chunkPos.z())); 
/*      */         });
/*      */   }
/*      */   
/*      */   public void forEachReadyToSendChunk(Consumer<LevelChunk> consumer) {
/* 1334 */     for (ObjectIterator<ChunkHolder> objectIterator = this.visibleChunkMap.values().iterator(); objectIterator.hasNext(); ) {
/* 1334 */       ChunkHolder chunkHolder = objectIterator.next();
/* 1335 */       LevelChunk chunk = chunkHolder.getChunkToSend();
/* 1336 */       if (chunk != null)
/* 1337 */         consumer.accept(chunk); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private class DistanceManager extends DistanceManager {
/*      */     protected DistanceManager(TicketStorage ticketStorage, Executor executor, Executor mainThreadExecutor) {
/* 1344 */       super(ticketStorage, executor, mainThreadExecutor);
/*      */     }
/*      */     
/*      */     protected boolean isChunkToRemove(long node) {
/* 1349 */       return ChunkMap.this.toDrop.contains(node);
/*      */     }
/*      */     
/*      */     protected ChunkHolder getChunk(long node) {
/* 1354 */       return ChunkMap.this.getUpdatingChunkIfPresent(node);
/*      */     }
/*      */     
/*      */     protected ChunkHolder updateChunkScheduling(long node, int level, ChunkHolder chunk, int oldLevel) {
/* 1359 */       return ChunkMap.this.updateChunkScheduling(node, level, chunk, oldLevel);
/*      */     }
/*      */   }
/*      */   
/*      */   private class TrackedEntity implements ServerEntity.Synchronizer {
/*      */     private final ServerEntity serverEntity;
/*      */     
/*      */     private final Entity entity;
/*      */     
/*      */     private final int range;
/*      */     
/*      */     private SectionPos lastSectionPos;
/*      */     
/* 1371 */     private final Set<ServerPlayerConnection> seenBy = Sets.newIdentityHashSet();
/*      */     
/*      */     public TrackedEntity(Entity entity, int range, int updateInterval, boolean trackDelta) {
/* 1374 */       this.serverEntity = new ServerEntity(ChunkMap.this.level, entity, updateInterval, trackDelta, this);
/* 1375 */       this.entity = entity;
/* 1376 */       this.range = range;
/* 1377 */       this.lastSectionPos = SectionPos.of((EntityAccess)entity);
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj) {
/* 1382 */       if (obj instanceof TrackedEntity) {
/* 1382 */         TrackedEntity trackedEntity = (TrackedEntity)obj;
/* 1383 */         return (trackedEntity.entity.getId() == this.entity.getId());
/*      */       } 
/* 1386 */       return false;
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1391 */       return this.entity.getId();
/*      */     }
/*      */     
/*      */     public void sendToTrackingPlayers(Packet<? super ClientGamePacketListener> packet) {
/* 1396 */       for (ServerPlayerConnection connection : this.seenBy)
/* 1397 */         connection.send(packet); 
/*      */     }
/*      */     
/*      */     public void sendToTrackingPlayersAndSelf(Packet<? super ClientGamePacketListener> packet) {
/* 1403 */       sendToTrackingPlayers(packet);
/* 1404 */       Entity entity = this.entity;
/* 1404 */       if (entity instanceof ServerPlayer) {
/* 1404 */         ServerPlayer player = (ServerPlayer)entity;
/* 1405 */         player.connection.send(packet);
/*      */       } 
/*      */     }
/*      */     
/*      */     public void sendToTrackingPlayersFiltered(Packet<? super ClientGamePacketListener> packet, Predicate<ServerPlayer> targetPredicate) {
/* 1411 */       for (ServerPlayerConnection connection : this.seenBy) {
/* 1412 */         if (targetPredicate.test(connection.getPlayer()))
/* 1413 */           connection.send(packet); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void broadcastRemoved() {
/* 1419 */       for (ServerPlayerConnection connection : this.seenBy)
/* 1420 */         this.serverEntity.removePairing(connection.getPlayer()); 
/*      */     }
/*      */     
/*      */     public void removePlayer(ServerPlayer player) {
/* 1425 */       if (this.seenBy.remove(player.connection)) {
/* 1426 */         this.serverEntity.removePairing(player);
/* 1427 */         if (this.seenBy.isEmpty())
/* 1428 */           ChunkMap.this.level.debugSynchronizers().dropEntity(this.entity); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void updatePlayer(ServerPlayer player) {
/* 1434 */       if (player == this.entity)
/*      */         return; 
/* 1438 */       Vec3 deltaToPlayer = player.position().subtract(this.entity.position());
/* 1439 */       int playerViewDistance = ChunkMap.this.getPlayerViewDistance(player);
/* 1440 */       double visibleRange = Math.min(getEffectiveRange(), playerViewDistance * 16);
/* 1441 */       double distanceSquared = deltaToPlayer.x * deltaToPlayer.x + deltaToPlayer.z * deltaToPlayer.z;
/* 1442 */       double rangeSquared = visibleRange * visibleRange;
/* 1444 */       boolean visibleToPlayer = (distanceSquared <= rangeSquared && this.entity.broadcastToPlayer(player) && ChunkMap.this.isChunkTracked(player, this.entity.chunkPosition().x(), this.entity.chunkPosition().z()));
/* 1446 */       if (visibleToPlayer) {
/* 1447 */         if (this.seenBy.add(player.connection)) {
/* 1448 */           this.serverEntity.addPairing(player);
/* 1449 */           if (this.seenBy.size() == 1)
/* 1450 */             ChunkMap.this.level.debugSynchronizers().registerEntity(this.entity); 
/* 1452 */           ChunkMap.this.level.debugSynchronizers().startTrackingEntity(player, this.entity);
/*      */         } 
/*      */       } else {
/* 1455 */         removePlayer(player);
/*      */       } 
/*      */     }
/*      */     
/*      */     private int scaledRange(int range) {
/* 1460 */       return ChunkMap.this.level.getServer().getScaledTrackingDistance(range);
/*      */     }
/*      */     
/*      */     private int getEffectiveRange() {
/* 1464 */       int effectiveRange = this.range;
/* 1465 */       for (Entity passenger : this.entity.getIndirectPassengers()) {
/* 1466 */         int passengerRange = passenger.getType().clientTrackingRange() * 16;
/* 1467 */         if (passengerRange > effectiveRange)
/* 1468 */           effectiveRange = passengerRange; 
/*      */       } 
/* 1471 */       return scaledRange(effectiveRange);
/*      */     }
/*      */     
/*      */     public void updatePlayers(List<ServerPlayer> players) {
/* 1475 */       for (ServerPlayer player : players)
/* 1476 */         updatePlayer(player); 
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */