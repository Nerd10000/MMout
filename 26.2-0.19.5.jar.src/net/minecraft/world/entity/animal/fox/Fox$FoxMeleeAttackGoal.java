/*     */ package net.minecraft.world.entity.animal.fox;
/*     */ 
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ 
/*     */ class FoxMeleeAttackGoal extends MeleeAttackGoal {
/*     */   public FoxMeleeAttackGoal(double speedModifier, boolean trackTarget) {
/* 907 */     super((PathfinderMob)paramFox, speedModifier, trackTarget);
/*     */   }
/*     */   
/*     */   protected void checkAndPerformAttack(LivingEntity target) {
/* 912 */     if (canPerformAttack(target)) {
/* 913 */       resetAttackCooldown();
/* 914 */       this.mob.doHurtTarget(getServerLevel((Entity)this.mob), (Entity)target);
/* 915 */       Fox.this.playSound(SoundEvents.FOX_BITE, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void start() {
/* 921 */     Fox.this.setIsInterested(false);
/* 922 */     super.start();
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 927 */     return (!Fox.this.isSitting() && !Fox.this.isSleeping() && !Fox.this.isCrouching() && !Fox.this.isFaceplanted() && super.canUse());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxMeleeAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */