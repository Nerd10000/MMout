/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class AxolotlAttackablesSensor extends NearestVisibleLivingEntitySensor {
/*    */   public static final float TARGET_DETECTION_DISTANCE = 8.0F;
/*    */   
/*    */   protected boolean isMatchingEntity(ServerLevel level, LivingEntity body, LivingEntity mob) {
/* 17 */     return (isClose(body, mob) && mob.isInWater() && (
/* 18 */       isHostileTarget(mob) || isHuntTarget(body, mob)) && 
/* 19 */       Sensor.isEntityAttackable(level, body, mob));
/*    */   }
/*    */   
/*    */   private boolean isHuntTarget(LivingEntity body, LivingEntity mob) {
/* 23 */     return (!body.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN) && mob.is(EntityTypeTags.AXOLOTL_HUNT_TARGETS));
/*    */   }
/*    */   
/*    */   private boolean isHostileTarget(LivingEntity mob) {
/* 27 */     return mob.is(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES);
/*    */   }
/*    */   
/*    */   private boolean isClose(LivingEntity body, LivingEntity mob) {
/* 31 */     return (mob.distanceToSqr((Entity)body) <= 64.0D);
/*    */   }
/*    */   
/*    */   protected MemoryModuleType<LivingEntity> getMemoryToSet() {
/* 36 */     return MemoryModuleType.NEAREST_ATTACKABLE;
/*    */   }
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 41 */     return (Set<MemoryModuleType<?>>)Sets.union(super.requires(), Set.of(MemoryModuleType.HAS_HUNTING_COOLDOWN));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\sensing\AxolotlAttackablesSensor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */