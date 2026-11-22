package net.minecraft.world.item.crafting;

import net.minecraft.resources.ResourceKey;

public interface RecipeAccess {
  RecipePropertySet propertySet(ResourceKey<RecipePropertySet> paramResourceKey);
  
  SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */