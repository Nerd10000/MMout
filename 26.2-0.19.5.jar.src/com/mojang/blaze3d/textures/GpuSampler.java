package com.mojang.blaze3d.textures;

import java.util.OptionalDouble;

public abstract class GpuSampler implements AutoCloseable {
  public abstract AddressMode getAddressModeU();
  
  public abstract AddressMode getAddressModeV();
  
  public abstract FilterMode getMinFilter();
  
  public abstract FilterMode getMagFilter();
  
  public abstract int getMaxAnisotropy();
  
  public abstract OptionalDouble getMaxLod();
  
  public abstract void close();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\blaze3d\textures\GpuSampler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */