package net.minecraft.world.entity;

import net.minecraft.world.level.Level;

@FunctionalInterface
public interface EntityFactory<T extends Entity> {
  T create(EntityType<T> paramEntityType, Level paramLevel);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\EntityType$EntityFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */