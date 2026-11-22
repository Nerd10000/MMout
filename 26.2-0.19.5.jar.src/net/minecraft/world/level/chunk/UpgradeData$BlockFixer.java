package net.minecraft.world.level.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockFixer {
  BlockState updateShape(BlockState paramBlockState1, Direction paramDirection, BlockState paramBlockState2, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos1, BlockPos paramBlockPos2);
  
  default void processChunk(LevelAccessor level) {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\UpgradeData$BlockFixer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */