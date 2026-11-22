/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ 
/*    */ public abstract class EyesLayer<S extends EntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
/*    */   public EyesLayer(RenderLayerParent<S, M> renderer) {
/* 13 */     super(renderer);
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 18 */     submitNodeCollector.order(1).submitModel((Model)getParentModel(), state, poseStack, renderType(), lightCoords, OverlayTexture.NO_OVERLAY, ((EntityRenderState)state).outlineColor, null);
/*    */   }
/*    */   
/*    */   public abstract RenderType renderType();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\EyesLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */