/*     */ package net.minecraft.world.entity.boss.wither;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class WitherDoNothingGoal extends Goal {
/*     */   public WitherDoNothingGoal() {
/* 545 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 550 */     return (WitherBoss.this.getInvulnerableTicks() > 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\boss\wither\WitherBoss$WitherDoNothingGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */