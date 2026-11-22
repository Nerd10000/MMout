package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;

@FunctionalInterface
public interface BlockEntityTagOutput {
  void accept(BlockPos paramBlockPos, BlockEntityType<?> paramBlockEntityType, CompoundTag paramCompoundTag);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundLevelChunkPacketData$BlockEntityTagOutput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */