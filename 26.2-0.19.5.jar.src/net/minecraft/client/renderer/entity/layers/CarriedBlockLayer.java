/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.monster.enderman.EndermanModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EndermanRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class CarriedBlockLayer extends RenderLayer<EndermanRenderState, EndermanModel<EndermanRenderState>> {
/*    */   public CarriedBlockLayer(RenderLayerParent<EndermanRenderState, EndermanModel<EndermanRenderState>> renderer) {
/* 15 */     super(renderer);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, EndermanRenderState state, float yRot, float xRot) {
/* 20 */     BlockModelRenderState carriedBlock = state.carriedBlock;
/* 21 */     if (carriedBlock.isEmpty())
/*    */       return; 
/* 25 */     poseStack.pushPose();
/* 27 */     poseStack.translate(0.0F, 0.6875F, -0.75F);
/* 28 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(20.0F));
/* 29 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(45.0F));
/* 30 */     poseStack.translate(0.25F, 0.1875F, 0.25F);
/* 31 */     float s = 0.5F;
/* 32 */     poseStack.scale(-0.5F, -0.5F, 0.5F);
/* 33 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0F));
/* 35 */     carriedBlock.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/* 36 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\CarriedBlockLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */