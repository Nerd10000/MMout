/*     */ package net.minecraft.server.jsonrpc.internalapi;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.server.dedicated.DedicatedServer;
/*     */ import net.minecraft.server.jsonrpc.JsonRpcLogger;
/*     */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*     */ import net.minecraft.server.notifications.NotificationManager;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class MinecraftServerSettingsServiceImpl implements MinecraftServerSettingsService {
/*     */   private final NotificationManager notificationManager;
/*     */   
/*     */   private final JsonRpcLogger jsonrpcLogger;
/*     */   
/*     */   public MinecraftServerSettingsServiceImpl(NotificationManager notificationManager, JsonRpcLogger jsonrpcLogger) {
/*  19 */     this.notificationManager = notificationManager;
/*  20 */     this.jsonrpcLogger = jsonrpcLogger;
/*     */   }
/*     */   
/*     */   private DedicatedServer server() {
/*  24 */     return Objects.<DedicatedServer>requireNonNull(this.notificationManager.server());
/*     */   }
/*     */   
/*     */   public boolean isAutoSave() {
/*  29 */     return server().isAutoSave();
/*     */   }
/*     */   
/*     */   public boolean setAutoSave(boolean enabled, ClientInfo clientInfo) {
/*  34 */     this.jsonrpcLogger.log(clientInfo, "Update autosave from {} to {}", new Object[] { Boolean.valueOf(isAutoSave()), Boolean.valueOf(enabled) });
/*  35 */     server().setAutoSave(enabled);
/*  36 */     return isAutoSave();
/*     */   }
/*     */   
/*     */   public Difficulty getDifficulty() {
/*  41 */     return server().getWorldData().getDifficulty();
/*     */   }
/*     */   
/*     */   public Difficulty setDifficulty(Difficulty difficulty, ClientInfo clientInfo) {
/*  46 */     this.jsonrpcLogger.log(clientInfo, "Update difficulty from '{}' to '{}'", new Object[] { getDifficulty(), difficulty });
/*  47 */     server().setDifficulty(difficulty);
/*  48 */     return getDifficulty();
/*     */   }
/*     */   
/*     */   public boolean isEnforceWhitelist() {
/*  53 */     return server().isEnforceWhitelist();
/*     */   }
/*     */   
/*     */   public boolean setEnforceWhitelist(boolean enforce, ClientInfo clientInfo) {
/*  58 */     this.jsonrpcLogger.log(clientInfo, "Update enforce allowlist from {} to {}", new Object[] { Boolean.valueOf(isEnforceWhitelist()), Boolean.valueOf(enforce) });
/*  59 */     server().setEnforceWhitelist(enforce);
/*  60 */     server().kickUnlistedPlayers();
/*  61 */     return isEnforceWhitelist();
/*     */   }
/*     */   
/*     */   public boolean isUsingWhitelist() {
/*  66 */     return server().isUsingWhitelist();
/*     */   }
/*     */   
/*     */   public boolean setUsingWhitelist(boolean use, ClientInfo clientInfo) {
/*  71 */     this.jsonrpcLogger.log(clientInfo, "Update using allowlist from {} to {}", new Object[] { Boolean.valueOf(isUsingWhitelist()), Boolean.valueOf(use) });
/*  72 */     server().setUsingWhitelist(use);
/*  73 */     server().kickUnlistedPlayers();
/*  74 */     return isUsingWhitelist();
/*     */   }
/*     */   
/*     */   public int getMaxPlayers() {
/*  79 */     return server().getMaxPlayers();
/*     */   }
/*     */   
/*     */   public int setMaxPlayers(int maxPlayers, ClientInfo clientInfo) {
/*  84 */     this.jsonrpcLogger.log(clientInfo, "Update max players from {} to {}", new Object[] { Integer.valueOf(getMaxPlayers()), Integer.valueOf(maxPlayers) });
/*  85 */     server().setMaxPlayers(maxPlayers);
/*  86 */     return getMaxPlayers();
/*     */   }
/*     */   
/*     */   public int getPauseWhenEmptySeconds() {
/*  91 */     return server().pauseWhenEmptySeconds();
/*     */   }
/*     */   
/*     */   public int setPauseWhenEmptySeconds(int emptySeconds, ClientInfo clientInfo) {
/*  96 */     this.jsonrpcLogger.log(clientInfo, "Update pause when empty from {} seconds to {} seconds", new Object[] { Integer.valueOf(getPauseWhenEmptySeconds()), Integer.valueOf(emptySeconds) });
/*  97 */     server().setPauseWhenEmptySeconds(emptySeconds);
/*  98 */     return getPauseWhenEmptySeconds();
/*     */   }
/*     */   
/*     */   public int getPlayerIdleTimeout() {
/* 103 */     return server().playerIdleTimeout();
/*     */   }
/*     */   
/*     */   public int setPlayerIdleTimeout(int idleTime, ClientInfo clientInfo) {
/* 108 */     this.jsonrpcLogger.log(clientInfo, "Update player idle timeout from {} minutes to {} minutes", new Object[] { Integer.valueOf(getPlayerIdleTimeout()), Integer.valueOf(idleTime) });
/* 109 */     server().setPlayerIdleTimeout(idleTime);
/* 110 */     return getPlayerIdleTimeout();
/*     */   }
/*     */   
/*     */   public boolean allowFlight() {
/* 115 */     return server().allowFlight();
/*     */   }
/*     */   
/*     */   public boolean setAllowFlight(boolean allow, ClientInfo clientInfo) {
/* 120 */     this.jsonrpcLogger.log(clientInfo, "Update allow flight from {} to {}", new Object[] { Boolean.valueOf(allowFlight()), Boolean.valueOf(allow) });
/* 121 */     server().setAllowFlight(allow);
/* 122 */     return allowFlight();
/*     */   }
/*     */   
/*     */   public int getSpawnProtectionRadius() {
/* 127 */     return server().spawnProtectionRadius();
/*     */   }
/*     */   
/*     */   public int setSpawnProtectionRadius(int spawnProtection, ClientInfo clientInfo) {
/* 132 */     this.jsonrpcLogger.log(clientInfo, "Update spawn protection radius from {} to {}", new Object[] { Integer.valueOf(getSpawnProtectionRadius()), Integer.valueOf(spawnProtection) });
/* 133 */     server().setSpawnProtectionRadius(spawnProtection);
/* 134 */     return getSpawnProtectionRadius();
/*     */   }
/*     */   
/*     */   public String getMotd() {
/* 139 */     return server().getMotd();
/*     */   }
/*     */   
/*     */   public String setMotd(String motd, ClientInfo clientInfo) {
/* 144 */     this.jsonrpcLogger.log(clientInfo, "Update MOTD from '{}' to '{}'", new Object[] { getMotd(), motd });
/* 145 */     server().setMotd(motd);
/* 146 */     return getMotd();
/*     */   }
/*     */   
/*     */   public boolean forceGameMode() {
/* 151 */     return server().forceGameMode();
/*     */   }
/*     */   
/*     */   public boolean setForceGameMode(boolean force, ClientInfo clientInfo) {
/* 156 */     this.jsonrpcLogger.log(clientInfo, "Update force game mode from {} to {}", new Object[] { Boolean.valueOf(forceGameMode()), Boolean.valueOf(force) });
/* 157 */     server().setForceGameMode(force);
/* 158 */     return forceGameMode();
/*     */   }
/*     */   
/*     */   public GameType getGameMode() {
/* 163 */     return server().gameMode();
/*     */   }
/*     */   
/*     */   public GameType setGameMode(GameType gameMode, ClientInfo clientInfo) {
/* 168 */     this.jsonrpcLogger.log(clientInfo, "Update game mode from '{}' to '{}'", new Object[] { getGameMode(), gameMode });
/* 169 */     server().setGameMode(gameMode);
/* 170 */     return getGameMode();
/*     */   }
/*     */   
/*     */   public int getViewDistance() {
/* 175 */     return server().viewDistance();
/*     */   }
/*     */   
/*     */   public int setViewDistance(int viewDistance, ClientInfo clientInfo) {
/* 180 */     this.jsonrpcLogger.log(clientInfo, "Update view distance from {} to {}", new Object[] { Integer.valueOf(getViewDistance()), Integer.valueOf(viewDistance) });
/* 181 */     server().setViewDistance(viewDistance);
/* 182 */     return getViewDistance();
/*     */   }
/*     */   
/*     */   public int getSimulationDistance() {
/* 187 */     return server().simulationDistance();
/*     */   }
/*     */   
/*     */   public int setSimulationDistance(int simulationDistance, ClientInfo clientInfo) {
/* 192 */     this.jsonrpcLogger.log(clientInfo, "Update simulation distance from {} to {}", new Object[] { Integer.valueOf(getSimulationDistance()), Integer.valueOf(simulationDistance) });
/* 193 */     server().setSimulationDistance(simulationDistance);
/* 194 */     return getSimulationDistance();
/*     */   }
/*     */   
/*     */   public boolean acceptsTransfers() {
/* 199 */     return server().acceptsTransfers();
/*     */   }
/*     */   
/*     */   public boolean setAcceptsTransfers(boolean accept, ClientInfo clientInfo) {
/* 204 */     this.jsonrpcLogger.log(clientInfo, "Update accepts transfers from {} to {}", new Object[] { Boolean.valueOf(acceptsTransfers()), Boolean.valueOf(accept) });
/* 205 */     server().setAcceptsTransfers(accept);
/* 206 */     return acceptsTransfers();
/*     */   }
/*     */   
/*     */   public int getStatusHeartbeatInterval() {
/* 211 */     return server().statusHeartbeatInterval();
/*     */   }
/*     */   
/*     */   public int setStatusHeartbeatInterval(int statusHeartbeatInterval, ClientInfo clientInfo) {
/* 216 */     if (server().setStatusHeartbeatInterval(statusHeartbeatInterval)) {
/* 217 */       this.jsonrpcLogger.log(clientInfo, "Updated status heartbeat interval from {} to {}", new Object[] { Integer.valueOf(getStatusHeartbeatInterval()), Integer.valueOf(statusHeartbeatInterval) });
/*     */     } else {
/* 219 */       this.jsonrpcLogger.log(clientInfo, "The heartbeat interval was not updated to the new value of {} seconds as the json rpc server is null or a pre-existing heartbeat failed to get canceled", new Object[] { Integer.valueOf(statusHeartbeatInterval) });
/*     */     } 
/* 221 */     return getStatusHeartbeatInterval();
/*     */   }
/*     */   
/*     */   public LevelBasedPermissionSet getOperatorUserPermissions() {
/* 226 */     return server().operatorUserPermissions();
/*     */   }
/*     */   
/*     */   public LevelBasedPermissionSet setOperatorUserPermissions(LevelBasedPermissionSet permissions, ClientInfo clientInfo) {
/* 231 */     this.jsonrpcLogger.log(clientInfo, "Update operator user permission level from {} to {}", new Object[] { getOperatorUserPermissions(), permissions.level() });
/* 232 */     server().setOperatorUserPermissions(permissions);
/* 233 */     return getOperatorUserPermissions();
/*     */   }
/*     */   
/*     */   public boolean hidesOnlinePlayers() {
/* 238 */     return server().hidesOnlinePlayers();
/*     */   }
/*     */   
/*     */   public boolean setHidesOnlinePlayers(boolean hide, ClientInfo clientInfo) {
/* 243 */     this.jsonrpcLogger.log(clientInfo, "Update hides online players from {} to {}", new Object[] { Boolean.valueOf(hidesOnlinePlayers()), Boolean.valueOf(hide) });
/* 244 */     server().setHidesOnlinePlayers(hide);
/* 245 */     return hidesOnlinePlayers();
/*     */   }
/*     */   
/*     */   public boolean repliesToStatus() {
/* 250 */     return server().repliesToStatus();
/*     */   }
/*     */   
/*     */   public boolean setRepliesToStatus(boolean enable, ClientInfo clientInfo) {
/* 255 */     this.jsonrpcLogger.log(clientInfo, "Update replies to status from {} to {}", new Object[] { Boolean.valueOf(repliesToStatus()), Boolean.valueOf(enable) });
/* 256 */     server().setRepliesToStatus(enable);
/* 257 */     return repliesToStatus();
/*     */   }
/*     */   
/*     */   public int getEntityBroadcastRangePercentage() {
/* 262 */     return server().entityBroadcastRangePercentage();
/*     */   }
/*     */   
/*     */   public int setEntityBroadcastRangePercentage(int percentage, ClientInfo clientInfo) {
/* 267 */     this.jsonrpcLogger.log(clientInfo, "Update entity broadcast range percentage from {}% to {}%", new Object[] { Integer.valueOf(getEntityBroadcastRangePercentage()), Integer.valueOf(percentage) });
/* 268 */     server().setEntityBroadcastRangePercentage(percentage);
/* 269 */     return getEntityBroadcastRangePercentage();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftServerSettingsServiceImpl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */