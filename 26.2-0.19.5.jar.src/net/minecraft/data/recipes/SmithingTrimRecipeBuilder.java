/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import net.minecraft.advancements.triggers.Criterion;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.SmithingTrimRecipe;
/*    */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*    */ 
/*    */ public class SmithingTrimRecipeBuilder {
/*    */   private final RecipeCategory category;
/*    */   
/*    */   private final Ingredient template;
/*    */   
/*    */   private final Ingredient base;
/*    */   
/*    */   private final Ingredient addition;
/*    */   
/*    */   private final Holder<TrimPattern> pattern;
/*    */   
/* 17 */   private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
/*    */   
/*    */   public SmithingTrimRecipeBuilder(RecipeCategory category, Ingredient template, Ingredient base, Ingredient addition, Holder<TrimPattern> pattern) {
/* 20 */     this.category = category;
/* 21 */     this.template = template;
/* 22 */     this.base = base;
/* 23 */     this.addition = addition;
/* 24 */     this.pattern = pattern;
/*    */   }
/*    */   
/*    */   public static SmithingTrimRecipeBuilder smithingTrim(Ingredient template, Ingredient base, Ingredient addition, Holder<TrimPattern> pattern, RecipeCategory category) {
/* 28 */     return new SmithingTrimRecipeBuilder(category, template, base, addition, pattern);
/*    */   }
/*    */   
/*    */   public SmithingTrimRecipeBuilder unlocks(String name, Criterion<?> criterion) {
/* 32 */     this.advancementBuilder.unlockedBy(name, criterion);
/* 33 */     return this;
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 37 */     SmithingTrimRecipe recipe = new SmithingTrimRecipe(new Recipe.CommonInfo(true), this.template, this.base, this.addition, this.pattern);
/* 44 */     output.accept(id, (Recipe<?>)recipe, this.advancementBuilder.build(output, id, this.category));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\SmithingTrimRecipeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */