/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class HealOrHarmMobEffect extends InstantaneousMobEffect {
/*    */   private final boolean isHarm;
/*    */   
/*    */   public HealOrHarmMobEffect(MobEffectCategory category, int color, boolean isHarm) {
/* 14 */     super(category, color);
/* 15 */     this.isHarm = isHarm;
/*    */   }
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 20 */     if (this.isHarm == mob.isInvertedHealAndHarm()) {
/* 21 */       mob.heal(Math.max(4 << amplification, 0));
/*    */     } else {
/* 23 */       mob.hurtServer(level, mob.damageSources().magic(), (6 << amplification));
/*    */     } 
/* 25 */     return true;
/*    */   }
/*    */   
/*    */   public void applyInstantaneousEffect(ServerLevel serverLevel, Entity source, Entity owner, LivingEntity mob, int amplification, double scale) {
/* 30 */     if (this.isHarm == mob.isInvertedHealAndHarm()) {
/* 31 */       int amount = (int)(scale * (4 << amplification) + 0.5D);
/* 32 */       mob.heal(amount);
/*    */     } else {
/* 34 */       int amount = (int)(scale * (6 << amplification) + 0.5D);
/* 35 */       if (source == null) {
/* 36 */         mob.hurtServer(serverLevel, mob.damageSources().magic(), amount);
/*    */       } else {
/* 38 */         mob.hurtServer(serverLevel, mob.damageSources().indirectMagic(source, owner), amount);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\HealOrHarmMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */