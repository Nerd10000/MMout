/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public interface SpawnPlacementType {
/*    */   boolean isSpawnPositionOk(LevelReader paramLevelReader, BlockPos paramBlockPos, EntityType<?> paramEntityType);
/*    */   
/*    */   default BlockPos adjustSpawnPosition(LevelReader level, BlockPos candidate) {
/* 11 */     return candidate;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\SpawnPlacementType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */