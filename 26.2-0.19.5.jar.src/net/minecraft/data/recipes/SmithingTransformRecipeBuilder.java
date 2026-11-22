/*    */ package net.minecraft.data.recipes;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.triggers.Criterion;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStackTemplate;
/*    */ import net.minecraft.world.item.crafting.Ingredient;
/*    */ import net.minecraft.world.item.crafting.Recipe;
/*    */ import net.minecraft.world.item.crafting.SmithingTransformRecipe;
/*    */ 
/*    */ public class SmithingTransformRecipeBuilder {
/*    */   private final Ingredient template;
/*    */   
/*    */   private final Ingredient base;
/*    */   
/*    */   private final Ingredient addition;
/*    */   
/*    */   private final RecipeCategory category;
/*    */   
/*    */   private final ItemStackTemplate result;
/*    */   
/* 21 */   private final RecipeUnlockAdvancementBuilder advancementBuilder = new RecipeUnlockAdvancementBuilder();
/*    */   
/*    */   public SmithingTransformRecipeBuilder(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, ItemStackTemplate result) {
/* 24 */     this.category = category;
/* 25 */     this.template = template;
/* 26 */     this.base = base;
/* 27 */     this.addition = addition;
/* 28 */     this.result = result;
/*    */   }
/*    */   
/*    */   public static SmithingTransformRecipeBuilder smithing(Ingredient template, Ingredient base, Ingredient addition, RecipeCategory category, Item result) {
/* 32 */     return new SmithingTransformRecipeBuilder(template, base, addition, category, new ItemStackTemplate(result));
/*    */   }
/*    */   
/*    */   public SmithingTransformRecipeBuilder unlocks(String name, Criterion<?> criterion) {
/* 36 */     this.advancementBuilder.unlockedBy(name, criterion);
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, String id) {
/* 41 */     save(output, ResourceKey.create(Registries.RECIPE, Identifier.parse(id)));
/*    */   }
/*    */   
/*    */   public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
/* 49 */     SmithingTransformRecipe recipe = new SmithingTransformRecipe(new Recipe.CommonInfo(true), Optional.of(this.template), this.base, Optional.of(this.addition), this.result);
/* 52 */     output.accept(id, (Recipe<?>)recipe, this.advancementBuilder.build(output, id, this.category));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\SmithingTransformRecipeBuilder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */