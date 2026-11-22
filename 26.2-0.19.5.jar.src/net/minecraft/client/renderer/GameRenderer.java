/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.pipeline.MainTarget;
/*     */ import com.mojang.blaze3d.pipeline.RenderTarget;
/*     */ import com.mojang.blaze3d.platform.Lighting;
/*     */ import com.mojang.blaze3d.platform.MessageBox;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import com.mojang.blaze3d.resource.CrossFrameResourcePool;
/*     */ import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
/*     */ import com.mojang.blaze3d.shaders.ShaderSource;
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.math.Axis;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.DeltaTracker;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.PrioritizeChunkUpdates;
/*     */ import net.minecraft.client.Screenshot;
/*     */ import net.minecraft.client.TextureFilteringMethod;
/*     */ import net.minecraft.client.gui.render.GuiRenderer;
/*     */ import net.minecraft.client.gui.render.pip.GuiBannerResultRenderer;
/*     */ import net.minecraft.client.gui.render.pip.GuiBookModelRenderer;
/*     */ import net.minecraft.client.gui.render.pip.GuiEntityRenderer;
/*     */ import net.minecraft.client.gui.render.pip.GuiProfilerChartRenderer;
/*     */ import net.minecraft.client.gui.render.pip.GuiSkinRenderer;
/*     */ import net.minecraft.client.main.SilentInitException;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.renderer.feature.FeatureRenderDispatcher;
/*     */ import net.minecraft.client.renderer.fog.FogRenderer;
/*     */ import net.minecraft.client.renderer.state.GameRenderState;
/*     */ import net.minecraft.client.renderer.state.OptionsRenderState;
/*     */ import net.minecraft.client.renderer.state.WindowRenderState;
/*     */ import net.minecraft.client.renderer.state.level.CameraRenderState;
/*     */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.model.ModelManager;
/*     */ import net.minecraft.client.resources.model.sprite.AtlasManager;
/*     */ import net.minecraft.client.resources.model.sprite.SpriteGetter;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.waypoints.TrackedWaypoint;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.joml.Matrix4fc;
/*     */ import org.joml.Quaternionfc;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ import org.joml.Vector4fc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GameRenderer implements AutoCloseable, TrackedWaypoint.Projector {
/*  90 */   private static final Identifier BLUR_POST_CHAIN_ID = Identifier.withDefaultNamespace("blur");
/*     */   
/*     */   public static final int MAX_BLUR_RADIUS = 10;
/*     */   
/*  94 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final float PROJECTION_3D_HUD_Z_FAR = 100.0F;
/*     */   
/*     */   private static final float PORTAL_SPINNING_SPEED = 20.0F;
/*     */   
/*     */   private static final float NAUSEA_SPINNING_SPEED = 7.0F;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/* 103 */   private final GameRenderState gameRenderState = new GameRenderState();
/*     */   
/* 105 */   private final RandomSource random = RandomSource.create();
/*     */   
/*     */   public final ItemInHandRenderer itemInHandRenderer;
/*     */   
/*     */   private final ScreenEffectRenderer screenEffectRenderer;
/*     */   
/*     */   private final DebugCrosshairRenderer debugCrosshairRenderer;
/*     */   
/*     */   private final RenderBuffers renderBuffers;
/*     */   
/*     */   private final RenderTarget mainRenderTarget;
/*     */   
/*     */   private float spinningEffectTime;
/*     */   
/*     */   private float spinningEffectSpeed;
/*     */   
/*     */   private float bossOverlayWorldDarkening;
/*     */   
/*     */   private float bossOverlayWorldDarkeningO;
/*     */   
/*     */   private boolean renderBlockOutline = true;
/*     */   
/*     */   private long lastScreenshotAttempt;
/*     */   
/*     */   private boolean hasWorldScreenshot;
/*     */   
/* 123 */   private final Lightmap lightmap = new Lightmap();
/*     */   
/*     */   private final LightmapRenderStateExtractor lightmapRenderStateExtractor;
/*     */   
/* 125 */   private final UiLightmap uiLightmap = new UiLightmap();
/*     */   
/*     */   private boolean useUiLightmap;
/*     */   
/* 128 */   private final OverlayTexture overlayTexture = new OverlayTexture();
/*     */   
/* 130 */   protected final Panorama panorama = new Panorama();
/*     */   
/* 132 */   private final CrossFrameResourcePool resourcePool = new CrossFrameResourcePool(3);
/*     */   
/* 133 */   private final FogRenderer fogRenderer = new FogRenderer();
/*     */   
/*     */   private final GuiRenderer guiRenderer;
/*     */   
/*     */   private final FeatureRenderDispatcher featureRenderDispatcher;
/*     */   
/* 137 */   private final SubmitNodeStorage handAndScreenSubmitNodeStorage = new SubmitNodeStorage();
/*     */   
/*     */   private Identifier postEffectId;
/*     */   
/*     */   private boolean effectActive;
/*     */   
/* 141 */   private final Camera mainCamera = new Camera();
/*     */   
/* 142 */   private final Projection hudProjection = new Projection();
/*     */   
/* 143 */   private final Lighting lighting = new Lighting();
/*     */   
/* 144 */   private final GlobalSettingsUniform globalSettingsUniform = new GlobalSettingsUniform();
/*     */   
/* 145 */   private final ProjectionMatrixBuffer levelProjectionMatrixBuffer = new ProjectionMatrixBuffer("level");
/*     */   
/* 146 */   private final ProjectionMatrixBuffer hud3dProjectionMatrixBuffer = new ProjectionMatrixBuffer("3d hud");
/*     */   
/*     */   public GameRenderer(Minecraft minecraft, ItemInHandRenderer itemInHandRenderer, ModelManager modelManager) {
/* 149 */     this.minecraft = minecraft;
/* 150 */     this.itemInHandRenderer = itemInHandRenderer;
/* 151 */     this.lightmapRenderStateExtractor = new LightmapRenderStateExtractor(this, minecraft);
/*     */     try {
/* 153 */       int maxSectionBuilders = Runtime.getRuntime().availableProcessors();
/* 154 */       this.renderBuffers = new RenderBuffers(maxSectionBuilders);
/* 155 */     } catch (OutOfMemoryError e) {
/* 156 */       MessageBox.error("Oh no! The game was unable to allocate memory off-heap while trying to start. You may try to free some memory by closing other applications on your computer, check that your system meets the minimum requirements, and try again. If the problem persists, please visit: " + String.valueOf(CommonLinks.GENERAL_HELP));
/* 157 */       throw new SilentInitException("Unable to allocate render buffers", e);
/*     */     } 
/* 159 */     AtlasManager atlasManager = minecraft.getAtlasManager();
/* 160 */     this.featureRenderDispatcher = new FeatureRenderDispatcher(this.renderBuffers, modelManager, atlasManager, minecraft.font, this.gameRenderState);
/* 161 */     this.guiRenderer = new GuiRenderer(this.gameRenderState.guiRenderState, this.featureRenderDispatcher, List.of(new GuiEntityRenderer(minecraft
/* 162 */             .getEntityRenderDispatcher()), new GuiSkinRenderer(), new GuiBookModelRenderer(), new GuiBannerResultRenderer((SpriteGetter)atlasManager), new GuiProfilerChartRenderer()));
/* 168 */     this.screenEffectRenderer = new ScreenEffectRenderer(minecraft, (SpriteGetter)atlasManager);
/* 169 */     this.debugCrosshairRenderer = new DebugCrosshairRenderer();
/* 170 */     this.mainRenderTarget = (RenderTarget)new MainTarget(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());
/*     */   }
/*     */   
/*     */   public void close() {
/* 175 */     this.debugCrosshairRenderer.close();
/* 176 */     this.globalSettingsUniform.close();
/* 177 */     this.lightmap.close();
/* 178 */     this.overlayTexture.close();
/* 179 */     this.uiLightmap.close();
/* 180 */     this.resourcePool.close();
/* 181 */     this.guiRenderer.close();
/* 182 */     this.levelProjectionMatrixBuffer.close();
/* 183 */     this.hud3dProjectionMatrixBuffer.close();
/* 184 */     this.lighting.close();
/* 185 */     this.fogRenderer.close();
/* 186 */     this.featureRenderDispatcher.close();
/* 187 */     this.mainRenderTarget.destroyBuffers();
/* 188 */     this.renderBuffers.close();
/*     */   }
/*     */   
/*     */   public RenderBuffers renderBuffers() {
/* 192 */     return this.renderBuffers;
/*     */   }
/*     */   
/*     */   public FeatureRenderDispatcher featureRenderDispatcher() {
/* 196 */     return this.featureRenderDispatcher;
/*     */   }
/*     */   
/*     */   public GameRenderState gameRenderState() {
/* 200 */     return this.gameRenderState;
/*     */   }
/*     */   
/*     */   public void setRenderBlockOutline(boolean renderBlockOutline) {
/* 204 */     this.renderBlockOutline = renderBlockOutline;
/*     */   }
/*     */   
/*     */   public void clearPostEffect() {
/* 208 */     this.postEffectId = null;
/* 209 */     this.effectActive = false;
/*     */   }
/*     */   
/*     */   public void togglePostEffect() {
/* 213 */     this.effectActive = !this.effectActive;
/*     */   }
/*     */   
/*     */   public void checkEntityPostEffect(Entity cameraEntity) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: astore_2
/*     */     //   2: iconst_0
/*     */     //   3: istore_3
/*     */     //   4: aload_2
/*     */     //   5: iload_3
/*     */     //   6: <illegal opcode> typeSwitch : (Lnet/minecraft/world/entity/Entity;I)I
/*     */     //   11: tableswitch default -> 97, -1 -> 97, 0 -> 40, 1 -> 59, 2 -> 78
/*     */     //   40: aload_2
/*     */     //   41: checkcast net/minecraft/world/entity/monster/Creeper
/*     */     //   44: astore #4
/*     */     //   46: aload_0
/*     */     //   47: ldc_w 'creeper'
/*     */     //   50: invokestatic withDefaultNamespace : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*     */     //   53: invokevirtual setPostEffect : (Lnet/minecraft/resources/Identifier;)V
/*     */     //   56: goto -> 101
/*     */     //   59: aload_2
/*     */     //   60: checkcast net/minecraft/world/entity/monster/spider/Spider
/*     */     //   63: astore #5
/*     */     //   65: aload_0
/*     */     //   66: ldc_w 'spider'
/*     */     //   69: invokestatic withDefaultNamespace : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*     */     //   72: invokevirtual setPostEffect : (Lnet/minecraft/resources/Identifier;)V
/*     */     //   75: goto -> 101
/*     */     //   78: aload_2
/*     */     //   79: checkcast net/minecraft/world/entity/monster/EnderMan
/*     */     //   82: astore #6
/*     */     //   84: aload_0
/*     */     //   85: ldc_w 'invert'
/*     */     //   88: invokestatic withDefaultNamespace : (Ljava/lang/String;)Lnet/minecraft/resources/Identifier;
/*     */     //   91: invokevirtual setPostEffect : (Lnet/minecraft/resources/Identifier;)V
/*     */     //   94: goto -> 101
/*     */     //   97: aload_0
/*     */     //   98: invokevirtual clearPostEffect : ()V
/*     */     //   101: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #217	-> 0
/*     */     //   #218	-> 40
/*     */     //   #219	-> 59
/*     */     //   #220	-> 78
/*     */     //   #221	-> 97
/*     */     //   #223	-> 101
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   46	13	4	ignored	Lnet/minecraft/world/entity/monster/Creeper;
/*     */     //   65	13	5	ignored	Lnet/minecraft/world/entity/monster/spider/Spider;
/*     */     //   84	13	6	ignored	Lnet/minecraft/world/entity/monster/EnderMan;
/*     */     //   0	102	0	this	Lnet/minecraft/client/renderer/GameRenderer;
/*     */     //   0	102	1	cameraEntity	Lnet/minecraft/world/entity/Entity;
/*     */   }
/*     */   
/*     */   private void setPostEffect(Identifier id) {
/* 226 */     this.postEffectId = id;
/* 227 */     this.effectActive = true;
/*     */   }
/*     */   
/*     */   public void processBlurEffect() {
/* 231 */     PostChain postChain = this.minecraft.getShaderManager().getPostChain(BLUR_POST_CHAIN_ID, LevelTargetBundle.MAIN_TARGETS);
/* 232 */     if (postChain != null)
/* 233 */       postChain.process(this.mainRenderTarget, (GraphicsResourceAllocator)this.resourcePool); 
/*     */   }
/*     */   
/*     */   public void preloadUiShader(ResourceProvider resourceProvider) {
/* 238 */     GpuDevice device = RenderSystem.getDevice();
/* 239 */     ShaderSource shaderSource = (id, type) -> {
/*     */         Identifier location = type.idConverter().idToFile(id);
/*     */         try {
/*     */           Reader reader = resourceProvider.getResourceOrThrow(location).openAsReader();
/*     */           try {
/*     */             String str = IOUtils.toString(reader);
/*     */             if (reader != null)
/*     */               reader.close(); 
/*     */             return str;
/* 241 */           } catch (Throwable t$) {
/*     */             if (reader != null)
/*     */               try {
/*     */                 reader.close();
/* 241 */               } catch (Throwable x2) {
/*     */                 t$.addSuppressed(x2);
/*     */               }  
/*     */             throw t$;
/*     */           } 
/* 243 */         } catch (IOException exception) {
/*     */           LOGGER.error("Coudln't preload {} shader {}: {}", new Object[] { type, id, exception });
/*     */           return null;
/*     */         } 
/*     */       };
/* 248 */     device.precompilePipeline(RenderPipelines.GUI, shaderSource);
/* 249 */     device.precompilePipeline(RenderPipelines.GUI_TEXTURED, shaderSource);
/* 250 */     if (TracyClient.isAvailable())
/* 251 */       device.precompilePipeline(RenderPipelines.TRACY_BLIT, shaderSource); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 256 */     this.lightmapRenderStateExtractor.tick();
/* 258 */     LocalPlayer player = this.minecraft.player;
/* 259 */     if (this.mainCamera.entity() == null)
/* 260 */       this.mainCamera.setEntity((Entity)player); 
/* 262 */     this.mainCamera.tick();
/* 263 */     this.itemInHandRenderer.tick();
/* 265 */     float portalIntensity = player.portalEffectIntensity;
/* 266 */     float nauseaIntensity = player.getEffectBlendFactor(MobEffects.NAUSEA, 1.0F);
/* 267 */     if (portalIntensity > 0.0F || nauseaIntensity > 0.0F) {
/* 268 */       this.spinningEffectSpeed = (portalIntensity * 20.0F + nauseaIntensity * 7.0F) / (portalIntensity + nauseaIntensity);
/* 269 */       this.spinningEffectTime += this.spinningEffectSpeed;
/*     */     } else {
/* 271 */       this.spinningEffectSpeed = 0.0F;
/*     */     } 
/* 274 */     if (!this.minecraft.level.tickRateManager().runsNormally())
/*     */       return; 
/* 278 */     this.bossOverlayWorldDarkeningO = this.bossOverlayWorldDarkening;
/* 279 */     if (this.minecraft.gui.hud.getBossOverlay().shouldDarkenScreen()) {
/* 280 */       this.bossOverlayWorldDarkening += 0.05F;
/* 281 */       if (this.bossOverlayWorldDarkening > 1.0F)
/* 282 */         this.bossOverlayWorldDarkening = 1.0F; 
/* 284 */     } else if (this.bossOverlayWorldDarkening > 0.0F) {
/* 285 */       this.bossOverlayWorldDarkening -= 0.0125F;
/*     */     } 
/* 288 */     this.screenEffectRenderer.tick();
/*     */   }
/*     */   
/*     */   public Identifier currentPostEffect() {
/* 292 */     return this.postEffectId;
/*     */   }
/*     */   
/*     */   public void resize(int width, int height) {
/* 296 */     this.resourcePool.clear();
/* 298 */     this.mainRenderTarget.resize(width, height);
/* 299 */     this.minecraft.levelRenderer.resize(width, height);
/*     */   }
/*     */   
/*     */   private void bobHurt(CameraRenderState cameraState, PoseStack poseStack) {
/* 303 */     if (cameraState.entityRenderState.isLiving) {
/* 305 */       float hurt = cameraState.entityRenderState.hurtTime;
/* 307 */       if (cameraState.entityRenderState.isDeadOrDying) {
/* 308 */         float duration = Math.min(cameraState.entityRenderState.deathTime, 20.0F);
/* 310 */         poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(40.0F - 8000.0F / (duration + 200.0F)));
/*     */       } 
/* 313 */       if (hurt < 0.0F)
/*     */         return; 
/* 316 */       hurt /= cameraState.entityRenderState.hurtDuration;
/* 317 */       hurt = Mth.sin((hurt * hurt * hurt * hurt * 3.1415927F));
/* 319 */       float rr = cameraState.entityRenderState.hurtDir;
/* 321 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-rr));
/* 322 */       float tiltAmount = (float)(-hurt * 14.0D * this.gameRenderState.optionsRenderState.damageTiltStrength);
/* 323 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(tiltAmount));
/* 324 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(rr));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void bobView(CameraRenderState cameraState, PoseStack poseStack) {
/* 329 */     if (!cameraState.entityRenderState.isPlayer)
/*     */       return; 
/* 333 */     float backwardsInterpolatedWalkDistance = cameraState.entityRenderState.backwardsInterpolatedWalkDistance;
/* 334 */     float bob = cameraState.entityRenderState.bob;
/* 335 */     poseStack.translate(Mth.sin((backwardsInterpolatedWalkDistance * 3.1415927F)) * bob * 0.5F, -Math.abs(Mth.cos((backwardsInterpolatedWalkDistance * 3.1415927F)) * bob), 0.0F);
/* 336 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(Mth.sin((backwardsInterpolatedWalkDistance * 3.1415927F)) * bob * 3.0F));
/* 337 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Math.abs(Mth.cos((backwardsInterpolatedWalkDistance * 3.1415927F - 0.2F)) * bob) * 5.0F));
/*     */   }
/*     */   
/*     */   private void renderItemInHand(CameraRenderState cameraState, float deltaPartialTick, Matrix4fc modelViewMatrix) {
/* 341 */     if (cameraState.isPanoramicMode)
/*     */       return; 
/* 344 */     if (!this.gameRenderState.optionsRenderState.cameraType.isFirstPerson() || cameraState.entityRenderState.isSleeping || this.gameRenderState.guiRenderState.isHudHidden || this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR)
/*     */       return; 
/* 348 */     PoseStack poseStack = new PoseStack();
/* 349 */     poseStack.pushPose();
/* 350 */     poseStack.mulPose((Matrix4fc)modelViewMatrix.invert(new Matrix4f()));
/* 351 */     Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
/* 352 */     modelViewStack.pushMatrix().mul(modelViewMatrix);
/* 354 */     bobHurt(cameraState, poseStack);
/* 355 */     if (this.gameRenderState.optionsRenderState.bobView)
/* 356 */       bobView(cameraState, poseStack); 
/* 359 */     this.itemInHandRenderer.submitHandsWithItems(deltaPartialTick, poseStack, this.handAndScreenSubmitNodeStorage, this.minecraft.player, this.minecraft.getEntityRenderDispatcher().getPackedLightCoords((Entity)this.minecraft.player, deltaPartialTick));
/* 360 */     this.featureRenderDispatcher.renderAllFeatures(this.handAndScreenSubmitNodeStorage);
/* 362 */     modelViewStack.popMatrix();
/* 363 */     poseStack.popPose();
/*     */   }
/*     */   
/*     */   public static float nightVisionScale(LivingEntity camera, float a) {
/* 367 */     MobEffectInstance nightVision = camera.getEffect(MobEffects.NIGHT_VISION);
/* 368 */     if (!nightVision.endsWithin(200))
/* 369 */       return 1.0F; 
/* 371 */     return 0.7F + Mth.sin(((nightVision.getDuration() - a) * 3.1415927F * 0.2F)) * 0.3F;
/*     */   }
/*     */   
/*     */   public void update(DeltaTracker deltaTracker) {
/* 376 */     ProfilerFiller profiler = Profiler.get();
/* 377 */     profiler.push("camera");
/* 378 */     this.mainCamera.update(deltaTracker);
/* 379 */     profiler.pop();
/*     */   }
/*     */   
/*     */   public void extract(DeltaTracker deltaTracker, boolean advanceGameTime) {
/* 383 */     boolean resourcesLoaded = this.minecraft.isGameLoadFinished();
/* 384 */     boolean readyForLevelRendering = (resourcesLoaded && advanceGameTime && this.minecraft.level != null);
/* 386 */     float worldPartialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
/* 388 */     extractWindow();
/* 390 */     extractOptions();
/* 392 */     if (readyForLevelRendering) {
/* 393 */       this.lightmapRenderStateExtractor.extract(this.gameRenderState.lightmapRenderState, 1.0F);
/* 395 */       float cameraEntityPartialTicks = this.mainCamera.getCameraEntityPartialTicks(deltaTracker);
/* 396 */       extractCamera(deltaTracker, worldPartialTicks, cameraEntityPartialTicks);
/* 398 */       this.minecraft.levelExtractor.extract(deltaTracker, this.mainCamera, worldPartialTicks);
/*     */     } 
/* 401 */     this.minecraft.gui.extractRenderState(deltaTracker, readyForLevelRendering, resourcesLoaded);
/* 403 */     this.minecraft.getMetricsRecorder().sampleDuringExtract();
/*     */   }
/*     */   
/*     */   public void render(DeltaTracker deltaTracker, boolean advanceGameTime) {
/* 407 */     ProfilerFiller profiler = Profiler.get();
/* 409 */     profiler.push("render");
/* 411 */     WindowRenderState windowRenderState = this.gameRenderState.windowRenderState;
/* 412 */     if (windowRenderState.width != this.mainRenderTarget.width || windowRenderState.height != this.mainRenderTarget.height)
/* 413 */       resize(windowRenderState.width, windowRenderState.height); 
/* 416 */     RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(this.mainRenderTarget.getColorTexture(), (Vector4fc)this.gameRenderState.guiRenderState.clearColorOverride, this.mainRenderTarget.getDepthTexture(), 0.0D);
/* 418 */     boolean resourcesLoaded = this.minecraft.isGameLoadFinished();
/* 419 */     boolean shouldRenderLevel = (resourcesLoaded && advanceGameTime && this.minecraft.level != null);
/* 421 */     this.globalSettingsUniform.update(windowRenderState.width, windowRenderState.height, this.gameRenderState.optionsRenderState.glintStrength, (this.minecraft.level == null) ? 0L : this.minecraft.level.getGameTime(), deltaTracker, this.gameRenderState.optionsRenderState.menuBackgroundBlurriness, this.gameRenderState.levelRenderState.cameraRenderState.pos, (this.gameRenderState.optionsRenderState.textureFiltering == TextureFilteringMethod.RGSS));
/* 423 */     if (shouldRenderLevel) {
/* 424 */       this.lightmap.render(this.gameRenderState.lightmapRenderState);
/* 426 */       profiler.push("world");
/* 428 */       renderLevel(deltaTracker);
/* 430 */       tryTakeScreenshotIfNeeded();
/* 432 */       this.minecraft.levelRenderer.doEntityOutline();
/* 434 */       if (this.postEffectId != null && this.effectActive) {
/* 435 */         PostChain postChain = this.minecraft.getShaderManager().getPostChain(this.postEffectId, LevelTargetBundle.MAIN_TARGETS);
/* 436 */         if (postChain != null)
/* 437 */           postChain.process(this.mainRenderTarget, (GraphicsResourceAllocator)this.resourcePool); 
/*     */       } 
/* 440 */       profiler.pop();
/*     */     } 
/* 442 */     this.fogRenderer.endFrame();
/* 444 */     RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(this.mainRenderTarget.getDepthTexture(), 0.0D);
/* 446 */     lighting().setupFor(Lighting.Entry.ITEMS_3D);
/* 447 */     this.useUiLightmap = true;
/* 449 */     profiler.push("gui");
/* 450 */     this.guiRenderer.render();
/* 451 */     this.guiRenderer.endFrame();
/* 452 */     profiler.pop();
/* 453 */     this.useUiLightmap = false;
/* 455 */     this.renderBuffers.endFrame();
/* 457 */     this.resourcePool.endFrame();
/* 459 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void tryTakeScreenshotIfNeeded() {
/* 463 */     if (this.hasWorldScreenshot || !this.minecraft.isLocalServer())
/*     */       return; 
/* 467 */     long time = Util.getMillis();
/* 468 */     if (time - this.lastScreenshotAttempt < 1000L)
/*     */       return; 
/* 471 */     this.lastScreenshotAttempt = time;
/* 473 */     IntegratedServer server = this.minecraft.getSingleplayerServer();
/* 474 */     if (server == null || server.isStopped())
/*     */       return; 
/* 478 */     server.getWorldScreenshotFile().ifPresent(path -> {
/*     */           if (Files.isRegularFile(path, new java.nio.file.LinkOption[0])) {
/*     */             this.hasWorldScreenshot = true;
/*     */           } else {
/*     */             takeAutoScreenshot(path);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private void takeAutoScreenshot(Path screenshotFile) {
/* 489 */     if (this.minecraft.levelExtractor.countRenderedSections() > 10 && this.minecraft.levelRenderer.hasRenderedAllSections())
/* 490 */       Screenshot.takeScreenshot(this.mainRenderTarget, screenshot -> Util.ioPool().execute(())); 
/*     */   }
/*     */   
/*     */   private boolean shouldRenderBlockOutline() {
/* 516 */     if (!this.renderBlockOutline)
/* 517 */       return false; 
/* 520 */     Entity cameraEntity = this.minecraft.getCameraEntity();
/* 521 */     boolean renderOutline = (cameraEntity instanceof Player && !this.minecraft.gui.hud.isHidden());
/* 522 */     if (renderOutline && !(((Player)cameraEntity).getAbilities()).mayBuild) {
/* 524 */       ItemStack itemStack = ((LivingEntity)cameraEntity).getMainHandItem();
/* 525 */       HitResult hitResult = this.minecraft.hitResult;
/* 526 */       if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK) {
/* 527 */         BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
/* 528 */         BlockState blockState = this.minecraft.level.getBlockState(pos);
/* 529 */         if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
/* 530 */           renderOutline = (blockState.getMenuProvider((Level)this.minecraft.level, pos) != null);
/*     */         } else {
/* 532 */           BlockInWorld blockInWorld = new BlockInWorld((LevelReader)this.minecraft.level, pos, false);
/* 533 */           Registry<Block> blockRegistry = this.minecraft.level.registryAccess().lookupOrThrow(Registries.BLOCK);
/* 534 */           renderOutline = (!itemStack.isEmpty() && (itemStack.canBreakBlockInAdventureMode(blockInWorld) || itemStack.canPlaceOnBlockInAdventureMode(blockInWorld)));
/*     */         } 
/*     */       } 
/*     */     } 
/* 538 */     return renderOutline;
/*     */   }
/*     */   
/*     */   public void renderLevel(DeltaTracker deltaTracker) {
/* 542 */     float worldPartialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
/* 543 */     float cameraEntityPartialTicks = this.mainCamera.getCameraEntityPartialTicks(deltaTracker);
/* 544 */     LocalPlayer player = this.minecraft.player;
/* 546 */     ProfilerFiller profiler = Profiler.get();
/* 547 */     boolean renderOutline = shouldRenderBlockOutline();
/* 549 */     OptionsRenderState optionsState = this.gameRenderState.optionsRenderState;
/* 551 */     CameraRenderState cameraState = this.gameRenderState.levelRenderState.cameraRenderState;
/* 552 */     Matrix4f matrix4f1 = cameraState.viewRotationMatrix;
/* 554 */     profiler.push("matrices");
/* 555 */     Matrix4f projectionMatrix = new Matrix4f((Matrix4fc)cameraState.projectionMatrix);
/* 557 */     PoseStack bobStack = new PoseStack();
/* 558 */     bobHurt(cameraState, bobStack);
/* 559 */     if (optionsState.bobView)
/* 560 */       bobView(cameraState, bobStack); 
/* 562 */     projectionMatrix.mul((Matrix4fc)bobStack.last().pose());
/* 564 */     float screenEffectScale = optionsState.screenEffectScale;
/* 565 */     float portalIntensity = Mth.lerp(worldPartialTicks, player.oPortalEffectIntensity, player.portalEffectIntensity);
/* 566 */     float nauseaIntensity = player.getEffectBlendFactor(MobEffects.NAUSEA, worldPartialTicks);
/* 567 */     float spinningEffectIntensity = Math.max(portalIntensity, nauseaIntensity) * screenEffectScale * screenEffectScale;
/* 568 */     if (spinningEffectIntensity > 0.0F) {
/* 569 */       float skew = 5.0F / (spinningEffectIntensity * spinningEffectIntensity + 5.0F) - spinningEffectIntensity * 0.04F;
/* 570 */       skew *= skew;
/* 572 */       Vector3f axis = new Vector3f(0.0F, Mth.SQRT_OF_TWO / 2.0F, Mth.SQRT_OF_TWO / 2.0F);
/* 573 */       float angle = (this.spinningEffectTime + worldPartialTicks * this.spinningEffectSpeed) * 0.017453292F;
/* 574 */       projectionMatrix.rotate(angle, (Vector3fc)axis);
/* 575 */       projectionMatrix.scale(1.0F / skew, 1.0F, 1.0F);
/* 576 */       projectionMatrix.rotate(-angle, (Vector3fc)axis);
/*     */     } 
/* 579 */     RenderSystem.setProjectionMatrix(this.levelProjectionMatrixBuffer.getBuffer(projectionMatrix), ProjectionType.PERSPECTIVE);
/* 581 */     profiler.popPush("fog");
/* 582 */     this.fogRenderer.updateBuffer(cameraState.fogData);
/* 583 */     GpuBufferSlice terrainFog = this.fogRenderer.getBuffer(FogRenderer.FogMode.WORLD);
/* 585 */     profiler.popPush("level");
/* 586 */     boolean shouldCreateBossFog = this.minecraft.gui.hud.getBossOverlay().shouldCreateWorldFog();
/* 588 */     this.minecraft.levelRenderer.render((GraphicsResourceAllocator)this.resourcePool, deltaTracker, renderOutline, cameraState, (Matrix4fc)matrix4f1, terrainFog, cameraState.fogData.color, !shouldCreateBossFog);
/* 590 */     profiler.popPush("hand");
/* 591 */     boolean isSleeping = cameraState.entityRenderState.isSleeping;
/* 592 */     this.hudProjection.setupPerspective(0.05F, 100.0F, cameraState.hudFov, this.gameRenderState.windowRenderState.width, this.gameRenderState.windowRenderState.height);
/* 593 */     RenderSystem.setProjectionMatrix(this.hud3dProjectionMatrixBuffer.getBuffer(this.hudProjection), ProjectionType.PERSPECTIVE);
/* 594 */     RenderSystem.getDevice().createCommandEncoder().clearDepthTexture(this.mainRenderTarget.getDepthTexture(), 0.0D);
/* 596 */     renderItemInHand(cameraState, cameraEntityPartialTicks, (Matrix4fc)matrix4f1);
/* 598 */     profiler.popPush("screenEffects");
/* 599 */     this.screenEffectRenderer.submit(optionsState.cameraType.isFirstPerson(), isSleeping, worldPartialTicks, this.handAndScreenSubmitNodeStorage, this.gameRenderState.guiRenderState.isHudHidden);
/* 600 */     this.featureRenderDispatcher.renderAllFeatures(this.handAndScreenSubmitNodeStorage);
/* 601 */     profiler.pop();
/* 602 */     RenderSystem.setShaderFog(this.fogRenderer.getBuffer(FogRenderer.FogMode.NONE));
/* 604 */     if (this.gameRenderState.levelRenderState.render3dCrosshair && optionsState.cameraType.isFirstPerson() && !this.gameRenderState.guiRenderState.isHudHidden)
/* 605 */       this.debugCrosshairRenderer.render(cameraState, this.gameRenderState.windowRenderState.guiScale); 
/*     */   }
/*     */   
/*     */   private void extractWindow() {
/* 610 */     WindowRenderState windowState = this.gameRenderState.windowRenderState;
/* 611 */     Window window = this.minecraft.getWindow();
/* 612 */     windowState.width = window.getWidth();
/* 613 */     windowState.height = window.getHeight();
/* 614 */     windowState.guiScale = window.getGuiScale();
/* 615 */     windowState.appropriateLineWidth = window.getAppropriateLineWidth();
/* 616 */     windowState.isMinimized = window.isMinimized();
/*     */   }
/*     */   
/*     */   private void extractOptions() {
/* 620 */     OptionsRenderState optionsState = this.gameRenderState.optionsRenderState;
/* 621 */     Options options = this.minecraft.options;
/* 622 */     optionsState.cloudRange = ((Integer)options.cloudRange().get()).intValue();
/* 623 */     optionsState.cutoutLeaves = ((Boolean)options.cutoutLeaves().get()).booleanValue();
/* 624 */     optionsState.improvedTransparency = ((Boolean)options.improvedTransparency().get()).booleanValue();
/* 625 */     optionsState.ambientOcclusion = ((Boolean)options.ambientOcclusion().get()).booleanValue();
/* 626 */     optionsState.menuBackgroundBlurriness = options.getMenuBackgroundBlurriness();
/* 627 */     optionsState.panoramaSpeed = ((Double)options.panoramaSpeed().get()).doubleValue();
/* 628 */     optionsState.maxAnisotropyValue = options.maxAnisotropyValue();
/* 629 */     optionsState.textureFiltering = (TextureFilteringMethod)options.textureFiltering().get();
/* 630 */     optionsState.bobView = ((Boolean)options.bobView().get()).booleanValue();
/* 631 */     optionsState.screenEffectScale = ((Double)options.screenEffectScale().get()).floatValue();
/* 632 */     optionsState.glintSpeed = ((Double)options.glintSpeed().get()).doubleValue();
/* 633 */     optionsState.glintStrength = ((Double)options.glintStrength().get()).doubleValue();
/* 634 */     optionsState.damageTiltStrength = ((Double)options.damageTiltStrength().get()).doubleValue();
/* 635 */     optionsState.backgroundForChatOnly = ((Boolean)options.backgroundForChatOnly().get()).booleanValue();
/* 636 */     optionsState.textBackgroundOpacity = ((Double)options.textBackgroundOpacity().get()).floatValue();
/* 637 */     optionsState.cloudStatus = options.getCloudStatus();
/* 638 */     optionsState.cameraType = options.getCameraType();
/* 639 */     optionsState.renderDistance = options.getEffectiveRenderDistance();
/* 640 */     optionsState.chunkSectionFadeInTime = ((Double)options.chunkSectionFadeInTime().get()).doubleValue();
/* 641 */     optionsState.prioritizeChunkUpdates = (PrioritizeChunkUpdates)options.prioritizeChunkUpdates().get();
/* 642 */     optionsState.fov = ((Integer)options.fov().get()).intValue();
/*     */   }
/*     */   
/*     */   private void extractCamera(DeltaTracker deltaTracker, float worldPartialTicks, float cameraEntityPartialTicks) {
/* 646 */     CameraRenderState cameraState = this.gameRenderState.levelRenderState.cameraRenderState;
/* 647 */     this.mainCamera.extractRenderState(cameraState, cameraEntityPartialTicks);
/* 648 */     cameraState.fogType = this.mainCamera.getFluidInCamera();
/* 649 */     cameraState.fogData = this.fogRenderer.setupFog(this.mainCamera, this.minecraft.options.getEffectiveRenderDistance(), deltaTracker, bossOverlayWorldDarkening(worldPartialTicks), this.minecraft.level);
/*     */   }
/*     */   
/*     */   public void resetData() {
/* 653 */     this.screenEffectRenderer.resetItemActivation();
/* 654 */     this.minecraft.getMapTextureManager().resetData();
/* 655 */     this.mainCamera.reset();
/* 656 */     this.hasWorldScreenshot = false;
/*     */   }
/*     */   
/*     */   public void displayItemActivation(ItemStack itemStack) {
/* 660 */     this.screenEffectRenderer.displayItemActivation(itemStack, this.random);
/*     */   }
/*     */   
/*     */   public float bossOverlayWorldDarkening(float a) {
/* 664 */     return Mth.lerp(a, this.bossOverlayWorldDarkeningO, this.bossOverlayWorldDarkening);
/*     */   }
/*     */   
/*     */   public Camera mainCamera() {
/* 668 */     return this.mainCamera;
/*     */   }
/*     */   
/*     */   public GpuTextureView lightmap() {
/* 672 */     return this.useUiLightmap ? this.uiLightmap.getTextureView() : this.lightmap.getTextureView();
/*     */   }
/*     */   
/*     */   public GpuTextureView levelLightmap() {
/* 676 */     return this.lightmap.getTextureView();
/*     */   }
/*     */   
/*     */   public OverlayTexture overlayTexture() {
/* 680 */     return this.overlayTexture;
/*     */   }
/*     */   
/*     */   public RenderTarget mainRenderTarget() {
/* 684 */     return this.mainRenderTarget;
/*     */   }
/*     */   
/*     */   public Vec3 projectPointToScreen(Vec3 point) {
/* 689 */     Matrix4f mvp = this.mainCamera.getViewRotationProjectionMatrix(new Matrix4f());
/* 691 */     Vec3 camPos = this.mainCamera.position();
/* 692 */     Vec3 offset = point.subtract(camPos);
/* 694 */     Vector3f vector3f = mvp.transformProject(offset.toVector3f());
/* 695 */     return new Vec3((Vector3fc)vector3f);
/*     */   }
/*     */   
/*     */   public double projectHorizonToScreen() {
/* 700 */     float xRot = this.mainCamera.xRot();
/* 701 */     if (xRot <= -90.0F)
/* 702 */       return Double.NEGATIVE_INFINITY; 
/* 703 */     if (xRot >= 90.0F)
/* 704 */       return Double.POSITIVE_INFINITY; 
/* 706 */     float fov = this.mainCamera.getFov();
/* 707 */     return Math.tan((xRot * 0.017453292F)) / Math.tan((fov / 2.0F * 0.017453292F));
/*     */   }
/*     */   
/*     */   public Lighting lighting() {
/* 712 */     return this.lighting;
/*     */   }
/*     */   
/*     */   public void setLevel(ClientLevel level) {
/* 716 */     if (level != null)
/* 717 */       this.lighting.updateLevel(level.dimensionType().cardinalLightType()); 
/* 719 */     this.mainCamera.setLevel(level);
/*     */   }
/*     */   
/*     */   public Panorama panorama() {
/* 723 */     return this.panorama;
/*     */   }
/*     */   
/*     */   public void registerPanoramaTextures(TextureManager textureManager) {
/* 727 */     this.guiRenderer.registerPanoramaTextures(textureManager);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\GameRenderer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */