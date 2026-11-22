package net.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface SubmitNodeCollector extends OrderedSubmitNodeCollector {
  OrderedSubmitNodeCollector order(int paramInt);
  
  public static interface CustomGeometryRenderer {
    void render(PoseStack.Pose param1Pose, VertexConsumer param1VertexConsumer);
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SubmitNodeCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */