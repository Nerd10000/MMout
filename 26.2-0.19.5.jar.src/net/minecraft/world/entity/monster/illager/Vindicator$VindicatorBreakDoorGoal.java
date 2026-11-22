/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class VindicatorBreakDoorGoal extends BreakDoorGoal {
/*     */   public VindicatorBreakDoorGoal(Mob mob) {
/* 191 */     super(mob, 6, Vindicator.DOOR_BREAKING_PREDICATE);
/* 192 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 197 */     Vindicator vindicator = (Vindicator)this.mob;
/* 198 */     return (vindicator.hasActiveRaid() && super.canContinueToUse());
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 203 */     Vindicator vindicator = (Vindicator)this.mob;
/* 204 */     return (vindicator.hasActiveRaid() && Vindicator.access$000(vindicator).nextInt(reducedTickDelay(10)) == 0 && super.canUse());
/*     */   }
/*     */   
/*     */   public void start() {
/* 209 */     super.start();
/* 210 */     this.mob.setNoActionTime(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\Vindicator$VindicatorBreakDoorGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */