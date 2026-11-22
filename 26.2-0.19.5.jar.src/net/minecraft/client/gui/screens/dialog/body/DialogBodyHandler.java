package net.minecraft.client.gui.screens.dialog.body;

import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.dialog.DialogScreen;

public interface DialogBodyHandler<T extends net.minecraft.server.dialog.body.DialogBody> {
  LayoutElement createControls(DialogScreen<?> paramDialogScreen, T paramT);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\body\DialogBodyHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */