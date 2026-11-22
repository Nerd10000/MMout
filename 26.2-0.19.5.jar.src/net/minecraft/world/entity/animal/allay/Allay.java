/*     */ package net.minecraft.world.entity.animal.allay;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.DynamicGameEventListener;
/*     */ import net.minecraft.world.level.gameevent.EntityPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Allay extends PathfinderMob implements InventoryCarrier, VibrationSystem {
/*  75 */   private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 1, 1);
/*     */   
/*     */   private static final int LIFTING_ITEM_ANIMATION_DURATION = 5;
/*     */   
/*     */   private static final float DANCING_LOOP_DURATION = 55.0F;
/*     */   
/*     */   private static final float SPINNING_ANIMATION_DURATION = 15.0F;
/*     */   
/*     */   private static final int DEFAULT_DUPLICATION_COOLDOWN = 0;
/*     */   
/*     */   private static final int DUPLICATION_COOLDOWN_TICKS = 6000;
/*     */   
/*     */   private static final int NUM_OF_DUPLICATION_HEARTS = 3;
/*     */   
/*     */   public static final int MAX_NOTEBLOCK_DISTANCE = 1024;
/*     */   
/*  85 */   private static final EntityDataAccessor<Boolean> DATA_DANCING = SynchedEntityData.defineId(Allay.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  86 */   private static final EntityDataAccessor<Boolean> DATA_CAN_DUPLICATE = SynchedEntityData.defineId(Allay.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  88 */   private static final Brain.Provider<Allay> BRAIN_PROVIDER = Brain.provider(
/*  89 */       List.of(MemoryModuleType.LIKED_PLAYER, MemoryModuleType.LIKED_NOTEBLOCK_POSITION, MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS), 
/*     */       
/*  94 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NEAREST_ITEMS), paramAllay -> AllayAi.getActivities());
/*     */   
/* 103 */   public static final ImmutableList<Float> THROW_SOUND_PITCHES = ImmutableList.of(
/* 104 */       Float.valueOf(0.5625F), 
/* 105 */       Float.valueOf(0.625F), 
/* 106 */       Float.valueOf(0.75F), 
/* 107 */       Float.valueOf(0.9375F), 
/* 108 */       Float.valueOf(1.0F), 
/* 109 */       Float.valueOf(1.0F), 
/* 110 */       Float.valueOf(1.125F), 
/* 111 */       Float.valueOf(1.25F), 
/* 112 */       Float.valueOf(1.5F), 
/* 113 */       Float.valueOf(1.875F), 
/* 114 */       Float.valueOf(2.0F), 
/* 115 */       Float.valueOf(2.25F), (Object[])new Float[] { Float.valueOf(2.5F), 
/* 117 */         Float.valueOf(3.0F), 
/* 118 */         Float.valueOf(3.75F), 
/* 119 */         Float.valueOf(4.0F) });
/*     */   
/*     */   private final DynamicGameEventListener<VibrationSystem.Listener> dynamicVibrationListener;
/*     */   
/*     */   private VibrationSystem.Data vibrationData;
/*     */   
/*     */   private final VibrationSystem.User vibrationUser;
/*     */   
/*     */   private final DynamicGameEventListener<JukeboxListener> dynamicJukeboxListener;
/*     */   
/* 128 */   private final SimpleContainer inventory = new SimpleContainer(1);
/*     */   
/*     */   private BlockPos jukeboxPos;
/*     */   
/* 130 */   private long duplicationCooldown = 0L;
/*     */   
/*     */   private float holdingItemAnimationTicks;
/*     */   
/*     */   private float holdingItemAnimationTicks0;
/*     */   
/*     */   private float dancingAnimationTicks;
/*     */   
/*     */   private float spinningAnimationTicks;
/*     */   
/*     */   private float spinningAnimationTicks0;
/*     */   
/*     */   public Allay(EntityType<? extends Allay> type, Level level) {
/* 139 */     super(type, level);
/* 140 */     this.moveControl = (MoveControl)new FlyingMoveControl((Mob)this, 20, true);
/* 141 */     setCanPickUpLoot(canPickUpLoot());
/* 143 */     this.vibrationUser = new VibrationUser();
/* 144 */     this.vibrationData = new VibrationSystem.Data();
/* 145 */     this.dynamicVibrationListener = new DynamicGameEventListener((GameEventListener)new VibrationSystem.Listener(this));
/* 146 */     this.dynamicJukeboxListener = new DynamicGameEventListener(new JukeboxListener(this.vibrationUser.getPositionSource(), ((GameEvent)GameEvent.JUKEBOX_PLAY.value()).notificationRadius()));
/*     */   }
/*     */   
/*     */   protected Brain<Allay> makeBrain(Brain.Packed packedBrain) {
/* 151 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Allay> getBrain() {
/* 157 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 161 */     return Mob.createMobAttributes()
/* 162 */       .add(Attributes.MAX_HEALTH, 20.0D)
/* 163 */       .add(Attributes.FLYING_SPEED, 0.10000000149011612D)
/* 164 */       .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612D)
/* 165 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 170 */     FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation((Mob)this, level);
/* 171 */     flyingPathNavigation.setCanOpenDoors(false);
/* 172 */     flyingPathNavigation.setCanFloat(true);
/* 173 */     flyingPathNavigation.setRequiredPathLength(48.0F);
/* 174 */     return (PathNavigation)flyingPathNavigation;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 179 */     super.defineSynchedData(entityData);
/* 180 */     entityData.define(DATA_DANCING, Boolean.valueOf(false));
/* 181 */     entityData.define(DATA_CAN_DUPLICATE, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   public void travel(Vec3 input) {
/* 186 */     travelFlying(input, getSpeed());
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 191 */     if (isLikedPlayer(source.getEntity()))
/* 192 */       return false; 
/* 194 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   protected boolean considersEntityAsAlly(Entity other) {
/* 199 */     return (isLikedPlayer(other) || super.considersEntityAsAlly(other));
/*     */   }
/*     */   
/*     */   private boolean isLikedPlayer(Entity other) {
/* 203 */     if (other instanceof Player) {
/* 203 */       Player player = (Player)other;
/* 204 */       Optional<UUID> likedPlayer = getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
/* 205 */       return (likedPlayer.isPresent() && player.getUUID().equals(likedPlayer.get()));
/*     */     } 
/* 207 */     return false;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {}
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 222 */     return hasItemInSlot(EquipmentSlot.MAINHAND) ? SoundEvents.ALLAY_AMBIENT_WITH_ITEM : SoundEvents.ALLAY_AMBIENT_WITHOUT_ITEM;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 227 */     return SoundEvents.ALLAY_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 232 */     return SoundEvents.ALLAY_DEATH;
/*     */   }
/*     */   
/*     */   protected float getSoundVolume() {
/* 237 */     return 0.4F;
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 242 */     ProfilerFiller profiler = Profiler.get();
/* 243 */     profiler.push("allayBrain");
/* 244 */     getBrain().tick(level, (LivingEntity)this);
/* 245 */     profiler.pop();
/* 247 */     profiler.push("allayActivityUpdate");
/* 248 */     AllayAi.updateActivity(this);
/* 249 */     profiler.pop();
/* 251 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 256 */     super.aiStep();
/* 258 */     if (!level().isClientSide() && isAlive() && this.tickCount % 10 == 0)
/* 259 */       heal(1.0F); 
/* 262 */     if (isDancing() && shouldStopDancing() && this.tickCount % 20 == 0) {
/* 263 */       setDancing(false);
/* 264 */       this.jukeboxPos = null;
/*     */     } 
/* 266 */     updateDuplicationCooldown();
/*     */   }
/*     */   
/*     */   public void tick() {
/* 271 */     super.tick();
/* 273 */     if (level().isClientSide()) {
/* 274 */       this.holdingItemAnimationTicks0 = this.holdingItemAnimationTicks;
/* 275 */       if (hasItemInHand()) {
/* 276 */         this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks + 1.0F, 0.0F, 5.0F);
/*     */       } else {
/* 278 */         this.holdingItemAnimationTicks = Mth.clamp(this.holdingItemAnimationTicks - 1.0F, 0.0F, 5.0F);
/*     */       } 
/* 281 */       if (isDancing()) {
/* 282 */         this.dancingAnimationTicks++;
/* 283 */         this.spinningAnimationTicks0 = this.spinningAnimationTicks;
/* 284 */         if (isSpinning()) {
/* 285 */           this.spinningAnimationTicks++;
/*     */         } else {
/* 287 */           this.spinningAnimationTicks--;
/*     */         } 
/* 289 */         this.spinningAnimationTicks = Mth.clamp(this.spinningAnimationTicks, 0.0F, 15.0F);
/*     */       } else {
/* 291 */         this.dancingAnimationTicks = 0.0F;
/* 292 */         this.spinningAnimationTicks = 0.0F;
/* 293 */         this.spinningAnimationTicks0 = 0.0F;
/*     */       } 
/*     */     } else {
/* 296 */       VibrationSystem.Ticker.tick(level(), this.vibrationData, this.vibrationUser);
/* 297 */       if (isPanicking())
/* 298 */         setDancing(false); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canPickUpLoot() {
/* 305 */     return (!isOnPickupCooldown() && hasItemInHand());
/*     */   }
/*     */   
/*     */   public boolean hasItemInHand() {
/* 309 */     return !getItemInHand(InteractionHand.MAIN_HAND).isEmpty();
/*     */   }
/*     */   
/*     */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/* 314 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isOnPickupCooldown() {
/* 318 */     return getBrain().checkMemory(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryStatus.VALUE_PRESENT);
/*     */   }
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 323 */     ItemStack interactionItem = player.getItemInHand(hand);
/* 324 */     ItemStack itemInHand = getItemInHand(InteractionHand.MAIN_HAND);
/* 326 */     if (isDancing() && interactionItem.is(ItemTags.DUPLICATES_ALLAYS) && canDuplicate()) {
/* 327 */       duplicateAllay();
/* 328 */       level().broadcastEntityEvent((Entity)this, (byte)18);
/* 329 */       level().playSound((Entity)player, (Entity)this, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.NEUTRAL, 2.0F, 1.0F);
/* 330 */       removeInteractionItem(player, interactionItem);
/* 331 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 334 */     if (itemInHand.isEmpty() && !interactionItem.isEmpty()) {
/* 335 */       ItemStack itemToGive = interactionItem.copyWithCount(1);
/* 336 */       setItemInHand(InteractionHand.MAIN_HAND, itemToGive);
/* 337 */       removeInteractionItem(player, interactionItem);
/* 338 */       level().playSound((Entity)player, (Entity)this, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
/* 339 */       getBrain().setMemory(MemoryModuleType.LIKED_PLAYER, player.getUUID());
/* 341 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 344 */     if (!itemInHand.isEmpty() && hand == InteractionHand.MAIN_HAND && interactionItem.isEmpty()) {
/* 345 */       setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 346 */       level().playSound((Entity)player, (Entity)this, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.NEUTRAL, 2.0F, 1.0F);
/* 347 */       swing(InteractionHand.MAIN_HAND);
/* 348 */       for (ItemStack itemStack : getInventory().removeAllItems())
/* 349 */         BehaviorUtils.throwItem((LivingEntity)this, itemStack, position()); 
/* 351 */       getBrain().eraseMemory(MemoryModuleType.LIKED_PLAYER);
/* 352 */       player.addItem(itemInHand);
/* 354 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 357 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   public void setJukeboxPlaying(BlockPos jukebox, boolean isPlaying) {
/* 361 */     if (isPlaying) {
/* 362 */       if (!isDancing()) {
/* 363 */         this.jukeboxPos = jukebox;
/* 364 */         setDancing(true);
/*     */       } 
/* 366 */     } else if (jukebox.equals(this.jukeboxPos) || this.jukeboxPos == null) {
/* 367 */       this.jukeboxPos = null;
/* 368 */       setDancing(false);
/*     */     } 
/*     */   }
/*     */   
/*     */   public SimpleContainer getInventory() {
/* 374 */     return this.inventory;
/*     */   }
/*     */   
/*     */   protected Vec3i getPickupReach() {
/* 379 */     return ITEM_PICKUP_REACH;
/*     */   }
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 384 */     ItemStack itemInHand = getItemInHand(InteractionHand.MAIN_HAND);
/* 385 */     return (!itemInHand.isEmpty() && ((Boolean)level
/* 386 */       .getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && this.inventory
/* 387 */       .canAddItem(itemStack) && 
/* 388 */       allayConsidersItemEqual(itemInHand, itemStack));
/*     */   }
/*     */   
/*     */   private boolean allayConsidersItemEqual(ItemStack item1, ItemStack item2) {
/* 392 */     return (ItemStack.isSameItem(item1, item2) && !hasNonMatchingPotion(item1, item2));
/*     */   }
/*     */   
/*     */   private boolean hasNonMatchingPotion(ItemStack itemInHand, ItemStack pickupItem) {
/* 396 */     PotionContents potionInHand = (PotionContents)itemInHand.get(DataComponents.POTION_CONTENTS);
/* 397 */     PotionContents potionInPickupItem = (PotionContents)pickupItem.get(DataComponents.POTION_CONTENTS);
/* 398 */     return !Objects.equals(potionInHand, potionInPickupItem);
/*     */   }
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 403 */     InventoryCarrier.pickUpItem(level, (Mob)this, this, entity);
/*     */   }
/*     */   
/*     */   public boolean isFlapping() {
/* 408 */     return !onGround();
/*     */   }
/*     */   
/*     */   public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> action) {
/* 413 */     Level level = level();
/* 413 */     if (level instanceof ServerLevel) {
/* 413 */       ServerLevel serverLevel = (ServerLevel)level;
/* 414 */       action.accept(this.dynamicVibrationListener, serverLevel);
/* 415 */       action.accept(this.dynamicJukeboxListener, serverLevel);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isDancing() {
/* 420 */     return ((Boolean)this.entityData.get(DATA_DANCING)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDancing(boolean isDancing) {
/* 424 */     if (level().isClientSide() || !isEffectiveAi() || (isDancing && isPanicking()))
/*     */       return; 
/* 427 */     this.entityData.set(DATA_DANCING, Boolean.valueOf(isDancing));
/*     */   }
/*     */   
/*     */   private boolean shouldStopDancing() {
/* 431 */     return (this.jukeboxPos == null || 
/* 432 */       !this.jukeboxPos.closerToCenterThan((Position)position(), ((GameEvent)GameEvent.JUKEBOX_PLAY.value()).notificationRadius()) || 
/* 433 */       !level().getBlockState(this.jukeboxPos).is(Blocks.JUKEBOX));
/*     */   }
/*     */   
/*     */   public float getHoldingItemAnimationProgress(float a) {
/* 437 */     return Mth.lerp(a, this.holdingItemAnimationTicks0, this.holdingItemAnimationTicks) / 5.0F;
/*     */   }
/*     */   
/*     */   public boolean isSpinning() {
/* 441 */     float spinningProgress = this.dancingAnimationTicks % 55.0F;
/* 442 */     return (spinningProgress < 15.0F);
/*     */   }
/*     */   
/*     */   public float getSpinningProgress(float a) {
/* 446 */     return Mth.lerp(a, this.spinningAnimationTicks0, this.spinningAnimationTicks) / 15.0F;
/*     */   }
/*     */   
/*     */   public boolean equipmentHasChanged(ItemStack previous, ItemStack current) {
/* 451 */     return !allayConsidersItemEqual(previous, current);
/*     */   }
/*     */   
/*     */   protected void dropEquipment(ServerLevel level) {
/* 456 */     super.dropEquipment(level);
/* 457 */     this.inventory.removeAllItems().forEach(stack -> spawnAtLocation(level, stack));
/* 461 */     ItemStack itemStack = getItemBySlot(EquipmentSlot.MAINHAND);
/* 462 */     if (!itemStack.isEmpty() && !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
/* 463 */       spawnAtLocation(level, itemStack);
/* 464 */       setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 470 */     return false;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 475 */     super.addAdditionalSaveData(output);
/* 477 */     writeInventoryToTag(output);
/* 479 */     output.store("listener", VibrationSystem.Data.CODEC, this.vibrationData);
/* 481 */     output.putLong("DuplicationCooldown", this.duplicationCooldown);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 486 */     super.readAdditionalSaveData(input);
/* 488 */     readInventoryFromTag(input);
/* 490 */     this.vibrationData = input.read("listener", VibrationSystem.Data.CODEC).orElseGet(net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Data::new);
/* 492 */     setDuplicationCooldown(input.getIntOr("DuplicationCooldown", 0));
/*     */   }
/*     */   
/*     */   protected boolean shouldStayCloseToLeashHolder() {
/* 497 */     return false;
/*     */   }
/*     */   
/*     */   private void updateDuplicationCooldown() {
/* 501 */     if (!level().isClientSide() && this.duplicationCooldown > 0L)
/* 502 */       setDuplicationCooldown(this.duplicationCooldown - 1L); 
/*     */   }
/*     */   
/*     */   private void setDuplicationCooldown(long duplicationCooldown) {
/* 507 */     this.duplicationCooldown = duplicationCooldown;
/* 508 */     this.entityData.set(DATA_CAN_DUPLICATE, Boolean.valueOf((duplicationCooldown == 0L)));
/*     */   }
/*     */   
/*     */   private void duplicateAllay() {
/* 512 */     Allay allay = (Allay)EntityTypes.ALLAY.create(level(), EntitySpawnReason.BREEDING);
/* 513 */     if (allay != null) {
/* 514 */       allay.snapTo(position());
/* 515 */       allay.setPersistenceRequired();
/* 516 */       allay.resetDuplicationCooldown();
/* 517 */       resetDuplicationCooldown();
/* 518 */       level().addFreshEntity((Entity)allay);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void resetDuplicationCooldown() {
/* 523 */     setDuplicationCooldown(6000L);
/*     */   }
/*     */   
/*     */   private boolean canDuplicate() {
/* 527 */     return ((Boolean)this.entityData.get(DATA_CAN_DUPLICATE)).booleanValue();
/*     */   }
/*     */   
/*     */   private void removeInteractionItem(Player player, ItemStack interactionItem) {
/* 531 */     interactionItem.consume(1, (LivingEntity)player);
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 536 */     return new Vec3(0.0D, getEyeHeight() * 0.6D, getBbWidth() * 0.1D);
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 541 */     if (id == 18) {
/* 542 */       for (int i = 0; i < 3; i++)
/* 543 */         spawnHeartParticle(); 
/*     */     } else {
/* 546 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnHeartParticle() {
/* 551 */     double xd = this.random.nextGaussian() * 0.02D;
/* 552 */     double yd = this.random.nextGaussian() * 0.02D;
/* 553 */     double zd = this.random.nextGaussian() * 0.02D;
/* 554 */     level().addParticle((ParticleOptions)ParticleTypes.HEART, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), xd, yd, zd);
/*     */   }
/*     */   
/*     */   public VibrationSystem.Data getVibrationData() {
/* 559 */     return this.vibrationData;
/*     */   }
/*     */   
/*     */   public VibrationSystem.User getVibrationUser() {
/* 564 */     return this.vibrationUser;
/*     */   }
/*     */   
/*     */   private class JukeboxListener implements GameEventListener {
/*     */     private final PositionSource listenerSource;
/*     */     
/*     */     private final int listenerRadius;
/*     */     
/*     */     public JukeboxListener(PositionSource listenerSource, int listenerRadius) {
/* 572 */       this.listenerSource = listenerSource;
/* 573 */       this.listenerRadius = listenerRadius;
/*     */     }
/*     */     
/*     */     public PositionSource getListenerSource() {
/* 578 */       return this.listenerSource;
/*     */     }
/*     */     
/*     */     public int getListenerRadius() {
/* 583 */       return this.listenerRadius;
/*     */     }
/*     */     
/*     */     public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> event, GameEvent.Context context, Vec3 sourcePosition) {
/* 588 */       if (event.is((Holder)GameEvent.JUKEBOX_PLAY)) {
/* 589 */         Allay.this.setJukeboxPlaying(BlockPos.containing((Position)sourcePosition), true);
/* 590 */         return true;
/*     */       } 
/* 593 */       if (event.is((Holder)GameEvent.JUKEBOX_STOP_PLAY)) {
/* 594 */         Allay.this.setJukeboxPlaying(BlockPos.containing((Position)sourcePosition), false);
/* 595 */         return true;
/*     */       } 
/* 598 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private class VibrationUser implements VibrationSystem.User {
/*     */     private static final int VIBRATION_EVENT_LISTENER_RANGE = 16;
/*     */     
/* 605 */     private final PositionSource positionSource = (PositionSource)new EntityPositionSource((Entity)Allay.this, Allay.this.getEyeHeight());
/*     */     
/*     */     public int getListenerRadius() {
/* 609 */       return 16;
/*     */     }
/*     */     
/*     */     public PositionSource getPositionSource() {
/* 614 */       return this.positionSource;
/*     */     }
/*     */     
/*     */     public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 619 */       if (Allay.this.isNoAi())
/* 620 */         return false; 
/* 623 */       Optional<GlobalPos> maybeGlobalPos = Allay.this.getBrain().getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/* 624 */       if (maybeGlobalPos.isEmpty())
/* 625 */         return true; 
/* 627 */       GlobalPos globalPos = maybeGlobalPos.get();
/* 628 */       return (globalPos.isCloseEnough(level.dimension(), Allay.this.blockPosition(), 1024) && globalPos.pos().equals(pos));
/*     */     }
/*     */     
/*     */     public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 633 */       if (event.is((Holder)GameEvent.NOTE_BLOCK_PLAY))
/* 634 */         AllayAi.hearNoteblock((LivingEntity)Allay.this, new BlockPos((Vec3i)pos)); 
/*     */     }
/*     */     
/*     */     public TagKey<GameEvent> getListenableEvents() {
/* 640 */       return GameEventTags.ALLAY_CAN_LISTEN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\allay\Allay.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */