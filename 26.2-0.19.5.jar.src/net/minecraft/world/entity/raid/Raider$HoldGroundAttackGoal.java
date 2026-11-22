/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*     */ 
/*     */ public class HoldGroundAttackGoal extends Goal {
/*     */   private final Raider mob;
/*     */   
/*     */   private final float hostileRadiusSqr;
/*     */   
/* 430 */   public final TargetingConditions shoutTargeting = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */   
/*     */   public HoldGroundAttackGoal(AbstractIllager mob, float hostileRadius) {
/* 433 */     this.mob = (Raider)mob;
/* 434 */     this.hostileRadiusSqr = hostileRadius * hostileRadius;
/* 435 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 441 */     LivingEntity lastHurtByMob = this.mob.getLastHurtByMob();
/* 442 */     return (this.mob.getCurrentRaid() == null && Raider.access$200(this.mob) && this.mob.getTarget() != null && !this.mob.isAggressive() && (lastHurtByMob == null || !lastHurtByMob.is(EntityTypes.PLAYER)));
/*     */   }
/*     */   
/*     */   public void start() {
/* 447 */     super.start();
/* 448 */     this.mob.getNavigation().stop();
/* 450 */     List<Raider> nearbyEntities = getServerLevel((Entity)this.mob).getNearbyEntities(Raider.class, this.shoutTargeting, (LivingEntity)this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/* 451 */     for (Raider entity : nearbyEntities)
/* 452 */       entity.setTarget(this.mob.getTarget()); 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 458 */     super.stop();
/* 460 */     LivingEntity target = this.mob.getTarget();
/* 461 */     if (target != null) {
/* 462 */       List<Raider> nearbyEntities = getServerLevel((Entity)this.mob).getNearbyEntities(Raider.class, this.shoutTargeting, (LivingEntity)this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/* 463 */       for (Raider entity : nearbyEntities) {
/* 464 */         entity.setTarget(target);
/* 465 */         entity.setAggressive(true);
/*     */       } 
/* 467 */       this.mob.setAggressive(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 473 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 478 */     LivingEntity target = this.mob.getTarget();
/* 479 */     if (target == null)
/*     */       return; 
/* 483 */     if (this.mob.distanceToSqr((Entity)target) > this.hostileRadiusSqr) {
/* 484 */       this.mob.getLookControl().setLookAt((Entity)target, 30.0F, 30.0F);
/* 486 */       if (Raider.access$300(this.mob).nextInt(50) == 0)
/* 487 */         this.mob.playAmbientSound(); 
/*     */     } else {
/* 490 */       this.mob.setAggressive(true);
/*     */     } 
/* 493 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\raid\Raider$HoldGroundAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */