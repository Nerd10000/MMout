/*     */ package net.minecraft.client.gui.render;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.font.TextRenderable;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.render.pip.OversizedItemRenderer;
/*     */ import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
/*     */ import net.minecraft.client.renderer.CubeMap;
/*     */ import net.minecraft.client.renderer.Projection;
/*     */ import net.minecraft.client.renderer.ProjectionMatrixBuffer;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.StagedVertexBuffer;
/*     */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*     */ import net.minecraft.client.renderer.item.TrackingItemStackRenderState;
/*     */ import net.minecraft.client.renderer.state.WindowRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.BlitRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GlyphRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiElementRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiItemRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.GuiTextRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.pip.OversizedItemRenderState;
/*     */ import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.joml.Matrix3x2fc;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GuiRenderer implements AutoCloseable {
/*  61 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final float MAX_GUI_Z = 10000.0F;
/*     */   
/*     */   public static final float MIN_GUI_Z = 0.0F;
/*     */   
/*     */   private static final float GUI_Z_NEAR = 1000.0F;
/*     */   
/*     */   public static final int GUI_3D_Z_FAR = 1000;
/*     */   
/*     */   public static final int GUI_3D_Z_NEAR = -1000;
/*     */   
/*     */   public static final int DEFAULT_ITEM_SIZE = 16;
/*     */   
/*  71 */   public static final Vector4fc CLEAR_COLOR = (Vector4fc)new Vector4f(0.0F);
/*     */   
/*  73 */   private static final Comparator<ScreenRectangle> SCISSOR_COMPARATOR = Comparator.nullsFirst(
/*  74 */       Comparator.comparing(ScreenRectangle::top)
/*  75 */       .thenComparing(ScreenRectangle::bottom)
/*  76 */       .thenComparing(ScreenRectangle::left)
/*  77 */       .thenComparing(ScreenRectangle::right));
/*     */   
/*  80 */   private static final Comparator<TextureSetup> TEXTURE_COMPARATOR = Comparator.nullsFirst(
/*  81 */       Comparator.comparing(TextureSetup::getSortKey));
/*     */   
/*  85 */   private static final Comparator<GuiElementRenderState> ELEMENT_SORT_COMPARATOR = Comparator.comparing(GuiElementRenderState::scissorArea, SCISSOR_COMPARATOR)
/*  86 */     .thenComparing(GuiElementRenderState::pipeline, Comparator.comparing(RenderPipeline::getSortKey))
/*  87 */     .thenComparing(GuiElementRenderState::textureSetup, TEXTURE_COMPARATOR);
/*     */   
/*  89 */   private final Map<Object, OversizedItemRenderer> oversizedItemRenderers = (Map<Object, OversizedItemRenderer>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private final GuiRenderState renderState;
/*     */   
/*  93 */   private final List<Draw> draws = new ArrayList<>();
/*     */   
/*  94 */   private final StagedVertexBuffer vertexBuffer = new StagedVertexBuffer(() -> "GUI Vertex Buffer", 786432);
/*     */   
/*  95 */   private int firstDrawIndexAfterBlur = Integer.MAX_VALUE;
/*     */   
/*  97 */   private final Projection guiProjection = new Projection();
/*     */   
/*  98 */   private final ProjectionMatrixBuffer guiProjectionMatrixBuffer = new ProjectionMatrixBuffer("gui");
/*     */   
/*     */   private final FeatureRenderDispatcher featureRenderDispatcher;
/*     */   
/*     */   private final Map<Class<? extends PictureInPictureRenderState>, PictureInPictureRenderer<?>> pictureInPictureRenderers;
/*     */   
/*     */   private GuiItemAtlas itemAtlas;
/*     */   
/*     */   private int cachedGuiScale;
/*     */   
/* 106 */   private final CubeMap cubeMap = new CubeMap(Identifier.withDefaultNamespace("textures/gui/title/background/panorama"));
/*     */   
/*     */   private ScreenRectangle previousScissorArea;
/*     */   
/*     */   private RenderPipeline previousPipeline;
/*     */   
/*     */   private TextureSetup previousTextureSetup;
/*     */   
/*     */   private StagedVertexBuffer.Draw previousDraw;
/*     */   
/*     */   private static final class Draw extends Record {
/*     */     private final StagedVertexBuffer.Draw draw;
/*     */     
/*     */     private final RenderPipeline pipeline;
/*     */     
/*     */     private final TextureSetup textureSetup;
/*     */     
/*     */     private final ScreenRectangle scissorArea;
/*     */     
/*     */     private Draw(StagedVertexBuffer.Draw draw, RenderPipeline pipeline, TextureSetup textureSetup, ScreenRectangle scissorArea) {
/* 108 */       this.draw = draw;
/* 108 */       this.pipeline = pipeline;
/* 108 */       this.textureSetup = textureSetup;
/* 108 */       this.scissorArea = scissorArea;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/GuiRenderer$Draw;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$Draw;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/GuiRenderer$Draw;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$Draw;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/GuiRenderer$Draw;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #108	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/render/GuiRenderer$Draw;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public StagedVertexBuffer.Draw draw() {
/* 108 */       return this.draw;
/*     */     }
/*     */     
/*     */     public RenderPipeline pipeline() {
/* 108 */       return this.pipeline;
/*     */     }
/*     */     
/*     */     public TextureSetup textureSetup() {
/* 108 */       return this.textureSetup;
/*     */     }
/*     */     
/*     */     public ScreenRectangle scissorArea() {
/* 108 */       return this.scissorArea;
/*     */     }
/*     */   }
/*     */   
/*     */   public void endFrame() {
/* 122 */     if (this.itemAtlas != null)
/* 123 */       this.itemAtlas.endFrame(); 
/*     */   }
/*     */   
/*     */   public GuiRenderer(GuiRenderState renderState, FeatureRenderDispatcher featureRenderDispatcher, List<PictureInPictureRenderer<?>> pictureInPictureRenderers) {
/* 127 */     this.previousScissorArea = null;
/* 128 */     this.previousPipeline = null;
/* 129 */     this.previousTextureSetup = null;
/*     */     this.renderState = renderState;
/*     */     this.featureRenderDispatcher = featureRenderDispatcher;
/*     */     ImmutableMap.Builder<Class<? extends PictureInPictureRenderState>, PictureInPictureRenderer<?>> builder = ImmutableMap.builder();
/*     */     for (PictureInPictureRenderer<?> pictureInPictureRenderer : pictureInPictureRenderers)
/*     */       builder.put(pictureInPictureRenderer.getRenderStateClass(), pictureInPictureRenderer); 
/*     */     this.pictureInPictureRenderers = (Map<Class<? extends PictureInPictureRenderState>, PictureInPictureRenderer<?>>)builder.buildOrThrow();
/*     */   }
/*     */   
/*     */   public void render() {
/* 133 */     ProfilerFiller profiler = Profiler.get();
/* 135 */     if (this.renderState.panoramaRenderState != null)
/* 136 */       this.cubeMap.render(10.0F, this.renderState.panoramaRenderState.spin()); 
/* 139 */     profiler.push("prepare");
/* 140 */     prepare();
/* 142 */     profiler.popPush("upload");
/* 143 */     this.vertexBuffer.upload();
/* 145 */     profiler.popPush("draw");
/* 146 */     draw();
/* 148 */     profiler.popPush("endFrame");
/* 149 */     this.vertexBuffer.endDraw();
/* 150 */     this.vertexBuffer.endFrame();
/* 151 */     this.draws.clear();
/* 152 */     this.renderState.reset();
/* 153 */     this.firstDrawIndexAfterBlur = Integer.MAX_VALUE;
/* 154 */     clearUnusedOversizedItemRenderers();
/* 155 */     if (SharedConstants.DEBUG_SHUFFLE_UI_RENDERING_ORDER) {
/* 156 */       RenderPipeline.updateSortKeySeed();
/* 157 */       TextureSetup.updateSortKeySeed();
/*     */     } 
/* 159 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void clearUnusedOversizedItemRenderers() {
/* 163 */     Iterator<Map.Entry<Object, OversizedItemRenderer>> oversizedItemRendererIterator = this.oversizedItemRenderers.entrySet().iterator();
/* 164 */     while (oversizedItemRendererIterator.hasNext()) {
/* 165 */       Map.Entry<Object, OversizedItemRenderer> next = oversizedItemRendererIterator.next();
/* 166 */       OversizedItemRenderer renderer = next.getValue();
/* 167 */       if (!renderer.usedOnThisFrame()) {
/* 168 */         renderer.close();
/* 169 */         oversizedItemRendererIterator.remove();
/*     */         continue;
/*     */       } 
/* 171 */       renderer.resetUsedOnThisFrame();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void prepare() {
/* 177 */     preparePictureInPicture();
/* 178 */     prepareItemElements();
/* 179 */     prepareText();
/* 181 */     this.renderState.sortElements(ELEMENT_SORT_COMPARATOR);
/* 183 */     addElementsToMeshes(GuiRenderState.TraverseRange.BEFORE_BLUR);
/* 185 */     this.firstDrawIndexAfterBlur = this.draws.size();
/* 187 */     addElementsToMeshes(GuiRenderState.TraverseRange.AFTER_BLUR);
/*     */   }
/*     */   
/*     */   private void addElementsToMeshes(GuiRenderState.TraverseRange range) {
/* 191 */     this.previousScissorArea = null;
/* 192 */     this.previousPipeline = null;
/* 193 */     this.previousTextureSetup = null;
/* 194 */     this.previousDraw = null;
/* 195 */     this.renderState.forEachElement(this::addElementToMesh, range);
/*     */   }
/*     */   
/*     */   private void draw() {
/* 199 */     if (this.draws.isEmpty())
/*     */       return; 
/* 203 */     Minecraft minecraft = Minecraft.getInstance();
/* 205 */     WindowRenderState windowState = (minecraft.gameRenderer.gameRenderState()).windowRenderState;
/* 207 */     this.guiProjection.setupOrtho(1000.0F, 11000.0F, windowState.width / windowState.guiScale, windowState.height / windowState.guiScale, true);
/* 208 */     RenderSystem.setProjectionMatrix(this.guiProjectionMatrixBuffer.getBuffer(this.guiProjection), ProjectionType.ORTHOGRAPHIC);
/* 210 */     RenderTarget mainRenderTarget = minecraft.gameRenderer.mainRenderTarget();
/* 211 */     GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms().writeTransform((new Matrix4f()).setTranslation(0.0F, 0.0F, -11000.0F));
/* 212 */     if (this.firstDrawIndexAfterBlur > 0)
/* 213 */       executeDrawRange(() -> "GUI before blur", mainRenderTarget, dynamicTransforms, 0, Math.min(this.firstDrawIndexAfterBlur, this.draws.size())); 
/* 216 */     if (this.draws.size() <= this.firstDrawIndexAfterBlur)
/*     */       return; 
/* 220 */     RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(mainRenderTarget.getDepthTexture(), 0.0D);
/* 221 */     minecraft.gameRenderer.processBlurEffect();
/* 223 */     executeDrawRange(() -> "GUI after blur", mainRenderTarget, dynamicTransforms, this.firstDrawIndexAfterBlur, this.draws.size());
/*     */   }
/*     */   
/*     */   private void executeDrawRange(Supplier<String> label, RenderTarget mainRenderTarget, GpuBufferSlice dynamicTransforms, int startIndex, int endIndex) {
/* 227 */     RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(label, mainRenderTarget.getColorTextureView(), Optional.empty(), mainRenderTarget.useDepth ? mainRenderTarget.getDepthTextureView() : null, OptionalDouble.empty());
/*     */     try {
/* 228 */       RenderSystem.bindDefaultUniforms(renderPass);
/* 229 */       renderPass.setUniform("DynamicTransforms", dynamicTransforms);
/* 230 */       for (int i = startIndex; i < endIndex; i++) {
/* 231 */         Draw draw = this.draws.get(i);
/* 232 */         executeDraw(draw, renderPass);
/*     */       } 
/* 234 */       if (renderPass != null)
/* 234 */         renderPass.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (renderPass != null)
/*     */         try {
/*     */           renderPass.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addElementToMesh(GuiElementRenderState elementState) {
/* 238 */     RenderPipeline pipeline = elementState.pipeline();
/* 239 */     TextureSetup textureSetup = elementState.textureSetup();
/* 240 */     ScreenRectangle scissorArea = elementState.scissorArea();
/* 241 */     if (this.previousDraw == null || pipeline != this.previousPipeline || scissorChanged(scissorArea, this.previousScissorArea) || !textureSetup.equals(this.previousTextureSetup)) {
/* 242 */       this.previousPipeline = pipeline;
/* 243 */       this.previousTextureSetup = textureSetup;
/* 244 */       this.previousScissorArea = scissorArea;
/* 245 */       this.previousDraw = this.vertexBuffer.appendDraw(pipeline.getVertexFormatBinding(0), pipeline.getPrimitiveTopology());
/* 246 */       this.draws.add(new Draw(this.previousDraw, pipeline, textureSetup, scissorArea));
/*     */     } 
/* 248 */     elementState.buildVertices(this.vertexBuffer.getVertexBuilder(Objects.<StagedVertexBuffer.Draw>requireNonNull(this.previousDraw)));
/*     */   }
/*     */   
/*     */   private void prepareText() {
/* 252 */     this.renderState.forEachText(text -> {
/*     */           final Matrix3x2fc pose = text.pose;
/*     */           final ScreenRectangle scissor = text.scissor;
/*     */           text.ensurePrepared().visit(new Font.GlyphVisitor() {
/*     */                 public void acceptRenderable(TextRenderable renderable) {
/* 258 */                   GuiRenderer.this.renderState.addGlyphToCurrentLayer((GuiElementRenderState)new GlyphRenderState(pose, renderable, scissor));
/*     */                 }
/*     */               });
/*     */         });
/*     */   }
/*     */   
/*     */   private void prepareItemElements() {
/* 265 */     Set<Object> itemsInFrame = this.renderState.getItemModelIdentities();
/* 266 */     if (itemsInFrame.isEmpty())
/*     */       return; 
/* 270 */     int guiScale = getGuiScaleInvalidatingItemAtlasIfChanged();
/* 271 */     GuiItemAtlas itemAtlas = prepareItemAtlas(itemsInFrame, 16 * guiScale);
/* 273 */     MutableBoolean hasOversizedItems = new MutableBoolean(false);
/* 274 */     this.renderState.forEachItem(itemState -> {
/*     */           if (itemState.oversizedItemBounds() != null) {
/*     */             hasOversizedItems.setTrue();
/*     */             return;
/*     */           } 
/*     */           GuiItemAtlas.SlotView slotView = itemAtlas.getOrUpdate(itemState.itemStackRenderState());
/*     */           if (slotView != null)
/*     */             submitBlitFromItemAtlas(itemState, slotView); 
/*     */         });
/* 285 */     if (hasOversizedItems.booleanValue())
/* 286 */       this.renderState.forEachItem(itemState -> {
/*     */             if (itemState.oversizedItemBounds() != null) {
/*     */               TrackingItemStackRenderState itemStackRenderState = itemState.itemStackRenderState();
/*     */               OversizedItemRenderer oversizedItemRenderer = this.oversizedItemRenderers.computeIfAbsent(itemStackRenderState.getModelIdentity(), ());
/*     */               ScreenRectangle actualItemBounds = itemState.oversizedItemBounds();
/*     */               OversizedItemRenderState oversizedItemRenderState = new OversizedItemRenderState(itemState, actualItemBounds.left(), actualItemBounds.top(), actualItemBounds.right(), actualItemBounds.bottom());
/*     */               oversizedItemRenderer.prepare((PictureInPictureRenderState)oversizedItemRenderState, this.renderState, this.featureRenderDispatcher, guiScale);
/*     */             } 
/*     */           }); 
/*     */   }
/*     */   
/*     */   private void preparePictureInPicture() {
/* 300 */     int guiScale = ((Minecraft.getInstance()).gameRenderer.gameRenderState()).windowRenderState.guiScale;
/* 301 */     this.renderState.forEachPictureInPicture(pictureInPictureState -> preparePictureInPictureState(pictureInPictureState, guiScale));
/*     */   }
/*     */   
/*     */   private <T extends PictureInPictureRenderState> void preparePictureInPictureState(T picturesInPictureState, int guiScale) {
/* 307 */     PictureInPictureRenderer<T> renderer = (PictureInPictureRenderer<T>)this.pictureInPictureRenderers.get(picturesInPictureState.getClass());
/* 308 */     if (renderer != null)
/* 309 */       renderer.prepare((PictureInPictureRenderState)picturesInPictureState, this.renderState, this.featureRenderDispatcher, guiScale); 
/*     */   }
/*     */   
/*     */   private void submitBlitFromItemAtlas(GuiItemRenderState itemState, GuiItemAtlas.SlotView slotView) {
/* 314 */     this.renderState.addBlitToCurrentLayer(new BlitRenderState(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, 
/*     */           
/* 316 */           TextureSetup.singleTexture(slotView.textureView(), RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST)), (Matrix3x2fc)itemState
/* 317 */           .pose(), itemState
/* 318 */           .x(), itemState.y(), itemState.x() + 16, itemState.y() + 16, slotView
/* 319 */           .u0(), slotView.u1(), slotView
/* 320 */           .v0(), slotView.v1(), -1, itemState
/*     */           
/* 322 */           .scissorArea(), null));
/*     */   }
/*     */   
/*     */   private GuiItemAtlas prepareItemAtlas(Set<Object> itemsInFrame, int slotTextureSize) {
/* 328 */     if (this.itemAtlas != null && this.itemAtlas.tryPrepareFor(itemsInFrame))
/* 329 */       return this.itemAtlas; 
/* 332 */     int newTextureSize = GuiItemAtlas.computeTextureSizeFor(slotTextureSize, itemsInFrame.size());
/* 333 */     if (this.itemAtlas != null && this.itemAtlas.textureSize() == newTextureSize) {
/* 335 */       LOGGER.warn("Too many items ({}) in UI, some will be skipped! (Reached maximum texture size {}x{})", new Object[] { Integer.valueOf(itemsInFrame.size()), Integer.valueOf(newTextureSize), Integer.valueOf(newTextureSize) });
/* 336 */       return this.itemAtlas;
/*     */     } 
/* 339 */     if (this.itemAtlas != null)
/* 340 */       this.itemAtlas.close(); 
/* 342 */     this.itemAtlas = new GuiItemAtlas(this.featureRenderDispatcher, newTextureSize, slotTextureSize);
/* 343 */     return this.itemAtlas;
/*     */   }
/*     */   
/*     */   private int getGuiScaleInvalidatingItemAtlasIfChanged() {
/* 347 */     int guiScale = ((Minecraft.getInstance()).gameRenderer.gameRenderState()).windowRenderState.guiScale;
/* 348 */     if (guiScale != this.cachedGuiScale) {
/* 349 */       invalidateItemAtlas();
/* 350 */       for (OversizedItemRenderer renderer : this.oversizedItemRenderers.values())
/* 351 */         renderer.invalidateTexture(); 
/* 353 */       this.cachedGuiScale = guiScale;
/*     */     } 
/* 355 */     return guiScale;
/*     */   }
/*     */   
/*     */   private void invalidateItemAtlas() {
/* 359 */     if (this.itemAtlas != null) {
/* 360 */       this.itemAtlas.close();
/* 361 */       this.itemAtlas = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void executeDraw(Draw draw, RenderPass renderPass) {
/* 366 */     StagedVertexBuffer.ExecuteInfo executeInfo = this.vertexBuffer.getExecuteInfo(draw.draw);
/* 367 */     if (executeInfo == null)
/*     */       return; 
/* 371 */     RenderPipeline pipeline = draw.pipeline();
/* 373 */     renderPass.setPipeline(pipeline);
/* 374 */     renderPass.setVertexBuffer(0, executeInfo.vertexBuffer().slice());
/* 376 */     ScreenRectangle scissorArea = draw.scissorArea();
/* 377 */     if (scissorArea != null) {
/* 378 */       enableScissor(scissorArea, renderPass);
/*     */     } else {
/* 380 */       renderPass.disableScissor();
/*     */     } 
/* 383 */     if (draw.textureSetup.texure0() != null)
/* 384 */       renderPass.bindTexture("Sampler0", draw.textureSetup.texure0(), draw.textureSetup.sampler0()); 
/* 386 */     if (draw.textureSetup.texure1() != null)
/* 387 */       renderPass.bindTexture("Sampler1", draw.textureSetup.texure1(), draw.textureSetup.sampler1()); 
/* 389 */     if (draw.textureSetup.texure2() != null)
/* 390 */       renderPass.bindTexture("Sampler2", draw.textureSetup.texure2(), draw.textureSetup.sampler2()); 
/* 392 */     renderPass.setIndexBuffer(executeInfo.indexBuffer(), executeInfo.indexType());
/* 394 */     renderPass.drawIndexed(executeInfo.indexCount(), 1, executeInfo.firstIndex(), executeInfo.baseVertex(), 0);
/*     */   }
/*     */   
/*     */   private boolean scissorChanged(ScreenRectangle newScissor, ScreenRectangle oldScissor) {
/* 398 */     if (newScissor == oldScissor)
/* 399 */       return false; 
/* 401 */     if (newScissor != null)
/* 402 */       return !newScissor.equals(oldScissor); 
/* 404 */     return true;
/*     */   }
/*     */   
/*     */   private void enableScissor(ScreenRectangle rectangle, RenderPass renderPass) {
/* 408 */     WindowRenderState window = ((Minecraft.getInstance()).gameRenderer.gameRenderState()).windowRenderState;
/* 409 */     int guiScale = window.guiScale;
/* 412 */     double left = (rectangle.left() * guiScale);
/* 413 */     double top = (rectangle.top() * guiScale);
/* 414 */     double right = Math.min(rectangle.right() * guiScale, window.width);
/* 415 */     double bottom = Math.min(rectangle.bottom() * guiScale, window.height);
/* 418 */     renderPass.enableScissor((int)left, window.height - (int)bottom, Math.max(0, (int)(right - left)), Math.max(0, (int)(bottom - top)));
/*     */   }
/*     */   
/*     */   public void registerPanoramaTextures(TextureManager textureManager) {
/* 422 */     this.cubeMap.registerTextures(textureManager);
/*     */   }
/*     */   
/*     */   public void close() {
/* 427 */     this.vertexBuffer.close();
/* 428 */     if (this.itemAtlas != null) {
/* 429 */       this.itemAtlas.close();
/* 430 */       this.itemAtlas = null;
/*     */     } 
/* 432 */     this.pictureInPictureRenderers.values().forEach(PictureInPictureRenderer::close);
/* 433 */     this.guiProjectionMatrixBuffer.close();
/* 434 */     this.oversizedItemRenderers.values().forEach(PictureInPictureRenderer::close);
/* 435 */     this.cubeMap.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\render\GuiRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */