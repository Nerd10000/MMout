package com.mojang.blaze3d.systems;

import java.util.OptionalLong;

public interface GpuQueryPool extends AutoCloseable {
  int size();
  
  OptionalLong getValue(int paramInt);
  
  OptionalLong[] getValues(int paramInt1, int paramInt2);
  
  void close();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\GpuQueryPool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */