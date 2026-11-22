/*     */ package net.minecraft.world.entity.animal.panda;
/*     */ 
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ 
/*     */ class PandaAttackGoal extends MeleeAttackGoal {
/*     */   private final Panda panda;
/*     */   
/*     */   public PandaAttackGoal(Panda mob, double speedModifier, boolean trackTarget) {
/* 828 */     super((PathfinderMob)mob, speedModifier, trackTarget);
/* 829 */     this.panda = mob;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 834 */     return (this.panda.canPerformAction() && super.canUse());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */