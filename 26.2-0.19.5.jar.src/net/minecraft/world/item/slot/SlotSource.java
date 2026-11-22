package net.minecraft.world.item.slot;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface SlotSource extends LootContextUser {
  MapCodec<? extends SlotSource> codec();
  
  SlotCollection provide(LootContext paramLootContext);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\item\slot\SlotSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */