package net.minecraft.util.worldupdate;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface TagModifier {
  boolean modifyTagAfterFix(ChunkPos paramChunkPos, CompoundTag paramCompoundTag);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\RegionStorageUpgrader$TagModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */