package net.minecraft.core.component;

import java.util.function.BiConsumer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface PendingComponents<T> {
  ResourceKey<? extends Registry<? extends T>> key();
  
  void forEach(BiConsumer<Holder.Reference<T>, DataComponentMap> paramBiConsumer);
  
  void apply();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentInitializers$PendingComponents.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */