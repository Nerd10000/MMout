/*     */ package net.minecraft.server;
/*     */ 
/*     */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.awt.GraphicsEnvironment;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.net.Proxy;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import joptsimple.AbstractOptionSpec;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.NonOptionArgumentSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import joptsimple.OptionSpecBuilder;
/*     */ import joptsimple.ValueConverter;
/*     */ import joptsimple.util.PathConverter;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.server.dedicated.DedicatedServerProperties;
/*     */ import net.minecraft.server.dedicated.DedicatedServerSettings;
/*     */ import net.minecraft.server.jsonrpc.JsonRpc;
/*     */ import net.minecraft.server.jsonrpc.ManagementServer;
/*     */ import net.minecraft.server.notifications.NotificationManager;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.profiling.jfr.Environment;
/*     */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*     */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*     */ import net.minecraft.util.worldupdate.WorldUpgrader;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFileVersion;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelDataAndDimensions;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Main {
/*  67 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   @SuppressForbidden(reason = "System.out needed before bootstrap")
/*     */   public static void main(String[] args) {
/*  71 */     SharedConstants.tryDetectVersion();
/*  73 */     OptionParser parser = new OptionParser();
/*  74 */     OptionSpecBuilder optionSpecBuilder1 = parser.accepts("nogui");
/*  75 */     OptionSpecBuilder optionSpecBuilder2 = parser.accepts("initSettings", "Initializes 'server.properties' and 'eula.txt', then quits");
/*  76 */     OptionSpecBuilder optionSpecBuilder3 = parser.accepts("demo");
/*  77 */     OptionSpecBuilder optionSpecBuilder4 = parser.accepts("bonusChest");
/*  78 */     OptionSpecBuilder optionSpecBuilder5 = parser.accepts("forceUpgrade");
/*  79 */     OptionSpecBuilder optionSpecBuilder6 = parser.accepts("eraseCache");
/*  80 */     OptionSpecBuilder optionSpecBuilder7 = parser.accepts("recreateRegionFiles");
/*  81 */     OptionSpecBuilder optionSpecBuilder8 = parser.accepts("safeMode", "Loads level with vanilla datapack only");
/*  82 */     AbstractOptionSpec abstractOptionSpec = parser.accepts("help").forHelp();
/*  83 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = parser.accepts("universe").withRequiredArg().defaultsTo(".", (Object[])new String[0]);
/*  84 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = parser.accepts("world").withRequiredArg();
/*  85 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec3 = parser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(-1), (Object[])new Integer[0]);
/*  86 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec4 = parser.accepts("serverId").withRequiredArg();
/*  87 */     OptionSpecBuilder optionSpecBuilder9 = parser.accepts("jfrProfile");
/*  88 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec5 = parser.accepts("pidFile").withRequiredArg().withValuesConvertedBy((ValueConverter)new PathConverter(new joptsimple.util.PathProperties[0]));
/*  89 */     NonOptionArgumentSpec nonOptionArgumentSpec = parser.nonOptions();
/*     */     try {
/*     */       Dynamic<?> levelDataTag;
/*     */       WorldStem worldStem;
/*  92 */       OptionSet options = parser.parse(args);
/*  94 */       if (options.has((OptionSpec)abstractOptionSpec)) {
/*  95 */         parser.printHelpOn(System.err);
/*     */         return;
/*     */       } 
/*  99 */       Path pidFilePath = (Path)options.valueOf((OptionSpec)argumentAcceptingOptionSpec5);
/* 100 */       if (pidFilePath != null)
/* 101 */         writePidFile(pidFilePath); 
/* 104 */       CrashReport.preload();
/* 107 */       if (options.has((OptionSpec)optionSpecBuilder9))
/* 108 */         JvmProfiler.INSTANCE.start(Environment.SERVER); 
/* 111 */       Bootstrap.bootStrap();
/* 112 */       Bootstrap.validate();
/* 114 */       Util.startTimerHackThread();
/* 116 */       Path settingsFile = Paths.get("server.properties", new String[0]);
/* 117 */       DedicatedServerSettings settings = new DedicatedServerSettings(settingsFile);
/* 118 */       settings.forceSave();
/* 120 */       RegionFileVersion.configure((settings.getProperties()).regionFileComression);
/* 122 */       Path eulaFile = Paths.get("eula.txt", new String[0]);
/* 123 */       Eula eula = new Eula(eulaFile);
/* 125 */       if (options.has((OptionSpec)optionSpecBuilder2)) {
/* 126 */         LOGGER.info("Initialized '{}' and '{}'", settingsFile.toAbsolutePath(), eulaFile.toAbsolutePath());
/*     */         return;
/*     */       } 
/* 130 */       if (!eula.hasAgreedToEULA()) {
/* 131 */         LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
/*     */         return;
/*     */       } 
/* 135 */       File universePath = new File((String)options.valueOf((OptionSpec)argumentAcceptingOptionSpec1));
/* 136 */       Services services = Services.create(new YggdrasilAuthenticationService(Proxy.NO_PROXY), universePath);
/* 137 */       NotificationManager notificationManager = new NotificationManager();
/* 138 */       ManagementServer jsonRpcServer = JsonRpc.create(settings, notificationManager);
/* 140 */       String levelName = Optional.<String>ofNullable((String)options.valueOf((OptionSpec)argumentAcceptingOptionSpec2)).orElse((settings.getProperties()).levelName);
/* 141 */       LevelStorageSource levelStorageSource = LevelStorageSource.createDefault(universePath.toPath());
/* 142 */       LevelStorageSource.LevelStorageAccess access = levelStorageSource.validateAndCreateAccess(levelName);
/* 145 */       if (access.hasWorldData()) {
/*     */         Dynamic<?> levelDataUnfixed;
/*     */         try {
/* 148 */           levelDataUnfixed = access.getUnfixedDataTagWithFallback();
/* 149 */         } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException ex) {
/* 150 */           LOGGER.error("Failed to load world data. World files may be corrupted. Shutting down.", ex);
/*     */           return;
/*     */         } 
/* 153 */         LevelSummary summary = access.fixAndGetSummaryFromTag(levelDataUnfixed);
/* 154 */         if (summary.requiresManualConversion()) {
/* 155 */           LOGGER.info("This world must be opened in an older version (like 1.6.4) to be safely converted");
/*     */           return;
/*     */         } 
/* 158 */         if (!summary.isCompatible()) {
/* 159 */           LOGGER.info("This world was created by an incompatible version.");
/*     */           return;
/*     */         } 
/* 162 */         levelDataTag = DataFixers.getFileFixer().fix(access, levelDataUnfixed, new UpgradeProgress());
/*     */       } else {
/* 164 */         levelDataTag = null;
/*     */       } 
/* 167 */       boolean safeModeEnabled = options.has((OptionSpec)optionSpecBuilder8);
/* 168 */       if (safeModeEnabled)
/* 169 */         LOGGER.warn("Safe mode active, only vanilla datapack will be loaded"); 
/* 172 */       PackRepository packRepository = ServerPacksSource.createPackRepository(access);
/*     */       try {
/* 176 */         WorldLoader.InitConfig worldLoadConfig = loadOrCreateConfig(settings.getProperties(), levelDataTag, safeModeEnabled, packRepository);
/* 198 */         worldStem = Util.blockUntilDone(executor -> WorldLoader.load(worldLoadConfig, (), WorldStem::new, (Executor)Util.backgroundExecutor(), executor)).get();
/* 199 */       } catch (Exception e) {
/* 200 */         LOGGER.warn("Failed to load datapacks, can't proceed with server load. You can either fix your datapacks or reset to vanilla with --safeMode", e);
/*     */         return;
/*     */       } 
/* 204 */       RegistryAccess.Frozen registryHolder = worldStem.registries().compositeAccess();
/* 205 */       WorldData data = worldStem.worldDataAndGenSettings().data();
/* 207 */       boolean recreateRegionFilesValue = options.has((OptionSpec)optionSpecBuilder7);
/* 208 */       if (options.has((OptionSpec)optionSpecBuilder5) || recreateRegionFilesValue)
/* 209 */         forceUpgrade(access, DataFixers.getDataFixer(), options.has((OptionSpec)optionSpecBuilder6), () -> true, (RegistryAccess)registryHolder, recreateRegionFilesValue); 
/* 212 */       access.saveDataTag(data);
/* 214 */       final DedicatedServer dedicatedServer = MinecraftServer.<DedicatedServer>spin(thread -> {
/*     */             DedicatedServer server = new DedicatedServer(thread, access, packRepository, worldStem, Optional.empty(), settings, DataFixers.getDataFixer(), services, jsonRpcServer, notificationManager);
/*     */             notificationManager.setServer(server);
/*     */             server.setPort(((Integer)options.valueOf(port)).intValue());
/*     */             server.setDemo(options.has(demo));
/*     */             server.setId((String)options.valueOf(serverId));
/* 222 */             boolean gui = (!options.has(nogui) && !options.valuesOf(nonOptions).contains("nogui"));
/*     */             if (gui && !GraphicsEnvironment.isHeadless())
/*     */               server.showGui(); 
/*     */             return server;
/*     */           });
/* 229 */       Thread shutdownThread = new Thread("Server Shutdown Thread") {
/*     */           public void run() {
/* 232 */             dedicatedServer.halt(true);
/*     */           }
/*     */         };
/* 235 */       shutdownThread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER));
/* 236 */       Runtime.getRuntime().addShutdownHook(shutdownThread);
/* 237 */     } catch (Throwable t) {
/* 238 */       LOGGER.error(LogUtils.FATAL_MARKER, "Failed to start the minecraft server", t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static WorldLoader.DataLoadOutput<LevelDataAndDimensions.WorldDataAndGenSettings> createNewWorldData(DedicatedServerSettings settings, WorldLoader.DataLoadContext context, Registry<LevelStem> datapackDimensions, boolean demoMode, boolean bonusChest) {
/*     */     LevelSettings createLevelSettings;
/*     */     WorldOptions worldOptions;
/*     */     WorldDimensions dimensions;
/* 246 */     if (demoMode) {
/* 247 */       createLevelSettings = MinecraftServer.DEMO_SETTINGS;
/* 248 */       worldOptions = WorldOptions.DEMO_OPTIONS;
/* 249 */       dimensions = WorldPresets.createNormalWorldDimensions(context.datapackWorldgen());
/*     */     } else {
/* 251 */       DedicatedServerProperties properties = settings.getProperties();
/* 261 */       createLevelSettings = new LevelSettings(properties.levelName, (GameType)properties.gameMode.get(), new LevelSettings.DifficultySettings((Difficulty)properties.difficulty.get(), properties.hardcore, false), false, context.dataConfiguration());
/* 263 */       worldOptions = bonusChest ? properties.worldOptions.withBonusChest(true) : properties.worldOptions;
/* 264 */       dimensions = properties.createDimensions(context.datapackWorldgen());
/*     */     } 
/* 266 */     WorldDimensions.Complete finalDimensions = dimensions.bake(datapackDimensions);
/* 267 */     Lifecycle lifecycle = finalDimensions.lifecycle().add(context.datapackWorldgen().allRegistriesLifecycle());
/* 268 */     PrimaryLevelData primaryLevelData = new PrimaryLevelData(createLevelSettings, finalDimensions.specialWorldProperty(), lifecycle);
/* 269 */     return new WorldLoader.DataLoadOutput<>(new LevelDataAndDimensions.WorldDataAndGenSettings((WorldData)primaryLevelData, new WorldGenSettings(worldOptions, dimensions)), finalDimensions
/*     */         
/* 271 */         .dimensionsRegistryAccess());
/*     */   }
/*     */   
/*     */   private static void writePidFile(Path path) {
/*     */     try {
/* 277 */       long pid = ProcessHandle.current().pid();
/* 278 */       Files.writeString(path, Long.toString(pid), new java.nio.file.OpenOption[0]);
/* 279 */     } catch (IOException e) {
/* 280 */       throw new UncheckedIOException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static WorldLoader.InitConfig loadOrCreateConfig(DedicatedServerProperties properties, Dynamic<?> levelDataTag, boolean safeModeEnabled, PackRepository packRepository) {
/*     */     boolean initMode;
/*     */     WorldDataConfiguration dataConfigToUse;
/* 287 */     if (levelDataTag != null) {
/* 288 */       WorldDataConfiguration storedConfiguration = LevelStorageSource.readDataConfig(levelDataTag);
/* 289 */       initMode = false;
/* 290 */       dataConfigToUse = storedConfiguration;
/*     */     } else {
/* 292 */       initMode = true;
/* 293 */       dataConfigToUse = new WorldDataConfiguration(properties.initialDataPackConfiguration, FeatureFlags.DEFAULT_FLAGS);
/*     */     } 
/* 295 */     WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, dataConfigToUse, safeModeEnabled, initMode);
/* 296 */     return new WorldLoader.InitConfig(packConfig, Commands.CommandSelection.DEDICATED, (PermissionSet)properties.functionPermissions);
/*     */   }
/*     */   
/*     */   private static void forceUpgrade(LevelStorageSource.LevelStorageAccess storageSource, DataFixer fixerUpper, boolean eraseCache, BooleanSupplier isRunning, RegistryAccess registryAccess, boolean recreateRegionFiles) {
/* 300 */     LOGGER.info("Forcing world upgrade!");
/* 302 */     WorldUpgrader upgrader = new WorldUpgrader(storageSource, fixerUpper, registryAccess, eraseCache, recreateRegionFiles);
/*     */     try {
/* 303 */       Component lastStatus = null;
/* 304 */       while (!upgrader.isFinished()) {
/* 305 */         Component status = upgrader.getStatus();
/* 306 */         if (lastStatus != status) {
/* 307 */           lastStatus = status;
/* 308 */           LOGGER.info(upgrader.getStatus().getString());
/*     */         } 
/* 310 */         int totalChunks = upgrader.getTotalChunks();
/* 311 */         if (totalChunks > 0) {
/* 312 */           int done = upgrader.getConverted() + upgrader.getSkipped();
/* 313 */           LOGGER.info("{}% completed ({} / {} chunks)...", new Object[] { Integer.valueOf(Mth.floor(done / totalChunks * 100.0F)), Integer.valueOf(done), Integer.valueOf(totalChunks) });
/*     */         } 
/* 316 */         if (!isRunning.getAsBoolean()) {
/* 317 */           upgrader.cancel();
/*     */           continue;
/*     */         } 
/*     */         try {
/* 320 */           Thread.sleep(1000L);
/* 321 */         } catch (InterruptedException interruptedException) {}
/*     */       } 
/* 325 */       upgrader.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         upgrader.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\Main.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */