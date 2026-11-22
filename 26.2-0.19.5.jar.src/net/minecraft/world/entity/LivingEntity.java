/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ import com.google.common.annotations.VisibleForTesting;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
/*      */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*      */ import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import it.unimi.dsi.fastutil.objects.Reference2ObjectArrayMap;
/*      */ import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.EnumMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.function.Supplier;
/*      */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderSet;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.BlockParticleOption;
/*      */ import net.minecraft.core.particles.ItemParticleOption;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerChunkCache;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.waypoints.ServerWaypointManager;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.stats.Stats;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.DamageTypeTags;
/*      */ import net.minecraft.tags.EntityTypeTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.BlockUtil;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.damagesource.CombatRules;
/*      */ import net.minecraft.world.damagesource.CombatTracker;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.damagesource.DamageTypes;
/*      */ import net.minecraft.world.effect.MobEffect;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffectUtil;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.ai.Brain;
/*      */ import net.minecraft.world.entity.ai.attributes.Attribute;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeMap;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
/*      */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemInstance;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.ItemStackTemplate;
/*      */ import net.minecraft.world.item.Items;
/*      */ import net.minecraft.world.item.component.AttackRange;
/*      */ import net.minecraft.world.item.component.BlocksAttacks;
/*      */ import net.minecraft.world.item.component.DeathProtection;
/*      */ import net.minecraft.world.item.component.KineticWeapon;
/*      */ import net.minecraft.world.item.component.Weapon;
/*      */ import net.minecraft.world.item.enchantment.Enchantment;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.item.enchantment.effects.EnchantmentLocationBasedEffect;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ClipContext;
/*      */ import net.minecraft.world.level.CollisionGetter;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.BedBlock;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.HoneyBlock;
/*      */ import net.minecraft.world.level.block.LadderBlock;
/*      */ import net.minecraft.world.level.block.PowderSnowBlock;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.TrapDoorBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.gamerules.GameRules;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.LootParams;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*      */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.waypoints.Waypoint;
/*      */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*      */ import org.jetbrains.annotations.Contract;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public abstract class LivingEntity extends Entity implements Attackable, WaypointTransmitter {
/*  145 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   private static final String TAG_ACTIVE_EFFECTS = "active_effects";
/*      */   
/*      */   public static final String TAG_ATTRIBUTES = "attributes";
/*      */   
/*      */   public static final String TAG_SLEEPING_POS = "sleeping_pos";
/*      */   
/*      */   public static final String TAG_EQUIPMENT = "equipment";
/*      */   
/*      */   public static final String TAG_BRAIN = "Brain";
/*      */   
/*      */   public static final String TAG_FALL_FLYING = "FallFlying";
/*      */   
/*      */   public static final String TAG_HURT_TIME = "HurtTime";
/*      */   
/*      */   public static final String TAG_DEATH_TIME = "DeathTime";
/*      */   
/*      */   public static final String TAG_HEALTH = "Health";
/*      */   
/*  157 */   private static final Identifier SPEED_MODIFIER_POWDER_SNOW_ID = Identifier.withDefaultNamespace("powder_snow");
/*      */   
/*  158 */   private static final Identifier SPRINTING_MODIFIER_ID = Identifier.withDefaultNamespace("sprinting");
/*      */   
/*  159 */   private static final AttributeModifier SPEED_MODIFIER_SPRINTING = new AttributeModifier(SPRINTING_MODIFIER_ID, 0.30000001192092896D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
/*      */   
/*      */   public static final int EQUIPMENT_SLOT_OFFSET = 98;
/*      */   
/*      */   public static final int ARMOR_SLOT_OFFSET = 100;
/*      */   
/*      */   public static final int BODY_ARMOR_OFFSET = 105;
/*      */   
/*      */   public static final int SADDLE_OFFSET = 106;
/*      */   
/*      */   public static final int PLAYER_HURT_EXPERIENCE_TIME = 100;
/*      */   
/*      */   private static final int DAMAGE_SOURCE_TIMEOUT = 40;
/*      */   
/*      */   public static final double MIN_MOVEMENT_DISTANCE = 0.003D;
/*      */   
/*      */   public static final double DEFAULT_BASE_GRAVITY = 0.08D;
/*      */   
/*      */   public static final int DEATH_DURATION = 20;
/*      */   
/*      */   protected static final float INPUT_FRICTION = 0.98F;
/*      */   
/*      */   private static final int TICKS_PER_ELYTRA_FREE_FALL_EVENT = 10;
/*      */   
/*      */   private static final int FREE_FALL_EVENTS_PER_ELYTRA_BREAK = 2;
/*      */   
/*      */   public static final float BASE_JUMP_POWER = 0.42F;
/*      */   
/*      */   protected static final float DEFAULT_KNOCKBACK = 0.4F;
/*      */   
/*      */   protected static final int INVULNERABLE_DURATION = 20;
/*      */   
/*      */   protected static final int HURT_DURATION_TICKS = 10;
/*      */   
/*      */   private static final double CLIMBING_VERTICAL_SPEED = 0.2D;
/*      */   
/*      */   private static final float SWIM_AMOUNT_PER_TICK = 0.09F;
/*      */   
/*      */   private static final double MAX_LINE_OF_SIGHT_TEST_RANGE = 128.0D;
/*      */   
/*      */   protected static final int LIVING_ENTITY_FLAG_IS_USING = 1;
/*      */   
/*      */   protected static final int LIVING_ENTITY_FLAG_OFF_HAND = 2;
/*      */   
/*      */   protected static final int LIVING_ENTITY_FLAG_SPIN_ATTACK = 4;
/*      */   
/*  187 */   protected static final EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BYTE);
/*      */   
/*  188 */   private static final EntityDataAccessor<Float> DATA_HEALTH_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);
/*      */   
/*  189 */   private static final EntityDataAccessor<List<ParticleOptions>> DATA_EFFECT_PARTICLES = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.PARTICLES);
/*      */   
/*  190 */   private static final EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.BOOLEAN);
/*      */   
/*  191 */   private static final EntityDataAccessor<Integer> DATA_ARROW_COUNT_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
/*      */   
/*  192 */   private static final EntityDataAccessor<Integer> DATA_STINGER_COUNT_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.INT);
/*      */   
/*  193 */   private static final EntityDataAccessor<Optional<BlockPos>> SLEEPING_POS_ID = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
/*      */   
/*      */   private static final int PARTICLE_FREQUENCY_WHEN_INVISIBLE = 15;
/*      */   
/*  195 */   protected static final EntityDimensions SLEEPING_DIMENSIONS = EntityDimensions.fixed(0.2F, 0.2F).withEyeHeight(0.2F);
/*      */   
/*      */   public static final float EXTRA_RENDER_CULLING_SIZE_WITH_BIG_HAT = 0.5F;
/*      */   
/*      */   public static final float DEFAULT_BABY_SCALE = 0.5F;
/*      */   
/*      */   protected static final float SWIMMING_VERTICAL_SPEED = 0.04F;
/*      */   
/*      */   private static final int CURRENT_IMPULSE_CONTEXT_RESET_GRACE_TIME_TICKS = 40;
/*      */   
/*      */   private static final int DEFAULT_CURRENT_IMPULSE_CONTEXT_RESET_GRACE_TIME = 0;
/*      */   
/*      */   public static final float BASE_HORIZONTAL_AIR_DRAG = 0.91F;
/*      */   
/*      */   public static final float BASE_VERTICAL_AIR_DRAG = 0.98F;
/*      */   
/*      */   public static final float WATER_DRAG = 0.8F;
/*      */   
/*      */   public static final float SPRINTING_WATER_DRAG = 0.9F;
/*      */   
/*      */   public static final float LAVA_DRAG = 0.5F;
/*      */   
/*      */   public static final float LAVA_SHALLOW_VERTICAL_DRAG = 0.8F;
/*      */   
/*      */   public static final float DOLPHINS_GRACE_WATER_DRAG = 0.96F;
/*      */   
/*      */   public static final float FLYING_AIR_DRAG = 0.91F;
/*      */   
/*      */   public static final float FLYING_VERTICAL_AIR_DRAG = 0.91F;
/*      */   
/*      */   public static final float FLYING_LAVA_DRAG = 0.5F;
/*      */   
/*      */   public static final float FLYING_WATER_DRAG = 0.8F;
/*      */   
/*      */   public static final float ELYTRA_HORIZONTAL_AIR_DRAG = 0.99F;
/*      */   
/*      */   public static final float ELYTRA_VERTICAL_AIR_DRAG = 0.98F;
/*      */   
/*      */   public static final float BASE_SWIM_SPEED = 0.02F;
/*      */   
/*  217 */   private int currentImpulseContextResetGraceTime = 0;
/*      */   
/*      */   public static final Predicate<LivingEntity> PLAYER_NOT_WEARING_DISGUISE_ITEM;
/*      */   
/*      */   private final AttributeMap attributes;
/*      */   
/*      */   static {
/*  219 */     PLAYER_NOT_WEARING_DISGUISE_ITEM = (livingEntity -> {
/*      */         Player player;
/*      */         if (livingEntity instanceof Player) {
/*      */           player = (Player)livingEntity;
/*      */         } else {
/*      */           return true;
/*      */         } 
/*      */         ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
/*      */         return !helmet.is(ItemTags.GAZE_DISGUISE_EQUIPMENT);
/*      */       });
/*      */   }
/*      */   
/*  228 */   private final CombatTracker combatTracker = new CombatTracker(this);
/*      */   
/*  229 */   private final Map<Holder<MobEffect>, MobEffectInstance> activeEffects = Maps.newHashMap();
/*      */   
/*  230 */   private final Map<EquipmentSlot, ItemStack> lastEquipmentItems = Util.makeEnumMap(EquipmentSlot.class, slot -> ItemStack.EMPTY);
/*      */   
/*      */   public boolean swinging;
/*      */   
/*      */   private boolean discardFriction = false;
/*      */   
/*      */   public InteractionHand swingingArm;
/*      */   
/*      */   public int swingTime;
/*      */   
/*      */   public int removeArrowTime;
/*      */   
/*      */   public int removeStingerTime;
/*      */   
/*      */   public int hurtTime;
/*      */   
/*      */   public int hurtDuration;
/*      */   
/*      */   public int deathTime;
/*      */   
/*      */   public float oAttackAnim;
/*      */   
/*      */   public float attackAnim;
/*      */   
/*      */   protected int attackStrengthTicker;
/*      */   
/*      */   protected int itemSwapTicker;
/*      */   
/*  244 */   public final WalkAnimationState walkAnimation = new WalkAnimationState();
/*      */   
/*      */   public float yBodyRot;
/*      */   
/*      */   public float yBodyRotO;
/*      */   
/*      */   public float yHeadRot;
/*      */   
/*      */   public float yHeadRotO;
/*      */   
/*  249 */   public final ElytraAnimationState elytraAnimationState = new ElytraAnimationState(this);
/*      */   
/*      */   protected EntityReference<Player> lastHurtByPlayer;
/*      */   
/*      */   protected int lastHurtByPlayerMemoryTime;
/*      */   
/*      */   protected boolean dead;
/*      */   
/*      */   protected int noActionTime;
/*      */   
/*      */   protected float lastHurt;
/*      */   
/*      */   protected boolean jumping;
/*      */   
/*      */   public float xxa;
/*      */   
/*      */   public float yya;
/*      */   
/*      */   public float zza;
/*      */   
/*  262 */   protected final InterpolationHandler interpolation = new InterpolationHandler(this);
/*      */   
/*      */   protected double lerpYHeadRot;
/*      */   
/*      */   protected int lerpHeadSteps;
/*      */   
/*      */   private boolean effectsDirty = true;
/*      */   
/*      */   private EntityReference<LivingEntity> lastHurtByMob;
/*      */   
/*      */   private int lastHurtByMobTimestamp;
/*      */   
/*      */   private LivingEntity lastHurtMob;
/*      */   
/*      */   private int lastHurtMobTimestamp;
/*      */   
/*      */   private float speed;
/*      */   
/*      */   private int noJumpDelay;
/*      */   
/*      */   private float absorptionAmount;
/*      */   
/*  273 */   protected ItemStack useItem = ItemStack.EMPTY;
/*      */   
/*      */   protected int useItemRemaining;
/*      */   
/*      */   protected int fallFlyTicks;
/*      */   
/*  276 */   private long lastKineticHitFeedbackTime = -2147483648L;
/*      */   
/*      */   private BlockPos lastPos;
/*      */   
/*  278 */   private Optional<BlockPos> lastClimbablePos = Optional.empty();
/*      */   
/*      */   private DamageSource lastDamageSource;
/*      */   
/*      */   private long lastDamageStamp;
/*      */   
/*      */   protected int autoSpinAttackTicks;
/*      */   
/*      */   protected float autoSpinAttackDmg;
/*      */   
/*      */   protected ItemStack autoSpinAttackItemStack;
/*      */   
/*      */   protected Object2LongMap<Entity> recentKineticEnemies;
/*      */   
/*      */   private float swimAmount;
/*      */   
/*      */   private float swimAmountO;
/*      */   
/*      */   protected Brain<?> brain;
/*      */   
/*      */   private boolean skipDropExperience;
/*      */   
/*  289 */   private final EnumMap<EquipmentSlot, Reference2ObjectMap<Enchantment, Set<EnchantmentLocationBasedEffect>>> activeLocationDependentEnchantments = new EnumMap<>(EquipmentSlot.class);
/*      */   
/*      */   protected final EntityEquipment equipment;
/*      */   
/*  291 */   private Waypoint.Icon locatorBarIcon = new Waypoint.Icon();
/*      */   
/*      */   public Vec3 currentImpulseImpactPos;
/*      */   
/*      */   protected LivingEntity(EntityType<? extends LivingEntity> type, Level level) {
/*  296 */     super(type, level);
/*  298 */     this.attributes = new AttributeMap(DefaultAttributes.getSupplier(type));
/*  299 */     setHealth(getMaxHealth());
/*  301 */     this.equipment = createEquipment();
/*  303 */     this.blocksBuilding = true;
/*  304 */     reapplyPosition();
/*  305 */     setYRot(this.random.nextFloat() * 6.2831855F);
/*  306 */     this.yHeadRot = getYRot();
/*  309 */     this.brain = makeBrain(Brain.Packed.EMPTY);
/*      */   }
/*      */   
/*      */   public LivingEntity asLivingEntity() {
/*  314 */     return this;
/*      */   }
/*      */   
/*      */   @Contract(pure = true)
/*      */   protected EntityEquipment createEquipment() {
/*  320 */     return new EntityEquipment();
/*      */   }
/*      */   
/*      */   public Brain<? extends LivingEntity> getBrain() {
/*  324 */     return (Brain)this.brain;
/*      */   }
/*      */   
/*      */   protected Brain<? extends LivingEntity> makeBrain(Brain.Packed packedBrain) {
/*  329 */     return new Brain();
/*      */   }
/*      */   
/*      */   public void kill(ServerLevel level) {
/*  334 */     hurtServer(level, damageSources().genericKill(), Float.MAX_VALUE);
/*      */   }
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  339 */     entityData.define(DATA_LIVING_ENTITY_FLAGS, Byte.valueOf((byte)0));
/*  340 */     entityData.define(DATA_EFFECT_PARTICLES, List.of());
/*  341 */     entityData.define(DATA_EFFECT_AMBIENCE_ID, Boolean.valueOf(false));
/*  342 */     entityData.define(DATA_ARROW_COUNT_ID, Integer.valueOf(0));
/*  343 */     entityData.define(DATA_STINGER_COUNT_ID, Integer.valueOf(0));
/*  344 */     entityData.define(DATA_HEALTH_ID, Float.valueOf(1.0F));
/*  345 */     entityData.define(SLEEPING_POS_ID, Optional.empty());
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createLivingAttributes() {
/*  349 */     return AttributeSupplier.builder()
/*  350 */       .add(Attributes.MAX_HEALTH)
/*  351 */       .add(Attributes.KNOCKBACK_RESISTANCE)
/*  352 */       .add(Attributes.MOVEMENT_SPEED)
/*  353 */       .add(Attributes.ARMOR)
/*  354 */       .add(Attributes.ARMOR_TOUGHNESS)
/*  355 */       .add(Attributes.MAX_ABSORPTION)
/*  356 */       .add(Attributes.STEP_HEIGHT)
/*  357 */       .add(Attributes.SCALE)
/*  358 */       .add(Attributes.GRAVITY)
/*  359 */       .add(Attributes.SAFE_FALL_DISTANCE)
/*  360 */       .add(Attributes.FALL_DAMAGE_MULTIPLIER)
/*  361 */       .add(Attributes.JUMP_STRENGTH)
/*  362 */       .add(Attributes.ENTITY_INTERACTION_RANGE)
/*  363 */       .add(Attributes.OXYGEN_BONUS)
/*  364 */       .add(Attributes.BURNING_TIME)
/*  365 */       .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE)
/*  366 */       .add(Attributes.WATER_MOVEMENT_EFFICIENCY)
/*  367 */       .add(Attributes.MOVEMENT_EFFICIENCY)
/*  368 */       .add(Attributes.ATTACK_KNOCKBACK)
/*  369 */       .add(Attributes.CAMERA_DISTANCE)
/*  370 */       .add(Attributes.WAYPOINT_TRANSMIT_RANGE)
/*  371 */       .add(Attributes.BOUNCINESS)
/*  372 */       .add(Attributes.AIR_DRAG_MODIFIER)
/*  373 */       .add(Attributes.FRICTION_MODIFIER)
/*  374 */       .add(Attributes.NAME_TAG_DISTANCE)
/*  375 */       .add(Attributes.BELOW_NAME_DISTANCE);
/*      */   }
/*      */   
/*      */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
/*  381 */     if (!isInWater())
/*  383 */       updateFluidInteraction(); 
/*  386 */     Level level = level();
/*  386 */     if (level instanceof ServerLevel) {
/*  386 */       ServerLevel serverLevel = (ServerLevel)level;
/*  386 */       if (onGround && this.fallDistance > 0.0D) {
/*  388 */         onChangedBlock(serverLevel, pos);
/*  390 */         double power = Math.max(0, Mth.floor(calculateFallPower(this.fallDistance)));
/*  391 */         if (power > 0.0D && !onState.isAir()) {
/*  392 */           double x = getX();
/*  393 */           double y = getY();
/*  394 */           double z = getZ();
/*  396 */           BlockPos entityPos = blockPosition();
/*  397 */           if (pos.getX() != entityPos.getX() || pos.getZ() != entityPos.getZ()) {
/*  398 */             double xDiff = x - pos.getX() - 0.5D;
/*  399 */             double zDiff = z - pos.getZ() - 0.5D;
/*  400 */             double maxDiff = Math.max(Math.abs(xDiff), Math.abs(zDiff));
/*  402 */             x = pos.getX() + 0.5D + xDiff / maxDiff * 0.5D;
/*  403 */             z = pos.getZ() + 0.5D + zDiff / maxDiff * 0.5D;
/*      */           } 
/*  406 */           double scale = Math.min(0.20000000298023224D + power / 15.0D, 2.5D);
/*  407 */           int particles = (int)(150.0D * scale);
/*  408 */           serverLevel.sendParticles((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, onState), x, y, z, particles, 0.0D, 0.0D, 0.0D, 0.15000000596046448D);
/*      */         } 
/*      */       } 
/*      */     } 
/*  412 */     super.checkFallDamage(ya, onGround, onState, pos);
/*  414 */     if (onGround)
/*  415 */       this.lastClimbablePos = Optional.empty(); 
/*      */   }
/*      */   
/*      */   public boolean canBreatheUnderwater() {
/*  420 */     return is(EntityTypeTags.CAN_BREATHE_UNDER_WATER);
/*      */   }
/*      */   
/*      */   public float getSwimAmount(float a) {
/*  424 */     return Mth.lerp(a, this.swimAmountO, this.swimAmount);
/*      */   }
/*      */   
/*      */   public boolean hasLandedInLiquid() {
/*  428 */     return (getDeltaMovement().y() < 9.999999747378752E-6D && isInLiquid());
/*      */   }
/*      */   
/*      */   public void baseTick() {
/*  433 */     this.oAttackAnim = this.attackAnim;
/*  435 */     if (this.firstTick)
/*  436 */       getSleepingPos().ifPresent(this::setPosToBed); 
/*  439 */     Level level = level();
/*  439 */     if (level instanceof ServerLevel) {
/*  439 */       ServerLevel serverLevel = (ServerLevel)level;
/*  441 */       EnchantmentHelper.tickEffects(serverLevel, this);
/*      */     } 
/*  444 */     super.baseTick();
/*  446 */     ProfilerFiller profiler = Profiler.get();
/*  447 */     profiler.push("livingEntityBaseTick");
/*  449 */     if (isAlive()) {
/*  449 */       Level level1 = level();
/*  449 */       if (level1 instanceof ServerLevel) {
/*  449 */         ServerLevel serverLevel = (ServerLevel)level1;
/*  450 */         boolean isPlayer = this instanceof Player;
/*  451 */         if (isInWall()) {
/*  452 */           hurtServer(serverLevel, damageSources().inWall(), 1.0F);
/*  454 */         } else if (isPlayer && !serverLevel.getWorldBorder().isWithinBounds(getBoundingBox())) {
/*  455 */           double dist = serverLevel.getWorldBorder().getDistanceToBorder(this) + serverLevel.getWorldBorder().getSafeZone();
/*  456 */           if (dist < 0.0D) {
/*  457 */             double damagePerBlock = serverLevel.getWorldBorder().getDamagePerBlock();
/*  458 */             if (damagePerBlock > 0.0D)
/*  459 */               hurtServer(serverLevel, damageSources().outOfBorder(), Math.max(1, Mth.floor(-dist * damagePerBlock))); 
/*      */           } 
/*      */         } 
/*  465 */         if (isEyeInFluid(FluidTags.WATER) && !serverLevel.getBlockState(BlockPos.containing(getX(), getEyeY(), getZ())).is(Blocks.BUBBLE_COLUMN)) {
/*  466 */           boolean canDrownInWater = (!canBreatheUnderwater() && !MobEffectUtil.hasWaterBreathing(this) && (!isPlayer || !(((Player)this).getAbilities()).invulnerable));
/*  467 */           if (canDrownInWater) {
/*  468 */             setAirSupply(decreaseAirSupply(getAirSupply()));
/*  469 */             if (shouldTakeDrowningDamage()) {
/*  470 */               setAirSupply(0);
/*  471 */               serverLevel.broadcastEntityEvent(this, (byte)67);
/*  472 */               hurtServer(serverLevel, damageSources().drown(), 2.0F);
/*      */             } 
/*  474 */           } else if (getAirSupply() < getMaxAirSupply() && MobEffectUtil.shouldEffectsRefillAirsupply(this)) {
/*  475 */             setAirSupply(increaseAirSupply(getAirSupply()));
/*      */           } 
/*  478 */           if (isPassenger() && getVehicle() != null && getVehicle().dismountsUnderwater())
/*  479 */             stopRiding(); 
/*  481 */         } else if (getAirSupply() < getMaxAirSupply()) {
/*  482 */           setAirSupply(increaseAirSupply(getAirSupply()));
/*      */         } 
/*  485 */         BlockPos pos = blockPosition();
/*  486 */         if (!Objects.equal(this.lastPos, pos)) {
/*  487 */           this.lastPos = pos;
/*  488 */           onChangedBlock(serverLevel, pos);
/*      */         } 
/*      */       } 
/*      */     } 
/*  492 */     if (this.hurtTime > 0)
/*  493 */       this.hurtTime--; 
/*  495 */     if (this.invulnerableTime > 0 && !(this instanceof ServerPlayer))
/*  496 */       this.invulnerableTime--; 
/*  498 */     if (isDeadOrDying() && level().shouldTickDeath(this))
/*  499 */       tickDeath(); 
/*  501 */     if (this.lastHurtByPlayerMemoryTime > 0) {
/*  502 */       this.lastHurtByPlayerMemoryTime--;
/*      */     } else {
/*  504 */       this.lastHurtByPlayer = null;
/*      */     } 
/*  506 */     if (this.lastHurtMob != null && !this.lastHurtMob.isAlive())
/*  507 */       this.lastHurtMob = null; 
/*  510 */     LivingEntity hurtByMob = getLastHurtByMob();
/*  511 */     if (hurtByMob != null)
/*  512 */       if (!hurtByMob.isAlive()) {
/*  513 */         setLastHurtByMob((LivingEntity)null);
/*  514 */       } else if (this.tickCount - this.lastHurtByMobTimestamp > 100) {
/*  515 */         setLastHurtByMob((LivingEntity)null);
/*      */       }  
/*  520 */     tickEffects();
/*  522 */     this.yHeadRotO = this.yHeadRot;
/*  523 */     this.yBodyRotO = this.yBodyRot;
/*  524 */     this.yRotO = getYRot();
/*  525 */     this.xRotO = getXRot();
/*  527 */     profiler.pop();
/*      */   }
/*      */   
/*      */   protected boolean shouldTakeDrowningDamage() {
/*  531 */     return (getAirSupply() <= -20);
/*      */   }
/*      */   
/*      */   protected float getBlockSpeedFactor() {
/*  536 */     return Mth.lerp((float)getAttributeValue(Attributes.MOVEMENT_EFFICIENCY), super.getBlockSpeedFactor(), 1.0F);
/*      */   }
/*      */   
/*      */   private static float computeModifiedFriction(float friction, float modifier) {
/*  540 */     return Mth.clamp(1.0F - (1.0F - friction) * modifier, 0.0F, 1.0F);
/*      */   }
/*      */   
/*      */   public float getLuck() {
/*  544 */     return 0.0F;
/*      */   }
/*      */   
/*      */   protected void removeFrost() {
/*  548 */     AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/*  550 */     if (speed == null)
/*      */       return; 
/*  554 */     if (speed.getModifier(SPEED_MODIFIER_POWDER_SNOW_ID) != null)
/*  555 */       speed.removeModifier(SPEED_MODIFIER_POWDER_SNOW_ID); 
/*      */   }
/*      */   
/*      */   protected void tryAddFrost() {
/*  560 */     if (!getBlockStateOnLegacy().isAir()) {
/*  561 */       int ticksFrozen = getTicksFrozen();
/*  562 */       if (ticksFrozen > 0) {
/*  563 */         AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/*  565 */         if (speed == null)
/*      */           return; 
/*  569 */         float slowAmount = -0.05F * getPercentFrozen();
/*  570 */         speed.addTransientModifier(new AttributeModifier(SPEED_MODIFIER_POWDER_SNOW_ID, slowAmount, AttributeModifier.Operation.ADD_VALUE));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void onChangedBlock(ServerLevel level, BlockPos pos) {
/*  576 */     EnchantmentHelper.runLocationChangedEffects(level, this);
/*      */   }
/*      */   
/*      */   public boolean isBaby() {
/*  580 */     return false;
/*      */   }
/*      */   
/*      */   public float getAgeScale() {
/*  584 */     return isBaby() ? 0.5F : 1.0F;
/*      */   }
/*      */   
/*      */   public final float getScale() {
/*  588 */     AttributeMap attributes = getAttributes();
/*  589 */     if (attributes == null)
/*  591 */       return 1.0F; 
/*  593 */     return sanitizeScale((float)attributes.getValue(Attributes.SCALE));
/*      */   }
/*      */   
/*      */   protected float sanitizeScale(float scale) {
/*  597 */     return scale;
/*      */   }
/*      */   
/*      */   public boolean isAffectedByFluids() {
/*  601 */     return true;
/*      */   }
/*      */   
/*      */   protected void tickDeath() {
/*  605 */     this.deathTime++;
/*  606 */     if (this.deathTime >= 20 && !level().isClientSide() && !isRemoved()) {
/*  607 */       level().broadcastEntityEvent(this, (byte)60);
/*  608 */       remove(Entity.RemovalReason.KILLED);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean shouldDropExperience() {
/*  613 */     return !isBaby();
/*      */   }
/*      */   
/*      */   protected boolean shouldDropLoot(ServerLevel level) {
/*  617 */     return (!isBaby() && ((Boolean)level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue());
/*      */   }
/*      */   
/*      */   protected int decreaseAirSupply(int currentSupply) {
/*      */     double oxygenBonus;
/*  621 */     AttributeInstance respiration = getAttribute(Attributes.OXYGEN_BONUS);
/*  623 */     if (respiration != null) {
/*  624 */       oxygenBonus = respiration.getValue();
/*      */     } else {
/*  626 */       oxygenBonus = 0.0D;
/*      */     } 
/*  628 */     if (oxygenBonus > 0.0D && 
/*  629 */       this.random.nextDouble() >= 1.0D / (oxygenBonus + 1.0D))
/*  631 */       return currentSupply; 
/*  634 */     return currentSupply - 1;
/*      */   }
/*      */   
/*      */   protected int increaseAirSupply(int currentSupply) {
/*  638 */     return Math.min(currentSupply + 4, getMaxAirSupply());
/*      */   }
/*      */   
/*      */   public final int getExperienceReward(ServerLevel level, Entity killer) {
/*  642 */     return EnchantmentHelper.processMobExperience(level, killer, this, getBaseExperienceReward(level));
/*      */   }
/*      */   
/*      */   protected int getBaseExperienceReward(ServerLevel level) {
/*  646 */     return 0;
/*      */   }
/*      */   
/*      */   protected boolean isAlwaysExperienceDropper() {
/*  650 */     return false;
/*      */   }
/*      */   
/*      */   public LivingEntity getLastHurtByMob() {
/*  654 */     return EntityReference.getLivingEntity(this.lastHurtByMob, level());
/*      */   }
/*      */   
/*      */   public Player getLastHurtByPlayer() {
/*  658 */     return EntityReference.getPlayer(this.lastHurtByPlayer, level());
/*      */   }
/*      */   
/*      */   public LivingEntity getLastAttacker() {
/*  663 */     return getLastHurtByMob();
/*      */   }
/*      */   
/*      */   public int getLastHurtByMobTimestamp() {
/*  667 */     return this.lastHurtByMobTimestamp;
/*      */   }
/*      */   
/*      */   public void setLastHurtByPlayer(Player player, int timeToRemember) {
/*  671 */     setLastHurtByPlayer(EntityReference.of(player), timeToRemember);
/*      */   }
/*      */   
/*      */   public void setLastHurtByPlayer(UUID player, int timeToRemember) {
/*  675 */     setLastHurtByPlayer(EntityReference.of(player), timeToRemember);
/*      */   }
/*      */   
/*      */   private void setLastHurtByPlayer(EntityReference<Player> player, int timeToRemember) {
/*  679 */     this.lastHurtByPlayer = player;
/*  680 */     this.lastHurtByPlayerMemoryTime = timeToRemember;
/*      */   }
/*      */   
/*      */   public void setLastHurtByMob(LivingEntity hurtBy) {
/*  684 */     this.lastHurtByMob = EntityReference.of(hurtBy);
/*  685 */     this.lastHurtByMobTimestamp = this.tickCount;
/*      */   }
/*      */   
/*      */   public LivingEntity getLastHurtMob() {
/*  689 */     return this.lastHurtMob;
/*      */   }
/*      */   
/*      */   public int getLastHurtMobTimestamp() {
/*  693 */     return this.lastHurtMobTimestamp;
/*      */   }
/*      */   
/*      */   public void setLastHurtMob(Entity target) {
/*  697 */     if (target instanceof LivingEntity) {
/*  697 */       LivingEntity livingEntity = (LivingEntity)target;
/*  698 */       this.lastHurtMob = livingEntity;
/*      */     } else {
/*  700 */       this.lastHurtMob = null;
/*      */     } 
/*  702 */     this.lastHurtMobTimestamp = this.tickCount;
/*      */   }
/*      */   
/*      */   public int getNoActionTime() {
/*  706 */     return this.noActionTime;
/*      */   }
/*      */   
/*      */   public void setNoActionTime(int noActionTime) {
/*  710 */     this.noActionTime = noActionTime;
/*      */   }
/*      */   
/*      */   public boolean shouldDiscardFriction() {
/*  714 */     return this.discardFriction;
/*      */   }
/*      */   
/*      */   public void setDiscardFriction(boolean discardFriction) {
/*  718 */     this.discardFriction = discardFriction;
/*      */   }
/*      */   
/*      */   protected boolean doesEmitEquipEvent(EquipmentSlot slot) {
/*  722 */     return true;
/*      */   }
/*      */   
/*      */   public void onEquipItem(EquipmentSlot slot, ItemStack oldStack, ItemStack stack) {
/*  726 */     if (level().isClientSide() || isSpectator())
/*      */       return; 
/*  730 */     if (ItemStack.isSameItemSameComponents(oldStack, stack) || this.firstTick)
/*      */       return; 
/*  734 */     Equippable equippable = (Equippable)stack.get(DataComponents.EQUIPPABLE);
/*  735 */     if (!isSilent() && equippable != null && slot == equippable.slot())
/*  736 */       level().playSeededSound(null, getX(), getY(), getZ(), getEquipSound(slot, stack, equippable), getSoundSource(), 1.0F, 1.0F, this.random.nextLong()); 
/*  738 */     if (doesEmitEquipEvent(slot))
/*  739 */       gameEvent((equippable != null) ? (Holder<GameEvent>)GameEvent.EQUIP : (Holder<GameEvent>)GameEvent.UNEQUIP); 
/*      */   }
/*      */   
/*      */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot slot, ItemStack stack, Equippable equippable) {
/*  744 */     return equippable.equipSound();
/*      */   }
/*      */   
/*      */   public void remove(Entity.RemovalReason reason) {
/*  749 */     if (reason == Entity.RemovalReason.KILLED || reason == Entity.RemovalReason.DISCARDED) {
/*  749 */       Level level = level();
/*  749 */       if (level instanceof ServerLevel) {
/*  749 */         ServerLevel serverLevel = (ServerLevel)level;
/*  751 */         triggerOnDeathMobEffects(serverLevel, reason);
/*      */       } 
/*      */     } 
/*  753 */     super.remove(reason);
/*  754 */     this.brain.clearMemories();
/*      */   }
/*      */   
/*      */   public void onRemoval(Entity.RemovalReason reason) {
/*  759 */     super.onRemoval(reason);
/*  760 */     Level level = level();
/*  760 */     if (level instanceof ServerLevel) {
/*  760 */       ServerLevel serverLevel = (ServerLevel)level;
/*  761 */       serverLevel.getWaypointManager().untrackWaypoint(this);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void triggerOnDeathMobEffects(ServerLevel level, Entity.RemovalReason reason) {
/*  766 */     for (MobEffectInstance effect : getActiveEffects())
/*  767 */       effect.onMobRemoved(level, this, reason); 
/*  769 */     this.activeEffects.clear();
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  774 */     output.putFloat("Health", getHealth());
/*  775 */     output.putShort("HurtTime", (short)this.hurtTime);
/*  776 */     output.putShort("DeathTime", (short)this.deathTime);
/*  777 */     output.putFloat("AbsorptionAmount", getAbsorptionAmount());
/*  778 */     output.putInt("current_impulse_context_reset_grace_time", this.currentImpulseContextResetGraceTime);
/*  779 */     output.storeNullable("current_explosion_impact_pos", Vec3.CODEC, this.currentImpulseImpactPos);
/*  781 */     output.store("attributes", AttributeInstance.Packed.LIST_CODEC, getAttributes().pack());
/*  783 */     if (!this.activeEffects.isEmpty())
/*  784 */       output.store("active_effects", MobEffectInstance.CODEC.listOf(), List.copyOf(this.activeEffects.values())); 
/*  787 */     output.putBoolean("FallFlying", isFallFlying());
/*  789 */     getSleepingPos().ifPresent(sleepingPos -> output.store("sleeping_pos", BlockPos.CODEC, sleepingPos));
/*  793 */     output.store("Brain", Brain.Packed.CODEC, this.brain.pack());
/*  795 */     if (this.lastHurtByPlayer != null) {
/*  796 */       this.lastHurtByPlayer.store(output, "last_hurt_by_player");
/*  797 */       output.putInt("last_hurt_by_player_memory_time", this.lastHurtByPlayerMemoryTime);
/*      */     } 
/*  799 */     if (this.lastHurtByMob != null) {
/*  800 */       this.lastHurtByMob.store(output, "last_hurt_by_mob");
/*  801 */       output.putInt("ticks_since_last_hurt_by_mob", this.tickCount - this.lastHurtByMobTimestamp);
/*      */     } 
/*  804 */     if (!this.equipment.isEmpty())
/*  805 */       output.store("equipment", EntityEquipment.CODEC, this.equipment); 
/*  808 */     if (this.locatorBarIcon.hasData())
/*  809 */       output.store("locator_bar_icon", Waypoint.Icon.CODEC, this.locatorBarIcon); 
/*      */   }
/*      */   
/*      */   public ItemEntity drop(ItemStack itemStack, boolean randomly, boolean thrownFromHand) {
/*  814 */     if (itemStack.isEmpty())
/*  815 */       return null; 
/*  818 */     if (level().isClientSide()) {
/*  819 */       swing(InteractionHand.MAIN_HAND);
/*  820 */       return null;
/*      */     } 
/*  823 */     ItemEntity entity = createItemStackToDrop(itemStack, randomly, thrownFromHand);
/*  824 */     if (entity != null)
/*  825 */       level().addFreshEntity((Entity)entity); 
/*  827 */     return entity;
/*      */   }
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  832 */     internalSetAbsorptionAmount(input.getFloatOr("AbsorptionAmount", 0.0F));
/*  834 */     if (level() != null && !level().isClientSide()) {
/*  835 */       Objects.requireNonNull(getAttributes());
/*  835 */       input.read("attributes", AttributeInstance.Packed.LIST_CODEC).ifPresent(getAttributes()::apply);
/*      */     } 
/*  838 */     List<MobEffectInstance> effects = input.read("active_effects", MobEffectInstance.CODEC.listOf()).orElse(List.of());
/*  839 */     this.activeEffects.clear();
/*  840 */     for (MobEffectInstance effect : effects) {
/*  841 */       this.activeEffects.put(effect.getEffect(), effect);
/*  842 */       this.effectsDirty = true;
/*      */     } 
/*  845 */     setHealth(input.getFloatOr("Health", getMaxHealth()));
/*  847 */     this.hurtTime = input.getShortOr("HurtTime", (short)0);
/*  848 */     this.deathTime = input.getShortOr("DeathTime", (short)0);
/*  851 */     input.getString("Team").ifPresent(teamName -> {
/*      */           Scoreboard scoreboard = level().getScoreboard();
/*      */           PlayerTeam team = scoreboard.getPlayerTeam(teamName);
/*  854 */           boolean success = (team != null && scoreboard.addPlayerToTeam(getStringUUID(), team));
/*      */           if (!success)
/*      */             LOGGER.warn("Unable to add mob to team \"{}\" (that team probably doesn't exist)", teamName); 
/*      */         });
/*  860 */     setSharedFlag(7, input.getBooleanOr("FallFlying", false));
/*  862 */     input.read("sleeping_pos", BlockPos.CODEC).ifPresentOrElse(sleepingPos -> {
/*      */           setSleepingPos(sleepingPos);
/*      */           this.entityData.set(DATA_POSE, Pose.SLEEPING);
/*      */           if (!this.firstTick)
/*      */             setPosToBed(sleepingPos); 
/*  862 */         }this::clearSleepingPos);
/*  875 */     input.read("Brain", Brain.Packed.CODEC).ifPresent(packedBrain -> this.brain = makeBrain(packedBrain));
/*  877 */     this.lastHurtByPlayer = EntityReference.read(input, "last_hurt_by_player");
/*  878 */     this.lastHurtByPlayerMemoryTime = input.getIntOr("last_hurt_by_player_memory_time", 0);
/*  880 */     this.lastHurtByMob = EntityReference.read(input, "last_hurt_by_mob");
/*  881 */     this.lastHurtByMobTimestamp = this.tickCount - input.getIntOr("ticks_since_last_hurt_by_mob", 0);
/*  883 */     this.equipment.setAll(input.read("equipment", EntityEquipment.CODEC).orElseGet(EntityEquipment::new));
/*  885 */     this.locatorBarIcon = input.read("locator_bar_icon", Waypoint.Icon.CODEC).orElseGet(net.minecraft.world.waypoints.Waypoint.Icon::new);
/*  887 */     this.currentImpulseContextResetGraceTime = input.getIntOr("current_impulse_context_reset_grace_time", 0);
/*  888 */     this.currentImpulseImpactPos = input.read("current_explosion_impact_pos", Vec3.CODEC).orElse(null);
/*      */   }
/*      */   
/*      */   public void updateDataBeforeSync() {
/*  893 */     super.updateDataBeforeSync();
/*  894 */     updateDirtyEffects();
/*      */   }
/*      */   
/*      */   protected void tickEffects() {
/*  898 */     Level level = level();
/*  898 */     if (level instanceof ServerLevel) {
/*  898 */       ServerLevel serverLevel = (ServerLevel)level;
/*  899 */       Iterator<Holder<MobEffect>> iterator = this.activeEffects.keySet().iterator();
/*      */       try {
/*  901 */         while (iterator.hasNext()) {
/*  902 */           Holder<MobEffect> mobEffect = iterator.next();
/*  903 */           MobEffectInstance effect = this.activeEffects.get(mobEffect);
/*  905 */           if (!effect.tickServer(serverLevel, this, () -> onEffectUpdated(effect, true, (Entity)null))) {
/*  906 */             iterator.remove();
/*  907 */             onEffectsRemoved(List.of(effect));
/*      */             continue;
/*      */           } 
/*  908 */           if (effect.getDuration() % 600 == 0)
/*  910 */             onEffectUpdated(effect, false, (Entity)null); 
/*      */         } 
/*  913 */       } catch (ConcurrentModificationException concurrentModificationException) {}
/*      */     } else {
/*  919 */       for (MobEffectInstance effect : this.activeEffects.values())
/*  920 */         effect.tickClient(); 
/*  923 */       List<ParticleOptions> particles = (List<ParticleOptions>)this.entityData.get(DATA_EFFECT_PARTICLES);
/*  924 */       if (!particles.isEmpty()) {
/*  925 */         boolean isAmbient = ((Boolean)this.entityData.get(DATA_EFFECT_AMBIENCE_ID)).booleanValue();
/*  926 */         int bound = isInvisible() ? 15 : 4;
/*  927 */         int ambientFactor = isAmbient ? 5 : 1;
/*  929 */         if (this.random.nextInt(bound * ambientFactor) == 0)
/*  930 */           level().addParticle((ParticleOptions)Util.getRandom(particles, this.random), getRandomX(0.5D), getRandomY(), getRandomZ(0.5D), 1.0D, 1.0D, 1.0D); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void updateDirtyEffects() {
/*  937 */     if (this.effectsDirty) {
/*  938 */       updateInvisibilityStatus();
/*  939 */       updateGlowingStatus();
/*  940 */       this.effectsDirty = false;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void updateInvisibilityStatus() {
/*  945 */     if (this.activeEffects.isEmpty()) {
/*  946 */       removeEffectParticles();
/*  947 */       setInvisible(false);
/*      */       return;
/*      */     } 
/*  951 */     setInvisible(hasEffect(MobEffects.INVISIBILITY));
/*  953 */     updateSynchronizedMobEffectParticles();
/*      */   }
/*      */   
/*      */   private void updateSynchronizedMobEffectParticles() {
/*  960 */     List<ParticleOptions> visibleEffectParticles = this.activeEffects.values().stream().filter(MobEffectInstance::isVisible).map(MobEffectInstance::getParticleOptions).toList();
/*  962 */     this.entityData.set(DATA_EFFECT_PARTICLES, visibleEffectParticles);
/*  963 */     this.entityData.set(DATA_EFFECT_AMBIENCE_ID, Boolean.valueOf(areAllEffectsAmbient(this.activeEffects.values())));
/*      */   }
/*      */   
/*      */   private void updateGlowingStatus() {
/*  967 */     boolean glowingState = isCurrentlyGlowing();
/*  968 */     if (getSharedFlag(6) != glowingState)
/*  969 */       setSharedFlag(6, glowingState); 
/*      */   }
/*      */   
/*      */   public double getVisibilityPercent(Entity targetingEntity) {
/*  974 */     double visibilityPercent = 1.0D;
/*  976 */     if (isDiscrete())
/*  977 */       visibilityPercent *= 0.8D; 
/*  979 */     if (isInvisible()) {
/*  980 */       float coverPercentage = getArmorCoverPercentage();
/*  981 */       if (coverPercentage < 0.1F)
/*  982 */         coverPercentage = 0.1F; 
/*  984 */       visibilityPercent *= 0.7D * coverPercentage;
/*      */     } 
/*  986 */     if (targetingEntity != null) {
/*  987 */       ItemStack itemStack = getItemBySlot(EquipmentSlot.HEAD);
/*  989 */       if ((targetingEntity.is(EntityTypes.SKELETON) && itemStack.is(Items.SKELETON_SKULL)) || (targetingEntity
/*  990 */         .is(EntityTypes.ZOMBIE) && itemStack.is(Items.ZOMBIE_HEAD)) || (targetingEntity
/*  991 */         .is(EntityTypes.PIGLIN) && itemStack.is(Items.PIGLIN_HEAD)) || (targetingEntity
/*  992 */         .is(EntityTypes.PIGLIN_BRUTE) && itemStack.is(Items.PIGLIN_HEAD)) || (targetingEntity
/*  993 */         .is(EntityTypes.CREEPER) && itemStack.is(Items.CREEPER_HEAD)))
/*  995 */         visibilityPercent *= 0.5D; 
/*      */     } 
/*  999 */     return visibilityPercent;
/*      */   }
/*      */   
/*      */   public boolean canAttack(LivingEntity target) {
/* 1003 */     if (target instanceof Player && level().getDifficulty() == Difficulty.PEACEFUL)
/* 1004 */       return false; 
/* 1006 */     return target.canBeSeenAsEnemy();
/*      */   }
/*      */   
/*      */   public boolean canBeSeenAsEnemy() {
/* 1011 */     return (!isInvulnerable() && canBeSeenByAnyone());
/*      */   }
/*      */   
/*      */   public boolean canBeSeenByAnyone() {
/* 1015 */     return (!isSpectator() && isAlive());
/*      */   }
/*      */   
/*      */   public static boolean areAllEffectsAmbient(Collection<MobEffectInstance> effects) {
/* 1019 */     for (MobEffectInstance effect : effects) {
/* 1020 */       if (effect.isVisible() && !effect.isAmbient())
/* 1021 */         return false; 
/*      */     } 
/* 1025 */     return true;
/*      */   }
/*      */   
/*      */   protected void removeEffectParticles() {
/* 1029 */     this.entityData.set(DATA_EFFECT_PARTICLES, List.of());
/*      */   }
/*      */   
/*      */   public boolean removeAllEffects() {
/* 1033 */     if (level().isClientSide())
/* 1034 */       return false; 
/* 1037 */     if (this.activeEffects.isEmpty())
/* 1038 */       return false; 
/* 1041 */     Map<Holder<MobEffect>, MobEffectInstance> copy = Maps.newHashMap(this.activeEffects);
/* 1042 */     this.activeEffects.clear();
/* 1043 */     onEffectsRemoved(copy.values());
/* 1045 */     return true;
/*      */   }
/*      */   
/*      */   public Collection<MobEffectInstance> getActiveEffects() {
/* 1049 */     return this.activeEffects.values();
/*      */   }
/*      */   
/*      */   public Map<Holder<MobEffect>, MobEffectInstance> getActiveEffectsMap() {
/* 1053 */     return this.activeEffects;
/*      */   }
/*      */   
/*      */   public boolean hasEffect(Holder<MobEffect> effect) {
/* 1057 */     return this.activeEffects.containsKey(effect);
/*      */   }
/*      */   
/*      */   public MobEffectInstance getEffect(Holder<MobEffect> effect) {
/* 1061 */     return this.activeEffects.get(effect);
/*      */   }
/*      */   
/*      */   public float getEffectBlendFactor(Holder<MobEffect> effect, float partialTicks) {
/* 1065 */     MobEffectInstance instance = getEffect(effect);
/* 1066 */     if (instance != null)
/* 1067 */       return instance.getBlendFactor(this, partialTicks); 
/* 1069 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public final boolean addEffect(MobEffectInstance newEffect) {
/* 1073 */     return addEffect(newEffect, (Entity)null);
/*      */   }
/*      */   
/*      */   public boolean addEffect(MobEffectInstance newEffect, Entity source) {
/* 1077 */     if (!canBeAffected(newEffect))
/* 1078 */       return false; 
/* 1081 */     MobEffectInstance effect = this.activeEffects.get(newEffect.getEffect());
/* 1082 */     boolean changed = false;
/* 1083 */     if (effect == null) {
/* 1084 */       this.activeEffects.put(newEffect.getEffect(), newEffect);
/* 1085 */       onEffectAdded(newEffect, source);
/* 1086 */       changed = true;
/* 1087 */       newEffect.onEffectAdded(this);
/* 1090 */     } else if (effect.update(newEffect)) {
/* 1091 */       onEffectUpdated(effect, true, source);
/* 1092 */       changed = true;
/*      */     } 
/* 1095 */     newEffect.onEffectStarted(this);
/* 1097 */     return changed;
/*      */   }
/*      */   
/*      */   public boolean canBeAffected(MobEffectInstance newEffect) {
/* 1101 */     if (is(EntityTypeTags.IMMUNE_TO_INFESTED))
/* 1102 */       return !newEffect.is(MobEffects.INFESTED); 
/* 1104 */     if (is(EntityTypeTags.IMMUNE_TO_OOZING))
/* 1105 */       return !newEffect.is(MobEffects.OOZING); 
/* 1107 */     if (is(EntityTypeTags.IGNORES_POISON_AND_REGEN))
/* 1108 */       return (!newEffect.is(MobEffects.REGENERATION) && !newEffect.is(MobEffects.POISON)); 
/* 1110 */     return true;
/*      */   }
/*      */   
/*      */   public void forceAddEffect(MobEffectInstance newEffect, Entity source) {
/* 1114 */     if (!canBeAffected(newEffect))
/*      */       return; 
/* 1118 */     MobEffectInstance previousEffect = this.activeEffects.put(newEffect.getEffect(), newEffect);
/* 1119 */     if (previousEffect == null) {
/* 1120 */       onEffectAdded(newEffect, source);
/*      */     } else {
/* 1122 */       newEffect.copyBlendState(previousEffect);
/* 1123 */       onEffectUpdated(newEffect, true, source);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isInvertedHealAndHarm() {
/* 1128 */     return is(EntityTypeTags.INVERTED_HEALING_AND_HARM);
/*      */   }
/*      */   
/*      */   public final MobEffectInstance removeEffectNoUpdate(Holder<MobEffect> effect) {
/* 1132 */     return this.activeEffects.remove(effect);
/*      */   }
/*      */   
/*      */   public boolean removeEffect(Holder<MobEffect> effect) {
/* 1136 */     MobEffectInstance effectInstance = removeEffectNoUpdate(effect);
/* 1137 */     if (effectInstance != null) {
/* 1138 */       onEffectsRemoved(List.of(effectInstance));
/* 1139 */       return true;
/*      */     } 
/* 1141 */     return false;
/*      */   }
/*      */   
/*      */   protected void onEffectAdded(MobEffectInstance effect, Entity source) {
/* 1145 */     if (!level().isClientSide()) {
/* 1146 */       this.effectsDirty = true;
/* 1147 */       ((MobEffect)effect.getEffect().value()).addAttributeModifiers(getAttributes(), effect.getAmplifier());
/* 1148 */       sendEffectToPassengers(effect);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void sendEffectToPassengers(MobEffectInstance effect) {
/* 1153 */     for (Entity passenger : getPassengers()) {
/* 1154 */       if (passenger instanceof ServerPlayer) {
/* 1154 */         ServerPlayer serverPlayer = (ServerPlayer)passenger;
/* 1155 */         serverPlayer.connection.send((Packet)new ClientboundUpdateMobEffectPacket(getId(), effect, false));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void onEffectUpdated(MobEffectInstance effect, boolean doRefreshAttributes, Entity source) {
/* 1161 */     if (level().isClientSide())
/*      */       return; 
/* 1164 */     this.effectsDirty = true;
/* 1165 */     if (doRefreshAttributes) {
/* 1166 */       MobEffect mobEffect = (MobEffect)effect.getEffect().value();
/* 1167 */       mobEffect.removeAttributeModifiers(getAttributes());
/* 1168 */       mobEffect.addAttributeModifiers(getAttributes(), effect.getAmplifier());
/* 1169 */       refreshDirtyAttributes();
/*      */     } 
/* 1171 */     sendEffectToPassengers(effect);
/*      */   }
/*      */   
/*      */   protected void onEffectsRemoved(Collection<MobEffectInstance> effects) {
/* 1175 */     if (level().isClientSide())
/*      */       return; 
/* 1178 */     this.effectsDirty = true;
/* 1179 */     for (MobEffectInstance effect : effects) {
/* 1180 */       ((MobEffect)effect.getEffect().value()).removeAttributeModifiers(getAttributes());
/* 1181 */       for (Entity passenger : getPassengers()) {
/* 1182 */         if (passenger instanceof ServerPlayer) {
/* 1182 */           ServerPlayer serverPlayer = (ServerPlayer)passenger;
/* 1183 */           serverPlayer.connection.send((Packet)new ClientboundRemoveMobEffectPacket(getId(), effect.getEffect()));
/*      */         } 
/*      */       } 
/*      */     } 
/* 1187 */     refreshDirtyAttributes();
/*      */   }
/*      */   
/*      */   private void refreshDirtyAttributes() {
/* 1191 */     Set<AttributeInstance> attributesToUpdate = getAttributes().getAttributesToUpdate();
/* 1192 */     for (AttributeInstance changedAttributeInstance : attributesToUpdate)
/* 1193 */       onAttributeUpdated(changedAttributeInstance.getAttribute()); 
/* 1195 */     attributesToUpdate.clear();
/*      */   }
/*      */   
/*      */   protected void onAttributeUpdated(Holder<Attribute> attribute) {
/* 1199 */     if (attribute.is(Attributes.MAX_HEALTH)) {
/* 1200 */       float currentMaxHealth = getMaxHealth();
/* 1201 */       if (getHealth() > currentMaxHealth)
/* 1202 */         setHealth(currentMaxHealth); 
/* 1204 */     } else if (attribute.is(Attributes.MAX_ABSORPTION)) {
/* 1205 */       float currentMaxAbsorption = getMaxAbsorption();
/* 1206 */       if (getAbsorptionAmount() > currentMaxAbsorption)
/* 1207 */         setAbsorptionAmount(currentMaxAbsorption); 
/* 1209 */     } else if (attribute.is(Attributes.SCALE)) {
/* 1210 */       refreshDimensions();
/* 1211 */     } else if (attribute.is(Attributes.WAYPOINT_TRANSMIT_RANGE)) {
/* 1212 */       Level level = level();
/* 1212 */       if (level instanceof ServerLevel) {
/* 1212 */         ServerLevel serverLevel = (ServerLevel)level;
/* 1213 */         ServerWaypointManager waypointManager = serverLevel.getWaypointManager();
/* 1214 */         if (this.attributes.getValue(attribute) > 0.0D) {
/* 1215 */           waypointManager.trackWaypoint(this);
/*      */         } else {
/* 1217 */           waypointManager.untrackWaypoint(this);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void heal(float heal) {
/* 1224 */     float health = getHealth();
/* 1225 */     if (health > 0.0F)
/* 1226 */       setHealth(health + heal); 
/*      */   }
/*      */   
/*      */   public float getHealth() {
/* 1231 */     return ((Float)this.entityData.get(DATA_HEALTH_ID)).floatValue();
/*      */   }
/*      */   
/*      */   public void setHealth(float health) {
/* 1235 */     this.entityData.set(DATA_HEALTH_ID, Float.valueOf(Mth.clamp(health, 0.0F, getMaxHealth())));
/*      */   }
/*      */   
/*      */   public boolean isDeadOrDying() {
/* 1239 */     return (getHealth() <= 0.0F || this.dead);
/*      */   }
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 1244 */     if (isInvulnerableTo(level, source))
/* 1245 */       return false; 
/* 1248 */     if (isDeadOrDying())
/* 1249 */       return false; 
/* 1252 */     if (source.is(DamageTypeTags.IS_FIRE) && hasEffect(MobEffects.FIRE_RESISTANCE))
/* 1253 */       return false; 
/* 1256 */     if (isSleeping())
/* 1257 */       stopSleeping(); 
/* 1260 */     this.noActionTime = 0;
/* 1261 */     if (damage < 0.0F)
/* 1262 */       damage = 0.0F; 
/* 1264 */     float originalDamage = damage;
/* 1267 */     ItemStack itemInUse = getUseItem();
/* 1268 */     float damageBlocked = applyItemBlocking(level, source, damage);
/* 1269 */     damage -= damageBlocked;
/* 1270 */     boolean blocked = (damageBlocked > 0.0F);
/* 1272 */     if (source.is(DamageTypeTags.IS_FREEZING) && is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES))
/* 1273 */       damage *= 5.0F; 
/* 1276 */     if (source.is(DamageTypeTags.DAMAGES_HELMET) && !getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
/* 1277 */       hurtHelmet(source, damage);
/* 1278 */       damage *= 0.75F;
/*      */     } 
/* 1282 */     if (Float.isNaN(damage) || Float.isInfinite(damage))
/* 1283 */       damage = Float.MAX_VALUE; 
/* 1286 */     boolean tookFullDamage = true;
/* 1287 */     if (this.invulnerableTime > 10.0F && !source.is(DamageTypeTags.BYPASSES_COOLDOWN)) {
/* 1288 */       if (damage <= this.lastHurt)
/* 1289 */         return false; 
/* 1291 */       actuallyHurt(level, source, damage - this.lastHurt);
/* 1292 */       this.lastHurt = damage;
/* 1293 */       tookFullDamage = false;
/*      */     } else {
/* 1295 */       this.lastHurt = damage;
/* 1296 */       this.invulnerableTime = 20;
/* 1297 */       actuallyHurt(level, source, damage);
/* 1298 */       this.hurtDuration = 10;
/* 1299 */       this.hurtTime = this.hurtDuration;
/*      */     } 
/* 1302 */     resolveMobResponsibleForDamage(source);
/* 1303 */     resolvePlayerResponsibleForDamage(source);
/* 1305 */     if (tookFullDamage) {
/* 1306 */       BlocksAttacks blocksAttacks = (BlocksAttacks)itemInUse.get(DataComponents.BLOCKS_ATTACKS);
/* 1307 */       if (blocked && blocksAttacks != null) {
/* 1308 */         blocksAttacks.onBlocked(level, this);
/*      */       } else {
/* 1310 */         level.broadcastDamageEvent(this, source);
/*      */       } 
/* 1312 */       if (!source.is(DamageTypeTags.NO_IMPACT) && (!blocked || damage > 0.0F))
/* 1313 */         markHurt(); 
/* 1315 */       if (!source.is(DamageTypeTags.NO_KNOCKBACK))
/* 1316 */         dealDefaultKnockback(source, damage, blocked); 
/*      */     } 
/* 1320 */     if (isDeadOrDying()) {
/* 1321 */       if (!checkTotemDeathProtection(source)) {
/* 1322 */         if (tookFullDamage) {
/* 1323 */           makeSound(getDeathSound());
/* 1324 */           playSecondaryHurtSound(source);
/*      */         } 
/* 1326 */         die(source);
/*      */       } 
/* 1328 */     } else if (tookFullDamage) {
/* 1329 */       playHurtSound(source);
/* 1330 */       playSecondaryHurtSound(source);
/*      */     } 
/* 1333 */     boolean success = (!blocked || damage > 0.0F);
/* 1334 */     if (success) {
/* 1335 */       this.lastDamageSource = source;
/* 1336 */       this.lastDamageStamp = level().getGameTime();
/* 1338 */       for (MobEffectInstance effect : getActiveEffects())
/* 1339 */         effect.onMobHurt(level, this, source, damage); 
/*      */     } 
/* 1344 */     LivingEntity livingEntity = this;
/* 1344 */     if (livingEntity instanceof ServerPlayer) {
/* 1344 */       ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
/* 1345 */       CriteriaTriggers.ENTITY_HURT_PLAYER.trigger(serverPlayer, source, originalDamage, damage, blocked);
/* 1347 */       if (damageBlocked > 0.0F && damageBlocked < 3.4028235E37F)
/* 1348 */         serverPlayer.awardStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(damageBlocked * 10.0F)); 
/*      */     } 
/* 1351 */     Entity entity = source.getEntity();
/* 1351 */     if (entity instanceof ServerPlayer) {
/* 1351 */       ServerPlayer sourcePlayer = (ServerPlayer)entity;
/* 1352 */       CriteriaTriggers.PLAYER_HURT_ENTITY.trigger(sourcePlayer, this, source, originalDamage, damage, blocked);
/*      */     } 
/* 1355 */     return success;
/*      */   }
/*      */   
/*      */   public void dealDefaultKnockback(DamageSource source, float damage, boolean blocked) {
/* 1359 */     double xd = 0.0D;
/* 1360 */     double zd = 0.0D;
/* 1361 */     Entity entity = source.getDirectEntity();
/* 1361 */     if (entity instanceof Projectile) {
/* 1361 */       Projectile projectile = (Projectile)entity;
/* 1362 */       DoubleDoubleImmutablePair knockbackDirection = projectile.calculateHorizontalHurtKnockbackDirection(this, source);
/* 1363 */       xd = -knockbackDirection.leftDouble();
/* 1364 */       zd = -knockbackDirection.rightDouble();
/* 1365 */     } else if (source.getSourcePosition() != null) {
/* 1366 */       xd = source.getSourcePosition().x() - getX();
/* 1367 */       zd = source.getSourcePosition().z() - getZ();
/*      */     } 
/* 1369 */     knockback(0.4000000059604645D, xd, zd, source, damage);
/* 1370 */     if (!blocked)
/* 1371 */       indicateDamage(xd, zd); 
/*      */   }
/*      */   
/*      */   public float applyItemBlocking(ServerLevel level, DamageSource source, float damage) {
/*      */     double angle;
/* 1376 */     if (damage <= 0.0F)
/* 1377 */       return 0.0F; 
/* 1379 */     ItemStack blockingWith = getItemBlockingWith();
/* 1380 */     if (blockingWith == null)
/* 1381 */       return 0.0F; 
/* 1383 */     BlocksAttacks blocksAttacks = (BlocksAttacks)blockingWith.get(DataComponents.BLOCKS_ATTACKS);
/* 1384 */     if (blocksAttacks == null || ((Boolean)blocksAttacks.bypassedBy().map(t -> Boolean.valueOf(t.contains(source.typeHolder()))).orElse(Boolean.valueOf(false))).booleanValue())
/* 1385 */       return 0.0F; 
/* 1387 */     Entity entity = source.getDirectEntity();
/* 1387 */     if (entity instanceof AbstractArrow) {
/* 1387 */       AbstractArrow abstractArrow = (AbstractArrow)entity;
/* 1387 */       if (abstractArrow.getPierceLevel() > 0)
/* 1388 */         return 0.0F; 
/*      */     } 
/* 1392 */     Vec3 sourcePosition = source.getSourcePosition();
/* 1393 */     if (sourcePosition != null) {
/* 1394 */       Vec3 viewVector = calculateViewVector(0.0F, getYHeadRot());
/* 1395 */       Vec3 vectorTo = sourcePosition.subtract(position());
/* 1396 */       vectorTo = (new Vec3(vectorTo.x, 0.0D, vectorTo.z)).normalize();
/* 1400 */       angle = Math.acos(vectorTo.dot(viewVector));
/*      */     } else {
/* 1403 */       angle = 3.1415927410125732D;
/*      */     } 
/* 1405 */     float damageBlocked = blocksAttacks.resolveBlockedDamage(source, damage, angle);
/* 1406 */     blocksAttacks.hurtBlockingItem(level(), blockingWith, this, getUsedItemHand(), damageBlocked);
/* 1409 */     if (damageBlocked > 0.0F && !source.is(DamageTypeTags.IS_PROJECTILE)) {
/* 1410 */       Entity directEntity = source.getDirectEntity();
/* 1411 */       if (directEntity instanceof LivingEntity) {
/* 1411 */         LivingEntity livingEntity = (LivingEntity)directEntity;
/* 1412 */         blockUsingItem(level, livingEntity, source, damage);
/*      */       } 
/*      */     } 
/* 1415 */     return damageBlocked;
/*      */   }
/*      */   
/*      */   private void playSecondaryHurtSound(DamageSource source) {
/* 1419 */     if (source.is(DamageTypes.THORNS)) {
/* 1420 */       SoundSource soundSource = (this instanceof Player) ? SoundSource.PLAYERS : SoundSource.HOSTILE;
/* 1421 */       level().playSound(null, (position()).x, (position()).y, (position()).z, SoundEvents.THORNS_HIT, soundSource);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void resolveMobResponsibleForDamage(DamageSource source) {
/* 1426 */     Entity entity = source.getEntity();
/* 1426 */     if (entity instanceof LivingEntity) {
/* 1426 */       LivingEntity livingSource = (LivingEntity)entity;
/* 1426 */       if (!source.is(DamageTypeTags.NO_ANGER) && (
/* 1427 */         !source.is(DamageTypes.WIND_CHARGE) || !is(EntityTypeTags.NO_ANGER_FROM_WIND_CHARGE)))
/* 1428 */         setLastHurtByMob(livingSource); 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Player resolvePlayerResponsibleForDamage(DamageSource source) {
/* 1433 */     Entity sourceEntity = source.getEntity();
/* 1434 */     if (sourceEntity instanceof Player) {
/* 1434 */       Player playerSource = (Player)sourceEntity;
/* 1435 */       setLastHurtByPlayer(playerSource, 100);
/* 1436 */     } else if (sourceEntity instanceof Wolf) {
/* 1436 */       Wolf wolf = (Wolf)sourceEntity;
/* 1436 */       if (wolf.isTame())
/* 1437 */         if (wolf.getOwnerReference() != null) {
/* 1438 */           setLastHurtByPlayer(wolf.getOwnerReference().getUUID(), 100);
/*      */         } else {
/* 1440 */           this.lastHurtByPlayer = null;
/* 1441 */           this.lastHurtByPlayerMemoryTime = 0;
/*      */         }  
/*      */     } 
/* 1444 */     return EntityReference.getPlayer(this.lastHurtByPlayer, level());
/*      */   }
/*      */   
/*      */   protected void blockUsingItem(ServerLevel level, LivingEntity attacker, DamageSource source, float damage) {
/* 1448 */     attacker.blockedByItem(this, source, damage);
/*      */   }
/*      */   
/*      */   protected void blockedByItem(LivingEntity defender, DamageSource source, float damage) {
/* 1452 */     defender.knockback(0.5D, defender.getX() - getX(), defender.getZ() - getZ(), source, damage);
/*      */   }
/*      */   
/*      */   private boolean checkTotemDeathProtection(DamageSource killingDamage) {
/* 1456 */     if (killingDamage.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
/* 1457 */       return false; 
/* 1460 */     ItemStack protectionItem = null;
/* 1461 */     DeathProtection protection = null;
/* 1463 */     for (InteractionHand hand : InteractionHand.values()) {
/* 1464 */       ItemStack itemStack = getItemInHand(hand);
/* 1465 */       protection = (DeathProtection)itemStack.get(DataComponents.DEATH_PROTECTION);
/* 1466 */       if (protection != null) {
/* 1467 */         protectionItem = itemStack.copy();
/* 1468 */         itemStack.shrink(1);
/*      */         break;
/*      */       } 
/*      */     } 
/* 1473 */     if (protectionItem != null) {
/* 1475 */       LivingEntity livingEntity = this;
/* 1475 */       if (livingEntity instanceof ServerPlayer) {
/* 1475 */         ServerPlayer player = (ServerPlayer)livingEntity;
/* 1476 */         player.awardStat(Stats.ITEM_USED.get(protectionItem.getItem()));
/* 1477 */         CriteriaTriggers.USED_TOTEM.trigger(player, protectionItem);
/* 1478 */         protectionItem.causeUseVibration(this, GameEvent.ITEM_INTERACT_FINISH);
/*      */       } 
/* 1480 */       setHealth(1.0F);
/* 1481 */       protection.applyEffects(protectionItem, this);
/* 1482 */       level().broadcastEntityEvent(this, (byte)35);
/*      */     } 
/* 1484 */     return (protection != null);
/*      */   }
/*      */   
/*      */   public DamageSource getLastDamageSource() {
/* 1489 */     if (level().getGameTime() - this.lastDamageStamp > 40L)
/* 1490 */       this.lastDamageSource = null; 
/* 1492 */     return this.lastDamageSource;
/*      */   }
/*      */   
/*      */   protected void playHurtSound(DamageSource source) {
/* 1496 */     makeSound(getHurtSound(source));
/*      */   }
/*      */   
/*      */   public void makeSound(SoundEvent sound) {
/* 1500 */     if (sound != null)
/* 1501 */       playSound(sound, getSoundVolume(), getVoicePitch()); 
/*      */   }
/*      */   
/*      */   private void breakItem(ItemStack itemStack) {
/* 1506 */     if (!itemStack.isEmpty()) {
/* 1507 */       Holder<SoundEvent> breakSound = (Holder<SoundEvent>)itemStack.get(DataComponents.BREAK_SOUND);
/* 1508 */       if (breakSound != null && !isSilent())
/* 1509 */         level().playLocalSound(getX(), getY(), getZ(), (SoundEvent)breakSound.value(), getSoundSource(), 0.8F, 0.8F + this.random.nextFloat() * 0.4F, false); 
/* 1511 */       spawnItemParticles(itemStack, 5);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void die(DamageSource source) {
/* 1516 */     if (isRemoved() || this.dead)
/*      */       return; 
/* 1519 */     Entity sourceEntity = source.getEntity();
/* 1520 */     LivingEntity killer = getKillCredit();
/* 1521 */     if (killer != null)
/* 1522 */       killer.awardKillScore(this, source); 
/* 1525 */     if (isSleeping())
/* 1526 */       stopSleeping(); 
/* 1528 */     stopUsingItem();
/* 1529 */     if (!level().isClientSide() && hasCustomName())
/* 1530 */       LOGGER.info("Named entity {} died: {}", this, getCombatTracker().getDeathMessage().getString()); 
/* 1533 */     handleKillingBlow();
/* 1534 */     getCombatTracker().recheckStatus();
/* 1536 */     Level level = level();
/* 1536 */     if (level instanceof ServerLevel) {
/* 1536 */       ServerLevel serverLevel = (ServerLevel)level;
/* 1537 */       if (sourceEntity == null || sourceEntity.killedEntity(serverLevel, this, source)) {
/* 1538 */         gameEvent((Holder<GameEvent>)GameEvent.ENTITY_DIE);
/* 1539 */         dropAllDeathLoot(serverLevel, source);
/* 1540 */         createWitherRose(killer);
/*      */       } 
/* 1542 */       level().broadcastEntityEvent(this, (byte)3);
/*      */     } 
/* 1544 */     setPose(Pose.DYING);
/*      */   }
/*      */   
/*      */   protected void handleKillingBlow() {
/* 1548 */     this.dead = true;
/*      */   }
/*      */   
/*      */   protected void createWitherRose(LivingEntity killer) {
/*      */     ServerLevel serverLevel;
/* 1552 */     Level level = level();
/* 1552 */     if (level instanceof ServerLevel) {
/* 1552 */       serverLevel = (ServerLevel)level;
/*      */     } else {
/*      */       return;
/*      */     } 
/* 1556 */     boolean plantedWitherRose = false;
/* 1557 */     if (killer instanceof net.minecraft.world.entity.boss.wither.WitherBoss) {
/* 1558 */       if (((Boolean)serverLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 1559 */         BlockPos pos = blockPosition();
/* 1560 */         BlockState state = Blocks.WITHER_ROSE.defaultBlockState();
/* 1561 */         if (level().getBlockState(pos).isAir() && state.canSurvive((LevelReader)level(), pos)) {
/* 1562 */           level().setBlock(pos, state, 3);
/* 1563 */           plantedWitherRose = true;
/*      */         } 
/*      */       } 
/* 1567 */       if (!plantedWitherRose) {
/* 1568 */         ItemEntity itemEntity = new ItemEntity(level(), getX(), getY(), getZ(), new ItemStack((ItemLike)Items.WITHER_ROSE));
/* 1569 */         level().addFreshEntity((Entity)itemEntity);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void dropAllDeathLoot(ServerLevel level, DamageSource source) {
/* 1575 */     boolean playerKilled = (this.lastHurtByPlayerMemoryTime > 0);
/* 1577 */     if (shouldDropLoot(level)) {
/* 1578 */       dropFromLootTable(level, source, playerKilled);
/* 1579 */       dropCustomDeathLoot(level, source, playerKilled);
/*      */     } 
/* 1581 */     dropEquipment(level);
/* 1582 */     dropExperience(level, source.getEntity());
/*      */   }
/*      */   
/*      */   protected void dropEquipment(ServerLevel level) {}
/*      */   
/*      */   protected void dropExperience(ServerLevel level, Entity killer) {
/* 1589 */     if (!wasExperienceConsumed() && (isAlwaysExperienceDropper() || (this.lastHurtByPlayerMemoryTime > 0 && shouldDropExperience() && ((Boolean)level.getGameRules().get(GameRules.MOB_DROPS)).booleanValue())))
/* 1590 */       ExperienceOrb.award(level, position(), getExperienceReward(level, killer)); 
/*      */   }
/*      */   
/*      */   protected void dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean killedByPlayer) {}
/*      */   
/*      */   public long getLootTableSeed() {
/* 1598 */     return 0L;
/*      */   }
/*      */   
/*      */   protected float getKnockback(Entity target, DamageSource damageSource) {
/* 1602 */     float knockback = (float)getAttributeValue(Attributes.ATTACK_KNOCKBACK);
/* 1603 */     Level level = level();
/* 1603 */     if (level instanceof ServerLevel) {
/* 1603 */       ServerLevel serverLevel = (ServerLevel)level;
/* 1604 */       return EnchantmentHelper.modifyKnockback(serverLevel, getWeaponItem(), target, damageSource, knockback) / 2.0F;
/*      */     } 
/* 1606 */     return knockback / 2.0F;
/*      */   }
/*      */   
/*      */   protected void dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled) {
/* 1610 */     Optional<ResourceKey<LootTable>> lootTable = getLootTable();
/* 1611 */     if (lootTable.isEmpty())
/*      */       return; 
/* 1614 */     dropFromLootTable(level, source, playerKilled, lootTable.get());
/*      */   }
/*      */   
/*      */   public void dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled, ResourceKey<LootTable> lootTable) {
/* 1618 */     dropFromLootTable(level, source, playerKilled, lootTable, itemStack -> spawnAtLocation(level, itemStack));
/*      */   }
/*      */   
/*      */   public void dropFromLootTable(ServerLevel level, DamageSource source, boolean playerKilled, ResourceKey<LootTable> lootTable, Consumer<ItemStack> itemStackConsumer) {
/* 1622 */     LootTable table = level.getServer().reloadableRegistries().getLootTable(lootTable);
/* 1629 */     LootParams.Builder builder = (new LootParams.Builder(level)).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.DAMAGE_SOURCE, source).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity()).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity());
/* 1631 */     Player killerPlayer = getLastHurtByPlayer();
/* 1632 */     if (playerKilled && killerPlayer != null)
/* 1633 */       builder = builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, killerPlayer).withLuck(killerPlayer.getLuck()); 
/* 1635 */     LootParams params = builder.create(LootContextParamSets.ENTITY);
/* 1636 */     table.getRandomItems(params, getLootTableSeed(), itemStackConsumer);
/*      */   }
/*      */   
/*      */   public boolean dropFromEntityInteractLootTable(ServerLevel level, ResourceKey<LootTable> key, Entity interactingEntity, ItemInstance tool, BiConsumer<ServerLevel, ItemStack> consumer) {
/* 1640 */     return dropFromLootTable(level, key, params -> params.withParameter(LootContextParams.TARGET_ENTITY, this).withOptionalParameter(LootContextParams.INTERACTING_ENTITY, interactingEntity).withParameter(LootContextParams.TOOL, tool).create(LootContextParamSets.ENTITY_INTERACT), consumer);
/*      */   }
/*      */   
/*      */   public boolean dropFromGiftLootTable(ServerLevel level, ResourceKey<LootTable> key, BiConsumer<ServerLevel, ItemStack> consumer) {
/* 1653 */     return dropFromLootTable(level, key, params -> params.withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.THIS_ENTITY, this).create(LootContextParamSets.GIFT), consumer);
/*      */   }
/*      */   
/*      */   protected void dropFromShearingLootTable(ServerLevel level, ResourceKey<LootTable> key, ItemInstance tool, BiConsumer<ServerLevel, ItemStack> consumer) {
/* 1665 */     dropFromLootTable(level, key, params -> params.withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.THIS_ENTITY, this).withParameter(LootContextParams.TOOL, tool).create(LootContextParamSets.SHEARING), consumer);
/*      */   }
/*      */   
/*      */   protected boolean dropFromLootTable(ServerLevel level, ResourceKey<LootTable> key, Function<LootParams.Builder, LootParams> paramsBuilder, BiConsumer<ServerLevel, ItemStack> consumer) {
/* 1678 */     LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(key);
/* 1679 */     LootParams params = paramsBuilder.apply(new LootParams.Builder(level));
/* 1680 */     ObjectArrayList objectArrayList = lootTable.getRandomItems(params);
/* 1681 */     if (!objectArrayList.isEmpty()) {
/* 1682 */       objectArrayList.forEach(stack -> consumer.accept(level, stack));
/* 1683 */       return true;
/*      */     } 
/* 1685 */     return false;
/*      */   }
/*      */   
/*      */   public void knockback(double power, double xd, double zd, DamageSource source, float damage, boolean comesFromEffect) {
/* 1689 */     power *= 1.0D - getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
/* 1690 */     if (power <= 0.0D)
/*      */       return; 
/* 1694 */     this.needsSync = true;
/* 1696 */     Vec3 deltaMovement = getDeltaMovement();
/* 1697 */     while (xd * xd + zd * zd < 9.999999747378752E-6D) {
/* 1698 */       xd = (this.random.nextDouble() - this.random.nextDouble()) * 0.01D;
/* 1699 */       zd = (this.random.nextDouble() - this.random.nextDouble()) * 0.01D;
/*      */     } 
/* 1702 */     Vec3 deltaVector = (new Vec3(xd, 0.0D, zd)).normalize().scale(power);
/* 1704 */     setDeltaMovement(deltaMovement.x / 2.0D - deltaVector.x, 
/*      */         
/* 1706 */         onGround() ? Math.min(0.4D, deltaMovement.y / 2.0D + power) : deltaMovement.y, deltaMovement.z / 2.0D - deltaVector.z);
/*      */   }
/*      */   
/*      */   public void knockback(double power, double xd, double zd, DamageSource source, float damage) {
/* 1712 */     knockback(power, xd, zd, source, damage, false);
/*      */   }
/*      */   
/*      */   public void indicateDamage(double xd, double zd) {}
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource source) {
/* 1719 */     return SoundEvents.GENERIC_HURT;
/*      */   }
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/* 1723 */     return SoundEvents.GENERIC_DEATH;
/*      */   }
/*      */   
/*      */   private SoundEvent getFallDamageSound(int dmg) {
/* 1727 */     return (dmg > 4) ? getFallSounds().big() : getFallSounds().small();
/*      */   }
/*      */   
/*      */   public void skipDropExperience() {
/* 1731 */     this.skipDropExperience = true;
/*      */   }
/*      */   
/*      */   public boolean wasExperienceConsumed() {
/* 1735 */     return this.skipDropExperience;
/*      */   }
/*      */   
/*      */   public float getHurtDir() {
/* 1740 */     return 0.0F;
/*      */   }
/*      */   
/*      */   protected AABB getHitbox() {
/* 1744 */     AABB aabb = getBoundingBox();
/* 1745 */     Entity vehicle = getVehicle();
/* 1746 */     if (vehicle != null) {
/* 1747 */       Vec3 pos = vehicle.getPassengerRidingPosition(this);
/* 1748 */       return aabb.setMinY(Math.max(pos.y, aabb.minY));
/*      */     } 
/* 1750 */     return aabb;
/*      */   }
/*      */   
/*      */   public Map<Enchantment, Set<EnchantmentLocationBasedEffect>> activeLocationDependentEnchantments(EquipmentSlot slot) {
/* 1754 */     return (Map<Enchantment, Set<EnchantmentLocationBasedEffect>>)this.activeLocationDependentEnchantments.computeIfAbsent(slot, s -> new Reference2ObjectArrayMap());
/*      */   }
/*      */   
/*      */   public void postPiercingAttack() {
/* 1758 */     Level level = level();
/* 1758 */     if (level instanceof ServerLevel) {
/* 1758 */       ServerLevel serverLevel = (ServerLevel)level;
/* 1759 */       EnchantmentHelper.doPostPiercingAttackEffects(serverLevel, this);
/*      */     } 
/*      */   }
/*      */   
/*      */   public static final class Fallsounds extends Record {
/*      */     private final SoundEvent small;
/*      */     
/*      */     private final SoundEvent big;
/*      */     
/*      */     public Fallsounds(SoundEvent small, SoundEvent big) {
/* 1763 */       this.small = small;
/* 1763 */       this.big = big;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/LivingEntity$Fallsounds;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1763	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/LivingEntity$Fallsounds;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/LivingEntity$Fallsounds;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1763	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/LivingEntity$Fallsounds;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/LivingEntity$Fallsounds;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1763	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/entity/LivingEntity$Fallsounds;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public SoundEvent small() {
/* 1763 */       return this.small;
/*      */     }
/*      */     
/*      */     public SoundEvent big() {
/* 1763 */       return this.big;
/*      */     }
/*      */   }
/*      */   
/*      */   public Fallsounds getFallSounds() {
/* 1767 */     return new Fallsounds(SoundEvents.GENERIC_SMALL_FALL, SoundEvents.GENERIC_BIG_FALL);
/*      */   }
/*      */   
/*      */   public Optional<BlockPos> getLastClimbablePos() {
/* 1771 */     return this.lastClimbablePos;
/*      */   }
/*      */   
/*      */   public boolean onClimbable() {
/* 1775 */     if (isSpectator())
/* 1776 */       return false; 
/* 1779 */     BlockPos ladderCheckPos = blockPosition();
/* 1781 */     BlockState state = getInBlockState();
/* 1783 */     if (isFallFlying() && state.is(BlockTags.CAN_GLIDE_THROUGH))
/* 1784 */       return false; 
/* 1787 */     if (state.is(BlockTags.CLIMBABLE)) {
/* 1788 */       this.lastClimbablePos = Optional.of(ladderCheckPos);
/* 1789 */       return true;
/*      */     } 
/* 1792 */     if (state.getBlock() instanceof TrapDoorBlock && trapdoorUsableAsLadder(ladderCheckPos, state)) {
/* 1793 */       this.lastClimbablePos = Optional.of(ladderCheckPos);
/* 1794 */       return true;
/*      */     } 
/* 1796 */     return false;
/*      */   }
/*      */   
/*      */   private boolean trapdoorUsableAsLadder(BlockPos pos, BlockState state) {
/* 1800 */     if (((Boolean)state.getValue((Property)TrapDoorBlock.OPEN)).booleanValue()) {
/* 1801 */       BlockState belowState = level().getBlockState(pos.below());
/* 1802 */       return (belowState.is(Blocks.LADDER) && belowState.getValue((Property)LadderBlock.FACING) == state.getValue((Property)TrapDoorBlock.FACING));
/*      */     } 
/* 1804 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isAlive() {
/* 1810 */     return (!isRemoved() && getHealth() > 0.0F);
/*      */   }
/*      */   
/*      */   public boolean isLookingAtMe(LivingEntity target, double coneSize, boolean adjustForDistance, boolean seeThroughTransparentBlocks, double... gazeHeights) {
/* 1814 */     Vec3 look = target.getViewVector(1.0F).normalize();
/* 1815 */     for (double gazeHeight : gazeHeights) {
/* 1816 */       Vec3 dir = new Vec3(getX() - target.getX(), gazeHeight - target.getEyeY(), getZ() - target.getZ());
/* 1817 */       double dist = dir.length();
/* 1818 */       dir = dir.normalize();
/* 1819 */       double dot = look.dot(dir);
/* 1820 */       if (dot > 1.0D - coneSize / (adjustForDistance ? dist : 1.0D) && 
/* 1821 */         target.hasLineOfSight(this, seeThroughTransparentBlocks ? ClipContext.Block.VISUAL : ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, gazeHeight))
/* 1822 */         return true; 
/*      */     } 
/* 1826 */     return false;
/*      */   }
/*      */   
/*      */   public int getMaxFallDistance() {
/* 1831 */     return getComfortableFallDistance(0.0F);
/*      */   }
/*      */   
/*      */   protected final int getComfortableFallDistance(float allowedDamage) {
/* 1836 */     return Mth.floor(allowedDamage + 3.0F);
/*      */   }
/*      */   
/*      */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/*      */     double effectiveFallDistance;
/* 1842 */     if (isIgnoringFallDamageFromCurrentImpulse()) {
/* 1843 */       effectiveFallDistance = Math.min(fallDistance, this.currentImpulseImpactPos.y - getY());
/* 1844 */       boolean hasLandedAboveCurrentImpulseImpactPosY = (effectiveFallDistance <= 0.0D);
/* 1845 */       if (hasLandedAboveCurrentImpulseImpactPosY) {
/* 1846 */         resetCurrentImpulseContext();
/*      */       } else {
/* 1848 */         tryResetCurrentImpulseContext();
/*      */       } 
/*      */     } else {
/* 1851 */       effectiveFallDistance = fallDistance;
/*      */     } 
/* 1854 */     boolean damaged = super.causeFallDamage(effectiveFallDistance, damageModifier, damageSource);
/* 1855 */     int dmg = calculateFallDamage(effectiveFallDistance, damageModifier);
/* 1857 */     if (dmg > 0) {
/* 1858 */       resetCurrentImpulseContext();
/* 1859 */       playSound(getFallDamageSound(dmg), 1.0F, 1.0F);
/* 1860 */       playBlockFallSound();
/* 1861 */       hurt(damageSource, dmg);
/* 1862 */       return true;
/*      */     } 
/* 1864 */     return damaged;
/*      */   }
/*      */   
/*      */   public void setIgnoreFallDamageFromCurrentImpulse(boolean ignoreFallDamage, Vec3 newImpulseImpactPos) {
/* 1868 */     if (ignoreFallDamage) {
/* 1869 */       applyPostImpulseGraceTime(40);
/* 1870 */       this.currentImpulseImpactPos = newImpulseImpactPos;
/*      */     } else {
/* 1872 */       this.currentImpulseContextResetGraceTime = 0;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void applyPostImpulseGraceTime(int ticks) {
/* 1877 */     this.currentImpulseContextResetGraceTime = Math.max(this.currentImpulseContextResetGraceTime, ticks);
/*      */   }
/*      */   
/*      */   public boolean isIgnoringFallDamageFromCurrentImpulse() {
/* 1881 */     return (this.currentImpulseImpactPos != null);
/*      */   }
/*      */   
/*      */   public void tryResetCurrentImpulseContext() {
/* 1885 */     if (this.currentImpulseContextResetGraceTime == 0)
/* 1886 */       resetCurrentImpulseContext(); 
/*      */   }
/*      */   
/*      */   public boolean isInPostImpulseGraceTime() {
/* 1891 */     return (this.currentImpulseContextResetGraceTime > 0);
/*      */   }
/*      */   
/*      */   public void resetCurrentImpulseContext() {
/* 1895 */     this.currentImpulseContextResetGraceTime = 0;
/* 1896 */     this.currentImpulseImpactPos = null;
/*      */   }
/*      */   
/*      */   protected int calculateFallDamage(double fallDistance, float damageModifier) {
/* 1900 */     if (is(EntityTypeTags.FALL_DAMAGE_IMMUNE))
/* 1901 */       return 0; 
/* 1903 */     double baseDamage = calculateFallPower(fallDistance);
/* 1904 */     return Mth.floor(baseDamage * damageModifier * getAttributeValue(Attributes.FALL_DAMAGE_MULTIPLIER));
/*      */   }
/*      */   
/*      */   private double calculateFallPower(double fallDistance) {
/* 1908 */     return fallDistance + 1.0E-6D - getAttributeValue(Attributes.SAFE_FALL_DISTANCE);
/*      */   }
/*      */   
/*      */   protected void playBlockFallSound() {
/* 1912 */     if (isSilent())
/*      */       return; 
/* 1915 */     int xx = Mth.floor(getX());
/* 1916 */     int yy = Mth.floor(getY() - 0.20000000298023224D);
/* 1917 */     int zz = Mth.floor(getZ());
/* 1919 */     BlockState state = level().getBlockState(new BlockPos(xx, yy, zz));
/* 1920 */     if (!state.isAir()) {
/* 1921 */       SoundType soundType = state.getSoundType();
/* 1922 */       playSound(soundType.getFallSound(), soundType.getVolume() * 0.5F, soundType.getPitch() * 0.75F);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void animateHurt(float yaw) {
/* 1928 */     this.hurtDuration = 10;
/* 1929 */     this.hurtTime = this.hurtDuration;
/*      */   }
/*      */   
/*      */   public int getArmorValue() {
/* 1936 */     return Mth.floor(getAttributeValue(Attributes.ARMOR));
/*      */   }
/*      */   
/*      */   protected void hurtArmor(DamageSource damageSource, float damage) {}
/*      */   
/*      */   protected void hurtHelmet(DamageSource damageSource, float damage) {}
/*      */   
/*      */   protected void doHurtEquipment(DamageSource damageSource, float damage, EquipmentSlot... slots) {
/* 1946 */     if (damage <= 0.0F)
/*      */       return; 
/* 1949 */     int durabilityDamage = (int)Math.max(1.0F, damage / 4.0F);
/* 1951 */     for (EquipmentSlot slot : slots) {
/* 1952 */       ItemStack itemStack = getItemBySlot(slot);
/* 1953 */       Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 1954 */       if (equippable != null && equippable.damageOnHurt() && itemStack.isDamageableItem() && itemStack.canBeHurtBy(damageSource))
/* 1955 */         itemStack.hurtAndBreak(durabilityDamage, this, slot); 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected float getDamageAfterArmorAbsorb(DamageSource damageSource, float damage) {
/* 1961 */     if (!damageSource.is(DamageTypeTags.BYPASSES_ARMOR)) {
/* 1962 */       hurtArmor(damageSource, damage);
/* 1963 */       damage = CombatRules.getDamageAfterAbsorb(this, damage, damageSource, getArmorValue(), (float)getAttributeValue(Attributes.ARMOR_TOUGHNESS));
/*      */     } 
/* 1965 */     return damage;
/*      */   }
/*      */   
/*      */   protected float getDamageAfterMagicAbsorb(DamageSource damageSource, float damage) {
/*      */     float enchantmentArmor;
/* 1969 */     if (damageSource.is(DamageTypeTags.BYPASSES_EFFECTS))
/* 1970 */       return damage; 
/* 1973 */     if (hasEffect(MobEffects.RESISTANCE) && !damageSource.is(DamageTypeTags.BYPASSES_RESISTANCE)) {
/* 1974 */       int absorbValue = (getEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
/* 1975 */       int absorb = 25 - absorbValue;
/* 1976 */       float v = damage * absorb;
/* 1977 */       float oldDamage = damage;
/* 1978 */       damage = Math.max(v / 25.0F, 0.0F);
/* 1980 */       float damageResisted = oldDamage - damage;
/* 1981 */       if (damageResisted > 0.0F && damageResisted < 3.4028235E37F) {
/* 1982 */         LivingEntity livingEntity = this;
/* 1982 */         if (livingEntity instanceof ServerPlayer) {
/* 1982 */           ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
/* 1983 */           serverPlayer.awardStat(Stats.DAMAGE_RESISTED, Math.round(damageResisted * 10.0F));
/* 1984 */         } else if (damageSource.getEntity() instanceof ServerPlayer) {
/* 1985 */           ((ServerPlayer)damageSource.getEntity()).awardStat(Stats.DAMAGE_DEALT_RESISTED, Math.round(damageResisted * 10.0F));
/*      */         } 
/*      */       } 
/*      */     } 
/* 1990 */     if (damage <= 0.0F)
/* 1991 */       return 0.0F; 
/* 1994 */     if (damageSource.is(DamageTypeTags.BYPASSES_ENCHANTMENTS))
/* 1995 */       return damage; 
/* 1999 */     Level level = level();
/* 1999 */     if (level instanceof ServerLevel) {
/* 1999 */       ServerLevel serverLevel = (ServerLevel)level;
/* 2000 */       enchantmentArmor = EnchantmentHelper.getDamageProtection(serverLevel, this, damageSource);
/*      */     } else {
/* 2002 */       enchantmentArmor = 0.0F;
/*      */     } 
/* 2004 */     if (enchantmentArmor > 0.0F)
/* 2005 */       damage = CombatRules.getDamageAfterMagicAbsorb(damage, enchantmentArmor); 
/* 2008 */     return damage;
/*      */   }
/*      */   
/*      */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/* 2012 */     if (isInvulnerableTo(level, source))
/*      */       return; 
/* 2015 */     dmg = getDamageAfterArmorAbsorb(source, dmg);
/* 2016 */     dmg = getDamageAfterMagicAbsorb(source, dmg);
/* 2018 */     float originalDamage = dmg;
/* 2019 */     dmg = Math.max(dmg - getAbsorptionAmount(), 0.0F);
/* 2020 */     setAbsorptionAmount(getAbsorptionAmount() - originalDamage - dmg);
/* 2022 */     float absorbedDamage = originalDamage - dmg;
/* 2023 */     if (absorbedDamage > 0.0F && absorbedDamage < 3.4028235E37F) {
/* 2023 */       Entity entity = source.getEntity();
/* 2023 */       if (entity instanceof ServerPlayer) {
/* 2023 */         ServerPlayer serverPlayer = (ServerPlayer)entity;
/* 2024 */         serverPlayer.awardStat(Stats.DAMAGE_DEALT_ABSORBED, Math.round(absorbedDamage * 10.0F));
/*      */       } 
/*      */     } 
/* 2027 */     if (dmg == 0.0F)
/*      */       return; 
/* 2031 */     getCombatTracker().recordDamage(source, dmg);
/* 2032 */     setHealth(getHealth() - dmg);
/* 2033 */     setAbsorptionAmount(getAbsorptionAmount() - dmg);
/* 2034 */     gameEvent((Holder<GameEvent>)GameEvent.ENTITY_DAMAGE);
/*      */   }
/*      */   
/*      */   public CombatTracker getCombatTracker() {
/* 2038 */     return this.combatTracker;
/*      */   }
/*      */   
/*      */   public LivingEntity getKillCredit() {
/* 2042 */     if (this.lastHurtByPlayer != null)
/* 2043 */       return (LivingEntity)this.lastHurtByPlayer.getEntity(level(), Player.class); 
/* 2045 */     if (this.lastHurtByMob != null)
/* 2046 */       return this.lastHurtByMob.getEntity(level(), LivingEntity.class); 
/* 2048 */     return null;
/*      */   }
/*      */   
/*      */   public final float getMaxHealth() {
/* 2052 */     return (float)getAttributeValue(Attributes.MAX_HEALTH);
/*      */   }
/*      */   
/*      */   public final float getMaxAbsorption() {
/* 2056 */     return (float)getAttributeValue(Attributes.MAX_ABSORPTION);
/*      */   }
/*      */   
/*      */   public final int getArrowCount() {
/* 2060 */     return ((Integer)this.entityData.get(DATA_ARROW_COUNT_ID)).intValue();
/*      */   }
/*      */   
/*      */   public final void setArrowCount(int count) {
/* 2064 */     this.entityData.set(DATA_ARROW_COUNT_ID, Integer.valueOf(count));
/*      */   }
/*      */   
/*      */   public final int getStingerCount() {
/* 2068 */     return ((Integer)this.entityData.get(DATA_STINGER_COUNT_ID)).intValue();
/*      */   }
/*      */   
/*      */   public final void setStingerCount(int count) {
/* 2072 */     this.entityData.set(DATA_STINGER_COUNT_ID, Integer.valueOf(count));
/*      */   }
/*      */   
/*      */   private int getCurrentSwingDuration() {
/* 2076 */     InteractionHand hand = (this.swingingArm != null) ? this.swingingArm : InteractionHand.MAIN_HAND;
/* 2077 */     ItemStack handStack = getItemInHand(hand);
/* 2078 */     int swingDuration = handStack.getSwingAnimation().duration();
/* 2079 */     if (MobEffectUtil.hasDigSpeed(this))
/* 2080 */       return swingDuration - 1 + MobEffectUtil.getDigSpeedAmplification(this); 
/* 2082 */     if (hasEffect(MobEffects.MINING_FATIGUE))
/* 2083 */       return swingDuration + (1 + getEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2; 
/* 2085 */     return swingDuration;
/*      */   }
/*      */   
/*      */   public void swing(InteractionHand hand) {
/* 2089 */     swing(hand, false);
/*      */   }
/*      */   
/*      */   public void swing(InteractionHand hand, boolean sendToSwingingEntity) {
/* 2093 */     if (!this.swinging || this.swingTime >= getCurrentSwingDuration() / 2 || this.swingTime < 0) {
/* 2094 */       this.swingTime = -1;
/* 2095 */       this.swinging = true;
/* 2096 */       this.swingingArm = hand;
/* 2098 */       if (level() instanceof ServerLevel) {
/* 2099 */         ClientboundAnimatePacket packet = new ClientboundAnimatePacket(this, (hand == InteractionHand.MAIN_HAND) ? 0 : 3);
/* 2100 */         ServerChunkCache chunkSource = ((ServerLevel)level()).getChunkSource();
/* 2102 */         if (sendToSwingingEntity) {
/* 2103 */           chunkSource.sendToTrackingPlayersAndSelf(this, (Packet)packet);
/*      */         } else {
/* 2105 */           chunkSource.sendToTrackingPlayers(this, (Packet)packet);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleDamageEvent(DamageSource source) {
/* 2113 */     this.walkAnimation.setSpeed(1.5F);
/* 2115 */     this.invulnerableTime = 20;
/* 2116 */     this.hurtDuration = 10;
/* 2117 */     this.hurtTime = this.hurtDuration;
/* 2119 */     SoundEvent hurtSound = getHurtSound(source);
/* 2120 */     if (hurtSound != null)
/* 2121 */       playSound(hurtSound, getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F); 
/* 2123 */     this.lastDamageSource = source;
/* 2124 */     this.lastDamageStamp = level().getGameTime();
/*      */   }
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/*      */     SoundEvent deathSound;
/*      */     int count, i;
/* 2129 */     switch (id) {
/*      */       case 3:
/* 2131 */         deathSound = getDeathSound();
/* 2132 */         if (deathSound != null)
/* 2133 */           playSound(deathSound, getSoundVolume(), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F); 
/* 2136 */         if (!(this instanceof Player)) {
/* 2137 */           setHealth(0.0F);
/* 2138 */           die(damageSources().generic());
/*      */         } 
/*      */         return;
/*      */       case 46:
/* 2142 */         count = 128;
/* 2144 */         for (i = 0; i < 128; i++) {
/* 2145 */           double d = i / 127.0D;
/* 2146 */           float xa = (this.random.nextFloat() - 0.5F) * 0.2F;
/* 2147 */           float ya = (this.random.nextFloat() - 0.5F) * 0.2F;
/* 2148 */           float za = (this.random.nextFloat() - 0.5F) * 0.2F;
/* 2150 */           double x = Mth.lerp(d, this.xo, getX()) + (this.random.nextDouble() - 0.5D) * getBbWidth() * 2.0D;
/* 2151 */           double y = Mth.lerp(d, this.yo, getY()) + this.random.nextDouble() * getBbHeight();
/* 2152 */           double z = Mth.lerp(d, this.zo, getZ()) + (this.random.nextDouble() - 0.5D) * getBbWidth() * 2.0D;
/* 2153 */           level().addParticle((ParticleOptions)ParticleTypes.PORTAL, x, y, z, xa, ya, za);
/*      */         } 
/*      */         return;
/*      */       case 47:
/* 2156 */         breakItem(getItemBySlot(EquipmentSlot.MAINHAND));
/*      */         return;
/*      */       case 48:
/* 2157 */         breakItem(getItemBySlot(EquipmentSlot.OFFHAND));
/*      */         return;
/*      */       case 49:
/* 2158 */         breakItem(getItemBySlot(EquipmentSlot.HEAD));
/*      */         return;
/*      */       case 50:
/* 2159 */         breakItem(getItemBySlot(EquipmentSlot.CHEST));
/*      */         return;
/*      */       case 51:
/* 2160 */         breakItem(getItemBySlot(EquipmentSlot.LEGS));
/*      */         return;
/*      */       case 52:
/* 2161 */         breakItem(getItemBySlot(EquipmentSlot.FEET));
/*      */         return;
/*      */       case 65:
/* 2162 */         breakItem(getItemBySlot(EquipmentSlot.BODY));
/*      */         return;
/*      */       case 68:
/* 2163 */         breakItem(getItemBySlot(EquipmentSlot.SADDLE));
/*      */         return;
/*      */       case 54:
/* 2164 */         HoneyBlock.showJumpParticles(this);
/*      */         return;
/*      */       case 55:
/* 2165 */         swapHandItems();
/*      */         return;
/*      */       case 60:
/* 2166 */         makePoofParticles();
/*      */         return;
/*      */       case 67:
/* 2167 */         makeDrownParticles();
/*      */         return;
/*      */       case 2:
/* 2168 */         onKineticHit();
/*      */         return;
/*      */     } 
/* 2169 */     super.handleEntityEvent(id);
/*      */   }
/*      */   
/*      */   public float getTicksSinceLastKineticHitFeedback(float partial) {
/* 2174 */     if (this.lastKineticHitFeedbackTime < 0L)
/* 2175 */       return 0.0F; 
/* 2177 */     return (float)(level().getGameTime() - this.lastKineticHitFeedbackTime) + partial;
/*      */   }
/*      */   
/*      */   public void makePoofParticles() {
/* 2181 */     for (int i = 0; i < 20; i++) {
/* 2182 */       double xa = this.random.nextGaussian() * 0.02D;
/* 2183 */       double ya = this.random.nextGaussian() * 0.02D;
/* 2184 */       double za = this.random.nextGaussian() * 0.02D;
/* 2185 */       double dd = 10.0D;
/* 2186 */       level().addParticle((ParticleOptions)ParticleTypes.POOF, getRandomX(1.0D) - xa * 10.0D, getRandomY() - ya * 10.0D, getRandomZ(1.0D) - za * 10.0D, xa, ya, za);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void makeDrownParticles() {
/* 2191 */     Vec3 movement = getDeltaMovement();
/* 2192 */     for (int i = 0; i < 8; i++) {
/* 2193 */       double offsetX = this.random.triangle(0.0D, 1.0D);
/* 2194 */       double offsetY = this.random.triangle(0.0D, 1.0D);
/* 2195 */       double offsetZ = this.random.triangle(0.0D, 1.0D);
/* 2196 */       level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, getX() + offsetX, getY() + offsetY, getZ() + offsetZ, movement.x, movement.y, movement.z);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void onKineticHit() {
/* 2202 */     if (level().getGameTime() - this.lastKineticHitFeedbackTime <= 10L)
/*      */       return; 
/* 2206 */     this.lastKineticHitFeedbackTime = level().getGameTime();
/* 2208 */     KineticWeapon kineticWeapon = (KineticWeapon)this.useItem.get(DataComponents.KINETIC_WEAPON);
/* 2209 */     if (kineticWeapon == null)
/*      */       return; 
/* 2213 */     kineticWeapon.makeLocalHitSound(this);
/*      */   }
/*      */   
/*      */   private void swapHandItems() {
/* 2217 */     ItemStack tmp = getItemBySlot(EquipmentSlot.OFFHAND);
/* 2218 */     setItemSlot(EquipmentSlot.OFFHAND, getItemBySlot(EquipmentSlot.MAINHAND));
/* 2219 */     setItemSlot(EquipmentSlot.MAINHAND, tmp);
/*      */   }
/*      */   
/*      */   protected void onBelowWorld() {
/* 2224 */     hurt(damageSources().fellOutOfWorld(), 4.0F);
/*      */   }
/*      */   
/*      */   protected void updateSwingTime() {
/* 2228 */     int currentSwingDuration = getCurrentSwingDuration();
/* 2229 */     if (this.swinging) {
/* 2230 */       this.swingTime++;
/* 2231 */       if (this.swingTime >= currentSwingDuration) {
/* 2232 */         this.swingTime = 0;
/* 2233 */         this.swinging = false;
/*      */       } 
/*      */     } else {
/* 2236 */       this.swingTime = 0;
/*      */     } 
/* 2239 */     this.attackAnim = this.swingTime / currentSwingDuration;
/*      */   }
/*      */   
/*      */   protected double getEntityBounciness() {
/* 2244 */     return getAttributeValue(Attributes.BOUNCINESS);
/*      */   }
/*      */   
/*      */   public AttributeInstance getAttribute(Holder<Attribute> attribute) {
/* 2248 */     return getAttributes().getInstance(attribute);
/*      */   }
/*      */   
/*      */   public double getAttributeValue(Holder<Attribute> attribute) {
/* 2252 */     return getAttributes().getValue(attribute);
/*      */   }
/*      */   
/*      */   public double getAttributeBaseValue(Holder<Attribute> attribute) {
/* 2256 */     return getAttributes().getBaseValue(attribute);
/*      */   }
/*      */   
/*      */   public AttributeMap getAttributes() {
/* 2260 */     return this.attributes;
/*      */   }
/*      */   
/*      */   public ItemStack getMainHandItem() {
/* 2264 */     return getItemBySlot(EquipmentSlot.MAINHAND);
/*      */   }
/*      */   
/*      */   public ItemStack getOffhandItem() {
/* 2268 */     return getItemBySlot(EquipmentSlot.OFFHAND);
/*      */   }
/*      */   
/*      */   public ItemStack getItemHeldByArm(HumanoidArm arm) {
/* 2272 */     return (getMainArm() == arm) ? getMainHandItem() : getOffhandItem();
/*      */   }
/*      */   
/*      */   public ItemStack getWeaponItem() {
/* 2277 */     return getMainHandItem();
/*      */   }
/*      */   
/*      */   public AttackRange getAttackRangeWith(ItemStack weaponItem) {
/* 2281 */     AttackRange attackRange = (AttackRange)weaponItem.get(DataComponents.ATTACK_RANGE);
/* 2282 */     return (attackRange != null) ? attackRange : AttackRange.defaultFor(this);
/*      */   }
/*      */   
/*      */   public ItemStack getActiveItem() {
/* 2286 */     if (isSpectator())
/* 2287 */       return ItemStack.EMPTY; 
/* 2289 */     if (isUsingItem())
/* 2290 */       return getUseItem(); 
/* 2292 */     return getMainHandItem();
/*      */   }
/*      */   
/*      */   public boolean isHolding(Item item) {
/* 2296 */     return isHolding(heldItem -> heldItem.is(item));
/*      */   }
/*      */   
/*      */   public boolean isHolding(Predicate<ItemStack> itemPredicate) {
/* 2300 */     return (itemPredicate.test(getMainHandItem()) || itemPredicate.test(getOffhandItem()));
/*      */   }
/*      */   
/*      */   public ItemStack getItemInHand(InteractionHand hand) {
/* 2304 */     if (hand == InteractionHand.MAIN_HAND)
/* 2305 */       return getItemBySlot(EquipmentSlot.MAINHAND); 
/* 2306 */     if (hand == InteractionHand.OFF_HAND)
/* 2307 */       return getItemBySlot(EquipmentSlot.OFFHAND); 
/* 2309 */     throw new IllegalArgumentException("Invalid hand " + String.valueOf(hand));
/*      */   }
/*      */   
/*      */   public void setItemInHand(InteractionHand hand, ItemStack itemStack) {
/* 2314 */     if (hand == InteractionHand.MAIN_HAND) {
/* 2315 */       setItemSlot(EquipmentSlot.MAINHAND, itemStack);
/* 2316 */     } else if (hand == InteractionHand.OFF_HAND) {
/* 2317 */       setItemSlot(EquipmentSlot.OFFHAND, itemStack);
/*      */     } else {
/* 2319 */       throw new IllegalArgumentException("Invalid hand " + String.valueOf(hand));
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean hasItemInSlot(EquipmentSlot slot) {
/* 2324 */     return !getItemBySlot(slot).isEmpty();
/*      */   }
/*      */   
/*      */   public boolean canUseSlot(EquipmentSlot slot) {
/* 2328 */     return true;
/*      */   }
/*      */   
/*      */   public ItemStack getItemBySlot(EquipmentSlot slot) {
/* 2332 */     return this.equipment.get(slot);
/*      */   }
/*      */   
/*      */   public void setItemSlot(EquipmentSlot slot, ItemStack itemStack) {
/* 2336 */     onEquipItem(slot, this.equipment.set(slot, itemStack), itemStack);
/*      */   }
/*      */   
/*      */   public float getArmorCoverPercentage() {
/* 2340 */     int total = 0;
/* 2341 */     int count = 0;
/* 2342 */     for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
/* 2344 */       if (slot.getType() != EquipmentSlot.Type.HUMANOID_ARMOR)
/*      */         continue; 
/* 2347 */       ItemStack itemStack = getItemBySlot(slot);
/* 2348 */       if (!itemStack.isEmpty())
/* 2349 */         count++; 
/* 2351 */       total++;
/*      */     } 
/* 2353 */     return (total > 0) ? (count / total) : 0.0F;
/*      */   }
/*      */   
/*      */   public void setSprinting(boolean isSprinting) {
/* 2358 */     super.setSprinting(isSprinting);
/* 2360 */     AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/* 2361 */     speed.removeModifier(SPEED_MODIFIER_SPRINTING.id());
/* 2362 */     if (isSprinting)
/* 2363 */       speed.addTransientModifier(SPEED_MODIFIER_SPRINTING); 
/*      */   }
/*      */   
/*      */   protected float getSoundVolume() {
/* 2368 */     return 1.0F;
/*      */   }
/*      */   
/*      */   public float getVoicePitch() {
/* 2372 */     if (isBaby())
/* 2373 */       return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F; 
/* 2375 */     return (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F;
/*      */   }
/*      */   
/*      */   protected boolean isImmobile() {
/* 2379 */     return isDeadOrDying();
/*      */   }
/*      */   
/*      */   public void push(Entity entity) {
/* 2384 */     if (!isSleeping())
/* 2385 */       super.push(entity); 
/*      */   }
/*      */   
/*      */   private void dismountVehicle(Entity vehicle) {
/*      */     Vec3 teleportTarget;
/* 2391 */     if (isRemoved()) {
/* 2392 */       teleportTarget = position();
/* 2393 */     } else if (vehicle.isRemoved() || level().getBlockState(vehicle.blockPosition()).is(BlockTags.PORTALS)) {
/* 2396 */       double maxY = Math.max(getY(), vehicle.getY());
/* 2397 */       teleportTarget = new Vec3(getX(), maxY, getZ());
/* 2399 */       boolean isSmall = (getBbWidth() <= 4.0F && getBbHeight() <= 4.0F);
/* 2400 */       if (isSmall) {
/* 2401 */         double halfHeight = getBbHeight() / 2.0D;
/* 2402 */         Vec3 center = teleportTarget.add(0.0D, halfHeight, 0.0D);
/* 2403 */         VoxelShape allowedCenters = Shapes.create(AABB.ofSize(center, getBbWidth(), getBbHeight(), getBbWidth()));
/* 2407 */         teleportTarget = level().findFreePosition(this, allowedCenters, center, getBbWidth(), getBbHeight(), getBbWidth()).map(pos -> pos.add(0.0D, -halfHeight, 0.0D)).orElse(teleportTarget);
/*      */       } 
/*      */     } else {
/* 2410 */       teleportTarget = vehicle.getDismountLocationForPassenger(this);
/*      */     } 
/* 2413 */     dismountTo(teleportTarget.x, teleportTarget.y, teleportTarget.z);
/*      */   }
/*      */   
/*      */   public boolean shouldShowName() {
/* 2418 */     return isCustomNameVisible();
/*      */   }
/*      */   
/*      */   protected float getJumpPower() {
/* 2422 */     return getJumpPower(1.0F);
/*      */   }
/*      */   
/*      */   protected float getJumpPower(float multiplier) {
/* 2427 */     return (float)getAttributeValue(Attributes.JUMP_STRENGTH) * multiplier * getBlockJumpFactor() + getJumpBoostPower();
/*      */   }
/*      */   
/*      */   public float getJumpBoostPower() {
/* 2431 */     return hasEffect(MobEffects.JUMP_BOOST) ? (0.1F * (getEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1.0F)) : 0.0F;
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public void jumpFromGround() {
/* 2436 */     float jumpPower = getJumpPower();
/* 2437 */     if (jumpPower <= 1.0E-5F)
/*      */       return; 
/* 2441 */     Vec3 movement = getDeltaMovement();
/* 2442 */     setDeltaMovement(movement.x, Math.max(jumpPower, movement.y), movement.z);
/* 2444 */     if (isSprinting()) {
/* 2445 */       float angle = getYRot() * 0.017453292F;
/* 2446 */       addDeltaMovement(new Vec3(
/* 2447 */             -Mth.sin(angle) * 0.2D, 0.0D, 
/*      */             
/* 2449 */             Mth.cos(angle) * 0.2D));
/*      */     } 
/* 2452 */     this.needsSync = true;
/*      */   }
/*      */   
/*      */   protected void goDownInWater() {
/* 2456 */     setDeltaMovement(getDeltaMovement().add(0.0D, -0.03999999910593033D, 0.0D));
/*      */   }
/*      */   
/*      */   protected void jumpInLiquid(TagKey<Fluid> type) {
/* 2460 */     setDeltaMovement(getDeltaMovement().add(0.0D, 0.03999999910593033D, 0.0D));
/*      */   }
/*      */   
/*      */   protected float getWaterSlowDown() {
/* 2464 */     return 0.8F;
/*      */   }
/*      */   
/*      */   public boolean canStandOnFluid(FluidState fluid) {
/* 2468 */     return false;
/*      */   }
/*      */   
/*      */   protected double getDefaultGravity() {
/* 2473 */     return getAttributeValue(Attributes.GRAVITY);
/*      */   }
/*      */   
/*      */   protected double getEffectiveGravity() {
/* 2478 */     boolean isFalling = ((getDeltaMovement()).y <= 0.0D);
/* 2479 */     if (isFalling && hasEffect(MobEffects.SLOW_FALLING))
/* 2480 */       return Math.min(getGravity(), 0.01D); 
/* 2482 */     return getGravity();
/*      */   }
/*      */   
/*      */   public void travel(Vec3 input) {
/* 2486 */     if (shouldTravelInFluid(level().getFluidState(blockPosition()))) {
/* 2487 */       travelInFluid(input);
/* 2488 */     } else if (isFallFlying()) {
/* 2489 */       travelFallFlying(input);
/*      */     } else {
/* 2491 */       travelInAir(input);
/*      */     } 
/*      */   }
/*      */   
/*      */   public VoxelShape getLiquidCollisionShape() {
/* 2496 */     return Shapes.empty();
/*      */   }
/*      */   
/*      */   protected boolean shouldTravelInFluid(FluidState fluidState) {
/* 2500 */     return ((isInWater() || isInLava()) && isAffectedByFluids() && !canStandOnFluid(fluidState));
/*      */   }
/*      */   
/*      */   protected void travelFlying(Vec3 input, float speed) {
/* 2504 */     travelFlying(input, 0.02F, 0.02F, speed);
/*      */   }
/*      */   
/*      */   protected void travelFlying(Vec3 input, float waterSpeed, float lavaSpeed, float airSpeed) {
/* 2508 */     if (isInWater()) {
/* 2509 */       moveRelative(waterSpeed, input);
/* 2510 */       move(MoverType.SELF, getDeltaMovement());
/* 2511 */       setDeltaMovement(getDeltaMovement().scale(0.800000011920929D));
/* 2512 */     } else if (isInLava()) {
/* 2513 */       moveRelative(lavaSpeed, input);
/* 2514 */       move(MoverType.SELF, getDeltaMovement());
/* 2515 */       setDeltaMovement(getDeltaMovement().scale(0.5D));
/*      */     } else {
/* 2517 */       moveRelative(airSpeed, input);
/* 2518 */       move(MoverType.SELF, getDeltaMovement());
/* 2519 */       setDeltaMovement(getDeltaMovement().scale(0.9100000262260437D));
/*      */     } 
/*      */   }
/*      */   
/*      */   private void travelInAir(Vec3 input) {
/* 2524 */     BlockPos posBelow = getBlockPosBelowThatAffectsMyMovement();
/* 2525 */     float blockFriction = onGround() ? computeModifiedFriction(level().getBlockState(posBelow).getBlock().getFriction(), (float)getAttributeValue(Attributes.FRICTION_MODIFIER)) : 1.0F;
/* 2526 */     Vec3 movement = handleRelativeFrictionAndCalculateMovement(input, blockFriction);
/* 2528 */     double movementY = movement.y;
/* 2529 */     MobEffectInstance levitationEffect = getEffect(MobEffects.LEVITATION);
/* 2530 */     if (levitationEffect != null) {
/* 2531 */       movementY += (0.05D * (levitationEffect.getAmplifier() + 1) - movement.y) * 0.2D;
/* 2532 */     } else if (!level().isClientSide() || level().hasChunkAt(posBelow)) {
/* 2533 */       movementY -= getEffectiveGravity();
/* 2534 */     } else if (getY() > level().getMinY()) {
/* 2535 */       movementY = -0.1D;
/*      */     } else {
/* 2537 */       movementY = 0.0D;
/*      */     } 
/* 2540 */     if (shouldDiscardFriction()) {
/* 2541 */       setDeltaMovement(movement.x, movementY, movement.z);
/*      */     } else {
/* 2543 */       float entityAirDragModifier = (float)getAttributeValue(Attributes.AIR_DRAG_MODIFIER);
/* 2544 */       float airDrag = computeModifiedFriction(0.91F, entityAirDragModifier);
/* 2547 */       float friction = blockFriction * airDrag;
/* 2549 */       float verticalFriction = omnidirectionalAirMover() ? airDrag : computeModifiedFriction(0.98F, entityAirDragModifier);
/* 2550 */       setDeltaMovement(movement.x * friction, movementY * verticalFriction, movement.z * friction);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected float getAirDrag() {
/* 2556 */     return computeModifiedFriction(omnidirectionalAirMover() ? 0.91F : 0.98F, (float)getAttributeValue(Attributes.AIR_DRAG_MODIFIER));
/*      */   }
/*      */   
/*      */   protected void travelInFluid(Vec3 input) {
/* 2560 */     boolean isFalling = ((getDeltaMovement()).y <= 0.0D);
/* 2561 */     double oldY = getY();
/* 2562 */     double baseGravity = getEffectiveGravity();
/* 2564 */     if (isInWater()) {
/* 2565 */       travelInWater(input, baseGravity, isFalling, oldY);
/* 2566 */       floatInWaterWhileRidden();
/*      */     } else {
/* 2568 */       travelInLava(input, baseGravity, isFalling, oldY);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 2573 */     float slowDown = isSprinting() ? 0.9F : getWaterSlowDown();
/* 2574 */     float speed = 0.02F;
/* 2576 */     float waterWalker = (float)getAttributeValue(Attributes.WATER_MOVEMENT_EFFICIENCY);
/* 2577 */     if (!onGround())
/* 2578 */       waterWalker *= 0.5F; 
/* 2580 */     if (waterWalker > 0.0F) {
/* 2582 */       slowDown += (0.54600006F - slowDown) * waterWalker;
/* 2584 */       speed += (getSpeed() - speed) * waterWalker;
/*      */     } 
/* 2587 */     if (hasEffect(MobEffects.DOLPHINS_GRACE))
/* 2588 */       slowDown = 0.96F; 
/* 2591 */     moveRelative(speed, input);
/* 2592 */     move(MoverType.SELF, getDeltaMovement());
/* 2594 */     Vec3 movement = getDeltaMovement();
/* 2595 */     if (this.horizontalCollision && onClimbable())
/* 2596 */       movement = new Vec3(movement.x, 0.2D, movement.z); 
/* 2599 */     movement = movement.multiply(slowDown, 0.800000011920929D, slowDown);
/* 2600 */     setDeltaMovement(getFluidFallingAdjustedMovement(baseGravity, isFalling, movement));
/* 2602 */     jumpOutOfFluid(oldY);
/*      */   }
/*      */   
/*      */   protected boolean isInShallowFluid(TagKey<Fluid> fluidTag) {
/* 2606 */     return (getFluidHeight(fluidTag) <= getFluidJumpThreshold());
/*      */   }
/*      */   
/*      */   private void travelInLava(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 2610 */     moveRelative(0.02F, input);
/* 2611 */     move(MoverType.SELF, getDeltaMovement());
/* 2615 */     if (isInShallowFluid(FluidTags.LAVA)) {
/* 2616 */       setDeltaMovement(getDeltaMovement().multiply(0.5D, 0.800000011920929D, 0.5D));
/* 2617 */       Vec3 movement = getFluidFallingAdjustedMovement(baseGravity, isFalling, getDeltaMovement());
/* 2618 */       setDeltaMovement(movement);
/*      */     } else {
/* 2620 */       setDeltaMovement(getDeltaMovement().scale(0.5D));
/*      */     } 
/* 2623 */     if (baseGravity != 0.0D)
/* 2624 */       setDeltaMovement(getDeltaMovement().add(0.0D, -baseGravity / 4.0D, 0.0D)); 
/* 2627 */     jumpOutOfFluid(oldY);
/*      */   }
/*      */   
/*      */   private void jumpOutOfFluid(double oldY) {
/* 2631 */     Vec3 movement = getDeltaMovement();
/* 2632 */     if (this.horizontalCollision && isFree(movement.x, movement.y + 0.6000000238418579D - getY() + oldY, movement.z))
/* 2633 */       setDeltaMovement(movement.x, 0.30000001192092896D, movement.z); 
/*      */   }
/*      */   
/*      */   private void floatInWaterWhileRidden() {
/* 2638 */     boolean canEntityFloatInWater = is(EntityTypeTags.CAN_FLOAT_WHILE_RIDDEN);
/* 2639 */     if (canEntityFloatInWater && isVehicle() && getFluidHeight(FluidTags.WATER) > getFluidJumpThreshold())
/* 2640 */       setDeltaMovement(getDeltaMovement().add(0.0D, 0.03999999910593033D, 0.0D)); 
/*      */   }
/*      */   
/*      */   private void travelFallFlying(Vec3 input) {
/* 2645 */     if (onClimbable()) {
/* 2647 */       travelInAir(input);
/* 2648 */       stopFallFlying();
/*      */       return;
/*      */     } 
/* 2651 */     Vec3 lastMovement = getDeltaMovement();
/* 2652 */     double lastSpeed = lastMovement.horizontalDistance();
/* 2654 */     setDeltaMovement(updateFallFlyingMovement(lastMovement));
/* 2655 */     move(MoverType.SELF, getDeltaMovement());
/* 2657 */     if (!level().isClientSide()) {
/* 2658 */       double newSpeed = getDeltaMovement().horizontalDistance();
/* 2659 */       handleFallFlyingCollisions(lastSpeed, newSpeed);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void stopFallFlying() {
/* 2665 */     setSharedFlag(7, true);
/* 2666 */     setSharedFlag(7, false);
/*      */   }
/*      */   
/*      */   private Vec3 updateFallFlyingMovement(Vec3 movement) {
/* 2670 */     Vec3 lookAngle = getLookAngle();
/* 2671 */     float leanAngle = getXRot() * 0.017453292F;
/* 2672 */     double lookHorLength = Math.sqrt(lookAngle.x * lookAngle.x + lookAngle.z * lookAngle.z);
/* 2673 */     double moveHorLength = movement.horizontalDistance();
/* 2675 */     double gravity = getEffectiveGravity();
/* 2678 */     double liftForce = Mth.square(Math.cos(leanAngle));
/* 2679 */     movement = movement.add(0.0D, gravity * (-1.0D + liftForce * 0.75D), 0.0D);
/* 2682 */     if (movement.y < 0.0D && lookHorLength > 0.0D) {
/* 2683 */       double convert = movement.y * -0.1D * liftForce;
/* 2684 */       movement = movement.add(lookAngle.x * convert / lookHorLength, convert, lookAngle.z * convert / lookHorLength);
/*      */     } 
/* 2691 */     if (leanAngle < 0.0F && lookHorLength > 0.0D) {
/* 2692 */       double convert = moveHorLength * -Mth.sin(leanAngle) * 0.04D;
/* 2694 */       movement = movement.add(-lookAngle.x * convert / lookHorLength, convert * 3.2D, -lookAngle.z * convert / lookHorLength);
/*      */     } 
/* 2702 */     if (lookHorLength > 0.0D)
/* 2703 */       movement = movement.add((lookAngle.x / lookHorLength * moveHorLength - movement.x) * 0.1D, 0.0D, (lookAngle.z / lookHorLength * moveHorLength - movement.z) * 0.1D); 
/* 2710 */     return movement.multiply(0.9900000095367432D, 0.9800000190734863D, 0.9900000095367432D);
/*      */   }
/*      */   
/*      */   private void handleFallFlyingCollisions(double moveHorLength, double newMoveHorLength) {
/* 2714 */     if (this.horizontalCollision) {
/* 2715 */       double diff = moveHorLength - newMoveHorLength;
/* 2716 */       float dmg = (float)(diff * 10.0D - 3.0D);
/* 2718 */       if (dmg > 0.0F) {
/* 2719 */         playSound(getFallDamageSound((int)dmg), 1.0F, 1.0F);
/* 2720 */         hurt(damageSources().flyIntoWall(), dmg);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void travelRidden(Player controller, Vec3 selfInput) {
/* 2726 */     Vec3 riddenInput = getRiddenInput(controller, selfInput);
/* 2727 */     tickRidden(controller, riddenInput);
/* 2729 */     if (canSimulateMovement()) {
/* 2730 */       setSpeed(getRiddenSpeed(controller));
/* 2731 */       travel(riddenInput);
/*      */     } else {
/* 2733 */       setDeltaMovement(Vec3.ZERO);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void tickRidden(Player controller, Vec3 riddenInput) {}
/*      */   
/*      */   protected Vec3 getRiddenInput(Player controller, Vec3 selfInput) {
/* 2741 */     return selfInput;
/*      */   }
/*      */   
/*      */   protected float getRiddenSpeed(Player controller) {
/* 2745 */     return getSpeed();
/*      */   }
/*      */   
/*      */   public void calculateEntityAnimation(boolean useY) {
/* 2749 */     float distance = (float)Mth.length(
/* 2750 */         getX() - this.xo, 
/* 2751 */         useY ? (getY() - this.yo) : 0.0D, 
/* 2752 */         getZ() - this.zo);
/* 2755 */     if (isPassenger() || !isAlive()) {
/* 2756 */       this.walkAnimation.stop();
/*      */     } else {
/* 2758 */       updateWalkAnimation(distance);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void updateWalkAnimation(float distance) {
/* 2763 */     float targetSpeed = Math.min(distance * 4.0F, 1.0F);
/* 2764 */     this.walkAnimation.update(targetSpeed, 0.4F, isBaby() ? 3.0F : 1.0F);
/*      */   }
/*      */   
/*      */   private Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 input, float friction) {
/* 2768 */     moveRelative(getFrictionInfluencedSpeed(friction), input);
/* 2770 */     setDeltaMovement(handleOnClimbable(getDeltaMovement()));
/* 2771 */     move(MoverType.SELF, getDeltaMovement());
/* 2773 */     Vec3 movement = getDeltaMovement();
/* 2774 */     if ((this.horizontalCollision || this.jumping) && (onClimbable() || (this.wasInPowderSnow && PowderSnowBlock.canEntityWalkOnPowderSnow(this))))
/* 2775 */       movement = new Vec3(movement.x, 0.2D, movement.z); 
/* 2777 */     return movement;
/*      */   }
/*      */   
/*      */   public Vec3 getFluidFallingAdjustedMovement(double baseGravity, boolean isFalling, Vec3 movement) {
/* 2781 */     if (baseGravity != 0.0D && !isSprinting()) {
/*      */       double yd;
/* 2783 */       if (isFalling && Math.abs(movement.y - 0.005D) >= 0.003D && Math.abs(movement.y - baseGravity / 16.0D) < 0.003D) {
/* 2785 */         yd = -0.003D;
/*      */       } else {
/* 2787 */         yd = movement.y - baseGravity / 16.0D;
/*      */       } 
/* 2789 */       return new Vec3(movement.x, yd, movement.z);
/*      */     } 
/* 2791 */     return movement;
/*      */   }
/*      */   
/*      */   private Vec3 handleOnClimbable(Vec3 delta) {
/* 2795 */     if (onClimbable()) {
/* 2796 */       resetFallDistance();
/* 2798 */       float max = 0.15F;
/* 2799 */       double xd = Mth.clamp(delta.x, -0.15000000596046448D, 0.15000000596046448D);
/* 2800 */       double zd = Mth.clamp(delta.z, -0.15000000596046448D, 0.15000000596046448D);
/* 2802 */       double yd = Math.max(delta.y, -0.15000000596046448D);
/* 2803 */       if (yd < 0.0D && !getInBlockState().is(Blocks.SCAFFOLDING) && isSuppressingSlidingDownLadder() && this instanceof Player)
/* 2804 */         yd = 0.0D; 
/* 2807 */       delta = new Vec3(xd, yd, zd);
/*      */     } 
/* 2809 */     return delta;
/*      */   }
/*      */   
/*      */   private float getFrictionInfluencedSpeed(float blockFriction) {
/* 2813 */     if (onGround()) {
/* 2814 */       if (blockFriction > 0.6D)
/* 2816 */         return getSpeed() * 0.21600002F / blockFriction * blockFriction * blockFriction; 
/* 2819 */       return getSpeed();
/*      */     } 
/* 2821 */     return getFlyingSpeed();
/*      */   }
/*      */   
/*      */   protected float getFlyingSpeed() {
/* 2826 */     return (getControllingPassenger() instanceof Player) ? (getSpeed() * 0.1F) : 0.02F;
/*      */   }
/*      */   
/*      */   public float getSpeed() {
/* 2830 */     return this.speed;
/*      */   }
/*      */   
/*      */   public void setSpeed(float speed) {
/* 2834 */     this.speed = speed;
/*      */   }
/*      */   
/*      */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 2838 */     setLastHurtMob(target);
/* 2839 */     return false;
/*      */   }
/*      */   
/*      */   public void causeExtraKnockback(Entity target, float knockback, Vec3 oldMovement, DamageSource damageSource, float damage, boolean comesFromEffect) {
/* 2843 */     if (knockback > 0.0F && target instanceof LivingEntity) {
/* 2843 */       LivingEntity livingTarget = (LivingEntity)target;
/* 2844 */       livingTarget.knockback(knockback, Mth.sin((getYRot() * 0.017453292F)), -Mth.cos((getYRot() * 0.017453292F)), damageSource, damage, comesFromEffect);
/* 2845 */       setDeltaMovement(getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void playAttackSound() {}
/*      */   
/*      */   public void tick() {
/* 2854 */     super.tick();
/* 2855 */     updatingUsingItem();
/* 2856 */     updateSwimAmount();
/* 2858 */     if (!level().isClientSide()) {
/* 2859 */       int arrowCount = getArrowCount();
/* 2860 */       if (arrowCount > 0) {
/* 2861 */         if (this.removeArrowTime <= 0)
/* 2862 */           this.removeArrowTime = 20 * (30 - arrowCount); 
/* 2864 */         this.removeArrowTime--;
/* 2865 */         if (this.removeArrowTime <= 0)
/* 2866 */           setArrowCount(arrowCount - 1); 
/*      */       } 
/* 2870 */       int stingerCount = getStingerCount();
/* 2871 */       if (stingerCount > 0) {
/* 2872 */         if (this.removeStingerTime <= 0)
/* 2873 */           this.removeStingerTime = 20 * (30 - stingerCount); 
/* 2875 */         this.removeStingerTime--;
/* 2876 */         if (this.removeStingerTime <= 0)
/* 2877 */           setStingerCount(stingerCount - 1); 
/*      */       } 
/* 2881 */       detectEquipmentUpdates();
/* 2883 */       if (this.tickCount % 20 == 0)
/* 2884 */         getCombatTracker().recheckStatus(); 
/* 2887 */       if (isSleeping() && (!canInteractWithLevel() || !checkBedExists()))
/* 2888 */         stopSleeping(); 
/*      */     } 
/* 2892 */     if (!isRemoved())
/* 2893 */       aiStep(); 
/* 2896 */     double xd = getX() - this.xo;
/* 2897 */     double zd = getZ() - this.zo;
/* 2899 */     float sideDist = (float)(xd * xd + zd * zd);
/* 2901 */     float yBodyRotT = this.yBodyRot;
/* 2903 */     if (sideDist > 0.0025000002F) {
/* 2904 */       float walkDirection = (float)Mth.atan2(zd, xd) * 57.295776F - 90.0F;
/* 2905 */       float diffBetweenDirectionAndFacing = Mth.abs(Mth.wrapDegrees(getYRot()) - walkDirection);
/* 2906 */       if (95.0F < diffBetweenDirectionAndFacing && diffBetweenDirectionAndFacing < 265.0F) {
/* 2907 */         yBodyRotT = walkDirection - 180.0F;
/*      */       } else {
/* 2909 */         yBodyRotT = walkDirection;
/*      */       } 
/*      */     } 
/* 2912 */     if (this.attackAnim > 0.0F)
/* 2913 */       yBodyRotT = getYRot(); 
/* 2916 */     ProfilerFiller profiler = Profiler.get();
/* 2917 */     profiler.push("headTurn");
/* 2919 */     tickHeadTurn(yBodyRotT);
/* 2921 */     profiler.pop();
/* 2923 */     profiler.push("rangeChecks");
/* 2924 */     while (getYRot() - this.yRotO < -180.0F)
/* 2925 */       this.yRotO -= 360.0F; 
/* 2927 */     while (getYRot() - this.yRotO >= 180.0F)
/* 2928 */       this.yRotO += 360.0F; 
/* 2931 */     while (this.yBodyRot - this.yBodyRotO < -180.0F)
/* 2932 */       this.yBodyRotO -= 360.0F; 
/* 2934 */     while (this.yBodyRot - this.yBodyRotO >= 180.0F)
/* 2935 */       this.yBodyRotO += 360.0F; 
/* 2938 */     while (getXRot() - this.xRotO < -180.0F)
/* 2939 */       this.xRotO -= 360.0F; 
/* 2941 */     while (getXRot() - this.xRotO >= 180.0F)
/* 2942 */       this.xRotO += 360.0F; 
/* 2945 */     while (this.yHeadRot - this.yHeadRotO < -180.0F)
/* 2946 */       this.yHeadRotO -= 360.0F; 
/* 2948 */     while (this.yHeadRot - this.yHeadRotO >= 180.0F)
/* 2949 */       this.yHeadRotO += 360.0F; 
/* 2951 */     profiler.pop();
/* 2953 */     if (isFallFlying()) {
/* 2954 */       this.fallFlyTicks++;
/*      */     } else {
/* 2956 */       this.fallFlyTicks = 0;
/*      */     } 
/* 2959 */     if (isSleeping())
/* 2960 */       setXRot(0.0F); 
/* 2963 */     refreshDirtyAttributes();
/* 2964 */     this.elytraAnimationState.tick();
/* 2966 */     if (this.currentImpulseContextResetGraceTime > 0)
/* 2967 */       this.currentImpulseContextResetGraceTime--; 
/*      */   }
/*      */   
/*      */   public boolean wasRecentlyStabbed(Entity target, int allowedTime) {
/* 2972 */     if (this.recentKineticEnemies == null)
/* 2973 */       return false; 
/* 2975 */     if (this.recentKineticEnemies.containsKey(target))
/* 2976 */       return (level().getGameTime() - this.recentKineticEnemies.getLong(target) < allowedTime); 
/* 2978 */     return false;
/*      */   }
/*      */   
/*      */   public void rememberStabbedEntity(Entity target) {
/* 2982 */     if (this.recentKineticEnemies != null)
/* 2983 */       this.recentKineticEnemies.put(target, level().getGameTime()); 
/*      */   }
/*      */   
/*      */   public int stabbedEntities(Predicate<Entity> filter) {
/* 2988 */     if (this.recentKineticEnemies == null)
/* 2989 */       return 0; 
/* 2991 */     return (int)this.recentKineticEnemies.keySet().stream().filter(filter).count();
/*      */   }
/*      */   
/*      */   public boolean stabAttack(EquipmentSlot weaponSlot, Entity target, float baseDamage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts) {
/*      */     ServerLevel serverLevel;
/* 2995 */     Level level = level();
/* 2995 */     if (level instanceof ServerLevel) {
/* 2995 */       serverLevel = (ServerLevel)level;
/*      */     } else {
/* 2996 */       return false;
/*      */     } 
/* 2999 */     ItemStack weaponItem = getItemBySlot(weaponSlot);
/* 3001 */     DamageSource damageSource = weaponItem.getDamageSource(this);
/* 3003 */     float postEnchantmentDamage = EnchantmentHelper.modifyDamage(serverLevel, weaponItem, target, damageSource, baseDamage);
/* 3005 */     Vec3 oldMovement = target.getDeltaMovement();
/* 3007 */     boolean affected = dealsKnockback;
/* 3009 */     boolean dealtDamage = (dealsDamage && target.hurtServer(serverLevel, damageSource, postEnchantmentDamage));
/* 3011 */     affected |= dealtDamage;
/* 3013 */     if (dealsKnockback) {
/* 3016 */       causeExtraKnockback(target, 0.4F, oldMovement, damageSource, postEnchantmentDamage, false);
/* 3018 */       causeExtraKnockback(target, getKnockback(target, damageSource), oldMovement, damageSource, postEnchantmentDamage, true);
/*      */     } 
/* 3020 */     if (dismounts && target.isPassenger()) {
/* 3021 */       affected = true;
/* 3022 */       target.stopRiding();
/*      */     } 
/* 3025 */     if (target instanceof LivingEntity) {
/* 3025 */       LivingEntity livingTarget = (LivingEntity)target;
/* 3026 */       weaponItem.hurtEnemy(livingTarget, this);
/*      */     } 
/* 3028 */     if (dealtDamage)
/* 3029 */       EnchantmentHelper.doPostAttackEffects(serverLevel, target, damageSource); 
/* 3032 */     if (!affected)
/* 3033 */       return false; 
/* 3035 */     setLastHurtMob(target);
/* 3036 */     playAttackSound();
/* 3037 */     return true;
/*      */   }
/*      */   
/*      */   public void onAttack() {}
/*      */   
/*      */   private void detectEquipmentUpdates() {
/* 3044 */     Map<EquipmentSlot, ItemStack> changedItems = collectEquipmentChanges(this.lastEquipmentItems);
/* 3046 */     if (changedItems != null) {
/* 3047 */       handleHandSwap(changedItems);
/* 3049 */       if (!changedItems.isEmpty())
/* 3050 */         handleEquipmentChanges(changedItems); 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Map<EquipmentSlot, ItemStack> collectEquipmentChanges(Map<EquipmentSlot, ItemStack> lastEquipmentItems) {
/* 3056 */     Map<EquipmentSlot, ItemStack> changedItems = null;
/* 3057 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 3058 */       ItemStack previous = lastEquipmentItems.get(slot);
/* 3059 */       ItemStack current = getItemBySlot(slot);
/* 3061 */       if (equipmentHasChanged(previous, current)) {
/* 3062 */         if (changedItems == null)
/* 3063 */           changedItems = Maps.newEnumMap(EquipmentSlot.class); 
/* 3065 */         changedItems.put(slot, current);
/* 3067 */         AttributeMap attributes = getAttributes();
/* 3068 */         if (!previous.isEmpty())
/* 3069 */           stopLocationBasedEffects(previous, slot, attributes); 
/*      */       } 
/*      */     } 
/* 3074 */     if (changedItems != null)
/* 3075 */       for (Iterator<Map.Entry<EquipmentSlot, ItemStack>> iterator = changedItems.entrySet().iterator(); iterator.hasNext(); ) {
/* 3075 */         Map.Entry<EquipmentSlot, ItemStack> entry = iterator.next();
/* 3076 */         EquipmentSlot slot = entry.getKey();
/* 3077 */         ItemStack current = entry.getValue();
/* 3078 */         if (!current.isEmpty() && !current.isBroken()) {
/* 3079 */           current.forEachModifier(slot, (attribute, modifier) -> {
/*      */                 AttributeInstance instance = this.attributes.getInstance(attribute);
/*      */                 if (instance != null) {
/*      */                   instance.removeModifier(modifier.id());
/*      */                   instance.addTransientModifier(modifier);
/*      */                 } 
/*      */               });
/* 3086 */           Level level = level();
/* 3086 */           if (level instanceof ServerLevel) {
/* 3086 */             ServerLevel serverLevel = (ServerLevel)level;
/* 3087 */             EnchantmentHelper.runLocationChangedEffects(serverLevel, current, this, slot);
/*      */           } 
/*      */         } 
/*      */       }  
/* 3092 */     return changedItems;
/*      */   }
/*      */   
/*      */   public boolean equipmentHasChanged(ItemStack previous, ItemStack current) {
/* 3096 */     return !ItemStack.matches(current, previous);
/*      */   }
/*      */   
/*      */   private void handleHandSwap(Map<EquipmentSlot, ItemStack> changedItems) {
/* 3101 */     ItemStack currentMainHand = changedItems.get(EquipmentSlot.MAINHAND);
/* 3102 */     ItemStack currentOffHand = changedItems.get(EquipmentSlot.OFFHAND);
/* 3104 */     if (currentMainHand != null && currentOffHand != null && 
/* 3105 */       ItemStack.matches(currentMainHand, this.lastEquipmentItems.get(EquipmentSlot.OFFHAND)) && 
/* 3106 */       ItemStack.matches(currentOffHand, this.lastEquipmentItems.get(EquipmentSlot.MAINHAND))) {
/* 3108 */       ((ServerLevel)level()).getChunkSource().sendToTrackingPlayers(this, (Packet)new ClientboundEntityEventPacket(this, (byte)55));
/* 3109 */       changedItems.remove(EquipmentSlot.MAINHAND);
/* 3110 */       changedItems.remove(EquipmentSlot.OFFHAND);
/* 3111 */       this.lastEquipmentItems.put(EquipmentSlot.MAINHAND, currentMainHand.copy());
/* 3112 */       this.lastEquipmentItems.put(EquipmentSlot.OFFHAND, currentOffHand.copy());
/*      */     } 
/*      */   }
/*      */   
/*      */   private void handleEquipmentChanges(Map<EquipmentSlot, ItemStack> changedItems) {
/* 3117 */     List<Pair<EquipmentSlot, ItemStack>> itemsToSend = Lists.newArrayListWithCapacity(changedItems.size());
/* 3118 */     changedItems.forEach((slot, newItem) -> {
/*      */           ItemStack newItemToStore = newItem.copy();
/*      */           itemsToSend.add(Pair.of(slot, newItemToStore));
/*      */           this.lastEquipmentItems.put(slot, newItemToStore);
/*      */         });
/* 3124 */     ((ServerLevel)level()).getChunkSource().sendToTrackingPlayers(this, (Packet)new ClientboundSetEquipmentPacket(getId(), itemsToSend));
/*      */   }
/*      */   
/*      */   protected void tickHeadTurn(float yBodyRotT) {
/* 3128 */     float yBodyRotD = Mth.wrapDegrees(yBodyRotT - this.yBodyRot);
/* 3129 */     this.yBodyRot += yBodyRotD * 0.3F;
/* 3131 */     float headDiff = Mth.wrapDegrees(getYRot() - this.yBodyRot);
/* 3133 */     float maxHeadRotation = getMaxHeadRotationRelativeToBody();
/* 3134 */     if (Math.abs(headDiff) > maxHeadRotation)
/* 3135 */       this.yBodyRot += headDiff - Mth.sign(headDiff) * maxHeadRotation; 
/*      */   }
/*      */   
/*      */   protected float getMaxHeadRotationRelativeToBody() {
/* 3140 */     return 50.0F;
/*      */   }
/*      */   
/*      */   public void aiStep() {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield noJumpDelay : I
/*      */     //   4: ifle -> 17
/*      */     //   7: aload_0
/*      */     //   8: dup
/*      */     //   9: getfield noJumpDelay : I
/*      */     //   12: iconst_1
/*      */     //   13: isub
/*      */     //   14: putfield noJumpDelay : I
/*      */     //   17: aload_0
/*      */     //   18: invokevirtual isInterpolating : ()Z
/*      */     //   21: ifeq -> 34
/*      */     //   24: aload_0
/*      */     //   25: invokevirtual getInterpolation : ()Lnet/minecraft/world/entity/InterpolationHandler;
/*      */     //   28: invokevirtual interpolate : ()V
/*      */     //   31: goto -> 55
/*      */     //   34: aload_0
/*      */     //   35: invokevirtual canSimulateMovement : ()Z
/*      */     //   38: ifne -> 55
/*      */     //   41: aload_0
/*      */     //   42: aload_0
/*      */     //   43: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*      */     //   46: ldc2_w 0.98
/*      */     //   49: invokevirtual scale : (D)Lnet/minecraft/world/phys/Vec3;
/*      */     //   52: invokevirtual setDeltaMovement : (Lnet/minecraft/world/phys/Vec3;)V
/*      */     //   55: aload_0
/*      */     //   56: getfield lerpHeadSteps : I
/*      */     //   59: ifle -> 84
/*      */     //   62: aload_0
/*      */     //   63: aload_0
/*      */     //   64: getfield lerpHeadSteps : I
/*      */     //   67: aload_0
/*      */     //   68: getfield lerpYHeadRot : D
/*      */     //   71: invokevirtual lerpHeadRotationStep : (ID)V
/*      */     //   74: aload_0
/*      */     //   75: dup
/*      */     //   76: getfield lerpHeadSteps : I
/*      */     //   79: iconst_1
/*      */     //   80: isub
/*      */     //   81: putfield lerpHeadSteps : I
/*      */     //   84: aload_0
/*      */     //   85: getfield equipment : Lnet/minecraft/world/entity/EntityEquipment;
/*      */     //   88: aload_0
/*      */     //   89: invokevirtual tick : (Lnet/minecraft/world/entity/Entity;)V
/*      */     //   92: aload_0
/*      */     //   93: invokevirtual getDeltaMovement : ()Lnet/minecraft/world/phys/Vec3;
/*      */     //   96: astore_1
/*      */     //   97: aload_1
/*      */     //   98: getfield x : D
/*      */     //   101: dstore_2
/*      */     //   102: aload_1
/*      */     //   103: getfield y : D
/*      */     //   106: dstore #4
/*      */     //   108: aload_1
/*      */     //   109: getfield z : D
/*      */     //   112: dstore #6
/*      */     //   114: aload_0
/*      */     //   115: getstatic net/minecraft/world/entity/EntityTypes.PLAYER : Lnet/minecraft/world/entity/EntityType;
/*      */     //   118: invokevirtual is : (Ljava/lang/Object;)Z
/*      */     //   121: ifeq -> 143
/*      */     //   124: aload_1
/*      */     //   125: invokevirtual horizontalDistanceSqr : ()D
/*      */     //   128: ldc2_w 9.0E-6
/*      */     //   131: dcmpg
/*      */     //   132: ifge -> 176
/*      */     //   135: dconst_0
/*      */     //   136: dstore_2
/*      */     //   137: dconst_0
/*      */     //   138: dstore #6
/*      */     //   140: goto -> 176
/*      */     //   143: aload_1
/*      */     //   144: getfield x : D
/*      */     //   147: invokestatic abs : (D)D
/*      */     //   150: ldc2_w 0.003
/*      */     //   153: dcmpg
/*      */     //   154: ifge -> 159
/*      */     //   157: dconst_0
/*      */     //   158: dstore_2
/*      */     //   159: aload_1
/*      */     //   160: getfield z : D
/*      */     //   163: invokestatic abs : (D)D
/*      */     //   166: ldc2_w 0.003
/*      */     //   169: dcmpg
/*      */     //   170: ifge -> 176
/*      */     //   173: dconst_0
/*      */     //   174: dstore #6
/*      */     //   176: aload_1
/*      */     //   177: getfield y : D
/*      */     //   180: invokestatic abs : (D)D
/*      */     //   183: ldc2_w 0.003
/*      */     //   186: dcmpg
/*      */     //   187: ifge -> 193
/*      */     //   190: dconst_0
/*      */     //   191: dstore #4
/*      */     //   193: aload_0
/*      */     //   194: dload_2
/*      */     //   195: dload #4
/*      */     //   197: dload #6
/*      */     //   199: invokevirtual setDeltaMovement : (DDD)V
/*      */     //   202: invokestatic get : ()Lnet/minecraft/util/profiling/ProfilerFiller;
/*      */     //   205: astore #8
/*      */     //   207: aload #8
/*      */     //   209: ldc_w 'ai'
/*      */     //   212: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   217: aload_0
/*      */     //   218: invokevirtual applyInput : ()V
/*      */     //   221: aload_0
/*      */     //   222: invokevirtual isImmobile : ()Z
/*      */     //   225: ifeq -> 246
/*      */     //   228: aload_0
/*      */     //   229: iconst_0
/*      */     //   230: putfield jumping : Z
/*      */     //   233: aload_0
/*      */     //   234: fconst_0
/*      */     //   235: putfield xxa : F
/*      */     //   238: aload_0
/*      */     //   239: fconst_0
/*      */     //   240: putfield zza : F
/*      */     //   243: goto -> 284
/*      */     //   246: aload_0
/*      */     //   247: invokevirtual isEffectiveAi : ()Z
/*      */     //   250: ifeq -> 284
/*      */     //   253: aload_0
/*      */     //   254: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   257: invokevirtual isClientSide : ()Z
/*      */     //   260: ifne -> 284
/*      */     //   263: aload #8
/*      */     //   265: ldc_w 'newAi'
/*      */     //   268: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   273: aload_0
/*      */     //   274: invokevirtual serverAiStep : ()V
/*      */     //   277: aload #8
/*      */     //   279: invokeinterface pop : ()V
/*      */     //   284: aload #8
/*      */     //   286: invokeinterface pop : ()V
/*      */     //   291: aload #8
/*      */     //   293: ldc_w 'jump'
/*      */     //   296: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   301: aload_0
/*      */     //   302: getfield jumping : Z
/*      */     //   305: ifeq -> 474
/*      */     //   308: aload_0
/*      */     //   309: invokevirtual isAffectedByFluids : ()Z
/*      */     //   312: ifeq -> 474
/*      */     //   315: aload_0
/*      */     //   316: invokevirtual isInLava : ()Z
/*      */     //   319: ifeq -> 334
/*      */     //   322: aload_0
/*      */     //   323: getstatic net/minecraft/tags/FluidTags.LAVA : Lnet/minecraft/tags/TagKey;
/*      */     //   326: invokevirtual getFluidHeight : (Lnet/minecraft/tags/TagKey;)D
/*      */     //   329: dstore #9
/*      */     //   331: goto -> 343
/*      */     //   334: aload_0
/*      */     //   335: getstatic net/minecraft/tags/FluidTags.WATER : Lnet/minecraft/tags/TagKey;
/*      */     //   338: invokevirtual getFluidHeight : (Lnet/minecraft/tags/TagKey;)D
/*      */     //   341: dstore #9
/*      */     //   343: aload_0
/*      */     //   344: invokevirtual isInWater : ()Z
/*      */     //   347: ifeq -> 361
/*      */     //   350: dload #9
/*      */     //   352: dconst_0
/*      */     //   353: dcmpl
/*      */     //   354: ifle -> 361
/*      */     //   357: iconst_1
/*      */     //   358: goto -> 362
/*      */     //   361: iconst_0
/*      */     //   362: istore #11
/*      */     //   364: aload_0
/*      */     //   365: invokevirtual getFluidJumpThreshold : ()D
/*      */     //   368: dstore #12
/*      */     //   370: iload #11
/*      */     //   372: ifeq -> 400
/*      */     //   375: aload_0
/*      */     //   376: invokevirtual onGround : ()Z
/*      */     //   379: ifeq -> 390
/*      */     //   382: dload #9
/*      */     //   384: dload #12
/*      */     //   386: dcmpl
/*      */     //   387: ifle -> 400
/*      */     //   390: aload_0
/*      */     //   391: getstatic net/minecraft/tags/FluidTags.WATER : Lnet/minecraft/tags/TagKey;
/*      */     //   394: invokevirtual jumpInLiquid : (Lnet/minecraft/tags/TagKey;)V
/*      */     //   397: goto -> 471
/*      */     //   400: aload_0
/*      */     //   401: invokevirtual isInLava : ()Z
/*      */     //   404: ifeq -> 434
/*      */     //   407: aload_0
/*      */     //   408: invokevirtual onGround : ()Z
/*      */     //   411: ifeq -> 424
/*      */     //   414: aload_0
/*      */     //   415: getstatic net/minecraft/tags/FluidTags.LAVA : Lnet/minecraft/tags/TagKey;
/*      */     //   418: invokevirtual isInShallowFluid : (Lnet/minecraft/tags/TagKey;)Z
/*      */     //   421: ifne -> 434
/*      */     //   424: aload_0
/*      */     //   425: getstatic net/minecraft/tags/FluidTags.LAVA : Lnet/minecraft/tags/TagKey;
/*      */     //   428: invokevirtual jumpInLiquid : (Lnet/minecraft/tags/TagKey;)V
/*      */     //   431: goto -> 471
/*      */     //   434: aload_0
/*      */     //   435: invokevirtual onGround : ()Z
/*      */     //   438: ifne -> 454
/*      */     //   441: iload #11
/*      */     //   443: ifeq -> 471
/*      */     //   446: dload #9
/*      */     //   448: dload #12
/*      */     //   450: dcmpg
/*      */     //   451: ifgt -> 471
/*      */     //   454: aload_0
/*      */     //   455: getfield noJumpDelay : I
/*      */     //   458: ifne -> 471
/*      */     //   461: aload_0
/*      */     //   462: invokevirtual jumpFromGround : ()V
/*      */     //   465: aload_0
/*      */     //   466: bipush #10
/*      */     //   468: putfield noJumpDelay : I
/*      */     //   471: goto -> 479
/*      */     //   474: aload_0
/*      */     //   475: iconst_0
/*      */     //   476: putfield noJumpDelay : I
/*      */     //   479: aload #8
/*      */     //   481: invokeinterface pop : ()V
/*      */     //   486: aload #8
/*      */     //   488: ldc_w 'travel'
/*      */     //   491: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   496: aload_0
/*      */     //   497: invokevirtual isFallFlying : ()Z
/*      */     //   500: ifeq -> 507
/*      */     //   503: aload_0
/*      */     //   504: invokevirtual updateFallFlying : ()V
/*      */     //   507: aload_0
/*      */     //   508: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*      */     //   511: astore #9
/*      */     //   513: new net/minecraft/world/phys/Vec3
/*      */     //   516: dup
/*      */     //   517: aload_0
/*      */     //   518: getfield xxa : F
/*      */     //   521: f2d
/*      */     //   522: aload_0
/*      */     //   523: getfield yya : F
/*      */     //   526: f2d
/*      */     //   527: aload_0
/*      */     //   528: getfield zza : F
/*      */     //   531: f2d
/*      */     //   532: invokespecial <init> : (DDD)V
/*      */     //   535: astore #10
/*      */     //   537: aload_0
/*      */     //   538: getstatic net/minecraft/world/effect/MobEffects.SLOW_FALLING : Lnet/minecraft/core/Holder;
/*      */     //   541: invokevirtual hasEffect : (Lnet/minecraft/core/Holder;)Z
/*      */     //   544: ifne -> 557
/*      */     //   547: aload_0
/*      */     //   548: getstatic net/minecraft/world/effect/MobEffects.LEVITATION : Lnet/minecraft/core/Holder;
/*      */     //   551: invokevirtual hasEffect : (Lnet/minecraft/core/Holder;)Z
/*      */     //   554: ifeq -> 561
/*      */     //   557: aload_0
/*      */     //   558: invokevirtual resetFallDistance : ()V
/*      */     //   561: aload_0
/*      */     //   562: invokevirtual getControllingPassenger : ()Lnet/minecraft/world/entity/LivingEntity;
/*      */     //   565: astore #12
/*      */     //   567: aload #12
/*      */     //   569: instanceof net/minecraft/world/entity/player/Player
/*      */     //   572: ifeq -> 600
/*      */     //   575: aload #12
/*      */     //   577: checkcast net/minecraft/world/entity/player/Player
/*      */     //   580: astore #11
/*      */     //   582: aload_0
/*      */     //   583: invokevirtual isAlive : ()Z
/*      */     //   586: ifeq -> 600
/*      */     //   589: aload_0
/*      */     //   590: aload #11
/*      */     //   592: aload #10
/*      */     //   594: invokevirtual travelRidden : (Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/phys/Vec3;)V
/*      */     //   597: goto -> 620
/*      */     //   600: aload_0
/*      */     //   601: invokevirtual canSimulateMovement : ()Z
/*      */     //   604: ifeq -> 620
/*      */     //   607: aload_0
/*      */     //   608: invokevirtual isEffectiveAi : ()Z
/*      */     //   611: ifeq -> 620
/*      */     //   614: aload_0
/*      */     //   615: aload #10
/*      */     //   617: invokevirtual travel : (Lnet/minecraft/world/phys/Vec3;)V
/*      */     //   620: aload_0
/*      */     //   621: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   624: invokevirtual isClientSide : ()Z
/*      */     //   627: ifeq -> 637
/*      */     //   630: aload_0
/*      */     //   631: invokevirtual isLocalInstanceAuthoritative : ()Z
/*      */     //   634: ifeq -> 641
/*      */     //   637: aload_0
/*      */     //   638: invokevirtual applyEffectsFromBlocks : ()V
/*      */     //   641: aload_0
/*      */     //   642: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   645: invokevirtual isClientSide : ()Z
/*      */     //   648: ifeq -> 659
/*      */     //   651: aload_0
/*      */     //   652: aload_0
/*      */     //   653: invokevirtual omnidirectionalAirMover : ()Z
/*      */     //   656: invokevirtual calculateEntityAnimation : (Z)V
/*      */     //   659: aload #8
/*      */     //   661: invokeinterface pop : ()V
/*      */     //   666: aload_0
/*      */     //   667: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   670: astore #12
/*      */     //   672: aload #12
/*      */     //   674: instanceof net/minecraft/server/level/ServerLevel
/*      */     //   677: ifeq -> 779
/*      */     //   680: aload #12
/*      */     //   682: checkcast net/minecraft/server/level/ServerLevel
/*      */     //   685: astore #11
/*      */     //   687: aload #8
/*      */     //   689: ldc_w 'freezing'
/*      */     //   692: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   697: aload_0
/*      */     //   698: getfield isInPowderSnow : Z
/*      */     //   701: ifeq -> 711
/*      */     //   704: aload_0
/*      */     //   705: invokevirtual canFreeze : ()Z
/*      */     //   708: ifne -> 725
/*      */     //   711: aload_0
/*      */     //   712: iconst_0
/*      */     //   713: aload_0
/*      */     //   714: invokevirtual getTicksFrozen : ()I
/*      */     //   717: iconst_2
/*      */     //   718: isub
/*      */     //   719: invokestatic max : (II)I
/*      */     //   722: invokevirtual setTicksFrozen : (I)V
/*      */     //   725: aload_0
/*      */     //   726: invokevirtual removeFrost : ()V
/*      */     //   729: aload_0
/*      */     //   730: invokevirtual tryAddFrost : ()V
/*      */     //   733: aload_0
/*      */     //   734: getfield tickCount : I
/*      */     //   737: bipush #40
/*      */     //   739: irem
/*      */     //   740: ifne -> 772
/*      */     //   743: aload_0
/*      */     //   744: invokevirtual isFullyFrozen : ()Z
/*      */     //   747: ifeq -> 772
/*      */     //   750: aload_0
/*      */     //   751: invokevirtual canFreeze : ()Z
/*      */     //   754: ifeq -> 772
/*      */     //   757: aload_0
/*      */     //   758: aload #11
/*      */     //   760: aload_0
/*      */     //   761: invokevirtual damageSources : ()Lnet/minecraft/world/damagesource/DamageSources;
/*      */     //   764: invokevirtual freeze : ()Lnet/minecraft/world/damagesource/DamageSource;
/*      */     //   767: fconst_1
/*      */     //   768: invokevirtual hurtServer : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z
/*      */     //   771: pop
/*      */     //   772: aload #8
/*      */     //   774: invokeinterface pop : ()V
/*      */     //   779: aload #8
/*      */     //   781: ldc_w 'push'
/*      */     //   784: invokeinterface push : (Ljava/lang/String;)V
/*      */     //   789: aload_0
/*      */     //   790: getfield autoSpinAttackTicks : I
/*      */     //   793: ifle -> 816
/*      */     //   796: aload_0
/*      */     //   797: dup
/*      */     //   798: getfield autoSpinAttackTicks : I
/*      */     //   801: iconst_1
/*      */     //   802: isub
/*      */     //   803: putfield autoSpinAttackTicks : I
/*      */     //   806: aload_0
/*      */     //   807: aload #9
/*      */     //   809: aload_0
/*      */     //   810: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*      */     //   813: invokevirtual checkAutoSpinAttack : (Lnet/minecraft/world/phys/AABB;Lnet/minecraft/world/phys/AABB;)V
/*      */     //   816: aload_0
/*      */     //   817: invokevirtual pushEntities : ()V
/*      */     //   820: aload #8
/*      */     //   822: invokeinterface pop : ()V
/*      */     //   827: aload_0
/*      */     //   828: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   831: astore #12
/*      */     //   833: aload #12
/*      */     //   835: instanceof net/minecraft/server/level/ServerLevel
/*      */     //   838: ifeq -> 877
/*      */     //   841: aload #12
/*      */     //   843: checkcast net/minecraft/server/level/ServerLevel
/*      */     //   846: astore #11
/*      */     //   848: aload_0
/*      */     //   849: invokevirtual isSensitiveToWater : ()Z
/*      */     //   852: ifeq -> 877
/*      */     //   855: aload_0
/*      */     //   856: invokevirtual isInWaterOrRain : ()Z
/*      */     //   859: ifeq -> 877
/*      */     //   862: aload_0
/*      */     //   863: aload #11
/*      */     //   865: aload_0
/*      */     //   866: invokevirtual damageSources : ()Lnet/minecraft/world/damagesource/DamageSources;
/*      */     //   869: invokevirtual drown : ()Lnet/minecraft/world/damagesource/DamageSource;
/*      */     //   872: fconst_1
/*      */     //   873: invokevirtual hurtServer : (Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z
/*      */     //   876: pop
/*      */     //   877: return
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #3144	-> 0
/*      */     //   #3145	-> 7
/*      */     //   #3147	-> 17
/*      */     //   #3148	-> 24
/*      */     //   #3149	-> 34
/*      */     //   #3151	-> 41
/*      */     //   #3153	-> 55
/*      */     //   #3154	-> 62
/*      */     //   #3155	-> 74
/*      */     //   #3157	-> 84
/*      */     //   #3159	-> 92
/*      */     //   #3160	-> 97
/*      */     //   #3161	-> 102
/*      */     //   #3162	-> 108
/*      */     //   #3163	-> 114
/*      */     //   #3164	-> 124
/*      */     //   #3165	-> 135
/*      */     //   #3166	-> 137
/*      */     //   #3171	-> 143
/*      */     //   #3172	-> 157
/*      */     //   #3174	-> 159
/*      */     //   #3175	-> 173
/*      */     //   #3178	-> 176
/*      */     //   #3179	-> 190
/*      */     //   #3181	-> 193
/*      */     //   #3183	-> 202
/*      */     //   #3184	-> 207
/*      */     //   #3185	-> 217
/*      */     //   #3186	-> 221
/*      */     //   #3187	-> 228
/*      */     //   #3188	-> 233
/*      */     //   #3189	-> 238
/*      */     //   #3190	-> 246
/*      */     //   #3191	-> 263
/*      */     //   #3192	-> 273
/*      */     //   #3193	-> 277
/*      */     //   #3196	-> 284
/*      */     //   #3198	-> 291
/*      */     //   #3199	-> 301
/*      */     //   #3203	-> 315
/*      */     //   #3204	-> 322
/*      */     //   #3206	-> 334
/*      */     //   #3208	-> 343
/*      */     //   #3209	-> 364
/*      */     //   #3210	-> 370
/*      */     //   #3211	-> 390
/*      */     //   #3212	-> 400
/*      */     //   #3213	-> 424
/*      */     //   #3214	-> 434
/*      */     //   #3215	-> 454
/*      */     //   #3216	-> 461
/*      */     //   #3217	-> 465
/*      */     //   #3220	-> 471
/*      */     //   #3221	-> 474
/*      */     //   #3223	-> 479
/*      */     //   #3225	-> 486
/*      */     //   #3227	-> 496
/*      */     //   #3228	-> 503
/*      */     //   #3230	-> 507
/*      */     //   #3231	-> 513
/*      */     //   #3232	-> 537
/*      */     //   #3233	-> 557
/*      */     //   #3235	-> 561
/*      */     //   #3236	-> 589
/*      */     //   #3237	-> 600
/*      */     //   #3238	-> 614
/*      */     //   #3240	-> 620
/*      */     //   #3241	-> 637
/*      */     //   #3244	-> 641
/*      */     //   #3245	-> 651
/*      */     //   #3248	-> 659
/*      */     //   #3250	-> 666
/*      */     //   #3251	-> 687
/*      */     //   #3253	-> 697
/*      */     //   #3254	-> 711
/*      */     //   #3256	-> 725
/*      */     //   #3257	-> 729
/*      */     //   #3259	-> 733
/*      */     //   #3260	-> 757
/*      */     //   #3262	-> 772
/*      */     //   #3265	-> 779
/*      */     //   #3266	-> 789
/*      */     //   #3267	-> 796
/*      */     //   #3268	-> 806
/*      */     //   #3270	-> 816
/*      */     //   #3271	-> 820
/*      */     //   #3274	-> 827
/*      */     //   #3275	-> 862
/*      */     //   #3277	-> 877
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   331	3	9	fluidHeight	D
/*      */     //   343	128	9	fluidHeight	D
/*      */     //   364	107	11	inWaterAndHasFluidHeight	Z
/*      */     //   370	101	12	fluidJumpThreshold	D
/*      */     //   582	18	11	controller	Lnet/minecraft/world/entity/player/Player;
/*      */     //   687	92	11	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*      */     //   848	29	11	serverLevel	Lnet/minecraft/server/level/ServerLevel;
/*      */     //   0	878	0	this	Lnet/minecraft/world/entity/LivingEntity;
/*      */     //   97	781	1	movement	Lnet/minecraft/world/phys/Vec3;
/*      */     //   102	776	2	dx	D
/*      */     //   108	770	4	dy	D
/*      */     //   114	764	6	dz	D
/*      */     //   207	671	8	profiler	Lnet/minecraft/util/profiling/ProfilerFiller;
/*      */     //   513	365	9	beforeTravelBox	Lnet/minecraft/world/phys/AABB;
/*      */     //   537	341	10	input	Lnet/minecraft/world/phys/Vec3;
/*      */   }
/*      */   
/*      */   protected void applyInput() {
/* 3280 */     this.xxa *= 0.98F;
/* 3281 */     this.zza *= 0.98F;
/*      */   }
/*      */   
/*      */   public boolean isSensitiveToWater() {
/* 3286 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isJumping() {
/* 3290 */     return this.jumping;
/*      */   }
/*      */   
/*      */   protected void updateFallFlying() {
/* 3295 */     checkFallDistanceAccumulation();
/* 3297 */     if (!level().isClientSide()) {
/* 3298 */       if (!canGlide()) {
/* 3299 */         setSharedFlag(7, false);
/*      */         return;
/*      */       } 
/* 3303 */       int checkFallFlyTicks = this.fallFlyTicks + 1;
/* 3304 */       if (checkFallFlyTicks % 10 == 0) {
/* 3305 */         int freeFallInterval = checkFallFlyTicks / 10;
/* 3307 */         if (freeFallInterval % 2 == 0) {
/* 3310 */           List<EquipmentSlot> slotsWithGliders = EquipmentSlot.VALUES.stream().filter(slot -> canGlideUsing(getItemBySlot(slot), slot)).toList();
/* 3311 */           EquipmentSlot slotToDamage = (EquipmentSlot)Util.getRandom(slotsWithGliders, this.random);
/* 3313 */           getItemBySlot(slotToDamage).hurtAndBreak(1, this, slotToDamage);
/*      */         } 
/* 3316 */         gameEvent((Holder<GameEvent>)GameEvent.ELYTRA_GLIDE);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean canGlide() {
/* 3322 */     if (onGround() || isPassenger() || hasEffect(MobEffects.LEVITATION))
/* 3323 */       return false; 
/* 3325 */     for (EquipmentSlot slot : EquipmentSlot.VALUES) {
/* 3326 */       if (canGlideUsing(getItemBySlot(slot), slot))
/* 3327 */         return true; 
/*      */     } 
/* 3330 */     return false;
/*      */   }
/*      */   
/*      */   protected void serverAiStep() {}
/*      */   
/*      */   protected void pushEntities() {
/* 3337 */     List<Entity> pushableEntities = level().getPushableEntities(this, getBoundingBox());
/* 3338 */     if (pushableEntities.isEmpty())
/*      */       return; 
/* 3342 */     Level level = level();
/* 3342 */     if (level instanceof ServerLevel) {
/* 3342 */       ServerLevel serverLevel = (ServerLevel)level;
/* 3343 */       int maxCramming = ((Integer)serverLevel.getGameRules().get(GameRules.MAX_ENTITY_CRAMMING)).intValue();
/* 3344 */       if (maxCramming > 0 && pushableEntities.size() > maxCramming - 1 && this.random.nextInt(4) == 0) {
/* 3345 */         int count = 0;
/* 3346 */         for (Entity entity : pushableEntities) {
/* 3347 */           if (!entity.isPassenger())
/* 3348 */             count++; 
/*      */         } 
/* 3351 */         if (count > maxCramming - 1)
/* 3352 */           hurtServer(serverLevel, damageSources().cramming(), 6.0F); 
/*      */       } 
/*      */     } 
/* 3357 */     for (Entity entity : pushableEntities)
/* 3358 */       doPush(entity); 
/*      */   }
/*      */   
/*      */   protected void checkAutoSpinAttack(AABB old, AABB current) {
/* 3363 */     AABB minmax = old.minmax(current);
/* 3364 */     List<Entity> entities = level().getEntities(this, minmax);
/* 3365 */     if (!entities.isEmpty()) {
/* 3366 */       for (Entity entity : entities) {
/* 3367 */         if (entity instanceof LivingEntity) {
/* 3367 */           LivingEntity livingEntity = (LivingEntity)entity;
/* 3368 */           doAutoAttackOnTouch(livingEntity);
/* 3369 */           this.autoSpinAttackTicks = 0;
/* 3370 */           setDeltaMovement(getDeltaMovement().scale(-0.2D));
/*      */           break;
/*      */         } 
/*      */       } 
/* 3374 */     } else if (this.horizontalCollision) {
/* 3375 */       this.autoSpinAttackTicks = 0;
/*      */     } 
/* 3377 */     if (!level().isClientSide() && this.autoSpinAttackTicks <= 0) {
/* 3378 */       setLivingEntityFlag(4, false);
/* 3379 */       this.autoSpinAttackDmg = 0.0F;
/* 3380 */       this.autoSpinAttackItemStack = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void doPush(Entity entity) {
/* 3385 */     entity.push(this);
/*      */   }
/*      */   
/*      */   protected void doAutoAttackOnTouch(LivingEntity entity) {}
/*      */   
/*      */   public boolean isAutoSpinAttack() {
/* 3392 */     return ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & 0x4) != 0);
/*      */   }
/*      */   
/*      */   public void stopRiding() {
/* 3397 */     Entity oldVehicle = getVehicle();
/* 3398 */     super.stopRiding();
/* 3399 */     if (oldVehicle != null && oldVehicle != getVehicle() && !level().isClientSide())
/* 3400 */       dismountVehicle(oldVehicle); 
/*      */   }
/*      */   
/*      */   public void rideTick() {
/* 3406 */     super.rideTick();
/* 3407 */     resetFallDistance();
/*      */   }
/*      */   
/*      */   public InterpolationHandler getInterpolation() {
/* 3412 */     return this.interpolation;
/*      */   }
/*      */   
/*      */   public void lerpHeadTo(float yRot, int steps) {
/* 3417 */     this.lerpYHeadRot = yRot;
/* 3418 */     this.lerpHeadSteps = steps;
/*      */   }
/*      */   
/*      */   public void setJumping(boolean jump) {
/* 3422 */     this.jumping = jump;
/*      */   }
/*      */   
/*      */   public void onItemPickup(ItemEntity entity) {
/* 3426 */     Entity thrower = entity.getOwner();
/* 3427 */     if (thrower instanceof ServerPlayer) {
/* 3427 */       ServerPlayer serverPlayer = (ServerPlayer)thrower;
/* 3428 */       CriteriaTriggers.THROWN_ITEM_PICKED_UP_BY_ENTITY.trigger(serverPlayer, entity.getItem(), this);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void take(Entity entity, int orgCount) {
/* 3433 */     if (!entity.isRemoved() && !level().isClientSide() && (
/* 3434 */       entity instanceof ItemEntity || entity instanceof AbstractArrow || entity instanceof ExperienceOrb))
/* 3435 */       ((ServerLevel)level()).getChunkSource().sendToTrackingPlayers(entity, (Packet)new ClientboundTakeItemEntityPacket(entity.getId(), getId(), orgCount)); 
/*      */   }
/*      */   
/*      */   public boolean hasLineOfSight(Entity target) {
/* 3441 */     return hasLineOfSight(target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, target.getEyeY());
/*      */   }
/*      */   
/*      */   public boolean hasLineOfSight(Entity target, ClipContext.Block blockCollidingContext, ClipContext.Fluid fluidCollidingContext, double eyeHeight) {
/* 3446 */     if (target.level() != level())
/* 3447 */       return false; 
/* 3449 */     Vec3 from = new Vec3(getX(), getEyeY(), getZ());
/* 3450 */     Vec3 to = new Vec3(target.getX(), eyeHeight, target.getZ());
/* 3452 */     if (to.distanceTo(from) > 128.0D)
/* 3453 */       return false; 
/* 3455 */     return (level().clip(new ClipContext(from, to, blockCollidingContext, fluidCollidingContext, this)).getType() == HitResult.Type.MISS);
/*      */   }
/*      */   
/*      */   public float getViewYRot(float a) {
/* 3460 */     if (a == 1.0F)
/* 3461 */       return this.yHeadRot; 
/* 3463 */     return Mth.rotLerp(a, this.yHeadRotO, this.yHeadRot);
/*      */   }
/*      */   
/*      */   public float getAttackAnim(float a) {
/* 3467 */     float diff = this.attackAnim - this.oAttackAnim;
/* 3468 */     if (diff < 0.0F)
/* 3469 */       diff++; 
/* 3471 */     return this.oAttackAnim + diff * a;
/*      */   }
/*      */   
/*      */   public boolean isPickable() {
/* 3476 */     return !isRemoved();
/*      */   }
/*      */   
/*      */   public boolean isPushable() {
/* 3481 */     return (isAlive() && !isSpectator() && !onClimbable());
/*      */   }
/*      */   
/*      */   public float getYHeadRot() {
/* 3486 */     return this.yHeadRot;
/*      */   }
/*      */   
/*      */   public void setYHeadRot(float yHeadRot) {
/* 3491 */     this.yHeadRot = yHeadRot;
/*      */   }
/*      */   
/*      */   public void setYBodyRot(float yBodyRot) {
/* 3496 */     this.yBodyRot = yBodyRot;
/*      */   }
/*      */   
/*      */   public Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle portalArea) {
/* 3501 */     return resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, portalArea));
/*      */   }
/*      */   
/*      */   public static Vec3 resetForwardDirectionOfRelativePortalPosition(Vec3 offsets) {
/* 3506 */     return new Vec3(offsets.x, offsets.y, 0.0D);
/*      */   }
/*      */   
/*      */   public float getAbsorptionAmount() {
/* 3510 */     return this.absorptionAmount;
/*      */   }
/*      */   
/*      */   public final void setAbsorptionAmount(float absorptionAmount) {
/* 3514 */     internalSetAbsorptionAmount(Mth.clamp(absorptionAmount, 0.0F, getMaxAbsorption()));
/*      */   }
/*      */   
/*      */   protected void internalSetAbsorptionAmount(float absorptionAmount) {
/* 3518 */     this.absorptionAmount = absorptionAmount;
/*      */   }
/*      */   
/*      */   public void onEnterCombat() {}
/*      */   
/*      */   public void onLeaveCombat() {}
/*      */   
/*      */   protected void updateEffectVisibility() {
/* 3528 */     this.effectsDirty = true;
/*      */   }
/*      */   
/*      */   public boolean isUsingItem() {
/* 3534 */     return ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & 0x1) > 0);
/*      */   }
/*      */   
/*      */   public InteractionHand getUsedItemHand() {
/* 3538 */     return ((((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue() & 0x2) > 0) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
/*      */   }
/*      */   
/*      */   private void updatingUsingItem() {
/* 3542 */     if (isUsingItem())
/* 3544 */       if (ItemStack.isSameItem(getItemInHand(getUsedItemHand()), this.useItem)) {
/* 3545 */         this.useItem = getItemInHand(getUsedItemHand());
/* 3546 */         updateUsingItem(this.useItem);
/*      */       } else {
/* 3548 */         stopUsingItem();
/*      */       }  
/*      */   }
/*      */   
/*      */   private ItemEntity createItemStackToDrop(ItemStack itemStack, boolean randomly, boolean thrownFromHand) {
/* 3554 */     if (itemStack.isEmpty())
/* 3555 */       return null; 
/* 3557 */     double yHandPos = getEyeY() - 0.30000001192092896D;
/* 3558 */     ItemEntity entity = new ItemEntity(level(), getX(), yHandPos, getZ(), itemStack);
/* 3559 */     entity.setPickUpDelay(40);
/* 3561 */     if (thrownFromHand)
/* 3562 */       entity.setThrower(this); 
/* 3565 */     if (randomly) {
/* 3566 */       float pow = this.random.nextFloat() * 0.5F;
/* 3567 */       float dir = this.random.nextFloat() * 6.2831855F;
/* 3568 */       entity.setDeltaMovement((
/* 3569 */           -Mth.sin(dir) * pow), 0.20000000298023224D, (
/*      */           
/* 3571 */           Mth.cos(dir) * pow));
/*      */     } else {
/* 3574 */       float pow = 0.3F;
/* 3575 */       float sinX = Mth.sin((getXRot() * 0.017453292F));
/* 3576 */       float cosX = Mth.cos((getXRot() * 0.017453292F));
/* 3577 */       float sinY = Mth.sin((getYRot() * 0.017453292F));
/* 3578 */       float cosY = Mth.cos((getYRot() * 0.017453292F));
/* 3580 */       float dir = this.random.nextFloat() * 6.2831855F;
/* 3581 */       float pow2 = 0.02F * this.random.nextFloat();
/* 3583 */       entity.setDeltaMovement((-sinY * cosX * 0.3F) + 
/* 3584 */           Math.cos(dir) * pow2, (-sinX * 0.3F + 0.1F + (this.random
/* 3585 */           .nextFloat() - this.random.nextFloat()) * 0.1F), (cosY * cosX * 0.3F) + 
/* 3586 */           Math.sin(dir) * pow2);
/*      */     } 
/* 3589 */     return entity;
/*      */   }
/*      */   
/*      */   protected void updateUsingItem(ItemStack useItem) {
/* 3593 */     useItem.onUseTick(level(), this, getUseItemRemainingTicks());
/* 3594 */     if (--this.useItemRemaining == 0 && !level().isClientSide() && !useItem.useOnRelease())
/* 3595 */       completeUsingItem(); 
/*      */   }
/*      */   
/*      */   private void updateSwimAmount() {
/* 3600 */     this.swimAmountO = this.swimAmount;
/* 3601 */     if (isVisuallySwimming()) {
/* 3602 */       this.swimAmount = Math.min(1.0F, this.swimAmount + 0.09F);
/*      */     } else {
/* 3604 */       this.swimAmount = Math.max(0.0F, this.swimAmount - 0.09F);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void setLivingEntityFlag(int flag, boolean value) {
/* 3609 */     int currentFlags = ((Byte)this.entityData.get(DATA_LIVING_ENTITY_FLAGS)).byteValue();
/* 3610 */     if (value) {
/* 3611 */       currentFlags |= flag;
/*      */     } else {
/* 3613 */       currentFlags &= flag ^ 0xFFFFFFFF;
/*      */     } 
/* 3615 */     this.entityData.set(DATA_LIVING_ENTITY_FLAGS, Byte.valueOf((byte)currentFlags));
/*      */   }
/*      */   
/*      */   public void startUsingItem(InteractionHand hand) {
/* 3619 */     ItemStack itemStack = getItemInHand(hand);
/* 3620 */     if (itemStack.isEmpty() || isUsingItem())
/*      */       return; 
/* 3624 */     this.useItem = itemStack;
/* 3625 */     this.useItemRemaining = itemStack.getUseDuration(this);
/* 3627 */     if (!level().isClientSide()) {
/* 3628 */       setLivingEntityFlag(1, true);
/* 3629 */       setLivingEntityFlag(2, (hand == InteractionHand.OFF_HAND));
/* 3630 */       this.useItem.causeUseVibration(this, GameEvent.ITEM_INTERACT_START);
/* 3631 */       if (this.useItem.has(DataComponents.KINETIC_WEAPON))
/* 3632 */         this.recentKineticEnemies = (Object2LongMap<Entity>)new Object2LongOpenHashMap(); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 3639 */     super.onSyncedDataUpdated(accessor);
/* 3641 */     if (SLEEPING_POS_ID.equals(accessor)) {
/* 3642 */       if (level().isClientSide())
/* 3644 */         getSleepingPos().ifPresent(this::setPosToBed); 
/* 3646 */     } else if (DATA_LIVING_ENTITY_FLAGS.equals(accessor) && level().isClientSide()) {
/* 3647 */       if (isUsingItem() && this.useItem.isEmpty()) {
/* 3648 */         this.useItem = getItemInHand(getUsedItemHand());
/* 3649 */         if (!this.useItem.isEmpty())
/* 3650 */           this.useItemRemaining = this.useItem.getUseDuration(this); 
/* 3652 */       } else if (!isUsingItem() && !this.useItem.isEmpty()) {
/* 3653 */         this.useItem = ItemStack.EMPTY;
/* 3654 */         this.useItemRemaining = 0;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor anchor, Vec3 pos) {
/* 3661 */     super.lookAt(anchor, pos);
/* 3662 */     this.yHeadRotO = this.yHeadRot;
/* 3663 */     this.yBodyRot = this.yHeadRot;
/* 3664 */     this.yBodyRotO = this.yBodyRot;
/*      */   }
/*      */   
/*      */   public float getPreciseBodyRotation(float partial) {
/* 3669 */     return Mth.lerp(partial, this.yBodyRotO, this.yBodyRot);
/*      */   }
/*      */   
/*      */   public void spawnItemParticles(ItemStack itemStack, int count) {
/* 3673 */     if (itemStack.isEmpty())
/*      */       return; 
/* 3677 */     ItemParticleOption breakParticle = new ItemParticleOption(ParticleTypes.ITEM, ItemStackTemplate.fromNonEmptyStack(itemStack));
/* 3678 */     for (int i = 0; i < count; i++) {
/* 3679 */       Vec3 d = new Vec3((this.random.nextFloat() - 0.5D) * 0.1D, this.random.nextFloat() * 0.1D + 0.1D, 0.0D);
/* 3680 */       d = d.xRot(-getXRot() * 0.017453292F);
/* 3681 */       d = d.yRot(-getYRot() * 0.017453292F);
/* 3683 */       double y1 = -this.random.nextFloat() * 0.6D - 0.3D;
/* 3684 */       Vec3 p = new Vec3((this.random.nextFloat() - 0.5D) * 0.3D, y1, 0.6D);
/* 3685 */       p = p.xRot(-getXRot() * 0.017453292F);
/* 3686 */       p = p.yRot(-getYRot() * 0.017453292F);
/* 3687 */       p = p.add(getX(), getEyeY(), getZ());
/* 3688 */       level().addParticle((ParticleOptions)breakParticle, p.x, p.y, p.z, d.x, d.y + 0.05D, d.z);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void completeUsingItem() {
/* 3693 */     if (level().isClientSide() && !isUsingItem())
/*      */       return; 
/* 3697 */     InteractionHand hand = getUsedItemHand();
/* 3698 */     if (!this.useItem.equals(getItemInHand(hand))) {
/* 3699 */       releaseUsingItem();
/*      */       return;
/*      */     } 
/* 3703 */     if (!this.useItem.isEmpty() && isUsingItem()) {
/* 3704 */       ItemStack result = this.useItem.finishUsingItem(level(), this);
/* 3705 */       if (result != this.useItem)
/* 3706 */         setItemInHand(hand, result); 
/* 3708 */       stopUsingItem();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void handleExtraItemsCreatedOnUse(ItemStack extraCreatedRemainder) {}
/*      */   
/*      */   public ItemStack getUseItem() {
/* 3716 */     return this.useItem;
/*      */   }
/*      */   
/*      */   public int getUseItemRemainingTicks() {
/* 3720 */     return this.useItemRemaining;
/*      */   }
/*      */   
/*      */   public int getTicksUsingItem() {
/* 3724 */     if (isUsingItem())
/* 3725 */       return this.useItem.getUseDuration(this) - getUseItemRemainingTicks(); 
/* 3727 */     return 0;
/*      */   }
/*      */   
/*      */   public float getTicksUsingItem(float partialTicks) {
/* 3731 */     if (!isUsingItem())
/* 3732 */       return 0.0F; 
/* 3734 */     return getTicksUsingItem() + partialTicks;
/*      */   }
/*      */   
/*      */   public void releaseUsingItem() {
/* 3739 */     ItemStack itemInUsedHand = getItemInHand(getUsedItemHand());
/* 3740 */     if (!this.useItem.isEmpty() && ItemStack.isSameItem(itemInUsedHand, this.useItem)) {
/* 3741 */       this.useItem = itemInUsedHand;
/* 3742 */       this.useItem.releaseUsing(level(), this, getUseItemRemainingTicks());
/* 3743 */       if (this.useItem.useOnRelease())
/* 3744 */         updatingUsingItem(); 
/*      */     } 
/* 3747 */     stopUsingItem();
/*      */   }
/*      */   
/*      */   public void stopUsingItem() {
/* 3751 */     if (!level().isClientSide()) {
/* 3752 */       boolean wasUsingItem = isUsingItem();
/* 3753 */       this.recentKineticEnemies = null;
/* 3754 */       setLivingEntityFlag(1, false);
/* 3755 */       if (wasUsingItem)
/* 3756 */         this.useItem.causeUseVibration(this, GameEvent.ITEM_INTERACT_FINISH); 
/*      */     } 
/* 3759 */     this.useItem = ItemStack.EMPTY;
/* 3760 */     this.useItemRemaining = 0;
/*      */   }
/*      */   
/*      */   public boolean isBlocking() {
/* 3764 */     return (getItemBlockingWith() != null);
/*      */   }
/*      */   
/*      */   public ItemStack getItemBlockingWith() {
/* 3768 */     if (!isUsingItem())
/* 3769 */       return null; 
/* 3771 */     BlocksAttacks blocksAttacks = (BlocksAttacks)this.useItem.get(DataComponents.BLOCKS_ATTACKS);
/* 3772 */     if (blocksAttacks != null) {
/* 3773 */       int elapsedTicks = this.useItem.getItem().getUseDuration(this.useItem, this) - this.useItemRemaining;
/* 3774 */       if (elapsedTicks >= blocksAttacks.blockDelayTicks())
/* 3775 */         return this.useItem; 
/*      */     } 
/* 3778 */     return null;
/*      */   }
/*      */   
/*      */   public boolean isSuppressingSlidingDownLadder() {
/* 3782 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   public boolean isFallFlying() {
/* 3786 */     return getSharedFlag(7);
/*      */   }
/*      */   
/*      */   public boolean isVisuallySwimming() {
/* 3793 */     return (super.isVisuallySwimming() || (!isFallFlying() && hasPose(Pose.FALL_FLYING)));
/*      */   }
/*      */   
/*      */   public int getFallFlyingTicks() {
/* 3797 */     return this.fallFlyTicks;
/*      */   }
/*      */   
/*      */   public boolean randomTeleport(double xx, double yy, double zz, boolean showParticles) {
/* 3801 */     double xo = getX();
/* 3802 */     double yo = getY();
/* 3803 */     double zo = getZ();
/* 3805 */     double y = yy;
/* 3806 */     boolean ok = false;
/* 3807 */     BlockPos pos = BlockPos.containing(xx, y, zz);
/* 3808 */     Level level = level();
/* 3810 */     if (level.hasChunkAt(pos)) {
/* 3812 */       boolean landed = false;
/* 3813 */       while (!landed && pos.getY() > level.getMinY()) {
/* 3814 */         BlockPos below = pos.below();
/* 3815 */         BlockState state = level.getBlockState(below);
/* 3816 */         if (state.blocksMotion()) {
/* 3817 */           landed = true;
/*      */           continue;
/*      */         } 
/* 3819 */         y--;
/* 3820 */         pos = below;
/*      */       } 
/* 3823 */       if (landed) {
/* 3824 */         teleportTo(xx, y, zz);
/* 3825 */         if (level.noCollision(this) && !level.containsAnyLiquid(getBoundingBox()))
/* 3826 */           ok = true; 
/*      */       } 
/*      */     } 
/* 3831 */     if (!ok) {
/* 3832 */       teleportTo(xo, yo, zo);
/* 3833 */       return false;
/*      */     } 
/* 3836 */     if (showParticles)
/* 3837 */       level.broadcastEntityEvent(this, (byte)46); 
/* 3840 */     LivingEntity livingEntity = this;
/* 3840 */     if (livingEntity instanceof PathfinderMob) {
/* 3840 */       PathfinderMob pathfinderMob = (PathfinderMob)livingEntity;
/* 3841 */       pathfinderMob.getNavigation().stop();
/*      */     } 
/* 3844 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isAffectedByPotions() {
/* 3848 */     return !isDeadOrDying();
/*      */   }
/*      */   
/*      */   public boolean attackable() {
/* 3852 */     return true;
/*      */   }
/*      */   
/*      */   public void setRecordPlayingNearby(BlockPos jukebox, boolean isPlaying) {}
/*      */   
/*      */   public boolean canPickUpLoot() {
/* 3859 */     return false;
/*      */   }
/*      */   
/*      */   public final EntityDimensions getDimensions(Pose pose) {
/* 3864 */     return (pose == Pose.SLEEPING) ? SLEEPING_DIMENSIONS : getDefaultDimensions(pose).scale(getScale());
/*      */   }
/*      */   
/*      */   protected EntityDimensions getDefaultDimensions(Pose pose) {
/* 3868 */     return getType().getDimensions().scale(getAgeScale());
/*      */   }
/*      */   
/*      */   public ImmutableList<Pose> getDismountPoses() {
/* 3872 */     return ImmutableList.of(Pose.STANDING);
/*      */   }
/*      */   
/*      */   public AABB getLocalBoundsForPose(Pose pose) {
/* 3876 */     EntityDimensions dimensions = getDimensions(pose);
/* 3877 */     return new AABB((
/* 3878 */         -dimensions.width() / 2.0F), 0.0D, (-dimensions.width() / 2.0F), (dimensions
/* 3879 */         .width() / 2.0F), dimensions.height(), (dimensions.width() / 2.0F));
/*      */   }
/*      */   
/*      */   protected boolean wouldNotSuffocateAtTargetPose(Pose pose) {
/* 3884 */     AABB targetBB = getDimensions(pose).makeBoundingBox(position());
/* 3885 */     return level().noBlockCollision(this, targetBB);
/*      */   }
/*      */   
/*      */   public boolean canUsePortal(boolean ignorePassenger) {
/* 3890 */     return (super.canUsePortal(ignorePassenger) && !isSleeping());
/*      */   }
/*      */   
/*      */   public Optional<BlockPos> getSleepingPos() {
/* 3894 */     return (Optional<BlockPos>)this.entityData.get(SLEEPING_POS_ID);
/*      */   }
/*      */   
/*      */   public void setSleepingPos(BlockPos bedPosition) {
/* 3898 */     this.entityData.set(SLEEPING_POS_ID, Optional.of(bedPosition));
/*      */   }
/*      */   
/*      */   public void clearSleepingPos() {
/* 3902 */     this.entityData.set(SLEEPING_POS_ID, Optional.empty());
/*      */   }
/*      */   
/*      */   public boolean isSleeping() {
/* 3906 */     return getSleepingPos().isPresent();
/*      */   }
/*      */   
/*      */   public void startSleeping(BlockPos bedPosition) {
/* 3910 */     if (isPassenger())
/* 3911 */       stopRiding(); 
/* 3914 */     BlockState blockState = level().getBlockState(bedPosition);
/* 3915 */     if (blockState.getBlock() instanceof BedBlock)
/* 3916 */       level().setBlock(bedPosition, (BlockState)blockState.setValue((Property)BedBlock.OCCUPIED, Boolean.valueOf(true)), 3); 
/* 3919 */     setPose(Pose.SLEEPING);
/* 3920 */     setPosToBed(bedPosition);
/* 3921 */     setSleepingPos(bedPosition);
/* 3922 */     setDeltaMovement(Vec3.ZERO);
/* 3923 */     this.needsSync = true;
/*      */   }
/*      */   
/*      */   private void setPosToBed(BlockPos bedPosition) {
/* 3927 */     setPos(bedPosition.getX() + 0.5D, bedPosition.getY() + 0.6875D, bedPosition.getZ() + 0.5D);
/*      */   }
/*      */   
/*      */   private boolean checkBedExists() {
/* 3931 */     return ((Boolean)getSleepingPos().<Boolean>map(bedPosition -> Boolean.valueOf(level().getBlockState(bedPosition).getBlock() instanceof BedBlock)).orElse(Boolean.valueOf(false))).booleanValue();
/*      */   }
/*      */   
/*      */   public void stopSleeping() {
/* 3935 */     Objects.requireNonNull(level());
/* 3935 */     getSleepingPos().filter(level()::hasChunkAt).ifPresent(bedPosition -> {
/*      */           BlockState state = level().getBlockState(bedPosition);
/*      */           if (state.getBlock() instanceof BedBlock) {
/*      */             Direction facing = (Direction)state.getValue((Property)BedBlock.FACING);
/*      */             level().setBlock(bedPosition, (BlockState)state.setValue((Property)BedBlock.OCCUPIED, Boolean.valueOf(false)), 3);
/*      */             Vec3 standUp = BedBlock.findStandUpPosition(getType(), (CollisionGetter)level(), bedPosition, facing, getYRot()).orElseGet(());
/*      */             Vec3 lookDirection = Vec3.atBottomCenterOf((Vec3i)bedPosition).subtract(standUp).normalize();
/*      */             float yaw = (float)Mth.wrapDegrees(Mth.atan2(lookDirection.z, lookDirection.x) * 57.2957763671875D - 90.0D);
/*      */             setPos(standUp.x, standUp.y, standUp.z);
/*      */             setYRot(yaw);
/*      */             setXRot(0.0F);
/*      */           } 
/*      */         });
/* 3955 */     Vec3 pos = position();
/* 3956 */     setPose(Pose.STANDING);
/* 3957 */     setPos(pos.x, pos.y, pos.z);
/* 3958 */     clearSleepingPos();
/*      */   }
/*      */   
/*      */   public Direction getBedOrientation() {
/* 3962 */     BlockPos bedPos = getSleepingPos().orElse(null);
/* 3963 */     return (bedPos != null) ? BedBlock.getBedOrientation((BlockGetter)level(), bedPos) : null;
/*      */   }
/*      */   
/*      */   public boolean isInWall() {
/* 3968 */     return (!isSleeping() && super.isInWall());
/*      */   }
/*      */   
/*      */   public ItemStack getProjectile(ItemStack heldWeapon) {
/* 3972 */     return ItemStack.EMPTY;
/*      */   }
/*      */   
/*      */   private static byte entityEventForEquipmentBreak(EquipmentSlot equipmentSlot) {
/* 3976 */     switch (equipmentSlot) {
/*      */       default:
/* 3976 */         throw new MatchException(null, null);
/*      */       case MAINHAND:
/*      */       
/*      */       case OFFHAND:
/*      */       
/*      */       case HEAD:
/*      */       
/*      */       case CHEST:
/*      */       
/*      */       case FEET:
/*      */       
/*      */       case LEGS:
/*      */       
/*      */       case BODY:
/*      */       
/*      */       case SADDLE:
/*      */         break;
/*      */     } 
/* 3976 */     return 
/*      */       
/* 3984 */       68;
/*      */   }
/*      */   
/*      */   public void onEquippedItemBroken(Item brokenItem, EquipmentSlot inSlot) {
/* 3989 */     level().broadcastEntityEvent(this, entityEventForEquipmentBreak(inSlot));
/* 3990 */     stopLocationBasedEffects(getItemBySlot(inSlot), inSlot, this.attributes);
/*      */   }
/*      */   
/*      */   private void stopLocationBasedEffects(ItemStack previous, EquipmentSlot inSlot, AttributeMap attributes) {
/* 3994 */     previous.forEachModifier(inSlot, (attribute, modifier) -> {
/*      */           AttributeInstance instance = attributes.getInstance(attribute);
/*      */           if (instance != null)
/*      */             instance.removeModifier(modifier); 
/*      */         });
/* 4000 */     EnchantmentHelper.stopLocationBasedEffects(previous, this, inSlot);
/*      */   }
/*      */   
/*      */   public final boolean canEquipWithDispenser(ItemStack itemStack) {
/* 4004 */     if (!isAlive() || isSpectator())
/* 4005 */       return false; 
/* 4007 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 4008 */     if (equippable == null || !equippable.dispensable())
/* 4009 */       return false; 
/* 4011 */     EquipmentSlot slot = equippable.slot();
/* 4012 */     if (!canUseSlot(slot) || !equippable.canBeEquippedBy(typeHolder()))
/* 4013 */       return false; 
/* 4015 */     return (getItemBySlot(slot).isEmpty() && canDispenserEquipIntoSlot(slot));
/*      */   }
/*      */   
/*      */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/* 4019 */     return true;
/*      */   }
/*      */   
/*      */   public EquipmentSlot getEquipmentSlotForItem(ItemStack itemStack) {
/* 4023 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 4024 */     if (equippable != null && canUseSlot(equippable.slot()))
/* 4025 */       return equippable.slot(); 
/* 4027 */     return EquipmentSlot.MAINHAND;
/*      */   }
/*      */   
/*      */   public boolean isEquippableInSlot(ItemStack itemStack, EquipmentSlot slot) {
/* 4031 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 4032 */     if (equippable == null)
/* 4033 */       return (slot == EquipmentSlot.MAINHAND && canUseSlot(EquipmentSlot.MAINHAND)); 
/* 4035 */     return (slot == equippable.slot() && canUseSlot(equippable.slot()) && equippable.canBeEquippedBy(typeHolder()));
/*      */   }
/*      */   
/*      */   private static SlotAccess createEquipmentSlotAccess(LivingEntity entity, EquipmentSlot equipmentSlot) {
/* 4039 */     if (equipmentSlot == EquipmentSlot.HEAD || equipmentSlot == EquipmentSlot.MAINHAND || equipmentSlot == EquipmentSlot.OFFHAND)
/* 4040 */       return SlotAccess.forEquipmentSlot(entity, equipmentSlot); 
/* 4042 */     return SlotAccess.forEquipmentSlot(entity, equipmentSlot, stack -> (stack.isEmpty() || entity.getEquipmentSlotForItem(stack) == equipmentSlot));
/*      */   }
/*      */   
/*      */   private static EquipmentSlot getEquipmentSlot(int slot) {
/* 4047 */     if (slot == 100 + EquipmentSlot.HEAD.getIndex())
/* 4048 */       return EquipmentSlot.HEAD; 
/* 4050 */     if (slot == 100 + EquipmentSlot.CHEST.getIndex())
/* 4051 */       return EquipmentSlot.CHEST; 
/* 4053 */     if (slot == 100 + EquipmentSlot.LEGS.getIndex())
/* 4054 */       return EquipmentSlot.LEGS; 
/* 4056 */     if (slot == 100 + EquipmentSlot.FEET.getIndex())
/* 4057 */       return EquipmentSlot.FEET; 
/* 4059 */     if (slot == 98)
/* 4060 */       return EquipmentSlot.MAINHAND; 
/* 4062 */     if (slot == 99)
/* 4063 */       return EquipmentSlot.OFFHAND; 
/* 4065 */     if (slot == 105)
/* 4066 */       return EquipmentSlot.BODY; 
/* 4068 */     if (slot == 106)
/* 4069 */       return EquipmentSlot.SADDLE; 
/* 4071 */     return null;
/*      */   }
/*      */   
/*      */   public SlotAccess getSlot(int slot) {
/* 4076 */     EquipmentSlot equipmentSlot = getEquipmentSlot(slot);
/* 4078 */     if (equipmentSlot != null)
/* 4079 */       return createEquipmentSlotAccess(this, equipmentSlot); 
/* 4082 */     return super.getSlot(slot);
/*      */   }
/*      */   
/*      */   public boolean canFreeze() {
/* 4087 */     if (isSpectator())
/* 4088 */       return false; 
/* 4090 */     for (EquipmentSlot slot : EquipmentSlotGroup.ARMOR) {
/* 4091 */       if (getItemBySlot(slot).is(ItemTags.FREEZE_IMMUNE_WEARABLES))
/* 4092 */         return false; 
/*      */     } 
/* 4095 */     return super.canFreeze();
/*      */   }
/*      */   
/*      */   public boolean isCurrentlyGlowing() {
/* 4100 */     return ((!level().isClientSide() && hasEffect(MobEffects.GLOWING)) || super.isCurrentlyGlowing());
/*      */   }
/*      */   
/*      */   public float getVisualRotationYInDegrees() {
/* 4105 */     return this.yBodyRot;
/*      */   }
/*      */   
/*      */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 4110 */     double x = packet.getX();
/* 4111 */     double y = packet.getY();
/* 4112 */     double z = packet.getZ();
/* 4113 */     float yRot = packet.getYRot();
/* 4114 */     float xRot = packet.getXRot();
/* 4115 */     syncPacketPositionCodec(x, y, z);
/* 4116 */     this.yBodyRot = packet.getYHeadRot();
/* 4117 */     this.yHeadRot = packet.getYHeadRot();
/* 4118 */     this.yBodyRotO = this.yBodyRot;
/* 4119 */     this.yHeadRotO = this.yHeadRot;
/* 4121 */     setId(packet.getId());
/* 4122 */     setUUID(packet.getUUID());
/* 4123 */     absSnapTo(x, y, z, yRot, xRot);
/* 4124 */     setDeltaMovement(packet.getMovement());
/*      */   }
/*      */   
/*      */   public float getSecondsToDisableBlocking() {
/* 4128 */     ItemStack weaponItem = getWeaponItem();
/* 4129 */     Weapon weapon = (Weapon)weaponItem.get(DataComponents.WEAPON);
/* 4130 */     return (weapon != null && weaponItem == getActiveItem()) ? weapon.disableBlockingForSeconds() : 0.0F;
/*      */   }
/*      */   
/*      */   public float maxUpStep() {
/* 4135 */     float maxUpStep = (float)getAttributeValue(Attributes.STEP_HEIGHT);
/* 4136 */     return (getControllingPassenger() instanceof Player) ? Math.max(maxUpStep, 1.0F) : maxUpStep;
/*      */   }
/*      */   
/*      */   public Vec3 getPassengerRidingPosition(Entity passenger) {
/* 4141 */     return position().add(getPassengerAttachmentPoint(passenger, getDimensions(getPose()), getScale() * getAgeScale()));
/*      */   }
/*      */   
/*      */   protected void lerpHeadRotationStep(int lerpHeadSteps, double targetYHeadRot) {
/* 4145 */     this.yHeadRot = (float)Mth.rotLerp(1.0D / lerpHeadSteps, this.yHeadRot, targetYHeadRot);
/*      */   }
/*      */   
/*      */   public void igniteForTicks(int numberOfTicks) {
/* 4150 */     super.igniteForTicks(Mth.ceil(numberOfTicks * getAttributeValue(Attributes.BURNING_TIME)));
/*      */   }
/*      */   
/*      */   public boolean hasInfiniteMaterials() {
/* 4154 */     return false;
/*      */   }
/*      */   
/*      */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
/* 4158 */     return (isInvulnerableToBase(source) || 
/* 4159 */       EnchantmentHelper.isImmuneToDamage(level, this, source));
/*      */   }
/*      */   
/*      */   public static boolean canGlideUsing(ItemStack itemStack, EquipmentSlot slot) {
/* 4163 */     if (!itemStack.has(DataComponents.GLIDER))
/* 4164 */       return false; 
/* 4166 */     Equippable equippable = (Equippable)itemStack.get(DataComponents.EQUIPPABLE);
/* 4167 */     return (equippable != null && slot == equippable.slot() && !itemStack.nextDamageWillBreak());
/*      */   }
/*      */   
/*      */   @VisibleForTesting
/*      */   public int getLastHurtByPlayerMemoryTime() {
/* 4172 */     return this.lastHurtByPlayerMemoryTime;
/*      */   }
/*      */   
/*      */   public boolean isTransmittingWaypoint() {
/* 4177 */     return (getAttributeValue(Attributes.WAYPOINT_TRANSMIT_RANGE) > 0.0D);
/*      */   }
/*      */   
/*      */   public Optional<WaypointTransmitter.Connection> makeWaypointConnectionWith(ServerPlayer player) {
/* 4182 */     if (this.firstTick || player == this)
/* 4183 */       return Optional.empty(); 
/* 4186 */     if (WaypointTransmitter.doesSourceIgnoreReceiver(this, player))
/* 4187 */       return Optional.empty(); 
/* 4190 */     Waypoint.Icon icon = this.locatorBarIcon.cloneAndAssignStyle(this);
/* 4192 */     if (WaypointTransmitter.isReallyFar(this, player))
/* 4193 */       return (Optional)Optional.of(new WaypointTransmitter.EntityAzimuthConnection(this, icon, player)); 
/* 4196 */     if (!WaypointTransmitter.isChunkVisible(chunkPosition(), player))
/* 4197 */       return (Optional)Optional.of(new WaypointTransmitter.EntityChunkConnection(this, icon, player)); 
/* 4200 */     return (Optional)Optional.of(new WaypointTransmitter.EntityBlockConnection(this, icon, player));
/*      */   }
/*      */   
/*      */   public Waypoint.Icon waypointIcon() {
/* 4205 */     return this.locatorBarIcon;
/*      */   }
/*      */   
/*      */   public DamageSource createDamageSource() {
/* 4209 */     return damageSources().mobAttack(this);
/*      */   }
/*      */   
/*      */   public abstract HumanoidArm getMainArm();
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\LivingEntity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */