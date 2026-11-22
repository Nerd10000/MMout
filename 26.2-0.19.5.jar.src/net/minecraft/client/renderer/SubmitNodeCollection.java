/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.model.Model;
/*     */ import net.minecraft.client.renderer.block.BlockModelRenderState;
/*     */ import net.minecraft.client.renderer.block.MovingBlockRenderState;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.feature.BlockModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.CustomFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.FlameFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.GizmoFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.ItemFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.LeashFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.MovingBlockFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.QuadParticleFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.ShadowFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.ShapeOutlineFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.TextFeatureRenderer;
/*     */ import net.minecraft.client.renderer.feature.phase.FeatureRenderPhase;
/*     */ import net.minecraft.client.renderer.feature.phase.SimpleFeatureRenderPhase;
/*     */ import net.minecraft.client.renderer.feature.phase.TranslucentFeatureRenderPhase;
/*     */ import net.minecraft.client.renderer.feature.submit.SubmitNode;
/*     */ import net.minecraft.client.renderer.feature.submit.TranslucentSubmit;
/*     */ import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
/*     */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.resources.model.ModelBakery;
/*     */ import net.minecraft.client.resources.model.geometry.BakedQuad;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class SubmitNodeCollection implements OrderedSubmitNodeCollector {
/*  54 */   public final SimpleFeatureRenderPhase solid = new SimpleFeatureRenderPhase();
/*     */   
/*  55 */   public final SimpleFeatureRenderPhase shadows = new SimpleFeatureRenderPhase();
/*     */   
/*  56 */   public final SimpleFeatureRenderPhase nameTags = new SimpleFeatureRenderPhase();
/*     */   
/*  57 */   public final TranslucentFeatureRenderPhase seeThroughNameTags = new TranslucentFeatureRenderPhase();
/*     */   
/*  58 */   public final SimpleFeatureRenderPhase texts = new SimpleFeatureRenderPhase();
/*     */   
/*  59 */   public final SimpleFeatureRenderPhase shapeOutlines = new SimpleFeatureRenderPhase();
/*     */   
/*  60 */   public final TranslucentFeatureRenderPhase translucentBlocksAndItems = new TranslucentFeatureRenderPhase();
/*     */   
/*  61 */   public final TranslucentFeatureRenderPhase translucentModels = new TranslucentFeatureRenderPhase();
/*     */   
/*  62 */   public final SimpleFeatureRenderPhase translucentCustomGeometry = new SimpleFeatureRenderPhase();
/*     */   
/*  63 */   public final SimpleFeatureRenderPhase gizmos = new SimpleFeatureRenderPhase();
/*     */   
/*  64 */   public final SimpleFeatureRenderPhase breakingOverlay = new SimpleFeatureRenderPhase();
/*     */   
/*  65 */   public final SimpleFeatureRenderPhase waterMask = new SimpleFeatureRenderPhase();
/*     */   
/*  66 */   public final SimpleFeatureRenderPhase afterTerrain = new SimpleFeatureRenderPhase();
/*     */   
/*  67 */   public final SimpleFeatureRenderPhase alwaysOnTop = new SimpleFeatureRenderPhase();
/*     */   
/*  68 */   public final SimpleFeatureRenderPhase outline = new SimpleFeatureRenderPhase();
/*     */   
/*  70 */   private final List<FeatureRenderPhase<?>> allPhases = List.of((FeatureRenderPhase<?>[])new FeatureRenderPhase[] { 
/*  70 */         (FeatureRenderPhase)this.solid, (FeatureRenderPhase)this.shadows, (FeatureRenderPhase)this.nameTags, (FeatureRenderPhase)this.seeThroughNameTags, (FeatureRenderPhase)this.texts, (FeatureRenderPhase)this.shapeOutlines, (FeatureRenderPhase)this.translucentBlocksAndItems, (FeatureRenderPhase)this.translucentModels, (FeatureRenderPhase)this.translucentCustomGeometry, (FeatureRenderPhase)this.gizmos, 
/*  70 */         (FeatureRenderPhase)this.breakingOverlay, (FeatureRenderPhase)this.waterMask, (FeatureRenderPhase)this.afterTerrain, (FeatureRenderPhase)this.alwaysOnTop, (FeatureRenderPhase)this.outline });
/*     */   
/*     */   public void submitShadow(PoseStack poseStack, float radius, List<EntityRenderState.ShadowPiece> pieces) {
/*  90 */     PoseStack.Pose pose = poseStack.last();
/*  91 */     this.shadows.submit((SubmitNode)new ShadowFeatureRenderer.Submit((Matrix4fc)new Matrix4f((Matrix4fc)pose.pose()), radius, pieces));
/*     */   }
/*     */   
/*     */   public void submitNameTag(PoseStack poseStack, Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, CameraRenderState camera) {
/*  96 */     if (nameTagAttachment == null)
/*     */       return; 
/* 100 */     Minecraft minecraft = Minecraft.getInstance();
/* 102 */     poseStack.pushPose();
/* 103 */     poseStack.translate(nameTagAttachment.x, nameTagAttachment.y + 0.5D, nameTagAttachment.z);
/* 104 */     poseStack.mulPose((Quaternionfc)camera.orientation);
/* 105 */     poseStack.scale(0.025F, -0.025F, 0.025F);
/* 107 */     Matrix4f pose = new Matrix4f((Matrix4fc)poseStack.last().pose());
/* 109 */     float x = -minecraft.font.width((FormattedText)name) / 2.0F;
/* 110 */     int backgroundColor = ARGB.color((minecraft.gameRenderer.gameRenderState()).optionsRenderState.getBackgroundOpacity(0.25F), -16777216);
/* 112 */     if (seeThrough) {
/* 113 */       this.nameTags.submit((SubmitNode)new NameTagFeatureRenderer.Submit((Matrix4fc)pose, x, offset, name, LightCoordsUtil.lightCoordsWithEmission(lightCoords, 2), -1, 0, Font.DisplayMode.NORMAL));
/* 114 */       this.seeThroughNameTags.submit((TranslucentSubmit)new NameTagFeatureRenderer.Submit((Matrix4fc)pose, x, offset, name, lightCoords, -2130706433, backgroundColor, Font.DisplayMode.SEE_THROUGH));
/*     */     } else {
/* 116 */       this.nameTags.submit((SubmitNode)new NameTagFeatureRenderer.Submit((Matrix4fc)pose, x, offset, name, lightCoords, -2130706433, backgroundColor, Font.DisplayMode.NORMAL));
/*     */     } 
/* 119 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public void submitText(PoseStack poseStack, float x, float y, FormattedCharSequence string, boolean dropShadow, Font.DisplayMode displayMode, int lightCoords, int color, int backgroundColor, int outlineColor) {
/* 124 */     this.texts.submit((SubmitNode)new TextFeatureRenderer.Submit((Matrix4fc)new Matrix4f((Matrix4fc)poseStack.last().pose()), x, y, string, dropShadow, displayMode, lightCoords, color, backgroundColor, outlineColor));
/*     */   }
/*     */   
/*     */   public void submitFlame(PoseStack poseStack, EntityRenderState renderState, Quaternionf rotation) {
/* 129 */     this.solid.submit((SubmitNode)new FlameFeatureRenderer.Submit(poseStack.last().copy(), renderState, rotation));
/*     */   }
/*     */   
/*     */   public void submitLeash(PoseStack poseStack, EntityRenderState.LeashState leashState) {
/* 134 */     this.solid.submit((SubmitNode)new LeashFeatureRenderer.Submit(new Matrix4f((Matrix4fc)poseStack.last().pose()), leashState));
/*     */   }
/*     */   
/*     */   public <S> void submitModel(Model<? super S> model, S state, PoseStack poseStack, RenderType renderType, int lightCoords, int overlayCoords, int tintedColor, TextureAtlasSprite sprite, int outlineColor, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
/* 139 */     PoseStack.Pose pose = poseStack.last().copy();
/* 141 */     if (!renderType.isOutline()) {
/* 142 */       ModelFeatureRenderer.Submit<S> submit = new ModelFeatureRenderer.Submit(renderType, pose, model, state, lightCoords, overlayCoords, tintedColor, sprite, null);
/* 144 */       if (renderType == RenderTypes.waterMask()) {
/* 145 */         this.waterMask.submit((SubmitNode)submit);
/* 146 */       } else if (renderType.hasBlending()) {
/* 147 */         this.translucentModels.submit((TranslucentSubmit)submit);
/*     */       } else {
/* 149 */         this.solid.submit((SubmitNode)submit);
/*     */       } 
/*     */     } 
/* 153 */     if (outlineColor != 0) {
/* 154 */       RenderType outlineRenderType = getOutlineRenderType(renderType);
/* 155 */       if (outlineRenderType != null)
/* 156 */         this.outline.submit((SubmitNode)new ModelFeatureRenderer.Submit(outlineRenderType, pose, model, state, 15728880, OverlayTexture.NO_OVERLAY, outlineColor, sprite, null)); 
/*     */     } 
/* 160 */     if (crumblingOverlay != null && renderType.affectsCrumbling()) {
/* 161 */       RenderType crumblingRenderType = ModelBakery.DESTROY_TYPES.get(crumblingOverlay.progress());
/* 162 */       this.breakingOverlay.submit((SubmitNode)new ModelFeatureRenderer.Submit(crumblingRenderType, pose, model, state, lightCoords, overlayCoords, tintedColor, null, crumblingOverlay.cameraPose()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submitMovingBlock(PoseStack poseStack, MovingBlockRenderState movingBlockRenderState, int outlineColor) {
/* 168 */     MovingBlockFeatureRenderer.Submit submit = new MovingBlockFeatureRenderer.Submit((Matrix4fc)new Matrix4f((Matrix4fc)poseStack.last().pose()), movingBlockRenderState, 0);
/* 169 */     BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(movingBlockRenderState.blockState);
/* 170 */     if (model.hasMaterialFlag(1)) {
/* 171 */       this.translucentBlocksAndItems.submit((TranslucentSubmit)submit);
/*     */     } else {
/* 173 */       this.solid.submit((SubmitNode)submit);
/*     */     } 
/* 176 */     if (outlineColor != 0)
/* 177 */       this.outline.submit((SubmitNode)new MovingBlockFeatureRenderer.Submit((Matrix4fc)new Matrix4f((Matrix4fc)poseStack.last().pose()), movingBlockRenderState, outlineColor)); 
/*     */   }
/*     */   
/*     */   public void submitBlockModel(PoseStack poseStack, RenderType renderType, List<BlockStateModelPart> modelParts, int[] tintLayers, int lightCoords, int overlayCoords, int outlineColor) {
/* 183 */     PoseStack.Pose pose = poseStack.last().copy();
/* 185 */     if (!renderType.isOutline()) {
/* 186 */       BlockModelFeatureRenderer.Submit submit = new BlockModelFeatureRenderer.Submit(pose, renderType, modelParts, tintLayers, lightCoords, overlayCoords, -1, null);
/* 187 */       if (renderType.hasBlending()) {
/* 188 */         this.translucentBlocksAndItems.submit((TranslucentSubmit)submit);
/*     */       } else {
/* 190 */         this.solid.submit((SubmitNode)submit);
/*     */       } 
/*     */     } 
/* 194 */     if (outlineColor != 0) {
/* 195 */       RenderType outlineRenderType = getOutlineRenderType(renderType);
/* 196 */       if (outlineRenderType != null)
/* 197 */         this.outline.submit((SubmitNode)new BlockModelFeatureRenderer.Submit(pose, outlineRenderType, modelParts, BlockModelRenderState.EMPTY_TINTS, 15728880, OverlayTexture.NO_OVERLAY, outlineColor, null)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static RenderType getOutlineRenderType(RenderType renderType) {
/* 203 */     if (renderType.isOutline())
/* 204 */       return renderType; 
/* 205 */     if (renderType.outline().isPresent())
/* 206 */       return renderType.outline().get(); 
/* 208 */     return null;
/*     */   }
/*     */   
/*     */   public void submitBreakingBlockModel(PoseStack poseStack, List<BlockStateModelPart> parts, int progress) {
/* 215 */     PoseStack.Pose pose = poseStack.last().copy();
/* 216 */     this.breakingOverlay.submit((SubmitNode)new BlockModelFeatureRenderer.Submit(pose, ModelBakery.DESTROY_TYPES.get(progress), List.copyOf(parts), BlockModelRenderState.EMPTY_TINTS, 15728880, OverlayTexture.NO_OVERLAY, 0, pose));
/*     */   }
/*     */   
/*     */   public void submitShapeOutline(PoseStack poseStack, VoxelShape shape, RenderType renderType, int color, float width, boolean afterTerrain) {
/* 221 */     ShapeOutlineFeatureRenderer.Submit submit = new ShapeOutlineFeatureRenderer.Submit(poseStack.last().copy(), shape, renderType, color, width);
/* 222 */     if (afterTerrain) {
/* 223 */       this.afterTerrain.submit((SubmitNode)submit);
/*     */     } else {
/* 225 */       this.shapeOutlines.submit((SubmitNode)submit);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submitItem(PoseStack poseStack, ItemDisplayContext displayContext, int lightCoords, int overlayCoords, int outlineColor, int[] tintLayers, List<BakedQuad> quads, ItemStackRenderState.FoilType foilType) {
/* 231 */     PoseStack.Pose pose = poseStack.last().copy();
/* 233 */     ItemFeatureRenderer.Submit submit = new ItemFeatureRenderer.Submit(pose, displayContext, lightCoords, overlayCoords, 0, tintLayers, quads, foilType);
/* 234 */     if (submit.hasTranslucency()) {
/* 235 */       this.translucentBlocksAndItems.submit((TranslucentSubmit)submit);
/*     */     } else {
/* 237 */       this.solid.submit((SubmitNode)submit);
/*     */     } 
/* 240 */     if (outlineColor != 0)
/* 241 */       this.outline.submit((SubmitNode)new ItemFeatureRenderer.Submit(pose, displayContext, 15728880, OverlayTexture.NO_OVERLAY, outlineColor, ItemStackRenderState.LayerRenderState.EMPTY_TINTS, quads, ItemStackRenderState.FoilType.NONE)); 
/*     */   }
/*     */   
/*     */   public void submitCustomGeometry(PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer) {
/* 247 */     CustomFeatureRenderer.Submit submit = new CustomFeatureRenderer.Submit(poseStack.last().copy(), renderType, customGeometryRenderer);
/* 248 */     if (renderType.isOutline()) {
/* 249 */       this.outline.submit((SubmitNode)submit);
/* 250 */     } else if (renderType.hasBlending()) {
/* 251 */       this.translucentCustomGeometry.submit((SubmitNode)submit);
/*     */     } else {
/* 253 */       this.solid.submit((SubmitNode)submit);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void submitQuadParticleGroup(QuadParticleRenderState particles) {
/* 259 */     this.solid.submit((SubmitNode)new QuadParticleFeatureRenderer.Submit(particles, false));
/* 260 */     this.afterTerrain.submit((SubmitNode)new QuadParticleFeatureRenderer.Submit(particles, true));
/*     */   }
/*     */   
/*     */   public void submitGizmoPrimitives(DrawableGizmoPrimitives.Group group, CameraRenderState camera, boolean onTop) {
/* 265 */     GizmoFeatureRenderer.Submit submit = new GizmoFeatureRenderer.Submit(group, camera);
/* 266 */     if (onTop) {
/* 267 */       this.alwaysOnTop.submit((SubmitNode)submit);
/*     */     } else {
/* 269 */       this.gizmos.submit((SubmitNode)submit);
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<FeatureRenderPhase<?>> allPhases() {
/* 274 */     return this.allPhases;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\SubmitNodeCollection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */