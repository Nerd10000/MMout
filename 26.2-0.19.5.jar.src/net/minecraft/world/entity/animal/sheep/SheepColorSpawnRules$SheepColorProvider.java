package net.minecraft.world.entity.animal.sheep;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;

@FunctionalInterface
interface SheepColorProvider {
  DyeColor get(RandomSource paramRandomSource);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sheep\SheepColorSpawnRules$SheepColorProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */