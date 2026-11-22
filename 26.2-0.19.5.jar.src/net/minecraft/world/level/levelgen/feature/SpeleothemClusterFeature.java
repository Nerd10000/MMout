/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.ClampedNormalFloat;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.LevelSimulatedReader;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Column;
/*     */ import net.minecraft.world.level.levelgen.feature.configurations.SpeleothemClusterConfiguration;
/*     */ 
/*     */ public class SpeleothemClusterFeature extends Feature<SpeleothemClusterConfiguration> {
/*     */   public SpeleothemClusterFeature(Codec<SpeleothemClusterConfiguration> codec) {
/*  28 */     super(codec);
/*     */   }
/*     */   
/*     */   public boolean place(FeaturePlaceContext<SpeleothemClusterConfiguration> context) {
/*  33 */     WorldGenLevel level = context.level();
/*  34 */     BlockPos origin = context.origin();
/*  35 */     SpeleothemClusterConfiguration config = context.config();
/*  36 */     RandomSource random = context.random();
/*  38 */     if (!SpeleothemUtils.isEmptyOrWater((LevelAccessor)level, origin))
/*  39 */       return false; 
/*  43 */     int height = config.height().sample(random);
/*  45 */     float wetness = config.wetness().sample(random);
/*  46 */     float density = config.density().sample(random);
/*  51 */     int xRadius = config.radius().sample(random);
/*  52 */     int zRadius = config.radius().sample(random);
/*  53 */     for (int dx = -xRadius; dx <= xRadius; dx++) {
/*  54 */       for (int dz = -zRadius; dz <= zRadius; dz++) {
/*  55 */         double chanceOfStalagmiteOrStalactite = getChanceOfStalagmiteOrStalactite(xRadius, zRadius, dx, dz, config);
/*  56 */         BlockPos pos = origin.offset(dx, 0, dz);
/*  57 */         placeColumn(level, random, pos, dx, dz, wetness, chanceOfStalagmiteOrStalactite, height, density, config);
/*     */       } 
/*     */     } 
/*  60 */     return true;
/*     */   }
/*     */   
/*     */   private void placeColumn(WorldGenLevel level, RandomSource random, BlockPos pos, int dx, int dz, float chanceOfWater, double chanceOfStalagmiteOrStalactite, int clusterHeight, float density, SpeleothemClusterConfiguration config) {
/*     */     Column column;
/*     */     int stalactiteHeight, stalagmiteHeight, actualStalactiteHeight, actualStalagmiteHeight;
/*  65 */     Optional<Column> baseColumn = Column.scan((LevelSimulatedReader)level, pos, config.floorToCeilingSearchRange(), SpeleothemUtils::isEmptyOrWater, SpeleothemUtils::isNeitherEmptyNorWater);
/*  66 */     if (baseColumn.isEmpty())
/*     */       return; 
/*  70 */     OptionalInt ceiling = ((Column)baseColumn.get()).getCeiling();
/*  71 */     OptionalInt baseFloor = ((Column)baseColumn.get()).getFloor();
/*  73 */     if (ceiling.isEmpty() && baseFloor.isEmpty())
/*     */       return; 
/*  79 */     boolean wantPool = (random.nextFloat() < chanceOfWater);
/*  81 */     if (wantPool && baseFloor.isPresent() && canPlacePool(level, pos.atY(baseFloor.getAsInt()), config)) {
/*  83 */       int baseFloorY = baseFloor.getAsInt();
/*  84 */       column = ((Column)baseColumn.get()).withFloor(OptionalInt.of(baseFloorY - 1));
/*  85 */       level.setBlock(pos.atY(baseFloorY), Blocks.WATER.defaultBlockState(), 2);
/*     */     } else {
/*  87 */       column = baseColumn.get();
/*     */     } 
/*  90 */     OptionalInt floor = column.getFloor();
/*  94 */     boolean wantStalactite = (random.nextDouble() < chanceOfStalagmiteOrStalactite);
/*  95 */     if (ceiling.isPresent() && wantStalactite && !isLava((LevelReader)level, pos.atY(ceiling.getAsInt()))) {
/*  96 */       int maxHeightForThisColumn, ceilingThickness = config.speleothemBlockLayerThickness().sample(random);
/*  97 */       replaceBlocksWithBaseBlocks(level, pos.atY(ceiling.getAsInt()), ceilingThickness, Direction.UP, config);
/*  99 */       if (floor.isPresent()) {
/* 100 */         maxHeightForThisColumn = Math.min(clusterHeight, ceiling.getAsInt() - floor.getAsInt());
/*     */       } else {
/* 102 */         maxHeightForThisColumn = clusterHeight;
/*     */       } 
/* 104 */       stalactiteHeight = getSpeleothemHeight(random, dx, dz, density, maxHeightForThisColumn, config);
/*     */     } else {
/* 106 */       stalactiteHeight = 0;
/*     */     } 
/* 111 */     boolean wantStalagmite = (random.nextDouble() < chanceOfStalagmiteOrStalactite);
/* 112 */     if (floor.isPresent() && wantStalagmite && !isLava((LevelReader)level, pos.atY(floor.getAsInt()))) {
/* 113 */       int floorThickness = config.speleothemBlockLayerThickness().sample(random);
/* 114 */       replaceBlocksWithBaseBlocks(level, pos.atY(floor.getAsInt()), floorThickness, Direction.DOWN, config);
/* 116 */       if (ceiling.isPresent()) {
/* 117 */         stalagmiteHeight = Math.max(0, stalactiteHeight + Mth.randomBetweenInclusive(random, -config.maxStalagmiteStalactiteHeightDiff(), config.maxStalagmiteStalactiteHeightDiff()));
/*     */       } else {
/* 120 */         stalagmiteHeight = getSpeleothemHeight(random, dx, dz, density, clusterHeight, config);
/*     */       } 
/*     */     } else {
/* 123 */       stalagmiteHeight = 0;
/*     */     } 
/* 129 */     if (ceiling.isPresent() && floor.isPresent() && ceiling.getAsInt() - stalactiteHeight <= floor.getAsInt() + stalagmiteHeight) {
/* 132 */       int floorY = floor.getAsInt();
/* 133 */       int ceilingY = ceiling.getAsInt();
/* 134 */       int lowestStalactiteBottom = Math.max(ceilingY - stalactiteHeight, floorY + 1);
/* 135 */       int highestStalagmiteTop = Math.min(floorY + stalagmiteHeight, ceilingY - 1);
/* 136 */       int actualStalactiteBottom = Mth.randomBetweenInclusive(random, lowestStalactiteBottom, highestStalagmiteTop + 1);
/* 137 */       int actualStalagmiteTop = actualStalactiteBottom - 1;
/* 138 */       actualStalactiteHeight = ceilingY - actualStalactiteBottom;
/* 139 */       actualStalagmiteHeight = actualStalagmiteTop - floorY;
/*     */     } else {
/* 141 */       actualStalactiteHeight = stalactiteHeight;
/* 142 */       actualStalagmiteHeight = stalagmiteHeight;
/*     */     } 
/* 149 */     boolean mergeTips = (random.nextBoolean() && actualStalactiteHeight > 0 && actualStalagmiteHeight > 0 && column.getHeight().isPresent() && actualStalactiteHeight + actualStalagmiteHeight == column.getHeight().getAsInt());
/* 151 */     if (ceiling.isPresent())
/* 152 */       SpeleothemUtils.growSpeleothem((LevelAccessor)level, pos.atY(ceiling.getAsInt() - 1), Direction.DOWN, actualStalactiteHeight, mergeTips, config.baseBlock().getBlock(), config.pointedBlock().getBlock(), config.replaceableBlocks()); 
/* 154 */     if (floor.isPresent())
/* 155 */       SpeleothemUtils.growSpeleothem((LevelAccessor)level, pos.atY(floor.getAsInt() + 1), Direction.UP, actualStalagmiteHeight, mergeTips, config.baseBlock().getBlock(), config.pointedBlock().getBlock(), config.replaceableBlocks()); 
/*     */   }
/*     */   
/*     */   private boolean isLava(LevelReader level, BlockPos pos) {
/* 160 */     return level.getBlockState(pos).is(Blocks.LAVA);
/*     */   }
/*     */   
/*     */   private int getSpeleothemHeight(RandomSource random, int dx, int dz, float density, int maxHeight, SpeleothemClusterConfiguration config) {
/* 164 */     if (random.nextFloat() > density)
/* 165 */       return 0; 
/* 168 */     int distanceFromCenter = Math.abs(dx) + Math.abs(dz);
/* 171 */     float heightMean = (float)Mth.clampedMap(distanceFromCenter, 0.0D, config.maxDistanceFromCenterAffectingHeightBias(), maxHeight / 2.0D, 0.0D);
/* 172 */     return (int)randomBetweenBiased(random, 0.0F, maxHeight, heightMean, config.heightDeviation());
/*     */   }
/*     */   
/*     */   private boolean canPlacePool(WorldGenLevel level, BlockPos pos, SpeleothemClusterConfiguration config) {
/* 176 */     BlockState state = level.getBlockState(pos);
/* 177 */     if (state.is(Blocks.WATER) || state.is(config.baseBlock().getBlock()) || state.is(config.pointedBlock().getBlock()))
/* 178 */       return false; 
/* 180 */     if (level.getBlockState(pos.above()).getFluidState().is(FluidTags.WATER))
/* 181 */       return false; 
/* 184 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 185 */       if (!canBeAdjacentToWater((LevelAccessor)level, pos.relative(direction)))
/* 186 */         return false; 
/*     */     } 
/* 189 */     return canBeAdjacentToWater((LevelAccessor)level, pos.below());
/*     */   }
/*     */   
/*     */   private boolean canBeAdjacentToWater(LevelAccessor level, BlockPos pos) {
/* 193 */     BlockState state = level.getBlockState(pos);
/* 194 */     return (state.is(BlockTags.BASE_STONE_OVERWORLD) || state.getFluidState().is(FluidTags.WATER));
/*     */   }
/*     */   
/*     */   private void replaceBlocksWithBaseBlocks(WorldGenLevel level, BlockPos firstPos, int maxCount, Direction direction, SpeleothemClusterConfiguration config) {
/* 198 */     BlockPos.MutableBlockPos pos = firstPos.mutable();
/* 199 */     for (int i = 0; i < maxCount; i++) {
/* 200 */       if (!SpeleothemUtils.placeBaseBlockIfPossible((LevelAccessor)level, (BlockPos)pos, config.baseBlock().getBlock(), config.replaceableBlocks()))
/*     */         return; 
/* 203 */       pos.move(direction);
/*     */     } 
/*     */   }
/*     */   
/*     */   private double getChanceOfStalagmiteOrStalactite(int xRadius, int zRadius, int dx, int dz, SpeleothemClusterConfiguration config) {
/* 209 */     int xDistanceFromEdge = xRadius - Math.abs(dx);
/* 210 */     int zDistanceFromEdge = zRadius - Math.abs(dz);
/* 211 */     int distanceFromEdge = Math.min(xDistanceFromEdge, zDistanceFromEdge);
/* 213 */     return Mth.clampedMap(distanceFromEdge, 0.0F, config.maxDistanceFromEdgeAffectingChanceOfSpeleothem(), config.chanceOfSpeleothemAtMaxDistanceFromCenter(), 1.0F);
/*     */   }
/*     */   
/*     */   private static float randomBetweenBiased(RandomSource random, float min, float maxExclusive, float mean, float deviation) {
/* 217 */     return ClampedNormalFloat.sample(random, mean, deviation, min, maxExclusive);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\SpeleothemClusterFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */