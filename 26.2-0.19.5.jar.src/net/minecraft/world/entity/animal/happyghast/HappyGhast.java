/*     */ package net.minecraft.world.entity.animal.happyghast;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
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
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.Ghast;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class HappyGhast extends Animal {
/*     */   public static final float BABY_SCALE = 0.2375F;
/*     */   
/*     */   public static final int WANDER_GROUND_DISTANCE = 16;
/*     */   
/*     */   public static final int SMALL_RESTRICTION_RADIUS = 32;
/*     */   
/*     */   public static final int LARGE_RESTRICTION_RADIUS = 64;
/*     */   
/*     */   public static final int RESTRICTION_RADIUS_BUFFER = 16;
/*     */   
/*     */   public static final int FAST_HEALING_TICKS = 20;
/*     */   
/*     */   public static final int SLOW_HEALING_TICKS = 600;
/*     */   
/*     */   public static final int MAX_PASSANGERS = 4;
/*     */   
/*     */   private static final int STILL_TIMEOUT_ON_LOAD_GRACE_PERIOD = 60;
/*     */   
/*     */   private static final int MAX_STILL_TIMEOUT = 10;
/*     */   
/*  70 */   private static final Brain.Provider<HappyGhast> BRAIN_PROVIDER = Brain.provider(
/*  71 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FOOD_TEMPTATIONS, SensorType.NEAREST_ADULT_ANY_TYPE, SensorType.NEAREST_PLAYERS), paramHappyGhast -> HappyGhastAi.getActivities());
/*     */   
/*     */   public static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*     */   
/*  83 */   private int leashHolderTime = 0;
/*     */   
/*     */   private int serverStillTimeout;
/*     */   
/*  86 */   private static final EntityDataAccessor<Boolean> IS_LEASH_HOLDER = SynchedEntityData.defineId(HappyGhast.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  87 */   private static final EntityDataAccessor<Boolean> STAYS_STILL = SynchedEntityData.defineId(HappyGhast.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float MAX_SCALE = 1.0F;
/*     */   
/*  92 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.HAPPY_GHAST.getDimensions()
/*  93 */     .scale(0.2375F)
/*  94 */     .withEyeHeight(0.46875F);
/*     */   
/*     */   public HappyGhast(EntityType<? extends HappyGhast> type, Level level) {
/*  97 */     super(type, level);
/*  98 */     this.moveControl = (MoveControl)new Ghast.GhastMoveControl((Mob)this, true, this::isOnStillTimeout);
/*  99 */     this.lookControl = new HappyGhastLookControl();
/*     */   }
/*     */   
/*     */   private void setServerStillTimeout(int serverStillTimeout) {
/* 103 */     if (this.serverStillTimeout <= 0 && serverStillTimeout > 0) {
/* 103 */       Level level = level();
/* 103 */       if (level instanceof ServerLevel) {
/* 103 */         ServerLevel serverLevel = (ServerLevel)level;
/* 105 */         syncPacketPositionCodec(getX(), getY(), getZ());
/* 106 */         (serverLevel.getChunkSource()).chunkMap.sendToTrackingPlayers((Entity)this, (Packet)ClientboundEntityPositionSyncPacket.of((Entity)this));
/*     */       } 
/*     */     } 
/* 108 */     this.serverStillTimeout = serverStillTimeout;
/* 109 */     syncStayStillFlag();
/*     */   }
/*     */   
/*     */   private PathNavigation createBabyNavigation(Level level) {
/* 113 */     return (PathNavigation)new BabyFlyingPathNavigation(this, level);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 118 */     this.goalSelector.addGoal(3, (Goal)new HappyGhastFloatGoal());
/* 119 */     this.goalSelector.addGoal(4, (Goal)new TemptGoal.ForNonPathfinders((Mob)this, 1.0D, itemStack -> (isWearingBodyArmor() || isBaby()) ? itemStack.is(ItemTags.HAPPY_GHAST_FOOD) : itemStack.is(ItemTags.HAPPY_GHAST_TEMPT_ITEMS), false, 7.0D));
/* 120 */     this.goalSelector.addGoal(5, (Goal)new Ghast.RandomFloatAroundGoal((Mob)this, 16));
/*     */   }
/*     */   
/*     */   private void adultGhastSetup() {
/* 124 */     this.moveControl = (MoveControl)new Ghast.GhastMoveControl((Mob)this, true, this::isOnStillTimeout);
/* 125 */     this.lookControl = new HappyGhastLookControl();
/* 126 */     this.navigation = createNavigation(level());
/* 127 */     Level level = level();
/* 127 */     if (level instanceof ServerLevel) {
/* 127 */       ServerLevel serverLevel = (ServerLevel)level;
/* 128 */       removeAllGoals(goal -> true);
/* 129 */       registerGoals();
/* 130 */       getBrain().stopAll(serverLevel, (LivingEntity)this);
/* 131 */       this.brain.clearMemories();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void babyGhastSetup() {
/* 136 */     this.moveControl = (MoveControl)new FlyingMoveControl((Mob)this, 180, true);
/* 137 */     this.lookControl = new LookControl((Mob)this);
/* 138 */     this.navigation = createBabyNavigation(level());
/* 139 */     setServerStillTimeout(0);
/* 140 */     removeAllGoals(goal -> true);
/*     */   }
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 145 */     if (isBaby()) {
/* 146 */       babyGhastSetup();
/*     */     } else {
/* 148 */       adultGhastSetup();
/*     */     } 
/* 150 */     super.ageBoundaryReached();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 154 */     return Animal.createAnimalAttributes()
/* 155 */       .add(Attributes.MAX_HEALTH, 20.0D)
/* 156 */       .add(Attributes.TEMPT_RANGE, 16.0D)
/* 157 */       .add(Attributes.FLYING_SPEED, 0.05D)
/* 158 */       .add(Attributes.MOVEMENT_SPEED, 0.05D)
/* 159 */       .add(Attributes.FOLLOW_RANGE, 16.0D)
/* 160 */       .add(Attributes.CAMERA_DISTANCE, 8.0D);
/*     */   }
/*     */   
/*     */   protected float sanitizeScale(float scale) {
/* 165 */     return Math.min(scale, 1.0F);
/*     */   }
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */   
/*     */   public boolean onClimbable() {
/* 178 */     return false;
/*     */   }
/*     */   
/*     */   public void travel(Vec3 input) {
/* 183 */     float speed = (float)getAttributeValue(Attributes.FLYING_SPEED) * 5.0F / 3.0F;
/* 184 */     travelFlying(input, speed, speed, speed);
/*     */   }
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 190 */     if (!level.isEmptyBlock(pos))
/* 191 */       return 0.0F; 
/* 193 */     if (level.isEmptyBlock(pos.below()) && !level.isEmptyBlock(pos.below(2)))
/* 194 */       return 10.0F; 
/* 196 */     return 5.0F;
/*     */   }
/*     */   
/*     */   public boolean canBreatheUnderwater() {
/* 201 */     if (isBaby())
/* 202 */       return true; 
/* 204 */     return super.canBreatheUnderwater();
/*     */   }
/*     */   
/*     */   protected boolean shouldStayCloseToLeashHolder() {
/* 209 */     return false;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {}
/*     */   
/*     */   public float getVoicePitch() {
/* 222 */     return 1.0F;
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 227 */     return SoundSource.NEUTRAL;
/*     */   }
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 232 */     int interval = super.getAmbientSoundInterval();
/* 233 */     if (isVehicle())
/* 235 */       return interval * 6; 
/* 237 */     return interval;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 242 */     return isBaby() ? SoundEvents.GHASTLING_AMBIENT : SoundEvents.HAPPY_GHAST_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 247 */     return isBaby() ? SoundEvents.GHASTLING_HURT : SoundEvents.HAPPY_GHAST_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 252 */     return isBaby() ? SoundEvents.GHASTLING_DEATH : SoundEvents.HAPPY_GHAST_DEATH;
/*     */   }
/*     */   
/*     */   protected float getSoundVolume() {
/* 257 */     return isBaby() ? 1.0F : 4.0F;
/*     */   }
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/* 262 */     return 1;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 267 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 272 */     return (AgeableMob)EntityTypes.HAPPY_GHAST.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   public boolean canFallInLove() {
/* 277 */     return false;
/*     */   }
/*     */   
/*     */   public float getAgeScale() {
/* 282 */     return isBaby() ? 0.2375F : 1.0F;
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 287 */     return itemStack.is(ItemTags.HAPPY_GHAST_FOOD);
/*     */   }
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 292 */     if (slot == EquipmentSlot.BODY)
/* 293 */       return (isAlive() && !isBaby()); 
/* 295 */     return super.canUseSlot(slot);
/*     */   }
/*     */   
/*     */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/* 300 */     return (slot == EquipmentSlot.BODY);
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 305 */     if (isBaby())
/* 306 */       return super.mobInteract(player, hand); 
/* 310 */     ItemStack itemStack = player.getItemInHand(hand);
/* 311 */     if (!itemStack.isEmpty()) {
/* 312 */       InteractionResult interactionResult = itemStack.interactLivingEntity(player, (LivingEntity)this, hand);
/* 313 */       if (interactionResult.consumesAction())
/* 314 */         return interactionResult; 
/*     */     } 
/* 318 */     if (isWearingBodyArmor() && !player.isSecondaryUseActive()) {
/* 319 */       doPlayerRide(player);
/* 320 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 322 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   private void doPlayerRide(Player player) {
/* 328 */     if (!level().isClientSide())
/* 329 */       player.startRiding((Entity)this); 
/*     */   }
/*     */   
/*     */   protected void addPassenger(Entity passenger) {
/* 335 */     if (!isVehicle())
/* 336 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.HARNESS_GOGGLES_DOWN, getSoundSource(), 1.0F, 1.0F); 
/* 338 */     super.addPassenger(passenger);
/* 339 */     if (!level().isClientSide())
/* 340 */       if (!scanPlayerAboveGhast()) {
/* 341 */         setServerStillTimeout(0);
/* 342 */       } else if (this.serverStillTimeout > 10) {
/* 343 */         setServerStillTimeout(10);
/*     */       }  
/*     */   }
/*     */   
/*     */   protected void removePassenger(Entity passenger) {
/* 350 */     super.removePassenger(passenger);
/* 351 */     if (!level().isClientSide())
/* 352 */       setServerStillTimeout(10); 
/* 354 */     if (!isVehicle()) {
/* 355 */       clearHome();
/* 356 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.HARNESS_GOGGLES_UP, getSoundSource(), 1.0F, 1.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean canAddPassenger(Entity passenger) {
/* 363 */     return (getPassengers().size() < 4);
/*     */   }
/*     */   
/*     */   public LivingEntity getControllingPassenger() {
/* 368 */     Entity firstPassenger = getFirstPassenger();
/* 369 */     if (isWearingBodyArmor() && !isOnStillTimeout() && firstPassenger instanceof Player) {
/* 369 */       Player player = (Player)firstPassenger;
/* 370 */       return (LivingEntity)player;
/*     */     } 
/* 372 */     return super.getControllingPassenger();
/*     */   }
/*     */   
/*     */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/* 377 */     float strafe = controller.xxa;
/* 378 */     float forward = 0.0F;
/* 379 */     float up = 0.0F;
/* 381 */     if (controller.zza != 0.0F) {
/* 383 */       float forwardLook = Mth.cos((controller.getXRot() * 0.017453292F));
/* 384 */       float upLook = -Mth.sin((controller.getXRot() * 0.017453292F));
/* 385 */       if (controller.zza < 0.0F) {
/* 387 */         forwardLook *= -0.5F;
/* 388 */         upLook *= -0.5F;
/*     */       } 
/* 390 */       up = upLook;
/* 391 */       forward = forwardLook;
/*     */     } 
/* 393 */     if (controller.isJumping())
/* 394 */       up += 0.5F; 
/* 396 */     return (new Vec3(strafe, up, forward)).scale(3.9000000953674316D * getAttributeValue(Attributes.FLYING_SPEED));
/*     */   }
/*     */   
/*     */   protected Vec2 getRiddenRotation(LivingEntity controller) {
/* 400 */     return new Vec2(controller.getXRot() * 0.5F, controller.getYRot());
/*     */   }
/*     */   
/*     */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/* 405 */     super.tickRidden(controller, riddenInput);
/* 406 */     Vec2 rotation = getRiddenRotation((LivingEntity)controller);
/* 407 */     float yRot = getYRot();
/* 408 */     float diff = Mth.wrapDegrees(rotation.y - yRot);
/* 410 */     float turnSpeed = 0.08F;
/* 411 */     yRot += diff * 0.08F;
/* 412 */     setRot(yRot, rotation.x);
/* 413 */     this.yRotO = this.yBodyRot = this.yHeadRot = yRot;
/*     */   }
/*     */   
/*     */   protected Brain<HappyGhast> makeBrain(Brain.Packed packedBrain) {
/* 422 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<HappyGhast> getBrain() {
/* 428 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 433 */     if (isBaby()) {
/* 434 */       ProfilerFiller profiler = Profiler.get();
/* 435 */       profiler.push("happyGhastBrain");
/* 436 */       getBrain().tick(level, (LivingEntity)this);
/* 437 */       profiler.pop();
/* 439 */       profiler.push("happyGhastActivityUpdate");
/* 440 */       HappyGhastAi.updateActivity(this);
/* 441 */       profiler.pop();
/*     */     } 
/* 444 */     checkRestriction();
/* 446 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 451 */     super.tick();
/* 452 */     if (level().isClientSide())
/*     */       return; 
/* 455 */     if (this.leashHolderTime > 0)
/* 456 */       this.leashHolderTime--; 
/* 458 */     setLeashHolder((this.leashHolderTime > 0));
/* 460 */     if (this.serverStillTimeout > 0) {
/* 461 */       if (this.tickCount > 60)
/* 462 */         this.serverStillTimeout--; 
/* 464 */       setServerStillTimeout(this.serverStillTimeout);
/*     */     } 
/* 466 */     if (scanPlayerAboveGhast())
/* 467 */       setServerStillTimeout(10); 
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 473 */     if (!level().isClientSide())
/* 474 */       setRequiresPrecisePosition(isOnStillTimeout()); 
/* 476 */     super.aiStep();
/* 478 */     continuousHeal();
/*     */   }
/*     */   
/*     */   private int getHappyGhastRestrictionRadius() {
/* 482 */     if (!isBaby() && getItemBySlot(EquipmentSlot.BODY).isEmpty())
/* 483 */       return 64; 
/* 485 */     return 32;
/*     */   }
/*     */   
/*     */   private void checkRestriction() {
/* 489 */     if (isLeashed() || isVehicle())
/*     */       return; 
/* 492 */     int radius = getHappyGhastRestrictionRadius();
/* 493 */     if (hasHome() && getHomePosition().closerThan((Vec3i)blockPosition(), (radius + 16)) && radius == getHomeRadius())
/*     */       return; 
/* 496 */     setHomeTo(blockPosition(), radius);
/*     */   }
/*     */   
/*     */   private void continuousHeal() {
/* 500 */     Level level = level();
/* 500 */     if (level instanceof ServerLevel) {
/* 500 */       ServerLevel serverLevel = (ServerLevel)level;
/* 500 */       if (isAlive() && this.deathTime == 0 && getMaxHealth() != getHealth()) {
/* 504 */         boolean isFastHealing = (isInClouds() || serverLevel.precipitationAt(blockPosition()) != Biome.Precipitation.NONE);
/* 505 */         if (this.tickCount % (isFastHealing ? 20 : 600) == 0)
/* 506 */           heal(1.0F); 
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 512 */     super.defineSynchedData(entityData);
/* 513 */     entityData.define(IS_LEASH_HOLDER, Boolean.valueOf(false));
/* 514 */     entityData.define(STAYS_STILL, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   private void setLeashHolder(boolean isLeashHolder) {
/* 518 */     this.entityData.set(IS_LEASH_HOLDER, Boolean.valueOf(isLeashHolder));
/*     */   }
/*     */   
/*     */   public boolean isLeashHolder() {
/* 522 */     return ((Boolean)this.entityData.get(IS_LEASH_HOLDER)).booleanValue();
/*     */   }
/*     */   
/*     */   private void syncStayStillFlag() {
/* 526 */     this.entityData.set(STAYS_STILL, Boolean.valueOf((this.serverStillTimeout > 0)));
/*     */   }
/*     */   
/*     */   public boolean staysStill() {
/* 530 */     return ((Boolean)this.entityData.get(STAYS_STILL)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean supportQuadLeashAsHolder() {
/* 535 */     return true;
/*     */   }
/*     */   
/*     */   public Vec3[] getQuadLeashHolderOffsets() {
/* 540 */     return Leashable.createQuadLeashOffsets((Entity)this, -0.03125D, 0.4375D, 0.46875D, 0.03125D);
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 545 */     return Vec3.ZERO;
/*     */   }
/*     */   
/*     */   public double leashElasticDistance() {
/* 550 */     return 10.0D;
/*     */   }
/*     */   
/*     */   public double leashSnapDistance() {
/* 555 */     return 16.0D;
/*     */   }
/*     */   
/*     */   public void onElasticLeashPull() {
/* 560 */     super.onElasticLeashPull();
/* 561 */     getMoveControl().setWait();
/*     */   }
/*     */   
/*     */   public void notifyLeashHolder(Leashable entity) {
/* 566 */     if (entity.supportQuadLeash())
/* 567 */       this.leashHolderTime = 5; 
/*     */   }
/*     */   
/*     */   public void addAdditionalSaveData(ValueOutput tag) {
/* 575 */     super.addAdditionalSaveData(tag);
/* 576 */     tag.putInt("still_timeout", this.serverStillTimeout);
/*     */   }
/*     */   
/*     */   public void readAdditionalSaveData(ValueInput tag) {
/* 581 */     super.readAdditionalSaveData(tag);
/* 582 */     setServerStillTimeout(tag.getIntOr("still_timeout", 0));
/*     */   }
/*     */   
/*     */   private static class BabyFlyingPathNavigation extends FlyingPathNavigation {
/*     */     public BabyFlyingPathNavigation(HappyGhast mob, Level level) {
/* 587 */       super((Mob)mob, level);
/* 588 */       setCanOpenDoors(false);
/* 589 */       setCanFloat(true);
/* 590 */       setRequiredPathLength(48.0F);
/*     */     }
/*     */     
/*     */     protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) {
/* 595 */       return isClearForMovementBetween(this.mob, startPos, stopPos, false);
/*     */     }
/*     */   }
/*     */   
/*     */   private class HappyGhastFloatGoal extends FloatGoal {
/*     */     public HappyGhastFloatGoal() {
/* 601 */       super((Mob)HappyGhast.this);
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 607 */       return (!HappyGhast.this.isOnStillTimeout() && super.canUse());
/*     */     }
/*     */   }
/*     */   
/*     */   private class HappyGhastLookControl extends LookControl {
/*     */     private HappyGhastLookControl() {
/* 613 */       super((Mob)HappyGhast.this);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 618 */       if (HappyGhast.this.isOnStillTimeout()) {
/* 619 */         float closeAngle = Mth.wrapDegrees90(HappyGhast.this.getYRot());
/* 620 */         HappyGhast.this.setYRot(HappyGhast.this.getYRot() - closeAngle);
/* 621 */         HappyGhast.this.setYHeadRot(HappyGhast.this.getYRot());
/*     */         return;
/*     */       } 
/* 624 */       if (this.lookAtCooldown > 0) {
/* 625 */         this.lookAtCooldown--;
/* 626 */         double xdd = this.wantedX - HappyGhast.this.getX();
/* 627 */         double zdd = this.wantedZ - HappyGhast.this.getZ();
/* 628 */         HappyGhast.this.setYRot(-((float)Mth.atan2(xdd, zdd)) * 57.295776F);
/* 629 */         HappyGhast.this.yBodyRot = HappyGhast.this.getYRot();
/* 630 */         HappyGhast.this.yHeadRot = HappyGhast.this.yBodyRot;
/*     */         return;
/*     */       } 
/* 633 */       Ghast.faceMovementDirection(this.mob);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isOnStillTimeout() {
/* 638 */     return (staysStill() || this.serverStillTimeout > 0);
/*     */   }
/*     */   
/*     */   private boolean scanPlayerAboveGhast() {
/* 642 */     AABB happyGhastBb = getBoundingBox();
/* 648 */     AABB ghastDetectionBox = new AABB(happyGhastBb.minX - 1.0D, happyGhastBb.maxY - 9.999999747378752E-6D, happyGhastBb.minZ - 1.0D, happyGhastBb.maxX + 1.0D, happyGhastBb.maxY + happyGhastBb.getYsize() / 2.0D, happyGhastBb.maxZ + 1.0D);
/* 651 */     for (Player player : level().players()) {
/* 652 */       if (player.isSpectator())
/*     */         continue; 
/* 655 */       Entity rootVehicle = player.getRootVehicle();
/* 656 */       if (!(rootVehicle instanceof HappyGhast) && ghastDetectionBox.contains(rootVehicle.position()))
/* 657 */         return true; 
/*     */     } 
/* 660 */     return false;
/*     */   }
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/* 665 */     return new HappyGhastBodyRotationControl();
/*     */   }
/*     */   
/*     */   private class HappyGhastBodyRotationControl extends BodyRotationControl {
/*     */     public HappyGhastBodyRotationControl() {
/* 670 */       super((Mob)HappyGhast.this);
/*     */     }
/*     */     
/*     */     public void clientTick() {
/* 675 */       if (HappyGhast.this.isVehicle()) {
/* 676 */         HappyGhast.this.yHeadRot = HappyGhast.this.getYRot();
/* 677 */         HappyGhast.this.yBodyRot = HappyGhast.this.yHeadRot;
/*     */       } 
/* 679 */       super.clientTick();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canBeCollidedWith(Entity other) {
/* 685 */     if (isBaby() || !isAlive())
/* 687 */       return false; 
/* 689 */     if (level().isClientSide() && other instanceof Player)
/* 692 */       if ((other.position()).y >= (getBoundingBox()).maxY)
/* 693 */         return true;  
/* 696 */     if (isVehicle() && other instanceof HappyGhast)
/* 698 */       return true; 
/* 700 */     return isOnStillTimeout();
/*     */   }
/*     */   
/*     */   public boolean isFlyingVehicle() {
/* 705 */     return !isBaby();
/*     */   }
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
/* 710 */     return new Vec3(getX(), (getBoundingBox()).maxY, getZ());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\happyghast\HappyGhast.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */