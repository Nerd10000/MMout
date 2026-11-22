/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.animal.frog.Frog;
/*    */ 
/*    */ public class FrogAttackablesSensor extends NearestVisibleLivingEntitySensor {
/*    */   public static final float TARGET_DETECTION_DISTANCE = 10.0F;
/*    */   
/*    */   protected boolean isMatchingEntity(ServerLevel level, LivingEntity body, LivingEntity mob) {
/* 19 */     if (Sensor.isEntityAttackable(level, body, mob) && 
/* 20 */       Frog.canEat(mob) && 
/* 21 */       !isUnreachableAttackTarget(body, mob))
/* 23 */       return mob.closerThan((Entity)body, 10.0D); 
/* 25 */     return false;
/*    */   }
/*    */   
/*    */   private boolean isUnreachableAttackTarget(LivingEntity body, LivingEntity mob) {
/* 29 */     List<UUID> unreachableAttackTargets = body.getBrain().getMemory(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS).orElseGet(java.util.ArrayList::new);
/* 30 */     return unreachableAttackTargets.contains(mob.getUUID());
/*    */   }
/*    */   
/*    */   protected MemoryModuleType<LivingEntity> getMemoryToSet() {
/* 35 */     return MemoryModuleType.NEAREST_ATTACKABLE;
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 40 */     return (Set<MemoryModuleType<?>>)Sets.union(super.requires(), Set.of(MemoryModuleType.UNREACHABLE_TONGUE_TARGETS));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\FrogAttackablesSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */