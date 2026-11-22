package net.minecraft.client.renderer.item;

import net.minecraft.client.multiplayer.ClientLevel;

@FunctionalInterface
public interface ModelSelector<T> {
  ItemModel get(T paramT, ClientLevel paramClientLevel);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\SelectItemModel$ModelSelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */