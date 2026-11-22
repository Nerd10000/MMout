/*     */ package net.minecraft.world.entity.animal.wolf;
/*     */ 
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ 
/*     */ class WolfAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*     */   private final Wolf wolf;
/*     */   
/*     */   public WolfAvoidEntityGoal(Wolf wolf, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 721 */     super((PathfinderMob)wolf, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier);
/* 722 */     this.wolf = wolf;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 727 */     if (super.canUse() && 
/* 728 */       this.toAvoid instanceof Llama)
/* 729 */       return (!this.wolf.isTame() && avoidLlama((Llama)this.toAvoid)); 
/* 733 */     return false;
/*     */   }
/*     */   
/*     */   private boolean avoidLlama(Llama llama) {
/* 737 */     return (llama.getStrength() >= Wolf.access$000(Wolf.this).nextInt(5));
/*     */   }
/*     */   
/*     */   public void start() {
/* 742 */     Wolf.this.setTarget(null);
/* 743 */     super.start();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 748 */     Wolf.this.setTarget(null);
/* 749 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\wolf\Wolf$WolfAvoidEntityGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */