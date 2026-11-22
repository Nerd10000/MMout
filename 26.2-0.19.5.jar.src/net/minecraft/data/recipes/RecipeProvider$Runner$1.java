/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.CriterionTriggerInstance;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.advancements.triggers.ImpossibleTrigger;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ 
/*     */ class null implements RecipeOutput {
/*     */   null(RecipeProvider.Runner this$0) {}
/*     */   
/*     */   public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, AdvancementHolder advancementHolder) {
/* 101 */     if (!allRecipes.add(id))
/* 102 */       throw new IllegalStateException("Duplicate recipe " + String.valueOf(id.identifier())); 
/* 104 */     saveRecipe(id, recipe);
/* 105 */     if (advancementHolder != null)
/* 106 */       saveAdvancement(advancementHolder); 
/*     */   }
/*     */   
/*     */   public Advancement.Builder advancement() {
/* 112 */     return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/*     */   }
/*     */   
/*     */   public void includeRootAdvancement() {
/* 117 */     AdvancementHolder root = Advancement.Builder.recipeAdvancement().addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion((CriterionTriggerInstance)new ImpossibleTrigger.TriggerInstance())).build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/* 118 */     saveAdvancement(root);
/*     */   }
/*     */   
/*     */   private void saveRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe) {
/* 122 */     tasks.add(DataProvider.saveStable(cache, registries, Recipe.CODEC, recipe, recipePathProvider.json(id.identifier())));
/*     */   }
/*     */   
/*     */   private void saveAdvancement(AdvancementHolder advancementHolder) {
/* 126 */     tasks.add(DataProvider.saveStable(cache, registries, Advancement.CODEC, advancementHolder.value(), advancementPathProvider.json(advancementHolder.id())));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\RecipeProvider$Runner$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */