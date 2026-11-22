/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.item.Items;
/*     */ 
/*     */ class DrownedTridentAttackGoal extends RangedAttackGoal {
/*     */   private final Drowned drowned;
/*     */   
/*     */   public DrownedTridentAttackGoal(RangedAttackMob mob, double speedModifier, int attackInterval, float attackRadius) {
/* 346 */     super(mob, speedModifier, attackInterval, attackRadius);
/* 347 */     this.drowned = (Drowned)mob;
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 352 */     return (super.canUse() && this.drowned.getMainHandItem().is(Items.TRIDENT));
/*     */   }
/*     */   
/*     */   public void start() {
/* 357 */     super.start();
/* 358 */     this.drowned.setAggressive(true);
/* 359 */     this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 364 */     super.stop();
/* 365 */     this.drowned.stopUsingItem();
/* 366 */     this.drowned.setAggressive(false);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Drowned$DrownedTridentAttackGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */