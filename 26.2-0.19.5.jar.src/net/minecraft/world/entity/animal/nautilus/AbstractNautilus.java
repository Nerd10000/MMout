/*     */ package net.minecraft.world.entity.animal.nautilus;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PlayerRideableJumping;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.inventory.AbstractMountInventoryMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractNautilus extends TamableAnimal implements PlayerRideableJumping, HasCustomInventoryScreen {
/*     */   public static final int INVENTORY_SLOT_OFFSET = 500;
/*     */   
/*     */   public static final int INVENTORY_ROWS = 3;
/*     */   
/*     */   public static final int SMALL_RESTRICTION_RADIUS = 16;
/*     */   
/*     */   public static final int LARGE_RESTRICTION_RADIUS = 32;
/*     */   
/*     */   public static final int RESTRICTION_RADIUS_BUFFER = 8;
/*     */   
/*     */   private static final int EFFECT_DURATION = 60;
/*     */   
/*     */   private static final int EFFECT_REFRESH_RATE = 40;
/*     */   
/*     */   private static final double NAUTILUS_WATER_RESISTANCE = 0.9D;
/*     */   
/*     */   private static final float IN_WATER_SPEED_MODIFIER = 0.011F;
/*     */   
/*     */   private static final float RIDDEN_SPEED_MODIFIER_IN_WATER = 0.0325F;
/*     */   
/*     */   private static final float RIDDEN_SPEED_MODIFIER_ON_LAND = 0.02F;
/*     */   
/*  79 */   private static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(AbstractNautilus.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int DASH_COOLDOWN_TICKS = 40;
/*     */   
/*     */   private static final int DASH_MINIMUM_DURATION_TICKS = 5;
/*     */   
/*     */   private static final float DASH_MOMENTUM_IN_WATER = 1.2F;
/*     */   
/*     */   private static final float DASH_MOMENTUM_ON_LAND = 0.5F;
/*     */   
/*  85 */   private int dashCooldown = 0;
/*     */   
/*     */   protected float playerJumpPendingScale;
/*     */   
/*     */   protected SimpleContainer inventory;
/*     */   
/*     */   private static final double BUBBLE_SPREAD_FACTOR = 0.8D;
/*     */   
/*     */   private static final double BUBBLE_DIRECTION_SCALE = 1.1D;
/*     */   
/*     */   private static final double BUBBLE_Y_OFFSET = 0.25D;
/*     */   
/*     */   private static final double BUBBLE_PROBABILITY_MULTIPLIER = 2.0D;
/*     */   
/*     */   private static final float BUBBLE_PROBABILITY_MIN = 0.15F;
/*     */   
/*     */   private static final float BUBBLE_PROBABILITY_MAX = 1.0F;
/*     */   
/*     */   protected AbstractNautilus(EntityType<? extends AbstractNautilus> type, Level level) {
/*  97 */     super(type, level);
/*  99 */     this.moveControl = (MoveControl)new SmoothSwimmingMoveControl((Mob)this, 85, 10, 0.011F, 0.0F, true);
/* 100 */     this.lookControl = (LookControl)new SmoothSwimmingLookControl((Mob)this, 10);
/* 102 */     setPathfindingMalus(PathType.WATER, 0.0F);
/* 103 */     createInventory();
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 108 */     return (isTame() || isBaby()) ? itemStack.is(ItemTags.NAUTILUS_FOOD) : itemStack.is(ItemTags.NAUTILUS_TAMING_ITEMS);
/*     */   }
/*     */   
/*     */   protected void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack) {
/* 114 */     if (itemStack.is(ItemTags.NAUTILUS_BUCKET_FOOD)) {
/* 115 */       player.setItemInHand(hand, ItemUtils.createFilledResult(itemStack, player, new ItemStack((ItemLike)Items.WATER_BUCKET)));
/*     */     } else {
/* 117 */       super.usePlayerItem(player, hand, itemStack);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 122 */     return Animal.createAnimalAttributes()
/* 123 */       .add(Attributes.MAX_HEALTH, 15.0D)
/* 124 */       .add(Attributes.MOVEMENT_SPEED, 1.0D)
/* 125 */       .add(Attributes.ATTACK_DAMAGE, 3.0D)
/* 126 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.30000001192092896D);
/*     */   }
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 132 */     return false;
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 137 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, level);
/*     */   }
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 142 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public static boolean checkNautilusSpawnRules(EntityType<? extends AbstractNautilus> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 146 */     int seaLevel = level.getSeaLevel();
/* 147 */     int minSpawnLevel = seaLevel - 25;
/* 148 */     return (pos.getY() >= minSpawnLevel && pos
/* 149 */       .getY() <= seaLevel - 5 && level
/* 150 */       .getFluidState(pos.below()).is(FluidTags.WATER) && level
/* 151 */       .getBlockState(pos.above()).is(Blocks.WATER));
/*     */   }
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 156 */     return level.isUnobstructed((Entity)this);
/*     */   }
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot slot) {
/* 161 */     if (slot == EquipmentSlot.SADDLE || slot == EquipmentSlot.BODY)
/* 162 */       return (isAlive() && !isBaby() && isTame()); 
/* 164 */     return super.canUseSlot(slot);
/*     */   }
/*     */   
/*     */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/* 169 */     return (slot == EquipmentSlot.BODY || slot == EquipmentSlot.SADDLE || super.canDispenserEquipIntoSlot(slot));
/*     */   }
/*     */   
/*     */   protected boolean canAddPassenger(Entity passenger) {
/* 176 */     return !isVehicle();
/*     */   }
/*     */   
/*     */   public LivingEntity getControllingPassenger() {
/* 181 */     Entity firstPassenger = getFirstPassenger();
/* 182 */     if (isSaddled() && firstPassenger instanceof Player) {
/* 182 */       Player player = (Player)firstPassenger;
/* 183 */       return (LivingEntity)player;
/*     */     } 
/* 185 */     return super.getControllingPassenger();
/*     */   }
/*     */   
/*     */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/* 191 */     float strafe = controller.xxa;
/* 192 */     float forward = 0.0F;
/* 193 */     float up = 0.0F;
/* 195 */     if (controller.zza != 0.0F) {
/* 197 */       float forwardLook = Mth.cos((controller.getXRot() * 0.017453292F));
/* 198 */       float upLook = -Mth.sin((controller.getXRot() * 0.017453292F));
/* 199 */       if (controller.zza < 0.0F) {
/* 201 */         forwardLook *= -0.5F;
/* 202 */         upLook *= -0.5F;
/*     */       } 
/* 204 */       up = upLook;
/* 205 */       forward = forwardLook;
/*     */     } 
/* 207 */     return new Vec3(strafe, up, forward);
/*     */   }
/*     */   
/*     */   protected Vec2 getRiddenRotation(LivingEntity controller) {
/* 211 */     return new Vec2(controller.getXRot() * 0.5F, controller.getYRot());
/*     */   }
/*     */   
/*     */   protected void tickRidden(Player controller, Vec3 riddenInput) {
/* 216 */     super.tickRidden(controller, riddenInput);
/* 217 */     Vec2 rotation = getRiddenRotation((LivingEntity)controller);
/* 218 */     float yRot = getYRot();
/* 219 */     float diff = Mth.wrapDegrees(rotation.y - yRot);
/* 220 */     float turnSpeed = 0.5F;
/* 221 */     yRot += diff * 0.5F;
/* 222 */     setRot(yRot, rotation.x);
/* 223 */     this.yRotO = this.yBodyRot = this.yHeadRot = yRot;
/* 224 */     if (isLocalInstanceAuthoritative()) {
/* 226 */       if (this.playerJumpPendingScale > 0.0F && !isJumping())
/* 227 */         executeRidersJump(this.playerJumpPendingScale, controller); 
/* 229 */       this.playerJumpPendingScale = 0.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 235 */     float speed = getSpeed();
/* 236 */     moveRelative(speed, input);
/* 237 */     move(MoverType.SELF, getDeltaMovement());
/* 238 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */   }
/*     */   
/*     */   protected float getRiddenSpeed(Player controller) {
/* 243 */     return isInWater() ? (0.0325F * (float)getAttributeValue(Attributes.MOVEMENT_SPEED)) : (0.02F * (float)getAttributeValue(Attributes.MOVEMENT_SPEED));
/*     */   }
/*     */   
/*     */   protected void doPlayerRide(Player player) {
/* 247 */     if (!level().isClientSide()) {
/* 248 */       player.startRiding((Entity)this);
/* 249 */       if (!isVehicle())
/* 250 */         clearHome(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getNautilusRestrictionRadius() {
/* 258 */     if (!isBaby() && getItemBySlot(EquipmentSlot.SADDLE).isEmpty())
/* 259 */       return 32; 
/* 261 */     return 16;
/*     */   }
/*     */   
/*     */   protected void checkRestriction() {
/* 265 */     if (isLeashed() || isVehicle() || !isTame())
/*     */       return; 
/* 268 */     int radius = getNautilusRestrictionRadius();
/* 269 */     if (hasHome() && getHomePosition().closerThan((Vec3i)blockPosition(), (radius + 8)) && radius == getHomeRadius())
/*     */       return; 
/* 272 */     setHomeTo(blockPosition(), radius);
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 277 */     checkRestriction();
/* 278 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   private void applyEffects(Level level) {
/* 282 */     Entity passenger = getFirstPassenger();
/* 284 */     if (passenger instanceof Player) {
/* 284 */       Player player = (Player)passenger;
/* 285 */       boolean hasEffect = player.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS);
/* 286 */       boolean shouldRefresh = (level.getGameTime() % 40L == 0L);
/* 287 */       if (!hasEffect || shouldRefresh)
/* 288 */         player.addEffect(new MobEffectInstance(MobEffects.BREATH_OF_THE_NAUTILUS, 60, 0, true, true, true)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnBubbles() {
/* 295 */     double speed = getDeltaMovement().length();
/* 296 */     double bubbleProbability = Mth.clamp(speed * 2.0D, 0.15000000596046448D, 1.0D);
/* 297 */     if (this.random.nextFloat() < bubbleProbability) {
/* 299 */       float yRot = getYRot();
/* 300 */       float xRot = Mth.clamp(getXRot(), -10.0F, 10.0F);
/* 301 */       Vec3 mouthDirectionVector = calculateViewVector(xRot, yRot);
/* 302 */       double spread = this.random.nextDouble() * 0.8D * (1.0D + speed);
/* 303 */       double dx = (this.random.nextFloat() - 0.5D) * spread;
/* 304 */       double dy = (this.random.nextFloat() - 0.5D) * spread;
/* 305 */       double dz = (this.random.nextFloat() - 0.5D) * spread;
/* 306 */       level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, getX() - mouthDirectionVector.x * 1.1D, getY() - mouthDirectionVector.y + 0.25D, getZ() - mouthDirectionVector.z * 1.1D, dx, dy, dz);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 312 */     super.tick();
/* 313 */     if (!level().isClientSide())
/* 314 */       applyEffects(level()); 
/* 317 */     if (isDashing() && this.dashCooldown < 35)
/* 318 */       setDashing(false); 
/* 320 */     if (this.dashCooldown > 0) {
/* 321 */       this.dashCooldown--;
/* 322 */       if (this.dashCooldown == 0)
/* 323 */         makeSound(getDashReadySound()); 
/*     */     } 
/* 327 */     if (isInWater())
/* 328 */       spawnBubbles(); 
/*     */   }
/*     */   
/*     */   public boolean canJump() {
/* 336 */     return isSaddled();
/*     */   }
/*     */   
/*     */   public void onPlayerJump(int jumpAmount) {
/* 341 */     if (!isSaddled() || this.dashCooldown > 0)
/*     */       return; 
/* 344 */     this.playerJumpPendingScale = getPlayerJumpPendingScale(jumpAmount);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 349 */     super.defineSynchedData(entityData);
/* 350 */     entityData.define(DASH, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean isDashing() {
/* 354 */     return ((Boolean)this.entityData.get(DASH)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDashing(boolean isDashing) {
/* 358 */     this.entityData.set(DASH, Boolean.valueOf(isDashing));
/*     */   }
/*     */   
/*     */   protected void executeRidersJump(float amount, Player controller) {
/* 362 */     addDeltaMovement(controller.getLookAngle()
/* 363 */         .scale(((isInWater() ? 1.2F : 0.5F) * amount) * getAttributeValue(Attributes.MOVEMENT_SPEED) * getBlockSpeedFactor()));
/* 366 */     this.dashCooldown = 40;
/* 367 */     setDashing(true);
/* 368 */     this.needsSync = true;
/*     */   }
/*     */   
/*     */   public void handleStartJump(int jumpScale) {
/* 373 */     makeSound(getDashSound());
/* 374 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 375 */     setDashing(true);
/*     */   }
/*     */   
/*     */   public int getJumpCooldown() {
/* 380 */     return this.dashCooldown;
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 385 */     if (!this.firstTick && 
/* 386 */       DASH.equals(accessor))
/* 387 */       this.dashCooldown = (this.dashCooldown == 0) ? 40 : this.dashCooldown; 
/* 390 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   public void handleStopJump() {}
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {}
/*     */   
/*     */   protected SoundEvent getDashSound() {
/* 403 */     return null;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashReadySound() {
/* 407 */     return null;
/*     */   }
/*     */   
/*     */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/* 412 */     setPersistenceRequired();
/* 413 */     return super.interact(player, hand, location);
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 418 */     ItemStack itemStack = player.getItemInHand(hand);
/* 420 */     if (isBaby())
/* 421 */       return super.mobInteract(player, hand); 
/* 424 */     if (isTame() && player.isSecondaryUseActive()) {
/* 425 */       openCustomInventoryScreen(player);
/* 426 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 429 */     if (!itemStack.isEmpty()) {
/* 431 */       if (!level().isClientSide() && !isTame() && isFood(itemStack)) {
/* 432 */         usePlayerItem(player, hand, itemStack);
/* 433 */         tryToTame(player);
/* 434 */         return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */       } 
/* 435 */       if (isFood(itemStack) && getHealth() < getMaxHealth()) {
/* 436 */         feed(player, hand, itemStack, 2.0F, 1.0F);
/* 437 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/* 440 */       InteractionResult interactionResult = itemStack.interactLivingEntity(player, (LivingEntity)this, hand);
/* 441 */       if (interactionResult.consumesAction())
/* 442 */         return interactionResult; 
/*     */     } 
/* 446 */     if (isTame() && !player.isSecondaryUseActive() && !isFood(itemStack)) {
/* 447 */       doPlayerRide(player);
/* 448 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 451 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   private void tryToTame(Player player) {
/* 455 */     if (this.random.nextInt(3) == 0) {
/* 456 */       tame(player);
/* 457 */       this.navigation.stop();
/* 458 */       level().broadcastEntityEvent((Entity)this, (byte)7);
/*     */     } else {
/* 460 */       level().broadcastEntityEvent((Entity)this, (byte)6);
/*     */     } 
/* 462 */     playEatingSound();
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 467 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 472 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 473 */     if (wasHurt) {
/* 473 */       Entity entity = source.getEntity();
/* 473 */       if (entity instanceof LivingEntity) {
/* 473 */         LivingEntity sourceEntity = (LivingEntity)entity;
/* 474 */         NautilusAi.setAngerTarget(level, this, sourceEntity);
/*     */       } 
/*     */     } 
/* 476 */     return wasHurt;
/*     */   }
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 482 */     if (newEffect.getEffect() == MobEffects.POISON)
/* 483 */       return false; 
/* 485 */     return super.canBeAffected(newEffect);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 490 */     RandomSource random = level.getRandom();
/* 491 */     NautilusAi.initMemories(this, random);
/* 492 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/* 496 */     if (slot == EquipmentSlot.SADDLE && isUnderWater())
/* 497 */       return (Holder<SoundEvent>)SoundEvents.NAUTILUS_SADDLE_UNDERWATER_EQUIP; 
/* 499 */     if (slot == EquipmentSlot.SADDLE)
/* 500 */       return (Holder<SoundEvent>)SoundEvents.NAUTILUS_SADDLE_EQUIP; 
/* 502 */     return super.getEquipSound(slot, stack, equippable);
/*     */   }
/*     */   
/*     */   public final int getInventorySize() {
/* 506 */     return AbstractMountInventoryMenu.getInventorySize(getInventoryColumns());
/*     */   }
/*     */   
/*     */   protected void createInventory() {
/* 510 */     SimpleContainer old = this.inventory;
/* 511 */     this.inventory = new SimpleContainer(getInventorySize());
/* 512 */     if (old != null) {
/* 513 */       int max = Math.min(old.getContainerSize(), this.inventory.getContainerSize());
/* 514 */       for (int slot = 0; slot < max; slot++) {
/* 515 */         ItemStack itemStack = old.getItem(slot);
/* 516 */         if (!itemStack.isEmpty())
/* 517 */           this.inventory.setItem(slot, itemStack.copy()); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void openCustomInventoryScreen(Player player) {
/* 525 */     if (!level().isClientSide() && (!isVehicle() || hasPassenger((Entity)player)) && isTame())
/* 526 */       player.openNautilusInventory(this, (Container)this.inventory); 
/*     */   }
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 532 */     int inventorySlot = slot - 500;
/* 533 */     if (inventorySlot >= 0 && inventorySlot < this.inventory.getContainerSize())
/* 534 */       return this.inventory.getSlot(inventorySlot); 
/* 536 */     return super.getSlot(slot);
/*     */   }
/*     */   
/*     */   public boolean hasInventoryChanged(Container oldInventory) {
/* 540 */     return (this.inventory != oldInventory);
/*     */   }
/*     */   
/*     */   public int getInventoryColumns() {
/* 544 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean isMobControlled() {
/* 548 */     return getFirstPassenger() instanceof Mob;
/*     */   }
/*     */   
/*     */   protected boolean isAggravated() {
/* 552 */     return (getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT) || getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */   
/*     */   public boolean requiresCustomPersistence() {
/* 557 */     return (super.requiresCustomPersistence() || isTame());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\nautilus\AbstractNautilus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */