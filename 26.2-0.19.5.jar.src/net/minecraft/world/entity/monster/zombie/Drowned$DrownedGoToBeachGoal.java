/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ 
/*     */ class DrownedGoToBeachGoal extends MoveToBlockGoal {
/*     */   private final Drowned drowned;
/*     */   
/*     */   public DrownedGoToBeachGoal(Drowned drowned, double speedModifier) {
/* 424 */     super((PathfinderMob)drowned, speedModifier, 8, 2);
/* 425 */     this.drowned = drowned;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 430 */     return (super.canUse() && !this.drowned.level().isBrightOutside() && this.drowned.isInWater() && this.drowned.getY() >= (this.drowned.level().getSeaLevel() - 3));
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 435 */     return super.canContinueToUse();
/*     */   }
/*     */   
/*     */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 440 */     BlockPos above = pos.above();
/* 441 */     if (!level.isEmptyBlock(above) || !level.isEmptyBlock(above.above()))
/* 442 */       return false; 
/* 445 */     return level.getBlockState(pos).entityCanStandOn((BlockGetter)level, pos, (Entity)this.drowned);
/*     */   }
/*     */   
/*     */   public void start() {
/* 450 */     this.drowned.setSearchingForLand(false);
/* 451 */     super.start();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 456 */     super.stop();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Drowned$DrownedGoToBeachGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */