/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFixer;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.util.datafix.fixes.References;
/*    */ 
/*    */ public enum DataFixTypes {
/* 18 */   LEVEL(References.LEVEL),
/* 19 */   LEVEL_SUMMARY(References.LIGHTWEIGHT_LEVEL),
/* 20 */   PLAYER(References.PLAYER),
/* 21 */   CHUNK(References.CHUNK),
/* 22 */   HOTBAR(References.HOTBAR),
/* 23 */   OPTIONS(References.OPTIONS),
/* 24 */   STRUCTURE(References.STRUCTURE),
/* 25 */   STATS(References.STATS),
/* 26 */   SAVED_DATA_COMMAND_STORAGE(References.SAVED_DATA_COMMAND_STORAGE),
/* 27 */   SAVED_DATA_CUSTOM_BOSS_EVENTS(References.SAVED_DATA_CUSTOM_BOSS_EVENTS),
/* 28 */   SAVED_DATA_ENDER_DRAGON_FIGHT(References.SAVED_DATA_ENDER_DRAGON_FIGHT),
/* 29 */   SAVED_DATA_GAME_RULES(References.SAVED_DATA_GAME_RULES),
/* 30 */   SAVED_DATA_FORCED_CHUNKS(References.SAVED_DATA_TICKETS),
/* 31 */   SAVED_DATA_MAP_DATA(References.SAVED_DATA_MAP_DATA),
/* 32 */   SAVED_DATA_MAP_INDEX(References.SAVED_DATA_MAP_INDEX),
/* 33 */   SAVED_DATA_RAIDS(References.SAVED_DATA_RAIDS),
/* 34 */   SAVED_DATA_RANDOM_SEQUENCES(References.SAVED_DATA_RANDOM_SEQUENCES),
/* 35 */   SAVED_DATA_SCHEDULED_EVENTS(References.SAVED_DATA_SCHEDULED_EVENTS),
/* 36 */   SAVED_DATA_SCOREBOARD(References.SAVED_DATA_SCOREBOARD),
/* 37 */   SAVED_DATA_STOPWATCHES(References.SAVED_DATA_STOPWATCHES),
/* 38 */   SAVED_DATA_STRUCTURE_FEATURE_INDICES(References.SAVED_DATA_STRUCTURE_FEATURE_INDICES),
/* 39 */   SAVED_DATA_WANDERING_TRADER(References.SAVED_DATA_WANDERING_TRADER),
/* 40 */   SAVED_DATA_WEATHER(References.SAVED_DATA_WEATHER),
/* 41 */   SAVED_DATA_WORLD_BORDER(References.SAVED_DATA_WORLD_BORDER),
/* 42 */   SAVED_DATA_WORLD_CLOCKS(References.SAVED_DATA_WORLD_CLOCKS),
/* 43 */   SAVED_DATA_WORLD_GEN_SETTINGS(References.SAVED_DATA_WORLD_GEN_SETTINGS),
/* 44 */   ADVANCEMENTS(References.ADVANCEMENTS),
/* 45 */   POI_CHUNK(References.POI_CHUNK),
/* 46 */   WORLD_GEN_SETTINGS(References.WORLD_GEN_SETTINGS),
/* 47 */   ENTITY_CHUNK(References.ENTITY_CHUNK),
/* 48 */   DEBUG_PROFILE(References.DEBUG_PROFILE);
/*    */   
/*    */   public static final Set<DSL.TypeReference> TYPES_FOR_LEVEL_LIST;
/*    */   
/*    */   private final DSL.TypeReference type;
/*    */   
/*    */   static {
/* 51 */     TYPES_FOR_LEVEL_LIST = Set.of(LEVEL_SUMMARY.type);
/*    */   }
/*    */   
/*    */   DataFixTypes(DSL.TypeReference type) {
/* 56 */     this.type = type;
/*    */   }
/*    */   
/*    */   private static int currentVersion() {
/* 60 */     return SharedConstants.getCurrentVersion().dataVersion().version();
/*    */   }
/*    */   
/*    */   public <A> Codec<A> wrapCodec(final Codec<A> codec, final DataFixer dataFixer, final int defaultVersion) {
/* 64 */     return new Codec<A>() {
/*    */         public <T> DataResult<T> encode(A input, DynamicOps<T> ops, T prefix) {
/* 67 */           return codec.encode(input, ops, prefix).flatMap(data -> ops.mergeToMap(data, ops.createString("DataVersion"), ops.createInt(DataFixTypes.currentVersion())));
/*    */         }
/*    */         
/*    */         public <T> DataResult<Pair<A, T>> decode(DynamicOps<T> ops, T input) {
/* 75 */           Objects.requireNonNull(ops);
/* 78 */           int fromVersion = ((Integer)ops.get(input, "DataVersion").flatMap(ops::getNumberValue).map(Number::intValue).result().orElse(Integer.valueOf(defaultVersion))).intValue();
/* 79 */           Dynamic<T> dataWithoutVersion = new Dynamic(ops, ops.remove(input, "DataVersion"));
/* 80 */           Dynamic<T> fixedData = DataFixTypes.this.updateToCurrentVersion(dataFixer, dataWithoutVersion, fromVersion);
/* 81 */           return codec.decode(fixedData);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public <T> Dynamic<T> update(DataFixer fixerUpper, Dynamic<T> input, int fromVersion, int toVersion) {
/* 87 */     return fixerUpper.update(this.type, input, fromVersion, toVersion);
/*    */   }
/*    */   
/*    */   public <T> Dynamic<T> updateToCurrentVersion(DataFixer fixerUpper, Dynamic<T> input, int dataVersion) {
/* 91 */     return update(fixerUpper, input, dataVersion, currentVersion());
/*    */   }
/*    */   
/*    */   public CompoundTag update(DataFixer fixer, CompoundTag tag, int fromVersion, int toVersion) {
/* 95 */     return (CompoundTag)update(fixer, new Dynamic((DynamicOps)NbtOps.INSTANCE, tag), fromVersion, toVersion).getValue();
/*    */   }
/*    */   
/*    */   public CompoundTag updateToCurrentVersion(DataFixer fixer, CompoundTag tag, int fromVersion) {
/* 99 */     return update(fixer, tag, fromVersion, currentVersion());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\datafix\DataFixTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */