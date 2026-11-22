package net.minecraft.world.level.entity;

import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface ChunkStatusUpdateListener {
  void onChunkStatusChange(ChunkPos paramChunkPos, FullChunkStatus paramFullChunkStatus);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\ChunkStatusUpdateListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */