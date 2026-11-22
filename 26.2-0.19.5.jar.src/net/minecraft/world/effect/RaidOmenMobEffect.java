/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.particles.ParticleOptions;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ class RaidOmenMobEffect extends MobEffect {
/*    */   protected RaidOmenMobEffect(MobEffectCategory category, int color, ParticleOptions particleOptions) {
/* 13 */     super(category, color, particleOptions);
/*    */   }
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) {
/* 18 */     return (remainingDuration == 1);
/*    */   }
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 23 */     if (mob instanceof ServerPlayer) {
/* 23 */       ServerPlayer player = (ServerPlayer)mob;
/* 23 */       if (!mob.isSpectator()) {
/* 24 */         BlockPos raidOmenPosition = player.getRaidOmenPosition();
/* 26 */         if (raidOmenPosition != null) {
/* 27 */           level.getRaids().createOrExtendRaid(player, raidOmenPosition);
/* 28 */           player.clearRaidOmenPosition();
/* 29 */           return false;
/*    */         } 
/*    */       } 
/*    */     } 
/* 32 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\RaidOmenMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */