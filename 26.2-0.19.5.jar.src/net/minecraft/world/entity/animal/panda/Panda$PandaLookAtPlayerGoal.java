/*     */ package net.minecraft.world.entity.animal.panda;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ class PandaLookAtPlayerGoal extends LookAtPlayerGoal {
/*     */   private final Panda panda;
/*     */   
/*     */   public PandaLookAtPlayerGoal(Panda mob, Class<? extends LivingEntity> lookAtType, float lookDistance) {
/* 842 */     super((Mob)mob, lookAtType, lookDistance);
/* 843 */     this.panda = mob;
/*     */   }
/*     */   
/*     */   public void setTarget(LivingEntity entity) {
/* 847 */     this.lookAt = (Entity)entity;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 852 */     return (this.lookAt != null && super.canContinueToUse());
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 857 */     if (this.mob.getRandom().nextFloat() >= this.probability)
/* 858 */       return false; 
/* 863 */     if (this.lookAt == null) {
/* 864 */       ServerLevel level = getServerLevel((Entity)this.mob);
/* 865 */       if (this.lookAtType == Player.class) {
/* 866 */         this.lookAt = (Entity)level.getNearestPlayer(this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*     */       } else {
/* 868 */         this.lookAt = (Entity)level.getNearestEntity(this.mob.level().getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance), entity -> true), this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*     */       } 
/*     */     } 
/* 872 */     return (this.panda.canPerformAction() && this.lookAt != null);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 877 */     if (this.lookAt != null)
/* 878 */       super.tick(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\panda\Panda$PandaLookAtPlayerGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */