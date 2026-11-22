/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class AdultSensor extends Sensor<LivingEntity> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 17 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 24 */     body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).ifPresent(livingEntities -> setNearestVisibleAdult(body, livingEntities));
/*    */   }
/*    */   
/*    */   protected void setNearestVisibleAdult(LivingEntity body, NearestVisibleLivingEntities visibleLivingEntities) {
/* 30 */     Optional<LivingEntity> adult = visibleLivingEntities.findClosest(entity -> (entity.getType() == body.getType() && !entity.isBaby()));
/* 31 */     body.getBrain().setMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT, adult);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\AdultSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */