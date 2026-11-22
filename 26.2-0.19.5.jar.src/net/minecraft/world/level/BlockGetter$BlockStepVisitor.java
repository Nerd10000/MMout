package net.minecraft.world.level;

import net.minecraft.core.BlockPos;

@FunctionalInterface
public interface BlockStepVisitor {
  boolean visit(BlockPos paramBlockPos, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\BlockGetter$BlockStepVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */