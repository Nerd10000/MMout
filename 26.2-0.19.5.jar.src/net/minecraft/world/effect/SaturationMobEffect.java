/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ class SaturationMobEffect extends InstantaneousMobEffect {
/*    */   protected SaturationMobEffect(MobEffectCategory category, int color) {
/* 12 */     super(category, color);
/*    */   }
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 17 */     if (mob instanceof Player) {
/* 17 */       Player player = (Player)mob;
/* 18 */       player.getFoodData().eat(amplification + 1, 1.0F);
/*    */     } 
/* 20 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\SaturationMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */