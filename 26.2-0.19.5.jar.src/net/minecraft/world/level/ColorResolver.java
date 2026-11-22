package net.minecraft.world.level;

import net.minecraft.world.level.biome.Biome;

@FunctionalInterface
public interface ColorResolver {
  int getColor(Biome paramBiome, double paramDouble1, double paramDouble2);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\ColorResolver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */