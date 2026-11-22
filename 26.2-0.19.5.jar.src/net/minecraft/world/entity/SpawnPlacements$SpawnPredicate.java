package net.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;

@FunctionalInterface
public interface SpawnPredicate<T extends Entity> {
  boolean test(EntityType<T> paramEntityType, ServerLevelAccessor paramServerLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SpawnPlacements$SpawnPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */