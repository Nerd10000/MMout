package net.minecraft.world.level.biome;

import com.google.common.annotations.VisibleForTesting;

@VisibleForTesting
interface DistanceMetric<T> {
  long distance(Climate.RTree.Node<T> paramNode, long[] paramArrayOflong);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\biome\Climate$DistanceMetric.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */