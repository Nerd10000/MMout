package net.minecraft.world.inventory;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public interface ContainerSynchronizer {
  void sendInitialData(AbstractContainerMenu paramAbstractContainerMenu, List<ItemStack> paramList, ItemStack paramItemStack, int[] paramArrayOfint);
  
  void sendSlotChange(AbstractContainerMenu paramAbstractContainerMenu, int paramInt, ItemStack paramItemStack);
  
  void sendCarriedChange(AbstractContainerMenu paramAbstractContainerMenu, ItemStack paramItemStack);
  
  void sendDataChange(AbstractContainerMenu paramAbstractContainerMenu, int paramInt1, int paramInt2);
  
  RemoteSlot createSlot();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\inventory\ContainerSynchronizer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */