/*     */ package net.minecraft.client.renderer.extract;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongCollection;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.SortedSet;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Camera;
/*     */ import net.minecraft.client.DeltaTracker;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.SectionUpdateTracker;
/*     */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*     */ import net.minecraft.client.multiplayer.ClientChunkCache;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*     */ import net.minecraft.client.renderer.LevelRenderer;
/*     */ import net.minecraft.client.renderer.SkyRenderer;
/*     */ import net.minecraft.client.renderer.ViewArea;
/*     */ import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
/*     */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*     */ import net.minecraft.client.renderer.chunk.CompiledSectionMesh;
/*     */ import net.minecraft.client.renderer.chunk.RenderRegionCache;
/*     */ import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
/*     */ import net.minecraft.client.renderer.culling.Frustum;
/*     */ import net.minecraft.client.renderer.debug.DebugRenderer;
/*     */ import net.minecraft.client.renderer.debug.GameTestBlockHighlightRenderer;
/*     */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*     */ import net.minecraft.client.renderer.state.level.BlockBreakingRenderState;
/*     */ import net.minecraft.client.renderer.state.level.BlockOutlineRenderState;
/*     */ import net.minecraft.client.renderer.state.level.LevelRenderState;
/*     */ import net.minecraft.client.renderer.state.level.SectionUpdateRenderState;
/*     */ import net.minecraft.client.server.IntegratedServer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.gizmos.GizmoCollector;
/*     */ import net.minecraft.gizmos.Gizmos;
/*     */ import net.minecraft.gizmos.SimpleGizmoCollector;
/*     */ import net.minecraft.server.level.BlockDestructionProgress;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.TickRateManager;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class LevelExtractor implements ResourceManagerReloadListener {
/*     */   private static final float CHUNK_VISIBILITY_THRESHOLD = 0.3F;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final LevelRenderer levelRenderer;
/*     */   
/*     */   private ClientLevel level;
/*     */   
/*     */   private SectionUpdateTracker sectionUpdateTracker;
/*     */   
/*     */   private final LevelRenderState levelRenderState;
/*     */   
/*  81 */   public final DebugRenderer debugRenderer = new DebugRenderer();
/*     */   
/*  82 */   public final GameTestBlockHighlightRenderer gameTestBlockHighlightRenderer = new GameTestBlockHighlightRenderer();
/*     */   
/*  84 */   private final SimpleGizmoCollector mainThreadGizmos = new SimpleGizmoCollector();
/*     */   
/*  86 */   private double prevCamRotX = Double.MIN_VALUE;
/*     */   
/*  87 */   private double prevCamRotY = Double.MIN_VALUE;
/*     */   
/*  89 */   private int lastViewDistance = -1;
/*     */   
/*     */   private boolean shouldInvalidateCompiledGeometry;
/*     */   
/*     */   private boolean shouldResetLevelRenderData;
/*     */   
/*     */   private boolean shouldResetChunkLayerSampler;
/*     */   
/*     */   private boolean shouldResetSkyRenderer;
/*     */   
/*     */   public LevelExtractor(Minecraft minecraft, LevelRenderState levelRenderState, LevelRenderer levelRenderer) {
/*  97 */     this.minecraft = minecraft;
/*  98 */     this.levelRenderer = levelRenderer;
/*  99 */     this.levelRenderState = levelRenderState;
/*     */   }
/*     */   
/*     */   public void extract(DeltaTracker deltaTracker, Camera camera, float deltaPartialTick) {
/* 103 */     if (this.minecraft.options.getEffectiveRenderDistance() != this.lastViewDistance)
/* 104 */       allChanged(); 
/* 107 */     Vec3 cameraPos = camera.position();
/* 108 */     if (this.sectionUpdateTracker != null)
/* 109 */       this.sectionUpdateTracker.repositionCamera(SectionPos.of((Position)cameraPos)); 
/* 112 */     if (this.shouldResetLevelRenderData) {
/* 113 */       this.levelRenderer.resetLevelRenderData();
/* 114 */       this.shouldResetLevelRenderData = false;
/*     */     } 
/* 117 */     this.levelRenderState.reset();
/* 119 */     ProfilerFiller profiler = Profiler.get();
/* 121 */     profiler.push("level");
/* 123 */     this.levelRenderState.shouldResetChunkLayerSampler = this.shouldResetChunkLayerSampler;
/* 124 */     this.shouldResetChunkLayerSampler = false;
/* 126 */     this.levelRenderState.shouldResetSkyRenderer = this.shouldResetSkyRenderer;
/* 127 */     this.shouldResetSkyRenderer = false;
/* 129 */     this.levelRenderState.gameTime = this.level.getGameTime();
/* 131 */     Frustum cullFrustum = camera.getCullFrustum();
/* 133 */     profiler.push("prepareDispatchers");
/* 134 */     this.levelRenderer.blockEntityRenderDispatcher().prepare(cameraPos);
/* 135 */     this.levelRenderer.entityRenderDispatcher().prepare(camera, this.minecraft.crosshairPickEntity);
/* 137 */     if (this.shouldInvalidateCompiledGeometry) {
/* 138 */       this.levelRenderer.invalidateCompiledGeometry(this.level, this.minecraft.options, camera, this.minecraft.getBlockColors());
/* 139 */       this.shouldInvalidateCompiledGeometry = false;
/* 140 */     } else if (camera.getCapturedFrustum() == null) {
/* 142 */       double camRotX = Math.floor((camera.xRot() / 2.0F));
/* 143 */       double camRotY = Math.floor((camera.yRot() / 2.0F));
/* 144 */       if (this.levelRenderer.sectionOcclusionGraph().consumeFrustumUpdate() || camRotX != this.prevCamRotX || camRotY != this.prevCamRotY) {
/* 145 */         profiler.popPush("applyFrustum");
/* 146 */         applyFrustum(cullFrustum);
/* 147 */         this.prevCamRotX = camRotX;
/* 148 */         this.prevCamRotY = camRotY;
/*     */       } 
/*     */     } 
/* 152 */     if (this.sectionUpdateTracker != null && this.level != null) {
/* 153 */       ClientChunkCache chunkCache = this.level.getChunkSource();
/* 154 */       this.levelRenderState.chunkLoadingRenderState.addedEmptySections = chunkCache.addedEmptySections();
/* 155 */       this.levelRenderState.chunkLoadingRenderState.removedEmptySections = chunkCache.removedEmptySections();
/* 156 */       this.levelRenderState.chunkLoadingRenderState.addedLoadedChunks = chunkCache.addedLoadedChunks();
/* 157 */       this.levelRenderState.chunkLoadingRenderState.removedLoadedChunks = chunkCache.removedLoadedChunks();
/* 158 */       chunkCache.flipUpdateTrackingSets();
/* 160 */       LongCollection expectedChunks = this.levelRenderer.expectedChunks();
/* 161 */       expectedChunks.forEach(expectedChunk -> {
/*     */             if (chunkCache.hasChunk(ChunkPos.getX(expectedChunk), ChunkPos.getZ(expectedChunk)))
/*     */               this.levelRenderState.chunkLoadingRenderState.loadedExpectedChunks.add(expectedChunk); 
/*     */           });
/* 167 */       profiler.popPush("sectionUpdates");
/* 168 */       RenderRegionCache cache = new RenderRegionCache();
/* 169 */       for (ObjectListIterator<SectionRenderDispatcher.RenderSection> objectListIterator = this.levelRenderer.visibleSections().iterator(); objectListIterator.hasNext(); ) {
/* 169 */         SectionRenderDispatcher.RenderSection section = objectListIterator.next();
/* 170 */         SectionUpdateTracker.SectionDirtyState dirtyState = this.sectionUpdateTracker.getDirtyState(section.getSectionNode());
/* 171 */         if (dirtyState != null && dirtyState.isDirty() && (section.sectionMesh.get() != CompiledSectionMesh.UNCOMPILED || this.sectionUpdateTracker.hasAllNeighbors(this.level, section.getSectionNode()))) {
/* 172 */           this.levelRenderState.sectionUpdateRenderStates.add(new SectionUpdateRenderState(section.getSectionNode(), dirtyState.isDirtyFromPlayer(), cache.createRegion(this.level, section.getSectionNode())));
/* 173 */           dirtyState.setNotDirty();
/*     */         } 
/*     */       } 
/*     */     } 
/* 178 */     profiler.popPush("entities");
/* 179 */     extractVisibleEntities(camera, cullFrustum, deltaTracker, this.levelRenderState);
/* 181 */     profiler.popPush("blockEntities");
/* 182 */     extractVisibleBlockEntities(camera, deltaPartialTick, this.levelRenderState);
/* 184 */     profiler.popPush("blockOutline");
/* 185 */     extractBlockOutline(camera, this.levelRenderState);
/* 187 */     profiler.popPush("blockBreaking");
/* 188 */     extractBlockDestroyAnimation(camera, this.levelRenderState);
/* 190 */     profiler.popPush("weather");
/* 191 */     this.levelRenderer.weatherEffectRenderer().extractRenderState(this.level, deltaPartialTick, cameraPos, this.levelRenderState.weatherRenderState);
/* 193 */     SkyRenderer skyRenderer = this.levelRenderer.skyRenderer();
/* 194 */     if (skyRenderer != null) {
/* 195 */       profiler.popPush("sky");
/* 196 */       skyRenderer.extractRenderState(this.level, deltaPartialTick, camera, this.levelRenderState.skyRenderState);
/*     */     } 
/* 198 */     profiler.popPush("border");
/* 199 */     this.levelRenderer.worldBorderRenderer().extract(this.level.getWorldBorder(), deltaPartialTick, cameraPos, (this.minecraft.options.getEffectiveRenderDistance() * 16), this.levelRenderState.worldBorderRenderState);
/* 201 */     profiler.popPush("particles");
/* 202 */     this.minecraft.particleEngine.extract(this.levelRenderState.particlesRenderState, (new Frustum(cullFrustum)).offset(-3.0F), camera, deltaPartialTick);
/* 204 */     profiler.popPush("cloud");
/* 205 */     this.levelRenderState.cloudColor = ((Integer)camera.attributeProbe().getValue(EnvironmentAttributes.CLOUD_COLOR, deltaPartialTick)).intValue();
/* 206 */     if (ARGB.alpha(this.levelRenderState.cloudColor) > 0)
/* 207 */       this.levelRenderState.cloudHeight = ((Float)camera.attributeProbe().getValue(EnvironmentAttributes.CLOUD_HEIGHT, deltaPartialTick)).floatValue(); 
/* 210 */     profiler.popPush("debug");
/* 214 */     this.debugRenderer.emitGizmos(cullFrustum, cameraPos.x, cameraPos.y, cameraPos.z, deltaTracker.getGameTimeDeltaPartialTick(false));
/* 215 */     this.gameTestBlockHighlightRenderer.emitGizmos();
/* 217 */     this.levelRenderState.render3dCrosshair = this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.THREE_DIMENSIONAL_CROSSHAIR);
/* 219 */     ClientPacketListener connection = this.minecraft.getConnection();
/* 220 */     if (connection != null)
/* 221 */       this.levelRenderState.playerCompiledSectionCallback = connection.getPlayerCompiledSectionCallback(); 
/* 224 */     this.levelRenderState.shouldShowEntityOutlines = shouldShowEntityOutlines(camera);
/* 226 */     extractGizmos();
/* 228 */     profiler.pop();
/* 229 */     profiler.pop();
/*     */   }
/*     */   
/*     */   private void extractVisibleEntities(Camera camera, Frustum frustum, DeltaTracker deltaTracker, LevelRenderState output) {
/* 233 */     Vec3 cameraPos = camera.position();
/* 234 */     double camX = cameraPos.x();
/* 235 */     double camY = cameraPos.y();
/* 236 */     double camZ = cameraPos.z();
/* 237 */     TickRateManager tickRateManager = this.minecraft.level.tickRateManager();
/* 239 */     Entity.setViewScale(Mth.clamp(this.minecraft.options.getEffectiveRenderDistance() / 8.0D, 1.0D, 2.5D) * ((Double)this.minecraft.options.entityDistanceScaling().get()).doubleValue());
/* 241 */     EntityRenderDispatcher entityRenderDispatcher = this.levelRenderer.entityRenderDispatcher();
/* 243 */     for (Entity entity : this.level.entitiesForRendering()) {
/* 244 */       if (!isEntityVisible(entity, frustum, camX, camY, camZ))
/*     */         continue; 
/* 247 */       if (entity == camera.entity() && !camera.isDetached() && (!(camera.entity() instanceof LivingEntity) || !((LivingEntity)camera.entity()).isSleeping()))
/*     */         continue; 
/* 250 */       if (entity instanceof net.minecraft.client.player.LocalPlayer && camera.entity() != entity)
/*     */         continue; 
/* 254 */       if (entity.tickCount == 0) {
/* 255 */         entity.xOld = entity.getX();
/* 256 */         entity.yOld = entity.getY();
/* 257 */         entity.zOld = entity.getZ();
/*     */       } 
/* 259 */       float partialEntity = deltaTracker.getGameTimeDeltaPartialTick(!tickRateManager.isEntityFrozen(entity));
/* 260 */       EntityRenderState state = extractEntity(entity, partialEntity);
/* 261 */       output.entityRenderStates.add(state);
/*     */     } 
/* 263 */     output.lastEntityRenderStateCount = output.entityRenderStates.size();
/*     */   }
/*     */   
/*     */   public boolean isEntityVisible(Entity entity, Frustum frustum, double camX, double camY, double camZ) {
/* 267 */     if (this.level == null)
/* 268 */       return false; 
/* 270 */     if (!this.levelRenderer.entityRenderDispatcher().shouldRender(entity, frustum, camX, camY, camZ) && (this.minecraft.player == null || !entity.hasIndirectPassenger((Entity)this.minecraft.player)))
/* 271 */       return false; 
/* 273 */     BlockPos blockPos = entity.blockPosition();
/* 274 */     if (!this.level.isOutsideBuildHeight(blockPos.getY()) && !this.levelRenderer.isSectionCompiledAndVisible(blockPos))
/* 275 */       return false; 
/* 277 */     return true;
/*     */   }
/*     */   
/*     */   private EntityRenderState extractEntity(Entity entity, float partialTickTime) {
/* 281 */     return this.levelRenderer.entityRenderDispatcher().extractEntity(entity, partialTickTime);
/*     */   }
/*     */   
/*     */   private void extractVisibleBlockEntities(Camera camera, float deltaPartialTick, LevelRenderState levelRenderState) {
/* 285 */     Vec3 cameraPos = camera.position();
/* 286 */     double camX = cameraPos.x();
/* 287 */     double camY = cameraPos.y();
/* 288 */     double camZ = cameraPos.z();
/* 291 */     PoseStack poseStack = new PoseStack();
/* 293 */     for (ObjectListIterator<SectionRenderDispatcher.RenderSection> objectListIterator = this.levelRenderer.visibleSections().iterator(); objectListIterator.hasNext(); ) {
/* 293 */       SectionRenderDispatcher.RenderSection section = objectListIterator.next();
/* 294 */       List<BlockEntity> renderableBlockEntities = section.getSectionMesh().getRenderableBlockEntities();
/* 295 */       if (renderableBlockEntities.isEmpty() || section.getVisibility(Util.getMillis()) < 0.3F)
/*     */         continue; 
/* 299 */       for (BlockEntity blockEntity : renderableBlockEntities) {
/*     */         ModelFeatureRenderer.CrumblingOverlay breakProgress;
/* 300 */         BlockPos blockPos = blockEntity.getBlockPos();
/* 301 */         SortedSet<BlockDestructionProgress> progresses = (SortedSet<BlockDestructionProgress>)this.level.destructionProgress().get(blockPos.asLong());
/* 303 */         if (progresses == null || progresses.isEmpty()) {
/* 304 */           breakProgress = null;
/*     */         } else {
/* 306 */           poseStack.pushPose();
/* 307 */           poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
/* 308 */           breakProgress = new ModelFeatureRenderer.CrumblingOverlay(((BlockDestructionProgress)progresses.last()).getProgress(), poseStack.last());
/* 309 */           poseStack.popPose();
/*     */         } 
/* 311 */         BlockEntityRenderState state = this.levelRenderer.blockEntityRenderDispatcher().tryExtractRenderState(blockEntity, deltaPartialTick, breakProgress, false);
/* 312 */         if (state != null)
/* 313 */           levelRenderState.blockEntityRenderStates.add(state); 
/*     */       } 
/*     */     } 
/* 318 */     for (Iterator<BlockEntity> iterator = this.level.getGloballyRenderedBlockEntities().iterator(); iterator.hasNext(); ) {
/* 319 */       BlockEntity blockEntity = iterator.next();
/* 320 */       if (blockEntity.isRemoved()) {
/* 321 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 324 */       BlockEntityRenderState state = this.levelRenderer.blockEntityRenderDispatcher().tryExtractRenderState(blockEntity, deltaPartialTick, null, true);
/* 325 */       if (state != null)
/* 326 */         levelRenderState.blockEntityRenderStates.add(state); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractBlockDestroyAnimation(Camera camera, LevelRenderState levelRenderState) {
/* 332 */     Vec3 cameraPos = camera.position();
/* 333 */     double camX = cameraPos.x();
/* 334 */     double camY = cameraPos.y();
/* 335 */     double camZ = cameraPos.z();
/* 337 */     levelRenderState.blockBreakingRenderStates.clear();
/* 338 */     for (ObjectIterator<Long2ObjectMap.Entry<SortedSet<BlockDestructionProgress>>> objectIterator = this.level.destructionProgress().long2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) {
/* 338 */       Long2ObjectMap.Entry<SortedSet<BlockDestructionProgress>> entry = objectIterator.next();
/* 339 */       BlockPos pos = BlockPos.of(entry.getLongKey());
/* 340 */       if (pos.distToCenterSqr(camX, camY, camZ) > 1024.0D)
/*     */         continue; 
/* 344 */       SortedSet<BlockDestructionProgress> progresses = (SortedSet<BlockDestructionProgress>)entry.getValue();
/* 345 */       if (progresses == null || progresses.isEmpty())
/*     */         continue; 
/* 348 */       int progress = ((BlockDestructionProgress)progresses.last()).getProgress();
/* 349 */       levelRenderState.blockBreakingRenderStates.add(new BlockBreakingRenderState(pos, this.level.getBlockState(pos), progress));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractBlockOutline(Camera camera, LevelRenderState levelRenderState) {
/*     */     BlockHitResult blockHitResult;
/* 354 */     levelRenderState.blockOutlineRenderState = null;
/* 355 */     HitResult hitResult = this.minecraft.hitResult;
/* 355 */     if (hitResult instanceof BlockHitResult) {
/* 355 */       blockHitResult = (BlockHitResult)hitResult;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 359 */     if (blockHitResult.getType() == HitResult.Type.MISS)
/*     */       return; 
/* 362 */     BlockPos pos = blockHitResult.getBlockPos();
/* 363 */     BlockState state = this.level.getBlockState(pos);
/* 364 */     if (!state.isAir() && this.level.getWorldBorder().isWithinBounds(pos)) {
/* 365 */       BlockStateModel blockStateModel = this.minecraft.getModelManager().getBlockStateModelSet().get(state);
/* 366 */       boolean isBlockTranslucent = blockStateModel.hasMaterialFlag(1);
/* 367 */       boolean highContrast = ((Boolean)this.minecraft.options.highContrastBlockOutline().get()).booleanValue();
/* 368 */       CollisionContext context = CollisionContext.of(camera.entity());
/* 369 */       VoxelShape shape = state.getShape((BlockGetter)this.level, pos, context);
/* 370 */       if (SharedConstants.DEBUG_SHAPES) {
/* 371 */         VoxelShape collisionShape = state.getCollisionShape((BlockGetter)this.level, pos, context);
/* 372 */         VoxelShape occlusionShape = state.getOcclusionShape();
/* 373 */         VoxelShape interactionShape = state.getInteractionShape((BlockGetter)this.level, pos);
/* 374 */         levelRenderState.blockOutlineRenderState = new BlockOutlineRenderState(pos, isBlockTranslucent, highContrast, shape, collisionShape, occlusionShape, interactionShape);
/*     */       } else {
/* 376 */         levelRenderState.blockOutlineRenderState = new BlockOutlineRenderState(pos, isBlockTranslucent, highContrast, shape);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void extractGizmos() {
/* 382 */     this.mainThreadGizmos.addTemporaryGizmos(Minecraft.getInstance().getPerTickGizmos());
/* 383 */     IntegratedServer server = Minecraft.getInstance().getSingleplayerServer();
/* 384 */     if (server != null)
/* 385 */       this.mainThreadGizmos.addTemporaryGizmos(server.getPerTickGizmos()); 
/* 387 */     this.levelRenderer.addMainThreadGizmos(this.mainThreadGizmos.drainGizmos());
/*     */   }
/*     */   
/*     */   private void applyFrustum(Frustum frustum) {
/* 391 */     if (!Minecraft.getInstance().isSameThread())
/* 392 */       throw new IllegalStateException("applyFrustum called from wrong thread: " + Thread.currentThread().getName()); 
/* 394 */     this.levelRenderer.clearVisibleSections();
/* 395 */     this.levelRenderer.sectionOcclusionGraph().addSectionsInFrustum(frustum, (List)this.levelRenderer.visibleSections(), (List)this.levelRenderer.nearbyVisibleSections());
/*     */   }
/*     */   
/*     */   private boolean shouldShowEntityOutlines(Camera camera) {
/* 399 */     return (!camera.isPanoramicMode() && this.minecraft.player != null);
/*     */   }
/*     */   
/*     */   public void onResourceManagerReload(ResourceManager resourceManager) {
/* 404 */     this.shouldResetSkyRenderer = true;
/*     */   }
/*     */   
/*     */   public void setLevel(ClientLevel level) {
/* 408 */     this.level = level;
/* 409 */     if (level != null) {
/* 410 */       allChanged();
/*     */     } else {
/* 412 */       this.levelRenderer.entityRenderDispatcher().resetCamera();
/* 413 */       this.sectionUpdateTracker = null;
/*     */     } 
/* 415 */     this.shouldResetLevelRenderData = true;
/* 417 */     this.gameTestBlockHighlightRenderer.clear();
/*     */   }
/*     */   
/*     */   public void allChanged() {
/* 421 */     if (this.level == null)
/*     */       return; 
/* 425 */     this.level.clearTintCaches();
/* 427 */     Options options = this.minecraft.options;
/* 428 */     this.lastViewDistance = options.getEffectiveRenderDistance();
/* 430 */     this.sectionUpdateTracker = new SectionUpdateTracker((LevelHeightAccessor)this.level, this.lastViewDistance);
/* 432 */     Camera camera = this.minecraft.gameRenderer.mainCamera();
/* 433 */     SectionPos cameraSectionPos = SectionPos.of((Position)camera.position());
/* 434 */     this.sectionUpdateTracker.repositionCamera(cameraSectionPos);
/* 436 */     this.shouldInvalidateCompiledGeometry = true;
/*     */   }
/*     */   
/*     */   public void resetSampler() {
/* 440 */     this.shouldResetChunkLayerSampler = true;
/*     */   }
/*     */   
/*     */   public void blockChanged(BlockPos pos, int updateFlags) {
/* 444 */     setBlockDirty(pos, ((updateFlags & 0x8) != 0));
/*     */   }
/*     */   
/*     */   private void setBlockDirty(BlockPos pos, boolean playerChanged) {
/* 448 */     for (int z = pos.getZ() - 1; z <= pos.getZ() + 1; z++) {
/* 449 */       for (int x = pos.getX() - 1; x <= pos.getX() + 1; x++) {
/* 450 */         for (int y = pos.getY() - 1; y <= pos.getY() + 1; y++)
/* 451 */           setSectionDirty(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(y), SectionPos.blockToSectionCoord(z), playerChanged); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setBlocksDirty(int x0, int y0, int z0, int x1, int y1, int z1) {
/* 458 */     for (int z = z0 - 1; z <= z1 + 1; z++) {
/* 459 */       for (int x = x0 - 1; x <= x1 + 1; x++) {
/* 460 */         for (int y = y0 - 1; y <= y1 + 1; y++)
/* 461 */           setSectionDirty(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(y), SectionPos.blockToSectionCoord(z)); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setBlockDirty(BlockPos pos, BlockState oldState, BlockState newState) {
/* 468 */     if (this.minecraft.getModelManager().requiresRender(oldState, newState))
/* 469 */       setBlocksDirty(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ()); 
/*     */   }
/*     */   
/*     */   public void setSectionDirtyWithNeighbors(int sectionX, int sectionY, int sectionZ) {
/* 474 */     setSectionRangeDirty(sectionX - 1, sectionY - 1, sectionZ - 1, sectionX + 1, sectionY + 1, sectionZ + 1);
/*     */   }
/*     */   
/*     */   public void setSectionRangeDirty(int minSectionX, int minSectionY, int minSectionZ, int maxSectionX, int maxSectionY, int maxSectionZ) {
/* 478 */     for (int z = minSectionZ; z <= maxSectionZ; z++) {
/* 479 */       for (int x = minSectionX; x <= maxSectionX; x++) {
/* 480 */         for (int y = minSectionY; y <= maxSectionY; y++)
/* 481 */           setSectionDirty(x, y, z); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setSectionDirty(int sectionX, int sectionY, int sectionZ) {
/* 488 */     setSectionDirty(sectionX, sectionY, sectionZ, false);
/*     */   }
/*     */   
/*     */   private void setSectionDirty(int sectionX, int sectionY, int sectionZ, boolean playerChanged) {
/* 492 */     this.sectionUpdateTracker.setDirty(sectionX, sectionY, sectionZ, playerChanged);
/*     */   }
/*     */   
/*     */   public Gizmos.TemporaryCollection collectPerFrameMainThreadGizmos() {
/* 496 */     return Gizmos.withCollector((GizmoCollector)this.mainThreadGizmos);
/*     */   }
/*     */   
/*     */   public int countRenderedSections() {
/* 500 */     int rendered = 0;
/* 501 */     for (ObjectListIterator<SectionRenderDispatcher.RenderSection> objectListIterator = this.levelRenderer.visibleSections().iterator(); objectListIterator.hasNext(); ) {
/* 501 */       SectionRenderDispatcher.RenderSection section = objectListIterator.next();
/* 502 */       if (section.getSectionMesh().hasRenderableLayers())
/* 503 */         rendered++; 
/*     */     } 
/* 506 */     return rendered;
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public String sectionStatistics() {
/* 511 */     ViewArea viewArea = this.levelRenderer.viewArea();
/* 512 */     if (viewArea == null)
/* 513 */       return null; 
/* 515 */     int totalSections = viewArea.size();
/* 516 */     int rendered = countRenderedSections();
/* 518 */     SectionRenderDispatcher sectionRenderDispatcher = this.levelRenderer.sectionRenderDispatcher();
/* 520 */     return String.format(Locale.ROOT, "C: %d/%d %sD: %d, %s", new Object[] { Integer.valueOf(rendered), 
/* 522 */           Integer.valueOf(totalSections), 
/* 523 */           this.minecraft.smartCull ? "(s) " : "", 
/* 524 */           Integer.valueOf(this.lastViewDistance), 
/* 525 */           (sectionRenderDispatcher == null) ? "null" : sectionRenderDispatcher.getStats() });
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public String entityStatistics() {
/* 531 */     if (this.level == null)
/* 532 */       return null; 
/* 534 */     return "E: " + this.levelRenderState.lastEntityRenderStateCount + "/" + this.level.getEntityCount() + ", SD: " + this.level.getServerSimulationDistance();
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public double totalSections() {
/* 539 */     return (this.sectionUpdateTracker == null) ? 0.0D : this.sectionUpdateTracker.size();
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public double lastViewDistance() {
/* 544 */     return this.lastViewDistance;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\extract\LevelExtractor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */