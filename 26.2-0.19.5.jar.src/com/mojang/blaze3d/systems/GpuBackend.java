package com.mojang.blaze3d.systems;

import com.mojang.blaze3d.GLFWErrorCapture;
import com.mojang.blaze3d.shaders.GpuDebugOptions;
import com.mojang.blaze3d.shaders.ShaderSource;

public interface GpuBackend {
  String getName();
  
  void setWindowHints();
  
  void handleWindowCreationErrors(GLFWErrorCapture.Error paramError) throws BackendCreationException;
  
  GpuDevice createDevice(long paramLong, ShaderSource paramShaderSource, GpuDebugOptions paramGpuDebugOptions, Runnable paramRunnable) throws BackendCreationException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\GpuBackend.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */