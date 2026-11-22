package net.minecraft.world.level.entity;

import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public interface EntityAccess extends UniquelyIdentifyable {
  int getId();
  
  BlockPos blockPosition();
  
  AABB getBoundingBox();
  
  void setLevelCallback(EntityInLevelCallback paramEntityInLevelCallback);
  
  Stream<? extends EntityAccess> getSelfAndPassengers();
  
  Stream<? extends EntityAccess> getPassengersAndSelf();
  
  void setRemoved(Entity.RemovalReason paramRemovalReason);
  
  boolean shouldBeSaved();
  
  boolean isAlwaysTicking();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\entity\EntityAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */