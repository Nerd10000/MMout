/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.advancements.triggers.Criterion;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.ItemInstance;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.crafting.AbstractCookingRecipe;
/*    */ import net.minecraft.world.item.crafting.CookingBookCategory;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.level.ItemLike;
/*    */ 
/*    */ public class SimpleCookingRecipeBuilder implements RecipeBuilder {
/*    */   private final RecipeCategory craftingCategory;
/*    */   
/*    */   private final CookingBookCategory cookingCategory;
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/*    */   private final Ingredient ingredient;
/*    */   
/*    */   private final float experience;
/*    */   
/*    */   private final int cookingTime;
/*    */   
/* 26 */   private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
/*    */   
/*    */   private String group;
/*    */   
/*    */   private final AbstractCookingRecipe.Factory<?> factory;
/*    */   
/*    */   private SimpleCookingRecipeBuilder(RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemStackTemplate result, Ingredient ingredient, float experience, int cookingTime, AbstractCookingRecipe.Factory<?> factory) {
/* 31 */     this.craftingCategory = craftingCategory;
/* 32 */     this.cookingCategory = cookingCategory;
/* 33 */     this.result = result;
/* 34 */     this.ingredient = ingredient;
/* 35 */     this.experience = experience;
/* 36 */     this.cookingTime = cookingTime;
/* 37 */     this.factory = factory;
/*    */   }
/*    */   
/*    */   private SimpleCookingRecipeBuilder(RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, Ingredient ingredient, float experience, int cookingTime, AbstractCookingRecipe.Factory<?> factory) {
/* 41 */     this(craftingCategory, cookingCategory, new ItemStackTemplate(result.asItem()), ingredient, experience, cookingTime, factory);
/*    */   }
/*    */   
/*    */   public static <T extends AbstractCookingRecipe> SimpleCookingRecipeBuilder generic(Ingredient ingredient, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime, AbstractCookingRecipe.Factory<T> factory) {
/* 45 */     return new SimpleCookingRecipeBuilder(craftingCategory, cookingCategory, result, ingredient, experience, cookingTime, factory);
/*    */   }
/*    */   
/*    */   public static SimpleCookingRecipeBuilder campfireCooking(Ingredient ingredient, RecipeCategory craftingCategory, ItemLike result, float experience, int cookingTime) {
/* 49 */     return new SimpleCookingRecipeBuilder(craftingCategory, CookingBookCategory.FOOD, result, ingredient, experience, cookingTime, net.minecraft.world.item.crafting.CampfireCookingRecipe::new);
/*    */   }
/*    */   
/*    */   public static SimpleCookingRecipeBuilder blasting(Ingredient ingredient, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime) {
/* 53 */     return new SimpleCookingRecipeBuilder(craftingCategory, cookingCategory, result, ingredient, experience, cookingTime, net.minecraft.world.item.crafting.BlastingRecipe::new);
/*    */   }
/*    */   
/*    */   public static SimpleCookingRecipeBuilder smelting(Ingredient ingredient, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime) {
/* 57 */     return new SimpleCookingRecipeBuilder(craftingCategory, cookingCategory, result, ingredient, experience, cookingTime, net.minecraft.world.item.crafting.SmeltingRecipe::new);
/*    */   }
/*    */   
/*    */   public static SimpleCookingRecipeBuilder smoking(Ingredient ingredient, RecipeCategory craftingCategory, ItemLike result, float experience, int cookingTime) {
/* 61 */     return new SimpleCookingRecipeBuilder(craftingCategory, CookingBookCategory.FOOD, result, ingredient, experience, cookingTime, net.minecraft.world.item.crafting.SmokingRecipe::new);
/*    */   }
/*    */   
/*    */   public SimpleCookingRecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
/* 66 */     this.advancementBuilder.unlockedBy(name, criterion);
/* 67 */     return this;
/*    */   }
/*    */   
/*    */   public SimpleCookingRecipeBuilder group(String group) {
/* 72 */     this.group = group;
/* 73 */     return this;
/*    */   }
/*    */   
/*    */   public ResourceKey<Recipe<?>> defaultId() {
/* 78 */     return RecipeBuilder.getDefaultRecipeId((ItemInstance)this.result);
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 83 */     AbstractCookingRecipe recipe = this.factory.create(
/* 84 */         RecipeBuilder.createCraftingCommonInfo(true), new AbstractCookingRecipe.CookingBookInfo(this.cookingCategory, 
/* 85 */           Objects.<String>requireNonNullElse(this.group, "")), this.ingredient, this.result, this.experience, this.cookingTime);
/* 91 */     output.accept(id, (Recipe<?>)recipe, this.advancementBuilder.build(output, id, this.craftingCategory));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\SimpleCookingRecipeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */