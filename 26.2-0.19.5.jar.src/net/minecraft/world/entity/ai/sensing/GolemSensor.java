/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class GolemSensor extends Sensor<LivingEntity> {
/*    */   private static final int GOLEM_SCAN_RATE = 200;
/*    */   
/*    */   private static final int MEMORY_TIME_TO_LIVE = 599;
/*    */   
/*    */   public GolemSensor() {
/* 19 */     this(200);
/*    */   }
/*    */   
/*    */   public GolemSensor(int scanRate) {
/* 23 */     super(scanRate);
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 28 */     checkForNearbyGolem(body);
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 33 */     return Set.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.GOLEM_DETECTED_RECENTLY);
/*    */   }
/*    */   
/*    */   public static void checkForNearbyGolem(LivingEntity body) {
/* 40 */     Optional<List<LivingEntity>> livingEntitiesMemory = body.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/* 41 */     if (livingEntitiesMemory.isEmpty())
/*    */       return; 
/* 45 */     boolean golemPresent = ((List)livingEntitiesMemory.get()).stream().anyMatch(entity -> entity.is(EntityTypes.IRON_GOLEM));
/* 47 */     if (golemPresent)
/* 48 */       golemDetected(body); 
/*    */   }
/*    */   
/*    */   public static void golemDetected(LivingEntity body) {
/* 53 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.GOLEM_DETECTED_RECENTLY, Boolean.valueOf(true), 599L);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\GolemSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */