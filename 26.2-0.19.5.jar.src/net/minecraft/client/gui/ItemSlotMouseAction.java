package net.minecraft.client.gui;

import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface ItemSlotMouseAction {
  boolean matches(Slot paramSlot);
  
  boolean onMouseScrolled(double paramDouble1, double paramDouble2, int paramInt, ItemStack paramItemStack);
  
  void onStopHovering(Slot paramSlot);
  
  void onSlotClicked(Slot paramSlot, ContainerInput paramContainerInput);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\ItemSlotMouseAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */