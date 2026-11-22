/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortListIterator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.debug.DebugStructureInfo;
/*     */ import net.minecraft.util.debug.DebugSubscriptions;
/*     */ import net.minecraft.util.debug.DebugValueSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EntityBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.TickingBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.gameevent.EuclideanGameEventListenerRegistry;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.GameEventListenerRegistry;
/*     */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.BlendingData;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.LightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.ticks.LevelChunkTicks;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LevelChunk extends ChunkAccess implements DebugValueSource {
/*  66 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  68 */   private static final TickingBlockEntity NULL_TICKER = new TickingBlockEntity() {
/*     */       public void tick() {}
/*     */       
/*     */       public boolean isRemoved() {
/*  75 */         return true;
/*     */       }
/*     */       
/*     */       public BlockPos getPos() {
/*  80 */         return BlockPos.ZERO;
/*     */       }
/*     */       
/*     */       public String getType() {
/*  85 */         return "<null>";
/*     */       }
/*     */     };
/*     */   
/*  89 */   private final Map<BlockPos, RebindableTickingBlockEntityWrapper> tickersInLevel = Maps.newHashMap();
/*     */   
/*     */   private boolean loaded;
/*     */   
/*     */   private final Level level;
/*     */   
/*     */   private Supplier<FullChunkStatus> fullStatus;
/*     */   
/*     */   private PostLoadProcessor postLoad;
/*     */   
/*     */   private final Int2ObjectMap<GameEventListenerRegistry> gameEventListenerRegistrySections;
/*     */   
/*     */   private final LevelChunkTicks<Block> blockTicks;
/*     */   
/*     */   private final LevelChunkTicks<Fluid> fluidTicks;
/*     */   
/*     */   private UnsavedListener unsavedListener = chunkPos -> {
/*     */     
/*     */     };
/*     */   
/*     */   public LevelChunk(Level level, ChunkPos pos) {
/* 104 */     this(level, pos, UpgradeData.EMPTY, new LevelChunkTicks(), new LevelChunkTicks(), 0L, (LevelChunkSection[])null, (PostLoadProcessor)null, (BlendingData)null);
/*     */   }
/*     */   
/*     */   public LevelChunk(Level level, ChunkPos pos, UpgradeData upgradeData, LevelChunkTicks<Block> blockTicks, LevelChunkTicks<Fluid> fluidTicks, long inhabitedTime, LevelChunkSection[] sections, PostLoadProcessor postLoad, BlendingData blendingData) {
/* 108 */     super(pos, upgradeData, (LevelHeightAccessor)level, level.palettedContainerFactory(), inhabitedTime, sections, blendingData);
/* 109 */     this.level = level;
/* 111 */     this.gameEventListenerRegistrySections = (Int2ObjectMap<GameEventListenerRegistry>)new Int2ObjectOpenHashMap();
/* 113 */     for (Heightmap.Types type : Heightmap.Types.values()) {
/* 114 */       if (ChunkStatus.FULL.heightmapsAfter().contains(type))
/* 115 */         this.heightmaps.put(type, new Heightmap(this, type)); 
/*     */     } 
/* 119 */     this.postLoad = postLoad;
/* 120 */     this.blockTicks = blockTicks;
/* 121 */     this.fluidTicks = fluidTicks;
/*     */   }
/*     */   
/*     */   public LevelChunk(ServerLevel level, ProtoChunk protoChunk, PostLoadProcessor postLoad) {
/* 125 */     this((Level)level, protoChunk.getPos(), protoChunk.getUpgradeData(), protoChunk.unpackBlockTicks(), protoChunk.unpackFluidTicks(), protoChunk.getInhabitedTime(), protoChunk.getSections(), postLoad, protoChunk.getBlendingData());
/* 127 */     if (!Collections.disjoint(protoChunk.pendingBlockEntities.keySet(), protoChunk.blockEntities.keySet()))
/* 128 */       LOGGER.error("Chunk at {} contains duplicated block entities", protoChunk.getPos()); 
/* 131 */     for (BlockEntity blockEntity : protoChunk.getBlockEntities().values())
/* 132 */       setBlockEntity(blockEntity); 
/* 135 */     this.pendingBlockEntities.putAll(protoChunk.getBlockEntityNbts());
/* 137 */     for (int i = 0; i < (protoChunk.getPostProcessing()).length; i++)
/* 138 */       this.postProcessing[i] = protoChunk.getPostProcessing()[i]; 
/* 141 */     setAllStarts(protoChunk.getAllStarts());
/* 142 */     setAllReferences(protoChunk.getAllReferences());
/* 144 */     for (Map.Entry<Heightmap.Types, Heightmap> entry : protoChunk.getHeightmaps()) {
/* 145 */       if (ChunkStatus.FULL.heightmapsAfter().contains(entry.getKey()))
/* 146 */         setHeightmap(entry.getKey(), ((Heightmap)entry.getValue()).getRawData()); 
/*     */     } 
/* 150 */     this.skyLightSources = protoChunk.skyLightSources;
/* 151 */     setLightCorrect(protoChunk.isLightCorrect());
/* 153 */     markUnsaved();
/*     */   }
/*     */   
/*     */   public void setUnsavedListener(UnsavedListener unsavedListener) {
/* 157 */     this.unsavedListener = unsavedListener;
/* 158 */     if (isUnsaved())
/* 159 */       unsavedListener.setUnsaved(this.chunkPos); 
/*     */   }
/*     */   
/*     */   public void markUnsaved() {
/* 165 */     boolean wasUnsaved = isUnsaved();
/* 166 */     super.markUnsaved();
/* 167 */     if (!wasUnsaved)
/* 168 */       this.unsavedListener.setUnsaved(this.chunkPos); 
/*     */   }
/*     */   
/*     */   public TickContainerAccess<Block> getBlockTicks() {
/* 174 */     return (TickContainerAccess<Block>)this.blockTicks;
/*     */   }
/*     */   
/*     */   public TickContainerAccess<Fluid> getFluidTicks() {
/* 179 */     return (TickContainerAccess<Fluid>)this.fluidTicks;
/*     */   }
/*     */   
/*     */   public ChunkAccess.PackedTicks getTicksForSerialization(long currentTick) {
/* 184 */     return new ChunkAccess.PackedTicks(this.blockTicks.pack(currentTick), this.fluidTicks.pack(currentTick));
/*     */   }
/*     */   
/*     */   public GameEventListenerRegistry getListenerRegistry(int section) {
/* 189 */     Level level = this.level;
/* 189 */     if (level instanceof ServerLevel) {
/* 189 */       ServerLevel serverLevel = (ServerLevel)level;
/* 190 */       return (GameEventListenerRegistry)this.gameEventListenerRegistrySections.computeIfAbsent(section, key -> new EuclideanGameEventListenerRegistry(serverLevel, section, this::removeGameEventListenerRegistry));
/*     */     } 
/* 192 */     return super.getListenerRegistry(section);
/*     */   }
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/* 197 */     int x = pos.getX();
/* 198 */     int y = pos.getY();
/* 199 */     int z = pos.getZ();
/* 200 */     if (this.level.isDebug()) {
/* 201 */       BlockState blockState = null;
/* 202 */       if (y == 60)
/* 203 */         blockState = Blocks.BARRIER.defaultBlockState(); 
/* 205 */       if (y == 70)
/* 206 */         blockState = DebugLevelSource.getBlockStateFor(x, z); 
/* 208 */       return (blockState == null) ? Blocks.AIR.defaultBlockState() : blockState;
/*     */     } 
/*     */     try {
/* 212 */       int sectionIndex = getSectionIndex(y);
/* 213 */       if (sectionIndex >= 0 && sectionIndex < this.sections.length) {
/* 214 */         LevelChunkSection currentSection = this.sections[sectionIndex];
/* 215 */         if (!currentSection.hasOnlyAir())
/* 216 */           return currentSection.getBlockState(x & 0xF, y & 0xF, z & 0xF); 
/*     */       } 
/* 219 */       return Blocks.AIR.defaultBlockState();
/* 220 */     } catch (Throwable t) {
/* 221 */       CrashReport report = CrashReport.forThrowable(t, "Getting block state");
/* 222 */       CrashReportCategory category = report.addCategory("Block being got");
/* 223 */       category.setDetail("Location", () -> CrashReportCategory.formatLocation((LevelHeightAccessor)this, x, y, z));
/* 224 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public FluidState getFluidState(BlockPos pos) {
/* 230 */     return getFluidState(pos.getX(), pos.getY(), pos.getZ());
/*     */   }
/*     */   
/*     */   public FluidState getFluidState(int x, int y, int z) {
/*     */     try {
/* 235 */       int sectionIndex = getSectionIndex(y);
/* 236 */       if (sectionIndex >= 0 && sectionIndex < this.sections.length) {
/* 237 */         LevelChunkSection currentSection = this.sections[sectionIndex];
/* 238 */         if (!currentSection.hasOnlyAir())
/* 239 */           return currentSection.getFluidState(x & 0xF, y & 0xF, z & 0xF); 
/*     */       } 
/* 242 */       return Fluids.EMPTY.defaultFluidState();
/* 243 */     } catch (Throwable t) {
/* 244 */       CrashReport report = CrashReport.forThrowable(t, "Getting fluid state");
/* 245 */       CrashReportCategory category = report.addCategory("Block being got");
/* 246 */       category.setDetail("Location", () -> CrashReportCategory.formatLocation((LevelHeightAccessor)this, x, y, z));
/* 247 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockState setBlockState(BlockPos pos, BlockState state, int flags) {
/* 253 */     int y = pos.getY();
/* 255 */     LevelChunkSection section = getSection(getSectionIndex(y));
/* 256 */     boolean wasEmpty = section.hasOnlyAir();
/* 258 */     if (wasEmpty && state.isAir())
/* 259 */       return null; 
/* 262 */     int localX = pos.getX() & 0xF;
/* 263 */     int localY = y & 0xF;
/* 264 */     int localZ = pos.getZ() & 0xF;
/* 265 */     BlockState oldState = section.setBlockState(localX, localY, localZ, state);
/* 267 */     if (oldState == state)
/* 268 */       return null; 
/* 271 */     Block newBlock = state.getBlock();
/* 273 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING)).update(localX, y, localZ, state);
/* 274 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)).update(localX, y, localZ, state);
/* 275 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.OCEAN_FLOOR)).update(localX, y, localZ, state);
/* 276 */     ((Heightmap)this.heightmaps.get(Heightmap.Types.WORLD_SURFACE)).update(localX, y, localZ, state);
/* 277 */     boolean isEmpty = section.hasOnlyAir();
/* 278 */     if (wasEmpty != isEmpty) {
/* 279 */       this.level.getChunkSource().getLightEngine().updateSectionStatus(pos, isEmpty);
/* 280 */       this.level.getChunkSource().onSectionEmptinessChanged(this.chunkPos.x(), SectionPos.blockToSectionCoord(y), this.chunkPos.z(), isEmpty);
/*     */     } 
/* 283 */     if (LightEngine.hasDifferentLightProperties(oldState, state)) {
/* 284 */       ProfilerFiller profiler = Profiler.get();
/* 285 */       profiler.push("updateSkyLightSources");
/* 286 */       this.skyLightSources.update(this, localX, y, localZ);
/* 287 */       profiler.popPush("queueCheckLight");
/* 288 */       this.level.getChunkSource().getLightEngine().checkBlock(pos);
/* 289 */       profiler.pop();
/*     */     } 
/* 292 */     boolean blockChanged = !oldState.is(newBlock);
/* 293 */     boolean movedByPiston = ((flags & 0x40) != 0);
/* 294 */     boolean sideEffects = ((flags & 0x100) == 0);
/* 296 */     if (blockChanged && oldState.hasBlockEntity() && !state.shouldChangedStateKeepBlockEntity(oldState)) {
/* 297 */       if (!this.level.isClientSide() && sideEffects) {
/* 298 */         BlockEntity blockEntity = this.level.getBlockEntity(pos);
/* 299 */         if (blockEntity != null)
/* 300 */           blockEntity.preRemoveSideEffects(pos, oldState); 
/*     */       } 
/* 303 */       removeBlockEntity(pos);
/*     */     } 
/* 306 */     if (blockChanged || newBlock instanceof net.minecraft.world.level.block.BaseRailBlock) {
/* 307 */       Level level = this.level;
/* 307 */       if (level instanceof ServerLevel) {
/* 307 */         ServerLevel serverLevel = (ServerLevel)level;
/* 313 */         if ((flags & 0x1) != 0 || movedByPiston)
/* 314 */           oldState.affectNeighborsAfterRemoval(serverLevel, pos, movedByPiston); 
/*     */       } 
/*     */     } 
/* 319 */     if (!section.getBlockState(localX, localY, localZ).is(newBlock))
/* 320 */       return null; 
/* 323 */     if (!this.level.isClientSide() && (flags & 0x200) == 0)
/* 324 */       state.onPlace(this.level, pos, oldState, movedByPiston); 
/* 330 */     if (state.hasBlockEntity() && section.getBlockState(localX, localY, localZ).is(newBlock)) {
/* 331 */       BlockEntity blockEntity = getBlockEntity(pos, EntityCreationType.CHECK);
/* 335 */       if (blockEntity != null && !blockEntity.isValidBlockState(state)) {
/* 336 */         LOGGER.warn("Found mismatched block entity @ {}: type = {}, state = {}", new Object[] { pos, blockEntity.typeHolder().getRegisteredName(), state });
/* 337 */         removeBlockEntity(pos);
/* 338 */         blockEntity = null;
/*     */       } 
/* 341 */       if (blockEntity == null) {
/* 342 */         blockEntity = ((EntityBlock)newBlock).newBlockEntity(pos, state);
/* 343 */         if (blockEntity != null)
/* 344 */           addAndRegisterBlockEntity(blockEntity); 
/*     */       } else {
/* 347 */         blockEntity.setBlockState(state);
/* 348 */         updateBlockEntityTicker(blockEntity);
/*     */       } 
/*     */     } 
/* 352 */     markUnsaved();
/* 353 */     return oldState;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void addEntity(Entity entity) {}
/*     */   
/*     */   private BlockEntity createBlockEntity(BlockPos pos) {
/* 363 */     BlockState state = getBlockState(pos);
/* 364 */     if (!state.hasBlockEntity())
/* 365 */       return null; 
/* 368 */     return ((EntityBlock)state.getBlock()).newBlockEntity(pos, state);
/*     */   }
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 373 */     return getBlockEntity(pos, EntityCreationType.CHECK);
/*     */   }
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos, EntityCreationType creationType) {
/* 377 */     BlockEntity blockEntity = this.blockEntities.get(pos);
/* 378 */     if (blockEntity == null) {
/* 379 */       CompoundTag tag = this.pendingBlockEntities.remove(pos);
/* 380 */       if (tag != null) {
/* 381 */         BlockEntity promoted = promotePendingBlockEntity(pos, tag);
/* 382 */         if (promoted != null)
/* 383 */           return promoted; 
/*     */       } 
/*     */     } 
/* 387 */     if (blockEntity == null) {
/* 388 */       if (creationType == EntityCreationType.IMMEDIATE) {
/* 389 */         blockEntity = createBlockEntity(pos);
/* 390 */         if (blockEntity != null)
/* 391 */           addAndRegisterBlockEntity(blockEntity); 
/*     */       } 
/* 394 */     } else if (blockEntity.isRemoved()) {
/* 395 */       this.blockEntities.remove(pos);
/* 396 */       return null;
/*     */     } 
/* 399 */     return blockEntity;
/*     */   }
/*     */   
/*     */   public void addAndRegisterBlockEntity(BlockEntity blockEntity) {
/* 403 */     setBlockEntity(blockEntity);
/* 405 */     if (isInLevel()) {
/* 406 */       Level level = this.level;
/* 406 */       if (level instanceof ServerLevel) {
/* 406 */         ServerLevel serverLevel = (ServerLevel)level;
/* 407 */         addGameEventListener(blockEntity, serverLevel);
/*     */       } 
/* 409 */       this.level.onBlockEntityAdded(blockEntity);
/* 410 */       updateBlockEntityTicker(blockEntity);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isInLevel() {
/* 415 */     return (this.loaded || this.level.isClientSide());
/*     */   }
/*     */   
/*     */   private boolean isTicking(BlockPos pos) {
/* 419 */     if (!this.level.getWorldBorder().isWithinBounds(pos))
/* 420 */       return false; 
/* 423 */     Level level = this.level;
/* 423 */     if (level instanceof ServerLevel) {
/* 423 */       ServerLevel serverLevel = (ServerLevel)level;
/* 424 */       return (getFullStatus().isOrAfter(FullChunkStatus.BLOCK_TICKING) && serverLevel
/* 425 */         .areEntitiesLoaded(ChunkPos.pack(pos)));
/*     */     } 
/* 428 */     return true;
/*     */   }
/*     */   
/*     */   public void setBlockEntity(BlockEntity blockEntity) {
/* 433 */     BlockPos pos = blockEntity.getBlockPos();
/* 434 */     BlockState blockState = getBlockState(pos);
/* 437 */     if (!blockState.hasBlockEntity()) {
/* 438 */       LOGGER.warn("Trying to set block entity {} at position {}, but state {} does not allow it", new Object[] { blockEntity, pos, blockState });
/*     */       return;
/*     */     } 
/* 442 */     BlockState cachedBlockState = blockEntity.getBlockState();
/* 443 */     if (blockState != cachedBlockState) {
/* 445 */       if (!blockEntity.getType().isValid(blockState)) {
/* 446 */         LOGGER.warn("Trying to set block entity {} at position {}, but state {} does not allow it", new Object[] { blockEntity, pos, blockState });
/*     */         return;
/*     */       } 
/* 450 */       if (blockState.getBlock() != cachedBlockState.getBlock())
/* 452 */         LOGGER.warn("Block state mismatch on block entity {} in position {}, {} != {}, updating", new Object[] { blockEntity, pos, blockState, cachedBlockState }); 
/* 454 */       blockEntity.setBlockState(blockState);
/*     */     } 
/* 457 */     blockEntity.setLevel(this.level);
/* 458 */     blockEntity.clearRemoved();
/* 460 */     BlockEntity previousEntry = this.blockEntities.put(pos.immutable(), blockEntity);
/* 461 */     if (previousEntry != null && previousEntry != blockEntity)
/* 462 */       previousEntry.setRemoved(); 
/*     */   }
/*     */   
/*     */   public CompoundTag getBlockEntityNbtForSaving(BlockPos blockPos, HolderLookup.Provider registryAccess) {
/* 468 */     BlockEntity blockEntity = getBlockEntity(blockPos);
/* 469 */     if (blockEntity != null && !blockEntity.isRemoved()) {
/* 470 */       CompoundTag compoundTag = blockEntity.saveWithFullMetadata((HolderLookup.Provider)this.level.registryAccess());
/* 471 */       compoundTag.putBoolean("keepPacked", false);
/* 472 */       return compoundTag;
/*     */     } 
/* 474 */     CompoundTag result = this.pendingBlockEntities.get(blockPos);
/* 475 */     if (result != null) {
/* 476 */       result = result.copy();
/* 477 */       result.putBoolean("keepPacked", true);
/*     */     } 
/* 479 */     return result;
/*     */   }
/*     */   
/*     */   public void removeBlockEntity(BlockPos pos) {
/* 484 */     if (isInLevel()) {
/* 485 */       BlockEntity removeThis = this.blockEntities.remove(pos);
/* 486 */       if (removeThis != null) {
/* 487 */         Level level = this.level;
/* 487 */         if (level instanceof ServerLevel) {
/* 487 */           ServerLevel serverLevel = (ServerLevel)level;
/* 488 */           removeGameEventListener(removeThis, serverLevel);
/* 489 */           serverLevel.debugSynchronizers().dropBlockEntity(pos);
/*     */         } 
/* 491 */         removeThis.setRemoved();
/*     */       } 
/*     */     } 
/* 495 */     removeBlockEntityTicker(pos);
/*     */   }
/*     */   
/*     */   private <T extends BlockEntity> void removeGameEventListener(T blockEntity, ServerLevel level) {
/* 499 */     Block block = blockEntity.getBlockState().getBlock();
/* 501 */     if (block instanceof EntityBlock) {
/* 501 */       EntityBlock entityBlock = (EntityBlock)block;
/* 502 */       GameEventListener listener = entityBlock.getListener(level, (BlockEntity)blockEntity);
/* 503 */       if (listener != null) {
/* 504 */         int section = SectionPos.blockToSectionCoord(blockEntity.getBlockPos().getY());
/* 506 */         GameEventListenerRegistry listenerRegistry = getListenerRegistry(section);
/* 507 */         listenerRegistry.unregister(listener);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeGameEventListenerRegistry(int sectionY) {
/* 513 */     this.gameEventListenerRegistrySections.remove(sectionY);
/*     */   }
/*     */   
/*     */   private void removeBlockEntityTicker(BlockPos pos) {
/* 517 */     RebindableTickingBlockEntityWrapper ticker = this.tickersInLevel.remove(pos);
/* 518 */     if (ticker != null)
/* 519 */       ticker.rebind(NULL_TICKER); 
/*     */   }
/*     */   
/*     */   public void runPostLoad() {
/* 524 */     if (this.postLoad != null) {
/* 525 */       this.postLoad.run(this);
/* 526 */       this.postLoad = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 531 */     return false;
/*     */   }
/*     */   
/*     */   public void replaceWithPacketData(FriendlyByteBuf buffer, Map<Heightmap.Types, long[]> heightmaps, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> blockEntities) {
/* 536 */     clearAllBlockEntities();
/* 538 */     for (LevelChunkSection section : this.sections)
/* 539 */       section.read(buffer); 
/* 542 */     heightmaps.forEach(this::setHeightmap);
/* 544 */     initializeLightSources();
/* 546 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER);
/*     */     try {
/* 547 */       blockEntities.accept((pos, type, tag) -> {
/*     */             BlockEntity blockEntity = getBlockEntity(pos, EntityCreationType.IMMEDIATE);
/*     */             if (blockEntity != null && tag != null && blockEntity.getType() == type)
/*     */               blockEntity.loadWithComponents(TagValueInput.create(reporter.forChild(blockEntity.problemPath()), (HolderLookup.Provider)this.level.registryAccess(), tag)); 
/*     */           });
/* 555 */       reporter.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         reporter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void replaceBiomes(FriendlyByteBuf buffer) {
/* 559 */     for (LevelChunkSection section : this.sections)
/* 560 */       section.readBiomes(buffer); 
/*     */   }
/*     */   
/*     */   public void setLoaded(boolean loaded) {
/* 565 */     this.loaded = loaded;
/*     */   }
/*     */   
/*     */   public Level getLevel() {
/* 569 */     return this.level;
/*     */   }
/*     */   
/*     */   public Map<BlockPos, BlockEntity> getBlockEntities() {
/* 573 */     return this.blockEntities;
/*     */   }
/*     */   
/*     */   public void postProcessGeneration(ServerLevel level) {
/* 577 */     ChunkPos chunkPos = getPos();
/* 578 */     for (int sectionIndex = 0; sectionIndex < this.postProcessing.length; sectionIndex++) {
/* 579 */       ShortList postProcessingSection = this.postProcessing[sectionIndex];
/* 580 */       if (postProcessingSection != null) {
/* 581 */         for (ShortListIterator<Short> shortListIterator = postProcessingSection.iterator(); shortListIterator.hasNext(); ) {
/* 581 */           Short packedOffset = shortListIterator.next();
/* 582 */           BlockPos blockPos = ProtoChunk.unpackOffsetCoordinates(packedOffset.shortValue(), getSectionYFromSectionIndex(sectionIndex), chunkPos);
/* 583 */           BlockState blockState = getBlockState(blockPos);
/* 584 */           FluidState fluidState = blockState.getFluidState();
/* 585 */           if (!fluidState.isEmpty())
/* 586 */             fluidState.tick(level, blockPos, blockState); 
/* 590 */           if (blockState.getBlock() instanceof net.minecraft.world.level.block.LiquidBlock) {
/* 591 */             blockState.tick(level, blockPos, level.getRandom());
/*     */             continue;
/*     */           } 
/* 593 */           BlockState blockStateNew = Block.updateFromNeighbourShapes(blockState, (LevelAccessor)level, blockPos);
/* 594 */           if (blockStateNew != blockState)
/* 595 */             level.setBlock(blockPos, blockStateNew, 276); 
/*     */         } 
/* 599 */         postProcessingSection.clear();
/*     */       } 
/*     */     } 
/* 603 */     for (UnmodifiableIterator<BlockPos> unmodifiableIterator = ImmutableList.copyOf(this.pendingBlockEntities.keySet()).iterator(); unmodifiableIterator.hasNext(); ) {
/* 603 */       BlockPos pos = unmodifiableIterator.next();
/* 604 */       getBlockEntity(pos);
/*     */     } 
/* 606 */     this.pendingBlockEntities.clear();
/* 607 */     this.upgradeData.upgrade(this);
/*     */   }
/*     */   
/*     */   private BlockEntity promotePendingBlockEntity(BlockPos pos, CompoundTag tag) {
/*     */     BlockEntity blockEntity;
/* 612 */     BlockState state = getBlockState(pos);
/* 613 */     if ("DUMMY".equals(tag.getStringOr("id", ""))) {
/* 614 */       if (state.hasBlockEntity()) {
/* 615 */         blockEntity = ((EntityBlock)state.getBlock()).newBlockEntity(pos, state);
/*     */       } else {
/* 617 */         blockEntity = null;
/* 618 */         LOGGER.warn("Tried to load a DUMMY block entity @ {} but found not block entity block {} at location", pos, state);
/*     */       } 
/*     */     } else {
/* 621 */       blockEntity = BlockEntity.loadStatic(pos, state, tag, (HolderLookup.Provider)this.level.registryAccess());
/*     */     } 
/* 624 */     if (blockEntity != null) {
/* 625 */       blockEntity.setLevel(this.level);
/* 626 */       addAndRegisterBlockEntity(blockEntity);
/*     */     } else {
/* 628 */       LOGGER.warn("Tried to load a block entity for block {} but failed at location {}", state, pos);
/*     */     } 
/* 631 */     return blockEntity;
/*     */   }
/*     */   
/*     */   public void unpackTicks(long currentTick) {
/* 635 */     this.blockTicks.unpack(currentTick);
/* 636 */     this.fluidTicks.unpack(currentTick);
/*     */   }
/*     */   
/*     */   public void registerTickContainerInLevel(ServerLevel level) {
/* 640 */     level.getBlockTicks().addContainer(this.chunkPos, this.blockTicks);
/* 641 */     level.getFluidTicks().addContainer(this.chunkPos, this.fluidTicks);
/*     */   }
/*     */   
/*     */   public void unregisterTickContainerFromLevel(ServerLevel level) {
/* 645 */     level.getBlockTicks().removeContainer(this.chunkPos);
/* 646 */     level.getFluidTicks().removeContainer(this.chunkPos);
/*     */   }
/*     */   
/*     */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {
/* 652 */     if (!getAllStarts().isEmpty())
/* 653 */       registration.register(DebugSubscriptions.STRUCTURES, () -> {
/*     */             List<DebugStructureInfo> structures = new ArrayList<>();
/*     */             for (StructureStart start : getAllStarts().values()) {
/*     */               BoundingBox boundingBox = start.getBoundingBox();
/*     */               List<StructurePiece> pieces = start.getPieces();
/*     */               List<DebugStructureInfo.Piece> pieceInfos = new ArrayList<>(pieces.size());
/*     */               for (int i = 0; i < pieces.size(); i++) {
/*     */                 boolean isStart = (i == 0);
/*     */                 pieceInfos.add(new DebugStructureInfo.Piece(((StructurePiece)pieces.get(i)).getBoundingBox(), isStart));
/*     */               } 
/*     */               structures.add(new DebugStructureInfo(boundingBox, pieceInfos));
/*     */             } 
/*     */             return structures;
/*     */           }); 
/* 668 */     registration.register(DebugSubscriptions.RAIDS, () -> level.getRaids().getRaidCentersInChunk(this.chunkPos));
/*     */   }
/*     */   
/*     */   public enum EntityCreationType {
/* 672 */     IMMEDIATE, QUEUED, CHECK;
/*     */   }
/*     */   
/*     */   public ChunkStatus getPersistedStatus() {
/* 679 */     return ChunkStatus.FULL;
/*     */   }
/*     */   
/*     */   public FullChunkStatus getFullStatus() {
/* 683 */     if (this.fullStatus == null)
/* 684 */       return FullChunkStatus.FULL; 
/* 686 */     return this.fullStatus.get();
/*     */   }
/*     */   
/*     */   public void setFullStatus(Supplier<FullChunkStatus> fullStatus) {
/* 690 */     this.fullStatus = fullStatus;
/*     */   }
/*     */   
/*     */   public void clearAllBlockEntities() {
/* 694 */     this.blockEntities.values().forEach(BlockEntity::setRemoved);
/* 695 */     this.blockEntities.clear();
/* 697 */     this.tickersInLevel.values().forEach(ticker -> ticker.rebind(NULL_TICKER));
/* 698 */     this.tickersInLevel.clear();
/*     */   }
/*     */   
/*     */   public void registerAllBlockEntitiesAfterLevelLoad() {
/* 702 */     this.blockEntities.values().forEach(blockEntity -> {
/*     */           Level patt0$temp = this.level;
/*     */           if (patt0$temp instanceof ServerLevel) {
/*     */             ServerLevel serverLevel = (ServerLevel)patt0$temp;
/*     */             addGameEventListener(blockEntity, serverLevel);
/*     */           } 
/*     */           this.level.onBlockEntityAdded(blockEntity);
/*     */           updateBlockEntityTicker(blockEntity);
/*     */         });
/*     */   }
/*     */   
/*     */   private <T extends BlockEntity> void addGameEventListener(T blockEntity, ServerLevel level) {
/* 712 */     Block block = blockEntity.getBlockState().getBlock();
/* 714 */     if (block instanceof EntityBlock) {
/* 714 */       EntityBlock entityBlock = (EntityBlock)block;
/* 715 */       GameEventListener listener = entityBlock.getListener(level, (BlockEntity)blockEntity);
/* 716 */       if (listener != null)
/* 717 */         getListenerRegistry(SectionPos.blockToSectionCoord(blockEntity.getBlockPos().getY())).register(listener); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T extends BlockEntity> void updateBlockEntityTicker(T blockEntity) {
/* 723 */     BlockState state = blockEntity.getBlockState();
/* 724 */     BlockEntityTicker<T> ticker = state.getTicker(this.level, blockEntity.getType());
/* 725 */     if (ticker == null) {
/* 726 */       removeBlockEntityTicker(blockEntity.getBlockPos());
/*     */     } else {
/* 728 */       this.tickersInLevel.compute(blockEntity.getBlockPos(), (blockPos, existingTicker) -> {
/*     */             TickingBlockEntity actualTicker = createTicker(blockEntity, ticker);
/*     */             if (existingTicker != null) {
/*     */               existingTicker.rebind(actualTicker);
/*     */               return existingTicker;
/*     */             } 
/*     */             if (isInLevel()) {
/*     */               RebindableTickingBlockEntityWrapper result = new RebindableTickingBlockEntityWrapper(actualTicker);
/*     */               this.level.addBlockEntityTicker(result);
/*     */               return result;
/*     */             } 
/*     */             return null;
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private <T extends BlockEntity> TickingBlockEntity createTicker(T blockEntity, BlockEntityTicker<T> ticker) {
/* 747 */     return new BoundTickingBlockEntity<>(blockEntity, ticker);
/*     */   }
/*     */   
/*     */   private class BoundTickingBlockEntity<T extends BlockEntity> implements TickingBlockEntity {
/*     */     private final T blockEntity;
/*     */     
/*     */     private final BlockEntityTicker<T> ticker;
/*     */     
/*     */     private boolean loggedInvalidBlockState;
/*     */     
/*     */     private BoundTickingBlockEntity(T blockEntity, BlockEntityTicker<T> ticker) {
/* 756 */       this.blockEntity = blockEntity;
/* 757 */       this.ticker = ticker;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 762 */       if (!this.blockEntity.isRemoved() && this.blockEntity.hasLevel()) {
/* 763 */         BlockPos pos = this.blockEntity.getBlockPos();
/* 764 */         if (LevelChunk.this.isTicking(pos))
/*     */           try {
/* 766 */             ProfilerFiller profiler = Profiler.get();
/* 767 */             profiler.push(this::getType);
/* 768 */             BlockState blockState = LevelChunk.this.getBlockState(pos);
/* 769 */             if (this.blockEntity.getType().isValid(blockState)) {
/* 770 */               this.ticker.tick(LevelChunk.this.level, this.blockEntity.getBlockPos(), blockState, (BlockEntity)this.blockEntity);
/* 771 */               this.loggedInvalidBlockState = false;
/* 773 */             } else if (!this.loggedInvalidBlockState) {
/* 774 */               this.loggedInvalidBlockState = true;
/* 775 */               LevelChunk.LOGGER.warn("Block entity {} @ {} state {} invalid for ticking:", new Object[] { LogUtils.defer(this::getType), LogUtils.defer(this::getPos), blockState });
/*     */             } 
/* 778 */             profiler.pop();
/* 779 */           } catch (Throwable t) {
/* 780 */             CrashReport report = CrashReport.forThrowable(t, "Ticking block entity");
/* 781 */             CrashReportCategory category = report.addCategory("Block entity being ticked");
/* 782 */             this.blockEntity.fillCrashReportCategory(category);
/* 784 */             throw new ReportedException(report);
/*     */           }  
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isRemoved() {
/* 792 */       return this.blockEntity.isRemoved();
/*     */     }
/*     */     
/*     */     public BlockPos getPos() {
/* 797 */       return this.blockEntity.getBlockPos();
/*     */     }
/*     */     
/*     */     public String getType() {
/* 802 */       return this.blockEntity.typeHolder().getRegisteredName();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 807 */       return "Level ticker for " + getType() + "@" + String.valueOf(getPos());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RebindableTickingBlockEntityWrapper implements TickingBlockEntity {
/*     */     private TickingBlockEntity ticker;
/*     */     
/*     */     private RebindableTickingBlockEntityWrapper(TickingBlockEntity ticker) {
/* 815 */       this.ticker = ticker;
/*     */     }
/*     */     
/*     */     private void rebind(TickingBlockEntity ticker) {
/* 819 */       this.ticker = ticker;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 824 */       this.ticker.tick();
/*     */     }
/*     */     
/*     */     public boolean isRemoved() {
/* 829 */       return this.ticker.isRemoved();
/*     */     }
/*     */     
/*     */     public BlockPos getPos() {
/* 834 */       return this.ticker.getPos();
/*     */     }
/*     */     
/*     */     public String getType() {
/* 839 */       return this.ticker.getType();
/*     */     }
/*     */     
/*     */     public String toString() {
/* 844 */       return String.valueOf(this.ticker) + " <wrapped>";
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface PostLoadProcessor {
/*     */     void run(LevelChunk param1LevelChunk);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface UnsavedListener {
/*     */     void setUnsaved(ChunkPos param1ChunkPos);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\LevelChunk.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */