/*     */ package net.minecraft.world.entity.animal.squid;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ class SquidRandomMovementGoal extends Goal {
/*     */   private final Squid squid;
/*     */   
/*     */   public SquidRandomMovementGoal(Squid squid) {
/* 254 */     this.squid = squid;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 259 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 264 */     int noActionTime = this.squid.getNoActionTime();
/* 266 */     if (noActionTime > 100) {
/* 267 */       this.squid.movementVector = Vec3.ZERO;
/* 268 */     } else if (this.squid.getRandom().nextInt(reducedTickDelay(50)) == 0 || !Squid.access$000(this.squid) || !this.squid.hasMovementVector()) {
/* 269 */       float angle = this.squid.getRandom().nextFloat() * 6.2831855F;
/* 270 */       this.squid
/*     */         
/* 273 */         .movementVector = new Vec3((Mth.cos(angle) * 0.2F), (-0.1F + this.squid.getRandom().nextFloat() * 0.2F), (Mth.sin(angle) * 0.2F));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\squid\Squid$SquidRandomMovementGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */