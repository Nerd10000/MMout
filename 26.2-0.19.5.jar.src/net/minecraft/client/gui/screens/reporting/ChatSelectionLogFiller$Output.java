package net.minecraft.client.gui.screens.reporting;

import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
import net.minecraft.network.chat.Component;

public interface Output {
  void acceptMessage(int paramInt, LoggedChatMessage.Player paramPlayer);
  
  void acceptDivider(Component paramComponent);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\reporting\ChatSelectionLogFiller$Output.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */