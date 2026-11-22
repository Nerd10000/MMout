package net.minecraft.world.item.component;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ConsumableListener {
  void onConsume(Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack, Consumable paramConsumable);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\component\ConsumableListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */