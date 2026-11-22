/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ import net.minecraft.world.level.material.Fluids;
/*    */ 
/*    */ public enum EmptyBlockGetter implements BlockGetter {
/* 12 */   INSTANCE;
/*    */   
/*    */   public BlockEntity getBlockEntity(BlockPos pos) {
/* 16 */     return null;
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 21 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */   
/*    */   public FluidState getFluidState(BlockPos pos) {
/* 26 */     return Fluids.EMPTY.defaultFluidState();
/*    */   }
/*    */   
/*    */   public int getMinY() {
/* 31 */     return 0;
/*    */   }
/*    */   
/*    */   public int getHeight() {
/* 36 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\EmptyBlockGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */