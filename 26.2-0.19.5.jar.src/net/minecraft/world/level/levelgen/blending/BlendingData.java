/*     */ package net.minecraft.world.level.levelgen.blending;
/*     */ 
/*     */ import com.google.common.primitives.Doubles;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleArrays;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Direction8;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ public class BlendingData {
/*     */   private static final double BLENDING_DENSITY_FACTOR = 0.1D;
/*     */   
/*     */   protected static final int CELL_WIDTH = 4;
/*     */   
/*     */   protected static final int CELL_HEIGHT = 8;
/*     */   
/*     */   protected static final int CELL_RATIO = 2;
/*     */   
/*     */   private static final double SOLID_DENSITY = 1.0D;
/*     */   
/*     */   private static final double AIR_DENSITY = -1.0D;
/*     */   
/*     */   private static final int CELLS_PER_SECTION_Y = 2;
/*     */   
/*  48 */   private static final int QUARTS_PER_SECTION = QuartPos.fromBlock(16);
/*     */   
/*  49 */   private static final int CELL_HORIZONTAL_MAX_INDEX_INSIDE = QUARTS_PER_SECTION - 1;
/*     */   
/*  50 */   private static final int CELL_HORIZONTAL_MAX_INDEX_OUTSIDE = QUARTS_PER_SECTION;
/*     */   
/*  51 */   private static final int CELL_COLUMN_INSIDE_COUNT = 2 * CELL_HORIZONTAL_MAX_INDEX_INSIDE + 1;
/*     */   
/*  52 */   private static final int CELL_COLUMN_OUTSIDE_COUNT = 2 * CELL_HORIZONTAL_MAX_INDEX_OUTSIDE + 1;
/*     */   
/*  53 */   private static final int CELL_COLUMN_COUNT = CELL_COLUMN_INSIDE_COUNT + CELL_COLUMN_OUTSIDE_COUNT;
/*     */   
/*     */   private final LevelHeightAccessor areaWithOldGeneration;
/*     */   
/*  58 */   private static final List<Block> SURFACE_BLOCKS = List.of(new Block[] { 
/*  58 */         Blocks.PODZOL, Blocks.GRAVEL, Blocks.GRASS_BLOCK, Blocks.STONE, Blocks.COARSE_DIRT, Blocks.SAND, Blocks.RED_SAND, Blocks.MYCELIUM, Blocks.SNOW_BLOCK, Blocks.TERRACOTTA, 
/*  58 */         Blocks.DIRT });
/*     */   
/*     */   protected static final double NO_VALUE = 1.7976931348623157E308D;
/*     */   
/*     */   private boolean hasCalculatedData;
/*     */   
/*     */   private final double[] heights;
/*     */   
/*     */   private final List<List<Holder<Biome>>> biomes;
/*     */   
/*     */   private final transient double[][] densities;
/*     */   
/*     */   private BlendingData(int minSection, int maxSection, Optional<double[]> heights) {
/*  82 */     this.heights = heights.orElseGet(() -> (double[])Util.make(new double[CELL_COLUMN_COUNT], ()));
/*  84 */     this.densities = new double[CELL_COLUMN_COUNT][];
/*  86 */     ObjectArrayList<List<Holder<Biome>>> biomes = new ObjectArrayList(CELL_COLUMN_COUNT);
/*  87 */     biomes.size(CELL_COLUMN_COUNT);
/*  88 */     this.biomes = (List<List<Holder<Biome>>>)biomes;
/*  90 */     int minY = SectionPos.sectionToBlockCoord(minSection);
/*  91 */     int height = SectionPos.sectionToBlockCoord(maxSection) - minY;
/*  93 */     this.areaWithOldGeneration = LevelHeightAccessor.create(minY, height);
/*     */   }
/*     */   
/*     */   public static BlendingData unpack(Packed packed) {
/*  97 */     if (packed == null)
/*  98 */       return null; 
/* 100 */     return new BlendingData(packed.minSection(), packed.maxSection(), packed.heights());
/*     */   }
/*     */   
/*     */   public Packed pack() {
/* 104 */     boolean hasHeight = false;
/* 105 */     for (double height : this.heights) {
/* 106 */       if (height != Double.MAX_VALUE) {
/* 107 */         hasHeight = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 111 */     return new Packed(this.areaWithOldGeneration
/* 112 */         .getMinSectionY(), this.areaWithOldGeneration
/* 113 */         .getMaxSectionY() + 1, 
/* 114 */         hasHeight ? (Optional)Optional.<double[]>of(DoubleArrays.copy(this.heights)) : (Optional)Optional.<double[]>empty());
/*     */   }
/*     */   
/*     */   public static BlendingData getOrUpdateBlendingData(WorldGenRegion region, int chunkX, int chunkZ) {
/* 119 */     ChunkAccess chunk = region.getChunk(chunkX, chunkZ);
/* 120 */     BlendingData blendingData = chunk.getBlendingData();
/* 121 */     if (blendingData == null || chunk.getHighestGeneratedStatus().isBefore(ChunkStatus.BIOMES))
/* 122 */       return null; 
/* 125 */     blendingData.calculateData(chunk, sideByGenerationAge((WorldGenLevel)region, chunkX, chunkZ, false));
/* 127 */     return blendingData;
/*     */   }
/*     */   
/*     */   public static Set<Direction8> sideByGenerationAge(WorldGenLevel region, int chunkX, int chunkZ, boolean wantedOldGen) {
/* 131 */     Set<Direction8> sides = EnumSet.noneOf(Direction8.class);
/* 132 */     for (Direction8 direction8 : Direction8.values()) {
/* 133 */       int testChunkX = chunkX + direction8.getStepX();
/* 134 */       int testChunkZ = chunkZ + direction8.getStepZ();
/* 136 */       if (region.getChunk(testChunkX, testChunkZ).isOldNoiseGeneration() == wantedOldGen)
/* 137 */         sides.add(direction8); 
/*     */     } 
/* 140 */     return sides;
/*     */   }
/*     */   
/*     */   private void calculateData(ChunkAccess chunk, Set<Direction8> newSides) {
/* 144 */     if (this.hasCalculatedData)
/*     */       return; 
/* 149 */     if (newSides.contains(Direction8.NORTH) || newSides.contains(Direction8.WEST) || newSides.contains(Direction8.NORTH_WEST))
/* 150 */       addValuesForColumn(getInsideIndex(0, 0), chunk, 0, 0); 
/* 152 */     if (newSides.contains(Direction8.NORTH))
/* 153 */       for (int i = 1; i < QUARTS_PER_SECTION; i++)
/* 154 */         addValuesForColumn(getInsideIndex(i, 0), chunk, 4 * i, 0);  
/* 157 */     if (newSides.contains(Direction8.WEST))
/* 158 */       for (int i = 1; i < QUARTS_PER_SECTION; i++)
/* 159 */         addValuesForColumn(getInsideIndex(0, i), chunk, 0, 4 * i);  
/* 164 */     if (newSides.contains(Direction8.EAST))
/* 165 */       for (int i = 1; i < QUARTS_PER_SECTION; i++)
/* 166 */         addValuesForColumn(getOutsideIndex(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE, i), chunk, 15, 4 * i);  
/* 169 */     if (newSides.contains(Direction8.SOUTH))
/* 170 */       for (int i = 0; i < QUARTS_PER_SECTION; i++)
/* 171 */         addValuesForColumn(getOutsideIndex(i, CELL_HORIZONTAL_MAX_INDEX_OUTSIDE), chunk, 4 * i, 15);  
/* 174 */     if (newSides.contains(Direction8.EAST) && newSides.contains(Direction8.NORTH_EAST))
/* 175 */       addValuesForColumn(getOutsideIndex(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE, 0), chunk, 15, 0); 
/* 178 */     if (newSides.contains(Direction8.EAST) && newSides.contains(Direction8.SOUTH) && newSides.contains(Direction8.SOUTH_EAST))
/* 179 */       addValuesForColumn(getOutsideIndex(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE, CELL_HORIZONTAL_MAX_INDEX_OUTSIDE), chunk, 15, 15); 
/* 181 */     this.hasCalculatedData = true;
/*     */   }
/*     */   
/*     */   private void addValuesForColumn(int index, ChunkAccess chunk, int blockX, int blockZ) {
/* 185 */     if (this.heights[index] == Double.MAX_VALUE)
/* 186 */       this.heights[index] = getHeightAtXZ(chunk, blockX, blockZ); 
/* 188 */     this.densities[index] = getDensityColumn(chunk, blockX, blockZ, Mth.floor(this.heights[index]));
/* 190 */     this.biomes.set(index, getBiomeColumn(chunk, blockX, blockZ));
/*     */   }
/*     */   
/*     */   private int getHeightAtXZ(ChunkAccess chunk, int blockX, int blockZ) {
/*     */     int height;
/* 195 */     if (chunk.hasPrimedHeightmap(Heightmap.Types.WORLD_SURFACE_WG)) {
/* 196 */       height = Math.min(chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, blockX, blockZ), this.areaWithOldGeneration.getMaxY());
/*     */     } else {
/* 198 */       height = this.areaWithOldGeneration.getMaxY();
/*     */     } 
/* 201 */     int minY = this.areaWithOldGeneration.getMinY();
/* 202 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(blockX, height, blockZ);
/* 203 */     while (pos.getY() > minY) {
/* 204 */       if (SURFACE_BLOCKS.contains(chunk.getBlockState((BlockPos)pos).getBlock()))
/* 205 */         return pos.getY(); 
/* 207 */       pos.move(Direction.DOWN);
/*     */     } 
/* 209 */     return minY;
/*     */   }
/*     */   
/*     */   private static double read1(ChunkAccess chunk, BlockPos.MutableBlockPos pos) {
/* 213 */     return isGround(chunk, (BlockPos)pos.move(Direction.DOWN)) ? 1.0D : -1.0D;
/*     */   }
/*     */   
/*     */   private static double read7(ChunkAccess chunk, BlockPos.MutableBlockPos pos) {
/* 217 */     double sum = 0.0D;
/* 218 */     for (int i = 0; i < 7; i++)
/* 219 */       sum += read1(chunk, pos); 
/* 221 */     return sum;
/*     */   }
/*     */   
/*     */   private double[] getDensityColumn(ChunkAccess chunk, int x, int z, int height) {
/* 225 */     double[] densities = new double[cellCountPerColumn()];
/* 226 */     Arrays.fill(densities, -1.0D);
/* 228 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, this.areaWithOldGeneration.getMaxY() + 1, z);
/* 230 */     double last7 = read7(chunk, pos);
/* 232 */     for (int cellIndex = densities.length - 2; cellIndex >= 0; cellIndex--) {
/* 233 */       double one = read1(chunk, pos);
/* 234 */       double current7 = read7(chunk, pos);
/* 236 */       densities[cellIndex] = (last7 + one + current7) / 15.0D;
/* 238 */       last7 = current7;
/*     */     } 
/* 241 */     int highestCellWithSurfaceIndex = getCellYIndex(Mth.floorDiv(height, 8));
/* 242 */     if (highestCellWithSurfaceIndex >= 0 && highestCellWithSurfaceIndex < densities.length - 1) {
/* 243 */       double inCellIndex = (height + 0.5D) % 8.0D / 8.0D;
/* 244 */       double amplitudeAboveToMakeSurfaceBeAtHeight = (1.0D - inCellIndex) / inCellIndex;
/* 245 */       double max = Math.max(amplitudeAboveToMakeSurfaceBeAtHeight, 1.0D) * 0.25D;
/* 247 */       densities[highestCellWithSurfaceIndex + 1] = -amplitudeAboveToMakeSurfaceBeAtHeight / max;
/* 248 */       densities[highestCellWithSurfaceIndex] = 1.0D / max;
/*     */     } 
/* 250 */     return densities;
/*     */   }
/*     */   
/*     */   private List<Holder<Biome>> getBiomeColumn(ChunkAccess chunk, int blockX, int blockZ) {
/* 254 */     ObjectArrayList<Holder<Biome>> biomes = new ObjectArrayList(quartCountPerColumn());
/* 255 */     biomes.size(quartCountPerColumn());
/* 256 */     for (int quartIndex = 0; quartIndex < biomes.size(); quartIndex++) {
/* 257 */       int quartY = quartIndex + QuartPos.fromBlock(this.areaWithOldGeneration.getMinY());
/* 258 */       biomes.set(quartIndex, chunk.getNoiseBiome(QuartPos.fromBlock(blockX), quartY, QuartPos.fromBlock(blockZ)));
/*     */     } 
/* 260 */     return (List<Holder<Biome>>)biomes;
/*     */   }
/*     */   
/*     */   private static boolean isGround(ChunkAccess chunk, BlockPos pos) {
/* 264 */     BlockState state = chunk.getBlockState(pos);
/* 265 */     if (state.isAir())
/* 266 */       return false; 
/* 268 */     if (state.is(BlockTags.LEAVES))
/* 269 */       return false; 
/* 271 */     if (state.is(BlockTags.LOGS))
/* 272 */       return false; 
/* 274 */     if (state.is(Blocks.BROWN_MUSHROOM_BLOCK) || state.is(Blocks.RED_MUSHROOM_BLOCK))
/* 275 */       return false; 
/* 277 */     if (state.getCollisionShape((BlockGetter)chunk, pos).isEmpty())
/* 278 */       return false; 
/* 281 */     return true;
/*     */   }
/*     */   
/*     */   protected double getHeight(int cellX, int cellY, int cellZ) {
/* 285 */     if (cellX == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE || cellZ == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE)
/* 286 */       return this.heights[getOutsideIndex(cellX, cellZ)]; 
/* 288 */     if (cellX == 0 || cellZ == 0)
/* 289 */       return this.heights[getInsideIndex(cellX, cellZ)]; 
/* 291 */     return Double.MAX_VALUE;
/*     */   }
/*     */   
/*     */   private double getDensity(double[] densityColumn, int cellY) {
/* 295 */     if (densityColumn == null)
/* 296 */       return Double.MAX_VALUE; 
/* 298 */     int yIndex = getCellYIndex(cellY);
/* 299 */     if (yIndex < 0 || yIndex >= densityColumn.length)
/* 300 */       return Double.MAX_VALUE; 
/* 302 */     return densityColumn[yIndex] * 0.1D;
/*     */   }
/*     */   
/*     */   protected double getDensity(int cellX, int cellY, int cellZ) {
/* 306 */     if (cellY == getMinY())
/* 307 */       return 0.1D; 
/* 309 */     if (cellX == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE || cellZ == CELL_HORIZONTAL_MAX_INDEX_OUTSIDE)
/* 310 */       return getDensity(this.densities[getOutsideIndex(cellX, cellZ)], cellY); 
/* 312 */     if (cellX == 0 || cellZ == 0)
/* 313 */       return getDensity(this.densities[getInsideIndex(cellX, cellZ)], cellY); 
/* 315 */     return Double.MAX_VALUE;
/*     */   }
/*     */   
/*     */   protected void iterateBiomes(int minCellX, int quartY, int minCellZ, BiomeConsumer biomeConsumer) {
/* 323 */     if (quartY < QuartPos.fromBlock(this.areaWithOldGeneration.getMinY()) || quartY > QuartPos.fromBlock(this.areaWithOldGeneration.getMaxY()))
/*     */       return; 
/* 326 */     int quartIndex = quartY - QuartPos.fromBlock(this.areaWithOldGeneration.getMinY());
/* 327 */     for (int i = 0; i < this.biomes.size(); i++) {
/* 328 */       List<Holder<Biome>> biomeCell = this.biomes.get(i);
/* 329 */       if (biomeCell != null) {
/* 332 */         Holder<Biome> value = biomeCell.get(quartIndex);
/* 333 */         if (value != null)
/* 334 */           biomeConsumer.consume(minCellX + getX(i), minCellZ + getZ(i), value); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void iterateHeights(int minCellX, int minCellZ, HeightConsumer heightConsumer) {
/* 344 */     for (int i = 0; i < this.heights.length; i++) {
/* 345 */       double value = this.heights[i];
/* 346 */       if (value != Double.MAX_VALUE)
/* 347 */         heightConsumer.consume(minCellX + getX(i), minCellZ + getZ(i), value); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void iterateDensities(int minCellX, int minCellZ, int fromCellY, int toCellY, DensityConsumer densityConsumer) {
/* 357 */     int minCellY = getColumnMinY();
/* 358 */     int minYIndex = Math.max(0, fromCellY - minCellY);
/* 359 */     int maxYIndex = Math.min(cellCountPerColumn(), toCellY - minCellY);
/* 361 */     for (int i = 0; i < this.densities.length; i++) {
/* 362 */       double[] densityColumn = this.densities[i];
/* 363 */       if (densityColumn != null) {
/* 364 */         int testCellX = minCellX + getX(i);
/* 365 */         int testCellZ = minCellZ + getZ(i);
/* 366 */         for (int yIndex = minYIndex; yIndex < maxYIndex; yIndex++)
/* 367 */           densityConsumer.consume(testCellX, yIndex + minCellY, testCellZ, densityColumn[yIndex] * 0.1D); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int cellCountPerColumn() {
/* 374 */     return this.areaWithOldGeneration.getSectionsCount() * 2;
/*     */   }
/*     */   
/*     */   private int quartCountPerColumn() {
/* 378 */     return QuartPos.fromSection(this.areaWithOldGeneration.getSectionsCount());
/*     */   }
/*     */   
/*     */   private int getColumnMinY() {
/* 382 */     return getMinY() + 1;
/*     */   }
/*     */   
/*     */   private int getMinY() {
/* 386 */     return this.areaWithOldGeneration.getMinSectionY() * 2;
/*     */   }
/*     */   
/*     */   private int getCellYIndex(int cellY) {
/* 390 */     return cellY - getColumnMinY();
/*     */   }
/*     */   
/*     */   private static int getInsideIndex(int x, int z) {
/* 408 */     return CELL_HORIZONTAL_MAX_INDEX_INSIDE - x + z;
/*     */   }
/*     */   
/*     */   private static int getOutsideIndex(int x, int z) {
/* 428 */     return CELL_COLUMN_INSIDE_COUNT + x + CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - z;
/*     */   }
/*     */   
/*     */   private static int getX(int index) {
/* 432 */     if (index < CELL_COLUMN_INSIDE_COUNT)
/* 433 */       return zeroIfNegative(CELL_HORIZONTAL_MAX_INDEX_INSIDE - index); 
/* 435 */     int offsetIndex = index - CELL_COLUMN_INSIDE_COUNT;
/* 436 */     return CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - zeroIfNegative(CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - offsetIndex);
/*     */   }
/*     */   
/*     */   private static int getZ(int index) {
/* 441 */     if (index < CELL_COLUMN_INSIDE_COUNT)
/* 442 */       return zeroIfNegative(index - CELL_HORIZONTAL_MAX_INDEX_INSIDE); 
/* 444 */     int offsetIndex = index - CELL_COLUMN_INSIDE_COUNT;
/* 445 */     return CELL_HORIZONTAL_MAX_INDEX_OUTSIDE - zeroIfNegative(offsetIndex - CELL_HORIZONTAL_MAX_INDEX_OUTSIDE);
/*     */   }
/*     */   
/*     */   private static int zeroIfNegative(int value) {
/* 450 */     return value & (value >> 31 ^ 0xFFFFFFFF);
/*     */   }
/*     */   
/*     */   public LevelHeightAccessor getAreaWithOldGeneration() {
/* 454 */     return this.areaWithOldGeneration;
/*     */   }
/*     */   
/*     */   public static final class Packed extends Record {
/*     */     private final int minSection;
/*     */     
/*     */     private final int maxSection;
/*     */     
/*     */     private final Optional<double[]> heights;
/*     */     
/*     */     public Packed(int minSection, int maxSection, Optional<double[]> heights) {
/* 457 */       this.minSection = minSection;
/* 457 */       this.maxSection = maxSection;
/* 457 */       this.heights = heights;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #457	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #457	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #457	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/blending/BlendingData$Packed;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int minSection() {
/* 457 */       return this.minSection;
/*     */     }
/*     */     
/*     */     public int maxSection() {
/* 457 */       return this.maxSection;
/*     */     }
/*     */     
/*     */     public Optional<double[]> heights() {
/* 457 */       return this.heights;
/*     */     }
/*     */     
/* 460 */     private static final Codec<double[]> DOUBLE_ARRAY_CODEC = Codec.DOUBLE.listOf().xmap(Doubles::toArray, Doubles::asList);
/*     */     
/*     */     public static final Codec<Packed> CODEC;
/*     */     
/*     */     static {
/* 465 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.INT.fieldOf("min_section").forGetter(Packed::minSection), (App)Codec.INT.fieldOf("max_section").forGetter(Packed::maxSection), (App)DOUBLE_ARRAY_CODEC.lenientOptionalFieldOf("heights").forGetter(Packed::heights)).apply((Applicative)i, Packed::new)).validate(Packed::validateArraySize);
/*     */     }
/*     */     
/*     */     private static DataResult<Packed> validateArraySize(Packed blendingData) {
/* 468 */       if (blendingData.heights.isPresent() && ((double[])blendingData.heights.get()).length != BlendingData.CELL_COLUMN_COUNT)
/* 469 */         return DataResult.error(() -> "heights has to be of length " + BlendingData.CELL_COLUMN_COUNT); 
/* 472 */       return DataResult.success(blendingData);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static interface BiomeConsumer {
/*     */     void consume(int param1Int1, int param1Int2, Holder<Biome> param1Holder);
/*     */   }
/*     */   
/*     */   protected static interface HeightConsumer {
/*     */     void consume(int param1Int1, int param1Int2, double param1Double);
/*     */   }
/*     */   
/*     */   protected static interface DensityConsumer {
/*     */     void consume(int param1Int1, int param1Int2, int param1Int3, double param1Double);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\blending\BlendingData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */