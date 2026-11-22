/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import java.util.EnumSet;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ 
/*      */ class FaceplantGoal extends Goal {
/*      */   private int countdown;
/*      */   
/*      */   public FaceplantGoal() {
/* 1317 */     setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.JUMP, Goal.Flag.MOVE));
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1322 */     return Fox.this.isFaceplanted();
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1327 */     return (canUse() && this.countdown > 0);
/*      */   }
/*      */   
/*      */   public void start() {
/* 1332 */     this.countdown = adjustedTickDelay(40);
/*      */   }
/*      */   
/*      */   public void stop() {
/* 1337 */     Fox.this.setFaceplanted(false);
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1342 */     this.countdown--;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$FaceplantGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */