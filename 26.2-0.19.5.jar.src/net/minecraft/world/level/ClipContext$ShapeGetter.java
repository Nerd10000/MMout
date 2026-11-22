package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface ShapeGetter {
  VoxelShape get(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\ClipContext$ShapeGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */