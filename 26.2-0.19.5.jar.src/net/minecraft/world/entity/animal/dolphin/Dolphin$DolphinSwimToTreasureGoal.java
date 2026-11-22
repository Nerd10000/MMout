/*     */ package net.minecraft.world.entity.animal.dolphin;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.StructureTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class DolphinSwimToTreasureGoal extends Goal {
/*     */   private final Dolphin dolphin;
/*     */   
/*     */   private boolean stuck;
/*     */   
/*     */   public DolphinSwimToTreasureGoal(Dolphin dolphin) {
/* 519 */     this.dolphin = dolphin;
/* 520 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean isInterruptable() {
/* 525 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 530 */     return (this.dolphin.gotFish() && this.dolphin.getAirSupply() >= 100);
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 535 */     BlockPos treasurePos = this.dolphin.treasurePos;
/* 536 */     if (treasurePos == null)
/* 537 */       return false; 
/* 539 */     return (!BlockPos.containing(treasurePos.getX(), this.dolphin.getY(), treasurePos.getZ()).closerToCenterThan((Position)this.dolphin.position(), 4.0D) && !this.stuck && this.dolphin.getAirSupply() >= 100);
/*     */   }
/*     */   
/*     */   public void start() {
/* 544 */     if (!(this.dolphin.level() instanceof ServerLevel))
/*     */       return; 
/* 547 */     ServerLevel level = (ServerLevel)this.dolphin.level();
/* 548 */     this.stuck = false;
/* 549 */     this.dolphin.getNavigation().stop();
/* 551 */     BlockPos dolphinPos = this.dolphin.blockPosition();
/* 553 */     BlockPos treasurePos = level.findNearestMapStructure(StructureTags.DOLPHIN_LOCATED, dolphinPos, 50, false);
/* 554 */     if (treasurePos != null) {
/* 555 */       this.dolphin.treasurePos = treasurePos;
/*     */     } else {
/* 558 */       this.stuck = true;
/*     */       return;
/*     */     } 
/* 562 */     level.broadcastEntityEvent((Entity)this.dolphin, (byte)38);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 567 */     BlockPos treasurePos = this.dolphin.treasurePos;
/* 568 */     if (treasurePos == null || BlockPos.containing(treasurePos.getX(), this.dolphin.getY(), treasurePos.getZ()).closerToCenterThan((Position)this.dolphin.position(), 4.0D) || this.stuck)
/* 569 */       this.dolphin.setGotFish(false); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 575 */     if (this.dolphin.treasurePos == null)
/*     */       return; 
/* 579 */     Level level = this.dolphin.level();
/* 581 */     if (this.dolphin.closeToNextPos() || this.dolphin.getNavigation().isDone()) {
/* 582 */       Vec3 treasurePos = Vec3.atCenterOf((Vec3i)this.dolphin.treasurePos);
/* 583 */       Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.dolphin, 16, 1, treasurePos, 0.39269909262657166D);
/* 584 */       if (nextPos == null)
/* 585 */         nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.dolphin, 8, 4, treasurePos, 1.5707963705062866D); 
/* 588 */       if (nextPos != null) {
/* 589 */         BlockPos next = BlockPos.containing((Position)nextPos);
/* 590 */         if (!level.getFluidState(next).is(FluidTags.WATER) || !level.getBlockState(next).isPathfindable(PathComputationType.WATER))
/* 591 */           nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.dolphin, 8, 5, treasurePos, 1.5707963705062866D); 
/*     */       } 
/* 595 */       if (nextPos == null) {
/* 596 */         this.stuck = true;
/*     */         return;
/*     */       } 
/* 600 */       this.dolphin.getLookControl().setLookAt(nextPos.x, nextPos.y, nextPos.z, (this.dolphin.getMaxHeadYRot() + 20), this.dolphin.getMaxHeadXRot());
/* 601 */       this.dolphin.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, 1.3D);
/* 603 */       if (level.getRandom().nextInt(adjustedTickDelay(80)) == 0)
/* 604 */         level.broadcastEntityEvent((Entity)this.dolphin, (byte)38); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\dolphin\Dolphin$DolphinSwimToTreasureGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */