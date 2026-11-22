/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class RegenerationMobEffect extends MobEffect {
/*    */   protected RegenerationMobEffect(MobEffectCategory category, int color) {
/* 10 */     super(category, color);
/*    */   }
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 15 */     if (mob.getHealth() < mob.getMaxHealth())
/* 16 */       mob.heal(1.0F); 
/* 18 */     return true;
/*    */   }
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
/* 23 */     int interval = 50 >> amplification;
/* 24 */     if (interval > 0)
/* 25 */       return (tickCount % interval == 0); 
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\RegenerationMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */