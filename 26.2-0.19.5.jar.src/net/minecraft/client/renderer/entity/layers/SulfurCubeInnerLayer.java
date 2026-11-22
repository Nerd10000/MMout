/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.slime.SulfurCubeModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.TntRenderer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SulfurCubeRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class SulfurCubeInnerLayer extends RenderLayer<SulfurCubeRenderState, SulfurCubeModel> {
/* 21 */   private static final Identifier SULFUR_CUBE_INNER_LOCATION = Identifier.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_inner.png");
/*    */   
/* 22 */   private static final Identifier SULFUR_CUBE_SMALL_INNER_LOCATION = Identifier.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_inner_small.png");
/*    */   
/*    */   private final SulfurCubeModel normalModel;
/*    */   
/*    */   private final SulfurCubeModel smallModel;
/*    */   
/*    */   public SulfurCubeInnerLayer(RenderLayerParent<SulfurCubeRenderState, SulfurCubeModel> renderer, EntityModelSet modelSet) {
/* 28 */     super(renderer);
/* 29 */     this.normalModel = new SulfurCubeModel(modelSet.bakeLayer(ModelLayers.SULFUR_CUBE_INNER));
/* 30 */     this.smallModel = new SulfurCubeModel(modelSet.bakeLayer(ModelLayers.SULFUR_CUBE_SMALL_INNER));
/*    */   }
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SulfurCubeRenderState state, float yRot, float xRot) {
/* 37 */     int overlayCoords = (state.fuseRemainingTicks > 0.0F && TntRenderer.isLit(state.fuseRemainingTicks)) ? OverlayTexture.pack(OverlayTexture.u(1.0F), 10) : LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F);
/* 39 */     if (!state.containedBlock.isEmpty()) {
/* 40 */       poseStack.pushPose();
/* 42 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(180.0F));
/* 44 */       if (state.isBaby)
/* 45 */         poseStack.scale(0.5F, 0.5F, 0.5F); 
/* 48 */       poseStack.translate(-0.5F, -0.518F, -0.5F);
/* 50 */       state.containedBlock.submit(poseStack, submitNodeCollector, state.lightCoords, overlayCoords, state.outlineColor);
/* 51 */       poseStack.popPose();
/* 52 */     } else if (!state.isInvisible) {
/* 53 */       Identifier location = state.isBaby ? SULFUR_CUBE_SMALL_INNER_LOCATION : SULFUR_CUBE_INNER_LOCATION;
/* 54 */       SulfurCubeModel model = state.isBaby ? this.smallModel : this.normalModel;
/* 56 */       RenderType renderType = RenderTypes.entityTranslucent(location);
/* 57 */       submitNodeCollector.order(-1).submitModel((Model)model, state, poseStack, renderType, lightCoords, overlayCoords, -1, null, state.outlineColor, null);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\layers\SulfurCubeInnerLayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */