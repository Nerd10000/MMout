/*    */ package net.minecraft.client.renderer.fog.environment;
/*    */ 
/*    */ import net.minecraft.client.Camera;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.fog.FogData;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.effect.MobEffectInstance;
/*    */ import net.minecraft.world.effect.MobEffects;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public class DarknessFogEnvironment extends MobEffectFogEnvironment {
/*    */   public Holder<MobEffect> getMobEffect() {
/* 17 */     return MobEffects.DARKNESS;
/*    */   }
/*    */   
/*    */   public void setupFog(FogData fog, Camera camera, ClientLevel level, float renderDistance, DeltaTracker deltaTracker) {
/* 22 */     Entity entity = camera.entity();
/* 22 */     if (entity instanceof LivingEntity) {
/* 22 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 23 */       MobEffectInstance effect = livingEntity.getEffect(getMobEffect());
/* 24 */       if (effect != null) {
/* 25 */         float distance = Mth.lerp(effect.getBlendFactor(livingEntity, deltaTracker.getGameTimeDeltaPartialTick(false)), renderDistance, 15.0F);
/* 26 */         fog.environmentalStart = distance * 0.75F;
/* 27 */         fog.environmentalEnd = distance;
/* 28 */         fog.skyEnd = distance;
/* 29 */         fog.cloudEnd = distance;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public float getModifiedDarkness(LivingEntity entity, float darkness, float partialTickTime) {
/* 36 */     MobEffectInstance instance = entity.getEffect(getMobEffect());
/* 37 */     return (instance != null) ? Math.max(instance.getBlendFactor(entity, partialTickTime), darkness) : darkness;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\fog\environment\DarknessFogEnvironment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */