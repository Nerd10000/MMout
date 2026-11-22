/*     */ package net.minecraft.world.entity.animal.fish;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class PufferfishPuffGoal extends Goal {
/*     */   private final Pufferfish fish;
/*     */   
/*     */   public PufferfishPuffGoal(Pufferfish fish) {
/* 207 */     this.fish = fish;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 212 */     List<LivingEntity> entities = this.fish.level().getEntitiesOfClass(LivingEntity.class, this.fish.getBoundingBox().inflate(2.0D), target -> Pufferfish.TARGETING_CONDITIONS.test(getServerLevel((Entity)this.fish), (LivingEntity)this.fish, target));
/* 214 */     return !entities.isEmpty();
/*     */   }
/*     */   
/*     */   public void start() {
/* 219 */     this.fish.inflateCounter = 1;
/* 220 */     this.fish.deflateTimer = 0;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 225 */     this.fish.inflateCounter = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fish\Pufferfish$PufferfishPuffGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */