package net.minecraft.client.renderer.block;

import com.mojang.blaze3d.vertex.QuadInstance;
import net.minecraft.client.resources.model.geometry.BakedQuad;

@FunctionalInterface
public interface BlockQuadOutput {
  void put(float paramFloat1, float paramFloat2, float paramFloat3, BakedQuad paramBakedQuad, QuadInstance paramQuadInstance);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\block\BlockQuadOutput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */