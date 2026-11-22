/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.ghast.HappyGhastModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HappyGhastRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ 
/*    */ public class RopesLayer<M extends HappyGhastModel> extends RenderLayer<HappyGhastRenderState, M> {
/*    */   private final Identifier ropesTexture;
/*    */   
/*    */   private final HappyGhastModel adultModel;
/*    */   
/*    */   private final HappyGhastModel babyModel;
/*    */   
/*    */   public RopesLayer(RenderLayerParent<HappyGhastRenderState, M> renderer, EntityModelSet modelSet, Identifier ropesTexture) {
/* 20 */     super(renderer);
/* 21 */     this.ropesTexture = ropesTexture;
/* 22 */     this.adultModel = new HappyGhastModel(modelSet.bakeLayer(ModelLayers.HAPPY_GHAST_ROPES));
/* 23 */     this.babyModel = new HappyGhastModel(modelSet.bakeLayer(ModelLayers.HAPPY_GHAST_BABY_ROPES));
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HappyGhastRenderState state, float yRot, float xRot) {
/* 28 */     if (!state.isLeashHolder || !state.bodyItem.is(ItemTags.HARNESSES))
/*    */       return; 
/* 31 */     HappyGhastModel model = state.isBaby ? this.babyModel : this.adultModel;
/* 32 */     submitNodeCollector.submitModel((Model)model, state, poseStack, this.ropesTexture, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\RopesLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */