/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class TurtleGoHomeGoal extends Goal {
/*     */   private final Turtle turtle;
/*     */   
/*     */   private final double speedModifier;
/*     */   
/*     */   private boolean stuck;
/*     */   
/*     */   private int closeToHomeTryTicks;
/*     */   
/*     */   private static final int GIVE_UP_TICKS = 600;
/*     */   
/*     */   public TurtleGoHomeGoal(Turtle turtle, double speedModifier) {
/* 431 */     this.turtle = turtle;
/* 432 */     this.speedModifier = speedModifier;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 437 */     if (this.turtle.isBaby())
/* 438 */       return false; 
/* 441 */     if (this.turtle.hasEgg())
/* 442 */       return true; 
/* 445 */     if (this.turtle.getRandom().nextInt(reducedTickDelay(700)) != 0)
/* 446 */       return false; 
/* 449 */     return !this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 64.0D);
/*     */   }
/*     */   
/*     */   public void start() {
/* 454 */     this.turtle.goingHome = true;
/* 455 */     this.stuck = false;
/* 456 */     this.closeToHomeTryTicks = 0;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 461 */     this.turtle.goingHome = false;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 466 */     return (!this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 7.0D) && !this.stuck && this.closeToHomeTryTicks <= adjustedTickDelay(600));
/*     */   }
/*     */   
/*     */   public void tick() {
/* 471 */     BlockPos homePos = this.turtle.homePos;
/* 472 */     boolean closeToHome = homePos.closerToCenterThan((Position)this.turtle.position(), 16.0D);
/* 473 */     if (closeToHome)
/* 474 */       this.closeToHomeTryTicks++; 
/* 477 */     if (this.turtle.getNavigation().isDone()) {
/* 478 */       Vec3 homePosVec = Vec3.atBottomCenterOf((Vec3i)homePos);
/* 479 */       Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 3, homePosVec, 0.3141592741012573D);
/* 480 */       if (nextPos == null)
/* 481 */         nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 8, 7, homePosVec, 1.5707963705062866D); 
/* 484 */       if (nextPos != null && !closeToHome && !this.turtle.level().getBlockState(BlockPos.containing((Position)nextPos)).is(Blocks.WATER))
/* 486 */         nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 5, homePosVec, 1.5707963705062866D); 
/* 489 */       if (nextPos == null) {
/* 490 */         this.stuck = true;
/*     */         return;
/*     */       } 
/* 494 */       this.turtle.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtleGoHomeGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */