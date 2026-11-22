/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class GhastShootFireballGoal extends Goal {
/*     */   private final Ghast ghast;
/*     */   
/*     */   public int chargeTime;
/*     */   
/*     */   public GhastShootFireballGoal(Ghast ghast) {
/* 442 */     this.ghast = ghast;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 447 */     return (this.ghast.getTarget() != null);
/*     */   }
/*     */   
/*     */   public void start() {
/* 452 */     this.chargeTime = 0;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 457 */     this.ghast.setCharging(false);
/*     */   }
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 462 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 467 */     LivingEntity target = this.ghast.getTarget();
/* 468 */     if (target == null)
/*     */       return; 
/* 472 */     double maxDist = 64.0D;
/* 473 */     if (target.distanceToSqr((Entity)this.ghast) < 4096.0D && this.ghast.hasLineOfSight((Entity)target)) {
/* 474 */       Level level = this.ghast.level();
/* 476 */       this.chargeTime++;
/* 477 */       if (this.chargeTime == 10 && !this.ghast.isSilent())
/* 478 */         level.levelEvent(null, 1015, this.ghast.blockPosition(), 0); 
/* 480 */       if (this.chargeTime == 20) {
/* 481 */         double d = 4.0D;
/* 482 */         Vec3 viewVector = this.ghast.getViewVector(1.0F);
/* 484 */         double xdd = target.getX() - this.ghast.getX() + viewVector.x * 4.0D;
/* 485 */         double ydd = target.getY(0.5D) - 0.5D + this.ghast.getY(0.5D);
/* 486 */         double zdd = target.getZ() - this.ghast.getZ() + viewVector.z * 4.0D;
/* 487 */         Vec3 direction = new Vec3(xdd, ydd, zdd);
/* 489 */         if (!this.ghast.isSilent())
/* 490 */           level.levelEvent(null, 1016, this.ghast.blockPosition(), 0); 
/* 492 */         LargeFireball entity = new LargeFireball(level, (LivingEntity)this.ghast, direction.normalize(), this.ghast.getExplosionPower());
/* 493 */         entity.setPos(this.ghast.getX() + viewVector.x * 4.0D, this.ghast.getY(0.5D) + 0.5D, entity.getZ() + viewVector.z * 4.0D);
/* 494 */         level.addFreshEntity((Entity)entity);
/* 495 */         this.chargeTime = -40;
/*     */       } 
/* 497 */     } else if (this.chargeTime > 0) {
/* 498 */       this.chargeTime--;
/*     */     } 
/* 500 */     this.ghast.setCharging((this.chargeTime > 10));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Ghast$GhastShootFireballGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */