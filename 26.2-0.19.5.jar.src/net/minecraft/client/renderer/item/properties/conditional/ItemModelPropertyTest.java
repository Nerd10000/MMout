package net.minecraft.client.renderer.item.properties.conditional;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface ItemModelPropertyTest {
  boolean get(ItemStack paramItemStack, ClientLevel paramClientLevel, LivingEntity paramLivingEntity, int paramInt, ItemDisplayContext paramItemDisplayContext);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\item\properties\conditional\ItemModelPropertyTest.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */