package net.minecraft.data.recipes;

import java.util.function.BiFunction;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;

@FunctionalInterface
public interface Factory extends BiFunction<Recipe.CommonInfo, CraftingRecipe.CraftingBookInfo, Recipe<?>> {}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\recipes\CustomCraftingRecipeBuilder$Factory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */