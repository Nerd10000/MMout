/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ 
/*     */ class IllusionerBlindnessSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */   private int lastTargetId;
/*     */   
/*     */   public boolean canUse() {
/* 221 */     if (!super.canUse())
/* 222 */       return false; 
/* 224 */     if (Illusioner.this.getTarget() == null)
/* 225 */       return false; 
/* 227 */     if (Illusioner.this.getTarget().getId() == this.lastTargetId)
/* 228 */       return false; 
/* 230 */     if (!getServerLevel((Entity)Illusioner.this).getCurrentDifficultyAt(Illusioner.this.blockPosition()).isHarderThan(Difficulty.NORMAL.ordinal()))
/* 231 */       return false; 
/* 233 */     return true;
/*     */   }
/*     */   
/*     */   public void start() {
/* 238 */     super.start();
/* 240 */     LivingEntity target = Illusioner.this.getTarget();
/* 241 */     if (target != null)
/* 242 */       this.lastTargetId = target.getId(); 
/*     */   }
/*     */   
/*     */   protected int getCastingTime() {
/* 248 */     return 20;
/*     */   }
/*     */   
/*     */   protected int getCastingInterval() {
/* 253 */     return 180;
/*     */   }
/*     */   
/*     */   protected void performSpellCasting() {
/* 258 */     Illusioner.this.getTarget().addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 400), (Entity)Illusioner.this);
/*     */   }
/*     */   
/*     */   protected SoundEvent getSpellPrepareSound() {
/* 263 */     return SoundEvents.ILLUSIONER_PREPARE_BLINDNESS;
/*     */   }
/*     */   
/*     */   protected SpellcasterIllager.IllagerSpell getSpell() {
/* 268 */     return SpellcasterIllager.IllagerSpell.BLINDNESS;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\Illusioner$IllusionerBlindnessSpellGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */