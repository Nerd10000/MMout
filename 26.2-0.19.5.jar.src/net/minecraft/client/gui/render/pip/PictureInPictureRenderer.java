/*     */ package net.minecraft.client.gui.render.pip;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import net.minecraft.client.gui.render.GuiRenderer;
/*     */ import net.minecraft.client.gui.render.TextureSetup;
/*     */ import net.minecraft.client.renderer.Projection;
/*     */ import net.minecraft.client.renderer.ProjectionMatrixBuffer;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*     */ import net.minecraft.client.renderer.SubmitNodeStorage;
/*     */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*     */ import net.minecraft.client.renderer.state.gui.BlitRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*     */ import org.joml.Matrix4fStack;
/*     */ 
/*     */ public abstract class PictureInPictureRenderer<T extends PictureInPictureRenderState> implements AutoCloseable {
/*     */   private GpuTexture texture;
/*     */   
/*     */   private GpuTextureView textureView;
/*     */   
/*     */   private GpuTexture depthTexture;
/*     */   
/*     */   private GpuTextureView depthTextureView;
/*     */   
/*  32 */   private final Projection projection = new Projection();
/*     */   
/*  33 */   private final ProjectionMatrixBuffer projectionMatrixBuffer = new ProjectionMatrixBuffer("PIP - " + getClass().getSimpleName());
/*     */   
/*  35 */   private final SubmitNodeStorage submitNodeStorage = new SubmitNodeStorage();
/*     */   
/*     */   public void prepare(T renderState, GuiRenderState guiRenderState, FeatureRenderDispatcher featureRenderDispatcher, int guiScale) {
/*  38 */     int width = (renderState.x1() - renderState.x0()) * guiScale;
/*  39 */     int height = (renderState.y1() - renderState.y0()) * guiScale;
/*  41 */     boolean needsAResize = (this.texture == null || this.texture.getWidth(0) != width || this.texture.getHeight(0) != height);
/*  42 */     if (!needsAResize && textureIsReadyToBlit(renderState)) {
/*  43 */       blitTexture(renderState, guiRenderState);
/*     */       return;
/*     */     } 
/*  46 */     prepareTexturesAndProjection(needsAResize, width, height);
/*  48 */     RenderSystem.outputColorTextureOverride = this.textureView;
/*  49 */     RenderSystem.outputDepthTextureOverride = this.depthTextureView;
/*  51 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/*  52 */     modelViewStack.pushMatrix();
/*  54 */     PoseStack poseStack = new PoseStack();
/*  56 */     poseStack.translate(width / 2.0F, getTranslateY(height, guiScale), 0.0F);
/*  58 */     float scale = guiScale * renderState.scale();
/*  59 */     poseStack.scale(scale, scale, -scale);
/*  61 */     renderToTexture(renderState, poseStack, (SubmitNodeCollector)this.submitNodeStorage);
/*  62 */     featureRenderDispatcher.renderAllFeatures(this.submitNodeStorage);
/*  64 */     modelViewStack.popMatrix();
/*  65 */     RenderSystem.outputColorTextureOverride = null;
/*  66 */     RenderSystem.outputDepthTextureOverride = null;
/*  68 */     blitTexture(renderState, guiRenderState);
/*     */   }
/*     */   
/*     */   protected void blitTexture(T renderState, GuiRenderState guiRenderState) {
/*  72 */     guiRenderState.addBlitToCurrentLayer(new BlitRenderState(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, 
/*     */           
/*  74 */           TextureSetup.singleTexture(this.textureView, RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)), renderState
/*  75 */           .pose(), renderState
/*  76 */           .x0(), renderState.y0(), renderState.x1(), renderState.y1(), 0.0F, 1.0F, 1.0F, 0.0F, -1, renderState
/*     */           
/*  80 */           .scissorArea(), null));
/*     */   }
/*     */   
/*     */   private void prepareTexturesAndProjection(boolean needsAResize, int width, int height) {
/*  86 */     if (this.texture != null && needsAResize) {
/*  87 */       this.texture.close();
/*  88 */       this.texture = null;
/*  89 */       this.textureView.close();
/*  90 */       this.textureView = null;
/*  91 */       this.depthTexture.close();
/*  92 */       this.depthTexture = null;
/*  93 */       this.depthTextureView.close();
/*  94 */       this.depthTextureView = null;
/*     */     } 
/*  96 */     GpuDevice device = RenderSystem.getDevice();
/*  97 */     if (this.texture == null) {
/*  98 */       this.texture = device.createTexture(() -> "UI " + getTextureLabel() + " texture", 13, GpuFormat.RGBA8_UNORM, width, height, 1, 1);
/*  99 */       this.textureView = device.createTextureView(this.texture);
/* 100 */       this.depthTexture = device.createTexture(() -> "UI " + getTextureLabel() + " depth texture", 9, GpuFormat.D32_FLOAT, width, height, 1, 1);
/* 101 */       this.depthTextureView = device.createTextureView(this.depthTexture);
/*     */     } 
/* 103 */     device.createCommandEncoder().clearColorAndDepthTextures(this.texture, GuiRenderer.CLEAR_COLOR, this.depthTexture, 0.0D);
/* 105 */     this.projection.setupOrtho(-1000.0F, 1000.0F, width, height, true);
/* 106 */     RenderSystem.setProjectionMatrix(this.projectionMatrixBuffer.getBuffer(this.projection), ProjectionType.ORTHOGRAPHIC);
/*     */   }
/*     */   
/*     */   protected boolean textureIsReadyToBlit(T renderState) {
/* 110 */     return false;
/*     */   }
/*     */   
/*     */   protected float getTranslateY(int height, int guiScale) {
/* 114 */     return height;
/*     */   }
/*     */   
/*     */   public void close() {
/* 119 */     if (this.texture != null)
/* 120 */       this.texture.close(); 
/* 122 */     if (this.textureView != null)
/* 123 */       this.textureView.close(); 
/* 125 */     if (this.depthTexture != null)
/* 126 */       this.depthTexture.close(); 
/* 128 */     if (this.depthTextureView != null)
/* 129 */       this.depthTextureView.close(); 
/* 131 */     this.projectionMatrixBuffer.close();
/*     */   }
/*     */   
/*     */   public abstract Class<T> getRenderStateClass();
/*     */   
/*     */   protected abstract void renderToTexture(T paramT, PoseStack paramPoseStack, SubmitNodeCollector paramSubmitNodeCollector);
/*     */   
/*     */   protected abstract String getTextureLabel();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\pip\PictureInPictureRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */