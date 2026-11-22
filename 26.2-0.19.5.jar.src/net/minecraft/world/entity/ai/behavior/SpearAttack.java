/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SpearAttack extends Behavior<PathfinderMob> {
/*     */   public static final int MIN_REPOSITION_DISTANCE = 6;
/*     */   
/*     */   public static final int MAX_REPOSITION_DISTANCE = 7;
/*     */   
/*     */   private final double speedModifierWhenCharging;
/*     */   
/*     */   private final double speedModifierWhenRepositioning;
/*     */   
/*     */   private final float targetInRangeRadiusSq;
/*     */   
/*     */   public SpearAttack(double speedModifierWhenCharging, double speedModifierWhenRepositioning, float targetInRangeRadius) {
/*  28 */     super(Map.of(MemoryModuleType.SPEAR_STATUS, MemoryStatus.VALUE_PRESENT));
/*  29 */     this.speedModifierWhenCharging = speedModifierWhenCharging;
/*  30 */     this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
/*  31 */     this.targetInRangeRadiusSq = targetInRangeRadius * targetInRangeRadius;
/*     */   }
/*     */   
/*     */   private LivingEntity getTarget(PathfinderMob mob) {
/*  35 */     return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
/*     */   }
/*     */   
/*     */   private boolean ableToAttack(PathfinderMob mob) {
/*  39 */     return (getTarget(mob) != null && mob.getMainHandItem().has(DataComponents.KINETIC_WEAPON));
/*     */   }
/*     */   
/*     */   private int getKineticWeaponUseDuration(PathfinderMob mob) {
/*  43 */     return ((Integer)Optional.<KineticWeapon>ofNullable((KineticWeapon)mob.getMainHandItem().get(DataComponents.KINETIC_WEAPON)).map(KineticWeapon::computeDamageUseDuration).orElse(Integer.valueOf(0))).intValue();
/*     */   }
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob body) {
/*  48 */     return (body.getBrain().getMemory(MemoryModuleType.SPEAR_STATUS).orElse(SpearStatus.APPROACH) == SpearStatus.CHARGING && 
/*  49 */       ableToAttack(body) && !body.isUsingItem());
/*     */   }
/*     */   
/*     */   protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/*  54 */     body.setAggressive(true);
/*  55 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_ENGAGE_TIME, Integer.valueOf(getKineticWeaponUseDuration(body)));
/*  57 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_CHARGE_POSITION);
/*  58 */     body.startUsingItem(InteractionHand.MAIN_HAND);
/*  59 */     super.start(level, body, timestamp);
/*     */   }
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, PathfinderMob body, long timestamp) {
/*  64 */     return (((Integer)body.getBrain().getMemory(MemoryModuleType.SPEAR_ENGAGE_TIME).orElse(Integer.valueOf(0))).intValue() > 0 && ableToAttack(body));
/*     */   }
/*     */   
/*     */   protected void tick(ServerLevel level, PathfinderMob mob, long timestamp) {
/*  69 */     LivingEntity target = getTarget(mob);
/*  70 */     double targetDistSqr = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
/*  71 */     Entity mount = mob.getRootVehicle();
/*  72 */     float speedModifier = 1.0F;
/*  73 */     if (mount instanceof Mob) {
/*  73 */       Mob vehicleMob = (Mob)mount;
/*  74 */       speedModifier = vehicleMob.chargeSpeedModifier();
/*     */     } 
/*  76 */     int mountDistance = mob.isPassenger() ? 2 : 0;
/*  78 */     mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)target, true));
/*  80 */     mob.getBrain().setMemory(MemoryModuleType.SPEAR_ENGAGE_TIME, Integer.valueOf(((Integer)mob.getBrain().getMemory(MemoryModuleType.SPEAR_ENGAGE_TIME).orElse(Integer.valueOf(0))).intValue() - 1));
/*  82 */     Vec3 awayPos = mob.getBrain().getMemory(MemoryModuleType.SPEAR_CHARGE_POSITION).orElse(null);
/*  85 */     if (awayPos != null) {
/*  86 */       mob.getNavigation().moveTo(awayPos.x, awayPos.y, awayPos.z, speedModifier * this.speedModifierWhenRepositioning);
/*  87 */       if (mob.getNavigation().isDone())
/*  88 */         mob.getBrain().eraseMemory(MemoryModuleType.SPEAR_CHARGE_POSITION); 
/*     */     } else {
/*  91 */       mob.getNavigation().moveTo((Entity)target, speedModifier * this.speedModifierWhenCharging);
/*  93 */       if (targetDistSqr < this.targetInRangeRadiusSq || mob.getNavigation().isDone()) {
/*  94 */         double distance = Math.sqrt(targetDistSqr);
/*  96 */         Vec3 newAwayPos = LandRandomPos.getPosAway(mob, (6 + mountDistance) - distance, (7 + mountDistance) - distance, 7, target.position());
/*  97 */         mob.getBrain().setMemory(MemoryModuleType.SPEAR_CHARGE_POSITION, newAwayPos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void stop(ServerLevel level, PathfinderMob body, long timestamp) {
/* 104 */     body.getNavigation().stop();
/* 105 */     body.stopUsingItem();
/* 106 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_CHARGE_POSITION);
/* 107 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_ENGAGE_TIME);
/* 108 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_STATUS, SpearStatus.RETREAT);
/*     */   }
/*     */   
/*     */   protected boolean timedOut(long timestamp) {
/* 113 */     return false;
/*     */   }
/*     */   
/*     */   public enum SpearStatus {
/* 117 */     APPROACH, CHARGING, RETREAT;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\SpearAttack.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */