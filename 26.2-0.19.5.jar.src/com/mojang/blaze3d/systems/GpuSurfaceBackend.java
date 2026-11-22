package com.mojang.blaze3d.systems;

import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.Collection;

public interface GpuSurfaceBackend extends AutoCloseable {
  void configure(GpuSurface.Configuration paramConfiguration) throws SurfaceException;
  
  boolean isSuboptimal();
  
  void acquireNextTexture() throws SurfaceException;
  
  void blitFromTexture(CommandEncoderBackend paramCommandEncoderBackend, GpuTextureView paramGpuTextureView);
  
  void present();
  
  void close();
  
  Collection<GpuSurface.PresentMode> supportedPresentModes();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\systems\GpuSurfaceBackend.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */