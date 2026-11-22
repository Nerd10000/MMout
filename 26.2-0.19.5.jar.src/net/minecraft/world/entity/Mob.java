/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.collect.Maps;
/*      */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.tags.EntityTypeTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.VisibleForDebug;
/*      */ import net.minecraft.util.debug.DebugBrainDump;
/*      */ import net.minecraft.util.debug.DebugGoalInfo;
/*      */ import net.minecraft.util.debug.DebugPathInfo;
/*      */ import net.minecraft.util.debug.DebugSubscriptions;
/*      */ import net.minecraft.util.debug.DebugValueSource;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.Container;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*      */ import net.minecraft.world.entity.ai.control.JumpControl;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.GoalSelector;
/*      */ import net.minecraft.world.entity.ai.goal.WrappedGoal;
/*      */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*      */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*      */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*      */ import net.minecraft.world.entity.ai.sensing.Sensing;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.SpawnEggItem;
/*      */ import net.minecraft.world.item.component.AttackRange;
/*      */ import net.minecraft.world.item.component.ItemAttributeModifiers;
/*      */ import net.minecraft.world.item.component.UseRemainder;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.ItemEnchantments;
/*      */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelAccessor;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.ServerLevelAccessor;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ import net.minecraft.world.level.pathfinder.PathType;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.ticks.ContainerSingleItem;
/*      */ 
/*      */ public abstract class Mob extends LivingEntity implements Targeting, EquipmentUser, Leashable {
/*  101 */   private static final EntityDataAccessor<Byte> DATA_MOB_FLAGS_ID = SynchedEntityData.defineId(Mob.class, EntityDataSerializers.BYTE);
/*      */   
/*      */   private static final int MOB_FLAG_NO_AI = 1;
/*      */   
/*      */   private static final int MOB_FLAG_LEFTHANDED = 2;
/*      */   
/*      */   private static final int MOB_FLAG_AGGRESSIVE = 4;
/*      */   
/*      */   protected static final int PICKUP_REACH = 1;
/*      */   
/*  106 */   private static final Vec3i ITEM_PICKUP_REACH = new Vec3i(1, 0, 1);
/*      */   
/*  108 */   private static final List<EquipmentSlot> EQUIPMENT_POPULATION_ORDER = List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
/*      */   
/*      */   public static final float MAX_WEARING_ARMOR_CHANCE = 0.15F;
/*      */   
/*      */   public static final float WEARING_ARMOR_UPGRADE_MATERIAL_CHANCE = 0.1087F;
/*      */   
/*      */   public static final float WEARING_ARMOR_UPGRADE_MATERIAL_ATTEMPTS = 3.0F;
/*      */   
/*      */   public static final float MAX_PICKUP_LOOT_CHANCE = 0.55F;
/*      */   
/*      */   public static final float MAX_ENCHANTED_ARMOR_CHANCE = 0.5F;
/*      */   
/*      */   public static final float MAX_ENCHANTED_WEAPON_CHANCE = 0.25F;
/*      */   
/*      */   public static final int UPDATE_GOAL_SELECTOR_EVERY_N_TICKS = 2;
/*      */   
/*  121 */   private static final double DEFAULT_ATTACK_REACH = Math.sqrt(2.0399999618530273D) - 0.6000000238418579D;
/*      */   
/*      */   private static final boolean DEFAULT_CAN_PICK_UP_LOOT = false;
/*      */   
/*      */   private static final boolean DEFAULT_PERSISTENCE_REQUIRED = false;
/*      */   
/*      */   private static final boolean DEFAULT_LEFT_HANDED = false;
/*      */   
/*      */   private static final boolean DEFAULT_NO_AI = false;
/*      */   
/*  127 */   protected static final Identifier RANDOM_SPAWN_BONUS_ID = Identifier.withDefaultNamespace("random_spawn_bonus");
/*      */   
/*      */   public static final String TAG_DROP_CHANCES = "drop_chances";
/*      */   
/*      */   public static final String TAG_LEFT_HANDED = "LeftHanded";
/*      */   
/*      */   public static final String TAG_CAN_PICK_UP_LOOT = "CanPickUpLoot";
/*      */   
/*      */   public static final String TAG_NO_AI = "NoAI";
/*      */   
/*      */   public static final String TAG_PERSISTENCE_REQUIRED = "PersistenceRequired";
/*      */   
/*      */   public int ambientSoundTime;
/*      */   
/*      */   protected int xpReward;
/*      */   
/*      */   protected LookControl lookControl;
/*      */   
/*      */   protected MoveControl moveControl;
/*      */   
/*      */   protected JumpControl jumpControl;
/*      */   
/*      */   private final BodyRotationControl bodyRotationControl;
/*      */   
/*      */   protected PathNavigation navigation;
/*      */   
/*      */   protected final GoalSelector goalSelector;
/*      */   
/*      */   protected final GoalSelector targetSelector;
/*      */   
/*      */   private LivingEntity target;
/*      */   
/*      */   private final Sensing sensing;
/*      */   
/*  146 */   private DropChances dropChances = DropChances.DEFAULT;
/*      */   
/*      */   private boolean canPickUpLoot = false;
/*      */   
/*      */   private boolean persistenceRequired = false;
/*      */   
/*  149 */   private final Map<PathType, Float> pathfindingMalus = Maps.newEnumMap(PathType.class);
/*      */   
/*  150 */   private Optional<ResourceKey<LootTable>> lootTable = Optional.empty();
/*      */   
/*      */   private long lootTableSeed;
/*      */   
/*      */   private Leashable.LeashData leashData;
/*      */   
/*  155 */   private BlockPos homePosition = BlockPos.ZERO;
/*      */   
/*  156 */   private int homeRadius = -1;
/*      */   
/*      */   protected Mob(EntityType<? extends Mob> type, Level level) {
/*  159 */     super((EntityType)type, level);
/*  161 */     this.goalSelector = new GoalSelector();
/*  162 */     this.targetSelector = new GoalSelector();
/*  163 */     this.lookControl = new LookControl(this);
/*  164 */     this.moveControl = new MoveControl(this);
/*  165 */     this.jumpControl = new JumpControl(this);
/*  166 */     this.bodyRotationControl = createBodyControl();
/*  167 */     this.navigation = createNavigation(level);
/*  168 */     this.sensing = new Sensing(this);
/*  170 */     if (level instanceof ServerLevel)
/*  171 */       registerGoals(); 
/*      */   }
/*      */   
/*      */   protected void registerGoals() {}
/*      */   
/*      */   public static AttributeSupplier.Builder createMobAttributes() {
/*  179 */     return LivingEntity.createLivingAttributes()
/*  180 */       .add(Attributes.FOLLOW_RANGE, 16.0D);
/*      */   }
/*      */   
/*      */   protected PathNavigation createNavigation(Level level) {
/*  184 */     return (PathNavigation)new GroundPathNavigation(this, level);
/*      */   }
/*      */   
/*      */   protected boolean shouldPassengersInheritMalus() {
/*  195 */     return false;
/*      */   }
/*      */   
/*      */   public float getPathfindingMalus(PathType pathType) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual getControlledVehicle : ()Lnet/minecraft/world/entity/Entity;
/*      */     //   4: astore #4
/*      */     //   6: aload #4
/*      */     //   8: instanceof net/minecraft/world/entity/Mob
/*      */     //   11: ifeq -> 32
/*      */     //   14: aload #4
/*      */     //   16: checkcast net/minecraft/world/entity/Mob
/*      */     //   19: astore_3
/*      */     //   20: aload_3
/*      */     //   21: invokevirtual shouldPassengersInheritMalus : ()Z
/*      */     //   24: ifeq -> 32
/*      */     //   27: aload_3
/*      */     //   28: astore_2
/*      */     //   29: goto -> 34
/*      */     //   32: aload_0
/*      */     //   33: astore_2
/*      */     //   34: aload_2
/*      */     //   35: getfield pathfindingMalus : Ljava/util/Map;
/*      */     //   38: aload_1
/*      */     //   39: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   44: checkcast java/lang/Float
/*      */     //   47: astore_3
/*      */     //   48: aload_3
/*      */     //   49: ifnonnull -> 59
/*      */     //   52: aload_1
/*      */     //   53: invokevirtual getMalus : ()F
/*      */     //   56: goto -> 63
/*      */     //   59: aload_3
/*      */     //   60: invokevirtual floatValue : ()F
/*      */     //   63: freturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #203	-> 0
/*      */     //   #204	-> 27
/*      */     //   #206	-> 32
/*      */     //   #209	-> 34
/*      */     //   #210	-> 48
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   29	3	2	inheritFrom	Lnet/minecraft/world/entity/Mob;
/*      */     //   20	12	3	riding	Lnet/minecraft/world/entity/Mob;
/*      */     //   0	64	0	this	Lnet/minecraft/world/entity/Mob;
/*      */     //   0	64	1	pathType	Lnet/minecraft/world/level/pathfinder/PathType;
/*      */     //   34	30	2	inheritFrom	Lnet/minecraft/world/entity/Mob;
/*      */     //   48	16	3	malus	Ljava/lang/Float;
/*      */   }
/*      */   
/*      */   public void setPathfindingMalus(PathType pathType, float cost) {
/*  214 */     this.pathfindingMalus.put(pathType, Float.valueOf(cost));
/*      */   }
/*      */   
/*      */   public void onPathfindingStart() {}
/*      */   
/*      */   public void onPathfindingDone() {}
/*      */   
/*      */   protected BodyRotationControl createBodyControl() {
/*  226 */     return new BodyRotationControl(this);
/*      */   }
/*      */   
/*      */   public LookControl getLookControl() {
/*  230 */     return this.lookControl;
/*      */   }
/*      */   
/*      */   public MoveControl getMoveControl() {
/*  234 */     Entity entity = getControlledVehicle();
/*  234 */     if (entity instanceof Mob) {
/*  234 */       Mob riding = (Mob)entity;
/*  235 */       return riding.getMoveControl();
/*      */     } 
/*  237 */     return this.moveControl;
/*      */   }
/*      */   
/*      */   public JumpControl getJumpControl() {
/*  241 */     return this.jumpControl;
/*      */   }
/*      */   
/*      */   public PathNavigation getNavigation() {
/*  245 */     Entity entity = getControlledVehicle();
/*  245 */     if (entity instanceof Mob) {
/*  245 */       Mob riding = (Mob)entity;
/*  246 */       return riding.getNavigation();
/*      */     } 
/*  248 */     return this.navigation;
/*      */   }
/*      */   
/*      */   public LivingEntity getControllingPassenger() {
/*  253 */     Entity firstPassenger = getFirstPassenger();
/*  254 */     if (!isNoAi() && firstPassenger instanceof Mob) {
/*  254 */       Mob passenger = (Mob)firstPassenger;
/*  254 */       if (firstPassenger.canControlVehicle());
/*      */     } 
/*  254 */     return null;
/*      */   }
/*      */   
/*      */   public Sensing getSensing() {
/*  258 */     return this.sensing;
/*      */   }
/*      */   
/*      */   public LivingEntity getTarget() {
/*  263 */     return asValidTarget(this.target);
/*      */   }
/*      */   
/*      */   public LivingEntity getTargetUnchecked() {
/*  267 */     return this.target;
/*      */   }
/*      */   
/*      */   protected LivingEntity asValidTarget(LivingEntity target) {
/*  272 */     if (target instanceof Player) {
/*  272 */       Player player = (Player)target;
/*  272 */       if (player.isCreative() || player.isSpectator())
/*  273 */         return null; 
/*      */     } 
/*  275 */     if (target != null && !canAttack(target))
/*  276 */       return null; 
/*  278 */     return target;
/*      */   }
/*      */   
/*      */   protected final LivingEntity getTargetFromBrain() {
/*  282 */     return asValidTarget(getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null));
/*      */   }
/*      */   
/*      */   public void setTarget(LivingEntity target) {
/*  286 */     this.target = asValidTarget(target);
/*      */   }
/*      */   
/*      */   public boolean canAttack(LivingEntity target) {
/*  291 */     return (!target.is(EntityTypes.GHAST) && super.canAttack(target));
/*      */   }
/*      */   
/*      */   public boolean canUseNonMeleeWeapon(ItemStack item) {
/*  295 */     return false;
/*      */   }
/*      */   
/*      */   public void ate() {
/*  300 */     gameEvent((Holder<GameEvent>)GameEvent.EAT);
/*      */   }
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  305 */     super.defineSynchedData(entityData);
/*  306 */     entityData.define(DATA_MOB_FLAGS_ID, Byte.valueOf((byte)0));
/*      */   }
/*      */   
/*      */   public int getAmbientSoundInterval() {
/*  310 */     return 80;
/*      */   }
/*      */   
/*      */   public void playAmbientSound() {
/*  314 */     makeSound(getAmbientSound());
/*      */   }
/*      */   
/*      */   public void baseTick() {
/*  319 */     super.baseTick();
/*  321 */     ProfilerFiller profiler = Profiler.get();
/*  322 */     profiler.push("mobBaseTick");
/*  323 */     if (isAlive() && this.random.nextInt(1000) < this.ambientSoundTime++) {
/*  324 */       resetAmbientSoundTime();
/*  325 */       playAmbientSound();
/*      */     } 
/*  327 */     profiler.pop();
/*      */   }
/*      */   
/*      */   protected void playHurtSound(DamageSource source) {
/*  332 */     resetAmbientSoundTime();
/*  333 */     super.playHurtSound(source);
/*      */   }
/*      */   
/*      */   private void resetAmbientSoundTime() {
/*  337 */     this.ambientSoundTime = -getAmbientSoundInterval();
/*      */   }
/*      */   
/*      */   protected int getBaseExperienceReward(ServerLevel level) {
/*  342 */     if (this.xpReward > 0) {
/*  343 */       int result = this.xpReward;
/*  344 */       for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/*  345 */         if (!slot.canIncreaseExperience())
/*      */           continue; 
/*  348 */         ItemStack item = getItemBySlot(slot);
/*  349 */         if (!item.isEmpty() && this.dropChances.byEquipment(slot) <= 1.0F)
/*  350 */           result += 1 + this.random.nextInt(3); 
/*      */       } 
/*  353 */       return result;
/*      */     } 
/*  355 */     return this.xpReward;
/*      */   }
/*      */   
/*      */   public void spawnAnim() {
/*  360 */     if (level().isClientSide()) {
/*  361 */       makePoofParticles();
/*      */     } else {
/*  363 */       level().broadcastEntityEvent(this, (byte)20);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*  369 */     if (id == 20) {
/*  370 */       spawnAnim();
/*      */     } else {
/*  372 */       super.handleEntityEvent(id);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void tick() {
/*  378 */     super.tick();
/*  380 */     if (!level().isClientSide() && 
/*  381 */       this.tickCount % 5 == 0)
/*  382 */       updateControlFlags(); 
/*      */   }
/*      */   
/*      */   protected void updateControlFlags() {
/*  389 */     boolean noController = !(getControllingPassenger() instanceof Mob);
/*  390 */     boolean notInBoat = !(getVehicle() instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat);
/*  391 */     this.goalSelector.setControlFlag(Goal.Flag.MOVE, noController);
/*  392 */     this.goalSelector.setControlFlag(Goal.Flag.JUMP, (noController && notInBoat));
/*  393 */     this.goalSelector.setControlFlag(Goal.Flag.LOOK, noController);
/*      */   }
/*      */   
/*      */   protected void tickHeadTurn(float yBodyRotT) {
/*  398 */     this.bodyRotationControl.clientTick();
/*      */   }
/*      */   
/*      */   protected SoundEvent getAmbientSound() {
/*  405 */     return null;
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  410 */     super.addAdditionalSaveData(output);
/*  411 */     output.putBoolean("CanPickUpLoot", canPickUpLoot());
/*  412 */     output.putBoolean("PersistenceRequired", this.persistenceRequired);
/*  414 */     if (!this.dropChances.equals(DropChances.DEFAULT))
/*  415 */       output.store("drop_chances", DropChances.CODEC, this.dropChances); 
/*  418 */     writeLeashData(output, this.leashData);
/*  420 */     if (hasHome()) {
/*  421 */       output.putInt("home_radius", this.homeRadius);
/*  422 */       output.store("home_pos", BlockPos.CODEC, this.homePosition);
/*      */     } 
/*  425 */     output.putBoolean("LeftHanded", isLeftHanded());
/*  427 */     this.lootTable.ifPresent(lootTable -> output.store("DeathLootTable", LootTable.KEY_CODEC, lootTable));
/*  428 */     if (this.lootTableSeed != 0L)
/*  429 */       output.putLong("DeathLootTableSeed", this.lootTableSeed); 
/*  432 */     if (isNoAi())
/*  433 */       output.putBoolean("NoAI", isNoAi()); 
/*      */   }
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  439 */     super.readAdditionalSaveData(input);
/*  441 */     setCanPickUpLoot(input.getBooleanOr("CanPickUpLoot", false));
/*  442 */     this.persistenceRequired = input.getBooleanOr("PersistenceRequired", false);
/*  444 */     this.dropChances = input.read("drop_chances", DropChances.CODEC).orElse(DropChances.DEFAULT);
/*  446 */     readLeashData(input);
/*  448 */     this.homeRadius = input.getIntOr("home_radius", -1);
/*  449 */     if (this.homeRadius >= 0)
/*  450 */       this.homePosition = input.read("home_pos", BlockPos.CODEC).orElse(BlockPos.ZERO); 
/*  453 */     setLeftHanded(input.getBooleanOr("LeftHanded", false));
/*  455 */     this.lootTable = input.read("DeathLootTable", LootTable.KEY_CODEC);
/*  456 */     this.lootTableSeed = input.getLongOr("DeathLootTableSeed", 0L);
/*  458 */     setNoAi(input.getBooleanOr("NoAI", false));
/*      */   }
/*      */   
/*      */   protected void dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled) {
/*  463 */     super.dropFromLootTable(level, source, playerKilled);
/*  464 */     this.lootTable = Optional.empty();
/*      */   }
/*      */   
/*      */   public final Optional<ResourceKey<LootTable>> getLootTable() {
/*  469 */     if (this.lootTable.isPresent())
/*  470 */       return this.lootTable; 
/*  473 */     return super.getLootTable();
/*      */   }
/*      */   
/*      */   public long getLootTableSeed() {
/*  478 */     return this.lootTableSeed;
/*      */   }
/*      */   
/*      */   public void setZza(float zza) {
/*  483 */     this.zza = zza;
/*      */   }
/*      */   
/*      */   public void setYya(float yya) {
/*  487 */     this.yya = yya;
/*      */   }
/*      */   
/*      */   public void setXxa(float xxa) {
/*  492 */     this.xxa = xxa;
/*      */   }
/*      */   
/*      */   public void setSpeed(float speed) {
/*  497 */     super.setSpeed(speed);
/*  498 */     setZza(speed);
/*      */   }
/*      */   
/*      */   public void stopInPlace() {
/*  502 */     getNavigation().stop();
/*  503 */     setXxa(0.0F);
/*  504 */     setYya(0.0F);
/*  505 */     setSpeed(0.0F);
/*  506 */     setDeltaMovement(0.0D, 0.0D, 0.0D);
/*  507 */     resetAngularLeashMomentum();
/*      */   }
/*      */   
/*      */   public void aiStep() {
/*  512 */     super.aiStep();
/*  514 */     if (is(EntityTypeTags.BURN_IN_DAYLIGHT))
/*  515 */       burnUndead(); 
/*  518 */     ProfilerFiller profiler = Profiler.get();
/*  519 */     profiler.push("looting");
/*  521 */     Level level = level();
/*  521 */     if (level instanceof ServerLevel) {
/*  521 */       ServerLevel serverLevel = (ServerLevel)level;
/*  521 */       if (canPickUpLoot() && isAlive() && !this.dead && ((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/*  522 */         Vec3i pickupReach = getPickupReach();
/*  523 */         List<ItemEntity> entities = level().getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(pickupReach.getX(), pickupReach.getY(), pickupReach.getZ()));
/*  524 */         for (ItemEntity entity : entities) {
/*  525 */           if (entity.isRemoved() || entity.getItem().isEmpty() || entity.hasPickUpDelay())
/*      */             continue; 
/*  528 */           if (wantsToPickUp(serverLevel, entity.getItem()))
/*  529 */             pickUpItem(serverLevel, entity); 
/*      */         } 
/*      */       } 
/*      */     } 
/*  533 */     profiler.pop();
/*      */   }
/*      */   
/*      */   protected EquipmentSlot sunProtectionSlot() {
/*  537 */     return EquipmentSlot.HEAD;
/*      */   }
/*      */   
/*      */   private void burnUndead() {
/*  541 */     if (!isAlive() || !isSunBurnTick())
/*      */       return; 
/*  545 */     EquipmentSlot slot = sunProtectionSlot();
/*  546 */     ItemStack sunBlocker = getItemBySlot(slot);
/*  547 */     if (!sunBlocker.isEmpty()) {
/*  548 */       if (sunBlocker.isDamageableItem()) {
/*  549 */         Item sunBlockerItem = sunBlocker.getItem();
/*  550 */         sunBlocker.setDamageValue(sunBlocker.getDamageValue() + this.random.nextInt(2));
/*  551 */         if (sunBlocker.getDamageValue() >= sunBlocker.getMaxDamage()) {
/*  552 */           onEquippedItemBroken(sunBlockerItem, slot);
/*  553 */           setItemSlot(slot, ItemStack.EMPTY);
/*      */         } 
/*      */       } 
/*      */       return;
/*      */     } 
/*  559 */     igniteForSeconds(8.0F);
/*      */   }
/*      */   
/*      */   private boolean isSunBurnTick() {
/*  563 */     if (!level().isClientSide() && ((Boolean)level().environmentAttributes().getValue(EnvironmentAttributes.MONSTERS_BURN, position())).booleanValue()) {
/*  564 */       float br = getLightLevelDependentMagicValue();
/*  565 */       BlockPos roundedPos = BlockPos.containing(getX(), getEyeY(), getZ());
/*  567 */       boolean isInNonBurnableBlock = (isInWaterOrRain() || this.isInPowderSnow || this.wasInPowderSnow);
/*  568 */       if (br > 0.5F && this.random.nextFloat() * 30.0F < (br - 0.4F) * 2.0F && !isInNonBurnableBlock && level().canSeeSky(roundedPos))
/*  569 */         return true; 
/*      */     } 
/*  572 */     return false;
/*      */   }
/*      */   
/*      */   protected Vec3i getPickupReach() {
/*  576 */     return ITEM_PICKUP_REACH;
/*      */   }
/*      */   
/*      */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/*  581 */     ItemStack itemStack = entity.getItem();
/*  582 */     ItemStack equippedWithStack = equipItemIfPossible(level, itemStack.copy());
/*  584 */     if (!equippedWithStack.isEmpty()) {
/*  585 */       onItemPickup(entity);
/*  586 */       take((Entity)entity, equippedWithStack.getCount());
/*  589 */       itemStack.shrink(equippedWithStack.getCount());
/*  591 */       if (itemStack.isEmpty())
/*  592 */         entity.discard(); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public ItemStack equipItemIfPossible(ServerLevel level, ItemStack itemStack) {
/*  598 */     EquipmentSlot slot = getEquipmentSlotForItem(itemStack);
/*  599 */     if (!isEquippableInSlot(itemStack, slot))
/*  600 */       return ItemStack.EMPTY; 
/*  603 */     ItemStack current = getItemBySlot(slot);
/*  604 */     boolean canReplace = canReplaceCurrentItem(itemStack, current, slot);
/*  606 */     if (slot.isArmor() && !canReplace) {
/*  607 */       slot = EquipmentSlot.MAINHAND;
/*  608 */       current = getItemBySlot(slot);
/*  609 */       canReplace = current.isEmpty();
/*      */     } 
/*  612 */     if (canReplace && canHoldItem(itemStack)) {
/*  613 */       double dropChance = this.dropChances.byEquipment(slot);
/*  614 */       if (!current.isEmpty() && Math.max(this.random.nextFloat() - 0.1F, 0.0F) < dropChance)
/*  615 */         spawnAtLocation(level, current); 
/*  618 */       ItemStack toEquip = slot.limit(itemStack);
/*  619 */       setItemSlotAndDropWhenKilled(slot, toEquip);
/*  620 */       this.persistenceRequired = true;
/*  621 */       return toEquip;
/*      */     } 
/*  623 */     return ItemStack.EMPTY;
/*      */   }
/*      */   
/*      */   protected void setItemSlotAndDropWhenKilled(EquipmentSlot slot, ItemStack itemStack) {
/*  627 */     setItemSlot(slot, itemStack);
/*  628 */     setGuaranteedDrop(slot);
/*      */   }
/*      */   
/*      */   protected boolean canShearEquipment(Player player) {
/*  632 */     return !isVehicle();
/*      */   }
/*      */   
/*      */   protected boolean attemptToShearEquipment(Player player, InteractionHand hand, ItemStack heldItem) {
/*  640 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/*  641 */       ItemStack itemStack = getItemBySlot(slot);
/*  642 */       Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/*  643 */       if (equippable != null && equippable.canBeSheared() && (!EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE) || player.isCreative())) {
/*  644 */         shearItem(player, hand, heldItem, slot, itemStack);
/*  645 */         playSound((SoundEvent)equippable.shearingSound().value());
/*  646 */         return true;
/*      */       } 
/*      */     } 
/*  649 */     return false;
/*      */   }
/*      */   
/*      */   protected void shearItem(Player player, InteractionHand hand, ItemStack heldItem, EquipmentSlot slot, ItemStack itemStackToShear) {
/*  653 */     heldItem.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/*  654 */     Vec3 equipmentSpawnOffset = getAttachments().getAverage(EntityAttachment.PASSENGER);
/*  655 */     setItemSlot(slot, ItemStack.EMPTY);
/*  656 */     gameEvent((Holder<GameEvent>)GameEvent.SHEAR, (Entity)player);
/*  657 */     Level level = level();
/*  657 */     if (level instanceof ServerLevel) {
/*  657 */       ServerLevel serverLevel = (ServerLevel)level;
/*  658 */       spawnAtLocation(serverLevel, itemStackToShear, equipmentSpawnOffset);
/*  659 */       CriteriaTriggers.PLAYER_SHEARED_EQUIPMENT.trigger((ServerPlayer)player, itemStackToShear, this);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void setGuaranteedDrop(EquipmentSlot slot) {
/*  664 */     this.dropChances = this.dropChances.withGuaranteedDrop(slot);
/*      */   }
/*      */   
/*      */   protected boolean canReplaceCurrentItem(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
/*  668 */     if (currentItemStack.isEmpty())
/*  669 */       return true; 
/*  670 */     if (slot.isArmor())
/*  671 */       return compareArmor(newItemStack, currentItemStack, slot); 
/*  672 */     if (slot == EquipmentSlot.MAINHAND)
/*  673 */       return compareWeapons(newItemStack, currentItemStack, slot); 
/*  675 */     return false;
/*      */   }
/*      */   
/*      */   private boolean compareArmor(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
/*  679 */     if (EnchantmentHelper.has(currentItemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE))
/*  680 */       return false; 
/*  682 */     double newDefense = getApproximateAttributeWith(newItemStack, Attributes.ARMOR, slot);
/*  683 */     double oldDefense = getApproximateAttributeWith(currentItemStack, Attributes.ARMOR, slot);
/*  684 */     double newToughness = getApproximateAttributeWith(newItemStack, Attributes.ARMOR_TOUGHNESS, slot);
/*  685 */     double oldToughness = getApproximateAttributeWith(currentItemStack, Attributes.ARMOR_TOUGHNESS, slot);
/*  686 */     if (newDefense != oldDefense)
/*  687 */       return (newDefense > oldDefense); 
/*  688 */     if (newToughness != oldToughness)
/*  689 */       return (newToughness > oldToughness); 
/*  691 */     return canReplaceEqualItem(newItemStack, currentItemStack);
/*      */   }
/*      */   
/*      */   private boolean compareWeapons(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
/*  696 */     TagKey<Item> preferredWeaponType = getPreferredWeaponType();
/*  697 */     if (preferredWeaponType != null) {
/*  698 */       if (currentItemStack.is(preferredWeaponType) && !newItemStack.is(preferredWeaponType))
/*  699 */         return false; 
/*  700 */       if (!currentItemStack.is(preferredWeaponType) && newItemStack.is(preferredWeaponType))
/*  701 */         return true; 
/*      */     } 
/*  705 */     double newAttackDamage = getApproximateAttributeWith(newItemStack, Attributes.ATTACK_DAMAGE, slot);
/*  706 */     double oldAttackDamage = getApproximateAttributeWith(currentItemStack, Attributes.ATTACK_DAMAGE, slot);
/*  707 */     if (newAttackDamage != oldAttackDamage)
/*  708 */       return (newAttackDamage > oldAttackDamage); 
/*  710 */     return canReplaceEqualItem(newItemStack, currentItemStack);
/*      */   }
/*      */   
/*      */   private double getApproximateAttributeWith(ItemStack itemStack, Holder<Attribute> attribute, EquipmentSlot slot) {
/*  715 */     double baseValue = getAttributes().hasAttribute(attribute) ? getAttributeBaseValue(attribute) : 0.0D;
/*  716 */     ItemAttributeModifiers attributeModifiers = (ItemAttributeModifiers)itemStack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
/*  717 */     return attributeModifiers.compute(attribute, baseValue, slot);
/*      */   }
/*      */   
/*      */   public boolean canReplaceEqualItem(ItemStack newItemStack, ItemStack currentItemStack) {
/*  721 */     Set<Object2IntMap.Entry<Holder<Enchantment>>> currentEnchantments = ((ItemEnchantments)currentItemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)).entrySet();
/*  722 */     Set<Object2IntMap.Entry<Holder<Enchantment>>> newEnchantments = ((ItemEnchantments)newItemStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)).entrySet();
/*  724 */     if (newEnchantments.size() != currentEnchantments.size())
/*  725 */       return (newEnchantments.size() > currentEnchantments.size()); 
/*  728 */     int newDamageValue = newItemStack.getDamageValue();
/*  729 */     int currentDamageValue = currentItemStack.getDamageValue();
/*  730 */     if (newDamageValue != currentDamageValue)
/*  731 */       return (newDamageValue < currentDamageValue); 
/*  734 */     return (newItemStack.has(DataComponents.CUSTOM_NAME) && !currentItemStack.has(DataComponents.CUSTOM_NAME));
/*      */   }
/*      */   
/*      */   public boolean canHoldItem(ItemStack itemStack) {
/*  739 */     return true;
/*      */   }
/*      */   
/*      */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/*  744 */     return canHoldItem(itemStack);
/*      */   }
/*      */   
/*      */   public TagKey<Item> getPreferredWeaponType() {
/*  748 */     return null;
/*      */   }
/*      */   
/*      */   public boolean removeWhenFarAway(double distSqr) {
/*  752 */     return true;
/*      */   }
/*      */   
/*      */   public boolean requiresCustomPersistence() {
/*  756 */     return (isPassenger() || isLeashed());
/*      */   }
/*      */   
/*      */   public void checkDespawn() {
/*  761 */     if (level().getDifficulty() == Difficulty.PEACEFUL && !getType().isAllowedInPeaceful()) {
/*  762 */       discard();
/*      */       return;
/*      */     } 
/*  766 */     if (isPersistenceRequired() || requiresCustomPersistence()) {
/*  767 */       this.noActionTime = 0;
/*      */       return;
/*      */     } 
/*  771 */     Player player = level().getNearestPlayer(this, -1.0D);
/*  772 */     if (player != null) {
/*  773 */       double distSqr = player.distanceToSqr(this);
/*  774 */       int instantDespawnDistance = getType().getCategory().getDespawnDistance();
/*  775 */       int despawnDistanceSqr = instantDespawnDistance * instantDespawnDistance;
/*  777 */       if (distSqr > despawnDistanceSqr && removeWhenFarAway(distSqr))
/*  778 */         discard(); 
/*  781 */       int noDespawnDistance = getType().getCategory().getNoDespawnDistance();
/*  782 */       int noDespawnDistanceSqr = noDespawnDistance * noDespawnDistance;
/*  783 */       if (this.noActionTime > 600 && this.random.nextInt(800) == 0 && distSqr > noDespawnDistanceSqr && removeWhenFarAway(distSqr)) {
/*  784 */         discard();
/*  785 */       } else if (distSqr < noDespawnDistanceSqr) {
/*  786 */         this.noActionTime = 0;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected final void serverAiStep() {
/*  794 */     this.noActionTime++;
/*  796 */     ProfilerFiller profiler = Profiler.get();
/*  797 */     profiler.push("sensing");
/*  798 */     this.sensing.tick();
/*  799 */     profiler.pop();
/*  801 */     int idBasedTickCount = this.tickCount + getId();
/*  802 */     if (idBasedTickCount % 2 == 0 || this.tickCount <= 1) {
/*  803 */       profiler.push("targetSelector");
/*  804 */       this.targetSelector.tick();
/*  805 */       profiler.pop();
/*  807 */       profiler.push("goalSelector");
/*  808 */       this.goalSelector.tick();
/*  809 */       profiler.pop();
/*      */     } else {
/*  811 */       profiler.push("targetSelector");
/*  812 */       this.targetSelector.tickRunningGoals(false);
/*  813 */       profiler.pop();
/*  815 */       profiler.push("goalSelector");
/*  816 */       this.goalSelector.tickRunningGoals(false);
/*  817 */       profiler.pop();
/*      */     } 
/*  820 */     profiler.push("navigation");
/*  821 */     this.navigation.tick();
/*  822 */     profiler.pop();
/*  824 */     profiler.push("mob tick");
/*  826 */     customServerAiStep((ServerLevel)level());
/*  827 */     profiler.pop();
/*  829 */     profiler.push("controls");
/*  830 */     profiler.push("move");
/*  831 */     this.moveControl.tick();
/*  832 */     profiler.popPush("look");
/*  833 */     this.lookControl.tick();
/*  834 */     profiler.popPush("jump");
/*  835 */     this.jumpControl.tick();
/*  836 */     profiler.pop();
/*  837 */     profiler.pop();
/*      */   }
/*      */   
/*      */   protected void customServerAiStep(ServerLevel level) {}
/*      */   
/*      */   public int getMaxHeadXRot() {
/*  844 */     return 40;
/*      */   }
/*      */   
/*      */   public int getMaxHeadYRot() {
/*  849 */     return 75;
/*      */   }
/*      */   
/*      */   protected void clampHeadRotationToBody() {
/*  853 */     float limit = getMaxHeadYRot();
/*  854 */     float headYRot = getYHeadRot();
/*  855 */     float delta = Mth.wrapDegrees(this.yBodyRot - headYRot);
/*  856 */     float targetDelta = Mth.clamp(Mth.wrapDegrees(this.yBodyRot - headYRot), -limit, limit);
/*  857 */     float newHeadYRot = headYRot + delta - targetDelta;
/*  858 */     setYHeadRot(newHeadYRot);
/*      */   }
/*      */   
/*      */   public int getHeadRotSpeed() {
/*  863 */     return 10;
/*      */   }
/*      */   
/*      */   public void lookAt(Entity entity, float yMax, float xMax) {
/*  867 */     double yd, xd = entity.getX() - getX();
/*  869 */     double zd = entity.getZ() - getZ();
/*  871 */     if (entity instanceof LivingEntity) {
/*  871 */       LivingEntity mob = (LivingEntity)entity;
/*  872 */       yd = mob.getEyeY() - getEyeY();
/*      */     } else {
/*  874 */       yd = ((entity.getBoundingBox()).minY + (entity.getBoundingBox()).maxY) / 2.0D - getEyeY();
/*      */     } 
/*  877 */     double sd = Math.sqrt(xd * xd + zd * zd);
/*  879 */     float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/*  880 */     float xRotD = (float)-(Mth.atan2(yd, sd) * 57.2957763671875D);
/*  881 */     setXRot(rotlerp(getXRot(), xRotD, xMax));
/*  882 */     setYRot(rotlerp(getYRot(), yRotD, yMax));
/*      */   }
/*      */   
/*      */   private float rotlerp(float a, float b, float max) {
/*  886 */     float diff = Mth.wrapDegrees(b - a);
/*  887 */     if (diff > max)
/*  888 */       diff = max; 
/*  890 */     if (diff < -max)
/*  891 */       diff = -max; 
/*  893 */     return a + diff;
/*      */   }
/*      */   
/*      */   public static boolean checkMobSpawnRules(EntityType<? extends Mob> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/*  897 */     BlockPos below = pos.below();
/*  898 */     return (EntitySpawnReason.isSpawner(spawnReason) || level.getBlockState(below).isValidSpawn((BlockGetter)level, below, type));
/*      */   }
/*      */   
/*      */   public boolean checkSpawnRules(LevelAccessor level, EntitySpawnReason spawnReason) {
/*  902 */     return true;
/*      */   }
/*      */   
/*      */   public boolean checkSpawnObstruction(LevelReader level) {
/*  906 */     return (!level.containsAnyLiquid(getBoundingBox()) && level.isUnobstructed(this));
/*      */   }
/*      */   
/*      */   public int getMaxSpawnClusterSize() {
/*  910 */     return 4;
/*      */   }
/*      */   
/*      */   public boolean isMaxGroupSizeReached(int groupSize) {
/*  914 */     return false;
/*      */   }
/*      */   
/*      */   public int getMaxFallDistance() {
/*  919 */     if (getTarget() == null)
/*  920 */       return getComfortableFallDistance(0.0F); 
/*  922 */     int sacrifice = (int)(getHealth() - getMaxHealth() * 0.33F);
/*  923 */     sacrifice -= (3 - level().getDifficulty().getId()) * 4;
/*  924 */     if (sacrifice < 0)
/*  925 */       sacrifice = 0; 
/*  927 */     return getComfortableFallDistance(sacrifice);
/*      */   }
/*      */   
/*      */   public ItemStack getBodyArmorItem() {
/*  931 */     return getItemBySlot(EquipmentSlot.BODY);
/*      */   }
/*      */   
/*      */   public boolean isSaddled() {
/*  935 */     return hasValidEquippableItemForSlot(EquipmentSlot.SADDLE);
/*      */   }
/*      */   
/*      */   public boolean isWearingBodyArmor() {
/*  939 */     return hasValidEquippableItemForSlot(EquipmentSlot.BODY);
/*      */   }
/*      */   
/*      */   private boolean hasValidEquippableItemForSlot(EquipmentSlot slot) {
/*  943 */     return (hasItemInSlot(slot) && isEquippableInSlot(getItemBySlot(slot), slot));
/*      */   }
/*      */   
/*      */   public Container createEquipmentSlotContainer(final EquipmentSlot slot) {
/*  947 */     return (Container)new ContainerSingleItem() {
/*      */         public ItemStack getTheItem() {
/*  950 */           return Mob.this.getItemBySlot(slot);
/*      */         }
/*      */         
/*      */         public void setTheItem(ItemStack itemStack) {
/*  955 */           Mob.this.setItemSlot(slot, itemStack);
/*  956 */           if (!itemStack.isEmpty()) {
/*  957 */             Mob.this.setGuaranteedDrop(slot);
/*  958 */             Mob.this.setPersistenceRequired();
/*      */           } 
/*      */         }
/*      */         
/*      */         public void setChanged() {}
/*      */         
/*      */         public boolean stillValid(Player player) {
/*  968 */           return (player.getVehicle() == Mob.this || player.isWithinEntityInteractionRange(Mob.this, 4.0D));
/*      */         }
/*      */       };
/*      */   }
/*      */   
/*      */   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {
/*  975 */     super.dropCustomDeathLoot(level, source, killedByPlayer);
/*  976 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/*  977 */       ItemStack itemStack = getItemBySlot(slot);
/*  978 */       float dropChance = this.dropChances.byEquipment(slot);
/*  979 */       if (dropChance == 0.0F)
/*      */         continue; 
/*  983 */       boolean preserve = this.dropChances.isPreserved(slot);
/*  984 */       Entity entity = source.getEntity();
/*  984 */       if (entity instanceof LivingEntity) {
/*  984 */         LivingEntity livingSource = (LivingEntity)entity;
/*  984 */         Level level1 = level();
/*  984 */         if (level1 instanceof ServerLevel) {
/*  984 */           ServerLevel serverLevel = (ServerLevel)level1;
/*  985 */           dropChance = EnchantmentHelper.processEquipmentDropChance(serverLevel, livingSource, source, dropChance);
/*      */         } 
/*      */       } 
/*  988 */       if (!itemStack.isEmpty() && 
/*  989 */         !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP) && (killedByPlayer || preserve) && this.random
/*      */         
/*  991 */         .nextFloat() < dropChance) {
/*  993 */         if (!preserve && itemStack.isDamageableItem())
/*  994 */           itemStack.setDamageValue(itemStack.getMaxDamage() - this.random.nextInt(1 + this.random.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1)))); 
/*  996 */         spawnAtLocation(level, itemStack);
/*  997 */         setItemSlot(slot, ItemStack.EMPTY);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public DropChances getDropChances() {
/* 1003 */     return this.dropChances;
/*      */   }
/*      */   
/*      */   public void dropPreservedEquipment(ServerLevel level) {
/* 1007 */     dropPreservedEquipment(level, stack -> true);
/*      */   }
/*      */   
/*      */   public Set<EquipmentSlot> dropPreservedEquipment(ServerLevel level, Predicate<ItemStack> shouldDrop) {
/* 1016 */     Set<EquipmentSlot> slotsPreventedFromDropping = new HashSet<>();
/* 1017 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 1018 */       ItemStack itemStack = getItemBySlot(slot);
/* 1019 */       if (itemStack.isEmpty())
/*      */         continue; 
/* 1022 */       if (!shouldDrop.test(itemStack)) {
/* 1023 */         slotsPreventedFromDropping.add(slot);
/*      */         continue;
/*      */       } 
/* 1027 */       if (this.dropChances.isPreserved(slot)) {
/* 1028 */         setItemSlot(slot, ItemStack.EMPTY);
/* 1029 */         spawnAtLocation(level, itemStack);
/*      */       } 
/*      */     } 
/* 1032 */     return slotsPreventedFromDropping;
/*      */   }
/*      */   
/*      */   private LootParams createEquipmentParams(ServerLevel serverLevel) {
/* 1036 */     return (new LootParams.Builder(serverLevel))
/* 1037 */       .withParameter(LootContextParams.ORIGIN, position())
/* 1038 */       .withParameter(LootContextParams.THIS_ENTITY, this)
/* 1039 */       .create(LootContextParamSets.EQUIPMENT);
/*      */   }
/*      */   
/*      */   public void equip(EquipmentTable equipment) {
/* 1043 */     equip(equipment.lootTable(), equipment.slotDropChances());
/*      */   }
/*      */   
/*      */   public void equip(ResourceKey<LootTable> lootTable, Map<EquipmentSlot, Float> dropChances) {
/* 1047 */     Level level = level();
/* 1047 */     if (level instanceof ServerLevel) {
/* 1047 */       ServerLevel serverLevel = (ServerLevel)level;
/* 1048 */       equip(lootTable, createEquipmentParams(serverLevel), dropChances);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 1053 */     if (random.nextFloat() < 0.15F * difficulty.getSpecialMultiplier()) {
/* 1054 */       int armorType = random.nextInt(3);
/* 1055 */       for (int i = 1; i <= 3.0F; i++) {
/* 1056 */         if (random.nextFloat() < 0.1087F)
/* 1057 */           armorType++; 
/*      */       } 
/* 1061 */       float partialChance = (level().getDifficulty() == Difficulty.HARD) ? 0.1F : 0.25F;
/* 1062 */       boolean first = true;
/* 1063 */       for (EquipmentSlot slot : EQUIPMENT_POPULATION_ORDER) {
/* 1064 */         ItemStack itemStack = getItemBySlot(slot);
/* 1065 */         if (!first && random.nextFloat() < partialChance)
/*      */           break; 
/* 1068 */         first = false;
/* 1069 */         if (itemStack.isEmpty()) {
/* 1070 */           Item equip = getEquipmentForSlot(slot, armorType);
/* 1071 */           if (equip != null)
/* 1072 */             setItemSlot(slot, new ItemStack((ItemLike)equip)); 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static Item getEquipmentForSlot(EquipmentSlot slot, int type) {
/* 1080 */     switch (slot) {
/*      */       case HEAD:
/* 1082 */         if (type == 0)
/* 1083 */           return Items.LEATHER_HELMET; 
/* 1085 */         if (type == 1)
/* 1086 */           return Items.COPPER_HELMET; 
/* 1088 */         if (type == 2)
/* 1089 */           return Items.GOLDEN_HELMET; 
/* 1091 */         if (type == 3)
/* 1092 */           return Items.CHAINMAIL_HELMET; 
/* 1094 */         if (type == 4)
/* 1095 */           return Items.IRON_HELMET; 
/* 1097 */         if (type == 5)
/* 1098 */           return Items.DIAMOND_HELMET; 
/*      */       case CHEST:
/* 1101 */         if (type == 0)
/* 1102 */           return Items.LEATHER_CHESTPLATE; 
/* 1104 */         if (type == 1)
/* 1105 */           return Items.COPPER_CHESTPLATE; 
/* 1107 */         if (type == 2)
/* 1108 */           return Items.GOLDEN_CHESTPLATE; 
/* 1110 */         if (type == 3)
/* 1111 */           return Items.CHAINMAIL_CHESTPLATE; 
/* 1113 */         if (type == 4)
/* 1114 */           return Items.IRON_CHESTPLATE; 
/* 1116 */         if (type == 5)
/* 1117 */           return Items.DIAMOND_CHESTPLATE; 
/*      */       case LEGS:
/* 1120 */         if (type == 0)
/* 1121 */           return Items.LEATHER_LEGGINGS; 
/* 1123 */         if (type == 1)
/* 1124 */           return Items.COPPER_LEGGINGS; 
/* 1126 */         if (type == 2)
/* 1127 */           return Items.GOLDEN_LEGGINGS; 
/* 1129 */         if (type == 3)
/* 1130 */           return Items.CHAINMAIL_LEGGINGS; 
/* 1132 */         if (type == 4)
/* 1133 */           return Items.IRON_LEGGINGS; 
/* 1135 */         if (type == 5)
/* 1136 */           return Items.DIAMOND_LEGGINGS; 
/*      */       case FEET:
/* 1139 */         if (type == 0)
/* 1140 */           return Items.LEATHER_BOOTS; 
/* 1142 */         if (type == 1)
/* 1143 */           return Items.COPPER_BOOTS; 
/* 1145 */         if (type == 2)
/* 1146 */           return Items.GOLDEN_BOOTS; 
/* 1148 */         if (type == 3)
/* 1149 */           return Items.CHAINMAIL_BOOTS; 
/* 1151 */         if (type == 4)
/* 1152 */           return Items.IRON_BOOTS; 
/* 1154 */         if (type == 5)
/* 1155 */           return Items.DIAMOND_BOOTS; 
/*      */         break;
/*      */     } 
/* 1159 */     return null;
/*      */   }
/*      */   
/*      */   protected void populateDefaultEquipmentEnchantments(ServerLevelAccessor level, RandomSource random, DifficultyInstance localDifficulty) {
/* 1163 */     enchantSpawnedWeapon(level, random, localDifficulty);
/* 1165 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 1166 */       if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR)
/*      */         continue; 
/* 1169 */       enchantSpawnedArmor(level, random, slot, localDifficulty);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void enchantSpawnedWeapon(ServerLevelAccessor level, RandomSource random, DifficultyInstance difficulty) {
/* 1174 */     enchantSpawnedEquipment(level, EquipmentSlot.MAINHAND, random, 0.25F, difficulty);
/*      */   }
/*      */   
/*      */   protected void enchantSpawnedArmor(ServerLevelAccessor level, RandomSource random, EquipmentSlot slot, DifficultyInstance difficulty) {
/* 1178 */     enchantSpawnedEquipment(level, slot, random, 0.5F, difficulty);
/*      */   }
/*      */   
/*      */   private void enchantSpawnedEquipment(ServerLevelAccessor level, EquipmentSlot slot, RandomSource random, float chance, DifficultyInstance difficulty) {
/* 1182 */     ItemStack itemStack = getItemBySlot(slot);
/* 1183 */     if (!itemStack.isEmpty() && random.nextFloat() < chance * difficulty.getSpecialMultiplier()) {
/* 1184 */       EnchantmentHelper.enchantItemFromProvider(itemStack, level.registryAccess(), VanillaEnchantmentProviders.MOB_SPAWN_EQUIPMENT, difficulty, random);
/* 1185 */       setItemSlot(slot, itemStack);
/*      */     } 
/*      */   }
/*      */   
/*      */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 1190 */     RandomSource random = level.getRandom();
/* 1191 */     AttributeInstance followRange = Objects.<AttributeInstance>requireNonNull(getAttribute(Attributes.FOLLOW_RANGE));
/* 1192 */     if (!followRange.hasModifier(RANDOM_SPAWN_BONUS_ID))
/* 1193 */       followRange.addPermanentModifier(new AttributeModifier(RANDOM_SPAWN_BONUS_ID, random.triangle(0.0D, 0.11485000000000001D), AttributeModifier.Operation.ADD_MULTIPLIED_BASE)); 
/* 1196 */     setLeftHanded((random.nextFloat() < 0.05F));
/* 1198 */     return groupData;
/*      */   }
/*      */   
/*      */   public void setPersistenceRequired() {
/* 1202 */     this.persistenceRequired = true;
/*      */   }
/*      */   
/*      */   public void setDropChance(EquipmentSlot slot, float percent) {
/* 1207 */     this.dropChances = this.dropChances.withEquipmentChance(slot, percent);
/*      */   }
/*      */   
/*      */   public boolean canPickUpLoot() {
/* 1212 */     return this.canPickUpLoot;
/*      */   }
/*      */   
/*      */   public void setCanPickUpLoot(boolean canPickUpLoot) {
/* 1216 */     this.canPickUpLoot = canPickUpLoot;
/*      */   }
/*      */   
/*      */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/* 1221 */     return canPickUpLoot();
/*      */   }
/*      */   
/*      */   public boolean isPersistenceRequired() {
/* 1225 */     return this.persistenceRequired;
/*      */   }
/*      */   
/*      */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/* 1230 */     if (!isAlive())
/* 1231 */       return (InteractionResult)InteractionResult.PASS; 
/* 1234 */     InteractionResult interactionResult = checkAndHandleImportantInteractions(player, hand);
/* 1235 */     if (interactionResult.consumesAction()) {
/* 1236 */       gameEvent((Holder<GameEvent>)GameEvent.ENTITY_INTERACT, (Entity)player);
/* 1237 */       return interactionResult;
/*      */     } 
/* 1239 */     InteractionResult superReaction = super.interact(player, hand, location);
/* 1240 */     if (superReaction != InteractionResult.PASS)
/* 1241 */       return superReaction; 
/* 1244 */     interactionResult = mobInteract(player, hand);
/* 1245 */     if (interactionResult.consumesAction()) {
/* 1246 */       gameEvent((Holder<GameEvent>)GameEvent.ENTITY_INTERACT, (Entity)player);
/* 1247 */       return interactionResult;
/*      */     } 
/* 1250 */     return (InteractionResult)InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   private InteractionResult checkAndHandleImportantInteractions(Player player, InteractionHand hand) {
/* 1254 */     ItemStack itemStack = player.getItemInHand(hand);
/* 1256 */     if (itemStack.is(Items.NAME_TAG)) {
/* 1257 */       InteractionResult nameTagInteractionResult = itemStack.interactLivingEntity(player, this, hand);
/* 1258 */       if (nameTagInteractionResult.consumesAction())
/* 1259 */         return nameTagInteractionResult; 
/*      */     } 
/* 1263 */     if (itemStack.getItem() instanceof SpawnEggItem) {
/* 1264 */       Level level = level();
/* 1264 */       if (level instanceof ServerLevel) {
/* 1264 */         ServerLevel serverLevel = (ServerLevel)level;
/* 1265 */         Optional<Mob> offspring = SpawnEggItem.spawnOffspringFromSpawnEgg(player, this, getType(), serverLevel, position(), itemStack);
/* 1266 */         offspring.ifPresent(mob -> onOffspringSpawnedFromEgg(player, mob));
/* 1267 */         if (offspring.isEmpty())
/* 1268 */           return (InteractionResult)InteractionResult.PASS; 
/*      */       } 
/* 1272 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*      */     } 
/* 1274 */     return (InteractionResult)InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   protected void onOffspringSpawnedFromEgg(Player spawner, Mob offspring) {}
/*      */   
/*      */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 1281 */     return (InteractionResult)InteractionResult.PASS;
/*      */   }
/*      */   
/*      */   protected void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack) {
/* 1285 */     int beforeUseCount = itemStack.getCount();
/* 1286 */     UseRemainder useRemainder = (UseRemainder)itemStack.get(DataComponents.USE_REMAINDER);
/* 1288 */     itemStack.consume(1, (LivingEntity)player);
/* 1289 */     if (useRemainder != null) {
/* 1290 */       Objects.requireNonNull(player);
/* 1290 */       ItemStack newHandStack = useRemainder.convertIntoRemainder(itemStack, beforeUseCount, player.hasInfiniteMaterials(), player::handleExtraItemsCreatedOnUse);
/* 1291 */       player.setItemInHand(hand, newHandStack);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isWithinHome() {
/* 1297 */     return isWithinHome(blockPosition());
/*      */   }
/*      */   
/*      */   public boolean isWithinHome(BlockPos pos) {
/* 1301 */     if (this.homeRadius == -1)
/* 1302 */       return true; 
/* 1304 */     return (this.homePosition.distSqr((Vec3i)pos) < (this.homeRadius * this.homeRadius));
/*      */   }
/*      */   
/*      */   public boolean isWithinHome(Vec3 pos) {
/* 1308 */     if (this.homeRadius == -1)
/* 1309 */       return true; 
/* 1311 */     return (this.homePosition.distToCenterSqr((Position)pos) < (this.homeRadius * this.homeRadius));
/*      */   }
/*      */   
/*      */   public void setHomeTo(BlockPos newCenter, int radius) {
/* 1315 */     this.homePosition = newCenter;
/* 1316 */     this.homeRadius = radius;
/*      */   }
/*      */   
/*      */   public BlockPos getHomePosition() {
/* 1320 */     return this.homePosition;
/*      */   }
/*      */   
/*      */   public int getHomeRadius() {
/* 1324 */     return this.homeRadius;
/*      */   }
/*      */   
/*      */   public void clearHome() {
/* 1328 */     this.homeRadius = -1;
/*      */   }
/*      */   
/*      */   public boolean hasHome() {
/* 1332 */     return (this.homeRadius != -1);
/*      */   }
/*      */   
/*      */   public <T extends Mob> T convertTo(EntityType<T> entityType, ConversionParams params, EntitySpawnReason spawnReason, ConversionParams.AfterConversion<T> afterConversion) {
/* 1336 */     if (isRemoved())
/* 1337 */       return null; 
/* 1340 */     Mob mob = (Mob)entityType.create(level(), spawnReason);
/* 1341 */     if (mob == null)
/* 1342 */       return null; 
/* 1345 */     params.type().convert(this, mob, params);
/* 1346 */     afterConversion.finalizeConversion((T)mob);
/* 1348 */     Level level = level();
/* 1348 */     if (level instanceof ServerLevel) {
/* 1348 */       ServerLevel serverLevel = (ServerLevel)level;
/* 1349 */       serverLevel.addFreshEntity(mob);
/*      */     } 
/* 1352 */     if (params.type().shouldDiscardAfterConversion())
/* 1353 */       discard(); 
/* 1355 */     return (T)mob;
/*      */   }
/*      */   
/*      */   public <T extends Mob> T convertTo(EntityType<T> entityType, ConversionParams params, ConversionParams.AfterConversion<T> afterConversion) {
/* 1359 */     return convertTo(entityType, params, EntitySpawnReason.CONVERSION, afterConversion);
/*      */   }
/*      */   
/*      */   public Leashable.LeashData getLeashData() {
/* 1364 */     return this.leashData;
/*      */   }
/*      */   
/*      */   private void resetAngularLeashMomentum() {
/* 1368 */     if (this.leashData != null)
/* 1369 */       this.leashData.angularMomentum = 0.0D; 
/*      */   }
/*      */   
/*      */   public void setLeashData(Leashable.LeashData leashData) {
/* 1375 */     this.leashData = leashData;
/*      */   }
/*      */   
/*      */   public void onLeashRemoved() {
/* 1380 */     if (getLeashData() == null)
/* 1381 */       clearHome(); 
/*      */   }
/*      */   
/*      */   public void leashTooFarBehaviour() {
/* 1387 */     super.leashTooFarBehaviour();
/* 1388 */     this.goalSelector.disableControlFlag(Goal.Flag.MOVE);
/*      */   }
/*      */   
/*      */   public boolean canBeLeashed() {
/* 1393 */     return !(this instanceof net.minecraft.world.entity.monster.Enemy);
/*      */   }
/*      */   
/*      */   public boolean startRiding(Entity entity, boolean force, boolean sendEventAndTriggers) {
/* 1398 */     boolean result = super.startRiding(entity, force, sendEventAndTriggers);
/* 1399 */     if (result && isLeashed())
/* 1401 */       dropLeash(); 
/* 1404 */     return result;
/*      */   }
/*      */   
/*      */   public boolean isEffectiveAi() {
/* 1409 */     return (super.isEffectiveAi() && !isNoAi());
/*      */   }
/*      */   
/*      */   public void setNoAi(boolean flag) {
/* 1413 */     byte val = ((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue();
/* 1414 */     this.entityData.set(DATA_MOB_FLAGS_ID, Byte.valueOf(flag ? (byte)(val | 0x1) : (byte)(val & 0xFFFFFFFE)));
/*      */   }
/*      */   
/*      */   public void setLeftHanded(boolean flag) {
/* 1418 */     byte val = ((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue();
/* 1419 */     this.entityData.set(DATA_MOB_FLAGS_ID, Byte.valueOf(flag ? (byte)(val | 0x2) : (byte)(val & 0xFFFFFFFD)));
/*      */   }
/*      */   
/*      */   public void setAggressive(boolean flag) {
/* 1423 */     byte val = ((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue();
/* 1424 */     this.entityData.set(DATA_MOB_FLAGS_ID, Byte.valueOf(flag ? (byte)(val | 0x4) : (byte)(val & 0xFFFFFFFB)));
/*      */   }
/*      */   
/*      */   public boolean isNoAi() {
/* 1428 */     return ((((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue() & 0x1) != 0);
/*      */   }
/*      */   
/*      */   public boolean isLeftHanded() {
/* 1432 */     return ((((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue() & 0x2) != 0);
/*      */   }
/*      */   
/*      */   public boolean isAggressive() {
/* 1436 */     return ((((Byte)this.entityData.get(DATA_MOB_FLAGS_ID)).byteValue() & 0x4) != 0);
/*      */   }
/*      */   
/*      */   public void setBaby(boolean baby) {}
/*      */   
/*      */   public HumanoidArm getMainArm() {
/* 1445 */     return isLeftHanded() ? HumanoidArm.LEFT : HumanoidArm.RIGHT;
/*      */   }
/*      */   
/*      */   public boolean isWithinMeleeAttackRange(LivingEntity target) {
/*      */     double maxRange, minRange;
/* 1449 */     AttackRange attackRange = (AttackRange)getActiveItem().get(DataComponents.ATTACK_RANGE);
/* 1454 */     if (attackRange == null) {
/* 1455 */       maxRange = DEFAULT_ATTACK_REACH;
/* 1456 */       minRange = 0.0D;
/*      */     } else {
/* 1458 */       maxRange = attackRange.effectiveMaxRange(this);
/* 1459 */       minRange = attackRange.effectiveMinRange(this);
/*      */     } 
/* 1462 */     AABB hitbox = target.getHitbox();
/* 1464 */     return (getAttackBoundingBox(maxRange).intersects(hitbox) && (minRange <= 0.0D || !getAttackBoundingBox(minRange).intersects(hitbox)));
/*      */   }
/*      */   
/*      */   protected AABB getAttackBoundingBox(double horizontalExpansion) {
/*      */     AABB aabb;
/* 1469 */     Entity vehicle = getVehicle();
/* 1470 */     if (vehicle != null) {
/* 1471 */       AABB mountAabb = vehicle.getBoundingBox();
/* 1472 */       AABB ownAabb = getBoundingBox();
/* 1479 */       aabb = new AABB(Math.min(ownAabb.minX, mountAabb.minX), ownAabb.minY, Math.min(ownAabb.minZ, mountAabb.minZ), Math.max(ownAabb.maxX, mountAabb.maxX), ownAabb.maxY, Math.max(ownAabb.maxZ, mountAabb.maxZ));
/*      */     } else {
/* 1482 */       aabb = getBoundingBox();
/*      */     } 
/* 1484 */     return aabb.inflate(horizontalExpansion, 0.0D, horizontalExpansion);
/*      */   }
/*      */   
/*      */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 1490 */     float dmg = (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/* 1492 */     ItemStack weaponItem = getWeaponItem();
/* 1494 */     DamageSource damageSource = weaponItem.getDamageSource(this);
/* 1496 */     dmg = EnchantmentHelper.modifyDamage(level, weaponItem, target, damageSource, dmg);
/* 1498 */     dmg += weaponItem.getItem().getAttackDamageBonus(target, dmg, damageSource);
/* 1500 */     Vec3 oldMovement = target.getDeltaMovement();
/* 1502 */     boolean wasHurt = target.hurtServer(level, damageSource, dmg);
/* 1504 */     if (wasHurt) {
/* 1505 */       causeExtraKnockback(target, getKnockback(target, damageSource), oldMovement, damageSource, dmg, true);
/* 1507 */       if (target instanceof LivingEntity) {
/* 1507 */         LivingEntity livingTarget = (LivingEntity)target;
/* 1508 */         weaponItem.hurtEnemy(livingTarget, this);
/*      */       } 
/* 1510 */       EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/* 1511 */       setLastHurtMob(target);
/* 1512 */       playAttackSound();
/*      */     } 
/* 1515 */     postPiercingAttack();
/* 1517 */     return wasHurt;
/*      */   }
/*      */   
/*      */   protected void jumpInLiquid(TagKey<Fluid> type) {
/* 1522 */     if (getNavigation().canFloat()) {
/* 1523 */       super.jumpInLiquid(type);
/*      */     } else {
/* 1525 */       setDeltaMovement(getDeltaMovement().add(0.0D, 0.3D, 0.0D));
/*      */     } 
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public void removeFreeWill() {
/* 1531 */     removeAllGoals(goal -> true);
/* 1532 */     getBrain().removeAllBehaviors();
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   @VisibleForTesting
/*      */   public GoalSelector getGoalSelector() {
/* 1538 */     return this.goalSelector;
/*      */   }
/*      */   
/*      */   public void removeAllGoals(Predicate<Goal> predicate) {
/* 1542 */     this.goalSelector.removeAllGoals(predicate);
/*      */   }
/*      */   
/*      */   protected void removeAfterChangingDimensions() {
/* 1547 */     super.removeAfterChangingDimensions();
/* 1548 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 1549 */       ItemStack itemStack = getItemBySlot(slot);
/* 1550 */       if (!itemStack.isEmpty())
/* 1551 */         itemStack.setCount(0); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public ItemStack getPickResult() {
/* 1558 */     return SpawnEggItem.byId(getType()).map(ItemStack::new).orElse(null);
/*      */   }
/*      */   
/*      */   protected void onAttributeUpdated(Holder<Attribute> attribute) {
/* 1563 */     super.onAttributeUpdated(attribute);
/* 1565 */     if (attribute.is(Attributes.FOLLOW_RANGE) || attribute.is(Attributes.TEMPT_RANGE))
/* 1566 */       getNavigation().updatePathfinderMaxVisitedNodes(); 
/*      */   }
/*      */   
/*      */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {
/* 1572 */     registration.register(DebugSubscriptions.ENTITY_PATHS, () -> {
/*      */           Path path = getNavigation().getPath();
/* 1574 */           return (path != null && path.debugData() != null) ? new DebugPathInfo(path.copy(), getNavigation().getMaxDistanceToWaypoint()) : null;
/*      */         });
/* 1579 */     registration.register(DebugSubscriptions.GOAL_SELECTORS, () -> {
/*      */           Set<WrappedGoal> availableGoals = this.goalSelector.getAvailableGoals();
/*      */           List<DebugGoalInfo.DebugGoal> goalInfo = new ArrayList<>(availableGoals.size());
/*      */           availableGoals.forEach(());
/*      */           return new DebugGoalInfo(goalInfo);
/*      */         });
/* 1587 */     if (!this.brain.isBrainDead())
/* 1588 */       registration.register(DebugSubscriptions.BRAINS, () -> DebugBrainDump.takeBrainDump(level, this)); 
/*      */   }
/*      */   
/*      */   public float chargeSpeedModifier() {
/* 1593 */     return 1.0F;
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Mob.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */