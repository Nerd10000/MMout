/*     */ package net.minecraft.advancements.predicates.entity;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.advancements.predicates.FoodPredicate;
/*     */ import net.minecraft.advancements.predicates.GameTypePredicate;
/*     */ import net.minecraft.advancements.predicates.InputPredicate;
/*     */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.stats.StatType;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ 
/*     */ public class Builder {
/* 195 */   private MinMaxBounds.Ints level = MinMaxBounds.Ints.ANY;
/*     */   
/* 196 */   private FoodPredicate food = FoodPredicate.ANY;
/*     */   
/* 197 */   private GameTypePredicate gameType = GameTypePredicate.ANY;
/*     */   
/* 198 */   private final ImmutableList.Builder<PlayerPredicate.StatMatcher<?>> stats = ImmutableList.builder();
/*     */   
/* 199 */   private final Object2BooleanMap<ResourceKey<Recipe<?>>> recipes = (Object2BooleanMap<ResourceKey<Recipe<?>>>)new Object2BooleanOpenHashMap();
/*     */   
/* 200 */   private final Map<Identifier, PlayerPredicate.AdvancementPredicate> advancements = Maps.newHashMap();
/*     */   
/* 201 */   private Optional<EntityPredicate> lookingAt = Optional.empty();
/*     */   
/* 202 */   private Optional<InputPredicate> input = Optional.empty();
/*     */   
/*     */   public static Builder player() {
/* 205 */     return new Builder();
/*     */   }
/*     */   
/*     */   public Builder setLevel(MinMaxBounds.Ints level) {
/* 209 */     this.level = level;
/* 210 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setFood(FoodPredicate food) {
/* 214 */     this.food = food;
/* 215 */     return this;
/*     */   }
/*     */   
/*     */   public <T> Builder addStat(StatType<T> type, Holder.Reference<T> value, MinMaxBounds.Ints range) {
/* 219 */     this.stats.add(new PlayerPredicate.StatMatcher<>(type, (Holder<T>)value, range));
/* 220 */     return this;
/*     */   }
/*     */   
/*     */   public Builder addRecipe(ResourceKey<Recipe<?>> recipe, boolean present) {
/* 224 */     this.recipes.put(recipe, present);
/* 225 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setGameType(GameTypePredicate gameType) {
/* 229 */     this.gameType = gameType;
/* 230 */     return this;
/*     */   }
/*     */   
/*     */   public Builder setLookingAt(EntityPredicate.Builder lookingAt) {
/* 234 */     this.lookingAt = Optional.of(lookingAt.build());
/* 235 */     return this;
/*     */   }
/*     */   
/*     */   public Builder checkAdvancementDone(Identifier advancement, boolean isDone) {
/* 239 */     this.advancements.put(advancement, new PlayerPredicate.AdvancementDonePredicate(isDone));
/* 240 */     return this;
/*     */   }
/*     */   
/*     */   public Builder checkAdvancementCriterions(Identifier advancement, Map<String, Boolean> criterions) {
/* 244 */     this.advancements.put(advancement, new PlayerPredicate.AdvancementCriterionsPredicate((Object2BooleanMap<String>)new Object2BooleanOpenHashMap(criterions)));
/* 245 */     return this;
/*     */   }
/*     */   
/*     */   public Builder hasInput(InputPredicate input) {
/* 249 */     this.input = Optional.of(input);
/* 250 */     return this;
/*     */   }
/*     */   
/*     */   public PlayerPredicate build() {
/* 254 */     return new PlayerPredicate(this.level, this.food, this.gameType, (List<PlayerPredicate.StatMatcher<?>>)this.stats.build(), this.recipes, this.advancements, this.lookingAt, this.input);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\predicates\entity\PlayerPredicate$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */