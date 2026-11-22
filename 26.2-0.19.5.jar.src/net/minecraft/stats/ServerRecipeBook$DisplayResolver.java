package net.minecraft.stats;

import java.util.function.Consumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;

@FunctionalInterface
public interface DisplayResolver {
  void displaysForRecipe(ResourceKey<Recipe<?>> paramResourceKey, Consumer<RecipeDisplayEntry> paramConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\stats\ServerRecipeBook$DisplayResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */