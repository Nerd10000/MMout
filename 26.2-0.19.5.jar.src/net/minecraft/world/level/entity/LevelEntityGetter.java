package net.minecraft.world.level.entity;

import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.world.phys.AABB;

public interface LevelEntityGetter<T extends EntityAccess> {
  T get(int paramInt);
  
  T get(UUID paramUUID);
  
  Iterable<T> getAll();
  
  <U extends T> void get(EntityTypeTest<T, U> paramEntityTypeTest, AbortableIterationConsumer<U> paramAbortableIterationConsumer);
  
  void get(AABB paramAABB, Consumer<T> paramConsumer);
  
  <U extends T> void get(EntityTypeTest<T, U> paramEntityTypeTest, AABB paramAABB, AbortableIterationConsumer<U> paramAbortableIterationConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\LevelEntityGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */