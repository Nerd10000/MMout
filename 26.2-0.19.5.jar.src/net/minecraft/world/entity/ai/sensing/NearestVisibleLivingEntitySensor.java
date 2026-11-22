/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public abstract class NearestVisibleLivingEntitySensor extends Sensor<LivingEntity> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 19 */     return Set.of(getMemoryToSet(), MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 24 */     body.getBrain().setMemory(getMemoryToSet(), getNearestEntity(level, body));
/*    */   }
/*    */   
/*    */   private Optional<LivingEntity> getNearestEntity(ServerLevel level, LivingEntity body) {
/* 28 */     return getVisibleEntities(body).flatMap(livingEntities -> livingEntities.findClosest(()));
/*    */   }
/*    */   
/*    */   protected Optional<NearestVisibleLivingEntities> getVisibleEntities(LivingEntity body) {
/* 33 */     return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */   
/*    */   protected abstract boolean isMatchingEntity(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2);
/*    */   
/*    */   protected abstract MemoryModuleType<LivingEntity> getMemoryToSet();
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\NearestVisibleLivingEntitySensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */