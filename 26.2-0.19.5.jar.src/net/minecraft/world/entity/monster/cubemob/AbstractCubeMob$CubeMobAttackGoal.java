/*     */ package net.minecraft.world.entity.monster.cubemob;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ public class CubeMobAttackGoal extends Goal {
/*     */   private final AbstractCubeMob cubeMob;
/*     */   
/*     */   private int growTiredTimer;
/*     */   
/*     */   public CubeMobAttackGoal(AbstractCubeMob cubeMob) {
/* 331 */     this.cubeMob = cubeMob;
/* 332 */     setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 337 */     LivingEntity target = this.cubeMob.getTarget();
/* 339 */     if (target == null)
/* 340 */       return false; 
/* 343 */     if (!this.cubeMob.canAttack(target))
/* 344 */       return false; 
/* 347 */     return this.cubeMob.getMoveControl() instanceof AbstractCubeMob.CubeMobMoveControl;
/*     */   }
/*     */   
/*     */   public void start() {
/* 352 */     this.growTiredTimer = reducedTickDelay(300);
/* 353 */     super.start();
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 358 */     LivingEntity target = this.cubeMob.getTarget();
/* 360 */     if (target == null)
/* 361 */       return false; 
/* 364 */     if (!this.cubeMob.canAttack(target))
/* 365 */       return false; 
/* 368 */     if (--this.growTiredTimer <= 0)
/* 369 */       return false; 
/* 372 */     return true;
/*     */   }
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 377 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 382 */     LivingEntity target = this.cubeMob.getTarget();
/* 383 */     if (target != null)
/* 384 */       this.cubeMob.lookAt((Entity)target, 10.0F, 10.0F); 
/* 386 */     MoveControl moveControl = this.cubeMob.getMoveControl();
/* 386 */     if (moveControl instanceof AbstractCubeMob.CubeMobMoveControl) {
/* 386 */       AbstractCubeMob.CubeMobMoveControl cubeMobMoveControl = (AbstractCubeMob.CubeMobMoveControl)moveControl;
/* 387 */       cubeMobMoveControl.setDirection(this.cubeMob.getYRot(), this.cubeMob.isDealsDamage());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\cubemob\AbstractCubeMob$CubeMobAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */