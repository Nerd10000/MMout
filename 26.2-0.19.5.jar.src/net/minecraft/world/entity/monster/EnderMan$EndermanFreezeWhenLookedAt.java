/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ class EndermanFreezeWhenLookedAt extends Goal {
/*     */   private final EnderMan enderman;
/*     */   
/*     */   private LivingEntity target;
/*     */   
/*     */   public EndermanFreezeWhenLookedAt(EnderMan enderman) {
/* 503 */     this.enderman = enderman;
/* 504 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/*     */     Player playerTarget;
/* 509 */     this.target = this.enderman.getTarget();
/* 510 */     LivingEntity livingEntity = this.target;
/* 510 */     if (livingEntity instanceof Player) {
/* 510 */       playerTarget = (Player)livingEntity;
/*     */     } else {
/* 511 */       return false;
/*     */     } 
/* 513 */     double dist = this.target.distanceToSqr((Entity)this.enderman);
/* 514 */     if (dist > 256.0D)
/* 515 */       return false; 
/* 517 */     return this.enderman.isBeingStaredBy(playerTarget);
/*     */   }
/*     */   
/*     */   public void start() {
/* 522 */     this.enderman.getNavigation().stop();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 527 */     this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\EnderMan$EndermanFreezeWhenLookedAt.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */