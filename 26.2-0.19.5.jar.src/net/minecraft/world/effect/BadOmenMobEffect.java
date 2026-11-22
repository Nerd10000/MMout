/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.raid.Raid;
/*    */ 
/*    */ class BadOmenMobEffect extends MobEffect {
/*    */   protected BadOmenMobEffect(MobEffectCategory category, int color) {
/* 14 */     super(category, color);
/*    */   }
/*    */   
/*    */   public boolean shouldApplyEffectTickThisTick(int remainingDuration, int amplification) {
/* 19 */     return true;
/*    */   }
/*    */   
/*    */   public boolean applyEffectTick(ServerLevel level, LivingEntity mob, int amplification) {
/* 24 */     if (mob instanceof ServerPlayer) {
/* 24 */       ServerPlayer player = (ServerPlayer)mob;
/* 24 */       if (!player.isSpectator() && 
/* 25 */         level.getDifficulty() != Difficulty.PEACEFUL && level.isVillage(player.blockPosition())) {
/* 26 */         Raid raid = level.getRaidAt(player.blockPosition());
/* 28 */         if (raid == null || raid.getRaidOmenLevel() < raid.getMaxRaidOmenLevel()) {
/* 29 */           player.addEffect(new MobEffectInstance(MobEffects.RAID_OMEN, 600, amplification));
/* 30 */           player.setRaidOmenPosition(player.blockPosition());
/* 31 */           return false;
/*    */         } 
/*    */       } 
/*    */     } 
/* 35 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\BadOmenMobEffect.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */