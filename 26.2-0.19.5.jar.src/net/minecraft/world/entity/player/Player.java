/*      */ package net.minecraft.world.entity.player;
/*      */ 
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.math.IntMath;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.OptionalInt;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.NbtUtils;
/*      */ import net.minecraft.network.chat.ClickEvent;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.dialog.Dialog;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.server.permissions.Permissions;
/*      */ import net.minecraft.server.players.NameAndId;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.Stat;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.DamageTypeTags;
/*      */ import net.minecraft.tags.EntityTypeTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Unit;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.ItemStackWithSlot;
/*      */ import net.minecraft.world.MenuProvider;
/*      */ import net.minecraft.world.attribute.BedRule;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffectUtil;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Avatar;
/*      */ import net.minecraft.world.entity.ContainerUser;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityEquipment;
/*      */ import net.minecraft.world.entity.EntityReference;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.MoverType;
/*      */ import net.minecraft.world.entity.Pose;
/*      */ import net.minecraft.world.entity.SlotAccess;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*      */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*      */ import net.minecraft.world.entity.animal.parrot.Parrot;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
/*      */ import net.minecraft.world.entity.boss.enderdragon.EnderDragonPart;
/*      */ import net.minecraft.world.entity.decoration.ArmorStand;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.monster.warden.WardenSpawnTracker;
/*      */ import net.minecraft.world.entity.projectile.FishingHook;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*      */ import net.minecraft.world.entity.vehicle.minecart.MinecartCommandBlock;
/*      */ import net.minecraft.world.food.FoodData;
/*      */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*      */ import net.minecraft.world.inventory.ClickAction;
/*      */ import net.minecraft.world.inventory.InventoryMenu;
/*      */ import net.minecraft.world.inventory.PlayerEnderChestContainer;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemCooldowns;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.ProjectileWeaponItem;
/*      */ import net.minecraft.world.item.component.BlocksAttacks;
/*      */ import net.minecraft.world.item.component.ResolvableProfile;
/*      */ import net.minecraft.world.item.crafting.Recipe;
/*      */ import net.minecraft.world.item.crafting.RecipeHolder;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.item.trading.MerchantOffers;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.entity.CommandBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
/*      */ import net.minecraft.world.level.block.entity.JigsawBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.SignBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.StructureBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.pattern.BlockInWorld;
/*      */ import net.minecraft.world.level.entity.UniquelyIdentifyable;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.Team;
/*      */ 
/*      */ public abstract class Player extends Avatar implements ContainerUser {
/*      */   public static final int MAX_HEALTH = 20;
/*      */   
/*      */   public static final int SLEEP_DURATION = 100;
/*      */   
/*      */   public static final int WAKE_UP_DURATION = 10;
/*      */   
/*      */   public static final int ENDER_SLOT_OFFSET = 200;
/*      */   
/*      */   public static final int HELD_ITEM_SLOT = 499;
/*      */   
/*      */   public static final int CRAFTING_SLOT_OFFSET = 500;
/*      */   
/*      */   public static final float DEFAULT_BLOCK_INTERACTION_RANGE = 4.5F;
/*      */   
/*      */   public static final float DEFAULT_ENTITY_INTERACTION_RANGE = 3.0F;
/*      */   
/*  143 */   private static final EntityDataAccessor<Float> DATA_PLAYER_ABSORPTION_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.FLOAT);
/*      */   
/*  144 */   private static final EntityDataAccessor<Integer> DATA_SCORE_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
/*      */   
/*  146 */   private static final EntityDataAccessor<OptionalInt> DATA_SHOULDER_PARROT_LEFT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
/*      */   
/*  147 */   private static final EntityDataAccessor<OptionalInt> DATA_SHOULDER_PARROT_RIGHT = SynchedEntityData.defineId(Player.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
/*      */   
/*      */   private static final short DEFAULT_SLEEP_TIMER = 0;
/*      */   
/*      */   private static final float DEFAULT_EXPERIENCE_PROGRESS = 0.0F;
/*      */   
/*      */   private static final int DEFAULT_EXPERIENCE_LEVEL = 0;
/*      */   
/*      */   private static final int DEFAULT_TOTAL_EXPERIENCE = 0;
/*      */   
/*      */   private static final int NO_ENCHANTMENT_SEED = 0;
/*      */   
/*      */   private static final int DEFAULT_SELECTED_SLOT = 0;
/*      */   
/*      */   private static final int DEFAULT_SCORE = 0;
/*      */   
/*      */   public static final float CREATIVE_ENTITY_INTERACTION_RANGE_MODIFIER_VALUE = 2.0F;
/*      */   
/*      */   private final Inventory inventory;
/*      */   
/*  159 */   protected PlayerEnderChestContainer enderChestInventory = new PlayerEnderChestContainer();
/*      */   
/*      */   public final InventoryMenu inventoryMenu;
/*      */   
/*      */   public AbstractContainerMenu containerMenu;
/*      */   
/*  163 */   protected FoodData foodData = new FoodData();
/*      */   
/*      */   protected int jumpTriggerTime;
/*      */   
/*      */   public int takeXpDelay;
/*      */   
/*  169 */   private int sleepCounter = 0;
/*      */   
/*      */   protected boolean wasUnderwater;
/*      */   
/*  173 */   private final Abilities abilities = new Abilities();
/*      */   
/*  175 */   public int experienceLevel = 0;
/*      */   
/*  176 */   public int totalExperience = 0;
/*      */   
/*  177 */   public float experienceProgress = 0.0F;
/*      */   
/*  178 */   protected int enchantmentSeed = 0;
/*      */   
/*  180 */   protected final float defaultFlySpeed = 0.02F;
/*      */   
/*      */   private int lastLevelUpTime;
/*      */   
/*      */   private final GameProfile gameProfile;
/*      */   
/*      */   private boolean reducedDebugInfo;
/*      */   
/*  184 */   private ItemStack lastItemInMainHand = ItemStack.EMPTY;
/*      */   
/*  185 */   private final ItemCooldowns cooldowns = createItemCooldowns();
/*      */   
/*  186 */   private Optional<GlobalPos> lastDeathLocation = Optional.empty();
/*      */   
/*      */   public FishingHook fishing;
/*      */   
/*      */   protected float hurtDir;
/*      */   
/*      */   public Player(Level level, GameProfile gameProfile) {
/*  192 */     super(EntityTypes.PLAYER, level);
/*  193 */     setUUID(gameProfile.id());
/*  195 */     this.gameProfile = gameProfile;
/*  197 */     this.inventory = new Inventory(this, this.equipment);
/*  198 */     this.inventoryMenu = new InventoryMenu(this.inventory, !level.isClientSide(), this);
/*  199 */     this.containerMenu = (AbstractContainerMenu)this.inventoryMenu;
/*      */   }
/*      */   
/*      */   protected EntityEquipment createEquipment() {
/*  204 */     return new PlayerEquipment(this);
/*      */   }
/*      */   
/*      */   public boolean blockActionRestricted(Level level, BlockPos pos, GameType gameType) {
/*  208 */     if (!gameType.isBlockPlacingRestricted())
/*  209 */       return false; 
/*  211 */     if (gameType == GameType.SPECTATOR)
/*  212 */       return true; 
/*  214 */     if (mayBuild())
/*  215 */       return false; 
/*  217 */     ItemStack itemStack = getMainHandItem();
/*  218 */     return (itemStack.isEmpty() || !itemStack.canBreakBlockInAdventureMode(new BlockInWorld((LevelReader)level, pos, false)));
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  222 */     return LivingEntity.createLivingAttributes()
/*  223 */       .add(Attributes.ATTACK_DAMAGE, 1.0D)
/*  224 */       .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612D)
/*  225 */       .add(Attributes.ATTACK_SPEED)
/*  226 */       .add(Attributes.LUCK)
/*  227 */       .add(Attributes.BLOCK_INTERACTION_RANGE)
/*  228 */       .add(Attributes.BLOCK_BREAK_SPEED)
/*  229 */       .add(Attributes.SUBMERGED_MINING_SPEED)
/*  230 */       .add(Attributes.SNEAKING_SPEED)
/*  231 */       .add(Attributes.MINING_EFFICIENCY)
/*  232 */       .add(Attributes.SWEEPING_DAMAGE_RATIO)
/*  233 */       .add(Attributes.WAYPOINT_TRANSMIT_RANGE, 6.0E7D)
/*  234 */       .add(Attributes.WAYPOINT_RECEIVE_RANGE, 6.0E7D);
/*      */   }
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  240 */     super.defineSynchedData(entityData);
/*  242 */     entityData.define(DATA_PLAYER_ABSORPTION_ID, Float.valueOf(0.0F));
/*  243 */     entityData.define(DATA_SCORE_ID, Integer.valueOf(0));
/*  244 */     entityData.define(DATA_SHOULDER_PARROT_LEFT, OptionalInt.empty());
/*  245 */     entityData.define(DATA_SHOULDER_PARROT_RIGHT, OptionalInt.empty());
/*      */   }
/*      */   
/*      */   public void tick() {
/*  250 */     this.noPhysics = isSpectator();
/*  251 */     if (isSpectator() || isPassenger())
/*  252 */       setOnGround(false); 
/*  255 */     if (this.takeXpDelay > 0)
/*  256 */       this.takeXpDelay--; 
/*  258 */     if (isSleeping()) {
/*  259 */       this.sleepCounter++;
/*  260 */       if (this.sleepCounter > 100)
/*  261 */         this.sleepCounter = 100; 
/*  264 */       if (!level().isClientSide() && !((BedRule)level().environmentAttributes().getValue(EnvironmentAttributes.BED_RULE, position())).canSleep(level()))
/*  265 */         stopSleepInBed(false, true); 
/*  267 */     } else if (this.sleepCounter > 0) {
/*  268 */       this.sleepCounter++;
/*  269 */       if (this.sleepCounter >= 110)
/*  270 */         this.sleepCounter = 0; 
/*      */     } 
/*  274 */     updateIsUnderwater();
/*  276 */     super.tick();
/*  278 */     int maxPositionOffset = 29999999;
/*  279 */     double nx = Mth.clamp(getX(), -2.9999999E7D, 2.9999999E7D);
/*  280 */     double nz = Mth.clamp(getZ(), -2.9999999E7D, 2.9999999E7D);
/*  281 */     if (nx != getX() || nz != getZ())
/*  282 */       setPos(nx, getY(), nz); 
/*  285 */     this.attackStrengthTicker++;
/*  286 */     this.itemSwapTicker++;
/*  288 */     ItemStack mainHandItemStack = getMainHandItem();
/*  289 */     if (!ItemStack.matches(this.lastItemInMainHand, mainHandItemStack)) {
/*  293 */       if (!ItemStack.isSameItem(this.lastItemInMainHand, mainHandItemStack))
/*  294 */         resetAttackStrengthTicker(); 
/*  296 */       this.lastItemInMainHand = mainHandItemStack.copy();
/*      */     } 
/*  299 */     if (!isEyeInFluid(FluidTags.WATER) && isEquipped(Items.TURTLE_HELMET))
/*  300 */       turtleHelmetTick(); 
/*  302 */     this.cooldowns.tick();
/*  303 */     updatePlayerPose();
/*      */   }
/*      */   
/*      */   protected float getMaxHeadRotationRelativeToBody() {
/*  308 */     if (isBlocking())
/*  309 */       return 15.0F; 
/*  311 */     return super.getMaxHeadRotationRelativeToBody();
/*      */   }
/*      */   
/*      */   public boolean isSecondaryUseActive() {
/*  319 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   protected boolean wantsToStopRiding() {
/*  323 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   protected boolean isStayingOnGroundSurface() {
/*  329 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   protected boolean updateIsUnderwater() {
/*  333 */     this.wasUnderwater = isEyeInFluid(FluidTags.WATER);
/*  334 */     return this.wasUnderwater;
/*      */   }
/*      */   
/*      */   public void onAboveBubbleColumn(boolean dragDown, BlockPos pos) {
/*  339 */     if (!(getAbilities()).flying)
/*  340 */       super.onAboveBubbleColumn(dragDown, pos); 
/*      */   }
/*      */   
/*      */   public void onInsideBubbleColumn(boolean dragDown) {
/*  346 */     if (!(getAbilities()).flying)
/*  347 */       super.onInsideBubbleColumn(dragDown); 
/*      */   }
/*      */   
/*      */   private void turtleHelmetTick() {
/*  352 */     addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false, true));
/*      */   }
/*      */   
/*      */   private boolean isEquipped(Item item) {
/*  356 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/*  357 */       ItemStack itemStack = getItemBySlot(slot);
/*  358 */       Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/*  359 */       if (itemStack.is(item) && equippable != null && equippable.slot() == slot)
/*  360 */         return true; 
/*      */     } 
/*  363 */     return false;
/*      */   }
/*      */   
/*      */   protected ItemCooldowns createItemCooldowns() {
/*  367 */     return new ItemCooldowns();
/*      */   }
/*      */   
/*      */   protected void updatePlayerPose() {
/*      */     Pose actualPose;
/*  371 */     if (!canPlayerFitWithinBlocksAndEntitiesWhen(Pose.SWIMMING))
/*      */       return; 
/*  379 */     Pose desiredPose = getDesiredPose();
/*  381 */     if (isSpectator() || isPassenger() || canPlayerFitWithinBlocksAndEntitiesWhen(desiredPose)) {
/*  382 */       actualPose = desiredPose;
/*  383 */     } else if (canPlayerFitWithinBlocksAndEntitiesWhen(Pose.CROUCHING)) {
/*  385 */       actualPose = Pose.CROUCHING;
/*      */     } else {
/*  388 */       actualPose = Pose.SWIMMING;
/*      */     } 
/*  390 */     setPose(actualPose);
/*      */   }
/*      */   
/*      */   private Pose getDesiredPose() {
/*  394 */     if (isSleeping())
/*  395 */       return Pose.SLEEPING; 
/*  396 */     if (isSwimming())
/*  397 */       return Pose.SWIMMING; 
/*  398 */     if (isFallFlying())
/*  399 */       return Pose.FALL_FLYING; 
/*  400 */     if (isAutoSpinAttack())
/*  401 */       return Pose.SPIN_ATTACK; 
/*  402 */     if (isShiftKeyDown() && !this.abilities.flying)
/*  403 */       return Pose.CROUCHING; 
/*  405 */     return Pose.STANDING;
/*      */   }
/*      */   
/*      */   protected boolean canPlayerFitWithinBlocksAndEntitiesWhen(Pose newPose) {
/*  409 */     return level().noCollision((Entity)this, getDimensions(newPose).makeBoundingBox(position()).deflate(1.0E-7D));
/*      */   }
/*      */   
/*      */   protected SoundEvent getSwimSound() {
/*  414 */     return SoundEvents.PLAYER_SWIM;
/*      */   }
/*      */   
/*      */   protected SoundEvent getSwimSplashSound() {
/*  419 */     return SoundEvents.PLAYER_SPLASH;
/*      */   }
/*      */   
/*      */   protected SoundEvent getSwimHighSpeedSplashSound() {
/*  424 */     return SoundEvents.PLAYER_SPLASH_HIGH_SPEED;
/*      */   }
/*      */   
/*      */   public int getDimensionChangingDelay() {
/*  429 */     return 10;
/*      */   }
/*      */   
/*      */   public void playSound(SoundEvent sound, float volume, float pitch) {
/*  435 */     level().playSound((Entity)this, getX(), getY(), getZ(), sound, getSoundSource(), volume, pitch);
/*      */   }
/*      */   
/*      */   public SoundSource getSoundSource() {
/*  440 */     return SoundSource.PLAYERS;
/*      */   }
/*      */   
/*      */   protected int getFireImmuneTicks() {
/*  445 */     return 20;
/*      */   }
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  450 */     if (id == 9) {
/*  451 */       completeUsingItem();
/*  452 */     } else if (id == 23) {
/*  453 */       setReducedDebugInfo(false);
/*  454 */     } else if (id == 22) {
/*  455 */       setReducedDebugInfo(true);
/*      */     } else {
/*  457 */       super.handleEntityEvent(id);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void closeContainer() {
/*  462 */     this.containerMenu = (AbstractContainerMenu)this.inventoryMenu;
/*      */   }
/*      */   
/*      */   protected void doCloseContainer() {}
/*      */   
/*      */   public void rideTick() {
/*  470 */     if (!level().isClientSide() && wantsToStopRiding() && isPassenger()) {
/*  471 */       stopRiding();
/*      */       return;
/*      */     } 
/*  474 */     super.rideTick();
/*      */   }
/*      */   
/*      */   public void aiStep() {
/*  479 */     if (this.jumpTriggerTime > 0)
/*  480 */       this.jumpTriggerTime--; 
/*  483 */     tickRegeneration();
/*  484 */     this.inventory.tick();
/*  486 */     if (this.abilities.flying && !isPassenger())
/*  487 */       resetFallDistance(); 
/*  490 */     super.aiStep();
/*  491 */     updateSwingTime();
/*  493 */     this.yHeadRot = getYRot();
/*  495 */     setSpeed((float)getAttributeValue(Attributes.MOVEMENT_SPEED));
/*  497 */     if (getHealth() > 0.0F && !isSpectator()) {
/*      */       AABB pickupArea;
/*  499 */       if (isPassenger() && !getVehicle().isRemoved()) {
/*  501 */         pickupArea = getBoundingBox().minmax(getVehicle().getBoundingBox()).inflate(1.0D, 0.0D, 1.0D);
/*      */       } else {
/*  503 */         pickupArea = getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
/*      */       } 
/*  506 */       List<Entity> entities = level().getEntities((Entity)this, pickupArea);
/*  507 */       List<Entity> orbs = Lists.newArrayList();
/*  508 */       for (Entity entity : entities) {
/*  509 */         if (entity.is(EntityTypes.EXPERIENCE_ORB)) {
/*  510 */           orbs.add(entity);
/*      */           continue;
/*      */         } 
/*  511 */         if (!entity.isRemoved())
/*  512 */           touch(entity); 
/*      */       } 
/*  515 */       if (!orbs.isEmpty())
/*  516 */         touch((Entity)Util.getRandom(orbs, this.random)); 
/*      */     } 
/*  520 */     handleShoulderEntities();
/*      */   }
/*      */   
/*      */   protected void tickRegeneration() {}
/*      */   
/*      */   public void handleShoulderEntities() {}
/*      */   
/*      */   protected void removeEntitiesOnShoulder() {}
/*      */   
/*      */   private void touch(Entity entity) {
/*  533 */     entity.playerTouch(this);
/*      */   }
/*      */   
/*      */   public int getScore() {
/*  537 */     return ((Integer)this.entityData.get(DATA_SCORE_ID)).intValue();
/*      */   }
/*      */   
/*      */   public void setScore(int value) {
/*  541 */     this.entityData.set(DATA_SCORE_ID, Integer.valueOf(value));
/*      */   }
/*      */   
/*      */   public void increaseScore(int amount) {
/*  545 */     int score = getScore();
/*  546 */     this.entityData.set(DATA_SCORE_ID, Integer.valueOf(score + amount));
/*      */   }
/*      */   
/*      */   public void startAutoSpinAttack(int activationTicks, float dmg, ItemStack itemStackUsed) {
/*  550 */     this.autoSpinAttackTicks = activationTicks;
/*  551 */     this.autoSpinAttackDmg = dmg;
/*  552 */     this.autoSpinAttackItemStack = itemStackUsed;
/*  553 */     if (!level().isClientSide()) {
/*  554 */       removeEntitiesOnShoulder();
/*  555 */       setLivingEntityFlag(4, true);
/*      */     } 
/*      */   }
/*      */   
/*      */   public ItemStack getWeaponItem() {
/*  561 */     if (isAutoSpinAttack() && this.autoSpinAttackItemStack != null)
/*  562 */       return this.autoSpinAttackItemStack; 
/*  564 */     return super.getWeaponItem();
/*      */   }
/*      */   
/*      */   public void die(DamageSource source) {
/*  570 */     super.die(source);
/*  571 */     reapplyPosition();
/*  573 */     if (!isSpectator()) {
/*  573 */       Level level = level();
/*  573 */       if (level instanceof ServerLevel) {
/*  573 */         ServerLevel serverLevel = (ServerLevel)level;
/*  574 */         dropAllDeathLoot(serverLevel, source);
/*      */       } 
/*      */     } 
/*  577 */     if (source != null) {
/*  578 */       setDeltaMovement((
/*  579 */           -Mth.cos(((getHurtDir() + getYRot()) * 0.017453292F)) * 0.1F), 0.10000000149011612D, (
/*      */           
/*  581 */           -Mth.sin(((getHurtDir() + getYRot()) * 0.017453292F)) * 0.1F));
/*      */     } else {
/*  584 */       setDeltaMovement(0.0D, 0.1D, 0.0D);
/*      */     } 
/*  587 */     awardStat(Stats.DEATHS);
/*  588 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_DEATH));
/*  589 */     resetStat(Stats.CUSTOM.get(Stats.TIME_SINCE_REST));
/*  590 */     clearFire();
/*  591 */     setSharedFlagOnFire(false);
/*  592 */     setLastDeathLocation(Optional.of(GlobalPos.of(level().dimension(), blockPosition())));
/*      */   }
/*      */   
/*      */   protected void dropEquipment(ServerLevel level) {
/*  597 */     super.dropEquipment(level);
/*  598 */     if (!((Boolean)level.getGameRules().get(GameRules.KEEP_INVENTORY)).booleanValue()) {
/*  599 */       destroyVanishingCursedItems();
/*  600 */       this.inventory.dropAll();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void destroyVanishingCursedItems() {
/*  605 */     for (int i = 0; i < this.inventory.getContainerSize(); i++) {
/*  606 */       ItemStack itemStack = this.inventory.getItem(i);
/*  607 */       if (!itemStack.isEmpty() && EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP))
/*  608 */         this.inventory.removeItemNoUpdate(i); 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource source) {
/*  615 */     return source.type().effects().sound();
/*      */   }
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/*  620 */     return SoundEvents.PLAYER_DEATH;
/*      */   }
/*      */   
/*      */   public void handleCreativeModeItemDrop(ItemStack stack) {}
/*      */   
/*      */   public ItemEntity drop(ItemStack itemStack, boolean thrownFromHand) {
/*  627 */     return drop(itemStack, false, thrownFromHand);
/*      */   }
/*      */   
/*      */   public float getDestroySpeed(BlockState state) {
/*  631 */     float speed = this.inventory.getSelectedItem().getDestroySpeed(state);
/*  632 */     if (speed > 1.0F)
/*  633 */       speed += (float)getAttributeValue(Attributes.MINING_EFFICIENCY); 
/*  636 */     if (MobEffectUtil.hasDigSpeed((LivingEntity)this))
/*  637 */       speed *= 1.0F + (MobEffectUtil.getDigSpeedAmplification((LivingEntity)this) + 1) * 0.2F; 
/*  639 */     if (hasEffect(MobEffects.MINING_FATIGUE)) {
/*  642 */       switch (getEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
/*      */         case 0:
/*      */         
/*      */         case 1:
/*      */         
/*      */         case 2:
/*      */         
/*      */         default:
/*      */           break;
/*      */       } 
/*  647 */       float scale = 8.1E-4F;
/*  648 */       speed *= scale;
/*      */     } 
/*  651 */     speed *= (float)getAttributeValue(Attributes.BLOCK_BREAK_SPEED);
/*  653 */     if (isEyeInFluid(FluidTags.WATER))
/*  654 */       speed *= (float)getAttribute(Attributes.SUBMERGED_MINING_SPEED).getValue(); 
/*  656 */     if (!onGround())
/*  657 */       speed /= 5.0F; 
/*  660 */     return speed;
/*      */   }
/*      */   
/*      */   public boolean hasCorrectToolForDrops(BlockState state) {
/*  664 */     return (!state.requiresCorrectToolForDrops() || this.inventory.getSelectedItem().isCorrectToolForDrops(state));
/*      */   }
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  669 */     super.readAdditionalSaveData(input);
/*  671 */     setUUID(this.gameProfile.id());
/*  672 */     this.inventory.load(input.listOrEmpty("Inventory", ItemStackWithSlot.CODEC));
/*  673 */     this.inventory.setSelectedSlot(input.getIntOr("SelectedItemSlot", 0));
/*  674 */     this.sleepCounter = input.getShortOr("SleepTimer", (short)0);
/*  676 */     this.experienceProgress = input.getFloatOr("XpP", 0.0F);
/*  677 */     this.experienceLevel = input.getIntOr("XpLevel", 0);
/*  678 */     this.totalExperience = input.getIntOr("XpTotal", 0);
/*  679 */     this.enchantmentSeed = input.getIntOr("XpSeed", 0);
/*  680 */     if (this.enchantmentSeed == 0)
/*  681 */       this.enchantmentSeed = this.random.nextInt(); 
/*  683 */     setScore(input.getIntOr("Score", 0));
/*  685 */     this.foodData.readAdditionalSaveData(input);
/*  687 */     Objects.requireNonNull(this.abilities);
/*  687 */     input.read("abilities", Abilities.Packed.CODEC).ifPresent(this.abilities::apply);
/*  689 */     getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(this.abilities.getWalkingSpeed());
/*  691 */     this.enderChestInventory.fromSlots(input.listOrEmpty("EnderItems", ItemStackWithSlot.CODEC));
/*  693 */     setLastDeathLocation(input.read("LastDeathLocation", GlobalPos.CODEC));
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  698 */     super.addAdditionalSaveData(output);
/*  699 */     NbtUtils.addCurrentDataVersion(output);
/*  700 */     this.inventory.save(output.list("Inventory", ItemStackWithSlot.CODEC));
/*  701 */     output.putInt("SelectedItemSlot", this.inventory.getSelectedSlot());
/*  702 */     output.putShort("SleepTimer", (short)this.sleepCounter);
/*  703 */     output.putFloat("XpP", this.experienceProgress);
/*  704 */     output.putInt("XpLevel", this.experienceLevel);
/*  705 */     output.putInt("XpTotal", this.totalExperience);
/*  706 */     output.putInt("XpSeed", this.enchantmentSeed);
/*  707 */     output.putInt("Score", getScore());
/*  709 */     this.foodData.addAdditionalSaveData(output);
/*  711 */     output.store("abilities", Abilities.Packed.CODEC, this.abilities.pack());
/*  712 */     this.enderChestInventory.storeAsSlots(output.list("EnderItems", ItemStackWithSlot.CODEC));
/*  714 */     this.lastDeathLocation.ifPresent(pos -> output.store("LastDeathLocation", GlobalPos.CODEC, pos));
/*      */   }
/*      */   
/*      */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
/*  719 */     if (super.isInvulnerableTo(level, source))
/*  720 */       return true; 
/*  723 */     if (source.is(DamageTypeTags.IS_DROWNING))
/*  724 */       return !((Boolean)level.getGameRules().get(GameRules.DROWNING_DAMAGE)).booleanValue(); 
/*  725 */     if (source.is(DamageTypeTags.IS_FALL))
/*  726 */       return !((Boolean)level.getGameRules().get(GameRules.FALL_DAMAGE)).booleanValue(); 
/*  727 */     if (source.is(DamageTypeTags.IS_FIRE))
/*  728 */       return !((Boolean)level.getGameRules().get(GameRules.FIRE_DAMAGE)).booleanValue(); 
/*  729 */     if (source.is(DamageTypeTags.IS_FREEZING))
/*  730 */       return !((Boolean)level.getGameRules().get(GameRules.FREEZE_DAMAGE)).booleanValue(); 
/*  732 */     return false;
/*      */   }
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  737 */     if (isInvulnerableTo(level, source))
/*  738 */       return false; 
/*  740 */     if (this.abilities.invulnerable && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
/*  741 */       return false; 
/*  744 */     this.noActionTime = 0;
/*  745 */     if (isDeadOrDying())
/*  746 */       return false; 
/*  749 */     removeEntitiesOnShoulder();
/*  751 */     if (source.scalesWithDifficulty()) {
/*  752 */       if (level.getDifficulty() == Difficulty.PEACEFUL)
/*  753 */         damage = 0.0F; 
/*  755 */       if (level.getDifficulty() == Difficulty.EASY)
/*  756 */         damage = Math.min(damage / 2.0F + 1.0F, damage); 
/*  758 */       if (level.getDifficulty() == Difficulty.HARD)
/*  759 */         damage = damage * 3.0F / 2.0F; 
/*      */     } 
/*  763 */     if (damage == 0.0F)
/*  764 */       return false; 
/*  767 */     return super.hurtServer(level, source, damage);
/*      */   }
/*      */   
/*      */   protected void blockUsingItem(ServerLevel level, LivingEntity attacker, DamageSource source, float damage) {
/*  772 */     super.blockUsingItem(level, attacker, source, damage);
/*  774 */     ItemStack itemBlockingWith = getItemBlockingWith();
/*  775 */     BlocksAttacks blocksAttacks = (itemBlockingWith != null) ? (BlocksAttacks)itemBlockingWith.get(DataComponents.BLOCKS_ATTACKS) : null;
/*  776 */     float secondsToDisableBlocking = attacker.getSecondsToDisableBlocking();
/*  777 */     if (secondsToDisableBlocking > 0.0F && blocksAttacks != null)
/*  778 */       blocksAttacks.disable(level, (LivingEntity)this, secondsToDisableBlocking, itemBlockingWith); 
/*      */   }
/*      */   
/*      */   public boolean canBeSeenAsEnemy() {
/*  784 */     return (!(getAbilities()).invulnerable && super.canBeSeenAsEnemy());
/*      */   }
/*      */   
/*      */   public boolean canHarmPlayer(Player target) {
/*  788 */     PlayerTeam playerTeam1 = getTeam();
/*  789 */     PlayerTeam playerTeam2 = target.getTeam();
/*  791 */     if (playerTeam1 == null)
/*  792 */       return true; 
/*  794 */     if (!playerTeam1.isAlliedTo((Team)playerTeam2))
/*  795 */       return true; 
/*  797 */     return playerTeam1.isAllowFriendlyFire();
/*      */   }
/*      */   
/*      */   protected void hurtArmor(DamageSource damageSource, float damage) {
/*  802 */     doHurtEquipment(damageSource, damage, new EquipmentSlot[] { EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD });
/*      */   }
/*      */   
/*      */   protected void hurtHelmet(DamageSource damageSource, float damage) {
/*  807 */     doHurtEquipment(damageSource, damage, new EquipmentSlot[] { EquipmentSlot.HEAD });
/*      */   }
/*      */   
/*      */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/*  812 */     if (isInvulnerableTo(level, source))
/*      */       return; 
/*  815 */     dmg = getDamageAfterArmorAbsorb(source, dmg);
/*  816 */     dmg = getDamageAfterMagicAbsorb(source, dmg);
/*  818 */     float originalDamage = dmg;
/*  819 */     dmg = Math.max(dmg - getAbsorptionAmount(), 0.0F);
/*  820 */     setAbsorptionAmount(getAbsorptionAmount() - originalDamage - dmg);
/*  822 */     float absorbedDamage = originalDamage - dmg;
/*  823 */     if (absorbedDamage > 0.0F && absorbedDamage < 3.4028235E37F)
/*  824 */       awardStat(Stats.DAMAGE_ABSORBED, Math.round(absorbedDamage * 10.0F)); 
/*  827 */     if (dmg == 0.0F)
/*      */       return; 
/*  831 */     causeFoodExhaustion(source.getFoodExhaustion());
/*  832 */     getCombatTracker().recordDamage(source, dmg);
/*  833 */     setHealth(getHealth() - dmg);
/*  834 */     if (dmg < 3.4028235E37F)
/*  835 */       awardStat(Stats.DAMAGE_TAKEN, Math.round(dmg * 10.0F)); 
/*  837 */     gameEvent((Holder)GameEvent.ENTITY_DAMAGE);
/*      */   }
/*      */   
/*      */   public boolean isTextFilteringEnabled() {
/*  841 */     return false;
/*      */   }
/*      */   
/*      */   public void openTextEdit(SignBlockEntity sign, boolean isFrontText) {}
/*      */   
/*      */   public void openMinecartCommandBlock(MinecartCommandBlock commandBlock) {}
/*      */   
/*      */   public void openCommandBlock(CommandBlockEntity commandBlock) {}
/*      */   
/*      */   public void openStructureBlock(StructureBlockEntity structureBlock) {}
/*      */   
/*      */   public void openTestBlock(TestBlockEntity testBlock) {}
/*      */   
/*      */   public void openTestInstanceBlock(TestInstanceBlockEntity testInstanceBlock) {}
/*      */   
/*      */   public void openJigsawBlock(JigsawBlockEntity jigsawBlock) {}
/*      */   
/*      */   public void openHorseInventory(AbstractHorse horse, Container container) {}
/*      */   
/*      */   public void openNautilusInventory(AbstractNautilus nautilus, Container container) {}
/*      */   
/*      */   public OptionalInt openMenu(MenuProvider provider) {
/*  872 */     return OptionalInt.empty();
/*      */   }
/*      */   
/*      */   public void openDialog(Holder<Dialog> dialog) {}
/*      */   
/*      */   public void sendMerchantOffers(int containerId, MerchantOffers offers, int merchantLevel, int merchantXp, boolean showProgressBar, boolean canRestock) {}
/*      */   
/*      */   public void openItemGui(ItemStack itemStack, InteractionHand hand) {}
/*      */   
/*      */   public InteractionResult interactOn(Entity entity, InteractionHand hand, Vec3 location) {
/*  889 */     if (isSpectator()) {
/*  890 */       if (entity instanceof MenuProvider) {
/*  890 */         MenuProvider menuProvider = (MenuProvider)entity;
/*  891 */         openMenu(menuProvider);
/*      */       } 
/*  893 */       return (InteractionResult)InteractionResult.PASS;
/*      */     } 
/*  896 */     ItemStack itemStack = getItemInHand(hand);
/*  898 */     ItemStack itemStackClone = itemStack.copy();
/*  899 */     InteractionResult interact = entity.interact(this, hand, location);
/*  900 */     if (interact.consumesAction()) {
/*  901 */       if (hasInfiniteMaterials() && itemStack == getItemInHand(hand) && itemStack.getCount() < itemStackClone.getCount())
/*  902 */         itemStack.setCount(itemStackClone.getCount()); 
/*  904 */       return interact;
/*      */     } 
/*  907 */     if (!itemStack.isEmpty() && entity instanceof LivingEntity) {
/*  907 */       LivingEntity livingEntity = (LivingEntity)entity;
/*  909 */       if (hasInfiniteMaterials())
/*  910 */         itemStack = itemStackClone; 
/*  912 */       InteractionResult interactionResult = itemStack.interactLivingEntity(this, livingEntity, hand);
/*  913 */       if (interactionResult.consumesAction()) {
/*  914 */         level().gameEvent((Holder)GameEvent.ENTITY_INTERACT, entity.position(), GameEvent.Context.of((Entity)this));
/*  916 */         if (itemStack.isEmpty() && !hasInfiniteMaterials())
/*  917 */           setItemInHand(hand, ItemStack.EMPTY); 
/*  919 */         return interactionResult;
/*      */       } 
/*      */     } 
/*  922 */     return (InteractionResult)InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   public void removeVehicle() {
/*  927 */     super.removeVehicle();
/*  929 */     this.boardingCooldown = 0;
/*      */   }
/*      */   
/*      */   protected boolean isImmobile() {
/*  934 */     return (super.isImmobile() || isSleeping());
/*      */   }
/*      */   
/*      */   public boolean isAffectedByFluids() {
/*  939 */     return !this.abilities.flying;
/*      */   }
/*      */   
/*      */   protected Vec3 maybeBackOffFromEdge(Vec3 delta, MoverType moverType) {
/*  947 */     float maxDownStep = maxUpStep();
/*  949 */     if (this.abilities.flying || delta.y > 0.0D || (moverType != MoverType.SELF && moverType != MoverType.PLAYER) || !isStayingOnGroundSurface() || !isAboveGround(maxDownStep))
/*  950 */       return delta; 
/*  953 */     double deltaX = delta.x;
/*  954 */     double deltaZ = delta.z;
/*  955 */     double step = 0.05D;
/*  956 */     double stepX = Math.signum(deltaX) * 0.05D;
/*  957 */     double stepZ = Math.signum(deltaZ) * 0.05D;
/*  960 */     while (deltaX != 0.0D && canFallAtLeast(deltaX, 0.0D, maxDownStep)) {
/*  961 */       if (Math.abs(deltaX) <= 0.05D) {
/*  962 */         deltaX = 0.0D;
/*      */         break;
/*      */       } 
/*  965 */       deltaX -= stepX;
/*      */     } 
/*  969 */     while (deltaZ != 0.0D && canFallAtLeast(0.0D, deltaZ, maxDownStep)) {
/*  970 */       if (Math.abs(deltaZ) <= 0.05D) {
/*  971 */         deltaZ = 0.0D;
/*      */         break;
/*      */       } 
/*  974 */       deltaZ -= stepZ;
/*      */     } 
/*  978 */     while (deltaX != 0.0D && deltaZ != 0.0D && canFallAtLeast(deltaX, deltaZ, maxDownStep)) {
/*  979 */       if (Math.abs(deltaX) <= 0.05D) {
/*  980 */         deltaX = 0.0D;
/*      */       } else {
/*  982 */         deltaX -= stepX;
/*      */       } 
/*  985 */       if (Math.abs(deltaZ) <= 0.05D) {
/*  986 */         deltaZ = 0.0D;
/*      */         continue;
/*      */       } 
/*  988 */       deltaZ -= stepZ;
/*      */     } 
/*  992 */     return new Vec3(deltaX, delta.y, deltaZ);
/*      */   }
/*      */   
/*      */   private boolean isAboveGround(float maxDownStep) {
/*  996 */     return (onGround() || (this.fallDistance < maxDownStep && !canFallAtLeast(0.0D, 0.0D, maxDownStep - this.fallDistance)));
/*      */   }
/*      */   
/*      */   private boolean canFallAtLeast(double deltaX, double deltaZ, double minHeight) {
/* 1000 */     AABB boundingBox = getBoundingBox();
/* 1001 */     return level().noCollision((Entity)this, new AABB(boundingBox.minX + 1.0E-7D + deltaX, boundingBox.minY - minHeight - 1.0E-7D, boundingBox.minZ + 1.0E-7D + deltaZ, boundingBox.maxX - 1.0E-7D + deltaX, boundingBox.minY, boundingBox.maxZ - 1.0E-7D + deltaZ));
/*      */   }
/*      */   
/*      */   public void attack(Entity entity) {
/* 1008 */     if (cannotAttack(entity))
/*      */       return; 
/* 1012 */     float baseDamage = isAutoSpinAttack() ? this.autoSpinAttackDmg : (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 1013 */     ItemStack attackingItemStack = getWeaponItem();
/* 1015 */     DamageSource damageSource = createAttackSource(attackingItemStack);
/* 1017 */     float attackStrengthScale = getAttackStrengthScale(0.5F);
/* 1020 */     float magicBoost = attackStrengthScale * (getEnchantedDamage(entity, baseDamage, damageSource) - baseDamage);
/* 1021 */     baseDamage *= baseDamageScaleFactor();
/* 1023 */     onAttack();
/* 1025 */     if (deflectProjectile(entity))
/*      */       return; 
/* 1029 */     if (baseDamage > 0.0F || magicBoost > 0.0F) {
/* 1030 */       boolean knockbackAttack, fullStrengthAttack = (attackStrengthScale > 0.9F);
/* 1034 */       if (isSprinting() && fullStrengthAttack) {
/* 1035 */         playServerSideSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK);
/* 1036 */         knockbackAttack = true;
/*      */       } else {
/* 1038 */         knockbackAttack = false;
/*      */       } 
/* 1042 */       baseDamage += attackingItemStack.getItem().getAttackDamageBonus(entity, baseDamage, damageSource);
/* 1046 */       boolean criticalAttack = (fullStrengthAttack && canCriticalAttack(entity));
/* 1047 */       if (criticalAttack)
/* 1048 */         baseDamage *= 1.5F; 
/* 1050 */       float totalDamage = baseDamage + magicBoost;
/* 1052 */       boolean sweepAttack = isSweepAttack(fullStrengthAttack, criticalAttack, knockbackAttack);
/* 1054 */       float oldLivingEntityHealth = 0.0F;
/* 1055 */       if (entity instanceof LivingEntity) {
/* 1055 */         LivingEntity livingTarget = (LivingEntity)entity;
/* 1056 */         oldLivingEntityHealth = livingTarget.getHealth();
/*      */       } 
/* 1059 */       Vec3 oldMovement = entity.getDeltaMovement();
/* 1061 */       boolean wasHurt = entity.hurtOrSimulate(damageSource, totalDamage);
/* 1062 */       if (wasHurt) {
/* 1063 */         causeExtraKnockback(entity, getKnockback(entity, damageSource) + (knockbackAttack ? 0.5F : 0.0F), oldMovement, damageSource, totalDamage, true);
/* 1064 */         if (sweepAttack)
/* 1065 */           doSweepAttack(entity, baseDamage, damageSource, attackStrengthScale); 
/* 1068 */         attackVisualEffects(entity, criticalAttack, sweepAttack, fullStrengthAttack, false, magicBoost);
/* 1070 */         setLastHurtMob(entity);
/* 1072 */         itemAttackInteraction(entity, attackingItemStack, damageSource, true);
/* 1074 */         damageStatsAndHearts(entity, oldLivingEntityHealth);
/* 1076 */         causeFoodExhaustion(0.1F);
/*      */       } else {
/* 1078 */         playServerSideSound(SoundEvents.PLAYER_ATTACK_NODAMAGE);
/*      */       } 
/*      */     } 
/* 1081 */     postPiercingAttack();
/*      */   }
/*      */   
/*      */   private void playServerSideSound(SoundEvent sound) {
/* 1085 */     level().playSound(null, getX(), getY(), getZ(), sound, getSoundSource(), 1.0F, 1.0F);
/*      */   }
/*      */   
/*      */   private DamageSource createAttackSource(ItemStack attackingItemStack) {
/* 1089 */     return attackingItemStack.getDamageSource((LivingEntity)this);
/*      */   }
/*      */   
/*      */   private boolean cannotAttack(Entity entity) {
/* 1093 */     if (!entity.isAttackable())
/* 1094 */       return true; 
/* 1096 */     return entity.skipAttackInteraction((Entity)this);
/*      */   }
/*      */   
/*      */   private boolean deflectProjectile(Entity entity) {
/* 1101 */     if (entity.is(EntityTypeTags.REDIRECTABLE_PROJECTILE) && entity instanceof Projectile) {
/* 1102 */       Projectile projectile = (Projectile)entity;
/* 1102 */       if (projectile
/* 1103 */         .deflect(ProjectileDeflection.AIM_DEFLECT, (Entity)this, EntityReference.of((UniquelyIdentifyable)this), true)) {
/* 1104 */         level().playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, getSoundSource());
/* 1105 */         return true;
/*      */       } 
/*      */     } 
/* 1107 */     return false;
/*      */   }
/*      */   
/*      */   private boolean canCriticalAttack(Entity entity) {
/* 1111 */     return (this.fallDistance > 0.0D && 
/* 1112 */       !onGround() && 
/* 1113 */       !onClimbable() && 
/* 1114 */       !isInWater() && 
/* 1115 */       !isMobilityRestricted() && 
/* 1116 */       !isPassenger() && entity instanceof LivingEntity && 
/*      */       
/* 1118 */       !isSprinting());
/*      */   }
/*      */   
/*      */   private boolean isSweepAttack(boolean fullStrengthAttack, boolean criticalAttack, boolean knockbackAttack) {
/* 1122 */     if (fullStrengthAttack && !criticalAttack && !knockbackAttack && onGround()) {
/* 1124 */       double approximateSpeedSq = getKnownMovement().horizontalDistanceSqr();
/* 1125 */       double maxSpeedForSweepAttack = getSpeed() * 2.5D;
/* 1126 */       if (approximateSpeedSq < Mth.square(maxSpeedForSweepAttack))
/* 1128 */         return getItemInHand(InteractionHand.MAIN_HAND).is(ItemTags.SWORDS); 
/*      */     } 
/* 1131 */     return false;
/*      */   }
/*      */   
/*      */   private void attackVisualEffects(Entity entity, boolean criticalAttack, boolean sweepAttack, boolean fullStrengthAttack, boolean stabAttack, float magicBoost) {
/* 1135 */     if (criticalAttack) {
/* 1136 */       playServerSideSound(SoundEvents.PLAYER_ATTACK_CRIT);
/* 1137 */       crit(entity);
/*      */     } 
/* 1140 */     if (!criticalAttack && !sweepAttack && !stabAttack)
/* 1141 */       playServerSideSound(fullStrengthAttack ? SoundEvents.PLAYER_ATTACK_STRONG : SoundEvents.PLAYER_ATTACK_WEAK); 
/* 1144 */     if (magicBoost > 0.0F)
/* 1145 */       magicCrit(entity); 
/*      */   }
/*      */   
/*      */   private void damageStatsAndHearts(Entity entity, float oldLivingEntityHealth) {
/* 1150 */     if (entity instanceof LivingEntity) {
/* 1150 */       LivingEntity livingEntity = (LivingEntity)entity;
/* 1151 */       float actualDamage = oldLivingEntityHealth - livingEntity.getHealth();
/* 1153 */       awardStat(Stats.DAMAGE_DEALT, Math.round(actualDamage * 10.0F));
/* 1154 */       if (level() instanceof ServerLevel && actualDamage > 2.0F) {
/* 1155 */         int count = (int)(actualDamage * 0.5D);
/* 1156 */         ((ServerLevel)level()).sendParticles((ParticleOptions)ParticleTypes.DAMAGE_INDICATOR, entity.getX(), entity.getY(0.5D), entity.getZ(), count, 0.1D, 0.0D, 0.1D, 0.2D);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void itemAttackInteraction(Entity entity, ItemStack attackingItemStack, DamageSource damageSource, boolean applyToTarget) {
/*      */     EnderDragon enderDragon;
/* 1162 */     Entity hurtTarget = entity;
/* 1163 */     if (entity instanceof EnderDragonPart) {
/* 1163 */       EnderDragonPart enderDragonPart = (EnderDragonPart)entity;
/* 1164 */       enderDragon = enderDragonPart.parentMob;
/*      */     } 
/* 1166 */     boolean itemHurtEnemy = false;
/* 1167 */     Level level = level();
/* 1167 */     if (level instanceof ServerLevel) {
/* 1167 */       ServerLevel serverLevel = (ServerLevel)level;
/* 1168 */       if (enderDragon instanceof LivingEntity) {
/* 1168 */         LivingEntity livingTarget = (LivingEntity)enderDragon;
/* 1169 */         itemHurtEnemy = attackingItemStack.hurtEnemy(livingTarget, (LivingEntity)this);
/*      */       } 
/* 1171 */       if (applyToTarget)
/* 1172 */         EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, entity, damageSource, attackingItemStack); 
/*      */     } 
/* 1176 */     if (!level().isClientSide() && !attackingItemStack.isEmpty() && enderDragon instanceof LivingEntity) {
/* 1177 */       if (itemHurtEnemy)
/* 1178 */         attackingItemStack.postHurtEnemy((LivingEntity)enderDragon, (LivingEntity)this); 
/* 1180 */       if (attackingItemStack.isEmpty())
/* 1181 */         if (attackingItemStack == getMainHandItem()) {
/* 1182 */           setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
/*      */         } else {
/* 1184 */           setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
/*      */         }  
/*      */     } 
/*      */   }
/*      */   
/*      */   public void causeExtraKnockback(Entity entity, float knockbackAmount, Vec3 oldMovement, DamageSource damageSource, float damage, boolean comesFromEffect) {
/* 1192 */     if (knockbackAmount > 0.0F) {
/* 1193 */       if (entity instanceof LivingEntity) {
/* 1193 */         LivingEntity livingTarget = (LivingEntity)entity;
/* 1194 */         livingTarget.knockback(knockbackAmount, Mth.sin((getYRot() * 0.017453292F)), -Mth.cos((getYRot() * 0.017453292F)), damageSource, damage, comesFromEffect);
/*      */       } else {
/* 1196 */         entity.push((-Mth.sin((getYRot() * 0.017453292F)) * knockbackAmount), 0.1D, (Mth.cos((getYRot() * 0.017453292F)) * knockbackAmount));
/*      */       } 
/* 1198 */       setDeltaMovement(getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
/* 1199 */       setSprinting(false);
/*      */     } 
/* 1201 */     if (entity instanceof ServerPlayer) {
/* 1201 */       ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 1201 */       if (entity.hurtMarked) {
/* 1202 */         serverPlayer.connection.send((Packet)new ClientboundSetEntityMotionPacket(entity));
/* 1203 */         entity.hurtMarked = false;
/* 1204 */         entity.setDeltaMovement(oldMovement);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public float getVoicePitch() {
/* 1210 */     return 1.0F;
/*      */   }
/*      */   
/*      */   private void doSweepAttack(Entity entity, float baseDamage, DamageSource damageSource, float attackStrengthScale) {
/*      */     ServerLevel serverLevel;
/* 1214 */     playServerSideSound(SoundEvents.PLAYER_ATTACK_SWEEP);
/* 1215 */     Level level = level();
/* 1215 */     if (level instanceof ServerLevel) {
/* 1215 */       serverLevel = (ServerLevel)level;
/*      */     } else {
/*      */       return;
/*      */     } 
/* 1218 */     float sweepDamage = 1.0F + (float)getAttributeValue(Attributes.SWEEPING_DAMAGE_RATIO) * baseDamage;
/* 1219 */     List<LivingEntity> nearbyEntities = level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1.0D, 0.25D, 1.0D));
/* 1220 */     for (LivingEntity nearby : nearbyEntities) {
/* 1221 */       if (nearby == this || nearby == entity || isAlliedTo((Entity)nearby))
/*      */         continue; 
/* 1225 */       if (nearby instanceof ArmorStand) {
/* 1225 */         ArmorStand armorStand = (ArmorStand)nearby;
/* 1225 */         if (armorStand.isMarker())
/*      */           continue; 
/*      */       } 
/* 1229 */       if (distanceToSqr((Entity)nearby) < 9.0D) {
/* 1230 */         float enchantedDamage = getEnchantedDamage((Entity)nearby, sweepDamage, damageSource) * attackStrengthScale;
/* 1231 */         if (nearby.hurtServer(serverLevel, damageSource, enchantedDamage)) {
/* 1232 */           nearby.knockback(0.4000000059604645D, Mth.sin((getYRot() * 0.017453292F)), -Mth.cos((getYRot() * 0.017453292F)), damageSource, enchantedDamage);
/* 1233 */           EnchantmentHelper.doPostAttackEffects(serverLevel, (Entity)nearby, damageSource);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1238 */     double dx = -Mth.sin((getYRot() * 0.017453292F));
/* 1239 */     double dz = Mth.cos((getYRot() * 0.017453292F));
/* 1240 */     serverLevel.sendParticles((ParticleOptions)ParticleTypes.SWEEP_ATTACK, getX() + dx, getY(0.5D), getZ() + dz, 0, dx, 0.0D, dz, 0.0D);
/*      */   }
/*      */   
/*      */   protected float getEnchantedDamage(Entity entity, float dmg, DamageSource damageSource) {
/* 1245 */     return dmg;
/*      */   }
/*      */   
/*      */   protected void doAutoAttackOnTouch(LivingEntity entity) {
/* 1250 */     attack((Entity)entity);
/*      */   }
/*      */   
/*      */   public void crit(Entity entity) {}
/*      */   
/*      */   private float baseDamageScaleFactor() {
/* 1258 */     float attackStrengthScale = getAttackStrengthScale(0.5F);
/* 1259 */     return 0.2F + attackStrengthScale * attackStrengthScale * 0.8F;
/*      */   }
/*      */   
/*      */   public boolean stabAttack(EquipmentSlot slot, Entity target, float baseDamage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts) {
/* 1264 */     if (cannotAttack(target))
/* 1265 */       return false; 
/* 1268 */     ItemStack weaponItem = getItemBySlot(slot);
/* 1270 */     DamageSource damageSource = createAttackSource(weaponItem);
/* 1272 */     float magicBoost = getEnchantedDamage(target, baseDamage, damageSource) - baseDamage;
/* 1274 */     if (!isUsingItem() || getUsedItemHand().asEquipmentSlot() != slot) {
/* 1276 */       magicBoost *= getAttackStrengthScale(0.5F);
/* 1277 */       baseDamage *= baseDamageScaleFactor();
/*      */     } 
/* 1280 */     if (dealsKnockback && deflectProjectile(target))
/* 1281 */       return true; 
/* 1284 */     float totalDamage = dealsDamage ? (baseDamage + magicBoost) : 0.0F;
/* 1286 */     float oldLivingEntityHealth = 0.0F;
/* 1287 */     if (target instanceof LivingEntity) {
/* 1287 */       LivingEntity livingTarget = (LivingEntity)target;
/* 1288 */       oldLivingEntityHealth = livingTarget.getHealth();
/*      */     } 
/* 1291 */     Vec3 oldMovement = target.getDeltaMovement();
/* 1293 */     boolean wasHurt = (dealsDamage && target.hurtOrSimulate(damageSource, totalDamage));
/* 1294 */     if (dealsKnockback) {
/* 1297 */       causeExtraKnockback(target, 0.4F, oldMovement, damageSource, totalDamage, false);
/* 1299 */       causeExtraKnockback(target, getKnockback(target, damageSource), oldMovement, damageSource, totalDamage, true);
/*      */     } 
/* 1301 */     boolean dismounted = false;
/* 1302 */     if (dismounts && target.isPassenger()) {
/* 1303 */       dismounted = true;
/* 1304 */       target.stopRiding();
/*      */     } 
/* 1306 */     if (!wasHurt && !dealsKnockback && !dismounted)
/* 1307 */       return false; 
/* 1309 */     attackVisualEffects(target, false, false, dealsDamage, true, magicBoost);
/* 1311 */     setLastHurtMob(target);
/* 1313 */     itemAttackInteraction(target, weaponItem, damageSource, wasHurt);
/* 1315 */     damageStatsAndHearts(target, oldLivingEntityHealth);
/* 1317 */     causeFoodExhaustion(0.1F);
/* 1318 */     return true;
/*      */   }
/*      */   
/*      */   public void magicCrit(Entity entity) {}
/*      */   
/*      */   public void remove(Entity.RemovalReason reason) {
/* 1326 */     super.remove(reason);
/* 1328 */     this.inventoryMenu.removed(this);
/* 1329 */     if (hasContainerOpen())
/* 1330 */       doCloseContainer(); 
/*      */   }
/*      */   
/*      */   public boolean isClientAuthoritative() {
/* 1336 */     return true;
/*      */   }
/*      */   
/*      */   protected boolean isLocalClientAuthoritative() {
/* 1341 */     return isLocalPlayer();
/*      */   }
/*      */   
/*      */   public boolean isLocalPlayer() {
/* 1345 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canSimulateMovement() {
/* 1350 */     return (!level().isClientSide() || isLocalPlayer());
/*      */   }
/*      */   
/*      */   public boolean isEffectiveAi() {
/* 1355 */     return (!level().isClientSide() || isLocalPlayer());
/*      */   }
/*      */   
/*      */   public GameProfile getGameProfile() {
/* 1359 */     return this.gameProfile;
/*      */   }
/*      */   
/*      */   public NameAndId nameAndId() {
/* 1363 */     return new NameAndId(this.gameProfile);
/*      */   }
/*      */   
/*      */   public Inventory getInventory() {
/* 1367 */     return this.inventory;
/*      */   }
/*      */   
/*      */   public Abilities getAbilities() {
/* 1371 */     return this.abilities;
/*      */   }
/*      */   
/*      */   public boolean hasInfiniteMaterials() {
/* 1377 */     return this.abilities.instabuild;
/*      */   }
/*      */   
/*      */   public boolean preventsBlockDrops() {
/* 1381 */     return this.abilities.instabuild;
/*      */   }
/*      */   
/*      */   public void updateTutorialInventoryAction(ItemStack itemCarried, ItemStack itemInSlot, ClickAction clickAction) {}
/*      */   
/*      */   public boolean hasContainerOpen() {
/* 1389 */     return (this.containerMenu != this.inventoryMenu);
/*      */   }
/*      */   
/*      */   public boolean canDropItems() {
/* 1393 */     return true;
/*      */   }
/*      */   
/*      */   public static final class BedSleepingProblem extends Record {
/*      */     private final Component message;
/*      */     
/*      */     public BedSleepingProblem(Component message) {
/* 1396 */       this.message = message;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1396	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1396	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1396	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/entity/player/Player$BedSleepingProblem;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public Component message() {
/* 1396 */       return this.message;
/*      */     }
/*      */     
/* 1399 */     public static final BedSleepingProblem TOO_FAR_AWAY = new BedSleepingProblem((Component)Component.translatable("block.minecraft.bed.too_far_away"));
/*      */     
/* 1400 */     public static final BedSleepingProblem OBSTRUCTED = new BedSleepingProblem((Component)Component.translatable("block.minecraft.bed.obstructed"));
/*      */     
/* 1401 */     public static final BedSleepingProblem OTHER_PROBLEM = new BedSleepingProblem(null);
/*      */     
/* 1402 */     public static final BedSleepingProblem NOT_SAFE = new BedSleepingProblem((Component)Component.translatable("block.minecraft.bed.not_safe"));
/*      */   }
/*      */   
/*      */   public Either<BedSleepingProblem, Unit> startSleepInBed(BlockPos pos) {
/* 1406 */     startSleeping(pos);
/* 1408 */     this.sleepCounter = 0;
/* 1410 */     return Either.right(Unit.INSTANCE);
/*      */   }
/*      */   
/*      */   public void stopSleepInBed(boolean forcefulWakeUp, boolean updateLevelList) {
/* 1419 */     super.stopSleeping();
/* 1421 */     if (level() instanceof ServerLevel && updateLevelList)
/* 1422 */       ((ServerLevel)level()).updateSleepingPlayerList(); 
/* 1425 */     this.sleepCounter = forcefulWakeUp ? 0 : 100;
/*      */   }
/*      */   
/*      */   public void stopSleeping() {
/* 1430 */     stopSleepInBed(true, true);
/*      */   }
/*      */   
/*      */   public boolean isSleepingLongEnough() {
/* 1434 */     return (isSleeping() && this.sleepCounter >= 100);
/*      */   }
/*      */   
/*      */   public int getSleepTimer() {
/* 1438 */     return this.sleepCounter;
/*      */   }
/*      */   
/*      */   public void sendSystemMessage(Component message) {}
/*      */   
/*      */   public void sendOverlayMessage(Component message) {}
/*      */   
/*      */   public void awardStat(Identifier location) {
/* 1448 */     awardStat(Stats.CUSTOM.get(location));
/*      */   }
/*      */   
/*      */   public void awardStat(Identifier location, int count) {
/* 1452 */     awardStat(Stats.CUSTOM.get(location), count);
/*      */   }
/*      */   
/*      */   public void awardStat(Stat<?> stat) {
/* 1456 */     awardStat(stat, 1);
/*      */   }
/*      */   
/*      */   public void awardStat(Stat<?> stat, int count) {}
/*      */   
/*      */   public void resetStat(Stat<?> stat) {}
/*      */   
/*      */   public int awardRecipes(Collection<RecipeHolder<?>> recipes) {
/* 1466 */     return 0;
/*      */   }
/*      */   
/*      */   public void triggerRecipeCrafted(RecipeHolder<?> recipe, List<ItemStack> itemStacks) {}
/*      */   
/*      */   public void awardRecipesByKey(List<ResourceKey<Recipe<?>>> recipeIds) {}
/*      */   
/*      */   public int resetRecipes(Collection<RecipeHolder<?>> recipe) {
/* 1476 */     return 0;
/*      */   }
/*      */   
/*      */   public void travel(Vec3 input) {
/* 1481 */     if (isPassenger()) {
/* 1482 */       super.travel(input);
/*      */       return;
/*      */     } 
/* 1486 */     if (isSwimming()) {
/* 1487 */       double lookAngleY = (getLookAngle()).y;
/* 1488 */       double multiplier = (lookAngleY < -0.2D) ? 0.085D : 0.06D;
/* 1490 */       if (lookAngleY <= 0.0D || this.jumping || !level().getFluidState(BlockPos.containing(getX(), getY() + 1.0D - 0.1D, getZ())).isEmpty()) {
/* 1491 */         Vec3 movement = getDeltaMovement();
/* 1492 */         setDeltaMovement(movement.add(0.0D, (lookAngleY - movement.y) * multiplier, 0.0D));
/*      */       } 
/*      */     } 
/* 1496 */     if ((getAbilities()).flying) {
/* 1497 */       double originalMovementY = (getDeltaMovement()).y;
/* 1498 */       super.travel(input);
/* 1499 */       setDeltaMovement(getDeltaMovement().with(Direction.Axis.Y, originalMovementY * 0.6D));
/*      */     } else {
/* 1501 */       super.travel(input);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean canGlide() {
/* 1507 */     return (!this.abilities.flying && super.canGlide());
/*      */   }
/*      */   
/*      */   public void updateSwimming() {
/* 1512 */     if (this.abilities.flying) {
/* 1513 */       setSwimming(false);
/*      */     } else {
/* 1515 */       super.updateSwimming();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean freeAt(BlockPos pos) {
/* 1520 */     return !level().getBlockState(pos).isSuffocating((BlockGetter)level(), pos);
/*      */   }
/*      */   
/*      */   public float getSpeed() {
/* 1525 */     return (float)getAttributeValue(Attributes.MOVEMENT_SPEED);
/*      */   }
/*      */   
/*      */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 1531 */     if (this.abilities.mayfly)
/* 1532 */       return false; 
/* 1535 */     if (fallDistance >= 2.0D)
/* 1536 */       awardStat(Stats.FALL_ONE_CM, (int)Math.round(fallDistance * 100.0D)); 
/* 1539 */     return super.causeFallDamage(fallDistance, damageModifier, damageSource);
/*      */   }
/*      */   
/*      */   public boolean tryToStartFallFlying() {
/* 1543 */     if (!isFallFlying() && canGlide() && !isInWater()) {
/* 1544 */       startFallFlying();
/* 1545 */       return true;
/*      */     } 
/* 1547 */     return false;
/*      */   }
/*      */   
/*      */   public void startFallFlying() {
/* 1551 */     setSharedFlag(7, true);
/*      */   }
/*      */   
/*      */   protected void doWaterSplashEffect() {
/* 1556 */     if (!isSpectator())
/* 1557 */       super.doWaterSplashEffect(); 
/*      */   }
/*      */   
/*      */   protected void playStepSound(BlockPos onPos, BlockState onState) {
/* 1563 */     if (isInWater()) {
/* 1564 */       waterSwimSound();
/* 1565 */       playMuffledStepSound(onState);
/*      */     } else {
/* 1567 */       BlockPos primaryStepSoundPos = getPrimaryStepSoundBlockPos(onPos);
/* 1568 */       if (!onPos.equals(primaryStepSoundPos)) {
/* 1569 */         BlockState primaryStepState = level().getBlockState(primaryStepSoundPos);
/* 1570 */         if (primaryStepState.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)) {
/* 1571 */           playCombinationStepSounds(primaryStepState, onState);
/*      */         } else {
/* 1573 */           super.playStepSound(primaryStepSoundPos, primaryStepState);
/*      */         } 
/*      */       } else {
/* 1576 */         super.playStepSound(onPos, onState);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public LivingEntity.Fallsounds getFallSounds() {
/* 1583 */     return new LivingEntity.Fallsounds(SoundEvents.PLAYER_SMALL_FALL, SoundEvents.PLAYER_BIG_FALL);
/*      */   }
/*      */   
/*      */   public boolean killedEntity(ServerLevel level, LivingEntity entity, DamageSource source) {
/* 1588 */     awardStat(Stats.ENTITY_KILLED.get(entity.getType()));
/* 1589 */     return true;
/*      */   }
/*      */   
/*      */   public void makeStuckInBlock(BlockState blockState, Vec3 speedMultiplier) {
/* 1594 */     if (!this.abilities.flying)
/* 1595 */       super.makeStuckInBlock(blockState, speedMultiplier); 
/* 1597 */     tryResetCurrentImpulseContext();
/*      */   }
/*      */   
/*      */   public void giveExperiencePoints(int i) {
/* 1601 */     increaseScore(i);
/* 1602 */     this.experienceProgress += i / getXpNeededForNextLevel();
/* 1603 */     this.totalExperience = Mth.clamp(this.totalExperience + i, 0, 2147483647);
/* 1604 */     while (this.experienceProgress < 0.0F) {
/* 1605 */       float remaining = this.experienceProgress * getXpNeededForNextLevel();
/* 1606 */       if (this.experienceLevel > 0) {
/* 1607 */         giveExperienceLevels(-1);
/* 1608 */         this.experienceProgress = 1.0F + remaining / getXpNeededForNextLevel();
/*      */         continue;
/*      */       } 
/* 1610 */       giveExperienceLevels(-1);
/* 1611 */       this.experienceProgress = 0.0F;
/*      */     } 
/* 1614 */     while (this.experienceProgress >= 1.0F) {
/* 1615 */       this.experienceProgress = (this.experienceProgress - 1.0F) * getXpNeededForNextLevel();
/* 1616 */       giveExperienceLevels(1);
/* 1617 */       this.experienceProgress /= getXpNeededForNextLevel();
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getEnchantmentSeed() {
/* 1622 */     return this.enchantmentSeed;
/*      */   }
/*      */   
/*      */   public void onEnchantmentPerformed(ItemStack itemStack, int enchantmentCost) {
/* 1626 */     this.experienceLevel -= enchantmentCost;
/* 1627 */     if (this.experienceLevel < 0) {
/* 1628 */       this.experienceLevel = 0;
/* 1629 */       this.experienceProgress = 0.0F;
/* 1630 */       this.totalExperience = 0;
/*      */     } 
/* 1632 */     this.enchantmentSeed = this.random.nextInt();
/*      */   }
/*      */   
/*      */   public void giveExperienceLevels(int amount) {
/* 1636 */     this.experienceLevel = IntMath.saturatedAdd(this.experienceLevel, amount);
/* 1637 */     if (this.experienceLevel < 0) {
/* 1638 */       this.experienceLevel = 0;
/* 1639 */       this.experienceProgress = 0.0F;
/* 1640 */       this.totalExperience = 0;
/*      */     } 
/* 1643 */     if (amount > 0 && this.experienceLevel % 5 == 0 && this.lastLevelUpTime < this.tickCount - 100.0F) {
/* 1644 */       float vol = (this.experienceLevel > 30) ? 1.0F : (this.experienceLevel / 30.0F);
/* 1645 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.PLAYER_LEVELUP, getSoundSource(), vol * 0.75F, 1.0F);
/* 1646 */       this.lastLevelUpTime = this.tickCount;
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getXpNeededForNextLevel() {
/* 1651 */     if (this.experienceLevel >= 30)
/* 1652 */       return 112 + (this.experienceLevel - 30) * 9; 
/* 1654 */     if (this.experienceLevel >= 15)
/* 1655 */       return 37 + (this.experienceLevel - 15) * 5; 
/* 1657 */     return 7 + this.experienceLevel * 2;
/*      */   }
/*      */   
/*      */   public void causeFoodExhaustion(float amount) {
/* 1667 */     if (this.abilities.invulnerable)
/*      */       return; 
/* 1671 */     if (!level().isClientSide())
/* 1672 */       this.foodData.addExhaustion(amount); 
/*      */   }
/*      */   
/*      */   protected boolean hasEnoughFoodToDoExhaustiveManoeuvres() {
/* 1677 */     return (getFoodData().hasEnoughFood() || (getAbilities()).mayfly);
/*      */   }
/*      */   
/*      */   public Optional<WardenSpawnTracker> getWardenSpawnTracker() {
/* 1681 */     return Optional.empty();
/*      */   }
/*      */   
/*      */   public FoodData getFoodData() {
/* 1685 */     return this.foodData;
/*      */   }
/*      */   
/*      */   public boolean canEat(boolean canAlwaysEat) {
/* 1689 */     return (this.abilities.invulnerable || canAlwaysEat || this.foodData.needsFood());
/*      */   }
/*      */   
/*      */   public boolean isHurt() {
/* 1693 */     return (getHealth() > 0.0F && getHealth() < getMaxHealth());
/*      */   }
/*      */   
/*      */   public boolean mayBuild() {
/* 1697 */     return this.abilities.mayBuild;
/*      */   }
/*      */   
/*      */   public boolean mayUseItemAt(BlockPos pos, Direction direction, ItemStack itemStack) {
/* 1702 */     if (this.abilities.mayBuild)
/* 1703 */       return true; 
/* 1706 */     BlockPos target = pos.relative(direction.getOpposite());
/* 1707 */     BlockInWorld block = new BlockInWorld((LevelReader)level(), target, false);
/* 1708 */     return itemStack.canPlaceOnBlockInAdventureMode(block);
/*      */   }
/*      */   
/*      */   protected int getBaseExperienceReward(ServerLevel level) {
/* 1713 */     if (((Boolean)level.getGameRules().get(GameRules.KEEP_INVENTORY)).booleanValue() || isSpectator())
/* 1714 */       return 0; 
/* 1717 */     return Math.min(this.experienceLevel * 7, 100);
/*      */   }
/*      */   
/*      */   protected boolean isAlwaysExperienceDropper() {
/* 1723 */     return true;
/*      */   }
/*      */   
/*      */   public boolean shouldShowName() {
/* 1728 */     return true;
/*      */   }
/*      */   
/*      */   protected Entity.MovementEmission getMovementEmission() {
/* 1735 */     return (!this.abilities.flying && (!onGround() || !isDiscrete())) ? Entity.MovementEmission.ALL : Entity.MovementEmission.NONE;
/*      */   }
/*      */   
/*      */   public void onUpdateAbilities() {}
/*      */   
/*      */   public Component getName() {
/* 1743 */     return (Component)Component.literal(this.gameProfile.name());
/*      */   }
/*      */   
/*      */   public String getPlainTextName() {
/* 1748 */     return this.gameProfile.name();
/*      */   }
/*      */   
/*      */   public PlayerEnderChestContainer getEnderChestInventory() {
/* 1752 */     return this.enderChestInventory;
/*      */   }
/*      */   
/*      */   protected boolean doesEmitEquipEvent(EquipmentSlot slot) {
/* 1759 */     return (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR);
/*      */   }
/*      */   
/*      */   public boolean addItem(ItemStack itemStack) {
/* 1763 */     return this.inventory.add(itemStack);
/*      */   }
/*      */   
/*      */   public boolean isSpectator() {
/* 1770 */     return (gameMode() == GameType.SPECTATOR);
/*      */   }
/*      */   
/*      */   public boolean isPickable() {
/* 1775 */     return (!isSpectator() && super.isPickable());
/*      */   }
/*      */   
/*      */   public boolean isSwimming() {
/* 1780 */     return (!this.abilities.flying && !isSpectator() && super.isSwimming());
/*      */   }
/*      */   
/*      */   public boolean isCreative() {
/* 1784 */     return (gameMode() == GameType.CREATIVE);
/*      */   }
/*      */   
/*      */   public boolean isPushedByFluid() {
/* 1789 */     return !this.abilities.flying;
/*      */   }
/*      */   
/*      */   public Component getDisplayName() {
/* 1794 */     MutableComponent result = PlayerTeam.formatNameForTeam((Team)getTeam(), getName());
/* 1795 */     return (Component)decorateDisplayNameComponent(result);
/*      */   }
/*      */   
/*      */   private MutableComponent decorateDisplayNameComponent(MutableComponent nameComponent) {
/* 1799 */     String name = getGameProfile().name();
/* 1801 */     return nameComponent.withStyle(s -> s.withClickEvent((ClickEvent)new ClickEvent.SuggestCommand("/tell " + name + " ")).withHoverEvent(createHoverEvent()).withInsertion(name));
/*      */   }
/*      */   
/*      */   public String getScoreboardName() {
/* 1810 */     return getGameProfile().name();
/*      */   }
/*      */   
/*      */   protected void internalSetAbsorptionAmount(float absorptionAmount) {
/* 1815 */     getEntityData().set(DATA_PLAYER_ABSORPTION_ID, Float.valueOf(absorptionAmount));
/*      */   }
/*      */   
/*      */   public float getAbsorptionAmount() {
/* 1820 */     return ((Float)getEntityData().get(DATA_PLAYER_ABSORPTION_ID)).floatValue();
/*      */   }
/*      */   
/*      */   public SlotAccess getSlot(int slot) {
/* 1825 */     if (slot == 499)
/* 1826 */       return new SlotAccess() {
/*      */           public ItemStack get() {
/* 1829 */             return Player.this.containerMenu.getCarried();
/*      */           }
/*      */           
/*      */           public boolean set(ItemStack itemStack) {
/* 1834 */             Player.this.containerMenu.setCarried(itemStack);
/* 1835 */             return true;
/*      */           }
/*      */         }; 
/* 1839 */     final int craftSlot = slot - 500;
/* 1840 */     if (craftSlot >= 0 && craftSlot < 4)
/* 1841 */       return new SlotAccess() {
/*      */           public ItemStack get() {
/* 1844 */             return Player.this.inventoryMenu.getCraftSlots().getItem(craftSlot);
/*      */           }
/*      */           
/*      */           public boolean set(ItemStack itemStack) {
/* 1849 */             Player.this.inventoryMenu.getCraftSlots().setItem(craftSlot, itemStack);
/* 1850 */             Player.this.inventoryMenu.slotsChanged(Player.this.inventory);
/* 1851 */             return true;
/*      */           }
/*      */         }; 
/* 1855 */     if (slot >= 0 && slot < this.inventory.getNonEquipmentItems().size())
/* 1856 */       return this.inventory.getSlot(slot); 
/* 1858 */     int enderSlot = slot - 200;
/* 1859 */     if (enderSlot >= 0 && enderSlot < this.enderChestInventory.getContainerSize())
/* 1860 */       return this.enderChestInventory.getSlot(enderSlot); 
/* 1862 */     return super.getSlot(slot);
/*      */   }
/*      */   
/*      */   public boolean isReducedDebugInfo() {
/* 1866 */     return this.reducedDebugInfo;
/*      */   }
/*      */   
/*      */   public void setReducedDebugInfo(boolean reducedDebugInfo) {
/* 1870 */     this.reducedDebugInfo = reducedDebugInfo;
/*      */   }
/*      */   
/*      */   public void setRemainingFireTicks(int remainingTicks) {
/* 1875 */     super.setRemainingFireTicks(this.abilities.invulnerable ? Math.min(remainingTicks, 1) : remainingTicks);
/*      */   }
/*      */   
/*      */   protected static Optional<Parrot.Variant> extractParrotVariant(CompoundTag tag) {
/* 1879 */     if (!tag.isEmpty()) {
/* 1880 */       EntityType<?> entityType = tag.read("id", EntityType.CODEC).orElse(null);
/* 1881 */       if (entityType == EntityTypes.PARROT)
/* 1882 */         return tag.read("Variant", Parrot.Variant.LEGACY_CODEC); 
/*      */     } 
/* 1885 */     return Optional.empty();
/*      */   }
/*      */   
/*      */   protected static OptionalInt convertParrotVariant(Optional<Parrot.Variant> variant) {
/* 1889 */     return variant.<OptionalInt>map(v -> OptionalInt.of(v.getId())).orElse(OptionalInt.empty());
/*      */   }
/*      */   
/*      */   private static Optional<Parrot.Variant> convertParrotVariant(OptionalInt variant) {
/* 1893 */     if (variant.isPresent())
/* 1894 */       return Optional.of(Parrot.Variant.byId(variant.getAsInt())); 
/* 1896 */     return Optional.empty();
/*      */   }
/*      */   
/*      */   public void setShoulderParrotLeft(Optional<Parrot.Variant> variant) {
/* 1900 */     this.entityData.set(DATA_SHOULDER_PARROT_LEFT, convertParrotVariant(variant));
/*      */   }
/*      */   
/*      */   public Optional<Parrot.Variant> getShoulderParrotLeft() {
/* 1904 */     return convertParrotVariant((OptionalInt)this.entityData.get(DATA_SHOULDER_PARROT_LEFT));
/*      */   }
/*      */   
/*      */   public void setShoulderParrotRight(Optional<Parrot.Variant> variant) {
/* 1908 */     this.entityData.set(DATA_SHOULDER_PARROT_RIGHT, convertParrotVariant(variant));
/*      */   }
/*      */   
/*      */   public Optional<Parrot.Variant> getShoulderParrotRight() {
/* 1912 */     return convertParrotVariant((OptionalInt)this.entityData.get(DATA_SHOULDER_PARROT_RIGHT));
/*      */   }
/*      */   
/*      */   public float getCurrentItemAttackStrengthDelay() {
/* 1916 */     return (float)(1.0D / getAttributeValue(Attributes.ATTACK_SPEED) * 20.0D);
/*      */   }
/*      */   
/*      */   public boolean cannotAttackWithItem(ItemStack itemStack, int tolerance) {
/* 1920 */     float requiredStrength = ((Float)itemStack.getOrDefault(DataComponents.MINIMUM_ATTACK_CHARGE, Float.valueOf(0.0F))).floatValue();
/* 1921 */     float optimisticStrength = (this.attackStrengthTicker + tolerance) / getCurrentItemAttackStrengthDelay();
/* 1922 */     return (requiredStrength > 0.0F && optimisticStrength < requiredStrength);
/*      */   }
/*      */   
/*      */   public float getAttackStrengthScale(float a) {
/* 1926 */     return Mth.clamp((this.attackStrengthTicker + a) / getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public float getItemSwapScale(float a) {
/* 1930 */     return Mth.clamp((this.itemSwapTicker + a) / getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public void resetAttackStrengthTicker() {
/* 1934 */     this.attackStrengthTicker = 0;
/* 1935 */     this.itemSwapTicker = 0;
/*      */   }
/*      */   
/*      */   public void onAttack() {
/* 1940 */     resetOnlyAttackStrengthTicker();
/* 1941 */     super.onAttack();
/*      */   }
/*      */   
/*      */   public void resetOnlyAttackStrengthTicker() {
/* 1945 */     this.attackStrengthTicker = 0;
/*      */   }
/*      */   
/*      */   public ItemCooldowns getCooldowns() {
/* 1949 */     return this.cooldowns;
/*      */   }
/*      */   
/*      */   protected float getBlockSpeedFactor() {
/* 1954 */     return (this.abilities.flying || isFallFlying()) ? 1.0F : super.getBlockSpeedFactor();
/*      */   }
/*      */   
/*      */   public float getLuck() {
/* 1959 */     return (float)getAttributeValue(Attributes.LUCK);
/*      */   }
/*      */   
/*      */   public boolean canUseGameMasterBlocks() {
/* 1963 */     return (this.abilities.instabuild && permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER));
/*      */   }
/*      */   
/*      */   public PermissionSet permissions() {
/* 1967 */     return PermissionSet.NO_PERMISSIONS;
/*      */   }
/*      */   
/*      */   public ImmutableList<Pose> getDismountPoses() {
/* 1972 */     return ImmutableList.of(Pose.STANDING, Pose.CROUCHING, Pose.SWIMMING);
/*      */   }
/*      */   
/*      */   public ItemStack getProjectile(ItemStack heldWeapon) {
/* 1977 */     if (!(heldWeapon.getItem() instanceof ProjectileWeaponItem))
/* 1978 */       return ItemStack.EMPTY; 
/* 1981 */     Predicate<ItemStack> supportedProjectiles = ((ProjectileWeaponItem)heldWeapon.getItem()).getSupportedHeldProjectiles();
/* 1982 */     ItemStack heldProjectile = ProjectileWeaponItem.getHeldProjectile((LivingEntity)this, supportedProjectiles);
/* 1983 */     if (!heldProjectile.isEmpty())
/* 1984 */       return heldProjectile; 
/* 1987 */     supportedProjectiles = ((ProjectileWeaponItem)heldWeapon.getItem()).getAllSupportedProjectiles();
/* 1988 */     for (int i = 0; i < this.inventory.getContainerSize(); i++) {
/* 1989 */       ItemStack itemStack = this.inventory.getItem(i);
/* 1990 */       if (supportedProjectiles.test(itemStack))
/* 1991 */         return itemStack; 
/*      */     } 
/* 1994 */     return hasInfiniteMaterials() ? new ItemStack((ItemLike)Items.ARROW) : ItemStack.EMPTY;
/*      */   }
/*      */   
/*      */   public Vec3 getRopeHoldPosition(float partialTickTime) {
/* 1999 */     double xOff = 0.22D * ((getMainArm() == HumanoidArm.RIGHT) ? -1.0D : 1.0D);
/* 2000 */     float xRot = Mth.lerp(partialTickTime * 0.5F, getXRot(), this.xRotO) * 0.017453292F;
/* 2001 */     float yRot = Mth.lerp(partialTickTime, this.yBodyRotO, this.yBodyRot) * 0.017453292F;
/* 2002 */     if (isFallFlying() || isAutoSpinAttack()) {
/*      */       float zRot;
/* 2004 */       Vec3 lookAngle = getViewVector(partialTickTime);
/* 2005 */       Vec3 movement = getDeltaMovement();
/* 2006 */       double speedLen = movement.horizontalDistanceSqr();
/* 2007 */       double lookLen = lookAngle.horizontalDistanceSqr();
/* 2009 */       if (speedLen > 0.0D && lookLen > 0.0D) {
/* 2010 */         double dot = (movement.x * lookAngle.x + movement.z * lookAngle.z) / Math.sqrt(speedLen * lookLen);
/* 2011 */         double sign = movement.x * lookAngle.z - movement.z * lookAngle.x;
/* 2012 */         zRot = (float)(Math.signum(sign) * Math.acos(dot));
/*      */       } else {
/* 2014 */         zRot = 0.0F;
/*      */       } 
/* 2016 */       return getPosition(partialTickTime).add((new Vec3(xOff, -0.11D, 0.85D)).zRot(-zRot).xRot(-xRot).yRot(-yRot));
/*      */     } 
/* 2017 */     if (isVisuallySwimming())
/* 2018 */       return getPosition(partialTickTime).add((new Vec3(xOff, 0.2D, -0.15D)).xRot(-xRot).yRot(-yRot)); 
/* 2020 */     double yOff = getBoundingBox().getYsize() - 1.0D;
/* 2021 */     double zOff = isCrouching() ? -0.2D : 0.07D;
/* 2022 */     return getPosition(partialTickTime).add((new Vec3(xOff, yOff, zOff)).yRot(-yRot));
/*      */   }
/*      */   
/*      */   public boolean isAlwaysTicking() {
/* 2028 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isScoping() {
/* 2032 */     return (isUsingItem() && getUseItem().is(Items.SPYGLASS));
/*      */   }
/*      */   
/*      */   public boolean shouldBeSaved() {
/* 2037 */     return false;
/*      */   }
/*      */   
/*      */   public Optional<GlobalPos> getLastDeathLocation() {
/* 2041 */     return this.lastDeathLocation;
/*      */   }
/*      */   
/*      */   public void setLastDeathLocation(Optional<GlobalPos> pos) {
/* 2045 */     this.lastDeathLocation = pos;
/*      */   }
/*      */   
/*      */   public float getHurtDir() {
/* 2050 */     return this.hurtDir;
/*      */   }
/*      */   
/*      */   public void animateHurt(float yaw) {
/* 2055 */     super.animateHurt(yaw);
/* 2056 */     this.hurtDir = yaw;
/*      */   }
/*      */   
/*      */   public boolean isMobilityRestricted() {
/* 2060 */     return hasEffect(MobEffects.BLINDNESS);
/*      */   }
/*      */   
/*      */   public boolean canSprint() {
/* 2065 */     return true;
/*      */   }
/*      */   
/*      */   protected float getFlyingSpeed() {
/* 2070 */     if (this.abilities.flying && !isPassenger())
/* 2071 */       return isSprinting() ? (this.abilities.getFlyingSpeed() * 2.0F) : this.abilities.getFlyingSpeed(); 
/* 2073 */     return isSprinting() ? 0.025999999F : 0.02F;
/*      */   }
/*      */   
/*      */   public boolean hasContainerOpen(ContainerOpenersCounter container, BlockPos blockPos) {
/* 2079 */     return container.isOwnContainer(this);
/*      */   }
/*      */   
/*      */   public double getContainerInteractionRange() {
/* 2084 */     return blockInteractionRange();
/*      */   }
/*      */   
/*      */   public double blockInteractionRange() {
/* 2088 */     return getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE);
/*      */   }
/*      */   
/*      */   public double entityInteractionRange() {
/* 2092 */     return getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
/*      */   }
/*      */   
/*      */   public boolean isWithinEntityInteractionRange(Entity entity, double buffer) {
/* 2096 */     if (entity.isRemoved())
/* 2097 */       return false; 
/* 2099 */     return isWithinEntityInteractionRange(entity.getBoundingBox(), buffer);
/*      */   }
/*      */   
/*      */   public boolean isWithinEntityInteractionRange(AABB aabb, double buffer) {
/* 2103 */     double maxRange = entityInteractionRange() + buffer;
/* 2104 */     double distanceToSq = aabb.distanceToSqr(getEyePosition());
/* 2105 */     return (distanceToSq < maxRange * maxRange);
/*      */   }
/*      */   
/*      */   public boolean isWithinAttackRange(ItemStack weaponItem, AABB aabb, double buffer) {
/* 2109 */     return getAttackRangeWith(weaponItem).isInRange((LivingEntity)this, aabb, buffer);
/*      */   }
/*      */   
/*      */   public boolean isWithinBlockInteractionRange(BlockPos pos, double buffer) {
/* 2113 */     double maxRange = blockInteractionRange() + buffer;
/* 2113 */     return 
/* 2114 */       ((new AABB(pos)).distanceToSqr(getEyePosition()) < maxRange * maxRange);
/*      */   }
/*      */   
/*      */   public boolean shouldRotateWithMinecart() {
/* 2118 */     return false;
/*      */   }
/*      */   
/*      */   public boolean onClimbable() {
/* 2123 */     if (this.abilities.flying)
/* 2124 */       return false; 
/* 2126 */     return super.onClimbable();
/*      */   }
/*      */   
/*      */   public String debugInfo() {
/* 2130 */     return MoreObjects.toStringHelper(this)
/* 2131 */       .add("name", getPlainTextName())
/* 2132 */       .add("id", getId())
/* 2133 */       .add("pos", position())
/* 2134 */       .add("mode", gameMode())
/* 2135 */       .add("permission", printPlayerPermissions(permissions()))
/* 2136 */       .toString();
/*      */   }
/*      */   
/*      */   private static String printPlayerPermissions(PermissionSet permissions) {
/* 2140 */     if (permissions.hasPermission(Permissions.COMMANDS_OWNER))
/* 2141 */       return "owner"; 
/* 2143 */     if (permissions.hasPermission(Permissions.COMMANDS_ADMIN))
/* 2144 */       return "admin"; 
/* 2146 */     if (permissions.hasPermission(Permissions.COMMANDS_GAMEMASTER))
/* 2147 */       return "gamemaster"; 
/* 2149 */     if (permissions.hasPermission(Permissions.COMMANDS_MODERATOR))
/* 2150 */       return "moderator"; 
/* 2152 */     return "none";
/*      */   }
/*      */   
/*      */   public ResolvableProfile getProfile() {
/* 2157 */     return ResolvableProfile.createResolved(this.gameProfile);
/*      */   }
/*      */   
/*      */   public DamageSource createDamageSource() {
/* 2162 */     return damageSources().playerAttack(this);
/*      */   }
/*      */   
/*      */   public abstract GameType gameMode();
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\player\Player.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */