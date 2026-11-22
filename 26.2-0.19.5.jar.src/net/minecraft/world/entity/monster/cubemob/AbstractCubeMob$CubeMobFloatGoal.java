/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class CubeMobFloatGoal extends Goal {
/*     */   private final AbstractCubeMob cubeMob;
/*     */   
/*     */   public CubeMobFloatGoal(AbstractCubeMob mob) {
/* 424 */     this.cubeMob = mob;
/* 425 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/* 426 */     mob.getNavigation().setCanFloat(true);
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 431 */     return ((this.cubeMob.isInWater() || this.cubeMob.isInLava()) && this.cubeMob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl);
/*     */   }
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 436 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 441 */     if (this.cubeMob.getRandom().nextFloat() < 0.8F)
/* 442 */       this.cubeMob.getJumpControl().jump(); 
/* 444 */     MoveControl moveControl = this.cubeMob.getMoveControl();
/* 444 */     if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 444 */       AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 445 */       cubeMobMoveControl.setWantedMovement(1.2D);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\AbstractCubeMob$CubeMobFloatGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */