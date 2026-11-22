/*     */ package net.minecraft.world.entity.animal.bee;
/*     */ 
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ abstract class BaseBeeGoal extends Goal {
/*     */   public abstract boolean canBeeUse();
/*     */   
/*     */   public abstract boolean canBeeContinueToUse();
/*     */   
/*     */   public boolean canUse() {
/* 807 */     return (canBeeUse() && !Bee.this.isAngry());
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 812 */     return (canBeeContinueToUse() && !Bee.this.isAngry());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee$BaseBeeGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */