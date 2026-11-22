/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class TurtleTravelGoal extends Goal {
/*     */   private final Turtle turtle;
/*     */   
/*     */   private final double speedModifier;
/*     */   
/*     */   private boolean stuck;
/*     */   
/*     */   public TurtleTravelGoal(Turtle turtle, double speedModifier) {
/* 353 */     this.turtle = turtle;
/* 354 */     this.speedModifier = speedModifier;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 359 */     return (!this.turtle.goingHome && !this.turtle.hasEgg() && this.turtle.isInWater());
/*     */   }
/*     */   
/*     */   public void start() {
/* 364 */     int xzDist = 512;
/* 365 */     int yDist = 4;
/* 366 */     RandomSource random = Turtle.access$000(this.turtle);
/* 367 */     int xt = random.nextInt(1025) - 512;
/* 368 */     int yt = random.nextInt(9) - 4;
/* 369 */     int zt = random.nextInt(1025) - 512;
/* 371 */     if (yt + this.turtle.getY() > (this.turtle.level().getSeaLevel() - 1))
/* 372 */       yt = 0; 
/* 374 */     this.turtle.travelPos = BlockPos.containing(xt + this.turtle.getX(), yt + this.turtle.getY(), zt + this.turtle.getZ());
/* 375 */     this.stuck = false;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 380 */     if (this.turtle.travelPos == null) {
/* 381 */       this.stuck = true;
/*     */       return;
/*     */     } 
/* 385 */     if (this.turtle.getNavigation().isDone()) {
/* 386 */       Vec3 targetPos = Vec3.atBottomCenterOf((Vec3i)this.turtle.travelPos);
/* 387 */       Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 3, targetPos, 0.3141592741012573D);
/* 388 */       if (nextPos == null)
/* 389 */         nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 8, 7, targetPos, 1.5707963705062866D); 
/* 393 */       if (nextPos != null) {
/* 394 */         int xc = Mth.floor(nextPos.x);
/* 395 */         int zc = Mth.floor(nextPos.z);
/* 396 */         int r = 34;
/* 397 */         if (!this.turtle.level().hasChunksAt(xc - 34, zc - 34, xc + 34, zc + 34))
/* 398 */           nextPos = null; 
/*     */       } 
/* 402 */       if (nextPos == null) {
/* 403 */         this.stuck = true;
/*     */         return;
/*     */       } 
/* 407 */       this.turtle.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 413 */     return (!this.turtle.getNavigation().isDone() && !this.stuck && !this.turtle.goingHome && !this.turtle.isInLove() && !this.turtle.hasEgg());
/*     */   }
/*     */   
/*     */   public void stop() {
/* 418 */     this.turtle.travelPos = null;
/* 419 */     super.stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtleTravelGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */