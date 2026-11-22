/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.ColumnPos;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Climate;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.material.MaterialRuleList;
/*     */ 
/*     */ public class NoiseChunk implements DensityFunction.FunctionContext, DensityFunction.ContextProvider {
/*     */   private final int cellCountXZ;
/*     */   
/*     */   private final int cellCountY;
/*     */   
/*     */   private final int cellNoiseMinY;
/*     */   
/*     */   private final int firstCellX;
/*     */   
/*     */   private final int firstCellZ;
/*     */   
/*     */   private final int firstNoiseX;
/*     */   
/*     */   private final int firstNoiseZ;
/*     */   
/*     */   private final List<NoiseInterpolator> interpolators;
/*     */   
/*     */   private final List<CacheAllInCell> cellCaches;
/*     */   
/*  38 */   private final Map<DensityFunction, DensityFunction> wrapped = new HashMap<>();
/*     */   
/*  40 */   private final Long2IntMap preliminarySurfaceLevelCache = (Long2IntMap)new Long2IntOpenHashMap();
/*     */   
/*     */   private final Aquifer aquifer;
/*     */   
/*     */   private final DensityFunction preliminarySurfaceLevel;
/*     */   
/*     */   private final DensityFunction fullNoiseDensity;
/*     */   
/*     */   private final BlockStateFiller blockStateRule;
/*     */   
/*     */   private final Blender blender;
/*     */   
/*     */   private final FlatCache blendAlpha;
/*     */   
/*     */   private final FlatCache blendOffset;
/*     */   
/*     */   private final DensityFunctions.BeardifierOrMarker beardifier;
/*     */   
/*  54 */   private long lastBlendingDataPos = ChunkPos.INVALID_CHUNK_POS;
/*     */   
/*  55 */   private Blender.BlendingOutput lastBlendingOutput = new Blender.BlendingOutput(1.0D, 0.0D);
/*     */   
/*     */   private final int noiseSizeXZ;
/*     */   
/*     */   private final int cellWidth;
/*     */   
/*     */   private final int cellHeight;
/*     */   
/*     */   private boolean interpolating;
/*     */   
/*     */   private boolean fillingCell;
/*     */   
/*     */   private int cellStartBlockX;
/*     */   
/*     */   private int cellStartBlockY;
/*     */   
/*     */   private int cellStartBlockZ;
/*     */   
/*     */   private int inCellX;
/*     */   
/*     */   private int inCellY;
/*     */   
/*     */   private int inCellZ;
/*     */   
/*     */   private long interpolationCounter;
/*     */   
/*     */   private long arrayInterpolationCounter;
/*     */   
/*     */   private int arrayIndex;
/*     */   
/*  78 */   private final DensityFunction.ContextProvider sliceFillingContextProvider = new DensityFunction.ContextProvider() {
/*     */       public DensityFunction.FunctionContext forIndex(int cellYIndex) {
/*  81 */         NoiseChunk.this.cellStartBlockY = (cellYIndex + NoiseChunk.this.cellNoiseMinY) * NoiseChunk.this.cellHeight;
/*  82 */         NoiseChunk.this.interpolationCounter++;
/*  84 */         NoiseChunk.this.inCellY = 0;
/*  85 */         NoiseChunk.this.arrayIndex = cellYIndex;
/*  86 */         return NoiseChunk.this;
/*     */       }
/*     */       
/*     */       public void fillAllDirectly(double[] output, DensityFunction function) {
/*  92 */         for (int cellYIndex = 0; cellYIndex < NoiseChunk.this.cellCountY + 1; cellYIndex++) {
/*  93 */           NoiseChunk.this.cellStartBlockY = (cellYIndex + NoiseChunk.this.cellNoiseMinY) * NoiseChunk.this.cellHeight;
/*  94 */           NoiseChunk.this.interpolationCounter++;
/*  96 */           NoiseChunk.this.inCellY = 0;
/*  97 */           NoiseChunk.this.arrayIndex = cellYIndex;
/*  99 */           output[cellYIndex] = function.compute(NoiseChunk.this);
/*     */         } 
/*     */       }
/*     */     };
/*     */   
/*     */   public static NoiseChunk forChunk(ChunkAccess chunk, RandomState randomState, DensityFunctions.BeardifierOrMarker beardifier, NoiseGeneratorSettings settings, Aquifer.FluidPicker globalFluidPicker, Blender blender) {
/* 105 */     NoiseSettings noiseSettings = settings.noiseSettings().clampToHeightAccessor((LevelHeightAccessor)chunk);
/* 106 */     ChunkPos pos = chunk.getPos();
/* 107 */     int cellCountXZ = 16 / noiseSettings.getCellWidth();
/* 108 */     return new NoiseChunk(cellCountXZ, randomState, pos.getMinBlockX(), pos.getMinBlockZ(), noiseSettings, beardifier, settings, globalFluidPicker, blender);
/*     */   }
/*     */   
/*     */   public NoiseChunk(int cellCountXZ, RandomState randomState, int chunkMinBlockX, int chunkMinBlockZ, NoiseSettings noiseSettings, DensityFunctions.BeardifierOrMarker beardifier, NoiseGeneratorSettings settings, Aquifer.FluidPicker globalFluidPicker, Blender blender) {
/* 115 */     this.cellWidth = noiseSettings.getCellWidth();
/* 116 */     this.cellHeight = noiseSettings.getCellHeight();
/* 118 */     this.cellCountXZ = cellCountXZ;
/* 119 */     this.cellCountY = Mth.floorDiv(noiseSettings.height(), this.cellHeight);
/* 120 */     this.cellNoiseMinY = Mth.floorDiv(noiseSettings.minY(), this.cellHeight);
/* 122 */     this.firstCellX = Math.floorDiv(chunkMinBlockX, this.cellWidth);
/* 123 */     this.firstCellZ = Math.floorDiv(chunkMinBlockZ, this.cellWidth);
/* 125 */     this.interpolators = Lists.newArrayList();
/* 126 */     this.cellCaches = Lists.newArrayList();
/* 128 */     this.firstNoiseX = QuartPos.fromBlock(chunkMinBlockX);
/* 129 */     this.firstNoiseZ = QuartPos.fromBlock(chunkMinBlockZ);
/* 131 */     this.noiseSizeXZ = QuartPos.fromBlock(cellCountXZ * this.cellWidth);
/* 133 */     this.blender = blender;
/* 134 */     this.beardifier = beardifier;
/* 136 */     if (!blender.isEmpty()) {
/* 137 */       this.blendAlpha = new FlatCache(new BlendAlpha(), false);
/* 138 */       this.blendOffset = new FlatCache(new BlendOffset(), false);
/* 141 */       for (int x = 0; x <= this.noiseSizeXZ; x++) {
/* 142 */         int quartX = this.firstNoiseX + x;
/* 143 */         int blockX = QuartPos.toBlock(quartX);
/* 145 */         for (int z = 0; z <= this.noiseSizeXZ; z++) {
/* 146 */           int quartZ = this.firstNoiseZ + z;
/* 147 */           int blockZ = QuartPos.toBlock(quartZ);
/* 149 */           Blender.BlendingOutput blendingOutput = blender.blendOffsetAndFactor(blockX, blockZ);
/* 151 */           this.blendAlpha.values[x + z * this.blendAlpha.sizeXZ] = blendingOutput.alpha();
/* 152 */           this.blendOffset.values[x + z * this.blendOffset.sizeXZ] = blendingOutput.blendingOffset();
/*     */         } 
/*     */       } 
/*     */     } else {
/* 156 */       this.blendAlpha = null;
/* 157 */       this.blendOffset = null;
/*     */     } 
/* 160 */     NoiseRouter router = randomState.router();
/* 163 */     NoiseRouter wrappedRouter = router.mapAll(this::wrap);
/* 164 */     this.preliminarySurfaceLevel = wrappedRouter.preliminarySurfaceLevel();
/* 168 */     if (!settings.isAquifersEnabled()) {
/* 169 */       this.aquifer = Aquifer.createDisabled(globalFluidPicker);
/*     */     } else {
/* 171 */       int chunkX = SectionPos.blockToSectionCoord(chunkMinBlockX);
/* 172 */       int chunkZ = SectionPos.blockToSectionCoord(chunkMinBlockZ);
/* 173 */       this.aquifer = Aquifer.create(this, new ChunkPos(chunkX, chunkZ), wrappedRouter, randomState
/*     */           
/* 177 */           .aquiferRandom(), noiseSettings
/* 178 */           .minY(), noiseSettings
/* 179 */           .height(), globalFluidPicker);
/*     */     } 
/* 184 */     List<BlockStateFiller> builder = new ArrayList<>();
/* 189 */     DensityFunction fullNoiseValue = DensityFunctions.cacheAllInCell(DensityFunctions.add(wrappedRouter.finalDensity(), DensityFunctions.BeardifierMarker.INSTANCE)).mapAll(this::wrap);
/* 190 */     this.fullNoiseDensity = fullNoiseValue;
/* 193 */     builder.add(context -> this.aquifer.computeSubstance(context, fullNoiseValue.compute(context)));
/* 196 */     if (settings.oreVeinsEnabled())
/* 197 */       builder.add(OreVeinifier.create(wrappedRouter
/* 198 */             .veinToggle(), wrappedRouter
/* 199 */             .veinRidged(), wrappedRouter
/* 200 */             .veinGap(), randomState
/* 201 */             .oreRandom())); 
/* 205 */     this.blockStateRule = (BlockStateFiller)new MaterialRuleList(builder.<BlockStateFiller>toArray(new BlockStateFiller[0]));
/*     */   }
/*     */   
/*     */   protected Climate.Sampler cachedClimateSampler(NoiseRouter noises, List<Climate.ParameterPoint> spawnTarget) {
/* 209 */     return new Climate.Sampler(noises
/* 210 */         .temperature().mapAll(this::wrap), noises
/* 211 */         .vegetation().mapAll(this::wrap), noises
/* 212 */         .continents().mapAll(this::wrap), noises
/* 213 */         .erosion().mapAll(this::wrap), noises
/* 214 */         .depth().mapAll(this::wrap), noises
/* 215 */         .ridges().mapAll(this::wrap), spawnTarget);
/*     */   }
/*     */   
/*     */   protected BlockState getInterpolatedState() {
/* 221 */     return this.blockStateRule.calculate(this);
/*     */   }
/*     */   
/*     */   protected double getInterpolatedDensity() {
/* 225 */     return this.fullNoiseDensity.compute(this);
/*     */   }
/*     */   
/*     */   public int blockX() {
/* 230 */     return this.cellStartBlockX + this.inCellX;
/*     */   }
/*     */   
/*     */   public int blockY() {
/* 235 */     return this.cellStartBlockY + this.inCellY;
/*     */   }
/*     */   
/*     */   public int blockZ() {
/* 240 */     return this.cellStartBlockZ + this.inCellZ;
/*     */   }
/*     */   
/*     */   public int maxPreliminarySurfaceLevel(int minBlockX, int minBlockZ, int maxBlockX, int maxBlockZ) {
/* 244 */     int maxY = Integer.MIN_VALUE;
/* 245 */     for (int blockZ = minBlockZ; blockZ <= maxBlockZ; blockZ += 4) {
/* 246 */       for (int blockX = minBlockX; blockX <= maxBlockX; blockX += 4) {
/* 247 */         int surfaceLevel = preliminarySurfaceLevel(blockX, blockZ);
/* 248 */         if (surfaceLevel > maxY)
/* 249 */           maxY = surfaceLevel; 
/*     */       } 
/*     */     } 
/* 253 */     return maxY;
/*     */   }
/*     */   
/*     */   public int preliminarySurfaceLevel(int sampleX, int sampleZ) {
/* 259 */     int quantizedX = QuartPos.toBlock(QuartPos.fromBlock(sampleX));
/* 260 */     int quantizedZ = QuartPos.toBlock(QuartPos.fromBlock(sampleZ));
/* 261 */     return this.preliminarySurfaceLevelCache.computeIfAbsent(ColumnPos.asLong(quantizedX, quantizedZ), this::computePreliminarySurfaceLevel);
/*     */   }
/*     */   
/*     */   private int computePreliminarySurfaceLevel(long key) {
/* 265 */     int blockX = ColumnPos.getX(key);
/* 266 */     int blockZ = ColumnPos.getZ(key);
/* 268 */     return Mth.floor(this.preliminarySurfaceLevel.compute(new DensityFunction.SinglePointContext(blockX, 0, blockZ)));
/*     */   }
/*     */   
/*     */   private void fillSlice(boolean slice0, int cellX) {
/* 272 */     this.cellStartBlockX = cellX * this.cellWidth;
/* 274 */     this.inCellX = 0;
/* 276 */     for (int cellZIndex = 0; cellZIndex < this.cellCountXZ + 1; cellZIndex++) {
/* 277 */       int cellZ = this.firstCellZ + cellZIndex;
/* 278 */       this.cellStartBlockZ = cellZ * this.cellWidth;
/* 279 */       this.inCellZ = 0;
/* 281 */       this.arrayInterpolationCounter++;
/* 283 */       for (NoiseInterpolator noiseInterpolator : this.interpolators) {
/* 284 */         double[] slice = (slice0 ? noiseInterpolator.slice0 : noiseInterpolator.slice1)[cellZIndex];
/* 286 */         noiseInterpolator.fillArray(slice, this.sliceFillingContextProvider);
/*     */       } 
/*     */     } 
/* 289 */     this.arrayInterpolationCounter++;
/*     */   }
/*     */   
/*     */   public void initializeForFirstCellX() {
/* 294 */     if (this.interpolating)
/* 295 */       throw new IllegalStateException("Staring interpolation twice"); 
/* 297 */     this.interpolating = true;
/* 298 */     this.interpolationCounter = 0L;
/* 299 */     fillSlice(true, this.firstCellX);
/*     */   }
/*     */   
/*     */   public void advanceCellX(int cellXIndex) {
/* 308 */     fillSlice(false, this.firstCellX + cellXIndex + 1);
/* 309 */     this.cellStartBlockX = (this.firstCellX + cellXIndex) * this.cellWidth;
/*     */   }
/*     */   
/*     */   public NoiseChunk forIndex(int cellIndex) {
/* 315 */     int zInCell = Math.floorMod(cellIndex, this.cellWidth);
/* 316 */     int xyIndex = Math.floorDiv(cellIndex, this.cellWidth);
/* 318 */     int xInCell = Math.floorMod(xyIndex, this.cellWidth);
/* 319 */     int yInCell = this.cellHeight - 1 - Math.floorDiv(xyIndex, this.cellWidth);
/* 321 */     this.inCellX = xInCell;
/* 322 */     this.inCellY = yInCell;
/* 323 */     this.inCellZ = zInCell;
/* 325 */     this.arrayIndex = cellIndex;
/* 326 */     return this;
/*     */   }
/*     */   
/*     */   public void fillAllDirectly(double[] output, DensityFunction function) {
/* 332 */     this.arrayIndex = 0;
/* 333 */     for (int yInCell = this.cellHeight - 1; yInCell >= 0; yInCell--) {
/* 334 */       this.inCellY = yInCell;
/* 335 */       for (int xInCell = 0; xInCell < this.cellWidth; xInCell++) {
/* 336 */         this.inCellX = xInCell;
/* 337 */         for (int zInCell = 0; zInCell < this.cellWidth; zInCell++) {
/* 338 */           this.inCellZ = zInCell;
/* 339 */           output[this.arrayIndex++] = function.compute(this);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void selectCellYZ(int cellYIndex, int cellZIndex) {
/* 346 */     for (NoiseInterpolator i : this.interpolators)
/* 347 */       i.selectCellYZ(cellYIndex, cellZIndex); 
/* 350 */     this.fillingCell = true;
/* 351 */     this.cellStartBlockY = (cellYIndex + this.cellNoiseMinY) * this.cellHeight;
/* 352 */     this.cellStartBlockZ = (this.firstCellZ + cellZIndex) * this.cellWidth;
/* 354 */     this.arrayInterpolationCounter++;
/* 356 */     for (CacheAllInCell cellCache : this.cellCaches)
/* 357 */       cellCache.noiseFiller.fillArray(cellCache.values, this); 
/* 360 */     this.arrayInterpolationCounter++;
/* 361 */     this.fillingCell = false;
/*     */   }
/*     */   
/*     */   public void updateForY(int posY, double factorY) {
/* 365 */     this.inCellY = posY - this.cellStartBlockY;
/* 366 */     for (NoiseInterpolator i : this.interpolators)
/* 367 */       i.updateForY(factorY); 
/*     */   }
/*     */   
/*     */   public void updateForX(int posX, double factorX) {
/* 372 */     this.inCellX = posX - this.cellStartBlockX;
/* 373 */     for (NoiseInterpolator i : this.interpolators)
/* 374 */       i.updateForX(factorX); 
/*     */   }
/*     */   
/*     */   public void updateForZ(int posZ, double factorZ) {
/* 379 */     this.inCellZ = posZ - this.cellStartBlockZ;
/* 380 */     this.interpolationCounter++;
/* 381 */     for (NoiseInterpolator i : this.interpolators)
/* 382 */       i.updateForZ(factorZ); 
/*     */   }
/*     */   
/*     */   public void stopInterpolation() {
/* 387 */     if (!this.interpolating)
/* 388 */       throw new IllegalStateException("Staring interpolation twice"); 
/* 390 */     this.interpolating = false;
/*     */   }
/*     */   
/*     */   public void swapSlices() {
/* 394 */     this.interpolators.forEach(NoiseInterpolator::swapSlices);
/*     */   }
/*     */   
/*     */   public Aquifer aquifer() {
/* 398 */     return this.aquifer;
/*     */   }
/*     */   
/*     */   protected int cellWidth() {
/* 402 */     return this.cellWidth;
/*     */   }
/*     */   
/*     */   protected int cellHeight() {
/* 406 */     return this.cellHeight;
/*     */   }
/*     */   
/*     */   private static interface NoiseChunkDensityFunction extends DensityFunction {
/*     */     DensityFunction wrapped();
/*     */     
/*     */     default double minValue() {
/* 414 */       return wrapped().minValue();
/*     */     }
/*     */     
/*     */     default double maxValue() {
/* 419 */       return wrapped().maxValue();
/*     */     }
/*     */   }
/*     */   
/*     */   private class FlatCache implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */     private final DensityFunction noiseFiller;
/*     */     
/*     */     private final double[] values;
/*     */     
/*     */     private final int sizeXZ;
/*     */     
/*     */     private FlatCache(DensityFunction noiseFiller, boolean fill) {
/* 429 */       this.noiseFiller = noiseFiller;
/* 430 */       this.sizeXZ = NoiseChunk.this.noiseSizeXZ + 1;
/* 431 */       this.values = new double[this.sizeXZ * this.sizeXZ];
/* 432 */       if (fill)
/* 433 */         for (int x = 0; x <= NoiseChunk.this.noiseSizeXZ; x++) {
/* 434 */           int quartX = NoiseChunk.this.firstNoiseX + x;
/* 435 */           int blockX = QuartPos.toBlock(quartX);
/* 437 */           for (int z = 0; z <= NoiseChunk.this.noiseSizeXZ; z++) {
/* 438 */             int quartZ = NoiseChunk.this.firstNoiseZ + z;
/* 439 */             int blockZ = QuartPos.toBlock(quartZ);
/* 441 */             this.values[x + z * this.sizeXZ] = noiseFiller.compute(new DensityFunction.SinglePointContext(blockX, 0, blockZ));
/*     */           } 
/*     */         }  
/*     */     }
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 449 */       int quartX = QuartPos.fromBlock(context.blockX());
/* 450 */       int quartZ = QuartPos.fromBlock(context.blockZ());
/* 452 */       int x = quartX - NoiseChunk.this.firstNoiseX;
/* 453 */       int z = quartZ - NoiseChunk.this.firstNoiseZ;
/* 455 */       if (x >= 0 && z >= 0 && x < this.sizeXZ && z < this.sizeXZ)
/* 456 */         return this.values[x + z * this.sizeXZ]; 
/* 458 */       return this.noiseFiller.compute(context);
/*     */     }
/*     */     
/*     */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 464 */       contextProvider.fillAllDirectly(output, this);
/*     */     }
/*     */     
/*     */     public DensityFunction wrapped() {
/* 469 */       return this.noiseFiller;
/*     */     }
/*     */     
/*     */     public DensityFunctions.Marker.Type type() {
/* 474 */       return DensityFunctions.Marker.Type.FlatCache;
/*     */     }
/*     */   }
/*     */   
/*     */   private class CacheAllInCell implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */     private final DensityFunction noiseFiller;
/*     */     
/*     */     private final double[] values;
/*     */     
/*     */     private CacheAllInCell(DensityFunction noiseFiller) {
/* 483 */       this.noiseFiller = noiseFiller;
/* 484 */       this.values = new double[NoiseChunk.this.cellWidth * NoiseChunk.this.cellWidth * NoiseChunk.this.cellHeight];
/* 485 */       NoiseChunk.this.cellCaches.add(this);
/*     */     }
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 491 */       if (context != NoiseChunk.this)
/* 492 */         return this.noiseFiller.compute(context); 
/* 494 */       if (!NoiseChunk.this.interpolating)
/* 495 */         throw new IllegalStateException("Trying to sample interpolator outside the interpolation loop"); 
/* 497 */       int x = NoiseChunk.this.inCellX;
/* 498 */       int y = NoiseChunk.this.inCellY;
/* 499 */       int z = NoiseChunk.this.inCellZ;
/* 501 */       if (x >= 0 && y >= 0 && z >= 0 && x < NoiseChunk.this.cellWidth && y < NoiseChunk.this.cellHeight && z < NoiseChunk.this.cellWidth)
/* 502 */         return this.values[((NoiseChunk.this.cellHeight - 1 - y) * NoiseChunk.this.cellWidth + x) * NoiseChunk.this.cellWidth + z]; 
/* 504 */       return this.noiseFiller.compute(context);
/*     */     }
/*     */     
/*     */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 510 */       contextProvider.fillAllDirectly(output, this);
/*     */     }
/*     */     
/*     */     public DensityFunction wrapped() {
/* 515 */       return this.noiseFiller;
/*     */     }
/*     */     
/*     */     public DensityFunctions.Marker.Type type() {
/* 520 */       return DensityFunctions.Marker.Type.CacheAllInCell;
/*     */     }
/*     */   }
/*     */   
/*     */   public class NoiseInterpolator implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */     private double[][] slice0;
/*     */     
/*     */     private double[][] slice1;
/*     */     
/*     */     private final DensityFunction noiseFiller;
/*     */     
/*     */     private double noise000;
/*     */     
/*     */     private double noise001;
/*     */     
/*     */     private double noise100;
/*     */     
/*     */     private double noise101;
/*     */     
/*     */     private double noise010;
/*     */     
/*     */     private double noise011;
/*     */     
/*     */     private double noise110;
/*     */     
/*     */     private double noise111;
/*     */     
/*     */     private double valueXZ00;
/*     */     
/*     */     private double valueXZ10;
/*     */     
/*     */     private double valueXZ01;
/*     */     
/*     */     private double valueXZ11;
/*     */     
/*     */     private double valueZ0;
/*     */     
/*     */     private double valueZ1;
/*     */     
/*     */     private double value;
/*     */     
/*     */     private NoiseInterpolator(DensityFunction noiseFiller) {
/* 558 */       this.noiseFiller = noiseFiller;
/* 559 */       this.slice0 = allocateSlice(NoiseChunk.this.cellCountY, NoiseChunk.this.cellCountXZ);
/* 560 */       this.slice1 = allocateSlice(NoiseChunk.this.cellCountY, NoiseChunk.this.cellCountXZ);
/* 562 */       NoiseChunk.this.interpolators.add(this);
/*     */     }
/*     */     
/*     */     private double[][] allocateSlice(int cellCountY, int cellCountZ) {
/* 566 */       int sizeZ = cellCountZ + 1;
/* 567 */       int sizeY = cellCountY + 1;
/* 568 */       double[][] result = new double[sizeZ][sizeY];
/* 569 */       for (int cellZIndex = 0; cellZIndex < sizeZ; cellZIndex++)
/* 570 */         result[cellZIndex] = new double[sizeY]; 
/* 572 */       return result;
/*     */     }
/*     */     
/*     */     private void selectCellYZ(int cellYIndex, int cellZIndex) {
/* 580 */       this.noise000 = this.slice0[cellZIndex][cellYIndex];
/* 581 */       this.noise001 = this.slice0[cellZIndex + 1][cellYIndex];
/* 582 */       this.noise100 = this.slice1[cellZIndex][cellYIndex];
/* 583 */       this.noise101 = this.slice1[cellZIndex + 1][cellYIndex];
/* 585 */       this.noise010 = this.slice0[cellZIndex][cellYIndex + 1];
/* 586 */       this.noise011 = this.slice0[cellZIndex + 1][cellYIndex + 1];
/* 587 */       this.noise110 = this.slice1[cellZIndex][cellYIndex + 1];
/* 588 */       this.noise111 = this.slice1[cellZIndex + 1][cellYIndex + 1];
/*     */     }
/*     */     
/*     */     private void updateForY(double factorY) {
/* 595 */       this.valueXZ00 = Mth.lerp(factorY, this.noise000, this.noise010);
/* 596 */       this.valueXZ10 = Mth.lerp(factorY, this.noise100, this.noise110);
/* 597 */       this.valueXZ01 = Mth.lerp(factorY, this.noise001, this.noise011);
/* 598 */       this.valueXZ11 = Mth.lerp(factorY, this.noise101, this.noise111);
/*     */     }
/*     */     
/*     */     private void updateForX(double factorX) {
/* 605 */       this.valueZ0 = Mth.lerp(factorX, this.valueXZ00, this.valueXZ10);
/* 606 */       this.valueZ1 = Mth.lerp(factorX, this.valueXZ01, this.valueXZ11);
/*     */     }
/*     */     
/*     */     private void updateForZ(double factorZ) {
/* 613 */       this.value = Mth.lerp(factorZ, this.valueZ0, this.valueZ1);
/*     */     }
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 618 */       if (context != NoiseChunk.this)
/* 619 */         return this.noiseFiller.compute(context); 
/* 621 */       if (!NoiseChunk.this.interpolating)
/* 622 */         throw new IllegalStateException("Trying to sample interpolator outside the interpolation loop"); 
/* 624 */       if (NoiseChunk.this.fillingCell)
/* 625 */         return Mth.lerp3(NoiseChunk.this.inCellX / NoiseChunk.this.cellWidth, NoiseChunk.this.inCellY / NoiseChunk.this.cellHeight, NoiseChunk.this.inCellZ / NoiseChunk.this.cellWidth, this.noise000, this.noise100, this.noise010, this.noise110, this.noise001, this.noise101, this.noise011, this.noise111); 
/* 633 */       return this.value;
/*     */     }
/*     */     
/*     */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 638 */       if (NoiseChunk.this.fillingCell) {
/* 640 */         contextProvider.fillAllDirectly(output, this);
/*     */         return;
/*     */       } 
/* 643 */       wrapped().fillArray(output, contextProvider);
/*     */     }
/*     */     
/*     */     public DensityFunction wrapped() {
/* 648 */       return this.noiseFiller;
/*     */     }
/*     */     
/*     */     private void swapSlices() {
/* 653 */       double[][] tmp = this.slice0;
/* 654 */       this.slice0 = this.slice1;
/* 655 */       this.slice1 = tmp;
/*     */     }
/*     */     
/*     */     public DensityFunctions.Marker.Type type() {
/* 660 */       return DensityFunctions.Marker.Type.Interpolated;
/*     */     }
/*     */   }
/*     */   
/*     */   private class CacheOnce implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */     private final DensityFunction function;
/*     */     
/*     */     private long lastCounter;
/*     */     
/*     */     private long lastArrayCounter;
/*     */     
/*     */     private double lastValue;
/*     */     
/*     */     private double[] lastArray;
/*     */     
/*     */     private CacheOnce(DensityFunction function) {
/* 673 */       this.function = function;
/*     */     }
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 678 */       if (context != NoiseChunk.this)
/* 679 */         return this.function.compute(context); 
/* 681 */       if (this.lastArray != null && this.lastArrayCounter == NoiseChunk.this.arrayInterpolationCounter)
/* 682 */         return this.lastArray[NoiseChunk.this.arrayIndex]; 
/* 684 */       if (this.lastCounter == NoiseChunk.this.interpolationCounter)
/* 685 */         return this.lastValue; 
/* 687 */       this.lastCounter = NoiseChunk.this.interpolationCounter;
/* 688 */       double value = this.function.compute(context);
/* 689 */       this.lastValue = value;
/* 690 */       return value;
/*     */     }
/*     */     
/*     */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 695 */       if (this.lastArray != null && this.lastArrayCounter == NoiseChunk.this.arrayInterpolationCounter) {
/* 696 */         System.arraycopy(this.lastArray, 0, output, 0, output.length);
/*     */         return;
/*     */       } 
/* 699 */       wrapped().fillArray(output, contextProvider);
/* 700 */       if (this.lastArray != null && this.lastArray.length == output.length) {
/* 701 */         System.arraycopy(output, 0, this.lastArray, 0, output.length);
/*     */       } else {
/* 703 */         this.lastArray = (double[])output.clone();
/*     */       } 
/* 705 */       this.lastArrayCounter = NoiseChunk.this.arrayInterpolationCounter;
/*     */     }
/*     */     
/*     */     public DensityFunction wrapped() {
/* 710 */       return this.function;
/*     */     }
/*     */     
/*     */     public DensityFunctions.Marker.Type type() {
/* 715 */       return DensityFunctions.Marker.Type.CacheOnce;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Cache2D implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */     private final DensityFunction function;
/*     */     
/* 722 */     private long lastPos2D = ChunkPos.INVALID_CHUNK_POS;
/*     */     
/*     */     private double lastValue;
/*     */     
/*     */     private Cache2D(DensityFunction function) {
/* 726 */       this.function = function;
/*     */     }
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 731 */       int blockX = context.blockX();
/* 732 */       int blockZ = context.blockZ();
/* 733 */       long pos2D = ChunkPos.pack(blockX, blockZ);
/* 734 */       if (this.lastPos2D == pos2D)
/* 735 */         return this.lastValue; 
/* 737 */       this.lastPos2D = pos2D;
/* 738 */       double value = this.function.compute(context);
/* 739 */       this.lastValue = value;
/* 740 */       return value;
/*     */     }
/*     */     
/*     */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 745 */       this.function.fillArray(output, contextProvider);
/*     */     }
/*     */     
/*     */     public DensityFunction wrapped() {
/* 750 */       return this.function;
/*     */     }
/*     */     
/*     */     public DensityFunctions.Marker.Type type() {
/* 755 */       return DensityFunctions.Marker.Type.Cache2D;
/*     */     }
/*     */   }
/*     */   
/*     */   private Blender.BlendingOutput getOrComputeBlendingOutput(int blockX, int blockZ) {
/* 760 */     long pos2D = ChunkPos.pack(blockX, blockZ);
/* 761 */     if (this.lastBlendingDataPos == pos2D)
/* 762 */       return this.lastBlendingOutput; 
/* 764 */     this.lastBlendingDataPos = pos2D;
/* 765 */     Blender.BlendingOutput output = this.blender.blendOffsetAndFactor(blockX, blockZ);
/* 766 */     this.lastBlendingOutput = output;
/* 767 */     return output;
/*     */   }
/*     */   
/*     */   private class BlendAlpha implements NoiseChunkDensityFunction {
/*     */     public DensityFunction wrapped() {
/* 773 */       return DensityFunctions.BlendAlpha.INSTANCE;
/*     */     }
/*     */     
/*     */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 778 */       return visitor.apply(wrapped());
/*     */     }
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 783 */       return NoiseChunk.this.getOrComputeBlendingOutput(context.blockX(), context.blockZ()).alpha();
/*     */     }
/*     */     
/*     */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 789 */       contextProvider.fillAllDirectly(output, this);
/*     */     }
/*     */     
/*     */     public double minValue() {
/* 794 */       return 0.0D;
/*     */     }
/*     */     
/*     */     public double maxValue() {
/* 799 */       return 1.0D;
/*     */     }
/*     */     
/*     */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 804 */       return DensityFunctions.BlendAlpha.CODEC;
/*     */     }
/*     */   }
/*     */   
/*     */   private class BlendOffset implements NoiseChunkDensityFunction {
/*     */     public DensityFunction wrapped() {
/* 811 */       return DensityFunctions.BlendOffset.INSTANCE;
/*     */     }
/*     */     
/*     */     public DensityFunction mapChildren(DensityFunction.Visitor visitor) {
/* 816 */       return visitor.apply(wrapped());
/*     */     }
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 821 */       return NoiseChunk.this.getOrComputeBlendingOutput(context.blockX(), context.blockZ()).blendingOffset();
/*     */     }
/*     */     
/*     */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 827 */       contextProvider.fillAllDirectly(output, this);
/*     */     }
/*     */     
/*     */     public double minValue() {
/* 833 */       return Double.NEGATIVE_INFINITY;
/*     */     }
/*     */     
/*     */     public double maxValue() {
/* 839 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*     */     
/*     */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 844 */       return DensityFunctions.BlendOffset.CODEC;
/*     */     }
/*     */   }
/*     */   
/*     */   private class BlendDensity implements NoiseChunkDensityFunction, DensityFunctions.MarkerOrMarked {
/*     */     private final DensityFunction input;
/*     */     
/*     */     private BlendDensity(DensityFunction input) {
/* 852 */       this.input = input;
/*     */     }
/*     */     
/*     */     public double compute(DensityFunction.FunctionContext context) {
/* 857 */       return NoiseChunk.this.blender.blendDensity(context, this.input.compute(context));
/*     */     }
/*     */     
/*     */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 862 */       this.input.fillArray(output, contextProvider);
/* 863 */       for (int i = 0; i < output.length; i++) {
/* 864 */         DensityFunction.FunctionContext context = contextProvider.forIndex(i);
/* 865 */         output[i] = NoiseChunk.this.blender.blendDensity(context, output[i]);
/*     */       } 
/*     */     }
/*     */     
/*     */     public double minValue() {
/* 872 */       return Double.NEGATIVE_INFINITY;
/*     */     }
/*     */     
/*     */     public double maxValue() {
/* 878 */       return Double.POSITIVE_INFINITY;
/*     */     }
/*     */     
/*     */     public DensityFunction wrapped() {
/* 883 */       return this.input;
/*     */     }
/*     */     
/*     */     public DensityFunctions.Marker.Type type() {
/* 888 */       return DensityFunctions.Marker.Type.BlendDensity;
/*     */     }
/*     */   }
/*     */   
/*     */   protected DensityFunction wrap(DensityFunction function) {
/* 898 */     return this.wrapped.computeIfAbsent(function, this::wrapNew);
/*     */   }
/*     */   
/*     */   private DensityFunction wrapNew(DensityFunction function) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_2
/*     */     //   7: iconst_0
/*     */     //   8: istore_3
/*     */     //   9: aload_2
/*     */     //   10: iload_3
/*     */     //   11: <illegal opcode> typeSwitch : (Lnet/minecraft/world/level/levelgen/DensityFunction;I)I
/*     */     //   16: lookupswitch default -> 253, 0 -> 44, 1 -> 223
/*     */     //   44: aload_2
/*     */     //   45: checkcast net/minecraft/world/level/levelgen/DensityFunctions$Marker
/*     */     //   48: astore #4
/*     */     //   50: aload #4
/*     */     //   52: invokevirtual type : ()Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker$Type;
/*     */     //   55: astore #7
/*     */     //   57: aload #7
/*     */     //   59: astore #5
/*     */     //   61: aload #4
/*     */     //   63: invokevirtual wrapped : ()Lnet/minecraft/world/level/levelgen/DensityFunction;
/*     */     //   66: astore #7
/*     */     //   68: aload #7
/*     */     //   70: astore #6
/*     */     //   72: getstatic net/minecraft/world/level/levelgen/NoiseChunk$2.$SwitchMap$net$minecraft$world$level$levelgen$DensityFunctions$Marker$Type : [I
/*     */     //   75: aload #5
/*     */     //   77: invokevirtual ordinal : ()I
/*     */     //   80: iaload
/*     */     //   81: tableswitch default -> 120, 1 -> 130, 2 -> 143, 3 -> 157, 4 -> 169, 5 -> 182, 6 -> 195
/*     */     //   120: new java/lang/MatchException
/*     */     //   123: dup
/*     */     //   124: aconst_null
/*     */     //   125: aconst_null
/*     */     //   126: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   129: athrow
/*     */     //   130: new net/minecraft/world/level/levelgen/NoiseChunk$NoiseInterpolator
/*     */     //   133: dup
/*     */     //   134: aload_0
/*     */     //   135: aload #6
/*     */     //   137: invokespecial <init> : (Lnet/minecraft/world/level/levelgen/NoiseChunk;Lnet/minecraft/world/level/levelgen/DensityFunction;)V
/*     */     //   140: goto -> 310
/*     */     //   143: new net/minecraft/world/level/levelgen/NoiseChunk$FlatCache
/*     */     //   146: dup
/*     */     //   147: aload_0
/*     */     //   148: aload #6
/*     */     //   150: iconst_1
/*     */     //   151: invokespecial <init> : (Lnet/minecraft/world/level/levelgen/NoiseChunk;Lnet/minecraft/world/level/levelgen/DensityFunction;Z)V
/*     */     //   154: goto -> 310
/*     */     //   157: new net/minecraft/world/level/levelgen/NoiseChunk$Cache2D
/*     */     //   160: dup
/*     */     //   161: aload #6
/*     */     //   163: invokespecial <init> : (Lnet/minecraft/world/level/levelgen/DensityFunction;)V
/*     */     //   166: goto -> 310
/*     */     //   169: new net/minecraft/world/level/levelgen/NoiseChunk$CacheOnce
/*     */     //   172: dup
/*     */     //   173: aload_0
/*     */     //   174: aload #6
/*     */     //   176: invokespecial <init> : (Lnet/minecraft/world/level/levelgen/NoiseChunk;Lnet/minecraft/world/level/levelgen/DensityFunction;)V
/*     */     //   179: goto -> 310
/*     */     //   182: new net/minecraft/world/level/levelgen/NoiseChunk$CacheAllInCell
/*     */     //   185: dup
/*     */     //   186: aload_0
/*     */     //   187: aload #6
/*     */     //   189: invokespecial <init> : (Lnet/minecraft/world/level/levelgen/NoiseChunk;Lnet/minecraft/world/level/levelgen/DensityFunction;)V
/*     */     //   192: goto -> 310
/*     */     //   195: aload_0
/*     */     //   196: getfield blender : Lnet/minecraft/world/level/levelgen/blending/Blender;
/*     */     //   199: invokevirtual isEmpty : ()Z
/*     */     //   202: ifne -> 218
/*     */     //   205: new net/minecraft/world/level/levelgen/NoiseChunk$BlendDensity
/*     */     //   208: dup
/*     */     //   209: aload_0
/*     */     //   210: aload #6
/*     */     //   212: invokespecial <init> : (Lnet/minecraft/world/level/levelgen/NoiseChunk;Lnet/minecraft/world/level/levelgen/DensityFunction;)V
/*     */     //   215: goto -> 310
/*     */     //   218: aload #6
/*     */     //   220: goto -> 310
/*     */     //   223: aload_2
/*     */     //   224: checkcast net/minecraft/world/level/levelgen/DensityFunctions$HolderHolder
/*     */     //   227: astore #7
/*     */     //   229: aload #7
/*     */     //   231: invokevirtual function : ()Lnet/minecraft/core/Holder;
/*     */     //   234: astore #9
/*     */     //   236: aload #9
/*     */     //   238: astore #8
/*     */     //   240: aload #8
/*     */     //   242: invokeinterface value : ()Ljava/lang/Object;
/*     */     //   247: checkcast net/minecraft/world/level/levelgen/DensityFunction
/*     */     //   250: goto -> 310
/*     */     //   253: aload_1
/*     */     //   254: getstatic net/minecraft/world/level/levelgen/DensityFunctions$BlendAlpha.INSTANCE : Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendAlpha;
/*     */     //   257: if_acmpne -> 274
/*     */     //   260: aload_0
/*     */     //   261: getfield blendAlpha : Lnet/minecraft/world/level/levelgen/NoiseChunk$FlatCache;
/*     */     //   264: ifnull -> 274
/*     */     //   267: aload_0
/*     */     //   268: getfield blendAlpha : Lnet/minecraft/world/level/levelgen/NoiseChunk$FlatCache;
/*     */     //   271: goto -> 310
/*     */     //   274: aload_1
/*     */     //   275: getstatic net/minecraft/world/level/levelgen/DensityFunctions$BlendOffset.INSTANCE : Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendOffset;
/*     */     //   278: if_acmpne -> 295
/*     */     //   281: aload_0
/*     */     //   282: getfield blendOffset : Lnet/minecraft/world/level/levelgen/NoiseChunk$FlatCache;
/*     */     //   285: ifnull -> 295
/*     */     //   288: aload_0
/*     */     //   289: getfield blendOffset : Lnet/minecraft/world/level/levelgen/NoiseChunk$FlatCache;
/*     */     //   292: goto -> 310
/*     */     //   295: aload_1
/*     */     //   296: getstatic net/minecraft/world/level/levelgen/DensityFunctions$BeardifierMarker.INSTANCE : Lnet/minecraft/world/level/levelgen/DensityFunctions$BeardifierMarker;
/*     */     //   299: if_acmpne -> 309
/*     */     //   302: aload_0
/*     */     //   303: getfield beardifier : Lnet/minecraft/world/level/levelgen/DensityFunctions$BeardifierOrMarker;
/*     */     //   306: goto -> 310
/*     */     //   309: aload_1
/*     */     //   310: areturn
/*     */     //   311: astore_2
/*     */     //   312: new java/lang/MatchException
/*     */     //   315: dup
/*     */     //   316: aload_2
/*     */     //   317: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   320: aload_2
/*     */     //   321: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   324: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #902	-> 0
/*     */     //   #903	-> 44
/*     */     //   #904	-> 72
/*     */     //   #905	-> 130
/*     */     //   #906	-> 143
/*     */     //   #907	-> 157
/*     */     //   #908	-> 169
/*     */     //   #909	-> 182
/*     */     //   #910	-> 195
/*     */     //   #912	-> 223
/*     */     //   #914	-> 253
/*     */     //   #915	-> 267
/*     */     //   #916	-> 274
/*     */     //   #917	-> 288
/*     */     //   #918	-> 295
/*     */     //   #919	-> 302
/*     */     //   #921	-> 309
/*     */     //   #902	-> 310
/*     */     //   #912	-> 311
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   61	162	5	type	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker$Type;
/*     */     //   72	151	6	wrapped	Lnet/minecraft/world/level/levelgen/DensityFunction;
/*     */     //   240	13	8	holder	Lnet/minecraft/core/Holder;
/*     */     //   0	325	0	this	Lnet/minecraft/world/level/levelgen/NoiseChunk;
/*     */     //   0	325	1	function	Lnet/minecraft/world/level/levelgen/DensityFunction;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   240	13	8	holder	Lnet/minecraft/core/Holder<Lnet/minecraft/world/level/levelgen/DensityFunction;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   52	55	311	java/lang/Throwable
/*     */     //   63	66	311	java/lang/Throwable
/*     */     //   231	234	311	java/lang/Throwable
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface BlockStateFiller {
/*     */     BlockState calculate(DensityFunction.FunctionContext param1FunctionContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\NoiseChunk.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */