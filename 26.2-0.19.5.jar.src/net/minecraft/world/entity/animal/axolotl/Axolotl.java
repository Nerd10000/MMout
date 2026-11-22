/*     */ package net.minecraft.world.entity.animal.axolotl;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.BinaryAnimator;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.EasingType;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Bucketable;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Axolotl extends Animal implements Bucketable {
/*     */   public static final int TOTAL_PLAYDEAD_TIME = 200;
/*     */   
/*     */   private static final int POSE_ANIMATION_TICKS = 10;
/*     */   
/*  84 */   private static final Brain.Provider<Axolotl> BRAIN_PROVIDER = Brain.provider(
/*  85 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.HURT_BY, SensorType.AXOLOTL_ATTACKABLES, SensorType.FOOD_TEMPTATIONS), paramAxolotl -> AxolotlAi.getActivities());
/*     */   
/*  95 */   private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(Axolotl.class, EntityDataSerializers.INT);
/*     */   
/*  96 */   private static final EntityDataAccessor<Boolean> DATA_PLAYING_DEAD = SynchedEntityData.defineId(Axolotl.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  97 */   private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Axolotl.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final double PLAYER_REGEN_DETECTION_RANGE = 20.0D;
/*     */   
/*     */   public static final int RARE_VARIANT_CHANCE = 1200;
/*     */   
/*     */   private static final int AXOLOTL_TOTAL_AIR_SUPPLY = 6000;
/*     */   
/*     */   public static final String VARIANT_TAG = "Variant";
/*     */   
/*     */   private static final int REHYDRATE_AIR_SUPPLY = 1800;
/*     */   
/*     */   private static final int REGEN_BUFF_MAX_DURATION = 2400;
/*     */   
/*     */   private static final boolean DEFAULT_FROM_BUCKET = false;
/*     */   
/* 109 */   public final BinaryAnimator playingDeadAnimator = new BinaryAnimator(10, EasingType.IN_OUT_SINE);
/*     */   
/* 110 */   public final BinaryAnimator inWaterAnimator = new BinaryAnimator(10, EasingType.IN_OUT_SINE);
/*     */   
/* 111 */   public final BinaryAnimator onGroundAnimator = new BinaryAnimator(10, EasingType.IN_OUT_SINE);
/*     */   
/* 112 */   public final BinaryAnimator movingAnimator = new BinaryAnimator(10, EasingType.IN_OUT_SINE);
/*     */   
/* 114 */   public final AnimationState swimAnimationState = new AnimationState();
/*     */   
/* 115 */   public final AnimationState walkAnimationState = new AnimationState();
/*     */   
/* 116 */   public final AnimationState walkUnderWaterAnimationState = new AnimationState();
/*     */   
/* 117 */   public final AnimationState idleUnderWaterAnimationState = new AnimationState();
/*     */   
/* 118 */   public final AnimationState idleUnderWaterOnGroundAnimationState = new AnimationState();
/*     */   
/* 119 */   public final AnimationState idleOnGroundAnimationState = new AnimationState();
/*     */   
/* 120 */   public final AnimationState playDeadAnimationState = new AnimationState();
/*     */   
/* 122 */   private final ImmutableList<AnimationState> ALL_ANIMATIONS = ImmutableList.of(this.swimAnimationState, this.walkAnimationState, this.walkUnderWaterAnimationState, this.idleUnderWaterAnimationState, this.idleUnderWaterOnGroundAnimationState, this.idleOnGroundAnimationState, this.playDeadAnimationState);
/*     */   
/* 132 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.375F, 0.21F)
/* 133 */     .withEyeHeight(0.09375F)
/* 134 */     .withAttachments(EntityAttachments.builder()
/* 135 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.1875F, 0.0F));
/*     */   
/*     */   private static final int REGEN_BUFF_BASE_DURATION = 100;
/*     */   
/*     */   public enum Variant implements StringRepresentable {
/* 138 */     LUCY(0, "lucy", true),
/* 139 */     WILD(1, "wild", true),
/* 140 */     GOLD(2, "gold", true),
/* 141 */     CYAN(3, "cyan", true),
/* 142 */     BLUE(4, "blue", false);
/*     */     
/* 144 */     public static final Variant DEFAULT = LUCY;
/*     */     
/* 146 */     private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/* 148 */     public static final StreamCodec<ByteBuf, Variant> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Variant::getId);
/*     */     
/* 150 */     public static final Codec<Variant> CODEC = (Codec<Variant>)StringRepresentable.fromEnum(Variant::values);
/*     */     
/*     */     @Deprecated
/* 152 */     public static final Codec<Variant> LEGACY_CODEC = Codec.INT.xmap(BY_ID::apply, Variant::getId);
/*     */     
/*     */     private final int id;
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private final boolean common;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     Variant(int id, String name, boolean common) {
/* 159 */       this.id = id;
/* 160 */       this.name = name;
/* 161 */       this.common = common;
/*     */     }
/*     */     
/*     */     public int getId() {
/* 165 */       return this.id;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 169 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/* 174 */       return this.name;
/*     */     }
/*     */     
/*     */     public static Variant byId(int id) {
/* 178 */       return BY_ID.apply(id);
/*     */     }
/*     */     
/*     */     public static Variant getCommonSpawnVariant(RandomSource random) {
/* 182 */       return getSpawnVariant(random, true);
/*     */     }
/*     */     
/*     */     public static Variant getRareSpawnVariant(RandomSource random) {
/* 186 */       return getSpawnVariant(random, false);
/*     */     }
/*     */     
/*     */     private static Variant getSpawnVariant(RandomSource random, boolean common) {
/* 190 */       Variant[] validVariants = (Variant[])Arrays.<Variant>stream(values()).filter(v -> (v.common == common)).toArray(x$0 -> new Variant[x$0]);
/* 191 */       return (Variant)Util.getRandom((Object[])validVariants, random);
/*     */     }
/*     */   }
/*     */   
/*     */   public Axolotl(EntityType<? extends Axolotl> type, Level level) {
/* 198 */     super(type, level);
/* 200 */     setPathfindingMalus(PathType.WATER, 0.0F);
/* 201 */     this.moveControl = (MoveControl)new AxolotlMoveControl<>(this);
/* 202 */     this.lookControl = (LookControl)new AxolotlLookControl(this, 20);
/*     */   }
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 207 */     return 0.0F;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 212 */     super.defineSynchedData(entityData);
/* 213 */     entityData.define(DATA_VARIANT, Integer.valueOf(0));
/* 214 */     entityData.define(DATA_PLAYING_DEAD, Boolean.valueOf(false));
/* 215 */     entityData.define(FROM_BUCKET, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 220 */     super.addAdditionalSaveData(output);
/* 221 */     output.store("Variant", Variant.LEGACY_CODEC, getVariant());
/* 222 */     output.putBoolean("FromBucket", fromBucket());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 227 */     super.readAdditionalSaveData(input);
/* 228 */     setVariant(input.read("Variant", Variant.LEGACY_CODEC).orElse(Variant.DEFAULT));
/* 229 */     setFromBucket(input.getBooleanOr("FromBucket", false));
/*     */   }
/*     */   
/*     */   public void playAmbientSound() {
/* 234 */     if (isPlayingDead())
/*     */       return; 
/* 237 */     super.playAmbientSound();
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AxolotlGroupData axolotlGroupData;
/* 242 */     boolean isBaby = false;
/* 244 */     if (spawnReason == EntitySpawnReason.BUCKET)
/* 245 */       return groupData; 
/* 248 */     RandomSource random = level.getRandom();
/* 249 */     if (groupData instanceof AxolotlGroupData) {
/* 250 */       if (((AxolotlGroupData)groupData).getGroupSize() >= 2)
/* 251 */         isBaby = true; 
/*     */     } else {
/* 255 */       axolotlGroupData = new AxolotlGroupData(new Variant[] { Variant.getCommonSpawnVariant(random), Variant.getCommonSpawnVariant(random) });
/*     */     } 
/* 258 */     setVariant(axolotlGroupData.getVariant(random));
/* 259 */     if (isBaby)
/* 260 */       setAge(-24000); 
/* 263 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)axolotlGroupData);
/*     */   }
/*     */   
/*     */   public void baseTick() {
/* 268 */     int airSupply = getAirSupply();
/* 269 */     super.baseTick();
/* 270 */     if (!isNoAi()) {
/* 270 */       Level level = level();
/* 270 */       if (level instanceof ServerLevel) {
/* 270 */         ServerLevel serverLevel = (ServerLevel)level;
/* 271 */         handleAirSupply(serverLevel, airSupply);
/*     */       } 
/*     */     } 
/* 274 */     if (level().isClientSide())
/* 275 */       if (isBaby()) {
/* 276 */         tickBabyAnimations();
/*     */       } else {
/* 278 */         tickAdultAnimations();
/*     */       }  
/*     */   }
/*     */   
/*     */   private void tickBabyAnimations() {
/* 284 */     boolean isPlayingDead = isPlayingDead();
/* 285 */     boolean isInWater = isInWater();
/* 286 */     boolean onGround = onGround();
/* 287 */     boolean isMoving = (this.walkAnimation.isMoving() || getXRot() != this.xRotO || getYRot() != this.yRotO);
/* 288 */     this.movingAnimator.tick(isMoving);
/* 290 */     if (isPlayingDead) {
/* 291 */       soloAnimation(this.playDeadAnimationState);
/*     */       return;
/*     */     } 
/* 295 */     if (isMoving) {
/* 296 */       if (isInWater && !onGround) {
/* 297 */         soloAnimation(this.swimAnimationState);
/* 298 */       } else if (!isInWater && onGround) {
/* 299 */         soloAnimation(this.walkAnimationState);
/*     */       } else {
/* 301 */         soloAnimation(this.walkUnderWaterAnimationState);
/*     */       } 
/* 304 */     } else if (isInWater && !onGround) {
/* 305 */       soloAnimation(this.idleUnderWaterAnimationState);
/* 306 */     } else if (isInWater && onGround) {
/* 307 */       soloAnimation(this.idleUnderWaterOnGroundAnimationState);
/*     */     } else {
/* 309 */       soloAnimation(this.idleOnGroundAnimationState);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void soloAnimation(AnimationState toStart) {
/* 315 */     for (UnmodifiableIterator<AnimationState> unmodifiableIterator = this.ALL_ANIMATIONS.iterator(); unmodifiableIterator.hasNext(); ) {
/* 315 */       AnimationState animation = unmodifiableIterator.next();
/* 316 */       if (animation == toStart) {
/* 317 */         animation.startIfStopped(this.tickCount);
/*     */         continue;
/*     */       } 
/* 319 */       animation.stop();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void tickAdultAnimations() {
/*     */     AxolotlAnimationState animationState;
/* 326 */     if (isPlayingDead()) {
/* 327 */       animationState = AxolotlAnimationState.PLAYING_DEAD;
/* 328 */     } else if (isInWater()) {
/* 329 */       animationState = AxolotlAnimationState.IN_WATER;
/* 330 */     } else if (onGround()) {
/* 331 */       animationState = AxolotlAnimationState.ON_GROUND;
/*     */     } else {
/* 333 */       animationState = AxolotlAnimationState.IN_AIR;
/*     */     } 
/* 336 */     this.playingDeadAnimator.tick((animationState == AxolotlAnimationState.PLAYING_DEAD));
/* 337 */     this.inWaterAnimator.tick((animationState == AxolotlAnimationState.IN_WATER));
/* 338 */     this.onGroundAnimator.tick((animationState == AxolotlAnimationState.ON_GROUND));
/* 342 */     boolean isMoving = (this.walkAnimation.isMoving() || getXRot() != this.xRotO || getYRot() != this.yRotO);
/* 343 */     this.movingAnimator.tick(isMoving);
/*     */   }
/*     */   
/*     */   protected void handleAirSupply(ServerLevel level, int preTickAirSupply) {
/* 348 */     if (isAlive() && !isInWaterOrRain()) {
/* 349 */       setAirSupply(preTickAirSupply - 1);
/* 350 */       if (shouldTakeDrowningDamage()) {
/* 351 */         setAirSupply(0);
/* 352 */         hurtServer(level, damageSources().dryOut(), 2.0F);
/*     */       } 
/*     */     } else {
/* 355 */       setAirSupply(getMaxAirSupply());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void rehydrate() {
/* 360 */     int newAirSupply = getAirSupply() + 1800;
/* 361 */     setAirSupply(Math.min(newAirSupply, getMaxAirSupply()));
/*     */   }
/*     */   
/*     */   public int getMaxAirSupply() {
/* 366 */     return 6000;
/*     */   }
/*     */   
/*     */   public Variant getVariant() {
/* 370 */     return Variant.byId(((Integer)this.entityData.get(DATA_VARIANT)).intValue());
/*     */   }
/*     */   
/*     */   private void setVariant(Variant variant) {
/* 374 */     this.entityData.set(DATA_VARIANT, Integer.valueOf(variant.getId()));
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 379 */     if (type == DataComponents.AXOLOTL_VARIANT)
/* 380 */       return (T)castComponentValue(type, getVariant()); 
/* 383 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 388 */     applyImplicitComponentIfPresent(components, DataComponents.AXOLOTL_VARIANT);
/* 389 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 394 */     if (type == DataComponents.AXOLOTL_VARIANT) {
/* 395 */       setVariant((Variant)castComponentValue(DataComponents.AXOLOTL_VARIANT, value));
/* 396 */       return true;
/*     */     } 
/* 399 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   private static boolean useRareVariant(RandomSource random) {
/* 403 */     return (random.nextInt(1200) == 0);
/*     */   }
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 408 */     return level.isUnobstructed((Entity)this);
/*     */   }
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 414 */     return false;
/*     */   }
/*     */   
/*     */   public void setPlayingDead(boolean playingDead) {
/* 418 */     this.entityData.set(DATA_PLAYING_DEAD, Boolean.valueOf(playingDead));
/*     */   }
/*     */   
/*     */   public boolean isPlayingDead() {
/* 422 */     return ((Boolean)this.entityData.get(DATA_PLAYING_DEAD)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean fromBucket() {
/* 427 */     return ((Boolean)this.entityData.get(FROM_BUCKET)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setFromBucket(boolean fromBucket) {
/* 432 */     this.entityData.set(FROM_BUCKET, Boolean.valueOf(fromBucket));
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 437 */     Axolotl baby = (Axolotl)EntityTypes.AXOLOTL.create((Level)level, EntitySpawnReason.BREEDING);
/* 438 */     if (baby != null) {
/*     */       Variant variant;
/* 440 */       if (useRareVariant(this.random)) {
/* 441 */         variant = Variant.getRareSpawnVariant(this.random);
/*     */       } else {
/* 443 */         variant = this.random.nextBoolean() ? getVariant() : ((Axolotl)partner).getVariant();
/*     */       } 
/* 445 */       baby.setVariant(variant);
/* 446 */       baby.setPersistenceRequired();
/*     */     } 
/* 448 */     return (AgeableMob)baby;
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 453 */     return itemStack.is(ItemTags.AXOLOTL_FOOD);
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/* 458 */     return true;
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 463 */     ProfilerFiller profiler = Profiler.get();
/* 464 */     profiler.push("axolotlBrain");
/* 465 */     getBrain().tick(level, (LivingEntity)this);
/* 466 */     profiler.pop();
/* 468 */     profiler.push("axolotlActivityUpdate");
/* 469 */     AxolotlAi.updateActivity(this);
/* 470 */     profiler.pop();
/* 472 */     if (!isNoAi()) {
/* 473 */       Optional<Integer> playDeadTicks = getBrain().getMemory(MemoryModuleType.PLAY_DEAD_TICKS);
/* 474 */       setPlayingDead((playDeadTicks.isPresent() && ((Integer)playDeadTicks.get()).intValue() > 0));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 479 */     return Animal.createAnimalAttributes()
/* 480 */       .add(Attributes.MAX_HEALTH, 14.0D)
/* 481 */       .add(Attributes.MOVEMENT_SPEED, 1.0D)
/* 482 */       .add(Attributes.ATTACK_DAMAGE, 2.0D)
/* 483 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 488 */     return (PathNavigation)new AmphibiousPathNavigation((Mob)this, level);
/*     */   }
/*     */   
/*     */   public void playAttackSound() {
/* 493 */     playSound(SoundEvents.AXOLOTL_ATTACK, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 498 */     float currentHealth = getHealth();
/* 499 */     if (!isNoAi() && this.random
/* 500 */       .nextInt(3) == 0 && (this.random
/* 501 */       .nextInt(3) < damage || currentHealth / getMaxHealth() < 0.5F) && damage < currentHealth && 
/*     */       
/* 503 */       isInWater() && (source
/* 504 */       .getEntity() != null || source.getDirectEntity() != null) && 
/* 505 */       !isPlayingDead())
/* 506 */       this.brain.setMemory(MemoryModuleType.PLAY_DEAD_TICKS, Integer.valueOf(200)); 
/* 509 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 514 */     return 1;
/*     */   }
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 519 */     return 1;
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 524 */     return Bucketable.bucketMobPickup(player, hand, (LivingEntity)this).orElse(super.mobInteract(player, hand));
/*     */   }
/*     */   
/*     */   public void saveToBucketTag(ItemStack bucket) {
/* 529 */     Bucketable.saveDefaultDataToBucketTag((Mob)this, bucket);
/* 530 */     bucket.copyFrom(DataComponents.AXOLOTL_VARIANT, (DataComponentGetter)this);
/* 532 */     CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucket, tag -> {
/*     */           tag.putInt("Age", getAge());
/*     */           tag.putBoolean("AgeLocked", isAgeLocked());
/*     */           Brain<?> brain = getBrain();
/*     */           if (brain.hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN))
/*     */             tag.putLong("HuntingCooldown", brain.getTimeUntilExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN)); 
/*     */         });
/*     */   }
/*     */   
/*     */   public void loadFromBucketTag(CompoundTag tag) {
/* 544 */     Bucketable.loadDefaultDataFromBucketTag((Mob)this, tag);
/* 546 */     setAge(tag.getIntOr("Age", 0));
/* 547 */     setAgeLocked(tag.getBooleanOr("AgeLocked", false));
/* 548 */     tag.getLong("HuntingCooldown").ifPresentOrElse(huntingCooldown -> getBrain().setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, Boolean.valueOf(true), tag.getLongOr("HuntingCooldown", 0L)), () -> getBrain().setMemory(MemoryModuleType.HAS_HUNTING_COOLDOWN, Optional.empty()));
/*     */   }
/*     */   
/*     */   public ItemStack getBucketItemStack() {
/* 556 */     return new ItemStack((ItemLike)Items.AXOLOTL_BUCKET);
/*     */   }
/*     */   
/*     */   public SoundEvent getPickupSound() {
/* 561 */     return SoundEvents.BUCKET_FILL_AXOLOTL;
/*     */   }
/*     */   
/*     */   public boolean canBeSeenAsEnemy() {
/* 566 */     return (!isPlayingDead() && super.canBeSeenAsEnemy());
/*     */   }
/*     */   
/*     */   public static void onStopAttacking(ServerLevel level, Axolotl body, LivingEntity target) {
/* 570 */     if (target.isDeadOrDying()) {
/* 571 */       DamageSource lastDamageSource = target.getLastDamageSource();
/* 572 */       if (lastDamageSource != null) {
/* 573 */         Entity entity = lastDamageSource.getEntity();
/* 574 */         if (entity instanceof Player) {
/* 574 */           Player player = (Player)entity;
/* 575 */           List<Player> playersInRange = level.getEntitiesOfClass(Player.class, body.getBoundingBox().inflate(20.0D));
/* 579 */           if (playersInRange.contains(player))
/* 580 */             body.applySupportingEffects(player); 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void applySupportingEffects(Player player) {
/* 589 */     MobEffectInstance regenEffect = player.getEffect(MobEffects.REGENERATION);
/* 591 */     if (regenEffect == null || regenEffect.endsWithin(2399)) {
/* 592 */       int previousDuration = (regenEffect != null) ? regenEffect.getDuration() : 0;
/* 593 */       int regenDuration = Math.min(2400, 100 + previousDuration);
/* 594 */       player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, regenDuration, 0), (Entity)this);
/*     */     } 
/* 598 */     player.removeEffect(MobEffects.MINING_FATIGUE);
/*     */   }
/*     */   
/*     */   public boolean requiresCustomPersistence() {
/* 603 */     return (super.requiresCustomPersistence() || fromBucket());
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 608 */     return SoundEvents.AXOLOTL_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 613 */     return SoundEvents.AXOLOTL_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 618 */     return isInWater() ? SoundEvents.AXOLOTL_IDLE_WATER : SoundEvents.AXOLOTL_IDLE_AIR;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSplashSound() {
/* 623 */     return SoundEvents.AXOLOTL_SPLASH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 628 */     return SoundEvents.AXOLOTL_SWIM;
/*     */   }
/*     */   
/*     */   protected Brain<Axolotl> makeBrain(Brain.Packed packedBrain) {
/* 633 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Axolotl> getBrain() {
/* 639 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 644 */     moveRelative(getSpeed(), input);
/* 645 */     move(MoverType.SELF, getDeltaMovement());
/* 646 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */   }
/*     */   
/*     */   protected void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack) {
/* 652 */     if (itemStack.is(Items.TROPICAL_FISH_BUCKET)) {
/* 653 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack((ItemLike)Items.WATER_BUCKET)));
/*     */     } else {
/* 655 */       super.usePlayerItem(player, hand, itemStack);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 661 */     return (!fromBucket() && !hasCustomName());
/*     */   }
/*     */   
/*     */   public LivingEntity getTarget() {
/* 666 */     return getTargetFromBrain();
/*     */   }
/*     */   
/*     */   public static boolean checkAxolotlSpawnRules(EntityType<? extends LivingEntity> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 670 */     return level.getBlockState(pos.below()).is(BlockTags.AXOLOTLS_SPAWNABLE_ON);
/*     */   }
/*     */   
/*     */   private static class AxolotlMoveControl<T extends Axolotl> extends SmoothSwimmingMoveControl<T> {
/*     */     public AxolotlMoveControl(T axolotl) {
/* 675 */       super((Mob)axolotl, 85, 10, 0.1F, 0.5F, false);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 680 */       if (!((Axolotl)this.mob).isPlayingDead())
/* 681 */         super.tick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class AxolotlLookControl extends SmoothSwimmingLookControl {
/*     */     public AxolotlLookControl(Axolotl axolotl, int maxYRotFromCenter) {
/* 688 */       super((Mob)axolotl, maxYRotFromCenter);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 693 */       if (!Axolotl.this.isPlayingDead())
/* 694 */         super.tick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AxolotlGroupData extends AgeableMob.AgeableMobGroupData {
/*     */     public final Axolotl.Variant[] types;
/*     */     
/*     */     public AxolotlGroupData(Axolotl.Variant... types) {
/* 703 */       super(false);
/* 704 */       this.types = types;
/*     */     }
/*     */     
/*     */     public Axolotl.Variant getVariant(RandomSource random) {
/* 708 */       return this.types[random.nextInt(this.types.length)];
/*     */     }
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 714 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public enum AxolotlAnimationState {
/* 718 */     PLAYING_DEAD, IN_WATER, ON_GROUND, IN_AIR;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\axolotl\Axolotl.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */