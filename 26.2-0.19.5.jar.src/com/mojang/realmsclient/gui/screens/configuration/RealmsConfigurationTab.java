package com.mojang.realmsclient.gui.screens.configuration;

import com.mojang.realmsclient.dto.RealmsServer;

public interface RealmsConfigurationTab {
  void updateData(RealmsServer paramRealmsServer);
  
  default void onSelected(RealmsServer serverData) {}
  
  default void onDeselected(RealmsServer serverData) {}
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsConfigurationTab.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */