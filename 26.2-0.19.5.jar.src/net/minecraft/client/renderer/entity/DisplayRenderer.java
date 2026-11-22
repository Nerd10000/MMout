/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*     */ import com.mojang.math.Axis;
/*     */ import com.mojang.math.Transformation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.block.BlockModelResolver;
/*     */ import net.minecraft.client.renderer.block.model.BlockDisplayContext;
/*     */ import net.minecraft.client.renderer.entity.state.BlockDisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.DisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.ItemDisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.TextDisplayEntityRenderState;
/*     */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.LightCoordsUtil;
/*     */ import net.minecraft.world.entity.Display;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Quaternionf;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public abstract class DisplayRenderer<T extends Display, S, ST extends DisplayEntityRenderState> extends EntityRenderer<T, ST> {
/*  35 */   public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
/*     */   
/*     */   private final EntityRenderDispatcher entityRenderDispatcher;
/*     */   
/*     */   protected final BlockModelResolver blockModelResolver;
/*     */   
/*     */   protected DisplayRenderer(EntityRendererProvider.Context context) {
/*  41 */     super(context);
/*  42 */     this.entityRenderDispatcher = context.getEntityRenderDispatcher();
/*  43 */     this.blockModelResolver = context.getBlockModelResolver();
/*     */   }
/*     */   
/*     */   protected AABB getBoundingBoxForCulling(T entity) {
/*  48 */     return entity.getBoundingBoxForCulling();
/*     */   }
/*     */   
/*     */   protected boolean affectedByCulling(T entity) {
/*  53 */     return entity.affectedByCulling();
/*     */   }
/*     */   
/*     */   private static int getBrightnessOverride(Display entity) {
/*  57 */     Display.RenderState renderState = entity.renderState();
/*  58 */     return (renderState != null) ? renderState.brightnessOverride() : -1;
/*     */   }
/*     */   
/*     */   protected int getSkyLightLevel(T entity, BlockPos blockPos) {
/*  63 */     int packedBrightnessOverride = getBrightnessOverride((Display)entity);
/*  64 */     if (packedBrightnessOverride != -1)
/*  65 */       return LightCoordsUtil.sky(packedBrightnessOverride); 
/*  67 */     return super.getSkyLightLevel(entity, blockPos);
/*     */   }
/*     */   
/*     */   protected int getBlockLightLevel(T entity, BlockPos blockPos) {
/*  72 */     int packedBrightnessOverride = getBrightnessOverride((Display)entity);
/*  73 */     if (packedBrightnessOverride != -1)
/*  74 */       return LightCoordsUtil.block(packedBrightnessOverride); 
/*  76 */     return super.getBlockLightLevel(entity, blockPos);
/*     */   }
/*     */   
/*     */   protected float getShadowRadius(ST state) {
/*  81 */     Display.RenderState renderState = ((DisplayEntityRenderState)state).renderState;
/*  82 */     if (renderState == null)
/*  83 */       return 0.0F; 
/*  85 */     return renderState.shadowRadius().get(((DisplayEntityRenderState)state).interpolationProgress);
/*     */   }
/*     */   
/*     */   protected float getShadowStrength(ST state) {
/*  90 */     Display.RenderState renderState = ((DisplayEntityRenderState)state).renderState;
/*  91 */     if (renderState == null)
/*  92 */       return 0.0F; 
/*  94 */     return renderState.shadowStrength().get(((DisplayEntityRenderState)state).interpolationProgress);
/*     */   }
/*     */   
/*     */   public void submit(ST state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/*  99 */     Display.RenderState renderState = ((DisplayEntityRenderState)state).renderState;
/* 100 */     if (renderState == null || !state.hasSubState())
/*     */       return; 
/* 103 */     float interpolationProgress = ((DisplayEntityRenderState)state).interpolationProgress;
/* 105 */     super.submit(state, poseStack, submitNodeCollector, camera);
/* 107 */     poseStack.pushPose();
/* 108 */     poseStack.mulPose((Quaternionfc)calculateOrientation(renderState, state, new Quaternionf()));
/* 109 */     Transformation transformation = (Transformation)renderState.transformation().get(interpolationProgress);
/* 110 */     poseStack.mulPose(transformation);
/* 111 */     submitInner(state, poseStack, submitNodeCollector, ((DisplayEntityRenderState)state).lightCoords, interpolationProgress);
/* 112 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private Quaternionf calculateOrientation(Display.RenderState renderState, ST state, Quaternionf output) {
/* 116 */     switch (renderState.billboardConstraints()) {
/*     */       default:
/* 116 */         throw new MatchException(null, null);
/*     */       case LEFT:
/*     */       
/*     */       case RIGHT:
/*     */       
/*     */       case CENTER:
/*     */       
/*     */       case null:
/*     */         break;
/*     */     } 
/* 116 */     return 
/*     */       
/* 124 */       output.rotationYXZ(-0.017453292F * transformYRot(((DisplayEntityRenderState)state).cameraYRot), 0.017453292F * transformXRot(((DisplayEntityRenderState)state).cameraXRot), 0.0F);
/*     */   }
/*     */   
/*     */   private static float transformYRot(float cameraYRot) {
/* 129 */     return cameraYRot - 180.0F;
/*     */   }
/*     */   
/*     */   private static float transformXRot(float cameraXRot) {
/* 133 */     return -cameraXRot;
/*     */   }
/*     */   
/*     */   private static <T extends Display> float entityYRot(T entity, float partialTicks) {
/* 137 */     return entity.getYRot(partialTicks);
/*     */   }
/*     */   
/*     */   private static <T extends Display> float entityXRot(T entity, float partialTicks) {
/* 141 */     return entity.getXRot(partialTicks);
/*     */   }
/*     */   
/*     */   public void extractRenderState(T entity, ST state, float partialTicks) {
/* 148 */     super.extractRenderState(entity, state, partialTicks);
/* 149 */     ((DisplayEntityRenderState)state).renderState = entity.renderState();
/* 150 */     ((DisplayEntityRenderState)state).interpolationProgress = entity.calculateInterpolationProgress(partialTicks);
/* 151 */     ((DisplayEntityRenderState)state).entityYRot = entityYRot(entity, partialTicks);
/* 152 */     ((DisplayEntityRenderState)state).entityXRot = entityXRot(entity, partialTicks);
/* 153 */     Camera camera = this.entityRenderDispatcher.camera;
/* 154 */     ((DisplayEntityRenderState)state).cameraXRot = camera.xRot();
/* 155 */     ((DisplayEntityRenderState)state).cameraYRot = camera.yRot();
/*     */   }
/*     */   
/*     */   protected abstract void submitInner(ST paramST, PoseStack paramPoseStack, SubmitNodeCollector paramSubmitNodeCollector, int paramInt, float paramFloat);
/*     */   
/*     */   public static class BlockDisplayRenderer extends DisplayRenderer<Display.BlockDisplay, Display.BlockDisplay.BlockRenderState, BlockDisplayEntityRenderState> {
/*     */     protected BlockDisplayRenderer(EntityRendererProvider.Context context) {
/* 160 */       super(context);
/*     */     }
/*     */     
/*     */     public BlockDisplayEntityRenderState createRenderState() {
/* 165 */       return new BlockDisplayEntityRenderState();
/*     */     }
/*     */     
/*     */     public void extractRenderState(Display.BlockDisplay entity, BlockDisplayEntityRenderState state, float partialTicks) {
/* 170 */       super.extractRenderState(entity, state, partialTicks);
/* 171 */       Display.BlockDisplay.BlockRenderState blockRenderState = entity.blockRenderState();
/* 172 */       if (blockRenderState != null) {
/* 173 */         this.blockModelResolver.update(state.blockModel, blockRenderState.blockState(), BLOCK_DISPLAY_CONTEXT);
/*     */       } else {
/* 175 */         state.blockModel.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void submitInner(BlockDisplayEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress) {
/* 181 */       state.blockModel.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ItemDisplayRenderer extends DisplayRenderer<Display.ItemDisplay, Display.ItemDisplay.ItemRenderState, ItemDisplayEntityRenderState> {
/*     */     private final ItemModelResolver itemModelResolver;
/*     */     
/*     */     protected ItemDisplayRenderer(EntityRendererProvider.Context context) {
/* 189 */       super(context);
/* 190 */       this.itemModelResolver = context.getItemModelResolver();
/*     */     }
/*     */     
/*     */     public ItemDisplayEntityRenderState createRenderState() {
/* 195 */       return new ItemDisplayEntityRenderState();
/*     */     }
/*     */     
/*     */     public void extractRenderState(Display.ItemDisplay entity, ItemDisplayEntityRenderState state, float partialTicks) {
/* 200 */       super.extractRenderState(entity, state, partialTicks);
/* 201 */       Display.ItemDisplay.ItemRenderState itemRenderState = entity.itemRenderState();
/* 202 */       if (itemRenderState != null) {
/* 203 */         this.itemModelResolver.updateForNonLiving(state.item, itemRenderState.itemStack(), itemRenderState.itemTransform(), (Entity)entity);
/*     */       } else {
/* 205 */         state.item.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     public void submitInner(ItemDisplayEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress) {
/* 211 */       if (state.item.isEmpty())
/*     */         return; 
/* 215 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotation(3.1415927F));
/* 216 */       state.item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TextDisplayRenderer extends DisplayRenderer<Display.TextDisplay, Display.TextDisplay.TextRenderState, TextDisplayEntityRenderState> {
/*     */     private static final float TEXT_BACKGROUND_OFFSET = -0.01F;
/*     */     
/*     */     private final Font font;
/*     */     
/*     */     protected TextDisplayRenderer(EntityRendererProvider.Context context) {
/* 225 */       super(context);
/* 226 */       this.font = context.getFont();
/*     */     }
/*     */     
/*     */     public TextDisplayEntityRenderState createRenderState() {
/* 231 */       return new TextDisplayEntityRenderState();
/*     */     }
/*     */     
/*     */     public void extractRenderState(Display.TextDisplay entity, TextDisplayEntityRenderState state, float partialTicks) {
/* 236 */       super.extractRenderState(entity, state, partialTicks);
/* 237 */       state.textRenderState = entity.textRenderState();
/* 238 */       state.cachedInfo = entity.cacheDisplay(this::splitLines);
/*     */     }
/*     */     
/*     */     private Display.TextDisplay.CachedInfo splitLines(Component input, int width) {
/* 242 */       List<FormattedCharSequence> lines = this.font.split((FormattedText)input, width);
/* 243 */       List<Display.TextDisplay.CachedLine> result = new ArrayList<>(lines.size());
/* 245 */       int maxLineWidth = 0;
/* 246 */       for (FormattedCharSequence line : lines) {
/* 247 */         int lineWidth = this.font.width(line);
/* 248 */         maxLineWidth = Math.max(maxLineWidth, lineWidth);
/* 249 */         result.add(new Display.TextDisplay.CachedLine(line, lineWidth));
/*     */       } 
/* 252 */       return new Display.TextDisplay.CachedInfo(result, maxLineWidth);
/*     */     }
/*     */     
/*     */     public void submitInner(TextDisplayEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float interpolationProgress) {
/*     */       int backgroundColor;
/* 257 */       Display.TextDisplay.TextRenderState renderState = state.textRenderState;
/* 258 */       byte flags = renderState.flags();
/* 260 */       boolean seeThrough = ((flags & 0x2) != 0);
/* 261 */       boolean useDefaultBackground = ((flags & 0x4) != 0);
/* 262 */       boolean shadow = ((flags & 0x1) != 0);
/* 263 */       Display.TextDisplay.Align alignment = Display.TextDisplay.getAlign(flags);
/* 264 */       byte textOpacity = (byte)renderState.textOpacity().get(interpolationProgress);
/* 266 */       if (useDefaultBackground) {
/* 267 */         float backgroundAlpha = ((Minecraft.getInstance()).gameRenderer.gameRenderState()).optionsRenderState.getBackgroundOpacity(0.25F);
/* 268 */         backgroundColor = (int)(backgroundAlpha * 255.0F) << 24;
/*     */       } else {
/* 270 */         backgroundColor = renderState.backgroundColor().get(interpolationProgress);
/*     */       } 
/* 273 */       float y = 0.0F;
/* 275 */       Matrix4f pose = poseStack.last().pose();
/* 277 */       pose.rotate(3.1415927F, 0.0F, 1.0F, 0.0F);
/* 278 */       pose.scale(-0.025F, -0.025F, -0.025F);
/* 279 */       Display.TextDisplay.CachedInfo cachedInfo = state.cachedInfo;
/* 281 */       int lineSpacing = 1;
/* 282 */       Objects.requireNonNull(this.font);
/* 282 */       int lineHeight = 9 + 1;
/* 284 */       int width = cachedInfo.width();
/* 285 */       int height = cachedInfo.lines().size() * lineHeight - 1;
/* 286 */       pose.translate(1.0F - width / 2.0F, -height, 0.0F);
/* 288 */       if (backgroundColor != 0)
/* 289 */         submitNodeCollector.submitCustomGeometry(poseStack, seeThrough ? RenderTypes.textBackgroundSeeThrough() : RenderTypes.textBackground(), (lambdaPose, buffer) -> {
/*     */               buffer.addVertex(lambdaPose, -1.0F, -1.0F, -0.01F).setColor(backgroundColor).setLight(lightCoords);
/*     */               buffer.addVertex(lambdaPose, -1.0F, height, -0.01F).setColor(backgroundColor).setLight(lightCoords);
/*     */               buffer.addVertex(lambdaPose, width, height, -0.01F).setColor(backgroundColor).setLight(lightCoords);
/*     */               buffer.addVertex(lambdaPose, width, -1.0F, -0.01F).setColor(backgroundColor).setLight(lightCoords);
/*     */             }); 
/* 297 */       OrderedSubmitNodeCollector textCollector = submitNodeCollector.order((backgroundColor != 0) ? 1 : 0);
/* 298 */       for (Display.TextDisplay.CachedLine line : cachedInfo.lines()) {
/* 299 */         switch (alignment) {
/*     */           default:
/* 299 */             throw new MatchException(null, null);
/*     */           case LEFT:
/*     */           
/*     */           case RIGHT:
/*     */           
/*     */           case CENTER:
/*     */             break;
/*     */         } 
/* 299 */         float offset = 
/*     */           
/* 302 */           width / 2.0F - line.width() / 2.0F;
/* 304 */         textCollector.submitText(poseStack, offset, y, line.contents(), shadow, seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.POLYGON_OFFSET, lightCoords, textOpacity << 24 | 0xFFFFFF, 0, 0);
/* 305 */         y += lineHeight;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\entity\DisplayRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */