/*     */ package net.minecraft.server.jsonrpc;
/*     */ 
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.server.jsonrpc.api.Schema;
/*     */ import net.minecraft.server.jsonrpc.internalapi.MinecraftApi;
/*     */ import net.minecraft.server.jsonrpc.methods.AllowlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.BanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.DiscoveryService;
/*     */ import net.minecraft.server.jsonrpc.methods.GameRulesService;
/*     */ import net.minecraft.server.jsonrpc.methods.IpBanlistService;
/*     */ import net.minecraft.server.jsonrpc.methods.OperatorService;
/*     */ import net.minecraft.server.jsonrpc.methods.PlayerService;
/*     */ import net.minecraft.server.jsonrpc.methods.ServerSettingsService;
/*     */ import net.minecraft.server.jsonrpc.methods.ServerStateService;
/*     */ 
/*     */ public class IncomingRpcMethods {
/*     */   public static IncomingRpcMethod<?, ?> bootstrap(Registry<IncomingRpcMethod<?, ?>> methodRegistry) {
/*  19 */     registerAllowListService(methodRegistry);
/*  21 */     registerBanlistService(methodRegistry);
/*  23 */     registerIpBanlistService(methodRegistry);
/*  25 */     registerPlayerService(methodRegistry);
/*  27 */     registerOperatorService(methodRegistry);
/*  29 */     registerServerStateService(methodRegistry);
/*  31 */     registerServerSettingsService(methodRegistry);
/*  33 */     registerGameRuleService(methodRegistry);
/*  35 */     return IncomingRpcMethod.method(apiService -> DiscoveryService.discover(Schema.getSchemaRegistry()))
/*  36 */       .undiscoverable()
/*  37 */       .allowPreServerInit()
/*  38 */       .notOnMainThread()
/*  39 */       .response("result", Schema.DISCOVERY_SCHEMA)
/*  40 */       .register(methodRegistry, "rpc.discover");
/*     */   }
/*     */   
/*     */   private static void registerAllowListService(Registry<IncomingRpcMethod<?, ?>> methodRegistry) {
/*  44 */     IncomingRpcMethod.method(AllowlistService::get)
/*  45 */       .description("Get the allowlist")
/*  46 */       .response("allowlist", Schema.PLAYER_SCHEMA.asArray())
/*  47 */       .register(methodRegistry, "allowlist");
/*  49 */     IncomingRpcMethod.method(AllowlistService::set)
/*  50 */       .description("Set the allowlist")
/*  51 */       .param("players", Schema.PLAYER_SCHEMA.asArray())
/*  52 */       .response("allowlist", Schema.PLAYER_SCHEMA.asArray())
/*  53 */       .register(methodRegistry, "allowlist/set");
/*  55 */     IncomingRpcMethod.method(AllowlistService::add)
/*  56 */       .description("Add players to allowlist")
/*  57 */       .param("add", Schema.PLAYER_SCHEMA.asArray())
/*  58 */       .response("allowlist", Schema.PLAYER_SCHEMA.asArray())
/*  59 */       .register(methodRegistry, "allowlist/add");
/*  61 */     IncomingRpcMethod.method(AllowlistService::remove)
/*  62 */       .description("Remove players from allowlist")
/*  63 */       .param("remove", Schema.PLAYER_SCHEMA.asArray())
/*  64 */       .response("allowlist", Schema.PLAYER_SCHEMA.asArray())
/*  65 */       .register(methodRegistry, "allowlist/remove");
/*  67 */     IncomingRpcMethod.method(AllowlistService::clear)
/*  68 */       .description("Clear all players in allowlist")
/*  69 */       .response("allowlist", Schema.PLAYER_SCHEMA.asArray())
/*  70 */       .register(methodRegistry, "allowlist/clear");
/*     */   }
/*     */   
/*     */   private static void registerBanlistService(Registry<IncomingRpcMethod<?, ?>> methodRegistry) {
/*  74 */     IncomingRpcMethod.method(BanlistService::get)
/*  75 */       .description("Get the ban list")
/*  76 */       .response("banlist", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  77 */       .register(methodRegistry, "bans");
/*  79 */     IncomingRpcMethod.method(BanlistService::set)
/*  80 */       .description("Set the banlist")
/*  81 */       .param("bans", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  82 */       .response("banlist", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  83 */       .register(methodRegistry, "bans/set");
/*  85 */     IncomingRpcMethod.method(BanlistService::add)
/*  86 */       .description("Add players to ban list")
/*  87 */       .param("add", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  88 */       .response("banlist", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  89 */       .register(methodRegistry, "bans/add");
/*  91 */     IncomingRpcMethod.method(BanlistService::remove)
/*  92 */       .description("Remove players from ban list")
/*  93 */       .param("remove", Schema.PLAYER_SCHEMA.asArray())
/*  94 */       .response("banlist", Schema.PLAYER_BAN_SCHEMA.asArray())
/*  95 */       .register(methodRegistry, "bans/remove");
/*  97 */     IncomingRpcMethod.method(BanlistService::clear)
/*  98 */       .description("Clear all players in ban list")
/*  99 */       .response("banlist", Schema.PLAYER_BAN_SCHEMA.asArray())
/* 100 */       .register(methodRegistry, "bans/clear");
/*     */   }
/*     */   
/*     */   private static void registerIpBanlistService(Registry<IncomingRpcMethod<?, ?>> methodRegistry) {
/* 104 */     IncomingRpcMethod.method(IpBanlistService::get)
/* 105 */       .description("Get the ip ban list")
/* 106 */       .response("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 107 */       .register(methodRegistry, "ip_bans");
/* 109 */     IncomingRpcMethod.method(IpBanlistService::set)
/* 110 */       .description("Set the ip banlist")
/* 111 */       .param("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 112 */       .response("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 113 */       .register(methodRegistry, "ip_bans/set");
/* 115 */     IncomingRpcMethod.method(IpBanlistService::add)
/* 116 */       .description("Add ip to ban list")
/* 117 */       .param("add", Schema.INCOMING_IP_BAN_SCHEMA.asArray())
/* 118 */       .response("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 119 */       .register(methodRegistry, "ip_bans/add");
/* 121 */     IncomingRpcMethod.method(IpBanlistService::remove)
/* 122 */       .description("Remove ip from ban list")
/* 123 */       .param("ip", Schema.STRING_SCHEMA.asArray())
/* 124 */       .response("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 125 */       .register(methodRegistry, "ip_bans/remove");
/* 127 */     IncomingRpcMethod.method(IpBanlistService::clear)
/* 128 */       .description("Clear all ips in ban list")
/* 129 */       .response("banlist", Schema.IP_BAN_SCHEMA.asArray())
/* 130 */       .register(methodRegistry, "ip_bans/clear");
/*     */   }
/*     */   
/*     */   private static void registerPlayerService(Registry<IncomingRpcMethod<?, ?>> methodRegistry) {
/* 134 */     IncomingRpcMethod.method(PlayerService::get)
/* 135 */       .description("Get all connected players")
/* 136 */       .response("players", Schema.PLAYER_SCHEMA.asArray())
/* 137 */       .register(methodRegistry, "players");
/* 139 */     IncomingRpcMethod.method(PlayerService::kick)
/* 140 */       .description("Kick players")
/* 141 */       .param("kick", Schema.KICK_PLAYER_SCHEMA.asArray())
/* 142 */       .response("kicked", Schema.PLAYER_SCHEMA.asArray())
/* 143 */       .register(methodRegistry, "players/kick");
/*     */   }
/*     */   
/*     */   private static void registerOperatorService(Registry<IncomingRpcMethod<?, ?>> methodRegistry) {
/* 147 */     IncomingRpcMethod.method(OperatorService::get)
/* 148 */       .description("Get all oped players")
/* 149 */       .response("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 150 */       .register(methodRegistry, "operators");
/* 152 */     IncomingRpcMethod.method(OperatorService::set)
/* 153 */       .description("Set all oped players")
/* 154 */       .param("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 155 */       .response("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 156 */       .register(methodRegistry, "operators/set");
/* 158 */     IncomingRpcMethod.method(OperatorService::add)
/* 159 */       .description("Op players")
/* 160 */       .param("add", Schema.OPERATOR_SCHEMA.asArray())
/* 161 */       .response("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 162 */       .register(methodRegistry, "operators/add");
/* 164 */     IncomingRpcMethod.method(OperatorService::remove)
/* 165 */       .description("Deop players")
/* 166 */       .param("remove", Schema.PLAYER_SCHEMA.asArray())
/* 167 */       .response("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 168 */       .register(methodRegistry, "operators/remove");
/* 170 */     IncomingRpcMethod.method(OperatorService::clear)
/* 171 */       .description("Deop all players")
/* 172 */       .response("operators", Schema.OPERATOR_SCHEMA.asArray())
/* 173 */       .register(methodRegistry, "operators/clear");
/*     */   }
/*     */   
/*     */   private static void registerServerStateService(Registry<IncomingRpcMethod<?, ?>> methodRegistry) {
/* 177 */     IncomingRpcMethod.method(ServerStateService::status)
/* 178 */       .description("Get server status")
/* 179 */       .response("status", Schema.SERVER_STATE_SCHEMA.asRef())
/* 180 */       .register(methodRegistry, "server/status");
/* 182 */     IncomingRpcMethod.method(ServerStateService::save)
/* 183 */       .description("Save server state")
/* 184 */       .param("flush", Schema.BOOL_SCHEMA)
/* 185 */       .response("saving", Schema.BOOL_SCHEMA)
/* 186 */       .register(methodRegistry, "server/save");
/* 188 */     IncomingRpcMethod.method(ServerStateService::stop)
/* 189 */       .description("Stop server")
/* 190 */       .response("stopping", Schema.BOOL_SCHEMA)
/* 191 */       .register(methodRegistry, "server/stop");
/* 193 */     IncomingRpcMethod.method(ServerStateService::systemMessage)
/* 194 */       .description("Send a system message")
/* 195 */       .param("message", Schema.SYSTEM_MESSAGE_SCHEMA.asRef())
/* 196 */       .response("sent", Schema.BOOL_SCHEMA)
/* 197 */       .register(methodRegistry, "server/system_message");
/*     */   }
/*     */   
/*     */   private static void registerServerSettingsService(Registry<IncomingRpcMethod<?, ?>> methodRegistry) {
/* 201 */     IncomingRpcMethod.method(ServerSettingsService::autosave)
/* 202 */       .description("Get whether automatic world saving is enabled on the server")
/* 203 */       .response("enabled", Schema.BOOL_SCHEMA)
/* 204 */       .register(methodRegistry, "serversettings/autosave");
/* 206 */     IncomingRpcMethod.method(ServerSettingsService::setAutosave)
/* 207 */       .description("Enable or disable automatic world saving on the server")
/* 208 */       .param("enable", Schema.BOOL_SCHEMA)
/* 209 */       .response("enabled", Schema.BOOL_SCHEMA)
/* 210 */       .register(methodRegistry, "serversettings/autosave/set");
/* 212 */     IncomingRpcMethod.method(ServerSettingsService::difficulty)
/* 213 */       .description("Get the current difficulty level of the server")
/* 214 */       .response("difficulty", Schema.DIFFICULTY_SCHEMA.asRef())
/* 215 */       .register(methodRegistry, "serversettings/difficulty");
/* 217 */     IncomingRpcMethod.method(ServerSettingsService::setDifficulty)
/* 218 */       .description("Set the difficulty level of the server")
/* 219 */       .param("difficulty", Schema.DIFFICULTY_SCHEMA.asRef())
/* 220 */       .response("difficulty", Schema.DIFFICULTY_SCHEMA.asRef())
/* 221 */       .register(methodRegistry, "serversettings/difficulty/set");
/* 223 */     IncomingRpcMethod.method(ServerSettingsService::enforceAllowlist)
/* 224 */       .description("Get whether allowlist enforcement is enabled (kicks players immediately when removed from allowlist)")
/* 225 */       .response("enforced", Schema.BOOL_SCHEMA)
/* 226 */       .register(methodRegistry, "serversettings/enforce_allowlist");
/* 228 */     IncomingRpcMethod.method(ServerSettingsService::setEnforceAllowlist)
/* 229 */       .description("Enable or disable allowlist enforcement (when enabled, players are kicked immediately upon removal from allowlist)")
/* 230 */       .param("enforce", Schema.BOOL_SCHEMA)
/* 231 */       .response("enforced", Schema.BOOL_SCHEMA)
/* 232 */       .register(methodRegistry, "serversettings/enforce_allowlist/set");
/* 234 */     IncomingRpcMethod.method(ServerSettingsService::usingAllowlist)
/* 235 */       .description("Get whether the allowlist is enabled on the server")
/* 236 */       .response("used", Schema.BOOL_SCHEMA)
/* 237 */       .register(methodRegistry, "serversettings/use_allowlist");
/* 239 */     IncomingRpcMethod.method(ServerSettingsService::setUsingAllowlist)
/* 240 */       .description("Enable or disable the allowlist on the server (controls whether only allowlisted players can join)")
/* 241 */       .param("use", Schema.BOOL_SCHEMA)
/* 242 */       .response("used", Schema.BOOL_SCHEMA)
/* 243 */       .register(methodRegistry, "serversettings/use_allowlist/set");
/* 245 */     IncomingRpcMethod.method(ServerSettingsService::maxPlayers)
/* 246 */       .description("Get the maximum number of players allowed to connect to the server")
/* 247 */       .response("max", Schema.INT_SCHEMA)
/* 248 */       .register(methodRegistry, "serversettings/max_players");
/* 250 */     IncomingRpcMethod.method(ServerSettingsService::setMaxPlayers)
/* 251 */       .description("Set the maximum number of players allowed to connect to the server")
/* 252 */       .param("max", Schema.INT_SCHEMA)
/* 253 */       .response("max", Schema.INT_SCHEMA)
/* 254 */       .register(methodRegistry, "serversettings/max_players/set");
/* 256 */     IncomingRpcMethod.method(ServerSettingsService::pauseWhenEmpty)
/* 257 */       .description("Get the number of seconds before the game is automatically paused when no players are online")
/* 258 */       .response("seconds", Schema.INT_SCHEMA)
/* 259 */       .register(methodRegistry, "serversettings/pause_when_empty_seconds");
/* 261 */     IncomingRpcMethod.method(ServerSettingsService::setPauseWhenEmpty)
/* 262 */       .description("Set the number of seconds before the game is automatically paused when no players are online")
/* 263 */       .param("seconds", Schema.INT_SCHEMA)
/* 264 */       .response("seconds", Schema.INT_SCHEMA)
/* 265 */       .register(methodRegistry, "serversettings/pause_when_empty_seconds/set");
/* 267 */     IncomingRpcMethod.method(ServerSettingsService::playerIdleTimeout)
/* 268 */       .description("Get the number of seconds before idle players are automatically kicked from the server")
/* 269 */       .response("seconds", Schema.INT_SCHEMA)
/* 270 */       .register(methodRegistry, "serversettings/player_idle_timeout");
/* 272 */     IncomingRpcMethod.method(ServerSettingsService::setPlayerIdleTimeout)
/* 273 */       .description("Set the number of seconds before idle players are automatically kicked from the server")
/* 274 */       .param("seconds", Schema.INT_SCHEMA)
/* 275 */       .response("seconds", Schema.INT_SCHEMA)
/* 276 */       .register(methodRegistry, "serversettings/player_idle_timeout/set");
/* 278 */     IncomingRpcMethod.method(ServerSettingsService::allowFlight)
/* 279 */       .description("Get whether flight is allowed for players in Survival mode")
/* 280 */       .response("allowed", Schema.BOOL_SCHEMA)
/* 281 */       .register(methodRegistry, "serversettings/allow_flight");
/* 283 */     IncomingRpcMethod.method(ServerSettingsService::setAllowFlight)
/* 284 */       .description("Allow or disallow flight for players in Survival mode")
/* 285 */       .param("allow", Schema.BOOL_SCHEMA)
/* 286 */       .response("allowed", Schema.BOOL_SCHEMA)
/* 287 */       .register(methodRegistry, "serversettings/allow_flight/set");
/* 289 */     IncomingRpcMethod.method(ServerSettingsService::motd)
/* 290 */       .description("Get the server's message of the day displayed to players")
/* 291 */       .response("message", Schema.STRING_SCHEMA)
/* 292 */       .register(methodRegistry, "serversettings/motd");
/* 294 */     IncomingRpcMethod.method(ServerSettingsService::setMotd)
/* 295 */       .description("Set the server's message of the day displayed to players")
/* 296 */       .param("message", Schema.STRING_SCHEMA)
/* 297 */       .response("message", Schema.STRING_SCHEMA)
/* 298 */       .register(methodRegistry, "serversettings/motd/set");
/* 300 */     IncomingRpcMethod.method(ServerSettingsService::spawnProtection)
/* 301 */       .description("Get the spawn protection radius in blocks (only operators can edit within this area)")
/* 302 */       .response("radius", Schema.INT_SCHEMA)
/* 303 */       .register(methodRegistry, "serversettings/spawn_protection_radius");
/* 305 */     IncomingRpcMethod.method(ServerSettingsService::setSpawnProtection)
/* 306 */       .description("Set the spawn protection radius in blocks (only operators can edit within this area)")
/* 307 */       .param("radius", Schema.INT_SCHEMA)
/* 308 */       .response("radius", Schema.INT_SCHEMA)
/* 309 */       .register(methodRegistry, "serversettings/spawn_protection_radius/set");
/* 311 */     IncomingRpcMethod.method(ServerSettingsService::forceGameMode)
/* 312 */       .description("Get whether players are forced to use the server's default game mode")
/* 313 */       .response("forced", Schema.BOOL_SCHEMA)
/* 314 */       .register(methodRegistry, "serversettings/force_game_mode");
/* 316 */     IncomingRpcMethod.method(ServerSettingsService::setForceGameMode)
/* 317 */       .description("Enable or disable forcing players to use the server's default game mode")
/* 318 */       .param("force", Schema.BOOL_SCHEMA)
/* 319 */       .response("forced", Schema.BOOL_SCHEMA)
/* 320 */       .register(methodRegistry, "serversettings/force_game_mode/set");
/* 322 */     IncomingRpcMethod.method(ServerSettingsService::gameMode)
/* 323 */       .description("Get the server's default game mode")
/* 324 */       .response("mode", Schema.GAME_TYPE_SCHEMA.asRef())
/* 325 */       .register(methodRegistry, "serversettings/game_mode");
/* 327 */     IncomingRpcMethod.method(ServerSettingsService::setGameMode)
/* 328 */       .description("Set the server's default game mode")
/* 329 */       .param("mode", Schema.GAME_TYPE_SCHEMA.asRef())
/* 330 */       .response("mode", Schema.GAME_TYPE_SCHEMA.asRef())
/* 331 */       .register(methodRegistry, "serversettings/game_mode/set");
/* 333 */     IncomingRpcMethod.method(ServerSettingsService::viewDistance)
/* 334 */       .description("Get the server's view distance in chunks")
/* 335 */       .response("distance", Schema.INT_SCHEMA)
/* 336 */       .register(methodRegistry, "serversettings/view_distance");
/* 338 */     IncomingRpcMethod.method(ServerSettingsService::setViewDistance)
/* 339 */       .description("Set the server's view distance in chunks")
/* 340 */       .param("distance", Schema.INT_SCHEMA)
/* 341 */       .response("distance", Schema.INT_SCHEMA)
/* 342 */       .register(methodRegistry, "serversettings/view_distance/set");
/* 344 */     IncomingRpcMethod.method(ServerSettingsService::simulationDistance)
/* 345 */       .description("Get the server's simulation distance in chunks")
/* 346 */       .response("distance", Schema.INT_SCHEMA)
/* 347 */       .register(methodRegistry, "serversettings/simulation_distance");
/* 349 */     IncomingRpcMethod.method(ServerSettingsService::setSimulationDistance)
/* 350 */       .description("Set the server's simulation distance in chunks")
/* 351 */       .param("distance", Schema.INT_SCHEMA)
/* 352 */       .response("distance", Schema.INT_SCHEMA)
/* 353 */       .register(methodRegistry, "serversettings/simulation_distance/set");
/* 355 */     IncomingRpcMethod.method(ServerSettingsService::acceptTransfers)
/* 356 */       .description("Get whether the server accepts player transfers from other servers")
/* 357 */       .response("accepted", Schema.BOOL_SCHEMA)
/* 358 */       .register(methodRegistry, "serversettings/accept_transfers");
/* 360 */     IncomingRpcMethod.method(ServerSettingsService::setAcceptTransfers)
/* 361 */       .description("Enable or disable accepting player transfers from other servers")
/* 362 */       .param("accept", Schema.BOOL_SCHEMA)
/* 363 */       .response("accepted", Schema.BOOL_SCHEMA)
/* 364 */       .register(methodRegistry, "serversettings/accept_transfers/set");
/* 366 */     IncomingRpcMethod.method(ServerSettingsService::statusHeartbeatInterval)
/* 367 */       .description("Get the interval in seconds between server status heartbeats")
/* 368 */       .response("seconds", Schema.INT_SCHEMA)
/* 369 */       .register(methodRegistry, "serversettings/status_heartbeat_interval");
/* 371 */     IncomingRpcMethod.method(ServerSettingsService::setStatusHeartbeatInterval)
/* 372 */       .description("Set the interval in seconds between server status heartbeats")
/* 373 */       .param("seconds", Schema.INT_SCHEMA)
/* 374 */       .response("seconds", Schema.INT_SCHEMA)
/* 375 */       .register(methodRegistry, "serversettings/status_heartbeat_interval/set");
/* 377 */     IncomingRpcMethod.method(ServerSettingsService::operatorUserPermissionLevel)
/* 378 */       .description("Get default operator permission level")
/* 379 */       .response("level", Schema.PERMISSION_LEVEL_SCHEMA)
/* 380 */       .register(methodRegistry, "serversettings/operator_user_permission_level");
/* 382 */     IncomingRpcMethod.method(ServerSettingsService::setOperatorUserPermissionLevel)
/* 383 */       .description("Set default operator permission level")
/* 384 */       .param("level", Schema.PERMISSION_LEVEL_SCHEMA)
/* 385 */       .response("level", Schema.PERMISSION_LEVEL_SCHEMA)
/* 386 */       .register(methodRegistry, "serversettings/operator_user_permission_level/set");
/* 388 */     IncomingRpcMethod.method(ServerSettingsService::hidesOnlinePlayers)
/* 389 */       .description("Get whether the server hides online player information from status queries")
/* 390 */       .response("hidden", Schema.BOOL_SCHEMA)
/* 391 */       .register(methodRegistry, "serversettings/hide_online_players");
/* 393 */     IncomingRpcMethod.method(ServerSettingsService::setHidesOnlinePlayers)
/* 394 */       .description("Enable or disable hiding online player information from status queries")
/* 395 */       .param("hide", Schema.BOOL_SCHEMA)
/* 396 */       .response("hidden", Schema.BOOL_SCHEMA)
/* 397 */       .register(methodRegistry, "serversettings/hide_online_players/set");
/* 399 */     IncomingRpcMethod.method(ServerSettingsService::repliesToStatus)
/* 400 */       .description("Get whether the server responds to connection status requests")
/* 401 */       .response("enabled", Schema.BOOL_SCHEMA)
/* 402 */       .register(methodRegistry, "serversettings/status_replies");
/* 404 */     IncomingRpcMethod.method(ServerSettingsService::setRepliesToStatus)
/* 405 */       .description("Enable or disable the server responding to connection status requests")
/* 406 */       .param("enable", Schema.BOOL_SCHEMA)
/* 407 */       .response("enabled", Schema.BOOL_SCHEMA)
/* 408 */       .register(methodRegistry, "serversettings/status_replies/set");
/* 410 */     IncomingRpcMethod.method(ServerSettingsService::entityBroadcastRangePercentage)
/* 411 */       .description("Get the entity broadcast range as a percentage")
/* 412 */       .response("percentage_points", Schema.INT_SCHEMA)
/* 413 */       .register(methodRegistry, "serversettings/entity_broadcast_range");
/* 415 */     IncomingRpcMethod.method(ServerSettingsService::setEntityBroadcastRangePercentage)
/* 416 */       .description("Set the entity broadcast range as a percentage")
/* 417 */       .param("percentage_points", Schema.INT_SCHEMA)
/* 418 */       .response("percentage_points", Schema.INT_SCHEMA)
/* 419 */       .register(methodRegistry, "serversettings/entity_broadcast_range/set");
/*     */   }
/*     */   
/*     */   private static void registerGameRuleService(Registry<IncomingRpcMethod<?, ?>> methodRegistry) {
/* 423 */     IncomingRpcMethod.method(GameRulesService::get)
/* 424 */       .description("Get the available game rule keys and their current values")
/* 425 */       .response("gamerules", Schema.TYPED_GAME_RULE_SCHEMA.asRef().asArray())
/* 426 */       .register(methodRegistry, "gamerules");
/* 428 */     IncomingRpcMethod.method(GameRulesService::update)
/* 429 */       .description("Update game rule value")
/* 430 */       .param("gamerule", Schema.UNTYPED_GAME_RULE_SCHEMA.asRef())
/* 431 */       .response("gamerule", Schema.TYPED_GAME_RULE_SCHEMA.asRef())
/* 432 */       .register(methodRegistry, "gamerules/update");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\jsonrpc\IncomingRpcMethods.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */