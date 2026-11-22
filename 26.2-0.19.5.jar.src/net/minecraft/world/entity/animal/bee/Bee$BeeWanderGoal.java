/*     */ package net.minecraft.world.entity.animal.bee;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
/*     */ import net.minecraft.world.entity.ai.util.HoverRandomPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class BeeWanderGoal extends Goal {
/*     */   public BeeWanderGoal() {
/* 819 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 824 */     return (Bee.access$000(Bee.this).isDone() && Bee.access$100(Bee.this).nextInt(10) == 0);
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 829 */     return Bee.access$200(Bee.this).isInProgress();
/*     */   }
/*     */   
/*     */   public void start() {
/* 834 */     Vec3 targetPos = findPos();
/* 835 */     if (targetPos != null)
/* 836 */       Bee.access$400(Bee.this).moveTo(Bee.access$300(Bee.this).createPath(BlockPos.containing((Position)targetPos), 1), 1.0D); 
/*     */   }
/*     */   
/*     */   private Vec3 findPos() {
/*     */     Vec3 wanderDirection;
/* 843 */     if (Bee.this.isHiveValid() && !Bee.this.closerThan(Bee.this.hivePos, getWanderThreshold())) {
/* 845 */       Vec3 hivePosVec = Vec3.atCenterOf((Vec3i)Bee.this.hivePos);
/* 846 */       wanderDirection = hivePosVec.subtract(Bee.this.position()).normalize();
/*     */     } else {
/* 848 */       wanderDirection = Bee.this.getViewVector(0.0F);
/*     */     } 
/* 851 */     int xzDist = 8;
/* 852 */     Vec3 groundBasedPosition = HoverRandomPos.getPos((PathfinderMob)Bee.this, 8, 7, wanderDirection.x, wanderDirection.z, 1.5707964F, 3, 1);
/* 853 */     if (groundBasedPosition != null)
/* 854 */       return groundBasedPosition; 
/* 858 */     return AirAndWaterRandomPos.getPos((PathfinderMob)Bee.this, 8, 4, -2, wanderDirection.x, wanderDirection.z, 1.5707963705062866D);
/*     */   }
/*     */   
/*     */   private int getWanderThreshold() {
/* 863 */     int distanceReduction = (Bee.this.hasHive() || Bee.this.hasSavedFlowerPos()) ? 24 : 16;
/* 864 */     return 48 - distanceReduction;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BeeWanderGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */