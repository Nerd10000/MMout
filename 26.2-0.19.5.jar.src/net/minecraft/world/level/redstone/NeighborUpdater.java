/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.CrashReport;
/*    */ import net.minecraft.CrashReportCategory;
/*    */ import net.minecraft.ReportedException;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.LevelHeightAccessor;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public interface NeighborUpdater {
/* 19 */   public static final Direction[] UPDATE_ORDER = new Direction[] { Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH };
/*    */   
/*    */   void shapeUpdate(Direction paramDirection, BlockState paramBlockState, BlockPos paramBlockPos1, BlockPos paramBlockPos2, int paramInt1, int paramInt2);
/*    */   
/*    */   void neighborChanged(BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation);
/*    */   
/*    */   void neighborChanged(BlockState paramBlockState, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean);
/*    */   
/*    */   default void updateNeighborsAtExceptFromFacing(BlockPos pos, Block block, Direction skipDirection, Orientation orientation) {
/* 28 */     for (Direction direction : UPDATE_ORDER) {
/* 29 */       if (direction != skipDirection)
/* 30 */         neighborChanged(pos.relative(direction), block, null); 
/*    */     } 
/*    */   }
/*    */   
/*    */   static void executeShapeUpdate(LevelAccessor level, Direction direction, BlockPos pos, BlockPos neighborPos, BlockState neighborState, int updateFlags, int updateLimit) {
/* 36 */     BlockState currentState = level.getBlockState(pos);
/* 37 */     if ((updateFlags & 0x80) != 0 && currentState.is(Blocks.REDSTONE_WIRE))
/*    */       return; 
/*    */     try {
/* 41 */       BlockState newState = currentState.updateShape((LevelReader)level, (ScheduledTickAccess)level, pos, direction, neighborPos, neighborState, level.getRandom());
/* 42 */       Block.updateOrDestroy(currentState, newState, level, pos, updateFlags, updateLimit);
/* 43 */     } catch (Throwable t) {
/* 44 */       CrashReport report = CrashReport.forThrowable(t, "Exception while updating neighbour shapes");
/* 45 */       CrashReportCategory ownCategory = report.addCategory("Block being updated");
/* 46 */       CrashReportCategory.populateBlockDetails(ownCategory, (LevelHeightAccessor)level, pos, currentState);
/* 48 */       CrashReportCategory neighborCategory = report.addCategory("Neighbor block");
/* 49 */       CrashReportCategory.populateBlockDetails(neighborCategory, (LevelHeightAccessor)level, neighborPos, neighborState);
/* 51 */       throw new ReportedException(report);
/*    */     } 
/*    */   }
/*    */   
/*    */   static void executeUpdate(Level level, BlockState state, BlockPos pos, Block changedBlock, Orientation orientation, boolean movedByPiston) {
/*    */     try {
/* 57 */       state.handleNeighborChanged(level, pos, changedBlock, orientation, movedByPiston);
/* 58 */     } catch (Throwable t) {
/* 59 */       CrashReport report = CrashReport.forThrowable(t, "Exception while updating neighbours");
/* 60 */       CrashReportCategory category = report.addCategory("Block being updated");
/* 62 */       category.setDetail("Source block type", () -> {
/*    */             try {
/*    */               return String.format(Locale.ROOT, "ID #%s (%s // %s)", new Object[] { BuiltInRegistries.BLOCK.getKey(changedBlock), changedBlock.getDescriptionId(), changedBlock.getClass().getCanonicalName() });
/* 65 */             } catch (Throwable ignored) {
/*    */               return "ID #" + String.valueOf(BuiltInRegistries.BLOCK.getKey(changedBlock));
/*    */             } 
/*    */           });
/* 70 */       CrashReportCategory.populateBlockDetails(category, (LevelHeightAccessor)level, pos, state);
/* 72 */       throw new ReportedException(report);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\redstone\NeighborUpdater.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */