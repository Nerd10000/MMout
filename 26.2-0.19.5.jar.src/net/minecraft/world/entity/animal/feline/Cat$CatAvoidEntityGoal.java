/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ 
/*     */ class CatAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*     */   private final Cat cat;
/*     */   
/*     */   public CatAvoidEntityGoal(Cat cat, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 527 */     super((PathfinderMob)cat, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
/* 528 */     this.cat = cat;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 533 */     return (!this.cat.isTame() && super.canUse());
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 538 */     return (!this.cat.isTame() && super.canContinueToUse());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\Cat$CatAvoidEntityGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */