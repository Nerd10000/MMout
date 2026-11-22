/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class VexChargeAttackGoal extends Goal {
/*     */   public VexChargeAttackGoal() {
/* 272 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 277 */     LivingEntity target = Vex.this.getTarget();
/* 278 */     if (target != null && target.isAlive() && !Vex.this.getMoveControl().hasWanted() && Vex.access$000(Vex.this).nextInt(reducedTickDelay(7)) == 0)
/* 279 */       return (Vex.this.distanceToSqr((Entity)target) > 4.0D); 
/* 281 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 286 */     return (Vex.this.getMoveControl().hasWanted() && Vex.this.isCharging() && Vex.this.getTarget() != null && Vex.this.getTarget().isAlive());
/*     */   }
/*     */   
/*     */   public void start() {
/* 291 */     LivingEntity attackTarget = Vex.this.getTarget();
/* 292 */     if (attackTarget != null) {
/* 293 */       Vec3 eyePosition = attackTarget.getEyePosition();
/* 294 */       Vex.access$100(Vex.this).setWantedPosition(eyePosition.x, eyePosition.y, eyePosition.z, 1.0D);
/*     */     } 
/* 296 */     Vex.this.setIsCharging(true);
/* 297 */     Vex.this.playSound(SoundEvents.VEX_CHARGE, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 302 */     Vex.this.setIsCharging(false);
/*     */   }
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 307 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 312 */     LivingEntity attackTarget = Vex.this.getTarget();
/* 313 */     if (attackTarget == null)
/*     */       return; 
/* 316 */     if (Vex.this.getBoundingBox().intersects(attackTarget.getBoundingBox())) {
/* 317 */       Vex.this.doHurtTarget(getServerLevel(Vex.this.level()), (Entity)attackTarget);
/* 318 */       Vex.this.setIsCharging(false);
/*     */     } else {
/* 320 */       double distance = Vex.this.distanceToSqr((Entity)attackTarget);
/* 321 */       if (distance < 9.0D) {
/* 322 */         Vec3 eyePosition = attackTarget.getEyePosition();
/* 323 */         Vex.access$200(Vex.this).setWantedPosition(eyePosition.x, eyePosition.y, eyePosition.z, 1.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Vex$VexChargeAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */