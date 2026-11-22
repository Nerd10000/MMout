/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.slime.SlimeModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.SlimeRenderer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SlimeRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ 
/*    */ public class SlimeOuterLayer extends RenderLayer<SlimeRenderState, SlimeModel> {
/*    */   private final SlimeModel model;
/*    */   
/*    */   public SlimeOuterLayer(RenderLayerParent<SlimeRenderState, SlimeModel> renderer, EntityModelSet modelSet) {
/* 18 */     super(renderer);
/* 19 */     this.model = new SlimeModel(modelSet.bakeLayer(ModelLayers.SLIME_OUTER));
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SlimeRenderState state, float yRot, float xRot) {
/* 24 */     boolean appearsGlowingWithInvisibility = (state.appearsGlowing() && state.isInvisible);
/* 26 */     if (state.isInvisible && !appearsGlowingWithInvisibility)
/*    */       return; 
/* 30 */     int overlayCoords = LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F);
/* 31 */     if (appearsGlowingWithInvisibility) {
/* 32 */       submitNodeCollector.order(1).submitModel((Model)this.model, state, poseStack, RenderTypes.outline(SlimeRenderer.SLIME_LOCATION), lightCoords, overlayCoords, state.outlineColor, null);
/*    */     } else {
/* 34 */       submitNodeCollector.order(1).submitModel((Model)this.model, state, poseStack, RenderTypes.entityTranslucent(SlimeRenderer.SLIME_LOCATION), lightCoords, overlayCoords, state.outlineColor, null);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\SlimeOuterLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */