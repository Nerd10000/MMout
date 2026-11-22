/*     */ package net.minecraft.world.entity.monster.hoglin;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.Zoglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Hoglin extends Animal implements Enemy, HoglinBase {
/*  61 */   private static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(Hoglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  62 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.75F, 0.85F)
/*  63 */     .withEyeHeight(0.625F)
/*  64 */     .withAttachments(EntityAttachments.builder()
/*  65 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.875F, 0.0F));
/*     */   
/*     */   private static final int MAX_HEALTH = 40;
/*     */   
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.3F;
/*     */   
/*     */   private static final int ATTACK_KNOCKBACK = 1;
/*     */   
/*     */   private static final float KNOCKBACK_RESISTANCE = 0.6F;
/*     */   
/*     */   private static final int ATTACK_DAMAGE = 6;
/*     */   
/*     */   private static final float BABY_ATTACK_DAMAGE = 0.5F;
/*     */   
/*     */   private static final boolean DEFAULT_IMMUNE_TO_ZOMBIFICATION = false;
/*     */   
/*     */   private static final int DEFAULT_TIME_IN_OVERWORLD = 0;
/*     */   
/*     */   private static final boolean DEFAULT_CANNOT_BE_HUNTED = false;
/*     */   
/*     */   public static final int CONVERSION_TIME = 300;
/*     */   
/*     */   private int attackAnimationRemainingTicks;
/*     */   
/*  78 */   private int timeInOverworld = 0;
/*     */   
/*     */   private boolean cannotBeHunted = false;
/*     */   
/*  81 */   private static final Brain.Provider<Hoglin> BRAIN_PROVIDER = Brain.provider(
/*  82 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ADULT, SensorType.HOGLIN_SPECIFIC_SENSOR), paramHoglin -> HoglinAi.getActivities());
/*     */   
/*     */   public Hoglin(EntityType<? extends Hoglin> type, Level level) {
/*  92 */     super(type, level);
/*  93 */     this.xpReward = 5;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setTimeInOverworld(int timeInOverworld) {
/*  98 */     this.timeInOverworld = timeInOverworld;
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/* 103 */     return true;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 107 */     return Monster.createMonsterAttributes()
/* 108 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 109 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 110 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.6000000238418579D)
/* 111 */       .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
/* 112 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*     */     LivingEntity livingEntity;
/* 117 */     if (target instanceof LivingEntity) {
/* 117 */       livingEntity = (LivingEntity)target;
/*     */     } else {
/* 118 */       return false;
/*     */     } 
/* 120 */     this.attackAnimationRemainingTicks = 10;
/* 121 */     level().broadcastEntityEvent((Entity)this, (byte)4);
/* 123 */     makeSound(SoundEvents.HOGLIN_ATTACK);
/* 124 */     HoglinAi.onHitTarget(this, livingEntity);
/* 125 */     return HoglinBase.hurtAndThrowTarget(level, (LivingEntity)this, livingEntity);
/*     */   }
/*     */   
/*     */   protected void blockedByItem(LivingEntity defender, DamageSource source, float damage) {
/* 130 */     if (isAdult())
/* 131 */       HoglinBase.throwTarget((LivingEntity)this, defender); 
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 137 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 138 */     if (wasHurt) {
/* 138 */       Entity entity = source.getEntity();
/* 138 */       if (entity instanceof LivingEntity) {
/* 138 */         LivingEntity sourceEntity = (LivingEntity)entity;
/* 139 */         HoglinAi.wasHurtBy(level, this, sourceEntity);
/*     */       } 
/*     */     } 
/* 141 */     return wasHurt;
/*     */   }
/*     */   
/*     */   protected Brain<Hoglin> makeBrain(Brain.Packed packedBrain) {
/* 146 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Hoglin> getBrain() {
/* 152 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 157 */     ProfilerFiller profiler = Profiler.get();
/* 158 */     profiler.push("hoglinBrain");
/* 159 */     getBrain().tick(level, (LivingEntity)this);
/* 160 */     profiler.pop();
/* 162 */     HoglinAi.updateActivity(this);
/* 164 */     if (isConverting()) {
/* 165 */       this.timeInOverworld++;
/* 166 */       if (this.timeInOverworld > 300) {
/* 167 */         makeSound(SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED);
/* 168 */         finishConversion();
/*     */       } 
/*     */     } else {
/* 171 */       this.timeInOverworld = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 178 */     if (this.attackAnimationRemainingTicks > 0)
/* 179 */       this.attackAnimationRemainingTicks--; 
/* 181 */     super.aiStep();
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 186 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 191 */     if (isBaby()) {
/* 192 */       this.xpReward = 3;
/* 193 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
/*     */     } else {
/* 195 */       this.xpReward = 5;
/* 196 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean checkHoglinSpawnRules(EntityType<Hoglin> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 201 */     return !level.getBlockState(pos.below()).is(Blocks.NETHER_WART_BLOCK);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 206 */     if (level.getRandom().nextFloat() < 0.2F)
/* 207 */       setBaby(true); 
/* 210 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 215 */     return true;
/*     */   }
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 220 */     if (HoglinAi.isPosNearNearestRepellent(this, pos))
/* 221 */       return -1.0F; 
/* 223 */     if (level.getBlockState(pos.below()).is(Blocks.CRIMSON_NYLIUM))
/* 225 */       return 10.0F; 
/* 227 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 232 */     InteractionResult interactionSucceeded = super.mobInteract(player, hand);
/* 233 */     if (interactionSucceeded.consumesAction())
/* 234 */       setPersistenceRequired(); 
/* 236 */     return interactionSucceeded;
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 242 */     if (id == 4) {
/* 244 */       this.attackAnimationRemainingTicks = 10;
/* 245 */       makeSound(SoundEvents.HOGLIN_ATTACK);
/*     */     } else {
/* 247 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getAttackAnimationRemainingTicks() {
/* 253 */     return this.attackAnimationRemainingTicks;
/*     */   }
/*     */   
/*     */   public boolean shouldDropExperience() {
/* 258 */     return true;
/*     */   }
/*     */   
/*     */   protected int getBaseExperienceReward(ServerLevel level) {
/* 263 */     return this.xpReward;
/*     */   }
/*     */   
/*     */   private void finishConversion() {
/* 267 */     convertTo(EntityTypes.ZOGLIN, ConversionParams.single((Mob)this, true, false), zoglin -> zoglin.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0)));
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 274 */     return itemStack.is(ItemTags.HOGLIN_FOOD);
/*     */   }
/*     */   
/*     */   public boolean isAdult() {
/* 278 */     return !isBaby();
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 283 */     super.defineSynchedData(entityData);
/* 284 */     entityData.define(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 289 */     super.addAdditionalSaveData(output);
/* 290 */     output.putBoolean("IsImmuneToZombification", isImmuneToZombification());
/* 291 */     output.putInt("TimeInOverworld", this.timeInOverworld);
/* 292 */     output.putBoolean("CannotBeHunted", this.cannotBeHunted);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 297 */     super.readAdditionalSaveData(input);
/* 298 */     setImmuneToZombification(input.getBooleanOr("IsImmuneToZombification", false));
/* 299 */     this.timeInOverworld = input.getIntOr("TimeInOverworld", 0);
/* 300 */     setCannotBeHunted(input.getBooleanOr("CannotBeHunted", false));
/*     */   }
/*     */   
/*     */   public void setImmuneToZombification(boolean isImmuneToZombification) {
/* 304 */     getEntityData().set(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(isImmuneToZombification));
/*     */   }
/*     */   
/*     */   private boolean isImmuneToZombification() {
/* 308 */     return ((Boolean)getEntityData().get(DATA_IMMUNE_TO_ZOMBIFICATION)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isConverting() {
/* 312 */     return (!isImmuneToZombification() && !isNoAi() && ((Boolean)level().environmentAttributes().getValue(EnvironmentAttributes.PIGLINS_ZOMBIFY, position())).booleanValue());
/*     */   }
/*     */   
/*     */   private void setCannotBeHunted(boolean cannotBeHunted) {
/* 316 */     this.cannotBeHunted = cannotBeHunted;
/*     */   }
/*     */   
/*     */   public boolean canBeHunted() {
/* 320 */     return (isAdult() && !this.cannotBeHunted);
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 325 */     Hoglin offspring = (Hoglin)EntityTypes.HOGLIN.create((Level)level, EntitySpawnReason.BREEDING);
/* 326 */     if (offspring != null)
/* 327 */       offspring.setPersistenceRequired(); 
/* 329 */     return (AgeableMob)offspring;
/*     */   }
/*     */   
/*     */   public boolean canFallInLove() {
/* 334 */     return (!HoglinAi.isPacified(this) && super.canFallInLove());
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 339 */     return SoundSource.HOSTILE;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 344 */     if (level().isClientSide())
/* 345 */       return null; 
/* 347 */     return HoglinAi.getSoundForCurrentActivity(this).orElse(null);
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 352 */     return SoundEvents.HOGLIN_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 357 */     return SoundEvents.HOGLIN_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 362 */     return SoundEvents.HOSTILE_SWIM;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSplashSound() {
/* 367 */     return SoundEvents.HOSTILE_SPLASH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 372 */     playSound(SoundEvents.HOGLIN_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public LivingEntity getTarget() {
/* 377 */     return getTargetFromBrain();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\hoglin\Hoglin.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */