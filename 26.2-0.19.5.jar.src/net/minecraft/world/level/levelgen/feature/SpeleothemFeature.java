/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SpeleothemConfiguration;
/*    */ 
/*    */ public class SpeleothemFeature extends Feature<SpeleothemConfiguration> {
/*    */   public SpeleothemFeature(Codec<SpeleothemConfiguration> codec) {
/* 15 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<SpeleothemConfiguration> context) {
/* 20 */     WorldGenLevel worldGenLevel = context.level();
/* 21 */     BlockPos pos = context.origin();
/* 22 */     RandomSource random = context.random();
/* 23 */     SpeleothemConfiguration config = context.config();
/* 24 */     Optional<Direction> tipDirection = getTipDirection((LevelAccessor)worldGenLevel, pos, random, config);
/* 26 */     if (tipDirection.isEmpty())
/* 27 */       return false; 
/* 30 */     BlockPos rootPos = pos.relative(((Direction)tipDirection.get()).getOpposite());
/* 32 */     createPatchOfBaseBlocks((LevelAccessor)worldGenLevel, random, rootPos, config);
/* 34 */     int height = (random.nextFloat() < config.chanceOfTallerGeneration() && SpeleothemUtils.isEmptyOrWater(worldGenLevel.getBlockState(pos.relative(tipDirection.get())))) ? 2 : 1;
/* 36 */     SpeleothemUtils.growSpeleothem((LevelAccessor)worldGenLevel, pos, tipDirection.get(), height, false, config.baseBlock().getBlock(), config.pointedBlock().getBlock(), config.replaceableBlocks());
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   private static Optional<Direction> getTipDirection(LevelAccessor level, BlockPos pos, RandomSource random, SpeleothemConfiguration config) {
/* 41 */     boolean canPlaceAbove = SpeleothemUtils.isBase(level.getBlockState(pos.above()), config.baseBlock().getBlock(), config.replaceableBlocks());
/* 42 */     boolean canPlaceBelow = SpeleothemUtils.isBase(level.getBlockState(pos.below()), config.baseBlock().getBlock(), config.replaceableBlocks());
/* 44 */     if (canPlaceAbove && canPlaceBelow)
/* 45 */       return Optional.of(random.nextBoolean() ? Direction.DOWN : Direction.UP); 
/* 47 */     if (canPlaceAbove)
/* 48 */       return Optional.of(Direction.DOWN); 
/* 50 */     if (canPlaceBelow)
/* 51 */       return Optional.of(Direction.UP); 
/* 53 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   private static void createPatchOfBaseBlocks(LevelAccessor level, RandomSource random, BlockPos pos, SpeleothemConfiguration config) {
/* 57 */     SpeleothemUtils.placeBaseBlockIfPossible(level, pos, config.baseBlock().getBlock(), config.replaceableBlocks());
/* 59 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 60 */       if (random.nextFloat() > config.chanceOfDirectionalSpread())
/*    */         continue; 
/* 64 */       BlockPos pos1 = pos.relative(direction);
/* 65 */       SpeleothemUtils.placeBaseBlockIfPossible(level, pos1, config.baseBlock().getBlock(), config.replaceableBlocks());
/* 66 */       if (random.nextFloat() > config.chanceOfSpreadRadius2())
/*    */         continue; 
/* 69 */       BlockPos pos2 = pos1.relative(Direction.getRandom(random));
/* 70 */       SpeleothemUtils.placeBaseBlockIfPossible(level, pos2, config.baseBlock().getBlock(), config.replaceableBlocks());
/* 71 */       if (random.nextFloat() > config.chanceOfSpreadRadius3())
/*    */         continue; 
/* 74 */       BlockPos pos3 = pos2.relative(Direction.getRandom(random));
/* 75 */       SpeleothemUtils.placeBaseBlockIfPossible(level, pos3, config.baseBlock().getBlock(), config.replaceableBlocks());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\SpeleothemFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */