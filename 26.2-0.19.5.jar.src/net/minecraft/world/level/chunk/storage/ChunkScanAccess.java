package net.minecraft.world.level.chunk.storage;

import java.util.concurrent.CompletableFuture;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.world.level.ChunkPos;

public interface ChunkScanAccess {
  CompletableFuture<Void> scanChunk(ChunkPos paramChunkPos, StreamTagVisitor paramStreamTagVisitor);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\storage\ChunkScanAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */