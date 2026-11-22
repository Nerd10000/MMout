package net.minecraft.world.item.crafting;

import java.util.Optional;
import net.minecraft.server.level.ServerLevel;

public interface CachedCheck<I extends RecipeInput, T extends Recipe<I>> {
  Optional<RecipeHolder<T>> getRecipeFor(I paramI, ServerLevel paramServerLevel);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\crafting\RecipeManager$CachedCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */