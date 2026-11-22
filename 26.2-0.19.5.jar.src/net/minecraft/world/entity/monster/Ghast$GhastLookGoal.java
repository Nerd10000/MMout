/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ public class GhastLookGoal extends Goal {
/*     */   private final Mob ghast;
/*     */   
/*     */   public GhastLookGoal(Mob ghast) {
/* 398 */     this.ghast = ghast;
/* 400 */     setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 405 */     return true;
/*     */   }
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 410 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 415 */     Ghast.faceMovementDirection(this.ghast);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Ghast$GhastLookGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */