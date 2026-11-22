/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
/*     */ 
/*     */ class FishSwimGoal extends RandomSwimmingGoal {
/*     */   private final AbstractFish fish;
/*     */   
/*     */   public FishSwimGoal(AbstractFish fish) {
/* 161 */     super(fish, 1.0D, 40);
/* 162 */     this.fish = fish;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 167 */     return (this.fish.canRandomSwim() && super.canUse());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fish\AbstractFish$FishSwimGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */