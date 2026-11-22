/*     */ package net.minecraft.world.entity.animal.rabbit;
/*     */ 
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ 
/*     */ class RabbitPanicGoal extends PanicGoal {
/*     */   private final Rabbit rabbit;
/*     */   
/*     */   public RabbitPanicGoal(Rabbit rabbit, double speedModifier) {
/* 741 */     super((PathfinderMob)rabbit, speedModifier);
/* 742 */     this.rabbit = rabbit;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 747 */     super.tick();
/* 749 */     this.rabbit.setSpeedModifier(this.speedModifier);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\rabbit\Rabbit$RabbitPanicGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */