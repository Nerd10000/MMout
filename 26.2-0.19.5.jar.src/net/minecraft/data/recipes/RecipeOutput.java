package net.minecraft.data.recipes;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

public interface RecipeOutput {
  void accept(ResourceKey<Recipe<?>> paramResourceKey, Recipe<?> paramRecipe, AdvancementHolder paramAdvancementHolder);
  
  Advancement.Builder advancement();
  
  void includeRootAdvancement();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\RecipeOutput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */