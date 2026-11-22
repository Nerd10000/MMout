/*     */ package net.minecraft.world.entity.animal.feline;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.CatLieOnBedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.CatSitOnBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.OcelotAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.BedBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ 
/*     */ public class Cat extends TamableAnimal {
/*     */   public static final double TEMPT_SPEED_MOD = 0.6D;
/*     */   
/*     */   public static final double WALK_SPEED_MOD = 0.8D;
/*     */   
/*     */   public static final double SPRINT_SPEED_MOD = 1.33D;
/*     */   
/*  82 */   private static final EntityDataAccessor<Holder<CatVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.CAT_VARIANT);
/*     */   
/*  83 */   private static final EntityDataAccessor<Boolean> IS_LYING = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  84 */   private static final EntityDataAccessor<Boolean> RELAX_STATE_ONE = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  85 */   private static final EntityDataAccessor<Integer> DATA_COLLAR_COLOR = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.INT);
/*     */   
/*  86 */   private static final EntityDataAccessor<Holder<CatSoundVariant>> DATA_SOUND_VARIANT_ID = SynchedEntityData.defineId(Cat.class, EntityDataSerializers.CAT_SOUND_VARIANT);
/*     */   
/*  88 */   private static final ResourceKey<CatVariant> DEFAULT_VARIANT = CatVariants.BLACK;
/*     */   
/*  89 */   private static final DyeColor DEFAULT_COLLAR_COLOR = DyeColor.RED;
/*     */   
/*  90 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.3F, 0.35F)
/*  91 */     .withEyeHeight(0.34375F)
/*  92 */     .withAttachments(EntityAttachments.builder()
/*  93 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.3125F, 0.0F));
/*     */   
/*     */   private CatAvoidEntityGoal<Player> avoidPlayersGoal;
/*     */   
/*     */   private TemptGoal temptGoal;
/*     */   
/*     */   private float lieDownAmount;
/*     */   
/*     */   private float lieDownAmountO;
/*     */   
/*     */   private float lieDownAmountTail;
/*     */   
/*     */   private float lieDownAmountOTail;
/*     */   
/*     */   private boolean isLyingOnTopOfSleepingPlayer;
/*     */   
/*     */   private float relaxStateOneAmount;
/*     */   
/*     */   private float relaxStateOneAmountO;
/*     */   
/*     */   public Cat(EntityType<? extends Cat> type, Level level) {
/* 107 */     super(type, level);
/* 108 */     reassessTameGoals();
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 113 */     this.temptGoal = new CatTemptGoal(this, 0.6D, i -> i.is(ItemTags.CAT_FOOD), true);
/* 115 */     this.goalSelector.addGoal(1, (Goal)new FloatGoal((Mob)this));
/* 116 */     this.goalSelector.addGoal(1, (Goal)new TamableAnimal.TamableAnimalPanicGoal(this, 1.5D));
/* 117 */     this.goalSelector.addGoal(2, (Goal)new SitWhenOrderedToGoal(this));
/* 118 */     this.goalSelector.addGoal(3, new CatRelaxOnOwnerGoal(this));
/* 119 */     this.goalSelector.addGoal(4, (Goal)this.temptGoal);
/* 120 */     this.goalSelector.addGoal(5, (Goal)new CatLieOnBedGoal(this, 1.1D, 8));
/* 121 */     this.goalSelector.addGoal(6, (Goal)new FollowOwnerGoal(this, 1.0D, 10.0F, 5.0F));
/* 122 */     this.goalSelector.addGoal(7, (Goal)new CatSitOnBlockGoal(this, 0.8D));
/* 123 */     this.goalSelector.addGoal(8, (Goal)new LeapAtTargetGoal((Mob)this, 0.3F));
/* 124 */     this.goalSelector.addGoal(9, (Goal)new OcelotAttackGoal((Mob)this));
/* 125 */     this.goalSelector.addGoal(10, (Goal)new BreedGoal((Animal)this, 0.8D));
/* 126 */     this.goalSelector.addGoal(11, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.8D, 1.0000001E-5F));
/* 127 */     this.goalSelector.addGoal(12, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 10.0F));
/* 129 */     this.targetSelector.addGoal(1, (Goal)new NonTameRandomTargetGoal(this, Rabbit.class, false, null));
/* 130 */     this.targetSelector.addGoal(1, (Goal)new NonTameRandomTargetGoal(this, Turtle.class, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */   
/*     */   public Holder<CatVariant> getVariant() {
/* 134 */     return (Holder<CatVariant>)this.entityData.get(DATA_VARIANT_ID);
/*     */   }
/*     */   
/*     */   private void setVariant(Holder<CatVariant> variant) {
/* 138 */     this.entityData.set(DATA_VARIANT_ID, variant);
/*     */   }
/*     */   
/*     */   private Holder<CatSoundVariant> getSoundVariant() {
/* 142 */     return (Holder<CatSoundVariant>)this.entityData.get(DATA_SOUND_VARIANT_ID);
/*     */   }
/*     */   
/*     */   private void setSoundVariant(Holder<CatSoundVariant> soundVariant) {
/* 146 */     this.entityData.set(DATA_SOUND_VARIANT_ID, soundVariant);
/*     */   }
/*     */   
/*     */   private CatSoundVariant.CatSoundSet getSoundSet() {
/* 150 */     return isBaby() ? ((CatSoundVariant)getSoundVariant().value()).babySounds() : ((CatSoundVariant)getSoundVariant().value()).adultSounds();
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 155 */     if (type == DataComponents.CAT_VARIANT)
/* 156 */       return (T)castComponentValue(type, getVariant()); 
/* 159 */     if (type == DataComponents.CAT_SOUND_VARIANT)
/* 160 */       return (T)castComponentValue(type, getSoundVariant()); 
/* 163 */     if (type == DataComponents.CAT_COLLAR)
/* 164 */       return (T)castComponentValue(type, getCollarColor()); 
/* 167 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 172 */     applyImplicitComponentIfPresent(components, DataComponents.CAT_VARIANT);
/* 173 */     applyImplicitComponentIfPresent(components, DataComponents.CAT_SOUND_VARIANT);
/* 174 */     applyImplicitComponentIfPresent(components, DataComponents.CAT_COLLAR);
/* 175 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 180 */     if (type == DataComponents.CAT_VARIANT) {
/* 181 */       setVariant((Holder<CatVariant>)castComponentValue(DataComponents.CAT_VARIANT, value));
/* 182 */       return true;
/*     */     } 
/* 185 */     if (type == DataComponents.CAT_SOUND_VARIANT) {
/* 186 */       setSoundVariant((Holder<CatSoundVariant>)castComponentValue(DataComponents.CAT_SOUND_VARIANT, value));
/* 187 */       return true;
/*     */     } 
/* 190 */     if (type == DataComponents.CAT_COLLAR) {
/* 191 */       setCollarColor((DyeColor)castComponentValue(DataComponents.CAT_COLLAR, value));
/* 192 */       return true;
/*     */     } 
/* 195 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   public void setLying(boolean value) {
/* 199 */     this.entityData.set(IS_LYING, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   public boolean isLying() {
/* 203 */     return ((Boolean)this.entityData.get(IS_LYING)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setRelaxStateOne(boolean value) {
/* 207 */     this.entityData.set(RELAX_STATE_ONE, Boolean.valueOf(value));
/*     */   }
/*     */   
/*     */   private boolean isRelaxStateOne() {
/* 211 */     return ((Boolean)this.entityData.get(RELAX_STATE_ONE)).booleanValue();
/*     */   }
/*     */   
/*     */   public DyeColor getCollarColor() {
/* 215 */     return DyeColor.byId(((Integer)this.entityData.get(DATA_COLLAR_COLOR)).intValue());
/*     */   }
/*     */   
/*     */   private void setCollarColor(DyeColor color) {
/* 219 */     this.entityData.set(DATA_COLLAR_COLOR, Integer.valueOf(color.getId()));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 224 */     super.defineSynchedData(entityData);
/* 225 */     Registry<CatSoundVariant> catSoundVariants = registryAccess().lookupOrThrow(Registries.CAT_SOUND_VARIANT);
/* 226 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), DEFAULT_VARIANT));
/* 227 */     Objects.requireNonNull(catSoundVariants);
/* 227 */     entityData.define(DATA_SOUND_VARIANT_ID, catSoundVariants.get(CatSoundVariants.CLASSIC).or(catSoundVariants::getAny).orElseThrow());
/* 228 */     entityData.define(IS_LYING, Boolean.valueOf(false));
/* 229 */     entityData.define(RELAX_STATE_ONE, Boolean.valueOf(false));
/* 230 */     entityData.define(DATA_COLLAR_COLOR, Integer.valueOf(DEFAULT_COLLAR_COLOR.getId()));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 235 */     super.addAdditionalSaveData(output);
/* 236 */     VariantUtils.writeVariant(output, getVariant());
/* 237 */     getSoundVariant().unwrapKey().ifPresent(soundVariant -> output.store("sound_variant", ResourceKey.codec(Registries.CAT_SOUND_VARIANT), soundVariant));
/* 238 */     output.store("CollarColor", DyeColor.LEGACY_ID_CODEC, getCollarColor());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 243 */     super.readAdditionalSaveData(input);
/* 245 */     VariantUtils.readVariant(input, Registries.CAT_VARIANT).ifPresent(this::setVariant);
/* 246 */     input.read("sound_variant", ResourceKey.codec(Registries.CAT_SOUND_VARIANT))
/* 247 */       .flatMap(soundVariant -> registryAccess().lookupOrThrow(Registries.CAT_SOUND_VARIANT).get(soundVariant)).ifPresent(this::setSoundVariant);
/* 248 */     setCollarColor(input.read("CollarColor", DyeColor.LEGACY_ID_CODEC).orElse(DEFAULT_COLLAR_COLOR));
/*     */   }
/*     */   
/*     */   public void customServerAiStep(ServerLevel level) {
/* 253 */     if (getMoveControl().hasWanted()) {
/* 254 */       double speed = getMoveControl().getSpeedModifier();
/* 255 */       if (speed == 0.6D) {
/* 256 */         setPose(Pose.CROUCHING);
/* 257 */         setSprinting(false);
/* 258 */       } else if (speed == 1.33D) {
/* 259 */         setPose(Pose.STANDING);
/* 260 */         setSprinting(true);
/*     */       } else {
/* 262 */         setPose(Pose.STANDING);
/* 263 */         setSprinting(false);
/*     */       } 
/*     */     } else {
/* 266 */       setPose(Pose.STANDING);
/* 267 */       setSprinting(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 273 */     if (isTame()) {
/* 274 */       if (isInLove())
/* 275 */         return (SoundEvent)getSoundSet().purrSound().value(); 
/* 277 */       if (this.random.nextInt(4) == 0)
/* 278 */         return (SoundEvent)getSoundSet().purreowSound().value(); 
/* 280 */       return (SoundEvent)getSoundSet().ambientSound().value();
/*     */     } 
/* 283 */     return (SoundEvent)getSoundSet().strayAmbientSound().value();
/*     */   }
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 288 */     return 120;
/*     */   }
/*     */   
/*     */   public void hiss() {
/* 292 */     makeSound((SoundEvent)getSoundSet().hissSound().value());
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 297 */     return (SoundEvent)getSoundSet().hurtSound().value();
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 302 */     return (SoundEvent)getSoundSet().deathSound().value();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 306 */     return Animal.createAnimalAttributes()
/* 307 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 308 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 309 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 314 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   protected void playEatingSound() {
/* 319 */     playSound((SoundEvent)getSoundSet().eatSound().value(), 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 324 */     super.tick();
/* 326 */     if (this.temptGoal != null && this.temptGoal.isRunning() && !isTame() && this.tickCount % 100 == 0)
/* 327 */       playSound((SoundEvent)getSoundSet().begForFoodSound().value(), 1.0F, 1.0F); 
/* 329 */     handleLieDown();
/*     */   }
/*     */   
/*     */   private void handleLieDown() {
/* 333 */     if ((isLying() || isRelaxStateOne()) && this.tickCount % 5 == 0)
/* 334 */       playSound((SoundEvent)getSoundSet().purrSound().value(), 0.6F + 0.4F * (this.random.nextFloat() - this.random.nextFloat()), 1.0F); 
/* 336 */     updateLieDownAmount();
/* 337 */     updateRelaxStateOneAmount();
/* 338 */     this.isLyingOnTopOfSleepingPlayer = false;
/* 339 */     if (isLying()) {
/* 340 */       BlockPos catPos = blockPosition();
/* 341 */       List<Player> players = level().getEntitiesOfClass(Player.class, (new AABB(catPos)).inflate(2.0D, 2.0D, 2.0D));
/* 342 */       for (Player player : players) {
/* 343 */         if (player.isSleeping()) {
/* 344 */           this.isLyingOnTopOfSleepingPlayer = true;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isLyingOnTopOfSleepingPlayer() {
/* 352 */     return this.isLyingOnTopOfSleepingPlayer;
/*     */   }
/*     */   
/*     */   private void updateLieDownAmount() {
/* 356 */     this.lieDownAmountO = this.lieDownAmount;
/* 357 */     this.lieDownAmountOTail = this.lieDownAmountTail;
/* 358 */     if (isLying()) {
/* 359 */       this.lieDownAmount = Math.min(1.0F, this.lieDownAmount + 0.15F);
/* 360 */       this.lieDownAmountTail = Math.min(1.0F, this.lieDownAmountTail + 0.08F);
/*     */     } else {
/* 362 */       this.lieDownAmount = Math.max(0.0F, this.lieDownAmount - 0.22F);
/* 363 */       this.lieDownAmountTail = Math.max(0.0F, this.lieDownAmountTail - 0.13F);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateRelaxStateOneAmount() {
/* 368 */     this.relaxStateOneAmountO = this.relaxStateOneAmount;
/* 369 */     if (isRelaxStateOne()) {
/* 370 */       this.relaxStateOneAmount = Math.min(1.0F, this.relaxStateOneAmount + 0.1F);
/*     */     } else {
/* 372 */       this.relaxStateOneAmount = Math.max(0.0F, this.relaxStateOneAmount - 0.13F);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getLieDownAmount(float a) {
/* 377 */     return Mth.lerp(a, this.lieDownAmountO, this.lieDownAmount);
/*     */   }
/*     */   
/*     */   public float getLieDownAmountTail(float a) {
/* 381 */     return Mth.lerp(a, this.lieDownAmountOTail, this.lieDownAmountTail);
/*     */   }
/*     */   
/*     */   public float getRelaxStateOneAmount(float a) {
/* 385 */     return Mth.lerp(a, this.relaxStateOneAmountO, this.relaxStateOneAmount);
/*     */   }
/*     */   
/*     */   public Cat getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 390 */     Cat baby = (Cat)EntityTypes.CAT.create((Level)level, EntitySpawnReason.BREEDING);
/* 391 */     if (baby != null && partner instanceof Cat) {
/* 391 */       Cat partnerCat = (Cat)partner;
/* 392 */       if (this.random.nextBoolean()) {
/* 393 */         baby.setVariant(getVariant());
/*     */       } else {
/* 395 */         baby.setVariant(partnerCat.getVariant());
/*     */       } 
/* 398 */       if (isTame()) {
/* 399 */         baby.setOwnerReference(getOwnerReference());
/* 400 */         baby.setTame(true, true);
/* 401 */         DyeColor parent1CollarColor = getCollarColor();
/* 402 */         DyeColor parent2CollarColor = partnerCat.getCollarColor();
/* 403 */         baby.setCollarColor(DyeColor.getMixedColor(level, parent1CollarColor, parent2CollarColor));
/*     */       } 
/*     */     } 
/* 407 */     return baby;
/*     */   }
/*     */   
/*     */   public boolean canMate(Animal partner) {
/*     */     Cat cat;
/* 412 */     if (!isTame())
/* 413 */       return false; 
/* 416 */     if (partner instanceof Cat) {
/* 416 */       cat = (Cat)partner;
/*     */     } else {
/* 417 */       return false;
/*     */     } 
/* 420 */     return (cat.isTame() && super.canMate(partner));
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 425 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/* 426 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.CAT_VARIANT).ifPresent(this::setVariant);
/* 427 */     setSoundVariant(CatSoundVariants.pickRandomSoundVariant(registryAccess(), level.getRandom()));
/* 429 */     return groupData;
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 434 */     ItemStack itemStack = player.getItemInHand(hand);
/* 436 */     if (isTame()) {
/* 437 */       if (isOwnedBy((LivingEntity)player)) {
/* 438 */         if (itemStack.is(ItemTags.CAT_COLLAR_DYES)) {
/* 439 */           DyeColor color = (DyeColor)itemStack.get(DataComponents.DYE);
/* 440 */           if (color != null && color != getCollarColor()) {
/* 441 */             if (!level().isClientSide()) {
/* 442 */               setCollarColor(color);
/* 443 */               itemStack.consume(1, (LivingEntity)player);
/* 444 */               setPersistenceRequired();
/*     */             } 
/* 446 */             return (InteractionResult)InteractionResult.SUCCESS;
/*     */           } 
/* 448 */         } else if (isFood(itemStack) && getHealth() < getMaxHealth()) {
/* 449 */           if (!level().isClientSide())
/* 450 */             feed(player, hand, itemStack, 1.0F, 1.0F); 
/* 452 */           return (InteractionResult)InteractionResult.SUCCESS;
/*     */         } 
/* 455 */         InteractionResult parentInteraction = super.mobInteract(player, hand);
/* 456 */         if (!parentInteraction.consumesAction()) {
/* 457 */           setOrderedToSit(!isOrderedToSit());
/* 458 */           return (InteractionResult)InteractionResult.SUCCESS;
/*     */         } 
/* 460 */         return parentInteraction;
/*     */       } 
/* 463 */     } else if (isFood(itemStack)) {
/* 464 */       if (!level().isClientSide()) {
/* 465 */         usePlayerItem(player, hand, itemStack);
/* 466 */         tryToTame(player);
/* 467 */         setPersistenceRequired();
/* 468 */         playEatingSound();
/*     */       } 
/* 470 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 473 */     InteractionResult interact = super.mobInteract(player, hand);
/* 474 */     if (interact.consumesAction())
/* 475 */       setPersistenceRequired(); 
/* 477 */     return interact;
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 482 */     return itemStack.is(ItemTags.CAT_FOOD);
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 487 */     return (!isTame() && this.tickCount > 2400);
/*     */   }
/*     */   
/*     */   public void setTame(boolean isTame, boolean includeSideEffects) {
/* 492 */     super.setTame(isTame, includeSideEffects);
/* 493 */     reassessTameGoals();
/*     */   }
/*     */   
/*     */   protected void reassessTameGoals() {
/* 497 */     if (this.avoidPlayersGoal == null)
/* 498 */       this.avoidPlayersGoal = new CatAvoidEntityGoal<>(this, Player.class, 16.0F, 0.8D, 1.33D); 
/* 501 */     this.goalSelector.removeGoal((Goal)this.avoidPlayersGoal);
/* 503 */     if (!isTame())
/* 504 */       this.goalSelector.addGoal(4, (Goal)this.avoidPlayersGoal); 
/*     */   }
/*     */   
/*     */   private void tryToTame(Player player) {
/* 509 */     if (this.random.nextInt(3) == 0) {
/* 510 */       tame(player);
/* 511 */       setOrderedToSit(true);
/* 512 */       level().broadcastEntityEvent((Entity)this, (byte)7);
/*     */     } else {
/* 514 */       level().broadcastEntityEvent((Entity)this, (byte)6);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isSteppingCarefully() {
/* 520 */     return (isCrouching() || super.isSteppingCarefully());
/*     */   }
/*     */   
/*     */   private static class CatAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
/*     */     private final Cat cat;
/*     */     
/*     */     public CatAvoidEntityGoal(Cat cat, Class<T> avoidClass, float maxDist, double walkSpeedModifier, double sprintSpeedModifier) {
/* 527 */       super((PathfinderMob)cat, avoidClass, maxDist, walkSpeedModifier, sprintSpeedModifier, EntitySelector.NO_CREATIVE_OR_SPECTATOR);
/* 528 */       this.cat = cat;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 533 */       return (!this.cat.isTame() && super.canUse());
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 538 */       return (!this.cat.isTame() && super.canContinueToUse());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CatTemptGoal extends TemptGoal {
/*     */     private Player selectedPlayer;
/*     */     
/*     */     private final Cat cat;
/*     */     
/*     */     public CatTemptGoal(Cat mob, double speedModifier, Predicate<ItemStack> items, boolean canScare) {
/* 547 */       super((PathfinderMob)mob, speedModifier, items, canScare);
/* 548 */       this.cat = mob;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 553 */       super.tick();
/* 555 */       if (this.selectedPlayer == null && this.mob.getRandom().nextInt(adjustedTickDelay(600)) == 0) {
/* 556 */         this.selectedPlayer = this.player;
/* 557 */       } else if (this.mob.getRandom().nextInt(adjustedTickDelay(500)) == 0) {
/* 558 */         this.selectedPlayer = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     protected boolean canScare() {
/* 564 */       if (this.selectedPlayer != null && this.selectedPlayer.equals(this.player))
/* 565 */         return false; 
/* 568 */       return super.canScare();
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 573 */       return (super.canUse() && !this.cat.isTame());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CatRelaxOnOwnerGoal extends Goal {
/*     */     private final Cat cat;
/*     */     
/*     */     private Player ownerPlayer;
/*     */     
/*     */     private BlockPos goalPos;
/*     */     
/*     */     private int onBedTicks;
/*     */     
/*     */     public CatRelaxOnOwnerGoal(Cat cat) {
/* 584 */       this.cat = cat;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 589 */       if (!this.cat.isTame())
/* 590 */         return false; 
/* 593 */       if (this.cat.isOrderedToSit())
/* 594 */         return false; 
/* 597 */       LivingEntity owner = this.cat.getOwner();
/* 598 */       if (owner instanceof Player) {
/* 598 */         Player playerOwner = (Player)owner;
/* 599 */         this.ownerPlayer = playerOwner;
/* 601 */         if (!owner.isSleeping())
/* 602 */           return false; 
/* 605 */         if (this.cat.distanceToSqr((Entity)this.ownerPlayer) > 100.0D)
/* 606 */           return false; 
/* 609 */         BlockPos ownerPos = this.ownerPlayer.blockPosition();
/* 610 */         BlockState ownerPosState = this.cat.level().getBlockState(ownerPos);
/* 611 */         if (ownerPosState.is(BlockTags.BEDS)) {
/* 612 */           this.goalPos = ownerPosState.getOptionalValue((Property)BedBlock.FACING).map(bedDir -> ownerPos.relative(bedDir.getOpposite())).orElseGet(() -> new BlockPos((Vec3i)ownerPos));
/* 613 */           return !spaceIsOccupied();
/*     */         } 
/*     */       } 
/* 616 */       return false;
/*     */     }
/*     */     
/*     */     private boolean spaceIsOccupied() {
/* 620 */       List<Cat> cats = this.cat.level().getEntitiesOfClass(Cat.class, (new AABB(this.goalPos)).inflate(2.0D));
/* 621 */       for (Cat otherCat : cats) {
/* 622 */         if (otherCat != this.cat && (otherCat.isLying() || otherCat.isRelaxStateOne()))
/* 623 */           return true; 
/*     */       } 
/* 626 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 631 */       return (this.cat.isTame() && !this.cat.isOrderedToSit() && this.ownerPlayer != null && this.ownerPlayer.isSleeping() && this.goalPos != null && !spaceIsOccupied());
/*     */     }
/*     */     
/*     */     public void start() {
/* 636 */       if (this.goalPos != null) {
/* 637 */         this.cat.setInSittingPose(false);
/* 638 */         this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void stop() {
/* 644 */       this.cat.setLying(false);
/* 646 */       if (this.ownerPlayer.getSleepTimer() >= 100 && this.cat
/* 647 */         .level().getRandom().nextFloat() < ((Float)this.cat.level().environmentAttributes().getValue(EnvironmentAttributes.CAT_WAKING_UP_GIFT_CHANCE, this.cat.position())).floatValue())
/* 648 */         giveMorningGift(); 
/* 651 */       this.onBedTicks = 0;
/* 652 */       this.cat.setRelaxStateOne(false);
/* 653 */       this.cat.getNavigation().stop();
/*     */     }
/*     */     
/*     */     private void giveMorningGift() {
/* 657 */       RandomSource random = this.cat.getRandom();
/* 658 */       BlockPos.MutableBlockPos catPos = new BlockPos.MutableBlockPos();
/* 659 */       catPos.set(this.cat.isLeashed() ? (Vec3i)this.cat.getLeashHolder().blockPosition() : (Vec3i)this.cat.blockPosition());
/* 660 */       this.cat.randomTeleport((catPos.getX() + random.nextInt(11) - 5), (catPos.getY() + random.nextInt(5) - 2), (catPos.getZ() + random.nextInt(11) - 5), false);
/* 662 */       catPos.set((Vec3i)this.cat.blockPosition());
/* 663 */       this.cat.dropFromGiftLootTable(getServerLevel((Entity)this.cat), BuiltInLootTables.CAT_MORNING_GIFT, (level, itemStack) -> level.addFreshEntity((Entity)new ItemEntity((Level)level, catPos.getX() - Mth.sin((this.cat.yBodyRot * 0.017453292F)), catPos.getY(), catPos.getZ() + Mth.cos((this.cat.yBodyRot * 0.017453292F)), itemStack)));
/*     */     }
/*     */     
/*     */     public void tick() {
/* 670 */       if (this.ownerPlayer != null && this.goalPos != null) {
/* 671 */         this.cat.setInSittingPose(false);
/* 672 */         this.cat.getNavigation().moveTo(this.goalPos.getX(), this.goalPos.getY(), this.goalPos.getZ(), 1.100000023841858D);
/* 673 */         if (this.cat.distanceToSqr((Entity)this.ownerPlayer) < 2.5D) {
/* 674 */           this.onBedTicks++;
/* 675 */           if (this.onBedTicks > adjustedTickDelay(16)) {
/* 676 */             this.cat.setLying(true);
/* 677 */             this.cat.setRelaxStateOne(false);
/*     */           } else {
/* 679 */             this.cat.lookAt((Entity)this.ownerPlayer, 45.0F, 45.0F);
/* 680 */             this.cat.setRelaxStateOne(true);
/*     */           } 
/*     */         } else {
/* 683 */           this.cat.setLying(false);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\feline\Cat.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */