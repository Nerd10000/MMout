/*     */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WitherSkull extends AbstractHurtingProjectile {
/*  31 */   private static final EntityDataAccessor<Boolean> DATA_DANGEROUS = SynchedEntityData.defineId(WitherSkull.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final boolean DEFAULT_DANGEROUS = false;
/*     */   
/*     */   public WitherSkull(EntityType<? extends WitherSkull> type, Level level) {
/*  35 */     super((EntityType)type, level);
/*     */   }
/*     */   
/*     */   public WitherSkull(Level level, LivingEntity mob, Vec3 direction) {
/*  39 */     super(EntityTypes.WITHER_SKULL, mob, direction, level);
/*     */   }
/*     */   
/*     */   protected float getInertia() {
/*  44 */     return isDangerous() ? 0.73F : super.getInertia();
/*     */   }
/*     */   
/*     */   public boolean isOnFire() {
/*  49 */     return false;
/*     */   }
/*     */   
/*     */   public float getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid, float resistance) {
/*  54 */     if (isDangerous() && WitherBoss.canDestroy(block))
/*  55 */       return Math.min(0.8F, resistance); 
/*  58 */     return resistance;
/*     */   }
/*     */   
/*     */   protected void onHitEntity(EntityHitResult hitResult) {
/*     */     ServerLevel serverLevel;
/*     */     boolean wasHurt;
/*  63 */     super.onHitEntity(hitResult);
/*  65 */     Level level = level();
/*  65 */     if (level instanceof ServerLevel) {
/*  65 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/*     */       return;
/*     */     } 
/*  68 */     Entity entity = hitResult.getEntity();
/*  69 */     Entity owner = getOwner();
/*  71 */     if (owner instanceof LivingEntity) {
/*  71 */       LivingEntity livingOwner = (LivingEntity)owner;
/*  72 */       DamageSource damageSource = damageSources().witherSkull(this, (Entity)livingOwner);
/*  73 */       wasHurt = entity.hurtServer(serverLevel, damageSource, 8.0F);
/*  74 */       if (wasHurt)
/*  75 */         if (entity.isAlive()) {
/*  76 */           EnchantmentHelper.doPostAttackEffects(serverLevel, entity, damageSource);
/*     */         } else {
/*  78 */           livingOwner.heal(5.0F);
/*     */         }  
/*     */     } else {
/*  82 */       wasHurt = entity.hurtServer(serverLevel, damageSources().magic(), 5.0F);
/*     */     } 
/*  84 */     if (wasHurt && entity instanceof LivingEntity) {
/*  84 */       LivingEntity livingEntity = (LivingEntity)entity;
/*  85 */       int witherSeconds = 0;
/*  86 */       if (level().getDifficulty() == Difficulty.NORMAL) {
/*  87 */         witherSeconds = 10;
/*  88 */       } else if (level().getDifficulty() == Difficulty.HARD) {
/*  89 */         witherSeconds = 40;
/*     */       } 
/*  91 */       if (witherSeconds > 0)
/*  92 */         livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * witherSeconds, 1), getEffectSource()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onHit(HitResult hitResult) {
/*  99 */     super.onHit(hitResult);
/* 100 */     if (!level().isClientSide()) {
/* 101 */       level().explode((Entity)this, getX(), getY(), getZ(), 1.0F, false, Level.ExplosionInteraction.MOB);
/* 102 */       discard();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 108 */     entityData.define(DATA_DANGEROUS, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean isDangerous() {
/* 112 */     return ((Boolean)this.entityData.get(DATA_DANGEROUS)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDangerous(boolean value) {
/* 116 */     this.entityData.set(DATA_DANGEROUS, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   protected boolean shouldBurn() {
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 126 */     super.addAdditionalSaveData(output);
/* 127 */     output.putBoolean("dangerous", isDangerous());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 132 */     super.readAdditionalSaveData(input);
/* 133 */     setDangerous(input.getBooleanOr("dangerous", false));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\WitherSkull.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */