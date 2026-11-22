/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ 
/*     */ class DrownedAttackGoal extends ZombieAttackGoal {
/*     */   private final Drowned drowned;
/*     */   
/*     */   public DrownedAttackGoal(Drowned drowned, double speedModifier, boolean trackTarget) {
/* 523 */     super(drowned, speedModifier, trackTarget);
/* 524 */     this.drowned = drowned;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 529 */     return (super.canUse() && this.drowned.okTarget(this.drowned.getTarget()));
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 534 */     return (super.canContinueToUse() && this.drowned.okTarget(this.drowned.getTarget()));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Drowned$DrownedAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */