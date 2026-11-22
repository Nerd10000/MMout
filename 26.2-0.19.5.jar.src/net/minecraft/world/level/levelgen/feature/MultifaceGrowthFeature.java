/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.MultifaceSpreadeableBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.MultifaceGrowthConfiguration;
/*    */ 
/*    */ public class MultifaceGrowthFeature extends Feature<MultifaceGrowthConfiguration> {
/*    */   public MultifaceGrowthFeature(Codec<MultifaceGrowthConfiguration> codec) {
/* 18 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<MultifaceGrowthConfiguration> context) {
/*    */     MultifaceSpreadeableBlock placerBlock;
/* 23 */     WorldGenLevel level = context.level();
/* 24 */     BlockPos origin = context.origin();
/* 25 */     RandomSource random = context.random();
/* 26 */     MultifaceGrowthConfiguration config = context.config();
/* 27 */     if (!isAirOrWater(level.getBlockState(origin)))
/* 28 */       return false; 
/* 32 */     Block block = config.placeBlock;
/* 32 */     if (block instanceof MultifaceSpreadeableBlock) {
/* 32 */       placerBlock = (MultifaceSpreadeableBlock)block;
/*    */     } else {
/* 33 */       return false;
/*    */     } 
/* 37 */     List<Direction> searchDirections = config.getShuffledDirections(random);
/* 38 */     if (placeGrowthIfPossible(placerBlock, level, origin, level.getBlockState(origin), config, random, searchDirections))
/* 39 */       return true; 
/* 44 */     BlockPos.MutableBlockPos pos = origin.mutable();
/* 45 */     for (Direction searchDirection : searchDirections) {
/* 46 */       pos.set((Vec3i)origin);
/* 47 */       List<Direction> placementDirections = config.getShuffledDirectionsExcept(random, searchDirection.getOpposite());
/* 48 */       for (int i = 0; i < config.searchRange; i++) {
/* 49 */         pos.setWithOffset((Vec3i)origin, searchDirection);
/* 50 */         BlockState state = level.getBlockState((BlockPos)pos);
/* 51 */         if (!isAirOrWater(state) && !state.is(config.placeBlock))
/*    */           break; 
/* 55 */         if (placeGrowthIfPossible(placerBlock, level, (BlockPos)pos, state, config, random, placementDirections))
/* 56 */           return true; 
/*    */       } 
/*    */     } 
/* 61 */     return false;
/*    */   }
/*    */   
/*    */   public static boolean placeGrowthIfPossible(MultifaceSpreadeableBlock placerBlock, WorldGenLevel level, BlockPos pos, BlockState oldState, MultifaceGrowthConfiguration config, RandomSource random, List<Direction> placementDirections) {
/* 65 */     BlockPos.MutableBlockPos mutable = pos.mutable();
/* 66 */     for (Direction placementDirection : placementDirections) {
/* 67 */       BlockState neighbourState = level.getBlockState((BlockPos)mutable.setWithOffset((Vec3i)pos, placementDirection));
/* 68 */       if (neighbourState.is(config.canBePlacedOn)) {
/* 69 */         BlockState newState = placerBlock.getStateForPlacement(oldState, (BlockGetter)level, pos, placementDirection);
/* 70 */         if (newState == null)
/* 71 */           return false; 
/* 73 */         level.setBlock(pos, newState, 3);
/* 74 */         level.getChunk(pos).markPosForPostProcessing(pos);
/* 75 */         if (random.nextFloat() < config.chanceOfSpreading)
/* 76 */           placerBlock.getSpreader().spreadFromFaceTowardRandomDirection(newState, (LevelAccessor)level, pos, placementDirection, random, true); 
/* 78 */         return true;
/*    */       } 
/*    */     } 
/* 81 */     return false;
/*    */   }
/*    */   
/*    */   private static boolean isAirOrWater(BlockState state) {
/* 85 */     return (state.isAir() || state.is(Blocks.WATER));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\MultifaceGrowthFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */