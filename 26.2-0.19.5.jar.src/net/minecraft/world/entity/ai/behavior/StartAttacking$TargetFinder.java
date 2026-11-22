package net.minecraft.world.entity.ai.behavior;

import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface TargetFinder<E> {
  Optional<? extends LivingEntity> get(ServerLevel paramServerLevel, E paramE);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\StartAttacking$TargetFinder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */