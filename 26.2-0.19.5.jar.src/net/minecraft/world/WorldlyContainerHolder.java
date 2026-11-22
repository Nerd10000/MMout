package net.minecraft.world;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface WorldlyContainerHolder {
  WorldlyContainer getContainer(BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\WorldlyContainerHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */