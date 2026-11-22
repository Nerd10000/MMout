/*     */ package net.minecraft.world.entity.animal.wolf;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ItemParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Crackiness;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BegGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*     */ import net.minecraft.world.entity.animal.equine.Llama;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Wolf extends TamableAnimal implements NeutralMob {
/*  95 */   private static final EntityDataAccessor<Boolean> DATA_INTERESTED_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  96 */   private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.INT);
/*     */   
/*  97 */   private static final EntityDataAccessor<Long> DATA_ANGER_END_TIME = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.LONG);
/*     */   
/*  99 */   private static final EntityDataAccessor<Holder<WolfVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.WOLF_VARIANT);
/*     */   
/* 100 */   private static final EntityDataAccessor<Holder<WolfSoundVariant>> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Wolf.class, EntityDataSerializers.WOLF_SOUND_VARIANT);
/*     */   
/*     */   public static final TargetingConditions.Selector PREY_SELECTOR;
/*     */   
/*     */   static {
/* 101 */     PREY_SELECTOR = ((target, level) -> (target.is(EntityTypes.SHEEP) || target.is(EntityTypes.RABBIT) || target.is(EntityTypes.FOX)));
/*     */   }
/*     */   
/* 102 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.3F, 0.425F)
/* 103 */     .withEyeHeight(0.34375F)
/* 104 */     .withAttachments(EntityAttachments.builder()
/* 105 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.4375F, 0.0F));
/*     */   
/*     */   private static final float START_HEALTH = 8.0F;
/*     */   
/*     */   private static final float TAME_HEALTH = 40.0F;
/*     */   
/*     */   private static final float ARMOR_REPAIR_UNIT = 0.125F;
/*     */   
/*     */   public static final float DEFAULT_TAIL_ANGLE = 0.62831855F;
/*     */   
/* 112 */   private static final DyeColor DEFAULT_COLLAR_COLOR = DyeColor.RED;
/*     */   
/*     */   private float interestedAngle;
/*     */   
/*     */   private float interestedAngleO;
/*     */   
/*     */   private boolean isWet;
/*     */   
/*     */   private boolean isShaking;
/*     */   
/*     */   private float shakeAnim;
/*     */   
/*     */   private float shakeAnimO;
/*     */   
/* 121 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*     */   public Wolf(EntityType<? extends Wolf> type, Level level) {
/* 125 */     super(type, level);
/* 127 */     setTame(false, false);
/* 129 */     setPathfindingMalus(PathType.POWDER_SNOW, -1.0F);
/* 130 */     setPathfindingMalus(PathType.ON_TOP_OF_POWDER_SNOW, -1.0F);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 135 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/* 136 */     this.goalSelector.addGoal(1, (Goal)new TamableAnimal.TamableAnimalPanicGoal(this, 1.5D, DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
/* 137 */     this.goalSelector.addGoal(2, (Goal)new SitWhenOrderedToGoal(this));
/* 138 */     this.goalSelector.addGoal(3, (Goal)new WolfAvoidEntityGoal<>(this, Llama.class, 24.0F, 1.5D, 1.5D));
/* 139 */     this.goalSelector.addGoal(4, (Goal)new LeapAtTargetGoal((Mob)this, 0.4F));
/* 140 */     this.goalSelector.addGoal(5, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.0D, true));
/* 141 */     this.goalSelector.addGoal(6, (Goal)new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
/* 142 */     this.goalSelector.addGoal(7, (Goal)new BreedGoal((Animal)this, 1.0D));
/* 143 */     this.goalSelector.addGoal(8, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/* 144 */     this.goalSelector.addGoal(9, (Goal)new BegGoal(this, 8.0F));
/* 145 */     this.goalSelector.addGoal(10, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 146 */     this.goalSelector.addGoal(10, (Goal)new RandomLookAroundGoal((Mob)this));
/* 148 */     this.targetSelector.addGoal(1, (Goal)new OwnerHurtByTargetGoal(this));
/* 149 */     this.targetSelector.addGoal(2, (Goal)new OwnerHurtTargetGoal(this));
/* 150 */     this.targetSelector.addGoal(3, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[0])).setAlertOthers(new Class[0]));
/* 151 */     this.targetSelector.addGoal(4, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/* 152 */     this.targetSelector.addGoal(5, (Goal)new NonTameRandomTargetGoal(this, Animal.class, false, PREY_SELECTOR));
/* 153 */     this.targetSelector.addGoal(6, (Goal)new NonTameRandomTargetGoal(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
/* 154 */     this.targetSelector.addGoal(7, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractSkeleton.class, false));
/* 155 */     this.targetSelector.addGoal(8, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, true));
/*     */   }
/*     */   
/*     */   public Identifier getTexture() {
/* 159 */     WolfVariant variant = (WolfVariant)getVariant().value();
/* 160 */     WolfVariant.AssetInfo assetInfo = isBaby() ? variant.babyInfo() : variant.adultInfo();
/* 161 */     if (isTame())
/* 162 */       return assetInfo.tame().texturePath(); 
/* 164 */     if (isAngry())
/* 165 */       return assetInfo.angry().texturePath(); 
/* 167 */     return assetInfo.wild().texturePath();
/*     */   }
/*     */   
/*     */   private Holder<WolfVariant> getVariant() {
/* 171 */     return (Holder<WolfVariant>)this.entityData.get(DATA_VARIANT_ID);
/*     */   }
/*     */   
/*     */   private void setVariant(Holder<WolfVariant> variant) {
/* 175 */     this.entityData.set(DATA_VARIANT_ID, variant);
/*     */   }
/*     */   
/*     */   private Holder<WolfSoundVariant> getSoundVariant() {
/* 179 */     return (Holder<WolfSoundVariant>)this.entityData.get(DATA_SOUND_VARIANT_ID);
/*     */   }
/*     */   
/*     */   private WolfSoundVariant.WolfSoundSet getSoundSet() {
/* 183 */     return isBaby() ? ((WolfSoundVariant)getSoundVariant().value()).babySounds() : ((WolfSoundVariant)getSoundVariant().value()).adultSounds();
/*     */   }
/*     */   
/*     */   private void setSoundVariant(Holder<WolfSoundVariant> soundVariant) {
/* 187 */     this.entityData.set(DATA_SOUND_VARIANT_ID, soundVariant);
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 192 */     if (type == DataComponents.WOLF_VARIANT)
/* 193 */       return (T)castComponentValue(type, getVariant()); 
/* 196 */     if (type == DataComponents.WOLF_SOUND_VARIANT)
/* 197 */       return (T)castComponentValue(type, getSoundVariant()); 
/* 200 */     if (type == DataComponents.WOLF_COLLAR)
/* 201 */       return (T)castComponentValue(type, getCollarColor()); 
/* 204 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 209 */     applyImplicitComponentIfPresent(components, DataComponents.WOLF_VARIANT);
/* 210 */     applyImplicitComponentIfPresent(components, DataComponents.WOLF_SOUND_VARIANT);
/* 211 */     applyImplicitComponentIfPresent(components, DataComponents.WOLF_COLLAR);
/* 212 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 217 */     if (type == DataComponents.WOLF_VARIANT) {
/* 218 */       setVariant((Holder<WolfVariant>)castComponentValue(DataComponents.WOLF_VARIANT, value));
/* 219 */       return true;
/*     */     } 
/* 222 */     if (type == DataComponents.WOLF_SOUND_VARIANT) {
/* 223 */       setSoundVariant((Holder<WolfSoundVariant>)castComponentValue(DataComponents.WOLF_SOUND_VARIANT, value));
/* 224 */       return true;
/*     */     } 
/* 227 */     if (type == DataComponents.WOLF_COLLAR) {
/* 228 */       setCollarColor((DyeColor)castComponentValue(DataComponents.WOLF_COLLAR, value));
/* 229 */       return true;
/*     */     } 
/* 232 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 236 */     return Animal.createAnimalAttributes()
/* 237 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 238 */       .add(Attributes.MAX_HEALTH, 8.0D)
/* 239 */       .add(Attributes.ATTACK_DAMAGE, 4.0D);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 244 */     super.defineSynchedData(entityData);
/* 245 */     Registry<WolfSoundVariant> wolfSoundVariants = registryAccess().lookupOrThrow(Registries.WOLF_SOUND_VARIANT);
/* 246 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), WolfVariants.DEFAULT));
/* 247 */     Objects.requireNonNull(wolfSoundVariants);
/* 247 */     entityData.define(DATA_SOUND_VARIANT_ID, wolfSoundVariants.get(WolfSoundVariants.CLASSIC).or(wolfSoundVariants::getAny).orElseThrow());
/* 248 */     entityData.define(DATA_INTERESTED_ID, Boolean.valueOf(false));
/* 249 */     entityData.define(DATA_COLLAR_COLOR, Integer.valueOf(DEFAULT_COLLAR_COLOR.getId()));
/* 250 */     entityData.define(DATA_ANGER_END_TIME, Long.valueOf(-1L));
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 255 */     playSound((SoundEvent)getSoundSet().stepSound().value(), 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 260 */     super.addAdditionalSaveData(output);
/* 262 */     output.store("CollarColor", DyeColor.LEGACY_ID_CODEC, getCollarColor());
/* 263 */     VariantUtils.writeVariant(output, getVariant());
/* 264 */     addPersistentAngerSaveData(output);
/* 265 */     getSoundVariant().unwrapKey().ifPresent(soundVariant -> output.store("sound_variant", ResourceKey.codec(Registries.WOLF_SOUND_VARIANT), soundVariant));
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 270 */     super.readAdditionalSaveData(input);
/* 272 */     VariantUtils.readVariant(input, Registries.WOLF_VARIANT).ifPresent(this::setVariant);
/* 274 */     setCollarColor(input.read("CollarColor", DyeColor.LEGACY_ID_CODEC).orElse(DEFAULT_COLLAR_COLOR));
/* 276 */     readPersistentAngerSaveData(level(), input);
/* 278 */     input.read("sound_variant", ResourceKey.codec(Registries.WOLF_SOUND_VARIANT))
/* 279 */       .flatMap(soundVariant -> registryAccess().lookupOrThrow(Registries.WOLF_SOUND_VARIANT).get(soundVariant)).ifPresent(this::setSoundVariant);
/*     */   }
/*     */   
/*     */   public static class WolfPackData extends AgeableMob.AgeableMobGroupData {
/*     */     public final Holder<WolfVariant> type;
/*     */     
/*     */     public WolfPackData(Holder<WolfVariant> type) {
/* 286 */       super(false);
/* 287 */       this.type = type;
/*     */     }
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     WolfPackData wolfPackData;
/* 293 */     if (groupData instanceof WolfPackData) {
/* 293 */       WolfPackData wolfGroupData = (WolfPackData)groupData;
/* 294 */       setVariant(wolfGroupData.type);
/*     */     } else {
/* 296 */       Optional<? extends Holder<WolfVariant>> selectedVariant = VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.WOLF_VARIANT);
/* 297 */       if (selectedVariant.isPresent()) {
/* 298 */         setVariant(selectedVariant.get());
/* 299 */         wolfPackData = new WolfPackData(selectedVariant.get());
/*     */       } 
/*     */     } 
/* 302 */     setSoundVariant(WolfSoundVariants.pickRandomSoundVariant(registryAccess(), level.getRandom()));
/* 303 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)wolfPackData);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 308 */     if (isAngry())
/* 309 */       return (SoundEvent)getSoundSet().growlSound().value(); 
/* 311 */     if (this.random.nextInt(3) == 0) {
/* 312 */       if (isTame() && getHealth() < 20.0F)
/* 313 */         return (SoundEvent)getSoundSet().whineSound().value(); 
/* 315 */       return (SoundEvent)getSoundSet().pantSound().value();
/*     */     } 
/* 317 */     return (SoundEvent)getSoundSet().ambientSound().value();
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 322 */     if (canArmorAbsorb(source))
/* 323 */       return SoundEvents.WOLF_ARMOR_DAMAGE; 
/* 325 */     return (SoundEvent)getSoundSet().hurtSound().value();
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 330 */     return (SoundEvent)getSoundSet().deathSound().value();
/*     */   }
/*     */   
/*     */   protected float getSoundVolume() {
/* 335 */     return 0.4F;
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 340 */     super.aiStep();
/* 342 */     if (!level().isClientSide() && this.isWet && !this.isShaking && !isPathFinding() && onGround()) {
/* 343 */       this.isShaking = true;
/* 344 */       this.shakeAnim = 0.0F;
/* 345 */       this.shakeAnimO = 0.0F;
/* 346 */       level().broadcastEntityEvent((Entity)this, (byte)8);
/*     */     } 
/* 348 */     if (!level().isClientSide())
/* 349 */       updatePersistentAnger((ServerLevel)level(), true); 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 355 */     super.tick();
/* 357 */     if (!isAlive())
/*     */       return; 
/* 361 */     this.interestedAngleO = this.interestedAngle;
/* 362 */     if (isInterested()) {
/* 363 */       this.interestedAngle += (1.0F - this.interestedAngle) * 0.4F;
/*     */     } else {
/* 365 */       this.interestedAngle += (0.0F - this.interestedAngle) * 0.4F;
/*     */     } 
/* 368 */     if (isInWaterOrRain()) {
/* 369 */       this.isWet = true;
/* 370 */       if (this.isShaking && !level().isClientSide()) {
/* 371 */         level().broadcastEntityEvent((Entity)this, (byte)56);
/* 372 */         cancelShake();
/*     */       } 
/* 374 */     } else if ((this.isWet || this.isShaking) && 
/* 375 */       this.isShaking) {
/* 376 */       if (this.shakeAnim == 0.0F) {
/* 377 */         playSound(SoundEvents.WOLF_SHAKE, getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 378 */         gameEvent((Holder)GameEvent.ENTITY_ACTION);
/*     */       } 
/* 381 */       this.shakeAnimO = this.shakeAnim;
/* 382 */       this.shakeAnim += 0.05F;
/* 384 */       if (this.shakeAnimO >= 2.0F) {
/* 385 */         this.isWet = false;
/* 386 */         this.isShaking = false;
/* 387 */         this.shakeAnimO = 0.0F;
/* 388 */         this.shakeAnim = 0.0F;
/*     */       } 
/* 391 */       if (this.shakeAnim > 0.4F) {
/* 392 */         float yt = (float)getY();
/* 393 */         int shakeCount = (int)(Mth.sin(((this.shakeAnim - 0.4F) * 3.1415927F)) * 7.0F);
/* 394 */         Vec3 movement = getDeltaMovement();
/* 395 */         for (int i = 0; i < shakeCount; i++) {
/* 396 */           float xo = (this.random.nextFloat() * 2.0F - 1.0F) * getBbWidth() * 0.5F;
/* 397 */           float zo = (this.random.nextFloat() * 2.0F - 1.0F) * getBbWidth() * 0.5F;
/* 398 */           level().addParticle((ParticleOptions)ParticleTypes.SPLASH, getX() + xo, (yt + 0.8F), getZ() + zo, movement.x, movement.y, movement.z);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cancelShake() {
/* 406 */     this.isShaking = false;
/* 407 */     this.shakeAnim = 0.0F;
/* 408 */     this.shakeAnimO = 0.0F;
/*     */   }
/*     */   
/*     */   public void die(DamageSource source) {
/* 413 */     this.isWet = false;
/* 414 */     this.isShaking = false;
/* 415 */     this.shakeAnimO = 0.0F;
/* 416 */     this.shakeAnim = 0.0F;
/* 418 */     super.die(source);
/*     */   }
/*     */   
/*     */   public float getWetShade(float a) {
/* 422 */     if (!this.isWet)
/* 423 */       return 1.0F; 
/* 425 */     return Math.min(0.75F + Mth.lerp(a, this.shakeAnimO, this.shakeAnim) / 2.0F * 0.25F, 1.0F);
/*     */   }
/*     */   
/*     */   public float getShakeAnim(float a) {
/* 429 */     return Mth.lerp(a, this.shakeAnimO, this.shakeAnim);
/*     */   }
/*     */   
/*     */   public float getHeadRollAngle(float a) {
/* 433 */     return Mth.lerp(a, this.interestedAngleO, this.interestedAngle) * 0.15F * 3.1415927F;
/*     */   }
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 438 */     if (isInSittingPose())
/* 439 */       return 20; 
/* 441 */     return super.getMaxHeadXRot();
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 446 */     if (isInvulnerableTo(level, source))
/* 447 */       return false; 
/* 450 */     setOrderedToSit(false);
/* 452 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   protected void actuallyHurt(ServerLevel level, DamageSource source, float damage) {
/* 457 */     if (!canArmorAbsorb(source)) {
/* 458 */       super.actuallyHurt(level, source, damage);
/*     */       return;
/*     */     } 
/* 461 */     ItemStack armorBefore = getBodyArmorItem();
/* 462 */     int damageBefore = armorBefore.getDamageValue();
/* 463 */     int maxDamage = armorBefore.getMaxDamage();
/* 464 */     armorBefore.hurtAndBreak(Mth.ceil(damage), (LivingEntity)this, EquipmentSlot.BODY);
/* 465 */     if (Crackiness.WOLF_ARMOR.byDamage(damageBefore, maxDamage) != Crackiness.WOLF_ARMOR.byDamage(getBodyArmorItem())) {
/* 467 */       playSound(SoundEvents.WOLF_ARMOR_CRACK);
/* 468 */       level.sendParticles((ParticleOptions)new ItemParticleOption(ParticleTypes.ITEM, Items.ARMADILLO_SCUTE), getX(), getY() + 1.0D, getZ(), 20, 0.2D, 0.1D, 0.2D, 0.1D);
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canArmorAbsorb(DamageSource source) {
/* 473 */     return (getBodyArmorItem().is(Items.WOLF_ARMOR) && !source.is(DamageTypeTags.BYPASSES_WOLF_ARMOR));
/*     */   }
/*     */   
/*     */   protected void applyTamingSideEffects() {
/* 478 */     if (isTame()) {
/* 479 */       getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0D);
/* 480 */       setHealth(40.0F);
/*     */     } else {
/* 482 */       getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void hurtArmor(DamageSource damageSource, float damage) {
/* 488 */     doHurtEquipment(damageSource, damage, new EquipmentSlot[] { EquipmentSlot.BODY });
/*     */   }
/*     */   
/*     */   protected boolean canShearEquipment(Player player) {
/* 493 */     return isOwnedBy((LivingEntity)player);
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 498 */     ItemStack itemStack = player.getItemInHand(hand);
/* 500 */     if (isTame()) {
/* 502 */       if (isFood(itemStack) && getHealth() < getMaxHealth()) {
/* 503 */         feed(player, hand, itemStack, 2.0F, 2.0F);
/* 504 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/* 505 */       if (itemStack.is(ItemTags.WOLF_COLLAR_DYES) && isOwnedBy((LivingEntity)player)) {
/* 506 */         DyeColor color = (DyeColor)itemStack.get(DataComponents.DYE);
/* 507 */         if (color != null && color != getCollarColor()) {
/* 508 */           setCollarColor(color);
/* 509 */           itemStack.consume(1, (LivingEntity)player);
/* 510 */           return (InteractionResult)InteractionResult.SUCCESS;
/*     */         } 
/*     */       } else {
/* 512 */         if (isEquippableInSlot(itemStack, EquipmentSlot.BODY) && !isWearingBodyArmor() && isOwnedBy((LivingEntity)player) && !isBaby()) {
/* 513 */           setItemSlotAndDropWhenKilled(EquipmentSlot.BODY, itemStack.copyWithCount(1));
/* 514 */           itemStack.consume(1, (LivingEntity)player);
/* 515 */           return (InteractionResult)InteractionResult.SUCCESS;
/*     */         } 
/* 516 */         if (isInSittingPose() && isWearingBodyArmor() && isOwnedBy((LivingEntity)player) && getBodyArmorItem().isDamaged() && getBodyArmorItem().isValidRepairItem(itemStack)) {
/* 517 */           itemStack.shrink(1);
/* 518 */           playSound(SoundEvents.WOLF_ARMOR_REPAIR);
/* 519 */           ItemStack armor = getBodyArmorItem();
/* 520 */           int repairUnit = (int)(armor.getMaxDamage() * 0.125F);
/* 521 */           armor.setDamageValue(Math.max(0, armor.getDamageValue() - repairUnit));
/* 522 */           return (InteractionResult)InteractionResult.SUCCESS;
/*     */         } 
/* 525 */         InteractionResult interactionResult = super.mobInteract(player, hand);
/* 526 */         if (!interactionResult.consumesAction() && isOwnedBy((LivingEntity)player)) {
/* 527 */           setOrderedToSit(!isOrderedToSit());
/* 528 */           this.jumping = false;
/* 529 */           this.navigation.stop();
/* 530 */           setTarget(null);
/* 531 */           return (InteractionResult)InteractionResult.SUCCESS.withoutItem();
/*     */         } 
/* 533 */         return interactionResult;
/*     */       } 
/* 536 */     } else if (!level().isClientSide() && itemStack.is(Items.BONE) && !isAngry()) {
/* 537 */       itemStack.consume(1, (LivingEntity)player);
/* 538 */       tryToTame(player);
/* 539 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/* 542 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   private void tryToTame(Player player) {
/* 546 */     if (this.random.nextInt(3) == 0) {
/* 547 */       tame(player);
/* 548 */       this.navigation.stop();
/* 549 */       setTarget(null);
/* 550 */       setOrderedToSit(true);
/* 551 */       level().broadcastEntityEvent((Entity)this, (byte)7);
/*     */     } else {
/* 553 */       level().broadcastEntityEvent((Entity)this, (byte)6);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 559 */     if (id == 8) {
/* 560 */       this.isShaking = true;
/* 561 */       this.shakeAnim = 0.0F;
/* 562 */       this.shakeAnimO = 0.0F;
/* 563 */     } else if (id == 56) {
/* 564 */       cancelShake();
/*     */     } else {
/* 566 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getTailAngle() {
/* 571 */     if (isAngry())
/* 572 */       return 1.5393804F; 
/* 573 */     if (isTame()) {
/* 574 */       float maxHealth = getMaxHealth();
/* 575 */       float damageRatio = (maxHealth - getHealth()) / maxHealth;
/* 576 */       return (0.55F - damageRatio * 0.4F) * 3.1415927F;
/*     */     } 
/* 578 */     return 0.62831855F;
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 583 */     return itemStack.is(ItemTags.WOLF_FOOD);
/*     */   }
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/* 588 */     return 8;
/*     */   }
/*     */   
/*     */   public long getPersistentAngerEndTime() {
/* 593 */     return ((Long)this.entityData.get(DATA_ANGER_END_TIME)).longValue();
/*     */   }
/*     */   
/*     */   public void setPersistentAngerEndTime(long endTime) {
/* 598 */     this.entityData.set(DATA_ANGER_END_TIME, Long.valueOf(endTime));
/*     */   }
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 603 */     setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
/*     */   }
/*     */   
/*     */   public EntityReference<LivingEntity> getPersistentAngerTarget() {
/* 608 */     return this.persistentAngerTarget;
/*     */   }
/*     */   
/*     */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) {
/* 613 */     this.persistentAngerTarget = persistentAngerTarget;
/*     */   }
/*     */   
/*     */   public DyeColor getCollarColor() {
/* 617 */     return DyeColor.byId(((Integer)this.entityData.get(DATA_COLLAR_COLOR)).intValue());
/*     */   }
/*     */   
/*     */   private void setCollarColor(DyeColor color) {
/* 621 */     this.entityData.set(DATA_COLLAR_COLOR, Integer.valueOf(color.getId()));
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 626 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public Wolf getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 631 */     Wolf baby = (Wolf)EntityTypes.WOLF.create((Level)level, EntitySpawnReason.BREEDING);
/* 632 */     if (baby != null && partner instanceof Wolf) {
/* 632 */       Wolf partnerWolf = (Wolf)partner;
/* 633 */       if (this.random.nextBoolean()) {
/* 634 */         baby.setVariant(getVariant());
/*     */       } else {
/* 636 */         baby.setVariant(partnerWolf.getVariant());
/*     */       } 
/* 639 */       if (isTame()) {
/* 640 */         baby.setOwnerReference(getOwnerReference());
/* 641 */         baby.setTame(true, true);
/* 642 */         DyeColor parent1CollarColor = getCollarColor();
/* 643 */         DyeColor parent2CollarColor = partnerWolf.getCollarColor();
/* 644 */         baby.setCollarColor(DyeColor.getMixedColor(level, parent1CollarColor, parent2CollarColor));
/*     */       } 
/* 646 */       baby.setSoundVariant(WolfSoundVariants.pickRandomSoundVariant(registryAccess(), this.random));
/*     */     } 
/* 648 */     return baby;
/*     */   }
/*     */   
/*     */   public void setIsInterested(boolean value) {
/* 652 */     this.entityData.set(DATA_INTERESTED_ID, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean canMate(Animal partner) {
/*     */     Wolf wolf;
/* 657 */     if (partner == this)
/* 658 */       return false; 
/* 660 */     if (!isTame())
/* 661 */       return false; 
/* 663 */     if (partner instanceof Wolf) {
/* 663 */       wolf = (Wolf)partner;
/*     */     } else {
/* 664 */       return false;
/*     */     } 
/* 666 */     if (!wolf.isTame())
/* 667 */       return false; 
/* 669 */     if (wolf.isInSittingPose())
/* 670 */       return false; 
/* 672 */     return (isInLove() && wolf.isInLove());
/*     */   }
/*     */   
/*     */   public boolean isInterested() {
/* 676 */     return ((Boolean)this.entityData.get(DATA_INTERESTED_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
/* 682 */     if (target instanceof net.minecraft.world.entity.monster.Creeper || target instanceof net.minecraft.world.entity.monster.Ghast || target instanceof net.minecraft.world.entity.decoration.ArmorStand)
/* 683 */       return false; 
/* 686 */     if (target instanceof Wolf) {
/* 686 */       Wolf wolfTarget = (Wolf)target;
/* 687 */       return (!wolfTarget.isTame() || wolfTarget.getOwner() != owner);
/*     */     } 
/* 689 */     if (target instanceof Player) {
/* 689 */       Player playerTarget = (Player)target;
/* 689 */       if (owner instanceof Player) {
/* 689 */         Player playerOwner = (Player)owner;
/* 689 */         if (!playerOwner.canHarmPlayer(playerTarget))
/* 691 */           return false; 
/*     */       } 
/*     */     } 
/* 694 */     if (target instanceof AbstractHorse) {
/* 694 */       AbstractHorse horse = (AbstractHorse)target;
/* 694 */       if (horse.isTamed())
/* 695 */         return false; 
/*     */     } 
/* 698 */     if (target instanceof TamableAnimal) {
/* 698 */       TamableAnimal animal = (TamableAnimal)target;
/* 698 */       if (!animal.isTame());
/* 698 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/* 703 */     return !isAngry();
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 708 */     return new Vec3(0.0D, (0.6F * getEyeHeight()), (getBbWidth() * 0.4F));
/*     */   }
/*     */   
/*     */   public static boolean checkWolfSpawnRules(EntityType<Wolf> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 712 */     return (level.getBlockState(pos.below()).is(BlockTags.WOLVES_SPAWNABLE_ON) && 
/* 713 */       isBrightEnoughToSpawn((BlockAndLightGetter)level, pos));
/*     */   }
/*     */   
/*     */   private class WolfAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*     */     private final Wolf wolf;
/*     */     
/*     */     public WolfAvoidEntityGoal(Wolf wolf, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 721 */       super((PathfinderMob)wolf, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier);
/* 722 */       this.wolf = wolf;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 727 */       if (super.canUse() && 
/* 728 */         this.toAvoid instanceof Llama)
/* 729 */         return (!this.wolf.isTame() && avoidLlama((Llama)this.toAvoid)); 
/* 733 */       return false;
/*     */     }
/*     */     
/*     */     private boolean avoidLlama(Llama llama) {
/* 737 */       return (llama.getStrength() >= Wolf.this.random.nextInt(5));
/*     */     }
/*     */     
/*     */     public void start() {
/* 742 */       Wolf.this.setTarget(null);
/* 743 */       super.start();
/*     */     }
/*     */     
/*     */     public void tick() {
/* 748 */       Wolf.this.setTarget(null);
/* 749 */       super.tick();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\wolf\Wolf.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */