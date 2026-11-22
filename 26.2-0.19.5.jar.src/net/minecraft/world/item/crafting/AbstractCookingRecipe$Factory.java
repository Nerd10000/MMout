package net.minecraft.world.item.crafting;

import net.minecraft.world.item.ItemStackTemplate;

@FunctionalInterface
public interface Factory<T extends AbstractCookingRecipe> {
  T create(Recipe.CommonInfo paramCommonInfo, AbstractCookingRecipe.CookingBookInfo paramCookingBookInfo, Ingredient paramIngredient, ItemStackTemplate paramItemStackTemplate, float paramFloat, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\AbstractCookingRecipe$Factory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */