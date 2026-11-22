/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public interface ChangeOverTimeBlock<T extends Enum<T>> {
/*    */   public static final int SCAN_DISTANCE = 4;
/*    */   
/*    */   default void changeOverTime(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 20 */     float eachBlockOncePerDayChance = 0.05688889F;
/* 21 */     if (random.nextFloat() < 0.05688889F)
/* 22 */       getNextState(state, level, pos, random).ifPresent(weatheredState -> level.setBlockAndUpdate(pos, weatheredState)); 
/*    */   }
/*    */   
/*    */   default Optional<BlockState> getNextState(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 31 */     int ownAge = getAge().ordinal();
/* 32 */     int sameAgeCount = 0;
/* 33 */     int olderCount = 0;
/* 34 */     for (BlockPos blockPos : BlockPos.withinManhattan(pos, 4, 4, 4)) {
/* 35 */       int manhattanDistance = blockPos.distManhattan((Vec3i)pos);
/* 36 */       if (manhattanDistance > 4)
/*    */         break; 
/* 39 */       if (blockPos.equals(pos))
/*    */         continue; 
/* 43 */       Block block = level.getBlockState(blockPos).getBlock();
/* 43 */       if (block instanceof ChangeOverTimeBlock) {
/* 43 */         ChangeOverTimeBlock<?> neighborBlock = (ChangeOverTimeBlock)block;
/* 45 */         Enum<?> neighborAge = (Enum<?>)neighborBlock.getAge();
/* 46 */         if (getAge().getClass() != neighborAge.getClass())
/*    */           continue; 
/* 49 */         int foundAge = neighborAge.ordinal();
/* 50 */         if (foundAge < ownAge)
/* 51 */           return Optional.empty(); 
/* 52 */         if (foundAge > ownAge) {
/* 53 */           olderCount++;
/*    */           continue;
/*    */         } 
/* 55 */         sameAgeCount++;
/*    */       } 
/*    */     } 
/* 60 */     float chance = (olderCount + 1) / (olderCount + sameAgeCount + 1);
/* 61 */     float actualChance = chance * chance * getChanceModifier();
/* 63 */     if (random.nextFloat() < actualChance)
/* 64 */       return getNext(state); 
/* 67 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   Optional<BlockState> getNext(BlockState paramBlockState);
/*    */   
/*    */   float getChanceModifier();
/*    */   
/*    */   T getAge();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\ChangeOverTimeBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */