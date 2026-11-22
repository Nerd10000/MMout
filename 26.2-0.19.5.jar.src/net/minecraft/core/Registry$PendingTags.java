package net.minecraft.core;

import net.minecraft.resources.ResourceKey;

public interface PendingTags<T> {
  ResourceKey<? extends Registry<? extends T>> key();
  
  HolderLookup.RegistryLookup<T> lookup();
  
  void apply();
  
  int size();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\Registry$PendingTags.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */