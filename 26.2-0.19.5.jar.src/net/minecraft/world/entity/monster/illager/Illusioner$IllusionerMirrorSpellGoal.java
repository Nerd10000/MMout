/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ 
/*     */ class IllusionerMirrorSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */   public boolean canUse() {
/* 181 */     if (!super.canUse())
/* 182 */       return false; 
/* 184 */     if (Illusioner.this.hasEffect(MobEffects.INVISIBILITY))
/* 185 */       return false; 
/* 187 */     return true;
/*     */   }
/*     */   
/*     */   protected int getCastingTime() {
/* 192 */     return 20;
/*     */   }
/*     */   
/*     */   protected int getCastingInterval() {
/* 197 */     return 340;
/*     */   }
/*     */   
/*     */   protected void performSpellCasting() {
/* 202 */     Illusioner.this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 1200));
/*     */   }
/*     */   
/*     */   protected SoundEvent getSpellPrepareSound() {
/* 207 */     return SoundEvents.ILLUSIONER_PREPARE_MIRROR;
/*     */   }
/*     */   
/*     */   protected SpellcasterIllager.IllagerSpell getSpell() {
/* 212 */     return SpellcasterIllager.IllagerSpell.DISAPPEAR;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\Illusioner$IllusionerMirrorSpellGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */