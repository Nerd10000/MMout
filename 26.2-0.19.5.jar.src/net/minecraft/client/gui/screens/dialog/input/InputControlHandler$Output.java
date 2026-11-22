package net.minecraft.client.gui.screens.dialog.input;

import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.server.dialog.action.Action;

@FunctionalInterface
public interface Output {
  void accept(LayoutElement paramLayoutElement, Action.ValueGetter paramValueGetter);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\dialog\input\InputControlHandler$Output.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */