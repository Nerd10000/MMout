/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class DrownedGoToWaterGoal extends Goal {
/*     */   private final PathfinderMob mob;
/*     */   
/*     */   private double wantedX;
/*     */   
/*     */   private double wantedY;
/*     */   
/*     */   private double wantedZ;
/*     */   
/*     */   private final double speedModifier;
/*     */   
/*     */   private final Level level;
/*     */   
/*     */   public DrownedGoToWaterGoal(PathfinderMob mob, double speedModifier) {
/* 469 */     this.mob = mob;
/* 470 */     this.speedModifier = speedModifier;
/* 471 */     this.level = mob.level();
/* 472 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 477 */     if (!this.level.isBrightOutside())
/* 478 */       return false; 
/* 480 */     if (this.mob.isInWater())
/* 481 */       return false; 
/* 484 */     Vec3 pos = getWaterPos();
/* 485 */     if (pos == null)
/* 486 */       return false; 
/* 488 */     this.wantedX = pos.x;
/* 489 */     this.wantedY = pos.y;
/* 490 */     this.wantedZ = pos.z;
/* 491 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 496 */     return !this.mob.getNavigation().isDone();
/*     */   }
/*     */   
/*     */   public void start() {
/* 501 */     this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*     */   }
/*     */   
/*     */   private Vec3 getWaterPos() {
/* 505 */     RandomSource random = this.mob.getRandom();
/* 506 */     BlockPos pos = this.mob.blockPosition();
/* 508 */     for (int i = 0; i < 10; i++) {
/* 509 */       BlockPos randomPos = pos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
/* 511 */       if (this.level.getBlockState(randomPos).is(Blocks.WATER))
/* 512 */         return Vec3.atBottomCenterOf((Vec3i)randomPos); 
/*     */     } 
/* 515 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Drowned$DrownedGoToWaterGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */