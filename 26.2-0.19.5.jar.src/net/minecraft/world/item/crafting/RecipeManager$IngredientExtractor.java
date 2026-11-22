package net.minecraft.world.item.crafting;

import java.util.Optional;

@FunctionalInterface
public interface IngredientExtractor {
  Optional<Ingredient> apply(Recipe<?> paramRecipe);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeManager$IngredientExtractor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */