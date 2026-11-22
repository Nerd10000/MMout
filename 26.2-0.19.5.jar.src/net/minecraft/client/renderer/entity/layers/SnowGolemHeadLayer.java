/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.golem.SnowGolemModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SnowGolemRenderState;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class SnowGolemHeadLayer extends RenderLayer<SnowGolemRenderState, SnowGolemModel> {
/*    */   public SnowGolemHeadLayer(RenderLayerParent<SnowGolemRenderState, SnowGolemModel> renderer) {
/* 13 */     super(renderer);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SnowGolemRenderState state, float yRot, float xRot) {
/* 18 */     if (state.headBlock.isEmpty())
/*    */       return; 
/* 22 */     if (state.isInvisible && !state.appearsGlowing())
/*    */       return; 
/* 26 */     poseStack.pushPose();
/* 27 */     getParentModel().getHead().translateAndRotate(poseStack);
/* 29 */     float s = 0.625F;
/* 30 */     poseStack.translate(0.0F, -0.34375F, 0.0F);
/* 31 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/* 32 */     poseStack.scale(0.625F, -0.625F, -0.625F);
/* 34 */     int overlayCoords = LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F);
/* 36 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/* 39 */     if (state.appearsGlowing() && state.isInvisible) {
/* 40 */       state.headBlock.submitOnlyOutline(poseStack, submitNodeCollector, lightCoords, overlayCoords, state.outlineColor);
/*    */     } else {
/* 42 */       state.headBlock.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, state.outlineColor);
/*    */     } 
/* 45 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\SnowGolemHeadLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */