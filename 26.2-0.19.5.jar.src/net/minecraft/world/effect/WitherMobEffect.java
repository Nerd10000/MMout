/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class WitherMobEffect extends MobEffect {
/*    */   public static final int DAMAGE_INTERVAL = 40;
/*    */   
/*    */   protected WitherMobEffect(MobEffectCategory category, int color) {
/* 11 */     super(category, color);
/*    */   }
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 16 */     mob.hurtServer(level, mob.damageSources().wither(), 1.0F);
/* 17 */     return true;
/*    */   }
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
/* 22 */     int interval = 40 >> amplification;
/* 23 */     if (interval > 0)
/* 24 */       return (tickCount % interval == 0); 
/* 26 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\WitherMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */