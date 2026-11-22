/*     */ package net.minecraft.world.level.gamerules;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
/*     */ 
/*     */ public class Builder {
/* 130 */   private final Reference2ObjectMap<GameRule<?>, Object> map = (Reference2ObjectMap<GameRule<?>, Object>)new Reference2ObjectOpenHashMap();
/*     */   
/*     */   public <T> Builder set(GameRule<T> gameRule, T value) {
/* 133 */     this.map.put(gameRule, value);
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public GameRuleMap build() {
/* 138 */     return new GameRuleMap(this.map);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\gamerules\GameRuleMap$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */