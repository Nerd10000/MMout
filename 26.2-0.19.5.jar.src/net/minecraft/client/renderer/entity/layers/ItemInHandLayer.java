/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.ArmedModel;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.effects.SpearAnimations;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.SwingAnimationType;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class ItemInHandLayer<S extends ArmedEntityRenderState, M extends EntityModel<S> & ArmedModel<S>> extends RenderLayer<S, M> {
/*    */   public ItemInHandLayer(RenderLayerParent<S, M> renderer) {
/* 20 */     super(renderer);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 25 */     submitArmWithItem(state, ((ArmedEntityRenderState)state).rightHandItemState, ((ArmedEntityRenderState)state).rightHandItemStack, HumanoidArm.RIGHT, poseStack, submitNodeCollector, lightCoords);
/* 26 */     submitArmWithItem(state, ((ArmedEntityRenderState)state).leftHandItemState, ((ArmedEntityRenderState)state).leftHandItemStack, HumanoidArm.LEFT, poseStack, submitNodeCollector, lightCoords);
/*    */   }
/*    */   
/*    */   protected void submitArmWithItem(S state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 30 */     if (item.isEmpty())
/*    */       return; 
/* 34 */     poseStack.pushPose();
/* 35 */     ((ArmedModel)getParentModel()).translateToHand((EntityRenderState)state, arm, poseStack);
/* 38 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-90.0F));
/* 39 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/* 42 */     boolean isLeftHand = (arm == HumanoidArm.LEFT);
/* 43 */     float offsetX = useBabyOffset(state) ? 0.0F : 1.0F;
/* 44 */     float offsetY = useBabyOffset(state) ? 1.0F : 2.0F;
/* 45 */     float offsetZ = useBabyOffset(state) ? -4.5F : -10.0F;
/* 46 */     poseStack.translate((isLeftHand ? -1 : true) * offsetX / 16.0F, offsetY / 16.0F, offsetZ / 16.0F);
/* 47 */     if (((ArmedEntityRenderState)state).attackTime > 0.0F && ((ArmedEntityRenderState)state).attackArm == arm && ((ArmedEntityRenderState)state).swingAnimationType == SwingAnimationType.STAB)
/* 48 */       SpearAnimations.thirdPersonAttackItem((ArmedEntityRenderState)state, poseStack); 
/* 51 */     float ticksUsingItem = state.ticksUsingItem(arm);
/* 52 */     if (ticksUsingItem != 0.0F)
/* 53 */       ((arm == HumanoidArm.RIGHT) ? ((ArmedEntityRenderState)state).rightArmPose : ((ArmedEntityRenderState)state).leftArmPose).animateUseItem((ArmedEntityRenderState)state, poseStack, ticksUsingItem, arm, itemStack); 
/* 56 */     item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((ArmedEntityRenderState)state).outlineColor);
/* 57 */     poseStack.popPose();
/*    */   }
/*    */   
/*    */   private boolean useBabyOffset(S state) {
/* 61 */     return (((ArmedEntityRenderState)state).isBaby && ((ArmedEntityRenderState)state).entityType != EntityTypes.ARMOR_STAND);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\ItemInHandLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */