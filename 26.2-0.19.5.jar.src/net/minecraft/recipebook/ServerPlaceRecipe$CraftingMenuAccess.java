package net.minecraft.recipebook;

import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.crafting.RecipeHolder;

public interface CraftingMenuAccess<T extends net.minecraft.world.item.crafting.Recipe<?>> {
  void fillCraftSlotsStackedContents(StackedItemContents paramStackedItemContents);
  
  void clearCraftingContent();
  
  boolean recipeMatches(RecipeHolder<T> paramRecipeHolder);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\recipebook\ServerPlaceRecipe$CraftingMenuAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */