package net.minecraft.world.waypoints;

import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface PartialTickSupplier {
  float apply(Entity paramEntity);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\waypoints\PartialTickSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */