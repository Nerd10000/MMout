/*     */ package net.minecraft.client.telemetry;
/*     */ 
/*     */ import com.mojang.authlib.minecraft.TelemetryPropertyContainer;
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import java.time.Instant;
/*     */ import java.time.ZoneId;
/*     */ import java.time.ZoneOffset;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.telemetry.events.GameLoadTimesEvent;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ public final class TelemetryProperty<T> extends Record {
/*     */   private final String id;
/*     */   
/*     */   private final String exportKey;
/*     */   
/*     */   private final Codec<T> codec;
/*     */   
/*     */   private final Exporter<T> exporter;
/*     */   
/*     */   public TelemetryProperty(String id, String exportKey, Codec<T> codec, Exporter<T> exporter) {
/*  22 */     this.id = id;
/*  22 */     this.exportKey = exportKey;
/*  22 */     this.codec = codec;
/*  22 */     this.exporter = exporter;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/telemetry/TelemetryProperty;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/client/telemetry/TelemetryProperty;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	7	0	this	Lnet/minecraft/client/telemetry/TelemetryProperty<TT;>;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/telemetry/TelemetryProperty;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #22	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/client/telemetry/TelemetryProperty;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	8	0	this	Lnet/minecraft/client/telemetry/TelemetryProperty<TT;>;
/*     */   }
/*     */   
/*     */   public String id() {
/*  22 */     return this.id;
/*     */   }
/*     */   
/*     */   public String exportKey() {
/*  22 */     return this.exportKey;
/*     */   }
/*     */   
/*     */   public Codec<T> codec() {
/*  22 */     return this.codec;
/*     */   }
/*     */   
/*     */   public Exporter<T> exporter() {
/*  22 */     return this.exporter;
/*     */   }
/*     */   
/*  23 */   private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.from(ZoneOffset.UTC));
/*     */   
/*  28 */   public static final TelemetryProperty<String> USER_ID = string("user_id", "userId");
/*     */   
/*  29 */   public static final TelemetryProperty<String> CLIENT_ID = string("client_id", "clientId");
/*     */   
/*  30 */   public static final TelemetryProperty<UUID> MINECRAFT_SESSION_ID = uuid("minecraft_session_id", "deviceSessionId");
/*     */   
/*  31 */   public static final TelemetryProperty<String> GAME_VERSION = string("game_version", "buildDisplayName");
/*     */   
/*  32 */   public static final TelemetryProperty<String> OPERATING_SYSTEM = string("operating_system", "buildPlatform");
/*     */   
/*  33 */   public static final TelemetryProperty<String> PLATFORM = string("platform", "platform");
/*     */   
/*  34 */   public static final TelemetryProperty<Boolean> CLIENT_MODDED = bool("client_modded", "clientModded");
/*     */   
/*  35 */   public static final TelemetryProperty<String> LAUNCHER_NAME = string("launcher_name", "launcherName");
/*     */   
/*  36 */   public static final TelemetryProperty<String> BACKEND_NAME = string("backend_name", "backendName");
/*     */   
/*  37 */   public static final TelemetryProperty<String> BACKEND_FAILURE_MESSAGE = string("backend_failure_message", "backendFailureMessage");
/*     */   
/*  38 */   public static final TelemetryProperty<String> BACKEND_FAILURE_REASON = string("backend_failure_reason", "backendFailureReason");
/*     */   
/*  39 */   public static final TelemetryProperty<String> BACKEND_FAILURE_MISSING_CAPABILITIES = string("backend_failure_missing_capabilities", "backendFailureMissingCapabilities");
/*     */   
/*  42 */   public static final TelemetryProperty<UUID> WORLD_SESSION_ID = uuid("world_session_id", "worldSessionId");
/*     */   
/*  43 */   public static final TelemetryProperty<UUID> SERVER_SESSION_ID = uuid("server_session_id", "GlobalMultiplayerCorrelationId");
/*     */   
/*  44 */   public static final TelemetryProperty<Boolean> SERVER_MODDED = bool("server_modded", "serverModded");
/*     */   
/*     */   public static final TelemetryProperty<ServerType> SERVER_TYPE;
/*     */   
/*     */   static {
/*  45 */     SERVER_TYPE = create("server_type", "serverType", ServerType.CODEC, (output, key, value) -> output.addProperty(key, value.getSerializedName()));
/*     */   }
/*     */   
/*  48 */   public static final TelemetryProperty<Boolean> OPT_IN = bool("opt_in", "isOptional");
/*     */   
/*     */   public static final TelemetryProperty<Instant> EVENT_TIMESTAMP_UTC;
/*     */   
/*     */   public static final TelemetryProperty<GameMode> GAME_MODE;
/*     */   
/*     */   static {
/*  49 */     EVENT_TIMESTAMP_UTC = create("event_timestamp_utc", "eventTimestampUtc", ExtraCodecs.INSTANT_ISO8601, (output, key, value) -> output.addProperty(key, TIMESTAMP_FORMATTER.format(value)));
/*  52 */     GAME_MODE = create("game_mode", "playerGameMode", GameMode.CODEC, (output, key, value) -> output.addProperty(key, value.id()));
/*     */   }
/*     */   
/*  53 */   public static final TelemetryProperty<String> REALMS_MAP_CONTENT = string("realms_map_content", "realmsMapContent");
/*     */   
/*  56 */   public static final TelemetryProperty<Integer> SECONDS_SINCE_LOAD = integer("seconds_since_load", "secondsSinceLoad");
/*     */   
/*  57 */   public static final TelemetryProperty<Integer> TICKS_SINCE_LOAD = integer("ticks_since_load", "ticksSinceLoad");
/*     */   
/*  60 */   public static final TelemetryProperty<LongList> FRAME_RATE_SAMPLES = longSamples("frame_rate_samples", "serializedFpsSamples");
/*     */   
/*  61 */   public static final TelemetryProperty<LongList> RENDER_TIME_SAMPLES = longSamples("render_time_samples", "serializedRenderTimeSamples");
/*     */   
/*  62 */   public static final TelemetryProperty<LongList> USED_MEMORY_SAMPLES = longSamples("used_memory_samples", "serializedUsedMemoryKbSamples");
/*     */   
/*  63 */   public static final TelemetryProperty<Integer> NUMBER_OF_SAMPLES = integer("number_of_samples", "numSamples");
/*     */   
/*  64 */   public static final TelemetryProperty<Integer> RENDER_DISTANCE = integer("render_distance", "renderDistance");
/*     */   
/*  65 */   public static final TelemetryProperty<Integer> DEDICATED_MEMORY_KB = integer("dedicated_memory_kb", "dedicatedMemoryKb");
/*     */   
/*  68 */   public static final TelemetryProperty<Integer> WORLD_LOAD_TIME_MS = integer("world_load_time_ms", "worldLoadTimeMs");
/*     */   
/*  69 */   public static final TelemetryProperty<Boolean> NEW_WORLD = bool("new_world", "newWorld");
/*     */   
/*  72 */   public static final TelemetryProperty<GameLoadTimesEvent.Measurement> LOAD_TIME_TOTAL_TIME_MS = gameLoadMeasurement("load_time_total_time_ms", "loadTimeTotalTimeMs");
/*     */   
/*  73 */   public static final TelemetryProperty<GameLoadTimesEvent.Measurement> LOAD_TIME_PRE_WINDOW_MS = gameLoadMeasurement("load_time_pre_window_ms", "loadTimePreWindowMs");
/*     */   
/*  74 */   public static final TelemetryProperty<GameLoadTimesEvent.Measurement> LOAD_TIME_BOOTSTRAP_MS = gameLoadMeasurement("load_time_bootstrap_ms", "loadTimeBootstrapMs");
/*     */   
/*  75 */   public static final TelemetryProperty<GameLoadTimesEvent.Measurement> LOAD_TIME_LOADING_OVERLAY_MS = gameLoadMeasurement("load_time_loading_overlay_ms", "loadTimeLoadingOverlayMs");
/*     */   
/*  78 */   public static final TelemetryProperty<String> ADVANCEMENT_ID = string("advancement_id", "advancementId");
/*     */   
/*  79 */   public static final TelemetryProperty<Long> ADVANCEMENT_GAME_TIME = makeLong("advancement_game_time", "advancementGameTime");
/*     */   
/*     */   public static <T> TelemetryProperty<T> create(String id, String exportKey, Codec<T> codec, Exporter<T> exporter) {
/*  82 */     return new TelemetryProperty<>(id, exportKey, codec, exporter);
/*     */   }
/*     */   
/*     */   public static TelemetryProperty<Boolean> bool(String id, String exportKey) {
/*  86 */     return create(id, exportKey, (Codec<Boolean>)Codec.BOOL, TelemetryPropertyContainer::addProperty);
/*     */   }
/*     */   
/*     */   public static TelemetryProperty<String> string(String id, String exportKey) {
/*  90 */     return create(id, exportKey, (Codec<String>)Codec.STRING, TelemetryPropertyContainer::addProperty);
/*     */   }
/*     */   
/*     */   public static TelemetryProperty<Integer> integer(String id, String exportKey) {
/*  94 */     return create(id, exportKey, (Codec<Integer>)Codec.INT, TelemetryPropertyContainer::addProperty);
/*     */   }
/*     */   
/*     */   public static TelemetryProperty<Long> makeLong(String id, String exportKey) {
/*  98 */     return create(id, exportKey, (Codec<Long>)Codec.LONG, TelemetryPropertyContainer::addProperty);
/*     */   }
/*     */   
/*     */   public static TelemetryProperty<UUID> uuid(String id, String exportKey) {
/* 102 */     return create(id, exportKey, UUIDUtil.STRING_CODEC, (output, key, value) -> output.addProperty(key, value.toString()));
/*     */   }
/*     */   
/*     */   public static TelemetryProperty<GameLoadTimesEvent.Measurement> gameLoadMeasurement(String id, String exportKey) {
/* 106 */     return create(id, exportKey, GameLoadTimesEvent.Measurement.CODEC, (output, key, value) -> output.addProperty(key, value.millis()));
/*     */   }
/*     */   
/*     */   public static TelemetryProperty<LongList> longSamples(String id, String exportKey) {
/* 110 */     return create(id, exportKey, Codec.LONG
/* 111 */         .listOf().xmap(it.unimi.dsi.fastutil.longs.LongArrayList::new, Function.identity()), (output, key, value) -> output.addProperty(key, value.longStream().<CharSequence>mapToObj(String::valueOf).collect(Collectors.joining(";"))));
/*     */   }
/*     */   
/*     */   public void export(TelemetryPropertyMap input, TelemetryPropertyContainer output) {
/* 117 */     T value = input.get(this);
/* 118 */     if (value != null) {
/* 119 */       this.exporter.apply(output, this.exportKey, value);
/*     */     } else {
/* 121 */       output.addNullProperty(this.exportKey);
/*     */     } 
/*     */   }
/*     */   
/*     */   public MutableComponent title() {
/* 126 */     return Component.translatable("telemetry.property." + this.id + ".title");
/*     */   }
/*     */   
/*     */   public String toString() {
/* 131 */     return "TelemetryProperty[" + this.id + "]";
/*     */   }
/*     */   
/*     */   public enum ServerType implements StringRepresentable {
/* 139 */     REALM("realm"),
/* 140 */     LOCAL("local"),
/* 141 */     OTHER("server");
/*     */     
/* 144 */     public static final Codec<ServerType> CODEC = (Codec<ServerType>)StringRepresentable.fromEnum(ServerType::values);
/*     */     
/*     */     private final String key;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     ServerType(String key) {
/* 149 */       this.key = key;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 154 */       return this.key;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum GameMode implements StringRepresentable {
/* 160 */     SURVIVAL("survival", 0),
/* 161 */     CREATIVE("creative", 1),
/* 162 */     ADVENTURE("adventure", 2),
/* 163 */     SPECTATOR("spectator", 6),
/* 165 */     HARDCORE("hardcore", 99);
/*     */     
/* 168 */     public static final Codec<GameMode> CODEC = (Codec<GameMode>)StringRepresentable.fromEnum(GameMode::values);
/*     */     
/*     */     private final String key;
/*     */     
/*     */     private final int id;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     GameMode(String key, int id) {
/* 174 */       this.key = key;
/* 175 */       this.id = id;
/*     */     }
/*     */     
/*     */     public int id() {
/* 179 */       return this.id;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 184 */       return this.key;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Exporter<T> {
/*     */     void apply(TelemetryPropertyContainer param1TelemetryPropertyContainer, String param1String, T param1T);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\TelemetryProperty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */