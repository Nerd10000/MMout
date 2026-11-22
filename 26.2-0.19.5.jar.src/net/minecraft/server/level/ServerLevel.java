/*      */ package net.minecraft.server.level;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*      */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.longs.LongSet;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportType;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockDestructionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundBlockEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundDamageEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundExplodePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.ServerScoreboard;
/*      */ import net.minecraft.server.players.SleepStatus;
/*      */ import net.minecraft.server.waypoints.ServerWaypointManager;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.AbortableIterationConsumer;
/*      */ import net.minecraft.util.CsvOutput;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProgressListener;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.datafix.DataFixTypes;
/*      */ import net.minecraft.util.debug.DebugSubscriptions;
/*      */ import net.minecraft.util.debug.LevelDebugSynchronizers;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.random.WeightedList;
/*      */ import net.minecraft.util.valueproviders.IntProvider;
/*      */ import net.minecraft.util.valueproviders.UniformInt;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.TickRateManager;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.clock.ClockManager;
/*      */ import net.minecraft.world.clock.ClockTimeMarkers;
/*      */ import net.minecraft.world.clock.ServerClockManager;
/*      */ import net.minecraft.world.clock.WorldClock;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.LightningBolt;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.MobCategory;
/*      */ import net.minecraft.world.entity.ReputationEventHandler;
/*      */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*      */ import net.minecraft.world.entity.ai.village.ReputationEventType;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*      */ import net.minecraft.world.entity.animal.equine.SkeletonHorse;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.raid.Raid;
/*      */ import net.minecraft.world.entity.raid.Raids;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*      */ import net.minecraft.world.item.crafting.RecipeAccess;
/*      */ import net.minecraft.world.item.crafting.RecipeManager;
/*      */ import net.minecraft.world.level.BlockEventData;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.CustomSpawner;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.ExplosionDamageCalculator;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelHeightAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.MoonPhase;
/*      */ import net.minecraft.world.level.NaturalSpawner;
/*      */ import net.minecraft.world.level.ServerExplosion;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.biome.Biome;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.SnowLayerBlock;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.FuelValues;
/*      */ import net.minecraft.world.level.block.entity.TickingBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.chunk.ChunkAccess;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.chunk.ChunkSource;
/*      */ import net.minecraft.world.level.chunk.LevelChunk;
/*      */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*      */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*      */ import net.minecraft.world.level.chunk.storage.EntityStorage;
/*      */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*      */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.dimension.LevelStem;
/*      */ import net.minecraft.world.level.dimension.end.EnderDragonFight;
/*      */ import net.minecraft.world.level.entity.EntityAccess;
/*      */ import net.minecraft.world.level.entity.EntityPersistentStorage;
/*      */ import net.minecraft.world.level.entity.EntityTickList;
/*      */ import net.minecraft.world.level.entity.EntityTypeTest;
/*      */ import net.minecraft.world.level.entity.LevelCallback;
/*      */ import net.minecraft.world.level.entity.LevelEntityGetter;
/*      */ import net.minecraft.world.level.entity.PersistentEntitySectionManager;
/*      */ import net.minecraft.world.level.gameevent.DynamicGameEventListener;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gameevent.GameEventDispatcher;
/*      */ import net.minecraft.world.level.gamerules.GameRule;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*      */ import net.minecraft.world.level.levelgen.WorldOptions;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.Structure;
/*      */ import net.minecraft.world.level.levelgen.structure.StructureCheck;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.pathfinder.PathTypeCache;
/*      */ import net.minecraft.world.level.portal.PortalForcer;
/*      */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*      */ import net.minecraft.world.level.redstone.Orientation;
/*      */ import net.minecraft.world.level.saveddata.SavedData;
/*      */ import net.minecraft.world.level.saveddata.WeatherData;
/*      */ import net.minecraft.world.level.saveddata.maps.MapId;
/*      */ import net.minecraft.world.level.saveddata.maps.MapIndex;
/*      */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.level.storage.SavedDataStorage;
/*      */ import net.minecraft.world.level.storage.ServerLevelData;
/*      */ import net.minecraft.world.level.storage.WritableLevelData;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.BooleanOp;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.ticks.LevelTickAccess;
/*      */ import net.minecraft.world.ticks.LevelTicks;
/*      */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class ServerLevel extends Level implements WorldGenLevel, ServerEntityGetter {
/*  189 */   public static final BlockPos END_SPAWN_POINT = new BlockPos(100, 50, 0);
/*      */   
/*  191 */   public static final IntProvider RAIN_DELAY = (IntProvider)UniformInt.of(12000, 180000);
/*      */   
/*  192 */   public static final IntProvider RAIN_DURATION = (IntProvider)UniformInt.of(12000, 24000);
/*      */   
/*  195 */   private static final IntProvider THUNDER_DELAY = (IntProvider)UniformInt.of(12000, 180000);
/*      */   
/*  196 */   public static final IntProvider THUNDER_DURATION = (IntProvider)UniformInt.of(3600, 15600);
/*      */   
/*  198 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final int EMPTY_TIME_NO_TICK = 300;
/*      */   
/*      */   private static final int MAX_SCHEDULED_TICKS_PER_TICK = 65536;
/*      */   
/*  203 */   private static final AtomicInteger ENTITY_COUNTER = new AtomicInteger();
/*      */   
/*  206 */   private final List<ServerPlayer> players = Lists.newArrayList();
/*      */   
/*      */   private final ServerChunkCache chunkSource;
/*      */   
/*      */   private final MinecraftServer server;
/*      */   
/*      */   private final ServerLevelData serverLevelData;
/*      */   
/*  211 */   private final EntityTickList entityTickList = new EntityTickList();
/*      */   
/*      */   private final ServerWaypointManager waypointManager;
/*      */   
/*      */   private EnvironmentAttributeSystem environmentAttributes;
/*      */   
/*      */   private final PersistentEntitySectionManager<Entity> entityManager;
/*      */   
/*      */   private final GameEventDispatcher gameEventDispatcher;
/*      */   
/*      */   public boolean noSave;
/*      */   
/*      */   private final SleepStatus sleepStatus;
/*      */   
/*      */   private int emptyTime;
/*      */   
/*      */   private final PortalForcer portalForcer;
/*      */   
/*  222 */   private final LevelTicks<Block> blockTicks = new LevelTicks(this::isPositionTickingWithEntitiesLoaded);
/*      */   
/*  223 */   private final LevelTicks<Fluid> fluidTicks = new LevelTicks(this::isPositionTickingWithEntitiesLoaded);
/*      */   
/*  225 */   private final PathTypeCache pathTypesByPosCache = new PathTypeCache();
/*      */   
/*  226 */   private final Set<Mob> navigatingMobs = (Set<Mob>)new ObjectOpenHashSet();
/*      */   
/*      */   private volatile boolean isUpdatingNavigations;
/*      */   
/*      */   protected final Raids raids;
/*      */   
/*  231 */   private final ObjectLinkedOpenHashSet<BlockEventData> blockEvents = new ObjectLinkedOpenHashSet();
/*      */   
/*  232 */   private final List<BlockEventData> blockEventsToReschedule = new ArrayList<>(64);
/*      */   
/*      */   private boolean handlingTick;
/*      */   
/*      */   private final List<CustomSpawner> customSpawners;
/*      */   
/*      */   private EnderDragonFight dragonFight;
/*      */   
/*  239 */   private final Int2ObjectMap<EnderDragonPart> dragonParts = (Int2ObjectMap<EnderDragonPart>)new Int2ObjectOpenHashMap();
/*      */   
/*      */   private final StructureManager structureManager;
/*      */   
/*      */   private final StructureCheck structureCheck;
/*      */   
/*      */   private final boolean tickTime;
/*      */   
/*  245 */   private final LevelDebugSynchronizers debugSynchronizers = new LevelDebugSynchronizers(this);
/*      */   
/*      */   public ServerLevel(MinecraftServer server, Executor executor, LevelStorageSource.LevelStorageAccess levelStorage, ServerLevelData levelData, ResourceKey<Level> dimension, LevelStem levelStem, boolean isDebug, long biomeZoomSeed, List<CustomSpawner> customSpawners, boolean tickTime) {
/*  248 */     super((WritableLevelData)levelData, dimension, (RegistryAccess)server.registryAccess(), levelStem.type(), false, isDebug, biomeZoomSeed, server.getMaxChainedNeighborUpdates());
/*  249 */     this.tickTime = tickTime;
/*  250 */     this.server = server;
/*  251 */     this.customSpawners = customSpawners;
/*  252 */     this.serverLevelData = levelData;
/*  254 */     ChunkGenerator generator = levelStem.generator();
/*  256 */     boolean syncWrites = server.forceSynchronousWrites();
/*  257 */     DataFixer fixerUpper = server.getFixerUpper();
/*  258 */     EntityStorage entityStorage = new EntityStorage(new SimpleRegionStorage(new RegionStorageInfo(levelStorage.getLevelId(), dimension, "entities"), levelStorage.getDimensionPath(dimension).resolve("entities"), fixerUpper, syncWrites, DataFixTypes.ENTITY_CHUNK), this, (Executor)server);
/*  259 */     this.entityManager = new PersistentEntitySectionManager(Entity.class, new EntityCallbacks(), (EntityPersistentStorage)entityStorage);
/*  261 */     Objects.requireNonNull(this.entityManager);
/*  261 */     this.chunkSource = new ServerChunkCache(this, levelStorage, fixerUpper, server.getStructureManager(), executor, generator, server.getPlayerList().getViewDistance(), server.getPlayerList().getSimulationDistance(), syncWrites, this.entityManager::updateChunkStatus, () -> server.overworld().getDataStorage());
/*  262 */     this.chunkSource.getGeneratorState().ensureStructuresGenerated();
/*  264 */     this.portalForcer = new PortalForcer(this);
/*  266 */     if (canHaveWeather())
/*  267 */       prepareWeather(server.getWeatherData()); 
/*  270 */     this.raids = (Raids)getDataStorage().computeIfAbsent(Raids.TYPE);
/*  272 */     if (!server.isSingleplayer())
/*  273 */       levelData.setGameType(server.getDefaultGameType()); 
/*  276 */     WorldGenSettings worldGenSettings = server.getWorldGenSettings();
/*  277 */     WorldOptions options = worldGenSettings.options();
/*  278 */     long seed = options.seed();
/*  280 */     this.structureCheck = new StructureCheck(this.chunkSource.chunkScanner(), registryAccess(), server.getStructureManager(), dimension, generator, this.chunkSource.randomState(), (LevelHeightAccessor)this, generator.getBiomeSource(), seed, fixerUpper);
/*  281 */     this.structureManager = new StructureManager((LevelAccessor)this, options, this.structureCheck);
/*  283 */     if (dimensionType().hasEnderDragonFight()) {
/*  284 */       this.dragonFight = (EnderDragonFight)getDataStorage().computeIfAbsent(EnderDragonFight.TYPE);
/*  285 */       this.dragonFight.init(this, seed, BlockPos.ZERO);
/*      */     } 
/*  288 */     this.sleepStatus = new SleepStatus();
/*  289 */     this.gameEventDispatcher = new GameEventDispatcher(this);
/*  291 */     this.waypointManager = new ServerWaypointManager();
/*  292 */     this
/*      */       
/*  294 */       .environmentAttributes = EnvironmentAttributeSystem.builder().addDefaultLayers(this).build();
/*  296 */     updateSkyBrightness();
/*      */   }
/*      */   
/*      */   public int getNextEntityId() {
/*  302 */     int id = 0;
/*  303 */     while (id == 0 || this.chunkSource.hasEntityWithId(id))
/*  304 */       id = ENTITY_COUNTER.incrementAndGet(); 
/*  307 */     return id;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   @VisibleForTesting
/*      */   public void setDragonFight(EnderDragonFight fight) {
/*  314 */     this.dragonFight = fight;
/*      */   }
/*      */   
/*      */   public Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) {
/*  319 */     return getChunkSource().getGenerator().getBiomeSource().getNoiseBiome(quartX, quartY, quartZ, getChunkSource().randomState().sampler());
/*      */   }
/*      */   
/*      */   public StructureManager structureManager() {
/*  323 */     return this.structureManager;
/*      */   }
/*      */   
/*      */   public ServerClockManager clockManager() {
/*  328 */     return this.server.clockManager();
/*      */   }
/*      */   
/*      */   public EnvironmentAttributeSystem environmentAttributes() {
/*  333 */     return this.environmentAttributes;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   @VisibleForTesting
/*      */   public EnvironmentAttributeSystem setEnvironmentAttributes(EnvironmentAttributeSystem environmentAttributes) {
/*  340 */     EnvironmentAttributeSystem previous = this.environmentAttributes;
/*  341 */     this.environmentAttributes = environmentAttributes;
/*  342 */     return previous;
/*      */   }
/*      */   
/*      */   public void tick(BooleanSupplier haveTime) {
/*  346 */     ProfilerFiller profiler = Profiler.get();
/*  348 */     this.handlingTick = true;
/*  349 */     environmentAttributes().invalidateTickCache();
/*  350 */     TickRateManager tickRateManager = tickRateManager();
/*  351 */     boolean runs = tickRateManager.runsNormally();
/*  352 */     if (runs) {
/*  353 */       profiler.push("world border");
/*  354 */       getWorldBorder().tick();
/*  355 */       profiler.popPush("weather");
/*  356 */       advanceWeatherCycle();
/*  357 */       profiler.pop();
/*      */     } 
/*  359 */     int percentage = ((Integer)getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE)).intValue();
/*  360 */     if (this.sleepStatus.areEnoughSleeping(percentage) && this.sleepStatus.areEnoughDeepSleeping(percentage, this.players)) {
/*  361 */       Optional<Holder<WorldClock>> defaultClock = dimensionType().defaultClock();
/*  362 */       if (((Boolean)getGameRules().get(GameRules.ADVANCE_TIME)).booleanValue() && defaultClock.isPresent())
/*  363 */         this.server.clockManager().moveToTimeMarker(defaultClock.get(), ClockTimeMarkers.WAKE_UP_FROM_SLEEP); 
/*  366 */       wakeUpAllPlayers();
/*  368 */       if (((Boolean)getGameRules().get(GameRules.ADVANCE_WEATHER)).booleanValue() && isRaining())
/*  369 */         resetWeatherCycle(); 
/*      */     } 
/*  373 */     updateSkyBrightness();
/*  375 */     if (runs)
/*  376 */       tickTime(); 
/*  379 */     profiler.push("tickPending");
/*  380 */     if (!isDebug() && runs) {
/*  381 */       long tick = getGameTime();
/*  382 */       profiler.push("blockTicks");
/*  383 */       this.blockTicks.tick(tick, 65536, this::tickBlock);
/*  384 */       profiler.popPush("fluidTicks");
/*  385 */       this.fluidTicks.tick(tick, 65536, this::tickFluid);
/*  386 */       profiler.pop();
/*      */     } 
/*  389 */     profiler.popPush("raid");
/*  390 */     if (runs)
/*  391 */       this.raids.tick(this); 
/*  393 */     profiler.popPush("chunkSource");
/*  394 */     getChunkSource().tick(haveTime, true);
/*  396 */     profiler.popPush("blockEvents");
/*  397 */     if (runs)
/*  398 */       runBlockEvents(); 
/*  400 */     this.handlingTick = false;
/*  401 */     profiler.pop();
/*  403 */     boolean isActive = this.chunkSource.hasActiveTickets();
/*  405 */     if (isActive)
/*  406 */       resetEmptyTime(); 
/*  408 */     if (runs)
/*  409 */       this.emptyTime++; 
/*  411 */     if (this.emptyTime < 300) {
/*  412 */       profiler.push("entities");
/*  413 */       if (this.dragonFight != null && runs) {
/*  414 */         profiler.push("dragonFight");
/*  415 */         this.dragonFight.tick();
/*  416 */         profiler.pop();
/*      */       } 
/*  418 */       this.entityTickList.forEach(entity -> {
/*      */             if (entity.isRemoved())
/*      */               return; 
/*      */             if (tickRateManager.isEntityFrozen(entity))
/*      */               return; 
/*      */             profiler.push("checkDespawn");
/*      */             entity.checkDespawn();
/*      */             profiler.pop();
/*      */             if (!(entity instanceof ServerPlayer) && !this.chunkSource.chunkMap.getDistanceManager().inEntityTickingRange(entity.chunkPosition().pack()))
/*      */               return; 
/*      */             Entity vehicle = entity.getVehicle();
/*      */             if (vehicle != null)
/*      */               if (vehicle.isRemoved() || !vehicle.hasPassenger(entity)) {
/*      */                 entity.stopRiding();
/*      */               } else {
/*      */                 return;
/*      */               }  
/*      */             profiler.push("tick");
/*      */             guardEntityTick(this::tickNonPassenger, entity);
/*      */             profiler.pop();
/*      */           });
/*  451 */       profiler.popPush("blockEntities");
/*  452 */       tickBlockEntities();
/*  453 */       profiler.pop();
/*      */     } 
/*  456 */     profiler.push("entityManagement");
/*  457 */     this.entityManager.tick();
/*  458 */     profiler.pop();
/*  460 */     profiler.push("debugSynchronizers");
/*  461 */     if (this.debugSynchronizers.hasAnySubscriberFor(DebugSubscriptions.NEIGHBOR_UPDATES)) {
/*  462 */       this.neighborUpdater.setDebugListener(blockPos -> this.debugSynchronizers.broadcastEventToTracking(blockPos, DebugSubscriptions.NEIGHBOR_UPDATES, blockPos));
/*      */     } else {
/*  466 */       this.neighborUpdater.setDebugListener(null);
/*      */     } 
/*  468 */     this.debugSynchronizers.tick(this.server.debugSubscribers());
/*  469 */     profiler.pop();
/*      */   }
/*      */   
/*      */   public boolean shouldTickBlocksAt(long chunkPos) {
/*  474 */     return this.chunkSource.chunkMap.getDistanceManager().inBlockTickingRange(chunkPos);
/*      */   }
/*      */   
/*      */   protected void tickTime() {
/*  478 */     if (!this.tickTime)
/*      */       return; 
/*  481 */     long time = this.levelData.getGameTime() + 1L;
/*  482 */     this.serverLevelData.setGameTime(time);
/*  483 */     Profiler.get().push("scheduledFunctions");
/*  484 */     this.server.getScheduledEvents().tick(this.server, time);
/*  485 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   public void tickCustomSpawners(boolean spawnEnemies) {
/*  489 */     for (CustomSpawner spawner : this.customSpawners)
/*  490 */       spawner.tick(this, spawnEnemies); 
/*      */   }
/*      */   
/*      */   private void wakeUpAllPlayers() {
/*  495 */     this.sleepStatus.removeAllSleepers();
/*  497 */     ((List)this.players.stream().filter(LivingEntity::isSleeping).collect(Collectors.toList())).forEach(player -> player.stopSleepInBed(false, false));
/*      */   }
/*      */   
/*      */   public void tickChunk(LevelChunk chunk, int tickSpeed) {
/*  503 */     ChunkPos chunkPos = chunk.getPos();
/*  504 */     int minX = chunkPos.getMinBlockX();
/*  505 */     int minZ = chunkPos.getMinBlockZ();
/*  507 */     ProfilerFiller profiler = Profiler.get();
/*  509 */     profiler.push("iceandsnow");
/*  510 */     for (int i = 0; i < tickSpeed; i++) {
/*  511 */       if (this.random.nextInt(48) == 0)
/*  512 */         tickPrecipitation(getBlockRandomPos(minX, 0, minZ, 15)); 
/*      */     } 
/*  516 */     profiler.popPush("tickBlocks");
/*  517 */     if (tickSpeed > 0) {
/*  518 */       LevelChunkSection[] sections = chunk.getSections();
/*  519 */       for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/*  520 */         LevelChunkSection section = sections[sectionIndex];
/*  521 */         if (section.isRandomlyTicking()) {
/*  522 */           int sectionY = chunk.getSectionYFromSectionIndex(sectionIndex);
/*  523 */           int minYInSection = SectionPos.sectionToBlockCoord(sectionY);
/*  524 */           for (int j = 0; j < tickSpeed; j++) {
/*  525 */             BlockPos pos = getBlockRandomPos(minX, minYInSection, minZ, 15);
/*  527 */             profiler.push("randomTick");
/*  528 */             BlockState blockState = section.getBlockState(pos.getX() - minX, pos.getY() - minYInSection, pos.getZ() - minZ);
/*  529 */             if (blockState.isRandomlyTicking())
/*  530 */               blockState.randomTick(this, pos, this.random); 
/*  532 */             FluidState fluidState = blockState.getFluidState();
/*  533 */             if (fluidState.isRandomlyTicking())
/*  534 */               fluidState.randomTick(this, pos, this.random); 
/*  536 */             profiler.pop();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  541 */     profiler.pop();
/*      */   }
/*      */   
/*      */   public void tickThunder(LevelChunk chunk) {
/*  545 */     ChunkPos chunkPos = chunk.getPos();
/*  546 */     boolean raining = isRaining();
/*  547 */     int minX = chunkPos.getMinBlockX();
/*  548 */     int minZ = chunkPos.getMinBlockZ();
/*  550 */     ProfilerFiller profiler = Profiler.get();
/*  551 */     profiler.push("thunder");
/*  552 */     if (raining && isThundering() && this.random.nextInt(100000) == 0) {
/*  553 */       BlockPos pos = findLightningTargetAround(getBlockRandomPos(minX, 0, minZ, 15));
/*  554 */       if (isRainingAt(pos)) {
/*  555 */         DifficultyInstance difficulty = getCurrentDifficultyAt(pos);
/*  557 */         boolean isTrap = (((Boolean)getGameRules().get(GameRules.SPAWN_MOBS)).booleanValue() && this.random.nextDouble() < difficulty.getEffectiveDifficulty() * 0.01D && !getBlockState(pos.below()).is(BlockTags.LIGHTNING_RODS));
/*  558 */         if (isTrap) {
/*  559 */           SkeletonHorse horse = (SkeletonHorse)EntityTypes.SKELETON_HORSE.create(this, EntitySpawnReason.EVENT);
/*  560 */           if (horse != null) {
/*  561 */             horse.setTrap(true);
/*  562 */             horse.setAge(0);
/*  563 */             horse.setPos(pos.getX(), pos.getY(), pos.getZ());
/*  564 */             addFreshEntity((Entity)horse);
/*      */           } 
/*      */         } 
/*  567 */         LightningBolt bolt = (LightningBolt)EntityTypes.LIGHTNING_BOLT.create(this, EntitySpawnReason.EVENT);
/*  568 */         if (bolt != null) {
/*  569 */           bolt.snapTo(Vec3.atBottomCenterOf((Vec3i)pos));
/*  570 */           bolt.setVisualOnly(isTrap);
/*  571 */           addFreshEntity((Entity)bolt);
/*      */         } 
/*      */       } 
/*      */     } 
/*  576 */     profiler.pop();
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public void tickPrecipitation(BlockPos pos) {
/*  581 */     BlockPos topPos = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
/*  582 */     BlockPos belowPos = topPos.below();
/*  583 */     Biome biome = (Biome)getBiome(topPos).value();
/*  585 */     if (biome.shouldFreeze((LevelReader)this, belowPos))
/*  586 */       setBlockAndUpdate(belowPos, Blocks.ICE.defaultBlockState()); 
/*  589 */     if (isRaining()) {
/*  590 */       int maxHeight = ((Integer)getGameRules().get(GameRules.MAX_SNOW_ACCUMULATION_HEIGHT)).intValue();
/*  591 */       if (maxHeight > 0 && biome.shouldSnow((LevelReader)this, topPos)) {
/*  592 */         BlockState state = getBlockState(topPos);
/*  593 */         if (state.is(Blocks.SNOW)) {
/*  594 */           int currentLayers = ((Integer)state.getValue((Property)SnowLayerBlock.LAYERS)).intValue();
/*  595 */           if (currentLayers < Math.min(maxHeight, 8)) {
/*  596 */             BlockState newState = (BlockState)state.setValue((Property)SnowLayerBlock.LAYERS, Integer.valueOf(currentLayers + 1));
/*  597 */             Block.pushEntitiesUp(state, newState, (LevelAccessor)this, topPos);
/*  598 */             setBlockAndUpdate(topPos, newState);
/*      */           } 
/*      */         } else {
/*  601 */           setBlockAndUpdate(topPos, Blocks.SNOW.defaultBlockState());
/*      */         } 
/*      */       } 
/*  605 */       Biome.Precipitation precipitation = biome.getPrecipitationAt(belowPos, getSeaLevel());
/*  606 */       if (precipitation != Biome.Precipitation.NONE) {
/*  607 */         BlockState belowState = getBlockState(belowPos);
/*  608 */         belowState.getBlock().handlePrecipitation(belowState, this, belowPos, precipitation);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private Optional<BlockPos> findLightningRod(BlockPos center) {
/*  614 */     Optional<BlockPos> nearbyLightningRod = getPoiManager().findClosest(p -> p.is(PoiTypes.LIGHTNING_ROD), lightningRodPos -> (lightningRodPos.getY() == getHeight(Heightmap.Types.WORLD_SURFACE, lightningRodPos.getX(), lightningRodPos.getZ()) - 1), center, 128, PoiManager.Occupancy.ANY);
/*  621 */     return nearbyLightningRod.map(blockPos -> blockPos.above(1));
/*      */   }
/*      */   
/*      */   protected BlockPos findLightningTargetAround(BlockPos pos) {
/*  625 */     BlockPos center = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos);
/*  627 */     Optional<BlockPos> lightningRodTarget = findLightningRod(center);
/*  628 */     if (lightningRodTarget.isPresent())
/*  629 */       return lightningRodTarget.get(); 
/*  632 */     AABB search = AABB.encapsulatingFullBlocks(center, center.atY(getMaxY() + 1)).inflate(3.0D);
/*  634 */     List<LivingEntity> entities = getEntitiesOfClass(LivingEntity.class, search, input -> (input.isAlive() && canSeeSky(input.blockPosition())));
/*  636 */     if (!entities.isEmpty())
/*  637 */       return ((LivingEntity)entities.get(this.random.nextInt(entities.size()))).blockPosition(); 
/*  640 */     if (center.getY() == getMinY() - 1)
/*  641 */       center = center.above(2); 
/*  644 */     return center;
/*      */   }
/*      */   
/*      */   public boolean isHandlingTick() {
/*  648 */     return this.handlingTick;
/*      */   }
/*      */   
/*      */   public boolean canSleepThroughNights() {
/*  652 */     return (((Integer)getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE)).intValue() <= 100);
/*      */   }
/*      */   
/*      */   private void announceSleepStatus() {
/*      */     MutableComponent mutableComponent;
/*  656 */     if (!canSleepThroughNights())
/*      */       return; 
/*  660 */     if (getServer().isSingleplayer() && !getServer().isPublished())
/*      */       return; 
/*  663 */     int percentage = ((Integer)getGameRules().get(GameRules.PLAYERS_SLEEPING_PERCENTAGE)).intValue();
/*  665 */     if (this.sleepStatus.areEnoughSleeping(percentage)) {
/*  666 */       mutableComponent = Component.translatable("sleep.skipping_night");
/*      */     } else {
/*  668 */       mutableComponent = Component.translatable("sleep.players_sleeping", new Object[] { Integer.valueOf(this.sleepStatus.amountSleeping()), Integer.valueOf(this.sleepStatus.sleepersNeeded(percentage)) });
/*      */     } 
/*  671 */     for (ServerPlayer player : this.players)
/*  672 */       player.sendOverlayMessage((Component)mutableComponent); 
/*      */   }
/*      */   
/*      */   public void updateSleepingPlayerList() {
/*  677 */     if (!this.players.isEmpty() && this.sleepStatus.update(this.players))
/*  678 */       announceSleepStatus(); 
/*      */   }
/*      */   
/*      */   public ServerScoreboard getScoreboard() {
/*  684 */     return this.server.getScoreboard();
/*      */   }
/*      */   
/*      */   public ServerWaypointManager getWaypointManager() {
/*  688 */     return this.waypointManager;
/*      */   }
/*      */   
/*      */   public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
/*  693 */     long localTime = 0L;
/*  694 */     float moonBrightness = 0.0F;
/*  696 */     ChunkAccess chunk = getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
/*  697 */     if (chunk != null) {
/*  698 */       localTime = chunk.getInhabitedTime();
/*  699 */       moonBrightness = getMoonBrightness(pos);
/*      */     } 
/*  702 */     return new DifficultyInstance(getDifficulty(), getOverworldClockTime(), localTime, moonBrightness);
/*      */   }
/*      */   
/*      */   public float getMoonBrightness(BlockPos pos) {
/*  706 */     MoonPhase moonPhase = (MoonPhase)this.environmentAttributes.getValue(EnvironmentAttributes.MOON_PHASE, pos);
/*  707 */     return DimensionType.MOON_BRIGHTNESS_PER_PHASE[moonPhase.index()];
/*      */   }
/*      */   
/*      */   private void prepareWeather(WeatherData weatherData) {
/*  711 */     if (weatherData.isRaining()) {
/*  712 */       this.rainLevel = 1.0F;
/*  713 */       if (weatherData.isThundering())
/*  714 */         this.thunderLevel = 1.0F; 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void advanceWeatherCycle() {
/*  720 */     boolean wasRaining = isRaining();
/*  721 */     if (canHaveWeather()) {
/*  722 */       WeatherData weatherData = getWeatherData();
/*  723 */       if (((Boolean)getGameRules().get(GameRules.ADVANCE_WEATHER)).booleanValue()) {
/*  724 */         int clearWeatherTime = weatherData.getClearWeatherTime();
/*  725 */         int thunderTime = weatherData.getThunderTime();
/*  726 */         int rainTime = weatherData.getRainTime();
/*  727 */         boolean thundering = weatherData.isThundering();
/*  728 */         boolean raining = weatherData.isRaining();
/*  731 */         if (clearWeatherTime > 0) {
/*  732 */           clearWeatherTime--;
/*  733 */           thunderTime = thundering ? 0 : 1;
/*  734 */           rainTime = raining ? 0 : 1;
/*  735 */           thundering = false;
/*  736 */           raining = false;
/*      */         } else {
/*  739 */           if (thunderTime > 0) {
/*  740 */             thunderTime--;
/*  741 */             if (thunderTime == 0)
/*  743 */               thundering = !thundering; 
/*  747 */           } else if (thundering) {
/*  749 */             thunderTime = THUNDER_DURATION.sample(this.random);
/*      */           } else {
/*  752 */             thunderTime = THUNDER_DELAY.sample(this.random);
/*      */           } 
/*  757 */           if (rainTime > 0) {
/*  758 */             rainTime--;
/*  759 */             if (rainTime == 0)
/*  760 */               raining = !raining; 
/*  763 */           } else if (raining) {
/*  765 */             rainTime = RAIN_DURATION.sample(this.random);
/*      */           } else {
/*  769 */             rainTime = RAIN_DELAY.sample(this.random);
/*      */           } 
/*      */         } 
/*  774 */         weatherData.setThunderTime(thunderTime);
/*  775 */         weatherData.setRainTime(rainTime);
/*  776 */         weatherData.setClearWeatherTime(clearWeatherTime);
/*  777 */         weatherData.setThundering(thundering);
/*  778 */         weatherData.setRaining(raining);
/*      */       } 
/*  781 */       this.oThunderLevel = this.thunderLevel;
/*  782 */       if (weatherData.isThundering()) {
/*  783 */         this.thunderLevel += 0.01F;
/*      */       } else {
/*  785 */         this.thunderLevel -= 0.01F;
/*      */       } 
/*  787 */       this.thunderLevel = Mth.clamp(this.thunderLevel, 0.0F, 1.0F);
/*  789 */       this.oRainLevel = this.rainLevel;
/*  790 */       if (weatherData.isRaining()) {
/*  791 */         this.rainLevel += 0.01F;
/*      */       } else {
/*  793 */         this.rainLevel -= 0.01F;
/*      */       } 
/*  795 */       this.rainLevel = Mth.clamp(this.rainLevel, 0.0F, 1.0F);
/*      */     } 
/*  798 */     if (this.oRainLevel != this.rainLevel)
/*  799 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, this.rainLevel), dimension()); 
/*  801 */     if (this.oThunderLevel != this.thunderLevel)
/*  802 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, this.thunderLevel), dimension()); 
/*  805 */     if (wasRaining != isRaining()) {
/*  806 */       if (wasRaining) {
/*  807 */         this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.STOP_RAINING, 0.0F));
/*      */       } else {
/*  809 */         this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.START_RAINING, 0.0F));
/*      */       } 
/*  811 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.RAIN_LEVEL_CHANGE, this.rainLevel));
/*  812 */       this.server.getPlayerList().broadcastAll((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.THUNDER_LEVEL_CHANGE, this.thunderLevel));
/*      */     } 
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public void resetWeatherCycle() {
/*  818 */     WeatherData weatherData = getWeatherData();
/*  819 */     weatherData.setRainTime(0);
/*  820 */     weatherData.setRaining(false);
/*  821 */     weatherData.setThunderTime(0);
/*  822 */     weatherData.setThundering(false);
/*      */   }
/*      */   
/*      */   public void resetEmptyTime() {
/*  826 */     this.emptyTime = 0;
/*      */   }
/*      */   
/*      */   private void tickFluid(BlockPos pos, Fluid type) {
/*  830 */     BlockState blockState = getBlockState(pos);
/*  831 */     FluidState fluidState = blockState.getFluidState();
/*  832 */     if (fluidState.is(type))
/*  833 */       fluidState.tick(this, pos, blockState); 
/*      */   }
/*      */   
/*      */   private void tickBlock(BlockPos pos, Block type) {
/*  838 */     BlockState state = getBlockState(pos);
/*  839 */     if (state.is(type))
/*  840 */       state.tick(this, pos, this.random); 
/*      */   }
/*      */   
/*      */   public void tickNonPassenger(Entity entity) {
/*  845 */     entity.setOldPosAndRot();
/*  847 */     ProfilerFiller profiler = Profiler.get();
/*  848 */     entity.tickCount++;
/*  849 */     Objects.requireNonNull(entity.typeHolder());
/*  849 */     profiler.push(entity.typeHolder()::getRegisteredName);
/*  850 */     profiler.incrementCounter("tickNonPassenger");
/*  851 */     entity.tick();
/*  852 */     profiler.pop();
/*  854 */     for (Entity passenger : entity.getPassengers())
/*  855 */       tickPassenger(entity, passenger); 
/*      */   }
/*      */   
/*      */   private void tickPassenger(Entity vehicle, Entity entity) {
/*  860 */     if (entity.isRemoved() || entity.getVehicle() != vehicle) {
/*  861 */       entity.stopRiding();
/*      */       return;
/*      */     } 
/*  864 */     if (!(entity instanceof Player) && !this.entityTickList.contains(entity))
/*      */       return; 
/*  868 */     entity.setOldPosAndRot();
/*  870 */     entity.tickCount++;
/*  871 */     ProfilerFiller profiler = Profiler.get();
/*  872 */     Objects.requireNonNull(entity.typeHolder());
/*  872 */     profiler.push(entity.typeHolder()::getRegisteredName);
/*  873 */     profiler.incrementCounter("tickPassenger");
/*  874 */     entity.rideTick();
/*  875 */     profiler.pop();
/*  877 */     for (Entity passenger : entity.getPassengers())
/*  878 */       tickPassenger(entity, passenger); 
/*      */   }
/*      */   
/*      */   public void updateNeighboursOnBlockSet(BlockPos pos, BlockState oldState) {
/*  883 */     BlockState blockState = getBlockState(pos);
/*  884 */     Block newBlock = blockState.getBlock();
/*  885 */     boolean blockChanged = !oldState.is(newBlock);
/*  887 */     if (blockChanged)
/*  888 */       oldState.affectNeighborsAfterRemoval(this, pos, false); 
/*  891 */     updateNeighborsAt(pos, blockState.getBlock());
/*  892 */     if (blockState.hasAnalogOutputSignal())
/*  893 */       updateNeighbourForOutputSignal(pos, newBlock); 
/*      */   }
/*      */   
/*      */   public boolean mayInteract(Entity entity, BlockPos pos) {
/*  899 */     if (entity instanceof Player) {
/*  899 */       Player player = (Player)entity;
/*  899 */       if (!this.server.isUnderSpawnProtection(this, pos, player) && getWorldBorder().isWithinBounds(pos));
/*  899 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void save(ProgressListener progressListener, boolean flush, boolean noSave) {
/*  903 */     ServerChunkCache chunkSource = getChunkSource();
/*  904 */     if (noSave)
/*      */       return; 
/*  908 */     if (progressListener != null)
/*  909 */       progressListener.progressStartNoAbort((Component)Component.translatable("menu.savingLevel")); 
/*  911 */     saveLevelData(flush);
/*  913 */     if (progressListener != null)
/*  914 */       progressListener.progressStage((Component)Component.translatable("menu.savingChunks")); 
/*  916 */     chunkSource.save(flush);
/*  918 */     if (flush) {
/*  919 */       this.entityManager.saveAll();
/*      */     } else {
/*  921 */       this.entityManager.autoSave();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void saveLevelData(boolean sync) {
/*  926 */     SavedDataStorage savedDataStorage = getChunkSource().getDataStorage();
/*  928 */     if (sync) {
/*  929 */       savedDataStorage.saveAndJoin();
/*      */     } else {
/*  931 */       savedDataStorage.scheduleSave();
/*      */     } 
/*      */   }
/*      */   
/*      */   public <T extends Entity> List<? extends T> getEntities(EntityTypeTest<Entity, T> type, Predicate<? super T> selector) {
/*  936 */     List<T> result = Lists.newArrayList();
/*  937 */     getEntities(type, selector, result);
/*  938 */     return result;
/*      */   }
/*      */   
/*      */   public <T extends Entity> void getEntities(EntityTypeTest<Entity, T> type, Predicate<? super T> selector, List<? super T> result) {
/*  942 */     getEntities(type, selector, result, 2147483647);
/*      */   }
/*      */   
/*      */   public <T extends Entity> void getEntities(EntityTypeTest<Entity, T> type, Predicate<? super T> selector, List<? super T> result, int maxResults) {
/*  946 */     getEntities().get(type, entity -> {
/*      */           if (selector.test(entity)) {
/*      */             result.add(entity);
/*      */             if (result.size() >= maxResults)
/*      */               return AbortableIterationConsumer.Continuation.ABORT; 
/*      */           } 
/*      */           return AbortableIterationConsumer.Continuation.CONTINUE;
/*      */         });
/*      */   }
/*      */   
/*      */   public List<? extends EnderDragon> getDragons() {
/*  958 */     return getEntities((EntityTypeTest<Entity, EnderDragon>)EntityTypes.ENDER_DRAGON, LivingEntity::isAlive);
/*      */   }
/*      */   
/*      */   public List<ServerPlayer> getPlayers(Predicate<? super ServerPlayer> selector) {
/*  962 */     return getPlayers(selector, 2147483647);
/*      */   }
/*      */   
/*      */   public List<ServerPlayer> getPlayers(Predicate<? super ServerPlayer> selector, int maxResults) {
/*  966 */     List<ServerPlayer> result = Lists.newArrayList();
/*  967 */     for (ServerPlayer player : this.players) {
/*  968 */       if (selector.test(player)) {
/*  969 */         result.add(player);
/*  970 */         if (result.size() >= maxResults)
/*  971 */           return result; 
/*      */       } 
/*      */     } 
/*  975 */     return result;
/*      */   }
/*      */   
/*      */   public ServerPlayer getRandomPlayer() {
/*  979 */     List<ServerPlayer> players = getPlayers(LivingEntity::isAlive);
/*  980 */     if (players.isEmpty())
/*  981 */       return null; 
/*  983 */     return players.get(this.random.nextInt(players.size()));
/*      */   }
/*      */   
/*      */   public boolean addFreshEntity(Entity entity) {
/*  991 */     return addEntity(entity);
/*      */   }
/*      */   
/*      */   public boolean addWithUUID(Entity entity) {
/*  998 */     return addEntity(entity);
/*      */   }
/*      */   
/*      */   public void addDuringTeleport(Entity entity) {
/* 1002 */     if (entity instanceof ServerPlayer) {
/* 1002 */       ServerPlayer player = (ServerPlayer)entity;
/* 1003 */       addPlayer(player);
/*      */     } else {
/* 1005 */       addEntity(entity);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addNewPlayer(ServerPlayer player) {
/* 1010 */     addPlayer(player);
/*      */   }
/*      */   
/*      */   public void addRespawnedPlayer(ServerPlayer player) {
/* 1014 */     addPlayer(player);
/*      */   }
/*      */   
/*      */   private void addPlayer(ServerPlayer player) {
/* 1018 */     Entity existing = getEntity(player.getUUID());
/* 1019 */     if (existing != null) {
/* 1020 */       LOGGER.warn("Force-added player with duplicate UUID {}", player.getUUID());
/* 1021 */       existing.unRide();
/* 1022 */       removePlayerImmediately((ServerPlayer)existing, Entity.RemovalReason.DISCARDED);
/*      */     } 
/* 1024 */     this.entityManager.addNewEntity((EntityAccess)player);
/*      */   }
/*      */   
/*      */   private boolean addEntity(Entity entity) {
/* 1028 */     if (entity.isRemoved()) {
/* 1029 */       LOGGER.warn("Tried to add entity {} but it was marked as removed already", entity.typeHolder().getRegisteredName());
/* 1030 */       return false;
/*      */     } 
/* 1033 */     return this.entityManager.addNewEntity((EntityAccess)entity);
/*      */   }
/*      */   
/*      */   public boolean tryAddFreshEntityWithPassengers(Entity entity) {
/* 1037 */     Objects.requireNonNull(this.entityManager);
/* 1037 */     if (entity.getSelfAndPassengers().map(Entity::getUUID).anyMatch(this.entityManager::isLoaded))
/* 1038 */       return false; 
/* 1041 */     addFreshEntityWithPassengers(entity);
/* 1042 */     return true;
/*      */   }
/*      */   
/*      */   public void unload(LevelChunk levelChunk) {
/* 1046 */     levelChunk.clearAllBlockEntities();
/* 1047 */     levelChunk.unregisterTickContainerFromLevel(this);
/* 1048 */     this.debugSynchronizers.dropChunk(levelChunk.getPos());
/*      */   }
/*      */   
/*      */   public void removePlayerImmediately(ServerPlayer player, Entity.RemovalReason reason) {
/* 1052 */     player.remove(reason);
/*      */   }
/*      */   
/*      */   public void destroyBlockProgress(int id, BlockPos blockPos, int progress) {
/* 1057 */     for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
/* 1058 */       if (player.level() != this || player.getId() == id)
/*      */         continue; 
/* 1061 */       double xd = blockPos.getX() - player.getX();
/* 1062 */       double yd = blockPos.getY() - player.getY();
/* 1063 */       double zd = blockPos.getZ() - player.getZ();
/* 1065 */       if (xd * xd + yd * yd + zd * zd < 1024.0D)
/* 1066 */         player.connection.send((Packet)new ClientboundBlockDestructionPacket(id, blockPos, progress)); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void playSeededSound(Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
/* 1073 */     Player player = (Player)except;
/* 1073 */     this.server.getPlayerList().broadcast((except instanceof Player) ? player : null, x, y, z, ((SoundEvent)sound.value()).getRange(volume), dimension(), (Packet)new ClientboundSoundPacket(sound, source, x, y, z, volume, pitch, seed));
/*      */   }
/*      */   
/*      */   public void playSeededSound(Entity except, Entity sourceEntity, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
/* 1078 */     Player player = (Player)except;
/* 1078 */     this.server.getPlayerList().broadcast((except instanceof Player) ? player : null, sourceEntity.getX(), sourceEntity.getY(), sourceEntity.getZ(), ((SoundEvent)sound.value()).getRange(volume), dimension(), (Packet)new ClientboundSoundEntityPacket(sound, source, sourceEntity, volume, pitch, seed));
/*      */   }
/*      */   
/*      */   public void globalLevelEvent(int type, BlockPos pos, int data) {
/* 1083 */     if (((Boolean)getGameRules().get(GameRules.GLOBAL_SOUND_EVENTS)).booleanValue()) {
/* 1084 */       this.server.getPlayerList().getPlayers().forEach(player -> {
/*      */             Vec3 soundPos;
/*      */             if (player.level() == this) {
/*      */               Vec3 centerOfBlock = Vec3.atCenterOf((Vec3i)pos);
/*      */               if (player.distanceToSqr(centerOfBlock) < Mth.square(32)) {
/*      */                 soundPos = centerOfBlock;
/*      */               } else {
/*      */                 Vec3 directionToEvent = centerOfBlock.subtract(player.position()).normalize();
/*      */                 soundPos = player.position().add(directionToEvent.scale(32.0D));
/*      */               } 
/*      */             } else {
/*      */               soundPos = player.position();
/*      */             } 
/*      */             player.connection.send((Packet)new ClientboundLevelEventPacket(type, BlockPos.containing((Position)soundPos), data, true));
/*      */           });
/*      */     } else {
/* 1103 */       levelEvent((Entity)null, type, pos, data);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void levelEvent(Entity source, int type, BlockPos pos, int data) {
/* 1109 */     Player player = (Player)source;
/* 1109 */     this.server.getPlayerList().broadcast((source instanceof Player) ? player : null, pos.getX(), pos.getY(), pos.getZ(), 64.0D, dimension(), (Packet)new ClientboundLevelEventPacket(type, pos, data, false));
/*      */   }
/*      */   
/*      */   public int getLogicalHeight() {
/* 1113 */     return dimensionType().logicalHeight();
/*      */   }
/*      */   
/*      */   public void gameEvent(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) {
/* 1118 */     this.gameEventDispatcher.post(gameEvent, position, context);
/*      */   }
/*      */   
/*      */   public void sendBlockUpdated(BlockPos pos, BlockState old, BlockState current, int updateFlags) {
/* 1123 */     if (this.isUpdatingNavigations) {
/* 1124 */       String message = "recursive call to sendBlockUpdated";
/* 1125 */       Util.logAndPauseIfInIde("recursive call to sendBlockUpdated", new IllegalStateException("recursive call to sendBlockUpdated"));
/*      */     } 
/* 1127 */     getChunkSource().blockChanged(pos);
/* 1128 */     this.pathTypesByPosCache.invalidate(pos);
/* 1130 */     VoxelShape oldShape = old.getCollisionShape((BlockGetter)this, pos);
/* 1131 */     VoxelShape newShape = current.getCollisionShape((BlockGetter)this, pos);
/* 1132 */     if (!Shapes.joinIsNotEmpty(oldShape, newShape, BooleanOp.NOT_SAME))
/*      */       return; 
/* 1139 */     ObjectArrayList<PathNavigation> objectArrayList = new ObjectArrayList();
/* 1141 */     for (Mob navigatingMob : this.navigatingMobs) {
/* 1142 */       PathNavigation pathNavigation = navigatingMob.getNavigation();
/* 1143 */       if (pathNavigation.shouldRecomputePath(pos))
/* 1144 */         objectArrayList.add(pathNavigation); 
/*      */     } 
/*      */     try {
/* 1149 */       this.isUpdatingNavigations = true;
/* 1150 */       for (PathNavigation navigation : objectArrayList)
/* 1151 */         navigation.recomputePath(); 
/*      */     } finally {
/* 1154 */       this.isUpdatingNavigations = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void updateNeighborsAt(BlockPos pos, Block sourceBlock) {
/* 1160 */     updateNeighborsAt(pos, sourceBlock, ExperimentalRedstoneUtils.initialOrientation(this, null, null));
/*      */   }
/*      */   
/*      */   public void updateNeighborsAt(BlockPos pos, Block sourceBlock, Orientation orientation) {
/* 1165 */     this.neighborUpdater.updateNeighborsAtExceptFromFacing(pos, sourceBlock, null, orientation);
/*      */   }
/*      */   
/*      */   public void updateNeighborsAtExceptFromFacing(BlockPos pos, Block blockObject, Direction skipDirection, Orientation orientation) {
/* 1170 */     this.neighborUpdater.updateNeighborsAtExceptFromFacing(pos, blockObject, skipDirection, orientation);
/*      */   }
/*      */   
/*      */   public void neighborChanged(BlockPos pos, Block changedBlock, Orientation orientation) {
/* 1175 */     this.neighborUpdater.neighborChanged(pos, changedBlock, orientation);
/*      */   }
/*      */   
/*      */   public void neighborChanged(BlockState state, BlockPos pos, Block changedBlock, Orientation orientation, boolean movedByPiston) {
/* 1180 */     this.neighborUpdater.neighborChanged(state, pos, changedBlock, orientation, movedByPiston);
/*      */   }
/*      */   
/*      */   public void broadcastEntityEvent(Entity entity, byte event) {
/* 1185 */     getChunkSource().sendToTrackingPlayersAndSelf(entity, (Packet<? super ClientGamePacketListener>)new ClientboundEntityEventPacket(entity, event));
/*      */   }
/*      */   
/*      */   public void broadcastDamageEvent(Entity entity, DamageSource source) {
/* 1190 */     getChunkSource().sendToTrackingPlayersAndSelf(entity, (Packet<? super ClientGamePacketListener>)new ClientboundDamageEventPacket(entity, source));
/*      */   }
/*      */   
/*      */   public ServerChunkCache getChunkSource() {
/* 1195 */     return this.chunkSource;
/*      */   }
/*      */   
/*      */   public void explode(Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, double x, double y, double z, float r, boolean fire, Level.ExplosionInteraction interactionType, ParticleOptions smallExplosionParticles, ParticleOptions largeExplosionParticles, WeightedList<ExplosionParticleInfo> blockParticles, Holder<SoundEvent> explosionSound) {
/* 1200 */     switch (interactionType) {
/*      */       default:
/* 1200 */         throw new MatchException(null, null);
/*      */       case NONE:
/*      */       
/*      */       case BLOCK:
/*      */       
/*      */       case MOB:
/* 1203 */         if (((Boolean)getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue());
/*      */       case TNT:
/*      */       
/*      */       case TRIGGER:
/*      */         break;
/*      */     } 
/* 1207 */     Explosion.BlockInteraction blockInteraction = Explosion.BlockInteraction.TRIGGER_BLOCK;
/* 1210 */     Vec3 center = new Vec3(x, y, z);
/* 1211 */     ServerExplosion explosion = new ServerExplosion(this, source, damageSource, damageCalculator, center, r, fire, blockInteraction);
/* 1212 */     int blockCount = explosion.explode();
/* 1214 */     ParticleOptions explosionParticle = explosion.isSmall() ? smallExplosionParticles : largeExplosionParticles;
/* 1216 */     for (ServerPlayer player : this.players) {
/* 1217 */       if (player.distanceToSqr(center) < 4096.0D) {
/* 1218 */         Optional<Vec3> playerKnockback = Optional.ofNullable((Vec3)explosion.getHitPlayers().get(player));
/* 1219 */         player.connection.send((Packet)new ClientboundExplodePacket(center, r, blockCount, playerKnockback, explosionParticle, explosionSound, blockParticles));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private Explosion.BlockInteraction getDestroyType(GameRule<Boolean> gameRule) {
/* 1225 */     return ((Boolean)getGameRules().get(gameRule)).booleanValue() ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
/*      */   }
/*      */   
/*      */   public void blockEvent(BlockPos pos, Block block, int b0, int b1) {
/* 1230 */     this.blockEvents.add(new BlockEventData(pos, block, b0, b1));
/*      */   }
/*      */   
/*      */   private void runBlockEvents() {
/* 1234 */     this.blockEventsToReschedule.clear();
/* 1235 */     while (!this.blockEvents.isEmpty()) {
/* 1236 */       BlockEventData eventData = (BlockEventData)this.blockEvents.removeFirst();
/* 1237 */       if (shouldTickBlocksAt(eventData.pos())) {
/* 1238 */         if (doBlockEvent(eventData))
/* 1239 */           this.server.getPlayerList().broadcast(null, eventData.pos().getX(), eventData.pos().getY(), eventData.pos().getZ(), 64.0D, dimension(), (Packet)new ClientboundBlockEventPacket(eventData.pos(), eventData.block(), eventData.paramA(), eventData.paramB())); 
/*      */         continue;
/*      */       } 
/* 1242 */       this.blockEventsToReschedule.add(eventData);
/*      */     } 
/* 1245 */     this.blockEvents.addAll(this.blockEventsToReschedule);
/*      */   }
/*      */   
/*      */   private boolean doBlockEvent(BlockEventData eventData) {
/* 1249 */     BlockState state = getBlockState(eventData.pos());
/* 1250 */     if (state.is(eventData.block()))
/* 1251 */       return state.triggerEvent(this, eventData.pos(), eventData.paramA(), eventData.paramB()); 
/* 1253 */     return false;
/*      */   }
/*      */   
/*      */   public LevelTicks<Block> getBlockTicks() {
/* 1258 */     return this.blockTicks;
/*      */   }
/*      */   
/*      */   public LevelTicks<Fluid> getFluidTicks() {
/* 1263 */     return this.fluidTicks;
/*      */   }
/*      */   
/*      */   public MinecraftServer getServer() {
/* 1268 */     return this.server;
/*      */   }
/*      */   
/*      */   public PortalForcer getPortalForcer() {
/* 1272 */     return this.portalForcer;
/*      */   }
/*      */   
/*      */   public StructureTemplateManager getStructureManager() {
/* 1276 */     return this.server.getStructureManager();
/*      */   }
/*      */   
/*      */   public <T extends ParticleOptions> int sendParticles(T particle, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed) {
/* 1280 */     return sendParticles(particle, false, false, x, y, z, count, xDist, yDist, zDist, speed);
/*      */   }
/*      */   
/*      */   public <T extends ParticleOptions> int sendParticles(T particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed) {
/* 1284 */     ClientboundLevelParticlesPacket packet = new ClientboundLevelParticlesPacket((ParticleOptions)particle, overrideLimiter, alwaysShow, x, y, z, (float)xDist, (float)yDist, (float)zDist, (float)speed, count);
/* 1285 */     int result = 0;
/* 1287 */     for (int i = 0; i < this.players.size(); i++) {
/* 1288 */       ServerPlayer player = this.players.get(i);
/* 1290 */       if (sendParticles(player, overrideLimiter, x, y, z, (Packet<?>)packet))
/* 1291 */         result++; 
/*      */     } 
/* 1295 */     return result;
/*      */   }
/*      */   
/*      */   public <T extends ParticleOptions> boolean sendParticles(ServerPlayer player, T particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, int count, double xDist, double yDist, double zDist, double speed) {
/* 1299 */     ClientboundLevelParticlesPacket clientboundLevelParticlesPacket = new ClientboundLevelParticlesPacket((ParticleOptions)particle, overrideLimiter, alwaysShow, x, y, z, (float)xDist, (float)yDist, (float)zDist, (float)speed, count);
/* 1301 */     return sendParticles(player, overrideLimiter, x, y, z, (Packet<?>)clientboundLevelParticlesPacket);
/*      */   }
/*      */   
/*      */   private boolean sendParticles(ServerPlayer player, boolean overrideLimiter, double x, double y, double z, Packet<?> packet) {
/* 1305 */     if (player.level() != this)
/* 1306 */       return false; 
/* 1309 */     BlockPos pos = player.blockPosition();
/* 1311 */     if (pos.closerToCenterThan((Position)new Vec3(x, y, z), overrideLimiter ? 512.0D : 32.0D)) {
/* 1312 */       player.connection.send(packet);
/* 1313 */       return true;
/*      */     } 
/* 1316 */     return false;
/*      */   }
/*      */   
/*      */   public Entity getEntity(int id) {
/* 1321 */     return (Entity)getEntities().get(id);
/*      */   }
/*      */   
/*      */   public Entity getEntityInAnyDimension(UUID uuid) {
/* 1327 */     Entity entity = getEntity(uuid);
/* 1328 */     if (entity != null)
/* 1329 */       return entity; 
/* 1332 */     for (ServerLevel otherLevel : getServer().getAllLevels()) {
/* 1333 */       if (otherLevel == this)
/*      */         continue; 
/* 1337 */       Entity otherEntity = otherLevel.getEntity(uuid);
/* 1338 */       if (otherEntity != null)
/* 1339 */         return otherEntity; 
/*      */     } 
/* 1343 */     return null;
/*      */   }
/*      */   
/*      */   public Player getPlayerInAnyDimension(UUID uuid) {
/* 1348 */     return getServer().getPlayerList().getPlayer(uuid);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public Entity getEntityOrPart(int id) {
/* 1356 */     Entity entity = (Entity)getEntities().get(id);
/* 1357 */     if (entity != null)
/* 1358 */       return entity; 
/* 1360 */     return (Entity)this.dragonParts.get(id);
/*      */   }
/*      */   
/*      */   public Collection<EnderDragonPart> dragonParts() {
/* 1365 */     return (Collection<EnderDragonPart>)this.dragonParts.values();
/*      */   }
/*      */   
/*      */   public BlockPos findNearestMapStructure(TagKey<Structure> structureTag, BlockPos origin, int maxSearchRadius, boolean createReference) {
/* 1369 */     Optional<HolderSet.Named<Structure>> tag = registryAccess().lookupOrThrow(Registries.STRUCTURE).get(structureTag);
/* 1370 */     if (tag.isEmpty())
/* 1371 */       return null; 
/* 1373 */     Pair<BlockPos, Holder<Structure>> result = getChunkSource().getGenerator().findNearestMapStructure(this, (HolderSet)tag.get(), origin, maxSearchRadius, createReference);
/* 1374 */     return (result != null) ? (BlockPos)result.getFirst() : null;
/*      */   }
/*      */   
/*      */   public Pair<BlockPos, Holder<Biome>> findClosestBiome3d(Predicate<Holder<Biome>> biomeTest, BlockPos origin, int maxSearchRadius, int sampleResolutionHorizontal, int sampleResolutionVertical) {
/* 1378 */     return getChunkSource().getGenerator().getBiomeSource().findClosestBiome3d(origin, maxSearchRadius, sampleResolutionHorizontal, sampleResolutionVertical, biomeTest, getChunkSource().randomState().sampler(), (LevelReader)this);
/*      */   }
/*      */   
/*      */   public WorldBorder getWorldBorder() {
/* 1383 */     WorldBorder worldBorder = (WorldBorder)getDataStorage().computeIfAbsent(WorldBorder.TYPE);
/* 1384 */     worldBorder.applyInitialSettings(this.levelData.getGameTime());
/* 1385 */     return worldBorder;
/*      */   }
/*      */   
/*      */   public RecipeManager recipeAccess() {
/* 1390 */     return this.server.getRecipeManager();
/*      */   }
/*      */   
/*      */   public TickRateManager tickRateManager() {
/* 1395 */     return (TickRateManager)this.server.tickRateManager();
/*      */   }
/*      */   
/*      */   public boolean noSave() {
/* 1400 */     return this.noSave;
/*      */   }
/*      */   
/*      */   public SavedDataStorage getDataStorage() {
/* 1404 */     return getChunkSource().getDataStorage();
/*      */   }
/*      */   
/*      */   public MapItemSavedData getMapData(MapId id) {
/* 1409 */     return (MapItemSavedData)getServer().getDataStorage().get(MapItemSavedData.type(id));
/*      */   }
/*      */   
/*      */   public void setMapData(MapId id, MapItemSavedData data) {
/* 1413 */     getServer().getDataStorage().set(MapItemSavedData.type(id), (SavedData)data);
/*      */   }
/*      */   
/*      */   public MapId getFreeMapId() {
/* 1417 */     return ((MapIndex)getServer().getDataStorage().computeIfAbsent(MapIndex.TYPE)).getNextMapId();
/*      */   }
/*      */   
/*      */   public void setRespawnData(LevelData.RespawnData respawnData) {
/* 1422 */     getServer().setRespawnData(respawnData);
/*      */   }
/*      */   
/*      */   public LevelData.RespawnData getRespawnData() {
/* 1427 */     return getServer().getRespawnData();
/*      */   }
/*      */   
/*      */   public LongSet getForceLoadedChunks() {
/* 1431 */     return this.chunkSource.getForceLoadedChunks();
/*      */   }
/*      */   
/*      */   public boolean setChunkForced(int chunkX, int chunkZ, boolean forced) {
/* 1435 */     boolean updated = this.chunkSource.updateChunkForced(new ChunkPos(chunkX, chunkZ), forced);
/* 1436 */     if (forced && updated)
/* 1437 */       getChunk(chunkX, chunkZ); 
/* 1439 */     return updated;
/*      */   }
/*      */   
/*      */   public List<ServerPlayer> players() {
/* 1444 */     return this.players;
/*      */   }
/*      */   
/*      */   public void updatePOIOnBlockStateChange(BlockPos pos, BlockState oldState, BlockState newState) {
/* 1449 */     Optional<Holder<PoiType>> oldType = PoiTypes.forState(oldState);
/* 1450 */     Optional<Holder<PoiType>> newType = PoiTypes.forState(newState);
/* 1452 */     if (Objects.equals(oldType, newType))
/*      */       return; 
/* 1456 */     BlockPos immutable = pos.immutable();
/* 1457 */     oldType.ifPresent(poiType -> getServer().execute(()));
/* 1462 */     newType.ifPresent(poiType -> getServer().execute(()));
/*      */   }
/*      */   
/*      */   public PoiManager getPoiManager() {
/* 1471 */     return getChunkSource().getPoiManager();
/*      */   }
/*      */   
/*      */   public boolean isVillage(BlockPos pos) {
/* 1475 */     return isCloseToVillage(pos, 1);
/*      */   }
/*      */   
/*      */   public boolean isVillage(SectionPos sectionPos) {
/* 1479 */     return isVillage(sectionPos.center());
/*      */   }
/*      */   
/*      */   public boolean isCloseToVillage(BlockPos pos, int sectionDistance) {
/* 1483 */     if (sectionDistance > 6)
/* 1484 */       return false; 
/* 1486 */     return (sectionsToVillage(SectionPos.of(pos)) <= sectionDistance);
/*      */   }
/*      */   
/*      */   public int sectionsToVillage(SectionPos pos) {
/* 1490 */     return getPoiManager().sectionsToVillage(pos);
/*      */   }
/*      */   
/*      */   public Raids getRaids() {
/* 1494 */     return this.raids;
/*      */   }
/*      */   
/*      */   public Raid getRaidAt(BlockPos pos) {
/* 1499 */     return this.raids.getNearbyRaid(pos, 9216);
/*      */   }
/*      */   
/*      */   public boolean isRaided(BlockPos pos) {
/* 1503 */     return (getRaidAt(pos) != null);
/*      */   }
/*      */   
/*      */   public void onReputationEvent(ReputationEventType type, Entity source, ReputationEventHandler target) {
/* 1507 */     target.onReputationEventFrom(type, source);
/*      */   }
/*      */   
/*      */   public void saveDebugReport(Path rootDir) throws IOException {
/* 1511 */     ChunkMap chunkMap = (getChunkSource()).chunkMap;
/* 1513 */     Writer output = Files.newBufferedWriter(rootDir.resolve("stats.txt"), new java.nio.file.OpenOption[0]);
/*      */     try {
/* 1514 */       output.write(String.format(Locale.ROOT, "spawning_chunks: %d\n", new Object[] { Integer.valueOf(chunkMap.getDistanceManager().getNaturalSpawnChunkCount()) }));
/* 1515 */       NaturalSpawner.SpawnState lastSpawnState = getChunkSource().getLastSpawnState();
/* 1516 */       if (lastSpawnState != null)
/* 1517 */         for (ObjectIterator<Object2IntMap.Entry<MobCategory>> objectIterator = lastSpawnState.getMobCategoryCounts().object2IntEntrySet().iterator(); objectIterator.hasNext(); ) {
/* 1517 */           Object2IntMap.Entry<MobCategory> entry = objectIterator.next();
/* 1518 */           output.write(String.format(Locale.ROOT, "spawn_count.%s: %d\n", new Object[] { ((MobCategory)entry.getKey()).getName(), Integer.valueOf(entry.getIntValue()) }));
/*      */         }  
/* 1521 */       output.write(String.format(Locale.ROOT, "entities: %s\n", new Object[] { this.entityManager.gatherStats() }));
/* 1522 */       output.write(String.format(Locale.ROOT, "block_entity_tickers: %d\n", new Object[] { Integer.valueOf(this.blockEntityTickers.size()) }));
/* 1523 */       output.write(String.format(Locale.ROOT, "block_ticks: %d\n", new Object[] { Integer.valueOf(getBlockTicks().count()) }));
/* 1524 */       output.write(String.format(Locale.ROOT, "fluid_ticks: %d\n", new Object[] { Integer.valueOf(getFluidTicks().count()) }));
/* 1525 */       output.write("distance_manager: " + chunkMap.getDistanceManager().getDebugStatus() + "\n");
/* 1526 */       output.write(String.format(Locale.ROOT, "pending_tasks: %d\n", new Object[] { Integer.valueOf(getChunkSource().getPendingTasksCount()) }));
/* 1527 */       if (output != null)
/* 1527 */         output.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (output != null)
/*      */         try {
/*      */           output.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/* 1529 */     CrashReport test = new CrashReport("Level dump", new Exception("dummy"));
/* 1530 */     fillReportDetails(test);
/* 1531 */     Writer writer1 = Files.newBufferedWriter(rootDir.resolve("example_crash.txt"), new java.nio.file.OpenOption[0]);
/*      */     try {
/* 1532 */       writer1.write(test.getFriendlyReport(ReportType.TEST));
/* 1533 */       if (writer1 != null)
/* 1533 */         writer1.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (writer1 != null)
/*      */         try {
/*      */           writer1.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/* 1535 */     Path chunks = rootDir.resolve("chunks.csv");
/* 1536 */     Writer writer2 = Files.newBufferedWriter(chunks, new java.nio.file.OpenOption[0]);
/*      */     try {
/* 1537 */       chunkMap.dumpChunks(writer2);
/* 1538 */       if (writer2 != null)
/* 1538 */         writer2.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (writer2 != null)
/*      */         try {
/*      */           writer2.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/* 1540 */     Path entityChunks = rootDir.resolve("entity_chunks.csv");
/* 1541 */     Writer writer3 = Files.newBufferedWriter(entityChunks, new java.nio.file.OpenOption[0]);
/*      */     try {
/* 1542 */       this.entityManager.dumpSections(writer3);
/* 1543 */       if (writer3 != null)
/* 1543 */         writer3.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (writer3 != null)
/*      */         try {
/*      */           writer3.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/* 1545 */     Path entities = rootDir.resolve("entities.csv");
/* 1546 */     Writer writer4 = Files.newBufferedWriter(entities, new java.nio.file.OpenOption[0]);
/*      */     try {
/* 1547 */       dumpEntities(writer4, getEntities().getAll());
/* 1548 */       if (writer4 != null)
/* 1548 */         writer4.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (writer4 != null)
/*      */         try {
/*      */           writer4.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/* 1550 */     Path blockEntities = rootDir.resolve("block_entities.csv");
/* 1551 */     Writer writer5 = Files.newBufferedWriter(blockEntities, new java.nio.file.OpenOption[0]);
/*      */     try {
/* 1552 */       dumpBlockEntityTickers(writer5);
/* 1553 */       if (writer5 != null)
/* 1553 */         writer5.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (writer5 != null)
/*      */         try {
/*      */           writer5.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void dumpEntities(Writer output, Iterable<Entity> entities) throws IOException {
/* 1566 */     CsvOutput csvOutput = CsvOutput.builder().addColumn("x").addColumn("y").addColumn("z").addColumn("uuid").addColumn("type").addColumn("alive").addColumn("display_name").addColumn("custom_name").build(output);
/* 1568 */     for (Entity entity : entities) {
/* 1569 */       Component customName = entity.getCustomName();
/* 1570 */       Component displayName = entity.getDisplayName();
/* 1571 */       csvOutput.writeRow(new Object[] { Double.valueOf(entity.getX()), 
/* 1573 */             Double.valueOf(entity.getY()), 
/* 1574 */             Double.valueOf(entity.getZ()), entity
/* 1575 */             .getUUID(), entity
/* 1576 */             .typeHolder().getRegisteredName(), 
/* 1577 */             Boolean.valueOf(entity.isAlive()), displayName
/* 1578 */             .getString(), 
/* 1579 */             (customName != null) ? customName.getString() : null });
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpBlockEntityTickers(Writer output) throws IOException {
/* 1590 */     CsvOutput csvOutput = CsvOutput.builder().addColumn("x").addColumn("y").addColumn("z").addColumn("type").build(output);
/* 1592 */     for (TickingBlockEntity ticker : this.blockEntityTickers) {
/* 1593 */       BlockPos blockPos = ticker.getPos();
/* 1594 */       csvOutput.writeRow(new Object[] { Integer.valueOf(blockPos.getX()), 
/* 1596 */             Integer.valueOf(blockPos.getY()), 
/* 1597 */             Integer.valueOf(blockPos.getZ()), ticker
/* 1598 */             .getType() });
/*      */     } 
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public void clearBlockEvents(BoundingBox bb) {
/* 1605 */     this.blockEvents.removeIf(e -> bb.isInside((Vec3i)e.pos()));
/*      */   }
/*      */   
/*      */   public Iterable<Entity> getAllEntities() {
/* 1609 */     return getEntities().getAll();
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1614 */     return "ServerLevel[" + this.serverLevelData.getLevelName() + "]";
/*      */   }
/*      */   
/*      */   public boolean isFlat() {
/* 1618 */     return this.server.getWorldData().isFlatWorld();
/*      */   }
/*      */   
/*      */   public long getSeed() {
/* 1623 */     return this.server.getWorldGenSettings().options().seed();
/*      */   }
/*      */   
/*      */   public EnderDragonFight getDragonFight() {
/* 1627 */     return this.dragonFight;
/*      */   }
/*      */   
/*      */   public WeatherData getWeatherData() {
/* 1631 */     return this.server.getWeatherData();
/*      */   }
/*      */   
/*      */   public ServerLevel getLevel() {
/* 1636 */     return this;
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public String getWatchdogStats() {
/* 1641 */     return String.format(Locale.ROOT, "players: %s, entities: %s [%s], block_entities: %d [%s], block_ticks: %d, fluid_ticks: %d, chunk_source: %s", new Object[] { Integer.valueOf(this.players.size()), this.entityManager
/* 1643 */           .gatherStats(), 
/* 1644 */           getTypeCount(this.entityManager.getEntityGetter().getAll(), e -> e.typeHolder().getRegisteredName()), 
/* 1645 */           Integer.valueOf(this.blockEntityTickers.size()), 
/* 1646 */           getTypeCount(this.blockEntityTickers, TickingBlockEntity::getType), 
/* 1647 */           Integer.valueOf(getBlockTicks().count()), 
/* 1648 */           Integer.valueOf(getFluidTicks().count()), 
/* 1649 */           gatherChunkSourceStats() });
/*      */   }
/*      */   
/*      */   private static <T> String getTypeCount(Iterable<T> values, Function<T, String> typeGetter) {
/*      */     try {
/* 1655 */       Object2IntOpenHashMap<String> countByType = new Object2IntOpenHashMap();
/* 1656 */       for (T e : values) {
/* 1657 */         String type = typeGetter.apply(e);
/* 1658 */         countByType.addTo(type, 1);
/*      */       } 
/* 1660 */       Comparator<Object2IntMap.Entry<String>> compareByCount = Comparator.comparingInt(Object2IntMap.Entry::getIntValue);
/* 1661 */       return countByType.object2IntEntrySet().stream()
/* 1662 */         .sorted(compareByCount.reversed())
/* 1663 */         .limit(5L)
/* 1664 */         .map(e -> (String)e.getKey() + ":" + (String)e.getKey())
/* 1665 */         .collect(Collectors.joining(","));
/* 1666 */     } catch (Exception e) {
/* 1667 */       return "";
/*      */     } 
/*      */   }
/*      */   
/*      */   protected LevelEntityGetter<Entity> getEntities() {
/* 1673 */     return this.entityManager.getEntityGetter();
/*      */   }
/*      */   
/*      */   public void addLegacyChunkEntities(Stream<Entity> loaded) {
/* 1677 */     this.entityManager.addLegacyChunkEntities(loaded);
/*      */   }
/*      */   
/*      */   public void addWorldGenChunkEntities(Stream<Entity> loaded) {
/* 1681 */     this.entityManager.addWorldGenChunkEntities(loaded);
/*      */   }
/*      */   
/*      */   public void startTickingChunk(LevelChunk levelChunk) {
/* 1688 */     levelChunk.unpackTicks(getGameTime());
/*      */   }
/*      */   
/*      */   public void onStructureStartsAvailable(ChunkAccess chunk) {
/* 1692 */     this.server.execute(() -> this.structureCheck.onStructureLoad(chunk.getPos(), chunk.getAllStarts()));
/*      */   }
/*      */   
/*      */   public PathTypeCache getPathTypeCache() {
/* 1696 */     return this.pathTypesByPosCache;
/*      */   }
/*      */   
/*      */   public void waitForEntities(ChunkPos centerChunk, int radius) {
/* 1700 */     List<ChunkPos> chunks = ChunkPos.rangeClosed(centerChunk, radius).toList();
/* 1702 */     this.server.managedBlock(() -> {
/*      */           this.entityManager.processPendingLoads();
/*      */           for (ChunkPos chunk : chunks) {
/*      */             if (!areEntitiesLoaded(chunk.pack()))
/*      */               return false; 
/*      */           } 
/*      */           return true;
/*      */         });
/*      */   }
/*      */   
/*      */   public boolean isSpawningMonsters() {
/* 1714 */     return (((Boolean)getGameRules().get(GameRules.SPAWN_MOBS)).booleanValue() && ((Boolean)getGameRules().get(GameRules.SPAWN_MONSTERS)).booleanValue());
/*      */   }
/*      */   
/*      */   private final class EntityCallbacks implements LevelCallback<Entity> {
/*      */     public void onCreated(Entity entity) {
/* 1720 */       if (entity instanceof WaypointTransmitter) {
/* 1720 */         WaypointTransmitter waypoint = (WaypointTransmitter)entity;
/* 1720 */         if (waypoint.isTransmittingWaypoint())
/* 1721 */           ServerLevel.this.getWaypointManager().trackWaypoint(waypoint); 
/*      */       } 
/*      */     }
/*      */     
/*      */     public void onDestroyed(Entity entity) {
/* 1727 */       if (entity instanceof WaypointTransmitter) {
/* 1727 */         WaypointTransmitter waypoint = (WaypointTransmitter)entity;
/* 1728 */         ServerLevel.this.getWaypointManager().untrackWaypoint(waypoint);
/*      */       } 
/* 1730 */       ServerLevel.this.getScoreboard().entityRemoved(entity);
/*      */     }
/*      */     
/*      */     public void onTickingStart(Entity entity) {
/* 1735 */       ServerLevel.this.entityTickList.add(entity);
/*      */     }
/*      */     
/*      */     public void onTickingEnd(Entity entity) {
/* 1740 */       ServerLevel.this.entityTickList.remove(entity);
/*      */     }
/*      */     
/*      */     public void onTrackingStart(Entity entity) {
/* 1745 */       ServerLevel.this.getChunkSource().addEntity(entity);
/* 1746 */       if (entity instanceof ServerPlayer) {
/* 1746 */         ServerPlayer player = (ServerPlayer)entity;
/* 1747 */         ServerLevel.this.players.add(player);
/* 1748 */         if (player.isReceivingWaypoints())
/* 1749 */           ServerLevel.this.getWaypointManager().addPlayer(player); 
/* 1751 */         ServerLevel.this.updateSleepingPlayerList();
/*      */       } 
/* 1753 */       if (entity instanceof WaypointTransmitter) {
/* 1753 */         WaypointTransmitter waypoint = (WaypointTransmitter)entity;
/* 1753 */         if (waypoint.isTransmittingWaypoint())
/* 1754 */           ServerLevel.this.getWaypointManager().trackWaypoint(waypoint); 
/*      */       } 
/* 1756 */       if (entity instanceof Mob) {
/* 1756 */         Mob mob = (Mob)entity;
/* 1757 */         if (ServerLevel.this.isUpdatingNavigations) {
/* 1758 */           String message = "onTrackingStart called during navigation iteration";
/* 1759 */           Util.logAndPauseIfInIde("onTrackingStart called during navigation iteration", new IllegalStateException("onTrackingStart called during navigation iteration"));
/*      */         } 
/* 1761 */         ServerLevel.this.navigatingMobs.add(mob);
/*      */       } 
/* 1763 */       if (entity instanceof EnderDragon) {
/* 1763 */         EnderDragon dragon = (EnderDragon)entity;
/* 1764 */         for (EnderDragonPart subEntity : dragon.getSubEntities())
/* 1765 */           ServerLevel.this.dragonParts.put(subEntity.getId(), subEntity); 
/*      */       } 
/* 1769 */       entity.updateDynamicGameEventListener(DynamicGameEventListener::add);
/*      */     }
/*      */     
/*      */     public void onTrackingEnd(Entity entity) {
/* 1774 */       ServerLevel.this.getChunkSource().removeEntity(entity);
/* 1775 */       if (entity instanceof ServerPlayer) {
/* 1775 */         ServerPlayer player = (ServerPlayer)entity;
/* 1776 */         ServerLevel.this.players.remove(player);
/* 1777 */         ServerLevel.this.getWaypointManager().removePlayer(player);
/* 1778 */         ServerLevel.this.updateSleepingPlayerList();
/*      */       } 
/* 1780 */       if (entity instanceof Mob) {
/* 1780 */         Mob mob = (Mob)entity;
/* 1781 */         if (ServerLevel.this.isUpdatingNavigations) {
/* 1782 */           String message = "onTrackingStart called during navigation iteration";
/* 1783 */           Util.logAndPauseIfInIde("onTrackingStart called during navigation iteration", new IllegalStateException("onTrackingStart called during navigation iteration"));
/*      */         } 
/* 1785 */         ServerLevel.this.navigatingMobs.remove(mob);
/*      */       } 
/* 1787 */       if (entity instanceof EnderDragon) {
/* 1787 */         EnderDragon dragon = (EnderDragon)entity;
/* 1788 */         for (EnderDragonPart subEntity : dragon.getSubEntities())
/* 1789 */           ServerLevel.this.dragonParts.remove(subEntity.getId()); 
/*      */       } 
/* 1793 */       entity.updateDynamicGameEventListener(DynamicGameEventListener::remove);
/* 1795 */       ServerLevel.this.debugSynchronizers.dropEntity(entity);
/*      */     }
/*      */     
/*      */     public void onSectionChange(Entity entity) {
/* 1800 */       entity.updateDynamicGameEventListener(DynamicGameEventListener::move);
/*      */     }
/*      */   }
/*      */   
/*      */   public void close() throws IOException {
/* 1806 */     super.close();
/* 1807 */     this.entityManager.close();
/*      */   }
/*      */   
/*      */   public String gatherChunkSourceStats() {
/* 1812 */     return "Chunks[S] W: " + this.chunkSource.gatherStats() + " E: " + this.entityManager.gatherStats();
/*      */   }
/*      */   
/*      */   public boolean areEntitiesLoaded(long chunkKey) {
/* 1816 */     return this.entityManager.areEntitiesLoaded(chunkKey);
/*      */   }
/*      */   
/*      */   public boolean isPositionTickingWithEntitiesLoaded(long key) {
/* 1820 */     return (areEntitiesLoaded(key) && this.chunkSource.isPositionTicking(key));
/*      */   }
/*      */   
/*      */   public boolean isPositionEntityTicking(BlockPos pos) {
/* 1824 */     return (this.entityManager.canPositionTick(pos) && this.chunkSource.chunkMap.getDistanceManager().inEntityTickingRange(ChunkPos.pack(pos)));
/*      */   }
/*      */   
/*      */   public boolean areEntitiesActuallyLoadedAndTicking(ChunkPos pos) {
/* 1830 */     return (this.entityManager.isTicking(pos) && this.entityManager.areEntitiesLoaded(pos.pack()));
/*      */   }
/*      */   
/*      */   public boolean anyPlayerCloseEnoughForSpawning(BlockPos pos) {
/* 1834 */     return anyPlayerCloseEnoughForSpawning(ChunkPos.containing(pos));
/*      */   }
/*      */   
/*      */   public boolean anyPlayerCloseEnoughForSpawning(ChunkPos pos) {
/* 1838 */     return this.chunkSource.chunkMap.anyPlayerCloseEnoughForSpawning(pos);
/*      */   }
/*      */   
/*      */   public boolean canSpreadFireAround(BlockPos pos) {
/* 1842 */     int spreadRadius = ((Integer)getGameRules().get(GameRules.FIRE_SPREAD_RADIUS_AROUND_PLAYER)).intValue();
/* 1843 */     return (spreadRadius == -1 || this.chunkSource.chunkMap.anyPlayerCloseEnoughTo(pos, spreadRadius));
/*      */   }
/*      */   
/*      */   public boolean canSpawnEntitiesInChunk(ChunkPos pos) {
/* 1847 */     return (this.entityManager.canPositionTick(pos) && 
/* 1848 */       getWorldBorder().isWithinBounds(pos));
/*      */   }
/*      */   
/*      */   public FeatureFlagSet enabledFeatures() {
/* 1853 */     return this.server.getWorldData().enabledFeatures();
/*      */   }
/*      */   
/*      */   public PotionBrewing potionBrewing() {
/* 1858 */     return this.server.potionBrewing();
/*      */   }
/*      */   
/*      */   public FuelValues fuelValues() {
/* 1863 */     return this.server.fuelValues();
/*      */   }
/*      */   
/*      */   public GameRules getGameRules() {
/* 1867 */     return this.server.getGameRules();
/*      */   }
/*      */   
/*      */   public CrashReportCategory fillReportDetails(CrashReport report) {
/* 1872 */     CrashReportCategory category = super.fillReportDetails(report);
/* 1873 */     WeatherData weatherData = getWeatherData();
/* 1874 */     category.setDetail("Loaded entity count", () -> String.valueOf(this.entityManager.count()));
/* 1875 */     category.setDetail("Server weather", () -> String.format(Locale.ROOT, "Rain time: %d (now: %b), thunder time: %d (now: %b)", new Object[] { Integer.valueOf(weatherData.getRainTime()), Boolean.valueOf(isRaining()), Integer.valueOf(weatherData.getThunderTime()), Boolean.valueOf(isThundering()) }));
/* 1876 */     return category;
/*      */   }
/*      */   
/*      */   public int getSeaLevel() {
/* 1881 */     return this.chunkSource.getGenerator().getSeaLevel();
/*      */   }
/*      */   
/*      */   public void onBlockEntityAdded(BlockEntity blockEntity) {
/* 1886 */     super.onBlockEntityAdded(blockEntity);
/* 1887 */     this.debugSynchronizers.registerBlockEntity(blockEntity);
/*      */   }
/*      */   
/*      */   public LevelDebugSynchronizers debugSynchronizers() {
/* 1891 */     return this.debugSynchronizers;
/*      */   }
/*      */   
/*      */   public boolean isAllowedToEnterPortal(Level toLevel) {
/* 1895 */     if (toLevel.dimension() == Level.NETHER)
/* 1896 */       return ((Boolean)getGameRules().get(GameRules.ALLOW_ENTERING_NETHER_USING_PORTALS)).booleanValue(); 
/* 1898 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isPvpAllowed() {
/* 1902 */     return ((Boolean)getGameRules().get(GameRules.PVP)).booleanValue();
/*      */   }
/*      */   
/*      */   public boolean isCommandBlockEnabled() {
/* 1906 */     return ((Boolean)getGameRules().get(GameRules.COMMAND_BLOCKS_WORK)).booleanValue();
/*      */   }
/*      */   
/*      */   public boolean isSpawnerBlockEnabled() {
/* 1910 */     return ((Boolean)getGameRules().get(GameRules.SPAWNER_BLOCKS_WORK)).booleanValue();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerLevel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */