package net.minecraft.world.item.enchantment.effects;

import net.minecraft.util.RandomSource;

@FunctionalInterface
interface CoordinateSource {
  double getCoordinate(double paramDouble1, double paramDouble2, float paramFloat, RandomSource paramRandomSource);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\enchantment\effects\SpawnParticlesEffect$PositionSourceType$CoordinateSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */