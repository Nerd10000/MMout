/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class CubeMobKeepOnJumpingGoal extends Goal {
/*     */   private final AbstractCubeMob cubeMob;
/*     */   
/*     */   public CubeMobKeepOnJumpingGoal(AbstractCubeMob mob) {
/* 454 */     this.cubeMob = mob;
/* 455 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 460 */     return !this.cubeMob.isPassenger();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 465 */     MoveControl moveControl = this.cubeMob.getMoveControl();
/* 465 */     if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 465 */       AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 466 */       cubeMobMoveControl.setWantedMovement(1.0D);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\AbstractCubeMob$CubeMobKeepOnJumpingGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */