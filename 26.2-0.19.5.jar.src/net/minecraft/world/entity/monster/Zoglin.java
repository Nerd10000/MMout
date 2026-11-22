/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
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
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.hoglin.HoglinBase;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Zoglin extends Monster implements HoglinBase {
/*  66 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Zoglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  67 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.75F, 0.85F)
/*  68 */     .withEyeHeight(0.625F)
/*  69 */     .withAttachments(EntityAttachments.builder()
/*  70 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.875F, 0.0F));
/*     */   
/*     */   private static final int MAX_HEALTH = 40;
/*     */   
/*     */   private static final int ATTACK_KNOCKBACK = 1;
/*     */   
/*     */   private static final float KNOCKBACK_RESISTANCE = 0.6F;
/*     */   
/*     */   private static final int ATTACK_DAMAGE = 6;
/*     */   
/*     */   private static final float BABY_ATTACK_DAMAGE = 0.5F;
/*     */   
/*     */   private static final int ATTACK_INTERVAL = 40;
/*     */   
/*     */   private static final int BABY_ATTACK_INTERVAL = 15;
/*     */   
/*     */   private static final int ATTACK_DURATION = 200;
/*     */   
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.3F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.4F;
/*     */   
/*     */   private static final boolean DEFAULT_BABY = false;
/*     */   
/*     */   private int attackAnimationRemainingTicks;
/*     */   
/*  88 */   private static final Brain.Provider<Zoglin> BRAIN_PROVIDER = Brain.provider(
/*  89 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS), paramZoglin -> getActivities());
/*     */   
/*     */   public Zoglin(EntityType<? extends Zoglin> type, Level level) {
/*  97 */     super((EntityType)type, level);
/*  98 */     this.xpReward = 5;
/*     */   }
/*     */   
/*     */   protected Brain<Zoglin> makeBrain(Brain.Packed packedBrain) {
/* 103 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   protected static List<ActivityData<Zoglin>> getActivities() {
/* 107 */     return List.of(
/* 108 */         initCoreActivity(), 
/* 109 */         initIdleActivity(), 
/* 110 */         initFightActivity());
/*     */   }
/*     */   
/*     */   private static ActivityData<Zoglin> initCoreActivity() {
/* 115 */     return ActivityData.create(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
/*     */   }
/*     */   
/*     */   private static ActivityData<Zoglin> initIdleActivity() {
/* 122 */     return ActivityData.create(Activity.IDLE, 10, ImmutableList.of(
/* 123 */           StartAttacking.create(Zoglin::findNearestValidAttackTarget), 
/* 124 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), new RunOne(
/* 125 */             (List)ImmutableList.of(
/* 126 */               Pair.of(RandomStroll.stroll(0.4F), Integer.valueOf(2)), 
/* 127 */               Pair.of(SetWalkTargetFromLookTarget.create(0.4F, 3), Integer.valueOf(2)), 
/* 128 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))));
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 135 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   private static ActivityData<Zoglin> initFightActivity() {
/* 139 */     return ActivityData.create(Activity.FIGHT, 10, ImmutableList.of(
/* 140 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/* 141 */           BehaviorBuilder.triggerIf(Zoglin::isAdult, MeleeAttack.create(40)), 
/* 142 */           BehaviorBuilder.triggerIf(Zoglin::isBaby, MeleeAttack.create(15)), 
/* 143 */           StopAttackingIfTargetInvalid.create()), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel level, Mob mob) {
/* 148 */     return ((NearestVisibleLivingEntities)mob.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty()))
/* 149 */       .findClosest(target -> (!target.is(EntityTypes.ZOGLIN) && !target.is(EntityTypes.CREEPER) && Sensor.isEntityAttackable(level, (LivingEntity)mob, target)));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 154 */     super.defineSynchedData(entityData);
/* 155 */     entityData.define(DATA_BABY_ID, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 160 */     super.onSyncedDataUpdated(accessor);
/* 161 */     if (DATA_BABY_ID.equals(accessor))
/* 162 */       refreshDimensions(); 
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 168 */     if (level.getRandom().nextFloat() < 0.2F)
/* 169 */       setBaby(true); 
/* 172 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 176 */     return Monster.createMonsterAttributes()
/* 177 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 178 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 179 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.6000000238418579D)
/* 180 */       .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
/* 181 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */   
/*     */   public boolean isAdult() {
/* 185 */     return !isBaby();
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*     */     LivingEntity entity;
/* 190 */     if (target instanceof LivingEntity) {
/* 190 */       entity = (LivingEntity)target;
/*     */     } else {
/* 191 */       return false;
/*     */     } 
/* 193 */     this.attackAnimationRemainingTicks = 10;
/* 194 */     level.broadcastEntityEvent((Entity)this, (byte)4);
/* 196 */     makeSound(SoundEvents.ZOGLIN_ATTACK);
/* 197 */     return HoglinBase.hurtAndThrowTarget(level, (LivingEntity)this, entity);
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/* 202 */     return true;
/*     */   }
/*     */   
/*     */   protected void blockedByItem(LivingEntity defender, DamageSource source, float damage) {
/* 207 */     if (!isBaby())
/* 208 */       HoglinBase.throwTarget((LivingEntity)this, defender); 
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*     */     LivingEntity attacker;
/* 214 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 215 */     if (wasHurt) {
/* 215 */       Entity entity = source.getEntity();
/* 215 */       if (entity instanceof LivingEntity) {
/* 215 */         attacker = (LivingEntity)entity;
/*     */       } else {
/* 216 */         return wasHurt;
/*     */       } 
/*     */     } else {
/* 216 */       return wasHurt;
/*     */     } 
/* 218 */     if (canAttack(attacker) && !BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget((LivingEntity)this, attacker, 4.0D))
/* 219 */       setAttackTarget(attacker); 
/* 221 */     return true;
/*     */   }
/*     */   
/*     */   private void setAttackTarget(LivingEntity target) {
/* 225 */     this.brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 226 */     this.brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, target, 200L);
/*     */   }
/*     */   
/*     */   public Brain<Zoglin> getBrain() {
/* 232 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void updateActivity() {
/* 236 */     Activity oldActivity = this.brain.getActiveNonCoreActivity().orElse(null);
/* 239 */     this.brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/* 241 */     Activity newActivity = this.brain.getActiveNonCoreActivity().orElse(null);
/* 242 */     if (newActivity == Activity.FIGHT && oldActivity != Activity.FIGHT)
/* 244 */       playAngrySound(); 
/* 248 */     setAggressive(this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 253 */     ProfilerFiller profiler = Profiler.get();
/* 254 */     profiler.push("zoglinBrain");
/* 255 */     getBrain().tick(level, (LivingEntity)this);
/* 256 */     profiler.pop();
/* 258 */     updateActivity();
/*     */   }
/*     */   
/*     */   public void setBaby(boolean baby) {
/* 263 */     getEntityData().set(DATA_BABY_ID, Boolean.valueOf(baby));
/* 264 */     if (!level().isClientSide() && baby)
/* 265 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D); 
/*     */   }
/*     */   
/*     */   public boolean isBaby() {
/* 271 */     return ((Boolean)getEntityData().get(DATA_BABY_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 277 */     if (this.attackAnimationRemainingTicks > 0)
/* 278 */       this.attackAnimationRemainingTicks--; 
/* 280 */     super.aiStep();
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 286 */     if (id == 4) {
/* 288 */       this.attackAnimationRemainingTicks = 10;
/* 289 */       makeSound(SoundEvents.ZOGLIN_ATTACK);
/*     */     } else {
/* 291 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getAttackAnimationRemainingTicks() {
/* 297 */     return this.attackAnimationRemainingTicks;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 302 */     if (level().isClientSide())
/* 303 */       return null; 
/* 305 */     if (this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET))
/* 306 */       return SoundEvents.ZOGLIN_ANGRY; 
/* 308 */     return SoundEvents.ZOGLIN_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 313 */     return SoundEvents.ZOGLIN_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 318 */     return SoundEvents.ZOGLIN_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 323 */     playSound(SoundEvents.ZOGLIN_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playAngrySound() {
/* 327 */     makeSound(SoundEvents.ZOGLIN_ANGRY);
/*     */   }
/*     */   
/*     */   public LivingEntity getTarget() {
/* 332 */     return getTargetFromBrain();
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 337 */     super.addAdditionalSaveData(output);
/* 339 */     output.putBoolean("IsBaby", isBaby());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 344 */     super.readAdditionalSaveData(input);
/* 346 */     setBaby(input.getBooleanOr("IsBaby", false));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Zoglin.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */