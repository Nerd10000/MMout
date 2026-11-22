/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.CarvingMask;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.carver.CarvingContext;
/*     */ import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public final class NoiseBasedChunkGenerator extends ChunkGenerator {
/*     */   public static final MapCodec<NoiseBasedChunkGenerator> CODEC;
/*     */   
/*     */   static {
/*  53 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)BiomeSource.CODEC.fieldOf("biome_source").forGetter(()), (App)NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(())).apply((Applicative)i, i.stable(NoiseBasedChunkGenerator::new)));
/*     */   }
/*     */   
/*  58 */   private static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*     */   
/*     */   private final Holder<NoiseGeneratorSettings> settings;
/*     */   
/*     */   private final Supplier<Aquifer.FluidPicker> globalFluidPicker;
/*     */   
/*     */   public NoiseBasedChunkGenerator(BiomeSource biomeSource, Holder<NoiseGeneratorSettings> settings) {
/*  65 */     super(biomeSource);
/*  67 */     this.settings = settings;
/*  68 */     this.globalFluidPicker = (Supplier<Aquifer.FluidPicker>)Suppliers.memoize(() -> createFluidPicker((NoiseGeneratorSettings)settings.value()));
/*     */   }
/*     */   
/*     */   private static Aquifer.FluidPicker createFluidPicker(NoiseGeneratorSettings settings) {
/*  73 */     Aquifer.FluidStatus lavaStatus = new Aquifer.FluidStatus(-54, Blocks.LAVA.defaultBlockState());
/*  74 */     int seaLevel = settings.seaLevel();
/*  75 */     Aquifer.FluidStatus seaStatus = new Aquifer.FluidStatus(seaLevel, settings.defaultFluid());
/*  77 */     Aquifer.FluidStatus emptyStatus = new Aquifer.FluidStatus(DimensionType.MIN_Y * 2, Blocks.AIR.defaultBlockState());
/*  80 */     return (x, y, z) -> SharedConstants.DEBUG_DISABLE_FLUID_GENERATION ? emptyStatus : ((y < Math.min(-54, seaLevel)) ? lavaStatus : seaStatus);
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkAccess> createBiomes(RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess protoChunk) {
/*  93 */     return CompletableFuture.supplyAsync(() -> {
/*     */           doCreateBiomes(blender, randomState, structureManager, protoChunk);
/*     */           return protoChunk;
/*  96 */         }Util.backgroundExecutor().forName("init_biomes"));
/*     */   }
/*     */   
/*     */   private void doCreateBiomes(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess protoChunk) {
/* 100 */     NoiseChunk noiseChunk = protoChunk.getOrCreateNoiseChunk(chunk -> createNoiseChunk(chunk, structureManager, blender, randomState));
/* 102 */     BiomeResolver biomeResolver = BelowZeroRetrogen.getBiomeResolver(blender.getBiomeResolver((BiomeResolver)this.biomeSource), protoChunk);
/* 104 */     protoChunk.fillBiomesFromNoise(biomeResolver, noiseChunk.cachedClimateSampler(randomState.router(), ((NoiseGeneratorSettings)this.settings.value()).spawnTarget()));
/*     */   }
/*     */   
/*     */   private NoiseChunk createNoiseChunk(ChunkAccess chunk, StructureManager structureManager, Blender blender, RandomState randomState) {
/* 108 */     return NoiseChunk.forChunk(chunk, randomState, Beardifier.forStructuresInChunk(structureManager, chunk.getPos()), (NoiseGeneratorSettings)this.settings.value(), this.globalFluidPicker.get(), blender);
/*     */   }
/*     */   
/*     */   protected MapCodec<? extends ChunkGenerator> codec() {
/* 113 */     return (MapCodec)CODEC;
/*     */   }
/*     */   
/*     */   public Holder<NoiseGeneratorSettings> generatorSettings() {
/* 117 */     return this.settings;
/*     */   }
/*     */   
/*     */   public boolean stable(ResourceKey<NoiseGeneratorSettings> expectedPreset) {
/* 121 */     return this.settings.is(expectedPreset);
/*     */   }
/*     */   
/*     */   public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) {
/* 126 */     return iterateNoiseColumn(heightAccessor, randomState, x, z, (MutableObject<NoiseColumn>)null, type.isOpaque()).orElse(heightAccessor.getMinY());
/*     */   }
/*     */   
/*     */   public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor heightAccessor, RandomState randomState) {
/* 131 */     MutableObject<NoiseColumn> result = new MutableObject();
/* 132 */     iterateNoiseColumn(heightAccessor, randomState, x, z, result, (Predicate<BlockState>)null);
/* 133 */     return (NoiseColumn)result.get();
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public double getInterpolatedNoiseValue(RandomState randomState, DensityFunction.FunctionContext context) {
/* 140 */     NoiseSettings noiseSettings = ((NoiseGeneratorSettings)this.settings.value()).noiseSettings();
/* 141 */     int cellWidth = noiseSettings.getCellWidth();
/* 142 */     int cellHeight = noiseSettings.getCellHeight();
/* 143 */     int minY = noiseSettings.minY();
/* 145 */     int blockX = context.blockX();
/* 146 */     int blockY = context.blockY();
/* 147 */     int blockZ = context.blockZ();
/* 149 */     if (blockY < minY || blockY >= minY + noiseSettings.height())
/* 150 */       return Double.NaN; 
/* 162 */     NoiseChunk noiseChunk = new NoiseChunk(1, randomState, blockX - Math.floorMod(blockX, cellWidth), blockZ - Math.floorMod(blockZ, cellWidth), noiseSettings, DensityFunctions.BeardifierMarker.INSTANCE, (NoiseGeneratorSettings)this.settings.value(), this.globalFluidPicker.get(), Blender.empty());
/* 165 */     noiseChunk.initializeForFirstCellX();
/* 166 */     noiseChunk.advanceCellX(0);
/* 167 */     noiseChunk.selectCellYZ(Math.floorDiv(blockY - minY, cellHeight), 0);
/* 168 */     noiseChunk.updateForY(blockY, Math.floorMod(blockY - minY, cellHeight) / cellHeight);
/* 169 */     noiseChunk.updateForX(blockX, Math.floorMod(blockX, cellWidth) / cellWidth);
/* 170 */     noiseChunk.updateForZ(blockZ, Math.floorMod(blockZ, cellWidth) / cellWidth);
/* 172 */     return noiseChunk.getInterpolatedDensity();
/*     */   }
/*     */   
/*     */   public void addDebugScreenInfo(List<String> result, RandomState randomState, BlockPos feetPos) {
/* 177 */     DecimalFormat format = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.ROOT));
/* 179 */     NoiseRouter router = randomState.router();
/* 180 */     DensityFunction.SinglePointContext context = new DensityFunction.SinglePointContext(feetPos.getX(), feetPos.getY(), feetPos.getZ());
/* 182 */     double weirdness = router.ridges().compute(context);
/* 183 */     result.add("NoiseRouter N: " + format
/* 184 */         .format(getInterpolatedNoiseValue(randomState, context)) + " T: " + format
/* 185 */         .format(router.temperature().compute(context)) + " V: " + format
/* 186 */         .format(router.vegetation().compute(context)) + " C: " + format
/* 187 */         .format(router.continents().compute(context)) + " E: " + format
/* 188 */         .format(router.erosion().compute(context)) + " D: " + format
/* 189 */         .format(router.depth().compute(context)) + " W: " + format
/* 190 */         .format(weirdness) + " PV: " + format
/* 191 */         .format(NoiseRouterData.peaksAndValleys((float)weirdness)) + " PS: " + format
/* 192 */         .format(router.preliminarySurfaceLevel().compute(context)));
/*     */   }
/*     */   
/*     */   private OptionalInt iterateNoiseColumn(LevelHeightAccessor heightAccessor, RandomState randomState, int blockX, int blockZ, MutableObject<NoiseColumn> columnReference, Predicate<BlockState> tester) {
/*     */     BlockState[] writeTo;
/* 200 */     NoiseSettings noiseSettings = ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().clampToHeightAccessor(heightAccessor);
/* 201 */     int cellHeight = noiseSettings.getCellHeight();
/* 203 */     int minY = noiseSettings.minY();
/* 204 */     int cellMinY = Mth.floorDiv(minY, cellHeight);
/* 205 */     int cellCountY = Mth.floorDiv(noiseSettings.height(), cellHeight);
/* 207 */     if (cellCountY <= 0)
/* 208 */       return OptionalInt.empty(); 
/* 213 */     if (columnReference == null) {
/* 214 */       writeTo = null;
/*     */     } else {
/* 216 */       writeTo = new BlockState[noiseSettings.height()];
/* 217 */       columnReference.setValue(new NoiseColumn(minY, writeTo));
/*     */     } 
/* 220 */     int cellWidth = noiseSettings.getCellWidth();
/* 222 */     int noiseChunkX = Math.floorDiv(blockX, cellWidth);
/* 223 */     int noiseChunkZ = Math.floorDiv(blockZ, cellWidth);
/* 224 */     int xInCell = Math.floorMod(blockX, cellWidth);
/* 225 */     int zInCell = Math.floorMod(blockZ, cellWidth);
/* 226 */     int firstBlockX = noiseChunkX * cellWidth;
/* 227 */     int firstBlockZ = noiseChunkZ * cellWidth;
/* 229 */     double factorX = xInCell / cellWidth;
/* 230 */     double factorZ = zInCell / cellWidth;
/* 236 */     NoiseChunk noiseChunk = new NoiseChunk(1, randomState, firstBlockX, firstBlockZ, noiseSettings, DensityFunctions.BeardifierMarker.INSTANCE, (NoiseGeneratorSettings)this.settings.value(), this.globalFluidPicker.get(), Blender.empty());
/* 239 */     noiseChunk.initializeForFirstCellX();
/* 240 */     noiseChunk.advanceCellX(0);
/* 242 */     for (int cellYIndex = cellCountY - 1; cellYIndex >= 0; cellYIndex--) {
/* 243 */       noiseChunk.selectCellYZ(cellYIndex, 0);
/* 245 */       for (int yInCell = cellHeight - 1; yInCell >= 0; yInCell--) {
/* 246 */         int posY = (cellMinY + cellYIndex) * cellHeight + yInCell;
/* 248 */         double factorY = yInCell / cellHeight;
/* 249 */         noiseChunk.updateForY(posY, factorY);
/* 250 */         noiseChunk.updateForX(blockX, factorX);
/* 251 */         noiseChunk.updateForZ(blockZ, factorZ);
/* 253 */         BlockState baseState = noiseChunk.getInterpolatedState();
/* 254 */         BlockState state = (baseState == null) ? ((NoiseGeneratorSettings)this.settings.value()).defaultBlock() : baseState;
/* 256 */         if (writeTo != null) {
/* 257 */           int yIndex = cellYIndex * cellHeight + yInCell;
/* 258 */           writeTo[yIndex] = state;
/*     */         } 
/* 260 */         if (tester != null && tester.test(state)) {
/* 261 */           noiseChunk.stopInterpolation();
/* 262 */           return OptionalInt.of(posY + 1);
/*     */         } 
/*     */       } 
/*     */     } 
/* 267 */     noiseChunk.stopInterpolation();
/* 269 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   public void buildSurface(WorldGenRegion region, StructureManager structureManager, RandomState randomState, ChunkAccess protoChunk) {
/* 274 */     if (SharedConstants.debugVoidTerrain(protoChunk.getPos()) || SharedConstants.DEBUG_DISABLE_SURFACE)
/*     */       return; 
/* 278 */     WorldGenerationContext context = new WorldGenerationContext(this, (LevelHeightAccessor)region);
/* 280 */     Set<Holder<Biome>> possibleBiomes = collectPossibleBiomes(region, 1);
/* 281 */     buildSurface(protoChunk, context, randomState, structureManager, region.getBiomeManager(), Blender.of(region), possibleBiomes);
/*     */   }
/*     */   
/*     */   private static Set<Holder<Biome>> collectPossibleBiomes(WorldGenRegion region, int chunkRadius) {
/* 285 */     ReferenceOpenHashSet referenceOpenHashSet = new ReferenceOpenHashSet();
/* 286 */     ChunkPos center = region.getCenter();
/* 287 */     for (int z = center.z() - chunkRadius; z <= center.z() + chunkRadius; z++) {
/* 288 */       for (int x = center.x() - chunkRadius; x <= center.x() + chunkRadius; x++)
/* 289 */         region.getChunk(x, z).collectBiomesInPalette((Set)referenceOpenHashSet); 
/*     */     } 
/* 292 */     return (Set<Holder<Biome>>)referenceOpenHashSet;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void buildSurface(ChunkAccess protoChunk, WorldGenerationContext context, RandomState randomState, StructureManager structureManager, BiomeManager biomeManager, Blender blender, Set<Holder<Biome>> possibleBiomes) {
/* 297 */     NoiseChunk noiseChunk = protoChunk.getOrCreateNoiseChunk(chunk -> createNoiseChunk(chunk, structureManager, blender, randomState));
/* 298 */     NoiseGeneratorSettings settings = (NoiseGeneratorSettings)this.settings.value();
/* 299 */     randomState.surfaceSystem().buildSurface(randomState, biomeManager, settings.useLegacyRandomSource(), context, protoChunk, noiseChunk, settings.surfaceRule(), possibleBiomes);
/*     */   }
/*     */   
/*     */   public void applyCarvers(WorldGenRegion region, long seed, RandomState randomState, BiomeManager biomeManager, StructureManager structureManager, ChunkAccess chunk) {
/* 304 */     if (SharedConstants.DEBUG_DISABLE_CARVERS)
/*     */       return; 
/* 307 */     BiomeManager correctBiomeManager = biomeManager.withDifferentSource((quartX, quartY, quartZ) -> this.biomeSource.getNoiseBiome(quartX, quartY, quartZ, randomState.sampler()));
/* 309 */     WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
/* 310 */     int range = 8;
/* 312 */     ChunkPos pos = chunk.getPos();
/* 314 */     NoiseChunk noiseChunk = chunk.getOrCreateNoiseChunk(c -> createNoiseChunk(c, structureManager, Blender.of(region), randomState));
/* 315 */     Aquifer aquifer = noiseChunk.aquifer();
/* 316 */     CarvingContext context = new CarvingContext(this, region.registryAccess(), chunk.getHeightAccessorForGeneration(), noiseChunk, randomState, ((NoiseGeneratorSettings)this.settings.value()).surfaceRule());
/* 318 */     CarvingMask mask = ((ProtoChunk)chunk).getOrCreateCarvingMask();
/* 319 */     for (int dx = -8; dx <= 8; dx++) {
/* 320 */       for (int dz = -8; dz <= 8; dz++) {
/* 321 */         ChunkPos sourcePos = new ChunkPos(pos.x() + dx, pos.z() + dz);
/* 322 */         ChunkAccess carverCenterChunk = region.getChunk(sourcePos.x(), sourcePos.z());
/* 323 */         BiomeGenerationSettings sourceBiomeGenerationSettings = carverCenterChunk.carverBiome(() -> getBiomeGenerationSettings(this.biomeSource.getNoiseBiome(QuartPos.fromBlock(sourcePos.getMinBlockX()), 0, QuartPos.fromBlock(sourcePos.getMinBlockZ()), randomState.sampler())));
/* 324 */         Iterable<Holder<ConfiguredWorldCarver<?>>> carvers = sourceBiomeGenerationSettings.getCarvers();
/* 326 */         int index = 0;
/* 327 */         for (Holder<ConfiguredWorldCarver<?>> carverHolder : carvers) {
/* 328 */           ConfiguredWorldCarver<?> carver = (ConfiguredWorldCarver)carverHolder.value();
/* 329 */           random.setLargeFeatureSeed(seed + index, sourcePos.x(), sourcePos.z());
/* 330 */           if (carver.isStartChunk(random)) {
/* 331 */             Objects.requireNonNull(correctBiomeManager);
/* 331 */             carver.carve(context, chunk, correctBiomeManager::getBiome, random, aquifer, sourcePos, mask);
/*     */           } 
/* 333 */           index++;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess centerChunk) {
/* 341 */     NoiseSettings noiseSettings = ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().clampToHeightAccessor(centerChunk.getHeightAccessorForGeneration());
/* 343 */     int minY = noiseSettings.minY();
/* 344 */     int cellYMin = Mth.floorDiv(minY, noiseSettings.getCellHeight());
/* 345 */     int cellCountY = Mth.floorDiv(noiseSettings.height(), noiseSettings.getCellHeight());
/* 347 */     if (cellCountY <= 0)
/* 348 */       return CompletableFuture.completedFuture(centerChunk); 
/* 351 */     return CompletableFuture.supplyAsync(() -> {
/*     */           int topSectionIndex = centerChunk.getSectionIndex(cellCountY * noiseSettings.getCellHeight() - 1 + minY);
/*     */           int bottomSectionIndex = centerChunk.getSectionIndex(minY);
/*     */           Set<LevelChunkSection> sections = Sets.newHashSet();
/*     */           for (int sectionIndex = topSectionIndex; sectionIndex >= bottomSectionIndex; sectionIndex--) {
/*     */             LevelChunkSection section = centerChunk.getSection(sectionIndex);
/*     */             section.acquire();
/*     */             sections.add(section);
/*     */           } 
/*     */           try {
/*     */             return doFill(blender, structureManager, randomState, centerChunk, cellYMin, cellCountY);
/*     */           } finally {
/*     */             for (LevelChunkSection section : sections)
/*     */               section.release(); 
/*     */           } 
/* 369 */         }Util.backgroundExecutor().forName("wgen_fill_noise"));
/*     */   }
/*     */   
/*     */   private ChunkAccess doFill(Blender blender, StructureManager structureManager, RandomState randomState, ChunkAccess centerChunk, int cellMinY, int cellCountY) {
/* 374 */     NoiseChunk noiseChunk = centerChunk.getOrCreateNoiseChunk(chunk -> createNoiseChunk(chunk, structureManager, blender, randomState));
/* 376 */     Heightmap oceanFloor = centerChunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
/* 377 */     Heightmap worldSurface = centerChunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);
/* 379 */     ChunkPos chunkPos = centerChunk.getPos();
/* 381 */     int chunkStartBlockX = chunkPos.getMinBlockX();
/* 382 */     int chunkStartBlockZ = chunkPos.getMinBlockZ();
/* 384 */     Aquifer aquifer = noiseChunk.aquifer();
/* 386 */     noiseChunk.initializeForFirstCellX();
/* 388 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 390 */     int cellWidth = noiseChunk.cellWidth();
/* 391 */     int cellHeight = noiseChunk.cellHeight();
/* 393 */     int cellCountX = 16 / cellWidth;
/* 394 */     int cellCountZ = 16 / cellWidth;
/* 397 */     for (int cellXIndex = 0; cellXIndex < cellCountX; cellXIndex++) {
/* 398 */       noiseChunk.advanceCellX(cellXIndex);
/* 400 */       for (int cellZIndex = 0; cellZIndex < cellCountZ; cellZIndex++) {
/* 401 */         int lastSectionIndex = centerChunk.getSectionsCount() - 1;
/* 402 */         LevelChunkSection section = centerChunk.getSection(lastSectionIndex);
/* 404 */         for (int cellYIndex = cellCountY - 1; cellYIndex >= 0; cellYIndex--) {
/* 405 */           noiseChunk.selectCellYZ(cellYIndex, cellZIndex);
/* 407 */           for (int yInCell = cellHeight - 1; yInCell >= 0; yInCell--) {
/* 408 */             int posY = (cellMinY + cellYIndex) * cellHeight + yInCell;
/* 409 */             int yInSection = posY & 0xF;
/* 411 */             int sectionIndex = centerChunk.getSectionIndex(posY);
/* 412 */             if (lastSectionIndex != sectionIndex) {
/* 413 */               lastSectionIndex = sectionIndex;
/* 414 */               section = centerChunk.getSection(sectionIndex);
/*     */             } 
/* 417 */             double factorY = yInCell / cellHeight;
/* 418 */             noiseChunk.updateForY(posY, factorY);
/* 420 */             for (int xInCell = 0; xInCell < cellWidth; xInCell++) {
/* 421 */               int posX = chunkStartBlockX + cellXIndex * cellWidth + xInCell;
/* 422 */               int xInSection = posX & 0xF;
/* 424 */               double factorX = xInCell / cellWidth;
/* 425 */               noiseChunk.updateForX(posX, factorX);
/* 427 */               for (int zInCell = 0; zInCell < cellWidth; zInCell++) {
/* 428 */                 int posZ = chunkStartBlockZ + cellZIndex * cellWidth + zInCell;
/* 429 */                 int zInSection = posZ & 0xF;
/* 431 */                 double factorZ = zInCell / cellWidth;
/* 433 */                 noiseChunk.updateForZ(posZ, factorZ);
/* 435 */                 BlockState state = noiseChunk.getInterpolatedState();
/* 437 */                 if (state == null)
/* 438 */                   state = ((NoiseGeneratorSettings)this.settings.value()).defaultBlock(); 
/* 441 */                 state = debugPreliminarySurfaceLevel(noiseChunk, posX, posY, posZ, state);
/* 443 */                 if (state != AIR && !SharedConstants.debugVoidTerrain(centerChunk.getPos())) {
/* 446 */                   section.setBlockState(xInSection, yInSection, zInSection, state, false);
/* 447 */                   oceanFloor.update(xInSection, posY, zInSection, state);
/* 448 */                   worldSurface.update(xInSection, posY, zInSection, state);
/* 450 */                   if (aquifer.shouldScheduleFluidUpdate() && !state.getFluidState().isEmpty()) {
/* 451 */                     blockPos.set(posX, posY, posZ);
/* 453 */                     centerChunk.markPosForPostProcessing((BlockPos)blockPos);
/*     */                   } 
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 460 */       noiseChunk.swapSlices();
/*     */     } 
/* 462 */     noiseChunk.stopInterpolation();
/* 463 */     return centerChunk;
/*     */   }
/*     */   
/*     */   private BlockState debugPreliminarySurfaceLevel(NoiseChunk noiseChunk, int posX, int posY, int posZ, BlockState state) {
/* 467 */     if (SharedConstants.DEBUG_AQUIFERS && posZ >= 0 && posZ % 4 == 0) {
/* 468 */       int preliminarySurfaceLevel = noiseChunk.preliminarySurfaceLevel(posX, posZ);
/* 469 */       int adjustedSurfaceLevel = preliminarySurfaceLevel + 8;
/* 470 */       if (posY == adjustedSurfaceLevel)
/* 471 */         state = (adjustedSurfaceLevel < getSeaLevel()) ? Blocks.SLIME_BLOCK.defaultBlockState() : Blocks.HONEY_BLOCK.defaultBlockState(); 
/*     */     } 
/* 474 */     return state;
/*     */   }
/*     */   
/*     */   public int getGenDepth() {
/* 479 */     return ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().height();
/*     */   }
/*     */   
/*     */   public int getSeaLevel() {
/* 484 */     return ((NoiseGeneratorSettings)this.settings.value()).seaLevel();
/*     */   }
/*     */   
/*     */   public int getMinY() {
/* 489 */     return ((NoiseGeneratorSettings)this.settings.value()).noiseSettings().minY();
/*     */   }
/*     */   
/*     */   public void spawnOriginalMobs(WorldGenRegion worldGenRegion) {
/* 495 */     if (((NoiseGeneratorSettings)this.settings.value()).disableMobGeneration())
/*     */       return; 
/* 498 */     ChunkPos center = worldGenRegion.getCenter();
/* 500 */     Holder<Biome> biome = worldGenRegion.getBiome(center.getWorldPosition().atY(worldGenRegion.getMaxY()));
/* 502 */     WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(RandomSupport.generateUniqueSeed()));
/* 503 */     random.setDecorationSeed(worldGenRegion.getSeed(), center.getMinBlockX(), center.getMinBlockZ());
/* 504 */     NaturalSpawner.spawnMobsForChunkGeneration((ServerLevelAccessor)worldGenRegion, biome, center, random);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseBasedChunkGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */