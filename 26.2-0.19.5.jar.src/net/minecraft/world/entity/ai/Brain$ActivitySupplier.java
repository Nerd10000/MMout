package net.minecraft.world.entity.ai;

import java.util.List;

@FunctionalInterface
public interface ActivitySupplier<E extends net.minecraft.world.entity.LivingEntity> {
  List<ActivityData<E>> createActivities(E paramE);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\Brain$ActivitySupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */