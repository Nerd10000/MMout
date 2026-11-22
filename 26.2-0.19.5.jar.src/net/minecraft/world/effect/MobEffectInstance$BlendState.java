/*     */ package net.minecraft.world.effect;
/*     */ 
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ class BlendState {
/*     */   private float factor;
/*     */   
/*     */   private float factorPreviousFrame;
/*     */   
/*     */   public void setImmediate(MobEffectInstance instance) {
/* 389 */     this.factor = hasEffect(instance) ? 1.0F : 0.0F;
/* 390 */     this.factorPreviousFrame = this.factor;
/*     */   }
/*     */   
/*     */   public void copyFrom(BlendState other) {
/* 394 */     this.factor = other.factor;
/* 395 */     this.factorPreviousFrame = other.factorPreviousFrame;
/*     */   }
/*     */   
/*     */   public void tick(MobEffectInstance instance) {
/* 399 */     this.factorPreviousFrame = this.factor;
/* 401 */     boolean hasEffect = hasEffect(instance);
/* 402 */     float target = hasEffect ? 1.0F : 0.0F;
/* 403 */     if (this.factor == target)
/*     */       return; 
/* 407 */     MobEffect effect = (MobEffect)instance.getEffect().value();
/* 408 */     int blendDuration = hasEffect ? effect.getBlendInDurationTicks() : effect.getBlendOutDurationTicks();
/* 409 */     if (blendDuration == 0) {
/* 410 */       this.factor = target;
/*     */     } else {
/* 412 */       float maxDeltaPerTick = 1.0F / blendDuration;
/* 413 */       this.factor += Mth.clamp(target - this.factor, -maxDeltaPerTick, maxDeltaPerTick);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean hasEffect(MobEffectInstance instance) {
/* 418 */     return !instance.endsWithin(((MobEffect)instance.getEffect().value()).getBlendOutAdvanceTicks());
/*     */   }
/*     */   
/*     */   public float getFactor(LivingEntity livingEntity, float partialTickTime) {
/* 422 */     if (livingEntity.isRemoved())
/* 426 */       this.factorPreviousFrame = this.factor; 
/* 429 */     return Mth.lerp(partialTickTime, this.factorPreviousFrame, this.factor);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\effect\MobEffectInstance$BlendState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */