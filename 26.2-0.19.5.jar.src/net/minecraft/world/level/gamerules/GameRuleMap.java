/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ 
/*     */ public final class GameRuleMap extends SavedData {
/*  20 */   public static final Codec<GameRuleMap> CODEC = Codec.dispatchedMap(BuiltInRegistries.GAME_RULE.byNameCodec(), GameRule::valueCodec).xmap(GameRuleMap::ofTrusted, GameRuleMap::map);
/*     */   
/*  22 */   public static final SavedDataType<GameRuleMap> TYPE = new SavedDataType(
/*  23 */       Identifier.withDefaultNamespace("game_rules"), GameRuleMap::of, CODEC, DataFixTypes.SAVED_DATA_GAME_RULES);
/*     */   
/*     */   private final Reference2ObjectMap<GameRule<?>, Object> map;
/*     */   
/*     */   private GameRuleMap(Reference2ObjectMap<GameRule<?>, Object> map) {
/*  32 */     this.map = map;
/*     */   }
/*     */   
/*     */   private static GameRuleMap ofTrusted(Map<GameRule<?>, Object> map) {
/*  36 */     return new GameRuleMap((Reference2ObjectMap<GameRule<?>, Object>)new Reference2ObjectOpenHashMap(map));
/*     */   }
/*     */   
/*     */   public static GameRuleMap of() {
/*  40 */     return new GameRuleMap((Reference2ObjectMap<GameRule<?>, Object>)new Reference2ObjectOpenHashMap());
/*     */   }
/*     */   
/*     */   public static GameRuleMap of(Stream<GameRule<?>> gameRuleTypeStream) {
/*  44 */     Reference2ObjectOpenHashMap<GameRule<?>, Object> map = new Reference2ObjectOpenHashMap();
/*  45 */     gameRuleTypeStream.forEach(gameRule -> map.put(gameRule, gameRule.defaultValue()));
/*  46 */     return new GameRuleMap((Reference2ObjectMap<GameRule<?>, Object>)map);
/*     */   }
/*     */   
/*     */   public static GameRuleMap copyOf(GameRuleMap gameRuleMap) {
/*  50 */     return new GameRuleMap((Reference2ObjectMap<GameRule<?>, Object>)new Reference2ObjectOpenHashMap(gameRuleMap.map));
/*     */   }
/*     */   
/*     */   public boolean has(GameRule<?> gameRule) {
/*  54 */     return this.map.containsKey(gameRule);
/*     */   }
/*     */   
/*     */   public <T> T get(GameRule<T> gameRule) {
/*  59 */     return (T)this.map.get(gameRule);
/*     */   }
/*     */   
/*     */   public <T> void set(GameRule<T> gameRule, T value) {
/*  63 */     setDirty();
/*  64 */     this.map.put(gameRule, value);
/*     */   }
/*     */   
/*     */   public <T> void reset(GameRule<T> gameRule) {
/*  68 */     set(gameRule, gameRule.defaultValue());
/*     */   }
/*     */   
/*     */   public <T> T remove(GameRule<T> gameRule) {
/*  73 */     setDirty();
/*  74 */     return (T)this.map.remove(gameRule);
/*     */   }
/*     */   
/*     */   public Set<GameRule<?>> keySet() {
/*  78 */     return (Set<GameRule<?>>)this.map.keySet();
/*     */   }
/*     */   
/*     */   public int size() {
/*  82 */     return this.map.size();
/*     */   }
/*     */   
/*     */   public String toString() {
/*  87 */     return this.map.toString();
/*     */   }
/*     */   
/*     */   public GameRuleMap withOther(GameRuleMap other) {
/*  91 */     GameRuleMap result = copyOf(this);
/*  92 */     result.setFromIf(other, r -> true);
/*  93 */     return result;
/*     */   }
/*     */   
/*     */   public void setFromIf(GameRuleMap other, Predicate<GameRule<?>> predicate) {
/*  97 */     for (GameRule<?> gameRule : other.keySet()) {
/*  98 */       if (predicate.test(gameRule))
/*  99 */         setGameRule(other, gameRule, this); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> void setGameRule(GameRuleMap other, GameRule<T> gameRule, GameRuleMap result) {
/* 105 */     result.set(gameRule, Objects.requireNonNull(other.get(gameRule)));
/*     */   }
/*     */   
/*     */   private Reference2ObjectMap<GameRule<?>, Object> map() {
/* 109 */     return this.map;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj) {
/* 114 */     if (obj == this)
/* 115 */       return true; 
/* 117 */     if (obj == null || obj.getClass() != getClass())
/* 118 */       return false; 
/* 120 */     GameRuleMap that = (GameRuleMap)obj;
/* 121 */     return Objects.equals(this.map, that.map);
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 126 */     return Objects.hash(new Object[] { this.map });
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 130 */     private final Reference2ObjectMap<GameRule<?>, Object> map = (Reference2ObjectMap<GameRule<?>, Object>)new Reference2ObjectOpenHashMap();
/*     */     
/*     */     public <T> Builder set(GameRule<T> gameRule, T value) {
/* 133 */       this.map.put(gameRule, value);
/* 134 */       return this;
/*     */     }
/*     */     
/*     */     public GameRuleMap build() {
/* 138 */       return new GameRuleMap(this.map);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gamerules\GameRuleMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */