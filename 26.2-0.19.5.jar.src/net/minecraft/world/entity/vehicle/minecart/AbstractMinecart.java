/*     */ package net.minecraft.world.entity.vehicle.minecart;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InterpolationHandler;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.DismountHelper;
/*     */ import net.minecraft.world.entity.vehicle.VehicleEntity;
/*     */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.BaseRailBlock;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.PoweredRailBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class AbstractMinecart extends VehicleEntity {
/*  51 */   private static final Vec3 LOWERED_PASSENGER_ATTACHMENT = new Vec3(0.0D, 0.0D, 0.0D);
/*     */   
/*  53 */   private static final EntityDataAccessor<Optional<BlockState>> DATA_ID_CUSTOM_DISPLAY_BLOCK = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
/*     */   
/*  54 */   private static final EntityDataAccessor<Integer> DATA_ID_DISPLAY_OFFSET = SynchedEntityData.defineId(AbstractMinecart.class, EntityDataSerializers.INT);
/*     */   
/*  55 */   private static final ImmutableMap<Pose, ImmutableList<Integer>> POSE_DISMOUNT_HEIGHTS = ImmutableMap.of(Pose.STANDING, 
/*  56 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1)), Pose.CROUCHING, 
/*  57 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(-1)), Pose.SWIMMING, 
/*  58 */       ImmutableList.of(Integer.valueOf(0), Integer.valueOf(1)));
/*     */   
/*     */   protected static final float WATER_SLOWDOWN_FACTOR = 0.95F;
/*     */   
/*     */   private static final boolean DEFAULT_FLIPPED_ROTATION = false;
/*     */   
/*     */   private boolean onRails;
/*     */   
/*     */   private boolean flipped = false;
/*     */   
/*     */   private final MinecartBehavior behavior;
/*     */   
/*     */   private static final Map<RailShape, Pair<Vec3i, Vec3i>> EXITS;
/*     */   
/*     */   protected AbstractMinecart(EntityType<?> type, Level level) {
/*  67 */     super(type, level);
/*  68 */     this.blocksBuilding = true;
/*  69 */     if (useExperimentalMovement(level)) {
/*  70 */       this.behavior = new NewMinecartBehavior(this);
/*     */     } else {
/*  72 */       this.behavior = new OldMinecartBehavior(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected AbstractMinecart(EntityType<?> type, Level level, double x, double y, double z) {
/*  77 */     this(type, level);
/*  78 */     setInitialPos(x, y, z);
/*     */   }
/*     */   
/*     */   public void setInitialPos(double x, double y, double z) {
/*  82 */     setPos(x, y, z);
/*  85 */     this.xo = x;
/*  86 */     this.yo = y;
/*  87 */     this.zo = z;
/*     */   }
/*     */   
/*     */   public static <T extends AbstractMinecart> T createMinecart(Level level, double x, double y, double z, EntityType<T> type, EntitySpawnReason reason, ItemStack itemStack, Player player) {
/*  91 */     AbstractMinecart abstractMinecart = (AbstractMinecart)type.create(level, reason);
/*  92 */     if (abstractMinecart != null) {
/*  93 */       abstractMinecart.setInitialPos(x, y, z);
/*  94 */       EntityType.createDefaultStackConfig(level, itemStack, (LivingEntity)player).apply((Entity)abstractMinecart);
/*  96 */       MinecartBehavior minecartBehavior = abstractMinecart.getBehavior();
/*  96 */       if (minecartBehavior instanceof NewMinecartBehavior) {
/*  96 */         NewMinecartBehavior newMinecartBehavior = (NewMinecartBehavior)minecartBehavior;
/*  97 */         BlockPos currentPos = abstractMinecart.getCurrentBlockPosOrRailBelow();
/*  98 */         BlockState currentState = level.getBlockState(currentPos);
/*  99 */         newMinecartBehavior.adjustToRails(currentPos, currentState, true);
/*     */       } 
/*     */     } 
/* 102 */     return (T)abstractMinecart;
/*     */   }
/*     */   
/*     */   public MinecartBehavior getBehavior() {
/* 106 */     return this.behavior;
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 111 */     return Entity.MovementEmission.EVENTS;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 116 */     super.defineSynchedData(entityData);
/* 117 */     entityData.define(DATA_ID_CUSTOM_DISPLAY_BLOCK, Optional.empty());
/* 118 */     entityData.define(DATA_ID_DISPLAY_OFFSET, Integer.valueOf(getDefaultDisplayOffset()));
/*     */   }
/*     */   
/*     */   public boolean canCollideWith(Entity entity) {
/* 123 */     return AbstractBoat.canVehicleCollide((Entity)this, entity);
/*     */   }
/*     */   
/*     */   public boolean isPushable() {
/* 128 */     return true;
/*     */   }
/*     */   
/*     */   public Vec3 getRelativePortalPosition(Direction.Axis axis, BlockUtil.FoundRectangle portalArea) {
/* 133 */     return LivingEntity.resetForwardDirectionOfRelativePortalPosition(super.getRelativePortalPosition(axis, portalArea));
/*     */   }
/*     */   
/*     */   protected Vec3 getPassengerAttachmentPoint(Entity passenger, EntityDimensions dimensions, float scale) {
/* 140 */     boolean shouldLowerAttachmentPoint = (passenger instanceof net.minecraft.world.entity.npc.villager.Villager || passenger instanceof net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader);
/* 141 */     if (shouldLowerAttachmentPoint)
/* 142 */       return LOWERED_PASSENGER_ATTACHMENT; 
/* 144 */     return super.getPassengerAttachmentPoint(passenger, dimensions, scale);
/*     */   }
/*     */   
/*     */   public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
/* 149 */     Direction forward = getMotionDirection();
/* 150 */     if (forward.getAxis() == Direction.Axis.Y)
/* 151 */       return super.getDismountLocationForPassenger(passenger); 
/* 154 */     int[][] offsets = DismountHelper.offsetsForDirection(forward);
/* 155 */     BlockPos vehicleBlockPos = blockPosition();
/* 156 */     BlockPos.MutableBlockPos targetBlockPos = new BlockPos.MutableBlockPos();
/* 158 */     ImmutableList<Pose> dismountPoses = passenger.getDismountPoses();
/* 160 */     for (UnmodifiableIterator<Pose> unmodifiableIterator1 = dismountPoses.iterator(); unmodifiableIterator1.hasNext(); ) {
/* 160 */       Pose pose = unmodifiableIterator1.next();
/* 161 */       EntityDimensions passengerDimensions = passenger.getDimensions(pose);
/* 164 */       float dismountAreaReach = Math.min(passengerDimensions.width(), 1.0F) / 2.0F;
/* 166 */       for (UnmodifiableIterator<Integer> unmodifiableIterator = ((ImmutableList)POSE_DISMOUNT_HEIGHTS.get(pose)).iterator(); unmodifiableIterator.hasNext(); ) {
/* 166 */         int offsetY = ((Integer)unmodifiableIterator.next()).intValue();
/* 167 */         for (int[] offsetXZ : offsets) {
/* 168 */           targetBlockPos.set(vehicleBlockPos.getX() + offsetXZ[0], vehicleBlockPos.getY() + offsetY, vehicleBlockPos.getZ() + offsetXZ[1]);
/* 170 */           double blockFloorHeight = level().getBlockFloorHeight(DismountHelper.nonClimbableShape((BlockGetter)level(), (BlockPos)targetBlockPos), () -> DismountHelper.nonClimbableShape((BlockGetter)level(), targetBlockPos.below()));
/* 171 */           if (DismountHelper.isBlockFloorValid(blockFloorHeight)) {
/* 177 */             AABB dismountCollisionBox = new AABB(-dismountAreaReach, 0.0D, -dismountAreaReach, dismountAreaReach, passengerDimensions.height(), dismountAreaReach);
/* 180 */             Vec3 location = Vec3.upFromBottomCenterOf((Vec3i)targetBlockPos, blockFloorHeight);
/* 181 */             if (DismountHelper.canDismountTo((CollisionGetter)level(), passenger, dismountCollisionBox.move(location))) {
/* 182 */               passenger.setPose(pose);
/* 183 */               return location;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 189 */     double vehicleTop = (getBoundingBox()).maxY;
/* 190 */     targetBlockPos.set(vehicleBlockPos.getX(), vehicleTop, vehicleBlockPos.getZ());
/* 192 */     for (UnmodifiableIterator<Pose> unmodifiableIterator2 = dismountPoses.iterator(); unmodifiableIterator2.hasNext(); ) {
/* 192 */       Pose pose = unmodifiableIterator2.next();
/* 193 */       double poseHeight = passenger.getDimensions(pose).height();
/* 194 */       int blockCoverageY = Mth.ceil(vehicleTop - targetBlockPos.getY() + poseHeight);
/* 195 */       double ceilingAboveVehicle = DismountHelper.findCeilingFrom((BlockPos)targetBlockPos, blockCoverageY, pos -> level().getBlockState(pos).getCollisionShape((BlockGetter)level(), pos));
/* 197 */       if (vehicleTop + poseHeight <= ceilingAboveVehicle) {
/* 198 */         passenger.setPose(pose);
/*     */         break;
/*     */       } 
/*     */     } 
/* 203 */     return super.getDismountLocationForPassenger(passenger);
/*     */   }
/*     */   
/*     */   protected float getBlockSpeedFactor() {
/* 208 */     BlockState blockState = level().getBlockState(blockPosition());
/* 209 */     if (blockState.is(BlockTags.RAILS))
/* 210 */       return 1.0F; 
/* 212 */     return super.getBlockSpeedFactor();
/*     */   }
/*     */   
/*     */   public void animateHurt(float yaw) {
/* 217 */     setHurtDir(-getHurtDir());
/* 218 */     setHurtTime(10);
/* 219 */     setDamage(getDamage() + getDamage() * 10.0F);
/*     */   }
/*     */   
/*     */   public boolean isPickable() {
/* 224 */     return !isRemoved();
/*     */   }
/*     */   
/*     */   static {
/* 227 */     EXITS = Maps.newEnumMap((Map)Util.make(() -> {
/*     */             Vec3i xNeg = Direction.WEST.getUnitVec3i();
/*     */             Vec3i xPos = Direction.EAST.getUnitVec3i();
/*     */             Vec3i zNeg = Direction.NORTH.getUnitVec3i();
/*     */             Vec3i zPos = Direction.SOUTH.getUnitVec3i();
/*     */             Vec3i xNegBelow = xNeg.below();
/*     */             Vec3i xPosBelow = xPos.below();
/*     */             Vec3i zNegBelow = zNeg.below();
/*     */             Vec3i zPosBelow = zPos.below();
/*     */             return ImmutableMap.of(RailShape.NORTH_SOUTH, Pair.of(zNeg, zPos), RailShape.EAST_WEST, Pair.of(xNeg, xPos), RailShape.ASCENDING_EAST, Pair.of(xNegBelow, xPos), RailShape.ASCENDING_WEST, Pair.of(xNeg, xPosBelow), RailShape.ASCENDING_NORTH, Pair.of(zNeg, zPosBelow), RailShape.ASCENDING_SOUTH, Pair.of(zNegBelow, zPos), RailShape.SOUTH_EAST, Pair.of(zPos, xPos), RailShape.SOUTH_WEST, Pair.of(zPos, xNeg), RailShape.NORTH_WEST, Pair.of(zNeg, xNeg), RailShape.NORTH_EAST, Pair.of(zNeg, xPos));
/*     */           }));
/*     */   }
/*     */   
/*     */   public static Pair<Vec3i, Vec3i> exits(RailShape shape) {
/* 253 */     return EXITS.get(shape);
/*     */   }
/*     */   
/*     */   public Direction getMotionDirection() {
/* 258 */     return this.behavior.getMotionDirection();
/*     */   }
/*     */   
/*     */   protected double getDefaultGravity() {
/* 263 */     return isInWater() ? 0.005D : 0.04D;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 268 */     if (getHurtTime() > 0)
/* 269 */       setHurtTime(getHurtTime() - 1); 
/* 271 */     if (getDamage() > 0.0F)
/* 272 */       setDamage(getDamage() - 1.0F); 
/* 274 */     checkBelowWorld();
/* 275 */     computeSpeed();
/* 277 */     handlePortal();
/* 279 */     this.behavior.tick();
/* 281 */     updateFluidInteraction();
/* 283 */     if (isInLava()) {
/* 284 */       lavaIgnite();
/* 285 */       lavaHurt();
/* 286 */       this.fallDistance *= 0.5D;
/*     */     } 
/* 289 */     this.firstTick = false;
/*     */   }
/*     */   
/*     */   public boolean isFirstTick() {
/* 293 */     return this.firstTick;
/*     */   }
/*     */   
/*     */   public BlockPos getCurrentBlockPosOrRailBelow() {
/* 297 */     int xt = Mth.floor(getX());
/* 298 */     int yt = Mth.floor(getY());
/* 299 */     int zt = Mth.floor(getZ());
/* 301 */     if (useExperimentalMovement(level())) {
/* 302 */       double y = getY() - 0.1D - 9.999999747378752E-6D;
/* 303 */       if (level().getBlockState(BlockPos.containing(xt, y, zt)).is(BlockTags.RAILS))
/* 304 */         yt = Mth.floor(y); 
/* 306 */     } else if (level().getBlockState(new BlockPos(xt, yt - 1, zt)).is(BlockTags.RAILS)) {
/* 307 */       yt--;
/*     */     } 
/* 310 */     return new BlockPos(xt, yt, zt);
/*     */   }
/*     */   
/*     */   protected double getMaxSpeed(ServerLevel level) {
/* 314 */     return this.behavior.getMaxSpeed(level);
/*     */   }
/*     */   
/*     */   public void activateMinecart(ServerLevel level, int xt, int yt, int zt, boolean state) {}
/*     */   
/*     */   public void lerpPositionAndRotationStep(int stepsToTarget, double targetX, double targetY, double targetZ, double targetYRot, double targetXRot) {
/* 322 */     super.lerpPositionAndRotationStep(stepsToTarget, targetX, targetY, targetZ, targetYRot, targetXRot);
/*     */   }
/*     */   
/*     */   public void applyGravity() {
/* 327 */     super.applyGravity();
/*     */   }
/*     */   
/*     */   public void reapplyPosition() {
/* 332 */     super.reapplyPosition();
/*     */   }
/*     */   
/*     */   public boolean updateFluidInteraction() {
/* 337 */     return super.updateFluidInteraction();
/*     */   }
/*     */   
/*     */   public Vec3 getKnownMovement() {
/* 342 */     return this.behavior.getKnownMovement(super.getKnownMovement());
/*     */   }
/*     */   
/*     */   public InterpolationHandler getInterpolation() {
/* 347 */     return this.behavior.getInterpolation();
/*     */   }
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 352 */     super.recreateFromPacket(packet);
/* 353 */     this.behavior.lerpMotion(getDeltaMovement());
/*     */   }
/*     */   
/*     */   public void lerpMotion(Vec3 movement) {
/* 358 */     this.behavior.lerpMotion(movement);
/*     */   }
/*     */   
/*     */   protected void moveAlongTrack(ServerLevel level) {
/* 362 */     this.behavior.moveAlongTrack(level);
/*     */   }
/*     */   
/*     */   protected void comeOffTrack(ServerLevel level) {
/* 366 */     double maxSpeed = getMaxSpeed(level);
/* 367 */     Vec3 movement = getDeltaMovement();
/* 368 */     setDeltaMovement(
/* 369 */         Mth.clamp(movement.x, -maxSpeed, maxSpeed), movement.y, 
/*     */         
/* 371 */         Mth.clamp(movement.z, -maxSpeed, maxSpeed));
/* 373 */     if (onGround())
/* 374 */       setDeltaMovement(getDeltaMovement().scale(0.5D)); 
/* 376 */     move(MoverType.SELF, getDeltaMovement());
/* 378 */     if (!onGround())
/* 379 */       setDeltaMovement(getDeltaMovement().scale(getAirDrag())); 
/*     */   }
/*     */   
/*     */   protected float getAirDrag() {
/* 385 */     return 0.95F;
/*     */   }
/*     */   
/*     */   protected double makeStepAlongTrack(BlockPos pos, RailShape shape, double movementLeft) {
/* 390 */     return this.behavior.stepAlongTrack(pos, shape, movementLeft);
/*     */   }
/*     */   
/*     */   public void move(MoverType moverType, Vec3 delta) {
/* 395 */     if (useExperimentalMovement(level())) {
/* 396 */       Vec3 toPosition = position().add(delta);
/* 397 */       super.move(moverType, delta);
/* 398 */       boolean shouldContinue = this.behavior.pushAndPickupEntities();
/* 399 */       if (shouldContinue)
/* 401 */         super.move(moverType, toPosition.subtract(position())); 
/* 403 */       if (moverType.equals(MoverType.PISTON))
/* 404 */         this.onRails = false; 
/*     */     } else {
/* 407 */       super.move(moverType, delta);
/* 409 */       applyEffectsFromBlocks();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void applyEffectsFromBlocks() {
/* 415 */     if (useExperimentalMovement(level())) {
/* 416 */       super.applyEffectsFromBlocks();
/*     */     } else {
/* 419 */       applyEffectsFromBlocks(position(), position());
/* 420 */       clearMovementThisTick();
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isOnRails() {
/* 426 */     return this.onRails;
/*     */   }
/*     */   
/*     */   public void setOnRails(boolean onRails) {
/* 430 */     this.onRails = onRails;
/*     */   }
/*     */   
/*     */   public boolean isFlipped() {
/* 434 */     return this.flipped;
/*     */   }
/*     */   
/*     */   public void setFlipped(boolean flipped) {
/* 438 */     this.flipped = flipped;
/*     */   }
/*     */   
/*     */   public Vec3 getRedstoneDirection(BlockPos pos) {
/* 442 */     BlockState state = level().getBlockState(pos);
/* 443 */     if (!state.is(Blocks.POWERED_RAIL) || !((Boolean)state.getValue((Property)PoweredRailBlock.POWERED)).booleanValue())
/* 444 */       return Vec3.ZERO; 
/* 447 */     RailShape shape = (RailShape)state.getValue(((BaseRailBlock)state.getBlock()).getShapeProperty());
/* 449 */     if (shape == RailShape.EAST_WEST) {
/* 450 */       if (isRedstoneConductor(pos.west()))
/* 451 */         return new Vec3(1.0D, 0.0D, 0.0D); 
/* 452 */       if (isRedstoneConductor(pos.east()))
/* 453 */         return new Vec3(-1.0D, 0.0D, 0.0D); 
/* 455 */     } else if (shape == RailShape.NORTH_SOUTH) {
/* 456 */       if (isRedstoneConductor(pos.north()))
/* 457 */         return new Vec3(0.0D, 0.0D, 1.0D); 
/* 458 */       if (isRedstoneConductor(pos.south()))
/* 459 */         return new Vec3(0.0D, 0.0D, -1.0D); 
/*     */     } 
/* 463 */     return Vec3.ZERO;
/*     */   }
/*     */   
/*     */   public boolean isRedstoneConductor(BlockPos pos) {
/* 467 */     return level().getBlockState(pos).isRedstoneConductor((BlockGetter)level(), pos);
/*     */   }
/*     */   
/*     */   protected Vec3 applyNaturalSlowdown(Vec3 movement) {
/* 471 */     double slowdownFactor = this.behavior.getSlowdownFactor();
/* 472 */     Vec3 newMovement = movement.multiply(slowdownFactor, 0.0D, slowdownFactor);
/* 474 */     if (isInWater())
/* 475 */       newMovement = newMovement.scale(0.949999988079071D); 
/* 477 */     return newMovement;
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 482 */     setCustomDisplayBlockState(input.read("DisplayState", BlockState.CODEC));
/* 483 */     setDisplayOffset(input.getIntOr("DisplayOffset", getDefaultDisplayOffset()));
/* 485 */     this.flipped = input.getBooleanOr("FlippedRotation", false);
/* 486 */     this.firstTick = input.getBooleanOr("HasTicked", false);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 491 */     getCustomDisplayBlockState().ifPresent(blockState -> output.store("DisplayState", BlockState.CODEC, blockState));
/* 492 */     int displayOffset = getDisplayOffset();
/* 493 */     if (displayOffset != getDefaultDisplayOffset())
/* 494 */       output.putInt("DisplayOffset", displayOffset); 
/* 496 */     output.putBoolean("FlippedRotation", this.flipped);
/* 497 */     output.putBoolean("HasTicked", this.firstTick);
/*     */   }
/*     */   
/*     */   public void push(Entity entity) {
/* 502 */     if (level().isClientSide())
/*     */       return; 
/* 505 */     if (entity.noPhysics || this.noPhysics)
/*     */       return; 
/* 509 */     if (hasPassenger(entity))
/*     */       return; 
/* 513 */     double xa = entity.getX() - getX();
/* 514 */     double za = entity.getZ() - getZ();
/* 516 */     double dd = xa * xa + za * za;
/* 517 */     if (dd >= 9.999999747378752E-5D) {
/* 518 */       dd = Math.sqrt(dd);
/* 519 */       xa /= dd;
/* 520 */       za /= dd;
/* 521 */       double pow = 1.0D / dd;
/* 522 */       if (pow > 1.0D)
/* 523 */         pow = 1.0D; 
/* 525 */       xa *= pow;
/* 526 */       za *= pow;
/* 527 */       xa *= 0.10000000149011612D;
/* 528 */       za *= 0.10000000149011612D;
/* 530 */       xa *= 0.5D;
/* 531 */       za *= 0.5D;
/* 533 */       if (entity instanceof AbstractMinecart) {
/* 533 */         AbstractMinecart otherMinecart = (AbstractMinecart)entity;
/* 534 */         pushOtherMinecart(otherMinecart, xa, za);
/*     */       } else {
/* 536 */         push(-xa, 0.0D, -za);
/* 537 */         entity.push(xa / 4.0D, 0.0D, za / 4.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void pushOtherMinecart(AbstractMinecart otherMinecart, double xa, double za) {
/*     */     double xo, zo;
/* 545 */     if (useExperimentalMovement(level())) {
/* 546 */       xo = (getDeltaMovement()).x;
/* 547 */       zo = (getDeltaMovement()).z;
/*     */     } else {
/* 549 */       xo = otherMinecart.getX() - getX();
/* 550 */       zo = otherMinecart.getZ() - getZ();
/*     */     } 
/* 553 */     Vec3 dir = (new Vec3(xo, 0.0D, zo)).normalize();
/* 554 */     Vec3 facing = (new Vec3(Mth.cos((getYRot() * 0.017453292F)), 0.0D, Mth.sin((getYRot() * 0.017453292F)))).normalize();
/* 556 */     double dot = Math.abs(dir.dot(facing));
/* 558 */     if (dot < 0.800000011920929D && !useExperimentalMovement(level()))
/*     */       return; 
/* 562 */     Vec3 movement = getDeltaMovement();
/* 563 */     Vec3 entityMovement = otherMinecart.getDeltaMovement();
/* 565 */     if (otherMinecart.isFurnace() && !isFurnace()) {
/* 566 */       setDeltaMovement(movement.multiply(0.2D, 1.0D, 0.2D));
/* 567 */       push(entityMovement.x - xa, 0.0D, entityMovement.z - za);
/* 568 */       otherMinecart.setDeltaMovement(entityMovement.multiply(0.95D, 1.0D, 0.95D));
/* 569 */     } else if (!otherMinecart.isFurnace() && isFurnace()) {
/* 570 */       otherMinecart.setDeltaMovement(entityMovement.multiply(0.2D, 1.0D, 0.2D));
/* 571 */       otherMinecart.push(movement.x + xa, 0.0D, movement.z + za);
/* 572 */       setDeltaMovement(movement.multiply(0.95D, 1.0D, 0.95D));
/*     */     } else {
/* 574 */       double xdd = (entityMovement.x + movement.x) / 2.0D;
/* 575 */       double zdd = (entityMovement.z + movement.z) / 2.0D;
/* 577 */       setDeltaMovement(movement.multiply(0.2D, 1.0D, 0.2D));
/* 578 */       push(xdd - xa, 0.0D, zdd - za);
/* 579 */       otherMinecart.setDeltaMovement(entityMovement.multiply(0.2D, 1.0D, 0.2D));
/* 580 */       otherMinecart.push(xdd + xa, 0.0D, zdd + za);
/*     */     } 
/*     */   }
/*     */   
/*     */   public BlockState getDisplayBlockState() {
/* 585 */     return getCustomDisplayBlockState().orElseGet(this::getDefaultDisplayBlockState);
/*     */   }
/*     */   
/*     */   private Optional<BlockState> getCustomDisplayBlockState() {
/* 589 */     return (Optional<BlockState>)getEntityData().get(DATA_ID_CUSTOM_DISPLAY_BLOCK);
/*     */   }
/*     */   
/*     */   public BlockState getDefaultDisplayBlockState() {
/* 593 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */   
/*     */   public int getDisplayOffset() {
/* 597 */     return ((Integer)getEntityData().get(DATA_ID_DISPLAY_OFFSET)).intValue();
/*     */   }
/*     */   
/*     */   public int getDefaultDisplayOffset() {
/* 601 */     return 6;
/*     */   }
/*     */   
/*     */   public void setCustomDisplayBlockState(Optional<BlockState> state) {
/* 605 */     getEntityData().set(DATA_ID_CUSTOM_DISPLAY_BLOCK, state);
/*     */   }
/*     */   
/*     */   public void setDisplayOffset(int offset) {
/* 609 */     getEntityData().set(DATA_ID_DISPLAY_OFFSET, Integer.valueOf(offset));
/*     */   }
/*     */   
/*     */   public static boolean useExperimentalMovement(Level level) {
/* 613 */     return level.enabledFeatures().contains(FeatureFlags.MINECART_IMPROVEMENTS);
/*     */   }
/*     */   
/*     */   public boolean isRideable() {
/* 620 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFurnace() {
/* 625 */     return false;
/*     */   }
/*     */   
/*     */   public abstract ItemStack getPickResult();
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\vehicle\minecart\AbstractMinecart.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */