package net.minecraft.world;

import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public interface WorldlyContainer extends Container {
  int[] getSlotsForFace(Direction paramDirection);
  
  boolean canPlaceItemThroughFace(int paramInt, ItemStack paramItemStack, Direction paramDirection);
  
  boolean canTakeItemThroughFace(int paramInt, ItemStack paramItemStack, Direction paramDirection);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\WorldlyContainer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */