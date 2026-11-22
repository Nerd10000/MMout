package net.minecraft.client.renderer.item.properties.numeric;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemStack;

public interface RangeSelectItemModelProperty {
  float get(ItemStack paramItemStack, ClientLevel paramClientLevel, ItemOwner paramItemOwner, int paramInt);
  
  MapCodec<? extends RangeSelectItemModelProperty> type();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\numeric\RangeSelectItemModelProperty.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */