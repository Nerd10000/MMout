/*    */ package net.minecraft.client.gui.screens.recipebook;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.world.item.crafting.ExtendedRecipeBookCategory;
/*    */ import net.minecraft.world.item.crafting.RecipeBookCategories;
/*    */ import net.minecraft.world.item.crafting.RecipeBookCategory;
/*    */ 
/*    */ public enum SearchRecipeBookCategory implements ExtendedRecipeBookCategory {
/* 10 */   CRAFTING(new RecipeBookCategory[] { RecipeBookCategories.CRAFTING_EQUIPMENT, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS, RecipeBookCategories.CRAFTING_MISC, RecipeBookCategories.CRAFTING_REDSTONE }),
/* 16 */   FURNACE(new RecipeBookCategory[] { RecipeBookCategories.FURNACE_FOOD, RecipeBookCategories.FURNACE_BLOCKS, RecipeBookCategories.FURNACE_MISC }),
/* 21 */   BLAST_FURNACE(new RecipeBookCategory[] { RecipeBookCategories.BLAST_FURNACE_BLOCKS, RecipeBookCategories.BLAST_FURNACE_MISC }),
/* 25 */   SMOKER(new RecipeBookCategory[] { RecipeBookCategories.SMOKER_FOOD });
/*    */   
/*    */   private final List<RecipeBookCategory> includedCategories;
/*    */   
/*    */   SearchRecipeBookCategory(RecipeBookCategory... includedCategories) {
/* 33 */     this.includedCategories = List.of(includedCategories);
/*    */   }
/*    */   
/*    */   public List<RecipeBookCategory> includedCategories() {
/* 37 */     return this.includedCategories;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\recipebook\SearchRecipeBookCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */