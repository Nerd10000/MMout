/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Piglin extends AbstractPiglin implements CrossbowAttackMob, InventoryCarrier {
/*  69 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  70 */   private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  71 */   private static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  73 */   private static final Identifier SPEED_MODIFIER_BABY_ID = Identifier.withDefaultNamespace("baby");
/*     */   
/*  74 */   private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_ID, 0.20000000298023224D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
/*     */   
/*     */   private static final int MAX_HEALTH = 16;
/*     */   
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;
/*     */   
/*     */   private static final int ATTACK_DAMAGE = 5;
/*     */   
/*     */   private static final float CHANCE_OF_WEARING_EACH_ARMOUR_ITEM = 0.1F;
/*     */   
/*     */   private static final int MAX_PASSENGERS_ON_ONE_HOGLIN = 3;
/*     */   
/*     */   private static final float PROBABILITY_OF_SPAWNING_AS_BABY = 0.2F;
/*     */   
/*  83 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.49F, 0.98F)
/*  84 */     .withEyeHeight(0.78F)
/*  85 */     .withAttachments(EntityAttachments.builder()
/*  86 */       .attach(EntityAttachment.VEHICLE, 0.0F, 0.1875F, 0.0F));
/*     */   
/*     */   private static final double PROBABILITY_OF_SPAWNING_WITH_CROSSBOW_INSTEAD_OF_SWORD = 0.5D;
/*     */   
/*     */   private static final boolean DEFAULT_IS_BABY = false;
/*     */   
/*     */   private static final boolean DEFAULT_CANNOT_HUNT = false;
/*     */   
/*     */   private static final int INVENTORY_SLOT_OFFSET = 300;
/*     */   
/*     */   private static final int INVENTORY_SIZE = 8;
/*     */   
/*  94 */   private final SimpleContainer inventory = new SimpleContainer(8);
/*     */   
/*     */   private boolean cannotHunt = false;
/*     */   
/*  97 */   private static final Brain.Provider<Piglin> BRAIN_PROVIDER = Brain.provider(
/*  98 */       List.of(MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.SPEAR_FLEEING_TIME, MemoryModuleType.SPEAR_FLEEING_POSITION, MemoryModuleType.SPEAR_CHARGE_POSITION, MemoryModuleType.SPEAR_ENGAGE_TIME), 
/*     */       
/* 106 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR), PiglinAi::getActivities);
/*     */   
/*     */   public Piglin(EntityType<? extends AbstractPiglin> type, Level level) {
/* 117 */     super(type, level);
/* 118 */     this.xpReward = 5;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 123 */     super.addAdditionalSaveData(output);
/* 125 */     output.putBoolean("IsBaby", isBaby());
/* 126 */     output.putBoolean("CannotHunt", this.cannotHunt);
/* 127 */     writeInventoryToTag(output);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 132 */     super.readAdditionalSaveData(input);
/* 134 */     setBaby(input.getBooleanOr("IsBaby", false));
/* 135 */     setCannotHunt(input.getBooleanOr("CannotHunt", false));
/* 136 */     readInventoryFromTag(input);
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public SimpleContainer getInventory() {
/* 142 */     return this.inventory;
/*     */   }
/*     */   
/*     */   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
/* 147 */     super.dropCustomDeathLoot(level, source, killedByPlayer);
/* 148 */     this.inventory.removeAllItems().forEach(itemStack -> spawnAtLocation(level, itemStack));
/*     */   }
/*     */   
/*     */   protected ItemStack addToInventory(ItemStack itemStack) {
/* 153 */     return this.inventory.addItem(itemStack);
/*     */   }
/*     */   
/*     */   protected boolean canAddToInventory(ItemStack itemStack) {
/* 157 */     return this.inventory.canAddItem(itemStack);
/*     */   }
/*     */   
/*     */   public SlotAccess getSlot(int slot) {
/* 162 */     int inventorySlot = slot - 300;
/* 163 */     if (inventorySlot >= 0 && inventorySlot < this.inventory.getContainerSize())
/* 164 */       return this.inventory.getSlot(inventorySlot); 
/* 166 */     return super.getSlot(slot);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 171 */     super.defineSynchedData(entityData);
/* 172 */     entityData.define(DATA_BABY_ID, Boolean.valueOf(false));
/* 173 */     entityData.define(DATA_IS_CHARGING_CROSSBOW, Boolean.valueOf(false));
/* 174 */     entityData.define(DATA_IS_DANCING, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 179 */     super.onSyncedDataUpdated(accessor);
/* 180 */     if (DATA_BABY_ID.equals(accessor))
/* 181 */       refreshDimensions(); 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 186 */     return Monster.createMonsterAttributes()
/* 187 */       .add(Attributes.MAX_HEALTH, 16.0D)
/* 188 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/* 189 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */   
/*     */   public static boolean checkPiglinSpawnRules(EntityType<Piglin> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 193 */     return !level.getBlockState(pos.below()).is(Blocks.NETHER_WART_BLOCK);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 198 */     RandomSource random = level.getRandom();
/* 199 */     if (spawnReason != EntitySpawnReason.STRUCTURE)
/* 200 */       if (random.nextFloat() < 0.2F) {
/* 201 */         setBaby(true);
/* 202 */       } else if (isAdult()) {
/* 203 */         setItemSlot(EquipmentSlot.MAINHAND, createSpawnWeapon());
/*     */       }  
/* 206 */     PiglinAi.initMemories(this, level.getRandom());
/* 207 */     populateDefaultEquipmentSlots(random, difficulty);
/* 208 */     populateDefaultEquipmentEnchantments(level, random, difficulty);
/* 209 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 214 */     return !isPersistenceRequired();
/*     */   }
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 219 */     if (isAdult()) {
/* 220 */       maybeWearArmor(EquipmentSlot.HEAD, new ItemStack((ItemLike)Items.GOLDEN_HELMET), random);
/* 221 */       maybeWearArmor(EquipmentSlot.CHEST, new ItemStack((ItemLike)Items.GOLDEN_CHESTPLATE), random);
/* 222 */       maybeWearArmor(EquipmentSlot.LEGS, new ItemStack((ItemLike)Items.GOLDEN_LEGGINGS), random);
/* 223 */       maybeWearArmor(EquipmentSlot.FEET, new ItemStack((ItemLike)Items.GOLDEN_BOOTS), random);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void maybeWearArmor(EquipmentSlot slot, ItemStack itemStack, RandomSource random) {
/* 228 */     if (random.nextFloat() < 0.1F)
/* 229 */       setItemSlot(slot, itemStack); 
/*     */   }
/*     */   
/*     */   protected Brain<Piglin> makeBrain(Brain.Packed packedBrain) {
/* 235 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Piglin> getBrain() {
/* 241 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 246 */     InteractionResult interactionResult = super.mobInteract(player, hand);
/* 247 */     if (interactionResult.consumesAction())
/* 248 */       return interactionResult; 
/* 250 */     Level level = level();
/* 250 */     if (level instanceof ServerLevel) {
/* 250 */       ServerLevel serverLevel = (ServerLevel)level;
/* 251 */       return PiglinAi.mobInteract(serverLevel, this, player, hand);
/*     */     } 
/* 253 */     boolean canAdmire = (PiglinAi.canAdmire(this, player.getItemInHand(hand)) && getArmPose() != PiglinArmPose.ADMIRING_ITEM);
/* 254 */     return canAdmire ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 259 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public void setBaby(boolean baby) {
/* 264 */     getEntityData().set(DATA_BABY_ID, Boolean.valueOf(baby));
/* 266 */     if (!level().isClientSide()) {
/* 267 */       AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/* 268 */       speed.removeModifier(SPEED_MODIFIER_BABY.id());
/* 269 */       if (baby)
/* 270 */         speed.addTransientModifier(SPEED_MODIFIER_BABY); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isBaby() {
/* 277 */     return ((Boolean)getEntityData().get(DATA_BABY_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setCannotHunt(boolean cannotHunt) {
/* 281 */     this.cannotHunt = cannotHunt;
/*     */   }
/*     */   
/*     */   protected boolean canHunt() {
/* 286 */     return !this.cannotHunt;
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 291 */     ProfilerFiller profiler = Profiler.get();
/* 292 */     profiler.push("piglinBrain");
/* 293 */     getBrain().tick(level, (LivingEntity)this);
/* 294 */     profiler.pop();
/* 296 */     PiglinAi.updateActivity(this);
/* 298 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   protected int getBaseExperienceReward(ServerLevel level) {
/* 303 */     return this.xpReward;
/*     */   }
/*     */   
/*     */   protected void finishConversion(ServerLevel level) {
/* 308 */     PiglinAi.cancelAdmiring(level, this);
/* 309 */     this.inventory.removeAllItems().forEach(itemStack -> spawnAtLocation(level, itemStack));
/* 310 */     super.finishConversion(level);
/*     */   }
/*     */   
/*     */   private ItemStack createSpawnWeapon() {
/* 314 */     if (this.random.nextFloat() < 0.5D)
/* 315 */       return new ItemStack((ItemLike)Items.CROSSBOW); 
/* 317 */     return new ItemStack((this.random.nextInt(10) == 0) ? (ItemLike)Items.GOLDEN_SPEAR : (ItemLike)Items.GOLDEN_SWORD);
/*     */   }
/*     */   
/*     */   public TagKey<Item> getPreferredWeaponType() {
/* 322 */     if (isBaby())
/* 323 */       return null; 
/* 325 */     return ItemTags.PIGLIN_PREFERRED_WEAPONS;
/*     */   }
/*     */   
/*     */   private boolean isChargingCrossbow() {
/* 329 */     return ((Boolean)this.entityData.get(DATA_IS_CHARGING_CROSSBOW)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setChargingCrossbow(boolean isCharging) {
/* 334 */     this.entityData.set(DATA_IS_CHARGING_CROSSBOW, Boolean.valueOf(isCharging));
/*     */   }
/*     */   
/*     */   public void onCrossbowAttackPerformed() {
/* 339 */     this.noActionTime = 0;
/*     */   }
/*     */   
/*     */   public PiglinArmPose getArmPose() {
/* 345 */     if (isDancing())
/* 346 */       return PiglinArmPose.DANCING; 
/* 347 */     if (PiglinAi.isLovedItem(getOffhandItem()))
/* 348 */       return PiglinArmPose.ADMIRING_ITEM; 
/* 349 */     if (isAggressive() && isHoldingMeleeWeapon())
/* 350 */       return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON; 
/* 351 */     if (isChargingCrossbow())
/* 352 */       return PiglinArmPose.CROSSBOW_CHARGE; 
/* 353 */     if (isHolding(Items.CROSSBOW) && CrossbowItem.isCharged(getWeaponItem()))
/* 354 */       return PiglinArmPose.CROSSBOW_HOLD; 
/* 356 */     return PiglinArmPose.DEFAULT;
/*     */   }
/*     */   
/*     */   public boolean isDancing() {
/* 361 */     return ((Boolean)this.entityData.get(DATA_IS_DANCING)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDancing(boolean dancing) {
/* 365 */     this.entityData.set(DATA_IS_DANCING, Boolean.valueOf(dancing));
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 370 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 371 */     if (wasHurt) {
/* 371 */       Entity entity = source.getEntity();
/* 371 */       if (entity instanceof LivingEntity) {
/* 371 */         LivingEntity sourceEntity = (LivingEntity)entity;
/* 372 */         PiglinAi.wasHurtBy(level, this, sourceEntity);
/*     */       } 
/*     */     } 
/* 374 */     return wasHurt;
/*     */   }
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 379 */     performCrossbowAttack((LivingEntity)this, 1.6F);
/*     */   }
/*     */   
/*     */   public boolean canUseNonMeleeWeapon(ItemStack item) {
/* 384 */     return (item.getItem() == Items.CROSSBOW || item.has(DataComponents.KINETIC_WEAPON));
/*     */   }
/*     */   
/*     */   protected void holdInMainHand(ItemStack itemStack) {
/* 388 */     setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, itemStack);
/* 389 */     setPersistenceRequired();
/*     */   }
/*     */   
/*     */   protected void holdInOffHand(ItemStack itemStack) {
/* 393 */     setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, itemStack);
/* 394 */     if (!itemStack.is(PiglinAi.BARTERING_ITEM))
/* 396 */       setPersistenceRequired(); 
/*     */   }
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 402 */     return (((Boolean)level.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && canPickUpLoot() && PiglinAi.wantsToPickup(this, itemStack));
/*     */   }
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack newItemStack) {
/* 406 */     EquipmentSlot slot = getEquipmentSlotForItem(newItemStack);
/* 407 */     ItemStack currentItemStackInCorrespondingSlot = getItemBySlot(slot);
/* 408 */     return canReplaceCurrentItem(newItemStack, currentItemStackInCorrespondingSlot, slot);
/*     */   }
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
/* 413 */     if (EnchantmentHelper.has(currentItemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE))
/* 414 */       return false; 
/* 417 */     TagKey<Item> preferredWeaponType = getPreferredWeaponType();
/* 421 */     boolean newItemWanted = (PiglinAi.isLovedItem(newItemStack) || (preferredWeaponType != null && newItemStack.is(preferredWeaponType)));
/* 422 */     boolean currentItemWanted = (PiglinAi.isLovedItem(currentItemStack) || (preferredWeaponType != null && currentItemStack.is(preferredWeaponType)));
/* 426 */     if (newItemWanted && !currentItemWanted)
/* 427 */       return true; 
/* 429 */     if (!newItemWanted && currentItemWanted)
/* 430 */       return false; 
/* 434 */     return super.canReplaceCurrentItem(newItemStack, currentItemStack, slot);
/*     */   }
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 439 */     onItemPickup(entity);
/* 440 */     PiglinAi.pickUpItem(level, this, entity);
/*     */   }
/*     */   
/*     */   public boolean startRiding(Entity entityToRide, boolean force, boolean sendEventAndTriggers) {
/* 445 */     if (isBaby() && entityToRide.is(EntityTypes.HOGLIN))
/* 446 */       entityToRide = getTopPassenger(entityToRide, 3); 
/* 448 */     return super.startRiding(entityToRide, force, sendEventAndTriggers);
/*     */   }
/*     */   
/*     */   private Entity getTopPassenger(Entity vehicle, int counter) {
/* 452 */     List<Entity> passengers = vehicle.getPassengers();
/* 453 */     if (counter == 1 || passengers.isEmpty())
/* 454 */       return vehicle; 
/* 456 */     return getTopPassenger(passengers.getFirst(), counter - 1);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 462 */     if (level().isClientSide())
/* 463 */       return null; 
/* 465 */     return PiglinAi.getSoundForCurrentActivity(this).orElse(null);
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 470 */     return SoundEvents.PIGLIN_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 475 */     return SoundEvents.PIGLIN_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 480 */     playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playConvertedSound() {
/* 485 */     makeSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\piglin\Piglin.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */