package net.minecraft.client.gui.screens.dialog;

import net.minecraft.client.gui.screens.Screen;

@FunctionalInterface
public interface Factory<T extends net.minecraft.server.dialog.Dialog> {
  DialogScreen<T> create(Screen paramScreen, T paramT, DialogConnectionAccess paramDialogConnectionAccess);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\DialogScreens$Factory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */