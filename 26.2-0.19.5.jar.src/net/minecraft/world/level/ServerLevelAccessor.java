/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public interface ServerLevelAccessor extends LevelAccessor {
/*    */   ServerLevel getLevel();
/*    */   
/*    */   DifficultyInstance getCurrentDifficultyAt(BlockPos paramBlockPos);
/*    */   
/*    */   default void addFreshEntityWithPassengers(Entity entity) {
/* 15 */     entity.getSelfAndPassengers().forEach(this::addFreshEntity);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\ServerLevelAccessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */