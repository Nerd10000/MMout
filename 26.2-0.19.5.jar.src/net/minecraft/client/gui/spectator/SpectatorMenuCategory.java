package net.minecraft.client.gui.spectator;

import java.util.List;
import net.minecraft.network.chat.Component;

public interface SpectatorMenuCategory {
  List<SpectatorMenuItem> getItems();
  
  Component getPrompt();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\spectator\SpectatorMenuCategory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */