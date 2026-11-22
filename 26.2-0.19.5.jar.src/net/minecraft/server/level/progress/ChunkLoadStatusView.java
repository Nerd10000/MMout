package net.minecraft.server.level.progress;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.status.ChunkStatus;

public interface ChunkLoadStatusView {
  void moveTo(ResourceKey<Level> paramResourceKey, ChunkPos paramChunkPos);
  
  ChunkStatus get(int paramInt1, int paramInt2);
  
  int radius();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\progress\ChunkLoadStatusView.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */