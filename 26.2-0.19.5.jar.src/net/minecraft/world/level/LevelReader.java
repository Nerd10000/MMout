/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeReader;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public interface LevelReader extends BlockAndLightGetter, CollisionGetter, SignalGetter, BiomeManager.NoiseBiomeSource {
/*     */   ChunkAccess getChunk(int paramInt1, int paramInt2, ChunkStatus paramChunkStatus, boolean paramBoolean);
/*     */   
/*     */   @Deprecated
/*     */   boolean hasChunk(int paramInt1, int paramInt2);
/*     */   
/*     */   int getHeight(Heightmap.Types paramTypes, int paramInt1, int paramInt2);
/*     */   
/*     */   default int getHeight(Heightmap.Types type, BlockPos pos) {
/*  37 */     return getHeight(type, pos.getX(), pos.getZ());
/*     */   }
/*     */   
/*     */   int getSkyDarken();
/*     */   
/*     */   BiomeManager getBiomeManager();
/*     */   
/*     */   default Holder<Biome> getBiome(BlockPos pos) {
/*  45 */     return getBiomeManager().getBiome(pos);
/*     */   }
/*     */   
/*     */   default Stream<BlockState> getBlockStatesIfLoaded(AABB box) {
/*  49 */     int x0 = Mth.floor(box.minX);
/*  50 */     int x1 = Mth.floor(box.maxX);
/*  51 */     int y0 = Mth.floor(box.minY);
/*  52 */     int y1 = Mth.floor(box.maxY);
/*  53 */     int z0 = Mth.floor(box.minZ);
/*  54 */     int z1 = Mth.floor(box.maxZ);
/*  56 */     if (hasChunksAt(x0, y0, z0, x1, y1, z1))
/*  57 */       return getBlockStates(box); 
/*  59 */     return Stream.empty();
/*     */   }
/*     */   
/*     */   default Holder<Biome> getNoiseBiome(int quartX, int quartY, int quartZ) {
/*  64 */     ChunkAccess chunk = getChunk(QuartPos.toSection(quartX), QuartPos.toSection(quartZ), ChunkStatus.BIOMES, false);
/*  65 */     if (chunk != null)
/*  66 */       return chunk.getNoiseBiome(quartX, quartY, quartZ); 
/*  68 */     return getUncachedNoiseBiome(quartX, quartY, quartZ);
/*     */   }
/*     */   
/*     */   Holder<Biome> getUncachedNoiseBiome(int paramInt1, int paramInt2, int paramInt3);
/*     */   
/*     */   boolean isClientSide();
/*     */   
/*     */   int getSeaLevel();
/*     */   
/*     */   DimensionType dimensionType();
/*     */   
/*     */   default int getMinY() {
/*  81 */     return dimensionType().minY();
/*     */   }
/*     */   
/*     */   default int getHeight() {
/*  86 */     return dimensionType().height();
/*     */   }
/*     */   
/*     */   default BlockPos getHeightmapPos(Heightmap.Types type, BlockPos pos) {
/*  90 */     return new BlockPos(pos.getX(), getHeight(type, pos.getX(), pos.getZ()), pos.getZ());
/*     */   }
/*     */   
/*     */   default boolean isEmptyBlock(BlockPos pos) {
/*  94 */     return getBlockState(pos).isAir();
/*     */   }
/*     */   
/*     */   default boolean canSeeSkyFromBelowWater(BlockPos pos) {
/*  98 */     if (pos.getY() >= getSeaLevel())
/*  99 */       return canSeeSky(pos); 
/* 101 */     BlockPos scanPoint = new BlockPos(pos.getX(), getSeaLevel(), pos.getZ());
/* 102 */     if (!canSeeSky(scanPoint))
/* 103 */       return false; 
/* 105 */     scanPoint = scanPoint.below();
/* 106 */     while (scanPoint.getY() > pos.getY()) {
/* 107 */       BlockState state = getBlockState(scanPoint);
/* 108 */       if (state.getLightDampening() > 0 && !state.liquid())
/* 109 */         return false; 
/* 111 */       scanPoint = scanPoint.below();
/*     */     } 
/* 113 */     return true;
/*     */   }
/*     */   
/*     */   default float getPathfindingCostFromLightLevels(BlockPos pos) {
/* 118 */     return getLightLevelDependentMagicValue(pos) - 0.5F;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   default float getLightLevelDependentMagicValue(BlockPos pos) {
/* 131 */     float v = getMaxLocalRawBrightness(pos) / 15.0F;
/* 133 */     float curvedV = v / (4.0F - 3.0F * v);
/* 134 */     return Mth.lerp(dimensionType().ambientLight(), curvedV, 1.0F);
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(BlockPos pos) {
/* 138 */     return getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(int chunkX, int chunkZ) {
/* 142 */     return getChunk(chunkX, chunkZ, ChunkStatus.FULL, true);
/*     */   }
/*     */   
/*     */   default ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus status) {
/* 146 */     return getChunk(chunkX, chunkZ, status, true);
/*     */   }
/*     */   
/*     */   default BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
/* 151 */     return (BlockGetter)getChunk(chunkX, chunkZ, ChunkStatus.EMPTY, false);
/*     */   }
/*     */   
/*     */   default boolean isWaterAt(BlockPos pos) {
/* 155 */     return getFluidState(pos).is(FluidTags.WATER);
/*     */   }
/*     */   
/*     */   default boolean containsAnyLiquid(AABB box) {
/* 159 */     int x0 = Mth.floor(box.minX);
/* 160 */     int x1 = Mth.ceil(box.maxX);
/* 161 */     int y0 = Mth.floor(box.minY);
/* 162 */     int y1 = Mth.ceil(box.maxY);
/* 163 */     int z0 = Mth.floor(box.minZ);
/* 164 */     int z1 = Mth.ceil(box.maxZ);
/* 166 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 167 */     for (int x = x0; x < x1; x++) {
/* 168 */       for (int y = y0; y < y1; y++) {
/* 169 */         for (int z = z0; z < z1; z++) {
/* 170 */           BlockState blockState = getBlockState((BlockPos)pos.set(x, y, z));
/* 171 */           if (!blockState.getFluidState().isEmpty())
/* 172 */             return true; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 177 */     return false;
/*     */   }
/*     */   
/*     */   default int getMaxLocalRawBrightness(BlockPos pos) {
/* 181 */     return getMaxLocalRawBrightness(pos, getSkyDarken());
/*     */   }
/*     */   
/*     */   default int getMaxLocalRawBrightness(BlockPos pos, int skyDarkening) {
/* 185 */     if (pos.getX() < -30000000 || pos.getZ() < -30000000 || pos.getX() >= 30000000 || pos.getZ() >= 30000000)
/* 186 */       return 15; 
/* 189 */     return getRawBrightness(pos, skyDarkening);
/*     */   }
/*     */   
/*     */   default int getEffectiveSkyBrightness(BlockPos pos) {
/* 193 */     return getBrightness(LightLayer.SKY, pos) - getSkyDarken();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunkAt(int blockX, int blockZ) {
/* 199 */     return hasChunk(SectionPos.blockToSectionCoord(blockX), SectionPos.blockToSectionCoord(blockZ));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunkAt(BlockPos pos) {
/* 205 */     return hasChunkAt(pos.getX(), pos.getZ());
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(BlockPos pos0, BlockPos pos1) {
/* 211 */     return hasChunksAt(pos0.getX(), pos0.getY(), pos0.getZ(), pos1.getX(), pos1.getY(), pos1.getZ());
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(int x0, int y0, int z0, int x1, int y1, int z1) {
/* 217 */     if (y1 < getMinY() || y0 > getMaxY())
/* 218 */       return false; 
/* 221 */     return hasChunksAt(x0, z0, x1, z1);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   default boolean hasChunksAt(int x0, int z0, int x1, int z1) {
/* 227 */     int chunkX0 = SectionPos.blockToSectionCoord(x0);
/* 228 */     int chunkX1 = SectionPos.blockToSectionCoord(x1);
/* 229 */     int chunkZ0 = SectionPos.blockToSectionCoord(z0);
/* 230 */     int chunkZ1 = SectionPos.blockToSectionCoord(z1);
/* 232 */     for (int chunkX = chunkX0; chunkX <= chunkX1; chunkX++) {
/* 233 */       for (int chunkZ = chunkZ0; chunkZ <= chunkZ1; chunkZ++) {
/* 234 */         if (!hasChunk(chunkX, chunkZ))
/* 235 */           return false; 
/*     */       } 
/*     */     } 
/* 240 */     return true;
/*     */   }
/*     */   
/*     */   RegistryAccess registryAccess();
/*     */   
/*     */   FeatureFlagSet enabledFeatures();
/*     */   
/*     */   default <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<? extends T>> key) {
/* 248 */     Registry<T> registry = registryAccess().lookupOrThrow(key);
/* 249 */     return (HolderLookup<T>)registry.filterFeatures(enabledFeatures());
/*     */   }
/*     */   
/*     */   EnvironmentAttributeReader environmentAttributes();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\LevelReader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */