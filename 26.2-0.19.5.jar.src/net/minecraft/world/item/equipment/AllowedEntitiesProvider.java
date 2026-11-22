package net.minecraft.world.item.equipment;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;

@FunctionalInterface
public interface AllowedEntitiesProvider {
  HolderSet<EntityType<?>> get(HolderGetter<EntityType<?>> paramHolderGetter);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\equipment\AllowedEntitiesProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */