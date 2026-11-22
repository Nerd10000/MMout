package net.minecraft.client.gui.spectator;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public interface SpectatorMenuItem {
  void selectItem(SpectatorMenu paramSpectatorMenu);
  
  Component getName();
  
  void extractIcon(GuiGraphicsExtractor paramGuiGraphicsExtractor, float paramFloat1, float paramFloat2);
  
  boolean isEnabled();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\spectator\SpectatorMenuItem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */