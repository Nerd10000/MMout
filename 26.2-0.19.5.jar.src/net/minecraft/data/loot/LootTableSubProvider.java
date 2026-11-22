package net.minecraft.data.loot;

import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

@FunctionalInterface
public interface LootTableSubProvider {
  void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> paramBiConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\loot\LootTableSubProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */