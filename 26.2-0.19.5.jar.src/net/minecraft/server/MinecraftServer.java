/*      */ package net.minecraft.server;
/*      */ 
/*      */ import com.google.common.base.Splitter;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.datafixers.DataFixer;
/*      */ import com.mojang.jtracy.DiscontinuousFrame;
/*      */ import com.mojang.jtracy.TracyClient;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.lang.management.ThreadInfo;
/*      */ import java.net.Proxy;
/*      */ import java.nio.file.FileStore;
/*      */ import java.nio.file.Files;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.attribute.FileAttribute;
/*      */ import java.security.KeyPair;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.CompletableFuture;
/*      */ import java.util.concurrent.CompletionStage;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.RejectedExecutionException;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import java.util.concurrent.locks.LockSupport;
/*      */ import java.util.function.BooleanSupplier;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.LongSupplier;
/*      */ import java.util.function.Supplier;
/*      */ import java.util.stream.Collectors;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportType;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.SystemReport;
/*      */ import net.minecraft.commands.CommandSource;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.Commands;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderGetter;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.LayeredRegistryAccess;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.data.worldgen.features.MiscOverworldFeatures;
/*      */ import net.minecraft.gametest.framework.GameTestTicker;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.PacketProcessor;
/*      */ import net.minecraft.network.chat.ChatDecorator;
/*      */ import net.minecraft.network.chat.ChatType;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.PacketType;
/*      */ import net.minecraft.network.protocol.game.ClientboundChangeDifficultyPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetDefaultSpawnPositionPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
/*      */ import net.minecraft.network.protocol.status.ServerStatus;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.bossevents.CustomBossEvents;
/*      */ import net.minecraft.server.level.ChunkLoadCounter;
/*      */ import net.minecraft.server.level.ChunkMap;
/*      */ import net.minecraft.server.level.DemoMode;
/*      */ import net.minecraft.server.level.PlayerSpawnFinder;
/*      */ import net.minecraft.server.level.ServerChunkCache;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.level.ServerPlayerGameMode;
/*      */ import net.minecraft.server.level.progress.ChunkLoadStatusView;
/*      */ import net.minecraft.server.level.progress.LevelLoadListener;
/*      */ import net.minecraft.server.network.ServerConnectionListener;
/*      */ import net.minecraft.server.network.TextFilter;
/*      */ import net.minecraft.server.notifications.NotificationManager;
/*      */ import net.minecraft.server.notifications.ServerActivityMonitor;
/*      */ import net.minecraft.server.packs.PackType;
/*      */ import net.minecraft.server.packs.repository.Pack;
/*      */ import net.minecraft.server.packs.repository.PackRepository;
/*      */ import net.minecraft.server.packs.repository.PackSource;
/*      */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*      */ import net.minecraft.server.packs.resources.MultiPackResourceManager;
/*      */ import net.minecraft.server.packs.resources.ResourceManager;
/*      */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.server.players.NameAndId;
/*      */ import net.minecraft.server.players.PlayerList;
/*      */ import net.minecraft.server.players.ServerOpListEntry;
/*      */ import net.minecraft.server.players.UserWhiteList;
/*      */ import net.minecraft.server.waypoints.ServerWaypointManager;
/*      */ import net.minecraft.tags.TagLoader;
/*      */ import net.minecraft.util.Crypt;
/*      */ import net.minecraft.util.CryptException;
/*      */ import net.minecraft.util.FileUtil;
/*      */ import net.minecraft.util.ModCheck;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.NativeModuleLister;
/*      */ import net.minecraft.util.PngInfo;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.TimeUtil;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.debug.ServerDebugSubscribers;
/*      */ import net.minecraft.util.debugchart.SampleLogger;
/*      */ import net.minecraft.util.debugchart.TpsDebugDimensions;
/*      */ import net.minecraft.util.profiling.EmptyProfileResults;
/*      */ import net.minecraft.util.profiling.ProfileResults;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.util.profiling.ResultField;
/*      */ import net.minecraft.util.profiling.SingleTickProfiler;
/*      */ import net.minecraft.util.profiling.jfr.Environment;
/*      */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*      */ import net.minecraft.util.profiling.jfr.callback.ProfiledDuration;
/*      */ import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
/*      */ import net.minecraft.util.profiling.metrics.profiling.ActiveMetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.profiling.InactiveMetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.profiling.MetricsRecorder;
/*      */ import net.minecraft.util.profiling.metrics.profiling.ServerMetricsSamplersProvider;
/*      */ import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
/*      */ import net.minecraft.util.thread.ReentrantBlockableEventLoop;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.RandomSequences;
/*      */ import net.minecraft.world.Stopwatches;
/*      */ import net.minecraft.world.clock.ClockTimeMarkers;
/*      */ import net.minecraft.world.clock.ServerClockManager;
/*      */ import net.minecraft.world.clock.WorldClocks;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.ai.village.VillageSiege;
/*      */ import net.minecraft.world.entity.npc.CatSpawner;
/*      */ import net.minecraft.world.entity.npc.wanderingtrader.WanderingTraderSpawner;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.flag.FeatureFlagSet;
/*      */ import net.minecraft.world.flag.FeatureFlags;
/*      */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*      */ import net.minecraft.world.item.crafting.RecipeManager;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.DataPackConfig;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelHeightAccessor;
/*      */ import net.minecraft.world.level.LevelSettings;
/*      */ import net.minecraft.world.level.TicketStorage;
/*      */ import net.minecraft.world.level.WorldDataConfiguration;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.biome.BiomeManager;
/*      */ import net.minecraft.world.level.block.entity.FuelValues;
/*      */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*      */ import net.minecraft.world.level.chunk.storage.ChunkIOErrorReporter;
/*      */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*      */ import net.minecraft.world.level.dimension.LevelStem;
/*      */ import net.minecraft.world.level.gamerules.GameRule;
/*      */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*      */ import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.levelgen.PatrolSpawner;
/*      */ import net.minecraft.world.level.levelgen.PhantomSpawner;
/*      */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*      */ import net.minecraft.world.level.levelgen.WorldOptions;
/*      */ import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
/*      */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*      */ import net.minecraft.world.level.saveddata.SavedData;
/*      */ import net.minecraft.world.level.saveddata.WeatherData;
/*      */ import net.minecraft.world.level.storage.CommandStorage;
/*      */ import net.minecraft.world.level.storage.DerivedLevelData;
/*      */ import net.minecraft.world.level.storage.LevelData;
/*      */ import net.minecraft.world.level.storage.LevelResource;
/*      */ import net.minecraft.world.level.storage.LevelStorageSource;
/*      */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*      */ import net.minecraft.world.level.storage.SavedDataStorage;
/*      */ import net.minecraft.world.level.storage.ServerLevelData;
/*      */ import net.minecraft.world.level.storage.WorldData;
/*      */ import net.minecraft.world.level.timers.TimerQueue;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.ScoreboardSaveData;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public abstract class MinecraftServer extends ReentrantBlockableEventLoop<TickTask> implements CommandSource, ServerInfo, ChunkIOErrorReporter {
/*  196 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   public static final String VANILLA_BRAND = "vanilla";
/*      */   
/*      */   private static final float AVERAGE_TICK_TIME_SMOOTHING = 0.8F;
/*      */   
/*      */   private static final int TICK_STATS_SPAN = 100;
/*      */   
/*  200 */   private static final long OVERLOADED_THRESHOLD_NANOS = 20L * TimeUtil.NANOSECONDS_PER_SECOND / 20L;
/*      */   
/*      */   private static final int OVERLOADED_TICKS_THRESHOLD = 20;
/*      */   
/*  202 */   private static final long OVERLOADED_WARNING_INTERVAL_NANOS = 10L * TimeUtil.NANOSECONDS_PER_SECOND;
/*      */   
/*      */   private static final int OVERLOADED_TICKS_WARNING_INTERVAL = 100;
/*      */   
/*  204 */   private static final long STATUS_EXPIRE_TIME_NANOS = 5L * TimeUtil.NANOSECONDS_PER_SECOND;
/*      */   
/*  205 */   private static final long PREPARE_LEVELS_DEFAULT_DELAY_NANOS = 10L * TimeUtil.NANOSECONDS_PER_MILLISECOND;
/*      */   
/*      */   private static final int MAX_STATUS_PLAYER_SAMPLE = 12;
/*      */   
/*      */   public static final int SPAWN_POSITION_SEARCH_RADIUS = 5;
/*      */   
/*      */   private static final int SERVER_ACTIVITY_MONITOR_SECONDS_BETWEEN_NOTIFICATIONS = 30;
/*      */   
/*  210 */   private static final Map<String, String> LEGACY_WORLD_NAMES_FOR_REALMS_LOG = Map.of("overworld", "world", "the_nether", "DIM-1", "the_end", "DIM1");
/*      */   
/*      */   private static final int AUTOSAVE_INTERVAL = 6000;
/*      */   
/*      */   private static final int MIMINUM_AUTOSAVE_TICKS = 100;
/*      */   
/*      */   private static final int MAX_TICK_LATENCY = 3;
/*      */   
/*      */   public static final int ABSOLUTE_MAX_WORLD_SIZE = 29999984;
/*      */   
/*  222 */   public static final LevelSettings DEMO_SETTINGS = new LevelSettings("Demo World", GameType.SURVIVAL, LevelSettings.DifficultySettings.DEFAULT, false, WorldDataConfiguration.DEFAULT);
/*      */   
/*      */   public static final Supplier<GameRules> DEFAULT_GAME_RULES = () -> new GameRules(WorldDataConfiguration.DEFAULT.enabledFeatures());
/*      */   
/*  224 */   public static final NameAndId ANONYMOUS_PLAYER_PROFILE = new NameAndId(Util.NIL_UUID, "Anonymous Player");
/*      */   
/*      */   public static final String SERVER_THREAD_NAME = "Server thread";
/*      */   
/*      */   protected final LevelStorageSource.LevelStorageAccess storageSource;
/*      */   
/*      */   protected final PlayerDataStorage playerDataStorage;
/*      */   
/*      */   private final SavedDataStorage savedDataStorage;
/*      */   
/*  230 */   private final List<Runnable> tickables = Lists.newArrayList();
/*      */   
/*      */   private final GameRules gameRules;
/*      */   
/*  232 */   private MetricsRecorder metricsRecorder = InactiveMetricsRecorder.INSTANCE;
/*      */   
/*      */   private Consumer<ProfileResults> onMetricsRecordingStopped = results -> stopRecordingMetrics();
/*      */   
/*      */   private Consumer<Path> onMetricsRecordingFinished = ignored -> {
/*      */     
/*      */     };
/*      */   
/*      */   private boolean willStartRecordingMetrics;
/*      */   
/*      */   private TimeProfiler debugCommandProfiler;
/*      */   
/*      */   private boolean debugCommandProfilerDelayStart;
/*      */   
/*      */   private final ServerConnectionListener connection;
/*      */   
/*      */   private final LevelLoadListener levelLoadListener;
/*      */   
/*      */   private ServerStatus status;
/*      */   
/*      */   private ServerStatus.Favicon statusIcon;
/*      */   
/*  243 */   private final RandomSource random = RandomSource.create();
/*      */   
/*      */   private final DataFixer fixerUpper;
/*      */   
/*      */   private String localIp;
/*      */   
/*  247 */   private int port = -1;
/*      */   
/*      */   private final LayeredRegistryAccess<RegistryLayer> registries;
/*      */   
/*  249 */   private final Map<ResourceKey<Level>, ServerLevel> levels = Maps.newLinkedHashMap();
/*      */   
/*      */   private PlayerList playerList;
/*      */   
/*      */   private volatile boolean running = true;
/*      */   
/*      */   private boolean stopped;
/*      */   
/*      */   private int tickCount;
/*      */   
/*  254 */   private int ticksUntilAutosave = 6000;
/*      */   
/*      */   protected final Proxy proxy;
/*      */   
/*      */   private boolean onlineMode;
/*      */   
/*      */   private boolean preventProxyConnections;
/*      */   
/*      */   private String motd;
/*      */   
/*      */   private int playerIdleTimeout;
/*      */   
/*  262 */   private final long[] tickTimesNanos = new long[100];
/*      */   
/*  263 */   private long aggregatedTickTimesNanos = 0L;
/*      */   
/*      */   private KeyPair keyPair;
/*      */   
/*      */   private GameProfile singleplayerProfile;
/*      */   
/*      */   private boolean isDemo;
/*      */   
/*      */   private volatile boolean isReady;
/*      */   
/*      */   private long lastOverloadWarningNanos;
/*      */   
/*      */   protected final Services services;
/*      */   
/*      */   private final NotificationManager notificationManager;
/*      */   
/*      */   private final ServerActivityMonitor serverActivityMonitor;
/*      */   
/*      */   private long lastServerStatus;
/*      */   
/*      */   private final Thread serverThread;
/*      */   
/*  275 */   private long lastTickNanos = Util.getNanos();
/*      */   
/*  276 */   private long taskExecutionStartNanos = Util.getNanos();
/*      */   
/*      */   private long idleTimeNanos;
/*      */   
/*  278 */   private long nextTickTimeNanos = Util.getNanos();
/*      */   
/*      */   private boolean waitingForNextTick = false;
/*      */   
/*      */   private long delayedTasksMaxNextTickTimeNanos;
/*      */   
/*      */   private boolean mayHaveDelayedTasks;
/*      */   
/*      */   private final PackRepository packRepository;
/*      */   
/*      */   private final WorldGenSettings worldGenSettings;
/*      */   
/*  284 */   private final ServerScoreboard scoreboard = new ServerScoreboard(this);
/*      */   
/*      */   private Stopwatches stopwatches;
/*      */   
/*      */   private CommandStorage commandStorage;
/*      */   
/*      */   private final CustomBossEvents customBossEvents;
/*      */   
/*      */   private final RandomSequences randomSequences;
/*      */   
/*      */   private final WeatherData weatherData;
/*      */   
/*      */   private final ServerFunctionManager functionManager;
/*      */   
/*      */   private boolean enforceWhitelist;
/*      */   
/*      */   private boolean usingWhitelist;
/*      */   
/*      */   private float smoothedTickTimeMillis;
/*      */   
/*      */   private final Executor executor;
/*      */   
/*      */   private String serverId;
/*      */   
/*      */   private ReloadableResources resources;
/*      */   
/*      */   private final StructureTemplateManager structureTemplateManager;
/*      */   
/*      */   private final ServerTickRateManager tickRateManager;
/*      */   
/*  300 */   private final ServerDebugSubscribers debugSubscribers = new ServerDebugSubscribers(this);
/*      */   
/*      */   protected final WorldData worldData;
/*      */   
/*  303 */   private LevelData.RespawnData effectiveRespawnData = LevelData.RespawnData.DEFAULT;
/*      */   
/*      */   private final PotionBrewing potionBrewing;
/*      */   
/*      */   private FuelValues fuelValues;
/*      */   
/*      */   private int emptyTicks;
/*      */   
/*      */   private volatile boolean isSaving;
/*      */   
/*  309 */   private final SuppressedExceptionCollector suppressedExceptions = new SuppressedExceptionCollector();
/*      */   
/*      */   private final DiscontinuousFrame tickFrame;
/*      */   
/*      */   private final PacketProcessor packetProcessor;
/*      */   
/*      */   private final TimerQueue<MinecraftServer> scheduledEvents;
/*      */   
/*      */   private final ServerClockManager clockManager;
/*      */   
/*      */   public static <S extends MinecraftServer> S spin(Function<Thread, S> factory) {
/*  318 */     AtomicReference<S> serverReference = new AtomicReference<>();
/*  320 */     Thread thread = new Thread(() -> ((MinecraftServer)serverReference.get()).runServer(), "Server thread");
/*  321 */     thread.setUncaughtExceptionHandler((t, e) -> LOGGER.error("Uncaught exception in server thread", e));
/*  323 */     if (Runtime.getRuntime().availableProcessors() > 4)
/*  324 */       thread.setPriority(8); 
/*  327 */     MinecraftServer minecraftServer = (MinecraftServer)factory.apply(thread);
/*  328 */     serverReference.set((S)minecraftServer);
/*  329 */     thread.start();
/*  330 */     return (S)minecraftServer;
/*      */   }
/*      */   
/*      */   public MinecraftServer(Thread serverThread, LevelStorageSource.LevelStorageAccess storageSource, PackRepository packRepository, WorldStem worldStem, Optional<GameRules> gameRules, Proxy proxy, DataFixer fixerUpper, Services services, LevelLoadListener levelLoadListener, boolean propagatesCrashes, NotificationManager notificationManager) {
/*  334 */     super("Server", propagatesCrashes);
/*  335 */     this.registries = worldStem.registries();
/*  337 */     if (!this.registries.compositeAccess().lookupOrThrow(Registries.LEVEL_STEM).containsKey(LevelStem.OVERWORLD))
/*  338 */       throw new IllegalStateException("Missing Overworld dimension data"); 
/*  340 */     this.savedDataStorage = new SavedDataStorage(storageSource.getLevelPath(LevelResource.DATA), fixerUpper, (HolderLookup.Provider)this.registries.compositeAccess());
/*  341 */     this.worldData = worldStem.worldDataAndGenSettings().data();
/*  342 */     this.worldGenSettings = worldStem.worldDataAndGenSettings().genSettings();
/*  343 */     this.savedDataStorage.set(WorldGenSettings.TYPE, (SavedData)this.worldGenSettings);
/*  345 */     this.proxy = proxy;
/*  346 */     this.packRepository = packRepository;
/*  347 */     this.resources = new ReloadableResources(worldStem.resourceManager(), worldStem.dataPackResources());
/*  348 */     this.services = services;
/*  349 */     this.connection = new ServerConnectionListener(this);
/*  350 */     this.tickRateManager = new ServerTickRateManager(this);
/*  351 */     this.levelLoadListener = levelLoadListener;
/*  352 */     this.storageSource = storageSource;
/*  353 */     this.playerDataStorage = storageSource.createPlayerStorage();
/*  354 */     this.randomSequences = (RandomSequences)this.savedDataStorage.computeIfAbsent(RandomSequences.TYPE);
/*  355 */     this.weatherData = (WeatherData)getDataStorage().computeIfAbsent(WeatherData.TYPE);
/*  356 */     this.gameRules = new GameRules(this.worldData.enabledFeatures(), (GameRuleMap)this.savedDataStorage.computeIfAbsent(GameRuleMap.TYPE));
/*  357 */     gameRules.ifPresent(g -> this.gameRules.setAll(g, null));
/*  358 */     this.fixerUpper = fixerUpper;
/*  359 */     this.functionManager = new ServerFunctionManager(this, this.resources.managers.getFunctionLibrary());
/*  360 */     HolderLookup.RegistryLookup registryLookup = this.registries.compositeAccess().lookupOrThrow(Registries.BLOCK).filterFeatures(this.worldData.enabledFeatures());
/*  361 */     this.structureTemplateManager = new StructureTemplateManager((ResourceManager)worldStem.resourceManager(), storageSource, fixerUpper, (HolderGetter)registryLookup);
/*  362 */     this.serverThread = serverThread;
/*  363 */     this.executor = (Executor)Util.backgroundExecutor();
/*  364 */     this.potionBrewing = PotionBrewing.bootstrap(this.worldData.enabledFeatures());
/*  365 */     this.resources.managers.getRecipeManager().finalizeRecipeLoading(this.worldData.enabledFeatures());
/*  366 */     this.fuelValues = FuelValues.vanillaBurnTimes((HolderLookup.Provider)this.registries.compositeAccess(), this.worldData.enabledFeatures());
/*  367 */     this.tickFrame = TracyClient.createDiscontinuousFrame("Server Tick");
/*  368 */     this.notificationManager = notificationManager;
/*  369 */     this.serverActivityMonitor = new ServerActivityMonitor(notificationManager, 30);
/*  370 */     this.packetProcessor = new PacketProcessor(serverThread);
/*  371 */     this.clockManager = (ServerClockManager)getDataStorage().computeIfAbsent(ServerClockManager.TYPE);
/*  372 */     this.clockManager.init(this);
/*  373 */     this.customBossEvents = (CustomBossEvents)this.savedDataStorage.computeIfAbsent(CustomBossEvents.TYPE);
/*  374 */     this.scheduledEvents = (TimerQueue<MinecraftServer>)this.savedDataStorage.computeIfAbsent(TimerQueue.TYPE);
/*      */   }
/*      */   
/*      */   public ChunkLoadStatusView createChunkLoadStatusView(final int radius) {
/*  380 */     return new ChunkLoadStatusView() {
/*      */         private ChunkMap chunkMap;
/*      */         
/*      */         private int centerChunkX;
/*      */         
/*      */         private int centerChunkZ;
/*      */         
/*      */         public void moveTo(ResourceKey<Level> dimension, ChunkPos centerChunk) {
/*  387 */           ServerLevel level = MinecraftServer.this.getLevel(dimension);
/*  388 */           this.chunkMap = (level != null) ? (level.getChunkSource()).chunkMap : null;
/*  389 */           this.centerChunkX = centerChunk.x();
/*  390 */           this.centerChunkZ = centerChunk.z();
/*      */         }
/*      */         
/*      */         public ChunkStatus get(int x, int z) {
/*  395 */           if (this.chunkMap == null)
/*  396 */             return null; 
/*  398 */           return this.chunkMap.getLatestStatus(ChunkPos.pack(x + this.centerChunkX - radius, z + this.centerChunkZ - radius));
/*      */         }
/*      */         
/*      */         public int radius() {
/*  406 */           return radius;
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   protected void loadLevel() {
/*  414 */     boolean startedWorldLoadProfiling = (!JvmProfiler.INSTANCE.isRunning() && SharedConstants.DEBUG_JFR_PROFILING_ENABLE_LEVEL_LOADING && JvmProfiler.INSTANCE.start(Environment.from(this)));
/*  416 */     ProfiledDuration profiledDuration = JvmProfiler.INSTANCE.onWorldLoadedStarted();
/*  418 */     this.worldData.setModdedInfo(getServerModName(), getModdedStatus().shouldReportAsModified());
/*  420 */     createLevels();
/*  422 */     forceDifficulty();
/*  423 */     prepareLevels();
/*  424 */     if (profiledDuration != null)
/*  425 */       profiledDuration.finish(true); 
/*  428 */     if (startedWorldLoadProfiling)
/*      */       try {
/*  430 */         JvmProfiler.INSTANCE.stop();
/*  431 */       } catch (Throwable t) {
/*  432 */         LOGGER.warn("Failed to stop JFR profiling", t);
/*      */       }  
/*      */   }
/*      */   
/*      */   protected void forceDifficulty() {}
/*      */   
/*      */   protected void createLevels() {
/*  441 */     ServerLevelData levelData = this.worldData.overworldData();
/*  442 */     boolean isDebug = this.worldData.isDebugWorld();
/*  443 */     Registry<LevelStem> dimensions = this.registries.compositeAccess().lookupOrThrow(Registries.LEVEL_STEM);
/*  444 */     WorldOptions worldOptions = this.worldGenSettings.options();
/*  445 */     long seed = worldOptions.seed();
/*  446 */     long biomeZoomSeed = BiomeManager.obfuscateSeed(seed);
/*  447 */     ImmutableList immutableList = ImmutableList.of(new PhantomSpawner(), new PatrolSpawner(), new CatSpawner(), new VillageSiege(), new WanderingTraderSpawner(this.savedDataStorage));
/*  454 */     LevelStem overworldData = (LevelStem)dimensions.getValue(LevelStem.OVERWORLD);
/*  455 */     ServerLevel overworld = new ServerLevel(this, this.executor, this.storageSource, levelData, Level.OVERWORLD, overworldData, isDebug, biomeZoomSeed, (List)immutableList, true);
/*  456 */     this.levels.put(Level.OVERWORLD, overworld);
/*  457 */     this.scoreboard.load(((ScoreboardSaveData)this.savedDataStorage.computeIfAbsent(ScoreboardSaveData.TYPE)).getData());
/*  458 */     this.commandStorage = new CommandStorage(this.savedDataStorage);
/*  460 */     this.stopwatches = (Stopwatches)this.savedDataStorage.computeIfAbsent(Stopwatches.TYPE);
/*  462 */     if (!levelData.isInitialized()) {
/*      */       try {
/*  464 */         setInitialSpawn(overworld, levelData, worldOptions.generateBonusChest(), isDebug, this.levelLoadListener);
/*  465 */         levelData.setInitialized(true);
/*  466 */         if (isDebug)
/*  467 */           setupDebugLevel(this.worldData); 
/*  469 */       } catch (Throwable t) {
/*  470 */         CrashReport report = CrashReport.forThrowable(t, "Exception initializing level");
/*      */         try {
/*  472 */           overworld.fillReportDetails(report);
/*  473 */         } catch (Throwable throwable) {}
/*  476 */         throw new ReportedException(report);
/*      */       } 
/*  478 */       levelData.setInitialized(true);
/*      */     } 
/*  481 */     GlobalPos focusPos = selectLevelLoadFocusPos();
/*  482 */     this.levelLoadListener.updateFocus(focusPos.dimension(), ChunkPos.containing(focusPos.pos()));
/*  484 */     for (Map.Entry<ResourceKey<LevelStem>, LevelStem> entry : (Iterable<Map.Entry<ResourceKey<LevelStem>, LevelStem>>)dimensions.entrySet()) {
/*      */       ServerLevel level;
/*  485 */       ResourceKey<LevelStem> name = entry.getKey();
/*  487 */       if (name != LevelStem.OVERWORLD) {
/*  488 */         ResourceKey<Level> dimension = ResourceKey.create(Registries.DIMENSION, name.identifier());
/*  489 */         DerivedLevelData derivedLevelData = new DerivedLevelData(this.worldData, levelData);
/*  490 */         level = new ServerLevel(this, this.executor, this.storageSource, (ServerLevelData)derivedLevelData, dimension, entry.getValue(), isDebug, biomeZoomSeed, (List)ImmutableList.of(), false);
/*  491 */         this.levels.put(dimension, level);
/*      */       } else {
/*  493 */         level = overworld;
/*      */       } 
/*  495 */       level.getWorldBorder().setAbsoluteMaxSize(getAbsoluteMaxWorldSize());
/*  496 */       getPlayerList().addWorldborderListener(level);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void setInitialSpawn(ServerLevel level, ServerLevelData levelData, boolean spawnBonusChest, boolean isDebug, LevelLoadListener levelLoadListener) {
/*  501 */     if (SharedConstants.DEBUG_ONLY_GENERATE_HALF_THE_WORLD && SharedConstants.DEBUG_WORLD_RECREATE) {
/*  502 */       levelData.setSpawn(LevelData.RespawnData.of(level.dimension(), new BlockPos(0, 64, -100), 0.0F, 0.0F));
/*      */       return;
/*      */     } 
/*  506 */     if (isDebug) {
/*  507 */       levelData.setSpawn(LevelData.RespawnData.of(level.dimension(), BlockPos.ZERO.above(80), 0.0F, 0.0F));
/*      */       return;
/*      */     } 
/*  511 */     ServerChunkCache chunkSource = level.getChunkSource();
/*  512 */     ChunkPos spawnChunk = ChunkPos.containing(chunkSource.randomState().sampler().findSpawnPosition());
/*  514 */     levelLoadListener.start(LevelLoadListener.Stage.PREPARE_GLOBAL_SPAWN, 0);
/*  515 */     levelLoadListener.updateFocus(level.dimension(), spawnChunk);
/*  517 */     int height = chunkSource.getGenerator().getSpawnHeight((LevelHeightAccessor)level);
/*  519 */     if (height < level.getMinY()) {
/*  520 */       BlockPos worldPosition = spawnChunk.getWorldPosition();
/*  521 */       height = level.getHeight(Heightmap.Types.WORLD_SURFACE, worldPosition.getX() + 8, worldPosition.getZ() + 8);
/*      */     } 
/*  523 */     levelData.setSpawn(LevelData.RespawnData.of(level.dimension(), spawnChunk.getWorldPosition().offset(8, height, 8), 0.0F, 0.0F));
/*  525 */     int xChunkOffset = 0;
/*  526 */     int zChunkOffset = 0;
/*  527 */     int dXChunk = 0;
/*  528 */     int dZChunk = -1;
/*  529 */     for (int i = 0; i < Mth.square(11); i++) {
/*  530 */       if (xChunkOffset >= -5 && xChunkOffset <= 5 && zChunkOffset >= -5 && zChunkOffset <= 5) {
/*  531 */         BlockPos testedPos = PlayerSpawnFinder.getSpawnPosInChunk(level, new ChunkPos(spawnChunk.x() + xChunkOffset, spawnChunk.z() + zChunkOffset));
/*  532 */         if (testedPos != null) {
/*  533 */           levelData.setSpawn(LevelData.RespawnData.of(level.dimension(), testedPos, 0.0F, 0.0F));
/*      */           break;
/*      */         } 
/*      */       } 
/*  537 */       if (xChunkOffset == zChunkOffset || (xChunkOffset < 0 && xChunkOffset == -zChunkOffset) || (xChunkOffset > 0 && xChunkOffset == 1 - zChunkOffset)) {
/*  538 */         int olddx = dXChunk;
/*  539 */         dXChunk = -dZChunk;
/*  540 */         dZChunk = olddx;
/*      */       } 
/*  542 */       xChunkOffset += dXChunk;
/*  543 */       zChunkOffset += dZChunk;
/*      */     } 
/*  546 */     if (spawnBonusChest)
/*  549 */       level.registryAccess()
/*  550 */         .lookup(Registries.CONFIGURED_FEATURE)
/*  551 */         .flatMap(registry -> registry.get(MiscOverworldFeatures.BONUS_CHEST))
/*  552 */         .ifPresent(feature -> ((ConfiguredFeature)feature.value()).place((WorldGenLevel)level, chunkSource.getGenerator(), level.getRandom(), levelData.getRespawnData().pos())); 
/*  555 */     levelLoadListener.finish(LevelLoadListener.Stage.PREPARE_GLOBAL_SPAWN);
/*      */   }
/*      */   
/*      */   private void setupDebugLevel(WorldData worldData) {
/*  559 */     worldData.setDifficulty(Difficulty.PEACEFUL);
/*  560 */     worldData.setDifficultyLocked(true);
/*  562 */     ServerLevelData levelData = worldData.overworldData();
/*  563 */     getGameRules().set(GameRules.ADVANCE_WEATHER, Boolean.valueOf(false), this);
/*  564 */     this.clockManager.moveToTimeMarker((Holder)registryAccess().getOrThrow(WorldClocks.OVERWORLD), ClockTimeMarkers.NOON);
/*  565 */     levelData.setGameType(GameType.SPECTATOR);
/*      */   }
/*      */   
/*      */   private void prepareLevels() {
/*  569 */     ChunkLoadCounter chunkLoadCounter = new ChunkLoadCounter();
/*  571 */     for (ServerLevel level : this.levels.values()) {
/*  572 */       chunkLoadCounter.track(level, () -> {
/*      */             TicketStorage savedTickets = (TicketStorage)level.getDataStorage().get(TicketStorage.TYPE);
/*      */             if (savedTickets != null)
/*      */               savedTickets.activateAllDeactivatedTickets(); 
/*      */           });
/*      */     } 
/*  580 */     this.levelLoadListener.start(LevelLoadListener.Stage.LOAD_INITIAL_CHUNKS, chunkLoadCounter.totalChunks());
/*      */     do {
/*  583 */       this.levelLoadListener.update(LevelLoadListener.Stage.LOAD_INITIAL_CHUNKS, chunkLoadCounter
/*      */           
/*  585 */           .readyChunks(), chunkLoadCounter
/*  586 */           .totalChunks());
/*  589 */       this.nextTickTimeNanos = Util.getNanos() + PREPARE_LEVELS_DEFAULT_DELAY_NANOS;
/*  590 */       waitUntilNextTick();
/*  591 */     } while (chunkLoadCounter.pendingChunks() > 0);
/*  593 */     this.levelLoadListener.finish(LevelLoadListener.Stage.LOAD_INITIAL_CHUNKS);
/*  595 */     updateMobSpawningFlags();
/*  596 */     updateEffectiveRespawnData();
/*      */   }
/*      */   
/*      */   protected GlobalPos selectLevelLoadFocusPos() {
/*  600 */     return this.worldData.overworldData().getRespawnData().globalPos();
/*      */   }
/*      */   
/*      */   public GameType getDefaultGameType() {
/*  604 */     return this.worldData.getGameType();
/*      */   }
/*      */   
/*      */   public boolean isHardcore() {
/*  608 */     return this.worldData.isHardcore();
/*      */   }
/*      */   
/*      */   public boolean saveAllChunks(boolean silent, boolean flush, boolean force) {
/*  618 */     this.scoreboard.storeToSaveDataIfDirty((ScoreboardSaveData)getDataStorage().computeIfAbsent(ScoreboardSaveData.TYPE));
/*  620 */     boolean result = false;
/*  621 */     for (ServerLevel level : getAllLevels()) {
/*  622 */       if (!silent)
/*  623 */         LOGGER.info("Saving chunks for level '{}'/{}", level, level.dimension().identifier()); 
/*  625 */       level.save(null, flush, (SharedConstants.DEBUG_DONT_SAVE_WORLD || (level.noSave && !force)));
/*  626 */       result = true;
/*      */     } 
/*  628 */     GameProfile singleplayerProfile = getSingleplayerProfile();
/*  629 */     this.storageSource.saveDataTag(this.worldData, (singleplayerProfile == null) ? null : singleplayerProfile.id());
/*  630 */     if (flush) {
/*  631 */       this.savedDataStorage.saveAndJoin();
/*      */     } else {
/*  633 */       this.savedDataStorage.scheduleSave();
/*      */     } 
/*  636 */     if (flush) {
/*  638 */       for (ServerLevel level : getAllLevels()) {
/*  639 */         String storageName = (level.getChunkSource()).chunkMap.getStorageName();
/*  640 */         LOGGER.info("ThreadedAnvilChunkStorage ({}): All chunks are saved", LEGACY_WORLD_NAMES_FOR_REALMS_LOG.getOrDefault(storageName, storageName));
/*      */       } 
/*  642 */       LOGGER.info("ThreadedAnvilChunkStorage: All dimensions are saved");
/*      */     } 
/*  645 */     return result;
/*      */   }
/*      */   
/*      */   public boolean saveEverything(boolean silent, boolean flush, boolean force) {
/*      */     try {
/*  650 */       this.isSaving = true;
/*  651 */       getPlayerList().saveAll();
/*  652 */       boolean result = saveAllChunks(silent, flush, force);
/*  653 */       warnOnLowDiskSpace();
/*  654 */       return result;
/*      */     } finally {
/*  656 */       this.isSaving = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void close() {
/*  662 */     stopServer();
/*      */   }
/*      */   
/*      */   protected void stopServer() {
/*  666 */     this.packetProcessor.close();
/*  668 */     if (this.metricsRecorder.isRecording())
/*  669 */       cancelRecordingMetrics(); 
/*  672 */     LOGGER.info("Stopping server");
/*  673 */     getConnection().stop();
/*  674 */     this.isSaving = true;
/*  675 */     if (this.playerList != null) {
/*  676 */       LOGGER.info("Saving players");
/*  677 */       this.playerList.saveAll();
/*  678 */       this.playerList.removeAll();
/*      */     } 
/*  680 */     LOGGER.info("Saving worlds");
/*  681 */     for (ServerLevel level : getAllLevels()) {
/*  682 */       if (level != null)
/*  683 */         level.noSave = false; 
/*      */     } 
/*  687 */     while (this.levels.values().stream().anyMatch(l -> (l.getChunkSource()).chunkMap.hasWork())) {
/*  688 */       this.nextTickTimeNanos = Util.getNanos() + TimeUtil.NANOSECONDS_PER_MILLISECOND;
/*  689 */       for (ServerLevel level : getAllLevels()) {
/*  690 */         level.getChunkSource().deactivateTicketsOnClosing();
/*  691 */         level.getChunkSource().tick(() -> true, false);
/*      */       } 
/*  693 */       waitUntilNextTick();
/*      */     } 
/*  696 */     saveAllChunks(false, true, false);
/*  697 */     for (ServerLevel level : getAllLevels()) {
/*  698 */       if (level != null)
/*      */         try {
/*  700 */           level.close();
/*  701 */         } catch (IOException e) {
/*  702 */           LOGGER.error("Exception closing the level", e);
/*      */         }  
/*      */     } 
/*  706 */     this.isSaving = false;
/*  707 */     this.savedDataStorage.close();
/*  708 */     this.resources.close();
/*      */     try {
/*  710 */       this.storageSource.close();
/*  711 */     } catch (IOException e) {
/*  712 */       LOGGER.error("Failed to unlock level {}", this.storageSource.getLevelId(), e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public String getLocalIp() {
/*  717 */     return this.localIp;
/*      */   }
/*      */   
/*      */   public void setLocalIp(String ip) {
/*  721 */     this.localIp = ip;
/*      */   }
/*      */   
/*      */   public boolean isRunning() {
/*  725 */     return this.running;
/*      */   }
/*      */   
/*      */   public void halt(boolean wait) {
/*  729 */     this.running = false;
/*  730 */     if (wait)
/*      */       try {
/*  732 */         this.serverThread.join();
/*  733 */       } catch (InterruptedException e) {
/*  734 */         LOGGER.error("Error while shutting down", e);
/*      */       }  
/*      */   }
/*      */   
/*      */   protected void runServer() {
/*      */     try {
/*  741 */       if (initServer()) {
/*  742 */         this.nextTickTimeNanos = Util.getNanos();
/*  744 */         this.statusIcon = loadStatusIcon().orElse(null);
/*  745 */         this.status = buildServerStatus();
/*  747 */         while (this.running) {
/*      */           long thisTickNanos;
/*  749 */           if (!isPaused() && this.tickRateManager.isSprinting() && this.tickRateManager.checkShouldSprintThisTick()) {
/*  750 */             thisTickNanos = 0L;
/*  752 */             this.nextTickTimeNanos = Util.getNanos();
/*  753 */             this.lastOverloadWarningNanos = this.nextTickTimeNanos;
/*      */           } else {
/*  755 */             thisTickNanos = this.tickRateManager.nanosecondsPerTick();
/*  756 */             long behindTimeNanos = Util.getNanos() - this.nextTickTimeNanos;
/*  757 */             if (behindTimeNanos > OVERLOADED_THRESHOLD_NANOS + 20L * thisTickNanos && this.nextTickTimeNanos - this.lastOverloadWarningNanos >= OVERLOADED_WARNING_INTERVAL_NANOS + 100L * thisTickNanos) {
/*  758 */               long ticks = behindTimeNanos / thisTickNanos;
/*  759 */               LOGGER.warn("Can't keep up! Is the server overloaded? Running {}ms or {} ticks behind", Long.valueOf(behindTimeNanos / TimeUtil.NANOSECONDS_PER_MILLISECOND), Long.valueOf(ticks));
/*  760 */               this.nextTickTimeNanos += ticks * thisTickNanos;
/*  761 */               this.lastOverloadWarningNanos = this.nextTickTimeNanos;
/*      */             } 
/*      */           } 
/*  764 */           boolean sprinting = (thisTickNanos == 0L);
/*  766 */           if (this.debugCommandProfilerDelayStart) {
/*  767 */             this.debugCommandProfilerDelayStart = false;
/*  768 */             this.debugCommandProfiler = new TimeProfiler(Util.getNanos(), this.tickCount);
/*      */           } 
/*  771 */           this.nextTickTimeNanos += thisTickNanos;
/*      */           try {
/*  772 */             Profiler.Scope ignored = Profiler.use(createProfiler());
/*      */             try {
/*  773 */               processPacketsAndTick(sprinting);
/*  774 */               ProfilerFiller profiler = Profiler.get();
/*  775 */               profiler.push("nextTickWait");
/*  776 */               this.mayHaveDelayedTasks = true;
/*  777 */               this.delayedTasksMaxNextTickTimeNanos = Math.max(Util.getNanos() + thisTickNanos, this.nextTickTimeNanos);
/*  778 */               startMeasuringTaskExecutionTime();
/*  779 */               waitUntilNextTick();
/*  780 */               finishMeasuringTaskExecutionTime();
/*  781 */               if (sprinting)
/*  782 */                 this.tickRateManager.endTickWork(); 
/*  784 */               profiler.pop();
/*  785 */               logFullTickTime();
/*  786 */               if (ignored != null)
/*  786 */                 ignored.close(); 
/*      */             } catch (Throwable throwable) {
/*      */               if (ignored != null)
/*      */                 try {
/*      */                   ignored.close();
/*      */                 } catch (Throwable throwable1) {
/*      */                   throwable.addSuppressed(throwable1);
/*      */                 }  
/*      */               throw throwable;
/*      */             } 
/*      */           } finally {
/*  787 */             endMetricsRecordingTick();
/*      */           } 
/*  790 */           this.isReady = true;
/*  792 */           JvmProfiler.INSTANCE.onServerTick(this.smoothedTickTimeMillis);
/*      */         } 
/*      */       } else {
/*  795 */         throw new IllegalStateException("Failed to initialize server");
/*      */       } 
/*  797 */     } catch (Throwable t) {
/*  798 */       LOGGER.error("Encountered an unexpected exception", t);
/*  799 */       CrashReport report = constructOrExtractCrashReport(t);
/*  800 */       fillSystemReport(report.getSystemReport());
/*  802 */       Path file = getServerDirectory().resolve("crash-reports").resolve("crash-" + Util.getFilenameFormattedDateTime() + "-server.txt");
/*  804 */       if (report.saveToFile(file, ReportType.CRASH)) {
/*  805 */         LOGGER.error("This crash report has been saved to: {}", file.toAbsolutePath());
/*      */       } else {
/*  807 */         LOGGER.error("We were unable to save this crash report to disk.");
/*      */       } 
/*  810 */       onServerCrash(report);
/*      */     } finally {
/*      */       try {
/*  813 */         this.stopped = true;
/*  814 */         stopServer();
/*  815 */       } catch (Throwable t) {
/*  816 */         LOGGER.error("Exception stopping the server", t);
/*      */       } finally {
/*  818 */         onServerExit();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void logFullTickTime() {
/*  824 */     long currentTime = Util.getNanos();
/*  825 */     if (isTickTimeLoggingEnabled())
/*  826 */       getTickTimeLogger().logSample(currentTime - this.lastTickNanos); 
/*  828 */     this.lastTickNanos = currentTime;
/*      */   }
/*      */   
/*      */   private void startMeasuringTaskExecutionTime() {
/*  832 */     if (isTickTimeLoggingEnabled()) {
/*  833 */       this.taskExecutionStartNanos = Util.getNanos();
/*  834 */       this.idleTimeNanos = 0L;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void finishMeasuringTaskExecutionTime() {
/*  839 */     if (isTickTimeLoggingEnabled()) {
/*  840 */       SampleLogger tickTimelogger = getTickTimeLogger();
/*  841 */       tickTimelogger.logPartialSample(Util.getNanos() - this.taskExecutionStartNanos - this.idleTimeNanos, TpsDebugDimensions.SCHEDULED_TASKS.ordinal());
/*  842 */       tickTimelogger.logPartialSample(this.idleTimeNanos, TpsDebugDimensions.IDLE.ordinal());
/*      */     } 
/*      */   }
/*      */   
/*      */   private static CrashReport constructOrExtractCrashReport(Throwable t) {
/*      */     CrashReport report;
/*  847 */     ReportedException firstReported = null;
/*  848 */     Throwable cause = t;
/*  849 */     while (cause != null) {
/*  850 */       if (cause instanceof ReportedException) {
/*  850 */         ReportedException reportedException = (ReportedException)cause;
/*  851 */         firstReported = reportedException;
/*      */       } 
/*  853 */       cause = cause.getCause();
/*      */     } 
/*  857 */     if (firstReported != null) {
/*  858 */       report = firstReported.getReport();
/*  859 */       if (firstReported != t)
/*  860 */         report.addCategory("Wrapped in").setDetailError("Wrapping exception", t); 
/*      */     } else {
/*  863 */       report = new CrashReport("Exception in server tick loop", t);
/*      */     } 
/*  865 */     return report;
/*      */   }
/*      */   
/*      */   private boolean haveTime() {
/*  869 */     return (runningTask() || Util.getNanos() < (this.mayHaveDelayedTasks ? this.delayedTasksMaxNextTickTimeNanos : this.nextTickTimeNanos));
/*      */   }
/*      */   
/*      */   public NotificationManager notificationManager() {
/*  873 */     return this.notificationManager;
/*      */   }
/*      */   
/*      */   protected void waitUntilNextTick() {
/*  877 */     runAllTasks();
/*  878 */     this.waitingForNextTick = true;
/*      */     try {
/*  880 */       managedBlock(() -> !haveTime());
/*      */     } finally {
/*  882 */       this.waitingForNextTick = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void waitForTasks() {
/*  888 */     boolean shouldLogTime = isTickTimeLoggingEnabled();
/*  889 */     long waitStart = shouldLogTime ? Util.getNanos() : 0L;
/*  890 */     long waitNanos = this.waitingForNextTick ? (this.nextTickTimeNanos - Util.getNanos()) : 100000L;
/*  891 */     LockSupport.parkNanos("waiting for tasks", waitNanos);
/*  892 */     if (shouldLogTime)
/*  893 */       this.idleTimeNanos += Util.getNanos() - waitStart; 
/*      */   }
/*      */   
/*      */   public TickTask wrapRunnable(Runnable runnable) {
/*  899 */     return new TickTask(this.tickCount, runnable);
/*      */   }
/*      */   
/*      */   protected boolean shouldRun(TickTask task) {
/*  904 */     return (task.getTick() + 3 < this.tickCount || haveTime());
/*      */   }
/*      */   
/*      */   protected boolean pollTask() {
/*  909 */     boolean mayHaveMoreTasks = pollTaskInternal();
/*  910 */     this.mayHaveDelayedTasks = mayHaveMoreTasks;
/*  911 */     return mayHaveMoreTasks;
/*      */   }
/*      */   
/*      */   private boolean pollTaskInternal() {
/*  915 */     if (super.pollTask())
/*  916 */       return true; 
/*  919 */     if (this.tickRateManager.isSprinting() || shouldRunAllTasks() || haveTime())
/*  920 */       for (ServerLevel level : getAllLevels()) {
/*  921 */         if (level.getChunkSource().pollTask())
/*  922 */           return true; 
/*      */       }  
/*  927 */     return false;
/*      */   }
/*      */   
/*      */   protected void doRunTask(TickTask task) {
/*  932 */     Profiler.get().incrementCounter("runTask");
/*  933 */     super.doRunTask(task);
/*      */   }
/*      */   
/*      */   private Optional<ServerStatus.Favicon> loadStatusIcon() {
/*  938 */     Optional<Path> iconPath = Optional.<Path>of(getFile("server-icon.png")).filter(x$0 -> Files.isRegularFile(x$0, new java.nio.file.LinkOption[0])).or(() -> this.storageSource.getIconFile().filter(()));
/*  940 */     return iconPath.flatMap(path -> {
/*      */           try {
/*      */             byte[] contents = Files.readAllBytes(path);
/*      */             PngInfo pngInfo = PngInfo.fromBytes(contents);
/*      */             if (pngInfo.width() != 64 || pngInfo.height() != 64)
/*      */               throw new IllegalArgumentException("Invalid world icon size [" + pngInfo.width() + ", " + pngInfo.height() + "], but expected [64, 64]"); 
/*      */             return Optional.of(new ServerStatus.Favicon(contents));
/*  948 */           } catch (Exception e) {
/*      */             LOGGER.error("Couldn't load server icon", e);
/*      */             return Optional.empty();
/*      */           } 
/*      */         });
/*      */   }
/*      */   
/*      */   public Optional<Path> getWorldScreenshotFile() {
/*  957 */     return this.storageSource.getIconFile();
/*      */   }
/*      */   
/*      */   public Path getServerDirectory() {
/*  961 */     return Path.of("", new String[0]);
/*      */   }
/*      */   
/*      */   public ServerActivityMonitor getServerActivityMonitor() {
/*  965 */     return this.serverActivityMonitor;
/*      */   }
/*      */   
/*      */   protected void onServerCrash(CrashReport report) {}
/*      */   
/*      */   protected void onServerExit() {}
/*      */   
/*      */   public boolean isPaused() {
/*  975 */     return false;
/*      */   }
/*      */   
/*      */   protected void tickServer(BooleanSupplier haveTime) {
/*  979 */     long nano = Util.getNanos();
/*  980 */     int emptyTickThreshold = pauseWhenEmptySeconds() * 20;
/*  981 */     if (emptyTickThreshold > 0) {
/*  982 */       if (this.playerList.getPlayerCount() == 0 && !this.tickRateManager.isSprinting()) {
/*  983 */         this.emptyTicks++;
/*      */       } else {
/*  985 */         this.emptyTicks = 0;
/*      */       } 
/*  987 */       if (this.emptyTicks >= emptyTickThreshold) {
/*  988 */         if (this.emptyTicks == emptyTickThreshold) {
/*  989 */           LOGGER.info("Server empty for {} seconds, pausing", Integer.valueOf(pauseWhenEmptySeconds()));
/*  990 */           autoSave();
/*      */         } 
/*  992 */         tickConnection();
/*      */         return;
/*      */       } 
/*      */     } 
/*  997 */     this.tickCount++;
/*  999 */     this.tickRateManager.tick();
/* 1001 */     tickChildren(haveTime);
/* 1003 */     if (nano - this.lastServerStatus >= STATUS_EXPIRE_TIME_NANOS) {
/* 1004 */       this.lastServerStatus = nano;
/* 1005 */       this.status = buildServerStatus();
/*      */     } 
/* 1008 */     this.ticksUntilAutosave--;
/* 1009 */     if (this.ticksUntilAutosave <= 0)
/* 1010 */       autoSave(); 
/* 1013 */     ProfilerFiller profiler = Profiler.get();
/* 1014 */     profiler.push("tallying");
/* 1015 */     long tickTime = Util.getNanos() - nano;
/* 1016 */     int tickIndex = this.tickCount % 100;
/* 1017 */     this.aggregatedTickTimesNanos -= this.tickTimesNanos[tickIndex];
/* 1018 */     this.aggregatedTickTimesNanos += tickTime;
/* 1019 */     this.tickTimesNanos[tickIndex] = tickTime;
/* 1020 */     this.smoothedTickTimeMillis = this.smoothedTickTimeMillis * 0.8F + (float)tickTime / (float)TimeUtil.NANOSECONDS_PER_MILLISECOND * 0.19999999F;
/* 1022 */     logTickMethodTime(nano);
/* 1024 */     profiler.pop();
/*      */   }
/*      */   
/*      */   protected void processPacketsAndTick(boolean sprinting) {
/* 1028 */     ProfilerFiller profiler = Profiler.get();
/* 1029 */     profiler.push("tick");
/* 1030 */     this.tickFrame.start();
/* 1031 */     profiler.push("scheduledPacketProcessing");
/* 1032 */     this.packetProcessor.processQueuedPackets();
/* 1033 */     profiler.pop();
/* 1034 */     tickServer(sprinting ? (() -> false) : this::haveTime);
/* 1035 */     this.tickFrame.end();
/* 1036 */     profiler.pop();
/*      */   }
/*      */   
/*      */   private void autoSave() {
/* 1040 */     this.ticksUntilAutosave = computeNextAutosaveInterval();
/* 1041 */     LOGGER.debug("Autosave started");
/* 1042 */     ProfilerFiller profiler = Profiler.get();
/* 1043 */     profiler.push("save");
/* 1044 */     saveEverything(true, false, false);
/* 1045 */     profiler.pop();
/* 1046 */     LOGGER.debug("Autosave finished");
/*      */   }
/*      */   
/*      */   private void logTickMethodTime(long startTime) {
/* 1050 */     if (isTickTimeLoggingEnabled())
/* 1051 */       getTickTimeLogger().logPartialSample(Util.getNanos() - startTime, TpsDebugDimensions.TICK_SERVER_METHOD.ordinal()); 
/*      */   }
/*      */   
/*      */   private int computeNextAutosaveInterval() {
/*      */     float ticksPerSecond;
/* 1057 */     if (this.tickRateManager.isSprinting()) {
/* 1058 */       long estimatedTickTimeNanos = getAverageTickTimeNanos() + 1L;
/* 1059 */       ticksPerSecond = (float)TimeUtil.NANOSECONDS_PER_SECOND / (float)estimatedTickTimeNanos;
/*      */     } else {
/* 1061 */       ticksPerSecond = this.tickRateManager.tickrate();
/*      */     } 
/* 1063 */     int intendedIntervalInSeconds = 300;
/* 1065 */     return Math.max(100, (int)(ticksPerSecond * 300.0F));
/*      */   }
/*      */   
/*      */   public void onTickRateChanged() {
/* 1069 */     int newAutosaveInterval = computeNextAutosaveInterval();
/* 1070 */     if (newAutosaveInterval < this.ticksUntilAutosave)
/* 1071 */       this.ticksUntilAutosave = newAutosaveInterval; 
/*      */   }
/*      */   
/*      */   private ServerStatus buildServerStatus() {
/* 1080 */     ServerStatus.Players players = buildPlayerStatus();
/* 1081 */     return new ServerStatus(
/* 1082 */         Component.nullToEmpty(getMotd()), 
/* 1083 */         Optional.of(players), 
/* 1084 */         Optional.of(ServerStatus.Version.current()), 
/* 1085 */         Optional.ofNullable(this.statusIcon), 
/* 1086 */         enforceSecureProfile());
/*      */   }
/*      */   
/*      */   private ServerStatus.Players buildPlayerStatus() {
/* 1091 */     List<ServerPlayer> players = this.playerList.getPlayers();
/* 1092 */     int maxPlayers = getMaxPlayers();
/* 1093 */     if (hidesOnlinePlayers())
/* 1094 */       return new ServerStatus.Players(maxPlayers, players.size(), List.of()); 
/* 1097 */     int sampleSize = Math.min(players.size(), 12);
/* 1099 */     ObjectArrayList<NameAndId> sample = new ObjectArrayList(sampleSize);
/* 1100 */     int offset = Mth.nextInt(this.random, 0, players.size() - sampleSize);
/* 1101 */     for (int i = 0; i < sampleSize; i++) {
/* 1102 */       ServerPlayer player = players.get(offset + i);
/* 1103 */       sample.add(player.allowsListing() ? player.nameAndId() : ANONYMOUS_PLAYER_PROFILE);
/*      */     } 
/* 1106 */     Util.shuffle((List)sample, this.random);
/* 1108 */     return new ServerStatus.Players(maxPlayers, players.size(), (List)sample);
/*      */   }
/*      */   
/*      */   protected void tickChildren(BooleanSupplier haveTime) {
/* 1112 */     ProfilerFiller profiler = Profiler.get();
/* 1113 */     getPlayerList().getPlayers().forEach(player -> player.connection.suspendFlushing());
/* 1114 */     profiler.push("commandFunctions");
/* 1115 */     getFunctions().tick();
/* 1116 */     profiler.pop();
/* 1118 */     if (this.tickRateManager.runsNormally()) {
/* 1119 */       profiler.push("clocks");
/* 1120 */       this.clockManager.tick();
/* 1121 */       profiler.pop();
/*      */     } 
/* 1124 */     if (this.tickCount % 20 == 0) {
/* 1125 */       profiler.push("timeSync");
/* 1126 */       forceGameTimeSynchronization();
/* 1127 */       profiler.pop();
/*      */     } 
/* 1130 */     profiler.push("levels");
/* 1131 */     updateEffectiveRespawnData();
/* 1132 */     for (ServerLevel level : getAllLevels()) {
/* 1133 */       profiler.push(() -> String.valueOf(level) + " " + String.valueOf(level));
/* 1135 */       profiler.push("tick");
/*      */       try {
/* 1137 */         level.tick(haveTime);
/* 1138 */       } catch (Throwable t) {
/* 1139 */         CrashReport report = CrashReport.forThrowable(t, "Exception ticking world");
/* 1140 */         level.fillReportDetails(report);
/* 1141 */         throw new ReportedException(report);
/*      */       } 
/* 1143 */       profiler.pop();
/* 1144 */       profiler.pop();
/*      */     } 
/* 1147 */     profiler.popPush("connection");
/* 1148 */     tickConnection();
/* 1149 */     profiler.popPush("players");
/* 1150 */     this.playerList.tick();
/* 1151 */     profiler.popPush("debugSubscribers");
/* 1152 */     this.debugSubscribers.tick();
/* 1154 */     if (this.tickRateManager.runsNormally()) {
/* 1155 */       profiler.popPush("gameTests");
/* 1156 */       GameTestTicker.SINGLETON.tick();
/*      */     } 
/* 1158 */     profiler.popPush("server gui refresh");
/* 1159 */     for (Runnable tickable : this.tickables)
/* 1160 */       tickable.run(); 
/* 1162 */     profiler.popPush("send chunks");
/* 1163 */     for (ServerPlayer player : this.playerList.getPlayers()) {
/* 1164 */       player.connection.chunkSender.sendNextChunks(player);
/* 1165 */       player.connection.resumeFlushing();
/*      */     } 
/* 1167 */     profiler.pop();
/* 1168 */     this.serverActivityMonitor.tick();
/*      */   }
/*      */   
/*      */   private void updateEffectiveRespawnData() {
/* 1173 */     LevelData.RespawnData respawnData = this.worldData.overworldData().getRespawnData();
/* 1174 */     ServerLevel respawnLevel = findRespawnDimension();
/* 1175 */     this.effectiveRespawnData = respawnLevel.getWorldBorderAdjustedRespawnData(respawnData);
/*      */   }
/*      */   
/*      */   protected void tickConnection() {
/* 1179 */     getConnection().tick();
/*      */   }
/*      */   
/*      */   public void forceGameTimeSynchronization() {
/* 1183 */     ProfilerFiller profiler = Profiler.get();
/* 1184 */     profiler.push("timeSync");
/* 1185 */     this.playerList.broadcastAll((Packet)new ClientboundSetTimePacket(overworld().getGameTime(), Map.of()));
/* 1186 */     profiler.pop();
/*      */   }
/*      */   
/*      */   public void addTickable(Runnable tickable) {
/* 1190 */     this.tickables.add(tickable);
/*      */   }
/*      */   
/*      */   protected void setId(String serverId) {
/* 1194 */     this.serverId = serverId;
/*      */   }
/*      */   
/*      */   public boolean isShutdown() {
/* 1198 */     return !this.serverThread.isAlive();
/*      */   }
/*      */   
/*      */   public Path getFile(String name) {
/* 1202 */     return getServerDirectory().resolve(name);
/*      */   }
/*      */   
/*      */   public final ServerLevel overworld() {
/* 1206 */     return this.levels.get(Level.OVERWORLD);
/*      */   }
/*      */   
/*      */   public ServerLevel getLevel(ResourceKey<Level> dimension) {
/* 1210 */     return this.levels.get(dimension);
/*      */   }
/*      */   
/*      */   public Set<ResourceKey<Level>> levelKeys() {
/* 1214 */     return this.levels.keySet();
/*      */   }
/*      */   
/*      */   public Iterable<ServerLevel> getAllLevels() {
/* 1218 */     return this.levels.values();
/*      */   }
/*      */   
/*      */   public String getServerVersion() {
/* 1223 */     return SharedConstants.getCurrentVersion().name();
/*      */   }
/*      */   
/*      */   public int getPlayerCount() {
/* 1228 */     return this.playerList.getPlayerCount();
/*      */   }
/*      */   
/*      */   public String[] getPlayerNames() {
/* 1232 */     return this.playerList.getPlayerNamesArray();
/*      */   }
/*      */   
/*      */   public String getServerModName() {
/* 1236 */     return "vanilla";
/*      */   }
/*      */   
/*      */   public ServerClockManager clockManager() {
/* 1240 */     return this.clockManager;
/*      */   }
/*      */   
/*      */   public SystemReport fillSystemReport(SystemReport systemReport) {
/* 1244 */     systemReport.setDetail("Server Running", () -> Boolean.toString(this.running));
/* 1246 */     if (this.playerList != null)
/* 1247 */       systemReport.setDetail("Player Count", () -> "" + this.playerList.getPlayerCount() + " / " + this.playerList.getPlayerCount() + "; " + this.playerList.getMaxPlayers()); 
/* 1250 */     systemReport.setDetail("Active Data Packs", () -> PackRepository.displayPackList(this.packRepository.getSelectedPacks()));
/* 1251 */     systemReport.setDetail("Available Data Packs", () -> PackRepository.displayPackList(this.packRepository.getAvailablePacks()));
/* 1253 */     systemReport.setDetail("Enabled Feature Flags", () -> FeatureFlags.REGISTRY.toNames(this.worldData.enabledFeatures()).stream().map(Identifier::toString).collect(Collectors.joining(", ")));
/* 1256 */     systemReport.setDetail("World Generation", () -> this.worldData.worldGenSettingsLifecycle().toString());
/* 1257 */     systemReport.setDetail("World Seed", () -> String.valueOf(this.worldGenSettings.options().seed()));
/* 1259 */     Objects.requireNonNull(this.suppressedExceptions);
/* 1259 */     systemReport.setDetail("Suppressed Exceptions", this.suppressedExceptions::dump);
/* 1261 */     if (this.serverId != null)
/* 1262 */       systemReport.setDetail("Server Id", () -> this.serverId); 
/* 1265 */     return fillServerSystemReport(systemReport);
/*      */   }
/*      */   
/*      */   public ModCheck getModdedStatus() {
/* 1271 */     return ModCheck.identify("vanilla", this::getServerModName, "Server", MinecraftServer.class);
/*      */   }
/*      */   
/*      */   public void sendSystemMessage(Component message) {
/* 1276 */     LOGGER.info(message.getString());
/*      */   }
/*      */   
/*      */   public KeyPair getKeyPair() {
/* 1280 */     return Objects.<KeyPair>requireNonNull(this.keyPair);
/*      */   }
/*      */   
/*      */   public int getPort() {
/* 1284 */     return this.port;
/*      */   }
/*      */   
/*      */   public void setPort(int port) {
/* 1288 */     this.port = port;
/*      */   }
/*      */   
/*      */   public GameProfile getSingleplayerProfile() {
/* 1292 */     return this.singleplayerProfile;
/*      */   }
/*      */   
/*      */   public void setSingleplayerProfile(GameProfile singleplayerProfile) {
/* 1296 */     this.singleplayerProfile = singleplayerProfile;
/*      */   }
/*      */   
/*      */   public boolean isSingleplayer() {
/* 1300 */     return (this.singleplayerProfile != null);
/*      */   }
/*      */   
/*      */   protected void initializeKeyPair() {
/* 1304 */     LOGGER.info("Generating keypair");
/*      */     try {
/* 1306 */       this.keyPair = Crypt.generateKeyPair();
/* 1307 */     } catch (CryptException e) {
/* 1308 */       throw new IllegalStateException("Failed to generate key pair", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setDifficulty(Difficulty difficulty, boolean ignoreLock) {
/* 1313 */     if (!ignoreLock && this.worldData.isDifficultyLocked())
/*      */       return; 
/* 1317 */     this.worldData.setDifficulty(this.worldData.isHardcore() ? Difficulty.HARD : difficulty);
/* 1319 */     updateMobSpawningFlags();
/* 1320 */     getPlayerList().getPlayers().forEach(this::sendDifficultyUpdate);
/*      */   }
/*      */   
/*      */   public int getScaledTrackingDistance(int baseRange) {
/* 1324 */     return baseRange;
/*      */   }
/*      */   
/*      */   public void updateMobSpawningFlags() {
/* 1328 */     for (ServerLevel level : getAllLevels())
/* 1329 */       level.setSpawnSettings(level.isSpawningMonsters()); 
/*      */   }
/*      */   
/*      */   public void setDifficultyLocked(boolean locked) {
/* 1334 */     this.worldData.setDifficultyLocked(locked);
/* 1335 */     getPlayerList().getPlayers().forEach(this::sendDifficultyUpdate);
/*      */   }
/*      */   
/*      */   private void sendDifficultyUpdate(ServerPlayer player) {
/* 1339 */     LevelData levelData = player.level().getLevelData();
/* 1340 */     player.connection.send((Packet)new ClientboundChangeDifficultyPacket(levelData.getDifficulty(), levelData.isDifficultyLocked()));
/*      */   }
/*      */   
/*      */   public boolean isDemo() {
/* 1344 */     return this.isDemo;
/*      */   }
/*      */   
/*      */   public void setDemo(boolean demo) {
/* 1348 */     this.isDemo = demo;
/*      */   }
/*      */   
/*      */   public Map<String, String> getCodeOfConducts() {
/* 1352 */     return Map.of();
/*      */   }
/*      */   
/*      */   public Optional<ServerResourcePackInfo> getServerResourcePack() {
/* 1356 */     return Optional.empty();
/*      */   }
/*      */   
/*      */   public boolean isResourcePackRequired() {
/* 1360 */     return getServerResourcePack().filter(ServerResourcePackInfo::isRequired).isPresent();
/*      */   }
/*      */   
/*      */   public boolean usesAuthentication() {
/* 1372 */     return this.onlineMode;
/*      */   }
/*      */   
/*      */   public void setUsesAuthentication(boolean onlineMode) {
/* 1376 */     this.onlineMode = onlineMode;
/*      */   }
/*      */   
/*      */   public boolean getPreventProxyConnections() {
/* 1380 */     return this.preventProxyConnections;
/*      */   }
/*      */   
/*      */   public void setPreventProxyConnections(boolean preventProxyConnections) {
/* 1384 */     this.preventProxyConnections = preventProxyConnections;
/*      */   }
/*      */   
/*      */   public boolean allowFlight() {
/* 1390 */     return true;
/*      */   }
/*      */   
/*      */   public String getMotd() {
/* 1395 */     return this.motd;
/*      */   }
/*      */   
/*      */   public void setMotd(String motd) {
/* 1399 */     this.motd = motd;
/*      */   }
/*      */   
/*      */   public boolean isStopped() {
/* 1403 */     return this.stopped;
/*      */   }
/*      */   
/*      */   public PlayerList getPlayerList() {
/* 1407 */     return this.playerList;
/*      */   }
/*      */   
/*      */   public void setPlayerList(PlayerList players) {
/* 1411 */     this.playerList = players;
/*      */   }
/*      */   
/*      */   public void setDefaultGameType(GameType gameType) {
/* 1417 */     this.worldData.setGameType(gameType);
/*      */   }
/*      */   
/*      */   public int enforceGameTypeForPlayers(GameType gameType) {
/* 1421 */     if (gameType == null)
/* 1422 */       return 0; 
/* 1424 */     int count = 0;
/* 1425 */     for (ServerPlayer player : getPlayerList().getPlayers()) {
/* 1426 */       if (player.setGameMode(gameType))
/* 1427 */         count++; 
/*      */     } 
/* 1430 */     return count;
/*      */   }
/*      */   
/*      */   public ServerConnectionListener getConnection() {
/* 1434 */     return this.connection;
/*      */   }
/*      */   
/*      */   public boolean isReady() {
/* 1438 */     return this.isReady;
/*      */   }
/*      */   
/*      */   public boolean publishServer(MultiplayerScope scope, GameType gameMode, boolean allowCommands, int port) {
/* 1442 */     return false;
/*      */   }
/*      */   
/*      */   public boolean unpublishServer() {
/* 1446 */     return false;
/*      */   }
/*      */   
/*      */   public int getTickCount() {
/* 1450 */     return this.tickCount;
/*      */   }
/*      */   
/*      */   public boolean isUnderSpawnProtection(ServerLevel level, BlockPos pos, Player player) {
/* 1454 */     return false;
/*      */   }
/*      */   
/*      */   public boolean repliesToStatus() {
/* 1458 */     return true;
/*      */   }
/*      */   
/*      */   public boolean hidesOnlinePlayers() {
/* 1462 */     return false;
/*      */   }
/*      */   
/*      */   public Proxy getProxy() {
/* 1466 */     return this.proxy;
/*      */   }
/*      */   
/*      */   public int playerIdleTimeout() {
/* 1470 */     return this.playerIdleTimeout;
/*      */   }
/*      */   
/*      */   public void setPlayerIdleTimeout(int playerIdleTimeout) {
/* 1474 */     this.playerIdleTimeout = playerIdleTimeout;
/*      */   }
/*      */   
/*      */   public Services services() {
/* 1478 */     return this.services;
/*      */   }
/*      */   
/*      */   public ServerStatus getStatus() {
/* 1482 */     return this.status;
/*      */   }
/*      */   
/*      */   public void invalidateStatus() {
/* 1486 */     this.lastServerStatus = 0L;
/*      */   }
/*      */   
/*      */   public int getAbsoluteMaxWorldSize() {
/* 1490 */     return 29999984;
/*      */   }
/*      */   
/*      */   public boolean scheduleExecutables() {
/* 1495 */     return (super.scheduleExecutables() && !isStopped());
/*      */   }
/*      */   
/*      */   public void executeIfPossible(Runnable command) {
/* 1500 */     if (isStopped())
/* 1501 */       throw new RejectedExecutionException("Server already shutting down"); 
/* 1503 */     super.executeIfPossible(command);
/*      */   }
/*      */   
/*      */   public Thread getRunningThread() {
/* 1508 */     return this.serverThread;
/*      */   }
/*      */   
/*      */   public int getCompressionThreshold() {
/* 1512 */     return 256;
/*      */   }
/*      */   
/*      */   public boolean enforceSecureProfile() {
/* 1516 */     return false;
/*      */   }
/*      */   
/*      */   public long getNextTickTime() {
/* 1520 */     return this.nextTickTimeNanos;
/*      */   }
/*      */   
/*      */   public DataFixer getFixerUpper() {
/* 1524 */     return this.fixerUpper;
/*      */   }
/*      */   
/*      */   public ServerAdvancementManager getAdvancements() {
/* 1528 */     return this.resources.managers.getAdvancements();
/*      */   }
/*      */   
/*      */   public ServerFunctionManager getFunctions() {
/* 1532 */     return this.functionManager;
/*      */   }
/*      */   
/*      */   public CompletableFuture<Void> reloadResources(Collection<String> packsToEnable) {
/* 1552 */     CompletableFuture<Void> result = CompletableFuture.supplyAsync(() -> {
/*      */           Objects.requireNonNull(this.packRepository);
/*      */           return (ImmutableList)packsToEnable.stream().map(this.packRepository::getPack).filter(Objects::nonNull).map(Pack::open).collect(ImmutableList.toImmutableList());
/*      */         }(Executor)this).thenCompose(packsToLoad -> {
/*      */           MultiPackResourceManager multiPackResourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, (List)packsToLoad);
/*      */           List<Registry.PendingTags<?>> postponedTags = TagLoader.loadTagsForExistingRegistries((ResourceManager)multiPackResourceManager, (RegistryAccess)this.registries.compositeAccess());
/*      */           return ReloadableServerResources.loadResources((ResourceManager)multiPackResourceManager, this.registries, postponedTags, this.worldData.enabledFeatures(), isDedicatedServer() ? Commands.CommandSelection.DEDICATED : Commands.CommandSelection.INTEGRATED, getFunctionCompilationPermissions(), this.executor, (Executor)this).whenComplete(()).thenApply(());
/* 1552 */         }).thenAcceptAsync(newResources -> {
/*      */           this.resources.close();
/*      */           this.resources = newResources;
/*      */           this.packRepository.setSelected(packsToEnable);
/*      */           WorldDataConfiguration newConfig = new WorldDataConfiguration(getSelectedPacks(this.packRepository, true), this.worldData.enabledFeatures());
/*      */           this.worldData.setDataConfiguration(newConfig);
/*      */           this.resources.managers.updateComponentsAndStaticRegistryTags();
/*      */           this.resources.managers.getRecipeManager().finalizeRecipeLoading(this.worldData.enabledFeatures());
/*      */           getPlayerList().saveAll();
/*      */           getPlayerList().reloadResources();
/*      */           this.functionManager.replaceLibrary(this.resources.managers.getFunctionLibrary());
/*      */           this.structureTemplateManager.onResourceManagerReload((ResourceManager)this.resources.resourceManager);
/*      */           this.fuelValues = FuelValues.vanillaBurnTimes((HolderLookup.Provider)this.registries.compositeAccess(), this.worldData.enabledFeatures());
/*      */         }(Executor)this);
/* 1571 */     if (isSameThread()) {
/* 1572 */       Objects.requireNonNull(result);
/* 1572 */       managedBlock(result::isDone);
/*      */     } 
/* 1574 */     return result;
/*      */   }
/*      */   
/*      */   public static WorldDataConfiguration configurePackRepository(PackRepository packRepository, WorldDataConfiguration initialDataConfig, boolean initMode, boolean safeMode) {
/* 1578 */     DataPackConfig dataPackConfig = initialDataConfig.dataPacks();
/* 1580 */     FeatureFlagSet forcedFeatures = initMode ? FeatureFlagSet.of() : initialDataConfig.enabledFeatures();
/* 1581 */     FeatureFlagSet allowedFeatures = initMode ? FeatureFlags.REGISTRY.allFlags() : initialDataConfig.enabledFeatures();
/* 1583 */     packRepository.reload();
/* 1585 */     if (safeMode)
/* 1586 */       return configureRepositoryWithSelection(packRepository, List.of("vanilla"), forcedFeatures, false); 
/* 1589 */     Set<String> selected = Sets.newLinkedHashSet();
/* 1591 */     for (String id : dataPackConfig.getEnabled()) {
/* 1592 */       if (packRepository.isAvailable(id)) {
/* 1593 */         selected.add(id);
/*      */         continue;
/*      */       } 
/* 1595 */       LOGGER.warn("Missing data pack {}", id);
/*      */     } 
/* 1599 */     for (Pack pack : packRepository.getAvailablePacks()) {
/* 1600 */       String packId = pack.getId();
/* 1601 */       if (dataPackConfig.getDisabled().contains(packId))
/*      */         continue; 
/* 1604 */       FeatureFlagSet packFeatures = pack.getRequestedFeatures();
/* 1605 */       boolean isSelected = selected.contains(packId);
/* 1606 */       if (!isSelected && pack.getPackSource().shouldAddAutomatically())
/* 1607 */         if (packFeatures.isSubsetOf(allowedFeatures)) {
/* 1608 */           LOGGER.info("Found new data pack {}, loading it automatically", packId);
/* 1609 */           selected.add(packId);
/*      */         } else {
/* 1611 */           LOGGER.info("Found new data pack {}, but can't load it due to missing features {}", packId, FeatureFlags.printMissingFlags(allowedFeatures, packFeatures));
/*      */         }  
/* 1614 */       if (isSelected && !packFeatures.isSubsetOf(allowedFeatures)) {
/* 1615 */         LOGGER.warn("Pack {} requires features {} that are not enabled for this world, disabling pack.", packId, FeatureFlags.printMissingFlags(allowedFeatures, packFeatures));
/* 1616 */         selected.remove(packId);
/*      */       } 
/*      */     } 
/* 1620 */     if (selected.isEmpty()) {
/* 1621 */       LOGGER.info("No datapacks selected, forcing vanilla");
/* 1622 */       selected.add("vanilla");
/*      */     } 
/* 1625 */     return configureRepositoryWithSelection(packRepository, selected, forcedFeatures, true);
/*      */   }
/*      */   
/*      */   private static WorldDataConfiguration configureRepositoryWithSelection(PackRepository packRepository, Collection<String> selected, FeatureFlagSet forcedFeatures, boolean disableInactive) {
/* 1629 */     packRepository.setSelected(selected);
/* 1630 */     enableForcedFeaturePacks(packRepository, forcedFeatures);
/* 1633 */     DataPackConfig packConfig = getSelectedPacks(packRepository, disableInactive);
/* 1634 */     FeatureFlagSet packRequestedFeatures = packRepository.getRequestedFeatureFlags().join(forcedFeatures);
/* 1636 */     return new WorldDataConfiguration(packConfig, packRequestedFeatures);
/*      */   }
/*      */   
/*      */   private static void enableForcedFeaturePacks(PackRepository packRepository, FeatureFlagSet forcedFeatures) {
/* 1640 */     FeatureFlagSet providedFeatures = packRepository.getRequestedFeatureFlags();
/* 1641 */     FeatureFlagSet missingFeatures = forcedFeatures.subtract(providedFeatures);
/* 1642 */     if (missingFeatures.isEmpty())
/*      */       return; 
/* 1646 */     ObjectArraySet<String> objectArraySet = new ObjectArraySet(packRepository.getSelectedIds());
/* 1648 */     for (Pack pack : packRepository.getAvailablePacks()) {
/* 1649 */       if (missingFeatures.isEmpty())
/*      */         break; 
/* 1653 */       if (pack.getPackSource() != PackSource.FEATURE)
/*      */         continue; 
/* 1656 */       String packId = pack.getId();
/* 1657 */       FeatureFlagSet packFeatures = pack.getRequestedFeatures();
/* 1658 */       if (!packFeatures.isEmpty() && packFeatures.intersects(missingFeatures) && packFeatures.isSubsetOf(forcedFeatures)) {
/* 1659 */         if (!objectArraySet.add(packId))
/* 1660 */           throw new IllegalStateException("Tried to force '" + packId + "', but it was already enabled"); 
/* 1662 */         LOGGER.info("Found feature pack ('{}') for requested feature, forcing to enabled", packId);
/* 1663 */         missingFeatures = missingFeatures.subtract(packFeatures);
/*      */       } 
/*      */     } 
/* 1667 */     packRepository.setSelected((Collection)objectArraySet);
/*      */   }
/*      */   
/*      */   private static DataPackConfig getSelectedPacks(PackRepository packRepository, boolean disableInactive) {
/* 1671 */     Collection<String> selected = packRepository.getSelectedIds();
/* 1672 */     ImmutableList immutableList = ImmutableList.copyOf(selected);
/* 1673 */     List<String> disabled = disableInactive ? packRepository.getAvailableIds().stream().filter(id -> !selected.contains(id)).toList() : List.<String>of();
/* 1674 */     return new DataPackConfig((List)immutableList, disabled);
/*      */   }
/*      */   
/*      */   public void kickUnlistedPlayers() {
/* 1678 */     if (!isEnforceWhitelist() || !isUsingWhitelist())
/*      */       return; 
/* 1682 */     PlayerList playerList = getPlayerList();
/* 1683 */     UserWhiteList whiteList = playerList.getWhiteList();
/* 1685 */     List<ServerPlayer> players = Lists.newArrayList(playerList.getPlayers());
/* 1686 */     for (ServerPlayer player : players) {
/* 1687 */       if (!whiteList.isWhiteListed(player.nameAndId()))
/* 1688 */         player.connection.disconnect((Component)Component.translatable("multiplayer.disconnect.not_whitelisted")); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public PackRepository getPackRepository() {
/* 1694 */     return this.packRepository;
/*      */   }
/*      */   
/*      */   public Commands getCommands() {
/* 1698 */     return this.resources.managers.getCommands();
/*      */   }
/*      */   
/*      */   public CommandSourceStack createCommandSourceStack() {
/* 1702 */     ServerLevel level = findRespawnDimension();
/* 1703 */     return new CommandSourceStack(this, Vec3.atLowerCornerOf((Vec3i)getRespawnData().pos()), Vec2.ZERO, level, (PermissionSet)LevelBasedPermissionSet.OWNER, "Server", (Component)Component.literal("Server"), this, null);
/*      */   }
/*      */   
/*      */   public ServerLevel findRespawnDimension() {
/* 1707 */     LevelData.RespawnData respawnData = getWorldData().overworldData().getRespawnData();
/* 1708 */     ResourceKey<Level> respawnDimension = respawnData.dimension();
/* 1709 */     ServerLevel respawnLevel = getLevel(respawnDimension);
/* 1710 */     return (respawnLevel != null) ? respawnLevel : overworld();
/*      */   }
/*      */   
/*      */   public void setRespawnData(LevelData.RespawnData respawnData) {
/* 1714 */     ServerLevelData levelData = this.worldData.overworldData();
/* 1715 */     LevelData.RespawnData oldRespawnData = levelData.getRespawnData();
/* 1716 */     if (!oldRespawnData.equals(respawnData)) {
/* 1717 */       levelData.setSpawn(respawnData);
/* 1718 */       getPlayerList().broadcastAll((Packet)new ClientboundSetDefaultSpawnPositionPacket(respawnData));
/* 1719 */       updateEffectiveRespawnData();
/*      */     } 
/*      */   }
/*      */   
/*      */   public LevelData.RespawnData getRespawnData() {
/* 1724 */     return this.effectiveRespawnData;
/*      */   }
/*      */   
/*      */   public boolean acceptsSuccess() {
/* 1729 */     return true;
/*      */   }
/*      */   
/*      */   public boolean acceptsFailure() {
/* 1734 */     return true;
/*      */   }
/*      */   
/*      */   public WorldGenSettings getWorldGenSettings() {
/* 1741 */     return this.worldGenSettings;
/*      */   }
/*      */   
/*      */   public RecipeManager getRecipeManager() {
/* 1745 */     return this.resources.managers.getRecipeManager();
/*      */   }
/*      */   
/*      */   public ServerScoreboard getScoreboard() {
/* 1749 */     return this.scoreboard;
/*      */   }
/*      */   
/*      */   public CommandStorage getCommandStorage() {
/* 1753 */     if (this.commandStorage == null)
/* 1755 */       throw new NullPointerException("Called before server init"); 
/* 1757 */     return this.commandStorage;
/*      */   }
/*      */   
/*      */   public Stopwatches getStopwatches() {
/* 1761 */     if (this.stopwatches == null)
/* 1763 */       throw new NullPointerException("Called before server init"); 
/* 1765 */     return this.stopwatches;
/*      */   }
/*      */   
/*      */   public CustomBossEvents getCustomBossEvents() {
/* 1769 */     return this.customBossEvents;
/*      */   }
/*      */   
/*      */   public RandomSource getRandomSequence(Identifier key) {
/* 1773 */     return this.randomSequences.get(key, this.worldGenSettings.options().seed());
/*      */   }
/*      */   
/*      */   public RandomSequences getRandomSequences() {
/* 1777 */     return this.randomSequences;
/*      */   }
/*      */   
/*      */   public void setWeatherParameters(int clearTime, int rainTime, boolean raining, boolean thundering) {
/* 1781 */     WeatherData weatherData = getWeatherData();
/* 1782 */     weatherData.setClearWeatherTime(clearTime);
/* 1783 */     weatherData.setRainTime(rainTime);
/* 1784 */     weatherData.setThunderTime(rainTime);
/* 1785 */     weatherData.setRaining(raining);
/* 1786 */     weatherData.setThundering(thundering);
/*      */   }
/*      */   
/*      */   public WeatherData getWeatherData() {
/* 1790 */     return this.weatherData;
/*      */   }
/*      */   
/*      */   public boolean isEnforceWhitelist() {
/* 1794 */     return this.enforceWhitelist;
/*      */   }
/*      */   
/*      */   public void setEnforceWhitelist(boolean enforceWhitelist) {
/* 1798 */     this.enforceWhitelist = enforceWhitelist;
/*      */   }
/*      */   
/*      */   public boolean isUsingWhitelist() {
/* 1802 */     return this.usingWhitelist;
/*      */   }
/*      */   
/*      */   public void setUsingWhitelist(boolean usingWhitelist) {
/* 1806 */     this.usingWhitelist = usingWhitelist;
/*      */   }
/*      */   
/*      */   public float getCurrentSmoothedTickTime() {
/* 1810 */     return this.smoothedTickTimeMillis;
/*      */   }
/*      */   
/*      */   public ServerTickRateManager tickRateManager() {
/* 1814 */     return this.tickRateManager;
/*      */   }
/*      */   
/*      */   public long getAverageTickTimeNanos() {
/* 1818 */     return this.aggregatedTickTimesNanos / Math.min(100, Math.max(this.tickCount, 1));
/*      */   }
/*      */   
/*      */   public long[] getTickTimesNanos() {
/* 1822 */     return this.tickTimesNanos;
/*      */   }
/*      */   
/*      */   public LevelBasedPermissionSet getProfilePermissions(NameAndId nameAndId) {
/* 1826 */     if (getPlayerList().isOp(nameAndId)) {
/* 1827 */       ServerOpListEntry opListEntry = (ServerOpListEntry)getPlayerList().getOps().get(nameAndId);
/* 1828 */       if (opListEntry != null)
/* 1829 */         return opListEntry.permissions(); 
/* 1831 */       if (isSingleplayerOwner(nameAndId))
/* 1832 */         return LevelBasedPermissionSet.OWNER; 
/* 1834 */       if (isSingleplayer())
/* 1835 */         return getPlayerList().isAllowCommandsForAllPlayers() ? LevelBasedPermissionSet.OWNER : LevelBasedPermissionSet.ALL; 
/* 1837 */       return operatorUserPermissions();
/*      */     } 
/* 1839 */     return LevelBasedPermissionSet.ALL;
/*      */   }
/*      */   
/*      */   public void dumpServerProperties(Path path) throws IOException {}
/*      */   
/*      */   private void saveDebugReport(Path output) {
/* 1848 */     Path levelsDir = output.resolve("levels");
/*      */     try {
/* 1851 */       for (Map.Entry<ResourceKey<Level>, ServerLevel> level : this.levels.entrySet()) {
/* 1852 */         Identifier levelId = ((ResourceKey)level.getKey()).identifier();
/* 1853 */         Path levelPath = levelId.resolveAgainst(levelsDir);
/* 1854 */         Files.createDirectories(levelPath, (FileAttribute<?>[])new FileAttribute[0]);
/* 1855 */         ((ServerLevel)level.getValue()).saveDebugReport(levelPath);
/*      */       } 
/* 1858 */       dumpGameRules(output.resolve("gamerules.txt"));
/* 1859 */       dumpClasspath(output.resolve("classpath.txt"));
/* 1860 */       dumpMiscStats(output.resolve("stats.txt"));
/* 1861 */       dumpThreads(output.resolve("threads.txt"));
/* 1862 */       dumpServerProperties(output.resolve("server.properties.txt"));
/* 1863 */       dumpNativeModules(output.resolve("modules.txt"));
/* 1864 */     } catch (IOException e) {
/* 1865 */       LOGGER.warn("Failed to save debug report", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpMiscStats(Path path) throws IOException {
/* 1870 */     Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]);
/*      */     try {
/* 1871 */       output.write(String.format(Locale.ROOT, "pending_tasks: %d\n", new Object[] { Integer.valueOf(getPendingTasksCount()) }));
/* 1872 */       output.write(String.format(Locale.ROOT, "average_tick_time: %f\n", new Object[] { Float.valueOf(getCurrentSmoothedTickTime()) }));
/* 1873 */       output.write(String.format(Locale.ROOT, "tick_times: %s\n", new Object[] { Arrays.toString(this.tickTimesNanos) }));
/* 1874 */       output.write(String.format(Locale.ROOT, "queue: %s\n", new Object[] { Util.backgroundExecutor() }));
/* 1875 */       if (output != null)
/* 1875 */         output.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (output != null)
/*      */         try {
/*      */           output.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpGameRules(Path path) throws IOException {
/* 1879 */     Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]);
/*      */     try {
/* 1880 */       final List<String> entries = Lists.newArrayList();
/* 1881 */       final GameRules gameRules = getGameRules();
/* 1882 */       gameRules.visitGameRuleTypes(new GameRuleTypeVisitor(this) {
/*      */             public <T> void visit(GameRule<T> gameRule) {
/* 1885 */               entries.add(String.format(Locale.ROOT, "%s=%s\n", new Object[] { gameRule.getIdentifier(), this.val$gameRules.getAsString(gameRule) }));
/*      */             }
/*      */           });
/* 1888 */       for (String entry : entries)
/* 1889 */         output.write(entry); 
/* 1891 */       if (output != null)
/* 1891 */         output.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (output != null)
/*      */         try {
/*      */           output.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpClasspath(Path path) throws IOException {
/* 1895 */     Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]);
/*      */     try {
/* 1896 */       String classpath = System.getProperty("java.class.path");
/* 1897 */       String separator = File.pathSeparator;
/* 1898 */       for (String s : Splitter.on(separator).split(classpath)) {
/* 1899 */         output.write(s);
/* 1900 */         output.write("\n");
/*      */       } 
/* 1902 */       if (output != null)
/* 1902 */         output.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (output != null)
/*      */         try {
/*      */           output.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpThreads(Path path) throws IOException {
/* 1906 */     ThreadInfo[] threadInfos = Util.dumpThreadInfo();
/* 1907 */     Arrays.sort(threadInfos, Comparator.comparing(ThreadInfo::getThreadName));
/* 1909 */     Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]);
/*      */     try {
/* 1910 */       for (ThreadInfo threadInfo : threadInfos) {
/* 1911 */         output.write(threadInfo.toString());
/* 1912 */         output.write(10);
/*      */       } 
/* 1914 */       if (output != null)
/* 1914 */         output.close(); 
/*      */     } catch (Throwable throwable) {
/*      */       if (output != null)
/*      */         try {
/*      */           output.close();
/*      */         } catch (Throwable throwable1) {
/*      */           throwable.addSuppressed(throwable1);
/*      */         }  
/*      */       throw throwable;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void dumpNativeModules(Path path) throws IOException {
/* 1918 */     Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]);
/*      */     try {
/*      */       List<NativeModuleLister.NativeModuleInfo> modules;
/*      */       try {
/* 1921 */         modules = Lists.newArrayList(NativeModuleLister.listModules());
/* 1922 */       } catch (Throwable t) {
/* 1923 */         LOGGER.warn("Failed to list native modules", t);
/* 1932 */         if (output != null)
/* 1932 */           output.close(); 
/*      */         return;
/*      */       } 
/*      */       modules.sort(Comparator.comparing(NativeModuleLister.NativeModuleInfo::name, String.CASE_INSENSITIVE_ORDER));
/*      */       for (NativeModuleLister.NativeModuleInfo module : modules) {
/*      */         output.write(module.toString());
/*      */         output.write(10);
/*      */       } 
/* 1932 */       if (output != null)
/* 1932 */         output.close(); 
/*      */     } catch (Throwable modules) {
/*      */       if (output != null)
/*      */         try {
/*      */           output.close();
/*      */         } catch (Throwable throwable) {
/*      */           modules.addSuppressed(throwable);
/*      */         }  
/*      */       throw modules;
/*      */     } 
/*      */   }
/*      */   
/*      */   private ProfilerFiller createProfiler() {
/* 1936 */     if (this.willStartRecordingMetrics) {
/* 1937 */       this.metricsRecorder = (MetricsRecorder)ActiveMetricsRecorder.createStarted((MetricsSamplerProvider)new ServerMetricsSamplersProvider(
/* 1938 */             (LongSupplier)Util.timeSource(), isDedicatedServer()), 
/* 1939 */           (LongSupplier)Util.timeSource(), 
/* 1940 */           (Executor)Util.ioPool(), new MetricsPersister("server"), this.onMetricsRecordingStopped, reportPath -> {
/*      */             executeBlocking(());
/*      */             this.onMetricsRecordingFinished.accept(reportPath);
/*      */           });
/* 1950 */       this.willStartRecordingMetrics = false;
/*      */     } 
/* 1953 */     this.metricsRecorder.startTick();
/* 1955 */     return SingleTickProfiler.decorateFiller(this.metricsRecorder.getProfiler(), SingleTickProfiler.createTickProfiler("Server"));
/*      */   }
/*      */   
/*      */   protected void endMetricsRecordingTick() {
/* 1959 */     this.metricsRecorder.endTick();
/*      */   }
/*      */   
/*      */   public boolean isRecordingMetrics() {
/* 1963 */     return this.metricsRecorder.isRecording();
/*      */   }
/*      */   
/*      */   public void startRecordingMetrics(Consumer<ProfileResults> onStopped, Consumer<Path> onFinished) {
/* 1967 */     this.onMetricsRecordingStopped = (report -> {
/*      */         stopRecordingMetrics();
/*      */         onStopped.accept(report);
/*      */       });
/* 1971 */     this.onMetricsRecordingFinished = onFinished;
/* 1972 */     this.willStartRecordingMetrics = true;
/*      */   }
/*      */   
/*      */   public void stopRecordingMetrics() {
/* 1976 */     this.metricsRecorder = InactiveMetricsRecorder.INSTANCE;
/*      */   }
/*      */   
/*      */   public void finishRecordingMetrics() {
/* 1980 */     this.metricsRecorder.end();
/*      */   }
/*      */   
/*      */   public void cancelRecordingMetrics() {
/* 1984 */     this.metricsRecorder.cancel();
/*      */   }
/*      */   
/*      */   public Path getWorldPath(LevelResource resource) {
/* 1988 */     return this.storageSource.getLevelPath(resource);
/*      */   }
/*      */   
/*      */   public boolean forceSynchronousWrites() {
/* 1992 */     return true;
/*      */   }
/*      */   
/*      */   public StructureTemplateManager getStructureManager() {
/* 1996 */     return this.structureTemplateManager;
/*      */   }
/*      */   
/*      */   public WorldData getWorldData() {
/* 2000 */     return this.worldData;
/*      */   }
/*      */   
/*      */   public RegistryAccess.Frozen registryAccess() {
/* 2004 */     return this.registries.compositeAccess();
/*      */   }
/*      */   
/*      */   public LayeredRegistryAccess<RegistryLayer> registries() {
/* 2008 */     return this.registries;
/*      */   }
/*      */   
/*      */   public ReloadableServerRegistries.Holder reloadableRegistries() {
/* 2012 */     return this.resources.managers.fullRegistries();
/*      */   }
/*      */   
/*      */   public TextFilter createTextFilterForPlayer(ServerPlayer player) {
/* 2016 */     return TextFilter.DUMMY;
/*      */   }
/*      */   
/*      */   public ServerPlayerGameMode createGameModeForPlayer(ServerPlayer player) {
/* 2020 */     return isDemo() ? (ServerPlayerGameMode)new DemoMode(player) : new ServerPlayerGameMode(player);
/*      */   }
/*      */   
/*      */   public GameType getForcedGameType() {
/* 2024 */     return null;
/*      */   }
/*      */   
/*      */   public ResourceManager getResourceManager() {
/* 2028 */     return (ResourceManager)this.resources.resourceManager;
/*      */   }
/*      */   
/*      */   public boolean isCurrentlySaving() {
/* 2032 */     return this.isSaving;
/*      */   }
/*      */   
/*      */   public boolean isTimeProfilerRunning() {
/* 2036 */     return (this.debugCommandProfilerDelayStart || this.debugCommandProfiler != null);
/*      */   }
/*      */   
/*      */   public void startTimeProfiler() {
/* 2040 */     this.debugCommandProfilerDelayStart = true;
/*      */   }
/*      */   
/*      */   public ProfileResults stopTimeProfiler() {
/* 2044 */     if (this.debugCommandProfiler == null)
/* 2045 */       return (ProfileResults)EmptyProfileResults.EMPTY; 
/* 2047 */     ProfileResults results = this.debugCommandProfiler.stop(Util.getNanos(), this.tickCount);
/* 2048 */     this.debugCommandProfiler = null;
/* 2049 */     return results;
/*      */   }
/*      */   
/*      */   public int getMaxChainedNeighborUpdates() {
/* 2053 */     return 1000000;
/*      */   }
/*      */   
/*      */   public void logChatMessage(Component message, ChatType.Bound chatType, String tag) {
/* 2057 */     String decoratedMessage = chatType.decorate(message).getString();
/* 2058 */     if (tag != null) {
/* 2059 */       LOGGER.info("[{}] {}", tag, decoratedMessage);
/*      */     } else {
/* 2061 */       LOGGER.info("{}", decoratedMessage);
/*      */     } 
/*      */   }
/*      */   
/*      */   public ChatDecorator getChatDecorator() {
/* 2067 */     return ChatDecorator.PLAIN;
/*      */   }
/*      */   
/*      */   public boolean logIPs() {
/* 2071 */     return true;
/*      */   }
/*      */   
/*      */   public void handleCustomClickAction(Identifier id, Optional<Tag> payload) {
/* 2075 */     LOGGER.debug("Received custom click action {} with payload {}", id, payload.orElse(null));
/*      */   }
/*      */   
/*      */   public LevelLoadListener getLevelLoadListener() {
/* 2080 */     return this.levelLoadListener;
/*      */   }
/*      */   
/*      */   public boolean setAutoSave(boolean enable) {
/* 2084 */     boolean success = false;
/* 2085 */     for (ServerLevel level : getAllLevels()) {
/* 2086 */       if (level != null && level.noSave == enable) {
/* 2087 */         level.noSave = !enable;
/* 2088 */         success = true;
/*      */       } 
/*      */     } 
/* 2091 */     return success;
/*      */   }
/*      */   
/*      */   public boolean isAutoSave() {
/* 2095 */     for (ServerLevel level : getAllLevels()) {
/* 2096 */       if (level != null && !level.noSave)
/* 2097 */         return true; 
/*      */     } 
/* 2100 */     return false;
/*      */   }
/*      */   
/*      */   public <T> void onGameRuleChanged(GameRule<T> rule, T value) {
/* 2104 */     notificationManager().onGameRuleChanged(rule, value);
/* 2105 */     if (rule == GameRules.REDUCED_DEBUG_INFO) {
/* 2106 */       byte event = ((Boolean)value).booleanValue() ? 22 : 23;
/* 2107 */       for (ServerPlayer player : getPlayerList().getPlayers())
/* 2108 */         player.connection.send((Packet)new ClientboundEntityEventPacket((Entity)player, event)); 
/* 2110 */     } else if (rule == GameRules.LIMITED_CRAFTING || rule == GameRules.IMMEDIATE_RESPAWN) {
/* 2111 */       ClientboundGameEventPacket.Type eventType = (rule == GameRules.LIMITED_CRAFTING) ? ClientboundGameEventPacket.LIMITED_CRAFTING : ClientboundGameEventPacket.IMMEDIATE_RESPAWN;
/* 2112 */       ClientboundGameEventPacket packet = new ClientboundGameEventPacket(eventType, ((Boolean)value).booleanValue() ? 1.0F : 0.0F);
/* 2113 */       getPlayerList().getPlayers().forEach(player -> player.connection.send((Packet)packet));
/* 2114 */     } else if (rule == GameRules.LOCATOR_BAR) {
/* 2115 */       getAllLevels().forEach(level -> {
/*      */             ServerWaypointManager waypointManager = level.getWaypointManager();
/*      */             if (((Boolean)value).booleanValue()) {
/*      */               Objects.requireNonNull(waypointManager);
/*      */               level.players().forEach(waypointManager::updatePlayer);
/*      */             } else {
/*      */               waypointManager.breakAllConnections();
/*      */             } 
/*      */           });
/* 2123 */     } else if (rule == GameRules.SPAWN_MONSTERS) {
/* 2124 */       updateMobSpawningFlags();
/* 2125 */     } else if (rule == GameRules.ADVANCE_TIME) {
/* 2126 */       getPlayerList().broadcastAll((Packet)clockManager().createFullSyncPacket());
/*      */     } 
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public GameRules getGlobalGameRules() {
/* 2133 */     return overworld().getGameRules();
/*      */   }
/*      */   
/*      */   public SavedDataStorage getDataStorage() {
/* 2137 */     return this.savedDataStorage;
/*      */   }
/*      */   
/*      */   public TimerQueue<MinecraftServer> getScheduledEvents() {
/* 2141 */     return this.scheduledEvents;
/*      */   }
/*      */   
/*      */   public GameRules getGameRules() {
/* 2145 */     return this.gameRules;
/*      */   }
/*      */   
/*      */   public enum MultiplayerScope {
/* 2149 */     OFF("off"),
/* 2150 */     LAN("lan");
/*      */     
/*      */     private final Component translatable;
/*      */     
/*      */     private final Component tooltip;
/*      */     
/*      */     MultiplayerScope(String key) {
/* 2156 */       this.translatable = (Component)Component.translatable("menu.multiplayerOptions.network." + key);
/* 2157 */       this.tooltip = (Component)Component.translatable("menu.multiplayerOptions.network." + key + ".tooltip");
/*      */     }
/*      */     
/*      */     public Component getDisplayName() {
/* 2161 */       return this.translatable;
/*      */     }
/*      */     
/*      */     public Component getTooltip() {
/* 2165 */       return this.tooltip;
/*      */     }
/*      */   }
/*      */   
/*      */   public static final class ServerResourcePackInfo extends Record {
/*      */     private final UUID id;
/*      */     
/*      */     private final String url;
/*      */     
/*      */     private final String hash;
/*      */     
/*      */     private final boolean isRequired;
/*      */     
/*      */     private final Component prompt;
/*      */     
/*      */     public ServerResourcePackInfo(UUID id, String url, String hash, boolean isRequired, Component prompt) {
/* 2169 */       this.id = id;
/* 2169 */       this.url = url;
/* 2169 */       this.hash = hash;
/* 2169 */       this.isRequired = isRequired;
/* 2169 */       this.prompt = prompt;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2169	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2169	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2169	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/server/MinecraftServer$ServerResourcePackInfo;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public UUID id() {
/* 2169 */       return this.id;
/*      */     }
/*      */     
/*      */     public String url() {
/* 2169 */       return this.url;
/*      */     }
/*      */     
/*      */     public String hash() {
/* 2169 */       return this.hash;
/*      */     }
/*      */     
/*      */     public boolean isRequired() {
/* 2169 */       return this.isRequired;
/*      */     }
/*      */     
/*      */     public Component prompt() {
/* 2169 */       return this.prompt;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class ReloadableResources extends Record implements AutoCloseable {
/*      */     private final CloseableResourceManager resourceManager;
/*      */     
/*      */     private final ReloadableServerResources managers;
/*      */     
/*      */     private ReloadableResources(CloseableResourceManager resourceManager, ReloadableServerResources managers) {
/* 2171 */       this.resourceManager = resourceManager;
/* 2171 */       this.managers = managers;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/MinecraftServer$ReloadableResources;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2171	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/MinecraftServer$ReloadableResources;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/MinecraftServer$ReloadableResources;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2171	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/server/MinecraftServer$ReloadableResources;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/MinecraftServer$ReloadableResources;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #2171	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/server/MinecraftServer$ReloadableResources;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public CloseableResourceManager resourceManager() {
/* 2171 */       return this.resourceManager;
/*      */     }
/*      */     
/*      */     public ReloadableServerResources managers() {
/* 2171 */       return this.managers;
/*      */     }
/*      */     
/*      */     public void close() {
/* 2174 */       this.resourceManager.close();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class TimeProfiler {
/*      */     private final long startNanos;
/*      */     
/*      */     private final int startTick;
/*      */     
/*      */     private TimeProfiler(long startNanos, int startTick) {
/* 2183 */       this.startNanos = startNanos;
/* 2184 */       this.startTick = startTick;
/*      */     }
/*      */     
/*      */     private ProfileResults stop(final long stopNanos, final int stopTick) {
/* 2188 */       return new ProfileResults() {
/*      */           public List<ResultField> getTimes(String path) {
/* 2191 */             return Collections.emptyList();
/*      */           }
/*      */           
/*      */           public boolean saveResults(Path file) {
/* 2196 */             return false;
/*      */           }
/*      */           
/*      */           public long getStartTimeNano() {
/* 2201 */             return MinecraftServer.TimeProfiler.this.startNanos;
/*      */           }
/*      */           
/*      */           public int getStartTimeTicks() {
/* 2206 */             return MinecraftServer.TimeProfiler.this.startTick;
/*      */           }
/*      */           
/*      */           public long getEndTimeNano() {
/* 2211 */             return stopNanos;
/*      */           }
/*      */           
/*      */           public int getEndTimeTicks() {
/* 2216 */             return stopTick;
/*      */           }
/*      */           
/*      */           public String getProfilerResults() {
/* 2221 */             return "";
/*      */           }
/*      */         };
/*      */     }
/*      */   }
/*      */   
/*      */   class null implements ProfileResults {
/*      */     public List<ResultField> getTimes(String path) {
/*      */       return Collections.emptyList();
/*      */     }
/*      */     
/*      */     public boolean saveResults(Path file) {
/*      */       return false;
/*      */     }
/*      */     
/*      */     public long getStartTimeNano() {
/*      */       return MinecraftServer.TimeProfiler.this.startNanos;
/*      */     }
/*      */     
/*      */     public int getStartTimeTicks() {
/*      */       return MinecraftServer.TimeProfiler.this.startTick;
/*      */     }
/*      */     
/*      */     public long getEndTimeNano() {
/*      */       return stopNanos;
/*      */     }
/*      */     
/*      */     public int getEndTimeTicks() {
/*      */       return stopTick;
/*      */     }
/*      */     
/*      */     public String getProfilerResults() {
/* 2221 */       return "";
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean acceptsTransfers() {
/* 2228 */     return false;
/*      */   }
/*      */   
/*      */   private void storeChunkIoError(CrashReport report, ChunkPos pos, RegionStorageInfo storageInfo) {
/* 2232 */     Util.ioPool().execute(() -> {
/*      */           try {
/*      */             Path debugDir = getFile("debug");
/*      */             FileUtil.createDirectoriesSafe(debugDir);
/*      */             String sanitizedLevelName = FileUtil.sanitizeName(storageInfo.level());
/*      */             Path reportFile = debugDir.resolve("chunk-" + sanitizedLevelName + "-" + Util.getFilenameFormattedDateTime() + "-server.txt");
/*      */             FileStore fileStore = Files.getFileStore(debugDir);
/*      */             long remainingSpace = fileStore.getUsableSpace();
/*      */             if (remainingSpace < 8192L) {
/*      */               LOGGER.warn("Not storing chunk IO report due to low space on drive {}", fileStore.name());
/*      */               return;
/*      */             } 
/*      */             CrashReportCategory category = report.addCategory("Chunk Info");
/*      */             Objects.requireNonNull(storageInfo);
/*      */             category.setDetail("Level", storageInfo::level);
/*      */             category.setDetail("Dimension", ());
/*      */             Objects.requireNonNull(storageInfo);
/*      */             category.setDetail("Storage", storageInfo::type);
/*      */             Objects.requireNonNull(pos);
/*      */             category.setDetail("Position", pos::toString);
/*      */             report.saveToFile(reportFile, ReportType.CHUNK_IO_ERROR);
/*      */             LOGGER.info("Saved details to {}", report.getSaveFile());
/* 2254 */           } catch (Exception e) {
/*      */             LOGGER.warn("Failed to store chunk IO exception", e);
/*      */           } 
/*      */         });
/*      */   }
/*      */   
/*      */   public void reportChunkLoadFailure(Throwable throwable, RegionStorageInfo storageInfo, ChunkPos pos) {
/* 2262 */     LOGGER.error("Failed to load chunk {},{}", new Object[] { Integer.valueOf(pos.x()), Integer.valueOf(pos.z()), throwable });
/* 2263 */     this.suppressedExceptions.addEntry("chunk/load", throwable);
/* 2264 */     storeChunkIoError(CrashReport.forThrowable(throwable, "Chunk load failure"), pos, storageInfo);
/* 2265 */     warnOnLowDiskSpace();
/*      */   }
/*      */   
/*      */   public void reportChunkSaveFailure(Throwable throwable, RegionStorageInfo storageInfo, ChunkPos pos) {
/* 2270 */     LOGGER.error("Failed to save chunk {},{}", new Object[] { Integer.valueOf(pos.x()), Integer.valueOf(pos.z()), throwable });
/* 2271 */     this.suppressedExceptions.addEntry("chunk/save", throwable);
/* 2272 */     storeChunkIoError(CrashReport.forThrowable(throwable, "Chunk save failure"), pos, storageInfo);
/* 2273 */     warnOnLowDiskSpace();
/*      */   }
/*      */   
/*      */   protected void warnOnLowDiskSpace() {
/* 2277 */     if (this.storageSource.checkForLowDiskSpace())
/* 2278 */       sendLowDiskSpaceWarning(); 
/*      */   }
/*      */   
/*      */   public void sendLowDiskSpaceWarning() {
/* 2283 */     LOGGER.warn("Low disk space! Might not be able to save the world.");
/*      */   }
/*      */   
/*      */   public void reportPacketHandlingException(Throwable throwable, PacketType<?> packetType) {
/* 2287 */     this.suppressedExceptions.addEntry("packet/" + String.valueOf(packetType), throwable);
/*      */   }
/*      */   
/*      */   public PotionBrewing potionBrewing() {
/* 2291 */     return this.potionBrewing;
/*      */   }
/*      */   
/*      */   public FuelValues fuelValues() {
/* 2295 */     return this.fuelValues;
/*      */   }
/*      */   
/*      */   public ServerLinks serverLinks() {
/* 2299 */     return ServerLinks.EMPTY;
/*      */   }
/*      */   
/*      */   protected int pauseWhenEmptySeconds() {
/* 2303 */     return 0;
/*      */   }
/*      */   
/*      */   public PacketProcessor packetProcessor() {
/* 2307 */     return this.packetProcessor;
/*      */   }
/*      */   
/*      */   public ServerDebugSubscribers debugSubscribers() {
/* 2311 */     return this.debugSubscribers;
/*      */   }
/*      */   
/*      */   protected abstract boolean initServer() throws IOException;
/*      */   
/*      */   public abstract LevelBasedPermissionSet operatorUserPermissions();
/*      */   
/*      */   public abstract PermissionSet getFunctionCompilationPermissions();
/*      */   
/*      */   public abstract boolean shouldRconBroadcast();
/*      */   
/*      */   protected abstract SampleLogger getTickTimeLogger();
/*      */   
/*      */   public abstract boolean isTickTimeLoggingEnabled();
/*      */   
/*      */   public abstract SystemReport fillServerSystemReport(SystemReport paramSystemReport);
/*      */   
/*      */   public abstract boolean isDedicatedServer();
/*      */   
/*      */   public abstract int getRateLimitPacketsPerSecond();
/*      */   
/*      */   public abstract int getCommandSpamThresholdSeconds();
/*      */   
/*      */   public abstract int getChatSpamThresholdSeconds();
/*      */   
/*      */   public abstract boolean useNativeTransport();
/*      */   
/*      */   public abstract boolean isPublished();
/*      */   
/*      */   public abstract boolean shouldInformAdmins();
/*      */   
/*      */   public abstract boolean isSingleplayerOwner(NameAndId paramNameAndId);
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\MinecraftServer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */