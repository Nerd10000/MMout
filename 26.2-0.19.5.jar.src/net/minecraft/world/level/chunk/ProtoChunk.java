/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.shorts.ShortList;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.BlendingData;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.lighting.LightEngine;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.ticks.LevelChunkTicks;
/*     */ import net.minecraft.world.ticks.ProtoChunkTicks;
/*     */ import net.minecraft.world.ticks.TickContainerAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ProtoChunk extends ChunkAccess {
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private volatile LevelLightEngine lightEngine;
/*     */   
/*  49 */   private volatile ChunkStatus status = ChunkStatus.EMPTY;
/*     */   
/*  50 */   private final List<CompoundTag> entities = Lists.newArrayList();
/*     */   
/*     */   private CarvingMask carvingMask;
/*     */   
/*     */   private BelowZeroRetrogen belowZeroRetrogen;
/*     */   
/*     */   private final ProtoChunkTicks<Block> blockTicks;
/*     */   
/*     */   private final ProtoChunkTicks<Fluid> fluidTicks;
/*     */   
/*     */   public ProtoChunk(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, PalettedContainerFactory containerFactory, BlendingData blendingData) {
/*  65 */     this(chunkPos, upgradeData, (LevelChunkSection[])null, new ProtoChunkTicks(), new ProtoChunkTicks(), levelHeightAccessor, containerFactory, blendingData);
/*     */   }
/*     */   
/*     */   public ProtoChunk(ChunkPos chunkPos, UpgradeData upgradeData, LevelChunkSection[] sections, ProtoChunkTicks<Block> blockTicks, ProtoChunkTicks<Fluid> fluidTicks, LevelHeightAccessor levelHeightAccessor, PalettedContainerFactory containerFactory, BlendingData blendingData) {
/*  87 */     super(chunkPos, upgradeData, levelHeightAccessor, containerFactory, 0L, sections, blendingData);
/*  88 */     this.blockTicks = blockTicks;
/*  89 */     this.fluidTicks = fluidTicks;
/*     */   }
/*     */   
/*     */   public TickContainerAccess<Block> getBlockTicks() {
/*  94 */     return (TickContainerAccess<Block>)this.blockTicks;
/*     */   }
/*     */   
/*     */   public TickContainerAccess<Fluid> getFluidTicks() {
/*  99 */     return (TickContainerAccess<Fluid>)this.fluidTicks;
/*     */   }
/*     */   
/*     */   public ChunkAccess.PackedTicks getTicksForSerialization(long currentTick) {
/* 104 */     return new ChunkAccess.PackedTicks(this.blockTicks.pack(currentTick), this.fluidTicks.pack(currentTick));
/*     */   }
/*     */   
/*     */   public BlockState getBlockState(BlockPos pos) {
/* 109 */     int y = pos.getY();
/* 110 */     if (isOutsideBuildHeight(y))
/* 111 */       return Blocks.VOID_AIR.defaultBlockState(); 
/* 114 */     LevelChunkSection section = getSection(getSectionIndex(y));
/* 115 */     if (section.hasOnlyAir())
/* 116 */       return Blocks.AIR.defaultBlockState(); 
/* 119 */     return section.getBlockState(pos.getX() & 0xF, y & 0xF, pos.getZ() & 0xF);
/*     */   }
/*     */   
/*     */   public FluidState getFluidState(BlockPos pos) {
/* 124 */     int y = pos.getY();
/* 125 */     if (isOutsideBuildHeight(y))
/* 126 */       return Fluids.EMPTY.defaultFluidState(); 
/* 129 */     LevelChunkSection section = getSection(getSectionIndex(y));
/* 130 */     if (section.hasOnlyAir())
/* 131 */       return Fluids.EMPTY.defaultFluidState(); 
/* 134 */     return section.getFluidState(pos.getX() & 0xF, y & 0xF, pos.getZ() & 0xF);
/*     */   }
/*     */   
/*     */   public BlockState setBlockState(BlockPos pos, BlockState state, int flags) {
/* 139 */     int x = pos.getX();
/* 140 */     int y = pos.getY();
/* 141 */     int z = pos.getZ();
/* 143 */     if (isOutsideBuildHeight(y))
/* 144 */       return Blocks.VOID_AIR.defaultBlockState(); 
/* 147 */     int sectionIndex = getSectionIndex(y);
/* 148 */     LevelChunkSection section = getSection(sectionIndex);
/* 149 */     boolean wasEmpty = section.hasOnlyAir();
/* 150 */     if (wasEmpty && state.is(Blocks.AIR))
/* 151 */       return state; 
/* 154 */     int localX = SectionPos.sectionRelative(x);
/* 155 */     int localY = SectionPos.sectionRelative(y);
/* 156 */     int localZ = SectionPos.sectionRelative(z);
/* 157 */     BlockState oldState = section.setBlockState(localX, localY, localZ, state);
/* 159 */     if (this.status.isOrAfter(ChunkStatus.INITIALIZE_LIGHT)) {
/* 160 */       boolean isEmpty = section.hasOnlyAir();
/* 161 */       if (isEmpty != wasEmpty)
/* 162 */         this.lightEngine.updateSectionStatus(pos, isEmpty); 
/* 165 */       if (LightEngine.hasDifferentLightProperties(oldState, state)) {
/* 166 */         this.skyLightSources.update(this, localX, y, localZ);
/* 167 */         this.lightEngine.checkBlock(pos);
/*     */       } 
/*     */     } 
/* 171 */     EnumSet<Heightmap.Types> heightmapsAfter = getPersistedStatus().heightmapsAfter();
/* 172 */     EnumSet<Heightmap.Types> toPrime = null;
/* 174 */     for (Heightmap.Types type : heightmapsAfter) {
/* 175 */       Heightmap heightmap = this.heightmaps.get(type);
/* 176 */       if (heightmap == null) {
/* 177 */         if (toPrime == null)
/* 178 */           toPrime = EnumSet.noneOf(Heightmap.Types.class); 
/* 180 */         toPrime.add(type);
/*     */       } 
/*     */     } 
/* 184 */     if (toPrime != null)
/* 185 */       Heightmap.primeHeightmaps(this, toPrime); 
/* 188 */     for (Heightmap.Types type : heightmapsAfter)
/* 189 */       ((Heightmap)this.heightmaps.get(type)).update(localX, y, localZ, state); 
/* 192 */     return oldState;
/*     */   }
/*     */   
/*     */   public void setBlockEntity(BlockEntity blockEntity) {
/* 197 */     this.pendingBlockEntities.remove(blockEntity.getBlockPos());
/* 198 */     this.blockEntities.put(blockEntity.getBlockPos(), blockEntity);
/*     */   }
/*     */   
/*     */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 203 */     return this.blockEntities.get(pos);
/*     */   }
/*     */   
/*     */   public Map<BlockPos, BlockEntity> getBlockEntities() {
/* 207 */     return this.blockEntities;
/*     */   }
/*     */   
/*     */   public void addEntity(CompoundTag tag) {
/* 211 */     this.entities.add(tag);
/*     */   }
/*     */   
/*     */   public void addEntity(Entity entity) {
/* 216 */     if (entity.isPassenger())
/*     */       return; 
/* 222 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), LOGGER);
/*     */     try {
/* 223 */       TagValueOutput output = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)entity.registryAccess());
/* 224 */       entity.save((ValueOutput)output);
/* 225 */       addEntity(output.buildResult());
/* 226 */       reporter.close();
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
/*     */   public void setStartForStructure(Structure structure, StructureStart structureStart) {
/* 231 */     BelowZeroRetrogen belowZeroRetrogen = getBelowZeroRetrogen();
/* 232 */     if (belowZeroRetrogen != null && structureStart.isValid()) {
/* 233 */       BoundingBox boundingBox = structureStart.getBoundingBox();
/* 234 */       LevelHeightAccessor heightAccessor = getHeightAccessorForGeneration();
/* 235 */       if (boundingBox.minY() < heightAccessor.getMinY() || boundingBox.maxY() > heightAccessor.getMaxY())
/*     */         return; 
/*     */     } 
/* 239 */     super.setStartForStructure(structure, structureStart);
/*     */   }
/*     */   
/*     */   public List<CompoundTag> getEntities() {
/* 243 */     return this.entities;
/*     */   }
/*     */   
/*     */   public ChunkStatus getPersistedStatus() {
/* 248 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setPersistedStatus(ChunkStatus status) {
/* 252 */     this.status = status;
/* 253 */     if (this.belowZeroRetrogen != null && status.isOrAfter(this.belowZeroRetrogen.targetStatus()))
/* 254 */       setBelowZeroRetrogen((BelowZeroRetrogen)null); 
/* 256 */     markUnsaved();
/*     */   }
/*     */   
/*     */   public Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/* 261 */     if (getHighestGeneratedStatus().isOrAfter(ChunkStatus.BIOMES))
/* 262 */       return super.getNoiseBiome(quartX, quartY, quartZ); 
/* 264 */     throw new IllegalStateException("Asking for biomes before we have biomes");
/*     */   }
/*     */   
/*     */   public static short packOffsetCoordinates(BlockPos pos) {
/* 268 */     int x = pos.getX();
/* 269 */     int y = pos.getY();
/* 270 */     int z = pos.getZ();
/* 271 */     int dx = x & 0xF;
/* 272 */     int dy = y & 0xF;
/* 273 */     int dz = z & 0xF;
/* 274 */     return (short)(dx | dy << 4 | dz << 8);
/*     */   }
/*     */   
/*     */   public static BlockPos unpackOffsetCoordinates(short packedData, int sectionY, ChunkPos chunkPos) {
/* 278 */     int posX = SectionPos.sectionToBlockCoord(chunkPos.x(), packedData & 0xF);
/* 279 */     int posY = SectionPos.sectionToBlockCoord(sectionY, packedData >>> 4 & 0xF);
/* 280 */     int posZ = SectionPos.sectionToBlockCoord(chunkPos.z(), packedData >>> 8 & 0xF);
/* 281 */     return new BlockPos(posX, posY, posZ);
/*     */   }
/*     */   
/*     */   public void markPosForPostProcessing(BlockPos blockPos) {
/* 286 */     if (isInsideBuildHeight(blockPos))
/* 287 */       ChunkAccess.getOrCreateOffsetList(this.postProcessing, getSectionIndex(blockPos.getY())).add(packOffsetCoordinates(blockPos)); 
/*     */   }
/*     */   
/*     */   public void addPackedPostProcess(ShortList packedOffsets, int sectionIndex) {
/* 293 */     ChunkAccess.getOrCreateOffsetList(this.postProcessing, sectionIndex).addAll(packedOffsets);
/*     */   }
/*     */   
/*     */   public Map<BlockPos, CompoundTag> getBlockEntityNbts() {
/* 297 */     return Collections.unmodifiableMap(this.pendingBlockEntities);
/*     */   }
/*     */   
/*     */   public CompoundTag getBlockEntityNbtForSaving(BlockPos blockPos, HolderLookup.Provider registryAccess) {
/* 302 */     BlockEntity blockEntity = getBlockEntity(blockPos);
/* 303 */     if (blockEntity != null)
/* 304 */       return blockEntity.saveWithFullMetadata(registryAccess); 
/* 306 */     return this.pendingBlockEntities.get(blockPos);
/*     */   }
/*     */   
/*     */   public void removeBlockEntity(BlockPos pos) {
/* 311 */     this.blockEntities.remove(pos);
/* 312 */     this.pendingBlockEntities.remove(pos);
/*     */   }
/*     */   
/*     */   public CarvingMask getCarvingMask() {
/* 316 */     return this.carvingMask;
/*     */   }
/*     */   
/*     */   public CarvingMask getOrCreateCarvingMask() {
/* 320 */     if (this.carvingMask == null)
/* 321 */       this.carvingMask = new CarvingMask(getHeight(), getMinY()); 
/* 323 */     return this.carvingMask;
/*     */   }
/*     */   
/*     */   public void setCarvingMask(CarvingMask data) {
/* 327 */     this.carvingMask = data;
/*     */   }
/*     */   
/*     */   public void setLightEngine(LevelLightEngine lightEngine) {
/* 331 */     this.lightEngine = lightEngine;
/*     */   }
/*     */   
/*     */   public void setBelowZeroRetrogen(BelowZeroRetrogen belowZeroRetrogen) {
/* 335 */     this.belowZeroRetrogen = belowZeroRetrogen;
/*     */   }
/*     */   
/*     */   public BelowZeroRetrogen getBelowZeroRetrogen() {
/* 340 */     return this.belowZeroRetrogen;
/*     */   }
/*     */   
/*     */   private static <T> LevelChunkTicks<T> unpackTicks(ProtoChunkTicks<T> ticks) {
/* 344 */     return new LevelChunkTicks(ticks.scheduledTicks());
/*     */   }
/*     */   
/*     */   public LevelChunkTicks<Block> unpackBlockTicks() {
/* 348 */     return unpackTicks(this.blockTicks);
/*     */   }
/*     */   
/*     */   public LevelChunkTicks<Fluid> unpackFluidTicks() {
/* 352 */     return unpackTicks(this.fluidTicks);
/*     */   }
/*     */   
/*     */   public LevelHeightAccessor getHeightAccessorForGeneration() {
/* 357 */     if (isUpgrading())
/* 358 */       return BelowZeroRetrogen.UPGRADE_HEIGHT_ACCESSOR; 
/* 360 */     return (LevelHeightAccessor)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\ProtoChunk.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */