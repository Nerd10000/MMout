package net.minecraft.client.renderer.block;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

@FunctionalInterface
public interface Output {
  VertexConsumer getBuilder(ChunkSectionLayer paramChunkSectionLayer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\FluidRenderer$Output.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */