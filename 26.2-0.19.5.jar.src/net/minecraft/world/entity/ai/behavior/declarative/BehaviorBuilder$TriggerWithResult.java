package net.minecraft.world.entity.ai.behavior.declarative;

import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

interface TriggerWithResult<E extends net.minecraft.world.entity.LivingEntity, R> {
  R tryTrigger(ServerLevel paramServerLevel, E paramE, long paramLong);
  
  Set<MemoryModuleType<?>> memories();
  
  String debugString();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\declarative\BehaviorBuilder$TriggerWithResult.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */