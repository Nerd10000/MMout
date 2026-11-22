/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class PoisonMobEffect extends MobEffect {
/*    */   public static final int DAMAGE_INTERVAL = 25;
/*    */   
/*    */   protected PoisonMobEffect(MobEffectCategory category, int color) {
/* 10 */     super(category, color);
/*    */   }
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 15 */     if (mob.getHealth() > 1.0F)
/* 16 */       mob.hurtServer(level, mob.damageSources().magic(), 1.0F); 
/* 18 */     return true;
/*    */   }
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
/* 23 */     int interval = 25 >> amplification;
/* 24 */     if (interval > 0)
/* 25 */       return (tickCount % interval == 0); 
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\PoisonMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */