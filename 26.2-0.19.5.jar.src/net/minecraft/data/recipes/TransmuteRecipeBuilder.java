/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import net.minecraft.advancements.predicates.MinMaxBounds;
/*    */ import net.minecraft.advancements.triggers.Criterion;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.TransmuteRecipe;
/*    */ 
/*    */ public class TransmuteRecipeBuilder implements RecipeBuilder {
/*    */   private final RecipeCategory category;
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/*    */   private final Ingredient input;
/*    */   
/*    */   private final Ingredient material;
/*    */   
/* 18 */   private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
/*    */   
/*    */   private String group;
/*    */   
/* 20 */   private MinMaxBounds.Ints materialCount = TransmuteRecipe.DEFAULT_MATERIAL_COUNT;
/*    */   
/*    */   private boolean addMaterialCountToOutput;
/*    */   
/*    */   private TransmuteRecipeBuilder(RecipeCategory category, ItemStackTemplate result, Ingredient input, Ingredient material) {
/* 24 */     this.category = category;
/* 25 */     this.result = result;
/* 26 */     this.input = input;
/* 27 */     this.material = material;
/*    */   }
/*    */   
/*    */   public static TransmuteRecipeBuilder transmute(RecipeCategory category, Ingredient input, Ingredient material, Item result) {
/* 31 */     return transmute(category, input, material, new ItemStackTemplate(result));
/*    */   }
/*    */   
/*    */   public static TransmuteRecipeBuilder transmute(RecipeCategory category, Ingredient input, Ingredient material, ItemStackTemplate result) {
/* 35 */     return new TransmuteRecipeBuilder(category, result, input, material);
/*    */   }
/*    */   
/*    */   public TransmuteRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/* 40 */     this.advancementBuilder.unlockedBy(name, criterion);
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public TransmuteRecipeBuilder group(String group) {
/* 46 */     this.group = group;
/* 47 */     return this;
/*    */   }
/*    */   
/*    */   public TransmuteRecipeBuilder addMaterialCountToOutput() {
/* 51 */     this.addMaterialCountToOutput = true;
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public TransmuteRecipeBuilder setMaterialCount(MinMaxBounds.Ints materialCount) {
/* 56 */     this.materialCount = materialCount;
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public ResourceKey<Recipe<?>> defaultId() {
/* 62 */     return RecipeBuilder.getDefaultRecipeId((ItemInstance)this.result);
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 69 */     TransmuteRecipe recipe = new TransmuteRecipe(RecipeBuilder.createCraftingCommonInfo(true), RecipeBuilder.createCraftingBookInfo(this.category, this.group), this.input, this.material, this.materialCount, this.result, this.addMaterialCountToOutput);
/* 76 */     output.accept(id, (Recipe<?>)recipe, this.advancementBuilder.build(output, id, this.category));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\TransmuteRecipeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */