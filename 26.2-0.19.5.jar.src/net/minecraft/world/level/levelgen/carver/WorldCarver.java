/*     */ package net.minecraft.world.level.levelgen.carver;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.CarvingMask;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.levelgen.Aquifer;
/*     */ import net.minecraft.world.level.levelgen.DensityFunction;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public abstract class WorldCarver<C extends CarverConfiguration> {
/*  34 */   public static final WorldCarver<CaveCarverConfiguration> CAVE = register("cave", new CaveWorldCarver(CaveCarverConfiguration.CODEC));
/*     */   
/*  35 */   public static final WorldCarver<CaveCarverConfiguration> NETHER_CAVE = register("nether_cave", new NetherWorldCarver(CaveCarverConfiguration.CODEC));
/*     */   
/*  36 */   public static final WorldCarver<CanyonCarverConfiguration> CANYON = register("canyon", new CanyonWorldCarver(CanyonCarverConfiguration.CODEC));
/*     */   
/*  38 */   protected static final BlockState AIR = Blocks.AIR.defaultBlockState();
/*     */   
/*  39 */   protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */   
/*  40 */   protected static final FluidState WATER = Fluids.WATER.defaultFluidState();
/*     */   
/*  41 */   protected static final FluidState LAVA = Fluids.LAVA.defaultFluidState();
/*     */   
/*     */   private static <C extends CarverConfiguration, F extends WorldCarver<C>> F register(String name, F carver) {
/*  44 */     return (F)Registry.register(BuiltInRegistries.CARVER, name, carver);
/*     */   }
/*     */   
/*  47 */   protected Set<Fluid> liquids = (Set<Fluid>)ImmutableSet.of(Fluids.WATER);
/*     */   
/*     */   private final MapCodec<ConfiguredWorldCarver<C>> configuredCodec;
/*     */   
/*     */   public WorldCarver(Codec<C> codec) {
/*  54 */     this.configuredCodec = codec.fieldOf("config").xmap(this::configured, ConfiguredWorldCarver::config);
/*     */   }
/*     */   
/*     */   public ConfiguredWorldCarver<C> configured(C configuration) {
/*  58 */     return new ConfiguredWorldCarver<>(this, configuration);
/*     */   }
/*     */   
/*     */   public MapCodec<ConfiguredWorldCarver<C>> configuredCodec() {
/*  62 */     return this.configuredCodec;
/*     */   }
/*     */   
/*     */   public int getRange() {
/*  66 */     return 4;
/*     */   }
/*     */   
/*     */   protected boolean carveEllipsoid(CarvingContext context, C configuration, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, Aquifer aquifer, double x, double y, double z, double horizontalRadius, double verticalRadius, CarvingMask mask, CarveSkipChecker skipChecker) {
/*  70 */     ChunkPos chunkPos = chunk.getPos();
/*  72 */     double centerX = chunkPos.getMiddleBlockX();
/*  73 */     double centerZ = chunkPos.getMiddleBlockZ();
/*  75 */     double maxDelta = 16.0D + horizontalRadius * 2.0D;
/*  76 */     if (Math.abs(x - centerX) > maxDelta || Math.abs(z - centerZ) > maxDelta)
/*  77 */       return false; 
/*  80 */     int chunkMinX = chunkPos.getMinBlockX();
/*  81 */     int chunkMinZ = chunkPos.getMinBlockZ();
/*  84 */     int minXIndex = Math.max(Mth.floor(x - horizontalRadius) - chunkMinX - 1, 0);
/*  85 */     int maxXIndex = Math.min(Mth.floor(x + horizontalRadius) - chunkMinX, 15);
/*  88 */     int minY = Math.max(Mth.floor(y - verticalRadius) - 1, context.getMinGenY() + 1);
/*  89 */     int protectedBlocksOnTop = chunk.isUpgrading() ? 0 : 7;
/*  90 */     int maxY = Math.min(Mth.floor(y + verticalRadius) + 1, context.getMinGenY() + context.getGenDepth() - 1 - protectedBlocksOnTop);
/*  92 */     int minZIndex = Math.max(Mth.floor(z - horizontalRadius) - chunkMinZ - 1, 0);
/*  93 */     int maxZIndex = Math.min(Mth.floor(z + horizontalRadius) - chunkMinZ, 15);
/*  95 */     boolean carved = false;
/*  96 */     BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
/*  97 */     BlockPos.MutableBlockPos helperPos = new BlockPos.MutableBlockPos();
/*  99 */     for (int xIndex = minXIndex; xIndex <= maxXIndex; xIndex++) {
/* 100 */       int worldX = chunkPos.getBlockX(xIndex);
/* 103 */       double xd = (worldX + 0.5D - x) / horizontalRadius;
/* 104 */       for (int zIndex = minZIndex; zIndex <= maxZIndex; zIndex++) {
/* 105 */         int worldZ = chunkPos.getBlockZ(zIndex);
/* 106 */         double zd = (worldZ + 0.5D - z) / horizontalRadius;
/* 107 */         if (xd * xd + zd * zd < 1.0D) {
/* 111 */           MutableBoolean hasGrass = new MutableBoolean(false);
/* 113 */           for (int worldY = maxY; worldY > minY; worldY--) {
/* 114 */             double yd = (worldY - 0.5D - y) / verticalRadius;
/* 115 */             if (!skipChecker.shouldSkip(context, xd, yd, zd, worldY))
/* 119 */               if (!mask.get(xIndex, worldY, zIndex) || isDebugEnabled((CarverConfiguration)configuration)) {
/* 120 */                 mask.set(xIndex, worldY, zIndex);
/* 122 */                 blockPos.set(worldX, worldY, worldZ);
/* 123 */                 carved |= carveBlock(context, configuration, chunk, biomeGetter, mask, blockPos, helperPos, aquifer, hasGrass);
/*     */               }  
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 129 */     return carved;
/*     */   }
/*     */   
/*     */   protected boolean carveBlock(CarvingContext context, C configuration, ChunkAccess chunk, Function<BlockPos, Holder<Biome>> biomeGetter, CarvingMask mask, BlockPos.MutableBlockPos blockPos, BlockPos.MutableBlockPos helperPos, Aquifer aquifer, MutableBoolean hasGrass) {
/* 133 */     BlockState blockState = chunk.getBlockState((BlockPos)blockPos);
/* 136 */     if (blockState.is(Blocks.GRASS_BLOCK) || blockState.is(Blocks.MYCELIUM))
/* 137 */       hasGrass.setTrue(); 
/* 139 */     if (!canReplaceBlock(configuration, blockState) && !isDebugEnabled((CarverConfiguration)configuration))
/* 140 */       return false; 
/* 143 */     BlockState state = getCarveState(context, configuration, (BlockPos)blockPos, aquifer);
/* 144 */     if (state == null)
/* 145 */       return false; 
/* 147 */     chunk.setBlockState((BlockPos)blockPos, state);
/* 148 */     if (aquifer.shouldScheduleFluidUpdate() && !state.getFluidState().isEmpty())
/* 150 */       chunk.markPosForPostProcessing((BlockPos)blockPos); 
/* 155 */     if (hasGrass.isTrue()) {
/* 156 */       helperPos.setWithOffset((Vec3i)blockPos, Direction.DOWN);
/* 157 */       if (chunk.getBlockState((BlockPos)helperPos).is(Blocks.DIRT))
/* 158 */         context.topMaterial(biomeGetter, chunk, (BlockPos)helperPos, !state.getFluidState().isEmpty()).ifPresent(topMaterial -> {
/*     */               chunk.setBlockState((BlockPos)helperPos, topMaterial);
/*     */               if (!topMaterial.getFluidState().isEmpty())
/*     */                 chunk.markPosForPostProcessing((BlockPos)helperPos); 
/*     */             }); 
/*     */     } 
/* 167 */     return true;
/*     */   }
/*     */   
/*     */   private BlockState getCarveState(CarvingContext context, C configuration, BlockPos blockPos, Aquifer aquifer) {
/* 173 */     if (blockPos.getY() <= ((CarverConfiguration)configuration).lavaLevel.resolveY(context))
/* 175 */       return LAVA.createLegacyBlock(); 
/* 178 */     BlockState state = aquifer.computeSubstance((DensityFunction.FunctionContext)new DensityFunction.SinglePointContext(blockPos.getX(), blockPos.getY(), blockPos.getZ()), 0.0D);
/* 179 */     if (state == null)
/* 181 */       return isDebugEnabled((CarverConfiguration)configuration) ? ((CarverConfiguration)configuration).debugSettings.getBarrierState() : null; 
/* 184 */     return isDebugEnabled((CarverConfiguration)configuration) ? getDebugState((CarverConfiguration)configuration, state) : state;
/*     */   }
/*     */   
/*     */   private static BlockState getDebugState(CarverConfiguration configuration, BlockState state) {
/* 188 */     if (state.is(Blocks.AIR))
/* 189 */       return configuration.debugSettings.getAirState(); 
/* 190 */     if (state.is(Blocks.WATER)) {
/* 191 */       BlockState debugState = configuration.debugSettings.getWaterState();
/* 192 */       if (debugState.hasProperty((Property)BlockStateProperties.WATERLOGGED))
/* 193 */         return (BlockState)debugState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true)); 
/* 195 */       return debugState;
/*     */     } 
/* 196 */     if (state.is(Blocks.LAVA))
/* 197 */       return configuration.debugSettings.getLavaState(); 
/* 199 */     return state;
/*     */   }
/*     */   
/*     */   protected boolean canReplaceBlock(C configuration, BlockState state) {
/* 209 */     return state.is(((CarverConfiguration)configuration).replaceable);
/*     */   }
/*     */   
/*     */   protected static boolean canReach(ChunkPos chunkPos, double x, double z, int currentStep, int totalSteps, float thickness) {
/* 213 */     double xMid = chunkPos.getMiddleBlockX();
/* 214 */     double zMid = chunkPos.getMiddleBlockZ();
/* 216 */     double xd = x - xMid;
/* 217 */     double zd = z - zMid;
/* 218 */     double remaining = (totalSteps - currentStep);
/* 219 */     double rr = (thickness + 2.0F + 16.0F);
/* 221 */     return (xd * xd + zd * zd - remaining * remaining <= rr * rr);
/*     */   }
/*     */   
/*     */   private static boolean isDebugEnabled(CarverConfiguration configuration) {
/* 226 */     return (SharedConstants.DEBUG_CARVERS || configuration.debugSettings.isDebugMode());
/*     */   }
/*     */   
/*     */   public abstract boolean carve(CarvingContext paramCarvingContext, C paramC, ChunkAccess paramChunkAccess, Function<BlockPos, Holder<Biome>> paramFunction, RandomSource paramRandomSource, Aquifer paramAquifer, ChunkPos paramChunkPos, CarvingMask paramCarvingMask);
/*     */   
/*     */   public abstract boolean isStartChunk(C paramC, RandomSource paramRandomSource);
/*     */   
/*     */   public static interface CarveSkipChecker {
/*     */     boolean shouldSkip(CarvingContext param1CarvingContext, double param1Double1, double param1Double2, double param1Double3, int param1Int);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\carver\WorldCarver.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */