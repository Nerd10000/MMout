package net.minecraft.world.level.entity;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import net.minecraft.world.level.ChunkPos;

public interface EntityPersistentStorage<T> extends AutoCloseable {
  CompletableFuture<ChunkEntities<T>> loadEntities(ChunkPos paramChunkPos);
  
  void storeEntities(ChunkEntities<T> paramChunkEntities);
  
  void flush(boolean paramBoolean);
  
  default void close() throws IOException {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\EntityPersistentStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */