/*     */ package net.minecraft.world.entity.animal.bee;
/*     */ 
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ 
/*     */ class BeeHurtByOtherGoal extends HurtByTargetGoal {
/*     */   public BeeHurtByOtherGoal(Bee bee) {
/* 756 */     super((PathfinderMob)bee, new Class[0]);
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 762 */     return (Bee.this.isAngry() && super.canContinueToUse());
/*     */   }
/*     */   
/*     */   protected void alertOther(Mob other, LivingEntity hurtByMob) {
/* 767 */     if (other instanceof Bee && this.mob.hasLineOfSight((Entity)hurtByMob))
/* 768 */       other.setTarget(hurtByMob); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeeHurtByOtherGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */