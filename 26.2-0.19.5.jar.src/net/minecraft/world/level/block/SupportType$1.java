/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ enum null {
/*    */   public boolean isSupporting(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 15 */     return Block.isFaceFull(state.getBlockSupportShape(level, pos), direction);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\SupportType$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */