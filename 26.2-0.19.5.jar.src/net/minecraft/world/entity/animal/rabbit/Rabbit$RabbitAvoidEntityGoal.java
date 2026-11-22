/*     */ package net.minecraft.world.entity.animal.rabbit;
/*     */ 
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ 
/*     */ class RabbitAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*     */   private final Rabbit rabbit;
/*     */   
/*     */   public RabbitAvoidEntityGoal(Rabbit rabbit, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 647 */     super((PathfinderMob)rabbit, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier);
/* 648 */     this.rabbit = rabbit;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 653 */     return (this.rabbit.getVariant() != Rabbit.Variant.EVIL && super.canUse());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\rabbit\Rabbit$RabbitAvoidEntityGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */