/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuFormat;
/*     */ import com.mojang.blaze3d.IndexType;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.framegraph.FrameGraphBuilder;
/*     */ import com.mojang.blaze3d.framegraph.FramePass;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.pipeline.TextureTarget;
/*     */ import com.mojang.blaze3d.platform.Lighting;
/*     */ import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
/*     */ import com.mojang.blaze3d.resource.RenderTargetDescriptor;
/*     */ import com.mojang.blaze3d.resource.ResourceDescriptor;
/*     */ import com.mojang.blaze3d.resource.ResourceHandle;
/*     */ import com.mojang.blaze3d.systems.RenderPass;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.AddressMode;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongCollection;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalDouble;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.CloudStatus;
/*     */ import net.minecraft.client.DeltaTracker;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.PrioritizeChunkUpdates;
/*     */ import net.minecraft.client.TextureFilteringMethod;
/*     */ import net.minecraft.client.color.block.BlockColors;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
/*     */ import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
/*     */ import net.minecraft.client.renderer.chunk.ChunkSectionLayerGroup;
/*     */ import net.minecraft.client.renderer.chunk.ChunkSectionsToRender;
/*     */ import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
/*     */ import net.minecraft.client.renderer.chunk.SectionCompiler;
/*     */ import net.minecraft.client.renderer.chunk.SectionMesh;
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ import net.minecraft.client.renderer.chunk.TranslucencyPointOfView;
/*     */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*     */ import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.state.OptionsRenderState;
/*     */ import net.minecraft.client.renderer.state.level.BlockBreakingRenderState;
/*     */ import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.state.level.LevelRenderState;
/*     */ import net.minecraft.client.renderer.state.level.SectionUpdateRenderState;
/*     */ import net.minecraft.client.renderer.state.level.SkyRenderState;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.model.ModelManager;
/*     */ import net.minecraft.client.resources.model.sprite.AtlasManager;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.gizmos.GizmoCollector;
/*     */ import net.minecraft.gizmos.GizmoPrimitives;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.gizmos.SimpleGizmoCollector;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.level.block.LeavesBlock;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.material.FogType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Vector4f;
/*     */ import org.joml.Vector4fc;
/*     */ 
/*     */ public class LevelRenderer implements AutoCloseable {
/*  95 */   private static final Identifier TRANSPARENCY_POST_CHAIN_ID = Identifier.withDefaultNamespace("transparency");
/*     */   
/*  96 */   private static final Identifier ENTITY_OUTLINE_POST_CHAIN_ID = Identifier.withDefaultNamespace("entity_outline");
/*     */   
/*     */   private static final int MINIMUM_TRANSPARENT_SORT_COUNT = 15;
/*     */   
/*     */   private static final float CHUNK_VISIBILITY_THRESHOLD = 0.3F;
/*     */   
/* 103 */   private static final Vector4fc SCREEN_SIZE_TARGET_CLEAR_COLOR = (Vector4fc)new Vector4f(0.0F);
/*     */   
/* 104 */   private static final Vector4fc ENTITY_OUTLINE_CLEAR_COLOR = (Vector4fc)new Vector4f(0.0F);
/*     */   
/*     */   private final GameRenderer gameRenderer;
/*     */   
/*     */   private final EntityRenderDispatcher entityRenderDispatcher;
/*     */   
/*     */   private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;
/*     */   
/*     */   private final RenderBuffers renderBuffers;
/*     */   
/*     */   private final FeatureRenderDispatcher featureRenderDispatcher;
/*     */   
/* 111 */   private final SubmitNodeStorage submitNodeStorage = new SubmitNodeStorage();
/*     */   
/*     */   private final ModelManager modelManager;
/*     */   
/*     */   private final TextureManager textureManager;
/*     */   
/*     */   private final AtlasManager atlasManager;
/*     */   
/*     */   private final ShaderManager shaderManager;
/*     */   
/*     */   private final LevelRenderState levelRenderState;
/*     */   
/*     */   private final OptionsRenderState optionsRenderState;
/*     */   
/*     */   private SkyRenderer skyRenderer;
/*     */   
/* 121 */   private final CloudRenderer cloudRenderer = new CloudRenderer();
/*     */   
/* 122 */   private final WorldBorderRenderer worldBorderRenderer = new WorldBorderRenderer();
/*     */   
/* 123 */   private final WeatherEffectRenderer weatherEffectRenderer = new WeatherEffectRenderer();
/*     */   
/* 125 */   private final SectionOcclusionGraph sectionOcclusionGraph = new SectionOcclusionGraph();
/*     */   
/* 126 */   private final ObjectArrayList<SectionRenderDispatcher.RenderSection> visibleSections = new ObjectArrayList(10000);
/*     */   
/* 127 */   private final ObjectArrayList<SectionRenderDispatcher.RenderSection> nearbyVisibleSections = new ObjectArrayList(50);
/*     */   
/*     */   private ViewArea viewArea;
/*     */   
/*     */   private final RenderTarget entityOutlineTarget;
/*     */   
/* 134 */   private final LevelTargetBundle targets = new LevelTargetBundle();
/*     */   
/*     */   private SectionRenderDispatcher sectionRenderDispatcher;
/*     */   
/*     */   private BlockPos lastTranslucentSortBlockPos;
/*     */   
/*     */   private int translucencyResortIterationIndex;
/*     */   
/*     */   private GpuSampler chunkLayerSampler;
/*     */   
/* 142 */   private final SimpleGizmoCollector renderThreadGizmos = new SimpleGizmoCollector();
/*     */   
/* 145 */   private FinalizedGizmos finalizedGizmos = new FinalizedGizmos(new DrawableGizmoPrimitives(), new DrawableGizmoPrimitives());
/*     */   
/*     */   public LevelRenderer(EntityRenderDispatcher entityRenderDispatcher, BlockEntityRenderDispatcher blockEntityRenderDispatcher, ModelManager modelManager, TextureManager textureManager, AtlasManager atlasManager, ShaderManager shaderManager, GameRenderer gameRenderer, int width, int height) {
/* 148 */     this.gameRenderer = gameRenderer;
/* 149 */     this.entityRenderDispatcher = entityRenderDispatcher;
/* 150 */     this.blockEntityRenderDispatcher = blockEntityRenderDispatcher;
/* 151 */     this.renderBuffers = gameRenderer.renderBuffers();
/* 152 */     this.featureRenderDispatcher = gameRenderer.featureRenderDispatcher();
/* 153 */     this.modelManager = modelManager;
/* 154 */     this.textureManager = textureManager;
/* 155 */     this.atlasManager = atlasManager;
/* 156 */     this.shaderManager = shaderManager;
/* 158 */     this.levelRenderState = (gameRenderer.gameRenderState()).levelRenderState;
/* 159 */     this.optionsRenderState = (gameRenderer.gameRenderState()).optionsRenderState;
/* 161 */     this.entityOutlineTarget = (RenderTarget)new TextureTarget("Entity Outline", width, height, true, GpuFormat.RGBA8_UNORM);
/*     */   }
/*     */   
/*     */   public void render(GraphicsResourceAllocator resourceAllocator, DeltaTracker deltaTracker, boolean renderOutline, CameraRenderState cameraState, Matrix4fc modelViewMatrix, GpuBufferSlice terrainFog, Vector4f fogColor, boolean shouldRenderSky) {
/* 165 */     float deltaPartialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
/* 167 */     final ProfilerFiller profiler = Profiler.get();
/* 169 */     profiler.push("repositionCamera");
/* 170 */     repositionCamera(cameraState);
/* 175 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 176 */     modelViewStack.pushMatrix();
/* 177 */     modelViewStack.mul(modelViewMatrix);
/* 179 */     profiler.popPush("submitFeatures");
/* 180 */     submitFeatures(this.levelRenderState, this.submitNodeStorage, renderOutline);
/* 182 */     profiler.popPush("prepareFeatures");
/* 183 */     FeatureRenderDispatcher.PreparedFrame featureFrame = this.featureRenderDispatcher.prepareFrame(this.submitNodeStorage);
/* 185 */     profiler.popPush("setupFrameGraph");
/* 187 */     FrameGraphBuilder frame = new FrameGraphBuilder();
/* 188 */     this.targets.main = frame.importExternal("main", this.gameRenderer.mainRenderTarget());
/* 190 */     int screenWidth = (this.gameRenderer.mainRenderTarget()).width;
/* 191 */     int screenHeight = (this.gameRenderer.mainRenderTarget()).height;
/* 192 */     RenderTargetDescriptor screenSizeTargetDescriptor = new RenderTargetDescriptor(screenWidth, screenHeight, true, SCREEN_SIZE_TARGET_CLEAR_COLOR, GpuFormat.RGBA8_UNORM);
/* 193 */     PostChain transparencyChain = getTransparencyChain();
/* 194 */     if (transparencyChain != null) {
/* 195 */       this.targets.translucent = frame.createInternal("translucent", (ResourceDescriptor)screenSizeTargetDescriptor);
/* 196 */       this.targets.itemEntity = frame.createInternal("item_entity", (ResourceDescriptor)screenSizeTargetDescriptor);
/* 197 */       this.targets.particles = frame.createInternal("particles", (ResourceDescriptor)screenSizeTargetDescriptor);
/* 198 */       this.targets.weather = frame.createInternal("weather", (ResourceDescriptor)screenSizeTargetDescriptor);
/* 199 */       this.targets.clouds = frame.createInternal("clouds", (ResourceDescriptor)screenSizeTargetDescriptor);
/*     */     } 
/* 201 */     this.targets.entityOutline = frame.importExternal("entity_outline", this.entityOutlineTarget);
/* 203 */     FramePass clearPass = frame.addPass("clear");
/* 204 */     this.targets.main = clearPass.readsAndWrites(this.targets.main);
/* 205 */     clearPass.executes(() -> {
/*     */           RenderTarget mainRenderTarget = this.gameRenderer.mainRenderTarget();
/*     */           RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(mainRenderTarget.getColorTexture(), (Vector4fc)new Vector4f(fogColor.x, fogColor.y, fogColor.z, 0.0F), mainRenderTarget.getDepthTexture(), 0.0D);
/*     */         });
/* 210 */     if (shouldRenderSky)
/* 211 */       addSkyPass(frame, cameraState, terrainFog); 
/* 214 */     ChunkSectionsToRender chunkSectionsToRender = prepareChunkRenders((Matrix4fc)this.levelRenderState.cameraRenderState.viewRotationMatrix);
/* 216 */     addMainPass(frame, featureFrame, terrainFog, this.levelRenderState, profiler, chunkSectionsToRender);
/* 218 */     PostChain entityOutlineChain = this.shaderManager.getPostChain(ENTITY_OUTLINE_POST_CHAIN_ID, LevelTargetBundle.OUTLINE_TARGETS);
/* 220 */     if (featureFrame.hasAnyOutline() && entityOutlineChain != null)
/* 221 */       entityOutlineChain.addToFrame(frame, screenWidth, screenHeight, this.targets); 
/* 224 */     CloudStatus cloudStatus = this.optionsRenderState.cloudStatus;
/* 225 */     if (cloudStatus != CloudStatus.OFF && 
/* 226 */       ARGB.alpha(this.levelRenderState.cloudColor) > 0)
/* 227 */       addCloudsPass(frame, cloudStatus, this.levelRenderState.cameraRenderState.pos, this.levelRenderState.gameTime, deltaPartialTick, this.levelRenderState.cloudColor, this.levelRenderState.cloudHeight, this.optionsRenderState.cloudRange); 
/* 231 */     addWeatherPass(frame, terrainFog);
/* 233 */     if (transparencyChain != null)
/* 234 */       transparencyChain.addToFrame(frame, screenWidth, screenHeight, this.targets); 
/* 237 */     addAlwaysOnTopPass(frame, featureFrame, terrainFog);
/* 239 */     profiler.popPush("executeFrameGraph");
/* 240 */     frame.execute(resourceAllocator, new FrameGraphBuilder.Inspector(this) {
/*     */           public void beforeExecutePass(String name) {
/* 243 */             profiler.push(name);
/*     */           }
/*     */           
/*     */           public void afterExecutePass(String name) {
/* 248 */             profiler.pop();
/*     */           }
/*     */         });
/* 251 */     profiler.pop();
/* 253 */     this.targets.clear();
/* 255 */     modelViewStack.popMatrix();
/* 257 */     featureFrame.close();
/* 259 */     profiler.push("compileSections");
/* 260 */     compileSections(cameraState);
/* 261 */     profiler.pop();
/* 263 */     if (this.sectionRenderDispatcher != null) {
/* 264 */       this.sectionRenderDispatcher.lock();
/* 265 */       profiler.push("uploadTerrainBuffers");
/*     */       try {
/* 267 */         this.sectionRenderDispatcher.uploadTerrainBuffersToGpu();
/*     */       } finally {
/* 269 */         this.sectionRenderDispatcher.unlock();
/*     */       } 
/* 271 */       profiler.pop();
/*     */     } 
/* 274 */     profiler.push("updateSectionOcclusion");
/* 275 */     this.sectionOcclusionGraph.update(cameraState, this.optionsRenderState.fov, this.levelRenderState.chunkLoadingRenderState);
/* 276 */     profiler.pop();
/* 278 */     Runnable playerCompiledSectionCallback = this.levelRenderState.playerCompiledSectionCallback;
/* 279 */     if (playerCompiledSectionCallback != null && 
/* 280 */       isSectionCompiledAndVisible(this.levelRenderState.cameraRenderState.blockPos))
/* 281 */       playerCompiledSectionCallback.run(); 
/*     */   }
/*     */   
/*     */   private void submitFeatures(LevelRenderState levelRenderState, SubmitNodeCollector submitNodeCollector, boolean renderOutline) {
/* 287 */     PoseStack poseStack = new PoseStack();
/* 289 */     submitEntities(poseStack, levelRenderState, submitNodeCollector);
/* 290 */     levelRenderState.entityRenderStates.clear();
/* 292 */     submitBlockEntities(poseStack, levelRenderState, submitNodeCollector);
/* 293 */     levelRenderState.blockEntityRenderStates.clear();
/* 295 */     submitBlockDestroyAnimation(poseStack, submitNodeCollector, levelRenderState);
/* 296 */     levelRenderState.blockBreakingRenderStates.clear();
/* 299 */     levelRenderState.particlesRenderState.submit(submitNodeCollector, levelRenderState.cameraRenderState);
/* 301 */     if (renderOutline)
/* 302 */       submitBlockOutline(poseStack, this.submitNodeStorage, levelRenderState); 
/* 308 */     finalizeGizmoCollection();
/* 309 */     this.finalizedGizmos.standardPrimitives().submit(submitNodeCollector, levelRenderState.cameraRenderState, false);
/* 310 */     this.finalizedGizmos.alwaysOnTopPrimitives().submit(submitNodeCollector, levelRenderState.cameraRenderState, true);
/* 312 */     if (!levelRenderState.shouldShowEntityOutlines)
/* 313 */       for (ObjectIterator<SubmitNodeCollection> objectIterator = this.submitNodeStorage.getSubmitsPerOrder().values().iterator(); objectIterator.hasNext(); ) {
/* 313 */         SubmitNodeCollection collection = objectIterator.next();
/* 314 */         collection.outline.clear();
/*     */       }  
/* 318 */     checkPoseStack(poseStack);
/*     */   }
/*     */   
/*     */   private void repositionCamera(CameraRenderState camera) {
/* 322 */     Vec3 cameraPos = camera.pos;
/* 324 */     SectionPos cameraSectionPos = SectionPos.of((Position)cameraPos);
/* 325 */     if (this.viewArea.repositionCamera(cameraSectionPos))
/* 326 */       this.worldBorderRenderer.invalidate(); 
/* 329 */     this.sectionRenderDispatcher.setCameraPosition(cameraPos);
/*     */   }
/*     */   
/*     */   private void addSkyPass(FrameGraphBuilder frame, CameraRenderState cameraState, GpuBufferSlice skyFog) {
/* 333 */     FogType fogType = cameraState.fogType;
/* 334 */     if (fogType == FogType.POWDER_SNOW || fogType == FogType.LAVA || cameraState.entityRenderState.doesMobEffectBlockSky)
/*     */       return; 
/* 338 */     if (this.levelRenderState.shouldResetSkyRenderer || this.skyRenderer == null) {
/* 339 */       if (this.skyRenderer != null)
/* 340 */         this.skyRenderer.close(); 
/* 342 */       this.skyRenderer = new SkyRenderer(this.textureManager, this.atlasManager, this.gameRenderer.mainRenderTarget());
/*     */     } 
/* 345 */     SkyRenderState state = this.levelRenderState.skyRenderState;
/* 346 */     if (state.skybox == DimensionType.Skybox.NONE)
/*     */       return; 
/* 350 */     FramePass pass = frame.addPass("sky");
/* 351 */     this.targets.main = pass.readsAndWrites(this.targets.main);
/* 353 */     pass.executes(() -> {
/*     */           RenderSystem.setShaderFog(skyFog);
/*     */           if (state.skybox == DimensionType.Skybox.END) {
/*     */             this.skyRenderer.renderEndSky();
/*     */             if (state.endFlashIntensity > 1.0E-5F) {
/*     */               PoseStack poseStack1 = new PoseStack();
/*     */               this.skyRenderer.renderEndFlash(poseStack1, state.endFlashIntensity, state.endFlashXAngle, state.endFlashYAngle);
/*     */             } 
/*     */             return;
/*     */           } 
/*     */           PoseStack poseStack = new PoseStack();
/*     */           this.skyRenderer.renderSkyDisc(state.skyColor);
/*     */           this.skyRenderer.renderSunriseAndSunset(poseStack, state.sunAngle, state.sunriseAndSunsetColor);
/*     */           this.skyRenderer.renderSunMoonAndStars(poseStack, state.sunAngle, state.moonAngle, state.starAngle, state.moonPhase, state.rainBrightness, state.starBrightness);
/*     */           if (state.shouldRenderDarkDisc)
/*     */             this.skyRenderer.renderDarkDisc(); 
/*     */         });
/*     */   }
/*     */   
/*     */   private void addMainPass(FrameGraphBuilder frame, FeatureRenderDispatcher.PreparedFrame featureFrame, GpuBufferSlice terrainFog, LevelRenderState levelRenderState, ProfilerFiller profiler, ChunkSectionsToRender chunkSectionsToRender) {
/* 380 */     FramePass pass = frame.addPass("main");
/* 381 */     this.targets.main = pass.readsAndWrites(this.targets.main);
/* 382 */     if (this.targets.translucent != null)
/* 383 */       this.targets.translucent = pass.readsAndWrites(this.targets.translucent); 
/* 385 */     if (this.targets.itemEntity != null)
/* 386 */       this.targets.itemEntity = pass.readsAndWrites(this.targets.itemEntity); 
/* 388 */     if (this.targets.weather != null)
/* 389 */       this.targets.weather = pass.readsAndWrites(this.targets.weather); 
/* 391 */     if (this.targets.particles != null)
/* 392 */       this.targets.particles = pass.readsAndWrites(this.targets.particles); 
/* 394 */     if (featureFrame.hasAnyOutline() && this.targets.entityOutline != null)
/* 395 */       this.targets.entityOutline = pass.readsAndWrites(this.targets.entityOutline); 
/* 398 */     ResourceHandle<RenderTarget> mainTarget = this.targets.main;
/* 399 */     ResourceHandle<RenderTarget> translucentTarget = this.targets.translucent;
/* 400 */     ResourceHandle<RenderTarget> itemEntityTarget = this.targets.itemEntity;
/* 401 */     ResourceHandle<RenderTarget> entityOutlineTarget = this.targets.entityOutline;
/* 402 */     ResourceHandle<RenderTarget> particleTarget = this.targets.particles;
/* 404 */     pass.executes(() -> {
/*     */           RenderSystem.setShaderFog(terrainFog);
/*     */           if (levelRenderState.shouldResetChunkLayerSampler || this.chunkLayerSampler == null) {
/*     */             if (this.chunkLayerSampler != null)
/*     */               this.chunkLayerSampler.close(); 
/*     */             int maxAnisotropy = (this.optionsRenderState.textureFiltering == TextureFilteringMethod.ANISOTROPIC) ? this.optionsRenderState.maxAnisotropyValue : 1;
/*     */             this.chunkLayerSampler = RenderSystem.getDevice().createSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, FilterMode.LINEAR, FilterMode.LINEAR, maxAnisotropy, OptionalDouble.empty());
/*     */           } 
/*     */           profiler.push("solidTerrain");
/*     */           chunkSectionsToRender.renderGroup(ChunkSectionLayerGroup.OPAQUE, this.chunkLayerSampler);
/*     */           this.gameRenderer.lighting().setupFor(Lighting.Entry.LEVEL);
/*     */           if (levelRenderState.shouldShowEntityOutlines && entityOutlineTarget != null) {
/*     */             RenderTarget outlineTarget = (RenderTarget)entityOutlineTarget.get();
/*     */             RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(outlineTarget.getColorTexture(), ENTITY_OUTLINE_CLEAR_COLOR, outlineTarget.getDepthTexture(), 0.0D);
/*     */           } 
/*     */           profiler.popPush("renderSolidFeatures");
/*     */           featureFrame.executeSolid();
/*     */           profiler.pop();
/*     */           if (translucentTarget != null)
/*     */             ((RenderTarget)translucentTarget.get()).copyDepthFrom((RenderTarget)mainTarget.get()); 
/*     */           if (itemEntityTarget != null)
/*     */             ((RenderTarget)itemEntityTarget.get()).copyDepthFrom((RenderTarget)mainTarget.get()); 
/*     */           if (particleTarget != null)
/*     */             ((RenderTarget)particleTarget.get()).copyDepthFrom((RenderTarget)mainTarget.get()); 
/*     */           profiler.push("renderTranslucentFeatures");
/*     */           featureFrame.executeTranslucent();
/*     */           profiler.pop();
/*     */           featureFrame.executeOutline();
/*     */           profiler.push("translucentTerrain");
/*     */           chunkSectionsToRender.renderGroup(ChunkSectionLayerGroup.TRANSLUCENT, this.chunkLayerSampler);
/*     */           profiler.pop();
/*     */           featureFrame.executeTranslucentAfterTerrain();
/*     */         });
/*     */   }
/*     */   
/*     */   private void addCloudsPass(FrameGraphBuilder frame, CloudStatus cloudStatus, Vec3 cameraPosition, long gameTime, float partialTicks, int cloudColor, float cloudHeight, int cloudRange) {
/* 454 */     FramePass pass = frame.addPass("clouds");
/* 455 */     if (this.targets.clouds != null) {
/* 456 */       this.targets.clouds = pass.readsAndWrites(this.targets.clouds);
/*     */     } else {
/* 458 */       this.targets.main = pass.readsAndWrites(this.targets.main);
/*     */     } 
/* 460 */     pass.executes(() -> this.cloudRenderer.render(cloudColor, cloudStatus, cloudHeight, cloudRange, cameraPosition, gameTime, partialTicks));
/*     */   }
/*     */   
/*     */   private void addWeatherPass(FrameGraphBuilder frame, GpuBufferSlice fog) {
/* 464 */     int renderDistance = this.optionsRenderState.renderDistance * 16;
/* 466 */     FramePass pass = frame.addPass("weather");
/* 467 */     if (this.targets.weather != null) {
/* 468 */       this.targets.weather = pass.readsAndWrites(this.targets.weather);
/*     */     } else {
/* 470 */       this.targets.main = pass.readsAndWrites(this.targets.main);
/*     */     } 
/* 472 */     pass.executes(() -> {
/*     */           RenderSystem.setShaderFog(fog);
/*     */           CameraRenderState cameraState = this.levelRenderState.cameraRenderState;
/*     */           this.weatherEffectRenderer.render(cameraState.pos, this.levelRenderState.weatherRenderState);
/*     */           this.worldBorderRenderer.render(this.levelRenderState.worldBorderRenderState, cameraState.pos, renderDistance, this.levelRenderState.cameraRenderState.depthFar);
/*     */         });
/*     */   }
/*     */   
/*     */   private void addAlwaysOnTopPass(FrameGraphBuilder frame, FeatureRenderDispatcher.PreparedFrame featureFrame, GpuBufferSlice fog) {
/* 481 */     if (!featureFrame.hasAnyAlwaysOnTop())
/*     */       return; 
/* 485 */     FramePass pass = frame.addPass("always_on_top");
/* 486 */     this.targets.main = pass.readsAndWrites(this.targets.main);
/* 487 */     if (this.targets.itemEntity != null)
/* 489 */       this.targets.itemEntity = pass.readsAndWrites(this.targets.itemEntity); 
/* 491 */     ResourceHandle<RenderTarget> mainTarget = this.targets.main;
/* 492 */     pass.executes(() -> {
/*     */           RenderSystem.setShaderFog(fog);
/*     */           PoseStack poseStack = new PoseStack();
/*     */           RenderTarget mainRenderTarget = (RenderTarget)mainTarget.get();
/*     */           RenderSystem.outputColorTextureOverride = mainRenderTarget.getColorTextureView();
/*     */           RenderSystem.outputDepthTextureOverride = mainRenderTarget.getDepthTextureView();
/*     */           RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(mainRenderTarget.getDepthTexture(), 0.0D);
/*     */           featureFrame.executeAlwaysOnTop();
/*     */           RenderSystem.outputColorTextureOverride = null;
/*     */           RenderSystem.outputDepthTextureOverride = null;
/*     */           checkPoseStack(poseStack);
/*     */         });
/*     */   }
/*     */   
/*     */   public ChunkSectionsToRender prepareChunkRenders(Matrix4fc modelViewMatrix) {
/* 512 */     ObjectListIterator<SectionRenderDispatcher.RenderSection> iterator = this.visibleSections.listIterator(0);
/* 514 */     EnumMap<ChunkSectionLayer, Int2ObjectOpenHashMap<List<RenderPass.Draw<GpuBufferSlice[]>>>> drawGroups = new EnumMap<>(ChunkSectionLayer.class);
/* 515 */     int largestIndexCount = 0;
/* 517 */     for (ChunkSectionLayer layer : ChunkSectionLayer.values())
/* 518 */       drawGroups.put(layer, new Int2ObjectOpenHashMap()); 
/* 521 */     List<DynamicUniforms.ChunkSectionInfo> sectionInfos = new ArrayList<>();
/* 522 */     GpuTextureView blockAtlas = this.textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).getTextureView();
/* 523 */     int textureAtlasWidth = blockAtlas.getWidth(0);
/* 524 */     int textureAtlasHeight = blockAtlas.getHeight(0);
/* 526 */     if (this.sectionRenderDispatcher != null) {
/* 527 */       this.sectionRenderDispatcher.lock();
/*     */       try {
/* 529 */         while (iterator.hasNext()) {
/* 530 */           SectionRenderDispatcher.RenderSection section = (SectionRenderDispatcher.RenderSection)iterator.next();
/* 532 */           SectionMesh sectionMesh = section.getSectionMesh();
/* 533 */           BlockPos renderOffset = section.getRenderOrigin();
/* 534 */           long now = Util.getMillis();
/* 535 */           int uboIndex = -1;
/* 537 */           for (ChunkSectionLayer layer : ChunkSectionLayer.values()) {
/* 538 */             SectionMesh.SectionDraw draw = sectionMesh.getSectionDraw(layer);
/* 539 */             SectionRenderDispatcher.RenderSectionBufferSlice slice = this.sectionRenderDispatcher.getRenderSectionSlice(sectionMesh, layer);
/* 540 */             if (slice != null && draw != null && (!draw.hasCustomIndexBuffer() || slice.indexBuffer() != null)) {
/*     */               GpuBuffer indexBuffer;
/*     */               IndexType indexType;
/* 543 */               if (uboIndex == -1) {
/* 546 */                 uboIndex = sectionInfos.size();
/* 547 */                 sectionInfos.add(new DynamicUniforms.ChunkSectionInfo((Matrix4fc)new Matrix4f(modelViewMatrix), renderOffset.getX(), renderOffset.getY(), renderOffset.getZ(), section.getVisibility(now), textureAtlasWidth, textureAtlasHeight));
/*     */               } 
/* 549 */               int combinedHash = 173;
/* 550 */               VertexFormat vertexFormat = layer.pipeline().getVertexFormatBinding(0);
/* 551 */               GpuBuffer vertexBuffer = slice.vertexBuffer();
/* 553 */               if (layer != ChunkSectionLayer.TRANSLUCENT)
/* 554 */                 combinedHash = 31 * combinedHash + vertexBuffer.hashCode(); 
/* 559 */               int firstIndex = 0;
/* 560 */               if (!draw.hasCustomIndexBuffer()) {
/* 561 */                 if (draw.indexCount() > largestIndexCount)
/* 562 */                   largestIndexCount = draw.indexCount(); 
/* 564 */                 indexBuffer = null;
/* 565 */                 indexType = null;
/*     */               } else {
/* 567 */                 indexBuffer = slice.indexBuffer();
/* 568 */                 indexType = draw.indexType();
/* 570 */                 if (layer != ChunkSectionLayer.TRANSLUCENT) {
/* 571 */                   combinedHash = 31 * combinedHash + indexBuffer.hashCode();
/* 572 */                   combinedHash = 31 * combinedHash + indexType.hashCode();
/*     */                 } 
/* 574 */                 firstIndex = (int)(slice.indexBufferOffset() / indexType.bytes);
/*     */               } 
/* 577 */               int finalUboIndex = uboIndex;
/* 578 */               int baseVertex = (int)(slice.vertexBufferOffset() / vertexFormat.getVertexSize());
/* 579 */               List<RenderPass.Draw<GpuBufferSlice[]>> draws = (List<RenderPass.Draw<GpuBufferSlice[]>>)((Int2ObjectOpenHashMap)drawGroups.get(layer)).computeIfAbsent(combinedHash, paramInt -> new ArrayList());
/* 580 */               draws.add(new RenderPass.Draw(0, vertexBuffer, indexBuffer, indexType, firstIndex, draw.indexCount(), baseVertex, (sectionUbos, uploader) -> uploader.upload("ChunkSection", sectionUbos[finalUboIndex])));
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } finally {
/* 584 */         this.sectionRenderDispatcher.unlock();
/*     */       } 
/*     */     } 
/* 588 */     GpuBufferSlice[] chunkSectionInfos = RenderSystem.getDynamicUniforms().writeChunkSections(sectionInfos.<DynamicUniforms.ChunkSectionInfo>toArray(new DynamicUniforms.ChunkSectionInfo[0]));
/* 589 */     return new ChunkSectionsToRender(blockAtlas, drawGroups, largestIndexCount, chunkSectionInfos);
/*     */   }
/*     */   
/*     */   private void compileSections(CameraRenderState camera) {
/* 593 */     ProfilerFiller profiler = Profiler.get();
/* 594 */     profiler.push("populateSectionsToCompile");
/* 596 */     BlockPos cameraPosition = camera.blockPos;
/* 597 */     long fadeDuration = Mth.floor(this.optionsRenderState.chunkSectionFadeInTime * 1000.0D);
/* 598 */     for (SectionUpdateRenderState state : this.levelRenderState.sectionUpdateRenderStates) {
/* 599 */       BlockPos center = SectionPos.of(state.sectionNode()).center();
/* 600 */       double distSqr = center.distSqr((Vec3i)cameraPosition);
/* 601 */       boolean isNearby = (distSqr < 768.0D);
/* 602 */       boolean rebuildSync = false;
/* 603 */       if (this.optionsRenderState.prioritizeChunkUpdates == PrioritizeChunkUpdates.NEARBY) {
/* 604 */         rebuildSync = (isNearby || state.playerChanged());
/* 605 */       } else if (this.optionsRenderState.prioritizeChunkUpdates == PrioritizeChunkUpdates.PLAYER_AFFECTED) {
/* 606 */         rebuildSync = state.playerChanged();
/*     */       } 
/* 608 */       SectionRenderDispatcher.RenderSection section = this.viewArea.getRenderSection(state.sectionNode());
/* 609 */       if (isNearby || section.wasPreviouslyEmpty()) {
/* 610 */         section.setFadeDuration(0L);
/*     */       } else {
/* 612 */         section.setFadeDuration(fadeDuration);
/*     */       } 
/* 614 */       section.setWasPreviouslyEmpty(false);
/* 615 */       if (rebuildSync) {
/* 616 */         profiler.push("compileSectionSynchronously");
/* 617 */         section.compileSync(state.region());
/* 618 */         profiler.pop();
/*     */         continue;
/*     */       } 
/* 620 */       section.compileAsync(state.region());
/*     */     } 
/* 624 */     profiler.popPush("scheduleTranslucentResort");
/* 625 */     scheduleTranslucentSectionResort(camera.pos);
/* 626 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void checkPoseStack(PoseStack poseStack) {
/* 630 */     if (!poseStack.isEmpty())
/* 631 */       throw new IllegalStateException("Pose stack not empty"); 
/*     */   }
/*     */   
/*     */   private void submitEntities(PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeCollector output) {
/* 636 */     Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
/* 637 */     double camX = cameraPos.x();
/* 638 */     double camY = cameraPos.y();
/* 639 */     double camZ = cameraPos.z();
/* 640 */     for (EntityRenderState state : levelRenderState.entityRenderStates)
/* 641 */       this.entityRenderDispatcher.submit(state, levelRenderState.cameraRenderState, state.x - camX, state.y - camY, state.z - camZ, poseStack, output); 
/*     */   }
/*     */   
/*     */   private void submitBlockEntities(PoseStack poseStack, LevelRenderState levelRenderState, SubmitNodeCollector submitNodeCollector) {
/* 646 */     Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
/* 647 */     double camX = cameraPos.x();
/* 648 */     double camY = cameraPos.y();
/* 649 */     double camZ = cameraPos.z();
/* 650 */     for (BlockEntityRenderState renderState : levelRenderState.blockEntityRenderStates) {
/* 651 */       BlockPos blockPos = renderState.blockPos;
/* 652 */       poseStack.pushPose();
/* 653 */       poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
/* 654 */       this.blockEntityRenderDispatcher.submit(renderState, poseStack, submitNodeCollector, levelRenderState.cameraRenderState);
/* 655 */       poseStack.popPose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void submitBlockDestroyAnimation(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LevelRenderState levelRenderState) {
/* 660 */     if (levelRenderState.blockBreakingRenderStates.isEmpty())
/*     */       return; 
/* 664 */     Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
/* 665 */     double camX = cameraPos.x();
/* 666 */     double camY = cameraPos.y();
/* 667 */     double camZ = cameraPos.z();
/* 669 */     List<BlockStateModelPart> parts = new ArrayList<>();
/* 670 */     RandomSource random = RandomSource.createThreadLocalInstance();
/* 672 */     for (BlockBreakingRenderState state : levelRenderState.blockBreakingRenderStates) {
/* 673 */       if (state.blockState().getRenderShape() != RenderShape.MODEL)
/*     */         continue; 
/* 676 */       BlockPos pos = state.blockPos();
/* 677 */       poseStack.pushPose();
/* 678 */       poseStack.translate(pos.getX() - camX, pos.getY() - camY, pos.getZ() - camZ);
/* 679 */       poseStack.translate(state.blockState().getOffset(pos));
/* 680 */       BlockStateModel model = this.modelManager.getBlockStateModelSet().get(state.blockState());
/* 681 */       random.setSeed(state.blockState().getSeed(pos));
/* 682 */       model.collectParts(random, parts);
/* 683 */       submitNodeCollector.submitBreakingBlockModel(poseStack, List.copyOf(parts), state.progress());
/* 684 */       parts.clear();
/* 685 */       poseStack.popPose();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void submitBlockOutline(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, LevelRenderState levelRenderState) {
/* 690 */     BlockOutlineRenderState state = levelRenderState.blockOutlineRenderState;
/* 691 */     if (state == null)
/*     */       return; 
/* 694 */     Vec3 cameraPos = levelRenderState.cameraRenderState.pos;
/* 695 */     BlockPos pos = state.pos();
/* 696 */     poseStack.pushPose();
/* 697 */     poseStack.translate(pos.getX() - cameraPos.x, pos.getY() - cameraPos.y, pos.getZ() - cameraPos.z);
/* 698 */     if (state.highContrast())
/* 699 */       submitHitOutline(poseStack, submitNodeCollector, RenderTypes.secondaryBlockOutline(), state, -16777216, 7.0F, state.isTranslucent()); 
/* 701 */     int outlineColor = state.highContrast() ? -11010079 : ARGB.black(102);
/* 702 */     submitHitOutline(poseStack, submitNodeCollector, RenderTypes.lines(), state, outlineColor, (this.gameRenderer.gameRenderState()).windowRenderState.appropriateLineWidth, state.isTranslucent());
/* 703 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   private void submitHitOutline(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, RenderType renderType, BlockOutlineRenderState state, int color, float width, boolean afterTerrain) {
/* 707 */     if (SharedConstants.DEBUG_SHAPES) {
/* 708 */       submitNodeCollector.submitShapeOutline(poseStack, state.shape(), renderType, -1, width, afterTerrain);
/* 709 */       if (state.collisionShape() != null)
/* 710 */         submitNodeCollector.submitShapeOutline(poseStack, state.collisionShape(), renderType, ARGB.colorFromFloat(0.4F, 0.0F, 0.0F, 0.0F), width, afterTerrain); 
/* 712 */       if (state.occlusionShape() != null)
/* 713 */         submitNodeCollector.submitShapeOutline(poseStack, state.occlusionShape(), renderType, ARGB.colorFromFloat(0.4F, 0.0F, 1.0F, 0.0F), width, afterTerrain); 
/* 715 */       if (state.interactionShape() != null)
/* 716 */         submitNodeCollector.submitShapeOutline(poseStack, state.interactionShape(), renderType, ARGB.colorFromFloat(0.4F, 0.0F, 0.0F, 1.0F), width, afterTerrain); 
/*     */     } else {
/* 719 */       submitNodeCollector.submitShapeOutline(poseStack, state.shape(), renderType, color, width, afterTerrain);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resize(int width, int height) {
/* 724 */     this.sectionOcclusionGraph.invalidate();
/* 725 */     this.entityOutlineTarget.resize(width, height);
/*     */   }
/*     */   
/*     */   public void endFrame() {
/* 729 */     this.cloudRenderer.endFrame();
/*     */   }
/*     */   
/*     */   public void close() {
/* 735 */     resetLevelRenderData();
/* 736 */     this.entityOutlineTarget.destroyBuffers();
/* 738 */     if (this.skyRenderer != null)
/* 739 */       this.skyRenderer.close(); 
/* 741 */     if (this.chunkLayerSampler != null)
/* 742 */       this.chunkLayerSampler.close(); 
/* 744 */     this.worldBorderRenderer.close();
/* 745 */     this.cloudRenderer.close();
/* 746 */     this.weatherEffectRenderer.close();
/*     */   }
/*     */   
/*     */   public void doEntityOutline() {
/* 750 */     if (this.levelRenderState.shouldShowEntityOutlines)
/* 751 */       this.entityOutlineTarget.blitAndBlendToTexture(this.gameRenderer.mainRenderTarget().getColorTextureView(), this.gameRenderer.mainRenderTarget().getDepthTextureView()); 
/*     */   }
/*     */   
/*     */   public void invalidateCompiledGeometry(ClientLevel level, Options options, Camera camera, BlockColors blockColors) {
/* 756 */     SectionCompiler sectionCompiler = new SectionCompiler(((Boolean)options.ambientOcclusion().get()).booleanValue(), ((Boolean)options.cutoutLeaves().get()).booleanValue(), this.modelManager.getBlockStateModelSet(), this.modelManager.getFluidStateModelSet(), blockColors);
/* 757 */     if (this.sectionRenderDispatcher == null) {
/* 758 */       Objects.requireNonNull(this.sectionOcclusionGraph);
/* 758 */       this.sectionRenderDispatcher = new SectionRenderDispatcher(Util.backgroundExecutor(), this.renderBuffers, sectionCompiler, this.sectionOcclusionGraph::schedulePropagationFrom);
/*     */     } else {
/* 760 */       this.sectionRenderDispatcher.setCompiler(sectionCompiler);
/*     */     } 
/* 763 */     cloudRenderer().markForRebuild();
/* 765 */     LeavesBlock.setCutoutLeaves(((Boolean)options.cutoutLeaves().get()).booleanValue());
/* 767 */     if (this.viewArea != null)
/* 768 */       this.viewArea.releaseAllBuffers(); 
/* 771 */     this.sectionRenderDispatcher.clearCompileQueue();
/* 773 */     this.viewArea = new ViewArea(this.sectionRenderDispatcher, level.getMinY(), level.getMaxY(), level.getMinSectionY(), level.getMaxSectionY(), options.getEffectiveRenderDistance(), this.sectionOcclusionGraph);
/* 774 */     sectionOcclusionGraph().waitAndReset(this.viewArea);
/* 775 */     clearVisibleSections();
/* 777 */     SectionPos cameraSectionPos = SectionPos.of((Position)camera.position());
/* 778 */     this.viewArea.repositionCamera(cameraSectionPos);
/*     */   }
/*     */   
/*     */   private PostChain getTransparencyChain() {
/* 782 */     if (!this.gameRenderer.gameRenderState().useShaderTransparency())
/* 783 */       return null; 
/* 786 */     return this.shaderManager.getPostChain(TRANSPARENCY_POST_CHAIN_ID, LevelTargetBundle.SORTING_TARGETS);
/*     */   }
/*     */   
/*     */   private void scheduleTranslucentSectionResort(Vec3 cameraPos) {
/* 790 */     if (this.visibleSections.isEmpty())
/*     */       return; 
/* 793 */     BlockPos cameraBlockPos = BlockPos.containing((Position)cameraPos);
/* 794 */     boolean blockPosChanged = !cameraBlockPos.equals(this.lastTranslucentSortBlockPos);
/* 795 */     TranslucencyPointOfView pointOfView = new TranslucencyPointOfView();
/* 796 */     for (ObjectListIterator<SectionRenderDispatcher.RenderSection> objectListIterator = this.nearbyVisibleSections.iterator(); objectListIterator.hasNext(); ) {
/* 796 */       SectionRenderDispatcher.RenderSection section = objectListIterator.next();
/* 797 */       scheduleResort(section, pointOfView, cameraPos, blockPosChanged, true);
/*     */     } 
/* 799 */     this.translucencyResortIterationIndex %= this.visibleSections.size();
/* 800 */     int resortsLeft = Math.max(this.visibleSections.size() / 8, 15);
/* 801 */     while (resortsLeft-- > 0) {
/* 802 */       int index = this.translucencyResortIterationIndex++ % this.visibleSections.size();
/* 803 */       scheduleResort((SectionRenderDispatcher.RenderSection)this.visibleSections.get(index), pointOfView, cameraPos, blockPosChanged, false);
/*     */     } 
/* 805 */     this.lastTranslucentSortBlockPos = cameraBlockPos;
/*     */   }
/*     */   
/*     */   private void scheduleResort(SectionRenderDispatcher.RenderSection section, TranslucencyPointOfView pointOfView, Vec3 cameraPos, boolean blockPosChanged, boolean isNearby) {
/* 809 */     pointOfView.set(cameraPos, section.getSectionNode());
/* 810 */     boolean pointOfViewChanged = section.getSectionMesh().isDifferentPointOfView(pointOfView);
/* 811 */     boolean resortBecauseBlockPosChanged = (blockPosChanged && (pointOfView.isAxisAligned() || isNearby));
/* 815 */     if ((resortBecauseBlockPosChanged || pointOfViewChanged) && !section.transparencyResortingScheduled() && section.hasTranslucentGeometry())
/* 816 */       section.resortTransparency(); 
/*     */   }
/*     */   
/*     */   public void clearVisibleSections() {
/* 821 */     this.visibleSections.clear();
/* 822 */     this.nearbyVisibleSections.clear();
/*     */   }
/*     */   
/*     */   public void resetLevelRenderData() {
/* 826 */     if (this.viewArea != null) {
/* 827 */       this.viewArea.releaseAllBuffers();
/* 828 */       this.viewArea = null;
/*     */     } 
/* 830 */     if (this.sectionRenderDispatcher != null)
/* 831 */       this.sectionRenderDispatcher.dispose(); 
/* 833 */     this.sectionRenderDispatcher = null;
/* 834 */     this.sectionOcclusionGraph.waitAndReset(null);
/* 835 */     clearVisibleSections();
/*     */   }
/*     */   
/*     */   public boolean hasRenderedAllSections() {
/* 839 */     return (this.sectionRenderDispatcher == null || this.sectionRenderDispatcher.isQueueEmpty());
/*     */   }
/*     */   
/*     */   public boolean isSectionCompiledAndVisible(BlockPos blockPos) {
/* 843 */     if (this.viewArea == null)
/* 844 */       return false; 
/* 846 */     SectionRenderDispatcher.RenderSection renderSection = this.viewArea.getRenderSectionAt(blockPos);
/* 847 */     if (renderSection == null || renderSection.sectionMesh.get() == CompiledSectionMesh.UNCOMPILED)
/* 848 */       return false; 
/* 850 */     return (renderSection.getVisibility(Util.getMillis()) >= 0.3F);
/*     */   }
/*     */   
/*     */   public SectionRenderDispatcher sectionRenderDispatcher() {
/* 854 */     return this.sectionRenderDispatcher;
/*     */   }
/*     */   
/*     */   public EntityRenderDispatcher entityRenderDispatcher() {
/* 858 */     return this.entityRenderDispatcher;
/*     */   }
/*     */   
/*     */   public BlockEntityRenderDispatcher blockEntityRenderDispatcher() {
/* 862 */     return this.blockEntityRenderDispatcher;
/*     */   }
/*     */   
/*     */   public RenderTarget entityOutlineTarget() {
/* 866 */     return (this.targets.entityOutline != null) ? (RenderTarget)this.targets.entityOutline.get() : null;
/*     */   }
/*     */   
/*     */   public RenderTarget translucentTarget() {
/* 870 */     return (this.targets.translucent != null) ? (RenderTarget)this.targets.translucent.get() : null;
/*     */   }
/*     */   
/*     */   public RenderTarget itemEntityTarget() {
/* 874 */     return (this.targets.itemEntity != null) ? (RenderTarget)this.targets.itemEntity.get() : null;
/*     */   }
/*     */   
/*     */   public RenderTarget particlesTarget() {
/* 878 */     return (this.targets.particles != null) ? (RenderTarget)this.targets.particles.get() : null;
/*     */   }
/*     */   
/*     */   public RenderTarget weatherTarget() {
/* 882 */     return (this.targets.weather != null) ? (RenderTarget)this.targets.weather.get() : null;
/*     */   }
/*     */   
/*     */   public RenderTarget cloudsTarget() {
/* 886 */     return (this.targets.clouds != null) ? (RenderTarget)this.targets.clouds.get() : null;
/*     */   }
/*     */   
/*     */   public CloudRenderer cloudRenderer() {
/* 890 */     return this.cloudRenderer;
/*     */   }
/*     */   
/*     */   public SkyRenderer skyRenderer() {
/* 894 */     return this.skyRenderer;
/*     */   }
/*     */   
/*     */   public WeatherEffectRenderer weatherEffectRenderer() {
/* 898 */     return this.weatherEffectRenderer;
/*     */   }
/*     */   
/*     */   public WorldBorderRenderer worldBorderRenderer() {
/* 902 */     return this.worldBorderRenderer;
/*     */   }
/*     */   
/*     */   public ViewArea viewArea() {
/* 906 */     return this.viewArea;
/*     */   }
/*     */   
/*     */   public ObjectArrayList<SectionRenderDispatcher.RenderSection> visibleSections() {
/* 910 */     return this.visibleSections;
/*     */   }
/*     */   
/*     */   public ObjectArrayList<SectionRenderDispatcher.RenderSection> nearbyVisibleSections() {
/* 914 */     return this.nearbyVisibleSections;
/*     */   }
/*     */   
/*     */   public LongCollection expectedChunks() {
/* 918 */     return this.sectionOcclusionGraph.expectedChunks();
/*     */   }
/*     */   
/*     */   public SectionOcclusionGraph sectionOcclusionGraph() {
/* 922 */     return this.sectionOcclusionGraph;
/*     */   }
/*     */   
/*     */   public Gizmos.TemporaryCollection collectPerFrameRenderThreadGizmos() {
/* 926 */     return Gizmos.withCollector((GizmoCollector)this.renderThreadGizmos);
/*     */   }
/*     */   
/*     */   private void finalizeGizmoCollection() {
/* 930 */     DrawableGizmoPrimitives standardPrimitives = new DrawableGizmoPrimitives();
/* 931 */     DrawableGizmoPrimitives alwaysOnTopPrimitives = new DrawableGizmoPrimitives();
/* 932 */     long currentMillis = Util.getMillis();
/* 933 */     for (SimpleGizmoCollector.GizmoInstance instance : this.renderThreadGizmos.drainGizmos())
/* 934 */       instance.gizmo().emit(instance.isAlwaysOnTop() ? (GizmoPrimitives)alwaysOnTopPrimitives : (GizmoPrimitives)standardPrimitives, instance.getAlphaMultiplier(currentMillis)); 
/* 936 */     this.finalizedGizmos = new FinalizedGizmos(standardPrimitives, alwaysOnTopPrimitives);
/*     */   }
/*     */   
/*     */   public void addMainThreadGizmos(List<SimpleGizmoCollector.GizmoInstance> mainThreadGizmos) {
/* 940 */     this.renderThreadGizmos.addTemporaryGizmos(mainThreadGizmos);
/*     */   }
/*     */   
/*     */   private static final class FinalizedGizmos extends Record {
/*     */     private final DrawableGizmoPrimitives standardPrimitives;
/*     */     
/*     */     private final DrawableGizmoPrimitives alwaysOnTopPrimitives;
/*     */     
/*     */     private FinalizedGizmos(DrawableGizmoPrimitives standardPrimitives, DrawableGizmoPrimitives alwaysOnTopPrimitives) {
/* 943 */       this.standardPrimitives = standardPrimitives;
/* 943 */       this.alwaysOnTopPrimitives = alwaysOnTopPrimitives;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #943	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #943	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #943	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/LevelRenderer$FinalizedGizmos;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public DrawableGizmoPrimitives standardPrimitives() {
/* 943 */       return this.standardPrimitives;
/*     */     }
/*     */     
/*     */     public DrawableGizmoPrimitives alwaysOnTopPrimitives() {
/* 943 */       return this.alwaysOnTopPrimitives;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\LevelRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */