/*     */ package net.minecraft.client.main;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.base.Ticker;
/*     */ import com.mojang.blaze3d.TracyBootstrap;
/*     */ import com.mojang.blaze3d.platform.ClientShutdownWatchdog;
/*     */ import com.mojang.blaze3d.platform.DisplayData;
/*     */ import com.mojang.blaze3d.platform.NativeLibrariesBootstrap;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.util.UndashedUuid;
/*     */ import java.io.File;
/*     */ import java.net.Authenticator;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.Proxy;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.NonOptionArgumentSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import joptsimple.OptionSpecBuilder;
/*     */ import joptsimple.ValueConverter;
/*     */ import joptsimple.util.EnumConverter;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SuppressForbidden;
/*     */ import net.minecraft.client.ClientBootstrap;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.PreferredGraphicsApi;
/*     */ import net.minecraft.client.User;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.client.telemetry.TelemetryProperty;
/*     */ import net.minecraft.client.telemetry.events.GameLoadTimesEvent;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.server.Bootstrap;
/*     */ import net.minecraft.util.NativeModuleLister;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.util.profiling.jfr.Environment;
/*     */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*     */ import org.apache.commons.lang3.StringEscapeUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Main {
/*     */   static {
/*  61 */     System.setProperty("java.awt.headless", "true");
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     OptionParser parser;
/*     */     OptionSet optionSet;
/*     */     Logger logger;
/*     */     GameConfig gameConfig;
/*     */     try {
/*  67 */       SharedConstants.tryDetectVersion();
/*  68 */     } catch (Throwable t) {
/*  69 */       logEarlyException(t);
/*  70 */       System.exit(-7);
/*     */       return;
/*     */     } 
/*     */     try {
/*  78 */       parser = new OptionParser();
/*  79 */     } catch (Throwable t) {
/*  80 */       logEarlyException(t);
/*  81 */       System.exit(-4);
/*     */       return;
/*     */     } 
/*  85 */     parser.allowsUnrecognizedOptions();
/*  87 */     parser.accepts("demo");
/*  88 */     parser.accepts("disableMultiplayer");
/*  89 */     parser.accepts("disableChat");
/*  90 */     parser.accepts("fullscreen");
/*  91 */     parser.accepts("checkGlErrors");
/*  92 */     OptionSpecBuilder optionSpecBuilder1 = parser.accepts("renderDebugLabels");
/*  93 */     OptionSpecBuilder optionSpecBuilder2 = parser.accepts("vulkanValidation");
/*  94 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = parser.accepts("graphicsBackend").withRequiredArg().withValuesConvertedBy((ValueConverter)new EnumConverter<PreferredGraphicsApi>(PreferredGraphicsApi.class) {
/*     */         
/*     */         });
/*  95 */     OptionSpecBuilder optionSpecBuilder3 = parser.accepts("jfrProfile");
/*  96 */     OptionSpecBuilder optionSpecBuilder4 = parser.accepts("tracy");
/*  97 */     OptionSpecBuilder optionSpecBuilder5 = parser.accepts("tracyNoImages");
/*  98 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = parser.accepts("quickPlayPath").withRequiredArg();
/*  99 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec3 = parser.accepts("quickPlaySingleplayer").withOptionalArg();
/* 100 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec4 = parser.accepts("quickPlayMultiplayer").withRequiredArg();
/* 101 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec5 = parser.accepts("quickPlayRealms").withRequiredArg();
/* 102 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec6 = parser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), (Object[])new File[0]);
/* 103 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec7 = parser.accepts("assetsDir").withRequiredArg().ofType(File.class);
/* 104 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec8 = parser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
/* 105 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec9 = parser.accepts("proxyHost").withRequiredArg();
/* 106 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec10 = parser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", (Object[])new String[0]).ofType(Integer.class);
/* 107 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec11 = parser.accepts("proxyUser").withRequiredArg();
/* 108 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec12 = parser.accepts("proxyPass").withRequiredArg();
/* 109 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec13 = parser.accepts("username").withRequiredArg().defaultsTo("Player" + System.currentTimeMillis() % 1000L, (Object[])new String[0]);
/* 110 */     OptionSpecBuilder optionSpecBuilder6 = parser.accepts("offlineDeveloperMode");
/* 111 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec14 = parser.accepts("uuid").withRequiredArg();
/* 112 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec15 = parser.accepts("xuid").withOptionalArg().defaultsTo("", (Object[])new String[0]);
/* 113 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec16 = parser.accepts("clientId").withOptionalArg().defaultsTo("", (Object[])new String[0]);
/* 114 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec17 = parser.accepts("accessToken").withRequiredArg().required();
/* 115 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec18 = parser.accepts("version").withRequiredArg().required();
/* 116 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec19 = parser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(854), (Object[])new Integer[0]);
/* 117 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec20 = parser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(480), (Object[])new Integer[0]);
/* 118 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec21 = parser.accepts("fullscreenWidth").withRequiredArg().ofType(Integer.class);
/* 119 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec22 = parser.accepts("fullscreenHeight").withRequiredArg().ofType(Integer.class);
/* 120 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec23 = parser.accepts("assetIndex").withRequiredArg();
/* 121 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec24 = parser.accepts("versionType").withRequiredArg().defaultsTo("release", (Object[])new String[0]);
/* 122 */     NonOptionArgumentSpec nonOptionArgumentSpec = parser.nonOptions();
/*     */     try {
/* 126 */       optionSet = parser.parse(args);
/* 127 */     } catch (Throwable t) {
/* 128 */       logEarlyException(t);
/* 129 */       System.exit(-5);
/*     */       return;
/*     */     } 
/* 134 */     File gameDir = parseArgument(optionSet, (OptionSpec<File>)argumentAcceptingOptionSpec6);
/* 135 */     String launchedVersion = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec18);
/*     */     try {
/* 142 */       NativeLibrariesBootstrap.loadLibraries();
/* 143 */     } catch (Throwable t) {
/* 144 */       CrashReport report = CrashReport.forThrowable(t, "Loading native libraries");
/* 145 */       CrashReportCategory initialization = report.addCategory("Initialization");
/* 146 */       NativeModuleLister.addCrashSection(initialization);
/* 147 */       Minecraft.fillReport(null, null, launchedVersion, null, report);
/* 149 */       Minecraft.crash(null, gameDir, report, -3);
/*     */       return;
/*     */     } 
/* 154 */     String stage = "Pre-bootstrap";
/*     */     try {
/* 158 */       if (optionSet.has((OptionSpec)optionSpecBuilder3))
/* 159 */         JvmProfiler.INSTANCE.start(Environment.CLIENT); 
/* 161 */       if (optionSet.has((OptionSpec)optionSpecBuilder4))
/* 162 */         TracyBootstrap.setup(); 
/* 165 */       Stopwatch totalTimePreClassLoadTimer = Stopwatch.createStarted(Ticker.systemTicker());
/* 166 */       Stopwatch preWindowPreClassLoadTimer = Stopwatch.createStarted(Ticker.systemTicker());
/* 167 */       GameLoadTimesEvent.INSTANCE.beginStep(TelemetryProperty.LOAD_TIME_TOTAL_TIME_MS, totalTimePreClassLoadTimer);
/* 168 */       GameLoadTimesEvent.INSTANCE.beginStep(TelemetryProperty.LOAD_TIME_PRE_WINDOW_MS, preWindowPreClassLoadTimer);
/* 170 */       TracyClient.reportAppInfo("Minecraft Java Edition " + SharedConstants.getCurrentVersion().name());
/* 171 */       CompletableFuture<?> dataFixerOptimization = DataFixers.optimize(DataFixTypes.TYPES_FOR_LEVEL_LIST);
/* 172 */       CrashReport.preload();
/* 174 */       logger = LogUtils.getLogger();
/* 176 */       stage = "Bootstrap";
/* 177 */       Bootstrap.bootStrap();
/* 178 */       ClientBootstrap.bootstrap();
/* 179 */       GameLoadTimesEvent.INSTANCE.setBootstrapTime(Bootstrap.bootstrapDuration.get());
/* 181 */       Bootstrap.validate();
/* 183 */       stage = "Argument parsing";
/* 185 */       List<String> leftoverArgs = optionSet.valuesOf((OptionSpec)nonOptionArgumentSpec);
/* 186 */       if (!leftoverArgs.isEmpty())
/* 187 */         logger.info("Completely ignored arguments: {}", leftoverArgs); 
/* 191 */       String hostName = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec9);
/* 192 */       Proxy proxy = Proxy.NO_PROXY;
/* 193 */       if (hostName != null)
/*     */         try {
/* 195 */           proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(hostName, ((Integer)parseArgument(optionSet, (OptionSpec<Integer>)argumentAcceptingOptionSpec10)).intValue()));
/* 196 */         } catch (Exception exception) {} 
/* 201 */       final String proxyUser = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec11);
/* 202 */       final String proxyPass = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec12);
/* 203 */       if (!proxy.equals(Proxy.NO_PROXY) && stringHasValue(proxyUser) && stringHasValue(proxyPass))
/* 204 */         Authenticator.setDefault(new Authenticator() {
/*     */               protected PasswordAuthentication getPasswordAuthentication() {
/* 207 */                 return new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
/*     */               }
/*     */             }); 
/* 212 */       int width = ((Integer)parseArgument(optionSet, (OptionSpec<Integer>)argumentAcceptingOptionSpec19)).intValue();
/* 213 */       int height = ((Integer)parseArgument(optionSet, (OptionSpec<Integer>)argumentAcceptingOptionSpec20)).intValue();
/* 214 */       OptionalInt fullscreenWidth = ofNullable(parseArgument(optionSet, (OptionSpec<Integer>)argumentAcceptingOptionSpec21));
/* 215 */       OptionalInt fullscreenHeight = ofNullable(parseArgument(optionSet, (OptionSpec<Integer>)argumentAcceptingOptionSpec22));
/* 216 */       boolean isFullscreen = optionSet.has("fullscreen");
/* 217 */       boolean isDemo = optionSet.has("demo");
/* 218 */       boolean disableMultiplayer = optionSet.has("disableMultiplayer");
/* 219 */       boolean disableChat = optionSet.has("disableChat");
/* 220 */       boolean captureTracyImages = !optionSet.has((OptionSpec)optionSpecBuilder5);
/* 221 */       boolean renderDebugLabels = optionSet.has((OptionSpec)optionSpecBuilder1);
/* 222 */       String versionType = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec24);
/* 223 */       boolean vulkanValidation = optionSet.has((OptionSpec)optionSpecBuilder2);
/* 224 */       PreferredGraphicsApi forcedGraphicsApi = parseArgument(optionSet, (OptionSpec<PreferredGraphicsApi>)argumentAcceptingOptionSpec1);
/* 227 */       File assetsDir = optionSet.has((OptionSpec)argumentAcceptingOptionSpec7) ? parseArgument(optionSet, (OptionSpec<File>)argumentAcceptingOptionSpec7) : new File(gameDir, "assets/");
/* 228 */       File resourcePackDir = optionSet.has((OptionSpec)argumentAcceptingOptionSpec8) ? parseArgument(optionSet, (OptionSpec<File>)argumentAcceptingOptionSpec8) : new File(gameDir, "resourcepacks/");
/* 229 */       UUID uuid = hasValidUuid((OptionSpec<String>)argumentAcceptingOptionSpec14, optionSet, logger) ? UndashedUuid.fromStringLenient((String)argumentAcceptingOptionSpec14.value(optionSet)) : UUIDUtil.createOfflinePlayerUUID((String)argumentAcceptingOptionSpec13.value(optionSet));
/* 230 */       String assetIndex = optionSet.has((OptionSpec)argumentAcceptingOptionSpec23) ? (String)argumentAcceptingOptionSpec23.value(optionSet) : null;
/* 231 */       String xuid = (String)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec15);
/* 232 */       String clientId = (String)optionSet.valueOf((OptionSpec)argumentAcceptingOptionSpec16);
/* 234 */       String quickPlayLogPath = parseArgument(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec2);
/* 235 */       GameConfig.QuickPlayVariant quickPlayVariant = getQuickPlayVariant(optionSet, (OptionSpec<String>)argumentAcceptingOptionSpec3, (OptionSpec<String>)argumentAcceptingOptionSpec4, (OptionSpec<String>)argumentAcceptingOptionSpec5);
/* 237 */       User user = new User((String)argumentAcceptingOptionSpec13.value(optionSet), uuid, (String)argumentAcceptingOptionSpec17.value(optionSet), emptyStringToEmptyOptional(xuid), emptyStringToEmptyOptional(clientId));
/* 242 */       gameConfig = new GameConfig(new GameConfig.UserData(user, proxy), new DisplayData(width, height, fullscreenWidth, fullscreenHeight, isFullscreen), new GameConfig.FolderData(gameDir, resourcePackDir, assetsDir, assetIndex), new GameConfig.GameData(isDemo, launchedVersion, versionType, disableMultiplayer, disableChat, captureTracyImages, vulkanValidation, renderDebugLabels, forcedGraphicsApi, optionSet.has((OptionSpec)optionSpecBuilder6)), new GameConfig.QuickPlayData(quickPlayLogPath, quickPlayVariant));
/* 245 */       Util.startTimerHackThread();
/* 246 */       dataFixerOptimization.join();
/* 247 */     } catch (Throwable t) {
/* 248 */       CrashReport report = CrashReport.forThrowable(t, stage);
/* 249 */       CrashReportCategory initialization = report.addCategory("Initialization");
/* 250 */       NativeModuleLister.addCrashSection(initialization);
/* 251 */       Minecraft.fillReport(null, null, launchedVersion, null, report);
/* 252 */       Minecraft.crash(null, gameDir, report, -1);
/*     */       return;
/*     */     } 
/* 256 */     Thread shutdownThread = new Thread("Client Shutdown Thread") {
/*     */         public void run() {
/* 259 */           Minecraft instance = Minecraft.getInstance();
/* 260 */           if (instance == null)
/*     */             return; 
/* 264 */           IntegratedServer server = instance.getSingleplayerServer();
/* 265 */           if (server != null)
/* 266 */             server.halt(true); 
/*     */         }
/*     */       };
/* 270 */     shutdownThread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(logger));
/* 271 */     Runtime.getRuntime().addShutdownHook(shutdownThread);
/* 273 */     Minecraft minecraft = null;
/*     */     try {
/* 276 */       Thread.currentThread().setName("Render thread");
/* 277 */       RenderSystem.initRenderThread();
/* 278 */       minecraft = new Minecraft(gameConfig);
/* 279 */     } catch (SilentInitException e) {
/* 280 */       Util.shutdownExecutors();
/* 281 */       logger.warn("Failed to create window: ", e);
/*     */       return;
/* 283 */     } catch (Throwable t) {
/* 284 */       CrashReport report = CrashReport.forThrowable(t, "Initializing game");
/* 285 */       CrashReportCategory initialization = report.addCategory("Initialization");
/* 286 */       NativeModuleLister.addCrashSection(initialization);
/* 287 */       Minecraft.fillReport(minecraft, null, gameConfig.game.launchVersion, null, report);
/* 288 */       Minecraft.crash(minecraft, gameConfig.location.gameDirectory, report, -1);
/*     */       return;
/*     */     } 
/* 292 */     minecraft.run();
/*     */     try {
/* 296 */       minecraft.exitWorldAndClose();
/* 297 */     } catch (Throwable t) {
/* 298 */       CrashReport report = CrashReport.forThrowable(t, "Game shutdown");
/* 300 */       Minecraft.fillReport(null, null, launchedVersion, null, report);
/* 302 */       Minecraft.crash(null, gameDir, report, -6);
/*     */       return;
/*     */     } 
/* 308 */     ClientShutdownWatchdog.startShutdownWatchdog("post-main", true, null, gameConfig, Thread.currentThread().threadId());
/*     */   }
/*     */   
/*     */   @SuppressForbidden(reason = "Logging not available yet")
/*     */   private static void logEarlyException(Throwable t) {
/* 315 */     t.printStackTrace();
/*     */   }
/*     */   
/*     */   private static GameConfig.QuickPlayVariant getQuickPlayVariant(OptionSet optionSet, OptionSpec<String> quickPlaySingleplayerOption, OptionSpec<String> quickPlayMultiplayerOption, OptionSpec<String> quickPlayRealmsOption) {
/* 320 */     Objects.requireNonNull(optionSet);
/* 321 */     long enabledOptions = Stream.<OptionSpec>of(new OptionSpec[] { quickPlaySingleplayerOption, quickPlayMultiplayerOption, quickPlayRealmsOption }).filter(optionSet::has).count();
/* 323 */     if (enabledOptions == 0L)
/* 324 */       return GameConfig.QuickPlayVariant.DISABLED; 
/* 327 */     if (enabledOptions > 1L)
/* 328 */       throw new IllegalArgumentException("Only one quick play option can be specified"); 
/* 331 */     if (optionSet.has(quickPlaySingleplayerOption)) {
/* 332 */       String worldId = unescapeJavaArgument(parseArgument(optionSet, quickPlaySingleplayerOption));
/* 333 */       return new GameConfig.QuickPlaySinglePlayerData(worldId);
/*     */     } 
/* 336 */     if (optionSet.has(quickPlayMultiplayerOption)) {
/* 337 */       String serverAddress = unescapeJavaArgument(parseArgument(optionSet, quickPlayMultiplayerOption));
/* 338 */       return (GameConfig.QuickPlayVariant)Optionull.mapOrDefault(serverAddress, QuickPlayMultiplayerData::new, GameConfig.QuickPlayVariant.DISABLED);
/*     */     } 
/* 341 */     if (optionSet.has(quickPlayRealmsOption)) {
/* 342 */       String realmId = unescapeJavaArgument(parseArgument(optionSet, quickPlayRealmsOption));
/* 343 */       return (GameConfig.QuickPlayVariant)Optionull.mapOrDefault(realmId, QuickPlayRealmsData::new, GameConfig.QuickPlayVariant.DISABLED);
/*     */     } 
/* 346 */     return GameConfig.QuickPlayVariant.DISABLED;
/*     */   }
/*     */   
/*     */   private static String unescapeJavaArgument(String arg) {
/* 350 */     if (arg == null)
/* 351 */       return null; 
/* 353 */     return StringEscapeUtils.unescapeJava(arg);
/*     */   }
/*     */   
/*     */   private static Optional<String> emptyStringToEmptyOptional(String xuid) {
/* 357 */     return xuid.isEmpty() ? Optional.<String>empty() : Optional.<String>of(xuid);
/*     */   }
/*     */   
/*     */   private static OptionalInt ofNullable(Integer value) {
/* 361 */     return (value != null) ? OptionalInt.of(value.intValue()) : OptionalInt.empty();
/*     */   }
/*     */   
/*     */   private static <T> T parseArgument(OptionSet optionSet, OptionSpec<T> optionSpec) {
/*     */     try {
/* 366 */       return (T)optionSet.valueOf(optionSpec);
/* 367 */     } catch (Throwable t) {
/* 368 */       if (optionSpec instanceof ArgumentAcceptingOptionSpec) {
/* 368 */         ArgumentAcceptingOptionSpec<T> options = (ArgumentAcceptingOptionSpec<T>)optionSpec;
/* 369 */         List<T> defaultValues = options.defaultValues();
/* 370 */         if (!defaultValues.isEmpty())
/* 371 */           return defaultValues.get(0); 
/*     */       } 
/* 374 */       throw t;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean stringHasValue(String string) {
/* 379 */     return (string != null && !string.isEmpty());
/*     */   }
/*     */   
/*     */   private static boolean hasValidUuid(OptionSpec<String> uuidOption, OptionSet optionSet, Logger logger) {
/* 383 */     return (optionSet.has(uuidOption) && isUuidValid(uuidOption, optionSet, logger));
/*     */   }
/*     */   
/*     */   private static boolean isUuidValid(OptionSpec<String> uuidOption, OptionSet optionSet, Logger logger) {
/*     */     try {
/* 388 */       UndashedUuid.fromStringLenient((String)uuidOption.value(optionSet));
/* 389 */     } catch (IllegalArgumentException e) {
/* 390 */       logger.warn("Invalid UUID: '{}", uuidOption.value(optionSet));
/* 391 */       return false;
/*     */     } 
/* 393 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\main\Main.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */