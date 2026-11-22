/*     */ package net.minecraft.server.dedicated;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Properties;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.jsonrpc.security.SecurityConfig;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionLevel;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.DataPackConfig;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DedicatedServerProperties extends Settings<DedicatedServerProperties> {
/*  52 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  53 */   private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");
/*     */   
/*  54 */   private static final Splitter COMMA_SPLITTER = Splitter.on(',').trimResults();
/*     */   
/*     */   public static final String MANAGEMENT_SERVER_TLS_ENABLED_KEY = "management-server-tls-enabled";
/*     */   
/*     */   public static final String MANAGEMENT_SERVER_TLS_KEYSTORE_KEY = "management-server-tls-keystore";
/*     */   
/*     */   public static final String MANAGEMENT_SERVER_TLS_KEYSTORE_PASSWORD_KEY = "management-server-tls-keystore-password";
/*     */   
/*  59 */   public final boolean onlineMode = get("online-mode", true);
/*     */   
/*  60 */   public final boolean preventProxyConnections = get("prevent-proxy-connections", false);
/*     */   
/*  61 */   public final String serverIp = get("server-ip", "");
/*     */   
/*  62 */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> allowFlight = getMutable("allow-flight", false);
/*     */   
/*  63 */   public final Settings<DedicatedServerProperties>.MutableValue<String> motd = getMutable("motd", "A Minecraft Server");
/*     */   
/*  64 */   public final boolean codeOfConduct = get("enable-code-of-conduct", false);
/*     */   
/*  65 */   public final String bugReportLink = get("bug-report-link", "");
/*     */   
/*  66 */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> forceGameMode = getMutable("force-gamemode", false);
/*     */   
/*  67 */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> enforceWhitelist = getMutable("enforce-whitelist", false);
/*     */   
/*  68 */   public final Settings<DedicatedServerProperties>.MutableValue<Difficulty> difficulty = getMutable("difficulty", dispatchNumberOrString(Difficulty::byId, Difficulty::byName), Difficulty::getSerializedName, Difficulty.EASY);
/*     */   
/*  69 */   public final Settings<DedicatedServerProperties>.MutableValue<GameType> gameMode = getMutable("gamemode", dispatchNumberOrString(GameType::byId, GameType::byName), GameType::getName, GameType.SURVIVAL);
/*     */   
/*  70 */   public final String levelName = get("level-name", "world");
/*     */   
/*  71 */   public final int serverPort = get("server-port", 25565);
/*     */   
/*  73 */   public final boolean managementServerEnabled = get("management-server-enabled", false);
/*     */   
/*  74 */   public final String managementServerHost = get("management-server-host", "localhost");
/*     */   
/*  75 */   public final int managementServerPort = get("management-server-port", 0);
/*     */   
/*  76 */   public final String managementServerSecret = get("management-server-secret", SecurityConfig.generateSecretKey());
/*     */   
/*  77 */   public final boolean managementServerTlsEnabled = get("management-server-tls-enabled", true);
/*     */   
/*  78 */   public final String managementServerTlsKeystore = get("management-server-tls-keystore", "");
/*     */   
/*  79 */   public final String managementServerTlsKeystorePassword = get("management-server-tls-keystore-password", "");
/*     */   
/*  80 */   public final String managementServerAllowedOrigins = get("management-server-allowed-origins", "");
/*     */   
/*  82 */   public final Boolean announcePlayerAchievements = getLegacyBoolean("announce-player-achievements");
/*     */   
/*  83 */   public final boolean enableQuery = get("enable-query", false);
/*     */   
/*  84 */   public final int queryPort = get("query.port", 25565);
/*     */   
/*  85 */   public final boolean enableRcon = get("enable-rcon", false);
/*     */   
/*  86 */   public final int rconPort = get("rcon.port", 25575);
/*     */   
/*  87 */   public final String rconPassword = get("rcon.password", "");
/*     */   
/*  88 */   public final boolean hardcore = get("hardcore", false);
/*     */   
/*  89 */   public final boolean useNativeTransport = get("use-native-transport", true);
/*     */   
/*  90 */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> spawnProtection = getMutable("spawn-protection", 16);
/*     */   
/*  91 */   public final Settings<DedicatedServerProperties>.MutableValue<LevelBasedPermissionSet> opPermissions = getMutable("op-permission-level", DedicatedServerProperties::deserializePermission, DedicatedServerProperties::serializePermission, LevelBasedPermissionSet.OWNER);
/*     */   
/*  92 */   public final LevelBasedPermissionSet functionPermissions = (LevelBasedPermissionSet)get("function-permission-level", DedicatedServerProperties::deserializePermission, DedicatedServerProperties::serializePermission, LevelBasedPermissionSet.GAMEMASTER);
/*     */   
/*  93 */   public final long maxTickTime = get("max-tick-time", TimeUnit.MINUTES.toMillis(1L));
/*     */   
/*  94 */   public final int maxChainedNeighborUpdates = get("max-chained-neighbor-updates", 1000000);
/*     */   
/*  95 */   public final int rateLimitPacketsPerSecond = get("rate-limit", 0);
/*     */   
/*  96 */   public final int commandSpamThresholdSeconds = get("command-spam-threshold-seconds", 10);
/*     */   
/*  97 */   public final int chatSpamThresholdSeconds = get("chat-spam-threshold-seconds", 10);
/*     */   
/*  98 */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> viewDistance = getMutable("view-distance", 10);
/*     */   
/*  99 */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> simulationDistance = getMutable("simulation-distance", 10);
/*     */   
/* 100 */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> maxPlayers = getMutable("max-players", 20);
/*     */   
/* 101 */   public final int networkCompressionThreshold = get("network-compression-threshold", 256);
/*     */   
/* 102 */   public final boolean broadcastRconToOps = get("broadcast-rcon-to-ops", true);
/*     */   
/* 103 */   public final boolean broadcastConsoleToOps = get("broadcast-console-to-ops", true);
/*     */   
/*     */   public final int maxWorldSize;
/*     */   
/*     */   public final boolean syncChunkWrites;
/*     */   
/*     */   public final String regionFileComression;
/*     */   
/*     */   public final boolean enableJmxMonitoring;
/*     */   
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> enableStatus;
/*     */   
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> hideOnlinePlayers;
/*     */   
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> entityBroadcastRangePercentage;
/*     */   
/*     */   public final String textFilteringConfig;
/*     */   
/*     */   public final int textFilteringVersion;
/*     */   
/*     */   public final Optional<MinecraftServer.ServerResourcePackInfo> serverResourcePackInfo;
/*     */   
/*     */   public final DataPackConfig initialDataPackConfiguration;
/*     */   
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> playerIdleTimeout;
/*     */   
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> statusHeartbeatInterval;
/*     */   
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> whiteList;
/*     */   
/*     */   public final boolean enforceSecureProfile;
/*     */   
/*     */   public final boolean logIPs;
/*     */   
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Integer> pauseWhenEmptySeconds;
/*     */   
/*     */   private final WorldDimensionData worldDimensionData;
/*     */   
/*     */   public final WorldOptions worldOptions;
/*     */   
/*     */   public final Settings<DedicatedServerProperties>.MutableValue<Boolean> acceptsTransfers;
/*     */   
/*     */   public DedicatedServerProperties(Properties settings) {
/* 128 */     super(settings);
/*     */     this.maxWorldSize = get("max-world-size", v -> Integer.valueOf(Mth.clamp(v.intValue(), 1, 29999984)), 29999984);
/*     */     this.syncChunkWrites = get("sync-chunk-writes", true);
/*     */     this.regionFileComression = get("region-file-compression", "deflate");
/*     */     this.enableJmxMonitoring = get("enable-jmx-monitoring", false);
/*     */     this.enableStatus = getMutable("enable-status", true);
/*     */     this.hideOnlinePlayers = getMutable("hide-online-players", false);
/*     */     this.entityBroadcastRangePercentage = getMutable("entity-broadcast-range-percentage", v -> Integer.valueOf(Mth.clamp(Integer.parseInt(v), 10, 1000)), Integer.valueOf(100));
/*     */     this.textFilteringConfig = get("text-filtering-config", "");
/*     */     this.textFilteringVersion = get("text-filtering-version", 0);
/*     */     this.playerIdleTimeout = getMutable("player-idle-timeout", 0);
/*     */     this.statusHeartbeatInterval = getMutable("status-heartbeat-interval", 0);
/*     */     this.whiteList = getMutable("white-list", false);
/*     */     this.enforceSecureProfile = get("enforce-secure-profile", true);
/*     */     this.logIPs = get("log-ips", true);
/*     */     this.pauseWhenEmptySeconds = getMutable("pause-when-empty-seconds", 60);
/*     */     this.acceptsTransfers = getMutable("accepts-transfers", false);
/* 130 */     String levelSeed = get("level-seed", "");
/* 131 */     boolean generateStructures = get("generate-structures", true);
/* 133 */     long seed = WorldOptions.parseSeed(levelSeed).orElse(WorldOptions.randomSeed());
/* 134 */     this.worldOptions = new WorldOptions(seed, generateStructures, false);
/* 136 */     this
/*     */       
/* 138 */       .worldDimensionData = new WorldDimensionData((JsonObject)get("generator-settings", s -> GsonHelper.parse(!s.isEmpty() ? s : "{}"), new JsonObject()), (String)get("level-type", v -> v.toLowerCase(Locale.ROOT), WorldPresets.NORMAL.identifier().toString()));
/* 140 */     this.serverResourcePackInfo = getServerPackInfo(
/* 141 */         get("resource-pack-id", ""), 
/* 142 */         get("resource-pack", ""), 
/* 143 */         get("resource-pack-sha1", ""), 
/* 144 */         getLegacyString("resource-pack-hash"), 
/* 145 */         get("require-resource-pack", false), 
/* 146 */         get("resource-pack-prompt", ""));
/* 149 */     this.initialDataPackConfiguration = getDatapackConfig(
/* 150 */         get("initial-enabled-packs", String.join(",", WorldDataConfiguration.DEFAULT.dataPacks().getEnabled())), 
/* 151 */         get("initial-disabled-packs", String.join(",", WorldDataConfiguration.DEFAULT.dataPacks().getDisabled())));
/*     */   }
/*     */   
/*     */   public static DedicatedServerProperties fromFile(Path file) {
/* 156 */     return new DedicatedServerProperties(loadFromFile(file));
/*     */   }
/*     */   
/*     */   protected DedicatedServerProperties reload(RegistryAccess registryAccess, Properties properties) {
/* 161 */     return new DedicatedServerProperties(properties);
/*     */   }
/*     */   
/*     */   private static Component parseResourcePackPrompt(String prompt) {
/* 165 */     if (!Strings.isNullOrEmpty(prompt))
/*     */       try {
/* 167 */         JsonElement element = StrictJsonParser.parse(prompt);
/* 168 */         return ComponentSerialization.CODEC.parse((DynamicOps)RegistryAccess.EMPTY.createSerializationContext((DynamicOps)JsonOps.INSTANCE), element)
/* 169 */           .resultOrPartial(msg -> LOGGER.warn("Failed to parse resource pack prompt '{}': {}", prompt, msg)).orElse(null);
/* 170 */       } catch (Exception e) {
/* 171 */         LOGGER.warn("Failed to parse resource pack prompt '{}'", prompt, e);
/*     */       }  
/* 174 */     return null;
/*     */   }
/*     */   
/*     */   private static Optional<MinecraftServer.ServerResourcePackInfo> getServerPackInfo(String id, String url, String resourcePackSha1, String resourcePackHash, boolean requireResourcePack, String resourcePackPrompt) {
/*     */     String hash;
/*     */     UUID parsedId;
/* 178 */     if (url.isEmpty())
/* 179 */       return Optional.empty(); 
/* 183 */     if (!resourcePackSha1.isEmpty()) {
/* 184 */       hash = resourcePackSha1;
/* 185 */       if (!Strings.isNullOrEmpty(resourcePackHash))
/* 186 */         LOGGER.warn("resource-pack-hash is deprecated and found along side resource-pack-sha1. resource-pack-hash will be ignored."); 
/* 188 */     } else if (!Strings.isNullOrEmpty(resourcePackHash)) {
/* 189 */       LOGGER.warn("resource-pack-hash is deprecated. Please use resource-pack-sha1 instead.");
/* 190 */       hash = resourcePackHash;
/*     */     } else {
/* 192 */       hash = "";
/*     */     } 
/* 195 */     if (hash.isEmpty()) {
/* 196 */       LOGGER.warn("You specified a resource pack without providing a sha1 hash. Pack will be updated on the client only if you change the name of the pack.");
/* 197 */     } else if (!SHA1.matcher(hash).matches()) {
/* 198 */       LOGGER.warn("Invalid sha1 for resource-pack-sha1");
/*     */     } 
/* 201 */     Component prompt = parseResourcePackPrompt(resourcePackPrompt);
/* 204 */     if (id.isEmpty()) {
/* 205 */       parsedId = UUID.nameUUIDFromBytes(url.getBytes(StandardCharsets.UTF_8));
/* 206 */       LOGGER.warn("resource-pack-id missing, using default of {}", parsedId);
/*     */     } else {
/*     */       try {
/* 209 */         parsedId = UUID.fromString(id);
/* 210 */       } catch (IllegalArgumentException e) {
/* 211 */         LOGGER.warn("Failed to parse '{}' into UUID", id);
/* 212 */         return Optional.empty();
/*     */       } 
/*     */     } 
/* 216 */     return Optional.of(new MinecraftServer.ServerResourcePackInfo(parsedId, url, hash, requireResourcePack, prompt));
/*     */   }
/*     */   
/*     */   private static DataPackConfig getDatapackConfig(String enabledPacks, String disabledPacks) {
/* 220 */     List<String> enabledPacksIds = COMMA_SPLITTER.splitToList(enabledPacks);
/* 221 */     List<String> disabledPacksIds = COMMA_SPLITTER.splitToList(disabledPacks);
/* 222 */     return new DataPackConfig(enabledPacksIds, disabledPacksIds);
/*     */   }
/*     */   
/*     */   public static LevelBasedPermissionSet deserializePermission(String value) {
/*     */     try {
/* 227 */       PermissionLevel permissionLevel = PermissionLevel.byId(Integer.parseInt(value));
/* 228 */       return LevelBasedPermissionSet.forLevel(permissionLevel);
/* 229 */     } catch (NumberFormatException e) {
/* 230 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String serializePermission(LevelBasedPermissionSet permission) {
/* 235 */     return Integer.toString(permission.level().id());
/*     */   }
/*     */   
/*     */   public WorldDimensions createDimensions(HolderLookup.Provider registries) {
/* 239 */     return this.worldDimensionData.create(registries);
/*     */   }
/*     */   
/*     */   private static final class WorldDimensionData extends Record {
/*     */     private final JsonObject generatorSettings;
/*     */     
/*     */     private final String levelType;
/*     */     
/*     */     private WorldDimensionData(JsonObject generatorSettings, String levelType) {
/* 242 */       this.generatorSettings = generatorSettings;
/* 242 */       this.levelType = levelType;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #242	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #242	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #242	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/dedicated/DedicatedServerProperties$WorldDimensionData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public JsonObject generatorSettings() {
/* 242 */       return this.generatorSettings;
/*     */     }
/*     */     
/*     */     public String levelType() {
/* 242 */       return this.levelType;
/*     */     }
/*     */     
/* 243 */     private static final Map<String, ResourceKey<WorldPreset>> LEGACY_PRESET_NAMES = Map.of("default", WorldPresets.NORMAL, "largebiomes", WorldPresets.LARGE_BIOMES);
/*     */     
/*     */     public WorldDimensions create(HolderLookup.Provider registries) {
/* 249 */       HolderLookup.RegistryLookup registryLookup = registries.lookupOrThrow(Registries.WORLD_PRESET);
/* 251 */       Holder.Reference<WorldPreset> defaultHolder = (Holder.Reference<WorldPreset>)registryLookup.get(WorldPresets.NORMAL).or(() -> worldPresets.listElements().findAny()).orElseThrow(() -> new IllegalStateException("Invalid datapack contents: can't find default preset"));
/* 256 */       Objects.requireNonNull(registryLookup);
/* 257 */       Holder<WorldPreset> worldPreset = Optional.<Identifier>ofNullable(Identifier.tryParse(this.levelType)).map(id -> ResourceKey.create(Registries.WORLD_PRESET, id)).or(() -> Optional.ofNullable(LEGACY_PRESET_NAMES.get(this.levelType))).flatMap(registryLookup::get).orElseGet(() -> {
/*     */             DedicatedServerProperties.LOGGER.warn("Failed to parse level-type {}, defaulting to {}", this.levelType, defaultHolder.key().identifier());
/*     */             return defaultHolder;
/*     */           });
/* 262 */       WorldDimensions worldDimensions = ((WorldPreset)worldPreset.value()).createWorldDimensions();
/* 265 */       if (worldPreset.is(WorldPresets.FLAT)) {
/* 266 */         RegistryOps<JsonElement> ops = registries.createSerializationContext((DynamicOps)JsonOps.INSTANCE);
/* 267 */         Objects.requireNonNull(DedicatedServerProperties.LOGGER);
/* 267 */         Optional<FlatLevelGeneratorSettings> parsedSettings = FlatLevelGeneratorSettings.CODEC.parse(new Dynamic((DynamicOps)ops, generatorSettings())).resultOrPartial(DedicatedServerProperties.LOGGER::error);
/* 268 */         if (parsedSettings.isPresent())
/* 269 */           return worldDimensions.replaceOverworldGenerator(registries, (ChunkGenerator)new FlatLevelSource(parsedSettings.get())); 
/*     */       } 
/* 273 */       return worldDimensions;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\dedicated\DedicatedServerProperties.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */