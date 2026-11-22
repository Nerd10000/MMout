package net.minecraft.world.entity.ai.behavior.declarative;

import net.minecraft.server.level.ServerLevel;

public interface Trigger<E extends net.minecraft.world.entity.LivingEntity> {
  boolean trigger(ServerLevel paramServerLevel, E paramE, long paramLong);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\Trigger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */