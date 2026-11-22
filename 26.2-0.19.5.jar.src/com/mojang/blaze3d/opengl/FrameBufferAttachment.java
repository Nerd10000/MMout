package com.mojang.blaze3d.opengl;

public interface FrameBufferAttachment {
  int glId();
  
  int fboMipLevel();
  
  void addAssociatedFbo(FrameBufferCache.CacheKey paramCacheKey);
  
  void removeAssociatedFbo(FrameBufferCache.CacheKey paramCacheKey);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\opengl\FrameBufferAttachment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */