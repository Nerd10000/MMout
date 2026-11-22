package net.minecraft.world.entity.ai;

import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public interface Visitor {
  <U> void acceptEmpty(MemoryModuleType<U> paramMemoryModuleType);
  
  <U> void accept(MemoryModuleType<U> paramMemoryModuleType, U paramU);
  
  <U> void accept(MemoryModuleType<U> paramMemoryModuleType, U paramU, long paramLong);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\Brain$Visitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */