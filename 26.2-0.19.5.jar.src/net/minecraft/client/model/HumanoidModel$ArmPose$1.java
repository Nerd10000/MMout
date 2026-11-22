/*    */ package net.minecraft.client.model;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.effects.SpearAnimations;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ enum null {
/*    */   public <S extends ArmedEntityRenderState> void animateUseItem(S state, PoseStack poseStack, float ticksUsingItem, HumanoidArm arm, ItemStack actualItem) {
/* 76 */     SpearAnimations.thirdPersonUseItem((ArmedEntityRenderState)state, poseStack, ticksUsingItem, arm, actualItem);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\model\HumanoidModel$ArmPose$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */