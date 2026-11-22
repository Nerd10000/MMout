/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import java.util.function.BiPredicate;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class MobSensor<T extends LivingEntity> extends Sensor<T> {
/*    */   private final BiPredicate<T, LivingEntity> mobTest;
/*    */   
/*    */   private final Predicate<T> readyTest;
/*    */   
/*    */   private final MemoryModuleType<Boolean> toSet;
/*    */   
/*    */   private final int memoryTimeToLive;
/*    */   
/*    */   public MobSensor(int scanRate, BiPredicate<T, LivingEntity> mobTest, Predicate<T> readyTest, MemoryModuleType<Boolean> toSet, int memoryTimeToLive) {
/* 20 */     super(scanRate);
/* 21 */     this.mobTest = mobTest;
/* 22 */     this.readyTest = readyTest;
/* 23 */     this.toSet = toSet;
/* 24 */     this.memoryTimeToLive = memoryTimeToLive;
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, T body) {
/* 29 */     if (!this.readyTest.test(body)) {
/* 30 */       clearMemory(body);
/*    */     } else {
/* 32 */       checkForMobsNearby(body);
/*    */     } 
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 38 */     return Set.of(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/*    */   }
/*    */   
/*    */   public void checkForMobsNearby(T body) {
/* 42 */     Optional<List<LivingEntity>> livingEntitiesMemory = body.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/* 43 */     if (livingEntitiesMemory.isEmpty())
/*    */       return; 
/* 46 */     boolean mobPresent = ((List)livingEntitiesMemory.get()).stream().anyMatch(entity -> this.mobTest.test((T)body, entity));
/* 48 */     if (mobPresent)
/* 49 */       mobDetected(body); 
/*    */   }
/*    */   
/*    */   public void mobDetected(T body) {
/* 54 */     body.getBrain().setMemoryWithExpiry(this.toSet, Boolean.valueOf(true), this.memoryTimeToLive);
/*    */   }
/*    */   
/*    */   public void clearMemory(T body) {
/* 58 */     body.getBrain().eraseMemory(this.toSet);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\MobSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */