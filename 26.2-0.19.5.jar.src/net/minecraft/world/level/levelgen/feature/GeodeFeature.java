/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.BuddingAmethystBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.levelgen.GeodeBlockSettings;
/*     */ import net.minecraft.world.level.levelgen.GeodeCrackSettings;
/*     */ import net.minecraft.world.level.levelgen.GeodeLayerSettings;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ 
/*     */ public class GeodeFeature extends Feature<GeodeConfiguration> {
/*  31 */   private static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   public GeodeFeature(Codec<GeodeConfiguration> codec) {
/*  34 */     super(codec);
/*     */   }
/*     */   
/*     */   public boolean place(FeaturePlaceContext<GeodeConfiguration> context) {
/*  39 */     GeodeConfiguration config = context.config();
/*  40 */     RandomSource random = context.random();
/*  41 */     BlockPos origin = context.origin();
/*  42 */     WorldGenLevel level = context.level();
/*  43 */     int minGenOffset = config.minGenOffset();
/*  44 */     int maxGenOffset = config.maxGenOffset();
/*  46 */     List<Pair<BlockPos, Integer>> points = Lists.newLinkedList();
/*  47 */     int numPoints = config.distributionPoints().sample(random);
/*  48 */     WorldgenRandom random1 = new WorldgenRandom((RandomSource)new LegacyRandomSource(level.getSeed()));
/*  49 */     NormalNoise noise = NormalNoise.create((RandomSource)random1, -4, new double[] { 1.0D });
/*  50 */     List<BlockPos> crackPoints = Lists.newLinkedList();
/*  51 */     double crackSizeAdjustment = numPoints / config.outerWallDistance().maxInclusive();
/*  52 */     GeodeLayerSettings layerSettings = config.geodeLayerSettings();
/*  53 */     GeodeBlockSettings blockSettings = config.geodeBlockSettings();
/*  54 */     GeodeCrackSettings crackSettings = config.geodeCrackSettings();
/*  55 */     double innerAir = 1.0D / Math.sqrt(layerSettings.filling);
/*  56 */     double innermostBlockLayer = 1.0D / Math.sqrt(layerSettings.innerLayer + crackSizeAdjustment);
/*  57 */     double innerCrust = 1.0D / Math.sqrt(layerSettings.middleLayer + crackSizeAdjustment);
/*  58 */     double outerCrust = 1.0D / Math.sqrt(layerSettings.outerLayer + crackSizeAdjustment);
/*  59 */     double crackSize = 1.0D / Math.sqrt(crackSettings.baseCrackSize + random.nextDouble() / 2.0D + ((numPoints > 3) ? crackSizeAdjustment : 0.0D));
/*  60 */     boolean shouldGenerateCrack = (random.nextFloat() < crackSettings.generateCrackChance);
/*  62 */     int numInvalidPoints = 0;
/*  63 */     for (int i = 0; i < numPoints; i++) {
/*  64 */       int x = config.outerWallDistance().sample(random);
/*  65 */       int y = config.outerWallDistance().sample(random);
/*  66 */       int z = config.outerWallDistance().sample(random);
/*  67 */       BlockPos pos = origin.offset(x, y, z);
/*  68 */       BlockState state = level.getBlockState(pos);
/*  69 */       if ((state.isAir() || state.is(blockSettings.invalidBlocks())) && 
/*  70 */         ++numInvalidPoints > config.invalidBlocksThreshold())
/*  71 */         return false; 
/*  74 */       points.add(Pair.of(pos, Integer.valueOf(config.pointOffset().sample(random))));
/*     */     } 
/*  77 */     if (shouldGenerateCrack) {
/*  78 */       int offsetIndex = random.nextInt(4);
/*  80 */       int crackOffset = numPoints * 2 + 1;
/*  81 */       if (offsetIndex == 0) {
/*  82 */         crackPoints.add(origin.offset(crackOffset, 7, 0));
/*  83 */         crackPoints.add(origin.offset(crackOffset, 5, 0));
/*  84 */         crackPoints.add(origin.offset(crackOffset, 1, 0));
/*  85 */       } else if (offsetIndex == 1) {
/*  86 */         crackPoints.add(origin.offset(0, 7, crackOffset));
/*  87 */         crackPoints.add(origin.offset(0, 5, crackOffset));
/*  88 */         crackPoints.add(origin.offset(0, 1, crackOffset));
/*  89 */       } else if (offsetIndex == 2) {
/*  90 */         crackPoints.add(origin.offset(crackOffset, 7, crackOffset));
/*  91 */         crackPoints.add(origin.offset(crackOffset, 5, crackOffset));
/*  92 */         crackPoints.add(origin.offset(crackOffset, 1, crackOffset));
/*     */       } else {
/*  94 */         crackPoints.add(origin.offset(0, 7, 0));
/*  95 */         crackPoints.add(origin.offset(0, 5, 0));
/*  96 */         crackPoints.add(origin.offset(0, 1, 0));
/*     */       } 
/*     */     } 
/* 100 */     List<BlockPos> potentialCrystalPlacements = Lists.newArrayList();
/* 101 */     HolderSet<Block> cantReplace = config.geodeBlockSettings().cannotReplace();
/* 102 */     Predicate<BlockState> canReplace = s -> !s.is(cantReplace);
/* 104 */     for (BlockPos pointInside : BlockPos.betweenClosed(origin.offset(minGenOffset, minGenOffset, minGenOffset), origin.offset(maxGenOffset, maxGenOffset, maxGenOffset))) {
/* 105 */       double noiseOffset = noise.getValue(pointInside.getX(), pointInside.getY(), pointInside.getZ()) * config.noiseMultiplier();
/* 107 */       double distSumShell = 0.0D;
/* 108 */       double distSumCrack = 0.0D;
/* 110 */       for (Pair<BlockPos, Integer> point : points)
/* 111 */         distSumShell += Mth.invSqrt(pointInside.distSqr((Vec3i)point.getFirst()) + ((Integer)point.getSecond()).intValue()) + noiseOffset; 
/* 114 */       for (BlockPos point : crackPoints)
/* 115 */         distSumCrack += Mth.invSqrt(pointInside.distSqr((Vec3i)point) + crackSettings.crackPointOffset) + noiseOffset; 
/* 119 */       if (distSumShell < outerCrust)
/*     */         continue; 
/* 123 */       if (shouldGenerateCrack && distSumCrack >= crackSize && distSumShell < innerAir) {
/* 125 */         safeSetBlock(level, pointInside, Blocks.AIR.defaultBlockState(), canReplace);
/* 128 */         for (Direction direction : DIRECTIONS) {
/* 129 */           BlockPos adjacentPos = pointInside.relative(direction);
/* 130 */           FluidState adjacentFluidState = level.getFluidState(adjacentPos);
/* 131 */           if (!adjacentFluidState.isEmpty())
/* 132 */             level.scheduleTick(adjacentPos, adjacentFluidState.getType(), 0); 
/*     */         } 
/*     */         continue;
/*     */       } 
/* 135 */       if (distSumShell >= innerAir) {
/* 136 */         safeSetBlock(level, pointInside, blockSettings.fillingProvider().getState(level, random, pointInside), canReplace);
/*     */         continue;
/*     */       } 
/* 137 */       if (distSumShell >= innermostBlockLayer) {
/* 138 */         boolean useAlternateLayer = (random.nextFloat() < config.useAlternateLayer0Chance());
/* 139 */         if (useAlternateLayer) {
/* 140 */           safeSetBlock(level, pointInside, blockSettings.alternateInnerLayerProvider().getState(level, random, pointInside), canReplace);
/*     */         } else {
/* 142 */           safeSetBlock(level, pointInside, blockSettings.innerLayerProvider().getState(level, random, pointInside), canReplace);
/*     */         } 
/* 145 */         if ((!config.placementsRequireLayer0Alternate() || useAlternateLayer) && random.nextFloat() < config.usePotentialPlacementsChance())
/* 146 */           potentialCrystalPlacements.add(pointInside.immutable()); 
/*     */         continue;
/*     */       } 
/* 148 */       if (distSumShell >= innerCrust) {
/* 149 */         safeSetBlock(level, pointInside, blockSettings.middleLayerProvider().getState(level, random, pointInside), canReplace);
/*     */         continue;
/*     */       } 
/* 150 */       if (distSumShell >= outerCrust)
/* 151 */         safeSetBlock(level, pointInside, blockSettings.outerLayerProvider().getState(level, random, pointInside), canReplace); 
/*     */     } 
/* 155 */     List<BlockState> innerPlacements = blockSettings.innerPlacements();
/* 156 */     for (BlockPos crystalPos : potentialCrystalPlacements) {
/* 157 */       BlockState blockState = (BlockState)Util.getRandom(innerPlacements, random);
/* 158 */       for (Direction direction : DIRECTIONS) {
/* 159 */         if (blockState.hasProperty((Property)BlockStateProperties.FACING))
/* 160 */           blockState = (BlockState)blockState.setValue((Property)BlockStateProperties.FACING, (Comparable)direction); 
/* 163 */         BlockPos placePos = crystalPos.relative(direction);
/* 164 */         BlockState placeState = level.getBlockState(placePos);
/* 165 */         if (blockState.hasProperty((Property)BlockStateProperties.WATERLOGGED))
/* 166 */           blockState = (BlockState)blockState.setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(placeState.getFluidState().isSource())); 
/* 169 */         if (BuddingAmethystBlock.canClusterGrowAtState(placeState)) {
/* 170 */           safeSetBlock(level, placePos, blockState, canReplace);
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 176 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\GeodeFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */