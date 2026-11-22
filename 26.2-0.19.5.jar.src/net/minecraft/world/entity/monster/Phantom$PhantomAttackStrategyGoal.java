/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ 
/*     */ class PhantomAttackStrategyGoal extends Goal {
/*     */   private int nextSweepTick;
/*     */   
/*     */   public boolean canUse() {
/* 441 */     LivingEntity target = Phantom.this.getTarget();
/* 442 */     if (target != null)
/* 443 */       return Phantom.this.canAttack(getServerLevel(Phantom.this.level()), target, TargetingConditions.DEFAULT); 
/* 445 */     return false;
/*     */   }
/*     */   
/*     */   public void start() {
/* 450 */     this.nextSweepTick = adjustedTickDelay(10);
/* 451 */     Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/* 452 */     setAnchorAboveTarget();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 457 */     if (Phantom.this.anchorPoint != null)
/* 458 */       Phantom.this.anchorPoint = Phantom.this.level().getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, Phantom.this.anchorPoint).above(10 + Phantom.access$800(Phantom.this).nextInt(20)); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 464 */     if (Phantom.this.attackPhase == Phantom.AttackPhase.CIRCLE) {
/* 465 */       this.nextSweepTick--;
/* 466 */       if (this.nextSweepTick <= 0) {
/* 467 */         Phantom.this.attackPhase = Phantom.AttackPhase.SWOOP;
/* 468 */         setAnchorAboveTarget();
/* 469 */         this.nextSweepTick = adjustedTickDelay((8 + Phantom.access$900(Phantom.this).nextInt(4)) * 20);
/* 471 */         Phantom.this.playSound(SoundEvents.PHANTOM_SWOOP, 10.0F, 0.95F + Phantom.access$1000(Phantom.this).nextFloat() * 0.1F);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setAnchorAboveTarget() {
/* 477 */     if (Phantom.this.anchorPoint == null)
/*     */       return; 
/* 480 */     Phantom.this.anchorPoint = Phantom.this.getTarget().blockPosition().above(20 + Phantom.access$1100(Phantom.this).nextInt(20));
/* 481 */     if (Phantom.this.anchorPoint.getY() < Phantom.this.level().getSeaLevel())
/* 482 */       Phantom.this.anchorPoint = new BlockPos(Phantom.this.anchorPoint.getX(), Phantom.this.level().getSeaLevel() + 1, Phantom.this.anchorPoint.getZ()); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Phantom$PhantomAttackStrategyGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */