/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import net.minecraft.advancements.triggers.Criterion;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ 
/*    */ public class CustomCraftingRecipeBuilder {
/*    */   private final RecipeCategory category;
/*    */   
/* 15 */   private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
/*    */   
/*    */   private String group;
/*    */   
/*    */   private final Factory factory;
/*    */   
/*    */   public CustomCraftingRecipeBuilder(RecipeCategory category, Factory factory) {
/* 21 */     this.category = category;
/* 22 */     this.factory = factory;
/*    */   }
/*    */   
/*    */   public static CustomCraftingRecipeBuilder customCrafting(RecipeCategory category, Factory factory) {
/* 26 */     return new CustomCraftingRecipeBuilder(category, factory);
/*    */   }
/*    */   
/*    */   public CustomCraftingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/* 30 */     this.advancementBuilder.unlockedBy(name, criterion);
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public CustomCraftingRecipeBuilder group(String group) {
/* 35 */     this.group = group;
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, String name) {
/* 40 */     save(output, ResourceKey.create(Registries.RECIPE, Identifier.parse(name)));
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 44 */     Recipe.CommonInfo commonInfo = RecipeBuilder.createCraftingCommonInfo(true);
/* 45 */     CraftingRecipe.CraftingBookInfo bookInfo = RecipeBuilder.createCraftingBookInfo(this.category, this.group);
/* 46 */     Recipe<?> recipe = this.factory.apply(commonInfo, bookInfo);
/* 47 */     output.accept(id, recipe, this.advancementBuilder.build(output, id, this.category));
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory extends BiFunction<Recipe.CommonInfo, CraftingRecipe.CraftingBookInfo, Recipe<?>> {}
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\CustomCraftingRecipeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */