/*     */ package net.minecraft.world.entity.animal.bee;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ class BeeBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {
/*     */   public BeeBecomeAngryTargetGoal(Bee bee) {
/* 775 */     super((Mob)bee, Player.class, 10, true, false, bee::isAngryAt);
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 780 */     return (beeCanTarget() && super.canUse());
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 785 */     boolean beeCanTarget = beeCanTarget();
/* 786 */     if (!beeCanTarget || this.mob.getTarget() == null) {
/* 787 */       this.targetMob = null;
/* 788 */       return false;
/*     */     } 
/* 790 */     return super.canContinueToUse();
/*     */   }
/*     */   
/*     */   private boolean beeCanTarget() {
/* 794 */     Bee bee = (Bee)this.mob;
/* 795 */     return (bee.isAngry() && !bee.hasStung());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeeBecomeAngryTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */