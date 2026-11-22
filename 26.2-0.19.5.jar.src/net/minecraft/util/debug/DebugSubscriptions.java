/*     */ package net.minecraft.util.debug;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ 
/*     */ public class DebugSubscriptions<T> {
/*  75 */   public static final DebugSubscription<?> DEDICATED_SERVER_TICK_TIME = registerSimple("dedicated_server_tick_time");
/*     */   
/*  78 */   public static final DebugSubscription<DebugBeeInfo> BEES = registerWithValue("bees", (StreamCodec)DebugBeeInfo.STREAM_CODEC);
/*     */   
/*  79 */   public static final DebugSubscription<DebugBrainDump> BRAINS = registerWithValue("brains", (StreamCodec)DebugBrainDump.STREAM_CODEC);
/*     */   
/*  80 */   public static final DebugSubscription<DebugBreezeInfo> BREEZES = registerWithValue("breezes", (StreamCodec)DebugBreezeInfo.STREAM_CODEC);
/*     */   
/*  81 */   public static final DebugSubscription<DebugGoalInfo> GOAL_SELECTORS = registerWithValue("goal_selectors", (StreamCodec)DebugGoalInfo.STREAM_CODEC);
/*     */   
/*  82 */   public static final DebugSubscription<DebugPathInfo> ENTITY_PATHS = registerWithValue("entity_paths", (StreamCodec)DebugPathInfo.STREAM_CODEC);
/*     */   
/*  85 */   public static final DebugSubscription<DebugEntityBlockIntersection> ENTITY_BLOCK_INTERSECTIONS = registerTemporaryValue("entity_block_intersections", (StreamCodec)DebugEntityBlockIntersection.STREAM_CODEC, 100);
/*     */   
/*  86 */   public static final DebugSubscription<DebugHiveInfo> BEE_HIVES = registerWithValue("bee_hives", DebugHiveInfo.STREAM_CODEC);
/*     */   
/*  87 */   public static final DebugSubscription<DebugPoiInfo> POIS = registerWithValue("pois", DebugPoiInfo.STREAM_CODEC);
/*     */   
/*  88 */   public static final DebugSubscription<Orientation> REDSTONE_WIRE_ORIENTATIONS = registerTemporaryValue("redstone_wire_orientations", Orientation.STREAM_CODEC, 200);
/*     */   
/*  89 */   public static final DebugSubscription<Unit> VILLAGE_SECTIONS = registerWithValue("village_sections", Unit.STREAM_CODEC);
/*     */   
/*  92 */   public static final DebugSubscription<List<BlockPos>> RAIDS = registerWithValue("raids", BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()));
/*     */   
/*  93 */   public static final DebugSubscription<List<DebugStructureInfo>> STRUCTURES = registerWithValue("structures", DebugStructureInfo.STREAM_CODEC.apply(ByteBufCodecs.list()));
/*     */   
/*  96 */   public static final DebugSubscription<DebugGameEventListenerInfo> GAME_EVENT_LISTENERS = registerWithValue("game_event_listeners", DebugGameEventListenerInfo.STREAM_CODEC);
/*     */   
/*  99 */   public static final DebugSubscription<BlockPos> NEIGHBOR_UPDATES = registerTemporaryValue("neighbor_updates", BlockPos.STREAM_CODEC, 200);
/*     */   
/* 100 */   public static final DebugSubscription<DebugGameEventInfo> GAME_EVENTS = registerTemporaryValue("game_events", DebugGameEventInfo.STREAM_CODEC, 60);
/*     */   
/*     */   public static DebugSubscription<?> bootstrap(Registry<DebugSubscription<?>> registry) {
/* 103 */     return DEDICATED_SERVER_TICK_TIME;
/*     */   }
/*     */   
/*     */   private static DebugSubscription<?> registerSimple(String id) {
/* 107 */     return (DebugSubscription)Registry.register(BuiltInRegistries.DEBUG_SUBSCRIPTION, Identifier.withDefaultNamespace(id), new DebugSubscription(null));
/*     */   }
/*     */   
/*     */   private static <T> DebugSubscription<T> registerWithValue(String id, StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec) {
/* 111 */     return (DebugSubscription<T>)Registry.register(BuiltInRegistries.DEBUG_SUBSCRIPTION, Identifier.withDefaultNamespace(id), new DebugSubscription<>(valueStreamCodec));
/*     */   }
/*     */   
/*     */   private static <T> DebugSubscription<T> registerTemporaryValue(String id, StreamCodec<? super RegistryFriendlyByteBuf, T> valueStreamCodec, int expireAfterTicks) {
/* 115 */     return (DebugSubscription<T>)Registry.register(BuiltInRegistries.DEBUG_SUBSCRIPTION, Identifier.withDefaultNamespace(id), new DebugSubscription<>(valueStreamCodec, expireAfterTicks));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debug\DebugSubscriptions.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */