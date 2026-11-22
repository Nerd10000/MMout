/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.golem.IronGolemModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class IronGolemFlowerLayer extends RenderLayer<IronGolemRenderState, IronGolemModel> {
/*    */   public IronGolemFlowerLayer(RenderLayerParent<IronGolemRenderState, IronGolemModel> renderer) {
/* 15 */     super(renderer);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IronGolemRenderState state, float yRot, float xRot) {
/* 20 */     if (state.flowerBlock.isEmpty())
/*    */       return; 
/* 24 */     poseStack.pushPose();
/* 25 */     ModelPart arm = getParentModel().getFlowerHoldingArm();
/* 28 */     arm.translateAndRotate(poseStack);
/* 29 */     poseStack.translate(-1.1875F, 1.0625F, -0.9375F);
/* 31 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/* 32 */     float s = 0.5F;
/* 33 */     poseStack.scale(0.5F, 0.5F, 0.5F);
/* 34 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-90.0F));
/* 35 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/* 37 */     state.flowerBlock.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/* 38 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\IronGolemFlowerLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */