/*     */ package net.minecraft.gametest.framework;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.yggdrasil.ServicesKeySet;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.net.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.ReportType;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.commands.arguments.ResourceSelectorArgument;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.gizmos.GizmoCollector;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.Services;
/*     */ import net.minecraft.server.WorldLoader;
/*     */ import net.minecraft.server.WorldStem;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.progress.LevelLoadListener;
/*     */ import net.minecraft.server.level.progress.LoggingLevelLoadListener;
/*     */ import net.minecraft.server.notifications.EmptyNotificationService;
/*     */ import net.minecraft.server.notifications.NotificationManager;
/*     */ import net.minecraft.server.notifications.NotificationService;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.server.players.ProfileResolver;
/*     */ import net.minecraft.server.players.UserNameToIdResolver;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*     */ import net.minecraft.util.debugchart.SampleLogger;
/*     */ import net.minecraft.world.flag.FeatureFlag;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.DataPackConfig;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.level.storage.LevelDataAndDimensions;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.PlayerDataStorage;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameTestServer extends MinecraftServer {
/*  78 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int PROGRESS_REPORT_INTERVAL = 20;
/*     */   
/*     */   private static final int TEST_POSITION_RANGE = 14999992;
/*     */   
/*  85 */   private static final Services NO_SERVICES = new Services(null, ServicesKeySet.EMPTY, null, new MockUserNameToIdResolver(), new MockProfileResolver());
/*     */   
/*  92 */   private static final FeatureFlagSet ENABLED_FEATURES = FeatureFlags.REGISTRY.allFlags().subtract(FeatureFlagSet.of(FeatureFlags.REDSTONE_EXPERIMENTS, new FeatureFlag[] { FeatureFlags.MINECART_IMPROVEMENTS }));
/*     */   
/*  94 */   private final LocalSampleLogger sampleLogger = new LocalSampleLogger(4);
/*     */   
/*     */   private final Optional<String> testSelection;
/*     */   
/*     */   private final boolean verify;
/*     */   
/*     */   private final int repeatCount;
/*     */   
/*  98 */   private List<GameTestBatch> testBatches = new ArrayList<>();
/*     */   
/*  99 */   private final Stopwatch stopwatch = Stopwatch.createUnstarted();
/*     */   
/* 101 */   private static final WorldOptions WORLD_OPTIONS = new WorldOptions(0L, false, false);
/*     */   
/*     */   private MultipleTestTracker testTracker;
/*     */   
/*     */   public static GameTestServer create(Thread serverThread, LevelStorageSource.LevelStorageAccess levelStorageSource, PackRepository packRepository, Optional<String> testSelection, boolean verify, int repeatCount) {
/* 107 */     packRepository.reload();
/* 110 */     ArrayList<String> enabledPacks = new ArrayList<>(packRepository.getAvailableIds());
/* 111 */     enabledPacks.remove("vanilla");
/* 112 */     enabledPacks.addFirst("vanilla");
/* 114 */     WorldDataConfiguration defaultTestConfig = new WorldDataConfiguration(new DataPackConfig(enabledPacks, List.of()), ENABLED_FEATURES);
/* 118 */     LevelSettings testSettings = new LevelSettings("Test Level", GameType.CREATIVE, LevelSettings.DifficultySettings.DEFAULT, true, defaultTestConfig);
/* 120 */     WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, defaultTestConfig, false, true);
/* 121 */     WorldLoader.InitConfig initConfig = new WorldLoader.InitConfig(packConfig, Commands.CommandSelection.DEDICATED, (PermissionSet)LevelBasedPermissionSet.OWNER);
/*     */     try {
/* 124 */       LOGGER.debug("Starting resource loading");
/* 125 */       Stopwatch stopwatch = Stopwatch.createStarted();
/* 145 */       WorldStem worldStem = Util.blockUntilDone(executor -> WorldLoader.load(initConfig, (), WorldStem::new, (Executor)Util.backgroundExecutor(), executor)).get();
/* 146 */       stopwatch.stop();
/* 147 */       LOGGER.debug("Finished resource loading after {} ms", Long.valueOf(stopwatch.elapsed(TimeUnit.MILLISECONDS)));
/* 148 */       return new GameTestServer(serverThread, levelStorageSource, packRepository, worldStem, testSelection, verify, repeatCount);
/* 149 */     } catch (Exception e) {
/* 150 */       LOGGER.warn("Failed to load vanilla datapack, bit oops", e);
/* 151 */       System.exit(-1);
/* 152 */       throw new IllegalStateException();
/*     */     } 
/*     */   }
/*     */   
/*     */   private GameTestServer(Thread serverThread, LevelStorageSource.LevelStorageAccess levelStorageSource, PackRepository packRepository, WorldStem worldStem, Optional<String> testSelection, boolean verify, int repeatCount) {
/* 157 */     super(serverThread, levelStorageSource, packRepository, worldStem, Optional.of(new GameRules(ENABLED_FEATURES)), Proxy.NO_PROXY, DataFixers.getDataFixer(), NO_SERVICES, (LevelLoadListener)LoggingLevelLoadListener.forDedicatedServer(), false, new NotificationManager());
/* 158 */     this.testSelection = testSelection;
/* 159 */     this.repeatCount = repeatCount;
/* 160 */     this.verify = verify;
/*     */   }
/*     */   
/*     */   protected boolean initServer() {
/* 165 */     setPlayerList(new PlayerList(this, this, registries(), this.playerDataStorage, (NotificationService)new EmptyNotificationService()) {
/*     */         
/*     */         });
/* 166 */     Gizmos.withCollector(GizmoCollector.NOOP);
/* 167 */     loadLevel();
/* 168 */     ServerLevel level = overworld();
/* 169 */     this.testBatches = evaluateTestsToRun(level);
/* 170 */     LOGGER.info("Started game test server");
/* 171 */     return true;
/*     */   }
/*     */   
/*     */   private List<GameTestBatch> evaluateTestsToRun(ServerLevel level) {
/*     */     Collection<Holder.Reference<GameTestInstance>> tests;
/*     */     GameTestBatchFactory.TestDecorator decorator;
/* 175 */     Registry<GameTestInstance> testRegistry = level.registryAccess().lookupOrThrow(Registries.TEST_INSTANCE);
/* 178 */     if (this.testSelection.isPresent()) {
/* 179 */       tests = getTestsForSelection(level.registryAccess(), this.testSelection.get()).filter(test -> !((GameTestInstance)test.value()).manualOnly()).toList();
/* 180 */       if (tests.isEmpty()) {
/* 181 */         LOGGER.warn("Test selection matcher ({}) found no tests", this.testSelection.get());
/* 182 */         System.exit(-1);
/*     */       } 
/* 184 */       if (this.verify) {
/* 185 */         decorator = GameTestServer::rotateAndMultiply;
/* 186 */         LOGGER.info("Verify requested. Will run each test that matches {} {} times", this.testSelection.get(), Integer.valueOf(100 * (Rotation.values()).length));
/* 187 */       } else if (this.repeatCount > 0) {
/* 188 */         decorator = this::multiplyTest;
/* 189 */         LOGGER.info("Each test that matches {} will be run {} times (total: {})", new Object[] { this.testSelection.get(), Integer.valueOf(this.repeatCount), Integer.valueOf(tests.size() * this.repeatCount) });
/*     */       } else {
/* 191 */         decorator = GameTestBatchFactory.DIRECT;
/* 192 */         LOGGER.info("Will run tests matching {} ({} tests)", this.testSelection.get(), Integer.valueOf(tests.size()));
/*     */       } 
/*     */     } else {
/* 197 */       tests = testRegistry.listElements().filter(test -> !((GameTestInstance)test.value()).manualOnly()).toList();
/* 198 */       decorator = GameTestBatchFactory.DIRECT;
/*     */     } 
/* 201 */     return GameTestBatchFactory.divideIntoBatches(tests, decorator, level);
/*     */   }
/*     */   
/*     */   private static Stream<GameTestInfo> rotateAndMultiply(Holder.Reference<GameTestInstance> test, ServerLevel level) {
/* 205 */     Stream.Builder<GameTestInfo> builder = Stream.builder();
/* 206 */     for (Rotation rotation : Rotation.values()) {
/* 207 */       for (int i = 0; i < 100; i++)
/* 208 */         builder.add(new GameTestInfo(test, rotation, level, RetryOptions.noRetries())); 
/*     */     } 
/* 211 */     return builder.build();
/*     */   }
/*     */   
/*     */   public static Stream<Holder.Reference<GameTestInstance>> getTestsForSelection(RegistryAccess registries, String selection) {
/* 215 */     return ResourceSelectorArgument.parse(new StringReader(selection), (HolderLookup)registries.lookupOrThrow(Registries.TEST_INSTANCE)).stream();
/*     */   }
/*     */   
/*     */   private Stream<GameTestInfo> multiplyTest(Holder.Reference<GameTestInstance> test, ServerLevel level) {
/* 219 */     Stream.Builder<GameTestInfo> builder = Stream.builder();
/* 220 */     for (int i = 0; i < this.repeatCount; i++)
/* 221 */       builder.add(new GameTestInfo(test, Rotation.NONE, level, RetryOptions.noRetries())); 
/* 223 */     return builder.build();
/*     */   }
/*     */   
/*     */   protected void tickServer(BooleanSupplier haveTime) {
/* 228 */     super.tickServer(haveTime);
/* 230 */     ServerLevel level = overworld();
/* 232 */     if (!haveTestsStarted())
/* 233 */       startTests(level); 
/* 236 */     if (level.getGameTime() % 20L == 0L)
/* 237 */       LOGGER.info(this.testTracker.getProgressBar()); 
/* 240 */     if (this.testTracker.isDone()) {
/* 241 */       halt(false);
/* 242 */       LOGGER.info(this.testTracker.getProgressBar());
/* 244 */       GlobalTestReporter.finish();
/* 246 */       LOGGER.info("========= {} GAME TESTS COMPLETE IN {} ======================", Integer.valueOf(this.testTracker.getTotalCount()), this.stopwatch.stop());
/* 247 */       if (this.testTracker.hasFailedRequired()) {
/* 248 */         LOGGER.info("{} required tests failed :(", Integer.valueOf(this.testTracker.getFailedRequiredCount()));
/* 249 */         this.testTracker.getFailedRequired().forEach(GameTestServer::logFailedTest);
/*     */       } else {
/* 251 */         LOGGER.info("All {} required tests passed :)", Integer.valueOf(this.testTracker.getTotalCount()));
/*     */       } 
/* 253 */       if (this.testTracker.hasFailedOptional()) {
/* 254 */         LOGGER.info("{} optional tests failed", Integer.valueOf(this.testTracker.getFailedOptionalCount()));
/* 255 */         this.testTracker.getFailedOptional().forEach(GameTestServer::logFailedTest);
/*     */       } 
/* 257 */       LOGGER.info("====================================================");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void logFailedTest(GameTestInfo testInfo) {
/* 262 */     if (testInfo.getRotation() != Rotation.NONE) {
/* 263 */       LOGGER.info("   - {} with rotation {}: {}", new Object[] { testInfo.id(), testInfo.getRotation().getSerializedName(), testInfo.getError().getDescription().getString() });
/*     */     } else {
/* 265 */       LOGGER.info("   - {}: {}", testInfo.id(), testInfo.getError().getDescription().getString());
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SampleLogger getTickTimeLogger() {
/* 271 */     return (SampleLogger)this.sampleLogger;
/*     */   }
/*     */   
/*     */   public boolean isTickTimeLoggingEnabled() {
/* 276 */     return false;
/*     */   }
/*     */   
/*     */   protected void waitUntilNextTick() {
/* 281 */     runAllTasks();
/*     */   }
/*     */   
/*     */   public SystemReport fillServerSystemReport(SystemReport systemReport) {
/* 286 */     systemReport.setDetail("Type", "Game test server");
/* 287 */     return systemReport;
/*     */   }
/*     */   
/*     */   protected void onServerExit() {
/* 292 */     super.onServerExit();
/* 293 */     LOGGER.info("Game test server shutting down");
/* 294 */     System.exit((this.testTracker != null) ? this.testTracker.getFailedRequiredCount() : -1);
/*     */   }
/*     */   
/*     */   protected void onServerCrash(CrashReport report) {
/* 299 */     super.onServerCrash(report);
/* 300 */     LOGGER.error("Game test server crashed\n{}", report.getFriendlyReport(ReportType.CRASH));
/* 301 */     System.exit(1);
/*     */   }
/*     */   
/*     */   private void startTests(ServerLevel level) {
/* 305 */     RandomSource random = level.getRandom();
/* 309 */     BlockPos startPos = new BlockPos(random.nextIntBetweenInclusive(-14999992, 14999992), -59, random.nextIntBetweenInclusive(-14999992, 14999992));
/* 311 */     level.setRespawnData(LevelData.RespawnData.of(level.dimension(), startPos, 0.0F, 0.0F));
/* 314 */     GameTestRunner runner = GameTestRunner.Builder.fromBatches(this.testBatches, level).newStructureSpawner(new StructureGridSpawner(startPos, 8, false)).build();
/* 315 */     Collection<GameTestInfo> testInfos = runner.getTestInfos();
/* 316 */     this.testTracker = new MultipleTestTracker(testInfos);
/* 317 */     LOGGER.info("{} tests are now running at position {}!", Integer.valueOf(this.testTracker.getTotalCount()), startPos.toShortString());
/* 318 */     this.stopwatch.reset();
/* 319 */     this.stopwatch.start();
/* 320 */     runner.start();
/*     */   }
/*     */   
/*     */   private boolean haveTestsStarted() {
/* 324 */     return (this.testTracker != null);
/*     */   }
/*     */   
/*     */   public boolean isHardcore() {
/* 329 */     return false;
/*     */   }
/*     */   
/*     */   public LevelBasedPermissionSet operatorUserPermissions() {
/* 334 */     return LevelBasedPermissionSet.ALL;
/*     */   }
/*     */   
/*     */   public PermissionSet getFunctionCompilationPermissions() {
/* 339 */     return (PermissionSet)LevelBasedPermissionSet.OWNER;
/*     */   }
/*     */   
/*     */   public boolean shouldRconBroadcast() {
/* 344 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDedicatedServer() {
/* 349 */     return false;
/*     */   }
/*     */   
/*     */   public int getRateLimitPacketsPerSecond() {
/* 354 */     return 0;
/*     */   }
/*     */   
/*     */   public int getCommandSpamThresholdSeconds() {
/* 359 */     return 0;
/*     */   }
/*     */   
/*     */   public int getChatSpamThresholdSeconds() {
/* 364 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean useNativeTransport() {
/* 369 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPublished() {
/* 374 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldInformAdmins() {
/* 379 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isSingleplayerOwner(NameAndId nameAndId) {
/* 384 */     return false;
/*     */   }
/*     */   
/*     */   public int getMaxPlayers() {
/* 389 */     return 1;
/*     */   }
/*     */   
/*     */   private static class MockUserNameToIdResolver implements UserNameToIdResolver {
/* 393 */     private final Set<NameAndId> savedIds = new HashSet<>();
/*     */     
/*     */     public void add(NameAndId nameAndId) {
/* 397 */       this.savedIds.add(nameAndId);
/*     */     }
/*     */     
/*     */     public Optional<NameAndId> get(String name) {
/* 402 */       return this.savedIds.stream().filter(e -> e.name().equals(name)).findFirst()
/* 403 */         .or(() -> Optional.of(NameAndId.createOffline(name)));
/*     */     }
/*     */     
/*     */     public Optional<NameAndId> get(UUID id) {
/* 408 */       return this.savedIds.stream().filter(e -> e.id().equals(id)).findFirst();
/*     */     }
/*     */     
/*     */     public void resolveOfflineUsers(boolean value) {}
/*     */     
/*     */     public void save() {}
/*     */   }
/*     */   
/*     */   private static class MockProfileResolver implements ProfileResolver {
/*     */     public Optional<GameProfile> fetchByName(String name) {
/* 425 */       return Optional.empty();
/*     */     }
/*     */     
/*     */     public Optional<GameProfile> fetchById(UUID id) {
/* 430 */       return Optional.empty();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\gametest\framework\GameTestServer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */