/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Targeting;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ 
/*     */ class VexCopyOwnerTargetGoal extends TargetGoal {
/* 381 */   private final TargetingConditions copyOwnerTargeting = TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */   
/*     */   public VexCopyOwnerTargetGoal(PathfinderMob mob) {
/* 384 */     super((Mob)mob, false);
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 389 */     LivingEntity livingEntity = Vex.this.getOwner();
/* 389 */     if (livingEntity instanceof Targeting) {
/* 389 */       Targeting owner = (Targeting)livingEntity;
/* 389 */       if (owner.getTarget() != null && canAttack(owner.getTarget(), this.copyOwnerTargeting));
/*     */     } 
/* 389 */     return false;
/*     */   }
/*     */   
/*     */   public void start() {
/* 394 */     LivingEntity livingEntity = Vex.this.getOwner();
/* 394 */     Targeting owner = (Targeting)livingEntity;
/* 394 */     Vex.this.setTarget((livingEntity instanceof Targeting) ? owner.getTarget() : null);
/* 395 */     super.start();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Vex$VexCopyOwnerTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */