/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ class HungerMobEffect extends MobEffect {
/*    */   protected HungerMobEffect(MobEffectCategory category, int color) {
/* 12 */     super(category, color);
/*    */   }
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
/* 17 */     if (mob instanceof Player) {
/* 17 */       Player player = (Player)mob;
/* 19 */       player.causeFoodExhaustion(0.005F * (amplification + 1));
/*    */     } 
/* 21 */     return true;
/*    */   }
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
/* 26 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\HungerMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */