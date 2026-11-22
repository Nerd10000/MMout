package net.minecraft.world.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public interface Shearable {
  void shear(ServerLevel paramServerLevel, SoundSource paramSoundSource, ItemStack paramItemStack);
  
  boolean readyForShearing();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Shearable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */