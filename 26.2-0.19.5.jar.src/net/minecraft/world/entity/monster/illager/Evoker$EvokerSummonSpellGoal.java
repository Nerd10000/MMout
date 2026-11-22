/*     */ package net.minecraft.world.entity.monster.illager;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.monster.Vex;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ class EvokerSummonSpellGoal extends SpellcasterIllager.SpellcasterUseSpellGoal {
/*     */   private final TargetingConditions vexCountTargeting;
/*     */   
/*     */   private EvokerSummonSpellGoal() {
/* 229 */     this.vexCountTargeting = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */   }
/*     */   
/*     */   public boolean canUse() {
/* 233 */     if (!super.canUse())
/* 234 */       return false; 
/* 238 */     int vexes = getServerLevel(Evoker.this.level()).getNearbyEntities(Vex.class, this.vexCountTargeting, (LivingEntity)Evoker.this, Evoker.this.getBoundingBox().inflate(16.0D)).size();
/* 239 */     return (Evoker.access$000(Evoker.this).nextInt(8) + 1 > vexes);
/*     */   }
/*     */   
/*     */   protected int getCastingTime() {
/* 244 */     return 100;
/*     */   }
/*     */   
/*     */   protected int getCastingInterval() {
/* 249 */     return 340;
/*     */   }
/*     */   
/*     */   protected void performSpellCasting() {
/* 254 */     ServerLevel serverLevel = (ServerLevel)Evoker.this.level();
/* 255 */     PlayerTeam evokerTeam = Evoker.this.getTeam();
/* 256 */     for (int i = 0; i < 3; i++) {
/* 257 */       BlockPos pos = Evoker.this.blockPosition().offset(-2 + Evoker.access$100(Evoker.this).nextInt(5), 1, -2 + Evoker.access$200(Evoker.this).nextInt(5));
/* 258 */       Vex vex = (Vex)EntityTypes.VEX.create(Evoker.this.level(), EntitySpawnReason.MOB_SUMMONED);
/* 259 */       if (vex != null) {
/* 260 */         vex.snapTo(pos, 0.0F, 0.0F);
/* 261 */         vex.finalizeSpawn((ServerLevelAccessor)serverLevel, serverLevel.getCurrentDifficultyAt(pos), EntitySpawnReason.MOB_SUMMONED, null);
/* 262 */         vex.setOwner((Mob)Evoker.this);
/* 263 */         vex.setBoundOrigin(pos);
/* 264 */         vex.setLimitedLife(20 * (30 + Evoker.access$300(Evoker.this).nextInt(90)));
/* 265 */         if (evokerTeam != null)
/* 266 */           serverLevel.getScoreboard().addPlayerToTeam(vex.getScoreboardName(), evokerTeam); 
/* 268 */         serverLevel.addFreshEntityWithPassengers((Entity)vex);
/* 269 */         serverLevel.gameEvent((Holder)GameEvent.ENTITY_PLACE, pos, GameEvent.Context.of((Entity)Evoker.this));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SoundEvent getSpellPrepareSound() {
/* 276 */     return SoundEvents.EVOKER_PREPARE_SUMMON;
/*     */   }
/*     */   
/*     */   protected SpellcasterIllager.IllagerSpell getSpell() {
/* 281 */     return SpellcasterIllager.IllagerSpell.SUMMON_VEX;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\illager\Evoker$EvokerSummonSpellGoal.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */