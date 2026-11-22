package net.minecraft.client;

import net.minecraft.client.gui.components.Tooltip;

@FunctionalInterface
public interface TooltipSupplier<T> {
  Tooltip apply(T paramT);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\OptionInstance$TooltipSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */