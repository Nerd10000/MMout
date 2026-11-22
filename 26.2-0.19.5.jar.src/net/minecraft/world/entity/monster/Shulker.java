/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.InterpolationHandler;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.golem.AbstractGolem;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.ShulkerBullet;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.entity.EntityTypeTest;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.joml.Vector3f;
/*     */ import org.joml.Vector3fc;
/*     */ 
/*     */ public class Shulker extends AbstractGolem implements Enemy {
/*  70 */   private static final Identifier COVERED_ARMOR_MODIFIER_ID = Identifier.withDefaultNamespace("covered");
/*     */   
/*  71 */   private static final AttributeModifier COVERED_ARMOR_MODIFIER = new AttributeModifier(COVERED_ARMOR_MODIFIER_ID, 20.0D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*  73 */   protected static final EntityDataAccessor<Direction> DATA_ATTACH_FACE_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.DIRECTION);
/*     */   
/*  74 */   protected static final EntityDataAccessor<Byte> DATA_PEEK_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.BYTE);
/*     */   
/*  75 */   protected static final EntityDataAccessor<Byte> DATA_COLOR_ID = SynchedEntityData.defineId(Shulker.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final int TELEPORT_STEPS = 6;
/*     */   
/*     */   private static final byte NO_COLOR = 16;
/*     */   
/*     */   private static final byte DEFAULT_COLOR = 16;
/*     */   
/*     */   private static final int MAX_TELEPORT_DISTANCE = 8;
/*     */   
/*     */   private static final int OTHER_SHULKER_SCAN_RADIUS = 8;
/*     */   
/*     */   private static final int OTHER_SHULKER_LIMIT = 5;
/*     */   
/*     */   private static final float PEEK_PER_TICK = 0.05F;
/*     */   
/*     */   private static final byte DEFAULT_PEEK = 0;
/*     */   
/*  86 */   private static final Direction DEFAULT_ATTACH_FACE = Direction.DOWN;
/*     */   
/*  89 */   private static final Vector3fc FORWARD = Direction.SOUTH.getUnitVec3f();
/*     */   
/*     */   private static final float MAX_SCALE = 3.0F;
/*     */   
/*     */   private float currentPeekAmountO;
/*     */   
/*     */   private float currentPeekAmount;
/*     */   
/*     */   private BlockPos clientOldAttachPosition;
/*     */   
/*     */   private int clientSideTeleportInterpolation;
/*     */   
/*     */   private static final float MAX_LID_OPEN = 1.0F;
/*     */   
/*     */   public Shulker(EntityType<? extends Shulker> type, Level level) {
/* 103 */     super(type, level);
/* 105 */     this.xpReward = 5;
/* 107 */     this.lookControl = new ShulkerLookControl((Mob)this);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 112 */     this.goalSelector.addGoal(1, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F, 0.02F, true));
/* 113 */     this.goalSelector.addGoal(4, new ShulkerAttackGoal());
/* 114 */     this.goalSelector.addGoal(7, new ShulkerPeekGoal());
/* 115 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/* 117 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { getClass() })).setAlertOthers(new Class[0]));
/* 118 */     this.targetSelector.addGoal(2, (Goal)new ShulkerNearestAttackGoal(this));
/* 119 */     this.targetSelector.addGoal(3, (Goal)new ShulkerDefenseAttackGoal(this));
/*     */   }
/*     */   
/*     */   private class ShulkerLookControl extends LookControl {
/*     */     public ShulkerLookControl(Mob mob) {
/* 124 */       super(mob);
/*     */     }
/*     */     
/*     */     protected void clampHeadRotationToBody() {}
/*     */     
/*     */     protected Optional<Float> getYRotD() {
/* 134 */       Direction attachFace = Shulker.this.getAttachFace().getOpposite();
/* 137 */       Vector3f forward = attachFace.getRotation().transform(new Vector3f(Shulker.FORWARD));
/* 139 */       Vec3i upNormal = attachFace.getUnitVec3i();
/* 140 */       Vector3f right = new Vector3f(upNormal.getX(), upNormal.getY(), upNormal.getZ());
/* 141 */       right.cross((Vector3fc)forward);
/* 143 */       double xd = this.wantedX - this.mob.getX();
/* 144 */       double yd = this.wantedY - this.mob.getEyeY();
/* 145 */       double zd = this.wantedZ - this.mob.getZ();
/* 148 */       Vector3f out = new Vector3f((float)xd, (float)yd, (float)zd);
/* 149 */       float deltaRight = right.dot((Vector3fc)out);
/* 150 */       float deltaForward = forward.dot((Vector3fc)out);
/* 152 */       return (Math.abs(deltaRight) > 1.0E-5F || Math.abs(deltaForward) > 1.0E-5F) ? Optional.<Float>of(Float.valueOf((float)(Mth.atan2(-deltaRight, deltaForward) * 57.2957763671875D))) : Optional.<Float>empty();
/*     */     }
/*     */     
/*     */     protected Optional<Float> getXRotD() {
/* 157 */       return Optional.of(Float.valueOf(0.0F));
/*     */     }
/*     */   }
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 163 */     return Entity.MovementEmission.NONE;
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 168 */     return SoundSource.HOSTILE;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 173 */     return SoundEvents.SHULKER_AMBIENT;
/*     */   }
/*     */   
/*     */   public void playAmbientSound() {
/* 178 */     if (!isClosed())
/* 179 */       super.playAmbientSound(); 
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 185 */     return SoundEvents.SHULKER_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 190 */     if (isClosed())
/* 191 */       return SoundEvents.SHULKER_HURT_CLOSED; 
/* 193 */     return SoundEvents.SHULKER_HURT;
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 198 */     super.defineSynchedData(entityData);
/* 200 */     entityData.define(DATA_ATTACH_FACE_ID, DEFAULT_ATTACH_FACE);
/* 201 */     entityData.define(DATA_PEEK_ID, Byte.valueOf((byte)0));
/* 202 */     entityData.define(DATA_COLOR_ID, Byte.valueOf((byte)16));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 206 */     return Mob.createMobAttributes()
/* 207 */       .add(Attributes.MAX_HEALTH, 30.0D);
/*     */   }
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/* 212 */     return new ShulkerBodyRotationControl((Mob)this);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 217 */     super.readAdditionalSaveData(input);
/* 219 */     setAttachFace(input.read("AttachFace", Direction.LEGACY_ID_CODEC).orElse(DEFAULT_ATTACH_FACE));
/* 220 */     this.entityData.set(DATA_PEEK_ID, Byte.valueOf(input.getByteOr("Peek", (byte)0)));
/* 221 */     this.entityData.set(DATA_COLOR_ID, Byte.valueOf(input.getByteOr("Color", (byte)16)));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 226 */     super.addAdditionalSaveData(output);
/* 228 */     output.store("AttachFace", Direction.LEGACY_ID_CODEC, getAttachFace());
/* 229 */     output.putByte("Peek", ((Byte)this.entityData.get(DATA_PEEK_ID)).byteValue());
/* 230 */     output.putByte("Color", ((Byte)this.entityData.get(DATA_COLOR_ID)).byteValue());
/*     */   }
/*     */   
/*     */   public void tick() {
/* 235 */     super.tick();
/* 237 */     if (!level().isClientSide() && !isPassenger() && !canStayAt(blockPosition(), getAttachFace()))
/* 238 */       findNewAttachment(); 
/* 241 */     if (updatePeekAmount())
/* 242 */       onPeekAmountChange(); 
/* 245 */     if (level().isClientSide())
/* 246 */       if (this.clientSideTeleportInterpolation > 0) {
/* 247 */         this.clientSideTeleportInterpolation--;
/*     */       } else {
/* 249 */         this.clientOldAttachPosition = null;
/*     */       }  
/*     */   }
/*     */   
/*     */   private void findNewAttachment() {
/* 256 */     Direction attachmentDirection = findAttachableSurface(blockPosition());
/* 257 */     if (attachmentDirection != null) {
/* 258 */       setAttachFace(attachmentDirection);
/*     */     } else {
/* 261 */       teleportSomewhere();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected AABB makeBoundingBox(Vec3 position) {
/* 267 */     float physPeek = getPhysicalPeek(this.currentPeekAmount);
/* 268 */     Direction direction = getAttachFace().getOpposite();
/* 270 */     return getProgressAabb(getScale(), direction, physPeek, position);
/*     */   }
/*     */   
/*     */   private static float getPhysicalPeek(float amount) {
/* 274 */     return 0.5F - Mth.sin(((0.5F + amount) * 3.1415927F)) * 0.5F;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 279 */     EntityDimensions dimension = super.getDefaultDimensions(pose);
/* 280 */     if (getAttachFace() == Direction.DOWN && this.currentPeekAmount > 0.0F) {
/* 281 */       float heightScaleFactor = 1.0F + this.currentPeekAmount;
/* 282 */       return dimension.scale(1.0F, heightScaleFactor);
/*     */     } 
/* 284 */     return dimension;
/*     */   }
/*     */   
/*     */   private boolean updatePeekAmount() {
/* 288 */     this.currentPeekAmountO = this.currentPeekAmount;
/* 289 */     float targetPeekAmount = getRawPeekAmount() * 0.01F;
/* 290 */     if (this.currentPeekAmount == targetPeekAmount)
/* 291 */       return false; 
/* 294 */     if (this.currentPeekAmount > targetPeekAmount) {
/* 295 */       this.currentPeekAmount = Mth.clamp(this.currentPeekAmount - 0.05F, targetPeekAmount, 1.0F);
/*     */     } else {
/* 297 */       this.currentPeekAmount = Mth.clamp(this.currentPeekAmount + 0.05F, 0.0F, targetPeekAmount);
/*     */     } 
/* 299 */     return true;
/*     */   }
/*     */   
/*     */   private void onPeekAmountChange() {
/* 303 */     reapplyPosition();
/* 304 */     refreshDimensions();
/* 306 */     float physicalPeek = getPhysicalPeek(this.currentPeekAmount);
/* 307 */     float physicalPeekOld = getPhysicalPeek(this.currentPeekAmountO);
/* 308 */     Direction direction = getAttachFace().getOpposite();
/* 310 */     float push = (physicalPeek - physicalPeekOld) * getScale();
/* 311 */     if (push <= 0.0F)
/*     */       return; 
/* 314 */     List<Entity> entities = level().getEntities((Entity)this, getProgressDeltaAabb(getScale(), direction, physicalPeekOld, physicalPeek, position()), EntitySelector.NO_SPECTATORS.and(e -> !e.isPassengerOfSameVehicle((Entity)this)));
/* 315 */     for (Entity entity : entities) {
/* 316 */       if (!(entity instanceof Shulker) && !entity.noPhysics)
/* 317 */         entity.move(MoverType.SHULKER, new Vec3((push * direction
/* 318 */               .getStepX()), (push * direction
/* 319 */               .getStepY()), (push * direction
/* 320 */               .getStepZ()))); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AABB getProgressAabb(float size, Direction direction, float progressTo, Vec3 position) {
/* 327 */     return getProgressDeltaAabb(size, direction, -1.0F, progressTo, position);
/*     */   }
/*     */   
/*     */   public static AABB getProgressDeltaAabb(float size, Direction direction, float progressFrom, float progressTo, Vec3 position) {
/* 331 */     AABB boundsAtBottomCenter = new AABB(-size * 0.5D, 0.0D, -size * 0.5D, size * 0.5D, size, size * 0.5D);
/* 332 */     double maxMovement = Math.max(progressFrom, progressTo);
/* 333 */     double minMovement = Math.min(progressFrom, progressTo);
/* 338 */     AABB aabb = boundsAtBottomCenter.expandTowards(direction.getStepX() * maxMovement * size, direction.getStepY() * maxMovement * size, direction.getStepZ() * maxMovement * size).contract(
/* 339 */         -direction.getStepX() * (1.0D + minMovement) * size, 
/* 340 */         -direction.getStepY() * (1.0D + minMovement) * size, 
/* 341 */         -direction.getStepZ() * (1.0D + minMovement) * size);
/* 343 */     return aabb.move(position.x, position.y, position.z);
/*     */   }
/*     */   
/*     */   public boolean startRiding(Entity entity, boolean force, boolean sendEventAndTriggers) {
/* 348 */     if (level().isClientSide()) {
/* 349 */       this.clientOldAttachPosition = null;
/* 350 */       this.clientSideTeleportInterpolation = 0;
/*     */     } 
/* 352 */     setAttachFace(Direction.DOWN);
/* 353 */     return super.startRiding(entity, force, sendEventAndTriggers);
/*     */   }
/*     */   
/*     */   public void stopRiding() {
/* 358 */     super.stopRiding();
/* 359 */     if (level().isClientSide())
/* 360 */       this.clientOldAttachPosition = blockPosition(); 
/* 362 */     this.yBodyRotO = 0.0F;
/* 363 */     this.yBodyRot = 0.0F;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 368 */     setYRot(0.0F);
/* 369 */     this.yHeadRot = getYRot();
/* 370 */     setOldPosAndRot();
/* 372 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public void move(MoverType moverType, Vec3 delta) {
/* 377 */     if (moverType == MoverType.SHULKER_BOX) {
/* 378 */       teleportSomewhere();
/*     */     } else {
/* 380 */       super.move(moverType, delta);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Vec3 getDeltaMovement() {
/* 386 */     return Vec3.ZERO;
/*     */   }
/*     */   
/*     */   public void setDeltaMovement(Vec3 deltaMovement) {}
/*     */   
/*     */   public void setPos(double x, double y, double z) {
/* 396 */     BlockPos oldPos = blockPosition();
/* 397 */     if (isPassenger()) {
/* 398 */       super.setPos(x, y, z);
/*     */     } else {
/* 400 */       super.setPos(Mth.floor(x) + 0.5D, Mth.floor(y + 0.5D), Mth.floor(z) + 0.5D);
/*     */     } 
/* 402 */     if (this.tickCount == 0)
/*     */       return; 
/* 406 */     BlockPos pos = blockPosition();
/* 407 */     if (!pos.equals(oldPos)) {
/* 408 */       this.entityData.set(DATA_PEEK_ID, Byte.valueOf((byte)0));
/* 409 */       this.needsSync = true;
/* 410 */       if (level().isClientSide() && !isPassenger() && !pos.equals(this.clientOldAttachPosition)) {
/* 411 */         this.clientOldAttachPosition = oldPos;
/* 412 */         this.clientSideTeleportInterpolation = 6;
/* 415 */         this.xOld = getX();
/* 416 */         this.yOld = getY();
/* 417 */         this.zOld = getZ();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Direction findAttachableSurface(BlockPos target) {
/* 423 */     for (Direction direction : Direction.values()) {
/* 424 */       if (canStayAt(target, direction))
/* 425 */         return direction; 
/*     */     } 
/* 428 */     return null;
/*     */   }
/*     */   
/*     */   private boolean canStayAt(BlockPos target, Direction face) {
/* 433 */     if (isPositionBlocked(target))
/* 434 */       return false; 
/* 437 */     Direction oppositeFace = face.getOpposite();
/* 438 */     if (!level().loadedAndEntityCanStandOnFace(target.relative(face), (Entity)this, oppositeFace))
/* 439 */       return false; 
/* 443 */     AABB fullyOpened = getProgressAabb(getScale(), oppositeFace, 1.0F, Vec3.atBottomCenterOf((Vec3i)target)).deflate(1.0E-6D);
/* 444 */     return level().noCollision((Entity)this, fullyOpened);
/*     */   }
/*     */   
/*     */   private boolean isPositionBlocked(BlockPos target) {
/* 448 */     BlockState state = level().getBlockState(target);
/* 449 */     if (state.isAir())
/* 450 */       return false; 
/* 454 */     boolean movingPistonInOurCurrentPosition = (state.is(Blocks.MOVING_PISTON) && target.equals(blockPosition()));
/* 455 */     return !movingPistonInOurCurrentPosition;
/*     */   }
/*     */   
/*     */   protected boolean teleportSomewhere() {
/* 459 */     if (isNoAi() || !isAlive())
/* 460 */       return false; 
/* 462 */     BlockPos current = blockPosition();
/* 463 */     for (int attempt = 0; attempt < 5; attempt++) {
/* 464 */       BlockPos target = current.offset(
/* 465 */           Mth.randomBetweenInclusive(this.random, -8, 8), 
/* 466 */           Mth.randomBetweenInclusive(this.random, -8, 8), 
/* 467 */           Mth.randomBetweenInclusive(this.random, -8, 8));
/* 469 */       if (target.getY() > level().getMinY() && level().isEmptyBlock(target) && level().getWorldBorder().isWithinBounds(target) && level().noCollision((Entity)this, (new AABB(target)).deflate(1.0E-6D))) {
/* 470 */         Direction attachmentDirection = findAttachableSurface(target);
/* 471 */         if (attachmentDirection != null) {
/* 472 */           unRide();
/* 475 */           setAttachFace(attachmentDirection);
/* 477 */           playSound(SoundEvents.SHULKER_TELEPORT, 1.0F, 1.0F);
/* 478 */           setPos(target.getX() + 0.5D, target.getY(), target.getZ() + 0.5D);
/* 479 */           level().gameEvent((Holder)GameEvent.TELEPORT, current, GameEvent.Context.of((Entity)this));
/* 480 */           this.entityData.set(DATA_PEEK_ID, Byte.valueOf((byte)0));
/* 481 */           setTarget(null);
/* 482 */           return true;
/*     */         } 
/*     */       } 
/*     */     } 
/* 486 */     return false;
/*     */   }
/*     */   
/*     */   public InterpolationHandler getInterpolation() {
/* 491 */     return null;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 496 */     if (isClosed()) {
/* 497 */       Entity directEntity = source.getDirectEntity();
/* 498 */       if (directEntity instanceof net.minecraft.world.entity.projectile.arrow.AbstractArrow)
/* 499 */         return false; 
/*     */     } 
/* 503 */     if (super.hurtServer(level, source, damage)) {
/* 504 */       if (getHealth() < getMaxHealth() * 0.5D && this.random.nextInt(4) == 0) {
/* 505 */         teleportSomewhere();
/* 506 */       } else if (source.is(DamageTypeTags.IS_PROJECTILE)) {
/* 507 */         Entity directEntity = source.getDirectEntity();
/* 508 */         if (directEntity != null && directEntity.is(EntityTypes.SHULKER_BULLET))
/* 509 */           hitByShulkerBullet(); 
/*     */       } 
/* 513 */       return true;
/*     */     } 
/* 515 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isClosed() {
/* 519 */     return (getRawPeekAmount() == 0);
/*     */   }
/*     */   
/*     */   private void hitByShulkerBullet() {
/* 523 */     Vec3 oldPosition = position();
/* 524 */     AABB oldAabb = getBoundingBox();
/* 526 */     if (isClosed() || !teleportSomewhere())
/*     */       return; 
/* 530 */     int shulkerCount = level().getEntities((EntityTypeTest)EntityTypes.SHULKER, oldAabb.inflate(8.0D), Entity::isAlive).size();
/* 532 */     float failureChance = (shulkerCount - 1) / 5.0F;
/* 533 */     if (level().getRandom().nextFloat() < failureChance)
/*     */       return; 
/* 537 */     Shulker baby = (Shulker)EntityTypes.SHULKER.create(level(), EntitySpawnReason.BREEDING);
/* 539 */     if (baby != null) {
/* 540 */       baby.setVariant(getVariant());
/* 541 */       baby.snapTo(oldPosition);
/* 542 */       level().addFreshEntity((Entity)baby);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canBeCollidedWith(Entity other) {
/* 548 */     return isAlive();
/*     */   }
/*     */   
/*     */   public Direction getAttachFace() {
/* 552 */     return (Direction)this.entityData.get(DATA_ATTACH_FACE_ID);
/*     */   }
/*     */   
/*     */   private void setAttachFace(Direction attachmentDirection) {
/* 556 */     this.entityData.set(DATA_ATTACH_FACE_ID, attachmentDirection);
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 561 */     if (DATA_ATTACH_FACE_ID.equals(accessor))
/* 562 */       setBoundingBox(makeBoundingBox()); 
/* 564 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   private int getRawPeekAmount() {
/* 568 */     return ((Byte)this.entityData.get(DATA_PEEK_ID)).byteValue();
/*     */   }
/*     */   
/*     */   private void setRawPeekAmount(int amount) {
/* 572 */     if (!level().isClientSide()) {
/* 573 */       getAttribute(Attributes.ARMOR).removeModifier(COVERED_ARMOR_MODIFIER_ID);
/* 574 */       if (amount == 0) {
/* 575 */         getAttribute(Attributes.ARMOR).addPermanentModifier(COVERED_ARMOR_MODIFIER);
/* 576 */         playSound(SoundEvents.SHULKER_CLOSE, 1.0F, 1.0F);
/* 577 */         gameEvent((Holder)GameEvent.CONTAINER_CLOSE);
/*     */       } else {
/* 579 */         playSound(SoundEvents.SHULKER_OPEN, 1.0F, 1.0F);
/* 580 */         gameEvent((Holder)GameEvent.CONTAINER_OPEN);
/*     */       } 
/*     */     } 
/* 584 */     this.entityData.set(DATA_PEEK_ID, Byte.valueOf((byte)amount));
/*     */   }
/*     */   
/*     */   public float getClientPeekAmount(float a) {
/* 588 */     return Mth.lerp(a, this.currentPeekAmountO, this.currentPeekAmount);
/*     */   }
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 593 */     super.recreateFromPacket(packet);
/* 594 */     this.yBodyRot = 0.0F;
/* 595 */     this.yBodyRotO = 0.0F;
/*     */   }
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 600 */     return 180;
/*     */   }
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 605 */     return 180;
/*     */   }
/*     */   
/*     */   public void push(Entity entity) {}
/*     */   
/*     */   public Vec3 getRenderPosition(float a) {
/* 613 */     if (this.clientOldAttachPosition == null || this.clientSideTeleportInterpolation <= 0)
/* 614 */       return null; 
/* 617 */     double scale = (this.clientSideTeleportInterpolation - a) / 6.0D;
/* 618 */     scale *= scale;
/* 619 */     scale *= getScale();
/* 621 */     BlockPos currentPos = blockPosition();
/* 622 */     double ox = (currentPos.getX() - this.clientOldAttachPosition.getX()) * scale;
/* 623 */     double oy = (currentPos.getY() - this.clientOldAttachPosition.getY()) * scale;
/* 624 */     double oz = (currentPos.getZ() - this.clientOldAttachPosition.getZ()) * scale;
/* 626 */     return new Vec3(-ox, -oy, -oz);
/*     */   }
/*     */   
/*     */   protected float sanitizeScale(float scale) {
/* 631 */     return Math.min(scale, 3.0F);
/*     */   }
/*     */   
/*     */   private static class ShulkerBodyRotationControl extends BodyRotationControl {
/*     */     public ShulkerBodyRotationControl(Mob mob) {
/* 636 */       super(mob);
/*     */     }
/*     */     
/*     */     public void clientTick() {}
/*     */   }
/*     */   
/*     */   private class ShulkerPeekGoal extends Goal {
/*     */     private int peekTime;
/*     */     
/*     */     public boolean canUse() {
/* 650 */       return (Shulker.this.getTarget() == null && Shulker.this.random.nextInt(reducedTickDelay(40)) == 0 && Shulker.this.canStayAt(Shulker.this.blockPosition(), Shulker.this.getAttachFace()));
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 655 */       return (Shulker.this.getTarget() == null && this.peekTime > 0);
/*     */     }
/*     */     
/*     */     public void start() {
/* 660 */       this.peekTime = adjustedTickDelay(20 * (1 + Shulker.this.random.nextInt(3)));
/* 661 */       Shulker.this.setRawPeekAmount(30);
/*     */     }
/*     */     
/*     */     public void stop() {
/* 666 */       if (Shulker.this.getTarget() == null)
/* 667 */         Shulker.this.setRawPeekAmount(0); 
/*     */     }
/*     */     
/*     */     public void tick() {
/* 673 */       this.peekTime--;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ShulkerAttackGoal extends Goal {
/*     */     private int attackTime;
/*     */     
/*     */     public ShulkerAttackGoal() {
/* 681 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 686 */       LivingEntity target = Shulker.this.getTarget();
/* 687 */       if (target == null || !target.isAlive())
/* 688 */         return false; 
/* 690 */       if (Shulker.this.level().getDifficulty() == Difficulty.PEACEFUL)
/* 691 */         return false; 
/* 694 */       return true;
/*     */     }
/*     */     
/*     */     public void start() {
/* 699 */       this.attackTime = 20;
/* 700 */       Shulker.this.setRawPeekAmount(100);
/*     */     }
/*     */     
/*     */     public void stop() {
/* 705 */       Shulker.this.setRawPeekAmount(0);
/*     */     }
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 710 */       return true;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 715 */       if (Shulker.this.level().getDifficulty() == Difficulty.PEACEFUL)
/*     */         return; 
/* 718 */       this.attackTime--;
/* 720 */       LivingEntity target = Shulker.this.getTarget();
/* 721 */       if (target == null)
/*     */         return; 
/* 724 */       Shulker.this.getLookControl().setLookAt((Entity)target, 180.0F, 180.0F);
/* 726 */       double distance = Shulker.this.distanceToSqr((Entity)target);
/* 728 */       if (distance < 400.0D) {
/* 729 */         if (this.attackTime <= 0) {
/* 730 */           this.attackTime = 20 + Shulker.this.random.nextInt(10) * 20 / 2;
/* 732 */           Shulker.this.level().addFreshEntity((Entity)new ShulkerBullet(Shulker.this.level(), (LivingEntity)Shulker.this, (Entity)target, Shulker.this.getAttachFace().getAxis()));
/* 733 */           Shulker.this.playSound(SoundEvents.SHULKER_SHOOT, 2.0F, (Shulker.this.random.nextFloat() - Shulker.this.random.nextFloat()) * 0.2F + 1.0F);
/*     */         } 
/*     */       } else {
/* 736 */         Shulker.this.setTarget(null);
/*     */       } 
/* 739 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ShulkerNearestAttackGoal extends NearestAttackableTargetGoal<Player> {
/*     */     public ShulkerNearestAttackGoal(Shulker mob) {
/* 745 */       super((Mob)mob, Player.class, true);
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 750 */       if (Shulker.this.level().getDifficulty() == Difficulty.PEACEFUL)
/* 751 */         return false; 
/* 753 */       return super.canUse();
/*     */     }
/*     */     
/*     */     protected AABB getTargetSearchArea(double followDistance) {
/* 758 */       Direction attachFace = ((Shulker)this.mob).getAttachFace();
/* 759 */       if (attachFace.getAxis() == Direction.Axis.X)
/* 760 */         return this.mob.getBoundingBox().inflate(4.0D, followDistance, followDistance); 
/* 762 */       if (attachFace.getAxis() == Direction.Axis.Z)
/* 763 */         return this.mob.getBoundingBox().inflate(followDistance, followDistance, 4.0D); 
/* 765 */       return this.mob.getBoundingBox().inflate(followDistance, 4.0D, followDistance);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ShulkerDefenseAttackGoal extends NearestAttackableTargetGoal<LivingEntity> {
/*     */     public ShulkerDefenseAttackGoal(Shulker mob) {
/* 771 */       super((Mob)mob, LivingEntity.class, 10, true, false, (input, level) -> input instanceof Enemy);
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 776 */       if (this.mob.getTeam() == null)
/* 777 */         return false; 
/* 779 */       return super.canUse();
/*     */     }
/*     */     
/*     */     protected AABB getTargetSearchArea(double followDistance) {
/* 784 */       Direction attachFace = ((Shulker)this.mob).getAttachFace();
/* 785 */       if (attachFace.getAxis() == Direction.Axis.X)
/* 786 */         return this.mob.getBoundingBox().inflate(4.0D, followDistance, followDistance); 
/* 788 */       if (attachFace.getAxis() == Direction.Axis.Z)
/* 789 */         return this.mob.getBoundingBox().inflate(followDistance, followDistance, 4.0D); 
/* 791 */       return this.mob.getBoundingBox().inflate(followDistance, 4.0D, followDistance);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setVariant(Optional<DyeColor> color) {
/* 796 */     this.entityData.set(DATA_COLOR_ID, color.<Byte>map(dyeColor -> Byte.valueOf((byte)dyeColor.getId())).orElse(Byte.valueOf((byte)16)));
/*     */   }
/*     */   
/*     */   public Optional<DyeColor> getVariant() {
/* 800 */     return Optional.ofNullable(getColor());
/*     */   }
/*     */   
/*     */   public DyeColor getColor() {
/* 804 */     byte color = ((Byte)this.entityData.get(DATA_COLOR_ID)).byteValue();
/* 805 */     if (color == 16 || color > 15)
/* 806 */       return null; 
/* 808 */     return DyeColor.byId(color);
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 813 */     if (type == DataComponents.SHULKER_COLOR)
/* 814 */       return (T)castComponentValue(type, getColor()); 
/* 817 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 822 */     applyImplicitComponentIfPresent(components, DataComponents.SHULKER_COLOR);
/* 823 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 828 */     if (type == DataComponents.SHULKER_COLOR) {
/* 829 */       setVariant(Optional.of((DyeColor)castComponentValue(DataComponents.SHULKER_COLOR, value)));
/* 830 */       return true;
/*     */     } 
/* 833 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Shulker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */