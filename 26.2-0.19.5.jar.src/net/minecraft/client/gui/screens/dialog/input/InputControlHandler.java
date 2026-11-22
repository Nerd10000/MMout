package net.minecraft.client.gui.screens.dialog.input;

import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.dialog.action.Action;

@FunctionalInterface
public interface InputControlHandler<T extends net.minecraft.server.dialog.input.InputControl> {
  void addControl(T paramT, Screen paramScreen, Output paramOutput);
  
  @FunctionalInterface
  public static interface Output {
    void accept(LayoutElement param1LayoutElement, Action.ValueGetter param1ValueGetter);
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\input\InputControlHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */