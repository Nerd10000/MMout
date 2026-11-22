/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.EntityBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStep;
/*     */ import net.minecraft.world.level.chunk.status.ChunkType;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.ticks.LevelTickAccess;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import net.minecraft.world.ticks.WorldGenTickAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class WorldGenRegion implements WorldGenLevel {
/*  64 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final StaticCache2D<GenerationChunkHolder> cache;
/*     */   
/*     */   private final ChunkAccess center;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final long seed;
/*     */   
/*     */   private final LevelData levelData;
/*     */   
/*     */   private final RandomSource random;
/*     */   
/*     */   private final DimensionType dimensionType;
/*     */   
/*     */   private final WorldGenTickAccess<Block> blockTicks;
/*     */   
/*     */   private final WorldGenTickAccess<Fluid> fluidTicks;
/*     */   
/*     */   private final BiomeManager biomeManager;
/*     */   
/*     */   private final ChunkStep generatingStep;
/*     */   
/*     */   private Supplier<String> currentlyGenerating;
/*     */   
/*     */   private final AtomicLong subTickCount;
/*     */   
/*  79 */   private static final Identifier WORLDGEN_REGION_RANDOM = Identifier.withDefaultNamespace("worldgen_region_random");
/*     */   
/*     */   private final int centerChunkX;
/*     */   
/*     */   private final int centerChunkZ;
/*     */   
/*     */   private final int writeRadius;
/*     */   
/*     */   public WorldGenRegion(ServerLevel level, StaticCache2D<GenerationChunkHolder> cache, ChunkStep generatingStep, ChunkAccess center) {
/*     */     this.blockTicks = new WorldGenTickAccess(pos -> getChunk(pos).getBlockTicks());
/*     */     this.fluidTicks = new WorldGenTickAccess(pos -> getChunk(pos).getFluidTicks());
/*     */     this.subTickCount = new AtomicLong();
/*  87 */     this.generatingStep = generatingStep;
/*  88 */     this.cache = cache;
/*  89 */     this.center = center;
/*  90 */     this.level = level;
/*  91 */     this.seed = level.getSeed();
/*  92 */     this.levelData = level.getLevelData();
/*  93 */     this.random = level.getChunkSource().randomState().getOrCreateRandomFactory(WORLDGEN_REGION_RANDOM).at(this.center.getPos().getWorldPosition());
/*  95 */     this.dimensionType = level.dimensionType();
/*  96 */     this.biomeManager = new BiomeManager((BiomeManager.NoiseBiomeSource)this, BiomeManager.obfuscateSeed(this.seed));
/*  98 */     ChunkPos centerPos = center.getPos();
/*  99 */     this.centerChunkX = centerPos.x();
/* 100 */     this.centerChunkZ = centerPos.z();
/* 101 */     this.writeRadius = generatingStep.blockStateWriteRadius();
/*     */   }
/*     */   
/*     */   public boolean isOldChunkAround(ChunkPos pos, int range) {
/* 105 */     return (this.level.getChunkSource()).chunkMap.isOldChunkAround(pos, range);
/*     */   }
/*     */   
/*     */   public ChunkPos getCenter() {
/* 109 */     return this.center.getPos();
/*     */   }
/*     */   
/*     */   public void setCurrentlyGenerating(Supplier<String> currentlyGenerating) {
/* 114 */     this.currentlyGenerating = currentlyGenerating;
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunk(int chunkX, int chunkZ) {
/* 119 */     return getChunk(chunkX, chunkZ, ChunkStatus.EMPTY);
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus targetStatus, boolean loadOrGenerate) {
/*     */     GenerationChunkHolder chunkHolder;
/* 124 */     int distance = this.center.getPos().getChessboardDistance(chunkX, chunkZ);
/* 125 */     ChunkStatus maxAllowedStatus = (distance >= this.generatingStep.directDependencies().size()) ? null : this.generatingStep.directDependencies().get(distance);
/* 127 */     if (maxAllowedStatus != null) {
/* 128 */       chunkHolder = (GenerationChunkHolder)this.cache.get(chunkX, chunkZ);
/* 129 */       if (targetStatus.isOrBefore(maxAllowedStatus)) {
/* 130 */         ChunkAccess chunk = chunkHolder.getChunkIfPresentUnchecked(maxAllowedStatus);
/* 131 */         if (chunk != null)
/* 132 */           return chunk; 
/*     */       } 
/*     */     } else {
/* 136 */       chunkHolder = null;
/*     */     } 
/* 138 */     CrashReport report = CrashReport.forThrowable(new IllegalStateException("Requested chunk unavailable during world generation"), "Exception generating new chunk");
/* 139 */     CrashReportCategory category = report.addCategory("Chunk request details");
/* 140 */     category.setDetail("Requested chunk", String.format(Locale.ROOT, "%d, %d", new Object[] { Integer.valueOf(chunkX), Integer.valueOf(chunkZ) }));
/* 141 */     category.setDetail("Generating status", () -> this.generatingStep.targetStatus().getName());
/* 142 */     Objects.requireNonNull(targetStatus);
/* 142 */     category.setDetail("Requested status", targetStatus::getName);
/* 143 */     category.setDetail("Actual status", () -> (chunkHolder == null) ? "[out of cache bounds]" : chunkHolder.getPersistedStatus().getName());
/* 144 */     category.setDetail("Maximum allowed status", () -> (maxAllowedStatus == null) ? "null" : maxAllowedStatus.getName());
/* 145 */     Objects.requireNonNull(this.generatingStep.directDependencies());
/* 145 */     category.setDetail("Dependencies", this.generatingStep.directDependencies()::toString);
/* 146 */     category.setDetail("Requested distance", Integer.valueOf(distance));
/* 147 */     Objects.requireNonNull(this.center.getPos());
/* 147 */     category.setDetail("Generating chunk", this.center.getPos()::toString);
/* 148 */     throw new ReportedException(report);
/*     */   }
/*     */   
/*     */   public boolean hasChunk(int chunkX, int chunkZ) {
/* 153 */     int distance = this.center.getPos().getChessboardDistance(chunkX, chunkZ);
/* 154 */     return (distance < this.generatingStep.directDependencies().size());
/*     */   }
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/* 159 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/* 160 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/* 161 */     warnIfReadOutsideWriteZone(chunkX, chunkZ);
/* 162 */     return getChunk(chunkX, chunkZ).getBlockState(pos);
/*     */   }
/*     */   
/*     */   public FluidState getFluidState(BlockPos pos) {
/* 167 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/* 168 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/* 169 */     warnIfReadOutsideWriteZone(chunkX, chunkZ);
/* 170 */     return getChunk(chunkX, chunkZ).getFluidState(pos);
/*     */   }
/*     */   
/*     */   public Player getNearestPlayer(double x, double y, double z, double maxDist, Predicate<Entity> predicate) {
/* 175 */     return null;
/*     */   }
/*     */   
/*     */   public int getSkyDarken() {
/* 180 */     return 0;
/*     */   }
/*     */   
/*     */   public BiomeManager getBiomeManager() {
/* 185 */     return this.biomeManager;
/*     */   }
/*     */   
/*     */   public Holder<Biome> getUncachedNoiseBiome(int quartX, int quartY, int quartZ) {
/* 190 */     return this.level.getUncachedNoiseBiome(quartX, quartY, quartZ);
/*     */   }
/*     */   
/*     */   public LevelLightEngine getLightEngine() {
/* 195 */     return this.level.getLightEngine();
/*     */   }
/*     */   
/*     */   public boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker, int updateLimit) {
/* 200 */     BlockState blockState = getBlockState(pos);
/* 201 */     if (blockState.isAir())
/* 202 */       return false; 
/* 207 */     return setBlock(pos, Blocks.AIR.defaultBlockState(), 3, updateLimit);
/*     */   }
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 213 */     ChunkAccess chunk = getChunk(pos);
/* 214 */     BlockEntity blockEntity = chunk.getBlockEntity(pos);
/* 216 */     if (blockEntity != null)
/* 217 */       return blockEntity; 
/* 220 */     CompoundTag tag = chunk.getBlockEntityNbt(pos);
/* 221 */     BlockState state = chunk.getBlockState(pos);
/* 222 */     if (tag != null) {
/* 223 */       if ("DUMMY".equals(tag.getStringOr("id", ""))) {
/* 224 */         if (!state.hasBlockEntity())
/* 225 */           return null; 
/* 227 */         blockEntity = ((EntityBlock)state.getBlock()).newBlockEntity(pos, state);
/*     */       } else {
/* 229 */         blockEntity = BlockEntity.loadStatic(pos, state, tag, (HolderLookup.Provider)this.level.registryAccess());
/*     */       } 
/* 232 */       if (blockEntity != null) {
/* 233 */         chunk.setBlockEntity(blockEntity);
/* 234 */         return blockEntity;
/*     */       } 
/*     */     } 
/* 238 */     if (state.hasBlockEntity())
/* 239 */       LOGGER.warn("Tried to access a block entity before it was created. {}", pos); 
/* 242 */     return null;
/*     */   }
/*     */   
/*     */   private void warnIfReadOutsideWriteZone(int chunkX, int chunkZ) {
/* 250 */     if (this.centerChunkX == chunkX && this.centerChunkZ == chunkZ)
/*     */       return; 
/* 255 */     if (!isWithinWriteZone(chunkX, chunkZ)) {
/* 256 */       int readDistance = Math.max(Math.abs(this.centerChunkX - chunkX), Math.abs(this.centerChunkZ - chunkZ));
/* 262 */       String warning = "Detected unsafe terrain read during worldgen: reading from chunk [" + chunkX + ", " + chunkZ + "] while generating chunk [" + this.centerChunkX + ", " + this.centerChunkZ + "] (distance: " + readDistance + ", write radius: " + this.writeRadius + "), step: " + this.generatingStep.targetStatus().getName() + ((this.currentlyGenerating == null) ? "" : (", currently generating: " + (String)this.currentlyGenerating.get()));
/* 264 */       Util.logAndPauseIfInIde(warning);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isWithinWriteZone(BlockPos pos) {
/* 271 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/* 272 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/* 273 */     return isWithinWriteZone(chunkX, chunkZ);
/*     */   }
/*     */   
/*     */   private boolean isWithinWriteZone(int chunkX, int chunkZ) {
/* 277 */     return (Math.abs(this.centerChunkX - chunkX) <= this.writeRadius && Math.abs(this.centerChunkZ - chunkZ) <= this.writeRadius);
/*     */   }
/*     */   
/*     */   public boolean ensureCanWrite(BlockPos pos) {
/* 282 */     if (!isWithinWriteZone(pos)) {
/* 283 */       int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/* 284 */       int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/* 285 */       Util.logAndPauseIfInIde("Detected setBlock in a far chunk [" + chunkX + ", " + chunkZ + "], pos: " + String.valueOf(pos) + ", status: " + String.valueOf(this.generatingStep.targetStatus()) + ((this.currentlyGenerating == null) ? "" : (", currently generating: " + (String)this.currentlyGenerating.get())));
/* 286 */       return false;
/*     */     } 
/* 289 */     if (this.center.isUpgrading()) {
/* 290 */       LevelHeightAccessor levelHeightAccessor = this.center.getHeightAccessorForGeneration();
/* 291 */       return !levelHeightAccessor.isOutsideBuildHeight(pos.getY());
/*     */     } 
/* 294 */     return true;
/*     */   }
/*     */   
/*     */   public boolean setBlock(BlockPos pos, BlockState blockState, int updateFlags, int updateLimit) {
/* 299 */     if (!ensureCanWrite(pos))
/* 300 */       return false; 
/* 303 */     ChunkAccess chunk = getChunk(pos);
/* 304 */     BlockState oldState = chunk.setBlockState(pos, blockState, updateFlags);
/* 306 */     if (oldState != null)
/* 307 */       this.level.updatePOIOnBlockStateChange(pos, oldState, blockState); 
/* 312 */     if (blockState.hasBlockEntity()) {
/* 313 */       if (chunk.getPersistedStatus().getChunkType() == ChunkType.LEVELCHUNK) {
/* 314 */         BlockEntity blockEntity = ((EntityBlock)blockState.getBlock()).newBlockEntity(pos, blockState);
/* 315 */         if (blockEntity != null) {
/* 316 */           chunk.setBlockEntity(blockEntity);
/*     */         } else {
/* 318 */           chunk.removeBlockEntity(pos);
/*     */         } 
/*     */       } else {
/* 321 */         CompoundTag tag = new CompoundTag();
/* 322 */         tag.putInt("x", pos.getX());
/* 323 */         tag.putInt("y", pos.getY());
/* 324 */         tag.putInt("z", pos.getZ());
/* 325 */         tag.putString("id", "DUMMY");
/* 326 */         chunk.setBlockEntityNbt(tag);
/*     */       } 
/* 328 */     } else if (oldState != null && oldState.hasBlockEntity()) {
/* 329 */       chunk.removeBlockEntity(pos);
/*     */     } 
/* 332 */     if ((updateFlags & 0x10) == 0) {
/* 333 */       BlockPos postProcessPos = blockState.getPostProcessPos((BlockGetter)this, pos);
/* 334 */       if (postProcessPos != null)
/* 335 */         markPosForPostProcessing(postProcessPos); 
/*     */     } 
/* 339 */     return true;
/*     */   }
/*     */   
/*     */   private void markPosForPostProcessing(BlockPos blockPos) {
/* 343 */     getChunk(blockPos).markPosForPostProcessing(blockPos);
/*     */   }
/*     */   
/*     */   public boolean addFreshEntity(Entity entity) {
/* 351 */     int xc = SectionPos.blockToSectionCoord(entity.getBlockX());
/* 352 */     int zc = SectionPos.blockToSectionCoord(entity.getBlockZ());
/* 354 */     getChunk(xc, zc).addEntity(entity);
/* 355 */     return true;
/*     */   }
/*     */   
/*     */   public boolean removeBlock(BlockPos pos, boolean movedByPiston) {
/* 360 */     return setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
/*     */   }
/*     */   
/*     */   public WorldBorder getWorldBorder() {
/* 365 */     return this.level.getWorldBorder();
/*     */   }
/*     */   
/*     */   public boolean isClientSide() {
/* 370 */     return false;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public ServerLevel getLevel() {
/* 376 */     return this.level;
/*     */   }
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 381 */     return this.level.registryAccess();
/*     */   }
/*     */   
/*     */   public FeatureFlagSet enabledFeatures() {
/* 386 */     return this.level.enabledFeatures();
/*     */   }
/*     */   
/*     */   public LevelData getLevelData() {
/* 391 */     return this.levelData;
/*     */   }
/*     */   
/*     */   public DifficultyInstance getCurrentDifficultyAt(BlockPos pos) {
/* 396 */     if (!hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ())))
/* 397 */       throw new RuntimeException("We are asking a region for a chunk out of bound"); 
/* 400 */     return new DifficultyInstance(this.level.getDifficulty(), this.level.getOverworldClockTime(), 0L, this.level.getMoonBrightness(pos));
/*     */   }
/*     */   
/*     */   public MinecraftServer getServer() {
/* 405 */     return this.level.getServer();
/*     */   }
/*     */   
/*     */   public ChunkSource getChunkSource() {
/* 410 */     return this.level.getChunkSource();
/*     */   }
/*     */   
/*     */   public long getSeed() {
/* 415 */     return this.seed;
/*     */   }
/*     */   
/*     */   public LevelTickAccess<Block> getBlockTicks() {
/* 420 */     return (LevelTickAccess<Block>)this.blockTicks;
/*     */   }
/*     */   
/*     */   public LevelTickAccess<Fluid> getFluidTicks() {
/* 425 */     return (LevelTickAccess<Fluid>)this.fluidTicks;
/*     */   }
/*     */   
/*     */   public int getSeaLevel() {
/* 430 */     return this.level.getSeaLevel();
/*     */   }
/*     */   
/*     */   public RandomSource getRandom() {
/* 435 */     return this.random;
/*     */   }
/*     */   
/*     */   public int getHeight(Heightmap.Types type, int x, int z) {
/* 440 */     int chunkX = SectionPos.blockToSectionCoord(x);
/* 441 */     int chunkZ = SectionPos.blockToSectionCoord(z);
/* 443 */     warnIfReadOutsideWriteZone(chunkX, chunkZ);
/* 444 */     return getChunk(chunkX, chunkZ).getHeight(type, x & 0xF, z & 0xF) + 1;
/*     */   }
/*     */   
/*     */   public void playSound(Entity except, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {}
/*     */   
/*     */   public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {}
/*     */   
/*     */   public void levelEvent(Entity source, int type, BlockPos pos, int data) {}
/*     */   
/*     */   public void gameEvent(Holder<GameEvent> gameEvent, Vec3 position, GameEvent.Context context) {}
/*     */   
/*     */   public DimensionType dimensionType() {
/* 465 */     return this.dimensionType;
/*     */   }
/*     */   
/*     */   public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate) {
/* 470 */     return predicate.test(getBlockState(pos));
/*     */   }
/*     */   
/*     */   public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate) {
/* 475 */     return predicate.test(getFluidState(pos));
/*     */   }
/*     */   
/*     */   public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/* 480 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public List<Entity> getEntities(Entity except, AABB bb, Predicate<? super Entity> selector) {
/* 485 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public List<Player> players() {
/* 490 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public int getMinY() {
/* 495 */     return this.level.getMinY();
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 500 */     return this.level.getHeight();
/*     */   }
/*     */   
/*     */   public long nextSubTickCount() {
/* 505 */     return this.subTickCount.getAndIncrement();
/*     */   }
/*     */   
/*     */   public EnvironmentAttributeReader environmentAttributes() {
/* 510 */     return EnvironmentAttributeReader.EMPTY;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\WorldGenRegion.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */