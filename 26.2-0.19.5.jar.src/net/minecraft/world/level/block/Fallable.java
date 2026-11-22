/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public interface Fallable {
/*    */   default void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedBlock, FallingBlockEntity entity) {}
/*    */   
/*    */   default void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {}
/*    */   
/*    */   default DamageSource getFallDamageSource(Entity entity) {
/* 17 */     return entity.damageSources().fallingBlock(entity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\Fallable.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */