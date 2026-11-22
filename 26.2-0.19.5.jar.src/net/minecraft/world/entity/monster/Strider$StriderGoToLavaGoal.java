/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ 
/*     */ class StriderGoToLavaGoal extends MoveToBlockGoal {
/*     */   private final Strider strider;
/*     */   
/*     */   private StriderGoToLavaGoal(Strider strider, double speedModifier) {
/* 514 */     super((PathfinderMob)strider, speedModifier, 8, 2);
/* 515 */     this.strider = strider;
/*     */   }
/*     */   
/*     */   public BlockPos getMoveToTarget() {
/* 520 */     return this.blockPos;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 525 */     return (!this.strider.isInLava() && isValidTarget((LevelReader)this.strider.level(), this.blockPos));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 530 */     return (!this.strider.isInLava() && super.canUse());
/*     */   }
/*     */   
/*     */   public boolean shouldRecalculatePath() {
/* 535 */     return (this.tryTicks % 20 == 0);
/*     */   }
/*     */   
/*     */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 540 */     return (level.getBlockState(pos).is(Blocks.LAVA) && level.getBlockState(pos.above()).isPathfindable(PathComputationType.LAND));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Strider$StriderGoToLavaGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */