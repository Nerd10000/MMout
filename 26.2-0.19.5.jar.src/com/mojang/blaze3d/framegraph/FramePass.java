package com.mojang.blaze3d.framegraph;

import com.mojang.blaze3d.resource.ResourceDescriptor;
import com.mojang.blaze3d.resource.ResourceHandle;

public interface FramePass {
  <T> ResourceHandle<T> createsInternal(String paramString, ResourceDescriptor<T> paramResourceDescriptor);
  
  <T> void reads(ResourceHandle<T> paramResourceHandle);
  
  <T> ResourceHandle<T> readsAndWrites(ResourceHandle<T> paramResourceHandle);
  
  void requires(FramePass paramFramePass);
  
  void disableCulling();
  
  void executes(Runnable paramRunnable);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\framegraph\FramePass.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */