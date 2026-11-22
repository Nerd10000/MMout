/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.BaseSpawner;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.SpawnData;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ class null extends BaseSpawner {
/*    */   null(SpawnerBlockEntity this$0) {}
/*    */   
/*    */   public void broadcastEvent(Level level, BlockPos pos, int id) {
/* 25 */     level.blockEvent(pos, Blocks.SPAWNER, id, 0);
/*    */   }
/*    */   
/*    */   public void setNextSpawnData(Level level, BlockPos pos, SpawnData nextSpawnData) {
/* 30 */     super.setNextSpawnData(level, pos, nextSpawnData);
/* 31 */     if (level != null) {
/* 32 */       BlockState state = level.getBlockState(pos);
/* 33 */       level.sendBlockUpdated(pos, state, state, 260);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\SpawnerBlockEntity$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */