package net.minecraft.client.gui.screens.dialog;

import java.util.Optional;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerLinks;
import net.minecraft.server.dialog.Dialog;

public interface DialogConnectionAccess {
  void disconnect(Component paramComponent);
  
  void runCommand(String paramString, Screen paramScreen);
  
  void openDialog(Holder<Dialog> paramHolder, Screen paramScreen);
  
  void sendCustomAction(Identifier paramIdentifier, Optional<Tag> paramOptional);
  
  ServerLinks serverLinks();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\DialogConnectionAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */