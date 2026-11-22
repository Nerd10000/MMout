/*     */ package net.minecraft.world.entity.animal.dolphin;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ class DolphinSwimWithPlayerGoal extends Goal {
/*     */   private final Dolphin dolphin;
/*     */   
/*     */   private final double speedModifier;
/*     */   
/*     */   private Player player;
/*     */   
/*     */   public DolphinSwimWithPlayerGoal(Dolphin dolphin, double speedModifier) {
/* 469 */     this.dolphin = dolphin;
/* 470 */     this.speedModifier = speedModifier;
/* 471 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 476 */     this.player = getServerLevel((Entity)this.dolphin).getNearestPlayer(Dolphin.SWIM_WITH_PLAYER_TARGETING, (LivingEntity)this.dolphin);
/* 477 */     if (this.player == null)
/* 478 */       return false; 
/* 480 */     return (this.player.isSwimming() && this.dolphin.getTarget() != this.player);
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 485 */     return (this.player != null && this.player.isSwimming() && this.dolphin.distanceToSqr((Entity)this.player) < 256.0D);
/*     */   }
/*     */   
/*     */   public void start() {
/* 490 */     this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), (Entity)this.dolphin);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 495 */     this.player = null;
/* 496 */     this.dolphin.getNavigation().stop();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 501 */     this.dolphin.getLookControl().setLookAt((Entity)this.player, (this.dolphin.getMaxHeadYRot() + 20), this.dolphin.getMaxHeadXRot());
/* 502 */     if (this.dolphin.distanceToSqr((Entity)this.player) < 6.25D) {
/* 503 */       this.dolphin.getNavigation().stop();
/*     */     } else {
/* 505 */       this.dolphin.getNavigation().moveTo((Entity)this.player, this.speedModifier);
/*     */     } 
/* 508 */     if (this.player.isSwimming() && this.player.level().getRandom().nextInt(6) == 0)
/* 509 */       this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), (Entity)this.dolphin); 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\dolphin\Dolphin$DolphinSwimWithPlayerGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */