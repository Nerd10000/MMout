/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class IsInWaterSensor extends Sensor<LivingEntity> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 14 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.IS_IN_WATER);
/*    */   }
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 19 */     if (body.isInWater()) {
/* 20 */       body.getBrain().setMemory(MemoryModuleType.IS_IN_WATER, Unit.INSTANCE);
/*    */     } else {
/* 22 */       body.getBrain().eraseMemory(MemoryModuleType.IS_IN_WATER);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\IsInWaterSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */