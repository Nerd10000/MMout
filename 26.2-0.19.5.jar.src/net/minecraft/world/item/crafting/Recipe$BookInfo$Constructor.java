package net.minecraft.world.item.crafting;

import java.util.function.BiFunction;

@FunctionalInterface
public interface Constructor<CategoryType, SelfType extends Recipe.BookInfo<CategoryType>> extends BiFunction<CategoryType, String, SelfType> {}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\Recipe$BookInfo$Constructor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */