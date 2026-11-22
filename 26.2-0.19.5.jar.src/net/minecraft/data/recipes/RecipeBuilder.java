/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.advancements.triggers.Criterion;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.crafting.CraftingBookCategory;
/*    */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public interface RecipeBuilder {
/* 16 */   public static final Identifier ROOT_RECIPE_ADVANCEMENT = Identifier.withDefaultNamespace("recipes/root");
/*    */   
/*    */   RecipeBuilder unlockedBy(String paramString, Criterion<?> paramCriterion);
/*    */   
/*    */   RecipeBuilder group(String paramString);
/*    */   
/*    */   ResourceKey<Recipe<?>> defaultId();
/*    */   
/*    */   void save(RecipeOutput paramRecipeOutput, ResourceKey<Recipe<?>> paramResourceKey);
/*    */   
/*    */   default void save(RecipeOutput output) {
/* 27 */     save(output, defaultId());
/*    */   }
/*    */   
/*    */   default void save(RecipeOutput output, String id) {
/* 31 */     ResourceKey<Recipe<?>> defaultKey = defaultId();
/* 32 */     ResourceKey<Recipe<?>> overriddenKey = ResourceKey.create(Registries.RECIPE, Identifier.parse(id));
/* 33 */     if (overriddenKey == defaultKey)
/* 34 */       throw new IllegalStateException("Recipe " + id + " should remove its 'save' argument as it is equal to default one"); 
/* 36 */     save(output, overriddenKey);
/*    */   }
/*    */   
/*    */   static CraftingBookCategory determineCraftingBookCategory(RecipeCategory category) {
/* 40 */     switch (category) {
/*    */       case BUILDING_BLOCKS:
/*    */       
/*    */       case TOOLS:
/*    */       case COMBAT:
/*    */       
/*    */       case REDSTONE:
/*    */       
/*    */     } 
/* 40 */     return 
/*    */       
/* 44 */       CraftingBookCategory.MISC;
/*    */   }
/*    */   
/*    */   static Recipe.CommonInfo createCraftingCommonInfo(boolean showNotification) {
/* 49 */     return new Recipe.CommonInfo(showNotification);
/*    */   }
/*    */   
/*    */   static CraftingRecipe.CraftingBookInfo createCraftingBookInfo(RecipeCategory category, String group) {
/* 53 */     return new CraftingRecipe.CraftingBookInfo(
/* 54 */         determineCraftingBookCategory(category), 
/* 55 */         Objects.<String>requireNonNullElse(group, ""));
/*    */   }
/*    */   
/*    */   static ResourceKey<Recipe<?>> getDefaultRecipeId(ItemInstance result) {
/* 60 */     return ResourceKey.create(Registries.RECIPE, ((ResourceKey)result.typeHolder().unwrapKey().orElseThrow()).identifier());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\RecipeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */