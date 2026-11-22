package net.minecraft.world.level.storage.loot.entries;

import java.util.List;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

@FunctionalInterface
public interface EntryConstructor {
  LootPoolSingletonContainer build(int paramInt1, int paramInt2, List<LootItemCondition> paramList, List<LootItemFunction> paramList1);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\entries\LootPoolSingletonContainer$EntryConstructor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */