/*      */ package net.minecraft.world.entity;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.collect.UnmodifiableIterator;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Codec;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*      */ import it.unimi.dsi.fastutil.doubles.DoubleListIterator;
/*      */ import it.unimi.dsi.fastutil.floats.FloatArraySet;
/*      */ import it.unimi.dsi.fastutil.floats.FloatArrays;
/*      */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*      */ import it.unimi.dsi.fastutil.longs.LongSet;
/*      */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*      */ import java.lang.annotation.ElementType;
/*      */ import java.lang.annotation.Retention;
/*      */ import java.lang.annotation.RetentionPolicy;
/*      */ import java.lang.annotation.Target;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.function.BiConsumer;
/*      */ import java.util.function.Predicate;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.CrashReport;
/*      */ import net.minecraft.CrashReportCategory;
/*      */ import net.minecraft.ReportedException;
/*      */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*      */ import net.minecraft.commands.CommandSource;
/*      */ import net.minecraft.commands.CommandSourceStack;
/*      */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.HolderLookup;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.RegistryAccess;
/*      */ import net.minecraft.core.SectionPos;
/*      */ import net.minecraft.core.TypedInstance;
/*      */ import net.minecraft.core.UUIDUtil;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.component.DataComponentGetter;
/*      */ import net.minecraft.core.component.DataComponentType;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.core.particles.BlockParticleOption;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.ComponentSerialization;
/*      */ import net.minecraft.network.chat.HoverEvent;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.Style;
/*      */ import net.minecraft.network.chat.numbers.NumberFormat;
/*      */ import net.minecraft.network.chat.numbers.StyledFormat;
/*      */ import net.minecraft.network.protocol.Packet;
/*      */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*      */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*      */ import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
/*      */ import net.minecraft.network.protocol.game.VecDeltaCodec;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SyncedDataHolder;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.server.level.ServerEntity;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.server.level.ServerPlayer;
/*      */ import net.minecraft.server.level.TicketType;
/*      */ import net.minecraft.server.permissions.PermissionSet;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.DamageTypeTags;
/*      */ import net.minecraft.tags.EntityTypeTags;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.BlockUtil;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.ProblemReporter;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.debug.DebugEntityBlockIntersection;
/*      */ import net.minecraft.util.debug.DebugSubscriptions;
/*      */ import net.minecraft.util.debug.DebugValueSource;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.util.profiling.ProfilerFiller;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.Nameable;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.damagesource.DamageSources;
/*      */ import net.minecraft.world.entity.item.ItemEntity;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.entity.projectile.Projectile;
/*      */ import net.minecraft.world.entity.projectile.ProjectileDeflection;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.component.CustomData;
/*      */ import net.minecraft.world.level.BlockGetter;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.ClipContext;
/*      */ import net.minecraft.world.level.Explosion;
/*      */ import net.minecraft.world.level.ItemLike;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelHeightAccessor;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.HoneyBlock;
/*      */ import net.minecraft.world.level.block.Mirror;
/*      */ import net.minecraft.world.level.block.Portal;
/*      */ import net.minecraft.world.level.block.RenderShape;
/*      */ import net.minecraft.world.level.block.Rotation;
/*      */ import net.minecraft.world.level.block.SoundType;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.level.entity.EntityAccess;
/*      */ import net.minecraft.world.level.entity.EntityInLevelCallback;
/*      */ import net.minecraft.world.level.gameevent.DynamicGameEventListener;
/*      */ import net.minecraft.world.level.gameevent.GameEvent;
/*      */ import net.minecraft.world.level.levelgen.Heightmap;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.material.FluidState;
/*      */ import net.minecraft.world.level.material.PushReaction;
/*      */ import net.minecraft.world.level.portal.PortalShape;
/*      */ import net.minecraft.world.level.portal.TeleportTransition;
/*      */ import net.minecraft.world.level.storage.TagValueInput;
/*      */ import net.minecraft.world.level.storage.TagValueOutput;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.level.storage.loot.LootTable;
/*      */ import net.minecraft.world.phys.AABB;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.phys.Vec2;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import net.minecraft.world.phys.shapes.BooleanOp;
/*      */ import net.minecraft.world.phys.shapes.CollisionContext;
/*      */ import net.minecraft.world.phys.shapes.Shapes;
/*      */ import net.minecraft.world.phys.shapes.VoxelShape;
/*      */ import net.minecraft.world.scores.DisplaySlot;
/*      */ import net.minecraft.world.scores.Objective;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*      */ import net.minecraft.world.scores.ScoreHolder;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.scores.Team;
/*      */ import net.minecraft.world.scores.TeamColor;
/*      */ import net.minecraft.world.waypoints.WaypointTransmitter;
/*      */ import org.jetbrains.annotations.Contract;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public abstract class Entity implements Nameable, EntityAccess, ScoreHolder, SyncedDataHolder, DataComponentGetter, ItemOwner, SlotProvider, DebugValueSource, TypedInstance<EntityType<?>> {
/*  166 */   private static final Logger LOGGER = LogUtils.getLogger();
/*      */   
/*      */   public static final String TAG_ID = "id";
/*      */   
/*      */   public static final String TAG_UUID = "UUID";
/*      */   
/*      */   public static final String TAG_PASSENGERS = "Passengers";
/*      */   
/*      */   public static final String TAG_DATA = "data";
/*      */   
/*      */   public static final String TAG_POS = "Pos";
/*      */   
/*      */   public static final String TAG_MOTION = "Motion";
/*      */   
/*      */   public static final String TAG_ROTATION = "Rotation";
/*      */   
/*      */   public static final String TAG_PORTAL_COOLDOWN = "PortalCooldown";
/*      */   
/*      */   public static final String TAG_NO_GRAVITY = "NoGravity";
/*      */   
/*      */   public static final String TAG_AIR = "Air";
/*      */   
/*      */   public static final String TAG_ON_GROUND = "OnGround";
/*      */   
/*      */   public static final String TAG_FALL_DISTANCE = "fall_distance";
/*      */   
/*      */   public static final String TAG_FIRE = "Fire";
/*      */   
/*      */   public static final String TAG_SILENT = "Silent";
/*      */   
/*      */   public static final String TAG_GLOWING = "Glowing";
/*      */   
/*      */   public static final String TAG_INVULNERABLE = "Invulnerable";
/*      */   
/*      */   public static final String TAG_CUSTOM_NAME = "CustomName";
/*      */   
/*      */   public static final int INVALID_ENTITY_ID = 0;
/*      */   
/*      */   public static final int CONTENTS_SLOT_INDEX = 0;
/*      */   
/*      */   public static final int BOARDING_COOLDOWN = 60;
/*      */   
/*      */   public static final int TOTAL_AIR_SUPPLY = 300;
/*      */   
/*      */   public static final int MAX_ENTITY_TAG_COUNT = 1024;
/*      */   
/*  193 */   private static final Codec<List<String>> TAG_LIST_CODEC = Codec.STRING.sizeLimitedListOf(1024);
/*      */   
/*      */   public static final double DEFAULT_NAME_TAG_DISTANCE = 64.0D;
/*      */   
/*      */   public static final double DEFAULT_BELOW_NAME_DISTANCE = 10.0D;
/*      */   
/*      */   public static final double MAX_NAME_TAG_DISTANCE = 512.0D;
/*      */   
/*      */   public static final float DELTA_AFFECTED_BY_BLOCKS_BELOW_0_2 = 0.2F;
/*      */   
/*      */   public static final double DELTA_AFFECTED_BY_BLOCKS_BELOW_0_5 = 0.500001D;
/*      */   
/*      */   public static final double DELTA_AFFECTED_BY_BLOCKS_BELOW_1_0 = 0.999999D;
/*      */   
/*      */   public static final int BASE_TICKS_REQUIRED_TO_FREEZE = 140;
/*      */   
/*      */   public static final int FREEZE_HURT_FREQUENCY = 40;
/*      */   
/*      */   public static final int BASE_SAFE_FALL_DISTANCE = 3;
/*      */   
/*  206 */   private static final AABB INITIAL_AABB = new AABB(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
/*      */   
/*      */   private static final double WATER_FLOW_SCALE = 0.014D;
/*      */   
/*      */   private static final double LAVA_FAST_FLOW_SCALE = 0.007D;
/*      */   
/*      */   private static final double LAVA_SLOW_FLOW_SCALE = 0.0023333333333333335D;
/*      */   
/*      */   private static final int MAX_BLOCK_ITERATIONS_ALONG_TRAVEL_PER_TICK = 16;
/*      */   
/*      */   private static final double MAX_MOVEMENT_RESETTING_TRACE_DISTANCE = 8.0D;
/*      */   
/*  215 */   private static double viewScale = 1.0D;
/*      */   
/*      */   private final EntityType<?> type;
/*      */   
/*      */   private boolean requiresPrecisePosition;
/*      */   
/*  219 */   private int id = 0;
/*      */   
/*      */   public boolean blocksBuilding;
/*      */   
/*  222 */   private ImmutableList<Entity> passengers = ImmutableList.of();
/*      */   
/*      */   protected int boardingCooldown;
/*      */   
/*      */   private Entity vehicle;
/*      */   
/*      */   private Level level;
/*      */   
/*      */   public double xo;
/*      */   
/*      */   public double yo;
/*      */   
/*      */   public double zo;
/*      */   
/*      */   private Vec3 position;
/*      */   
/*      */   private BlockPos blockPosition;
/*      */   
/*      */   private ChunkPos chunkPosition;
/*      */   
/*  233 */   private Vec3 deltaMovement = Vec3.ZERO;
/*      */   
/*      */   private float yRot;
/*      */   
/*      */   private float xRot;
/*      */   
/*      */   public float yRotO;
/*      */   
/*      */   public float xRotO;
/*      */   
/*  238 */   private AABB bb = INITIAL_AABB;
/*      */   
/*      */   private boolean onGround;
/*      */   
/*      */   public boolean horizontalCollision;
/*      */   
/*      */   public boolean verticalCollision;
/*      */   
/*      */   public boolean verticalCollisionBelow;
/*      */   
/*      */   public boolean minorHorizontalCollision;
/*      */   
/*      */   public boolean hurtMarked;
/*      */   
/*  245 */   protected Vec3 stuckSpeedMultiplier = Vec3.ZERO;
/*      */   
/*      */   private RemovalReason removalReason;
/*      */   
/*      */   public static final float DEFAULT_BB_WIDTH = 0.6F;
/*      */   
/*      */   public static final float DEFAULT_BB_HEIGHT = 1.8F;
/*      */   
/*      */   public float moveDist;
/*      */   
/*      */   public float flyDist;
/*      */   
/*      */   public double fallDistance;
/*      */   
/*  255 */   private float nextStep = 1.0F;
/*      */   
/*      */   public double xOld;
/*      */   
/*      */   public double yOld;
/*      */   
/*      */   public double zOld;
/*      */   
/*      */   public boolean noPhysics;
/*      */   
/*  261 */   protected final RandomSource random = RandomSource.create();
/*      */   
/*      */   public int tickCount;
/*      */   
/*      */   private int remainingFireTicks;
/*      */   
/*  266 */   private final EntityFluidInteraction fluidInteraction = new EntityFluidInteraction(Set.of(FluidTags.WATER, FluidTags.LAVA));
/*      */   
/*      */   protected boolean wasTouchingWater;
/*      */   
/*      */   protected boolean wasEyeInWater;
/*      */   
/*      */   public int invulnerableTime;
/*      */   
/*      */   protected boolean firstTick = true;
/*      */   
/*      */   protected final SynchedEntityData entityData;
/*      */   
/*  278 */   protected static final EntityDataAccessor<Byte> DATA_SHARED_FLAGS_ID = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BYTE);
/*      */   
/*      */   protected static final int FLAG_ONFIRE = 0;
/*      */   
/*      */   private static final int FLAG_SHIFT_KEY_DOWN = 1;
/*      */   
/*      */   private static final int FLAG_SPRINTING = 3;
/*      */   
/*      */   private static final int FLAG_SWIMMING = 4;
/*      */   
/*      */   private static final int FLAG_INVISIBLE = 5;
/*      */   
/*      */   protected static final int FLAG_GLOWING = 6;
/*      */   
/*      */   protected static final int FLAG_FALL_FLYING = 7;
/*      */   
/*  288 */   private static final EntityDataAccessor<Integer> DATA_AIR_SUPPLY_ID = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.INT);
/*      */   
/*  289 */   private static final EntityDataAccessor<Optional<Component>> DATA_CUSTOM_NAME = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.OPTIONAL_COMPONENT);
/*      */   
/*  290 */   private static final EntityDataAccessor<Boolean> DATA_CUSTOM_NAME_VISIBLE = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BOOLEAN);
/*      */   
/*  291 */   private static final EntityDataAccessor<Boolean> DATA_SILENT = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BOOLEAN);
/*      */   
/*  292 */   private static final EntityDataAccessor<Boolean> DATA_NO_GRAVITY = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.BOOLEAN);
/*      */   
/*  293 */   protected static final EntityDataAccessor<Pose> DATA_POSE = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.POSE);
/*      */   
/*  294 */   private static final EntityDataAccessor<Integer> DATA_TICKS_FROZEN = SynchedEntityData.defineId(Entity.class, EntityDataSerializers.INT);
/*      */   
/*  296 */   private EntityInLevelCallback levelCallback = EntityInLevelCallback.NULL;
/*      */   
/*  299 */   private final VecDeltaCodec packetPositionCodec = new VecDeltaCodec();
/*      */   
/*      */   public boolean needsSync;
/*      */   
/*      */   public boolean syncPosition;
/*      */   
/*      */   public PortalProcessor portalProcess;
/*      */   
/*      */   private int portalCooldown;
/*      */   
/*      */   private boolean invulnerable;
/*      */   
/*  307 */   protected UUID uuid = Mth.createInsecureUUID(this.random);
/*      */   
/*  308 */   protected String stringUUID = this.uuid.toString();
/*      */   
/*      */   private boolean hasGlowingTag;
/*      */   
/*  311 */   private final Set<String> tags = Sets.newHashSet();
/*      */   
/*  313 */   private final double[] pistonDeltas = new double[] { 0.0D, 0.0D, 0.0D };
/*      */   
/*      */   private long pistonDeltasGameTime;
/*      */   
/*      */   private EntityDimensions dimensions;
/*      */   
/*      */   private float eyeHeight;
/*      */   
/*      */   public boolean isInPowderSnow;
/*      */   
/*      */   public boolean wasInPowderSnow;
/*      */   
/*  320 */   public Optional<BlockPos> mainSupportingBlockPos = Optional.empty();
/*      */   
/*      */   private boolean onGroundNoBlocks = false;
/*      */   
/*      */   private float crystalSoundIntensity;
/*      */   
/*      */   private int lastCrystalSoundPlayTick;
/*      */   
/*      */   private boolean hasVisualFire;
/*      */   
/*  327 */   private Vec3 lastKnownSpeed = Vec3.ZERO;
/*      */   
/*      */   private Vec3 lastKnownPosition;
/*      */   
/*  330 */   private BlockState inBlockState = null;
/*      */   
/*      */   public static final int MAX_MOVEMENTS_HANDELED_PER_TICK = 100;
/*      */   
/*  332 */   private final ArrayDeque<Movement> movementThisTick = new ArrayDeque<>(100);
/*      */   
/*  334 */   private final List<Movement> finalMovementsThisTick = (List<Movement>)new ObjectArrayList();
/*      */   
/*  335 */   private final LongSet visitedBlocks = (LongSet)new LongOpenHashSet();
/*      */   
/*  336 */   private final InsideBlockEffectApplier.StepBasedCollector insideEffectCollector = new InsideBlockEffectApplier.StepBasedCollector();
/*      */   
/*  338 */   private CustomData customData = CustomData.EMPTY;
/*      */   
/*      */   private static final class Movement extends Record {
/*      */     private final Vec3 from;
/*      */     
/*      */     private final Vec3 to;
/*      */     
/*      */     private final Optional<Vec3> axisDependentOriginalMovement;
/*      */     
/*      */     private Movement(Vec3 from, Vec3 to, Optional<Vec3> axisDependentOriginalMovement) {
/*  340 */       this.from = from;
/*  340 */       this.to = to;
/*  340 */       this.axisDependentOriginalMovement = axisDependentOriginalMovement;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Entity$Movement;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #340	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/Entity$Movement;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Entity$Movement;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #340	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/Entity$Movement;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Entity$Movement;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #340	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/entity/Entity$Movement;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public Vec3 from() {
/*  340 */       return this.from;
/*      */     }
/*      */     
/*      */     public Vec3 to() {
/*  340 */       return this.to;
/*      */     }
/*      */     
/*      */     public Optional<Vec3> axisDependentOriginalMovement() {
/*  340 */       return this.axisDependentOriginalMovement;
/*      */     }
/*      */     
/*      */     public Movement(Vec3 from, Vec3 to, Vec3 axisDependentOriginalMovement) {
/*  342 */       this(from, to, Optional.of(axisDependentOriginalMovement));
/*      */     }
/*      */     
/*      */     public Movement(Vec3 from, Vec3 to) {
/*  346 */       this(from, to, Optional.empty());
/*      */     }
/*      */   }
/*      */   
/*      */   public Entity(EntityType<?> type, Level level) {
/*  351 */     this.type = type;
/*  352 */     this.level = level;
/*  354 */     this.id = level.getNextEntityId();
/*  355 */     this.dimensions = type.getDimensions();
/*  356 */     this.position = Vec3.ZERO;
/*  357 */     this.blockPosition = BlockPos.ZERO;
/*  358 */     this.chunkPosition = ChunkPos.ZERO;
/*  360 */     SynchedEntityData.Builder entityDataBuilder = new SynchedEntityData.Builder(this);
/*  361 */     entityDataBuilder.define(DATA_SHARED_FLAGS_ID, Byte.valueOf((byte)0));
/*  362 */     entityDataBuilder.define(DATA_AIR_SUPPLY_ID, Integer.valueOf(getMaxAirSupply()));
/*  363 */     entityDataBuilder.define(DATA_CUSTOM_NAME_VISIBLE, Boolean.valueOf(false));
/*  364 */     entityDataBuilder.define(DATA_CUSTOM_NAME, Optional.empty());
/*  365 */     entityDataBuilder.define(DATA_SILENT, Boolean.valueOf(false));
/*  366 */     entityDataBuilder.define(DATA_NO_GRAVITY, Boolean.valueOf(false));
/*  367 */     entityDataBuilder.define(DATA_POSE, Pose.STANDING);
/*  368 */     entityDataBuilder.define(DATA_TICKS_FROZEN, Integer.valueOf(0));
/*  369 */     defineSynchedData(entityDataBuilder);
/*  370 */     this.entityData = entityDataBuilder.build();
/*  372 */     setPos(0.0D, 0.0D, 0.0D);
/*  374 */     this.eyeHeight = this.dimensions.eyeHeight();
/*      */   }
/*      */   
/*      */   public boolean isColliding(BlockPos pos, BlockState state) {
/*  378 */     VoxelShape movedBlockShape = state.getCollisionShape((BlockGetter)level(), pos, CollisionContext.of(this)).move((Vec3i)pos);
/*  379 */     return Shapes.joinIsNotEmpty(movedBlockShape, Shapes.create(getBoundingBox()), BooleanOp.AND);
/*      */   }
/*      */   
/*      */   public int getTeamColor() {
/*  383 */     PlayerTeam playerTeam = getTeam();
/*  384 */     if (playerTeam != null && playerTeam.getColor().isPresent())
/*  385 */       return ((TeamColor)playerTeam.getColor().get()).rgb(); 
/*  387 */     return 16777215;
/*      */   }
/*      */   
/*      */   public boolean isSpectator() {
/*  391 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canInteractWithLevel() {
/*  395 */     return (isAlive() && !isRemoved() && !isSpectator());
/*      */   }
/*      */   
/*      */   public final void unRide() {
/*  399 */     if (isVehicle())
/*  400 */       ejectPassengers(); 
/*  402 */     if (isPassenger())
/*  403 */       stopRiding(); 
/*      */   }
/*      */   
/*      */   public void syncPacketPositionCodec(double x, double y, double z) {
/*  411 */     this.packetPositionCodec.setBase(new Vec3(x, y, z));
/*      */   }
/*      */   
/*      */   public VecDeltaCodec getPositionCodec() {
/*  415 */     return this.packetPositionCodec;
/*      */   }
/*      */   
/*      */   public EntityType<?> getType() {
/*  419 */     return this.type;
/*      */   }
/*      */   
/*      */   public Holder<EntityType<?>> typeHolder() {
/*  424 */     return (Holder<EntityType<?>>)this.type.builtInRegistryHolder();
/*      */   }
/*      */   
/*      */   public boolean getRequiresPrecisePosition() {
/*  428 */     return this.requiresPrecisePosition;
/*      */   }
/*      */   
/*      */   public void setRequiresPrecisePosition(boolean requiresPrecisePosition) {
/*  432 */     this.requiresPrecisePosition = requiresPrecisePosition;
/*      */   }
/*      */   
/*      */   public int getId() {
/*  437 */     if (this.id == 0)
/*  438 */       throw new IllegalStateException("Tried to access entity ID before ID assignment"); 
/*  440 */     return this.id;
/*      */   }
/*      */   
/*      */   public void setId(int id) {
/*  444 */     this.id = id;
/*      */   }
/*      */   
/*      */   public Set<String> entityTags() {
/*  448 */     return this.tags;
/*      */   }
/*      */   
/*      */   public boolean addTag(String tag) {
/*  452 */     if (this.tags.size() >= 1024)
/*  453 */       return false; 
/*  455 */     return this.tags.add(tag);
/*      */   }
/*      */   
/*      */   public boolean removeTag(String tag) {
/*  459 */     return this.tags.remove(tag);
/*      */   }
/*      */   
/*      */   public void kill(ServerLevel level) {
/*  463 */     remove(RemovalReason.KILLED);
/*  464 */     gameEvent((Holder<GameEvent>)GameEvent.ENTITY_DIE);
/*      */   }
/*      */   
/*      */   public final void discard() {
/*  468 */     remove(RemovalReason.DISCARDED);
/*      */   }
/*      */   
/*      */   public SynchedEntityData getEntityData() {
/*  474 */     return this.entityData;
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj) {
/*  479 */     if (obj instanceof Entity) {
/*  479 */       Entity entity = (Entity)obj;
/*  480 */       return (entity.getId() == getId());
/*      */     } 
/*  482 */     return false;
/*      */   }
/*      */   
/*      */   public int hashCode() {
/*  487 */     return getId();
/*      */   }
/*      */   
/*      */   public void remove(RemovalReason reason) {
/*  492 */     setRemoved(reason);
/*      */   }
/*      */   
/*      */   public void onClientRemoval() {}
/*      */   
/*      */   public void onRemoval(RemovalReason reason) {}
/*      */   
/*      */   public void setPose(Pose pose) {
/*  502 */     this.entityData.set(DATA_POSE, pose);
/*      */   }
/*      */   
/*      */   public Pose getPose() {
/*  506 */     return (Pose)this.entityData.get(DATA_POSE);
/*      */   }
/*      */   
/*      */   public boolean hasPose(Pose pose) {
/*  510 */     return (getPose() == pose);
/*      */   }
/*      */   
/*      */   public boolean closerThan(Entity other, double distance) {
/*  514 */     return position().closerThan((Position)other.position(), distance);
/*      */   }
/*      */   
/*      */   public boolean closerThan(Entity other, double distanceXZ, double distanceY) {
/*  518 */     double dx = other.getX() - getX();
/*  519 */     double dy = other.getY() - getY();
/*  520 */     double dz = other.getZ() - getZ();
/*  521 */     return (Mth.lengthSquared(dx, dz) < Mth.square(distanceXZ) && 
/*  522 */       Mth.square(dy) < Mth.square(distanceY));
/*      */   }
/*      */   
/*      */   protected void setRot(float yRot, float xRot) {
/*  526 */     setYRot(yRot % 360.0F);
/*  527 */     setXRot(xRot % 360.0F);
/*      */   }
/*      */   
/*      */   public final void setPos(Vec3 pos) {
/*  531 */     setPos(pos.x(), pos.y(), pos.z());
/*      */   }
/*      */   
/*      */   public void setPos(double x, double y, double z) {
/*  536 */     setPosRaw(x, y, z);
/*  537 */     setBoundingBox(makeBoundingBox());
/*      */   }
/*      */   
/*      */   protected final AABB makeBoundingBox() {
/*  541 */     return makeBoundingBox(this.position);
/*      */   }
/*      */   
/*      */   protected AABB makeBoundingBox(Vec3 position) {
/*  545 */     return this.dimensions.makeBoundingBox(position);
/*      */   }
/*      */   
/*      */   protected void reapplyPosition() {
/*  550 */     this.lastKnownPosition = null;
/*  551 */     setPos(this.position.x, this.position.y, this.position.z);
/*      */   }
/*      */   
/*      */   public void turn(double xo, double yo) {
/*  555 */     float xDelta = (float)yo * 0.15F;
/*  556 */     float yDelta = (float)xo * 0.15F;
/*  558 */     setXRot(getXRot() + xDelta);
/*  559 */     setYRot(getYRot() + yDelta);
/*  560 */     setXRot(Mth.clamp(getXRot(), -90.0F, 90.0F));
/*  562 */     this.xRotO += xDelta;
/*  563 */     this.yRotO += yDelta;
/*  564 */     this.xRotO = Mth.clamp(this.xRotO, -90.0F, 90.0F);
/*  566 */     if (this.vehicle != null)
/*  567 */       this.vehicle.onPassengerTurned(this); 
/*      */   }
/*      */   
/*      */   public void updateDataBeforeSync() {}
/*      */   
/*      */   public void tick() {
/*  575 */     baseTick();
/*      */   }
/*      */   
/*      */   public void baseTick() {
/*  579 */     ProfilerFiller profiler = Profiler.get();
/*  580 */     profiler.push("entityBaseTick");
/*  582 */     computeSpeed();
/*  584 */     this.inBlockState = null;
/*  586 */     if (isPassenger() && getVehicle().isRemoved())
/*  587 */       stopRiding(); 
/*  590 */     if (this.boardingCooldown > 0)
/*  591 */       this.boardingCooldown--; 
/*  594 */     handlePortal();
/*  597 */     if (canSpawnSprintParticle())
/*  598 */       spawnSprintParticle(); 
/*  601 */     this.wasInPowderSnow = this.isInPowderSnow;
/*  602 */     this.isInPowderSnow = false;
/*  603 */     this.wasEyeInWater = isEyeInFluid(FluidTags.WATER);
/*  604 */     updateFluidInteraction();
/*  605 */     updateSwimming();
/*  607 */     Level level = level();
/*  607 */     if (level instanceof ServerLevel) {
/*  607 */       ServerLevel serverLevel = (ServerLevel)level;
/*  608 */       if (this.remainingFireTicks > 0)
/*  609 */         if (fireImmune()) {
/*  610 */           clearFire();
/*      */         } else {
/*  612 */           if (this.remainingFireTicks % 20 == 0 && !isInLava())
/*  613 */             hurtServer(serverLevel, damageSources().onFire(), 1.0F); 
/*  615 */           setRemainingFireTicks(this.remainingFireTicks - 1);
/*      */         }  
/*      */     } else {
/*  619 */       clearFire();
/*      */     } 
/*  622 */     if (isInLava())
/*  623 */       this.fallDistance *= 0.5D; 
/*  626 */     checkBelowWorld();
/*  628 */     if (!level().isClientSide())
/*  629 */       setSharedFlagOnFire((this.remainingFireTicks > 0)); 
/*  632 */     this.firstTick = false;
/*  634 */     level = level();
/*  634 */     if (level instanceof ServerLevel) {
/*  634 */       ServerLevel serverLevel = (ServerLevel)level;
/*  635 */       if (this instanceof Leashable)
/*  636 */         Leashable.tickLeash(serverLevel, this); 
/*      */     } 
/*  640 */     profiler.pop();
/*      */   }
/*      */   
/*      */   protected void computeSpeed() {
/*  644 */     if (this.lastKnownPosition == null)
/*  645 */       this.lastKnownPosition = position(); 
/*  647 */     this.lastKnownSpeed = position().subtract(this.lastKnownPosition);
/*  648 */     this.lastKnownPosition = position();
/*      */   }
/*      */   
/*      */   public void setSharedFlagOnFire(boolean value) {
/*  652 */     setSharedFlag(0, (value || this.hasVisualFire));
/*      */   }
/*      */   
/*      */   public void checkBelowWorld() {
/*  656 */     if (getY() < (level().getMinY() - 64))
/*  657 */       onBelowWorld(); 
/*      */   }
/*      */   
/*      */   public void setPortalCooldown() {
/*  662 */     this.portalCooldown = getDimensionChangingDelay();
/*      */   }
/*      */   
/*      */   public void setPortalCooldown(int portalCooldown) {
/*  666 */     this.portalCooldown = portalCooldown;
/*      */   }
/*      */   
/*      */   public int getPortalCooldown() {
/*  670 */     return this.portalCooldown;
/*      */   }
/*      */   
/*      */   public boolean isOnPortalCooldown() {
/*  674 */     return (this.portalCooldown > 0);
/*      */   }
/*      */   
/*      */   protected void processPortalCooldown() {
/*  678 */     if (isOnPortalCooldown())
/*  679 */       this.portalCooldown--; 
/*      */   }
/*      */   
/*      */   public void lavaIgnite() {
/*  684 */     if (fireImmune())
/*      */       return; 
/*  688 */     igniteForSeconds(15.0F);
/*      */   }
/*      */   
/*      */   public void lavaHurt() {
/*  692 */     if (fireImmune())
/*      */       return; 
/*  695 */     Level level = level();
/*  695 */     if (level instanceof ServerLevel) {
/*  695 */       ServerLevel serverLevel = (ServerLevel)level;
/*  696 */       if (hurtServer(serverLevel, damageSources().lava(), 4.0F) && 
/*  697 */         shouldPlayLavaHurtSound() && !isSilent())
/*  698 */         serverLevel.playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_BURN, getSoundSource(), 0.4F, 2.0F + this.random.nextFloat() * 0.4F); 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean shouldPlayLavaHurtSound() {
/*  705 */     return true;
/*      */   }
/*      */   
/*      */   public final void igniteForSeconds(float numberOfSeconds) {
/*  709 */     igniteForTicks(Mth.floor(numberOfSeconds * 20.0F));
/*      */   }
/*      */   
/*      */   public void igniteForTicks(int numberOfTicks) {
/*  713 */     if (this.remainingFireTicks < numberOfTicks)
/*  714 */       setRemainingFireTicks(numberOfTicks); 
/*  716 */     clearFreeze();
/*      */   }
/*      */   
/*      */   public void setRemainingFireTicks(int remainingTicks) {
/*  720 */     this.remainingFireTicks = remainingTicks;
/*      */   }
/*      */   
/*      */   public int getRemainingFireTicks() {
/*  724 */     return this.remainingFireTicks;
/*      */   }
/*      */   
/*      */   public void clearFire() {
/*  728 */     setRemainingFireTicks(Math.min(0, getRemainingFireTicks()));
/*      */   }
/*      */   
/*      */   protected void onBelowWorld() {
/*  732 */     discard();
/*      */   }
/*      */   
/*      */   public boolean isFree(double xa, double ya, double za) {
/*  736 */     return isFree(getBoundingBox().move(xa, ya, za));
/*      */   }
/*      */   
/*      */   private boolean isFree(AABB box) {
/*  740 */     return (level().noCollision(this, box) && !level().containsAnyLiquid(box));
/*      */   }
/*      */   
/*      */   public void setOnGround(boolean onGround) {
/*  744 */     this.onGround = onGround;
/*  745 */     checkSupportingBlock(onGround, (Vec3)null);
/*      */   }
/*      */   
/*      */   public void setOnGroundWithMovement(boolean onGround, Vec3 movement) {
/*  749 */     setOnGroundWithMovement(onGround, this.horizontalCollision, movement);
/*      */   }
/*      */   
/*      */   public void setOnGroundWithMovement(boolean onGround, boolean horizontalCollision, Vec3 movement) {
/*  753 */     this.onGround = onGround;
/*  754 */     this.horizontalCollision = horizontalCollision;
/*  755 */     checkSupportingBlock(onGround, movement);
/*      */   }
/*      */   
/*      */   public boolean isSupportedBy(BlockPos pos) {
/*  759 */     return (this.mainSupportingBlockPos.isPresent() && ((BlockPos)this.mainSupportingBlockPos.get()).equals(pos));
/*      */   }
/*      */   
/*      */   protected void checkSupportingBlock(boolean onGround, Vec3 movement) {
/*  763 */     if (onGround) {
/*  764 */       AABB boundingBox = getBoundingBox();
/*  765 */       AABB testArea = new AABB(boundingBox.minX, boundingBox.minY - 1.0E-6D, boundingBox.minZ, boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
/*  766 */       Optional<BlockPos> supportingBlock = this.level.findSupportingBlock(this, testArea);
/*  775 */       if (supportingBlock.isPresent() || this.onGroundNoBlocks) {
/*  776 */         this.mainSupportingBlockPos = supportingBlock;
/*  777 */       } else if (movement != null) {
/*  780 */         AABB onGroundCollisionTestArea = testArea.move(-movement.x, 0.0D, -movement.z);
/*  781 */         supportingBlock = this.level.findSupportingBlock(this, onGroundCollisionTestArea);
/*  782 */         this.mainSupportingBlockPos = supportingBlock;
/*      */       } 
/*  784 */       this.onGroundNoBlocks = supportingBlock.isEmpty();
/*      */     } else {
/*  786 */       this.onGroundNoBlocks = false;
/*  787 */       if (this.mainSupportingBlockPos.isPresent())
/*  788 */         this.mainSupportingBlockPos = Optional.empty(); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean onGround() {
/*  794 */     return this.onGround;
/*      */   }
/*      */   
/*      */   public void move(MoverType moverType, Vec3 delta) {
/*  798 */     if (this.noPhysics) {
/*  799 */       setPos(getX() + delta.x, getY() + delta.y, getZ() + delta.z);
/*  800 */       this.horizontalCollision = false;
/*  801 */       this.verticalCollision = false;
/*  802 */       this.verticalCollisionBelow = false;
/*  803 */       this.minorHorizontalCollision = false;
/*      */       return;
/*      */     } 
/*  808 */     if (moverType == MoverType.PISTON) {
/*  809 */       delta = limitPistonMovement(delta);
/*  810 */       if (delta.equals(Vec3.ZERO))
/*      */         return; 
/*      */     } 
/*  815 */     ProfilerFiller profiler = Profiler.get();
/*  816 */     profiler.push("move");
/*  818 */     if (this.stuckSpeedMultiplier.lengthSqr() > 1.0E-7D) {
/*  819 */       if (moverType != MoverType.PISTON)
/*  820 */         delta = delta.multiply(this.stuckSpeedMultiplier); 
/*  822 */       this.stuckSpeedMultiplier = Vec3.ZERO;
/*  823 */       setDeltaMovement(Vec3.ZERO);
/*      */     } 
/*  826 */     delta = maybeBackOffFromEdge(delta, moverType);
/*  828 */     Vec3 movement = collide(delta);
/*  829 */     double movementLength = movement.lengthSqr();
/*  830 */     if (movementLength > 1.0E-7D || delta.lengthSqr() - movementLength < 1.0E-7D) {
/*  831 */       if (this.fallDistance != 0.0D && movementLength >= 1.0D) {
/*  833 */         double checkDistance = Math.min(movement.length(), 8.0D);
/*  834 */         Vec3 checkTo = position().add(movement.normalize().scale(checkDistance));
/*  835 */         BlockHitResult hitResult = level().clip(new ClipContext(position(), checkTo, ClipContext.Block.FALLDAMAGE_RESETTING, ClipContext.Fluid.WATER, this));
/*  836 */         if (hitResult.getType() != HitResult.Type.MISS)
/*  837 */           resetFallDistance(); 
/*      */       } 
/*  841 */       Vec3 pos = position();
/*  842 */       Vec3 newPosition = pos.add(movement);
/*  843 */       addMovementThisTick(new Movement(pos, newPosition, delta));
/*  844 */       setPos(newPosition);
/*      */     } 
/*  847 */     profiler.pop();
/*  848 */     profiler.push("rest");
/*  851 */     boolean xCollision = !Mth.equal(delta.x, movement.x);
/*  852 */     boolean zCollision = !Mth.equal(delta.z, movement.z);
/*  853 */     this.horizontalCollision = (xCollision || zCollision);
/*  855 */     boolean movedVertically = (Math.abs(delta.y) > 0.0D);
/*  857 */     if (movedVertically || isLocalInstanceAuthoritative()) {
/*  859 */       this.verticalCollision = (delta.y != movement.y);
/*  860 */       this.verticalCollisionBelow = (this.verticalCollision && delta.y < 0.0D);
/*  861 */       setOnGroundWithMovement(this.verticalCollisionBelow, this.horizontalCollision, movement);
/*      */     } 
/*  864 */     if (this.horizontalCollision) {
/*  865 */       this.minorHorizontalCollision = isHorizontalCollisionMinor(movement);
/*      */     } else {
/*  867 */       this.minorHorizontalCollision = false;
/*      */     } 
/*  870 */     BlockPos effectPos = getOnPosLegacy();
/*  871 */     BlockState effectState = level().getBlockState(effectPos);
/*  873 */     if (isLocalInstanceAuthoritative())
/*  877 */       checkFallDamage(movement.y, onGround(), effectState, effectPos); 
/*  880 */     if (isRemoved()) {
/*  881 */       profiler.pop();
/*      */       return;
/*      */     } 
/*  885 */     if (canSimulateMovement() && ((movedVertically && this.verticalCollision) || this.horizontalCollision))
/*  886 */       restituteMovementAfterCollisions(effectState, xCollision, zCollision, movement); 
/*  889 */     if (!level().isClientSide() || isLocalInstanceAuthoritative()) {
/*  890 */       MovementEmission emission = getMovementEmission();
/*  891 */       if (emission.emitsAnything() && !isPassenger())
/*  892 */         applyMovementEmissionAndPlaySound(emission, movement, effectPos, effectState); 
/*      */     } 
/*  899 */     float blockSpeedFactor = getBlockSpeedFactor();
/*  900 */     setDeltaMovement(getDeltaMovement().multiply(blockSpeedFactor, 1.0D, blockSpeedFactor));
/*  902 */     profiler.pop();
/*      */   }
/*      */   
/*      */   private void restituteMovementAfterCollisions(BlockState effectState, boolean xCollision, boolean zCollision, Vec3 movement) {
/*  906 */     double restitution = isSuppressingBounce() ? 0.0D : getEntityBounciness();
/*  908 */     Vec3 currentMovement = getDeltaMovement();
/*  910 */     Vec3 movementAfterBounce = currentMovement;
/*  912 */     if (xCollision)
/*  913 */       movementAfterBounce = movementAfterBounce.with(Direction.Axis.X, -currentMovement.x * restitution); 
/*  915 */     if (zCollision)
/*  916 */       movementAfterBounce = movementAfterBounce.with(Direction.Axis.Z, -currentMovement.z * restitution); 
/*  919 */     boolean bounced = (restitution > 0.0D && (xCollision || zCollision));
/*  921 */     if (this.verticalCollision) {
/*      */       double gravityCompensation, effectiveDrag;
/*  922 */       if (this.verticalCollisionBelow)
/*  923 */         restitution = (-currentMovement.y < getEffectiveGravity() || isSuppressingBounce() || effectState.is(BlockTags.SUPPRESSES_BOUNCE)) ? 0.0D : Math.max(restitution, getBlockBounciness(effectState.getBlock())); 
/*  928 */       if (restitution > 0.0D) {
/*  929 */         double portionWithMovement = movement.y / currentMovement.y;
/*  930 */         gravityCompensation = portionWithMovement * getEffectiveGravity();
/*  931 */         effectiveDrag = Mth.lerp(portionWithMovement, 1.0D, getAirDrag());
/*  932 */         bounced = true;
/*      */       } else {
/*  934 */         gravityCompensation = 0.0D;
/*  935 */         effectiveDrag = 1.0D;
/*      */       } 
/*  938 */       movementAfterBounce = movementAfterBounce.with(Direction.Axis.Y, (gravityCompensation - currentMovement.y) * effectiveDrag * restitution);
/*      */     } 
/*  940 */     if (bounced) {
/*  941 */       gameEvent((Holder<GameEvent>)GameEvent.BOUNCE);
/*  942 */       this.syncPosition = true;
/*      */     } 
/*  944 */     setDeltaMovement(movementAfterBounce);
/*      */   }
/*      */   
/*      */   private double getBlockBounciness(Block onBlock) {
/*  948 */     float blockBounciness = onBlock.getBounceRestitution();
/*  949 */     if (!(this instanceof LivingEntity))
/*  951 */       blockBounciness *= 0.8F; 
/*  953 */     return blockBounciness;
/*      */   }
/*      */   
/*      */   protected double getEntityBounciness() {
/*  957 */     return 0.0D;
/*      */   }
/*      */   
/*      */   protected double getEffectiveGravity() {
/*  961 */     return getGravity();
/*      */   }
/*      */   
/*      */   protected boolean omnidirectionalAirMover() {
/*  965 */     return false;
/*      */   }
/*      */   
/*      */   private void applyMovementEmissionAndPlaySound(MovementEmission emission, Vec3 clippedMovement, BlockPos effectPos, BlockState effectState) {
/*  969 */     float moveDistScale = 0.6F;
/*  970 */     float movedDistance = (float)(clippedMovement.length() * 0.6000000238418579D);
/*  971 */     float horizontalMovedDistance = (float)(clippedMovement.horizontalDistance() * 0.6000000238418579D);
/*  973 */     BlockPos supportingPos = getOnPos();
/*  974 */     BlockState supportingState = level().getBlockState(supportingPos);
/*  976 */     boolean climbing = isStateClimbable(supportingState);
/*  977 */     this.moveDist += climbing ? movedDistance : horizontalMovedDistance;
/*  980 */     this.flyDist += movedDistance;
/*  982 */     if (this.moveDist > this.nextStep && !supportingState.isAir()) {
/*  983 */       boolean onlyEffectStateEmittions = supportingPos.equals(effectPos);
/*  984 */       boolean producedSideEffects = vibrationAndSoundEffectsFromBlock(effectPos, effectState, emission.emitsSounds(), onlyEffectStateEmittions, clippedMovement);
/*  985 */       if (!onlyEffectStateEmittions)
/*  986 */         producedSideEffects |= vibrationAndSoundEffectsFromBlock(supportingPos, supportingState, false, emission.emitsEvents(), clippedMovement); 
/*  988 */       if (producedSideEffects) {
/*  989 */         this.nextStep = nextStep();
/*  990 */       } else if (isInWater()) {
/*  991 */         this.nextStep = nextStep();
/*  992 */         if (emission.emitsSounds())
/*  993 */           waterSwimSound(); 
/*  995 */         if (emission.emitsEvents())
/*  996 */           gameEvent((Holder<GameEvent>)GameEvent.SWIM); 
/*      */       } 
/*  999 */     } else if (supportingState.isAir()) {
/* 1000 */       processFlappingMovement();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void applyEffectsFromBlocks() {
/* 1005 */     this.finalMovementsThisTick.clear();
/* 1006 */     this.finalMovementsThisTick.addAll(this.movementThisTick);
/* 1007 */     this.movementThisTick.clear();
/* 1009 */     if (this.finalMovementsThisTick.isEmpty()) {
/* 1011 */       this.finalMovementsThisTick.add(new Movement(oldPosition(), position()));
/* 1012 */     } else if (((Movement)this.finalMovementsThisTick.getLast()).to.distanceToSqr(position()) > 9.999999439624929E-11D) {
/* 1014 */       this.finalMovementsThisTick.add(new Movement(((Movement)this.finalMovementsThisTick.getLast()).to, position()));
/*      */     } 
/* 1017 */     applyEffectsFromBlocks(this.finalMovementsThisTick);
/*      */   }
/*      */   
/*      */   protected void applyEffectsFromBlocksForLastMovements() {
/* 1021 */     applyEffectsFromBlocks(this.finalMovementsThisTick);
/*      */   }
/*      */   
/*      */   private void addMovementThisTick(Movement movement) {
/* 1025 */     if (this.movementThisTick.size() >= 100) {
/* 1033 */       Movement first = this.movementThisTick.removeFirst();
/* 1034 */       Movement second = this.movementThisTick.removeFirst();
/* 1035 */       Movement combined = new Movement(first.from(), second.to());
/* 1036 */       this.movementThisTick.addFirst(combined);
/*      */     } 
/* 1038 */     this.movementThisTick.add(movement);
/*      */   }
/*      */   
/*      */   public void removeLatestMovementRecording() {
/* 1043 */     if (!this.movementThisTick.isEmpty())
/* 1044 */       this.movementThisTick.removeLast(); 
/*      */   }
/*      */   
/*      */   protected void clearMovementThisTick() {
/* 1049 */     this.movementThisTick.clear();
/*      */   }
/*      */   
/*      */   public boolean hasMovedHorizontallyRecently() {
/* 1053 */     return (Math.abs(this.lastKnownSpeed.horizontalDistance()) > 9.999999747378752E-6D);
/*      */   }
/*      */   
/*      */   public void applyEffectsFromBlocks(Vec3 from, Vec3 to) {
/* 1057 */     applyEffectsFromBlocks(List.of(new Movement(from, to)));
/*      */   }
/*      */   
/*      */   private void applyEffectsFromBlocks(List<Movement> movements) {
/* 1061 */     if (!isAffectedByBlocks())
/*      */       return; 
/* 1065 */     if (onGround()) {
/* 1066 */       BlockPos effectPos = getOnPosLegacy();
/* 1067 */       BlockState effectState = level().getBlockState(effectPos);
/* 1068 */       effectState.getBlock().stepOn(level(), effectPos, effectState, this);
/*      */     } 
/* 1071 */     boolean wasOnFire = isOnFire();
/* 1072 */     boolean wasFreezing = isFreezing();
/* 1073 */     int previousRemainingFireTicks = getRemainingFireTicks();
/* 1074 */     checkInsideBlocks(movements, this.insideEffectCollector);
/* 1075 */     this.insideEffectCollector.applyAndClear(this);
/* 1077 */     if (isInRain())
/* 1078 */       clearFire(); 
/* 1081 */     if ((wasOnFire && !isOnFire()) || (wasFreezing && 
/* 1082 */       !isFreezing()))
/* 1083 */       playEntityOnFireExtinguishedSound(); 
/* 1086 */     boolean wasIgnitedThisTick = (getRemainingFireTicks() > previousRemainingFireTicks);
/* 1087 */     if (!level().isClientSide() && !isOnFire() && !wasIgnitedThisTick)
/* 1089 */       setRemainingFireTicks(-getFireImmuneTicks()); 
/*      */   }
/*      */   
/*      */   protected boolean isAffectedByBlocks() {
/* 1094 */     return (!isRemoved() && !this.noPhysics);
/*      */   }
/*      */   
/*      */   private boolean isStateClimbable(BlockState state) {
/* 1098 */     return (state.is(BlockTags.CLIMBABLE) || state.is(Blocks.POWDER_SNOW));
/*      */   }
/*      */   
/*      */   private boolean vibrationAndSoundEffectsFromBlock(BlockPos pos, BlockState blockState, boolean shouldSound, boolean shouldVibrate, Vec3 clippedMovement) {
/* 1102 */     if (blockState.isAir())
/* 1103 */       return false; 
/* 1105 */     boolean isClimbable = isStateClimbable(blockState);
/* 1106 */     if ((onGround() || isClimbable || (isCrouching() && clippedMovement.y == 0.0D) || isOnRails()) && !isSwimming()) {
/* 1107 */       if (shouldSound)
/* 1108 */         walkingStepSound(pos, blockState); 
/* 1110 */       if (shouldVibrate)
/* 1111 */         level().gameEvent((Holder)GameEvent.STEP, position(), GameEvent.Context.of(this, blockState)); 
/* 1113 */       return true;
/*      */     } 
/* 1115 */     return false;
/*      */   }
/*      */   
/*      */   protected boolean isHorizontalCollisionMinor(Vec3 movement) {
/* 1119 */     return false;
/*      */   }
/*      */   
/*      */   protected void playEntityOnFireExtinguishedSound() {
/* 1123 */     if (!this.level.isClientSide())
/* 1124 */       level().playSound(null, getX(), getY(), getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE, getSoundSource(), 0.7F, 1.6F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F); 
/*      */   }
/*      */   
/*      */   public void extinguishFire() {
/* 1129 */     if (isOnFire())
/* 1130 */       playEntityOnFireExtinguishedSound(); 
/* 1132 */     clearFire();
/*      */   }
/*      */   
/*      */   protected void processFlappingMovement() {
/* 1136 */     if (isFlapping()) {
/* 1137 */       onFlap();
/* 1138 */       if (getMovementEmission().emitsEvents())
/* 1139 */         gameEvent((Holder<GameEvent>)GameEvent.FLAP); 
/*      */     } 
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public BlockPos getOnPosLegacy() {
/* 1153 */     return getOnPos(0.2F);
/*      */   }
/*      */   
/*      */   public BlockPos getBlockPosBelowThatAffectsMyMovement() {
/* 1162 */     return getOnPos(0.500001F);
/*      */   }
/*      */   
/*      */   public BlockPos getOnPos() {
/* 1170 */     return getOnPos(1.0E-5F);
/*      */   }
/*      */   
/*      */   protected BlockPos getOnPos(float offset) {
/* 1174 */     if (this.mainSupportingBlockPos.isPresent()) {
/* 1175 */       BlockPos getOnPos = this.mainSupportingBlockPos.get();
/* 1176 */       if (offset > 1.0E-5F) {
/* 1183 */         BlockState belowState = level().getBlockState(getOnPos);
/* 1185 */         if ((offset <= 0.5D && belowState.is(BlockTags.FENCES)) || belowState.is(BlockTags.WALLS) || belowState.getBlock() instanceof net.minecraft.world.level.block.FenceGateBlock)
/* 1186 */           return getOnPos; 
/* 1188 */         return getOnPos.atY(Mth.floor(this.position.y - offset));
/*      */       } 
/* 1190 */       return getOnPos;
/*      */     } 
/* 1192 */     int xTruncated = Mth.floor(this.position.x);
/* 1193 */     int yTruncatedBelow = Mth.floor(this.position.y - offset);
/* 1194 */     int zTruncated = Mth.floor(this.position.z);
/* 1196 */     return new BlockPos(xTruncated, yTruncatedBelow, zTruncated);
/*      */   }
/*      */   
/*      */   protected float getBlockJumpFactor() {
/* 1203 */     float jumpFactorHere = level().getBlockState(blockPosition()).getBlock().getJumpFactor();
/* 1204 */     float jumpFactorBelow = level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getJumpFactor();
/* 1205 */     return (jumpFactorHere == 1.0D) ? jumpFactorBelow : jumpFactorHere;
/*      */   }
/*      */   
/*      */   protected float getBlockSpeedFactor() {
/* 1212 */     BlockState state = level().getBlockState(blockPosition());
/* 1213 */     float speedFactorHere = state.getBlock().getSpeedFactor();
/* 1214 */     if (state.is(Blocks.WATER) || state.is(Blocks.BUBBLE_COLUMN))
/* 1215 */       return speedFactorHere; 
/* 1217 */     return (speedFactorHere == 1.0D) ? level().getBlockState(getBlockPosBelowThatAffectsMyMovement()).getBlock().getSpeedFactor() : speedFactorHere;
/*      */   }
/*      */   
/*      */   protected Vec3 maybeBackOffFromEdge(Vec3 delta, MoverType moverType) {
/* 1222 */     return delta;
/*      */   }
/*      */   
/*      */   protected Vec3 limitPistonMovement(Vec3 vec) {
/* 1227 */     if (vec.lengthSqr() <= 1.0E-7D)
/* 1228 */       return vec; 
/* 1231 */     long currentGameTime = level().getGameTime();
/* 1232 */     if (currentGameTime != this.pistonDeltasGameTime) {
/* 1233 */       Arrays.fill(this.pistonDeltas, 0.0D);
/* 1234 */       this.pistonDeltasGameTime = currentGameTime;
/*      */     } 
/* 1237 */     if (vec.x != 0.0D) {
/* 1238 */       double xa = applyPistonMovementRestriction(Direction.Axis.X, vec.x);
/* 1239 */       return (Math.abs(xa) <= 9.999999747378752E-6D) ? Vec3.ZERO : new Vec3(xa, 0.0D, 0.0D);
/*      */     } 
/* 1241 */     if (vec.y != 0.0D) {
/* 1242 */       double ya = applyPistonMovementRestriction(Direction.Axis.Y, vec.y);
/* 1243 */       return (Math.abs(ya) <= 9.999999747378752E-6D) ? Vec3.ZERO : new Vec3(0.0D, ya, 0.0D);
/*      */     } 
/* 1245 */     if (vec.z != 0.0D) {
/* 1246 */       double za = applyPistonMovementRestriction(Direction.Axis.Z, vec.z);
/* 1247 */       return (Math.abs(za) <= 9.999999747378752E-6D) ? Vec3.ZERO : new Vec3(0.0D, 0.0D, za);
/*      */     } 
/* 1250 */     return Vec3.ZERO;
/*      */   }
/*      */   
/*      */   private double applyPistonMovementRestriction(Direction.Axis axis, double amount) {
/* 1254 */     int ordinal = axis.ordinal();
/* 1255 */     double min = Mth.clamp(amount + this.pistonDeltas[ordinal], -0.51D, 0.51D);
/* 1256 */     amount = min - this.pistonDeltas[ordinal];
/* 1257 */     this.pistonDeltas[ordinal] = min;
/* 1258 */     return amount;
/*      */   }
/*      */   
/*      */   public double getAvailableSpaceBelow(double maxDistance) {
/* 1262 */     AABB aabb = getBoundingBox();
/* 1263 */     AABB below = aabb.setMinY(aabb.minY - maxDistance).setMaxY(aabb.minY);
/* 1264 */     List<VoxelShape> colliders = collectAllColliders(this, this.level, below);
/* 1266 */     if (colliders.isEmpty())
/* 1267 */       return maxDistance; 
/* 1269 */     return -Shapes.collide(Direction.Axis.Y, aabb, colliders, -maxDistance);
/*      */   }
/*      */   
/*      */   private Vec3 collide(Vec3 movement) {
/* 1273 */     AABB aabb = getBoundingBox();
/* 1276 */     List<VoxelShape> entityColliders = level().getEntityCollisions(this, aabb.expandTowards(movement));
/* 1277 */     Vec3 movementStep = (movement.lengthSqr() == 0.0D) ? movement : collideBoundingBox(this, movement, aabb, level(), entityColliders);
/* 1280 */     boolean xCollision = (movement.x != movementStep.x);
/* 1281 */     boolean yCollision = (movement.y != movementStep.y);
/* 1282 */     boolean zCollision = (movement.z != movementStep.z);
/* 1284 */     boolean onGroundAfterCollision = (yCollision && movement.y < 0.0D);
/* 1286 */     if (maxUpStep() > 0.0F && (onGroundAfterCollision || onGround()) && (xCollision || zCollision)) {
/* 1288 */       AABB groundedAABB = onGroundAfterCollision ? aabb.move(0.0D, movementStep.y, 0.0D) : aabb;
/* 1290 */       AABB stepUpAABB = groundedAABB.expandTowards(movement.x, maxUpStep(), movement.z);
/* 1293 */       if (!onGroundAfterCollision)
/* 1294 */         stepUpAABB = stepUpAABB.expandTowards(0.0D, -9.999999747378752E-6D, 0.0D); 
/* 1297 */       List<VoxelShape> colliders = collectCollidersIgnoringWorldBorder(this, this.level, entityColliders, stepUpAABB);
/* 1300 */       float stepHeightToSkip = (float)movementStep.y;
/* 1301 */       float[] candidateStepUpHeights = collectCandidateStepUpHeights(groundedAABB, colliders, maxUpStep(), stepHeightToSkip);
/* 1303 */       for (float candidateStepUpHeight : candidateStepUpHeights) {
/* 1304 */         Vec3 stepFromGround = collideWithShapes(new Vec3(movement.x, candidateStepUpHeight, movement.z), groundedAABB, colliders);
/* 1306 */         if (stepFromGround.horizontalDistanceSqr() > movementStep.horizontalDistanceSqr()) {
/* 1308 */           double distanceToGround = aabb.minY - groundedAABB.minY;
/* 1309 */           return stepFromGround.subtract(0.0D, distanceToGround, 0.0D);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1313 */     return movementStep;
/*      */   }
/*      */   
/*      */   private static float[] collectCandidateStepUpHeights(AABB boundingBox, List<VoxelShape> colliders, float maxStepHeight, float stepHeightToSkip) {
/* 1318 */     FloatArraySet floatArraySet = new FloatArraySet(4);
/* 1319 */     for (VoxelShape collider : colliders) {
/* 1320 */       DoubleList coords = collider.getCoords(Direction.Axis.Y);
/* 1321 */       for (DoubleListIterator<Double> doubleListIterator = coords.iterator(); doubleListIterator.hasNext(); ) {
/* 1321 */         double coord = ((Double)doubleListIterator.next()).doubleValue();
/* 1322 */         float relativeCoord = (float)(coord - boundingBox.minY);
/* 1324 */         if (relativeCoord < 0.0F)
/*      */           continue; 
/* 1326 */         if (relativeCoord == stepHeightToSkip)
/*      */           continue; 
/* 1328 */         if (relativeCoord > maxStepHeight)
/*      */           break; 
/* 1331 */         floatArraySet.add(relativeCoord);
/*      */       } 
/*      */     } 
/* 1335 */     float[] sortedCandidates = floatArraySet.toFloatArray();
/* 1336 */     FloatArrays.unstableSort(sortedCandidates);
/* 1337 */     return sortedCandidates;
/*      */   }
/*      */   
/*      */   public static Vec3 collideBoundingBox(Entity source, Vec3 movement, AABB boundingBox, Level level, List<VoxelShape> entityColliders) {
/* 1341 */     List<VoxelShape> colliders = collectCollidersIgnoringWorldBorder(source, level, entityColliders, boundingBox.expandTowards(movement));
/* 1342 */     return collideWithShapes(movement, boundingBox, colliders);
/*      */   }
/*      */   
/*      */   public static Vec3 collideBoundingBox(CollisionContext source, Vec3 movement, AABB boundingBox, Level level, List<VoxelShape> entityColliders) {
/* 1346 */     List<VoxelShape> colliders = collectCollidersIgnoringWorldBorder(source, level, entityColliders, boundingBox.expandTowards(movement));
/* 1347 */     return collideWithShapes(movement, boundingBox, colliders);
/*      */   }
/*      */   
/*      */   public static List<VoxelShape> collectAllColliders(Entity source, Level level, AABB boundingBox) {
/* 1351 */     List<VoxelShape> entityColliders = level.getEntityCollisions(source, boundingBox);
/* 1352 */     return collectCollidersIgnoringWorldBorder(source, level, entityColliders, boundingBox);
/*      */   }
/*      */   
/*      */   private static List<VoxelShape> collectCollidersIgnoringWorldBorder(Entity source, Level level, List<VoxelShape> entityColliders, AABB boundingBox) {
/* 1356 */     ImmutableList.Builder<VoxelShape> colliders = ImmutableList.builderWithExpectedSize(entityColliders.size() + 1);
/* 1358 */     if (!entityColliders.isEmpty())
/* 1359 */       colliders.addAll(entityColliders); 
/* 1362 */     WorldBorder worldBorder = level.getWorldBorder();
/* 1363 */     boolean isEntityInsideCloseToBorder = (source != null && worldBorder.isInsideCloseToBorder(source, boundingBox));
/* 1365 */     if (isEntityInsideCloseToBorder)
/* 1366 */       colliders.add(worldBorder.getCollisionShape()); 
/* 1369 */     colliders.addAll(level.getBlockCollisions(source, boundingBox));
/* 1370 */     return (List<VoxelShape>)colliders.build();
/*      */   }
/*      */   
/*      */   private static List<VoxelShape> collectCollidersIgnoringWorldBorder(CollisionContext source, Level level, List<VoxelShape> entityColliders, AABB boundingBox) {
/* 1374 */     ImmutableList.Builder<VoxelShape> colliders = ImmutableList.builderWithExpectedSize(entityColliders.size() + 1);
/* 1376 */     if (!entityColliders.isEmpty())
/* 1377 */       colliders.addAll(entityColliders); 
/* 1380 */     colliders.addAll(level.getBlockCollisionsFromContext(source, boundingBox));
/* 1381 */     return (List<VoxelShape>)colliders.build();
/*      */   }
/*      */   
/*      */   private static Vec3 collideWithShapes(Vec3 movement, AABB boundingBox, List<VoxelShape> shapes) {
/* 1385 */     if (shapes.isEmpty())
/* 1386 */       return movement; 
/* 1389 */     Vec3 resolvedMovement = Vec3.ZERO;
/* 1390 */     for (UnmodifiableIterator<Direction.Axis> unmodifiableIterator = Direction.axisStepOrder(movement).iterator(); unmodifiableIterator.hasNext(); ) {
/* 1390 */       Direction.Axis axis = unmodifiableIterator.next();
/* 1391 */       double axisMovement = movement.get(axis);
/* 1392 */       if (axisMovement == 0.0D)
/*      */         continue; 
/* 1395 */       double collision = Shapes.collide(axis, boundingBox.move(resolvedMovement), shapes, axisMovement);
/* 1396 */       resolvedMovement = resolvedMovement.with(axis, collision);
/*      */     } 
/* 1398 */     return resolvedMovement;
/*      */   }
/*      */   
/*      */   protected float nextStep() {
/* 1402 */     return ((int)this.moveDist + 1);
/*      */   }
/*      */   
/*      */   protected SoundEvent getSwimSound() {
/* 1406 */     return SoundEvents.GENERIC_SWIM;
/*      */   }
/*      */   
/*      */   protected SoundEvent getSwimSplashSound() {
/* 1410 */     return SoundEvents.GENERIC_SPLASH;
/*      */   }
/*      */   
/*      */   protected SoundEvent getSwimHighSpeedSplashSound() {
/* 1414 */     return SoundEvents.GENERIC_SPLASH;
/*      */   }
/*      */   
/*      */   private void checkInsideBlocks(List<Movement> movements, InsideBlockEffectApplier.StepBasedCollector effectCollector) {
/* 1418 */     if (!isAffectedByBlocks())
/*      */       return; 
/* 1422 */     LongSet visitedBlocks = this.visitedBlocks;
/* 1423 */     for (Movement movement : movements) {
/* 1424 */       Vec3 pos = movement.from;
/* 1425 */       Vec3 delta = movement.to().subtract(movement.from());
/* 1426 */       int maxMovementIterations = 16;
/* 1427 */       if (movement.axisDependentOriginalMovement().isPresent() && delta.lengthSqr() > 0.0D) {
/* 1434 */         for (UnmodifiableIterator<Direction.Axis> unmodifiableIterator = Direction.axisStepOrder(movement.axisDependentOriginalMovement().get()).iterator(); unmodifiableIterator.hasNext(); ) {
/* 1434 */           Direction.Axis axis = unmodifiableIterator.next();
/* 1435 */           double axisMove = delta.get(axis);
/* 1436 */           if (axisMove != 0.0D) {
/* 1437 */             Vec3 to = pos.relative(axis.getPositive(), axisMove);
/* 1438 */             maxMovementIterations -= checkInsideBlocks(pos, to, effectCollector, visitedBlocks, maxMovementIterations);
/* 1439 */             pos = to;
/*      */           } 
/*      */         } 
/*      */       } else {
/* 1443 */         maxMovementIterations -= checkInsideBlocks(movement.from(), movement.to(), effectCollector, visitedBlocks, 16);
/*      */       } 
/* 1446 */       if (maxMovementIterations <= 0)
/* 1448 */         checkInsideBlocks(movement.to(), movement.to(), effectCollector, visitedBlocks, 1); 
/*      */     } 
/* 1451 */     visitedBlocks.clear();
/*      */   }
/*      */   
/*      */   private int checkInsideBlocks(Vec3 from, Vec3 to, InsideBlockEffectApplier.StepBasedCollector effectCollector, LongSet visitedBlocks, int maxMovementIterations) {
/* 1457 */     AABB deflatedBoundingBoxAtTarget = makeBoundingBox(to).deflate(9.999999747378752E-6D);
/* 1459 */     boolean movedFar = (from.distanceToSqr(to) > Mth.square(0.9999900000002526D));
/* 1460 */     Level level = this.level;
/* 1460 */     if (level instanceof ServerLevel) {
/* 1460 */       ServerLevel serverLevel = (ServerLevel)level;
/* 1460 */       if (serverLevel.getServer().debugSubscribers().hasAnySubscriberFor(DebugSubscriptions.ENTITY_BLOCK_INTERSECTIONS));
/*      */     } 
/* 1460 */     boolean debugEntityBlockIntersections = false;
/* 1462 */     AtomicInteger iterations = new AtomicInteger();
/* 1463 */     BlockGetter.forEachBlockIntersectedBetween(from, to, deflatedBoundingBoxAtTarget, (blockIntersection, iteration) -> {
/*      */           if (!isAlive())
/*      */             return false; 
/*      */           if (iteration >= maxMovementIterations)
/*      */             return false; 
/*      */           iterations.set(iteration);
/*      */           BlockState state = level().getBlockState(blockIntersection);
/*      */           if (state.isAir()) {
/*      */             if (debugEntityBlockIntersections)
/*      */               debugBlockIntersection((ServerLevel)level(), blockIntersection.immutable(), false, false); 
/*      */             return true;
/*      */           } 
/*      */           VoxelShape intersectShape = state.getEntityInsideCollisionShape((BlockGetter)level(), blockIntersection, this);
/* 1484 */           boolean insideBlock = (intersectShape == Shapes.block() || collidedWithShapeMovingFrom(from, to, intersectShape.move(new Vec3((Vec3i)blockIntersection)).toAabbs()));
/*      */           boolean insideFluid = collidedWithFluid(state.getFluidState(), blockIntersection, from, to);
/*      */           if ((!insideBlock && !insideFluid) || !visitedBlocks.add(blockIntersection.asLong()))
/*      */             return true; 
/*      */           if (insideBlock)
/*      */             try {
/* 1496 */               boolean isPrecise = (movedFar || deflatedBoundingBoxAtTarget.intersects(blockIntersection));
/*      */               effectCollector.advanceStep(iteration);
/*      */               state.entityInside(level(), blockIntersection, this, effectCollector, isPrecise);
/*      */               onInsideBlock(state);
/* 1501 */             } catch (Throwable t) {
/*      */               CrashReport report = CrashReport.forThrowable(t, "Colliding entity with block");
/*      */               CrashReportCategory category = report.addCategory("Block being collided with");
/*      */               CrashReportCategory.populateBlockDetails(category, (LevelHeightAccessor)level(), blockIntersection, state);
/*      */               CrashReportCategory entityCategory = report.addCategory("Entity being checked for collision");
/*      */               fillCrashReportCategory(entityCategory);
/*      */               throw new ReportedException(report);
/*      */             }  
/*      */           if (insideFluid) {
/*      */             effectCollector.advanceStep(iteration);
/*      */             state.getFluidState().entityInside(level(), blockIntersection, this, effectCollector);
/*      */           } 
/*      */           if (debugEntityBlockIntersections)
/*      */             debugBlockIntersection((ServerLevel)level(), blockIntersection.immutable(), insideBlock, insideFluid); 
/*      */           return true;
/*      */         });
/* 1526 */     return iterations.get() + 1;
/*      */   }
/*      */   
/*      */   private void debugBlockIntersection(ServerLevel level, BlockPos pos, boolean insideBlock, boolean insideFluid) {
/*      */     DebugEntityBlockIntersection type;
/* 1531 */     if (insideFluid) {
/* 1532 */       type = DebugEntityBlockIntersection.IN_FLUID;
/* 1533 */     } else if (insideBlock) {
/* 1534 */       type = DebugEntityBlockIntersection.IN_BLOCK;
/*      */     } else {
/* 1536 */       type = DebugEntityBlockIntersection.IN_AIR;
/*      */     } 
/* 1538 */     level.debugSynchronizers().sendBlockValue(pos, DebugSubscriptions.ENTITY_BLOCK_INTERSECTIONS, type);
/*      */   }
/*      */   
/*      */   public boolean collidedWithFluid(FluidState fluidState, BlockPos blockPos, Vec3 from, Vec3 to) {
/* 1542 */     AABB fluidAABB = fluidState.getAABB((BlockGetter)level(), blockPos);
/* 1543 */     return (fluidAABB != null && collidedWithShapeMovingFrom(from, to, List.of(fluidAABB)));
/*      */   }
/*      */   
/*      */   public boolean collidedWithShapeMovingFrom(Vec3 from, Vec3 to, List<AABB> aabbs) {
/* 1547 */     AABB boundingBoxAtFrom = makeBoundingBox(from);
/* 1548 */     Vec3 travelVector = to.subtract(from);
/* 1549 */     return boundingBoxAtFrom.collidedAlongVector(travelVector, aabbs);
/*      */   }
/*      */   
/*      */   protected void onInsideBlock(BlockState state) {}
/*      */   
/*      */   public BlockPos adjustSpawnLocation(ServerLevel level, BlockPos spawnSuggestion) {
/* 1556 */     BlockPos spawnBlockPos = level.getRespawnData().pos();
/* 1557 */     Vec3 spawnPos = Vec3.atCenterOf((Vec3i)spawnBlockPos);
/* 1558 */     int spawnHeight = level.getChunkAt(spawnBlockPos).getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnBlockPos.getX(), spawnBlockPos.getZ()) + 1;
/* 1559 */     return BlockPos.containing(spawnPos.x, spawnHeight, spawnPos.z);
/*      */   }
/*      */   
/*      */   public void gameEvent(Holder<GameEvent> event, Entity sourceEntity) {
/* 1563 */     level().gameEvent(sourceEntity, event, this.position);
/*      */   }
/*      */   
/*      */   public void gameEvent(Holder<GameEvent> event) {
/* 1567 */     gameEvent(event, this);
/*      */   }
/*      */   
/*      */   private void walkingStepSound(BlockPos onPos, BlockState onState) {
/* 1571 */     playStepSound(onPos, onState);
/* 1573 */     if (shouldPlayAmethystStepSound(onState))
/* 1574 */       playAmethystStepSound(); 
/*      */   }
/*      */   
/*      */   protected void waterSwimSound() {
/* 1579 */     Entity entity = Objects.<Entity>requireNonNullElse(getControllingPassenger(), this);
/* 1580 */     float volumeModifier = (entity == this) ? 0.35F : 0.4F;
/* 1581 */     Vec3 deltaMovement = entity.getDeltaMovement();
/* 1582 */     float speed = Math.min(1.0F, (float)Math.sqrt(deltaMovement.x * deltaMovement.x * 0.20000000298023224D + deltaMovement.y * deltaMovement.y + deltaMovement.z * deltaMovement.z * 0.20000000298023224D) * volumeModifier);
/* 1583 */     playSwimSound(speed);
/*      */   }
/*      */   
/*      */   protected BlockPos getPrimaryStepSoundBlockPos(BlockPos affectingPos) {
/* 1587 */     BlockPos abovePos = affectingPos.above();
/* 1588 */     BlockState aboveState = level().getBlockState(abovePos);
/* 1589 */     if (aboveState.is(BlockTags.INSIDE_STEP_SOUND_BLOCKS) || aboveState.is(BlockTags.COMBINATION_STEP_SOUND_BLOCKS))
/* 1590 */       return abovePos; 
/* 1592 */     return affectingPos;
/*      */   }
/*      */   
/*      */   protected void playCombinationStepSounds(BlockState primaryStepSound, BlockState secondaryStepSound) {
/* 1597 */     SoundType primaryStepSoundType = primaryStepSound.getSoundType();
/* 1598 */     playSound(primaryStepSoundType.getStepSound(), primaryStepSoundType.getVolume() * 0.15F, primaryStepSoundType.getPitch());
/* 1599 */     playMuffledStepSound(secondaryStepSound);
/*      */   }
/*      */   
/*      */   protected void playMuffledStepSound(BlockState blockState) {
/* 1603 */     SoundType secondaryStepSoundType = blockState.getSoundType();
/* 1604 */     playSound(secondaryStepSoundType.getStepSound(), secondaryStepSoundType.getVolume() * 0.05F, secondaryStepSoundType.getPitch() * 0.8F);
/*      */   }
/*      */   
/*      */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 1608 */     SoundType soundType = blockState.getSoundType();
/* 1609 */     playSound(soundType.getStepSound(), soundType.getVolume() * 0.15F, soundType.getPitch());
/*      */   }
/*      */   
/*      */   private boolean shouldPlayAmethystStepSound(BlockState affectingState) {
/* 1613 */     return (affectingState.is(BlockTags.CRYSTAL_SOUND_BLOCKS) && this.tickCount >= this.lastCrystalSoundPlayTick + 20);
/*      */   }
/*      */   
/*      */   private void playAmethystStepSound() {
/* 1618 */     this.crystalSoundIntensity *= (float)Math.pow(0.997D, (this.tickCount - this.lastCrystalSoundPlayTick));
/* 1619 */     this.crystalSoundIntensity = Math.min(1.0F, this.crystalSoundIntensity + 0.07F);
/* 1621 */     float pitch = 0.5F + this.crystalSoundIntensity * this.random.nextFloat() * 1.2F;
/* 1622 */     float volume = 0.1F + this.crystalSoundIntensity * 1.2F;
/* 1623 */     playSound(SoundEvents.AMETHYST_BLOCK_CHIME, volume, pitch);
/* 1624 */     this.lastCrystalSoundPlayTick = this.tickCount;
/*      */   }
/*      */   
/*      */   protected void playSwimSound(float volume) {
/* 1628 */     playSound(getSwimSound(), volume, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/*      */   }
/*      */   
/*      */   protected void onFlap() {}
/*      */   
/*      */   protected boolean isFlapping() {
/* 1635 */     return false;
/*      */   }
/*      */   
/*      */   public void playSound(SoundEvent sound, float volume, float pitch) {
/* 1641 */     if (!isSilent())
/* 1642 */       level().playSound(null, getX(), getY(), getZ(), sound, getSoundSource(), volume, pitch); 
/*      */   }
/*      */   
/*      */   public void playSound(SoundEvent sound) {
/* 1647 */     if (!isSilent())
/* 1648 */       playSound(sound, 1.0F, 1.0F); 
/*      */   }
/*      */   
/*      */   public boolean isSilent() {
/* 1653 */     return ((Boolean)this.entityData.get(DATA_SILENT)).booleanValue();
/*      */   }
/*      */   
/*      */   public void setSilent(boolean silent) {
/* 1657 */     this.entityData.set(DATA_SILENT, Boolean.valueOf(silent));
/*      */   }
/*      */   
/*      */   public boolean isNoGravity() {
/* 1661 */     return ((Boolean)this.entityData.get(DATA_NO_GRAVITY)).booleanValue();
/*      */   }
/*      */   
/*      */   public void setNoGravity(boolean noGravity) {
/* 1665 */     this.entityData.set(DATA_NO_GRAVITY, Boolean.valueOf(noGravity));
/*      */   }
/*      */   
/*      */   protected double getDefaultGravity() {
/* 1669 */     return 0.0D;
/*      */   }
/*      */   
/*      */   public final double getGravity() {
/* 1673 */     return isNoGravity() ? 0.0D : getDefaultGravity();
/*      */   }
/*      */   
/*      */   protected void applyGravity() {
/* 1677 */     double gravity = getGravity();
/* 1678 */     if (gravity != 0.0D)
/* 1679 */       setDeltaMovement(getDeltaMovement().add(0.0D, -gravity, 0.0D)); 
/*      */   }
/*      */   
/*      */   protected float getAirDrag() {
/* 1684 */     return 0.98F;
/*      */   }
/*      */   
/*      */   protected MovementEmission getMovementEmission() {
/* 1696 */     return MovementEmission.ALL;
/*      */   }
/*      */   
/*      */   public boolean dampensVibrations() {
/* 1700 */     return false;
/*      */   }
/*      */   
/*      */   public final void doCheckFallDamage(double xa, double ya, double za, boolean onGround) {
/* 1704 */     if (touchingUnloadedChunk())
/*      */       return; 
/* 1707 */     checkSupportingBlock(onGround, new Vec3(xa, ya, za));
/* 1708 */     BlockPos pos = getOnPosLegacy();
/* 1709 */     BlockState state = level().getBlockState(pos);
/* 1711 */     checkFallDamage(ya, onGround, state, pos);
/*      */   }
/*      */   
/*      */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {
/* 1715 */     if (!isInWater() && ya < 0.0D)
/* 1716 */       this.fallDistance -= (float)ya; 
/* 1718 */     if (onGround) {
/* 1719 */       if (this.fallDistance > 0.0D) {
/* 1720 */         onState.getBlock().fallOn(level(), onState, pos, this, this.fallDistance);
/* 1723 */         level().gameEvent((Holder)GameEvent.HIT_GROUND, this.position, GameEvent.Context.of(this, this.mainSupportingBlockPos.<BlockState>map(blockPos -> level().getBlockState(blockPos)).orElse(onState)));
/*      */       } 
/* 1725 */       resetFallDistance();
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean fireImmune() {
/* 1730 */     return getType().fireImmune();
/*      */   }
/*      */   
/*      */   public boolean causeFallDamage(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 1735 */     if (is(EntityTypeTags.FALL_DAMAGE_IMMUNE))
/* 1736 */       return false; 
/* 1738 */     propagateFallToPassengers(fallDistance, damageModifier, damageSource);
/* 1739 */     return false;
/*      */   }
/*      */   
/*      */   protected void propagateFallToPassengers(double fallDistance, float damageModifier, DamageSource damageSource) {
/* 1743 */     if (isVehicle())
/* 1744 */       for (Entity passenger : getPassengers())
/* 1745 */         passenger.causeFallDamage(fallDistance, damageModifier, damageSource);  
/*      */   }
/*      */   
/*      */   public boolean isInWater() {
/* 1751 */     return this.wasTouchingWater;
/*      */   }
/*      */   
/*      */   private boolean isInRain() {
/* 1755 */     BlockPos pos = blockPosition();
/* 1756 */     return (level().isRainingAt(pos) || level().isRainingAt(BlockPos.containing(pos.getX(), (getBoundingBox()).maxY, pos.getZ())));
/*      */   }
/*      */   
/*      */   public boolean isInWaterOrRain() {
/* 1760 */     return (isInWater() || isInRain());
/*      */   }
/*      */   
/*      */   public boolean isInLiquid() {
/* 1764 */     return (isInWater() || isInLava());
/*      */   }
/*      */   
/*      */   public boolean isUnderWater() {
/* 1768 */     return (this.wasEyeInWater && isInWater());
/*      */   }
/*      */   
/*      */   public boolean isInShallowWater() {
/* 1772 */     return (isInWater() && !isUnderWater());
/*      */   }
/*      */   
/*      */   public boolean isInClouds() {
/* 1776 */     if (ARGB.alpha(((Integer)this.level.environmentAttributes().getValue(EnvironmentAttributes.CLOUD_COLOR, position())).intValue()) == 0)
/* 1777 */       return false; 
/* 1779 */     float cloudBottom = ((Float)this.level.environmentAttributes().getValue(EnvironmentAttributes.CLOUD_HEIGHT, position())).floatValue();
/* 1780 */     if (getY() + getBbHeight() < cloudBottom)
/* 1781 */       return false; 
/* 1783 */     float cloudTop = cloudBottom + 4.0F;
/* 1784 */     return (getY() <= cloudTop);
/*      */   }
/*      */   
/*      */   public void updateSwimming() {
/* 1788 */     if (isSwimming()) {
/* 1789 */       setSwimming((isSprinting() && isInWater() && !isPassenger()));
/*      */     } else {
/* 1791 */       setSwimming((isSprinting() && isUnderWater() && !isPassenger() && level().getFluidState(this.blockPosition).is(FluidTags.WATER)));
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean updateFluidInteraction() {
/* 1797 */     this.fluidInteraction.update(this, !isPushedByFluid());
/* 1799 */     boolean inWater = this.fluidInteraction.isInFluid(FluidTags.WATER);
/* 1800 */     boolean inLava = this.fluidInteraction.isInFluid(FluidTags.LAVA);
/* 1802 */     if (inWater) {
/* 1803 */       resetFallDistance();
/* 1804 */       if (!this.wasTouchingWater && !this.firstTick)
/* 1805 */         doWaterSplashEffect(); 
/*      */     } 
/* 1808 */     this.wasTouchingWater = inWater;
/* 1810 */     if (isPushedByFluid()) {
/* 1811 */       if (inWater)
/* 1812 */         this.fluidInteraction.applyCurrentTo(FluidTags.WATER, this, 0.014D); 
/* 1814 */       if (inLava) {
/* 1815 */         double lavaFlowScale = ((Boolean)this.level.environmentAttributes().getDimensionValue(EnvironmentAttributes.FAST_LAVA)).booleanValue() ? 0.007D : 0.0023333333333333335D;
/* 1816 */         this.fluidInteraction.applyCurrentTo(FluidTags.LAVA, this, lavaFlowScale);
/*      */       } 
/*      */     } 
/* 1820 */     return (inWater || inLava);
/*      */   }
/*      */   
/*      */   protected void doWaterSplashEffect() {
/* 1824 */     Entity entity = Objects.<Entity>requireNonNullElse(getControllingPassenger(), this);
/* 1825 */     float volumeModifier = (entity == this) ? 0.2F : 0.9F;
/* 1826 */     Vec3 movement = entity.getDeltaMovement();
/* 1829 */     float speed = Math.min(1.0F, (float)Math.sqrt(movement.x * movement.x * 0.20000000298023224D + movement.y * movement.y + movement.z * movement.z * 0.20000000298023224D) * volumeModifier);
/* 1831 */     if (speed < 0.25F) {
/* 1832 */       playSound(getSwimSplashSound(), speed, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/*      */     } else {
/* 1834 */       playSound(getSwimHighSpeedSplashSound(), speed, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
/*      */     } 
/* 1837 */     float yt = Mth.floor(getY());
/*      */     int i;
/* 1838 */     for (i = 0; i < 1.0F + this.dimensions.width() * 20.0F; i++) {
/* 1839 */       double xo = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width();
/* 1840 */       double zo = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width();
/* 1841 */       level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, getX() + xo, (yt + 1.0F), getZ() + zo, movement.x, movement.y - this.random.nextDouble() * 0.20000000298023224D, movement.z);
/*      */     } 
/* 1843 */     for (i = 0; i < 1.0F + this.dimensions.width() * 20.0F; i++) {
/* 1844 */       double xo = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width();
/* 1845 */       double zo = (this.random.nextDouble() * 2.0D - 1.0D) * this.dimensions.width();
/* 1846 */       level().addParticle((ParticleOptions)ParticleTypes.SPLASH, getX() + xo, (yt + 1.0F), getZ() + zo, movement.x, movement.y, movement.z);
/*      */     } 
/* 1849 */     gameEvent((Holder<GameEvent>)GameEvent.SPLASH);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected BlockState getBlockStateOnLegacy() {
/* 1860 */     return level().getBlockState(getOnPosLegacy());
/*      */   }
/*      */   
/*      */   public BlockState getBlockStateOn() {
/* 1864 */     return level().getBlockState(getOnPos());
/*      */   }
/*      */   
/*      */   public boolean canSpawnSprintParticle() {
/* 1868 */     return (isSprinting() && !isInWater() && !isSpectator() && !isCrouching() && !isInLava() && isAlive());
/*      */   }
/*      */   
/*      */   protected void spawnSprintParticle() {
/* 1872 */     BlockPos pos = getOnPosLegacy();
/* 1873 */     BlockState blockState = level().getBlockState(pos);
/* 1874 */     if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
/* 1875 */       Vec3 movement = getDeltaMovement();
/* 1876 */       BlockPos entityPosition = blockPosition();
/* 1877 */       double x = getX() + (this.random.nextDouble() - 0.5D) * this.dimensions.width();
/* 1878 */       double z = getZ() + (this.random.nextDouble() - 0.5D) * this.dimensions.width();
/* 1879 */       if (entityPosition.getX() != pos.getX())
/* 1881 */         x = Mth.clamp(x, pos.getX(), pos.getX() + 1.0D); 
/* 1883 */       if (entityPosition.getZ() != pos.getZ())
/* 1885 */         z = Mth.clamp(z, pos.getZ(), pos.getZ() + 1.0D); 
/* 1887 */       level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, blockState), x, getY() + 0.1D, z, movement.x * -4.0D, 1.5D, movement.z * -4.0D);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isEyeInFluid(TagKey<Fluid> type) {
/* 1892 */     return this.fluidInteraction.isEyeInFluid(type);
/*      */   }
/*      */   
/*      */   public boolean isInLava() {
/* 1896 */     return (!this.firstTick && this.fluidInteraction.isInFluid(FluidTags.LAVA));
/*      */   }
/*      */   
/*      */   public void moveRelative(float speed, Vec3 input) {
/* 1900 */     Vec3 delta = getInputVector(input, speed, getYRot());
/* 1902 */     setDeltaMovement(getDeltaMovement().add(delta));
/*      */   }
/*      */   
/*      */   protected static Vec3 getInputVector(Vec3 input, float speed, float yRot) {
/* 1906 */     double length = input.lengthSqr();
/* 1907 */     if (length < 1.0E-7D)
/* 1908 */       return Vec3.ZERO; 
/* 1912 */     Vec3 movement = ((length > 1.0D) ? input.normalize() : input).scale(speed);
/* 1914 */     float sin = Mth.sin((yRot * 0.017453292F));
/* 1915 */     float cos = Mth.cos((yRot * 0.017453292F));
/* 1916 */     return new Vec3(movement.x * cos - movement.z * sin, movement.y, movement.z * cos + movement.x * sin);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public float getLightLevelDependentMagicValue() {
/* 1922 */     if (level().hasChunkAt(getBlockX(), getBlockZ()))
/* 1923 */       return level().getLightLevelDependentMagicValue(BlockPos.containing(getX(), getEyeY(), getZ())); 
/* 1925 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public void absSnapTo(double x, double y, double z, float yRot, float xRot) {
/* 1929 */     absSnapTo(x, y, z);
/* 1931 */     absSnapRotationTo(yRot, xRot);
/*      */   }
/*      */   
/*      */   public void absSnapRotationTo(float yRot, float xRot) {
/* 1935 */     setYRot(yRot % 360.0F);
/* 1936 */     setXRot(Mth.clamp(xRot, -90.0F, 90.0F) % 360.0F);
/* 1938 */     this.yRotO = getYRot();
/* 1939 */     this.xRotO = getXRot();
/*      */   }
/*      */   
/*      */   public void absSnapTo(double x, double y, double z) {
/* 1943 */     double cx = Mth.clamp(x, -3.0E7D, 3.0E7D);
/* 1944 */     double cz = Mth.clamp(z, -3.0E7D, 3.0E7D);
/* 1946 */     this.xo = cx;
/* 1947 */     this.yo = y;
/* 1948 */     this.zo = cz;
/* 1950 */     setPos(cx, y, cz);
/*      */   }
/*      */   
/*      */   public void snapTo(Vec3 pos) {
/* 1954 */     snapTo(pos.x, pos.y, pos.z);
/*      */   }
/*      */   
/*      */   public void snapTo(double x, double y, double z) {
/* 1958 */     snapTo(x, y, z, getYRot(), getXRot());
/*      */   }
/*      */   
/*      */   public void snapTo(BlockPos spawnPos, float yRot, float xRot) {
/* 1962 */     snapTo(Vec3.atBottomCenterOf((Vec3i)spawnPos), yRot, xRot);
/*      */   }
/*      */   
/*      */   public void snapTo(Vec3 spawnPos, float yRot, float xRot) {
/* 1966 */     snapTo(spawnPos.x, spawnPos.y, spawnPos.z, yRot, xRot);
/*      */   }
/*      */   
/*      */   public void snapTo(double x, double y, double z, float yRot, float xRot) {
/* 1970 */     setPosRaw(x, y, z);
/* 1971 */     setYRot(yRot);
/* 1972 */     setXRot(xRot);
/* 1974 */     setOldPosAndRot();
/* 1976 */     reapplyPosition();
/*      */   }
/*      */   
/*      */   public final void setOldPosAndRot() {
/* 1980 */     setOldPos();
/* 1981 */     setOldRot();
/*      */   }
/*      */   
/*      */   public final void setOldPosAndRot(Vec3 position, float yRot, float xRot) {
/* 1985 */     setOldPos(position);
/* 1986 */     setOldRot(yRot, xRot);
/*      */   }
/*      */   
/*      */   protected void setOldPos() {
/* 1990 */     setOldPos(this.position);
/*      */   }
/*      */   
/*      */   public void setOldRot() {
/* 1994 */     setOldRot(getYRot(), getXRot());
/*      */   }
/*      */   
/*      */   private void setOldPos(Vec3 position) {
/* 1998 */     this.xo = this.xOld = position.x;
/* 1999 */     this.yo = this.yOld = position.y;
/* 2000 */     this.zo = this.zOld = position.z;
/*      */   }
/*      */   
/*      */   private void setOldRot(float yRot, float xRot) {
/* 2004 */     this.yRotO = yRot;
/* 2005 */     this.xRotO = xRot;
/*      */   }
/*      */   
/*      */   public final Vec3 oldPosition() {
/* 2009 */     return new Vec3(this.xOld, this.yOld, this.zOld);
/*      */   }
/*      */   
/*      */   public float distanceTo(Entity entity) {
/* 2013 */     float xd = (float)(getX() - entity.getX());
/* 2014 */     float yd = (float)(getY() - entity.getY());
/* 2015 */     float zd = (float)(getZ() - entity.getZ());
/* 2016 */     return Mth.sqrt(xd * xd + yd * yd + zd * zd);
/*      */   }
/*      */   
/*      */   public double distanceToSqr(double x2, double y2, double z2) {
/* 2020 */     double xd = getX() - x2;
/* 2021 */     double yd = getY() - y2;
/* 2022 */     double zd = getZ() - z2;
/* 2023 */     return xd * xd + yd * yd + zd * zd;
/*      */   }
/*      */   
/*      */   public double distanceToSqr(Entity entity) {
/* 2027 */     return distanceToSqr(entity.position());
/*      */   }
/*      */   
/*      */   public double distanceToSqr(Vec3 pos) {
/* 2031 */     double xd = getX() - pos.x;
/* 2032 */     double yd = getY() - pos.y;
/* 2033 */     double zd = getZ() - pos.z;
/* 2034 */     return xd * xd + yd * yd + zd * zd;
/*      */   }
/*      */   
/*      */   public void playerTouch(Player player) {}
/*      */   
/*      */   public void push(Entity entity) {
/* 2041 */     if (isPassengerOfSameVehicle(entity))
/*      */       return; 
/* 2044 */     if (entity.noPhysics || this.noPhysics)
/*      */       return; 
/* 2048 */     double xa = entity.getX() - getX();
/* 2049 */     double za = entity.getZ() - getZ();
/* 2051 */     double dd = Mth.absMax(xa, za);
/* 2053 */     if (dd >= 0.009999999776482582D) {
/* 2054 */       dd = Math.sqrt(dd);
/* 2055 */       xa /= dd;
/* 2056 */       za /= dd;
/* 2058 */       double pow = 1.0D / dd;
/* 2059 */       if (pow > 1.0D)
/* 2060 */         pow = 1.0D; 
/* 2062 */       xa *= pow;
/* 2063 */       za *= pow;
/* 2065 */       xa *= 0.05000000074505806D;
/* 2066 */       za *= 0.05000000074505806D;
/* 2068 */       if (!isVehicle() && isPushable())
/* 2069 */         push(-xa, 0.0D, -za); 
/* 2071 */       if (!entity.isVehicle() && entity.isPushable())
/* 2072 */         entity.push(xa, 0.0D, za); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void push(Vec3 impulse) {
/* 2078 */     if (impulse.isFinite())
/* 2079 */       push(impulse.x, impulse.y, impulse.z); 
/*      */   }
/*      */   
/*      */   public void push(double xa, double ya, double za) {
/* 2084 */     if (Double.isFinite(xa) && Double.isFinite(ya) && Double.isFinite(za)) {
/* 2085 */       setDeltaMovement(getDeltaMovement().add(xa, ya, za));
/* 2086 */       this.needsSync = true;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void markHurt() {
/* 2091 */     this.hurtMarked = true;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final void hurt(DamageSource source, float damage) {
/* 2097 */     Level level = this.level;
/* 2097 */     if (level instanceof ServerLevel) {
/* 2097 */       ServerLevel serverLevel = (ServerLevel)level;
/* 2098 */       hurtServer(serverLevel, source, damage);
/*      */     } 
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final boolean hurtOrSimulate(DamageSource source, float damage) {
/* 2107 */     Level level = this.level;
/* 2107 */     if (level instanceof ServerLevel) {
/* 2107 */       ServerLevel serverLevel = (ServerLevel)level;
/* 2108 */       return hurtServer(serverLevel, source, damage);
/*      */     } 
/* 2110 */     return hurtClient(source);
/*      */   }
/*      */   
/*      */   public boolean hurtClient(DamageSource source) {
/* 2119 */     return false;
/*      */   }
/*      */   
/*      */   public final Vec3 getViewVector(float a) {
/* 2123 */     return calculateViewVector(getViewXRot(a), getViewYRot(a));
/*      */   }
/*      */   
/*      */   public Direction getNearestViewDirection() {
/* 2127 */     return Direction.getApproximateNearest(getViewVector(1.0F));
/*      */   }
/*      */   
/*      */   public float getViewXRot(float a) {
/* 2131 */     return getXRot(a);
/*      */   }
/*      */   
/*      */   public float getViewYRot(float a) {
/* 2135 */     return getYRot(a);
/*      */   }
/*      */   
/*      */   public float getXRot(float partialTicks) {
/* 2139 */     if (partialTicks == 1.0F)
/* 2140 */       return getXRot(); 
/* 2142 */     return Mth.lerp(partialTicks, this.xRotO, getXRot());
/*      */   }
/*      */   
/*      */   public float getYRot(float partialTicks) {
/* 2146 */     if (partialTicks == 1.0F)
/* 2147 */       return getYRot(); 
/* 2149 */     return Mth.rotLerp(partialTicks, this.yRotO, getYRot());
/*      */   }
/*      */   
/*      */   public final Vec3 calculateViewVector(float xRot, float yRot) {
/* 2155 */     float realXRot = xRot * 0.017453292F;
/* 2156 */     float realYRot = -yRot * 0.017453292F;
/* 2158 */     float yCos = Mth.cos(realYRot);
/* 2159 */     float ySin = Mth.sin(realYRot);
/* 2160 */     float xCos = Mth.cos(realXRot);
/* 2161 */     float xSin = Mth.sin(realXRot);
/* 2163 */     return new Vec3((ySin * xCos), -xSin, (yCos * xCos));
/*      */   }
/*      */   
/*      */   public final Vec3 getUpVector(float a) {
/* 2167 */     return calculateUpVector(getViewXRot(a), getViewYRot(a));
/*      */   }
/*      */   
/*      */   protected final Vec3 calculateUpVector(float xRot, float yRot) {
/* 2171 */     return calculateViewVector(xRot - 90.0F, yRot);
/*      */   }
/*      */   
/*      */   public final Vec3 getEyePosition() {
/* 2175 */     return new Vec3(getX(), getEyeY(), getZ());
/*      */   }
/*      */   
/*      */   public final Vec3 getEyePosition(float partialTickTime) {
/* 2179 */     double x = Mth.lerp(partialTickTime, this.xo, getX());
/* 2180 */     double y = Mth.lerp(partialTickTime, this.yo, getY()) + getEyeHeight();
/* 2181 */     double z = Mth.lerp(partialTickTime, this.zo, getZ());
/* 2183 */     return new Vec3(x, y, z);
/*      */   }
/*      */   
/*      */   public Vec3 getLightProbePosition(float partialTickTime) {
/* 2187 */     return getEyePosition(partialTickTime);
/*      */   }
/*      */   
/*      */   public final Vec3 getPosition(float partialTickTime) {
/* 2191 */     double endX = Mth.lerp(partialTickTime, this.xo, getX());
/* 2192 */     double endY = Mth.lerp(partialTickTime, this.yo, getY());
/* 2193 */     double endZ = Mth.lerp(partialTickTime, this.zo, getZ());
/* 2194 */     return new Vec3(endX, endY, endZ);
/*      */   }
/*      */   
/*      */   public HitResult pick(double range, float a, boolean withLiquids) {
/* 2198 */     Vec3 from = getEyePosition(a);
/* 2199 */     Vec3 viewVector = getViewVector(a);
/* 2200 */     Vec3 to = from.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);
/* 2201 */     return (HitResult)level().clip(new ClipContext(from, to, ClipContext.Block.OUTLINE, withLiquids ? ClipContext.Fluid.ANY : ClipContext.Fluid.NONE, this));
/*      */   }
/*      */   
/*      */   public boolean canBeHitByProjectile() {
/* 2205 */     return (isAlive() && isPickable());
/*      */   }
/*      */   
/*      */   public boolean isPickable() {
/* 2209 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canBePickedFromInside() {
/* 2213 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isPushable() {
/* 2217 */     return false;
/*      */   }
/*      */   
/*      */   public void awardKillScore(Entity victim, DamageSource killingBlow) {
/* 2221 */     if (victim instanceof ServerPlayer) {
/* 2221 */       ServerPlayer serverPlayer = (ServerPlayer)victim;
/* 2222 */       CriteriaTriggers.ENTITY_KILLED_PLAYER.trigger(serverPlayer, this, killingBlow);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean shouldRender(double camX, double camY, double camZ) {
/* 2227 */     double xd = getX() - camX;
/* 2228 */     double yd = getY() - camY;
/* 2229 */     double zd = getZ() - camZ;
/* 2230 */     double distance = xd * xd + yd * yd + zd * zd;
/* 2231 */     return shouldRenderAtSqrDistance(distance);
/*      */   }
/*      */   
/*      */   public boolean shouldRenderAtSqrDistance(double distance) {
/* 2235 */     double size = getBoundingBox().getSize();
/* 2236 */     if (Double.isNaN(size))
/* 2237 */       size = 1.0D; 
/* 2239 */     size *= 64.0D * viewScale;
/* 2240 */     return (distance < size * size);
/*      */   }
/*      */   
/*      */   public boolean saveAsPassenger(ValueOutput output) {
/* 2244 */     if (this.removalReason != null && !this.removalReason.shouldSave())
/* 2245 */       return false; 
/* 2247 */     String id = getEncodeId();
/* 2248 */     if (id == null)
/* 2249 */       return false; 
/* 2251 */     output.putString("id", id);
/* 2252 */     saveWithoutId(output);
/* 2253 */     return true;
/*      */   }
/*      */   
/*      */   public boolean save(ValueOutput output) {
/* 2257 */     if (isPassenger())
/* 2258 */       return false; 
/* 2260 */     return saveAsPassenger(output);
/*      */   }
/*      */   
/*      */   public void saveWithoutId(ValueOutput output) {
/*      */     try {
/* 2265 */       if (this.vehicle != null) {
/* 2267 */         output.store("Pos", Vec3.CODEC, new Vec3(this.vehicle.getX(), getY(), this.vehicle.getZ()));
/*      */       } else {
/* 2269 */         output.store("Pos", Vec3.CODEC, position());
/*      */       } 
/* 2272 */       output.store("Motion", Vec3.CODEC, getDeltaMovement());
/* 2273 */       output.store("Rotation", Vec2.CODEC, new Vec2(getYRot(), getXRot()));
/* 2275 */       output.putDouble("fall_distance", this.fallDistance);
/* 2276 */       output.putShort("Fire", (short)this.remainingFireTicks);
/* 2277 */       output.putShort("Air", (short)getAirSupply());
/* 2278 */       output.putBoolean("OnGround", onGround());
/* 2279 */       output.putBoolean("Invulnerable", this.invulnerable);
/* 2280 */       output.putInt("PortalCooldown", this.portalCooldown);
/* 2282 */       output.store("UUID", UUIDUtil.CODEC, getUUID());
/* 2284 */       output.storeNullable("CustomName", ComponentSerialization.CODEC, getCustomName());
/* 2286 */       if (isCustomNameVisible())
/* 2287 */         output.putBoolean("CustomNameVisible", isCustomNameVisible()); 
/* 2289 */       if (isSilent())
/* 2290 */         output.putBoolean("Silent", isSilent()); 
/* 2292 */       if (isNoGravity())
/* 2293 */         output.putBoolean("NoGravity", isNoGravity()); 
/* 2295 */       if (this.hasGlowingTag)
/* 2296 */         output.putBoolean("Glowing", true); 
/* 2298 */       int ticksFrozen = getTicksFrozen();
/* 2299 */       if (ticksFrozen > 0)
/* 2300 */         output.putInt("TicksFrozen", getTicksFrozen()); 
/* 2302 */       if (this.hasVisualFire)
/* 2303 */         output.putBoolean("HasVisualFire", this.hasVisualFire); 
/* 2305 */       if (!this.tags.isEmpty())
/* 2306 */         output.store("Tags", TAG_LIST_CODEC, List.copyOf(this.tags)); 
/* 2309 */       if (!this.customData.isEmpty())
/* 2310 */         output.store("data", CustomData.CODEC, this.customData); 
/* 2313 */       addAdditionalSaveData(output);
/* 2315 */       if (isVehicle()) {
/* 2316 */         ValueOutput.ValueOutputList passengersList = output.childrenList("Passengers");
/* 2317 */         for (Entity passenger : getPassengers()) {
/* 2318 */           ValueOutput passengerOutput = passengersList.addChild();
/* 2319 */           if (!passenger.saveAsPassenger(passengerOutput))
/* 2320 */             passengersList.discardLast(); 
/*      */         } 
/* 2323 */         if (passengersList.isEmpty())
/* 2324 */           output.discard("Passengers"); 
/*      */       } 
/* 2327 */     } catch (Throwable t) {
/* 2328 */       CrashReport report = CrashReport.forThrowable(t, "Saving entity NBT");
/* 2329 */       CrashReportCategory category = report.addCategory("Entity being saved");
/* 2330 */       fillCrashReportCategory(category);
/* 2331 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void load(ValueInput input) {
/*      */     try {
/* 2337 */       Vec3 pos = input.read("Pos", Vec3.CODEC).orElse(Vec3.ZERO);
/* 2338 */       Vec3 motion = input.read("Motion", Vec3.CODEC).orElse(Vec3.ZERO);
/* 2339 */       Vec2 rotation = input.read("Rotation", Vec2.CODEC).orElse(Vec2.ZERO);
/* 2342 */       setDeltaMovement(
/* 2343 */           (Math.abs(motion.x) > 10.0D) ? 0.0D : motion.x, 
/* 2344 */           (Math.abs(motion.y) > 10.0D) ? 0.0D : motion.y, 
/* 2345 */           (Math.abs(motion.z) > 10.0D) ? 0.0D : motion.z);
/* 2347 */       this.needsSync = true;
/* 2349 */       double maxHorizontalPosition = 3.0000512E7D;
/* 2350 */       setPosRaw(
/* 2351 */           Mth.clamp(pos.x, -3.0000512E7D, 3.0000512E7D), 
/* 2352 */           Mth.clamp(pos.y, -2.0E7D, 2.0E7D), 
/* 2353 */           Mth.clamp(pos.z, -3.0000512E7D, 3.0000512E7D));
/* 2355 */       setYRot(rotation.x);
/* 2356 */       setXRot(rotation.y);
/* 2358 */       setOldPosAndRot();
/* 2360 */       setYHeadRot(getYRot());
/* 2361 */       setYBodyRot(getYRot());
/* 2363 */       this.fallDistance = input.getDoubleOr("fall_distance", 0.0D);
/* 2364 */       this.remainingFireTicks = input.getShortOr("Fire", (short)0);
/* 2365 */       setAirSupply(input.getIntOr("Air", getMaxAirSupply()));
/* 2367 */       this.onGround = input.getBooleanOr("OnGround", false);
/* 2368 */       this.invulnerable = input.getBooleanOr("Invulnerable", false);
/* 2369 */       this.portalCooldown = input.getIntOr("PortalCooldown", 0);
/* 2371 */       input.read("UUID", UUIDUtil.CODEC).ifPresent(id -> {
/*      */             this.uuid = id;
/*      */             this.stringUUID = this.uuid.toString();
/*      */           });
/* 2376 */       if (!Double.isFinite(getX()) || !Double.isFinite(getY()) || !Double.isFinite(getZ()))
/* 2377 */         throw new IllegalStateException("Entity has invalid position"); 
/* 2379 */       if (!Double.isFinite(getYRot()) || !Double.isFinite(getXRot()))
/* 2380 */         throw new IllegalStateException("Entity has invalid rotation"); 
/* 2383 */       reapplyPosition();
/* 2384 */       setRot(getYRot(), getXRot());
/* 2386 */       setCustomName(input.read("CustomName", ComponentSerialization.CODEC).orElse(null));
/* 2387 */       setCustomNameVisible(input.getBooleanOr("CustomNameVisible", false));
/* 2388 */       setSilent(input.getBooleanOr("Silent", false));
/* 2389 */       setNoGravity(input.getBooleanOr("NoGravity", false));
/* 2390 */       setGlowingTag(input.getBooleanOr("Glowing", false));
/* 2391 */       setTicksFrozen(input.getIntOr("TicksFrozen", 0));
/* 2392 */       this.hasVisualFire = input.getBooleanOr("HasVisualFire", false);
/* 2394 */       this.customData = input.read("data", CustomData.CODEC).orElse(CustomData.EMPTY);
/* 2396 */       this.tags.clear();
/* 2397 */       Objects.requireNonNull(this.tags);
/* 2397 */       input.read("Tags", TAG_LIST_CODEC).ifPresent(this.tags::addAll);
/* 2399 */       readAdditionalSaveData(input);
/* 2401 */       if (repositionEntityAfterLoad())
/* 2402 */         reapplyPosition(); 
/* 2404 */     } catch (Throwable t) {
/* 2405 */       CrashReport report = CrashReport.forThrowable(t, "Loading entity NBT");
/* 2406 */       CrashReportCategory category = report.addCategory("Entity being loaded");
/* 2407 */       fillCrashReportCategory(category);
/* 2408 */       throw new ReportedException(report);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean repositionEntityAfterLoad() {
/* 2413 */     return true;
/*      */   }
/*      */   
/*      */   protected final String getEncodeId() {
/* 2417 */     if (!getType().canSerialize())
/* 2418 */       return null; 
/* 2420 */     ResourceKey<EntityType<?>> typeId = (ResourceKey<EntityType<?>>)typeHolder().unwrapKey().orElseThrow(() -> new IllegalStateException("Unregistered entity"));
/* 2421 */     return typeId.identifier().toString();
/*      */   }
/*      */   
/*      */   public ItemEntity spawnAtLocation(ServerLevel level, ItemLike resource) {
/* 2429 */     return spawnAtLocation(level, new ItemStack(resource), 0.0F);
/*      */   }
/*      */   
/*      */   public ItemEntity spawnAtLocation(ServerLevel level, ItemStack itemStack) {
/* 2433 */     return spawnAtLocation(level, itemStack, 0.0F);
/*      */   }
/*      */   
/*      */   public ItemEntity spawnAtLocation(ServerLevel level, ItemStack itemStack, Vec3 offset) {
/* 2437 */     if (itemStack.isEmpty())
/* 2438 */       return null; 
/* 2440 */     ItemEntity entity = new ItemEntity((Level)level, getX() + offset.x, getY() + offset.y, getZ() + offset.z, itemStack);
/* 2441 */     entity.setDefaultPickUpDelay();
/* 2442 */     level.addFreshEntity((Entity)entity);
/* 2443 */     return entity;
/*      */   }
/*      */   
/*      */   public ItemEntity spawnAtLocation(ServerLevel level, ItemStack itemStack, float offset) {
/* 2447 */     return spawnAtLocation(level, itemStack, new Vec3(0.0D, offset, 0.0D));
/*      */   }
/*      */   
/*      */   public boolean isAlive() {
/* 2451 */     return !isRemoved();
/*      */   }
/*      */   
/*      */   public boolean isInWall() {
/* 2455 */     if (this.noPhysics)
/* 2456 */       return false; 
/* 2459 */     float checkWidth = this.dimensions.width() * 0.8F;
/* 2460 */     AABB eyeBb = AABB.ofSize(getEyePosition(), checkWidth, 1.0E-6D, checkWidth);
/* 2461 */     return BlockPos.betweenClosedStream(eyeBb)
/* 2462 */       .anyMatch(pos -> {
/*      */           BlockState state = level().getBlockState(pos);
/* 2464 */           return (!state.isAir() && state.isSuffocating((BlockGetter)level(), pos) && Shapes.joinIsNotEmpty(state.getCollisionShape((BlockGetter)level(), pos).move((Vec3i)pos), Shapes.create(eyeBb), BooleanOp.AND));
/*      */         });
/*      */   }
/*      */   
/*      */   public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   4: invokevirtual isClientSide : ()Z
/*      */     //   7: ifne -> 197
/*      */     //   10: aload_1
/*      */     //   11: invokevirtual isSecondaryUseActive : ()Z
/*      */     //   14: ifeq -> 197
/*      */     //   17: aload_0
/*      */     //   18: astore #5
/*      */     //   20: aload #5
/*      */     //   22: instanceof net/minecraft/world/entity/Leashable
/*      */     //   25: ifeq -> 197
/*      */     //   28: aload #5
/*      */     //   30: checkcast net/minecraft/world/entity/Leashable
/*      */     //   33: astore #4
/*      */     //   35: aload #4
/*      */     //   37: invokeinterface canBeLeashed : ()Z
/*      */     //   42: ifeq -> 197
/*      */     //   45: aload_0
/*      */     //   46: invokevirtual isAlive : ()Z
/*      */     //   49: ifeq -> 197
/*      */     //   52: aload_0
/*      */     //   53: astore #6
/*      */     //   55: aload #6
/*      */     //   57: instanceof net/minecraft/world/entity/LivingEntity
/*      */     //   60: ifeq -> 78
/*      */     //   63: aload #6
/*      */     //   65: checkcast net/minecraft/world/entity/LivingEntity
/*      */     //   68: astore #5
/*      */     //   70: aload #5
/*      */     //   72: invokevirtual isBaby : ()Z
/*      */     //   75: ifne -> 197
/*      */     //   78: aload_0
/*      */     //   79: aload_1
/*      */     //   80: <illegal opcode> test : (Lnet/minecraft/world/entity/player/Player;)Ljava/util/function/Predicate;
/*      */     //   85: invokestatic leashableInArea : (Lnet/minecraft/world/entity/Entity;Ljava/util/function/Predicate;)Ljava/util/List;
/*      */     //   88: astore #6
/*      */     //   90: aload #6
/*      */     //   92: invokeinterface isEmpty : ()Z
/*      */     //   97: ifne -> 197
/*      */     //   100: iconst_0
/*      */     //   101: istore #7
/*      */     //   103: aload #6
/*      */     //   105: invokeinterface iterator : ()Ljava/util/Iterator;
/*      */     //   110: astore #8
/*      */     //   112: aload #8
/*      */     //   114: invokeinterface hasNext : ()Z
/*      */     //   119: ifeq -> 160
/*      */     //   122: aload #8
/*      */     //   124: invokeinterface next : ()Ljava/lang/Object;
/*      */     //   129: checkcast net/minecraft/world/entity/Leashable
/*      */     //   132: astore #9
/*      */     //   134: aload #9
/*      */     //   136: aload_0
/*      */     //   137: invokeinterface canHaveALeashAttachedTo : (Lnet/minecraft/world/entity/Entity;)Z
/*      */     //   142: ifeq -> 157
/*      */     //   145: aload #9
/*      */     //   147: aload_0
/*      */     //   148: iconst_1
/*      */     //   149: invokeinterface setLeashedTo : (Lnet/minecraft/world/entity/Entity;Z)V
/*      */     //   154: iconst_1
/*      */     //   155: istore #7
/*      */     //   157: goto -> 112
/*      */     //   160: iload #7
/*      */     //   162: ifeq -> 197
/*      */     //   165: aload_0
/*      */     //   166: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   169: getstatic net/minecraft/world/level/gameevent/GameEvent.ENTITY_ACTION : Lnet/minecraft/core/Holder$Reference;
/*      */     //   172: aload_0
/*      */     //   173: invokevirtual blockPosition : ()Lnet/minecraft/core/BlockPos;
/*      */     //   176: aload_1
/*      */     //   177: invokestatic of : (Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/gameevent/GameEvent$Context;
/*      */     //   180: invokevirtual gameEvent : (Lnet/minecraft/core/Holder;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/gameevent/GameEvent$Context;)V
/*      */     //   183: aload_0
/*      */     //   184: getstatic net/minecraft/sounds/SoundEvents.LEAD_TIED : Lnet/minecraft/sounds/SoundEvent;
/*      */     //   187: invokevirtual playSound : (Lnet/minecraft/sounds/SoundEvent;)V
/*      */     //   190: getstatic net/minecraft/world/InteractionResult.SUCCESS_SERVER : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   193: invokevirtual withoutItem : ()Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   196: areturn
/*      */     //   197: aload_1
/*      */     //   198: aload_2
/*      */     //   199: invokevirtual getItemInHand : (Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;
/*      */     //   202: astore #4
/*      */     //   204: aload #4
/*      */     //   206: getstatic net/minecraft/world/item/Items.SHEARS : Lnet/minecraft/world/item/Item;
/*      */     //   209: invokevirtual is : (Ljava/lang/Object;)Z
/*      */     //   212: ifeq -> 235
/*      */     //   215: aload_0
/*      */     //   216: aload_1
/*      */     //   217: invokevirtual shearOffAllLeashConnections : (Lnet/minecraft/world/entity/player/Player;)Z
/*      */     //   220: ifeq -> 235
/*      */     //   223: aload #4
/*      */     //   225: iconst_1
/*      */     //   226: aload_1
/*      */     //   227: aload_2
/*      */     //   228: invokevirtual hurtAndBreak : (ILnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;)V
/*      */     //   231: getstatic net/minecraft/world/InteractionResult.SUCCESS : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   234: areturn
/*      */     //   235: aload_0
/*      */     //   236: astore #6
/*      */     //   238: aload #6
/*      */     //   240: instanceof net/minecraft/world/entity/Mob
/*      */     //   243: ifeq -> 296
/*      */     //   246: aload #6
/*      */     //   248: checkcast net/minecraft/world/entity/Mob
/*      */     //   251: astore #5
/*      */     //   253: aload #4
/*      */     //   255: getstatic net/minecraft/world/item/Items.SHEARS : Lnet/minecraft/world/item/Item;
/*      */     //   258: invokevirtual is : (Ljava/lang/Object;)Z
/*      */     //   261: ifeq -> 296
/*      */     //   264: aload #5
/*      */     //   266: aload_1
/*      */     //   267: invokevirtual canShearEquipment : (Lnet/minecraft/world/entity/player/Player;)Z
/*      */     //   270: ifeq -> 296
/*      */     //   273: aload_1
/*      */     //   274: invokevirtual isSecondaryUseActive : ()Z
/*      */     //   277: ifne -> 296
/*      */     //   280: aload #5
/*      */     //   282: aload_1
/*      */     //   283: aload_2
/*      */     //   284: aload #4
/*      */     //   286: invokevirtual attemptToShearEquipment : (Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;)Z
/*      */     //   289: ifeq -> 296
/*      */     //   292: getstatic net/minecraft/world/InteractionResult.SUCCESS : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   295: areturn
/*      */     //   296: aload_0
/*      */     //   297: invokevirtual isAlive : ()Z
/*      */     //   300: ifeq -> 487
/*      */     //   303: aload_0
/*      */     //   304: astore #6
/*      */     //   306: aload #6
/*      */     //   308: instanceof net/minecraft/world/entity/Leashable
/*      */     //   311: ifeq -> 487
/*      */     //   314: aload #6
/*      */     //   316: checkcast net/minecraft/world/entity/Leashable
/*      */     //   319: astore #5
/*      */     //   321: aload #5
/*      */     //   323: invokeinterface getLeashHolder : ()Lnet/minecraft/world/entity/Entity;
/*      */     //   328: aload_1
/*      */     //   329: if_acmpne -> 388
/*      */     //   332: aload_0
/*      */     //   333: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   336: invokevirtual isClientSide : ()Z
/*      */     //   339: ifne -> 381
/*      */     //   342: aload_1
/*      */     //   343: invokevirtual hasInfiniteMaterials : ()Z
/*      */     //   346: ifeq -> 359
/*      */     //   349: aload #5
/*      */     //   351: invokeinterface removeLeash : ()V
/*      */     //   356: goto -> 366
/*      */     //   359: aload #5
/*      */     //   361: invokeinterface dropLeash : ()V
/*      */     //   366: aload_0
/*      */     //   367: getstatic net/minecraft/world/level/gameevent/GameEvent.ENTITY_INTERACT : Lnet/minecraft/core/Holder$Reference;
/*      */     //   370: aload_1
/*      */     //   371: invokevirtual gameEvent : (Lnet/minecraft/core/Holder;Lnet/minecraft/world/entity/Entity;)V
/*      */     //   374: aload_0
/*      */     //   375: getstatic net/minecraft/sounds/SoundEvents.LEAD_UNTIED : Lnet/minecraft/sounds/SoundEvent;
/*      */     //   378: invokevirtual playSound : (Lnet/minecraft/sounds/SoundEvent;)V
/*      */     //   381: getstatic net/minecraft/world/InteractionResult.SUCCESS : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   384: invokevirtual withoutItem : ()Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   387: areturn
/*      */     //   388: aload_1
/*      */     //   389: aload_2
/*      */     //   390: invokevirtual getItemInHand : (Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;
/*      */     //   393: astore #6
/*      */     //   395: aload #6
/*      */     //   397: getstatic net/minecraft/world/item/Items.LEAD : Lnet/minecraft/world/item/Item;
/*      */     //   400: invokevirtual is : (Ljava/lang/Object;)Z
/*      */     //   403: ifeq -> 487
/*      */     //   406: aload #5
/*      */     //   408: invokeinterface getLeashHolder : ()Lnet/minecraft/world/entity/Entity;
/*      */     //   413: instanceof net/minecraft/world/entity/player/Player
/*      */     //   416: ifne -> 487
/*      */     //   419: aload_0
/*      */     //   420: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*      */     //   423: invokevirtual isClientSide : ()Z
/*      */     //   426: ifeq -> 433
/*      */     //   429: getstatic net/minecraft/world/InteractionResult.CONSUME : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   432: areturn
/*      */     //   433: aload #5
/*      */     //   435: aload_1
/*      */     //   436: invokeinterface canHaveALeashAttachedTo : (Lnet/minecraft/world/entity/Entity;)Z
/*      */     //   441: ifeq -> 487
/*      */     //   444: aload #5
/*      */     //   446: invokeinterface isLeashed : ()Z
/*      */     //   451: ifeq -> 461
/*      */     //   454: aload #5
/*      */     //   456: invokeinterface dropLeash : ()V
/*      */     //   461: aload #5
/*      */     //   463: aload_1
/*      */     //   464: iconst_1
/*      */     //   465: invokeinterface setLeashedTo : (Lnet/minecraft/world/entity/Entity;Z)V
/*      */     //   470: aload_0
/*      */     //   471: getstatic net/minecraft/sounds/SoundEvents.LEAD_TIED : Lnet/minecraft/sounds/SoundEvent;
/*      */     //   474: invokevirtual playSound : (Lnet/minecraft/sounds/SoundEvent;)V
/*      */     //   477: aload #6
/*      */     //   479: iconst_1
/*      */     //   480: invokevirtual shrink : (I)V
/*      */     //   483: getstatic net/minecraft/world/InteractionResult.SUCCESS_SERVER : Lnet/minecraft/world/InteractionResult$Success;
/*      */     //   486: areturn
/*      */     //   487: getstatic net/minecraft/world/InteractionResult.PASS : Lnet/minecraft/world/InteractionResult$Pass;
/*      */     //   490: areturn
/*      */     // Line number table:
/*      */     //   Java source line number -> byte code offset
/*      */     //   #2473	-> 0
/*      */     //   #2475	-> 52
/*      */     //   #2476	-> 78
/*      */     //   #2477	-> 90
/*      */     //   #2478	-> 100
/*      */     //   #2479	-> 103
/*      */     //   #2480	-> 134
/*      */     //   #2481	-> 145
/*      */     //   #2482	-> 154
/*      */     //   #2484	-> 157
/*      */     //   #2485	-> 160
/*      */     //   #2486	-> 165
/*      */     //   #2487	-> 183
/*      */     //   #2488	-> 190
/*      */     //   #2494	-> 197
/*      */     //   #2495	-> 204
/*      */     //   #2496	-> 223
/*      */     //   #2497	-> 231
/*      */     //   #2500	-> 235
/*      */     //   #2501	-> 280
/*      */     //   #2502	-> 292
/*      */     //   #2506	-> 296
/*      */     //   #2507	-> 321
/*      */     //   #2508	-> 332
/*      */     //   #2509	-> 342
/*      */     //   #2510	-> 349
/*      */     //   #2512	-> 359
/*      */     //   #2514	-> 366
/*      */     //   #2515	-> 374
/*      */     //   #2517	-> 381
/*      */     //   #2521	-> 388
/*      */     //   #2523	-> 395
/*      */     //   #2524	-> 419
/*      */     //   #2525	-> 429
/*      */     //   #2527	-> 433
/*      */     //   #2528	-> 444
/*      */     //   #2529	-> 454
/*      */     //   #2531	-> 461
/*      */     //   #2532	-> 470
/*      */     //   #2533	-> 477
/*      */     //   #2534	-> 483
/*      */     //   #2538	-> 487
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	descriptor
/*      */     //   70	8	5	le	Lnet/minecraft/world/entity/LivingEntity;
/*      */     //   134	23	9	mob	Lnet/minecraft/world/entity/Leashable;
/*      */     //   103	94	7	anyLeashed	Z
/*      */     //   90	107	6	mobsToLeash	Ljava/util/List;
/*      */     //   35	162	4	leashable	Lnet/minecraft/world/entity/Leashable;
/*      */     //   253	43	5	target	Lnet/minecraft/world/entity/Mob;
/*      */     //   395	92	6	itemStack	Lnet/minecraft/world/item/ItemStack;
/*      */     //   321	166	5	leashable	Lnet/minecraft/world/entity/Leashable;
/*      */     //   0	491	0	this	Lnet/minecraft/world/entity/Entity;
/*      */     //   0	491	1	player	Lnet/minecraft/world/entity/player/Player;
/*      */     //   0	491	2	hand	Lnet/minecraft/world/InteractionHand;
/*      */     //   0	491	3	location	Lnet/minecraft/world/phys/Vec3;
/*      */     //   204	287	4	heldItem	Lnet/minecraft/world/item/ItemStack;
/*      */     // Local variable type table:
/*      */     //   start	length	slot	name	signature
/*      */     //   90	107	6	mobsToLeash	Ljava/util/List<Lnet/minecraft/world/entity/Leashable;>;
/*      */   }
/*      */   
/*      */   public boolean shearOffAllLeashConnections(Player player) {
/* 2542 */     boolean dropped = dropAllLeashConnections(player);
/* 2543 */     if (dropped) {
/* 2543 */       Level level = level();
/* 2543 */       if (level instanceof ServerLevel) {
/* 2543 */         ServerLevel serverLevel = (ServerLevel)level;
/* 2544 */         serverLevel.playSound(null, blockPosition(), SoundEvents.SHEARS_SNIP, (player != null) ? player.getSoundSource() : getSoundSource());
/*      */       } 
/*      */     } 
/* 2546 */     return dropped;
/*      */   }
/*      */   
/*      */   public boolean dropAllLeashConnections(Player player) {
/* 2550 */     List<Leashable> leashables = Leashable.leashableLeashedTo(this);
/* 2551 */     boolean dropped = !leashables.isEmpty();
/* 2552 */     Entity entity = this;
/* 2552 */     if (entity instanceof Leashable) {
/* 2552 */       Leashable leashableThis = (Leashable)entity;
/* 2552 */       if (leashableThis.isLeashed()) {
/* 2553 */         leashableThis.dropLeash();
/* 2554 */         dropped = true;
/*      */       } 
/*      */     } 
/* 2556 */     for (Leashable leashable : leashables)
/* 2557 */       leashable.dropLeash(); 
/* 2559 */     if (dropped) {
/* 2560 */       gameEvent((Holder<GameEvent>)GameEvent.SHEAR, (Entity)player);
/* 2561 */       return true;
/*      */     } 
/* 2563 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canCollideWith(Entity entity) {
/* 2567 */     return (entity.canBeCollidedWith(this) && !isPassengerOfSameVehicle(entity));
/*      */   }
/*      */   
/*      */   public boolean canBeCollidedWith(Entity other) {
/* 2571 */     return false;
/*      */   }
/*      */   
/*      */   public void rideTick() {
/* 2575 */     setDeltaMovement(Vec3.ZERO);
/* 2576 */     tick();
/* 2577 */     if (!isPassenger())
/*      */       return; 
/* 2581 */     getVehicle().positionRider(this);
/*      */   }
/*      */   
/*      */   public final void positionRider(Entity passenger) {
/* 2585 */     if (!hasPassenger(passenger))
/*      */       return; 
/* 2588 */     positionRider(passenger, Entity::setPos);
/*      */   }
/*      */   
/*      */   protected void positionRider(Entity passenger, MoveFunction moveFunction) {
/* 2592 */     Vec3 position = getPassengerRidingPosition(passenger);
/* 2593 */     Vec3 offset = passenger.getVehicleAttachmentPoint(this);
/* 2594 */     moveFunction.accept(passenger, position.x - offset.x, position.y - offset.y, position.z - offset.z);
/*      */   }
/*      */   
/*      */   public void onPassengerTurned(Entity passenger) {}
/*      */   
/*      */   public Vec3 getVehicleAttachmentPoint(Entity vehicle) {
/* 2601 */     return getAttachments().get(EntityAttachment.VEHICLE, 0, this.yRot);
/*      */   }
/*      */   
/*      */   public Vec3 getPassengerRidingPosition(Entity passenger) {
/* 2605 */     return position().add(getPassengerAttachmentPoint(passenger, this.dimensions, 1.0F));
/*      */   }
/*      */   
/*      */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 2609 */     return getDefaultPassengerAttachmentPoint(this, passenger, dimensions.attachments());
/*      */   }
/*      */   
/*      */   protected static Vec3 getDefaultPassengerAttachmentPoint(Entity vehicle, Entity passenger, EntityAttachments attachments) {
/* 2613 */     int passengerIndex = vehicle.getPassengers().indexOf(passenger);
/* 2614 */     return attachments.getClamped(EntityAttachment.PASSENGER, passengerIndex, vehicle.yRot);
/*      */   }
/*      */   
/*      */   public final boolean startRiding(Entity entity) {
/* 2618 */     return startRiding(entity, false, true);
/*      */   }
/*      */   
/*      */   public boolean showVehicleHealth() {
/* 2622 */     return this instanceof LivingEntity;
/*      */   }
/*      */   
/*      */   public boolean startRiding(Entity entityToRide, boolean force, boolean sendEventAndTriggers) {
/* 2626 */     if (entityToRide == this.vehicle)
/* 2627 */       return false; 
/* 2629 */     if (!entityToRide.couldAcceptPassenger())
/* 2630 */       return false; 
/* 2633 */     if (!level().isClientSide() && !entityToRide.type.canSerialize())
/* 2634 */       return false; 
/* 2636 */     Entity vehicleEntity = entityToRide;
/* 2637 */     while (vehicleEntity.vehicle != null) {
/* 2638 */       if (vehicleEntity.vehicle == this)
/* 2639 */         return false; 
/* 2642 */       vehicleEntity = vehicleEntity.vehicle;
/*      */     } 
/* 2645 */     if (!force && (!canRide(entityToRide) || !entityToRide.canAddPassenger(this)))
/* 2646 */       return false; 
/* 2649 */     if (isPassenger())
/* 2650 */       stopRiding(); 
/* 2653 */     setPose(Pose.STANDING);
/* 2654 */     this.vehicle = entityToRide;
/* 2655 */     this.vehicle.addPassenger(this);
/* 2657 */     if (sendEventAndTriggers) {
/* 2658 */       level().gameEvent(this, (Holder)GameEvent.ENTITY_MOUNT, this.vehicle.position);
/* 2659 */       entityToRide.getIndirectPassengersStream()
/* 2660 */         .filter(e -> e instanceof ServerPlayer)
/* 2661 */         .forEach(player -> CriteriaTriggers.START_RIDING_TRIGGER.trigger((ServerPlayer)player));
/*      */     } 
/* 2666 */     return true;
/*      */   }
/*      */   
/*      */   protected boolean canRide(Entity vehicle) {
/* 2670 */     return (!isShiftKeyDown() && this.boardingCooldown <= 0);
/*      */   }
/*      */   
/*      */   public void ejectPassengers() {
/* 2674 */     for (int i = this.passengers.size() - 1; i >= 0; i--)
/* 2675 */       ((Entity)this.passengers.get(i)).stopRiding(); 
/*      */   }
/*      */   
/*      */   public void removeVehicle() {
/* 2680 */     if (this.vehicle != null) {
/* 2681 */       Entity oldVehicle = this.vehicle;
/* 2682 */       this.vehicle = null;
/* 2683 */       oldVehicle.removePassenger(this);
/* 2684 */       RemovalReason removalReason = getRemovalReason();
/* 2685 */       if (removalReason == null || removalReason.shouldDestroy())
/* 2687 */         level().gameEvent(this, (Holder)GameEvent.ENTITY_DISMOUNT, oldVehicle.position); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void stopRiding() {
/* 2693 */     removeVehicle();
/*      */   }
/*      */   
/*      */   protected void addPassenger(Entity passenger) {
/* 2697 */     if (passenger.getVehicle() != this)
/* 2698 */       throw new IllegalStateException("Use x.startRiding(y), not y.addPassenger(x)"); 
/* 2701 */     if (this.passengers.isEmpty()) {
/* 2702 */       this.passengers = ImmutableList.of(passenger);
/*      */     } else {
/* 2704 */       List<Entity> newPassengers = Lists.newArrayList((Iterable)this.passengers);
/* 2705 */       if (!level().isClientSide() && passenger instanceof Player && !(getFirstPassenger() instanceof Player)) {
/* 2706 */         newPassengers.add(0, passenger);
/*      */       } else {
/* 2708 */         newPassengers.add(passenger);
/*      */       } 
/* 2710 */       this.passengers = ImmutableList.copyOf(newPassengers);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void removePassenger(Entity passenger) {
/* 2715 */     if (passenger.getVehicle() == this)
/* 2716 */       throw new IllegalStateException("Use x.stopRiding(y), not y.removePassenger(x)"); 
/* 2719 */     if (this.passengers.size() == 1 && this.passengers.get(0) == passenger) {
/* 2720 */       this.passengers = ImmutableList.of();
/*      */     } else {
/* 2722 */       this.passengers = (ImmutableList<Entity>)this.passengers.stream().filter(p -> (p != passenger)).collect(ImmutableList.toImmutableList());
/*      */     } 
/* 2724 */     passenger.boardingCooldown = 60;
/*      */   }
/*      */   
/*      */   protected boolean canAddPassenger(Entity passenger) {
/* 2729 */     return this.passengers.isEmpty();
/*      */   }
/*      */   
/*      */   protected boolean couldAcceptPassenger() {
/* 2734 */     return true;
/*      */   }
/*      */   
/*      */   public final boolean isInterpolating() {
/* 2738 */     return (getInterpolation() != null && getInterpolation().hasActiveInterpolation());
/*      */   }
/*      */   
/*      */   public final void moveOrInterpolateTo(Vec3 position, float yRot, float xRot) {
/* 2742 */     moveOrInterpolateTo(Optional.of(position), Optional.of(Float.valueOf(yRot)), Optional.of(Float.valueOf(xRot)));
/*      */   }
/*      */   
/*      */   public final void moveOrInterpolateTo(float yRot, float xRot) {
/* 2746 */     moveOrInterpolateTo(Optional.empty(), Optional.of(Float.valueOf(yRot)), Optional.of(Float.valueOf(xRot)));
/*      */   }
/*      */   
/*      */   public final void moveOrInterpolateTo(Vec3 position) {
/* 2750 */     moveOrInterpolateTo(Optional.of(position), Optional.empty(), Optional.empty());
/*      */   }
/*      */   
/*      */   public final void moveOrInterpolateTo(Optional<Vec3> position, Optional<Float> yRot, Optional<Float> xRot) {
/* 2754 */     InterpolationHandler interpolationHandler = getInterpolation();
/* 2755 */     if (interpolationHandler != null) {
/* 2756 */       interpolationHandler.interpolateTo(position
/* 2757 */           .orElse(interpolationHandler.position()), ((Float)yRot
/* 2758 */           .orElse(Float.valueOf(interpolationHandler.yRot()))).floatValue(), ((Float)xRot
/* 2759 */           .orElse(Float.valueOf(interpolationHandler.xRot()))).floatValue());
/*      */     } else {
/* 2761 */       position.ifPresent(this::setPos);
/* 2762 */       yRot.ifPresent(y -> setYRot(y.floatValue() % 360.0F));
/* 2763 */       xRot.ifPresent(x -> setXRot(x.floatValue() % 360.0F));
/*      */     } 
/*      */   }
/*      */   
/*      */   public InterpolationHandler getInterpolation() {
/* 2768 */     return null;
/*      */   }
/*      */   
/*      */   public void lerpHeadTo(float yRot, int steps) {
/* 2772 */     setYHeadRot(yRot);
/*      */   }
/*      */   
/*      */   public float getPickRadius() {
/* 2776 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public Vec3 getLookAngle() {
/* 2780 */     return calculateViewVector(getXRot(), getYRot());
/*      */   }
/*      */   
/*      */   public Vec3 getHeadLookAngle() {
/* 2784 */     return calculateViewVector(getXRot(), getYHeadRot());
/*      */   }
/*      */   
/*      */   public Vec3 getHandHoldingItemAngle(Item item) {
/* 2788 */     Entity entity = this;
/* 2788 */     if (entity instanceof Player) {
/* 2788 */       Player player = (Player)entity;
/* 2789 */       boolean itemOnlyInOffhand = (player.getOffhandItem().is(item) && !player.getMainHandItem().is(item));
/* 2790 */       HumanoidArm itemArm = itemOnlyInOffhand ? player.getMainArm().getOpposite() : player.getMainArm();
/* 2791 */       return calculateViewVector(0.0F, getYRot() + ((itemArm == HumanoidArm.RIGHT) ? 80 : -80)).scale(0.5D);
/*      */     } 
/* 2793 */     return Vec3.ZERO;
/*      */   }
/*      */   
/*      */   public Vec2 getRotationVector() {
/* 2798 */     return new Vec2(getXRot(), getYRot());
/*      */   }
/*      */   
/*      */   public Vec3 getForward() {
/* 2802 */     return Vec3.directionFromRotation(getRotationVector());
/*      */   }
/*      */   
/*      */   public void setAsInsidePortal(Portal portal, BlockPos pos) {
/* 2806 */     if (isOnPortalCooldown()) {
/* 2807 */       setPortalCooldown();
/*      */       return;
/*      */     } 
/* 2811 */     if (this.portalProcess == null || !this.portalProcess.isSamePortal(portal)) {
/* 2812 */       this.portalProcess = new PortalProcessor(portal, pos.immutable());
/* 2813 */     } else if (!this.portalProcess.isInsidePortalThisTick()) {
/* 2815 */       this.portalProcess.updateEntryPosition(pos.immutable());
/* 2816 */       this.portalProcess.setAsInsidePortalThisTick(true);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void handlePortal() {
/*      */     ServerLevel level;
/* 2821 */     Level level1 = level();
/* 2821 */     if (level1 instanceof ServerLevel) {
/* 2821 */       level = (ServerLevel)level1;
/*      */     } else {
/*      */       return;
/*      */     } 
/* 2825 */     processPortalCooldown();
/* 2827 */     if (this.portalProcess == null)
/*      */       return; 
/* 2831 */     if (this.portalProcess.processPortalTeleportation(level, this, canUsePortal(false))) {
/* 2832 */       ProfilerFiller profiler = Profiler.get();
/* 2833 */       profiler.push("portal");
/* 2835 */       setPortalCooldown();
/* 2836 */       TeleportTransition teleportTransition = this.portalProcess.getPortalDestination(level, this);
/* 2837 */       if (teleportTransition != null) {
/* 2838 */         ServerLevel newLevel = teleportTransition.newLevel();
/* 2839 */         if (level.isAllowedToEnterPortal((Level)newLevel) && (
/* 2840 */           newLevel.dimension() == level.dimension() || canTeleport((Level)level, (Level)newLevel)))
/* 2841 */           teleport(teleportTransition); 
/*      */       } 
/* 2846 */       profiler.pop();
/* 2847 */     } else if (this.portalProcess.hasExpired()) {
/* 2848 */       this.portalProcess = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getDimensionChangingDelay() {
/* 2853 */     Entity firstPassenger = getFirstPassenger();
/* 2854 */     return (firstPassenger instanceof ServerPlayer) ? firstPassenger.getDimensionChangingDelay() : 300;
/*      */   }
/*      */   
/*      */   public void lerpMotion(Vec3 movement) {
/* 2858 */     setDeltaMovement(movement);
/*      */   }
/*      */   
/*      */   public void handleDamageEvent(DamageSource source) {}
/*      */   
/*      */   public void handleEntityEvent(byte id) {
/* 2865 */     switch (id) {
/*      */       case 53:
/* 2866 */         HoneyBlock.showSlideParticles(this);
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void animateHurt(float direction) {}
/*      */   
/*      */   public boolean isOnFire() {
/* 2875 */     boolean isClientSide = (level() != null && level().isClientSide());
/* 2877 */     return (!fireImmune() && (this.remainingFireTicks > 0 || (isClientSide && getSharedFlag(0))));
/*      */   }
/*      */   
/*      */   public boolean isPassenger() {
/* 2881 */     return (getVehicle() != null);
/*      */   }
/*      */   
/*      */   public boolean isVehicle() {
/* 2885 */     return !this.passengers.isEmpty();
/*      */   }
/*      */   
/*      */   public boolean dismountsUnderwater() {
/* 2889 */     return is(EntityTypeTags.DISMOUNTS_UNDERWATER);
/*      */   }
/*      */   
/*      */   public boolean canControlVehicle() {
/* 2893 */     return !is(EntityTypeTags.NON_CONTROLLING_RIDER);
/*      */   }
/*      */   
/*      */   public void setShiftKeyDown(boolean shiftKeyDown) {
/* 2897 */     setSharedFlag(1, shiftKeyDown);
/*      */   }
/*      */   
/*      */   public boolean isShiftKeyDown() {
/* 2902 */     return getSharedFlag(1);
/*      */   }
/*      */   
/*      */   public boolean isSteppingCarefully() {
/* 2907 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   public boolean isSuppressingBounce() {
/* 2911 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   public boolean isDiscrete() {
/* 2916 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   public boolean isDescending() {
/* 2921 */     return isShiftKeyDown();
/*      */   }
/*      */   
/*      */   public boolean isCrouching() {
/* 2926 */     return hasPose(Pose.CROUCHING);
/*      */   }
/*      */   
/*      */   public boolean isSprinting() {
/* 2930 */     return getSharedFlag(3);
/*      */   }
/*      */   
/*      */   public void setSprinting(boolean isSprinting) {
/* 2934 */     setSharedFlag(3, isSprinting);
/*      */   }
/*      */   
/*      */   public boolean isSwimming() {
/* 2938 */     return getSharedFlag(4);
/*      */   }
/*      */   
/*      */   public boolean isVisuallySwimming() {
/* 2946 */     return hasPose(Pose.SWIMMING);
/*      */   }
/*      */   
/*      */   public boolean isVisuallyCrawling() {
/* 2950 */     return (isVisuallySwimming() && !isInWater());
/*      */   }
/*      */   
/*      */   public void setSwimming(boolean swimming) {
/* 2954 */     setSharedFlag(4, swimming);
/*      */   }
/*      */   
/*      */   public final boolean hasGlowingTag() {
/* 2958 */     return this.hasGlowingTag;
/*      */   }
/*      */   
/*      */   public final void setGlowingTag(boolean value) {
/* 2962 */     this.hasGlowingTag = value;
/* 2963 */     setSharedFlag(6, isCurrentlyGlowing());
/*      */   }
/*      */   
/*      */   public boolean isCurrentlyGlowing() {
/* 2967 */     if (level().isClientSide())
/* 2968 */       return getSharedFlag(6); 
/* 2970 */     return this.hasGlowingTag;
/*      */   }
/*      */   
/*      */   public boolean isInvisible() {
/* 2974 */     return getSharedFlag(5);
/*      */   }
/*      */   
/*      */   public boolean isInvisibleTo(Player player) {
/* 2978 */     if (player.isSpectator())
/* 2979 */       return false; 
/* 2981 */     PlayerTeam playerTeam = getTeam();
/* 2982 */     if (playerTeam != null && player != null && player.getTeam() == playerTeam && playerTeam.canSeeFriendlyInvisibles())
/* 2983 */       return false; 
/* 2985 */     return isInvisible();
/*      */   }
/*      */   
/*      */   public boolean isOnRails() {
/* 2989 */     return false;
/*      */   }
/*      */   
/*      */   public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> action) {}
/*      */   
/*      */   public PlayerTeam getTeam() {
/* 2997 */     return level().getScoreboard().getPlayersTeam(getScoreboardName());
/*      */   }
/*      */   
/*      */   public final boolean isAlliedTo(Entity other) {
/* 3001 */     if (other == null)
/* 3002 */       return false; 
/* 3004 */     return (this == other || 
/* 3005 */       considersEntityAsAlly(other) || other
/* 3006 */       .considersEntityAsAlly(this));
/*      */   }
/*      */   
/*      */   protected boolean considersEntityAsAlly(Entity other) {
/* 3012 */     return isAlliedTo((Team)other.getTeam());
/*      */   }
/*      */   
/*      */   public boolean isAlliedTo(Team other) {
/* 3016 */     if (getTeam() != null)
/* 3017 */       return getTeam().isAlliedTo(other); 
/* 3019 */     return false;
/*      */   }
/*      */   
/*      */   public void setInvisible(boolean invisible) {
/* 3023 */     setSharedFlag(5, invisible);
/*      */   }
/*      */   
/*      */   protected boolean getSharedFlag(int flag) {
/* 3027 */     return ((((Byte)this.entityData.get(DATA_SHARED_FLAGS_ID)).byteValue() & 1 << flag) != 0);
/*      */   }
/*      */   
/*      */   protected void setSharedFlag(int flag, boolean value) {
/* 3031 */     byte currentValue = ((Byte)this.entityData.get(DATA_SHARED_FLAGS_ID)).byteValue();
/* 3032 */     if (value) {
/* 3033 */       this.entityData.set(DATA_SHARED_FLAGS_ID, Byte.valueOf((byte)(currentValue | 1 << flag)));
/*      */     } else {
/* 3035 */       this.entityData.set(DATA_SHARED_FLAGS_ID, Byte.valueOf((byte)(currentValue & (1 << flag ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getMaxAirSupply() {
/* 3040 */     return 300;
/*      */   }
/*      */   
/*      */   public int getAirSupply() {
/* 3044 */     return ((Integer)this.entityData.get(DATA_AIR_SUPPLY_ID)).intValue();
/*      */   }
/*      */   
/*      */   public void setAirSupply(int supply) {
/* 3048 */     this.entityData.set(DATA_AIR_SUPPLY_ID, Integer.valueOf(supply));
/*      */   }
/*      */   
/*      */   public void clearFreeze() {
/* 3052 */     setTicksFrozen(0);
/*      */   }
/*      */   
/*      */   public int getTicksFrozen() {
/* 3056 */     return ((Integer)this.entityData.get(DATA_TICKS_FROZEN)).intValue();
/*      */   }
/*      */   
/*      */   public void setTicksFrozen(int ticks) {
/* 3060 */     this.entityData.set(DATA_TICKS_FROZEN, Integer.valueOf(ticks));
/*      */   }
/*      */   
/*      */   public float getPercentFrozen() {
/* 3064 */     int ticksToFreeze = getTicksRequiredToFreeze();
/* 3065 */     return Math.min(getTicksFrozen(), ticksToFreeze) / ticksToFreeze;
/*      */   }
/*      */   
/*      */   public boolean isFullyFrozen() {
/* 3069 */     return (getTicksFrozen() >= getTicksRequiredToFreeze());
/*      */   }
/*      */   
/*      */   public int getTicksRequiredToFreeze() {
/* 3073 */     return 140;
/*      */   }
/*      */   
/*      */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/* 3077 */     setRemainingFireTicks(this.remainingFireTicks + 1);
/* 3078 */     if (this.remainingFireTicks == 0)
/* 3079 */       igniteForSeconds(8.0F); 
/* 3081 */     hurtServer(level, damageSources().lightningBolt(), 5.0F);
/*      */   }
/*      */   
/*      */   public void onAboveBubbleColumn(boolean dragDown, BlockPos pos) {
/* 3085 */     handleOnAboveBubbleColumn(this, dragDown, pos);
/*      */   }
/*      */   
/*      */   protected static void handleOnAboveBubbleColumn(Entity entity, boolean dragDown, BlockPos pos) {
/*      */     double yd;
/* 3089 */     Vec3 movement = entity.getDeltaMovement();
/* 3091 */     if (dragDown) {
/* 3092 */       yd = Math.max(-0.9D, movement.y - 0.03D);
/*      */     } else {
/* 3094 */       yd = Math.min(1.8D, movement.y + 0.1D);
/*      */     } 
/* 3096 */     entity.setDeltaMovement(movement.x, yd, movement.z);
/* 3097 */     sendBubbleColumnParticles(entity.level, pos);
/*      */   }
/*      */   
/*      */   protected static void sendBubbleColumnParticles(Level level, BlockPos pos) {
/* 3101 */     if (level instanceof ServerLevel) {
/* 3101 */       ServerLevel serverLevel = (ServerLevel)level;
/* 3102 */       RandomSource random = level.getRandom();
/* 3103 */       for (int i = 0; i < 2; i++) {
/* 3104 */         serverLevel.sendParticles((ParticleOptions)ParticleTypes.SPLASH, pos.getX() + random.nextDouble(), (pos.getY() + 1), pos.getZ() + random.nextDouble(), 1, 0.0D, 0.0D, 0.0D, 1.0D);
/* 3105 */         serverLevel.sendParticles((ParticleOptions)ParticleTypes.BUBBLE, pos.getX() + random.nextDouble(), (pos.getY() + 1), pos.getZ() + random.nextDouble(), 1, 0.0D, 0.01D, 0.0D, 0.2D);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void onInsideBubbleColumn(boolean dragDown) {
/* 3111 */     handleOnInsideBubbleColumn(this, dragDown);
/*      */   }
/*      */   
/*      */   protected static void handleOnInsideBubbleColumn(Entity entity, boolean dragDown) {
/*      */     double yd;
/* 3115 */     Vec3 movement = entity.getDeltaMovement();
/* 3117 */     if (dragDown) {
/* 3118 */       yd = Math.max(-0.3D, movement.y - 0.03D);
/*      */     } else {
/* 3120 */       yd = Math.min(0.7D, movement.y + 0.06D);
/*      */     } 
/* 3122 */     entity.setDeltaMovement(movement.x, yd, movement.z);
/* 3123 */     entity.resetFallDistance();
/*      */   }
/*      */   
/*      */   public boolean killedEntity(ServerLevel level, LivingEntity entity, DamageSource source) {
/* 3127 */     return true;
/*      */   }
/*      */   
/*      */   public void checkFallDistanceAccumulation() {
/* 3131 */     if (getDeltaMovement().y() > -0.5D && this.fallDistance > 1.0D)
/* 3132 */       this.fallDistance = 1.0D; 
/*      */   }
/*      */   
/*      */   public void resetFallDistance() {
/* 3137 */     this.fallDistance = 0.0D;
/*      */   }
/*      */   
/*      */   protected void moveTowardsClosestSpace(double x, double y, double z) {
/* 3141 */     BlockPos pos = BlockPos.containing(x, y, z);
/* 3146 */     Vec3 delta = new Vec3(x - pos.getX(), y - pos.getY(), z - pos.getZ());
/* 3149 */     BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
/* 3150 */     Direction closestDirection = Direction.UP;
/* 3151 */     double closest = Double.MAX_VALUE;
/* 3152 */     for (Direction direction : new Direction[] { Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.UP }) {
/* 3153 */       neighborPos.setWithOffset((Vec3i)pos, direction);
/* 3154 */       if (!level().getBlockState((BlockPos)neighborPos).isCollisionShapeFullBlock((BlockGetter)level(), (BlockPos)neighborPos)) {
/* 3155 */         double d = delta.get(direction.getAxis());
/* 3156 */         double orientedDelta = (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (1.0D - d) : d;
/* 3157 */         if (orientedDelta < closest) {
/* 3158 */           closest = orientedDelta;
/* 3159 */           closestDirection = direction;
/*      */         } 
/*      */       } 
/*      */     } 
/* 3164 */     float speed = this.random.nextFloat() * 0.2F + 0.1F;
/* 3165 */     float step = closestDirection.getAxisDirection().getStep();
/* 3167 */     Vec3 scaledMovement = getDeltaMovement().scale(0.75D);
/* 3168 */     if (closestDirection.getAxis() == Direction.Axis.X) {
/* 3169 */       setDeltaMovement((step * speed), scaledMovement.y, scaledMovement.z);
/* 3174 */     } else if (closestDirection.getAxis() == Direction.Axis.Y) {
/* 3175 */       setDeltaMovement(scaledMovement.x, (step * speed), scaledMovement.z);
/* 3180 */     } else if (closestDirection.getAxis() == Direction.Axis.Z) {
/* 3181 */       setDeltaMovement(scaledMovement.x, scaledMovement.y, (step * speed));
/*      */     } 
/*      */   }
/*      */   
/*      */   public void makeStuckInBlock(BlockState blockState, Vec3 speedMultiplier) {
/* 3190 */     resetFallDistance();
/* 3191 */     this.stuckSpeedMultiplier = speedMultiplier;
/*      */   }
/*      */   
/*      */   private static Component removeAction(Component component) {
/* 3195 */     MutableComponent result = component.plainCopy().setStyle(component.getStyle().withClickEvent(null));
/* 3196 */     for (Component s : component.getSiblings())
/* 3197 */       result.append(removeAction(s)); 
/* 3199 */     return (Component)result;
/*      */   }
/*      */   
/*      */   public Component getName() {
/* 3204 */     Component customName = getCustomName();
/* 3205 */     if (customName != null)
/* 3206 */       return removeAction(customName); 
/* 3208 */     return getTypeName();
/*      */   }
/*      */   
/*      */   protected Component getTypeName() {
/* 3212 */     return this.type.getDescription();
/*      */   }
/*      */   
/*      */   public boolean is(Entity other) {
/* 3217 */     return (this == other);
/*      */   }
/*      */   
/*      */   public float getYHeadRot() {
/* 3221 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public void setYHeadRot(float yHeadRot) {}
/*      */   
/*      */   public void setYBodyRot(float yBodyRot) {}
/*      */   
/*      */   public boolean isAttackable() {
/* 3231 */     return true;
/*      */   }
/*      */   
/*      */   public boolean skipAttackInteraction(Entity source) {
/* 3235 */     return false;
/*      */   }
/*      */   
/*      */   public String toString() {
/* 3240 */     String levelId = (level() == null) ? "~NULL~" : level().toString();
/* 3241 */     if (this.removalReason != null)
/* 3242 */       return String.format(Locale.ROOT, "%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f, removed=%s]", new Object[] { getClass().getSimpleName(), getPlainTextName(), Integer.valueOf(this.id), levelId, Double.valueOf(getX()), Double.valueOf(getY()), Double.valueOf(getZ()), this.removalReason }); 
/* 3244 */     return String.format(Locale.ROOT, "%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]", new Object[] { getClass().getSimpleName(), getPlainTextName(), Integer.valueOf(this.id), levelId, Double.valueOf(getX()), Double.valueOf(getY()), Double.valueOf(getZ()) });
/*      */   }
/*      */   
/*      */   protected final boolean isInvulnerableToBase(DamageSource source) {
/* 3249 */     return (isRemoved() || (this.invulnerable && 
/* 3250 */       !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !source.isCreativePlayer()) || (source
/* 3251 */       .is(DamageTypeTags.IS_FIRE) && fireImmune()) || (source
/* 3252 */       .is(DamageTypeTags.IS_FALL) && is(EntityTypeTags.FALL_DAMAGE_IMMUNE)));
/*      */   }
/*      */   
/*      */   public boolean isInvulnerable() {
/* 3256 */     return this.invulnerable;
/*      */   }
/*      */   
/*      */   public boolean isInvulnerableToPiercingWeapon() {
/* 3260 */     return isInvulnerable();
/*      */   }
/*      */   
/*      */   public void setInvulnerable(boolean invulnerable) {
/* 3264 */     this.invulnerable = invulnerable;
/*      */   }
/*      */   
/*      */   public void copyPosition(Entity target) {
/* 3268 */     snapTo(target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot());
/*      */   }
/*      */   
/*      */   public void restoreFrom(Entity oldEntity) {
/* 3272 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(problemPath(), LOGGER);
/*      */     try {
/* 3273 */       TagValueOutput entityData = TagValueOutput.createWithContext((ProblemReporter)reporter, (HolderLookup.Provider)oldEntity.registryAccess());
/* 3274 */       oldEntity.saveWithoutId((ValueOutput)entityData);
/* 3275 */       load(TagValueInput.create((ProblemReporter)reporter, (HolderLookup.Provider)registryAccess(), entityData.buildResult()));
/* 3276 */       reporter.close();
/*      */     } catch (Throwable throwable) {
/*      */       try {
/*      */         reporter.close();
/*      */       } catch (Throwable throwable1) {
/*      */         throwable.addSuppressed(throwable1);
/*      */       } 
/*      */       throw throwable;
/*      */     } 
/* 3277 */     this.portalCooldown = oldEntity.portalCooldown;
/* 3278 */     this.portalProcess = oldEntity.portalProcess;
/*      */   }
/*      */   
/*      */   public Entity teleport(TeleportTransition transition) {
/* 3282 */     Level level = level();
/* 3282 */     if (level instanceof ServerLevel) {
/* 3282 */       ServerLevel serverLevel = (ServerLevel)level;
/* 3282 */       if (!isRemoved()) {
/* 3286 */         ServerLevel newLevel = transition.newLevel();
/* 3287 */         boolean otherDimension = (newLevel.dimension() != serverLevel.dimension());
/* 3289 */         if (!transition.asPassenger())
/* 3290 */           stopRiding(); 
/* 3293 */         if (otherDimension)
/* 3294 */           return teleportCrossDimension(serverLevel, newLevel, transition); 
/* 3296 */         return teleportSameDimension(serverLevel, transition);
/*      */       } 
/*      */     } 
/*      */     return null;
/*      */   }
/*      */   
/*      */   private Entity teleportSameDimension(ServerLevel level, TeleportTransition transition) {
/* 3301 */     for (Entity passenger : getPassengers())
/* 3303 */       passenger.teleport(calculatePassengerTransition(transition, passenger)); 
/* 3306 */     ProfilerFiller profiler = Profiler.get();
/* 3307 */     profiler.push("teleportSameDimension");
/* 3309 */     teleportSetPosition(PositionMoveRotation.of(transition), transition.relatives());
/* 3310 */     if (!transition.asPassenger())
/* 3311 */       sendTeleportTransitionToRidingPlayers(transition); 
/* 3314 */     transition.postTeleportTransition().onTransition(this);
/* 3316 */     profiler.pop();
/* 3317 */     return this;
/*      */   }
/*      */   
/*      */   private Entity teleportCrossDimension(ServerLevel oldLevel, ServerLevel newLevel, TeleportTransition transition) {
/* 3321 */     List<Entity> oldPassengers = getPassengers();
/* 3322 */     List<Entity> newPassengers = new ArrayList<>(oldPassengers.size());
/* 3325 */     ejectPassengers();
/* 3327 */     for (Entity passenger : oldPassengers) {
/* 3328 */       Entity newPassenger = passenger.teleport(calculatePassengerTransition(transition, passenger));
/* 3329 */       if (newPassenger != null)
/* 3330 */         newPassengers.add(newPassenger); 
/*      */     } 
/* 3333 */     ProfilerFiller profiler = Profiler.get();
/* 3334 */     profiler.push("teleportCrossDimension");
/* 3335 */     Entity newEntity = (Entity)getType().create((Level)newLevel, EntitySpawnReason.DIMENSION_TRAVEL);
/* 3337 */     if (newEntity == null) {
/* 3338 */       profiler.pop();
/* 3339 */       return null;
/*      */     } 
/* 3342 */     newEntity.restoreFrom(this);
/* 3343 */     removeAfterChangingDimensions();
/* 3346 */     newEntity.teleportSetPosition(PositionMoveRotation.of(this), PositionMoveRotation.of(transition), transition.relatives());
/* 3347 */     newLevel.addDuringTeleport(newEntity);
/* 3349 */     for (Entity newPassenger : newPassengers)
/* 3350 */       newPassenger.startRiding(newEntity, true, false); 
/* 3353 */     newLevel.resetEmptyTime();
/* 3354 */     transition.postTeleportTransition().onTransition(newEntity);
/* 3356 */     teleportSpectators(transition, oldLevel);
/* 3358 */     profiler.pop();
/* 3359 */     return newEntity;
/*      */   }
/*      */   
/*      */   protected void teleportSpectators(TeleportTransition transition, ServerLevel oldLevel) {
/* 3363 */     List<ServerPlayer> players = List.copyOf(oldLevel.players());
/* 3364 */     for (ServerPlayer serverPlayer : players) {
/* 3365 */       if (serverPlayer.getCamera() == this) {
/* 3366 */         serverPlayer.teleport(transition);
/* 3367 */         serverPlayer.setCamera(null);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private TeleportTransition calculatePassengerTransition(TeleportTransition transition, Entity passenger) {
/* 3373 */     float passengerYRot = transition.yRot() + (transition.relatives().contains(Relative.Y_ROT) ? 0.0F : (passenger.getYRot() - getYRot()));
/* 3374 */     float passengerXRot = transition.xRot() + (transition.relatives().contains(Relative.X_ROT) ? 0.0F : (passenger.getXRot() - getXRot()));
/* 3376 */     Vec3 passengerOffset = passenger.position().subtract(position());
/* 3377 */     Vec3 passengerPos = transition.position().add(
/* 3378 */         transition.relatives().contains(Relative.X) ? 0.0D : passengerOffset.x(), 
/* 3379 */         transition.relatives().contains(Relative.Y) ? 0.0D : passengerOffset.y(), 
/* 3380 */         transition.relatives().contains(Relative.Z) ? 0.0D : passengerOffset.z());
/* 3383 */     return transition
/* 3384 */       .withPosition(passengerPos)
/* 3385 */       .withRotation(passengerYRot, passengerXRot)
/* 3386 */       .transitionAsPassenger();
/*      */   }
/*      */   
/*      */   private void sendTeleportTransitionToRidingPlayers(TeleportTransition transition) {
/* 3390 */     Entity controller = getControllingPassenger();
/* 3391 */     for (Entity passenger : getIndirectPassengers()) {
/* 3392 */       if (passenger instanceof ServerPlayer) {
/* 3392 */         ServerPlayer player = (ServerPlayer)passenger;
/* 3393 */         if (controller != null && player.getId() == controller.getId()) {
/* 3394 */           player.connection.send((Packet)ClientboundTeleportEntityPacket.teleport(getId(), PositionMoveRotation.of(transition), transition.relatives(), this.onGround));
/*      */           continue;
/*      */         } 
/* 3396 */         player.connection.send((Packet)ClientboundTeleportEntityPacket.teleport(getId(), PositionMoveRotation.of(this), Set.of(), this.onGround));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void teleportSetPosition(PositionMoveRotation destination, Set<Relative> relatives) {
/* 3403 */     teleportSetPosition(PositionMoveRotation.of(this), destination, relatives);
/*      */   }
/*      */   
/*      */   public void teleportSetPosition(PositionMoveRotation currentValues, PositionMoveRotation destination, Set<Relative> relatives) {
/* 3407 */     PositionMoveRotation absoluteDestination = PositionMoveRotation.calculateAbsolute(currentValues, destination, relatives);
/* 3408 */     setPosRaw((absoluteDestination.position()).x, (absoluteDestination.position()).y, (absoluteDestination.position()).z);
/* 3409 */     setYRot(absoluteDestination.yRot());
/* 3410 */     setYHeadRot(absoluteDestination.yRot());
/* 3411 */     setXRot(absoluteDestination.xRot());
/* 3412 */     reapplyPosition();
/* 3413 */     setOldPosAndRot();
/* 3414 */     setDeltaMovement(absoluteDestination.deltaMovement());
/* 3415 */     clearMovementThisTick();
/*      */   }
/*      */   
/*      */   public void forceSetRotation(float yRot, boolean relativeY, float xRot, boolean relativeX) {
/* 3419 */     Set<Relative> relatives = Relative.rotation(relativeY, relativeX);
/* 3420 */     PositionMoveRotation currentValues = PositionMoveRotation.of(this);
/* 3421 */     PositionMoveRotation destination = currentValues.withRotation(yRot, xRot);
/* 3422 */     PositionMoveRotation absoluteDestination = PositionMoveRotation.calculateAbsolute(currentValues, destination, relatives);
/* 3424 */     setYRot(absoluteDestination.yRot());
/* 3425 */     setYHeadRot(absoluteDestination.yRot());
/* 3426 */     setXRot(absoluteDestination.xRot());
/* 3427 */     setOldRot();
/*      */   }
/*      */   
/*      */   public void placePortalTicket(BlockPos ticketPosition) {
/* 3431 */     Level level = level();
/* 3431 */     if (level instanceof ServerLevel) {
/* 3431 */       ServerLevel serverLevel = (ServerLevel)level;
/* 3432 */       serverLevel.getChunkSource().addTicketWithRadius(TicketType.PORTAL, ChunkPos.containing(ticketPosition), 3);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void removeAfterChangingDimensions() {
/* 3437 */     setRemoved(RemovalReason.CHANGED_DIMENSION);
/* 3438 */     Entity entity1 = this;
/* 3438 */     if (entity1 instanceof Leashable) {
/* 3438 */       Leashable leashable = (Leashable)entity1;
/* 3440 */       leashable.removeLeash();
/*      */     } 
/* 3442 */     Entity entity2 = this;
/* 3442 */     if (entity2 instanceof WaypointTransmitter) {
/* 3442 */       WaypointTransmitter waypoint = (WaypointTransmitter)entity2;
/* 3442 */       Level level = this.level;
/* 3442 */       if (level instanceof ServerLevel) {
/* 3442 */         ServerLevel serverLevel = (ServerLevel)level;
/* 3443 */         serverLevel.getWaypointManager().untrackWaypoint(waypoint);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle portalArea) {
/* 3448 */     return PortalShape.getRelativePosition(portalArea, axis, position(), getDimensions(getPose()));
/*      */   }
/*      */   
/*      */   public boolean canUsePortal(boolean ignorePassenger) {
/* 3452 */     return ((ignorePassenger || !isPassenger()) && isAlive());
/*      */   }
/*      */   
/*      */   public boolean canTeleport(Level from, Level to) {
/* 3456 */     if (from.dimension() == Level.END && to.dimension() == Level.OVERWORLD)
/* 3457 */       for (Entity passenger : getPassengers()) {
/* 3458 */         if (passenger instanceof ServerPlayer) {
/* 3458 */           ServerPlayer player = (ServerPlayer)passenger;
/* 3458 */           if (!player.seenCredits)
/* 3459 */             return false; 
/*      */         } 
/*      */       }  
/* 3463 */     return true;
/*      */   }
/*      */   
/*      */   public float getBlockExplosionResistance(Explosion explosion, BlockGetter level, BlockPos pos, BlockState block, FluidState fluid, float resistance) {
/* 3467 */     return resistance;
/*      */   }
/*      */   
/*      */   public boolean shouldBlockExplode(Explosion explosion, BlockGetter level, BlockPos pos, BlockState state, float power) {
/* 3471 */     return true;
/*      */   }
/*      */   
/*      */   public int getMaxFallDistance() {
/* 3475 */     return 3;
/*      */   }
/*      */   
/*      */   public boolean isIgnoringBlockTriggers() {
/* 3479 */     return false;
/*      */   }
/*      */   
/*      */   public void fillCrashReportCategory(CrashReportCategory category) {
/* 3483 */     category.setDetail("Entity Type", () -> typeHolder().getRegisteredName() + " (" + typeHolder().getRegisteredName() + ")");
/* 3484 */     category.setDetail("Entity ID", Integer.valueOf(this.id));
/* 3485 */     category.setDetail("Entity Name", () -> getPlainTextName());
/* 3486 */     category.setDetail("Entity's Exact location", String.format(Locale.ROOT, "%.2f, %.2f, %.2f", new Object[] { Double.valueOf(getX()), Double.valueOf(getY()), Double.valueOf(getZ()) }));
/* 3487 */     category.setDetail("Entity's Block location", CrashReportCategory.formatLocation((LevelHeightAccessor)level(), Mth.floor(getX()), Mth.floor(getY()), Mth.floor(getZ())));
/* 3488 */     Vec3 movement = getDeltaMovement();
/* 3489 */     category.setDetail("Entity's Momentum", String.format(Locale.ROOT, "%.2f, %.2f, %.2f", new Object[] { Double.valueOf(movement.x), Double.valueOf(movement.y), Double.valueOf(movement.z) }));
/* 3490 */     category.setDetail("Entity's Passengers", () -> getPassengers().toString());
/* 3491 */     category.setDetail("Entity's Vehicle", () -> String.valueOf(getVehicle()));
/*      */   }
/*      */   
/*      */   public boolean displayFireAnimation() {
/* 3495 */     return (isOnFire() && !isSpectator());
/*      */   }
/*      */   
/*      */   public void setUUID(UUID uuid) {
/* 3499 */     this.uuid = uuid;
/* 3500 */     this.stringUUID = this.uuid.toString();
/*      */   }
/*      */   
/*      */   public UUID getUUID() {
/* 3505 */     return this.uuid;
/*      */   }
/*      */   
/*      */   public String getStringUUID() {
/* 3509 */     return this.stringUUID;
/*      */   }
/*      */   
/*      */   public String getScoreboardName() {
/* 3514 */     return this.stringUUID;
/*      */   }
/*      */   
/*      */   public boolean isPushedByFluid() {
/* 3518 */     return true;
/*      */   }
/*      */   
/*      */   public static double getViewScale() {
/* 3522 */     return viewScale;
/*      */   }
/*      */   
/*      */   public static void setViewScale(double viewScale) {
/* 3526 */     Entity.viewScale = viewScale;
/*      */   }
/*      */   
/*      */   public Component getDisplayName() {
/* 3531 */     return (Component)PlayerTeam.formatNameForTeam((Team)getTeam(), getName()).withStyle(s -> s.withHoverEvent(createHoverEvent()).withInsertion(getStringUUID()));
/*      */   }
/*      */   
/*      */   public void setCustomName(Component name) {
/* 3535 */     this.entityData.set(DATA_CUSTOM_NAME, Optional.ofNullable(name));
/*      */   }
/*      */   
/*      */   public Component getCustomName() {
/* 3540 */     return ((Optional<Component>)this.entityData.get(DATA_CUSTOM_NAME)).orElse(null);
/*      */   }
/*      */   
/*      */   public boolean hasCustomName() {
/* 3545 */     return ((Optional)this.entityData.get(DATA_CUSTOM_NAME)).isPresent();
/*      */   }
/*      */   
/*      */   public void setCustomNameVisible(boolean visible) {
/* 3549 */     this.entityData.set(DATA_CUSTOM_NAME_VISIBLE, Boolean.valueOf(visible));
/*      */   }
/*      */   
/*      */   public boolean isCustomNameVisible() {
/* 3553 */     return ((Boolean)this.entityData.get(DATA_CUSTOM_NAME_VISIBLE)).booleanValue();
/*      */   }
/*      */   
/*      */   public Component belowNameDisplay() {
/* 3557 */     Scoreboard scoreboard = level().getScoreboard();
/* 3558 */     Objective objective = scoreboard.getDisplayObjective(DisplaySlot.BELOW_NAME);
/* 3559 */     if (objective != null) {
/* 3560 */       ReadOnlyScoreInfo score = scoreboard.getPlayerScoreInfo(this, objective);
/* 3561 */       if (score != null) {
/* 3562 */         MutableComponent mutableComponent = score.formatValue(objective.numberFormatOrDefault((NumberFormat)StyledFormat.NO_STYLE));
/* 3563 */         return (Component)Component.empty().append((Component)mutableComponent).append(CommonComponents.SPACE).append(objective.getDisplayName());
/*      */       } 
/*      */     } 
/* 3566 */     return null;
/*      */   }
/*      */   
/*      */   public boolean teleportTo(ServerLevel level, double x, double y, double z, Set<Relative> relatives, float newYRot, float newXRot, boolean resetCamera) {
/* 3570 */     Entity newEntity = teleport(new TeleportTransition(level, new Vec3(x, y, z), Vec3.ZERO, newYRot, newXRot, relatives, TeleportTransition.DO_NOTHING));
/* 3571 */     return (newEntity != null);
/*      */   }
/*      */   
/*      */   public void dismountTo(double x, double y, double z) {
/* 3575 */     teleportTo(x, y, z);
/*      */   }
/*      */   
/*      */   public void teleportTo(double x, double y, double z) {
/* 3579 */     if (!(level() instanceof ServerLevel))
/*      */       return; 
/* 3582 */     snapTo(x, y, z, getYRot(), getXRot());
/* 3583 */     teleportPassengers();
/*      */   }
/*      */   
/*      */   private void teleportPassengers() {
/* 3587 */     getSelfAndPassengers().forEach(entity -> {
/*      */           UnmodifiableIterator<Entity> unmodifiableIterator = entity.passengers.iterator();
/*      */           while (unmodifiableIterator.hasNext()) {
/*      */             Entity passenger = unmodifiableIterator.next();
/*      */             entity.positionRider(passenger, Entity::snapTo);
/*      */           } 
/*      */         });
/*      */   }
/*      */   
/*      */   public void teleportRelative(double dx, double dy, double dz) {
/* 3595 */     teleportTo(getX() + dx, getY() + dy, getZ() + dz);
/*      */   }
/*      */   
/*      */   public boolean shouldShowName() {
/* 3599 */     return isCustomNameVisible();
/*      */   }
/*      */   
/*      */   public void onSyncedDataUpdated(List<SynchedEntityData.DataValue<?>> updatedItems) {}
/*      */   
/*      */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 3609 */     if (DATA_POSE.equals(accessor))
/* 3610 */       refreshDimensions(); 
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected void fixupDimensions() {
/* 3619 */     Pose pose = getPose();
/* 3620 */     EntityDimensions newDim = getDimensions(pose);
/* 3622 */     this.dimensions = newDim;
/* 3623 */     this.eyeHeight = newDim.eyeHeight();
/*      */   }
/*      */   
/*      */   public void refreshDimensions() {
/* 3627 */     EntityDimensions oldDim = this.dimensions;
/* 3628 */     Pose pose = getPose();
/* 3629 */     EntityDimensions newDim = getDimensions(pose);
/* 3631 */     this.dimensions = newDim;
/* 3632 */     this.eyeHeight = newDim.eyeHeight();
/* 3634 */     reapplyPosition();
/* 3636 */     boolean isSmall = (newDim.width() <= 4.0F && newDim.height() <= 4.0F);
/* 3637 */     if (!this.level.isClientSide() && !this.firstTick && !this.noPhysics && isSmall && (newDim.width() > oldDim.width() || newDim.height() > oldDim.height()) && !(this instanceof Player))
/* 3638 */       fudgePositionAfterSizeChange(oldDim); 
/*      */   }
/*      */   
/*      */   public boolean fudgePositionAfterSizeChange(EntityDimensions previousDimensions) {
/* 3643 */     EntityDimensions newDimensions = getDimensions(getPose());
/* 3644 */     Vec3 oldCenter = position().add(0.0D, previousDimensions.height() / 2.0D, 0.0D);
/* 3645 */     double widthDelta = Math.max(0.0F, newDimensions.width() - previousDimensions.width()) + 1.0E-6D;
/* 3646 */     double heightDelta = Math.max(0.0F, newDimensions.height() - previousDimensions.height()) + 1.0E-6D;
/* 3647 */     VoxelShape allowedCenters = Shapes.create(AABB.ofSize(oldCenter, widthDelta, heightDelta, widthDelta));
/* 3649 */     Optional<Vec3> freePosition = this.level.findFreePosition(this, allowedCenters, oldCenter, newDimensions.width(), newDimensions.height(), newDimensions.width());
/* 3650 */     if (freePosition.isPresent()) {
/* 3651 */       setPos(((Vec3)freePosition.get()).add(0.0D, -newDimensions.height() / 2.0D, 0.0D));
/* 3652 */       return true;
/*      */     } 
/* 3655 */     if (newDimensions.width() > previousDimensions.width() && newDimensions.height() > previousDimensions.height()) {
/* 3657 */       VoxelShape allowedCentersIgnoringY = Shapes.create(AABB.ofSize(oldCenter, widthDelta, 1.0E-6D, widthDelta));
/* 3658 */       Optional<Vec3> freePositionIgnoreVertical = this.level.findFreePosition(this, allowedCentersIgnoringY, oldCenter, newDimensions.width(), previousDimensions.height(), newDimensions.width());
/* 3659 */       if (freePositionIgnoreVertical.isPresent()) {
/* 3660 */         setPos(((Vec3)freePositionIgnoreVertical.get()).add(0.0D, -previousDimensions.height() / 2.0D + 1.0E-6D, 0.0D));
/* 3661 */         return true;
/*      */       } 
/*      */     } 
/* 3664 */     return false;
/*      */   }
/*      */   
/*      */   public Direction getDirection() {
/* 3668 */     return Direction.fromYRot(getYRot());
/*      */   }
/*      */   
/*      */   public Direction getMotionDirection() {
/* 3672 */     return getDirection();
/*      */   }
/*      */   
/*      */   protected HoverEvent createHoverEvent() {
/* 3676 */     return (HoverEvent)new HoverEvent.ShowEntity(new HoverEvent.EntityTooltipInfo(getType(), getUUID(), getName()));
/*      */   }
/*      */   
/*      */   public boolean broadcastToPlayer(ServerPlayer player) {
/* 3680 */     return true;
/*      */   }
/*      */   
/*      */   public final AABB getBoundingBox() {
/* 3685 */     return this.bb;
/*      */   }
/*      */   
/*      */   public final void setBoundingBox(AABB bb) {
/* 3689 */     this.bb = bb;
/*      */   }
/*      */   
/*      */   public final float getEyeHeight(Pose pose) {
/* 3693 */     return getDimensions(pose).eyeHeight();
/*      */   }
/*      */   
/*      */   public final float getEyeHeight() {
/* 3697 */     return this.eyeHeight;
/*      */   }
/*      */   
/*      */   public SlotAccess getSlot(int slot) {
/* 3702 */     return null;
/*      */   }
/*      */   
/*      */   public boolean ignoreExplosion(Explosion explosion) {
/* 3706 */     return false;
/*      */   }
/*      */   
/*      */   public void startSeenByPlayer(ServerPlayer player) {}
/*      */   
/*      */   public void stopSeenByPlayer(ServerPlayer player) {}
/*      */   
/*      */   public float rotate(Rotation rotation) {
/* 3716 */     float angle = Mth.wrapDegrees(getYRot());
/* 3717 */     switch (rotation) {
/*      */       case FRONT_BACK:
/*      */       
/*      */       case LEFT_RIGHT:
/*      */       
/*      */       case null:
/*      */       
/*      */     } 
/* 3717 */     return 
/*      */       
/* 3721 */       angle;
/*      */   }
/*      */   
/*      */   public float mirror(Mirror mirror) {
/* 3726 */     float angle = Mth.wrapDegrees(getYRot());
/* 3727 */     switch (mirror) {
/*      */       case FRONT_BACK:
/*      */       
/*      */       case LEFT_RIGHT:
/*      */       
/*      */     } 
/* 3727 */     return 
/*      */       
/* 3730 */       angle;
/*      */   }
/*      */   
/*      */   public ProjectileDeflection deflection(Projectile projectile) {
/* 3735 */     return is(EntityTypeTags.DEFLECTS_PROJECTILES) ? ProjectileDeflection.REVERSE : ProjectileDeflection.NONE;
/*      */   }
/*      */   
/*      */   public LivingEntity getControllingPassenger() {
/* 3739 */     return null;
/*      */   }
/*      */   
/*      */   public final boolean hasControllingPassenger() {
/* 3743 */     return (getControllingPassenger() != null);
/*      */   }
/*      */   
/*      */   public final List<Entity> getPassengers() {
/* 3747 */     return (List<Entity>)this.passengers;
/*      */   }
/*      */   
/*      */   public Entity getFirstPassenger() {
/* 3751 */     return this.passengers.isEmpty() ? null : (Entity)this.passengers.get(0);
/*      */   }
/*      */   
/*      */   public boolean hasPassenger(Entity entity) {
/* 3755 */     return this.passengers.contains(entity);
/*      */   }
/*      */   
/*      */   public boolean hasPassenger(Predicate<Entity> test) {
/* 3759 */     for (UnmodifiableIterator<Entity> unmodifiableIterator = this.passengers.iterator(); unmodifiableIterator.hasNext(); ) {
/* 3759 */       Entity passenger = unmodifiableIterator.next();
/* 3760 */       if (test.test(passenger))
/* 3761 */         return true; 
/*      */     } 
/* 3764 */     return false;
/*      */   }
/*      */   
/*      */   private Stream<Entity> getIndirectPassengersStream() {
/* 3768 */     return this.passengers.stream().flatMap(Entity::getSelfAndPassengers);
/*      */   }
/*      */   
/*      */   public Stream<Entity> getSelfAndPassengers() {
/* 3773 */     return Stream.concat(Stream.of(this), getIndirectPassengersStream());
/*      */   }
/*      */   
/*      */   public Stream<Entity> getPassengersAndSelf() {
/* 3778 */     return Stream.concat(this.passengers.stream().flatMap(Entity::getPassengersAndSelf), Stream.of(this));
/*      */   }
/*      */   
/*      */   public Iterable<Entity> getIndirectPassengers() {
/* 3782 */     return () -> getIndirectPassengersStream().iterator();
/*      */   }
/*      */   
/*      */   public int countPlayerPassengers() {
/* 3786 */     return (int)getIndirectPassengersStream().filter(e -> e instanceof Player).count();
/*      */   }
/*      */   
/*      */   public boolean hasExactlyOnePlayerPassenger() {
/* 3790 */     return (countPlayerPassengers() == 1);
/*      */   }
/*      */   
/*      */   public Entity getRootVehicle() {
/* 3794 */     Entity result = this;
/* 3795 */     while (result.isPassenger())
/* 3796 */       result = result.getVehicle(); 
/* 3798 */     return result;
/*      */   }
/*      */   
/*      */   public boolean isPassengerOfSameVehicle(Entity other) {
/* 3802 */     return (getRootVehicle() == other.getRootVehicle());
/*      */   }
/*      */   
/*      */   public boolean hasIndirectPassenger(Entity entity) {
/* 3806 */     if (!entity.isPassenger())
/* 3807 */       return false; 
/* 3809 */     Entity ridden = entity.getVehicle();
/* 3810 */     if (ridden == this)
/* 3811 */       return true; 
/* 3813 */     return hasIndirectPassenger(ridden);
/*      */   }
/*      */   
/*      */   public final boolean isLocalInstanceAuthoritative() {
/* 3817 */     if (this.level.isClientSide())
/* 3818 */       return isLocalClientAuthoritative(); 
/* 3820 */     return !isClientAuthoritative();
/*      */   }
/*      */   
/*      */   protected boolean isLocalClientAuthoritative() {
/* 3825 */     LivingEntity passenger = getControllingPassenger();
/* 3826 */     return (passenger != null && passenger.isLocalClientAuthoritative());
/*      */   }
/*      */   
/*      */   public boolean isClientAuthoritative() {
/* 3830 */     LivingEntity passenger = getControllingPassenger();
/* 3831 */     return (passenger != null && passenger.isClientAuthoritative());
/*      */   }
/*      */   
/*      */   public boolean canSimulateMovement() {
/* 3838 */     return isLocalInstanceAuthoritative();
/*      */   }
/*      */   
/*      */   public boolean isEffectiveAi() {
/* 3842 */     return isLocalInstanceAuthoritative();
/*      */   }
/*      */   
/*      */   protected static Vec3 getCollisionHorizontalEscapeVector(double colliderWidth, double collidingWidth, float directionDegrees) {
/* 3846 */     double distance = (colliderWidth + collidingWidth + 9.999999747378752E-6D) / 2.0D;
/* 3848 */     float directionX = -Mth.sin((directionDegrees * 0.017453292F));
/* 3849 */     float directionZ = Mth.cos((directionDegrees * 0.017453292F));
/* 3851 */     float scale = Math.max(Math.abs(directionX), Math.abs(directionZ));
/* 3853 */     return new Vec3(directionX * distance / scale, 0.0D, directionZ * distance / scale);
/*      */   }
/*      */   
/*      */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
/* 3857 */     return new Vec3(getX(), (getBoundingBox()).maxY, getZ());
/*      */   }
/*      */   
/*      */   public Entity getVehicle() {
/* 3861 */     return this.vehicle;
/*      */   }
/*      */   
/*      */   public Entity getControlledVehicle() {
/* 3865 */     return (this.vehicle != null && this.vehicle.getControllingPassenger() == this) ? this.vehicle : null;
/*      */   }
/*      */   
/*      */   public PushReaction getPistonPushReaction() {
/* 3869 */     return PushReaction.NORMAL;
/*      */   }
/*      */   
/*      */   public SoundSource getSoundSource() {
/* 3873 */     return SoundSource.NEUTRAL;
/*      */   }
/*      */   
/*      */   protected int getFireImmuneTicks() {
/* 3877 */     return 0;
/*      */   }
/*      */   
/*      */   public CommandSourceStack createCommandSourceStackForNameResolution(ServerLevel level) {
/* 3881 */     return new CommandSourceStack(CommandSource.NULL, position(), getRotationVector(), level, PermissionSet.NO_PERMISSIONS, getPlainTextName(), getDisplayName(), level.getServer(), this);
/*      */   }
/*      */   
/*      */   public void lookAt(EntityAnchorArgument.Anchor anchor, Vec3 pos) {
/* 3885 */     Vec3 from = anchor.apply(this);
/* 3886 */     double xd = pos.x - from.x;
/* 3887 */     double yd = pos.y - from.y;
/* 3888 */     double zd = pos.z - from.z;
/* 3889 */     double sd = Math.sqrt(xd * xd + zd * zd);
/* 3891 */     setXRot(Mth.wrapDegrees((float)-(Mth.atan2(yd, sd) * 57.2957763671875D)));
/* 3892 */     setYRot(Mth.wrapDegrees((float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F));
/* 3893 */     setYHeadRot(getYRot());
/* 3894 */     this.xRotO = getXRot();
/* 3895 */     this.yRotO = getYRot();
/*      */   }
/*      */   
/*      */   public float getPreciseBodyRotation(float partial) {
/* 3899 */     return Mth.lerp(partial, this.yRotO, this.yRot);
/*      */   }
/*      */   
/*      */   public boolean touchingUnloadedChunk() {
/* 3904 */     AABB box = getBoundingBox().inflate(1.0D);
/* 3905 */     int x0 = Mth.floor(box.minX);
/* 3906 */     int x1 = Mth.ceil(box.maxX);
/* 3907 */     int z0 = Mth.floor(box.minZ);
/* 3908 */     int z1 = Mth.ceil(box.maxZ);
/* 3909 */     return !level().hasChunksAt(x0, z0, x1, z1);
/*      */   }
/*      */   
/*      */   public double getFluidHeight(TagKey<Fluid> type) {
/* 3913 */     return this.fluidInteraction.getFluidHeight(type);
/*      */   }
/*      */   
/*      */   public double getFluidJumpThreshold() {
/* 3918 */     return (getEyeHeight() < 0.4D) ? 0.0D : 0.4D;
/*      */   }
/*      */   
/*      */   public final float getBbWidth() {
/* 3922 */     return this.dimensions.width();
/*      */   }
/*      */   
/*      */   public final float getBbHeight() {
/* 3926 */     return this.dimensions.height();
/*      */   }
/*      */   
/*      */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 3930 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket(this, serverEntity);
/*      */   }
/*      */   
/*      */   public EntityDimensions getDimensions(Pose pose) {
/* 3934 */     return this.type.getDimensions();
/*      */   }
/*      */   
/*      */   public final EntityAttachments getAttachments() {
/* 3938 */     return this.dimensions.attachments();
/*      */   }
/*      */   
/*      */   public Vec3 position() {
/* 3943 */     return this.position;
/*      */   }
/*      */   
/*      */   public Vec3 trackingPosition() {
/* 3947 */     return position();
/*      */   }
/*      */   
/*      */   public BlockPos blockPosition() {
/* 3952 */     return this.blockPosition;
/*      */   }
/*      */   
/*      */   public BlockState getInBlockState() {
/* 3956 */     if (this.inBlockState == null)
/* 3957 */       this.inBlockState = level().getBlockState(blockPosition()); 
/* 3959 */     return this.inBlockState;
/*      */   }
/*      */   
/*      */   public ChunkPos chunkPosition() {
/* 3963 */     return this.chunkPosition;
/*      */   }
/*      */   
/*      */   public Vec3 getDeltaMovement() {
/* 3967 */     return this.deltaMovement;
/*      */   }
/*      */   
/*      */   public void setDeltaMovement(Vec3 deltaMovement) {
/* 3971 */     if (deltaMovement.isFinite())
/* 3972 */       this.deltaMovement = deltaMovement; 
/*      */   }
/*      */   
/*      */   public void addDeltaMovement(Vec3 momentum) {
/* 3977 */     if (momentum.isFinite())
/* 3978 */       setDeltaMovement(getDeltaMovement().add(momentum)); 
/*      */   }
/*      */   
/*      */   public void setDeltaMovement(double xd, double yd, double zd) {
/* 3983 */     setDeltaMovement(new Vec3(xd, yd, zd));
/*      */   }
/*      */   
/*      */   public final int getBlockX() {
/* 3987 */     return this.blockPosition.getX();
/*      */   }
/*      */   
/*      */   public final double getX() {
/* 3991 */     return this.position.x;
/*      */   }
/*      */   
/*      */   public double getX(double progress) {
/* 3995 */     return this.position.x + getBbWidth() * progress;
/*      */   }
/*      */   
/*      */   public double getRandomX(double spread) {
/* 3999 */     return getX((2.0D * this.random.nextDouble() - 1.0D) * spread);
/*      */   }
/*      */   
/*      */   public final int getBlockY() {
/* 4003 */     return this.blockPosition.getY();
/*      */   }
/*      */   
/*      */   public final double getY() {
/* 4007 */     return this.position.y;
/*      */   }
/*      */   
/*      */   public double getY(double progress) {
/* 4011 */     return this.position.y + getBbHeight() * progress;
/*      */   }
/*      */   
/*      */   public double getRandomY(double spread) {
/* 4015 */     return getY((2.0D * this.random.nextDouble() - 1.0D) * spread);
/*      */   }
/*      */   
/*      */   public double getRandomY() {
/* 4019 */     return getY(this.random.nextDouble());
/*      */   }
/*      */   
/*      */   public double getEyeY() {
/* 4023 */     return this.position.y + this.eyeHeight;
/*      */   }
/*      */   
/*      */   public final int getBlockZ() {
/* 4027 */     return this.blockPosition.getZ();
/*      */   }
/*      */   
/*      */   public final double getZ() {
/* 4031 */     return this.position.z;
/*      */   }
/*      */   
/*      */   public double getZ(double progress) {
/* 4035 */     return this.position.z + getBbWidth() * progress;
/*      */   }
/*      */   
/*      */   public double getRandomZ(double spread) {
/* 4039 */     return getZ((2.0D * this.random.nextDouble() - 1.0D) * spread);
/*      */   }
/*      */   
/*      */   public final void setPosRaw(double x, double y, double z) {
/* 4043 */     if (this.position.x != x || this.position.y != y || this.position.z != z) {
/* 4044 */       this.position = new Vec3(x, y, z);
/* 4046 */       int fx = Mth.floor(x);
/* 4047 */       int fy = Mth.floor(y);
/* 4048 */       int fz = Mth.floor(z);
/* 4049 */       if (fx != this.blockPosition.getX() || fy != this.blockPosition.getY() || fz != this.blockPosition.getZ()) {
/* 4050 */         this.blockPosition = new BlockPos(fx, fy, fz);
/* 4051 */         this.inBlockState = null;
/* 4052 */         if (SectionPos.blockToSectionCoord(fx) != this.chunkPosition.x() || SectionPos.blockToSectionCoord(fz) != this.chunkPosition.z())
/* 4053 */           this.chunkPosition = ChunkPos.containing(this.blockPosition); 
/*      */       } 
/* 4056 */       this.levelCallback.onMove();
/* 4057 */       if (!this.firstTick) {
/* 4057 */         Level level = this.level;
/* 4057 */         if (level instanceof ServerLevel) {
/* 4057 */           ServerLevel serverLevel = (ServerLevel)level;
/* 4057 */           if (!isRemoved()) {
/* 4058 */             Entity entity = this;
/* 4058 */             if (entity instanceof WaypointTransmitter) {
/* 4058 */               WaypointTransmitter waypoint = (WaypointTransmitter)entity;
/* 4058 */               if (waypoint.isTransmittingWaypoint())
/* 4059 */                 serverLevel.getWaypointManager().updateWaypoint(waypoint); 
/*      */             } 
/* 4061 */             entity = this;
/* 4061 */             if (entity instanceof ServerPlayer) {
/* 4061 */               ServerPlayer player = (ServerPlayer)entity;
/* 4061 */               if (player.isReceivingWaypoints() && player.connection != null)
/* 4062 */                 serverLevel.getWaypointManager().updatePlayer(player); 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void checkDespawn() {}
/*      */   
/*      */   public Vec3[] getQuadLeashHolderOffsets() {
/* 4072 */     return Leashable.createQuadLeashOffsets(this, 0.0D, 0.5D, 0.5D, 0.0D);
/*      */   }
/*      */   
/*      */   public boolean supportQuadLeashAsHolder() {
/* 4076 */     return false;
/*      */   }
/*      */   
/*      */   public void notifyLeashHolder(Leashable entity) {}
/*      */   
/*      */   public void notifyLeasheeRemoved(Leashable entity) {}
/*      */   
/*      */   public Vec3 getRopeHoldPosition(float partialTickTime) {
/* 4086 */     return getPosition(partialTickTime).add(0.0D, this.eyeHeight * 0.7D, 0.0D);
/*      */   }
/*      */   
/*      */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 4090 */     int entityId = packet.getId();
/* 4091 */     double x = packet.getX();
/* 4092 */     double y = packet.getY();
/* 4093 */     double z = packet.getZ();
/* 4094 */     syncPacketPositionCodec(x, y, z);
/* 4095 */     snapTo(x, y, z, packet.getYRot(), packet.getXRot());
/* 4096 */     setId(entityId);
/* 4097 */     setUUID(packet.getUUID());
/* 4098 */     setDeltaMovement(packet.getMovement());
/*      */   }
/*      */   
/*      */   public ItemStack getPickResult() {
/* 4102 */     return null;
/*      */   }
/*      */   
/*      */   public void setIsInPowderSnow(boolean isInPowderSnow) {
/* 4106 */     this.isInPowderSnow = isInPowderSnow;
/*      */   }
/*      */   
/*      */   public boolean canFreeze() {
/* 4110 */     return !is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES);
/*      */   }
/*      */   
/*      */   public boolean isFreezing() {
/* 4114 */     return (getTicksFrozen() > 0);
/*      */   }
/*      */   
/*      */   public float getYRot() {
/* 4118 */     return this.yRot;
/*      */   }
/*      */   
/*      */   public float getVisualRotationYInDegrees() {
/* 4126 */     return getYRot();
/*      */   }
/*      */   
/*      */   public void setYRot(float yRot) {
/* 4130 */     if (!Float.isFinite(yRot)) {
/* 4131 */       Util.logAndPauseIfInIde("Invalid entity rotation: " + yRot + ", discarding.");
/*      */       return;
/*      */     } 
/* 4134 */     this.yRot = yRot;
/*      */   }
/*      */   
/*      */   public float getXRot() {
/* 4138 */     return this.xRot;
/*      */   }
/*      */   
/*      */   public void setXRot(float xRot) {
/* 4142 */     if (!Float.isFinite(xRot)) {
/* 4143 */       Util.logAndPauseIfInIde("Invalid entity rotation: " + xRot + ", discarding.");
/*      */       return;
/*      */     } 
/* 4146 */     this.xRot = Math.clamp(xRot % 360.0F, -90.0F, 90.0F);
/*      */   }
/*      */   
/*      */   public boolean canSprint() {
/* 4150 */     return false;
/*      */   }
/*      */   
/*      */   public float maxUpStep() {
/* 4154 */     return 0.0F;
/*      */   }
/*      */   
/*      */   public void onExplosionHit(Entity explosionCausedBy) {}
/*      */   
/*      */   public final boolean isRemoved() {
/* 4168 */     return (this.removalReason != null);
/*      */   }
/*      */   
/*      */   public RemovalReason getRemovalReason() {
/* 4172 */     return this.removalReason;
/*      */   }
/*      */   
/*      */   public final void setRemoved(RemovalReason reason) {
/* 4177 */     if (this.removalReason == null)
/* 4178 */       this.removalReason = reason; 
/* 4180 */     if (this.removalReason.shouldDestroy())
/* 4181 */       stopRiding(); 
/* 4183 */     getPassengers().forEach(Entity::stopRiding);
/* 4184 */     this.levelCallback.onRemove(reason);
/* 4185 */     onRemoval(reason);
/*      */   }
/*      */   
/*      */   protected void unsetRemoved() {
/* 4190 */     this.removalReason = null;
/*      */   }
/*      */   
/*      */   public enum MovementEmission {
/* 4194 */     NONE(false, false),
/* 4195 */     SOUNDS(true, false),
/* 4196 */     EVENTS(false, true),
/* 4197 */     ALL(true, true);
/*      */     
/*      */     private final boolean sounds;
/*      */     
/*      */     private final boolean events;
/*      */     
/*      */     MovementEmission(boolean sounds, boolean events) {
/* 4203 */       this.sounds = sounds;
/* 4204 */       this.events = events;
/*      */     }
/*      */     
/*      */     public boolean emitsAnything() {
/* 4208 */       return (this.events || this.sounds);
/*      */     }
/*      */     
/*      */     public boolean emitsEvents() {
/* 4212 */       return this.events;
/*      */     }
/*      */     
/*      */     public boolean emitsSounds() {
/* 4216 */       return this.sounds;
/*      */     }
/*      */   }
/*      */   
/*      */   public enum RemovalReason {
/* 4222 */     KILLED(true, false),
/* 4224 */     DISCARDED(true, false),
/* 4226 */     UNLOADED_TO_CHUNK(false, true),
/* 4228 */     UNLOADED_WITH_PLAYER(false, false),
/* 4230 */     CHANGED_DIMENSION(false, false);
/*      */     
/*      */     private final boolean destroy;
/*      */     
/*      */     private final boolean save;
/*      */     
/*      */     RemovalReason(boolean destroy, boolean save) {
/* 4238 */       this.destroy = destroy;
/* 4239 */       this.save = save;
/*      */     }
/*      */     
/*      */     public boolean shouldDestroy() {
/* 4244 */       return this.destroy;
/*      */     }
/*      */     
/*      */     public boolean shouldSave() {
/* 4249 */       return this.save;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setLevelCallback(EntityInLevelCallback levelCallback) {
/* 4255 */     this.levelCallback = levelCallback;
/*      */   }
/*      */   
/*      */   public boolean shouldBeSaved() {
/* 4260 */     if (this.removalReason != null && !this.removalReason.shouldSave())
/* 4261 */       return false; 
/* 4263 */     if (isPassenger())
/* 4264 */       return false; 
/* 4266 */     return (!isVehicle() || !hasExactlyOnePlayerPassenger());
/*      */   }
/*      */   
/*      */   public boolean isAlwaysTicking() {
/* 4271 */     return false;
/*      */   }
/*      */   
/*      */   public boolean mayInteract(ServerLevel level, BlockPos pos) {
/* 4276 */     return true;
/*      */   }
/*      */   
/*      */   public boolean isFlyingVehicle() {
/* 4280 */     return false;
/*      */   }
/*      */   
/*      */   public Level level() {
/* 4285 */     return this.level;
/*      */   }
/*      */   
/*      */   protected void setLevel(Level level) {
/* 4289 */     this.level = level;
/*      */   }
/*      */   
/*      */   public DamageSources damageSources() {
/* 4293 */     return level().damageSources();
/*      */   }
/*      */   
/*      */   public RegistryAccess registryAccess() {
/* 4297 */     return level().registryAccess();
/*      */   }
/*      */   
/*      */   protected void lerpPositionAndRotationStep(int stepsToTarget, double targetX, double targetY, double targetZ, double targetYRot, double targetXRot) {
/* 4301 */     double alpha = 1.0D / stepsToTarget;
/* 4303 */     double x = Mth.lerp(alpha, getX(), targetX);
/* 4304 */     double y = Mth.lerp(alpha, getY(), targetY);
/* 4305 */     double z = Mth.lerp(alpha, getZ(), targetZ);
/* 4307 */     float yRot = (float)Mth.rotLerp(alpha, getYRot(), targetYRot);
/* 4309 */     float xRot = (float)Mth.lerp(alpha, getXRot(), targetXRot);
/* 4311 */     setPos(x, y, z);
/* 4312 */     setRot(yRot, xRot);
/*      */   }
/*      */   
/*      */   public RandomSource getRandom() {
/* 4316 */     return this.random;
/*      */   }
/*      */   
/*      */   public Vec3 getKnownMovement() {
/* 4320 */     LivingEntity livingEntity = getControllingPassenger();
/* 4320 */     if (livingEntity instanceof Player) {
/* 4320 */       Player controller = (Player)livingEntity;
/* 4320 */       if (isAlive())
/* 4321 */         return controller.getKnownMovement(); 
/*      */     } 
/* 4323 */     return getDeltaMovement();
/*      */   }
/*      */   
/*      */   public Vec3 getKnownSpeed() {
/* 4327 */     LivingEntity livingEntity = getControllingPassenger();
/* 4327 */     if (livingEntity instanceof Player) {
/* 4327 */       Player controller = (Player)livingEntity;
/* 4327 */       if (isAlive())
/* 4328 */         return controller.getKnownSpeed(); 
/*      */     } 
/* 4330 */     return this.lastKnownSpeed;
/*      */   }
/*      */   
/*      */   public ItemStack getWeaponItem() {
/* 4335 */     return null;
/*      */   }
/*      */   
/*      */   public Optional<ResourceKey<LootTable>> getLootTable() {
/* 4339 */     return this.type.getDefaultLootTable();
/*      */   }
/*      */   
/*      */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 4343 */     applyImplicitComponentIfPresent(components, DataComponents.CUSTOM_NAME);
/* 4344 */     applyImplicitComponentIfPresent(components, DataComponents.CUSTOM_DATA);
/*      */   }
/*      */   
/*      */   public final void applyComponentsFromItemStack(ItemStack stack) {
/* 4348 */     applyImplicitComponents((DataComponentGetter)stack.getComponents());
/*      */   }
/*      */   
/*      */   public <T> T get(DataComponentType<? extends T> type) {
/* 4353 */     if (type == DataComponents.CUSTOM_NAME)
/* 4354 */       return castComponentValue((DataComponentType)type, getCustomName()); 
/* 4357 */     if (type == DataComponents.CUSTOM_DATA)
/* 4358 */       return castComponentValue((DataComponentType)type, this.customData); 
/* 4361 */     return (T)typeHolder().components().get(type);
/*      */   }
/*      */   
/*      */   @Contract("_,!null->!null;_,_->_")
/*      */   protected static <T> T castComponentValue(DataComponentType<T> type, Object value) {
/* 4367 */     return (T)value;
/*      */   }
/*      */   
/*      */   public <T> void setComponent(DataComponentType<T> type, T value) {
/* 4371 */     applyImplicitComponent(type, value);
/*      */   }
/*      */   
/*      */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 4376 */     if (type == DataComponents.CUSTOM_NAME) {
/* 4377 */       setCustomName(castComponentValue(DataComponents.CUSTOM_NAME, value));
/* 4378 */       return true;
/*      */     } 
/* 4381 */     if (type == DataComponents.CUSTOM_DATA) {
/* 4382 */       this.customData = castComponentValue(DataComponents.CUSTOM_DATA, value);
/* 4383 */       return true;
/*      */     } 
/* 4386 */     return false;
/*      */   }
/*      */   
/*      */   protected <T> boolean applyImplicitComponentIfPresent(DataComponentGetter components, DataComponentType<T> type) {
/* 4390 */     T value = (T)components.get(type);
/* 4391 */     if (value != null)
/* 4392 */       return applyImplicitComponent(type, value); 
/* 4394 */     return false;
/*      */   }
/*      */   
/*      */   private static final class EntityPathElement extends Record implements ProblemReporter.PathElement {
/*      */     private final Entity entity;
/*      */     
/*      */     private EntityPathElement(Entity entity) {
/* 4397 */       this.entity = entity;
/*      */     }
/*      */     
/*      */     public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/Entity$EntityPathElement;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #4397	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/Entity$EntityPathElement;
/*      */     }
/*      */     
/*      */     public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/Entity$EntityPathElement;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #4397	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/entity/Entity$EntityPathElement;
/*      */     }
/*      */     
/*      */     public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/Entity$EntityPathElement;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #4397	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/entity/Entity$EntityPathElement;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*      */     
/*      */     public Entity entity() {
/* 4397 */       return this.entity;
/*      */     }
/*      */     
/*      */     public String get() {
/* 4400 */       return this.entity.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   public ProblemReporter.PathElement problemPath() {
/* 4405 */     return new EntityPathElement(this);
/*      */   }
/*      */   
/*      */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {}
/*      */   
/*      */   public AABB getFluidInteractionBox() {
/* 4414 */     double margin = 0.001D;
/* 4415 */     AABB box = getBoundingBox().deflate(0.001D);
/* 4417 */     Entity vehicle = getVehicle();
/* 4418 */     if (vehicle != null)
/* 4419 */       box = vehicle.modifyPassengerFluidInteractionBox(box); 
/* 4421 */     return box;
/*      */   }
/*      */   
/*      */   protected AABB modifyPassengerFluidInteractionBox(AABB passengerBox) {
/* 4425 */     return passengerBox;
/*      */   }
/*      */   
/*      */   protected abstract void defineSynchedData(SynchedEntityData.Builder paramBuilder);
/*      */   
/*      */   public abstract boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat);
/*      */   
/*      */   protected abstract void readAdditionalSaveData(ValueInput paramValueInput);
/*      */   
/*      */   protected abstract void addAdditionalSaveData(ValueOutput paramValueOutput);
/*      */   
/*      */   @FunctionalInterface
/*      */   public static interface MoveFunction {
/*      */     void accept(Entity param1Entity, double param1Double1, double param1Double2, double param1Double3);
/*      */   }
/*      */   
/*      */   @Retention(RetentionPolicy.CLASS)
/*      */   @Target({ElementType.TYPE_USE})
/*      */   public static @interface Flags {}
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\Entity.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */