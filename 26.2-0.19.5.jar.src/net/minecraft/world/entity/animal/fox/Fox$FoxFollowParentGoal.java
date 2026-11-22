/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ 
/*      */ class FoxFollowParentGoal extends FollowParentGoal {
/*      */   private final Fox fox;
/*      */   
/*      */   public FoxFollowParentGoal(Fox fox, double speedModifier) {
/* 1540 */     super(fox, speedModifier);
/* 1541 */     this.fox = fox;
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1546 */     return (!this.fox.isDefending() && super.canUse());
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1551 */     return (!this.fox.isDefending() && super.canContinueToUse());
/*      */   }
/*      */   
/*      */   public void start() {
/* 1556 */     this.fox.clearStates();
/* 1557 */     super.start();
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxFollowParentGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */