package net.minecraft.world.level.block.entity.trialspawner;

import net.minecraft.world.level.Level;

public interface StateAccessor {
  void setState(Level paramLevel, TrialSpawnerState paramTrialSpawnerState);
  
  TrialSpawnerState getState();
  
  void markUpdated();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawner$StateAccessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */