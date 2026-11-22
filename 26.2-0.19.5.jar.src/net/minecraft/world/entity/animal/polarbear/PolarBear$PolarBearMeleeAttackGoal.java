/*     */ package net.minecraft.world.entity.animal.polarbear;
/*     */ 
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ 
/*     */ class PolarBearMeleeAttackGoal extends MeleeAttackGoal {
/*     */   public PolarBearMeleeAttackGoal() {
/* 327 */     super((PathfinderMob)paramPolarBear, 1.25D, true);
/*     */   }
/*     */   
/*     */   protected void checkAndPerformAttack(LivingEntity target) {
/* 332 */     if (canPerformAttack(target)) {
/* 333 */       resetAttackCooldown();
/* 334 */       this.mob.doHurtTarget(getServerLevel((Entity)this.mob), (Entity)target);
/* 335 */       PolarBear.this.setStanding(false);
/* 336 */     } else if (this.mob.distanceToSqr((Entity)target) < ((target.getBbWidth() + 3.0F) * (target.getBbWidth() + 3.0F))) {
/* 337 */       if (isTimeToAttack()) {
/* 338 */         PolarBear.this.setStanding(false);
/* 339 */         resetAttackCooldown();
/*     */       } 
/* 341 */       if (getTicksUntilNextAttack() <= 10) {
/* 342 */         PolarBear.this.setStanding(true);
/* 343 */         PolarBear.this.playWarningSound();
/*     */       } 
/*     */     } else {
/* 347 */       resetAttackCooldown();
/* 348 */       PolarBear.this.setStanding(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stop() {
/* 354 */     PolarBear.this.setStanding(false);
/* 355 */     super.stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\polarbear\PolarBear$PolarBearMeleeAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */