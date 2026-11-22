package net.minecraft.world.inventory;

import net.minecraft.world.item.ItemStack;

public interface ContainerListener {
  void slotChanged(AbstractContainerMenu paramAbstractContainerMenu, int paramInt, ItemStack paramItemStack);
  
  void dataChanged(AbstractContainerMenu paramAbstractContainerMenu, int paramInt1, int paramInt2);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\ContainerListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */