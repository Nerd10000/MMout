package net.minecraft.world.entity.ai.behavior;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface ItemThrower<E> {
  void onItemThrown(ServerLevel paramServerLevel, E paramE, ItemStack paramItemStack, BlockPos paramBlockPos);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\GoAndGiveItemsToTarget$ItemThrower.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */