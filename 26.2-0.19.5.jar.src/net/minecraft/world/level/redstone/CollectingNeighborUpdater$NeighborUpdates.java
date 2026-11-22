package net.minecraft.world.level.redstone;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

interface NeighborUpdates {
  boolean runNext(Level paramLevel);
  
  void forEachUpdatedPos(Consumer<BlockPos> paramConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\redstone\CollectingNeighborUpdater$NeighborUpdates.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */