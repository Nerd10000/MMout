/*    */ package net.minecraft.world.level.levelgen.feature;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.List;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
/*    */ 
/*    */ public class CoralClawFeature extends CoralFeature {
/*    */   public CoralClawFeature(Codec<NoneFeatureConfiguration> codec) {
/* 17 */     super(codec);
/*    */   }
/*    */   
/*    */   protected boolean placeFeature(LevelAccessor level, RandomSource random, BlockPos origin, BlockState state) {
/* 22 */     if (!placeCoralBlock(level, random, origin, state))
/* 23 */       return false; 
/* 26 */     Direction clawDirection = Direction.Plane.HORIZONTAL.getRandomDirection(random);
/* 27 */     int nBranches = random.nextInt(2) + 2;
/* 29 */     List<Direction> possibleDirections = Util.toShuffledList(Stream.of(new Direction[] { clawDirection, clawDirection.getClockWise(), clawDirection.getCounterClockWise() }, ), random);
/* 30 */     List<Direction> branchDirections = possibleDirections.subList(0, nBranches);
/* 32 */     for (Direction branchDirection : branchDirections) {
/*    */       int inwayLenth;
/*    */       Direction segmentDirection;
/* 33 */       BlockPos.MutableBlockPos mutPos = origin.mutable();
/* 34 */       int sidewayLength = random.nextInt(2) + 1;
/* 38 */       mutPos.move(branchDirection);
/* 39 */       if (branchDirection == clawDirection) {
/* 40 */         segmentDirection = clawDirection;
/* 41 */         inwayLenth = random.nextInt(3) + 2;
/*    */       } else {
/* 43 */         mutPos.move(Direction.UP);
/* 46 */         Direction[] segmentPossibleDirections = { branchDirection, Direction.UP };
/* 47 */         segmentDirection = (Direction)Util.getRandom((Object[])segmentPossibleDirections, random);
/* 48 */         inwayLenth = random.nextInt(3) + 3;
/*    */       } 
/*    */       int i;
/* 51 */       for (i = 0; i < sidewayLength && 
/* 52 */         placeCoralBlock(level, random, (BlockPos)mutPos, state); i++)
/* 55 */         mutPos.move(segmentDirection); 
/* 57 */       mutPos.move(segmentDirection.getOpposite());
/* 58 */       mutPos.move(Direction.UP);
/* 60 */       for (i = 0; i < inwayLenth; i++) {
/* 61 */         mutPos.move(clawDirection);
/* 62 */         if (!placeCoralBlock(level, random, (BlockPos)mutPos, state))
/*    */           break; 
/* 66 */         if (random.nextFloat() < 0.25F)
/* 67 */           mutPos.move(Direction.UP); 
/*    */       } 
/*    */     } 
/* 72 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\feature\CoralClawFeature.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */