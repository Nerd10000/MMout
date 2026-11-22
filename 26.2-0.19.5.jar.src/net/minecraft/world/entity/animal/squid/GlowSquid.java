/*     */ package net.minecraft.world.entity.animal.squid;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class GlowSquid extends Squid {
/*  27 */   private static final EntityDataAccessor<Integer> DATA_DARK_TICKS_REMAINING = SynchedEntityData.defineId(GlowSquid.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int DEFAULT_DARK_TICKS_REMAINING = 0;
/*     */   
/*     */   public GlowSquid(EntityType<? extends GlowSquid> type, Level level) {
/*  31 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   protected ParticleOptions getInkParticle() {
/*  36 */     return (ParticleOptions)ParticleTypes.GLOW_SQUID_INK;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  41 */     super.defineSynchedData(entityData);
/*  42 */     entityData.define(DATA_DARK_TICKS_REMAINING, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  47 */     return (AgeableMob)EntityTypes.GLOW_SQUID.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   protected SoundEvent getSquirtSound() {
/*  52 */     return SoundEvents.GLOW_SQUID_SQUIRT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  57 */     return SoundEvents.GLOW_SQUID_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/*  62 */     return SoundEvents.GLOW_SQUID_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  67 */     return SoundEvents.GLOW_SQUID_DEATH;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  72 */     super.addAdditionalSaveData(output);
/*  73 */     output.putInt("DarkTicksRemaining", getDarkTicksRemaining());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  78 */     super.readAdditionalSaveData(input);
/*  79 */     setDarkTicks(input.getIntOr("DarkTicksRemaining", 0));
/*     */   }
/*     */   
/*     */   public void aiStep() {
/*  84 */     super.aiStep();
/*  86 */     int darkTicks = getDarkTicksRemaining();
/*  87 */     if (darkTicks > 0)
/*  88 */       setDarkTicks(darkTicks - 1); 
/*  91 */     level().addParticle((ParticleOptions)ParticleTypes.GLOW, getRandomX(0.6D), getRandomY(), getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  96 */     boolean hurt = super.hurtServer(level, source, damage);
/*  97 */     if (hurt)
/*  98 */       setDarkTicks(100); 
/* 101 */     return hurt;
/*     */   }
/*     */   
/*     */   private void setDarkTicks(int ticks) {
/* 105 */     this.entityData.set(DATA_DARK_TICKS_REMAINING, Integer.valueOf(ticks));
/*     */   }
/*     */   
/*     */   public int getDarkTicksRemaining() {
/* 109 */     return ((Integer)this.entityData.get(DATA_DARK_TICKS_REMAINING)).intValue();
/*     */   }
/*     */   
/*     */   public static boolean checkGlowSquidSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 113 */     return (pos.getY() <= level.getSeaLevel() - 33 && level.getRawBrightness(pos, 0) == 0 && level.getBlockState(pos).is(Blocks.WATER));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\squid\GlowSquid.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */