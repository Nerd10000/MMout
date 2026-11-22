/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class EvokerWololoSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */   private final TargetingConditions wololoTargeting;
/*     */   
/*     */   public EvokerWololoSpellGoal() {
/* 286 */     this.wololoTargeting = TargetingConditions.forNonCombat().range(16.0D).selector((target, level) -> (((Sheep)target).getColor() == DyeColor.BLUE));
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 290 */     if (Evoker.this.getTarget() != null)
/* 292 */       return false; 
/* 294 */     if (Evoker.this.isCastingSpell())
/* 296 */       return false; 
/* 298 */     if (Evoker.this.tickCount < this.nextAttackTickCount)
/* 299 */       return false; 
/* 301 */     ServerLevel level = getServerLevel(Evoker.this.level());
/* 302 */     if (!((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue())
/* 303 */       return false; 
/* 306 */     List<Sheep> entities = level.getNearbyEntities(Sheep.class, this.wololoTargeting, (LivingEntity)Evoker.this, Evoker.this.getBoundingBox().inflate(16.0D, 4.0D, 16.0D));
/* 308 */     if (entities.isEmpty())
/* 309 */       return false; 
/* 311 */     Evoker.this.setWololoTarget(entities.get(Evoker.access$400(Evoker.this).nextInt(entities.size())));
/* 312 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canContinueToUse() {
/* 318 */     return (Evoker.this.getWololoTarget() != null && this.attackWarmupDelay > 0);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 323 */     super.stop();
/* 324 */     Evoker.this.setWololoTarget((Sheep)null);
/*     */   }
/*     */   
/*     */   protected void performSpellCasting() {
/* 329 */     Sheep wololoTarget = Evoker.this.getWololoTarget();
/* 330 */     if (wololoTarget != null && wololoTarget.isAlive())
/* 331 */       wololoTarget.setColor(DyeColor.RED); 
/*     */   }
/*     */   
/*     */   protected int getCastWarmupTime() {
/* 337 */     return 40;
/*     */   }
/*     */   
/*     */   protected int getCastingTime() {
/* 342 */     return 60;
/*     */   }
/*     */   
/*     */   protected int getCastingInterval() {
/* 347 */     return 140;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSpellPrepareSound() {
/* 352 */     return SoundEvents.EVOKER_PREPARE_WOLOLO;
/*     */   }
/*     */   
/*     */   protected SpellcasterIllager.IllagerSpell getSpell() {
/* 357 */     return SpellcasterIllager.IllagerSpell.WOLOLO;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\Evoker$EvokerWololoSpellGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */