package net.minecraft.client.resources.server;

import java.util.List;

public interface Callbacks {
  void onSuccess();
  
  void onFailure(boolean paramBoolean);
  
  List<PackReloadConfig.IdAndPath> packsToLoad();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\PackReloadConfig$Callbacks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */