package net.minecraft.world.entity.ai.behavior;

import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public interface BehaviorControl<E extends net.minecraft.world.entity.LivingEntity> {
  Behavior.Status getStatus();
  
  Set<MemoryModuleType<?>> getRequiredMemories();
  
  boolean tryStart(ServerLevel paramServerLevel, E paramE, long paramLong);
  
  void tickOrStop(ServerLevel paramServerLevel, E paramE, long paramLong);
  
  void doStop(ServerLevel paramServerLevel, E paramE, long paramLong);
  
  String debugString();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\BehaviorControl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */