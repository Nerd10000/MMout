/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class AbsorptionMobEffect extends MobEffect {
/*    */   protected AbsorptionMobEffect(MobEffectCategory category, int color) {
/* 10 */     super(category, color);
/*    */   }
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 15 */     return (mob.getAbsorptionAmount() > 0.0F);
/*    */   }
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
/* 20 */     return true;
/*    */   }
/*    */   
/*    */   public void onEffectStarted(LivingEntity mob, int amplifier) {
/* 25 */     super.onEffectStarted(mob, amplifier);
/* 26 */     mob.setAbsorptionAmount(Math.max(mob.getAbsorptionAmount(), (4 * (1 + amplifier))));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\AbsorptionMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */