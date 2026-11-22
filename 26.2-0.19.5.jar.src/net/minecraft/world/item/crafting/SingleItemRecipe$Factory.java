package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStackTemplate;

@FunctionalInterface
public interface Factory<T extends SingleItemRecipe> {
  T create(Recipe.CommonInfo paramCommonInfo, Ingredient paramIngredient, ItemStackTemplate paramItemStackTemplate);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\SingleItemRecipe$Factory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */