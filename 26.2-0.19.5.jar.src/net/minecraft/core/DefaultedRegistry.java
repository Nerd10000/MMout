package net.minecraft.core;

import net.minecraft.resources.Identifier;

public interface DefaultedRegistry<T> extends Registry<T> {
  Identifier getKey(T paramT);
  
  T getValue(Identifier paramIdentifier);
  
  T byId(int paramInt);
  
  Identifier getDefaultKey();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\DefaultedRegistry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */