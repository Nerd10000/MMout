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
/*    */   private final VoxelShape CENTER_SUPPORT_SHAPE;
/*    */   
/*    */   null() {
/* 19 */     this.CENTER_SUPPORT_SHAPE = Block.column(2.0D, 0.0D, 10.0D);
/*    */   }
/*    */   
/*    */   public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 23 */     return !Shapes.joinIsNotEmpty(state.getBlockSupportShape(level, pos).getFaceShape(direction), this.CENTER_SUPPORT_SHAPE, BooleanOp.ONLY_SECOND);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SupportType$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */