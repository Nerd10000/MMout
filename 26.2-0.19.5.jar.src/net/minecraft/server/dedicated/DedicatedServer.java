/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Writer;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.URI;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandlerWithName;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundLowDiskSpaceWarningPacket;
/*     */ import net.minecraft.server.ConsoleInput;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerInterface;
/*     */ import net.minecraft.server.ServerLinks;
/*     */ import net.minecraft.server.Services;
/*     */ import net.minecraft.server.WorldStem;
/*     */ import net.minecraft.server.gui.MinecraftServerGui;
/*     */ import net.minecraft.server.jsonrpc.ManagementServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.progress.LevelLoadListener;
/*     */ import net.minecraft.server.level.progress.LoggingLevelLoadListener;
/*     */ import net.minecraft.server.network.ServerTextFilter;
/*     */ import net.minecraft.server.network.TextFilter;
/*     */ import net.minecraft.server.notifications.NotificationManager;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.Permission;
/*     */ import net.minecraft.server.permissions.PermissionLevel;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.server.players.OldUsersConverter;
/*     */ import net.minecraft.server.players.PlayerList;
/*     */ import net.minecraft.server.rcon.RconConsoleSource;
/*     */ import net.minecraft.server.rcon.thread.QueryThreadGs4;
/*     */ import net.minecraft.server.rcon.thread.RconThread;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debugchart.RemoteDebugSampleType;
/*     */ import net.minecraft.util.debugchart.RemoteSampleLogger;
/*     */ import net.minecraft.util.debugchart.SampleLogger;
/*     */ import net.minecraft.util.debugchart.TpsDebugDimensions;
/*     */ import net.minecraft.util.monitoring.jmx.MinecraftServerStatistics;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DedicatedServer extends MinecraftServer implements ServerInterface {
/*  76 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int CONVERSION_RETRY_DELAY_MS = 5000;
/*     */   
/*     */   private static final int CONVERSION_RETRIES = 2;
/*     */   
/*  80 */   private final List<ConsoleInput> consoleInput = Collections.synchronizedList(Lists.newArrayList());
/*     */   
/*     */   private QueryThreadGs4 queryThreadGs4;
/*     */   
/*     */   private final RconConsoleSource rconConsoleSource;
/*     */   
/*     */   private RconThread rconThread;
/*     */   
/*     */   private final DedicatedServerSettings settings;
/*     */   
/*     */   private MinecraftServerGui gui;
/*     */   
/*     */   private final ServerTextFilter serverTextFilter;
/*     */   
/*     */   private RemoteSampleLogger tickTimeLogger;
/*     */   
/*     */   private boolean isTickTimeLoggingEnabled;
/*     */   
/*     */   private final ServerLinks serverLinks;
/*     */   
/*     */   private final Map<String, String> codeOfConductTexts;
/*     */   
/*     */   private final ManagementServer jsonRpcServer;
/*     */   
/*     */   public DedicatedServer(Thread serverThread, LevelStorageSource.LevelStorageAccess levelStorageSource, PackRepository packRepository, WorldStem worldStem, Optional<GameRules> gameRules, DedicatedServerSettings settings, DataFixer fixerUpper, Services services, ManagementServer jsonRpcServer, NotificationManager notificationManager) {
/*  94 */     super(serverThread, levelStorageSource, packRepository, worldStem, gameRules, Proxy.NO_PROXY, fixerUpper, services, (LevelLoadListener)LoggingLevelLoadListener.forDedicatedServer(), true, notificationManager);
/*  95 */     this.settings = settings;
/*  96 */     this.rconConsoleSource = new RconConsoleSource(this);
/*  97 */     this.serverTextFilter = ServerTextFilter.createFromConfig(settings.getProperties());
/*  98 */     this.jsonRpcServer = jsonRpcServer;
/* 100 */     this.serverLinks = createServerLinks(settings);
/* 101 */     if ((settings.getProperties()).codeOfConduct) {
/* 102 */       this.codeOfConductTexts = readCodeOfConducts();
/*     */     } else {
/* 104 */       this.codeOfConductTexts = Map.of();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Map<String, String> readCodeOfConducts() {
/* 109 */     Path path = Path.of("codeofconduct", new String[0]);
/* 110 */     if (!Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS }))
/* 111 */       throw new IllegalArgumentException("Code of Conduct folder does not exist: " + String.valueOf(path)); 
/*     */     try {
/* 114 */       ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
/* 115 */       Stream<Path> files = Files.list(path);
/*     */       try {
/* 116 */         for (Path file : files.toList()) {
/* 117 */           String filename = file.getFileName().toString();
/* 118 */           if (filename.endsWith(".txt")) {
/* 119 */             String language = filename.substring(0, filename.length() - 4).toLowerCase(Locale.ROOT);
/* 120 */             if (!file.toRealPath(new LinkOption[0]).getParent().equals(path.toAbsolutePath()))
/* 121 */               throw new IllegalArgumentException("Failed to read Code of Conduct file \"" + filename + "\" because it links to a file outside the allowed directory"); 
/*     */             try {
/* 124 */               String codeOfConduct = String.join("\n", (Iterable)Files.readAllLines(file, StandardCharsets.UTF_8));
/* 125 */               builder.put(language, StringUtil.stripColor(codeOfConduct));
/* 126 */             } catch (IOException e) {
/* 127 */               throw new IllegalArgumentException("Failed to read Code of Conduct file " + filename, e);
/*     */             } 
/*     */           } 
/*     */         } 
/* 131 */         if (files != null)
/* 131 */           files.close(); 
/*     */       } catch (Throwable throwable) {
/*     */         if (files != null)
/*     */           try {
/*     */             files.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 132 */       return (Map<String, String>)builder.build();
/* 133 */     } catch (IOException e) {
/* 134 */       throw new IllegalArgumentException("Failed to read Code of Conduct folder", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean initServer() throws IOException {
/* 140 */     Thread consoleThread = new Thread("Server console handler") {
/*     */         public void run() {
/* 143 */           BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
/*     */           try {
/*     */             String line;
/* 146 */             while (!DedicatedServer.this.isStopped() && DedicatedServer.this.isRunning() && (line = reader.readLine()) != null)
/* 147 */               DedicatedServer.this.handleConsoleInput(line, DedicatedServer.this.createCommandSourceStack()); 
/* 149 */           } catch (IOException e) {
/* 150 */             DedicatedServer.LOGGER.error("Exception handling console input", e);
/*     */           } 
/*     */         }
/*     */       };
/* 154 */     consoleThread.setDaemon(true);
/* 155 */     consoleThread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER));
/* 156 */     consoleThread.start();
/* 158 */     LOGGER.info("Starting minecraft server version {}", SharedConstants.getCurrentVersion().name());
/* 160 */     if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L)
/* 161 */       LOGGER.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\""); 
/* 164 */     LOGGER.info("Loading properties");
/* 165 */     DedicatedServerProperties properties = this.settings.getProperties();
/* 167 */     if (isSingleplayer()) {
/* 168 */       setLocalIp("127.0.0.1");
/*     */     } else {
/* 170 */       setUsesAuthentication(properties.onlineMode);
/* 171 */       setPreventProxyConnections(properties.preventProxyConnections);
/* 172 */       setLocalIp(properties.serverIp);
/*     */     } 
/* 176 */     this.worldData.setGameType(properties.gameMode.get());
/* 177 */     LOGGER.info("Default game type: {}", properties.gameMode.get());
/* 179 */     InetAddress localAddress = null;
/* 180 */     if (!getLocalIp().isEmpty())
/* 181 */       localAddress = InetAddress.getByName(getLocalIp()); 
/* 183 */     if (getPort() < 0)
/* 184 */       setPort(properties.serverPort); 
/* 187 */     initializeKeyPair();
/* 189 */     LOGGER.info("Starting Minecraft server on {}:{}", getLocalIp().isEmpty() ? "*" : getLocalIp(), Integer.valueOf(getPort()));
/*     */     try {
/* 191 */       getConnection().startTcpServerListener(localAddress, getPort());
/* 192 */     } catch (IOException e) {
/* 193 */       LOGGER.warn("**** FAILED TO BIND TO PORT!");
/* 194 */       LOGGER.warn("The exception was: {}", e.toString());
/* 195 */       LOGGER.warn("Perhaps a server is already running on that port?");
/* 196 */       return false;
/*     */     } 
/* 199 */     if (!usesAuthentication()) {
/* 200 */       LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
/* 201 */       LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
/* 202 */       LOGGER.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
/* 203 */       LOGGER.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
/*     */     } 
/* 206 */     if (convertOldUsers())
/* 207 */       this.services.nameToIdCache().save(); 
/* 209 */     if (!OldUsersConverter.areOldUserlistsRemoved())
/* 210 */       return false; 
/* 213 */     setPlayerList(new DedicatedPlayerList(this, registries(), this.playerDataStorage));
/* 214 */     this.tickTimeLogger = new RemoteSampleLogger((TpsDebugDimensions.values()).length, debugSubscribers(), RemoteDebugSampleType.TICK_TIME);
/* 216 */     long levelNanoTime = Util.getNanos();
/* 218 */     this.services.nameToIdCache().resolveOfflineUsers(!usesAuthentication());
/* 220 */     LOGGER.info("Preparing level \"{}\"", getLevelIdName());
/* 221 */     loadLevel();
/* 222 */     long elapsed = Util.getNanos() - levelNanoTime;
/* 223 */     String time = String.format(Locale.ROOT, "%.3fs", new Object[] { Double.valueOf(elapsed / 1.0E9D) });
/* 224 */     LOGGER.info("Done ({})! For help, type \"help\"", time);
/* 227 */     if (properties.announcePlayerAchievements != null)
/* 228 */       getGameRules().set(GameRules.SHOW_ADVANCEMENT_MESSAGES, properties.announcePlayerAchievements, this); 
/* 231 */     if (properties.enableQuery) {
/* 232 */       LOGGER.info("Starting GS4 status listener");
/* 233 */       this.queryThreadGs4 = QueryThreadGs4.create(this);
/*     */     } 
/* 235 */     if (properties.enableRcon) {
/* 236 */       LOGGER.info("Starting remote control listener");
/* 237 */       this.rconThread = RconThread.create(this);
/*     */     } 
/* 240 */     if (getMaxTickLength() > 0L) {
/* 241 */       Thread watchdog = new Thread(new ServerWatchdog(this), "Server Watchdog");
/* 242 */       watchdog.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandlerWithName(LOGGER));
/* 243 */       watchdog.setDaemon(true);
/* 244 */       watchdog.start();
/*     */     } 
/* 247 */     if (properties.enableJmxMonitoring) {
/* 248 */       MinecraftServerStatistics.registerJmxMonitoring(this);
/* 249 */       LOGGER.info("JMX monitoring enabled");
/*     */     } 
/* 251 */     saveEverything(false, true, true);
/* 252 */     notificationManager().serverStarted();
/* 253 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isEnforceWhitelist() {
/* 258 */     return ((Boolean)(this.settings.getProperties()).enforceWhitelist.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public void setEnforceWhitelist(boolean enforceWhitelist) {
/* 263 */     this.settings.update(p -> (DedicatedServerProperties)p.enforceWhitelist.update((RegistryAccess)registryAccess(), Boolean.valueOf(enforceWhitelist)));
/*     */   }
/*     */   
/*     */   public boolean isUsingWhitelist() {
/* 268 */     return ((Boolean)(this.settings.getProperties()).whiteList.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public void setUsingWhitelist(boolean usingWhitelist) {
/* 273 */     this.settings.update(p -> (DedicatedServerProperties)p.whiteList.update((RegistryAccess)registryAccess(), Boolean.valueOf(usingWhitelist)));
/*     */   }
/*     */   
/*     */   protected void tickServer(BooleanSupplier haveTime) {
/* 278 */     super.tickServer(haveTime);
/* 279 */     if (this.jsonRpcServer != null)
/* 280 */       this.jsonRpcServer.tick(); 
/*     */   }
/*     */   
/*     */   public boolean saveAllChunks(boolean silent, boolean flush, boolean force) {
/* 286 */     notificationManager().serverSaveStarted();
/* 287 */     boolean savedChunks = super.saveAllChunks(silent, flush, force);
/* 288 */     notificationManager().serverSaveCompleted();
/* 289 */     return savedChunks;
/*     */   }
/*     */   
/*     */   public void sendLowDiskSpaceWarning() {
/* 294 */     super.sendLowDiskSpaceWarning();
/* 295 */     Permission.HasCommandLevel adminCheck = new Permission.HasCommandLevel(PermissionLevel.ADMINS);
/* 296 */     getPlayerList()
/* 297 */       .getPlayers()
/* 298 */       .stream()
/* 299 */       .filter(p -> p.permissions().hasPermission((Permission)adminCheck))
/* 300 */       .forEach(p -> p.connection.send((Packet)ClientboundLowDiskSpaceWarningPacket.INSTANCE));
/*     */   }
/*     */   
/*     */   public boolean allowFlight() {
/* 305 */     return ((Boolean)(this.settings.getProperties()).allowFlight.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public void setAllowFlight(boolean allowed) {
/* 309 */     this.settings.update(p -> (DedicatedServerProperties)p.allowFlight.update((RegistryAccess)registryAccess(), Boolean.valueOf(allowed)));
/*     */   }
/*     */   
/*     */   public DedicatedServerProperties getProperties() {
/* 314 */     return this.settings.getProperties();
/*     */   }
/*     */   
/*     */   public void setDifficulty(Difficulty difficulty) {
/* 318 */     this.settings.update(p -> (DedicatedServerProperties)p.difficulty.update((RegistryAccess)registryAccess(), difficulty));
/* 319 */     forceDifficulty();
/*     */   }
/*     */   
/*     */   protected void forceDifficulty() {
/* 324 */     setDifficulty((getProperties()).difficulty.get(), true);
/*     */   }
/*     */   
/*     */   public int viewDistance() {
/* 328 */     return ((Integer)(this.settings.getProperties()).viewDistance.get()).intValue();
/*     */   }
/*     */   
/*     */   public void setViewDistance(int viewDistance) {
/* 332 */     this.settings.update(p -> (DedicatedServerProperties)p.viewDistance.update((RegistryAccess)registryAccess(), Integer.valueOf(viewDistance)));
/* 333 */     getPlayerList().setViewDistance(viewDistance);
/*     */   }
/*     */   
/*     */   public int simulationDistance() {
/* 337 */     return ((Integer)(this.settings.getProperties()).simulationDistance.get()).intValue();
/*     */   }
/*     */   
/*     */   public void setSimulationDistance(int simulationDistance) {
/* 341 */     this.settings.update(p -> (DedicatedServerProperties)p.simulationDistance.update((RegistryAccess)registryAccess(), Integer.valueOf(simulationDistance)));
/* 342 */     getPlayerList().setSimulationDistance(simulationDistance);
/*     */   }
/*     */   
/*     */   public SystemReport fillServerSystemReport(SystemReport systemReport) {
/* 347 */     systemReport.setDetail("Is Modded", () -> getModdedStatus().fullDescription());
/* 348 */     systemReport.setDetail("Type", () -> "Dedicated Server");
/* 350 */     return systemReport;
/*     */   }
/*     */   
/*     */   public void dumpServerProperties(Path path) throws IOException {
/* 355 */     DedicatedServerProperties serverProperties = getProperties();
/* 357 */     Writer output = Files.newBufferedWriter(path, new java.nio.file.OpenOption[0]);
/*     */     try {
/* 358 */       output.write(String.format(Locale.ROOT, "sync-chunk-writes=%s%n", new Object[] { Boolean.valueOf(serverProperties.syncChunkWrites) }));
/* 359 */       output.write(String.format(Locale.ROOT, "gamemode=%s%n", new Object[] { serverProperties.gameMode.get() }));
/* 360 */       output.write(String.format(Locale.ROOT, "entity-broadcast-range-percentage=%d%n", new Object[] { serverProperties.entityBroadcastRangePercentage.get() }));
/* 361 */       output.write(String.format(Locale.ROOT, "max-world-size=%d%n", new Object[] { Integer.valueOf(serverProperties.maxWorldSize) }));
/* 362 */       output.write(String.format(Locale.ROOT, "view-distance=%d%n", new Object[] { serverProperties.viewDistance.get() }));
/* 363 */       output.write(String.format(Locale.ROOT, "simulation-distance=%d%n", new Object[] { serverProperties.simulationDistance.get() }));
/* 364 */       output.write(String.format(Locale.ROOT, "generate-structures=%s%n", new Object[] { Boolean.valueOf(serverProperties.worldOptions.generateStructures()) }));
/* 365 */       output.write(String.format(Locale.ROOT, "use-native=%s%n", new Object[] { Boolean.valueOf(serverProperties.useNativeTransport) }));
/* 366 */       output.write(String.format(Locale.ROOT, "rate-limit=%d%n", new Object[] { Integer.valueOf(serverProperties.rateLimitPacketsPerSecond) }));
/* 367 */       output.write(String.format(Locale.ROOT, "command-spam-threshold-seconds=%d%n", new Object[] { Integer.valueOf(serverProperties.commandSpamThresholdSeconds) }));
/* 368 */       output.write(String.format(Locale.ROOT, "chat-spam-threshold-seconds=%d%n", new Object[] { Integer.valueOf(serverProperties.chatSpamThresholdSeconds) }));
/* 369 */       if (output != null)
/* 369 */         output.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (output != null)
/*     */         try {
/*     */           output.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onServerExit() {
/* 374 */     if (this.serverTextFilter != null)
/* 375 */       this.serverTextFilter.close(); 
/* 378 */     if (this.gui != null)
/* 379 */       this.gui.close(); 
/* 382 */     if (this.rconThread != null)
/* 383 */       this.rconThread.stop(); 
/* 386 */     if (this.queryThreadGs4 != null)
/* 387 */       this.queryThreadGs4.stop(); 
/* 390 */     if (this.jsonRpcServer != null)
/*     */       try {
/* 392 */         this.jsonRpcServer.stop(true);
/* 393 */       } catch (InterruptedException e) {
/* 394 */         LOGGER.error("Interrupted while stopping the management server", e);
/*     */       }  
/*     */   }
/*     */   
/*     */   protected void tickConnection() {
/* 401 */     super.tickConnection();
/* 402 */     handleConsoleInputs();
/*     */   }
/*     */   
/*     */   public void handleConsoleInput(String msg, CommandSourceStack source) {
/* 406 */     this.consoleInput.add(new ConsoleInput(msg, source));
/*     */   }
/*     */   
/*     */   public void handleConsoleInputs() {
/* 410 */     while (!this.consoleInput.isEmpty()) {
/* 411 */       ConsoleInput input = this.consoleInput.remove(0);
/* 412 */       getCommands().performPrefixedCommand(input.source, input.msg);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isDedicatedServer() {
/* 418 */     return true;
/*     */   }
/*     */   
/*     */   public int getRateLimitPacketsPerSecond() {
/* 423 */     return (getProperties()).rateLimitPacketsPerSecond;
/*     */   }
/*     */   
/*     */   public int getCommandSpamThresholdSeconds() {
/* 428 */     return (getProperties()).commandSpamThresholdSeconds;
/*     */   }
/*     */   
/*     */   public int getChatSpamThresholdSeconds() {
/* 433 */     return (getProperties()).chatSpamThresholdSeconds;
/*     */   }
/*     */   
/*     */   public boolean useNativeTransport() {
/* 438 */     return (getProperties()).useNativeTransport;
/*     */   }
/*     */   
/*     */   public DedicatedPlayerList getPlayerList() {
/* 443 */     return (DedicatedPlayerList)super.getPlayerList();
/*     */   }
/*     */   
/*     */   public int getMaxPlayers() {
/* 448 */     return ((Integer)(this.settings.getProperties()).maxPlayers.get()).intValue();
/*     */   }
/*     */   
/*     */   public void setMaxPlayers(int maxPlayers) {
/* 452 */     this.settings.update(p -> (DedicatedServerProperties)p.maxPlayers.update((RegistryAccess)registryAccess(), Integer.valueOf(maxPlayers)));
/*     */   }
/*     */   
/*     */   public boolean isPublished() {
/* 457 */     return true;
/*     */   }
/*     */   
/*     */   public String getServerIp() {
/* 462 */     return getLocalIp();
/*     */   }
/*     */   
/*     */   public int getServerPort() {
/* 467 */     return getPort();
/*     */   }
/*     */   
/*     */   public String getServerName() {
/* 472 */     return getMotd();
/*     */   }
/*     */   
/*     */   public void showGui() {
/* 476 */     if (this.gui == null)
/* 477 */       this.gui = MinecraftServerGui.showFrameFor(this); 
/*     */   }
/*     */   
/*     */   public int spawnProtectionRadius() {
/* 482 */     return ((Integer)(getProperties()).spawnProtection.get()).intValue();
/*     */   }
/*     */   
/*     */   public void setSpawnProtectionRadius(int spawnProtectionRadius) {
/* 486 */     this.settings.update(p -> (DedicatedServerProperties)p.spawnProtection.update((RegistryAccess)registryAccess(), Integer.valueOf(spawnProtectionRadius)));
/*     */   }
/*     */   
/*     */   public boolean isUnderSpawnProtection(ServerLevel level, BlockPos pos, Player player) {
/* 491 */     LevelData.RespawnData respawnData = level.getRespawnData();
/* 492 */     if (level.dimension() != respawnData.dimension())
/* 493 */       return false; 
/* 497 */     if (getPlayerList().getOps().isEmpty())
/* 498 */       return false; 
/* 500 */     if (getPlayerList().isOp(player.nameAndId()))
/* 501 */       return false; 
/* 503 */     if (spawnProtectionRadius() <= 0)
/* 504 */       return false; 
/* 507 */     BlockPos spawnPos = respawnData.pos();
/* 508 */     int xd = Mth.abs(pos.getX() - spawnPos.getX());
/* 509 */     int zd = Mth.abs(pos.getZ() - spawnPos.getZ());
/* 510 */     int dist = Math.max(xd, zd);
/* 512 */     return (dist <= spawnProtectionRadius());
/*     */   }
/*     */   
/*     */   public boolean repliesToStatus() {
/* 517 */     return ((Boolean)(getProperties()).enableStatus.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public void setRepliesToStatus(boolean enable) {
/* 521 */     this.settings.update(p -> (DedicatedServerProperties)p.enableStatus.update((RegistryAccess)registryAccess(), Boolean.valueOf(enable)));
/*     */   }
/*     */   
/*     */   public boolean hidesOnlinePlayers() {
/* 526 */     return ((Boolean)(getProperties()).hideOnlinePlayers.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public void setHidesOnlinePlayers(boolean hide) {
/* 530 */     this.settings.update(p -> (DedicatedServerProperties)p.hideOnlinePlayers.update((RegistryAccess)registryAccess(), Boolean.valueOf(hide)));
/*     */   }
/*     */   
/*     */   public LevelBasedPermissionSet operatorUserPermissions() {
/* 535 */     return (getProperties()).opPermissions.get();
/*     */   }
/*     */   
/*     */   public void setOperatorUserPermissions(LevelBasedPermissionSet permissions) {
/* 539 */     this.settings.update(p -> (DedicatedServerProperties)p.opPermissions.update((RegistryAccess)registryAccess(), permissions));
/*     */   }
/*     */   
/*     */   public PermissionSet getFunctionCompilationPermissions() {
/* 544 */     return (PermissionSet)(getProperties()).functionPermissions;
/*     */   }
/*     */   
/*     */   public int playerIdleTimeout() {
/* 549 */     return ((Integer)(this.settings.getProperties()).playerIdleTimeout.get()).intValue();
/*     */   }
/*     */   
/*     */   public void setPlayerIdleTimeout(int playerIdleTimeout) {
/* 554 */     this.settings.update(p -> (DedicatedServerProperties)p.playerIdleTimeout.update((RegistryAccess)registryAccess(), Integer.valueOf(playerIdleTimeout)));
/*     */   }
/*     */   
/*     */   public int statusHeartbeatInterval() {
/* 558 */     return ((Integer)(this.settings.getProperties()).statusHeartbeatInterval.get()).intValue();
/*     */   }
/*     */   
/*     */   public boolean setStatusHeartbeatInterval(int statusHeartbeatInterval) {
/* 562 */     if (this.jsonRpcServer != null && 
/* 563 */       this.jsonRpcServer.scheduleHeartbeat(notificationManager(), statusHeartbeatInterval)) {
/* 564 */       this.settings.update(p -> (DedicatedServerProperties)p.statusHeartbeatInterval.update((RegistryAccess)registryAccess(), Integer.valueOf(statusHeartbeatInterval)));
/* 565 */       return true;
/*     */     } 
/* 568 */     return false;
/*     */   }
/*     */   
/*     */   public String getMotd() {
/* 573 */     return (this.settings.getProperties()).motd.get();
/*     */   }
/*     */   
/*     */   public void setMotd(String motd) {
/* 578 */     this.settings.update(p -> (DedicatedServerProperties)p.motd.update((RegistryAccess)registryAccess(), motd));
/*     */   }
/*     */   
/*     */   public boolean shouldRconBroadcast() {
/* 583 */     return (getProperties()).broadcastRconToOps;
/*     */   }
/*     */   
/*     */   public boolean shouldInformAdmins() {
/* 588 */     return (getProperties()).broadcastConsoleToOps;
/*     */   }
/*     */   
/*     */   public int getAbsoluteMaxWorldSize() {
/* 593 */     return (getProperties()).maxWorldSize;
/*     */   }
/*     */   
/*     */   public int getCompressionThreshold() {
/* 598 */     return (getProperties()).networkCompressionThreshold;
/*     */   }
/*     */   
/*     */   public boolean enforceSecureProfile() {
/* 603 */     DedicatedServerProperties properties = getProperties();
/* 604 */     return (properties.enforceSecureProfile && properties.onlineMode && this.services.canValidateProfileKeys());
/*     */   }
/*     */   
/*     */   public boolean logIPs() {
/* 609 */     return (getProperties()).logIPs;
/*     */   }
/*     */   
/*     */   protected boolean convertOldUsers() {
/* 615 */     boolean userBanlistConverted = false;
/* 616 */     int retries = 0;
/* 617 */     while (!userBanlistConverted && retries <= 2) {
/* 618 */       if (retries > 0) {
/* 619 */         LOGGER.warn("Encountered a problem while converting the user banlist, retrying in a few seconds");
/* 620 */         waitForRetry();
/*     */       } 
/* 622 */       userBanlistConverted = OldUsersConverter.convertUserBanlist(this);
/* 623 */       retries++;
/*     */     } 
/* 626 */     boolean ipBanlistConverted = false;
/* 627 */     retries = 0;
/* 628 */     while (!ipBanlistConverted && retries <= 2) {
/* 629 */       if (retries > 0) {
/* 630 */         LOGGER.warn("Encountered a problem while converting the ip banlist, retrying in a few seconds");
/* 631 */         waitForRetry();
/*     */       } 
/* 633 */       ipBanlistConverted = OldUsersConverter.convertIpBanlist(this);
/* 634 */       retries++;
/*     */     } 
/* 637 */     boolean opListConverted = false;
/* 638 */     retries = 0;
/* 639 */     while (!opListConverted && retries <= 2) {
/* 640 */       if (retries > 0) {
/* 641 */         LOGGER.warn("Encountered a problem while converting the op list, retrying in a few seconds");
/* 642 */         waitForRetry();
/*     */       } 
/* 644 */       opListConverted = OldUsersConverter.convertOpsList(this);
/* 645 */       retries++;
/*     */     } 
/* 648 */     boolean whitelistConverted = false;
/* 649 */     retries = 0;
/* 650 */     while (!whitelistConverted && retries <= 2) {
/* 651 */       if (retries > 0) {
/* 652 */         LOGGER.warn("Encountered a problem while converting the whitelist, retrying in a few seconds");
/* 653 */         waitForRetry();
/*     */       } 
/* 655 */       whitelistConverted = OldUsersConverter.convertWhiteList(this);
/* 656 */       retries++;
/*     */     } 
/* 659 */     boolean playersConverted = false;
/* 660 */     retries = 0;
/* 661 */     while (!playersConverted && retries <= 2) {
/* 662 */       if (retries > 0) {
/* 663 */         LOGGER.warn("Encountered a problem while converting the player save files, retrying in a few seconds");
/* 664 */         waitForRetry();
/*     */       } 
/* 666 */       playersConverted = OldUsersConverter.convertPlayers(this);
/* 667 */       retries++;
/*     */     } 
/* 670 */     return (userBanlistConverted || ipBanlistConverted || opListConverted || whitelistConverted || playersConverted);
/*     */   }
/*     */   
/*     */   private void waitForRetry() {
/*     */     try {
/* 675 */       Thread.sleep(5000L);
/* 676 */     } catch (InterruptedException ignored) {
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   public long getMaxTickLength() {
/* 682 */     return (getProperties()).maxTickTime;
/*     */   }
/*     */   
/*     */   public int getMaxChainedNeighborUpdates() {
/* 687 */     return (getProperties()).maxChainedNeighborUpdates;
/*     */   }
/*     */   
/*     */   public String getPluginNames() {
/* 692 */     return "";
/*     */   }
/*     */   
/*     */   public String runCommand(String command) {
/* 697 */     this.rconConsoleSource.prepareForCommand();
/* 698 */     executeBlocking(() -> getCommands().performPrefixedCommand(this.rconConsoleSource.createCommandSourceStack(), command));
/* 699 */     return this.rconConsoleSource.getCommandResponse();
/*     */   }
/*     */   
/*     */   protected void stopServer() {
/* 704 */     notificationManager().serverShuttingDown();
/* 705 */     super.stopServer();
/* 706 */     Util.shutdownExecutors();
/*     */   }
/*     */   
/*     */   public boolean isSingleplayerOwner(NameAndId nameAndId) {
/* 711 */     return false;
/*     */   }
/*     */   
/*     */   public int getScaledTrackingDistance(int range) {
/* 716 */     return entityBroadcastRangePercentage() * range / 100;
/*     */   }
/*     */   
/*     */   public int entityBroadcastRangePercentage() {
/* 720 */     return ((Integer)(getProperties()).entityBroadcastRangePercentage.get()).intValue();
/*     */   }
/*     */   
/*     */   public void setEntityBroadcastRangePercentage(int range) {
/* 724 */     this.settings.update(p -> (DedicatedServerProperties)p.entityBroadcastRangePercentage.update((RegistryAccess)registryAccess(), Integer.valueOf(range)));
/*     */   }
/*     */   
/*     */   public String getLevelIdName() {
/* 729 */     return this.storageSource.getLevelId();
/*     */   }
/*     */   
/*     */   public boolean forceSynchronousWrites() {
/* 734 */     return (this.settings.getProperties()).syncChunkWrites;
/*     */   }
/*     */   
/*     */   public TextFilter createTextFilterForPlayer(ServerPlayer player) {
/* 739 */     if (this.serverTextFilter != null)
/* 740 */       return this.serverTextFilter.createContext(player.getGameProfile()); 
/* 742 */     return TextFilter.DUMMY;
/*     */   }
/*     */   
/*     */   public GameType getForcedGameType() {
/* 747 */     return forceGameMode() ? this.worldData.getGameType() : null;
/*     */   }
/*     */   
/*     */   public boolean forceGameMode() {
/* 751 */     return ((Boolean)(this.settings.getProperties()).forceGameMode.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public void setForceGameMode(boolean forceGameMode) {
/* 755 */     this.settings.update(p -> (DedicatedServerProperties)p.forceGameMode.update((RegistryAccess)registryAccess(), Boolean.valueOf(forceGameMode)));
/* 756 */     enforceGameTypeForPlayers(getForcedGameType());
/*     */   }
/*     */   
/*     */   public GameType gameMode() {
/* 760 */     return (getProperties()).gameMode.get();
/*     */   }
/*     */   
/*     */   public void setGameMode(GameType gameMode) {
/* 764 */     this.settings.update(p -> (DedicatedServerProperties)p.gameMode.update((RegistryAccess)registryAccess(), gameMode));
/* 765 */     this.worldData.setGameType(gameMode());
/* 766 */     enforceGameTypeForPlayers(getForcedGameType());
/*     */   }
/*     */   
/*     */   public Optional<MinecraftServer.ServerResourcePackInfo> getServerResourcePack() {
/* 771 */     return (this.settings.getProperties()).serverResourcePackInfo;
/*     */   }
/*     */   
/*     */   protected void endMetricsRecordingTick() {
/* 776 */     super.endMetricsRecordingTick();
/* 778 */     this.isTickTimeLoggingEnabled = debugSubscribers().hasAnySubscriberFor(DebugSubscriptions.DEDICATED_SERVER_TICK_TIME);
/*     */   }
/*     */   
/*     */   protected SampleLogger getTickTimeLogger() {
/* 783 */     return (SampleLogger)this.tickTimeLogger;
/*     */   }
/*     */   
/*     */   public boolean isTickTimeLoggingEnabled() {
/* 788 */     return this.isTickTimeLoggingEnabled;
/*     */   }
/*     */   
/*     */   public boolean acceptsTransfers() {
/* 793 */     return ((Boolean)(this.settings.getProperties()).acceptsTransfers.get()).booleanValue();
/*     */   }
/*     */   
/*     */   public void setAcceptsTransfers(boolean acceptTransfers) {
/* 797 */     this.settings.update(p -> (DedicatedServerProperties)p.acceptsTransfers.update((RegistryAccess)registryAccess(), Boolean.valueOf(acceptTransfers)));
/*     */   }
/*     */   
/*     */   public ServerLinks serverLinks() {
/* 802 */     return this.serverLinks;
/*     */   }
/*     */   
/*     */   public int pauseWhenEmptySeconds() {
/* 807 */     return ((Integer)(this.settings.getProperties()).pauseWhenEmptySeconds.get()).intValue();
/*     */   }
/*     */   
/*     */   public void setPauseWhenEmptySeconds(int seconds) {
/* 811 */     this.settings.update(p -> (DedicatedServerProperties)p.pauseWhenEmptySeconds.update((RegistryAccess)registryAccess(), Integer.valueOf(seconds)));
/*     */   }
/*     */   
/*     */   private static ServerLinks createServerLinks(DedicatedServerSettings settings) {
/* 815 */     Optional<URI> bugReportLink = parseBugReportLink(settings.getProperties());
/* 816 */     return bugReportLink.<ServerLinks>map(bugLink -> new ServerLinks(List.of(ServerLinks.KnownLinkType.BUG_REPORT.create(bugLink)))).orElse(ServerLinks.EMPTY);
/*     */   }
/*     */   
/*     */   private static Optional<URI> parseBugReportLink(DedicatedServerProperties properties) {
/* 820 */     String bugReportLink = properties.bugReportLink;
/* 821 */     if (bugReportLink.isEmpty())
/* 822 */       return Optional.empty(); 
/*     */     try {
/* 825 */       return Optional.of(Util.parseAndValidateUntrustedUri(bugReportLink));
/* 826 */     } catch (Exception e) {
/* 827 */       LOGGER.warn("Failed to parse bug link {}", bugReportLink, e);
/* 828 */       return Optional.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map<String, String> getCodeOfConducts() {
/* 834 */     return this.codeOfConductTexts;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dedicated\DedicatedServer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */