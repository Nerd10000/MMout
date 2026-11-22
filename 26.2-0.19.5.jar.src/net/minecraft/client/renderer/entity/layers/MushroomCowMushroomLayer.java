/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.cow.CowModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class MushroomCowMushroomLayer extends RenderLayer<MushroomCowRenderState, CowModel> {
/*    */   public MushroomCowMushroomLayer(RenderLayerParent<MushroomCowRenderState, CowModel> renderer) {
/* 14 */     super(renderer);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, MushroomCowRenderState state, float yRot, float xRot) {
/* 19 */     if (state.isBaby || state.mushroomModel.isEmpty())
/*    */       return; 
/* 23 */     boolean appearsGlowingWithInvisibility = (state.appearsGlowing() && state.isInvisible);
/* 25 */     if (state.isInvisible && !appearsGlowingWithInvisibility)
/*    */       return; 
/* 29 */     int overlayCoords = LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F);
/* 31 */     poseStack.pushPose();
/* 32 */     poseStack.translate(0.2F, -0.35F, 0.5F);
/* 33 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-48.0F));
/* 34 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/* 35 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/* 36 */     submitMushroomBlock(poseStack, submitNodeCollector, lightCoords, appearsGlowingWithInvisibility, state.outlineColor, state.mushroomModel, overlayCoords);
/* 37 */     poseStack.popPose();
/* 39 */     poseStack.pushPose();
/* 40 */     poseStack.translate(0.2F, -0.35F, 0.5F);
/* 41 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(42.0F));
/* 42 */     poseStack.translate(0.1F, 0.0F, -0.6F);
/* 43 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-48.0F));
/* 44 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/* 45 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/* 46 */     submitMushroomBlock(poseStack, submitNodeCollector, lightCoords, appearsGlowingWithInvisibility, state.outlineColor, state.mushroomModel, overlayCoords);
/* 47 */     poseStack.popPose();
/* 49 */     poseStack.pushPose();
/* 50 */     getParentModel().getHead().translateAndRotate(poseStack);
/* 51 */     poseStack.translate(0.0F, -0.7F, -0.2F);
/* 52 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-78.0F));
/* 53 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/* 54 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/* 55 */     submitMushroomBlock(poseStack, submitNodeCollector, lightCoords, appearsGlowingWithInvisibility, state.outlineColor, state.mushroomModel, overlayCoords);
/* 56 */     poseStack.popPose();
/*    */   }
/*    */   
/*    */   private void submitMushroomBlock(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, boolean appearsGlowingWithInvisibility, int outlineColor, BlockModelRenderState mushroomModel, int overlayCoords) {
/* 60 */     if (appearsGlowingWithInvisibility) {
/* 61 */       mushroomModel.submitOnlyOutline(poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
/*    */     } else {
/* 63 */       mushroomModel.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, outlineColor);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\MushroomCowMushroomLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */