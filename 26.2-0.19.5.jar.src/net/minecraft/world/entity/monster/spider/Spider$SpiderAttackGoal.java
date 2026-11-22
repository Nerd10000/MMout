/*     */ package net.minecraft.world.entity.monster.spider;
/*     */ 
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ 
/*     */ class SpiderAttackGoal extends MeleeAttackGoal {
/*     */   public SpiderAttackGoal(Spider mob) {
/* 219 */     super((PathfinderMob)mob, 1.0D, true);
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 224 */     return (super.canUse() && !this.mob.isVehicle());
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 229 */     float br = this.mob.getLightLevelDependentMagicValue();
/* 230 */     if (br >= 0.5F && this.mob.getRandom().nextInt(100) == 0) {
/* 231 */       this.mob.setTarget(null);
/* 232 */       return false;
/*     */     } 
/* 234 */     return super.canContinueToUse();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\spider\Spider$SpiderAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */