/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
/*     */ import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.joml.Quaternionf;
/*     */ 
/*     */ public class SubmitNodeStorage implements SubmitNodeCollector {
/*  31 */   private final Int2ObjectAVLTreeMap<SubmitNodeCollection> submitsPerOrder = new Int2ObjectAVLTreeMap();
/*     */   
/*     */   public SubmitNodeCollection order(int order) {
/*  35 */     return (SubmitNodeCollection)this.submitsPerOrder.computeIfAbsent(order, paramInt -> new SubmitNodeCollection());
/*     */   }
/*     */   
/*     */   public void submitShadow(PoseStack poseStack, float radius, List<EntityRenderState.ShadowPiece> pieces) {
/*  40 */     order(0).submitShadow(poseStack, radius, pieces);
/*     */   }
/*     */   
/*     */   public void submitNameTag(PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, CameraRenderState camera) {
/*  45 */     order(0).submitNameTag(poseStack, nameTagAttachment, offset, name, seeThrough, lightCoords, camera);
/*     */   }
/*     */   
/*     */   public void submitText(PoseStack poseStack, float x, float y, FormattedCharSequence string, boolean dropShadow, Font.DisplayMode displayMode, int lightCoords, int color, int backgroundColor, int outlineColor) {
/*  50 */     order(0).submitText(poseStack, x, y, string, dropShadow, displayMode, lightCoords, color, backgroundColor, outlineColor);
/*     */   }
/*     */   
/*     */   public void submitFlame(PoseStack poseStack, EntityRenderState renderState, Quaternionf rotation) {
/*  55 */     order(0).submitFlame(poseStack, renderState, rotation);
/*     */   }
/*     */   
/*     */   public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leashState) {
/*  60 */     order(0).submitLeash(poseStack, leashState);
/*     */   }
/*     */   
/*     */   public <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
/*  65 */     order(0).submitModel(model, state, poseStack, renderType, lightCoords, overlayCoords, tintedColor, sprite, outlineColor, crumblingOverlay);
/*     */   }
/*     */   
/*     */   public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState, int outlineColor) {
/*  70 */     order(0).submitMovingBlock(poseStack, movingBlockRenderState, outlineColor);
/*     */   }
/*     */   
/*     */   public void submitBlockModel(PoseStack poseStack, RenderType renderType, List<BlockStateModelPart> modelParts, int[] tintLayers, int lightCoords, int overlayCoords, int outlineColor) {
/*  75 */     order(0).submitBlockModel(poseStack, renderType, modelParts, tintLayers, lightCoords, overlayCoords, outlineColor);
/*     */   }
/*     */   
/*     */   public void submitBreakingBlockModel(PoseStack poseStack, List<BlockStateModelPart> parts, int progress) {
/*  80 */     order(0).submitBreakingBlockModel(poseStack, parts, progress);
/*     */   }
/*     */   
/*     */   public void submitShapeOutline(PoseStack poseStack, VoxelShape shape, RenderType renderType, int color, float width, boolean afterTerrain) {
/*  85 */     order(0).submitShapeOutline(poseStack, shape, renderType, color, width, afterTerrain);
/*     */   }
/*     */   
/*     */   public void submitItem(PoseStack poseStack, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, ItemStackRenderState.FoilType foilType) {
/*  90 */     order(0).submitItem(poseStack, displayContext, lightCoords, overlayCoords, outlineColor, tintLayers, quads, foilType);
/*     */   }
/*     */   
/*     */   public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer) {
/*  95 */     order(0).submitCustomGeometry(poseStack, renderType, customGeometryRenderer);
/*     */   }
/*     */   
/*     */   public void submitQuadParticleGroup(QuadParticleRenderState particles) {
/* 100 */     order(0).submitQuadParticleGroup(particles);
/*     */   }
/*     */   
/*     */   public void submitGizmoPrimitives(DrawableGizmoPrimitives.Group group, CameraRenderState camera, boolean onTop) {
/* 105 */     order(0).submitGizmoPrimitives(group, camera, onTop);
/*     */   }
/*     */   
/*     */   public Int2ObjectAVLTreeMap<SubmitNodeCollection> getSubmitsPerOrder() {
/* 109 */     return this.submitsPerOrder;
/*     */   }
/*     */   
/*     */   public void drainPhases(Consumer<FeatureRenderPhase<?>> consumer) {
/* 113 */     this.submitsPerOrder.values().removeIf(collection -> {
/*     */           boolean empty = true;
/*     */           for (FeatureRenderPhase<?> phase : collection.allPhases()) {
/*     */             if (phase.isEmpty())
/*     */               continue; 
/*     */             consumer.accept(phase);
/*     */             empty = false;
/*     */           } 
/*     */           return empty;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SubmitNodeStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */