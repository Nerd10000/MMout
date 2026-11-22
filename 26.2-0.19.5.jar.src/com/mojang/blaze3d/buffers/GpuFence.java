package com.mojang.blaze3d.buffers;

public interface GpuFence extends AutoCloseable {
  void close();
  
  boolean awaitCompletion(long paramLong);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\buffers\GpuFence.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */