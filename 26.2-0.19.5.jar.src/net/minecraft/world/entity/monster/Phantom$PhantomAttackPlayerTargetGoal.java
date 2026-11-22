/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ class PhantomAttackPlayerTargetGoal extends Goal {
/* 489 */   private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
/*     */   
/* 491 */   private int nextScanTick = reducedTickDelay(20);
/*     */   
/*     */   public boolean canUse() {
/* 495 */     if (this.nextScanTick > 0) {
/* 496 */       this.nextScanTick--;
/* 497 */       return false;
/*     */     } 
/* 499 */     this.nextScanTick = reducedTickDelay(60);
/* 501 */     ServerLevel level = getServerLevel(Phantom.this.level());
/* 502 */     List<Player> players = level.getNearbyPlayers(this.attackTargeting, (LivingEntity)Phantom.this, Phantom.this.getBoundingBox().inflate(16.0D, 64.0D, 16.0D));
/* 503 */     if (!players.isEmpty()) {
/* 505 */       players.sort(Comparator.<Player, Comparable>comparing(Entity::getY).reversed());
/* 506 */       for (Player player : players) {
/* 507 */         if (Phantom.this.canAttack(level, (LivingEntity)player, TargetingConditions.DEFAULT)) {
/* 508 */           Phantom.this.setTarget((LivingEntity)player);
/* 509 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 513 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 518 */     LivingEntity target = Phantom.this.getTarget();
/* 519 */     if (target != null)
/* 520 */       return Phantom.this.canAttack(getServerLevel(Phantom.this.level()), target, TargetingConditions.DEFAULT); 
/* 523 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Phantom$PhantomAttackPlayerTargetGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */