/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ 
/*     */ class TurtleRandomStrollGoal extends RandomStrollGoal {
/*     */   private final Turtle turtle;
/*     */   
/*     */   private TurtleRandomStrollGoal(Turtle turtle, double speedModifier, int interval) {
/* 597 */     super((PathfinderMob)turtle, speedModifier, interval);
/* 598 */     this.turtle = turtle;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 603 */     if (!this.mob.isInWater() && !this.turtle.goingHome && !this.turtle.hasEgg())
/* 604 */       return super.canUse(); 
/* 607 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\turtle\Turtle$TurtleRandomStrollGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */