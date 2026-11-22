package net.minecraft.client.renderer.state.gui;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.TextureSetup;

public interface GuiElementRenderState extends ScreenArea {
  void buildVertices(VertexConsumer paramVertexConsumer);
  
  RenderPipeline pipeline();
  
  TextureSetup textureSetup();
  
  ScreenRectangle scissorArea();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\state\gui\GuiElementRenderState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */