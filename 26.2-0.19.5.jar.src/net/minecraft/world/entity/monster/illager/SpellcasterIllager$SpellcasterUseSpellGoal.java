/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ public abstract class SpellcasterUseSpellGoal extends Goal {
/*     */   protected int attackWarmupDelay;
/*     */   
/*     */   protected int nextAttackTickCount;
/*     */   
/*     */   public boolean canUse() {
/* 158 */     LivingEntity target = SpellcasterIllager.this.getTarget();
/* 159 */     if (target == null || !target.isAlive())
/* 160 */       return false; 
/* 162 */     if (SpellcasterIllager.this.isCastingSpell())
/* 164 */       return false; 
/* 166 */     if (SpellcasterIllager.this.tickCount < this.nextAttackTickCount)
/* 167 */       return false; 
/* 169 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 174 */     LivingEntity target = SpellcasterIllager.this.getTarget();
/* 175 */     return (target != null && target.isAlive() && this.attackWarmupDelay > 0);
/*     */   }
/*     */   
/*     */   public void start() {
/* 180 */     this.attackWarmupDelay = adjustedTickDelay(getCastWarmupTime());
/* 181 */     SpellcasterIllager.this.spellCastingTickCount = getCastingTime();
/* 182 */     this.nextAttackTickCount = SpellcasterIllager.this.tickCount + getCastingInterval();
/* 183 */     SoundEvent spellPrepareSound = getSpellPrepareSound();
/* 184 */     if (spellPrepareSound != null)
/* 185 */       SpellcasterIllager.this.playSound(spellPrepareSound, 1.0F, 1.0F); 
/* 187 */     SpellcasterIllager.this.setIsCastingSpell(getSpell());
/*     */   }
/*     */   
/*     */   public void tick() {
/* 192 */     this.attackWarmupDelay--;
/* 193 */     if (this.attackWarmupDelay == 0) {
/* 194 */       performSpellCasting();
/* 195 */       SpellcasterIllager.this.playSound(SpellcasterIllager.this.getCastingSoundEvent(), 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract void performSpellCasting();
/*     */   
/*     */   protected int getCastWarmupTime() {
/* 202 */     return 20;
/*     */   }
/*     */   
/*     */   protected abstract int getCastingTime();
/*     */   
/*     */   protected abstract int getCastingInterval();
/*     */   
/*     */   protected abstract SoundEvent getSpellPrepareSound();
/*     */   
/*     */   protected abstract SpellcasterIllager.IllagerSpell getSpell();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\SpellcasterIllager$SpellcasterUseSpellGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */