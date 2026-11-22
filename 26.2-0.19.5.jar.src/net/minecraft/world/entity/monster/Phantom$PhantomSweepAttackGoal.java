/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.feline.Cat;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class PhantomSweepAttackGoal extends Phantom.PhantomMoveTargetGoal {
/*     */   private static final int CAT_SEARCH_TICK_DELAY = 20;
/*     */   
/*     */   private boolean isScaredOfCat;
/*     */   
/*     */   private int catSearchTick;
/*     */   
/*     */   public boolean canUse() {
/* 378 */     return (Phantom.this.getTarget() != null && Phantom.this.attackPhase == Phantom.AttackPhase.SWOOP);
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 383 */     LivingEntity target = Phantom.this.getTarget();
/* 384 */     if (target == null)
/* 385 */       return false; 
/* 387 */     if (!target.isAlive())
/* 388 */       return false; 
/* 390 */     if (target instanceof Player) {
/* 390 */       Player player = (Player)target;
/* 390 */       if (target.isSpectator() || player.isCreative())
/* 391 */         return false; 
/*     */     } 
/* 394 */     if (!canUse())
/* 395 */       return false; 
/* 398 */     if (Phantom.this.tickCount > this.catSearchTick) {
/* 399 */       this.catSearchTick = Phantom.this.tickCount + 20;
/* 400 */       List<Cat> cats = Phantom.this.level().getEntitiesOfClass(Cat.class, Phantom.this.getBoundingBox().inflate(16.0D), EntitySelector.ENTITY_STILL_ALIVE);
/* 401 */       for (Cat cat : cats)
/* 402 */         cat.hiss(); 
/* 404 */       this.isScaredOfCat = !cats.isEmpty();
/*     */     } 
/* 407 */     return !this.isScaredOfCat;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 412 */     Phantom.this.setTarget(null);
/* 413 */     Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 418 */     LivingEntity target = Phantom.this.getTarget();
/* 419 */     if (target == null)
/*     */       return; 
/* 422 */     Phantom.this.moveTargetPoint = new Vec3(target.getX(), target.getY(0.5D), target.getZ());
/* 424 */     if (Phantom.this.getBoundingBox().inflate(0.20000000298023224D).intersects(target.getBoundingBox())) {
/* 425 */       Phantom.this.doHurtTarget(getServerLevel(Phantom.this.level()), (Entity)target);
/* 426 */       Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/* 427 */       if (!Phantom.this.isSilent())
/* 428 */         Phantom.this.level().levelEvent(1039, Phantom.this.blockPosition(), 0); 
/* 430 */     } else if (Phantom.this.horizontalCollision || Phantom.this.hurtTime > 0) {
/* 431 */       Phantom.this.attackPhase = Phantom.AttackPhase.CIRCLE;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Phantom$PhantomSweepAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */