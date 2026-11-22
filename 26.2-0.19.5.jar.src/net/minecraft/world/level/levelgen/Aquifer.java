/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.OverworldBiomeBuilder;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import org.apache.commons.lang3.mutable.MutableDouble;
/*     */ 
/*     */ public interface Aquifer {
/*     */   static Aquifer create(NoiseChunk noiseChunk, ChunkPos pos, NoiseRouter router, PositionalRandomFactory positionalRandomFactory, int minBlockY, int yBlockSize, FluidPicker fluidRule) {
/*  49 */     return new NoiseBasedAquifer(noiseChunk, pos, router, positionalRandomFactory, minBlockY, yBlockSize, fluidRule);
/*     */   }
/*     */   
/*     */   static Aquifer createDisabled(final FluidPicker fluidRule) {
/*  53 */     return new Aquifer() {
/*     */         public BlockState computeSubstance(DensityFunction.FunctionContext context, double density) {
/*  56 */           if (density > 0.0D)
/*  57 */             return null; 
/*  59 */           return fluidRule.computeFluid(context.blockX(), context.blockY(), context.blockZ()).at(context.blockY());
/*     */         }
/*     */         
/*     */         public boolean shouldScheduleFluidUpdate() {
/*  64 */           return false;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   BlockState computeSubstance(DensityFunction.FunctionContext paramFunctionContext, double paramDouble);
/*     */   
/*     */   boolean shouldScheduleFluidUpdate();
/*     */   
/*     */   public static class NoiseBasedAquifer implements Aquifer {
/*     */     private static final int X_RANGE = 10;
/*     */     
/*     */     private static final int Y_RANGE = 9;
/*     */     
/*     */     private static final int Z_RANGE = 10;
/*     */     
/*     */     private static final int X_SEPARATION = 6;
/*     */     
/*     */     private static final int Y_SEPARATION = 3;
/*     */     
/*     */     private static final int Z_SEPARATION = 6;
/*     */     
/*     */     private static final int X_SPACING = 16;
/*     */     
/*     */     private static final int Y_SPACING = 12;
/*     */     
/*     */     private static final int Z_SPACING = 16;
/*     */     
/*     */     private static final int X_SPACING_SHIFT = 4;
/*     */     
/*     */     private static final int Z_SPACING_SHIFT = 4;
/*     */     
/*     */     private static final int MAX_REASONABLE_DISTANCE_TO_AQUIFER_CENTER = 11;
/*     */     
/*  92 */     private static final double FLOWING_UPDATE_SIMULARITY = similarity(
/*  93 */         Mth.square(10), 
/*  94 */         Mth.square(12));
/*     */     
/*     */     private static final int SAMPLE_OFFSET_X = -5;
/*     */     
/*     */     private static final int SAMPLE_OFFSET_Y = 1;
/*     */     
/*     */     private static final int SAMPLE_OFFSET_Z = -5;
/*     */     
/*     */     private static final int MIN_CELL_SAMPLE_X = 0;
/*     */     
/*     */     private static final int MIN_CELL_SAMPLE_Y = -1;
/*     */     
/*     */     private static final int MIN_CELL_SAMPLE_Z = 0;
/*     */     
/*     */     private static final int MAX_CELL_SAMPLE_X = 1;
/*     */     
/*     */     private static final int MAX_CELL_SAMPLE_Y = 1;
/*     */     
/*     */     private static final int MAX_CELL_SAMPLE_Z = 1;
/*     */     
/*     */     private final NoiseChunk noiseChunk;
/*     */     
/*     */     private final DensityFunction barrierNoise;
/*     */     
/*     */     private final DensityFunction fluidLevelFloodednessNoise;
/*     */     
/*     */     private final DensityFunction fluidLevelSpreadNoise;
/*     */     
/*     */     private final DensityFunction lavaNoise;
/*     */     
/*     */     private final PositionalRandomFactory positionalRandomFactory;
/*     */     
/*     */     private final Aquifer.FluidStatus[] aquiferCache;
/*     */     
/*     */     private final long[] aquiferLocationCache;
/*     */     
/*     */     private final Aquifer.FluidPicker globalFluidPicker;
/*     */     
/*     */     private final DensityFunction erosion;
/*     */     
/*     */     private final DensityFunction depth;
/*     */     
/*     */     private boolean shouldScheduleFluidUpdate;
/*     */     
/*     */     private final int skipSamplingAboveY;
/*     */     
/*     */     private final int minGridX;
/*     */     
/*     */     private final int minGridY;
/*     */     
/*     */     private final int minGridZ;
/*     */     
/*     */     private final int gridSizeX;
/*     */     
/*     */     private final int gridSizeZ;
/*     */     
/* 151 */     private static final int[][] SURFACE_SAMPLING_OFFSETS_IN_CHUNKS = new int[][] { 
/* 151 */         { 0, 0 }, { -2, -1 }, { -1, -1 }, { 0, -1 }, { 1, -1 }, { -3, 0 }, { -2, 0 }, { -1, 0 }, { 1, 0 }, { -2, 1 }, 
/* 151 */         { -1, 1 }, { 0, 1 }, { 1, 1 } };
/*     */     
/*     */     private NoiseBasedAquifer(NoiseChunk noiseChunk, ChunkPos pos, NoiseRouter router, PositionalRandomFactory positionalRandomFactory, int minBlockY, int yBlockSize, Aquifer.FluidPicker globalFluidPicker) {
/* 159 */       this.noiseChunk = noiseChunk;
/* 160 */       this.barrierNoise = router.barrierNoise();
/* 161 */       this.fluidLevelFloodednessNoise = router.fluidLevelFloodednessNoise();
/* 162 */       this.fluidLevelSpreadNoise = router.fluidLevelSpreadNoise();
/* 163 */       this.lavaNoise = router.lavaNoise();
/* 164 */       this.erosion = router.erosion();
/* 165 */       this.depth = router.depth();
/* 167 */       this.positionalRandomFactory = positionalRandomFactory;
/* 169 */       this.minGridX = gridX(pos.getMinBlockX() + -5) + 0;
/* 170 */       this.globalFluidPicker = globalFluidPicker;
/* 171 */       int maxGridX = gridX(pos.getMaxBlockX() + -5) + 1;
/* 172 */       this.gridSizeX = maxGridX - this.minGridX + 1;
/* 174 */       this.minGridY = gridY(minBlockY + 1) + -1;
/* 175 */       int maxGridY = gridY(minBlockY + yBlockSize + 1) + 1;
/* 176 */       int gridSizeY = maxGridY - this.minGridY + 1;
/* 178 */       this.minGridZ = gridZ(pos.getMinBlockZ() + -5) + 0;
/* 179 */       int maxGridZ = gridZ(pos.getMaxBlockZ() + -5) + 1;
/* 180 */       this.gridSizeZ = maxGridZ - this.minGridZ + 1;
/* 181 */       int totalGridSize = this.gridSizeX * gridSizeY * this.gridSizeZ;
/* 183 */       this.aquiferCache = new Aquifer.FluidStatus[totalGridSize];
/* 185 */       this.aquiferLocationCache = new long[totalGridSize];
/* 186 */       Arrays.fill(this.aquiferLocationCache, Long.MAX_VALUE);
/* 188 */       int maxAdjustedSurfaceLevel = adjustSurfaceLevel(noiseChunk.maxPreliminarySurfaceLevel(
/* 189 */             fromGridX(this.minGridX, 0), 
/* 190 */             fromGridZ(this.minGridZ, 0), 
/* 191 */             fromGridX(maxGridX, 9), 
/* 192 */             fromGridZ(maxGridZ, 9)));
/* 194 */       int skipSamplingAboveGridY = gridY(maxAdjustedSurfaceLevel + 12) - -1;
/* 195 */       this.skipSamplingAboveY = fromGridY(skipSamplingAboveGridY, 11) - 1;
/*     */     }
/*     */     
/*     */     private int getIndex(int gridX, int gridY, int gridZ) {
/* 199 */       int x = gridX - this.minGridX;
/* 200 */       int y = gridY - this.minGridY;
/* 201 */       int z = gridZ - this.minGridZ;
/* 203 */       return (y * this.gridSizeZ + z) * this.gridSizeX + x;
/*     */     }
/*     */     
/*     */     public BlockState computeSubstance(DensityFunction.FunctionContext context, double density) {
/* 209 */       if (density > 0.0D) {
/* 210 */         this.shouldScheduleFluidUpdate = false;
/* 211 */         return null;
/*     */       } 
/* 214 */       int posX = context.blockX();
/* 215 */       int posY = context.blockY();
/* 216 */       int posZ = context.blockZ();
/* 218 */       Aquifer.FluidStatus globalFluid = this.globalFluidPicker.computeFluid(posX, posY, posZ);
/* 221 */       if (posY > this.skipSamplingAboveY) {
/* 222 */         this.shouldScheduleFluidUpdate = false;
/* 223 */         return globalFluid.at(posY);
/*     */       } 
/* 226 */       if (globalFluid.at(posY).is(Blocks.LAVA)) {
/* 227 */         this.shouldScheduleFluidUpdate = false;
/* 228 */         return SharedConstants.DEBUG_DISABLE_FLUID_GENERATION ? Blocks.AIR.defaultBlockState() : Blocks.LAVA.defaultBlockState();
/*     */       } 
/* 231 */       int xAnchor = gridX(posX + -5);
/* 232 */       int yAnchor = gridY(posY + 1);
/* 233 */       int zAnchor = gridZ(posZ + -5);
/* 236 */       int distanceSqr1 = Integer.MAX_VALUE;
/* 237 */       int distanceSqr2 = Integer.MAX_VALUE;
/* 238 */       int distanceSqr3 = Integer.MAX_VALUE;
/* 239 */       int distanceSqr4 = Integer.MAX_VALUE;
/* 241 */       int closestIndex1 = 0;
/* 242 */       int closestIndex2 = 0;
/* 243 */       int closestIndex3 = 0;
/* 244 */       int closestIndex4 = 0;
/* 246 */       for (int x1 = 0; x1 <= 1; x1++) {
/* 247 */         for (int y1 = -1; y1 <= 1; y1++) {
/* 248 */           for (int z1 = 0; z1 <= 1; z1++) {
/*     */             long location;
/* 249 */             int spacedGridX = xAnchor + x1;
/* 250 */             int spacedGridY = yAnchor + y1;
/* 251 */             int spacedGridZ = zAnchor + z1;
/* 253 */             int index = getIndex(spacedGridX, spacedGridY, spacedGridZ);
/* 256 */             long existingLocation = this.aquiferLocationCache[index];
/* 257 */             if (existingLocation != Long.MAX_VALUE) {
/* 258 */               location = existingLocation;
/*     */             } else {
/* 260 */               RandomSource random = this.positionalRandomFactory.at(spacedGridX, spacedGridY, spacedGridZ);
/* 262 */               location = BlockPos.asLong(
/* 263 */                   fromGridX(spacedGridX, random.nextInt(10)), 
/* 264 */                   fromGridY(spacedGridY, random.nextInt(9)), 
/* 265 */                   fromGridZ(spacedGridZ, random.nextInt(10)));
/* 267 */               this.aquiferLocationCache[index] = location;
/*     */             } 
/* 270 */             int dx = BlockPos.getX(location) - posX;
/* 271 */             int dy = BlockPos.getY(location) - posY;
/* 272 */             int dz = BlockPos.getZ(location) - posZ;
/* 273 */             int newDistance = dx * dx + dy * dy + dz * dz;
/* 276 */             if (distanceSqr1 >= newDistance) {
/* 277 */               closestIndex4 = closestIndex3;
/* 278 */               closestIndex3 = closestIndex2;
/* 279 */               closestIndex2 = closestIndex1;
/* 280 */               closestIndex1 = index;
/* 282 */               distanceSqr4 = distanceSqr3;
/* 283 */               distanceSqr3 = distanceSqr2;
/* 284 */               distanceSqr2 = distanceSqr1;
/* 285 */               distanceSqr1 = newDistance;
/* 286 */             } else if (distanceSqr2 >= newDistance) {
/* 287 */               closestIndex4 = closestIndex3;
/* 288 */               closestIndex3 = closestIndex2;
/* 289 */               closestIndex2 = index;
/* 291 */               distanceSqr4 = distanceSqr3;
/* 292 */               distanceSqr3 = distanceSqr2;
/* 293 */               distanceSqr2 = newDistance;
/* 294 */             } else if (distanceSqr3 >= newDistance) {
/* 295 */               closestIndex4 = closestIndex3;
/* 296 */               closestIndex3 = index;
/* 298 */               distanceSqr4 = distanceSqr3;
/* 299 */               distanceSqr3 = newDistance;
/* 300 */             } else if (distanceSqr4 >= newDistance) {
/* 301 */               closestIndex4 = index;
/* 303 */               distanceSqr4 = newDistance;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 314 */       Aquifer.FluidStatus closestStatus1 = getAquiferStatus(closestIndex1);
/* 316 */       double similarity12 = similarity(distanceSqr1, distanceSqr2);
/* 318 */       BlockState fluidState = closestStatus1.at(posY);
/* 319 */       BlockState actualFluidState = SharedConstants.DEBUG_DISABLE_FLUID_GENERATION ? Blocks.AIR.defaultBlockState() : fluidState;
/* 323 */       if (similarity12 <= 0.0D) {
/* 324 */         if (similarity12 >= FLOWING_UPDATE_SIMULARITY) {
/* 325 */           Aquifer.FluidStatus fluidStatus = getAquiferStatus(closestIndex2);
/* 326 */           this.shouldScheduleFluidUpdate = !closestStatus1.equals(fluidStatus);
/*     */         } else {
/* 328 */           this.shouldScheduleFluidUpdate = false;
/*     */         } 
/* 330 */         return actualFluidState;
/*     */       } 
/* 335 */       if (fluidState.is(Blocks.WATER) && this.globalFluidPicker.computeFluid(posX, posY - 1, posZ).at(posY - 1).is(Blocks.LAVA)) {
/* 336 */         this.shouldScheduleFluidUpdate = true;
/* 337 */         return actualFluidState;
/*     */       } 
/* 354 */       MutableDouble barrierNoiseValue = new MutableDouble(Double.NaN);
/* 355 */       Aquifer.FluidStatus closestStatus2 = getAquiferStatus(closestIndex2);
/* 358 */       double barrier12 = similarity12 * calculatePressure(context, barrierNoiseValue, closestStatus1, closestStatus2);
/* 359 */       if (density + barrier12 > 0.0D) {
/* 360 */         this.shouldScheduleFluidUpdate = false;
/* 361 */         return null;
/*     */       } 
/* 364 */       Aquifer.FluidStatus closestStatus3 = getAquiferStatus(closestIndex3);
/* 366 */       double similarity13 = similarity(distanceSqr1, distanceSqr3);
/* 367 */       if (similarity13 > 0.0D) {
/* 369 */         double barrier13 = similarity12 * similarity13 * calculatePressure(context, barrierNoiseValue, closestStatus1, closestStatus3);
/* 370 */         if (density + barrier13 > 0.0D) {
/* 371 */           this.shouldScheduleFluidUpdate = false;
/* 372 */           return null;
/*     */         } 
/*     */       } 
/* 376 */       double similarity23 = similarity(distanceSqr2, distanceSqr3);
/* 377 */       if (similarity23 > 0.0D) {
/* 379 */         double barrier23 = similarity12 * similarity23 * calculatePressure(context, barrierNoiseValue, closestStatus2, closestStatus3);
/* 380 */         if (density + barrier23 > 0.0D) {
/* 381 */           this.shouldScheduleFluidUpdate = false;
/* 382 */           return null;
/*     */         } 
/*     */       } 
/* 387 */       boolean mayFlow12 = !closestStatus1.equals(closestStatus2);
/* 388 */       boolean mayFlow23 = (similarity23 >= FLOWING_UPDATE_SIMULARITY && !closestStatus2.equals(closestStatus3));
/* 389 */       boolean mayFlow13 = (similarity13 >= FLOWING_UPDATE_SIMULARITY && !closestStatus1.equals(closestStatus3));
/* 390 */       if (mayFlow12 || mayFlow23 || mayFlow13) {
/* 391 */         this.shouldScheduleFluidUpdate = true;
/*     */       } else {
/* 394 */         this
/*     */           
/* 396 */           .shouldScheduleFluidUpdate = (similarity13 >= FLOWING_UPDATE_SIMULARITY && similarity(distanceSqr1, distanceSqr4) >= FLOWING_UPDATE_SIMULARITY && !closestStatus1.equals(getAquiferStatus(closestIndex4)));
/*     */       } 
/* 399 */       return actualFluidState;
/*     */     }
/*     */     
/*     */     public boolean shouldScheduleFluidUpdate() {
/* 404 */       return this.shouldScheduleFluidUpdate;
/*     */     }
/*     */     
/*     */     private static double similarity(int distanceSqr1, int distanceSqr2) {
/* 415 */       double threshold = 25.0D;
/* 418 */       return 1.0D - (distanceSqr2 - distanceSqr1) / 25.0D;
/*     */     }
/*     */     
/*     */     private double calculatePressure(DensityFunction.FunctionContext context, MutableDouble barrierNoiseValue, Aquifer.FluidStatus statusClosest1, Aquifer.FluidStatus statusClosest2) {
/*     */       double gradient, noiseValue;
/* 424 */       int posY = context.blockY();
/* 425 */       BlockState type1 = statusClosest1.at(posY);
/* 426 */       BlockState type2 = statusClosest2.at(posY);
/* 428 */       if ((type1.is(Blocks.LAVA) && type2.is(Blocks.WATER)) || (type1.is(Blocks.WATER) && type2.is(Blocks.LAVA)))
/* 430 */         return 2.0D; 
/* 434 */       int fluidYDiff = Math.abs(statusClosest1.fluidLevel - statusClosest2.fluidLevel);
/* 436 */       if (fluidYDiff == 0)
/* 437 */         return 0.0D; 
/* 440 */       double averageFluidY = 0.5D * (statusClosest1.fluidLevel + statusClosest2.fluidLevel);
/* 443 */       double howFarAboveAverageFluidPoint = posY + 0.5D - averageFluidY;
/* 445 */       double baseValue = fluidYDiff / 2.0D;
/* 449 */       double topBias = 0.0D;
/* 451 */       double furthestRocksFromTopBias = 2.5D;
/* 453 */       double furthestHolesFromTopBias = 1.5D;
/* 456 */       double bottomBias = 3.0D;
/* 457 */       double furthestRocksFromBottomBias = 10.0D;
/* 458 */       double furthestHolesFromBottomBias = 3.0D;
/* 462 */       double distanceFromBarrierEdgeTowardsMiddle = baseValue - Math.abs(howFarAboveAverageFluidPoint);
/* 465 */       if (howFarAboveAverageFluidPoint > 0.0D) {
/* 467 */         double centerPoint = 0.0D + distanceFromBarrierEdgeTowardsMiddle;
/* 468 */         if (centerPoint > 0.0D) {
/* 470 */           gradient = centerPoint / 1.5D;
/*     */         } else {
/* 473 */           gradient = centerPoint / 2.5D;
/*     */         } 
/*     */       } else {
/* 477 */         double centerPoint = 3.0D + distanceFromBarrierEdgeTowardsMiddle;
/* 478 */         if (centerPoint > 0.0D) {
/* 480 */           gradient = centerPoint / 3.0D;
/*     */         } else {
/* 483 */           gradient = centerPoint / 10.0D;
/*     */         } 
/*     */       } 
/* 488 */       double amplitude = 2.0D;
/* 492 */       if (gradient < -2.0D || gradient > 2.0D) {
/* 493 */         noiseValue = 0.0D;
/*     */       } else {
/* 498 */         double currentNoiseValue = barrierNoiseValue.doubleValue();
/* 499 */         if (Double.isNaN(currentNoiseValue)) {
/* 500 */           double barrierNoise = this.barrierNoise.compute(context);
/* 501 */           barrierNoiseValue.setValue(barrierNoise);
/* 502 */           noiseValue = barrierNoise;
/*     */         } else {
/* 504 */           noiseValue = currentNoiseValue;
/*     */         } 
/*     */       } 
/* 508 */       return 2.0D * (noiseValue + gradient);
/*     */     }
/*     */     
/*     */     private static int gridX(int blockCoord) {
/* 512 */       return blockCoord >> 4;
/*     */     }
/*     */     
/*     */     private static int fromGridX(int gridCoord, int blockOffset) {
/* 516 */       return (gridCoord << 4) + blockOffset;
/*     */     }
/*     */     
/*     */     private static int gridY(int blockCoord) {
/* 520 */       return Math.floorDiv(blockCoord, 12);
/*     */     }
/*     */     
/*     */     private static int fromGridY(int gridCoord, int blockOffset) {
/* 524 */       return gridCoord * 12 + blockOffset;
/*     */     }
/*     */     
/*     */     private static int gridZ(int blockCoord) {
/* 528 */       return blockCoord >> 4;
/*     */     }
/*     */     
/*     */     private static int fromGridZ(int gridCoord, int blockOffset) {
/* 532 */       return (gridCoord << 4) + blockOffset;
/*     */     }
/*     */     
/*     */     private Aquifer.FluidStatus getAquiferStatus(int index) {
/* 536 */       Aquifer.FluidStatus oldStatus = this.aquiferCache[index];
/* 537 */       if (oldStatus != null)
/* 538 */         return oldStatus; 
/* 540 */       long location = this.aquiferLocationCache[index];
/* 541 */       Aquifer.FluidStatus status = computeFluid(BlockPos.getX(location), BlockPos.getY(location), BlockPos.getZ(location));
/* 542 */       this.aquiferCache[index] = status;
/* 543 */       return status;
/*     */     }
/*     */     
/*     */     private Aquifer.FluidStatus computeFluid(int x, int y, int z) {
/* 551 */       Aquifer.FluidStatus globalFluid = this.globalFluidPicker.computeFluid(x, y, z);
/* 553 */       int lowestPreliminarySurface = Integer.MAX_VALUE;
/* 560 */       int topOfAquiferCell = y + 12;
/* 561 */       int bottomOfAquiferCell = y - 12;
/* 562 */       boolean surfaceAtCenterIsUnderGlobalFluidLevel = false;
/* 563 */       for (int[] offset : SURFACE_SAMPLING_OFFSETS_IN_CHUNKS) {
/* 564 */         int sampleX = x + SectionPos.sectionToBlockCoord(offset[0]);
/* 565 */         int sampleZ = z + SectionPos.sectionToBlockCoord(offset[1]);
/* 566 */         int preliminarySurfaceLevel = this.noiseChunk.preliminarySurfaceLevel(sampleX, sampleZ);
/* 569 */         int adjustedSurfaceLevel = adjustSurfaceLevel(preliminarySurfaceLevel);
/* 571 */         boolean start = (offset[0] == 0 && offset[1] == 0);
/* 573 */         if (start && bottomOfAquiferCell > adjustedSurfaceLevel)
/* 576 */           return globalFluid; 
/* 579 */         boolean topOfAquiferCellPokesAboveSurface = (topOfAquiferCell > adjustedSurfaceLevel);
/* 580 */         if (topOfAquiferCellPokesAboveSurface || start) {
/* 581 */           Aquifer.FluidStatus globalFluidAtSurface = this.globalFluidPicker.computeFluid(sampleX, adjustedSurfaceLevel, sampleZ);
/* 582 */           if (!globalFluidAtSurface.at(adjustedSurfaceLevel).isAir()) {
/* 583 */             if (start)
/* 584 */               surfaceAtCenterIsUnderGlobalFluidLevel = true; 
/* 586 */             if (topOfAquiferCellPokesAboveSurface)
/* 588 */               return globalFluidAtSurface; 
/*     */           } 
/*     */         } 
/* 592 */         lowestPreliminarySurface = Math.min(lowestPreliminarySurface, preliminarySurfaceLevel);
/*     */       } 
/* 595 */       int fluidSurfaceLevel = computeSurfaceLevel(x, y, z, globalFluid, lowestPreliminarySurface, surfaceAtCenterIsUnderGlobalFluidLevel);
/* 597 */       return new Aquifer.FluidStatus(fluidSurfaceLevel, computeFluidType(x, y, z, globalFluid, fluidSurfaceLevel));
/*     */     }
/*     */     
/*     */     private int adjustSurfaceLevel(int preliminarySurfaceLevel) {
/* 601 */       return preliminarySurfaceLevel + 8;
/*     */     }
/*     */     
/*     */     private int computeSurfaceLevel(int x, int y, int z, Aquifer.FluidStatus globalFluid, int lowestPreliminarySurface, boolean surfaceAtCenterIsUnderGlobalFluidLevel) {
/*     */       double partiallyFloodedness, fullyFloodidness;
/*     */       int fluidSurfaceLevel;
/* 605 */       DensityFunction.SinglePointContext context = new DensityFunction.SinglePointContext(x, y, z);
/* 610 */       if (OverworldBiomeBuilder.isDeepDarkRegion(this.erosion, this.depth, context)) {
/* 611 */         partiallyFloodedness = -1.0D;
/* 612 */         fullyFloodidness = -1.0D;
/*     */       } else {
/* 614 */         int distanceBelowSurface = lowestPreliminarySurface + 8 - y;
/* 619 */         int floodednessMaxDepth = 64;
/* 620 */         double floodednessFactor = surfaceAtCenterIsUnderGlobalFluidLevel ? Mth.clampedMap(distanceBelowSurface, 0.0D, 64.0D, 1.0D, 0.0D) : 0.0D;
/* 625 */         double floodednessNoiseValue = Mth.clamp(this.fluidLevelFloodednessNoise.compute(context), -1.0D, 1.0D);
/* 629 */         double fullyFloodedThreshold = Mth.map(floodednessFactor, 1.0D, 0.0D, -0.3D, 0.8D);
/* 633 */         double partiallyFloodedThreshold = Mth.map(floodednessFactor, 1.0D, 0.0D, -0.8D, 0.4D);
/* 635 */         partiallyFloodedness = floodednessNoiseValue - partiallyFloodedThreshold;
/* 636 */         fullyFloodidness = floodednessNoiseValue - fullyFloodedThreshold;
/*     */       } 
/* 642 */       if (fullyFloodidness > 0.0D) {
/* 644 */         fluidSurfaceLevel = globalFluid.fluidLevel;
/* 645 */       } else if (partiallyFloodedness > 0.0D) {
/* 646 */         fluidSurfaceLevel = computeRandomizedFluidSurfaceLevel(x, y, z, lowestPreliminarySurface);
/*     */       } else {
/* 649 */         fluidSurfaceLevel = DimensionType.WAY_BELOW_MIN_Y;
/*     */       } 
/* 651 */       return fluidSurfaceLevel;
/*     */     }
/*     */     
/*     */     private int computeRandomizedFluidSurfaceLevel(int x, int y, int z, int lowestPreliminarySurface) {
/* 660 */       int fluidCellWidth = 16;
/* 661 */       int fluidCellHeight = 40;
/* 662 */       int fluidLevelCellX = Math.floorDiv(x, 16);
/* 663 */       int fluidLevelCellY = Math.floorDiv(y, 40);
/* 664 */       int fluidLevelCellZ = Math.floorDiv(z, 16);
/* 665 */       int fluidCellMiddleY = fluidLevelCellY * 40 + 20;
/* 676 */       int maxSpread = 10;
/* 677 */       double fluidLevelSpread = this.fluidLevelSpreadNoise.compute(new DensityFunction.SinglePointContext(fluidLevelCellX, fluidLevelCellY, fluidLevelCellZ)) * 10.0D;
/* 682 */       int fluidLevelSpreadQuantized = Mth.quantize(fluidLevelSpread, 3);
/* 684 */       int targetFluidSurfaceLevel = fluidCellMiddleY + fluidLevelSpreadQuantized;
/* 687 */       return Math.min(lowestPreliminarySurface, targetFluidSurfaceLevel);
/*     */     }
/*     */     
/*     */     private BlockState computeFluidType(int x, int y, int z, Aquifer.FluidStatus globalFluid, int fluidSurfaceLevel) {
/* 691 */       BlockState fluidType = globalFluid.fluidType;
/* 694 */       if (fluidSurfaceLevel <= -10 && fluidSurfaceLevel != DimensionType.WAY_BELOW_MIN_Y && globalFluid.fluidType != Blocks.LAVA.defaultBlockState()) {
/* 695 */         int fluidTypeCellWidth = 64;
/* 696 */         int fluidTypeCellHeight = 40;
/* 698 */         int fluidTypeCellX = Math.floorDiv(x, 64);
/* 699 */         int fluidTypeCellY = Math.floorDiv(y, 40);
/* 700 */         int fluidTypeCellZ = Math.floorDiv(z, 64);
/* 702 */         double lavaNoiseValue = this.lavaNoise.compute(new DensityFunction.SinglePointContext(fluidTypeCellX, fluidTypeCellY, fluidTypeCellZ));
/* 703 */         if (Math.abs(lavaNoiseValue) > 0.3D)
/* 704 */           fluidType = Blocks.LAVA.defaultBlockState(); 
/*     */       } 
/* 707 */       return fluidType;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class FluidStatus extends Record {
/*     */     private final int fluidLevel;
/*     */     
/*     */     private final BlockState fluidType;
/*     */     
/*     */     public FluidStatus(int fluidLevel, BlockState fluidType) {
/* 715 */       this.fluidLevel = fluidLevel;
/* 715 */       this.fluidType = fluidType;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #715	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #715	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #715	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/Aquifer$FluidStatus;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int fluidLevel() {
/* 715 */       return this.fluidLevel;
/*     */     }
/*     */     
/*     */     public BlockState fluidType() {
/* 715 */       return this.fluidType;
/*     */     }
/*     */     
/*     */     public BlockState at(int blockY) {
/* 721 */       return (blockY < this.fluidLevel) ? this.fluidType : Blocks.AIR.defaultBlockState();
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface FluidPicker {
/*     */     Aquifer.FluidStatus computeFluid(int param1Int1, int param1Int2, int param1Int3);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\Aquifer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */