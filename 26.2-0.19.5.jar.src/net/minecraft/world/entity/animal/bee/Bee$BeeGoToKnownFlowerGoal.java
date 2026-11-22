/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import java.util.EnumSet;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ 
/*      */ public class BeeGoToKnownFlowerGoal extends Bee.BaseBeeGoal {
/*      */   private static final int MAX_TRAVELLING_TICKS = 2400;
/*      */   
/*      */   private int travellingTicks;
/*      */   
/*      */   public BeeGoToKnownFlowerGoal() {
/* 1023 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */   }
/*      */   
/*      */   public boolean canBeeUse() {
/* 1028 */     return (Bee.this.savedFlowerPos != null && 
/* 1029 */       !Bee.this.hasHome() && 
/* 1030 */       wantsToGoToKnownFlower() && 
/* 1031 */       !Bee.this.closerThan(Bee.this.savedFlowerPos, 2));
/*      */   }
/*      */   
/*      */   public boolean canBeeContinueToUse() {
/* 1036 */     return canBeeUse();
/*      */   }
/*      */   
/*      */   public void start() {
/* 1041 */     this.travellingTicks = 0;
/* 1042 */     super.start();
/*      */   }
/*      */   
/*      */   public void stop() {
/* 1047 */     this.travellingTicks = 0;
/* 1048 */     Bee.access$1500(Bee.this).stop();
/* 1049 */     Bee.access$1600(Bee.this).resetMaxVisitedNodesMultiplier();
/*      */   }
/*      */   
/*      */   public void tick() {
/* 1054 */     if (Bee.this.savedFlowerPos == null)
/*      */       return; 
/* 1057 */     this.travellingTicks++;
/* 1059 */     if (this.travellingTicks > adjustedTickDelay(2400)) {
/* 1061 */       Bee.this.dropFlower();
/*      */       return;
/*      */     } 
/* 1065 */     if (Bee.access$1700(Bee.this).isInProgress())
/*      */       return; 
/* 1070 */     if (Bee.this.isTooFarAway(Bee.this.savedFlowerPos)) {
/* 1072 */       Bee.this.dropFlower();
/*      */       return;
/*      */     } 
/* 1079 */     Bee.this.pathfindRandomlyTowards(Bee.this.savedFlowerPos);
/*      */   }
/*      */   
/*      */   private boolean wantsToGoToKnownFlower() {
/* 1083 */     return (Bee.this.ticksWithoutNectarSinceExitingHive > 600);
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeeGoToKnownFlowerGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */