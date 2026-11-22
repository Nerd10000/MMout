package net.minecraft.client.gui.components;

import net.minecraft.resources.Identifier;

@FunctionalInterface
public interface SpriteSupplier<T> {
  Identifier apply(CycleButton<T> paramCycleButton, T paramT);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\CycleButton$SpriteSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */