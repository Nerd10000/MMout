package net.minecraft.server.level;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface LevelChangeListener {
  void onLevelChange(ChunkPos paramChunkPos, IntSupplier paramIntSupplier, int paramInt, IntConsumer paramIntConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ChunkHolder$LevelChangeListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */