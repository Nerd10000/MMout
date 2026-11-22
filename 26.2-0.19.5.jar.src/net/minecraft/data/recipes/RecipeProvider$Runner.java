/*     */ package net.minecraft.data.recipes;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import net.minecraft.advancements.Advancement;
/*     */ import net.minecraft.advancements.AdvancementHolder;
/*     */ import net.minecraft.advancements.CriterionTriggerInstance;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.advancements.triggers.ImpossibleTrigger;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.world.item.crafting.Recipe;
/*     */ 
/*     */ public abstract class Runner implements DataProvider {
/*     */   private final PackOutput packOutput;
/*     */   
/*     */   private final CompletableFuture<HolderLookup.Provider> registries;
/*     */   
/*     */   protected Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
/*  85 */     this.packOutput = packOutput;
/*  86 */     this.registries = registries;
/*     */   }
/*     */   
/*     */   public final CompletableFuture<?> run(final CachedOutput cache) {
/*  91 */     return this.registries.thenCompose(registries -> {
/*     */           final PackOutput.PathProvider recipePathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.RECIPE);
/*     */           final PackOutput.PathProvider advancementPathProvider = this.packOutput.createRegistryElementsPathProvider(Registries.ADVANCEMENT);
/*     */           final Set<ResourceKey<Recipe<?>>> allRecipes = Sets.newHashSet();
/*     */           final List<CompletableFuture<?>> tasks = new ArrayList<>();
/*     */           RecipeOutput recipeOutput = new RecipeOutput(this) {
/*     */               public void accept(ResourceKey<Recipe<?>> id, Recipe<?> recipe, AdvancementHolder advancementHolder) {
/* 101 */                 if (!allRecipes.add(id))
/* 102 */                   throw new IllegalStateException("Duplicate recipe " + String.valueOf(id.identifier())); 
/* 104 */                 saveRecipe(id, recipe);
/* 105 */                 if (advancementHolder != null)
/* 106 */                   saveAdvancement(advancementHolder); 
/*     */               }
/*     */               
/*     */               public Advancement.Builder advancement() {
/* 112 */                 return Advancement.Builder.recipeAdvancement().parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/*     */               }
/*     */               
/*     */               public void includeRootAdvancement() {
/* 117 */                 AdvancementHolder root = Advancement.Builder.recipeAdvancement().addCriterion("impossible", CriteriaTriggers.IMPOSSIBLE.createCriterion((CriterionTriggerInstance)new ImpossibleTrigger.TriggerInstance())).build(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT);
/* 118 */                 saveAdvancement(root);
/*     */               }
/*     */               
/*     */               private void saveRecipe(ResourceKey<Recipe<?>> id, Recipe<?> recipe) {
/* 122 */                 tasks.add(DataProvider.saveStable(cache, registries, Recipe.CODEC, recipe, recipePathProvider.json(id.identifier())));
/*     */               }
/*     */               
/*     */               private void saveAdvancement(AdvancementHolder advancementHolder) {
/* 126 */                 tasks.add(DataProvider.saveStable(cache, registries, Advancement.CODEC, advancementHolder.value(), advancementPathProvider.json(advancementHolder.id())));
/*     */               }
/*     */             };
/*     */           createRecipeProvider(registries, recipeOutput).buildRecipes();
/*     */           return CompletableFuture.allOf((CompletableFuture<?>[])tasks.toArray(()));
/*     */         });
/*     */   }
/*     */   
/*     */   protected abstract RecipeProvider createRecipeProvider(HolderLookup.Provider paramProvider, RecipeOutput paramRecipeOutput);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\RecipeProvider$Runner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */