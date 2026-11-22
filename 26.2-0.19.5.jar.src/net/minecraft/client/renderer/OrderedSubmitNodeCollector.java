/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*    */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*    */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*    */ import net.minecraft.client.resources.model.sprite.SpriteId;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ import org.joml.Quaternionf;
/*    */ 
/*    */ public interface OrderedSubmitNodeCollector {
/*    */   void submitShadow(PoseStack paramPoseStack, float paramFloat, List<EntityRenderState.ShadowPiece> paramList);
/*    */   
/*    */   void submitNameTag(PoseStack paramPoseStack, Vec3 paramVec3, int paramInt1, Component paramComponent, boolean paramBoolean, int paramInt2, CameraRenderState paramCameraRenderState);
/*    */   
/*    */   void submitText(PoseStack paramPoseStack, float paramFloat1, float paramFloat2, FormattedCharSequence paramFormattedCharSequence, boolean paramBoolean, Font.DisplayMode paramDisplayMode, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*    */   
/*    */   void submitFlame(PoseStack paramPoseStack, EntityRenderState paramEntityRenderState, Quaternionf paramQuaternionf);
/*    */   
/*    */   void submitLeash(PoseStack paramPoseStack, EntityRenderState.LeashState paramLeashState);
/*    */   
/*    */   <S> void submitModel(Model<? super S> paramModel, S paramS, PoseStack paramPoseStack, RenderType paramRenderType, int paramInt1, int paramInt2, int paramInt3, TextureAtlasSprite paramTextureAtlasSprite, int paramInt4, ModelFeatureRenderer.CrumblingOverlay paramCrumblingOverlay);
/*    */   
/*    */   default <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
/* 47 */     submitModel(model, state, poseStack, renderType, lightCoords, overlayCoords, -1, (TextureAtlasSprite)null, outlineColor, crumblingOverlay);
/*    */   }
/*    */   
/*    */   default <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, Identifier texture, int lightCoords, int overlayCoords, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
/* 51 */     submitModel(model, state, poseStack, model.renderType(texture), lightCoords, overlayCoords, -1, (TextureAtlasSprite)null, outlineColor, crumblingOverlay);
/*    */   }
/*    */   
/*    */   default <S> void submitModel(Model<S> model, S state, PoseStack poseStack, int lightCoords, int overlayCoords, int tintedColor, SpriteId sprite, SpriteGetter sprites, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
/* 55 */     submitModel(model, state, poseStack, sprite.renderType(model.renderType()), lightCoords, overlayCoords, tintedColor, sprites.get(sprite), outlineColor, crumblingOverlay);
/*    */   }
/*    */   
/*    */   default void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, TextureAtlasSprite sprite) {
/* 59 */     submitModelPart(modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, -1, null, 0);
/*    */   }
/*    */   
/*    */   default void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, TextureAtlasSprite sprite, int tintedColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
/* 63 */     submitModelPart(modelPart, poseStack, renderType, lightCoords, overlayCoords, sprite, tintedColor, crumblingOverlay, 0);
/*    */   }
/*    */   
/*    */   default void submitModelPart(ModelPart modelPart, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, TextureAtlasSprite sprite, int tintedColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, int outlineColor) {
/* 67 */     Model.Simple model = new Model.Simple(modelPart, paramIdentifier -> renderType);
/* 68 */     submitModel((Model<? super Unit>)model, Unit.INSTANCE, poseStack, renderType, lightCoords, overlayCoords, tintedColor, sprite, outlineColor, crumblingOverlay);
/*    */   }
/*    */   
/*    */   void submitMovingBlock(PoseStack paramPoseStack, MovingBlockRenderState paramMovingBlockRenderState, int paramInt);
/*    */   
/*    */   void submitBlockModel(PoseStack paramPoseStack, RenderType paramRenderType, List<BlockStateModelPart> paramList, int[] paramArrayOfint, int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   void submitBreakingBlockModel(PoseStack paramPoseStack, List<BlockStateModelPart> paramList, int paramInt);
/*    */   
/*    */   void submitShapeOutline(PoseStack paramPoseStack, VoxelShape paramVoxelShape, RenderType paramRenderType, int paramInt, float paramFloat, boolean paramBoolean);
/*    */   
/*    */   void submitItem(PoseStack paramPoseStack, ItemDisplayContext paramItemDisplayContext, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfint, List<BakedQuad> paramList, ItemStackRenderState.FoilType paramFoilType);
/*    */   
/*    */   void submitCustomGeometry(PoseStack paramPoseStack, RenderType paramRenderType, SubmitNodeCollector.CustomGeometryRenderer paramCustomGeometryRenderer);
/*    */   
/*    */   void submitQuadParticleGroup(QuadParticleRenderState paramQuadParticleRenderState);
/*    */   
/*    */   void submitGizmoPrimitives(DrawableGizmoPrimitives.Group paramGroup, CameraRenderState paramCameraRenderState, boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\OrderedSubmitNodeCollector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */