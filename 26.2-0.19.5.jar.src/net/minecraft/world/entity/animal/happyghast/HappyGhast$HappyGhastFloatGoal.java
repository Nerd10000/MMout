/*     */ package net.minecraft.world.entity.animal.happyghast;
/*     */ 
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ 
/*     */ class HappyGhastFloatGoal extends FloatGoal {
/*     */   public HappyGhastFloatGoal() {
/* 601 */     super((Mob)paramHappyGhast);
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 607 */     return (!HappyGhast.this.isOnStillTimeout() && super.canUse());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\happyghast\HappyGhast$HappyGhastFloatGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */