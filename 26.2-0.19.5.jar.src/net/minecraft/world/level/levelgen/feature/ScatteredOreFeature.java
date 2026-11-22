/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
/*    */ 
/*    */ public class ScatteredOreFeature extends Feature<OreConfiguration> {
/*    */   private static final int MAX_DIST_FROM_ORIGIN = 7;
/*    */   
/*    */   public ScatteredOreFeature(Codec<OreConfiguration> codec) {
/* 16 */     super(codec);
/*    */   }
/*    */   
/*    */   public boolean place(FeaturePlaceContext<OreConfiguration> context) {
/* 22 */     WorldGenLevel level = context.level();
/* 23 */     RandomSource random = context.random();
/* 24 */     OreConfiguration config = context.config();
/* 25 */     BlockPos origin = context.origin();
/* 26 */     int numberOfTries = random.nextInt(config.size + 1);
/* 27 */     BlockPos.MutableBlockPos targetPos = new BlockPos.MutableBlockPos();
/* 29 */     for (int i = 0; i < numberOfTries; i++) {
/* 31 */       offsetTargetPos(targetPos, random, origin, Math.min(i, 7));
/* 33 */       BlockState blockState = level.getBlockState((BlockPos)targetPos);
/* 34 */       for (OreConfiguration.TargetBlockState targetState : config.targetStates) {
/* 35 */         Objects.requireNonNull(level);
/* 35 */         if (OreFeature.canPlaceOre(blockState, level::getBlockState, random, config, targetState, targetPos)) {
/* 36 */           level.setBlock((BlockPos)targetPos, targetState.state, 2);
/*    */           break;
/*    */         } 
/*    */       } 
/*    */     } 
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   private void offsetTargetPos(BlockPos.MutableBlockPos targetPos, RandomSource random, BlockPos origin, int maxDistFromOriginForThisTry) {
/* 45 */     int xd = getRandomPlacementInOneAxisRelativeToOrigin(random, maxDistFromOriginForThisTry);
/* 46 */     int yd = getRandomPlacementInOneAxisRelativeToOrigin(random, maxDistFromOriginForThisTry);
/* 47 */     int zd = getRandomPlacementInOneAxisRelativeToOrigin(random, maxDistFromOriginForThisTry);
/* 48 */     targetPos.setWithOffset((Vec3i)origin, xd, yd, zd);
/*    */   }
/*    */   
/*    */   private int getRandomPlacementInOneAxisRelativeToOrigin(RandomSource random, int maxDistanceFromOrigin) {
/* 52 */     return Math.round((random.nextFloat() - random.nextFloat()) * maxDistanceFromOrigin);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\ScatteredOreFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */