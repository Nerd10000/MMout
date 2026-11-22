/*     */ package net.minecraft.client.telemetry;
/*     */ 
/*     */ import com.mojang.authlib.minecraft.TelemetryEvent;
/*     */ import com.mojang.authlib.minecraft.TelemetryPropertyContainer;
/*     */ import com.mojang.authlib.minecraft.TelemetrySession;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.telemetry.events.GameLoadTimesEvent;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TelemetryEventType {
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  25 */   private static final Map<String, TelemetryEventType> REGISTRY = (Map<String, TelemetryEventType>)new Object2ObjectLinkedOpenHashMap();
/*     */   
/*     */   public static final Codec<TelemetryEventType> CODEC;
/*     */   
/*     */   static {
/*  27 */     CODEC = Codec.STRING.comapFlatMap(key -> {
/*     */           TelemetryEventType type = REGISTRY.get(key);
/*     */           return (type != null) ? DataResult.success(type) : DataResult.error(());
/*  27 */         }TelemetryEventType::id);
/*     */   }
/*     */   
/*  35 */   private static final List<TelemetryProperty<?>> GLOBAL_PROPERTIES = List.of(TelemetryProperty.USER_ID, TelemetryProperty.CLIENT_ID, TelemetryProperty.MINECRAFT_SESSION_ID, TelemetryProperty.GAME_VERSION, TelemetryProperty.OPERATING_SYSTEM, TelemetryProperty.PLATFORM, TelemetryProperty.CLIENT_MODDED, TelemetryProperty.LAUNCHER_NAME, TelemetryProperty.EVENT_TIMESTAMP_UTC, TelemetryProperty.OPT_IN);
/*     */   
/*  51 */   private static final List<TelemetryProperty<?>> WORLD_SESSION_PROPERTIES = Stream.<TelemetryProperty<?>>concat(GLOBAL_PROPERTIES.stream(), Stream.of((TelemetryProperty<?>[])new TelemetryProperty[] { TelemetryProperty.WORLD_SESSION_ID, TelemetryProperty.SERVER_MODDED, TelemetryProperty.SERVER_TYPE, TelemetryProperty.SERVER_SESSION_ID })).toList();
/*     */   
/*  58 */   public static final TelemetryEventType GRAPHICS_CAPABILITIES = builder("graphics_capabilities", "GraphicsCapabilities")
/*  59 */     .defineAll(GLOBAL_PROPERTIES)
/*  60 */     .<String>define(TelemetryProperty.BACKEND_NAME)
/*  61 */     .<String>define(TelemetryProperty.BACKEND_FAILURE_MESSAGE)
/*  62 */     .<String>define(TelemetryProperty.BACKEND_FAILURE_REASON)
/*  63 */     .<String>define(TelemetryProperty.BACKEND_FAILURE_MISSING_CAPABILITIES)
/*  64 */     .register();
/*     */   
/*  66 */   public static final TelemetryEventType WORLD_LOADED = builder("world_loaded", "WorldLoaded")
/*  67 */     .defineAll(WORLD_SESSION_PROPERTIES)
/*  68 */     .<TelemetryProperty.GameMode>define(TelemetryProperty.GAME_MODE)
/*  69 */     .<String>define(TelemetryProperty.REALMS_MAP_CONTENT)
/*  70 */     .register();
/*     */   
/*  72 */   public static final TelemetryEventType PERFORMANCE_METRICS = builder("performance_metrics", "PerformanceMetrics")
/*  73 */     .defineAll(WORLD_SESSION_PROPERTIES)
/*  74 */     .<LongList>define(TelemetryProperty.FRAME_RATE_SAMPLES)
/*  75 */     .<LongList>define(TelemetryProperty.RENDER_TIME_SAMPLES)
/*  76 */     .<LongList>define(TelemetryProperty.USED_MEMORY_SAMPLES)
/*  77 */     .<Integer>define(TelemetryProperty.NUMBER_OF_SAMPLES)
/*  78 */     .<Integer>define(TelemetryProperty.RENDER_DISTANCE)
/*  79 */     .<Integer>define(TelemetryProperty.DEDICATED_MEMORY_KB)
/*  80 */     .optIn()
/*  81 */     .register();
/*     */   
/*  83 */   public static final TelemetryEventType WORLD_LOAD_TIMES = builder("world_load_times", "WorldLoadTimes")
/*  84 */     .defineAll(WORLD_SESSION_PROPERTIES)
/*  85 */     .<Integer>define(TelemetryProperty.WORLD_LOAD_TIME_MS)
/*  86 */     .<Boolean>define(TelemetryProperty.NEW_WORLD)
/*  87 */     .optIn()
/*  88 */     .register();
/*     */   
/*  90 */   public static final TelemetryEventType WORLD_UNLOADED = builder("world_unloaded", "WorldUnloaded")
/*  91 */     .defineAll(WORLD_SESSION_PROPERTIES)
/*  92 */     .<Integer>define(TelemetryProperty.SECONDS_SINCE_LOAD)
/*  93 */     .<Integer>define(TelemetryProperty.TICKS_SINCE_LOAD)
/*  94 */     .register();
/*     */   
/*  96 */   public static final TelemetryEventType ADVANCEMENT_MADE = builder("advancement_made", "AdvancementMade")
/*  97 */     .defineAll(WORLD_SESSION_PROPERTIES)
/*  98 */     .<String>define(TelemetryProperty.ADVANCEMENT_ID)
/*  99 */     .<Long>define(TelemetryProperty.ADVANCEMENT_GAME_TIME)
/* 100 */     .optIn()
/* 101 */     .register();
/*     */   
/* 103 */   public static final TelemetryEventType GAME_LOAD_TIMES = builder("game_load_times", "GameLoadTimes")
/* 104 */     .defineAll(GLOBAL_PROPERTIES)
/* 105 */     .<GameLoadTimesEvent.Measurement>define(TelemetryProperty.LOAD_TIME_TOTAL_TIME_MS)
/* 106 */     .<GameLoadTimesEvent.Measurement>define(TelemetryProperty.LOAD_TIME_PRE_WINDOW_MS)
/* 107 */     .<GameLoadTimesEvent.Measurement>define(TelemetryProperty.LOAD_TIME_BOOTSTRAP_MS)
/* 108 */     .<GameLoadTimesEvent.Measurement>define(TelemetryProperty.LOAD_TIME_LOADING_OVERLAY_MS)
/* 109 */     .optIn()
/* 110 */     .register();
/*     */   
/*     */   private final String id;
/*     */   
/*     */   private final String exportKey;
/*     */   
/*     */   private final List<TelemetryProperty<?>> properties;
/*     */   
/*     */   private final boolean isOptIn;
/*     */   
/*     */   private final MapCodec<TelemetryEventInstance> codec;
/*     */   
/*     */   private TelemetryEventType(String id, String exportKey, List<TelemetryProperty<?>> properties, boolean isOptIn) {
/* 120 */     this.id = id;
/* 121 */     this.exportKey = exportKey;
/* 122 */     this.properties = properties;
/* 123 */     this.isOptIn = isOptIn;
/* 124 */     this.codec = TelemetryPropertyMap.createCodec(properties).xmap(map -> new TelemetryEventInstance(this, map), TelemetryEventInstance::properties);
/*     */   }
/*     */   
/*     */   public static Builder builder(String id, String exportKey) {
/* 131 */     return new Builder(id, exportKey);
/*     */   }
/*     */   
/*     */   public String id() {
/* 135 */     return this.id;
/*     */   }
/*     */   
/*     */   public List<TelemetryProperty<?>> properties() {
/* 139 */     return this.properties;
/*     */   }
/*     */   
/*     */   public MapCodec<TelemetryEventInstance> codec() {
/* 143 */     return this.codec;
/*     */   }
/*     */   
/*     */   public boolean isOptIn() {
/* 147 */     return this.isOptIn;
/*     */   }
/*     */   
/*     */   public TelemetryEvent export(TelemetrySession session, TelemetryPropertyMap input) {
/* 151 */     TelemetryEvent output = session.createNewEvent(this.exportKey);
/* 152 */     for (TelemetryProperty<?> property : this.properties)
/* 153 */       property.export(input, (TelemetryPropertyContainer)output); 
/* 155 */     return output;
/*     */   }
/*     */   
/*     */   public <T> boolean contains(TelemetryProperty<T> property) {
/* 159 */     return this.properties.contains(property);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 164 */     return "TelemetryEventType[" + this.id + "]";
/*     */   }
/*     */   
/*     */   public MutableComponent title() {
/* 168 */     return makeTranslation("title");
/*     */   }
/*     */   
/*     */   public MutableComponent description() {
/* 172 */     return makeTranslation("description");
/*     */   }
/*     */   
/*     */   private MutableComponent makeTranslation(String suffix) {
/* 176 */     return Component.translatable("telemetry.event." + this.id + "." + suffix);
/*     */   }
/*     */   
/*     */   public static List<TelemetryEventType> values() {
/* 180 */     return List.copyOf(REGISTRY.values());
/*     */   }
/*     */   
/*     */   public static boolean selfTest() {
/* 184 */     boolean hasErrors = false;
/* 185 */     Set<TelemetryProperty<?>> allProperties = new HashSet<>();
/* 186 */     for (Map.Entry<String, TelemetryEventType> entry : REGISTRY.entrySet()) {
/* 187 */       TelemetryEventType type = entry.getValue();
/* 188 */       if (!ComponentUtils.isTranslationResolvable((Component)type.description()) || !ComponentUtils.isTranslationResolvable((Component)type.title())) {
/* 189 */         LOGGER.warn("Missing translations for telemetry event {}", entry.getKey());
/* 190 */         hasErrors = true;
/*     */       } 
/* 192 */       allProperties.addAll(type.properties);
/*     */     } 
/* 195 */     for (TelemetryProperty<?> property : allProperties) {
/* 196 */       if (!ComponentUtils.isTranslationResolvable((Component)property.title())) {
/* 197 */         LOGGER.warn("Missing translation for telemetry property {}", property.id());
/* 198 */         hasErrors = true;
/*     */       } 
/*     */     } 
/* 201 */     return hasErrors;
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final String id;
/*     */     
/*     */     private final String exportKey;
/*     */     
/* 207 */     private final List<TelemetryProperty<?>> properties = new ArrayList<>();
/*     */     
/*     */     private boolean isOptIn;
/*     */     
/*     */     private Builder(String id, String exportKey) {
/* 211 */       this.id = id;
/* 212 */       this.exportKey = exportKey;
/*     */     }
/*     */     
/*     */     public Builder defineAll(List<TelemetryProperty<?>> properties) {
/* 216 */       this.properties.addAll(properties);
/* 217 */       return this;
/*     */     }
/*     */     
/*     */     public <T> Builder define(TelemetryProperty<T> property) {
/* 221 */       this.properties.add(property);
/* 222 */       return this;
/*     */     }
/*     */     
/*     */     public Builder optIn() {
/* 226 */       this.isOptIn = true;
/* 227 */       return this;
/*     */     }
/*     */     
/*     */     public TelemetryEventType register() {
/* 231 */       TelemetryEventType type = new TelemetryEventType(this.id, this.exportKey, List.copyOf(this.properties), this.isOptIn);
/* 232 */       if (TelemetryEventType.REGISTRY.putIfAbsent(this.id, type) != null)
/* 233 */         throw new IllegalStateException("Duplicate TelemetryEventType with key: '" + this.id + "'"); 
/* 235 */       return type;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\telemetry\TelemetryEventType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */