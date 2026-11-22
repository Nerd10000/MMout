/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class SpearApproach extends Behavior<PathfinderMob> {
/*    */   private final double speedModifierWhenRepositioning;
/*    */   
/*    */   private final float approachDistanceSq;
/*    */   
/*    */   public SpearApproach(double speedModifierWhenRepositioning, float approachDistance) {
/* 20 */     super(Map.of(MemoryModuleType.SPEAR_STATUS, MemoryStatus.VALUE_ABSENT));
/* 21 */     this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
/* 22 */     this.approachDistanceSq = approachDistance * approachDistance;
/*    */   }
/*    */   
/*    */   private boolean ableToAttack(PathfinderMob mob) {
/* 26 */     return (getTarget(mob) != null && mob.getMainHandItem().has(DataComponents.KINETIC_WEAPON));
/*    */   }
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob body) {
/* 31 */     return (ableToAttack(body) && !body.isUsingItem());
/*    */   }
/*    */   
/*    */   protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/* 36 */     body.setAggressive(true);
/* 37 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_STATUS, SpearAttack.SpearStatus.APPROACH);
/* 38 */     super.start(level, body, timestamp);
/*    */   }
/*    */   
/*    */   private LivingEntity getTarget(PathfinderMob mob) {
/* 42 */     return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
/*    */   }
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, PathfinderMob body, long timestamp) {
/* 47 */     return (ableToAttack(body) && farEnough(body));
/*    */   }
/*    */   
/*    */   private boolean farEnough(PathfinderMob mob) {
/* 52 */     LivingEntity target = getTarget(mob);
/* 53 */     double targetDistSqr = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
/* 54 */     return (targetDistSqr > this.approachDistanceSq);
/*    */   }
/*    */   
/*    */   protected void tick(ServerLevel level, PathfinderMob mob, long timestamp) {
/* 59 */     LivingEntity target = getTarget(mob);
/* 60 */     Entity mount = mob.getRootVehicle();
/* 61 */     float speedModifier = 1.0F;
/* 62 */     if (mount instanceof Mob) {
/* 62 */       Mob vehicleMob = (Mob)mount;
/* 63 */       speedModifier = vehicleMob.chargeSpeedModifier();
/*    */     } 
/* 65 */     mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)target, true));
/* 66 */     mob.getNavigation().moveTo((Entity)target, speedModifier * this.speedModifierWhenRepositioning);
/*    */   }
/*    */   
/*    */   protected void stop(ServerLevel level, PathfinderMob body, long timestamp) {
/* 71 */     body.getNavigation().stop();
/* 72 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_STATUS, SpearAttack.SpearStatus.CHARGING);
/*    */   }
/*    */   
/*    */   protected boolean timedOut(long timestamp) {
/* 77 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\SpearApproach.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */