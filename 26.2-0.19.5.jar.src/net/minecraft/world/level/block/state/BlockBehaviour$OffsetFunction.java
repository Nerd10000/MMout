package net.minecraft.world.level.block.state;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

@FunctionalInterface
public interface OffsetFunction {
  Vec3 evaluate(BlockState paramBlockState, BlockPos paramBlockPos);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\state\BlockBehaviour$OffsetFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */