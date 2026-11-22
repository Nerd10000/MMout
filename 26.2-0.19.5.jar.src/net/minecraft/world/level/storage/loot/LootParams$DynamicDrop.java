package net.minecraft.world.level.storage.loot;

import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface DynamicDrop {
  void add(Consumer<ItemStack> paramConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\LootParams$DynamicDrop.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */