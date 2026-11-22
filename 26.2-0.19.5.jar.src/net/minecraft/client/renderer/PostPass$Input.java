package net.minecraft.client.renderer;

import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.util.Map;
import net.minecraft.resources.Identifier;

public interface Input {
  void addToPass(FramePass paramFramePass, Map<Identifier, ResourceHandle<RenderTarget>> paramMap);
  
  default void cleanup(Map<Identifier, ResourceHandle<RenderTarget>> targets) {}
  
  GpuTextureView texture(Map<Identifier, ResourceHandle<RenderTarget>> paramMap);
  
  String samplerName();
  
  boolean bilinear();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\PostPass$Input.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */