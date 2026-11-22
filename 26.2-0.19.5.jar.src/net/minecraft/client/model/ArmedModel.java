package net.minecraft.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.HumanoidArm;

public interface ArmedModel<T extends net.minecraft.client.renderer.entity.state.EntityRenderState> {
  void translateToHand(T paramT, HumanoidArm paramHumanoidArm, PoseStack paramPoseStack);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\ArmedModel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */