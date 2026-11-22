/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.util.VisibleForDebug;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ 
/*      */ @VisibleForDebug
/*      */ public class BeeGoToHiveGoal extends Bee.BaseBeeGoal {
/*      */   public static final int MAX_TRAVELLING_TICKS = 2400;
/*      */   
/*      */   private int travellingTicks;
/*      */   
/*      */   private static final int MAX_BLACKLISTED_TARGETS = 3;
/*      */   
/*  876 */   private final List<BlockPos> blacklistedTargets = Lists.newArrayList();
/*      */   
/*      */   private Path lastPath;
/*      */   
/*      */   private static final int TICKS_BEFORE_HIVE_DROP = 60;
/*      */   
/*      */   private int ticksStuck;
/*      */   
/*      */   public BeeGoToHiveGoal() {
/*  884 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */   }
/*      */   
/*      */   public boolean canBeeUse() {
/*  889 */     return (Bee.this.hivePos != null && 
/*  890 */       !Bee.this.isTooFarAway(Bee.this.hivePos) && 
/*  891 */       !Bee.this.hasHome() && Bee.this
/*  892 */       .wantsToEnterHive() && 
/*  893 */       !hasReachedTarget(Bee.this.hivePos) && Bee.this
/*  894 */       .level().getBlockState(Bee.this.hivePos).is(BlockTags.BEEHIVES));
/*      */   }
/*      */   
/*      */   public boolean canBeeContinueToUse() {
/*  899 */     return canBeeUse();
/*      */   }
/*      */   
/*      */   public void start() {
/*  904 */     this.travellingTicks = 0;
/*  905 */     this.ticksStuck = 0;
/*  906 */     super.start();
/*      */   }
/*      */   
/*      */   public void stop() {
/*  911 */     this.travellingTicks = 0;
/*  912 */     this.ticksStuck = 0;
/*  913 */     Bee.access$500(Bee.this).stop();
/*  914 */     Bee.access$600(Bee.this).resetMaxVisitedNodesMultiplier();
/*      */   }
/*      */   
/*      */   public void tick() {
/*  919 */     if (Bee.this.hivePos == null)
/*      */       return; 
/*  923 */     this.travellingTicks++;
/*  925 */     if (this.travellingTicks > adjustedTickDelay(2400)) {
/*  927 */       dropAndBlacklistHive();
/*      */       return;
/*      */     } 
/*  931 */     if (Bee.access$700(Bee.this).isInProgress())
/*      */       return; 
/*  937 */     if (Bee.this.closerThan(Bee.this.hivePos, 16)) {
/*  939 */       boolean canReachAllTheWayToTarget = pathfindDirectlyTowards(Bee.this.hivePos);
/*  940 */       if (!canReachAllTheWayToTarget) {
/*  942 */         dropAndBlacklistHive();
/*  944 */       } else if (this.lastPath != null && Bee.access$800(Bee.this).getPath().sameAs(this.lastPath)) {
/*  950 */         this.ticksStuck++;
/*  952 */         if (this.ticksStuck > 60) {
/*  953 */           Bee.this.dropHive();
/*  954 */           this.ticksStuck = 0;
/*      */         } 
/*      */       } else {
/*  958 */         this.lastPath = Bee.access$900(Bee.this).getPath();
/*      */       } 
/*      */       return;
/*      */     } 
/*  964 */     if (Bee.this.isTooFarAway(Bee.this.hivePos)) {
/*  966 */       Bee.this.dropHive();
/*      */       return;
/*      */     } 
/*  973 */     Bee.this.pathfindRandomlyTowards(Bee.this.hivePos);
/*      */   }
/*      */   
/*      */   private boolean pathfindDirectlyTowards(BlockPos targetPos) {
/*  978 */     int closeEnough = Bee.this.closerThan(targetPos, 3) ? 1 : 2;
/*  979 */     Bee.access$1000(Bee.this).setMaxVisitedNodesMultiplier(10.0F);
/*  980 */     Bee.access$1100(Bee.this).moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), closeEnough, 1.0D);
/*  981 */     return (Bee.access$1200(Bee.this).getPath() != null && Bee.access$1300(Bee.this).getPath().canReach());
/*      */   }
/*      */   
/*      */   private boolean isTargetBlacklisted(BlockPos targetPos) {
/*  985 */     return this.blacklistedTargets.contains(targetPos);
/*      */   }
/*      */   
/*      */   private void blacklistTarget(BlockPos targetPos) {
/*  989 */     this.blacklistedTargets.add(targetPos);
/*  990 */     while (this.blacklistedTargets.size() > 3)
/*  991 */       this.blacklistedTargets.remove(0); 
/*      */   }
/*      */   
/*      */   private void clearBlacklist() {
/*  996 */     this.blacklistedTargets.clear();
/*      */   }
/*      */   
/*      */   private void dropAndBlacklistHive() {
/* 1000 */     if (Bee.this.hivePos != null)
/* 1001 */       blacklistTarget(Bee.this.hivePos); 
/* 1003 */     Bee.this.dropHive();
/*      */   }
/*      */   
/*      */   private boolean hasReachedTarget(BlockPos targetPos) {
/* 1007 */     if (Bee.this.closerThan(targetPos, 2))
/* 1008 */       return true; 
/* 1010 */     Path path = Bee.access$1400(Bee.this).getPath();
/* 1011 */     return (path != null && path.getTarget().equals(targetPos) && path.canReach() && path.isDone());
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeeGoToHiveGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */