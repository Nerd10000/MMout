/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ 
/*      */ class BeeAttackGoal extends MeleeAttackGoal {
/*      */   public BeeAttackGoal(PathfinderMob mob, double speedModifier, boolean trackTarget) {
/* 1428 */     super(mob, speedModifier, trackTarget);
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1433 */     return (super.canUse() && Bee.this.isAngry() && !Bee.this.hasStung());
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1438 */     return (super.canContinueToUse() && Bee.this.isAngry() && !Bee.this.hasStung());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeeAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */