/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ class TurtleGoToWaterGoal extends MoveToBlockGoal {
/*     */   private static final int GIVE_UP_TICKS = 1200;
/*     */   
/*     */   private final Turtle turtle;
/*     */   
/*     */   private TurtleGoToWaterGoal(Turtle turtle, double speedModifier) {
/* 617 */     super((PathfinderMob)turtle, turtle.isBaby() ? 2.0D : speedModifier, 24);
/* 618 */     this.turtle = turtle;
/* 619 */     this.verticalSearchStart = -1;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 624 */     return (!this.turtle.isInWater() && this.tryTicks <= 1200 && isValidTarget((LevelReader)this.turtle.level(), this.blockPos));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 629 */     if (this.turtle.isBaby() && !this.turtle.isInWater())
/* 630 */       return super.canUse(); 
/* 633 */     if (!this.turtle.goingHome && !this.turtle.isInWater() && !this.turtle.hasEgg())
/* 634 */       return super.canUse(); 
/* 637 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldRecalculatePath() {
/* 642 */     return (this.tryTicks % 160 == 0);
/*     */   }
/*     */   
/*     */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 647 */     return level.getBlockState(pos).is(Blocks.WATER);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtleGoToWaterGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */