/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.VillagerLikeModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class CrossedArmsItemLayer<S extends HoldingEntityRenderState, M extends EntityModel<S> & VillagerLikeModel<S>> extends RenderLayer<S, M> {
/*    */   public CrossedArmsItemLayer(RenderLayerParent<S, M> renderer) {
/* 17 */     super(renderer);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 22 */     ItemStackRenderState item = ((HoldingEntityRenderState)state).heldItem;
/* 23 */     if (item.isEmpty())
/*    */       return; 
/* 27 */     poseStack.pushPose();
/* 28 */     applyTranslation(state, poseStack);
/* 30 */     item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((HoldingEntityRenderState)state).outlineColor);
/* 32 */     poseStack.popPose();
/*    */   }
/*    */   
/*    */   protected void applyTranslation(S state, PoseStack poseStack) {
/* 36 */     ((VillagerLikeModel)getParentModel()).translateToArms((EntityRenderState)state, poseStack);
/* 37 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation(0.75F));
/* 38 */     poseStack.scale(1.07F, 1.07F, 1.07F);
/* 39 */     poseStack.translate(0.0F, 0.13F, -0.34F);
/* 40 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotation(3.1415927F));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\CrossedArmsItemLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */