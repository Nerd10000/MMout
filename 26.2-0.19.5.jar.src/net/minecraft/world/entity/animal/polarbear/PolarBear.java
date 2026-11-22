/*     */ package net.minecraft.world.entity.animal.polarbear;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.fox.Fox;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class PolarBear extends Animal implements NeutralMob {
/*  62 */   private static final EntityDataAccessor<Boolean> DATA_STANDING_ID = SynchedEntityData.defineId(PolarBear.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float STAND_ANIMATION_TICKS = 6.0F;
/*     */   
/*     */   private float clientSideStandAnimationO;
/*     */   
/*     */   private float clientSideStandAnimation;
/*     */   
/*     */   private int warningSoundTicks;
/*     */   
/*  69 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   
/*     */   private long persistentAngerEndTime;
/*     */   
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*  72 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.7F, 0.7F)
/*  73 */     .withEyeHeight(0.34375F)
/*  74 */     .withAttachments(EntityAttachments.builder()
/*  75 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.625F, 0.0F));
/*     */   
/*     */   public PolarBear(EntityType<? extends PolarBear> type, Level level) {
/*  78 */     super(type, level);
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  83 */     return (AgeableMob)EntityTypes.POLAR_BEAR.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/*  88 */     return false;
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  93 */     super.registerGoals();
/*  95 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  96 */     this.goalSelector.addGoal(1, (Goal)new PolarBearMeleeAttackGoal());
/*  97 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 2.0D, bear -> bear.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
/*  98 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.25D));
/*  99 */     this.goalSelector.addGoal(5, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0D));
/* 100 */     this.goalSelector.addGoal(6, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/* 101 */     this.goalSelector.addGoal(7, (Goal)new RandomLookAroundGoal((Mob)this));
/* 103 */     this.targetSelector.addGoal(1, (Goal)new PolarBearHurtByTargetGoal());
/* 104 */     this.targetSelector.addGoal(2, (Goal)new PolarBearAttackPlayersGoal());
/* 105 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/* 106 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, Fox.class, 10, true, true, (target, level) -> !isBaby()));
/* 107 */     this.targetSelector.addGoal(5, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 111 */     return Animal.createAnimalAttributes()
/* 112 */       .add(Attributes.MAX_HEALTH, 30.0D)
/* 113 */       .add(Attributes.FOLLOW_RANGE, 20.0D)
/* 114 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/* 115 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */   
/*     */   public static boolean checkPolarBearSpawnRules(EntityType<PolarBear> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 119 */     Holder<Biome> biome = level.getBiome(pos);
/* 121 */     if (biome.is(BiomeTags.POLAR_BEARS_SPAWN_ON_ALTERNATE_BLOCKS))
/* 122 */       return (isBrightEnoughToSpawn((BlockAndLightGetter)level, pos) && level.getBlockState(pos.below()).is(BlockTags.POLAR_BEARS_SPAWNABLE_ON_ALTERNATE)); 
/* 125 */     return checkAnimalSpawnRules(type, level, spawnReason, pos, random);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 130 */     super.readAdditionalSaveData(input);
/* 131 */     readPersistentAngerSaveData(level(), input);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 136 */     super.addAdditionalSaveData(output);
/* 137 */     addPersistentAngerSaveData(output);
/*     */   }
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 142 */     setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
/*     */   }
/*     */   
/*     */   public void setPersistentAngerEndTime(long endTime) {
/* 147 */     this.persistentAngerEndTime = endTime;
/*     */   }
/*     */   
/*     */   public long getPersistentAngerEndTime() {
/* 152 */     return this.persistentAngerEndTime;
/*     */   }
/*     */   
/*     */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) {
/* 157 */     this.persistentAngerTarget = persistentAngerTarget;
/*     */   }
/*     */   
/*     */   public EntityReference<LivingEntity> getPersistentAngerTarget() {
/* 162 */     return this.persistentAngerTarget;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 167 */     if (isBaby())
/* 168 */       return SoundEvents.POLAR_BEAR_AMBIENT_BABY; 
/* 170 */     return SoundEvents.POLAR_BEAR_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 175 */     return SoundEvents.POLAR_BEAR_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 180 */     return SoundEvents.POLAR_BEAR_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 185 */     playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playWarningSound() {
/* 189 */     if (this.warningSoundTicks <= 0) {
/* 190 */       makeSound(SoundEvents.POLAR_BEAR_WARNING);
/* 192 */       this.warningSoundTicks = 40;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 198 */     super.defineSynchedData(entityData);
/* 200 */     entityData.define(DATA_STANDING_ID, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public void tick() {
/* 205 */     super.tick();
/* 207 */     if (level().isClientSide()) {
/* 208 */       if (this.clientSideStandAnimation != this.clientSideStandAnimationO)
/* 209 */         refreshDimensions(); 
/* 211 */       this.clientSideStandAnimationO = this.clientSideStandAnimation;
/* 212 */       if (isStanding()) {
/* 213 */         this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation + 1.0F, 0.0F, 6.0F);
/*     */       } else {
/* 215 */         this.clientSideStandAnimation = Mth.clamp(this.clientSideStandAnimation - 1.0F, 0.0F, 6.0F);
/*     */       } 
/*     */     } 
/* 219 */     if (this.warningSoundTicks > 0)
/* 220 */       this.warningSoundTicks--; 
/* 223 */     if (!level().isClientSide())
/* 224 */       updatePersistentAnger((ServerLevel)level(), true); 
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 230 */     EntityDimensions dimension = isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/* 231 */     if (this.clientSideStandAnimation > 0.0F) {
/* 233 */       float standFactor = this.clientSideStandAnimation / 6.0F;
/* 234 */       float heightScaleFactor = 1.0F + standFactor;
/* 235 */       return dimension.scale(1.0F, heightScaleFactor);
/*     */     } 
/* 237 */     return dimension;
/*     */   }
/*     */   
/*     */   public boolean isStanding() {
/* 242 */     return ((Boolean)this.entityData.get(DATA_STANDING_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setStanding(boolean value) {
/* 246 */     this.entityData.set(DATA_STANDING_ID, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public float getStandingAnimationScale(float a) {
/* 250 */     return Mth.lerp(a, this.clientSideStandAnimationO, this.clientSideStandAnimation) / 6.0F;
/*     */   }
/*     */   
/*     */   protected float getWaterSlowDown() {
/* 255 */     return 0.98F;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 260 */     if (groupData == null)
/* 261 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(1.0F); 
/* 264 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */   
/*     */   private class PolarBearHurtByTargetGoal extends HurtByTargetGoal {
/*     */     public PolarBearHurtByTargetGoal() {
/* 271 */       super((PathfinderMob)PolarBear.this, new Class[0]);
/*     */     }
/*     */     
/*     */     public void start() {
/* 276 */       super.start();
/* 277 */       if (PolarBear.this.isBaby()) {
/* 278 */         alertOthers();
/* 279 */         stop();
/*     */       } 
/*     */     }
/*     */     
/*     */     protected void alertOther(Mob other, LivingEntity hurtByMob) {
/* 285 */       if (other instanceof PolarBear && 
/* 286 */         !other.isBaby())
/* 287 */         super.alertOther(other, hurtByMob); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class PolarBearAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
/*     */     public PolarBearAttackPlayersGoal() {
/* 297 */       super((Mob)PolarBear.this, Player.class, 20, true, true, null);
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 302 */       if (PolarBear.this.isBaby())
/* 303 */         return false; 
/* 307 */       if (super.canUse()) {
/* 308 */         List<PolarBear> bears = PolarBear.this.level().getEntitiesOfClass(PolarBear.class, PolarBear.this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
/* 309 */         for (PolarBear bear : bears) {
/* 310 */           if (bear.isBaby())
/* 311 */             return true; 
/*     */         } 
/*     */       } 
/* 316 */       return false;
/*     */     }
/*     */     
/*     */     protected double getFollowDistance() {
/* 321 */       return super.getFollowDistance() * 0.5D;
/*     */     }
/*     */   }
/*     */   
/*     */   private class PolarBearMeleeAttackGoal extends MeleeAttackGoal {
/*     */     public PolarBearMeleeAttackGoal() {
/* 327 */       super((PathfinderMob)PolarBear.this, 1.25D, true);
/*     */     }
/*     */     
/*     */     protected void checkAndPerformAttack(LivingEntity target) {
/* 332 */       if (canPerformAttack(target)) {
/* 333 */         resetAttackCooldown();
/* 334 */         this.mob.doHurtTarget(getServerLevel((Entity)this.mob), (Entity)target);
/* 335 */         PolarBear.this.setStanding(false);
/* 336 */       } else if (this.mob.distanceToSqr((Entity)target) < ((target.getBbWidth() + 3.0F) * (target.getBbWidth() + 3.0F))) {
/* 337 */         if (isTimeToAttack()) {
/* 338 */           PolarBear.this.setStanding(false);
/* 339 */           resetAttackCooldown();
/*     */         } 
/* 341 */         if (getTicksUntilNextAttack() <= 10) {
/* 342 */           PolarBear.this.setStanding(true);
/* 343 */           PolarBear.this.playWarningSound();
/*     */         } 
/*     */       } else {
/* 347 */         resetAttackCooldown();
/* 348 */         PolarBear.this.setStanding(false);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void stop() {
/* 354 */       PolarBear.this.setStanding(false);
/* 355 */       super.stop();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\polarbear\PolarBear.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */