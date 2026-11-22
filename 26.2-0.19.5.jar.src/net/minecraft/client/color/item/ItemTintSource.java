package net.minecraft.client.color.item;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface ItemTintSource {
  int calculate(ItemStack paramItemStack, ClientLevel paramClientLevel, LivingEntity paramLivingEntity);
  
  MapCodec<? extends ItemTintSource> type();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\color\item\ItemTintSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */