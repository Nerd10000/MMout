/*     */ package net.minecraft.world.entity.animal.camel;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Camel extends AbstractHorse {
/*     */   public static final float BABY_SCALE = 0.6F;
/*     */   
/*     */   public static final int DASH_COOLDOWN_TICKS = 55;
/*     */   
/*     */   public static final int MAX_HEAD_Y_ROT = 30;
/*     */   
/*     */   private static final float RUNNING_SPEED_BONUS = 0.1F;
/*     */   
/*     */   private static final float DASH_VERTICAL_MOMENTUM = 1.4285F;
/*     */   
/*     */   private static final float DASH_HORIZONTAL_MOMENTUM = 22.2222F;
/*     */   
/*     */   private static final int DASH_MINIMUM_DURATION_TICKS = 5;
/*     */   
/*     */   private static final int SITDOWN_DURATION_TICKS = 40;
/*     */   
/*     */   private static final int STANDUP_DURATION_TICKS = 52;
/*     */   
/*     */   private static final int IDLE_MINIMAL_DURATION_TICKS = 80;
/*     */   
/*     */   private static final float SITTING_HEIGHT_DIFFERENCE = 1.43F;
/*     */   
/*     */   private static final long DEFAULT_LAST_POSE_CHANGE_TICK = 0L;
/*     */   
/*  84 */   private static final Brain.Provider<Camel> BRAIN_PROVIDER = Brain.provider(
/*  85 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FOOD_TEMPTATIONS, SensorType.NEAREST_ADULT), paramCamel -> CamelAi.getActivities());
/*     */   
/*  94 */   public static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(Camel.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  96 */   public static final EntityDataAccessor<Long> LAST_POSE_CHANGE_TICK = SynchedEntityData.defineId(Camel.class, EntityDataSerializers.LONG);
/*     */   
/*  98 */   public final AnimationState sitAnimationState = new AnimationState();
/*     */   
/*  99 */   public final AnimationState sitPoseAnimationState = new AnimationState();
/*     */   
/* 100 */   public final AnimationState sitUpAnimationState = new AnimationState();
/*     */   
/* 101 */   public final AnimationState idleAnimationState = new AnimationState();
/*     */   
/* 102 */   public final AnimationState dashAnimationState = new AnimationState();
/*     */   
/* 104 */   private static final EntityDimensions BABY_STANDING_DIMENSIONS = EntityDimensions.scalable(0.95F, 1.4F).withEyeHeight(1.38F);
/*     */   
/* 105 */   private static final EntityDimensions BABY_SITTING_DIMENSIONS = EntityDimensions.scalable(0.95F, 0.425F).withEyeHeight(0.41F);
/*     */   
/* 106 */   private static final EntityDimensions ADULT_SITTING_DIMENSIONS = EntityDimensions.scalable(EntityTypes.CAMEL.getWidth(), EntityTypes.CAMEL.getHeight() - 1.43F).withEyeHeight(0.845F);
/*     */   
/* 108 */   private int dashCooldown = 0;
/*     */   
/* 111 */   private int idleAnimationTimeout = 0;
/*     */   
/*     */   public Camel(EntityType<? extends Camel> type, Level level) {
/* 114 */     super(type, level);
/* 115 */     this.moveControl = new CamelMoveControl<>(this);
/* 116 */     this.lookControl = new CamelLookControl();
/* 117 */     GroundPathNavigation navigation = (GroundPathNavigation)getNavigation();
/* 118 */     navigation.setCanFloat(true);
/* 119 */     navigation.setCanWalkOverFences(true);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 124 */     super.addAdditionalSaveData(output);
/* 125 */     output.putLong("LastPoseTick", ((Long)this.entityData.get(LAST_POSE_CHANGE_TICK)).longValue());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 130 */     super.readAdditionalSaveData(input);
/* 131 */     long poseTick = input.getLongOr("LastPoseTick", 0L);
/* 132 */     if (poseTick < 0L)
/* 133 */       setPose(Pose.SITTING); 
/* 135 */     resetLastPoseChangeTick(poseTick);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 139 */     return createBaseHorseAttributes()
/* 140 */       .add(Attributes.MAX_HEALTH, 32.0D)
/* 141 */       .add(Attributes.MOVEMENT_SPEED, 0.09000000357627869D)
/* 142 */       .add(Attributes.JUMP_STRENGTH, 0.41999998688697815D)
/* 143 */       .add(Attributes.STEP_HEIGHT, 1.5D);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 148 */     super.defineSynchedData(entityData);
/* 149 */     entityData.define(DASH, Boolean.valueOf(false));
/* 150 */     entityData.define(LAST_POSE_CHANGE_TICK, Long.valueOf(0L));
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 155 */     CamelAi.initMemories(this, level.getRandom());
/* 156 */     resetLastPoseChangeTickToFullStand(level.getLevel().getGameTime());
/* 157 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public static boolean checkCamelSpawnRules(EntityType<Camel> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 161 */     return (level.getBlockState(pos.below()).is(BlockTags.CAMELS_SPAWNABLE_ON) && isBrightEnoughToSpawn((BlockAndLightGetter)level, pos));
/*     */   }
/*     */   
/*     */   protected Brain<Camel> makeBrain(Brain.Packed packedBrain) {
/* 166 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Camel> getBrain() {
/* 172 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void registerGoals() {}
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 183 */     return (pose == Pose.SITTING) ? (isBaby() ? BABY_SITTING_DIMENSIONS : ADULT_SITTING_DIMENSIONS) : (isBaby() ? BABY_STANDING_DIMENSIONS : super.getDefaultDimensions(pose));
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 188 */     ProfilerFiller profiler = Profiler.get();
/* 189 */     profiler.push("camelBrain");
/* 190 */     Brain<Camel> brain = getBrain();
/* 191 */     brain.tick(level, (LivingEntity)this);
/* 192 */     profiler.pop();
/* 194 */     profiler.push("camelActivityUpdate");
/* 195 */     CamelAi.updateActivity(this);
/* 196 */     profiler.pop();
/* 198 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 203 */     super.tick();
/* 204 */     if (isDashing() && this.dashCooldown < 50 && (onGround() || isInLiquid() || isPassenger()))
/* 205 */       setDashing(false); 
/* 207 */     if (this.dashCooldown > 0) {
/* 208 */       this.dashCooldown--;
/* 209 */       if (this.dashCooldown == 0)
/* 210 */         level().playSound(null, blockPosition(), getDashReadySound(), SoundSource.NEUTRAL, 1.0F, 1.0F); 
/*     */     } 
/* 213 */     if (level().isClientSide())
/* 214 */       setupAnimationStates(); 
/* 217 */     if (refuseToMove())
/* 218 */       clampHeadRotationToBody(); 
/* 221 */     if (isCamelSitting() && isInWater())
/* 222 */       standUpInstantly(); 
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 227 */     if (this.idleAnimationTimeout <= 0) {
/* 228 */       this.idleAnimationTimeout = this.random.nextInt(40) + 80;
/* 229 */       this.idleAnimationState.start(this.tickCount);
/*     */     } else {
/* 231 */       this.idleAnimationTimeout--;
/*     */     } 
/* 233 */     if (isCamelVisuallySitting()) {
/* 234 */       this.sitUpAnimationState.stop();
/* 235 */       this.dashAnimationState.stop();
/* 236 */       if (isVisuallySittingDown()) {
/* 237 */         this.sitAnimationState.startIfStopped(this.tickCount);
/* 238 */         this.sitPoseAnimationState.stop();
/*     */       } else {
/* 240 */         this.sitAnimationState.stop();
/* 241 */         this.sitPoseAnimationState.startIfStopped(this.tickCount);
/*     */       } 
/*     */     } else {
/* 244 */       this.sitAnimationState.stop();
/* 245 */       this.sitPoseAnimationState.stop();
/* 246 */       this.dashAnimationState.animateWhen(isDashing(), this.tickCount);
/* 247 */       this.sitUpAnimationState.animateWhen((isInPoseTransition() && getPoseTime() >= 0L), this.tickCount);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void updateWalkAnimation(float distance) {
/*     */     float targetSpeed;
/* 254 */     if (getPose() == Pose.STANDING && !this.dashAnimationState.isStarted()) {
/* 255 */       targetSpeed = Math.min(distance * 6.0F, 1.0F);
/*     */     } else {
/* 257 */       targetSpeed = 0.0F;
/*     */     } 
/* 259 */     this.walkAnimation.update(targetSpeed, 0.2F, isBaby() ? 3.0F : 1.0F);
/*     */   }
/*     */   
/*     */   public void travel(Vec3 input) {
/* 264 */     if (refuseToMove() && onGround()) {
/* 265 */       setDeltaMovement(getDeltaMovement().multiply(0.0D, 1.0D, 0.0D));
/* 266 */       input = input.multiply(0.0D, 1.0D, 0.0D);
/*     */     } 
/* 268 */     super.travel(input);
/*     */   }
/*     */   
/*     */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/* 273 */     super.tickRidden(controller, riddenInput);
/* 274 */     if (controller.zza > 0.0F && 
/* 275 */       isCamelSitting() && !isInPoseTransition())
/* 276 */       standUp(); 
/*     */   }
/*     */   
/*     */   public boolean refuseToMove() {
/* 282 */     return (isCamelSitting() || isInPoseTransition());
/*     */   }
/*     */   
/*     */   protected float getRiddenSpeed(Player controller) {
/* 287 */     float movementBonus = (controller.isSprinting() && getJumpCooldown() == 0) ? 0.1F : 0.0F;
/* 288 */     return (float)getAttributeValue(Attributes.MOVEMENT_SPEED) + movementBonus;
/*     */   }
/*     */   
/*     */   protected Vec2 getRiddenRotation(LivingEntity controller) {
/* 293 */     if (refuseToMove())
/* 294 */       return new Vec2(getXRot(), getYRot()); 
/* 296 */     return super.getRiddenRotation(controller);
/*     */   }
/*     */   
/*     */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/* 301 */     if (refuseToMove())
/* 302 */       return Vec3.ZERO; 
/* 304 */     return super.getRiddenInput(controller, selfInput);
/*     */   }
/*     */   
/*     */   public boolean canJump() {
/* 309 */     return (!refuseToMove() && super.canJump());
/*     */   }
/*     */   
/*     */   public void onPlayerJump(int jumpAmount) {
/* 314 */     if (!isSaddled() || this.dashCooldown > 0 || !onGround())
/*     */       return; 
/* 317 */     super.onPlayerJump(jumpAmount);
/*     */   }
/*     */   
/*     */   public boolean canSprint() {
/* 322 */     return true;
/*     */   }
/*     */   
/*     */   protected void executeRidersJump(float amount, Vec3 input) {
/* 327 */     double jumpMomentum = getJumpPower();
/* 329 */     addDeltaMovement(getLookAngle().multiply(1.0D, 0.0D, 1.0D).normalize()
/* 330 */         .scale((22.2222F * amount) * getAttributeValue(Attributes.MOVEMENT_SPEED) * getBlockSpeedFactor())
/* 331 */         .add(0.0D, (1.4285F * amount) * jumpMomentum, 0.0D));
/* 334 */     this.dashCooldown = 55;
/* 335 */     setDashing(true);
/* 336 */     this.needsSync = true;
/*     */   }
/*     */   
/*     */   public boolean isDashing() {
/* 340 */     return ((Boolean)this.entityData.get(DASH)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDashing(boolean isDashing) {
/* 344 */     this.entityData.set(DASH, Boolean.valueOf(isDashing));
/*     */   }
/*     */   
/*     */   public void handleStartJump(int jumpScale) {
/* 349 */     makeSound(getDashingSound());
/* 350 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 351 */     setDashing(true);
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashingSound() {
/* 355 */     return SoundEvents.CAMEL_DASH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashReadySound() {
/* 359 */     return SoundEvents.CAMEL_DASH_READY;
/*     */   }
/*     */   
/*     */   public void handleStopJump() {}
/*     */   
/*     */   public int getJumpCooldown() {
/* 368 */     return this.dashCooldown;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 373 */     return SoundEvents.CAMEL_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 378 */     return SoundEvents.CAMEL_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 383 */     return SoundEvents.CAMEL_HURT;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 388 */     if (blockState.is(BlockTags.CAMEL_SAND_STEP_SOUND_BLOCKS)) {
/* 389 */       playSound(SoundEvents.CAMEL_STEP_SAND, 1.0F, 1.0F);
/*     */     } else {
/* 391 */       playSound(SoundEvents.CAMEL_STEP, 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 397 */     return itemStack.is(ItemTags.CAMEL_FOOD);
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 402 */     ItemStack itemStack = player.getItemInHand(hand);
/* 404 */     if (player.isSecondaryUseActive() && !isBaby()) {
/* 405 */       openCustomInventoryScreen(player);
/* 406 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 410 */     InteractionResult interactionResult = itemStack.interactLivingEntity(player, (LivingEntity)this, hand);
/* 411 */     if (interactionResult.consumesAction())
/* 412 */       return interactionResult; 
/* 415 */     if (isFood(itemStack))
/* 416 */       return fedFood(player, itemStack); 
/* 419 */     if (getPassengers().size() < 2 && !isBaby())
/* 420 */       doPlayerRide(player); 
/* 423 */     if (isBaby() && player.isHolding(Items.GOLDEN_DANDELION))
/* 424 */       return super.mobInteract(player, hand); 
/* 426 */     return (InteractionResult)InteractionResult.CONSUME;
/*     */   }
/*     */   
/*     */   public void onElasticLeashPull() {
/* 431 */     super.onElasticLeashPull();
/* 432 */     if (isCamelSitting() && !isInPoseTransition() && canCamelChangePose())
/* 433 */       standUp(); 
/*     */   }
/*     */   
/*     */   public Vec3[] getQuadLeashOffsets() {
/* 439 */     return Leashable.createQuadLeashOffsets((Entity)this, 0.02D, 0.48D, 0.25D, 0.82D);
/*     */   }
/*     */   
/*     */   public boolean canCamelChangePose() {
/* 443 */     return wouldNotSuffocateAtTargetPose(isCamelSitting() ? Pose.STANDING : Pose.SITTING);
/*     */   }
/*     */   
/*     */   protected boolean handleEating(Player player, ItemStack itemStack) {
/* 448 */     if (!isFood(itemStack))
/* 449 */       return false; 
/* 452 */     boolean couldHeal = (getHealth() < getMaxHealth());
/* 453 */     if (couldHeal)
/* 454 */       heal(2.0F); 
/* 457 */     boolean couldSetInLove = (isTamed() && getAge() == 0 && canFallInLove());
/* 458 */     if (couldSetInLove)
/* 459 */       setInLove(player); 
/* 462 */     boolean couldAgeUp = canAgeUp();
/* 463 */     if (couldAgeUp) {
/* 464 */       level().addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/* 465 */       if (!level().isClientSide())
/* 466 */         ageUp(10); 
/*     */     } 
/* 470 */     if (couldHeal || couldSetInLove || couldAgeUp) {
/* 471 */       if (!isSilent()) {
/* 472 */         SoundEvent eatingSound = getEatingSound();
/* 473 */         if (eatingSound != null)
/* 474 */           level().playSound(null, getX(), getY(), getZ(), eatingSound, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F); 
/*     */       } 
/* 477 */       gameEvent((Holder)GameEvent.EAT);
/* 478 */       return true;
/*     */     } 
/* 480 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean canPerformRearing() {
/* 485 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canMate(Animal partner) {
/* 490 */     if (partner != this && partner instanceof Camel) {
/* 490 */       Camel camel = (Camel)partner;
/* 490 */       if (canParent() && camel.canParent());
/*     */     } 
/* 490 */     return false;
/*     */   }
/*     */   
/*     */   public Camel getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 495 */     return (Camel)EntityTypes.CAMEL.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   protected SoundEvent getEatingSound() {
/* 500 */     return SoundEvents.CAMEL_EAT;
/*     */   }
/*     */   
/*     */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/* 505 */     standUpInstantly();
/* 506 */     super.actuallyHurt(level, source, dmg);
/*     */   }
/*     */   
/*     */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 511 */     int index = Math.max(getPassengers().indexOf(passenger), 0);
/* 512 */     boolean driver = (index == 0);
/* 513 */     float offset = 0.5F;
/* 514 */     float height = (float)(isRemoved() ? 0.009999999776482582D : getBodyAnchorAnimationYOffset(driver, 0.0F, dimensions, scale));
/* 516 */     if (getPassengers().size() > 1) {
/* 517 */       if (!driver)
/* 518 */         offset = -0.7F; 
/* 521 */       if (passenger instanceof Animal)
/* 522 */         offset += 0.2F; 
/*     */     } 
/* 526 */     return (new Vec3(0.0D, height, (offset * scale))).yRot(-getYRot() * 0.017453292F);
/*     */   }
/*     */   
/*     */   public float getAgeScale() {
/* 531 */     return isBaby() ? 0.6F : 1.0F;
/*     */   }
/*     */   
/*     */   private double getBodyAnchorAnimationYOffset(boolean isFront, float partialTicks, EntityDimensions dimensions, float scale) {
/* 535 */     double ageSitYOffset = isBaby() ? 0.09375D : 0.375D;
/* 536 */     double baseSitOffset = dimensions.height() - ageSitYOffset;
/* 537 */     float sittingHeightDifference = scale * 1.43F;
/* 538 */     float verticalDrop = sittingHeightDifference - scale * 0.2F;
/* 539 */     float bottomPoint = sittingHeightDifference - verticalDrop;
/* 540 */     boolean isInTransition = isInPoseTransition();
/* 541 */     boolean isSitting = isCamelSitting();
/* 542 */     if (isInTransition) {
/*     */       int halfPoint;
/*     */       float flexPointOffset;
/* 544 */       int animationDuration = isSitting ? 40 : 52;
/* 549 */       if (isSitting) {
/* 550 */         halfPoint = 28;
/* 551 */         flexPointOffset = isFront ? 0.5F : 0.1F;
/*     */       } else {
/* 553 */         halfPoint = isFront ? 24 : 32;
/* 554 */         flexPointOffset = isFront ? 0.6F : 0.35F;
/*     */       } 
/* 556 */       float poseTime = Mth.clamp((float)getPoseTime() + partialTicks, 0.0F, animationDuration);
/* 557 */       boolean isFirstPart = (poseTime < halfPoint);
/* 558 */       float part = isFirstPart ? (poseTime / halfPoint) : ((poseTime - halfPoint) / (animationDuration - halfPoint));
/* 559 */       float flexPoint = sittingHeightDifference - flexPointOffset * verticalDrop;
/* 560 */       baseSitOffset += isSitting ? 
/* 561 */         Mth.lerp(part, isFirstPart ? sittingHeightDifference : flexPoint, isFirstPart ? flexPoint : bottomPoint) : 
/* 562 */         Mth.lerp(part, isFirstPart ? (bottomPoint - sittingHeightDifference) : (bottomPoint - flexPoint), isFirstPart ? (bottomPoint - flexPoint) : 0.0F);
/*     */     } 
/* 564 */     if (isSitting && !isInTransition)
/* 565 */       baseSitOffset += bottomPoint; 
/* 567 */     return baseSitOffset;
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset(float partialTicks) {
/* 572 */     EntityDimensions dimensions = getDimensions(getPose());
/* 573 */     float scale = getAgeScale();
/* 574 */     return new Vec3(0.0D, getBodyAnchorAnimationYOffset(true, partialTicks, dimensions, scale) - (0.2F * scale), (dimensions.width() * 0.56F));
/*     */   }
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 579 */     return 30;
/*     */   }
/*     */   
/*     */   protected boolean canAddPassenger(Entity passenger) {
/* 584 */     return (getPassengers().size() <= 2);
/*     */   }
/*     */   
/*     */   public boolean isCamelSitting() {
/* 588 */     return (((Long)this.entityData.get(LAST_POSE_CHANGE_TICK)).longValue() < 0L);
/*     */   }
/*     */   
/*     */   public boolean isCamelVisuallySitting() {
/* 593 */     return (((getPoseTime() < 0L)) != isCamelSitting());
/*     */   }
/*     */   
/*     */   public boolean isInPoseTransition() {
/* 597 */     long poseTime = getPoseTime();
/* 598 */     return (poseTime < (isCamelSitting() ? 40L : 52L));
/*     */   }
/*     */   
/*     */   private boolean isVisuallySittingDown() {
/* 602 */     return (isCamelSitting() && getPoseTime() < 40L && getPoseTime() >= 0L);
/*     */   }
/*     */   
/*     */   public void sitDown() {
/* 606 */     if (isCamelSitting())
/*     */       return; 
/* 609 */     makeSound(getSitDownSound());
/* 610 */     setPose(Pose.SITTING);
/* 611 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 612 */     resetLastPoseChangeTick(-level().getGameTime());
/*     */   }
/*     */   
/*     */   public void standUp() {
/* 616 */     if (!isCamelSitting())
/*     */       return; 
/* 619 */     makeSound(getStandUpSound());
/* 620 */     setPose(Pose.STANDING);
/* 621 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 622 */     resetLastPoseChangeTick(level().getGameTime());
/*     */   }
/*     */   
/*     */   protected SoundEvent getStandUpSound() {
/* 626 */     return SoundEvents.CAMEL_STAND;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSitDownSound() {
/* 630 */     return SoundEvents.CAMEL_SIT;
/*     */   }
/*     */   
/*     */   public void standUpInstantly() {
/* 634 */     setPose(Pose.STANDING);
/* 635 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 636 */     resetLastPoseChangeTickToFullStand(level().getGameTime());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void resetLastPoseChangeTick(long syncedPoseTickTime) {
/* 641 */     this.entityData.set(LAST_POSE_CHANGE_TICK, Long.valueOf(syncedPoseTickTime));
/*     */   }
/*     */   
/*     */   private void resetLastPoseChangeTickToFullStand(long currentTime) {
/* 647 */     resetLastPoseChangeTick(Math.max(0L, currentTime - 52L - 1L));
/*     */   }
/*     */   
/*     */   public long getPoseTime() {
/* 651 */     return level().getGameTime() - Math.abs(((Long)this.entityData.get(LAST_POSE_CHANGE_TICK)).longValue());
/*     */   }
/*     */   
/*     */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/* 656 */     if (slot == EquipmentSlot.SADDLE)
/* 657 */       return (Holder<SoundEvent>)getSaddleSound(); 
/* 659 */     return super.getEquipSound(slot, stack, equippable);
/*     */   }
/*     */   
/*     */   protected Holder.Reference<SoundEvent> getSaddleSound() {
/* 663 */     return SoundEvents.CAMEL_SADDLE;
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 668 */     if (!this.firstTick && 
/* 669 */       DASH.equals(accessor))
/* 670 */       this.dashCooldown = (this.dashCooldown == 0) ? 55 : this.dashCooldown; 
/* 673 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   public boolean isTamed() {
/* 678 */     return true;
/*     */   }
/*     */   
/*     */   public void openCustomInventoryScreen(Player player) {
/* 683 */     if (!level().isClientSide())
/* 684 */       player.openHorseInventory(this, (Container)this.inventory); 
/*     */   }
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/* 690 */     return new CamelBodyRotationControl(this);
/*     */   }
/*     */   
/*     */   private class CamelBodyRotationControl extends BodyRotationControl {
/*     */     public CamelBodyRotationControl(Camel camel) {
/* 695 */       super((Mob)camel);
/*     */     }
/*     */     
/*     */     public void clientTick() {
/* 700 */       if (!Camel.this.refuseToMove())
/* 701 */         super.clientTick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CamelLookControl extends LookControl {
/*     */     private CamelLookControl() {
/* 708 */       super((Mob)Camel.this);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 713 */       if (!Camel.this.hasControllingPassenger())
/* 714 */         super.tick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CamelMoveControl<T extends Camel> extends MoveControl<T> {
/*     */     public CamelMoveControl(T camel) {
/* 721 */       super((Mob)camel);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 726 */       if (this.operation == MoveControl.Operation.MOVE_TO && !((Camel)this.mob).isLeashed() && ((Camel)this.mob).isCamelSitting() && !((Camel)this.mob).isInPoseTransition() && ((Camel)this.mob).canCamelChangePose())
/* 727 */         ((Camel)this.mob).standUp(); 
/* 729 */       super.tick();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\camel\Camel.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */