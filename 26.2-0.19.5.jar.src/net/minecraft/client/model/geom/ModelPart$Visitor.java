package net.minecraft.client.model.geom;

import com.mojang.blaze3d.vertex.PoseStack;

@FunctionalInterface
public interface Visitor {
  void visit(PoseStack.Pose paramPose, String paramString, int paramInt, ModelPart.Cube paramCube);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\geom\ModelPart$Visitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */