package net.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.chunk.ChunkAccess;

@FunctionalInterface
public interface SpawnPredicate {
  boolean test(EntityType<?> paramEntityType, BlockPos paramBlockPos, ChunkAccess paramChunkAccess);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\NaturalSpawner$SpawnPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */