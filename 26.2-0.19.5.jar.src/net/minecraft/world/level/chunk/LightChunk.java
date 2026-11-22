package net.minecraft.world.level.chunk;

import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.ChunkSkyLightSources;

public interface LightChunk extends BlockGetter {
  void findBlockLightSources(BiConsumer<BlockPos, BlockState> paramBiConsumer);
  
  ChunkSkyLightSources getSkyLightSources();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\LightChunk.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */