package net.minecraft.util.valueproviders;

import com.mojang.serialization.MapCodec;

public interface FloatProvider extends SampledFloat {
  float min();
  
  float max();
  
  MapCodec<? extends FloatProvider> codec();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\FloatProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */