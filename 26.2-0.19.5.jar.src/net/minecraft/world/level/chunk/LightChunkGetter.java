package net.minecraft.world.level.chunk;

import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;

public interface LightChunkGetter {
  LightChunk getChunkForLighting(int paramInt1, int paramInt2);
  
  default void onLightUpdate(LightLayer layer, SectionPos pos) {}
  
  BlockGetter getLevel();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\LightChunkGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */