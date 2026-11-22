package net.minecraft.world.level.storage.loot.providers.score;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContextUser;
import net.minecraft.world.scores.ScoreHolder;

public interface ScoreboardNameProvider extends LootContextUser {
  ScoreHolder getScoreHolder(LootContext paramLootContext);
  
  MapCodec<? extends ScoreboardNameProvider> codec();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\loot\providers\score\ScoreboardNameProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */