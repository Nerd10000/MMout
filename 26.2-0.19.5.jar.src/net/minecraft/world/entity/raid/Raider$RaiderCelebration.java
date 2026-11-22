/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ public class RaiderCelebration extends Goal {
/*     */   private final Raider mob;
/*     */   
/*     */   public RaiderCelebration(Raider mob) {
/* 391 */     this.mob = mob;
/* 392 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 397 */     Raid currentRaid = this.mob.getCurrentRaid();
/* 398 */     return (this.mob.isAlive() && this.mob.getTarget() == null && currentRaid != null && currentRaid.isLoss());
/*     */   }
/*     */   
/*     */   public void start() {
/* 403 */     this.mob.setCelebrating(true);
/* 404 */     super.start();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 409 */     this.mob.setCelebrating(false);
/* 410 */     super.stop();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 415 */     if (!this.mob.isSilent() && Raider.access$000(this.mob).nextInt(adjustedTickDelay(100)) == 0)
/* 416 */       Raider.this.makeSound(Raider.this.getCelebrateSound()); 
/* 419 */     if (!this.mob.isPassenger() && Raider.access$100(this.mob).nextInt(adjustedTickDelay(50)) == 0)
/* 420 */       this.mob.getJumpControl().jump(); 
/* 423 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\raid\Raider$RaiderCelebration.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */