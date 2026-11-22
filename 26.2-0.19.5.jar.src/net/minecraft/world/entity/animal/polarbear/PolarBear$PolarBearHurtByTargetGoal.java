/*     */ package net.minecraft.world.entity.animal.polarbear;
/*     */ 
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ 
/*     */ class PolarBearHurtByTargetGoal extends HurtByTargetGoal {
/*     */   public PolarBearHurtByTargetGoal() {
/* 271 */     super((PathfinderMob)paramPolarBear, new Class[0]);
/*     */   }
/*     */   
/*     */   public void start() {
/* 276 */     super.start();
/* 277 */     if (PolarBear.this.isBaby()) {
/* 278 */       alertOthers();
/* 279 */       stop();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void alertOther(Mob other, LivingEntity hurtByMob) {
/* 285 */     if (other instanceof PolarBear && 
/* 286 */       !other.isBaby())
/* 287 */       super.alertOther(other, hurtByMob); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\polarbear\PolarBear$PolarBearHurtByTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */