package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

@FunctionalInterface
public interface SpreadPredicate {
  boolean test(BlockGetter paramBlockGetter, BlockPos paramBlockPos, MultifaceSpreader.SpreadPos paramSpreadPos);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\MultifaceSpreader$SpreadPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */