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
/*    */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SpearRetreat extends Behavior<PathfinderMob> {
/*    */   public static final int MIN_COOLDOWN_DISTANCE = 9;
/*    */   
/*    */   public static final int MAX_COOLDOWN_DISTANCE = 11;
/*    */   
/*    */   public static final int MAX_FLEEING_TIME = 100;
/*    */   
/*    */   private final double speedModifierWhenRepositioning;
/*    */   
/*    */   public SpearRetreat(double speedModifierWhenRepositioning) {
/* 24 */     super(Map.of(MemoryModuleType.SPEAR_STATUS, MemoryStatus.VALUE_PRESENT), 100);
/* 25 */     this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
/*    */   }
/*    */   
/*    */   private LivingEntity getTarget(PathfinderMob mob) {
/* 29 */     return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
/*    */   }
/*    */   
/*    */   private boolean ableToAttack(PathfinderMob mob) {
/* 33 */     return (getTarget(mob) != null && mob.getMainHandItem().has(DataComponents.KINETIC_WEAPON));
/*    */   }
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob body) {
/* 38 */     if (!ableToAttack(body) || body.isUsingItem())
/* 39 */       return false; 
/* 41 */     if (body.getBrain().getMemory(MemoryModuleType.SPEAR_STATUS).orElse(SpearAttack.SpearStatus.APPROACH) != SpearAttack.SpearStatus.RETREAT)
/* 42 */       return false; 
/* 44 */     LivingEntity target = getTarget(body);
/* 45 */     double targetDistSqr = body.distanceToSqr(target.getX(), target.getY(), target.getZ());
/* 47 */     int mountDistance = body.isPassenger() ? 2 : 0;
/* 48 */     double distance = Math.sqrt(targetDistSqr);
/* 49 */     Vec3 awayPos = LandRandomPos.getPosAway(body, Math.max(0.0D, (9 + mountDistance) - distance), Math.max(1.0D, (11 + mountDistance) - distance), 7, target.position());
/* 51 */     if (awayPos == null)
/* 52 */       return false; 
/* 55 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_FLEEING_POSITION, awayPos);
/* 56 */     return true;
/*    */   }
/*    */   
/*    */   protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/* 61 */     body.setAggressive(true);
/* 62 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_FLEEING_TIME, Integer.valueOf(0));
/* 63 */     super.start(level, body, timestamp);
/*    */   }
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, PathfinderMob body, long timestamp) {
/* 68 */     return (((Integer)body.getBrain().getMemory(MemoryModuleType.SPEAR_FLEEING_TIME).orElse((T)Integer.valueOf(100))).intValue() < 100 && body
/* 69 */       .getBrain().getMemory(MemoryModuleType.SPEAR_FLEEING_POSITION).isPresent() && 
/* 70 */       !body.getNavigation().isDone() && 
/* 71 */       ableToAttack(body));
/*    */   }
/*    */   
/*    */   protected void tick(ServerLevel level, PathfinderMob mob, long timestamp) {
/* 76 */     LivingEntity target = getTarget(mob);
/* 77 */     Entity mount = mob.getRootVehicle();
/* 78 */     Mob vehicleMob = (Mob)mount;
/* 78 */     float speedModifier = (mount instanceof Mob) ? vehicleMob.chargeSpeedModifier() : 1.0F;
/* 80 */     mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)target, true));
/* 82 */     mob.getBrain().setMemory(MemoryModuleType.SPEAR_FLEEING_TIME, Integer.valueOf(((Integer)mob.getBrain().getMemory(MemoryModuleType.SPEAR_FLEEING_TIME).orElse(Integer.valueOf(0))).intValue() + 1));
/* 84 */     mob.getBrain().getMemory(MemoryModuleType.SPEAR_FLEEING_POSITION).ifPresent(fleePos -> mob.getNavigation().moveTo(fleePos.x, fleePos.y, fleePos.z, speedModifier * this.speedModifierWhenRepositioning));
/*    */   }
/*    */   
/*    */   protected void stop(ServerLevel level, PathfinderMob body, long timestamp) {
/* 91 */     body.getNavigation().stop();
/* 92 */     body.setAggressive(false);
/* 93 */     body.stopUsingItem();
/* 94 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_FLEEING_TIME);
/* 95 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_FLEEING_POSITION);
/* 96 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_STATUS);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\ai\behavior\SpearRetreat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */