/*     */ package net.minecraft.world.entity.monster.spider;
/*     */ 
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ 
/*     */ class SpiderTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
/*     */   public SpiderTargetGoal(Spider mob, Class<T> targetType) {
/* 240 */     super((Mob)mob, targetType, true);
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 245 */     float br = this.mob.getLightLevelDependentMagicValue();
/* 246 */     if (br >= 0.5F)
/* 247 */       return false; 
/* 250 */     return super.canUse();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\spider\Spider$SpiderTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */