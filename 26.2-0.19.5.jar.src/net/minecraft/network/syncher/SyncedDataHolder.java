package net.minecraft.network.syncher;

import java.util.List;

public interface SyncedDataHolder {
  void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor);
  
  void onSyncedDataUpdated(List<SynchedEntityData.DataValue<?>> paramList);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\syncher\SyncedDataHolder.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */