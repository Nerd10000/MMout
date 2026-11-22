package net.minecraft.world.entity.ai.behavior;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface TargetErasedCallback<E> {
  void accept(ServerLevel paramServerLevel, E paramE, LivingEntity paramLivingEntity);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\StopAttackingIfTargetInvalid$TargetErasedCallback.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */