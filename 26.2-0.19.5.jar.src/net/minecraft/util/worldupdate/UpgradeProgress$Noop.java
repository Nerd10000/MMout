package net.minecraft.util.worldupdate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;

public class Noop extends UpgradeProgress {
  public void setFinished(boolean finished) {}
  
  public void addTotalFileFixOperations(int additionalFileFixOperations) {}
  
  public void setTotalProgress(float totalProgress) {}
  
  public void addTotalChunks(int additionalTotalChunks) {}
  
  public void setDimensionProgress(ResourceKey<Level> dimensionKey, float currentProgress) {}
  
  public void incrementConverted() {}
  
  public void incrementSkipped() {}
  
  public void incrementFinishedOperations() {}
  
  public void incrementFinishedOperationsBy(int count) {}
  
  public void setCanceled() {}
  
  public void setStatus(UpgradeProgress.Status status) {}
  
  public void setApplicableFixerAmount(int amount) {}
  
  public void incrementRunningFileFixer() {}
  
  public void reset(DataFixTypes dataFixType) {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\UpgradeProgress$Noop.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */