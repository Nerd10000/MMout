/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.particles.ExplosionParticleInfo;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.AbortableIterationConsumer;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.TickRateManager;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.clock.ClockManager;
/*     */ import net.minecraft.world.clock.WorldClock;
/*     */ import net.minecraft.world.clock.WorldClocks;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageSources;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*     */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.alchemy.PotionBrewing;
/*     */ import net.minecraft.world.item.component.FireworkExplosion;
/*     */ import net.minecraft.world.item.crafting.RecipeAccess;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.FuelValues;
/*     */ import net.minecraft.world.level.block.entity.TickingBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.border.WorldBorder;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.PalettedContainerFactory;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.entity.LevelEntityGetter;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.level.saveddata.maps.MapId;
/*     */ import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.level.storage.WritableLevelData;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public abstract class Level implements LevelAccessor, AutoCloseable {
/*  96 */   public static final Codec<ResourceKey<Level>> RESOURCE_KEY_CODEC = ResourceKey.codec(Registries.DIMENSION);
/*     */   
/*  98 */   public static final ResourceKey<Level> OVERWORLD = ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("overworld"));
/*     */   
/*  99 */   public static final ResourceKey<Level> NETHER = ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("the_nether"));
/*     */   
/* 100 */   public static final ResourceKey<Level> END = ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("the_end"));
/*     */   
/*     */   public static final int MAX_LEVEL_SIZE = 30000000;
/*     */   
/*     */   public static final int ACROSS_THE_WHOLE_WORLD = 60000000;
/*     */   
/*     */   public static final int LONG_PARTICLE_CLIP_RANGE = 512;
/*     */   
/*     */   public static final int SHORT_PARTICLE_CLIP_RANGE = 32;
/*     */   
/*     */   public static final int MAX_BRIGHTNESS = 15;
/*     */   
/*     */   public static final int MAX_ENTITY_SPAWN_Y = 20000000;
/*     */   
/*     */   public static final int MIN_ENTITY_SPAWN_Y = -20000000;
/*     */   
/* 112 */   private static final WeightedList<ExplosionParticleInfo> DEFAULT_EXPLOSION_BLOCK_PARTICLES = WeightedList.builder()
/* 113 */     .add(new ExplosionParticleInfo((ParticleOptions)ParticleTypes.POOF, 0.5F, 1.0F))
/* 114 */     .add(new ExplosionParticleInfo((ParticleOptions)ParticleTypes.SMOKE, 1.0F, 1.0F))
/* 115 */     .build();
/*     */   
/* 117 */   protected final List<TickingBlockEntity> blockEntityTickers = Lists.newArrayList();
/*     */   
/*     */   protected final CollectingNeighborUpdater neighborUpdater;
/*     */   
/* 119 */   private final List<TickingBlockEntity> pendingBlockEntityTickers = Lists.newArrayList();
/*     */   
/*     */   private boolean tickingBlockEntities;
/*     */   
/*     */   private final Thread thread;
/*     */   
/*     */   private final boolean isDebug;
/*     */   
/*     */   private int skyDarken;
/*     */   
/* 127 */   protected int randValue = RandomSource.createThreadLocalInstance().nextInt();
/*     */   
/* 128 */   protected final int addend = 1013904223;
/*     */   
/*     */   protected float oRainLevel;
/*     */   
/*     */   protected float rainLevel;
/*     */   
/*     */   protected float oThunderLevel;
/*     */   
/*     */   protected float thunderLevel;
/*     */   
/* 134 */   protected final RandomSource random = RandomSource.create();
/*     */   
/*     */   @Deprecated
/* 137 */   private final RandomSource soundSeedGenerator = RandomSource.createThreadSafe();
/*     */   
/*     */   private final Holder<DimensionType> dimensionTypeRegistration;
/*     */   
/*     */   protected final WritableLevelData levelData;
/*     */   
/*     */   private final boolean isClientSide;
/*     */   
/*     */   private final BiomeManager biomeManager;
/*     */   
/*     */   private final ResourceKey<Level> dimension;
/*     */   
/*     */   private final RegistryAccess registryAccess;
/*     */   
/*     */   private final DamageSources damageSources;
/*     */   
/*     */   private final PalettedContainerFactory palettedContainerFactory;
/*     */   
/*     */   private long subTickCount;
/*     */   
/*     */   protected Level(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
/* 153 */     this.levelData = levelData;
/* 154 */     this.dimensionTypeRegistration = dimensionTypeRegistration;
/* 155 */     this.dimension = dimension;
/* 156 */     this.isClientSide = isClientSide;
/* 157 */     this.thread = Thread.currentThread();
/* 158 */     this.biomeManager = new BiomeManager(this, biomeZoomSeed);
/* 159 */     this.isDebug = isDebug;
/* 160 */     this.neighborUpdater = new CollectingNeighborUpdater(this, maxChainedNeighborUpdates);
/* 161 */     this.registryAccess = registryAccess;
/* 162 */     this.palettedContainerFactory = PalettedContainerFactory.create(registryAccess);
/* 163 */     this.damageSources = new DamageSources(registryAccess);
/*     */   }
/*     */   
/*     */   public int getNextEntityId() {
/* 167 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isClientSide() {
/* 172 */     return this.isClientSide;
/*     */   }
/*     */   
/*     */   public MinecraftServer getServer() {
/* 177 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isInWorldBounds(BlockPos pos) {
/* 181 */     return (isInsideBuildHeight(pos) && isInWorldBoundsHorizontal(pos));
/*     */   }
/*     */   
/*     */   public boolean isInValidBounds(BlockPos pos) {
/* 185 */     return (isInsideBuildHeight(pos) && isInValidBoundsHorizontal(pos));
/*     */   }
/*     */   
/*     */   public static boolean isInSpawnableBounds(BlockPos pos) {
/* 189 */     return (!isOutsideSpawnableHeight(pos.getY()) && isInWorldBoundsHorizontal(pos));
/*     */   }
/*     */   
/*     */   private static boolean isInWorldBoundsHorizontal(BlockPos pos) {
/* 193 */     return (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000);
/*     */   }
/*     */   
/*     */   private static boolean isInValidBoundsHorizontal(BlockPos pos) {
/* 197 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/* 198 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/* 199 */     return ChunkPos.isValid(chunkX, chunkZ);
/*     */   }
/*     */   
/*     */   private static boolean isOutsideSpawnableHeight(int y) {
/* 203 */     return (y < -20000000 || y >= 20000000);
/*     */   }
/*     */   
/*     */   public LevelChunk getChunkAt(BlockPos pos) {
/* 207 */     return getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   public LevelChunk getChunk(int chunkX, int chunkZ) {
/* 212 */     return (LevelChunk)getChunk(chunkX, chunkZ, ChunkStatus.FULL);
/*     */   }
/*     */   
/*     */   public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus status, boolean loadOrGenerate) {
/* 217 */     ChunkAccess chunk = getChunkSource().getChunk(chunkX, chunkZ, status, loadOrGenerate);
/* 218 */     if (chunk == null && loadOrGenerate)
/* 219 */       throw new IllegalStateException("Should always be able to create a chunk!"); 
/* 221 */     return chunk;
/*     */   }
/*     */   
/*     */   public boolean setBlock(BlockPos pos, BlockState blockState, int updateFlags) {
/* 226 */     return setBlock(pos, blockState, updateFlags, 512);
/*     */   }
/*     */   
/*     */   public boolean setBlock(BlockPos pos, BlockState blockState, int updateFlags, int updateLimit) {
/* 231 */     if (!isInValidBounds(pos))
/* 232 */       return false; 
/* 235 */     if (!isClientSide() && isDebug())
/* 236 */       return false; 
/* 239 */     LevelChunk chunk = getChunkAt(pos);
/* 240 */     Block block = blockState.getBlock();
/* 241 */     BlockState oldState = chunk.setBlockState(pos, blockState, updateFlags);
/* 244 */     if (oldState != null) {
/* 246 */       BlockState newState = getBlockState(pos);
/* 249 */       if (newState == blockState) {
/* 250 */         if (oldState != newState)
/* 251 */           setBlocksDirty(pos, oldState, newState); 
/* 254 */         if ((updateFlags & 0x2) != 0 && (!isClientSide() || (updateFlags & 0x4) == 0) && (isClientSide() || (chunk.getFullStatus() != null && chunk.getFullStatus().isOrAfter(FullChunkStatus.BLOCK_TICKING))))
/* 255 */           sendBlockUpdated(pos, oldState, blockState, updateFlags); 
/* 258 */         if ((updateFlags & 0x1) != 0) {
/* 259 */           updateNeighborsAt(pos, oldState.getBlock());
/* 260 */           if (!isClientSide() && blockState.hasAnalogOutputSignal())
/* 261 */             updateNeighbourForOutputSignal(pos, block); 
/*     */         } 
/* 266 */         if ((updateFlags & 0x10) == 0 && updateLimit > 0) {
/* 267 */           int neighbourUpdateFlags = updateFlags & 0xFFFFFFDE;
/* 268 */           oldState.updateIndirectNeighbourShapes(this, pos, neighbourUpdateFlags, updateLimit - 1);
/* 269 */           blockState.updateNeighbourShapes(this, pos, neighbourUpdateFlags, updateLimit - 1);
/* 270 */           blockState.updateIndirectNeighbourShapes(this, pos, neighbourUpdateFlags, updateLimit - 1);
/*     */         } 
/* 273 */         updatePOIOnBlockStateChange(pos, oldState, newState);
/*     */       } 
/* 276 */       return true;
/*     */     } 
/* 279 */     return false;
/*     */   }
/*     */   
/*     */   public void updatePOIOnBlockStateChange(BlockPos pos, BlockState oldState, BlockState newState) {}
/*     */   
/*     */   public boolean removeBlock(BlockPos pos, boolean movedByPiston) {
/* 292 */     FluidState fluidState = getFluidState(pos);
/* 293 */     return setBlock(pos, fluidState.createLegacyBlock(), 0x3 | (movedByPiston ? 64 : 0));
/*     */   }
/*     */   
/*     */   public boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker, int updateLimit) {
/* 304 */     BlockState blockState = getBlockState(pos);
/* 305 */     if (blockState.isAir())
/* 306 */       return false; 
/* 309 */     FluidState fluidState = getFluidState(pos);
/* 310 */     if (!(blockState.getBlock() instanceof net.minecraft.world.level.block.BaseFireBlock))
/* 311 */       levelEvent(2001, pos, Block.getId(blockState)); 
/* 313 */     if (dropResources) {
/* 314 */       BlockEntity blockEntity = blockState.hasBlockEntity() ? getBlockEntity(pos) : null;
/* 315 */       Block.dropResources(blockState, this, pos, blockEntity, breaker, ItemStack.EMPTY);
/*     */     } 
/* 318 */     boolean destroyed = setBlock(pos, fluidState.createLegacyBlock(), 3, updateLimit);
/* 320 */     if (destroyed)
/* 321 */       gameEvent((Holder<GameEvent>)GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(breaker, blockState)); 
/* 324 */     return destroyed;
/*     */   }
/*     */   
/*     */   public void addDestroyBlockEffect(BlockPos pos, BlockState blockState) {}
/*     */   
/*     */   public boolean setBlockAndUpdate(BlockPos pos, BlockState blockState) {
/* 331 */     return setBlock(pos, blockState, 3);
/*     */   }
/*     */   
/*     */   public void setBlocksDirty(BlockPos pos, BlockState oldState, BlockState newState) {}
/*     */   
/*     */   public void updateNeighborsAt(BlockPos pos, Block sourceBlock, Orientation orientation) {}
/*     */   
/*     */   public void updateNeighborsAtExceptFromFacing(BlockPos pos, Block blockObject, Direction skipDirection, Orientation orientation) {}
/*     */   
/*     */   public void neighborChanged(BlockPos pos, Block changedBlock, Orientation orientation) {}
/*     */   
/*     */   public void neighborChanged(BlockState state, BlockPos pos, Block changedBlock, Orientation orientation, boolean movedByPiston) {}
/*     */   
/*     */   public void neighborShapeChanged(Direction direction, BlockPos pos, BlockPos neighborPos, BlockState neighborState, int updateFlags, int updateLimit) {
/* 353 */     this.neighborUpdater.shapeUpdate(direction, neighborState, pos, neighborPos, updateFlags, updateLimit);
/*     */   }
/*     */   
/*     */   public int getHeight(Heightmap.Types type, int x, int z) {
/*     */     int y;
/* 359 */     if (x < -30000000 || z < -30000000 || x >= 30000000 || z >= 30000000) {
/* 360 */       y = getSeaLevel() + 1;
/* 361 */     } else if (hasChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z))) {
/* 362 */       y = getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z)).getHeight(type, x & 0xF, z & 0xF) + 1;
/*     */     } else {
/* 364 */       y = getMinY();
/*     */     } 
/* 366 */     return y;
/*     */   }
/*     */   
/*     */   public LevelLightEngine getLightEngine() {
/* 371 */     return getChunkSource().getLightEngine();
/*     */   }
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/* 376 */     if (!isInValidBounds(pos))
/* 377 */       return Blocks.VOID_AIR.defaultBlockState(); 
/* 379 */     LevelChunk chunk = getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/* 380 */     return chunk.getBlockState(pos);
/*     */   }
/*     */   
/*     */   public FluidState getFluidState(BlockPos pos) {
/* 385 */     if (!isInValidBounds(pos))
/* 386 */       return Fluids.EMPTY.defaultFluidState(); 
/* 388 */     LevelChunk chunk = getChunkAt(pos);
/* 389 */     return chunk.getFluidState(pos);
/*     */   }
/*     */   
/*     */   public boolean isBrightOutside() {
/* 393 */     return (!dimensionType().hasFixedTime() && this.skyDarken < 4);
/*     */   }
/*     */   
/*     */   public boolean isDarkOutside() {
/* 397 */     return (!dimensionType().hasFixedTime() && !isBrightOutside());
/*     */   }
/*     */   
/*     */   public void playSound(Entity except, BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch) {
/* 403 */     playSound(except, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, sound, source, volume, pitch);
/*     */   }
/*     */   
/*     */   public void playSeededSound(Entity except, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, long seed) {
/* 410 */     playSeededSound(except, x, y, z, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), source, volume, pitch, seed);
/*     */   }
/*     */   
/*     */   public void playSound(Entity except, double x, double y, double z, SoundEvent sound, SoundSource source) {
/* 418 */     playSound(except, x, y, z, sound, source, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void playSound(Entity except, double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch) {
/* 423 */     playSeededSound(except, x, y, z, sound, source, volume, pitch, this.soundSeedGenerator.nextLong());
/*     */   }
/*     */   
/*     */   public void playSound(Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch) {
/* 428 */     playSeededSound(except, x, y, z, sound, source, volume, pitch, this.soundSeedGenerator.nextLong());
/*     */   }
/*     */   
/*     */   public void playSound(Entity except, Entity sourceEntity, SoundEvent sound, SoundSource source, float volume, float pitch) {
/* 433 */     playSeededSound(except, sourceEntity, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound), source, volume, pitch, this.soundSeedGenerator.nextLong());
/*     */   }
/*     */   
/*     */   public void playLocalSound(BlockPos pos, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {
/* 437 */     playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, sound, source, volume, pitch, distanceDelay);
/*     */   }
/*     */   
/*     */   public void playLocalSound(Entity sourceEntity, SoundEvent sound, SoundSource source, float volume, float pitch) {}
/*     */   
/*     */   public void playLocalSound(double x, double y, double z, SoundEvent sound, SoundSource source, float volume, float pitch, boolean distanceDelay) {}
/*     */   
/*     */   public void playPlayerSound(SoundEvent sound, SoundSource source, float volume, float pitch) {}
/*     */   
/*     */   public void addParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {}
/*     */   
/*     */   public void addParticle(ParticleOptions particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, double xd, double yd, double zd) {}
/*     */   
/*     */   public void addAlwaysVisibleParticle(ParticleOptions particle, double x, double y, double z, double xd, double yd, double zd) {}
/*     */   
/*     */   public void addAlwaysVisibleParticle(ParticleOptions particle, boolean overrideLimiter, double x, double y, double z, double xd, double yd, double zd) {}
/*     */   
/*     */   public void addBlockEntityTicker(TickingBlockEntity ticker) {
/* 465 */     (this.tickingBlockEntities ? this.pendingBlockEntityTickers : this.blockEntityTickers).add(ticker);
/*     */   }
/*     */   
/*     */   public void tickBlockEntities() {
/* 469 */     this.tickingBlockEntities = true;
/* 471 */     if (!this.pendingBlockEntityTickers.isEmpty()) {
/* 472 */       this.blockEntityTickers.addAll(this.pendingBlockEntityTickers);
/* 473 */       this.pendingBlockEntityTickers.clear();
/*     */     } 
/* 476 */     Iterator<TickingBlockEntity> iterator = this.blockEntityTickers.iterator();
/* 477 */     boolean tickBlockEntities = tickRateManager().runsNormally();
/* 478 */     while (iterator.hasNext()) {
/* 479 */       TickingBlockEntity ticker = iterator.next();
/* 480 */       if (ticker.isRemoved()) {
/* 481 */         iterator.remove();
/*     */         continue;
/*     */       } 
/* 483 */       if (tickBlockEntities && shouldTickBlocksAt(ticker.getPos()))
/* 484 */         ticker.tick(); 
/*     */     } 
/* 488 */     this.tickingBlockEntities = false;
/*     */   }
/*     */   
/*     */   public <T extends Entity> void guardEntityTick(Consumer<T> tick, T entity) {
/*     */     try {
/* 493 */       tick.accept(entity);
/* 494 */     } catch (Throwable t) {
/* 495 */       CrashReport report = CrashReport.forThrowable(t, "Ticking entity");
/* 496 */       CrashReportCategory category = report.addCategory("Entity being ticked");
/* 498 */       entity.fillCrashReportCategory(category);
/* 500 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldTickDeath(Entity entity) {
/* 505 */     return true;
/*     */   }
/*     */   
/*     */   public boolean shouldTickBlocksAt(long chunkPos) {
/* 509 */     return true;
/*     */   }
/*     */   
/*     */   public boolean shouldTickBlocksAt(BlockPos pos) {
/* 513 */     return shouldTickBlocksAt(ChunkPos.pack(pos));
/*     */   }
/*     */   
/*     */   public enum ExplosionInteraction implements StringRepresentable {
/* 517 */     NONE("none"),
/* 518 */     BLOCK("block"),
/* 519 */     MOB("mob"),
/* 520 */     TNT("tnt"),
/* 521 */     TRIGGER("trigger");
/*     */     
/* 523 */     public static final Codec<ExplosionInteraction> CODEC = (Codec<ExplosionInteraction>)StringRepresentable.fromEnum(ExplosionInteraction::values);
/*     */     
/*     */     private final String id;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     ExplosionInteraction(String id) {
/* 528 */       this.id = id;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 533 */       return this.id;
/*     */     }
/*     */   }
/*     */   
/*     */   public void explode(Entity source, double x, double y, double z, float r, ExplosionInteraction blockInteraction) {
/* 538 */     explode(source, Explosion.getDefaultDamageSource(this, source), (ExplosionDamageCalculator)null, x, y, z, r, false, blockInteraction, (ParticleOptions)ParticleTypes.EXPLOSION, (ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, DEFAULT_EXPLOSION_BLOCK_PARTICLES, (Holder<SoundEvent>)SoundEvents.GENERIC_EXPLODE);
/*     */   }
/*     */   
/*     */   public void explode(Entity source, double x, double y, double z, float r, boolean fire, ExplosionInteraction blockInteraction) {
/* 542 */     explode(source, Explosion.getDefaultDamageSource(this, source), (ExplosionDamageCalculator)null, x, y, z, r, fire, blockInteraction, (ParticleOptions)ParticleTypes.EXPLOSION, (ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, DEFAULT_EXPLOSION_BLOCK_PARTICLES, (Holder<SoundEvent>)SoundEvents.GENERIC_EXPLODE);
/*     */   }
/*     */   
/*     */   public void explode(Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, Vec3 boomPos, float r, boolean fire, ExplosionInteraction blockInteraction) {
/* 546 */     explode(source, damageSource, damageCalculator, boomPos.x(), boomPos.y(), boomPos.z(), r, fire, blockInteraction, (ParticleOptions)ParticleTypes.EXPLOSION, (ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, DEFAULT_EXPLOSION_BLOCK_PARTICLES, (Holder<SoundEvent>)SoundEvents.GENERIC_EXPLODE);
/*     */   }
/*     */   
/*     */   public void explode(Entity source, DamageSource damageSource, ExplosionDamageCalculator damageCalculator, double x, double y, double z, float r, boolean fire, ExplosionInteraction interactionType) {
/* 550 */     explode(source, damageSource, damageCalculator, x, y, z, r, fire, interactionType, (ParticleOptions)ParticleTypes.EXPLOSION, (ParticleOptions)ParticleTypes.EXPLOSION_EMITTER, DEFAULT_EXPLOSION_BLOCK_PARTICLES, (Holder<SoundEvent>)SoundEvents.GENERIC_EXPLODE);
/*     */   }
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 559 */     if (!isInValidBounds(pos))
/* 560 */       return null; 
/* 564 */     if (!isClientSide() && Thread.currentThread() != this.thread)
/* 565 */       return null; 
/* 568 */     return getChunkAt(pos).getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE);
/*     */   }
/*     */   
/*     */   public void setBlockEntity(BlockEntity blockEntity) {
/* 572 */     BlockPos pos = blockEntity.getBlockPos();
/* 573 */     if (!isInValidBounds(pos))
/*     */       return; 
/* 577 */     getChunkAt(pos).addAndRegisterBlockEntity(blockEntity);
/*     */   }
/*     */   
/*     */   public void removeBlockEntity(BlockPos pos) {
/* 581 */     if (!isInValidBounds(pos))
/*     */       return; 
/* 585 */     getChunkAt(pos).removeBlockEntity(pos);
/*     */   }
/*     */   
/*     */   public boolean isLoaded(BlockPos pos) {
/* 589 */     if (!isInValidBounds(pos))
/* 590 */       return false; 
/* 592 */     return getChunkSource().hasChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   public boolean loadedAndEntityCanStandOnFace(BlockPos pos, Entity entity, Direction faceDirection) {
/* 599 */     if (!isInValidBounds(pos))
/* 600 */       return false; 
/* 603 */     ChunkAccess chunk = getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()), ChunkStatus.FULL, false);
/* 604 */     if (chunk == null)
/* 605 */       return false; 
/* 608 */     return chunk.getBlockState(pos).entityCanStandOnFace(this, pos, entity, faceDirection);
/*     */   }
/*     */   
/*     */   public boolean loadedAndEntityCanStandOn(BlockPos pos, Entity entity) {
/* 612 */     return loadedAndEntityCanStandOnFace(pos, entity, Direction.UP);
/*     */   }
/*     */   
/*     */   public void updateSkyBrightness() {
/* 616 */     this.skyDarken = (int)(15.0F - ((Float)environmentAttributes().getDimensionValue(EnvironmentAttributes.SKY_LIGHT_LEVEL)).floatValue());
/*     */   }
/*     */   
/*     */   public void setSpawnSettings(boolean spawnEnemies) {
/* 620 */     getChunkSource().setSpawnSettings(spawnEnemies);
/*     */   }
/*     */   
/*     */   public LevelData.RespawnData getWorldBorderAdjustedRespawnData(LevelData.RespawnData respawnData) {
/* 628 */     WorldBorder worldBorder = getWorldBorder();
/* 629 */     if (!worldBorder.isWithinBounds(respawnData.pos())) {
/* 630 */       BlockPos newPos = getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(worldBorder.getCenterX(), 0.0D, worldBorder.getCenterZ()));
/* 631 */       return LevelData.RespawnData.of(respawnData.dimension(), newPos, respawnData.yaw(), respawnData.pitch());
/*     */     } 
/* 633 */     return respawnData;
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 639 */     getChunkSource().close();
/*     */   }
/*     */   
/*     */   public BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
/* 644 */     return (BlockGetter)getChunk(chunkX, chunkZ, ChunkStatus.FULL, false);
/*     */   }
/*     */   
/*     */   public List<Entity> getEntities(Entity except, AABB bb, Predicate<? super Entity> selector) {
/* 649 */     Profiler.get().incrementCounter("getEntities");
/* 651 */     List<Entity> output = Lists.newArrayList();
/* 652 */     getEntities().get(bb, entity -> {
/*     */           if (entity != except && selector.test(entity))
/*     */             output.add(entity); 
/*     */         });
/* 658 */     for (EnderDragonPart dragonPart : dragonParts()) {
/* 659 */       if (dragonPart != except && dragonPart.parentMob != except && 
/* 660 */         selector.test(dragonPart) && 
/* 661 */         bb.intersects(dragonPart.getBoundingBox()))
/* 662 */         output.add(dragonPart); 
/*     */     } 
/* 668 */     return output;
/*     */   }
/*     */   
/*     */   public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/* 673 */     List<T> output = Lists.newArrayList();
/* 674 */     getEntities(type, bb, selector, output);
/* 675 */     return output;
/*     */   }
/*     */   
/*     */   public <T extends Entity> void getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector, List<? super T> output) {
/* 679 */     getEntities(type, bb, selector, output, 2147483647);
/*     */   }
/*     */   
/*     */   public <T extends Entity> void getEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector, List<? super T> output, int maxResults) {
/* 683 */     Profiler.get().incrementCounter("getEntities");
/* 685 */     getEntities().get(type, bb, e -> {
/*     */           if (selector.test(e)) {
/*     */             output.add(e);
/*     */             if (output.size() >= maxResults)
/*     */               return AbortableIterationConsumer.Continuation.ABORT; 
/*     */           } 
/*     */           if (e instanceof EnderDragon) {
/*     */             EnderDragon enderDragon = (EnderDragon)e;
/*     */             for (EnderDragonPart subEntity : enderDragon.getSubEntities()) {
/*     */               Entity entity = (Entity)type.tryCast(subEntity);
/*     */               if (entity != null && selector.test(entity)) {
/*     */                 output.add(entity);
/*     */                 if (output.size() >= maxResults)
/*     */                   return AbortableIterationConsumer.Continuation.ABORT; 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           return AbortableIterationConsumer.Continuation.CONTINUE;
/*     */         });
/*     */   }
/*     */   
/*     */   public <T extends Entity> boolean hasEntities(EntityTypeTest<Entity, T> type, AABB bb, Predicate<? super T> selector) {
/* 709 */     Profiler.get().incrementCounter("hasEntities");
/* 711 */     MutableBoolean hasEntities = new MutableBoolean();
/* 712 */     getEntities().get(type, bb, e -> {
/*     */           if (selector.test(e)) {
/*     */             hasEntities.setTrue();
/*     */             return AbortableIterationConsumer.Continuation.ABORT;
/*     */           } 
/*     */           if (e instanceof EnderDragon) {
/*     */             EnderDragon enderDragon = (EnderDragon)e;
/*     */             for (EnderDragonPart subEntity : enderDragon.getSubEntities()) {
/*     */               Entity entity = (Entity)type.tryCast(subEntity);
/*     */               if (entity != null && selector.test(entity)) {
/*     */                 hasEntities.setTrue();
/*     */                 return AbortableIterationConsumer.Continuation.ABORT;
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           return AbortableIterationConsumer.Continuation.CONTINUE;
/*     */         });
/* 729 */     return hasEntities.isTrue();
/*     */   }
/*     */   
/*     */   public List<Entity> getPushableEntities(Entity pusher, AABB boundingBox) {
/* 733 */     return getEntities(pusher, boundingBox, EntitySelector.pushableBy(pusher));
/*     */   }
/*     */   
/*     */   public Entity getEntity(UUID uuid) {
/* 739 */     return (Entity)getEntities().get(uuid);
/*     */   }
/*     */   
/*     */   public Entity getEntityInAnyDimension(UUID uuid) {
/* 743 */     return getEntity(uuid);
/*     */   }
/*     */   
/*     */   public Player getPlayerInAnyDimension(UUID uuid) {
/* 747 */     return getPlayerByUUID(uuid);
/*     */   }
/*     */   
/*     */   public void blockEntityChanged(BlockPos pos) {
/* 753 */     if (hasChunkAt(pos))
/* 754 */       getChunkAt(pos).markUnsaved(); 
/*     */   }
/*     */   
/*     */   public void onBlockEntityAdded(BlockEntity blockEntity) {}
/*     */   
/*     */   public long getOverworldClockTime() {
/* 762 */     return getClockTimeTicks(registryAccess().get(WorldClocks.OVERWORLD));
/*     */   }
/*     */   
/*     */   public long getDefaultClockTime() {
/* 766 */     return getClockTimeTicks(dimensionType().defaultClock());
/*     */   }
/*     */   
/*     */   private long getClockTimeTicks(Optional<? extends Holder<WorldClock>> clock) {
/* 770 */     return ((Long)clock
/* 771 */       .<Long>map(holder -> Long.valueOf(clockManager().getTotalTicks(holder)))
/* 772 */       .orElse(Long.valueOf(0L))).longValue();
/*     */   }
/*     */   
/*     */   public boolean mayInteract(Entity entity, BlockPos pos) {
/* 776 */     return true;
/*     */   }
/*     */   
/*     */   public void broadcastEntityEvent(Entity entity, byte event) {}
/*     */   
/*     */   public void broadcastDamageEvent(Entity entity, DamageSource source) {}
/*     */   
/*     */   public void blockEvent(BlockPos pos, Block block, int b0, int b1) {
/* 786 */     getBlockState(pos).triggerEvent(this, pos, b0, b1);
/*     */   }
/*     */   
/*     */   public LevelData getLevelData() {
/* 791 */     return (LevelData)this.levelData;
/*     */   }
/*     */   
/*     */   public float getThunderLevel(float a) {
/* 797 */     return Mth.lerp(a, this.oThunderLevel, this.thunderLevel) * getRainLevel(a);
/*     */   }
/*     */   
/*     */   public void setThunderLevel(float thunderLevel) {
/* 801 */     float clampedThunderLevel = Mth.clamp(thunderLevel, 0.0F, 1.0F);
/* 802 */     this.oThunderLevel = clampedThunderLevel;
/* 803 */     this.thunderLevel = clampedThunderLevel;
/*     */   }
/*     */   
/*     */   public float getRainLevel(float a) {
/* 807 */     return Mth.lerp(a, this.oRainLevel, this.rainLevel);
/*     */   }
/*     */   
/*     */   public void setRainLevel(float rainLevel) {
/* 811 */     float clampedRainLevel = Mth.clamp(rainLevel, 0.0F, 1.0F);
/* 812 */     this.oRainLevel = clampedRainLevel;
/* 813 */     this.rainLevel = clampedRainLevel;
/*     */   }
/*     */   
/*     */   public boolean canHaveWeather() {
/* 818 */     return (dimensionType().hasSkyLight() && !dimensionType().hasCeiling() && dimension() != END);
/*     */   }
/*     */   
/*     */   public boolean isThundering() {
/* 822 */     return (canHaveWeather() && getThunderLevel(1.0F) > 0.9D);
/*     */   }
/*     */   
/*     */   public boolean isRaining() {
/* 826 */     return (canHaveWeather() && getRainLevel(1.0F) > 0.2D);
/*     */   }
/*     */   
/*     */   public boolean isRainingAt(BlockPos pos) {
/* 830 */     return (precipitationAt(pos) == Biome.Precipitation.RAIN);
/*     */   }
/*     */   
/*     */   public Biome.Precipitation precipitationAt(BlockPos pos) {
/* 834 */     if (!isRaining())
/* 835 */       return Biome.Precipitation.NONE; 
/* 837 */     if (!canSeeSky(pos))
/* 838 */       return Biome.Precipitation.NONE; 
/* 840 */     if (getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY())
/* 841 */       return Biome.Precipitation.NONE; 
/* 844 */     Biome biome = (Biome)getBiome(pos).value();
/* 846 */     return biome.getPrecipitationAt(pos, getSeaLevel());
/*     */   }
/*     */   
/*     */   public void globalLevelEvent(int type, BlockPos pos, int data) {}
/*     */   
/*     */   public CrashReportCategory fillReportDetails(CrashReport report) {
/* 855 */     CrashReportCategory category = report.addCategory("Affected level", 1);
/* 857 */     category.setDetail("All players", () -> {
/*     */           List<? extends Player> players = players();
/*     */           return "" + players.size() + " total; " + players.size();
/*     */         });
/* 861 */     Objects.requireNonNull(getChunkSource());
/* 861 */     category.setDetail("Chunk stats", getChunkSource()::gatherStats);
/* 862 */     category.setDetail("Level dimension", () -> dimension().identifier().toString());
/* 863 */     category.setDetail("Level time", () -> String.format(Locale.ROOT, "%d game time, %d day time", new Object[] { Long.valueOf(getGameTime()), Long.valueOf(getOverworldClockTime()) }));
/*     */     try {
/* 866 */       this.levelData.fillCrashReportCategory(category, this);
/* 867 */     } catch (Throwable t) {
/* 868 */       category.setDetailError("Level Data Unobtainable", t);
/*     */     } 
/* 871 */     return category;
/*     */   }
/*     */   
/*     */   public void createFireworks(double x, double y, double z, double xd, double yd, double zd, List<FireworkExplosion> explosions) {}
/*     */   
/*     */   public void updateNeighbourForOutputSignal(BlockPos pos, Block changedBlock) {
/* 882 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 883 */       BlockPos relativePos = pos.relative(direction);
/* 885 */       if (hasChunkAt(relativePos)) {
/* 886 */         BlockState state = getBlockState(relativePos);
/* 887 */         if (state.is(Blocks.COMPARATOR)) {
/* 888 */           neighborChanged(state, relativePos, changedBlock, (Orientation)null, false);
/*     */           continue;
/*     */         } 
/* 889 */         if (state.isRedstoneConductor(this, relativePos)) {
/* 890 */           relativePos = relativePos.relative(direction);
/* 891 */           state = getBlockState(relativePos);
/* 893 */           if (state.is(Blocks.COMPARATOR))
/* 894 */             neighborChanged(state, relativePos, changedBlock, (Orientation)null, false); 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getSkyDarken() {
/* 903 */     return this.skyDarken;
/*     */   }
/*     */   
/*     */   public void setSkyFlashTime(int skyFlashTime) {}
/*     */   
/*     */   public void sendPacketToServer(Packet<?> packet) {
/* 910 */     throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
/*     */   }
/*     */   
/*     */   public DimensionType dimensionType() {
/* 915 */     return (DimensionType)this.dimensionTypeRegistration.value();
/*     */   }
/*     */   
/*     */   public Holder<DimensionType> dimensionTypeRegistration() {
/* 919 */     return this.dimensionTypeRegistration;
/*     */   }
/*     */   
/*     */   public ResourceKey<Level> dimension() {
/* 923 */     return this.dimension;
/*     */   }
/*     */   
/*     */   public RandomSource getRandom() {
/* 928 */     return this.random;
/*     */   }
/*     */   
/*     */   public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> predicate) {
/* 933 */     return predicate.test(getBlockState(pos));
/*     */   }
/*     */   
/*     */   public boolean isFluidAtPosition(BlockPos pos, Predicate<FluidState> predicate) {
/* 938 */     return predicate.test(getFluidState(pos));
/*     */   }
/*     */   
/*     */   public BlockPos getBlockRandomPos(int xo, int yo, int zo, int yMask) {
/* 944 */     this.randValue = this.randValue * 3 + 1013904223;
/* 945 */     int val = this.randValue >> 2;
/* 947 */     return new BlockPos(xo + (val & 0xF), yo + (val >> 16 & yMask), zo + (val >> 8 & 0xF));
/*     */   }
/*     */   
/*     */   public boolean noSave() {
/* 951 */     return false;
/*     */   }
/*     */   
/*     */   public BiomeManager getBiomeManager() {
/* 956 */     return this.biomeManager;
/*     */   }
/*     */   
/*     */   public final boolean isDebug() {
/* 960 */     return this.isDebug;
/*     */   }
/*     */   
/*     */   public long nextSubTickCount() {
/* 967 */     return this.subTickCount++;
/*     */   }
/*     */   
/*     */   public RegistryAccess registryAccess() {
/* 972 */     return this.registryAccess;
/*     */   }
/*     */   
/*     */   public DamageSources damageSources() {
/* 976 */     return this.damageSources;
/*     */   }
/*     */   
/*     */   public int getClientLeafTintColor(BlockPos pos) {
/* 991 */     return 0;
/*     */   }
/*     */   
/*     */   public PalettedContainerFactory palettedContainerFactory() {
/* 995 */     return this.palettedContainerFactory;
/*     */   }
/*     */   
/*     */   public abstract void sendBlockUpdated(BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2, int paramInt);
/*     */   
/*     */   public abstract void playSeededSound(Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, Holder<SoundEvent> paramHolder, SoundSource paramSoundSource, float paramFloat1, float paramFloat2, long paramLong);
/*     */   
/*     */   public abstract void playSeededSound(Entity paramEntity1, Entity paramEntity2, Holder<SoundEvent> paramHolder, SoundSource paramSoundSource, float paramFloat1, float paramFloat2, long paramLong);
/*     */   
/*     */   public abstract void explode(Entity paramEntity, DamageSource paramDamageSource, ExplosionDamageCalculator paramExplosionDamageCalculator, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat, boolean paramBoolean, ExplosionInteraction paramExplosionInteraction, ParticleOptions paramParticleOptions1, ParticleOptions paramParticleOptions2, WeightedList<ExplosionParticleInfo> paramWeightedList, Holder<SoundEvent> paramHolder);
/*     */   
/*     */   public abstract String gatherChunkSourceStats();
/*     */   
/*     */   public abstract void setRespawnData(LevelData.RespawnData paramRespawnData);
/*     */   
/*     */   public abstract LevelData.RespawnData getRespawnData();
/*     */   
/*     */   public abstract Entity getEntity(int paramInt);
/*     */   
/*     */   public abstract Collection<EnderDragonPart> dragonParts();
/*     */   
/*     */   public abstract TickRateManager tickRateManager();
/*     */   
/*     */   public abstract MapItemSavedData getMapData(MapId paramMapId);
/*     */   
/*     */   public abstract void destroyBlockProgress(int paramInt1, BlockPos paramBlockPos, int paramInt2);
/*     */   
/*     */   public abstract Scoreboard getScoreboard();
/*     */   
/*     */   public abstract RecipeAccess recipeAccess();
/*     */   
/*     */   protected abstract LevelEntityGetter<Entity> getEntities();
/*     */   
/*     */   public abstract ClockManager clockManager();
/*     */   
/*     */   public abstract EnvironmentAttributeSystem environmentAttributes();
/*     */   
/*     */   public abstract PotionBrewing potionBrewing();
/*     */   
/*     */   public abstract FuelValues fuelValues();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\Level.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */