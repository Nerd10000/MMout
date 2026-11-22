/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.goal.StrollThroughVillageGoal;
/*      */ 
/*      */ class FoxStrollThroughVillageGoal extends StrollThroughVillageGoal {
/*      */   public FoxStrollThroughVillageGoal(int searchRadius, int interval) {
/* 1359 */     super((PathfinderMob)paramFox, interval);
/*      */   }
/*      */   
/*      */   public void start() {
/* 1364 */     Fox.this.clearStates();
/* 1365 */     super.start();
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1370 */     return (super.canUse() && canFoxMove());
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1375 */     return (super.canContinueToUse() && canFoxMove());
/*      */   }
/*      */   
/*      */   private boolean canFoxMove() {
/* 1379 */     return (!Fox.this.isSleeping() && !Fox.this.isSitting() && !Fox.this.isDefending() && Fox.this.getTarget() == null);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FoxStrollThroughVillageGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */