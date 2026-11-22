package com.mojang.blaze3d.audio;

interface ChannelPool {
  Channel acquire();
  
  boolean release(Channel paramChannel);
  
  void cleanup();
  
  int getMaxCount();
  
  int getUsedCount();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\audio\Library$ChannelPool.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */