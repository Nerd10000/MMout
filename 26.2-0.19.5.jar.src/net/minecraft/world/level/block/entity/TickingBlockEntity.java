package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;

public interface TickingBlockEntity {
  void tick();
  
  boolean isRemoved();
  
  BlockPos getPos();
  
  String getType();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\TickingBlockEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */