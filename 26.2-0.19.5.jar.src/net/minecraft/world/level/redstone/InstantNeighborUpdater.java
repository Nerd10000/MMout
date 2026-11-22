/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class InstantNeighborUpdater implements NeighborUpdater {
/*    */   private final Level level;
/*    */   
/*    */   public InstantNeighborUpdater(Level level) {
/* 14 */     this.level = level;
/*    */   }
/*    */   
/*    */   public void shapeUpdate(Direction direction, BlockState neighborState, BlockPos pos, BlockPos neighborPos, int updateFlags, int updateLimit) {
/* 19 */     NeighborUpdater.executeShapeUpdate((LevelAccessor)this.level, direction, pos, neighborPos, neighborState, updateFlags, updateLimit - 1);
/*    */   }
/*    */   
/*    */   public void neighborChanged(BlockPos pos, Block changedBlock, Orientation orientation) {
/* 24 */     BlockState state = this.level.getBlockState(pos);
/* 25 */     neighborChanged(state, pos, changedBlock, orientation, false);
/*    */   }
/*    */   
/*    */   public void neighborChanged(BlockState state, BlockPos pos, Block changedBlock, Orientation orientation, boolean movedByPiston) {
/* 30 */     NeighborUpdater.executeUpdate(this.level, state, pos, changedBlock, orientation, movedByPiston);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\redstone\InstantNeighborUpdater.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */