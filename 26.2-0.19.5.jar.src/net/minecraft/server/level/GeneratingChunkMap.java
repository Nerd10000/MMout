package net.minecraft.server.level;

import java.util.concurrent.CompletableFuture;
import net.minecraft.util.StaticCache2D;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.chunk.status.ChunkStep;

public interface GeneratingChunkMap {
  GenerationChunkHolder acquireGeneration(long paramLong);
  
  void releaseGeneration(GenerationChunkHolder paramGenerationChunkHolder);
  
  CompletableFuture<ChunkAccess> applyStep(GenerationChunkHolder paramGenerationChunkHolder, ChunkStep paramChunkStep, StaticCache2D<GenerationChunkHolder> paramStaticCache2D);
  
  ChunkGenerationTask scheduleGenerationTask(ChunkStatus paramChunkStatus, ChunkPos paramChunkPos);
  
  void runGenerationTasks();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\GeneratingChunkMap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */