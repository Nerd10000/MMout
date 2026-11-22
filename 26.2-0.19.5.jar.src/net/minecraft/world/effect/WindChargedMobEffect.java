/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.core.particles.ParticleTypes;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.random.WeightedList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ class WindChargedMobEffect extends MobEffect {
/*    */   protected WindChargedMobEffect(MobEffectCategory category, int color) {
/* 16 */     super(category, color, (ParticleOptions)ParticleTypes.SMALL_GUST);
/*    */   }
/*    */   
/*    */   public void onMobRemoved(ServerLevel level, LivingEntity mob, int amplifier, Entity.RemovalReason reason) {
/* 21 */     if (reason == Entity.RemovalReason.KILLED) {
/* 22 */       double x = mob.getX();
/* 23 */       double y = mob.getY() + (mob.getBbHeight() / 2.0F);
/* 24 */       double z = mob.getZ();
/* 25 */       float gustStrength = 3.0F + mob.getRandom().nextFloat() * 2.0F;
/* 26 */       level.explode((Entity)mob, null, AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR, x, y, z, gustStrength, false, Level.ExplosionInteraction.TRIGGER, (ParticleOptions)ParticleTypes.GUST_EMITTER_SMALL, (ParticleOptions)ParticleTypes.GUST_EMITTER_LARGE, WeightedList.of(), (Holder)SoundEvents.BREEZE_WIND_CHARGE_BURST);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\WindChargedMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */