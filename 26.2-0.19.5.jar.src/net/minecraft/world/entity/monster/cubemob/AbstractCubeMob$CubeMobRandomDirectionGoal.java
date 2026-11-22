/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class CubeMobRandomDirectionGoal extends Goal {
/*     */   private final AbstractCubeMob cubeMob;
/*     */   
/*     */   private float chosenDegrees;
/*     */   
/*     */   private int nextRandomizeTime;
/*     */   
/*     */   public CubeMobRandomDirectionGoal(AbstractCubeMob cubeMob) {
/* 399 */     this.cubeMob = cubeMob;
/* 400 */     setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 405 */     return (this.cubeMob.getTarget() == null && (this.cubeMob.onGround() || this.cubeMob.isInWater() || this.cubeMob.isInLava() || this.cubeMob.hasEffect(MobEffects.LEVITATION)) && this.cubeMob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 410 */     if (--this.nextRandomizeTime <= 0) {
/* 411 */       this.nextRandomizeTime = adjustedTickDelay(40 + this.cubeMob.getRandom().nextInt(60));
/* 412 */       this.chosenDegrees = this.cubeMob.getRandom().nextInt(360);
/*     */     } 
/* 414 */     MoveControl moveControl = this.cubeMob.getMoveControl();
/* 414 */     if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 414 */       AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 415 */       cubeMobMoveControl.setDirection(this.chosenDegrees, false);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\AbstractCubeMob$CubeMobRandomDirectionGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */