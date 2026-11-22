/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface BlockEntityTicker<T extends BlockEntity> {
/*    */   void tick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, T paramT);
/*    */   
/*    */   default BlockEntityTicker<T> andThen(BlockEntityTicker<? super T> after) {
/* 12 */     return (level, pos, state, entity) -> {
/*    */         tick(level, pos, state, (T)entity);
/*    */         after.tick(level, pos, state, entity);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\BlockEntityTicker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */