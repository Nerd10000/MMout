package net.minecraft.client.sounds;

import net.minecraft.util.RandomSource;

public interface Weighted<T> {
  int getWeight();
  
  T getSound(RandomSource paramRandomSource);
  
  void preloadIfRequired(SoundEngine paramSoundEngine);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\sounds\Weighted.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */