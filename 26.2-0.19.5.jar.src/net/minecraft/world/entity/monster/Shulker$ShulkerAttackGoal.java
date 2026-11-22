/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.projectile.ShulkerBullet;
/*     */ 
/*     */ class ShulkerAttackGoal extends Goal {
/*     */   private int attackTime;
/*     */   
/*     */   public ShulkerAttackGoal() {
/* 681 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 686 */     LivingEntity target = Shulker.this.getTarget();
/* 687 */     if (target == null || !target.isAlive())
/* 688 */       return false; 
/* 690 */     if (Shulker.this.level().getDifficulty() == Difficulty.PEACEFUL)
/* 691 */       return false; 
/* 694 */     return true;
/*     */   }
/*     */   
/*     */   public void start() {
/* 699 */     this.attackTime = 20;
/* 700 */     Shulker.this.setRawPeekAmount(100);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 705 */     Shulker.this.setRawPeekAmount(0);
/*     */   }
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 710 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 715 */     if (Shulker.this.level().getDifficulty() == Difficulty.PEACEFUL)
/*     */       return; 
/* 718 */     this.attackTime--;
/* 720 */     LivingEntity target = Shulker.this.getTarget();
/* 721 */     if (target == null)
/*     */       return; 
/* 724 */     Shulker.this.getLookControl().setLookAt((Entity)target, 180.0F, 180.0F);
/* 726 */     double distance = Shulker.this.distanceToSqr((Entity)target);
/* 728 */     if (distance < 400.0D) {
/* 729 */       if (this.attackTime <= 0) {
/* 730 */         this.attackTime = 20 + Shulker.access$200(Shulker.this).nextInt(10) * 20 / 2;
/* 732 */         Shulker.this.level().addFreshEntity((Entity)new ShulkerBullet(Shulker.this.level(), (LivingEntity)Shulker.this, (Entity)target, Shulker.this.getAttachFace().getAxis()));
/* 733 */         Shulker.this.playSound(SoundEvents.SHULKER_SHOOT, 2.0F, (Shulker.access$300(Shulker.this).nextFloat() - Shulker.access$400(Shulker.this).nextFloat()) * 0.2F + 1.0F);
/*     */       } 
/*     */     } else {
/* 736 */       Shulker.this.setTarget(null);
/*     */     } 
/* 739 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Shulker$ShulkerAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */