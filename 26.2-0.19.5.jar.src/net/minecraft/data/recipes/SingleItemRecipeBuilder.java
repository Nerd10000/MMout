/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import net.minecraft.advancements.triggers.Criterion;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.SingleItemRecipe;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public class SingleItemRecipeBuilder implements RecipeBuilder {
/*    */   private final RecipeCategory category;
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/*    */   private final Ingredient ingredient;
/*    */   
/* 17 */   private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
/*    */   
/*    */   private final SingleItemRecipe.Factory<?> factory;
/*    */   
/*    */   private SingleItemRecipeBuilder(RecipeCategory category, SingleItemRecipe.Factory<?> factory, Ingredient ingredient, ItemStackTemplate result) {
/* 21 */     this.category = category;
/* 22 */     this.result = result;
/* 23 */     this.ingredient = ingredient;
/* 24 */     this.factory = factory;
/*    */   }
/*    */   
/*    */   public SingleItemRecipeBuilder(RecipeCategory category, SingleItemRecipe.Factory<?> factory, Ingredient ingredient, ItemLike result, int count) {
/* 28 */     this(category, factory, ingredient, new ItemStackTemplate(result.asItem(), count));
/*    */   }
/*    */   
/*    */   public static SingleItemRecipeBuilder stonecutting(Ingredient ingredient, RecipeCategory category, ItemLike result, int count) {
/* 33 */     return new SingleItemRecipeBuilder(category, net.minecraft.world.item.crafting.StonecutterRecipe::new, ingredient, result, count);
/*    */   }
/*    */   
/*    */   public SingleItemRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/* 38 */     this.advancementBuilder.unlockedBy(name, criterion);
/* 39 */     return this;
/*    */   }
/*    */   
/*    */   public SingleItemRecipeBuilder group(String group) {
/* 45 */     return this;
/*    */   }
/*    */   
/*    */   public ResourceKey<Recipe<?>> defaultId() {
/* 50 */     return RecipeBuilder.getDefaultRecipeId((ItemInstance)this.result);
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 55 */     SingleItemRecipe recipe = this.factory.create(new Recipe.CommonInfo(true), this.ingredient, this.result);
/* 60 */     output.accept(id, (Recipe<?>)recipe, this.advancementBuilder.build(output, id, this.category));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\SingleItemRecipeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */