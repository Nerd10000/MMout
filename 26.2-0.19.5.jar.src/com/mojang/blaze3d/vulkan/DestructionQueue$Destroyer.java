package com.mojang.blaze3d.vulkan;

public interface Destroyer<T> {
  default void begin(int count) {}
  
  void destroy(T paramT);
  
  default void end() {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\vulkan\DestructionQueue$Destroyer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */