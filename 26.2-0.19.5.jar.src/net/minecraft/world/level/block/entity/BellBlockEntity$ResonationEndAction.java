package net.minecraft.world.level.block.entity;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@FunctionalInterface
interface ResonationEndAction {
  void run(Level paramLevel, BlockPos paramBlockPos, List<LivingEntity> paramList);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BellBlockEntity$ResonationEndAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */