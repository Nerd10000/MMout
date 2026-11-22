/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.BooleanOp;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public enum SupportType {
/* 12 */   FULL {
/*    */     public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 15 */       return Block.isFaceFull(state.getBlockSupportShape(level, pos), direction);
/*    */     }
/*    */   },
/* 18 */   CENTER {
/* 19 */     private final VoxelShape CENTER_SUPPORT_SHAPE = Block.column(2.0D, 0.0D, 10.0D);
/*    */     
/*    */     public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 23 */       return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.CENTER_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
/*    */     }
/*    */   },
/* 26 */   RIGID {
/* 27 */     private final VoxelShape RIGID_SUPPORT_SHAPE = Shapes.join(
/* 28 */         Shapes.block(), 
/* 29 */         Block.column(12.0D, 0.0D, 16.0D), BooleanOp.ONLY_FIRST);
/*    */     
/*    */     public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 35 */       return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.RIGID_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract boolean isSupporting(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SupportType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */