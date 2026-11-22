package net.minecraft.util.valueproviders;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.RandomSource;

public interface IntProvider {
  int sample(RandomSource paramRandomSource);
  
  int minInclusive();
  
  int maxInclusive();
  
  MapCodec<? extends IntProvider> codec();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\valueproviders\IntProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */