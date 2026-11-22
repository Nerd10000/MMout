/*     */ package net.minecraft.advancements;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ 
/*     */ public class Builder {
/*     */   private int experience;
/*     */   
/*  78 */   private final ImmutableList.Builder<ResourceKey<LootTable>> loot = ImmutableList.builder();
/*     */   
/*  79 */   private final ImmutableList.Builder<ResourceKey<Recipe<?>>> recipes = ImmutableList.builder();
/*     */   
/*  80 */   private Optional<Identifier> function = Optional.empty();
/*     */   
/*     */   public static Builder experience(int amount) {
/*  83 */     return (new Builder()).addExperience(amount);
/*     */   }
/*     */   
/*     */   public Builder addExperience(int amount) {
/*  87 */     this.experience += amount;
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public static Builder loot(ResourceKey<LootTable> id) {
/*  92 */     return (new Builder()).addLootTable(id);
/*     */   }
/*     */   
/*     */   public Builder addLootTable(ResourceKey<LootTable> id) {
/*  96 */     this.loot.add(id);
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public static Builder recipe(ResourceKey<Recipe<?>> id) {
/* 101 */     return (new Builder()).addRecipe(id);
/*     */   }
/*     */   
/*     */   public Builder addRecipe(ResourceKey<Recipe<?>> id) {
/* 105 */     this.recipes.add(id);
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public static Builder function(Identifier id) {
/* 110 */     return (new Builder()).runs(id);
/*     */   }
/*     */   
/*     */   public Builder runs(Identifier function) {
/* 114 */     this.function = Optional.of(function);
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public AdvancementRewards build() {
/* 119 */     return new AdvancementRewards(this.experience, (List<ResourceKey<LootTable>>)this.loot.build(), (List<ResourceKey<Recipe<?>>>)this.recipes.build(), this.function.map(net.minecraft.commands.CacheableFunction::new));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\advancements\AdvancementRewards$Builder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */