/*    */ package net.minecraft.client.renderer.fog.environment;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.effect.MobEffect;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.level.material.FogType;
/*    */ 
/*    */ public abstract class MobEffectFogEnvironment extends FogEnvironment {
/*    */   public abstract Holder<MobEffect> getMobEffect();
/*    */   
/*    */   public boolean providesColor() {
/* 15 */     return false;
/*    */   }
/*    */   
/*    */   public boolean modifiesDarkness() {
/* 20 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isApplicable(FogType fogType, Entity entity) {
/* 25 */     if (entity instanceof LivingEntity) {
/* 25 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 25 */       if (livingEntity.hasEffect(getMobEffect()));
/*    */     } 
/* 25 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\renderer\fog\environment\MobEffectFogEnvironment.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */