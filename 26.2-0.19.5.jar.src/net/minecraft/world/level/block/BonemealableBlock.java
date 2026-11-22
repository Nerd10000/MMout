/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public interface BonemealableBlock {
/*    */   boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   static boolean hasSpreadableNeighbourPos(LevelReader level, BlockPos pos, BlockState blockToPlace) {
/* 28 */     return getSpreadableNeighbourPos(Direction.Plane.HORIZONTAL.stream().toList(), level, pos, blockToPlace).isPresent();
/*    */   }
/*    */   
/*    */   static Optional<BlockPos> findSpreadableNeighbourPos(Level level, BlockPos pos, BlockState blockToPlace) {
/* 32 */     return getSpreadableNeighbourPos(Direction.Plane.HORIZONTAL.shuffledCopy(level.getRandom()), (LevelReader)level, pos, blockToPlace);
/*    */   }
/*    */   
/*    */   private static Optional<BlockPos> getSpreadableNeighbourPos(List<Direction> directions, LevelReader level, BlockPos pos, BlockState blockToPlace) {
/* 36 */     for (Direction direction : directions) {
/* 37 */       BlockPos neighbourPos = pos.relative(direction);
/* 38 */       if (level.isEmptyBlock(neighbourPos) && blockToPlace.canSurvive(level, neighbourPos))
/* 39 */         return Optional.of(neighbourPos); 
/*    */     } 
/* 42 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   default BlockPos getParticlePos(BlockPos blockPos) {
/* 46 */     switch (getType().ordinal()) {
/*    */       default:
/* 46 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */         break;
/*    */     } 
/* 46 */     return 
/*    */       
/* 48 */       blockPos;
/*    */   }
/*    */   
/*    */   default Type getType() {
/* 53 */     return Type.GROWER;
/*    */   }
/*    */   
/*    */   public enum Type {
/* 57 */     NEIGHBOR_SPREADER, GROWER;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\BonemealableBlock.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */