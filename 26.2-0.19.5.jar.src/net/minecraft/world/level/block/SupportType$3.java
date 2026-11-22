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
/*    */ enum null {
/*    */   private final VoxelShape RIGID_SUPPORT_SHAPE;
/*    */   
/*    */   null() {
/* 27 */     this.RIGID_SUPPORT_SHAPE = Shapes.join(
/* 28 */         Shapes.block(), 
/* 29 */         Block.column(12.0D, 0.0D, 16.0D), BooleanOp.ONLY_FIRST);
/*    */   }
/*    */   
/*    */   public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 35 */     return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.RIGID_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SupportType$3.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */