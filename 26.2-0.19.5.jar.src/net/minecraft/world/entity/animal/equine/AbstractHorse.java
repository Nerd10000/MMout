/*      */ package net.minecraft.world.entity.animal.equine;
/*      */ 
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import java.util.function.DoubleSupplier;
/*      */ import java.util.function.IntUnaryOperator;
/*      */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.core.particles.SimpleParticleType;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.SimpleContainer;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityDimensions;
/*      */ import net.minecraft.world.entity.EntityReference;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.Leashable;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.OwnableEntity;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.PlayerRideableJumping;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.entity.SpawnGroupData;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*      */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RandomStandGoal;
/*      */ import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
/*      */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*      */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*      */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*      */ import net.minecraft.world.entity.animal.Animal;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*      */ import net.minecraft.world.inventory.AbstractMountInventoryMenu;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.level.CollisionGetter;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public abstract class AbstractHorse extends Animal implements PlayerRideableJumping, HasCustomInventoryScreen, OwnableEntity {
/*      */   public static final int CHEST_SLOT_OFFSET = 499;
/*      */   
/*      */   public static final int INVENTORY_SLOT_OFFSET = 500;
/*      */   
/*      */   public static final double BREEDING_CROSS_FACTOR = 0.15D;
/*      */   
/*   88 */   private static final float MIN_MOVEMENT_SPEED = (float)generateSpeed(() -> 0.0D);
/*      */   
/*   89 */   private static final float MAX_MOVEMENT_SPEED = (float)generateSpeed(() -> 1.0D);
/*      */   
/*   90 */   private static final float MIN_JUMP_STRENGTH = (float)generateJumpStrength(() -> 0.0D);
/*      */   
/*   91 */   private static final float MAX_JUMP_STRENGTH = (float)generateJumpStrength(() -> 1.0D);
/*      */   
/*   92 */   private static final float MIN_HEALTH = generateMaxHealth(i -> 0);
/*      */   
/*      */   private static final float MAX_HEALTH;
/*      */   
/*      */   private static final float BACKWARDS_MOVE_SPEED_FACTOR = 0.25F;
/*      */   
/*      */   private static final float SIDEWAYS_MOVE_SPEED_FACTOR = 0.5F;
/*      */   
/*      */   private static final TargetingConditions.Selector PARENT_HORSE_SELECTOR;
/*      */   
/*      */   static {
/*   93 */     MAX_HEALTH = generateMaxHealth(i -> i - 1);
/*   98 */     PARENT_HORSE_SELECTOR = ((target, level) -> {
/*      */         if (target instanceof AbstractHorse) {
/*      */           AbstractHorse horse = (AbstractHorse)target;
/*      */           if (horse.isBred());
/*      */         } 
/*      */         return false;
/*      */       });
/*      */   }
/*      */   
/*   99 */   private static final TargetingConditions MOMMY_TARGETING = TargetingConditions.forNonCombat().range(16.0D).ignoreLineOfSight().selector(PARENT_HORSE_SELECTOR);
/*      */   
/*  101 */   private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(AbstractHorse.class, EntityDataSerializers.BYTE);
/*      */   
/*      */   private static final int FLAG_TAME = 2;
/*      */   
/*      */   private static final int FLAG_BRED = 8;
/*      */   
/*      */   private static final int FLAG_EATING = 16;
/*      */   
/*      */   private static final int FLAG_STANDING = 32;
/*      */   
/*      */   private static final int FLAG_OPEN_MOUTH = 64;
/*      */   
/*      */   protected static final float BABY_SCALE = 0.7F;
/*      */   
/*      */   public static final int INVENTORY_ROWS = 3;
/*      */   
/*      */   private static final int DEFAULT_TEMPER = 0;
/*      */   
/*      */   private static final boolean DEFAULT_EATING_HAYSTACK = false;
/*      */   
/*      */   private static final boolean DEFAULT_BRED = false;
/*      */   
/*      */   private static final boolean DEFAULT_TAME = false;
/*      */   
/*      */   private int eatingCounter;
/*      */   
/*      */   private int mouthCounter;
/*      */   
/*      */   private int standCounter;
/*      */   
/*      */   public int tailCounter;
/*      */   
/*      */   public int sprintCounter;
/*      */   
/*      */   protected SimpleContainer inventory;
/*      */   
/*  125 */   protected int temper = 0;
/*      */   
/*      */   protected float playerJumpPendingScale;
/*      */   
/*      */   protected boolean allowStandSliding;
/*      */   
/*      */   private float eatAnim;
/*      */   
/*      */   private float eatAnimO;
/*      */   
/*      */   private float standAnim;
/*      */   
/*      */   private float standAnimO;
/*      */   
/*      */   private float mouthAnim;
/*      */   
/*      */   private float mouthAnimO;
/*      */   
/*      */   protected boolean canGallop = true;
/*      */   
/*      */   protected int gallopSoundCounter;
/*      */   
/*      */   private EntityReference<LivingEntity> owner;
/*      */   
/*      */   protected AbstractHorse(EntityType<? extends AbstractHorse> type, Level level) {
/*  141 */     super(type, level);
/*  143 */     createInventory();
/*      */   }
/*      */   
/*      */   protected void registerGoals() {
/*  148 */     this.goalSelector.addGoal(1, (Goal)new RunAroundLikeCrazyGoal(this, 1.2D));
/*  149 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D, AbstractHorse.class));
/*  150 */     this.goalSelector.addGoal(4, (Goal)new FollowParentGoal(this, 1.0D));
/*  151 */     this.goalSelector.addGoal(6, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 0.7D));
/*  152 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  153 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*  154 */     if (canPerformRearing())
/*  155 */       this.goalSelector.addGoal(9, (Goal)new RandomStandGoal(this)); 
/*  158 */     addBehaviourGoals();
/*      */   }
/*      */   
/*      */   protected void addBehaviourGoals() {
/*  162 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  163 */     this.goalSelector.addGoal(1, (Goal)new MountPanicGoal(1.2D));
/*  164 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.25D, i -> i.is(ItemTags.HORSE_TEMPT_ITEMS), false));
/*      */   }
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  169 */     super.defineSynchedData(entityData);
/*  170 */     entityData.define(DATA_ID_FLAGS, Byte.valueOf((byte)0));
/*      */   }
/*      */   
/*      */   protected boolean getFlag(int flag) {
/*  174 */     return ((((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue() & flag) != 0);
/*      */   }
/*      */   
/*      */   protected void setFlag(int flag, boolean value) {
/*  178 */     byte current = ((Byte)this.entityData.get(DATA_ID_FLAGS)).byteValue();
/*  179 */     if (value) {
/*  180 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(current | flag)));
/*      */     } else {
/*  182 */       this.entityData.set(DATA_ID_FLAGS, Byte.valueOf((byte)(current & (flag ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isTamed() {
/*  187 */     return getFlag(2);
/*      */   }
/*      */   
/*      */   public EntityReference<LivingEntity> getOwnerReference() {
/*  192 */     return this.owner;
/*      */   }
/*      */   
/*      */   public void setOwner(LivingEntity owner) {
/*  196 */     this.owner = EntityReference.of((UniquelyIdentifyable)owner);
/*      */   }
/*      */   
/*      */   public void setTamed(boolean flag) {
/*  200 */     setFlag(2, flag);
/*      */   }
/*      */   
/*      */   public void onElasticLeashPull() {
/*  205 */     super.onElasticLeashPull();
/*  206 */     if (isEating())
/*  207 */       setEating(false); 
/*      */   }
/*      */   
/*      */   public boolean supportQuadLeash() {
/*  213 */     return true;
/*      */   }
/*      */   
/*      */   public Vec3[] getQuadLeashOffsets() {
/*  218 */     return Leashable.createQuadLeashOffsets((Entity)this, 0.04D, 0.52D, 0.23D, 0.87D);
/*      */   }
/*      */   
/*      */   public boolean isEating() {
/*  222 */     return getFlag(16);
/*      */   }
/*      */   
/*      */   public boolean isStanding() {
/*  226 */     return getFlag(32);
/*      */   }
/*      */   
/*      */   public boolean isBred() {
/*  230 */     return getFlag(8);
/*      */   }
/*      */   
/*      */   public void setBred(boolean flag) {
/*  234 */     setFlag(8, flag);
/*      */   }
/*      */   
/*      */   public boolean canUseSlot(EquipmentSlot slot) {
/*  239 */     if (slot == EquipmentSlot.SADDLE)
/*  240 */       return (isAlive() && !isBaby() && isTamed()); 
/*  242 */     return super.canUseSlot(slot);
/*      */   }
/*      */   
/*      */   public void equipBodyArmor(Player player, ItemStack itemStack) {
/*  246 */     if (isEquippableInSlot(itemStack, EquipmentSlot.BODY))
/*  247 */       setItemSlotAndDropWhenKilled(EquipmentSlot.BODY, itemStack.consumeAndReturn(1, (LivingEntity)player)); 
/*      */   }
/*      */   
/*      */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/*  253 */     return (((slot == EquipmentSlot.BODY || slot == EquipmentSlot.SADDLE) && isTamed()) || super.canDispenserEquipIntoSlot(slot));
/*      */   }
/*      */   
/*      */   public int getTemper() {
/*  257 */     return this.temper;
/*      */   }
/*      */   
/*      */   public void setTemper(int temper) {
/*  261 */     this.temper = temper;
/*      */   }
/*      */   
/*      */   public int modifyTemper(int amount) {
/*  265 */     int temper = Mth.clamp(getTemper() + amount, 0, getMaxTemper());
/*  267 */     setTemper(temper);
/*  268 */     return temper;
/*      */   }
/*      */   
/*      */   public boolean isPushable() {
/*  273 */     return !isVehicle();
/*      */   }
/*      */   
/*      */   private void eating() {
/*  277 */     openMouth();
/*  278 */     if (!isSilent()) {
/*  279 */       SoundEvent eatingSound = getEatingSound();
/*  280 */       if (eatingSound != null)
/*  281 */         level().playSound(null, getX(), getY(), getZ(), eatingSound, getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/*  288 */     if (fallDistance > 1.0D)
/*  289 */       playSound(isBaby() ? SoundEvents.HORSE_LAND_BABY : SoundEvents.HORSE_LAND, 0.4F, 1.0F); 
/*  292 */     int dmg = calculateFallDamage(fallDistance, damageModifier);
/*  293 */     if (dmg <= 0)
/*  294 */       return false; 
/*  297 */     hurt(damageSource, dmg);
/*  298 */     propagateFallToPassengers(fallDistance, damageModifier, damageSource);
/*  300 */     playBlockFallSound();
/*  301 */     return true;
/*      */   }
/*      */   
/*      */   public final int getInventorySize() {
/*  305 */     return AbstractMountInventoryMenu.getInventorySize(getInventoryColumns());
/*      */   }
/*      */   
/*      */   protected void createInventory() {
/*  309 */     SimpleContainer old = this.inventory;
/*  310 */     this.inventory = new SimpleContainer(getInventorySize());
/*  311 */     if (old != null) {
/*  312 */       int max = Math.min(old.getContainerSize(), this.inventory.getContainerSize());
/*  313 */       for (int slot = 0; slot < max; slot++) {
/*  314 */         ItemStack itemStack = old.getItem(slot);
/*  315 */         if (!itemStack.isEmpty())
/*  316 */           this.inventory.setItem(slot, itemStack.copy()); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/*  324 */     if (slot == EquipmentSlot.SADDLE)
/*  325 */       return (Holder<SoundEvent>)SoundEvents.HORSE_SADDLE; 
/*  327 */     return super.getEquipSound(slot, stack, equippable);
/*      */   }
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  332 */     boolean wasHurt = super.hurtServer(level, source, damage);
/*  333 */     if (wasHurt && this.random.nextInt(3) == 0)
/*  334 */       standIfPossible(); 
/*  336 */     return wasHurt;
/*      */   }
/*      */   
/*      */   protected boolean canPerformRearing() {
/*  340 */     return true;
/*      */   }
/*      */   
/*      */   protected SoundEvent getEatingSound() {
/*  344 */     return null;
/*      */   }
/*      */   
/*      */   protected SoundEvent getAngrySound() {
/*  348 */     return null;
/*      */   }
/*      */   
/*      */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/*  353 */     if (blockState.liquid())
/*      */       return; 
/*  357 */     BlockState aboveState = level().getBlockState(pos.above());
/*  358 */     SoundType soundType = blockState.getSoundType();
/*  359 */     if (aboveState.is(Blocks.SNOW))
/*  360 */       soundType = aboveState.getSoundType(); 
/*  363 */     if (isVehicle() && this.canGallop) {
/*  364 */       this.gallopSoundCounter++;
/*  365 */       if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
/*  366 */         playGallopSound(soundType);
/*  367 */       } else if (this.gallopSoundCounter <= 5) {
/*  368 */         playSound(SoundEvents.HORSE_STEP_WOOD, soundType.getVolume() * 0.15F, soundType.getPitch());
/*      */       } 
/*  370 */     } else if (isWoodSoundType(soundType)) {
/*  371 */       playSound(SoundEvents.HORSE_STEP_WOOD, soundType.getVolume() * 0.15F, soundType.getPitch());
/*      */     } else {
/*  373 */       playSound(isBaby() ? SoundEvents.HORSE_STEP_BABY : SoundEvents.HORSE_STEP, soundType.getVolume() * 0.15F, soundType.getPitch());
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isWoodSoundType(SoundType soundType) {
/*  378 */     return (soundType == SoundType.WOOD || soundType == SoundType.NETHER_WOOD || soundType == SoundType.STEM || soundType == SoundType.CHERRY_WOOD || soundType == SoundType.BAMBOO_WOOD);
/*      */   }
/*      */   
/*      */   protected void playGallopSound(SoundType soundType) {
/*  383 */     playSound(SoundEvents.HORSE_GALLOP, soundType.getVolume() * 0.15F, soundType.getPitch());
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createBaseHorseAttributes() {
/*  387 */     return Animal.createAnimalAttributes()
/*  388 */       .add(Attributes.JUMP_STRENGTH, 0.7D)
/*  389 */       .add(Attributes.MAX_HEALTH, 53.0D)
/*  390 */       .add(Attributes.MOVEMENT_SPEED, 0.22499999403953552D)
/*  391 */       .add(Attributes.STEP_HEIGHT, 1.0D)
/*  392 */       .add(Attributes.SAFE_FALL_DISTANCE, 6.0D)
/*  393 */       .add(Attributes.FALL_DAMAGE_MULTIPLIER, 0.5D);
/*      */   }
/*      */   
/*      */   public int getMaxSpawnClusterSize() {
/*  398 */     return 6;
/*      */   }
/*      */   
/*      */   public int getMaxTemper() {
/*  402 */     return 100;
/*      */   }
/*      */   
/*      */   protected float getSoundVolume() {
/*  407 */     return 0.8F;
/*      */   }
/*      */   
/*      */   public int getAmbientSoundInterval() {
/*  412 */     return 400;
/*      */   }
/*      */   
/*      */   public void openCustomInventoryScreen(Player player) {
/*  418 */     if (!level().isClientSide() && (!isVehicle() || hasPassenger((Entity)player)) && isTamed())
/*  419 */       player.openHorseInventory(this, (Container)this.inventory); 
/*      */   }
/*      */   
/*      */   public InteractionResult fedFood(Player player, ItemStack itemStack) {
/*  425 */     boolean ateFood = handleEating(player, itemStack);
/*  426 */     if (ateFood)
/*  427 */       itemStack.consume(1, (LivingEntity)player); 
/*  429 */     return (ateFood || level().isClientSide()) ? (InteractionResult)InteractionResult.SUCCESS_SERVER : (InteractionResult)InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   protected boolean handleEating(Player player, ItemStack itemStack) {
/*  433 */     boolean itemUsed = false;
/*  434 */     float heal = 0.0F;
/*  435 */     int ageUp = 0;
/*  436 */     int temper = 0;
/*  438 */     if (itemStack.is(Items.WHEAT)) {
/*  439 */       heal = 2.0F;
/*  440 */       ageUp = 20;
/*  441 */       temper = 3;
/*  442 */     } else if (itemStack.is(Items.SUGAR)) {
/*  443 */       heal = 1.0F;
/*  444 */       ageUp = 30;
/*  445 */       temper = 3;
/*  446 */     } else if (itemStack.is(Items.HAY_BLOCK)) {
/*  447 */       heal = 20.0F;
/*  448 */       ageUp = 180;
/*  449 */     } else if (itemStack.is(Items.APPLE)) {
/*  450 */       heal = 3.0F;
/*  451 */       ageUp = 60;
/*  452 */       temper = 3;
/*  453 */     } else if (itemStack.is(Items.RED_MUSHROOM)) {
/*  454 */       heal = 3.0F;
/*  455 */       ageUp = 0;
/*  456 */       temper = 3;
/*  457 */     } else if (itemStack.is(Items.CARROT)) {
/*  458 */       heal = 3.0F;
/*  459 */       ageUp = 60;
/*  460 */       temper = 3;
/*  461 */     } else if (itemStack.is(Items.GOLDEN_CARROT)) {
/*  462 */       heal = 4.0F;
/*  463 */       ageUp = 60;
/*  464 */       temper = 5;
/*  465 */       if (!level().isClientSide() && isTamed() && getAge() == 0 && !isInLove()) {
/*  466 */         itemUsed = true;
/*  467 */         setInLove(player);
/*      */       } 
/*  469 */     } else if (itemStack.is(Items.GOLDEN_APPLE) || itemStack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
/*  470 */       heal = 10.0F;
/*  471 */       ageUp = 240;
/*  472 */       temper = 10;
/*  473 */       if (!level().isClientSide() && isTamed() && getAge() == 0 && !isInLove()) {
/*  474 */         itemUsed = true;
/*  475 */         setInLove(player);
/*      */       } 
/*      */     } 
/*  478 */     if (getHealth() < getMaxHealth() && heal > 0.0F) {
/*  479 */       heal(heal);
/*  480 */       itemUsed = true;
/*      */     } 
/*  482 */     if (isBaby() && ageUp > 0 && !isAgeLocked()) {
/*  483 */       level().addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/*  484 */       if (!level().isClientSide()) {
/*  485 */         ageUp(ageUp);
/*  486 */         itemUsed = true;
/*      */       } 
/*      */     } 
/*  489 */     if (temper > 0 && (itemUsed || !isTamed()) && getTemper() < getMaxTemper() && 
/*  490 */       !level().isClientSide()) {
/*  491 */       modifyTemper(temper);
/*  492 */       itemUsed = true;
/*      */     } 
/*  495 */     if (itemUsed) {
/*  496 */       eating();
/*  497 */       gameEvent((Holder)GameEvent.EAT);
/*      */     } 
/*  499 */     return itemUsed;
/*      */   }
/*      */   
/*      */   protected void doPlayerRide(Player player) {
/*  503 */     setEating(false);
/*  504 */     clearStanding();
/*  505 */     if (!level().isClientSide()) {
/*  506 */       player.setYRot(getYRot());
/*  507 */       player.setXRot(getXRot());
/*  508 */       player.startRiding((Entity)this);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isImmobile() {
/*  514 */     return ((super.isImmobile() && isVehicle() && isSaddled()) || isEating() || isStanding());
/*      */   }
/*      */   
/*      */   public boolean isFood(ItemStack itemStack) {
/*  521 */     return itemStack.is(ItemTags.HORSE_FOOD);
/*      */   }
/*      */   
/*      */   private void moveTail() {
/*  525 */     this.tailCounter = 1;
/*      */   }
/*      */   
/*      */   protected void dropEquipment(ServerLevel level) {
/*  530 */     super.dropEquipment(level);
/*  531 */     if (this.inventory == null)
/*      */       return; 
/*  534 */     for (int i = 0; i < this.inventory.getContainerSize(); i++) {
/*  535 */       ItemStack itemStack = this.inventory.getItem(i);
/*  536 */       if (!itemStack.isEmpty() && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP))
/*  539 */         spawnAtLocation(level, itemStack); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void aiStep() {
/*  545 */     if (this.random.nextInt(200) == 0)
/*  546 */       moveTail(); 
/*  549 */     super.aiStep();
/*  551 */     Level level = level();
/*  551 */     if (level instanceof ServerLevel) {
/*  551 */       ServerLevel serverLevel = (ServerLevel)level;
/*  551 */       if (isAlive()) {
/*  555 */         if (this.random.nextInt(900) == 0 && this.deathTime == 0)
/*  556 */           heal(1.0F); 
/*  559 */         if (canEatGrass()) {
/*  560 */           if (!isEating() && !isVehicle() && this.random.nextInt(300) == 0 && 
/*  561 */             serverLevel.getBlockState(blockPosition().below()).is(Blocks.GRASS_BLOCK))
/*  562 */             setEating(true); 
/*  566 */           if (isEating() && ++this.eatingCounter > 50) {
/*  567 */             this.eatingCounter = 0;
/*  568 */             setEating(false);
/*      */           } 
/*      */         } 
/*  572 */         followMommy(serverLevel);
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void followMommy(ServerLevel level) {
/*  576 */     if (isBred() && isBaby() && !isEating()) {
/*  577 */       LivingEntity mommy = level.getNearestEntity(AbstractHorse.class, MOMMY_TARGETING, (LivingEntity)this, getX(), getY(), getZ(), getBoundingBox().inflate(16.0D));
/*  578 */       if (mommy != null && distanceToSqr((Entity)mommy) > 4.0D)
/*  579 */         this.navigation.createPath((Entity)mommy, 0); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean canEatGrass() {
/*  585 */     return true;
/*      */   }
/*      */   
/*      */   public void tick() {
/*  590 */     super.tick();
/*  592 */     if (this.mouthCounter > 0 && ++this.mouthCounter > 30) {
/*  593 */       this.mouthCounter = 0;
/*  594 */       setFlag(64, false);
/*      */     } 
/*  597 */     if (this.standCounter > 0 && 
/*  598 */       --this.standCounter <= 0)
/*  599 */       clearStanding(); 
/*  603 */     if (this.tailCounter > 0 && ++this.tailCounter > 8)
/*  604 */       this.tailCounter = 0; 
/*  607 */     if (this.sprintCounter > 0) {
/*  608 */       this.sprintCounter++;
/*  610 */       if (this.sprintCounter > 300)
/*  611 */         this.sprintCounter = 0; 
/*      */     } 
/*  615 */     this.eatAnimO = this.eatAnim;
/*  616 */     if (isEating()) {
/*  617 */       this.eatAnim += (1.0F - this.eatAnim) * 0.4F + 0.05F;
/*  618 */       if (this.eatAnim > 1.0F)
/*  619 */         this.eatAnim = 1.0F; 
/*      */     } else {
/*  622 */       this.eatAnim += (0.0F - this.eatAnim) * 0.4F - 0.05F;
/*  623 */       if (this.eatAnim < 0.0F)
/*  624 */         this.eatAnim = 0.0F; 
/*      */     } 
/*  627 */     this.standAnimO = this.standAnim;
/*  628 */     if (isStanding()) {
/*  630 */       this.eatAnim = 0.0F;
/*  631 */       this.eatAnimO = this.eatAnim;
/*  632 */       this.standAnim += (1.0F - this.standAnim) * 0.4F + 0.05F;
/*  633 */       if (this.standAnim > 1.0F)
/*  634 */         this.standAnim = 1.0F; 
/*      */     } else {
/*  637 */       this.allowStandSliding = false;
/*  639 */       this.standAnim += (0.8F * this.standAnim * this.standAnim * this.standAnim - this.standAnim) * 0.6F - 0.05F;
/*  640 */       if (this.standAnim < 0.0F)
/*  641 */         this.standAnim = 0.0F; 
/*      */     } 
/*  644 */     this.mouthAnimO = this.mouthAnim;
/*  645 */     if (getFlag(64)) {
/*  646 */       this.mouthAnim += (1.0F - this.mouthAnim) * 0.7F + 0.05F;
/*  647 */       if (this.mouthAnim > 1.0F)
/*  648 */         this.mouthAnim = 1.0F; 
/*      */     } else {
/*  651 */       this.mouthAnim += (0.0F - this.mouthAnim) * 0.7F - 0.05F;
/*  652 */       if (this.mouthAnim < 0.0F)
/*  653 */         this.mouthAnim = 0.0F; 
/*      */     } 
/*      */   }
/*      */   
/*      */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  660 */     if (isVehicle() || isBaby())
/*  661 */       return super.mobInteract(player, hand); 
/*  663 */     if (isTamed() && player.isSecondaryUseActive()) {
/*  664 */       openCustomInventoryScreen(player);
/*  665 */       return (InteractionResult)InteractionResult.SUCCESS;
/*      */     } 
/*  668 */     ItemStack itemStack = player.getItemInHand(hand);
/*  669 */     if (!itemStack.isEmpty()) {
/*  670 */       InteractionResult interactionResult = itemStack.interactLivingEntity(player, (LivingEntity)this, hand);
/*  671 */       if (interactionResult.consumesAction())
/*  672 */         return interactionResult; 
/*  675 */       if (isEquippableInSlot(itemStack, EquipmentSlot.BODY) && !isWearingBodyArmor()) {
/*  676 */         equipBodyArmor(player, itemStack);
/*  677 */         return (InteractionResult)InteractionResult.SUCCESS;
/*      */       } 
/*      */     } 
/*  681 */     doPlayerRide(player);
/*  682 */     return (InteractionResult)InteractionResult.SUCCESS;
/*      */   }
/*      */   
/*      */   private void openMouth() {
/*  686 */     if (!level().isClientSide()) {
/*  687 */       this.mouthCounter = 1;
/*  688 */       setFlag(64, true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setEating(boolean flag) {
/*  693 */     setFlag(16, flag);
/*      */   }
/*      */   
/*      */   public void setStanding(int ticks) {
/*  697 */     setEating(false);
/*  698 */     setFlag(32, true);
/*  699 */     this.standCounter = ticks;
/*      */   }
/*      */   
/*      */   public void clearStanding() {
/*  703 */     setFlag(32, false);
/*  704 */     this.standCounter = 0;
/*      */   }
/*      */   
/*      */   public SoundEvent getAmbientStandSound() {
/*  708 */     return getAmbientSound();
/*      */   }
/*      */   
/*      */   public void standIfPossible() {
/*  712 */     if (canPerformRearing() && (isEffectiveAi() || !level().isClientSide()))
/*  713 */       setStanding(20); 
/*      */   }
/*      */   
/*      */   public void makeMad() {
/*  718 */     if (!isStanding() && !level().isClientSide()) {
/*  719 */       standIfPossible();
/*  720 */       makeSound(getAngrySound());
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean tameWithName(Player player) {
/*  725 */     setOwner((LivingEntity)player);
/*  726 */     setTamed(true);
/*  727 */     if (player instanceof ServerPlayer) {
/*  727 */       ServerPlayer serverPlayer = (ServerPlayer)player;
/*  728 */       CriteriaTriggers.TAME_ANIMAL.trigger(serverPlayer, this);
/*      */     } 
/*  730 */     level().broadcastEntityEvent((Entity)this, (byte)7);
/*  731 */     return true;
/*      */   }
/*      */   
/*      */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/*  736 */     super.tickRidden(controller, riddenInput);
/*  737 */     Vec2 rotation = getRiddenRotation((LivingEntity)controller);
/*  738 */     setRot(rotation.y, rotation.x);
/*  739 */     this.yRotO = this.yBodyRot = this.yHeadRot = getYRot();
/*  741 */     if (isLocalInstanceAuthoritative()) {
/*  742 */       if (riddenInput.z <= 0.0D)
/*  743 */         this.gallopSoundCounter = 0; 
/*  746 */       if (onGround()) {
/*  747 */         if (this.playerJumpPendingScale > 0.0F && !isJumping())
/*  748 */           executeRidersJump(this.playerJumpPendingScale, riddenInput); 
/*  750 */         this.playerJumpPendingScale = 0.0F;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Vec2 getRiddenRotation(LivingEntity controller) {
/*  756 */     return new Vec2(controller.getXRot() * 0.5F, controller.getYRot());
/*      */   }
/*      */   
/*      */   protected void addPassenger(Entity passenger) {
/*  761 */     super.addPassenger(passenger);
/*  762 */     passenger.absSnapRotationTo(getViewYRot(0.0F), getViewXRot(0.0F));
/*      */   }
/*      */   
/*      */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/*  767 */     if (onGround() && this.playerJumpPendingScale == 0.0F && isStanding() && !this.allowStandSliding)
/*  768 */       return Vec3.ZERO; 
/*  771 */     float sideways = controller.xxa * 0.5F;
/*  773 */     float forward = controller.zza;
/*  774 */     if (forward <= 0.0F)
/*  775 */       forward *= 0.25F; 
/*  778 */     return new Vec3(sideways, 0.0D, forward);
/*      */   }
/*      */   
/*      */   protected float getRiddenSpeed(Player controller) {
/*  783 */     return (float)getAttributeValue(Attributes.MOVEMENT_SPEED);
/*      */   }
/*      */   
/*      */   protected void executeRidersJump(float amount, Vec3 input) {
/*  787 */     double impulse = getJumpPower(amount);
/*  788 */     Vec3 movement = getDeltaMovement();
/*  789 */     setDeltaMovement(movement.x, impulse, movement.z);
/*  791 */     this.needsSync = true;
/*  793 */     if (input.z > 0.0D) {
/*  794 */       float sin = Mth.sin((getYRot() * 0.017453292F));
/*  795 */       float cos = Mth.cos((getYRot() * 0.017453292F));
/*  797 */       setDeltaMovement(getDeltaMovement().add((-0.4F * sin * amount), 0.0D, (0.4F * cos * amount)));
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void playJumpSound() {
/*  806 */     playSound(SoundEvents.HORSE_JUMP, 0.4F, 1.0F);
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  811 */     super.addAdditionalSaveData(output);
/*  813 */     output.putBoolean("EatingHaystack", isEating());
/*  814 */     output.putBoolean("Bred", isBred());
/*  815 */     output.putInt("Temper", getTemper());
/*  816 */     output.putBoolean("Tame", isTamed());
/*  818 */     EntityReference.store(this.owner, output, "Owner");
/*      */   }
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  823 */     super.readAdditionalSaveData(input);
/*  824 */     setEating(input.getBooleanOr("EatingHaystack", false));
/*  825 */     setBred(input.getBooleanOr("Bred", false));
/*  826 */     setTemper(input.getIntOr("Temper", 0));
/*  827 */     setTamed(input.getBooleanOr("Tame", false));
/*  829 */     this.owner = EntityReference.readWithOldOwnerConversion(input, "Owner", level());
/*      */   }
/*      */   
/*      */   public boolean canMate(Animal partner) {
/*  834 */     return false;
/*      */   }
/*      */   
/*      */   protected boolean canParent() {
/*  838 */     return (!isVehicle() && !isPassenger() && isTamed() && !isBaby() && getHealth() >= getMaxHealth() && isInLove());
/*      */   }
/*      */   
/*      */   public boolean isMobControlled() {
/*  842 */     return false;
/*      */   }
/*      */   
/*      */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  847 */     return null;
/*      */   }
/*      */   
/*      */   protected void setOffspringAttributes(AgeableMob partner, AbstractHorse baby) {
/*  851 */     setOffspringAttribute(partner, baby, Attributes.MAX_HEALTH, MIN_HEALTH, MAX_HEALTH);
/*  852 */     setOffspringAttribute(partner, baby, Attributes.JUMP_STRENGTH, MIN_JUMP_STRENGTH, MAX_JUMP_STRENGTH);
/*  853 */     setOffspringAttribute(partner, baby, Attributes.MOVEMENT_SPEED, MIN_MOVEMENT_SPEED, MAX_MOVEMENT_SPEED);
/*      */   }
/*      */   
/*      */   private void setOffspringAttribute(AgeableMob partner, AbstractHorse baby, Holder<Attribute> attribute, double attributeRangeMin, double attributeRangeMax) {
/*  857 */     double newValue = createOffspringAttribute(getAttributeBaseValue(attribute), partner.getAttributeBaseValue(attribute), attributeRangeMin, attributeRangeMax, this.random);
/*  858 */     baby.getAttribute(attribute).setBaseValue(newValue);
/*      */   }
/*      */   
/*      */   public static double createOffspringAttribute(double parentAValue, double parentBValue, double attributeRangeMin, double attributeRangeMax, RandomSource random) {
/*  862 */     if (attributeRangeMax <= attributeRangeMin)
/*  863 */       throw new IllegalArgumentException("Incorrect range for an attribute"); 
/*  865 */     parentAValue = Mth.clamp(parentAValue, attributeRangeMin, attributeRangeMax);
/*  866 */     parentBValue = Mth.clamp(parentBValue, attributeRangeMin, attributeRangeMax);
/*  868 */     double margin = 0.15D * (attributeRangeMax - attributeRangeMin);
/*  869 */     double range = Math.abs(parentAValue - parentBValue) + margin * 2.0D;
/*  871 */     double average = (parentAValue + parentBValue) / 2.0D;
/*  872 */     double babyQuality = (random.nextDouble() + random.nextDouble() + random.nextDouble()) / 3.0D - 0.5D;
/*  873 */     double newValue = average + range * babyQuality;
/*  876 */     if (newValue > attributeRangeMax) {
/*  877 */       double difference = newValue - attributeRangeMax;
/*  878 */       return attributeRangeMax - difference;
/*      */     } 
/*  880 */     if (newValue < attributeRangeMin) {
/*  881 */       double difference = attributeRangeMin - newValue;
/*  882 */       return attributeRangeMin + difference;
/*      */     } 
/*  884 */     return newValue;
/*      */   }
/*      */   
/*      */   public float getEatAnim(float a) {
/*  888 */     return Mth.lerp(a, this.eatAnimO, this.eatAnim);
/*      */   }
/*      */   
/*      */   public float getStandAnim(float a) {
/*  892 */     return Mth.lerp(a, this.standAnimO, this.standAnim);
/*      */   }
/*      */   
/*      */   public float getMouthAnim(float a) {
/*  896 */     return Mth.lerp(a, this.mouthAnimO, this.mouthAnim);
/*      */   }
/*      */   
/*      */   public void onPlayerJump(int jumpAmount) {
/*  901 */     if (!isSaddled())
/*      */       return; 
/*  905 */     if (jumpAmount < 0) {
/*  906 */       jumpAmount = 0;
/*      */     } else {
/*  908 */       this.allowStandSliding = true;
/*  909 */       standIfPossible();
/*      */     } 
/*  911 */     this.playerJumpPendingScale = getPlayerJumpPendingScale(jumpAmount);
/*      */   }
/*      */   
/*      */   public boolean canJump() {
/*  916 */     return isSaddled();
/*      */   }
/*      */   
/*      */   public void handleStartJump(int jumpScale) {
/*  921 */     this.allowStandSliding = true;
/*  922 */     standIfPossible();
/*  923 */     playJumpSound();
/*      */   }
/*      */   
/*      */   public void handleStopJump() {}
/*      */   
/*      */   protected void spawnTamingParticles(boolean success) {
/*  931 */     SimpleParticleType simpleParticleType = success ? ParticleTypes.HEART : ParticleTypes.SMOKE;
/*  933 */     for (int i = 0; i < 7; i++) {
/*  934 */       double xa = this.random.nextGaussian() * 0.02D;
/*  935 */       double ya = this.random.nextGaussian() * 0.02D;
/*  936 */       double za = this.random.nextGaussian() * 0.02D;
/*  937 */       level().addParticle((ParticleOptions)simpleParticleType, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xa, ya, za);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  943 */     if (id == 7) {
/*  944 */       spawnTamingParticles(true);
/*  945 */     } else if (id == 6) {
/*  946 */       spawnTamingParticles(false);
/*      */     } else {
/*  948 */       super.handleEntityEvent(id);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void positionRider(Entity passenger, Entity.MoveFunction moveFunction) {
/*  954 */     super.positionRider(passenger, moveFunction);
/*  955 */     if (passenger instanceof LivingEntity) {
/*  955 */       LivingEntity livingEntity = (LivingEntity)passenger;
/*  956 */       livingEntity.yBodyRot = this.yBodyRot;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected static float generateMaxHealth(IntUnaryOperator integerByBoundProvider) {
/*  962 */     return 15.0F + integerByBoundProvider.applyAsInt(8) + integerByBoundProvider.applyAsInt(9);
/*      */   }
/*      */   
/*      */   protected static double generateJumpStrength(DoubleSupplier probabilityProvider) {
/*  966 */     return 0.4000000059604645D + probabilityProvider.getAsDouble() * 0.2D + probabilityProvider.getAsDouble() * 0.2D + probabilityProvider.getAsDouble() * 0.2D;
/*      */   }
/*      */   
/*      */   protected static double generateSpeed(DoubleSupplier probabilityProvider) {
/*  970 */     return (0.44999998807907104D + probabilityProvider.getAsDouble() * 0.3D + probabilityProvider.getAsDouble() * 0.3D + probabilityProvider.getAsDouble() * 0.3D) * 0.25D;
/*      */   }
/*      */   
/*      */   public boolean onClimbable() {
/*  975 */     return false;
/*      */   }
/*      */   
/*      */   public SlotAccess getSlot(int slot) {
/*  980 */     int inventorySlot = slot - 500;
/*  981 */     if (inventorySlot >= 0 && inventorySlot < this.inventory.getContainerSize())
/*  982 */       return this.inventory.getSlot(inventorySlot); 
/*  984 */     return super.getSlot(slot);
/*      */   }
/*      */   
/*      */   public LivingEntity getControllingPassenger() {
/*  989 */     if (isSaddled()) {
/*  989 */       Entity entity = getFirstPassenger();
/*  989 */       if (entity instanceof Player) {
/*  989 */         Player passenger = (Player)entity;
/*  990 */         return (LivingEntity)passenger;
/*      */       } 
/*      */     } 
/*  992 */     return super.getControllingPassenger();
/*      */   }
/*      */   
/*      */   private Vec3 getDismountLocationInDirection(Vec3 direction, LivingEntity passenger) {
/*  996 */     double targetX = getX() + direction.x;
/*  997 */     double targetY = (getBoundingBox()).minY;
/*  998 */     double targetZ = getZ() + direction.z;
/* 1000 */     BlockPos.MutableBlockPos targetBlockPos = new BlockPos.MutableBlockPos();
/*      */     UnmodifiableIterator<Pose> unmodifiableIterator;
/* 1001 */     label18: for (unmodifiableIterator = passenger.getDismountPoses().iterator(); unmodifiableIterator.hasNext(); ) {
/* 1001 */       Pose dismountPose = unmodifiableIterator.next();
/* 1002 */       targetBlockPos.set(targetX, targetY, targetZ);
/* 1003 */       double dismountJumpLimit = (getBoundingBox()).maxY + 0.75D;
/*      */       while (true) {
/* 1006 */         double blockFloorHeight = level().getBlockFloorHeight((BlockPos)targetBlockPos);
/* 1008 */         if (targetBlockPos.getY() + blockFloorHeight > dismountJumpLimit)
/*      */           continue label18; 
/* 1012 */         if (DismountHelper.isBlockFloorValid(blockFloorHeight)) {
/* 1013 */           AABB poseCollisionBox = passenger.getLocalBoundsForPose(dismountPose);
/* 1014 */           Vec3 location = new Vec3(targetX, targetBlockPos.getY() + blockFloorHeight, targetZ);
/* 1016 */           if (DismountHelper.canDismountTo((CollisionGetter)level(), passenger, poseCollisionBox.move(location))) {
/* 1017 */             passenger.setPose(dismountPose);
/* 1018 */             return location;
/*      */           } 
/*      */         } 
/* 1022 */         targetBlockPos.move(Direction.UP);
/* 1023 */         if (targetBlockPos.getY() >= dismountJumpLimit)
/*      */           continue label18; 
/*      */       } 
/*      */     } 
/* 1026 */     return null;
/*      */   }
/*      */   
/*      */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
/* 1031 */     Vec3 mainHandDirection = getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), getYRot() + ((passenger.getMainArm() == HumanoidArm.RIGHT) ? 90.0F : -90.0F));
/* 1032 */     Vec3 mainHandLocation = getDismountLocationInDirection(mainHandDirection, passenger);
/* 1034 */     if (mainHandLocation != null)
/* 1035 */       return mainHandLocation; 
/* 1038 */     Vec3 offHandDirection = getCollisionHorizontalEscapeVector(getBbWidth(), passenger.getBbWidth(), getYRot() + ((passenger.getMainArm() == HumanoidArm.LEFT) ? 90.0F : -90.0F));
/* 1039 */     Vec3 offHandLocation = getDismountLocationInDirection(offHandDirection, passenger);
/* 1041 */     if (offHandLocation != null)
/* 1042 */       return offHandLocation; 
/* 1045 */     return position();
/*      */   }
/*      */   
/*      */   protected void randomizeAttributes(RandomSource random) {}
/*      */   
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*      */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 1053 */     if (groupData == null)
/* 1054 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(0.2F); 
/* 1057 */     randomizeAttributes(level.getRandom());
/* 1059 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*      */   }
/*      */   
/*      */   public boolean hasInventoryChanged(Container oldInventory) {
/* 1063 */     return (this.inventory != oldInventory);
/*      */   }
/*      */   
/*      */   public int getAmbientStandInterval() {
/* 1067 */     return getAmbientSoundInterval();
/*      */   }
/*      */   
/*      */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 1072 */     return super.getPassengerAttachmentPoint(passenger, dimensions, scale).add((new Vec3(0.0D, 0.15D * this.standAnimO * scale, -0.7D * this.standAnimO * scale))
/*      */         
/* 1076 */         .yRot(-getYRot() * 0.017453292F));
/*      */   }
/*      */   
/*      */   public int getInventoryColumns() {
/* 1080 */     return 0;
/*      */   }
/*      */   
/*      */   private class MountPanicGoal extends PanicGoal {
/*      */     public MountPanicGoal(double speedModifier) {
/* 1085 */       super((PathfinderMob)AbstractHorse.this, speedModifier);
/*      */     }
/*      */     
/*      */     public boolean shouldPanic() {
/* 1090 */       return (!AbstractHorse.this.isMobControlled() && super.shouldPanic());
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\equine\AbstractHorse.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */