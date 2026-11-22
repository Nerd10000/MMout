/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import net.minecraft.recipebook.ServerPlaceRecipe;
/*    */ import net.minecraft.world.entity.player.StackedItemContents;
/*    */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*    */ import net.minecraft.world.item.crafting.RecipeHolder;
/*    */ import net.minecraft.world.item.crafting.RecipeInput;
/*    */ 
/*    */ class null implements ServerPlaceRecipe.CraftingMenuAccess<CraftingRecipe> {
/*    */   public void fillCraftSlotsStackedContents(StackedItemContents stackedContents) {
/* 50 */     AbstractCraftingMenu.this.fillCraftSlotsStackedContents(stackedContents);
/*    */   }
/*    */   
/*    */   public void clearCraftingContent() {
/* 55 */     AbstractCraftingMenu.this.resultSlots.clearContent();
/* 56 */     AbstractCraftingMenu.this.craftSlots.clearContent();
/*    */   }
/*    */   
/*    */   public boolean recipeMatches(RecipeHolder<CraftingRecipe> recipe) {
/* 61 */     return ((CraftingRecipe)recipe.value()).matches((RecipeInput)AbstractCraftingMenu.this.craftSlots.asCraftInput(), AbstractCraftingMenu.this.owner().level());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\AbstractCraftingMenu$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */