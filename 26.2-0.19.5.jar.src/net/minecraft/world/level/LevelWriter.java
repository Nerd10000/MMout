/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public interface LevelWriter {
/*    */   boolean setBlock(BlockPos paramBlockPos, BlockState paramBlockState, int paramInt1, int paramInt2);
/*    */   
/*    */   default boolean setBlock(BlockPos pos, BlockState blockState, int updateFlags) {
/* 14 */     return setBlock(pos, blockState, updateFlags, 512);
/*    */   }
/*    */   
/*    */   boolean removeBlock(BlockPos paramBlockPos, boolean paramBoolean);
/*    */   
/*    */   default boolean destroyBlock(BlockPos pos, boolean dropResources) {
/* 21 */     return destroyBlock(pos, dropResources, null);
/*    */   }
/*    */   
/*    */   default boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker) {
/* 26 */     return destroyBlock(pos, dropResources, breaker, 512);
/*    */   }
/*    */   
/*    */   boolean destroyBlock(BlockPos paramBlockPos, boolean paramBoolean, Entity paramEntity, int paramInt);
/*    */   
/*    */   default boolean addFreshEntity(Entity entity) {
/* 33 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\LevelWriter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */