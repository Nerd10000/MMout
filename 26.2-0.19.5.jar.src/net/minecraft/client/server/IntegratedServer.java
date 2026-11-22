/*     */ package net.minecraft.client.server;
/*     */ 
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.gizmos.GizmoCollector;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.gizmos.SimpleGizmoCollector;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.Services;
/*     */ import net.minecraft.server.WorldStem;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.server.level.progress.LevelLoadListener;
/*     */ import net.minecraft.server.notifications.NotificationManager;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.ModCheck;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*     */ import net.minecraft.util.debugchart.SampleLogger;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.thread.BlockableEventLoop;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class IntegratedServer extends MinecraftServer {
/*  55 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MIN_SIM_DISTANCE = 2;
/*     */   
/*     */   public static final int MAX_PLAYERS = 8;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private boolean paused = true;
/*     */   
/*  62 */   private int publishedPort = -1;
/*     */   
/*     */   private GameType gameTypeForOtherPlayers;
/*     */   
/*     */   private Boolean commandsAllowedForOtherPlayers;
/*     */   
/*     */   private LanServerPinger lanPinger;
/*     */   
/*     */   private UUID uuid;
/*     */   
/*  67 */   private int previousSimulationDistance = 0;
/*     */   
/*  68 */   private volatile List<SimpleGizmoCollector.GizmoInstance> latestTicksGizmos = new ArrayList<>();
/*     */   
/*  69 */   private final SimpleGizmoCollector gizmoCollector = new SimpleGizmoCollector();
/*     */   
/*  70 */   private MinecraftServer.MultiplayerScope multiplayerScope = MinecraftServer.MultiplayerScope.OFF;
/*     */   
/*     */   public IntegratedServer(Thread serverThread, Minecraft minecraft, LevelStorageSource.LevelStorageAccess levelStorageAccess, PackRepository packRepository, WorldStem worldStem, Optional<GameRules> gameRules, Services services, LevelLoadListener levelLoadListener) {
/*  73 */     super(serverThread, levelStorageAccess, packRepository, worldStem, gameRules, minecraft.getProxy(), minecraft.getFixerUpper(), services, levelLoadListener, false, new NotificationManager());
/*  75 */     setSingleplayerProfile(minecraft.getGameProfile());
/*  76 */     setDemo(minecraft.isDemo());
/*  77 */     setPlayerList(new IntegratedPlayerList(this, registries(), this.playerDataStorage));
/*  79 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   protected boolean initServer() {
/*  84 */     LOGGER.info("Starting integrated minecraft server version {}", SharedConstants.getCurrentVersion().name());
/*  86 */     setUsesAuthentication(true);
/*  88 */     initializeKeyPair();
/*  90 */     loadLevel();
/*  92 */     GameProfile host = getSingleplayerProfile();
/*  93 */     String levelName = getWorldData().getLevelName();
/*  94 */     setMotd((host != null) ? (host.name() + " - " + host.name()) : levelName);
/*  95 */     saveEverything(false, true, true);
/*  96 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPaused() {
/* 101 */     return this.paused;
/*     */   }
/*     */   
/*     */   protected void processPacketsAndTick(boolean sprinting) {
/* 106 */     Gizmos.TemporaryCollection ignored = Gizmos.withCollector((GizmoCollector)this.gizmoCollector);
/*     */     try {
/* 107 */       super.processPacketsAndTick(sprinting);
/* 108 */       if (ignored != null)
/* 108 */         ignored.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (ignored != null)
/*     */         try {
/*     */           ignored.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/* 109 */     if (tickRateManager().runsNormally())
/* 110 */       this.latestTicksGizmos = this.gizmoCollector.drainGizmos(); 
/*     */   }
/*     */   
/*     */   protected void tickServer(BooleanSupplier haveTime) {
/* 116 */     boolean wasPaused = this.paused;
/* 118 */     this.paused = (Minecraft.getInstance().isPaused() || getPlayerList().getPlayers().isEmpty());
/* 120 */     ProfilerFiller profiler = Profiler.get();
/* 121 */     if (!wasPaused && this.paused) {
/* 122 */       profiler.push("autoSave");
/* 123 */       LOGGER.info("Saving and pausing game...");
/* 124 */       saveEverything(false, false, false);
/* 125 */       profiler.pop();
/*     */     } 
/* 128 */     if (this.paused) {
/* 129 */       tickPaused();
/*     */       return;
/*     */     } 
/* 133 */     if (wasPaused)
/* 136 */       forceGameTimeSynchronization(); 
/* 139 */     super.tickServer(haveTime);
/* 141 */     int serverViewDistance = Math.max(2, ((Integer)this.minecraft.options.renderDistance().get()).intValue());
/* 142 */     if (serverViewDistance != getPlayerList().getViewDistance()) {
/* 143 */       LOGGER.info("Changing view distance to {}, from {}", Integer.valueOf(serverViewDistance), Integer.valueOf(getPlayerList().getViewDistance()));
/* 144 */       getPlayerList().setViewDistance(serverViewDistance);
/*     */     } 
/* 146 */     int serverSimulationDistance = Math.max(2, ((Integer)this.minecraft.options.simulationDistance().get()).intValue());
/* 147 */     if (serverSimulationDistance != this.previousSimulationDistance) {
/* 148 */       LOGGER.info("Changing simulation distance to {}, from {}", Integer.valueOf(serverSimulationDistance), Integer.valueOf(this.previousSimulationDistance));
/* 149 */       getPlayerList().setSimulationDistance(serverSimulationDistance);
/* 150 */       this.previousSimulationDistance = serverSimulationDistance;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected LocalSampleLogger getTickTimeLogger() {
/* 156 */     return this.minecraft.getDebugOverlay().getTickTimeLogger();
/*     */   }
/*     */   
/*     */   public boolean isTickTimeLoggingEnabled() {
/* 161 */     return true;
/*     */   }
/*     */   
/*     */   private void tickPaused() {
/* 165 */     tickConnection();
/* 166 */     for (ServerPlayer player : getPlayerList().getPlayers())
/* 167 */       player.awardStat(Stats.TOTAL_WORLD_TIME); 
/*     */   }
/*     */   
/*     */   public boolean shouldRconBroadcast() {
/* 173 */     return true;
/*     */   }
/*     */   
/*     */   public boolean shouldInformAdmins() {
/* 178 */     return true;
/*     */   }
/*     */   
/*     */   public Path getServerDirectory() {
/* 183 */     return this.minecraft.gameDirectory.toPath();
/*     */   }
/*     */   
/*     */   public boolean isDedicatedServer() {
/* 188 */     return false;
/*     */   }
/*     */   
/*     */   public int getRateLimitPacketsPerSecond() {
/* 193 */     return 0;
/*     */   }
/*     */   
/*     */   public int getCommandSpamThresholdSeconds() {
/* 198 */     return 0;
/*     */   }
/*     */   
/*     */   public int getChatSpamThresholdSeconds() {
/* 203 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean useNativeTransport() {
/* 208 */     return this.minecraft.options.useNativeTransport();
/*     */   }
/*     */   
/*     */   protected void onServerCrash(CrashReport report) {
/* 213 */     BlockableEventLoop.relayDelayCrash(report);
/*     */   }
/*     */   
/*     */   public SystemReport fillServerSystemReport(SystemReport systemReport) {
/* 218 */     systemReport.setDetail("Type", "Integrated Server");
/* 219 */     systemReport.setDetail("Is Modded", () -> getModdedStatus().fullDescription());
/* 220 */     Objects.requireNonNull(this.minecraft);
/* 220 */     systemReport.setDetail("Launched Version", this.minecraft::getLaunchedVersion);
/* 222 */     return systemReport;
/*     */   }
/*     */   
/*     */   public ModCheck getModdedStatus() {
/* 227 */     return Minecraft.checkModStatus().merge(super.getModdedStatus());
/*     */   }
/*     */   
/*     */   public boolean publishServer(MinecraftServer.MultiplayerScope scope, GameType gameMode, boolean allowCommands, int port) {
/* 232 */     if (gameMode != null)
/* 233 */       setGameTypeForOtherPlayers(gameMode); 
/* 235 */     setCommandsAllowedForOtherPlayers(allowCommands);
/* 236 */     return publishServer(scope, port);
/*     */   }
/*     */   
/*     */   public boolean publishServer(MinecraftServer.MultiplayerScope scope, int port) {
/* 240 */     if (scope == MinecraftServer.MultiplayerScope.OFF || isPublished())
/* 241 */       return false; 
/*     */     try {
/* 244 */       this.minecraft.prepareForMultiplayer();
/* 245 */       this.minecraft.getConnection().prepareKeyPair();
/* 247 */       getConnection().startTcpServerListener(null, port);
/* 248 */       LOGGER.info("Published LAN server on port {}", Integer.valueOf(port));
/* 249 */       this.publishedPort = port;
/* 250 */       this.lanPinger = new LanServerPinger(getMotd(), Integer.toString(port));
/* 251 */       this.lanPinger.start();
/* 253 */       setMultiplayerScope(scope);
/* 254 */       updateCommandsAllowedForOtherPlayers();
/* 256 */       return true;
/* 257 */     } catch (IOException iOException) {
/* 259 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setWorldGameType(GameType gameMode) {
/* 263 */     setDefaultGameType(gameMode);
/* 264 */     applyGameTypeToPlayers(gameMode, true);
/* 265 */     if (this.gameTypeForOtherPlayers == null)
/* 266 */       applyGameTypeToPlayers(gameMode, false); 
/*     */   }
/*     */   
/*     */   public GameType getGameTypeForOtherPlayers() {
/* 271 */     return (GameType)MoreObjects.firstNonNull(this.gameTypeForOtherPlayers, this.worldData.getGameType());
/*     */   }
/*     */   
/*     */   public void setGameTypeForOtherPlayers(GameType gameMode) {
/* 275 */     this.gameTypeForOtherPlayers = gameMode;
/* 276 */     applyGameTypeToPlayers(gameMode, false);
/*     */   }
/*     */   
/*     */   private void applyGameTypeToPlayers(GameType gameMode, boolean singleplayerOwner) {
/* 280 */     for (ServerPlayer player : getPlayerList().getPlayers()) {
/* 281 */       if (isSingleplayerOwner(player.nameAndId()) == singleplayerOwner)
/* 282 */         player.setGameMode(gameMode); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setWorldAllowCommands(boolean allowCommands) {
/* 288 */     getWorldData().setAllowCommands(allowCommands);
/* 289 */     updateCommandsAllowedForOtherPlayers();
/*     */   }
/*     */   
/*     */   public boolean commandsAllowedForOtherPlayers() {
/* 293 */     return ((Boolean)MoreObjects.firstNonNull(this.commandsAllowedForOtherPlayers, Boolean.valueOf(this.worldData.isAllowCommands()))).booleanValue();
/*     */   }
/*     */   
/*     */   public void setCommandsAllowedForOtherPlayers(boolean allowCommands) {
/* 297 */     this.commandsAllowedForOtherPlayers = Boolean.valueOf(allowCommands);
/* 298 */     updateCommandsAllowedForOtherPlayers();
/*     */   }
/*     */   
/*     */   private void updateCommandsAllowedForOtherPlayers() {
/* 302 */     getPlayerList().setAllowCommandsForAllPlayers(commandsAllowedForOtherPlayers());
/* 303 */     for (ServerPlayer serverPlayer : getPlayerList().getPlayers())
/* 304 */       getPlayerList().sendPlayerPermissionLevel(serverPlayer); 
/* 306 */     LocalPlayer player = this.minecraft.player;
/* 307 */     if (player != null)
/* 308 */       updatePermissionAndChatAbilities(player); 
/*     */   }
/*     */   
/*     */   private void updatePermissionAndChatAbilities(LocalPlayer player) {
/* 313 */     player.setPermissions((PermissionSet)getProfilePermissions(player.nameAndId()));
/* 314 */     player.refreshChatAbilities();
/*     */   }
/*     */   
/*     */   private void teardownPublishedState() {
/* 318 */     stopLanPinger();
/* 319 */     this.publishedPort = -1;
/* 320 */     setMultiplayerScope(MinecraftServer.MultiplayerScope.OFF);
/* 321 */     updateCommandsAllowedForOtherPlayers();
/*     */   }
/*     */   
/*     */   private void stopLanPinger() {
/* 325 */     if (this.lanPinger != null) {
/* 326 */       this.lanPinger.interrupt();
/* 327 */       this.lanPinger = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean unpublishServer() {
/* 333 */     if (!isPublished())
/* 334 */       return false; 
/* 337 */     if (this.multiplayerScope == MinecraftServer.MultiplayerScope.LAN)
/* 338 */       LOGGER.info("Unpublishing integrated server (was on port {})", Integer.valueOf(this.publishedPort)); 
/* 341 */     getConnection().stopTcpServerListener();
/* 343 */     MutableComponent mutableComponent = Component.translatable("multiplayer.disconnect.server_shutdown");
/* 344 */     for (ServerPlayer player : Lists.newArrayList(getPlayerList().getPlayers())) {
/* 345 */       if (!player.getUUID().equals(this.uuid))
/* 346 */         player.connection.disconnect((Component)mutableComponent); 
/*     */     } 
/* 349 */     getPlayerList().setAllowCommandsForAllPlayers(false);
/* 350 */     teardownPublishedState();
/* 351 */     return true;
/*     */   }
/*     */   
/*     */   public void stopServer() {
/* 356 */     teardownPublishedState();
/* 357 */     super.stopServer();
/*     */   }
/*     */   
/*     */   public void halt(boolean wait) {
/* 362 */     executeBlocking(() -> {
/*     */           List<ServerPlayer> players = Lists.newArrayList(getPlayerList().getPlayers());
/*     */           for (ServerPlayer player : players) {
/*     */             if (!player.getUUID().equals(this.uuid))
/*     */               getPlayerList().remove(player); 
/*     */           } 
/*     */         });
/* 372 */     super.halt(wait);
/* 373 */     stopLanPinger();
/*     */   }
/*     */   
/*     */   public boolean isPublished() {
/* 378 */     return (this.multiplayerScope != MinecraftServer.MultiplayerScope.OFF);
/*     */   }
/*     */   
/*     */   public int getPort() {
/* 383 */     return this.publishedPort;
/*     */   }
/*     */   
/*     */   public LevelBasedPermissionSet operatorUserPermissions() {
/* 388 */     return LevelBasedPermissionSet.GAMEMASTER;
/*     */   }
/*     */   
/*     */   public LevelBasedPermissionSet getFunctionCompilationPermissions() {
/* 393 */     return LevelBasedPermissionSet.GAMEMASTER;
/*     */   }
/*     */   
/*     */   public void setUUID(UUID uuid) {
/* 397 */     this.uuid = uuid;
/*     */   }
/*     */   
/*     */   public boolean isSingleplayerOwner(NameAndId nameAndId) {
/* 402 */     return (getSingleplayerProfile() != null && nameAndId.name().equalsIgnoreCase(getSingleplayerProfile().name()));
/*     */   }
/*     */   
/*     */   public int getScaledTrackingDistance(int baseRange) {
/* 407 */     return (int)(((Double)this.minecraft.options.entityDistanceScaling().get()).doubleValue() * baseRange);
/*     */   }
/*     */   
/*     */   public boolean forceSynchronousWrites() {
/* 412 */     return this.minecraft.options.syncWrites;
/*     */   }
/*     */   
/*     */   public GameType getForcedGameType() {
/* 417 */     if (isPublished() && !isHardcore())
/* 418 */       return getGameTypeForOtherPlayers(); 
/* 420 */     return null;
/*     */   }
/*     */   
/*     */   protected GlobalPos selectLevelLoadFocusPos() {
/* 425 */     UUID lastSinglePlayerOwnerUUID = this.worldData.getSinglePlayerUUID();
/* 426 */     if (lastSinglePlayerOwnerUUID == null)
/* 427 */       return super.selectLevelLoadFocusPos(); 
/* 429 */     Optional<CompoundTag> playerData = this.playerDataStorage.load(new NameAndId(lastSinglePlayerOwnerUUID, "<single player owner>"));
/* 430 */     if (playerData.isEmpty())
/* 431 */       return super.selectLevelLoadFocusPos(); 
/* 434 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(LOGGER);
/*     */     try {
/* 435 */       ValueInput input = TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)registryAccess(), playerData.get());
/* 436 */       ServerPlayer.SavedPosition loadedPosition = input.read(ServerPlayer.SavedPosition.MAP_CODEC).orElse(ServerPlayer.SavedPosition.EMPTY);
/* 437 */       if (loadedPosition.dimension().isPresent() && loadedPosition.position().isPresent()) {
/* 438 */         GlobalPos globalPos = new GlobalPos(loadedPosition.dimension().get(), BlockPos.containing(loadedPosition.position().get()));
/* 440 */         reporter.close();
/*     */         return globalPos;
/*     */       } 
/* 440 */       reporter.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/* 441 */     return super.selectLevelLoadFocusPos();
/*     */   }
/*     */   
/*     */   public void sendLowDiskSpaceWarning() {
/* 446 */     super.sendLowDiskSpaceWarning();
/* 447 */     this.minecraft.sendLowDiskSpaceWarning();
/*     */   }
/*     */   
/*     */   public void reportChunkLoadFailure(Throwable throwable, RegionStorageInfo storageInfo, ChunkPos pos) {
/* 452 */     super.reportChunkLoadFailure(throwable, storageInfo, pos);
/* 453 */     warnOnLowDiskSpace();
/* 454 */     this.minecraft.execute(() -> SystemToast.onChunkLoadFailure(this.minecraft, pos));
/*     */   }
/*     */   
/*     */   public void reportChunkSaveFailure(Throwable throwable, RegionStorageInfo storageInfo, ChunkPos pos) {
/* 459 */     super.reportChunkSaveFailure(throwable, storageInfo, pos);
/* 460 */     warnOnLowDiskSpace();
/* 461 */     this.minecraft.execute(() -> SystemToast.onChunkSaveFailure(this.minecraft, pos));
/*     */   }
/*     */   
/*     */   public int getMaxPlayers() {
/* 466 */     return 8;
/*     */   }
/*     */   
/*     */   public Collection<SimpleGizmoCollector.GizmoInstance> getPerTickGizmos() {
/* 470 */     return this.latestTicksGizmos;
/*     */   }
/*     */   
/*     */   private void setMultiplayerScope(MinecraftServer.MultiplayerScope multiplayerScope) {
/* 474 */     if (this.multiplayerScope == multiplayerScope)
/*     */       return; 
/* 477 */     this.multiplayerScope = multiplayerScope;
/*     */   }
/*     */   
/*     */   public MinecraftServer.MultiplayerScope getMultiplayerScope() {
/* 481 */     return this.multiplayerScope;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\server\IntegratedServer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */