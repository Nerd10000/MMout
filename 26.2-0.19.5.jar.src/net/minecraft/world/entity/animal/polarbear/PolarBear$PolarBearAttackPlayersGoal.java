/*     */ package net.minecraft.world.entity.animal.polarbear;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ class PolarBearAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
/*     */   public PolarBearAttackPlayersGoal() {
/* 297 */     super((Mob)paramPolarBear, Player.class, 20, true, true, null);
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 302 */     if (PolarBear.this.isBaby())
/* 303 */       return false; 
/* 307 */     if (super.canUse()) {
/* 308 */       List<PolarBear> bears = PolarBear.this.level().getEntitiesOfClass(PolarBear.class, PolarBear.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
/* 309 */       for (PolarBear bear : bears) {
/* 310 */         if (bear.isBaby())
/* 311 */           return true; 
/*     */       } 
/*     */     } 
/* 316 */     return false;
/*     */   }
/*     */   
/*     */   protected double getFollowDistance() {
/* 321 */     return super.getFollowDistance() * 0.5D;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\polarbear\PolarBear$PolarBearAttackPlayersGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */