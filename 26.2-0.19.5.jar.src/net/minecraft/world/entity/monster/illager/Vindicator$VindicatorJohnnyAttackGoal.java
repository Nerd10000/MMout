/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ 
/*     */ class VindicatorJohnnyAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
/*     */   public VindicatorJohnnyAttackGoal(Vindicator mob) {
/* 216 */     super((Mob)mob, LivingEntity.class, 0, true, true, (target, level) -> target.attackable());
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 221 */     return (((Vindicator)this.mob).isJohnny && super.canUse());
/*     */   }
/*     */   
/*     */   public void start() {
/* 226 */     super.start();
/* 227 */     this.mob.setNoActionTime(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\Vindicator$VindicatorJohnnyAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */