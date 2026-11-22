package net.minecraft.world.level.storage.loot.providers.nbt;

import com.mojang.serialization.MapCodec;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;

public interface NbtProvider extends LootContextUser {
  Tag get(LootContext paramLootContext);
  
  MapCodec<? extends NbtProvider> codec();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\nbt\NbtProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */