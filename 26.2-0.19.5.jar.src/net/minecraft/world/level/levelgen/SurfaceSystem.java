/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.BlockColumn;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.levelgen.carver.CarvingContext;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ public class SurfaceSystem {
/*  31 */   private static final BlockState WHITE_TERRACOTTA = ((Block)Blocks.DYED_TERRACOTTA.white()).defaultBlockState();
/*     */   
/*  32 */   private static final BlockState ORANGE_TERRACOTTA = ((Block)Blocks.DYED_TERRACOTTA.orange()).defaultBlockState();
/*     */   
/*  33 */   private static final BlockState TERRACOTTA = Blocks.TERRACOTTA.defaultBlockState();
/*     */   
/*  34 */   private static final BlockState YELLOW_TERRACOTTA = ((Block)Blocks.DYED_TERRACOTTA.yellow()).defaultBlockState();
/*     */   
/*  35 */   private static final BlockState BROWN_TERRACOTTA = ((Block)Blocks.DYED_TERRACOTTA.brown()).defaultBlockState();
/*     */   
/*  36 */   private static final BlockState RED_TERRACOTTA = ((Block)Blocks.DYED_TERRACOTTA.red()).defaultBlockState();
/*     */   
/*  37 */   private static final BlockState LIGHT_GRAY_TERRACOTTA = ((Block)Blocks.DYED_TERRACOTTA.lightGray()).defaultBlockState();
/*     */   
/*  39 */   private static final BlockState PACKED_ICE = Blocks.PACKED_ICE.defaultBlockState();
/*     */   
/*  40 */   private static final BlockState SNOW_BLOCK = Blocks.SNOW_BLOCK.defaultBlockState();
/*     */   
/*     */   private final BlockState defaultBlock;
/*     */   
/*     */   private final int seaLevel;
/*     */   
/*     */   private final BlockState[] clayBands;
/*     */   
/*     */   private final NormalNoise clayBandsOffsetNoise;
/*     */   
/*     */   private final NormalNoise badlandsPillarNoise;
/*     */   
/*     */   private final NormalNoise badlandsPillarRoofNoise;
/*     */   
/*     */   private final NormalNoise badlandsSurfaceNoise;
/*     */   
/*     */   private final NormalNoise icebergPillarNoise;
/*     */   
/*     */   private final NormalNoise icebergPillarRoofNoise;
/*     */   
/*     */   private final NormalNoise icebergSurfaceNoise;
/*     */   
/*     */   private final PositionalRandomFactory noiseRandom;
/*     */   
/*     */   private final NormalNoise surfaceNoise;
/*     */   
/*     */   private final NormalNoise surfaceSecondaryNoise;
/*     */   
/*     */   public SurfaceSystem(RandomState randomState, BlockState defaultBlock, int seaLevel, PositionalRandomFactory noiseRandom) {
/*  61 */     this.defaultBlock = defaultBlock;
/*  62 */     this.seaLevel = seaLevel;
/*  64 */     this.noiseRandom = noiseRandom;
/*  66 */     this.clayBandsOffsetNoise = randomState.getOrCreateNoise(Noises.CLAY_BANDS_OFFSET);
/*  67 */     this.clayBands = generateBands(noiseRandom.fromHashOf(Identifier.withDefaultNamespace("clay_bands")));
/*  69 */     this.surfaceNoise = randomState.getOrCreateNoise(Noises.SURFACE);
/*  70 */     this.surfaceSecondaryNoise = randomState.getOrCreateNoise(Noises.SURFACE_SECONDARY);
/*  72 */     this.badlandsPillarNoise = randomState.getOrCreateNoise(Noises.BADLANDS_PILLAR);
/*  73 */     this.badlandsPillarRoofNoise = randomState.getOrCreateNoise(Noises.BADLANDS_PILLAR_ROOF);
/*  74 */     this.badlandsSurfaceNoise = randomState.getOrCreateNoise(Noises.BADLANDS_SURFACE);
/*  76 */     this.icebergPillarNoise = randomState.getOrCreateNoise(Noises.ICEBERG_PILLAR);
/*  77 */     this.icebergPillarRoofNoise = randomState.getOrCreateNoise(Noises.ICEBERG_PILLAR_ROOF);
/*  78 */     this.icebergSurfaceNoise = randomState.getOrCreateNoise(Noises.ICEBERG_SURFACE);
/*     */   }
/*     */   
/*     */   public void buildSurface(RandomState randomState, BiomeManager biomeManager, boolean useLegacyRandom, WorldGenerationContext generationContext, final ChunkAccess protoChunk, NoiseChunk noiseChunk, SurfaceRules.RuleSource ruleSource, Set<Holder<Biome>> possibleBiomes) {
/*  82 */     final BlockPos.MutableBlockPos columnPos = new BlockPos.MutableBlockPos();
/*  84 */     final ChunkPos chunkPos = protoChunk.getPos();
/*  85 */     int minBlockX = chunkPos.getMinBlockX();
/*  86 */     int minBlockZ = chunkPos.getMinBlockZ();
/*  88 */     BlockColumn column = new BlockColumn(this) {
/*     */         public BlockState getBlock(int blockY) {
/*  91 */           return protoChunk.getBlockState((BlockPos)columnPos.setY(blockY));
/*     */         }
/*     */         
/*     */         public void setBlock(int blockY, BlockState state) {
/*  96 */           LevelHeightAccessor heightAccessor = protoChunk.getHeightAccessorForGeneration();
/*  97 */           if (heightAccessor.isInsideBuildHeight(blockY)) {
/*  98 */             protoChunk.setBlockState((BlockPos)columnPos.setY(blockY), state);
/*  99 */             if (!state.getFluidState().isEmpty())
/* 100 */               protoChunk.markPosForPostProcessing((BlockPos)columnPos); 
/*     */           } 
/*     */         }
/*     */         
/*     */         public String toString() {
/* 107 */           return "ChunkBlockColumn " + String.valueOf(chunkPos);
/*     */         }
/*     */       };
/* 111 */     Objects.requireNonNull(biomeManager);
/* 111 */     SurfaceRules.Context context = new SurfaceRules.Context(this, randomState, protoChunk, noiseChunk, biomeManager::getBiome, generationContext, possibleBiomes);
/* 112 */     SurfaceRules.SurfaceRule rule = ruleSource.apply(context);
/* 114 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/* 116 */     for (int x = 0; x < 16; x++) {
/* 117 */       for (int z = 0; z < 16; z++) {
/* 118 */         int blockX = minBlockX + x;
/* 119 */         int blockZ = minBlockZ + z;
/* 120 */         int startingHeight = protoChunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) + 1;
/* 122 */         columnPos.setX(blockX).setZ(blockZ);
/* 124 */         Holder<Biome> surfaceBiome = biomeManager.getBiome((BlockPos)blockPos.set(blockX, useLegacyRandom ? 0 : startingHeight, blockZ));
/* 127 */         if (surfaceBiome.is(Biomes.ERODED_BADLANDS))
/* 128 */           erodedBadlandsExtension(column, blockX, blockZ, startingHeight, (LevelHeightAccessor)protoChunk); 
/* 130 */         int height = protoChunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) + 1;
/* 132 */         context.updateXZ(blockX, blockZ);
/* 134 */         int stoneAboveDepth = 0;
/* 135 */         int waterHeight = Integer.MIN_VALUE;
/* 136 */         int nextCeilingStoneY = Integer.MAX_VALUE;
/* 138 */         int endY = protoChunk.getMinY();
/* 139 */         for (int y = height; y >= endY; y--) {
/* 140 */           BlockState old = column.getBlock(y);
/* 142 */           if (old.isAir()) {
/* 143 */             stoneAboveDepth = 0;
/* 145 */             waterHeight = Integer.MIN_VALUE;
/* 149 */           } else if (!old.getFluidState().isEmpty()) {
/* 153 */             if (waterHeight == Integer.MIN_VALUE)
/* 154 */               waterHeight = y + 1; 
/*     */           } else {
/* 159 */             if (nextCeilingStoneY >= y) {
/* 161 */               nextCeilingStoneY = DimensionType.WAY_BELOW_MIN_Y;
/* 162 */               for (int lookaheadY = y - 1; lookaheadY >= endY - 1; lookaheadY--) {
/* 163 */                 BlockState nextState = column.getBlock(lookaheadY);
/* 164 */                 if (!isStone(nextState)) {
/* 165 */                   nextCeilingStoneY = lookaheadY + 1;
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             } 
/* 171 */             stoneAboveDepth++;
/* 172 */             int stoneBelowDepth = y - nextCeilingStoneY + 1;
/* 174 */             context.updateY(stoneAboveDepth, stoneBelowDepth, waterHeight, y);
/* 176 */             if (old == this.defaultBlock) {
/* 182 */               BlockState state = rule.tryApply(blockX, y, blockZ);
/* 184 */               if (state != null)
/* 185 */                 column.setBlock(y, state); 
/*     */             } 
/*     */           } 
/*     */         } 
/* 190 */         if (surfaceBiome.is(Biomes.FROZEN_OCEAN) || surfaceBiome.is(Biomes.DEEP_FROZEN_OCEAN))
/* 191 */           frozenOceanExtension(context.getMinSurfaceLevel(), (Biome)surfaceBiome.value(), column, blockPos, blockX, blockZ, startingHeight); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int getSurfaceDepth(int blockX, int blockZ) {
/* 199 */     double noiseValue = this.surfaceNoise.getValue(blockX, 0.0D, blockZ);
/* 201 */     return (int)(noiseValue * 2.75D + 3.0D + this.noiseRandom.at(blockX, 0, blockZ).nextDouble() * 0.25D);
/*     */   }
/*     */   
/*     */   protected double getSurfaceSecondary(int blockX, int blockZ) {
/* 206 */     return this.surfaceSecondaryNoise.getValue(blockX, 0.0D, blockZ);
/*     */   }
/*     */   
/*     */   private boolean isStone(BlockState state) {
/* 211 */     return (!state.isAir() && state.getFluidState().isEmpty());
/*     */   }
/*     */   
/*     */   public int getSeaLevel() {
/* 215 */     return this.seaLevel;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Optional<BlockState> topMaterial(SurfaceRules.RuleSource ruleSource, CarvingContext carvingContext, Function<BlockPos, Holder<Biome>> biomeGetter, ChunkAccess chunk, NoiseChunk noiseChunk, BlockPos pos, boolean underFluid) {
/* 221 */     SurfaceRules.Context context = new SurfaceRules.Context(this, carvingContext.randomState(), chunk, noiseChunk, biomeGetter, (WorldGenerationContext)carvingContext, null);
/* 222 */     SurfaceRules.SurfaceRule rule = ruleSource.apply(context);
/* 224 */     int blockX = pos.getX();
/* 225 */     int blockY = pos.getY();
/* 226 */     int blockZ = pos.getZ();
/* 228 */     context.updateXZ(blockX, blockZ);
/* 229 */     context.updateY(1, 1, underFluid ? (blockY + 1) : Integer.MIN_VALUE, blockY);
/* 230 */     BlockState state = rule.tryApply(blockX, blockY, blockZ);
/* 232 */     return Optional.ofNullable(state);
/*     */   }
/*     */   
/*     */   private void erodedBadlandsExtension(BlockColumn column, int blockX, int blockZ, int height, LevelHeightAccessor protoChunk) {
/* 236 */     double pillarNoiseScale = 0.2D;
/* 237 */     double pillarBuffer = Math.min(Math.abs(this.badlandsSurfaceNoise.getValue(blockX, 0.0D, blockZ) * 8.25D), this.badlandsPillarNoise.getValue(blockX * 0.2D, 0.0D, blockZ * 0.2D) * 15.0D);
/* 238 */     if (pillarBuffer <= 0.0D)
/*     */       return; 
/* 242 */     double floorNoiseSampleResolution = 0.75D;
/* 243 */     double floorAmplitude = 1.5D;
/* 244 */     double pillarFloor = Math.abs(this.badlandsPillarRoofNoise.getValue(blockX * 0.75D, 0.0D, blockZ * 0.75D) * 1.5D);
/* 245 */     double extensionTop = 64.0D + Math.min(pillarBuffer * pillarBuffer * 2.5D, Math.ceil(pillarFloor * 50.0D) + 24.0D);
/* 247 */     int startY = Mth.floor(extensionTop);
/* 250 */     if (height > startY)
/*     */       return; 
/*     */     int y;
/* 254 */     for (y = startY; y >= protoChunk.getMinY(); y--) {
/* 255 */       BlockState oldState = column.getBlock(y);
/* 256 */       if (oldState.is(this.defaultBlock.getBlock()))
/*     */         break; 
/* 259 */       if (oldState.is(Blocks.WATER))
/*     */         return; 
/*     */     } 
/* 264 */     for (y = startY; y >= protoChunk.getMinY() && 
/* 265 */       column.getBlock(y).isAir(); y--)
/* 268 */       column.setBlock(y, this.defaultBlock); 
/*     */   }
/*     */   
/*     */   private void frozenOceanExtension(int minSurfaceLevel, Biome surfaceBiome, BlockColumn column, BlockPos.MutableBlockPos blockPos, int blockX, int blockZ, int height) {
/* 274 */     double extensionBottom, pillarScale = 1.28D;
/* 275 */     double iceberg = Math.min(Math.abs(this.icebergSurfaceNoise.getValue(blockX, 0.0D, blockZ) * 8.25D), this.icebergPillarNoise.getValue(blockX * 1.28D, 0.0D, blockZ * 1.28D) * 15.0D);
/* 278 */     if (iceberg <= 1.8D)
/*     */       return; 
/* 282 */     double roofScale = 1.17D;
/* 283 */     double roofAmplitude = 1.5D;
/* 284 */     double icebergRoof = Math.abs(this.icebergPillarRoofNoise.getValue(blockX * 1.17D, 0.0D, blockZ * 1.17D) * 1.5D);
/* 285 */     double top = Math.min(iceberg * iceberg * 1.2D, Math.ceil(icebergRoof * 40.0D) + 14.0D);
/* 287 */     if (surfaceBiome.shouldMeltFrozenOceanIcebergSlightly((BlockPos)blockPos.set(blockX, this.seaLevel, blockZ), this.seaLevel))
/* 288 */       top -= 2.0D; 
/* 291 */     if (top > 2.0D) {
/* 292 */       extensionBottom = this.seaLevel - top - 7.0D;
/* 293 */       top += this.seaLevel;
/*     */     } else {
/* 295 */       top = 0.0D;
/* 296 */       extensionBottom = 0.0D;
/*     */     } 
/* 298 */     double extensionTop = top;
/* 300 */     RandomSource random = this.noiseRandom.at(blockX, 0, blockZ);
/* 302 */     int maxSnowDepth = 2 + random.nextInt(4);
/* 303 */     int minSnowHeight = this.seaLevel + 18 + random.nextInt(10);
/* 305 */     int snowDepth = 0;
/* 307 */     for (int y = Math.max(height, (int)extensionTop + 1); y >= minSurfaceLevel; y--) {
/* 309 */       if ((column.getBlock(y).isAir() && y < (int)extensionTop && random.nextDouble() > 0.01D) || (column.getBlock(y).is(Blocks.WATER) && y > (int)extensionBottom && y < this.seaLevel && extensionBottom != 0.0D && random.nextDouble() > 0.15D))
/* 310 */         if (snowDepth <= maxSnowDepth && y > minSnowHeight) {
/* 311 */           column.setBlock(y, SNOW_BLOCK);
/* 312 */           snowDepth++;
/*     */         } else {
/* 314 */           column.setBlock(y, PACKED_ICE);
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BlockState[] generateBands(RandomSource random) {
/* 321 */     BlockState[] clayBands = new BlockState[192];
/* 322 */     Arrays.fill((Object[])clayBands, TERRACOTTA);
/* 325 */     for (int i = 0; i < clayBands.length; i++) {
/* 326 */       i += random.nextInt(5) + 1;
/* 327 */       if (i < clayBands.length)
/* 328 */         clayBands[i] = ORANGE_TERRACOTTA; 
/*     */     } 
/* 333 */     makeBands(random, clayBands, 1, YELLOW_TERRACOTTA);
/* 335 */     makeBands(random, clayBands, 2, BROWN_TERRACOTTA);
/* 337 */     makeBands(random, clayBands, 1, RED_TERRACOTTA);
/* 342 */     int whiteBandCount = random.nextIntBetweenInclusive(9, 15);
/*     */     int start;
/* 343 */     for (int j = 0; j < whiteBandCount && start < clayBands.length; j++, start += random.nextInt(16) + 4) {
/* 344 */       clayBands[start] = WHITE_TERRACOTTA;
/* 345 */       if (start - 1 > 0 && random.nextBoolean())
/* 346 */         clayBands[start - 1] = LIGHT_GRAY_TERRACOTTA; 
/* 348 */       if (start + 1 < clayBands.length && random.nextBoolean())
/* 349 */         clayBands[start + 1] = LIGHT_GRAY_TERRACOTTA; 
/*     */     } 
/* 353 */     return clayBands;
/*     */   }
/*     */   
/*     */   private static void makeBands(RandomSource random, BlockState[] clayBands, int baseWidth, BlockState state) {
/* 357 */     int bandCount = random.nextIntBetweenInclusive(6, 15);
/* 358 */     for (int i = 0; i < bandCount; i++) {
/* 359 */       int width = baseWidth + random.nextInt(3);
/* 360 */       int start = random.nextInt(clayBands.length);
/* 362 */       for (int p = 0; start + p < clayBands.length && p < width; p++)
/* 363 */         clayBands[start + p] = state; 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState getBand(int worldX, int y, int worldZ) {
/* 369 */     int offset = (int)Math.round(this.clayBandsOffsetNoise.getValue(worldX, 0.0D, worldZ) * 4.0D);
/* 370 */     return this.clayBands[(y + offset + this.clayBands.length) % this.clayBands.length];
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\SurfaceSystem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */