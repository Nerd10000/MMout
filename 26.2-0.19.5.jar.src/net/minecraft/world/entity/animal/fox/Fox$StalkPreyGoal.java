/*     */ package net.minecraft.world.entity.animal.fox;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ class StalkPreyGoal extends Goal {
/*     */   public StalkPreyGoal() {
/* 854 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 859 */     if (Fox.this.isSleeping())
/* 860 */       return false; 
/* 863 */     LivingEntity target = Fox.this.getTarget();
/* 864 */     return (target != null && target.isAlive() && Fox.STALKABLE_PREY.test(target) && Fox.this.distanceToSqr((Entity)target) > 36.0D && !Fox.this.isCrouching() && !Fox.this.isInterested() && !Fox.access$000(Fox.this));
/*     */   }
/*     */   
/*     */   public void start() {
/* 869 */     Fox.this.setSitting(false);
/* 870 */     Fox.this.setFaceplanted(false);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 876 */     LivingEntity target = Fox.this.getTarget();
/* 877 */     if (target != null && Fox.isPathClear(Fox.this, target)) {
/* 878 */       Fox.this.setIsInterested(true);
/* 879 */       Fox.this.setIsCrouching(true);
/* 880 */       Fox.this.getNavigation().stop();
/* 881 */       Fox.this.getLookControl().setLookAt((Entity)target, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/*     */     } else {
/* 883 */       Fox.this.setIsInterested(false);
/* 884 */       Fox.this.setIsCrouching(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 890 */     LivingEntity target = Fox.this.getTarget();
/* 891 */     if (target == null)
/*     */       return; 
/* 894 */     Fox.this.getLookControl().setLookAt((Entity)target, Fox.this.getMaxHeadYRot(), Fox.this.getMaxHeadXRot());
/* 895 */     if (Fox.this.distanceToSqr((Entity)target) <= 36.0D) {
/* 896 */       Fox.this.setIsInterested(true);
/* 897 */       Fox.this.setIsCrouching(true);
/* 898 */       Fox.this.getNavigation().stop();
/*     */     } else {
/* 900 */       Fox.this.getNavigation().moveTo((Entity)target, 1.5D);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\fox\Fox$StalkPreyGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */