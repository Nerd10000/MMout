/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class DrownedSwimUpGoal extends Goal {
/*     */   private final Drowned drowned;
/*     */   
/*     */   private final double speedModifier;
/*     */   
/*     */   private final int seaLevel;
/*     */   
/*     */   private boolean stuck;
/*     */   
/*     */   public DrownedSwimUpGoal(Drowned drowned, double speedModifier, int seaLevel) {
/* 377 */     this.drowned = drowned;
/* 378 */     this.speedModifier = speedModifier;
/* 379 */     this.seaLevel = seaLevel;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 384 */     return (!this.drowned.level().isBrightOutside() && this.drowned.isInWater() && this.drowned.getY() < (this.seaLevel - 2));
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 389 */     return (canUse() && !this.stuck);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 394 */     if (this.drowned.getY() < (this.seaLevel - 1) && (this.drowned.getNavigation().isDone() || this.drowned.closeToNextPos())) {
/* 396 */       Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.drowned, 4, 8, new Vec3(this.drowned.getX(), (this.seaLevel - 1), this.drowned.getZ()), 1.5707963705062866D);
/* 398 */       if (nextPos == null) {
/* 399 */         this.stuck = true;
/*     */         return;
/*     */       } 
/* 403 */       this.drowned.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void start() {
/* 409 */     this.drowned.setSearchingForLand(true);
/* 410 */     this.stuck = false;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 415 */     this.drowned.setSearchingForLand(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Drowned$DrownedSwimUpGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */