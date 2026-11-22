/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ 
/*     */ class GuardianAttackSelector implements TargetingConditions.Selector {
/*     */   private final Guardian guardian;
/*     */   
/*     */   public GuardianAttackSelector(Guardian guardian) {
/* 356 */     this.guardian = guardian;
/*     */   }
/*     */   
/*     */   public boolean test(LivingEntity target, ServerLevel level) {
/* 361 */     return ((target instanceof net.minecraft.world.entity.player.Player || target instanceof net.minecraft.world.entity.animal.squid.Squid || target instanceof net.minecraft.world.entity.animal.axolotl.Axolotl) && target.distanceToSqr((Entity)this.guardian) > 9.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Guardian$GuardianAttackSelector.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */