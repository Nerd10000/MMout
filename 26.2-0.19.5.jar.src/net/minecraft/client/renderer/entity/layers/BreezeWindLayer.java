/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.breeze.BreezeModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.BreezeRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class BreezeWindLayer extends RenderLayer<BreezeRenderState, BreezeModel> {
/* 17 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/breeze/breeze_wind.png");
/*    */   
/*    */   private final BreezeModel model;
/*    */   
/*    */   public BreezeWindLayer(RenderLayerParent<BreezeRenderState, BreezeModel> renderer, EntityModelSet modelSet) {
/* 21 */     super(renderer);
/* 22 */     this.model = new BreezeModel(modelSet.bakeLayer(ModelLayers.BREEZE_WIND));
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, BreezeRenderState state, float yRot, float xRot) {
/* 27 */     RenderType renderType = RenderTypes.breezeWind(TEXTURE_LOCATION, xOffset(state.ageInTicks) % 1.0F, 0.0F);
/* 28 */     submitNodeCollector.order(1).submitModel((Model)this.model, state, poseStack, renderType, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */   }
/*    */   
/*    */   private float xOffset(float t) {
/* 32 */     return t * 0.02F;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\BreezeWindLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */