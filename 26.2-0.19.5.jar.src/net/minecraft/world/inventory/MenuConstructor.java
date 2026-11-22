package net.minecraft.world.inventory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface MenuConstructor {
  AbstractContainerMenu createMenu(int paramInt, Inventory paramInventory, Player paramPlayer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\MenuConstructor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */