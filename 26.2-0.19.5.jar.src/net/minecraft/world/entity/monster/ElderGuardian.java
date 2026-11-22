/*    */ package net.minecraft.world.entity.monster;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffectUtil;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.EntityTypes;
/*    */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ElderGuardian extends Guardian {
/* 22 */   public static final float ELDER_SIZE_SCALE = EntityTypes.ELDER_GUARDIAN.getWidth() / EntityTypes.GUARDIAN.getWidth();
/*    */   
/*    */   private static final int EFFECT_INTERVAL = 1200;
/*    */   
/*    */   private static final int EFFECT_RADIUS = 50;
/*    */   
/*    */   private static final int EFFECT_DURATION = 6000;
/*    */   
/*    */   private static final int EFFECT_AMPLIFIER = 2;
/*    */   
/*    */   private static final int EFFECT_DISPLAY_LIMIT = 1200;
/*    */   
/*    */   public ElderGuardian(EntityType<? extends ElderGuardian> type, Level level) {
/* 31 */     super((EntityType)type, level);
/* 33 */     setPersistenceRequired();
/* 36 */     if (this.randomStrollGoal != null)
/* 37 */       this.randomStrollGoal.setInterval(400); 
/*    */   }
/*    */   
/*    */   public static AttributeSupplier.Builder createAttributes() {
/* 42 */     return Guardian.createAttributes()
/* 43 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 44 */       .add(Attributes.ATTACK_DAMAGE, 8.0D)
/* 45 */       .add(Attributes.MAX_HEALTH, 80.0D);
/*    */   }
/*    */   
/*    */   public int getAttackDuration() {
/* 50 */     return 60;
/*    */   }
/*    */   
/*    */   protected SoundEvent getAmbientSound() {
/* 55 */     return isInWater() ? SoundEvents.ELDER_GUARDIAN_AMBIENT : SoundEvents.ELDER_GUARDIAN_AMBIENT_LAND;
/*    */   }
/*    */   
/*    */   protected SoundEvent getHurtSound(DamageSource source) {
/* 60 */     return isInWater() ? SoundEvents.ELDER_GUARDIAN_HURT : SoundEvents.ELDER_GUARDIAN_HURT_LAND;
/*    */   }
/*    */   
/*    */   protected SoundEvent getDeathSound() {
/* 65 */     return isInWater() ? SoundEvents.ELDER_GUARDIAN_DEATH : SoundEvents.ELDER_GUARDIAN_DEATH_LAND;
/*    */   }
/*    */   
/*    */   protected SoundEvent getFlopSound() {
/* 70 */     return SoundEvents.ELDER_GUARDIAN_FLOP;
/*    */   }
/*    */   
/*    */   protected void customServerAiStep(ServerLevel level) {
/* 75 */     super.customServerAiStep(level);
/* 78 */     if ((this.tickCount + getId()) % 1200 == 0) {
/* 79 */       MobEffectInstance miningFatigue = new MobEffectInstance(MobEffects.MINING_FATIGUE, 6000, 2);
/* 80 */       List<ServerPlayer> affectedPlayers = MobEffectUtil.addEffectToPlayersAround(level, (Entity)this, position(), 50.0D, miningFatigue, 1200);
/* 81 */       affectedPlayers.forEach(player -> player.connection.send((Packet)new ClientboundGameEventPacket(ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT, isSilent() ? 0.0F : 1.0F)));
/*    */     } 
/* 85 */     if (!hasHome())
/* 86 */       setHomeTo(blockPosition(), 16); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\ElderGuardian.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */