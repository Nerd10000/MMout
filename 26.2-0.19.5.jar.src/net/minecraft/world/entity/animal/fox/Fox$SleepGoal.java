/*      */ package net.minecraft.world.entity.animal.fox;
/*      */ 
/*      */ import java.util.EnumSet;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ 
/*      */ class SleepGoal extends Fox.FoxBehaviorGoal {
/* 1116 */   private static final int WAIT_TIME_BEFORE_SLEEP = reducedTickDelay(140);
/*      */   
/*      */   private int countdown;
/*      */   
/*      */   public SleepGoal() {
/* 1120 */     this.countdown = Fox.access$100(paramFox).nextInt(WAIT_TIME_BEFORE_SLEEP);
/* 1121 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*      */   }
/*      */   
/*      */   public boolean canUse() {
/* 1126 */     if (Fox.this.xxa != 0.0F || Fox.this.yya != 0.0F || Fox.this.zza != 0.0F)
/* 1127 */       return false; 
/* 1129 */     return (canSleep() || Fox.this.isSleeping());
/*      */   }
/*      */   
/*      */   public boolean canContinueToUse() {
/* 1134 */     return canSleep();
/*      */   }
/*      */   
/*      */   private boolean canSleep() {
/* 1138 */     if (this.countdown > 0) {
/* 1139 */       this.countdown--;
/* 1140 */       return false;
/*      */     } 
/* 1142 */     return (Fox.this.level().isBrightOutside() && hasShelter() && !alertable() && !Fox.this.isInPowderSnow);
/*      */   }
/*      */   
/*      */   public void stop() {
/* 1147 */     this.countdown = Fox.access$200(Fox.this).nextInt(WAIT_TIME_BEFORE_SLEEP);
/* 1148 */     Fox.this.clearStates();
/*      */   }
/*      */   
/*      */   public void start() {
/* 1153 */     Fox.this.setSitting(false);
/* 1154 */     Fox.this.setIsCrouching(false);
/* 1155 */     Fox.this.setIsInterested(false);
/* 1156 */     Fox.this.setJumping(false);
/* 1157 */     Fox.this.setSleeping(true);
/* 1158 */     Fox.this.getNavigation().stop();
/* 1159 */     Fox.this.getMoveControl().setWantedPosition(Fox.this.getX(), Fox.this.getY(), Fox.this.getZ(), 0.0D);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$SleepGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */