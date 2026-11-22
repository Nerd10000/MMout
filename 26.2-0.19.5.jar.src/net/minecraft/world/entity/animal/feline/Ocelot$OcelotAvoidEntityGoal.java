/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ 
/*     */ class OcelotAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*     */   private final Ocelot ocelot;
/*     */   
/*     */   public OcelotAvoidEntityGoal(Ocelot ocelot, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 300 */     super((PathfinderMob)ocelot, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
/* 301 */     this.ocelot = ocelot;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 306 */     return (!this.ocelot.isTrusting() && super.canUse());
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 311 */     return (!this.ocelot.isTrusting() && super.canContinueToUse());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\Ocelot$OcelotAvoidEntityGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */