package net.minecraft.client.entity;

import net.minecraft.world.entity.animal.parrot.Parrot;
import net.minecraft.world.entity.player.PlayerSkin;

public interface ClientAvatarEntity {
  ClientAvatarState avatarState();
  
  PlayerSkin getSkin();
  
  Parrot.Variant getParrotVariantOnShoulder(boolean paramBoolean);
  
  boolean showExtraEars();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\entity\ClientAvatarEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */