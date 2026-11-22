/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class RaiderMoveThroughVillageGoal extends Goal {
/*     */   private final Raider raider;
/*     */   
/*     */   private final double speedModifier;
/*     */   
/*     */   private BlockPos poiPos;
/*     */   
/* 501 */   private final List<BlockPos> visited = Lists.newArrayList();
/*     */   
/*     */   private final int distanceToPoi;
/*     */   
/*     */   private boolean stuck;
/*     */   
/*     */   public RaiderMoveThroughVillageGoal(Raider mob, double speedModifier, int distanceToPoi) {
/* 506 */     this.raider = mob;
/* 507 */     this.speedModifier = speedModifier;
/* 508 */     this.distanceToPoi = distanceToPoi;
/* 509 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 514 */     updateVisited();
/* 515 */     return (isValidRaid() && hasSuitablePoi() && this.raider.getTarget() == null);
/*     */   }
/*     */   
/*     */   private boolean isValidRaid() {
/* 519 */     return (this.raider.hasActiveRaid() && !this.raider.getCurrentRaid().isOver());
/*     */   }
/*     */   
/*     */   private boolean hasSuitablePoi() {
/* 523 */     ServerLevel level = (ServerLevel)this.raider.level();
/* 524 */     BlockPos pos = this.raider.blockPosition();
/* 525 */     Optional<BlockPos> homePos = level.getPoiManager().getRandom(p -> p.is(PoiTypes.HOME), this::hasNotVisited, PoiManager.Occupancy.ANY, pos, 48, Raider.access$400(this.raider));
/* 526 */     if (homePos.isEmpty())
/* 527 */       return false; 
/* 530 */     this.poiPos = ((BlockPos)homePos.get()).immutable();
/* 532 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 537 */     if (this.raider.getNavigation().isDone())
/* 538 */       return false; 
/* 540 */     return (this.raider.getTarget() == null && !this.poiPos.closerToCenterThan((Position)this.raider.position(), (this.raider.getBbWidth() + this.distanceToPoi)) && !this.stuck);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 545 */     if (this.poiPos.closerToCenterThan((Position)this.raider.position(), this.distanceToPoi))
/* 546 */       this.visited.add(this.poiPos); 
/*     */   }
/*     */   
/*     */   public void start() {
/* 552 */     super.start();
/* 553 */     this.raider.setNoActionTime(0);
/* 554 */     this.raider.getNavigation().moveTo(this.poiPos.getX(), this.poiPos.getY(), this.poiPos.getZ(), this.speedModifier);
/* 555 */     this.stuck = false;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 560 */     if (this.raider.getNavigation().isDone()) {
/* 561 */       Vec3 poiVec = Vec3.atBottomCenterOf((Vec3i)this.poiPos);
/* 562 */       Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.raider, 16, 7, poiVec, 0.3141592741012573D);
/* 563 */       if (nextPos == null)
/* 564 */         nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.raider, 8, 7, poiVec, 1.5707963705062866D); 
/* 567 */       if (nextPos == null) {
/* 568 */         this.stuck = true;
/*     */         return;
/*     */       } 
/* 572 */       this.raider.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean hasNotVisited(BlockPos poi) {
/* 577 */     for (BlockPos visitedPoi : this.visited) {
/* 578 */       if (Objects.equals(poi, visitedPoi))
/* 579 */         return false; 
/*     */     } 
/* 582 */     return true;
/*     */   }
/*     */   
/*     */   private void updateVisited() {
/* 586 */     if (this.visited.size() > 2)
/* 587 */       this.visited.remove(0); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\raid\Raider$RaiderMoveThroughVillageGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */