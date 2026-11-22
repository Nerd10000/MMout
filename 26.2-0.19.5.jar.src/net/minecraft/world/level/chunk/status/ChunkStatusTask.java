package net.minecraft.world.level.chunk.status;

import java.util.concurrent.CompletableFuture;
import net.minecraft.server.level.GenerationChunkHolder;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.chunk.ChunkAccess;

@FunctionalInterface
public interface ChunkStatusTask {
  CompletableFuture<ChunkAccess> doWork(WorldGenContext paramWorldGenContext, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D, ChunkAccess paramChunkAccess);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\status\ChunkStatusTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */