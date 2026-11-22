/*    */ package net.minecraft.world.level.redstone;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public abstract class RedstoneWireEvaluator {
/*    */   protected final RedStoneWireBlock wireBlock;
/*    */   
/*    */   protected RedstoneWireEvaluator(RedStoneWireBlock wireBlock) {
/* 14 */     this.wireBlock = wireBlock;
/*    */   }
/*    */   
/*    */   public abstract void updatePowerStrength(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Orientation paramOrientation, boolean paramBoolean);
/*    */   
/*    */   protected int getBlockSignal(Level level, BlockPos pos) {
/* 20 */     return this.wireBlock.getBlockSignal(level, pos);
/*    */   }
/*    */   
/*    */   protected int getWireSignal(BlockPos pos, BlockState state) {
/* 24 */     return state.is(this.wireBlock) ? ((Integer)state.getValue((Property)RedStoneWireBlock.POWER)).intValue() : 0;
/*    */   }
/*    */   
/*    */   protected int getIncomingWireSignal(Level level, BlockPos pos) {
/* 28 */     int wireSignal = 0;
/* 29 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 30 */       BlockPos neighborPos = pos.relative(direction);
/* 31 */       BlockState neighborState = level.getBlockState(neighborPos);
/* 33 */       wireSignal = Math.max(wireSignal, getWireSignal(neighborPos, neighborState));
/* 35 */       BlockPos abovePos = pos.above();
/* 36 */       if (neighborState.isRedstoneConductor((BlockGetter)level, neighborPos) && !level.getBlockState(abovePos).isRedstoneConductor((BlockGetter)level, abovePos)) {
/* 37 */         BlockPos aboveNeighborPos = neighborPos.above();
/* 38 */         wireSignal = Math.max(wireSignal, getWireSignal(aboveNeighborPos, level.getBlockState(aboveNeighborPos)));
/*    */         continue;
/*    */       } 
/* 39 */       if (!neighborState.isRedstoneConductor((BlockGetter)level, neighborPos)) {
/* 40 */         BlockPos belowNeighborPos = neighborPos.below();
/* 41 */         wireSignal = Math.max(wireSignal, getWireSignal(belowNeighborPos, level.getBlockState(belowNeighborPos)));
/*    */       } 
/*    */     } 
/* 44 */     return Math.max(0, wireSignal - 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\redstone\RedstoneWireEvaluator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */