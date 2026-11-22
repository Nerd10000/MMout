package net.minecraft.world.level.storage.loot.entries;

import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;

public interface LootPoolEntry {
  int getWeight(float paramFloat);
  
  void createItemStack(Consumer<ItemStack> paramConsumer, LootContext paramLootContext);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolEntry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */