/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.boat.BoatModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.BoatRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public class BoatRenderer extends AbstractBoatRenderer {
/*    */   private final Model.Simple waterPatchModel;
/*    */   
/*    */   private final EntityModel<BoatRenderState> model;
/*    */   
/*    */   public BoatRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelId) {
/* 20 */     super(context, modelId.model().withPath(p -> "textures/entity/" + p + ".png"));
/* 21 */     this.waterPatchModel = new Model.Simple(context.bakeLayer(ModelLayers.BOAT_WATER_PATCH), t -> RenderTypes.waterMask());
/* 22 */     this.model = (EntityModel<BoatRenderState>)new BoatModel(context.bakeLayer(modelId));
/*    */   }
/*    */   
/*    */   protected EntityModel<BoatRenderState> model() {
/* 27 */     return this.model;
/*    */   }
/*    */   
/*    */   protected void submitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 32 */     if (!state.isUnderWater)
/* 33 */       submitNodeCollector.submitModel((Model)this.waterPatchModel, Unit.INSTANCE, poseStack, this.texture, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\BoatRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */