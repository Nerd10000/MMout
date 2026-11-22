/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Ghast extends Mob implements Enemy {
/*  47 */   private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(Ghast.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final byte DEFAULT_EXPLOSION_POWER = 1;
/*     */   
/*  51 */   private int explosionPower = 1;
/*     */   
/*     */   public Ghast(EntityType<? extends Ghast> type, Level level) {
/*  54 */     super(type, level);
/*  56 */     this.xpReward = 5;
/*  58 */     this.moveControl = new GhastMoveControl<>(this, false, () -> false);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  63 */     this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
/*  65 */     this.goalSelector.addGoal(7, new GhastLookGoal(this));
/*  66 */     this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
/*  69 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal(this, Player.class, 10, true, false, (target, level) -> (Math.abs(target.getY() - getY()) <= 4.0D)));
/*     */   }
/*     */   
/*     */   public boolean isCharging() {
/*  73 */     return ((Boolean)this.entityData.get(DATA_IS_CHARGING)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setCharging(boolean onOff) {
/*  77 */     this.entityData.set(DATA_IS_CHARGING, Boolean.valueOf(onOff));
/*     */   }
/*     */   
/*     */   public int getExplosionPower() {
/*  81 */     return this.explosionPower;
/*     */   }
/*     */   
/*     */   private static boolean isReflectedFireball(DamageSource source) {
/*  85 */     return (source.getDirectEntity() instanceof LargeFireball && source.getEntity() instanceof Player);
/*     */   }
/*     */   
/*     */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
/*  90 */     return ((isInvulnerable() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) || (
/*  91 */       !isReflectedFireball(source) && super.isInvulnerableTo(level, source)));
/*     */   }
/*     */   
/*     */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*     */   
/*     */   public boolean onClimbable() {
/* 102 */     return false;
/*     */   }
/*     */   
/*     */   public void travel(Vec3 input) {
/* 107 */     travelFlying(input, 0.02F);
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 112 */     if (isReflectedFireball(source)) {
/* 114 */       super.hurtServer(level, source, 1000.0F);
/* 115 */       return true;
/*     */     } 
/* 118 */     if (isInvulnerableTo(level, source))
/* 119 */       return false; 
/* 122 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 127 */     super.defineSynchedData(entityData);
/* 129 */     entityData.define(DATA_IS_CHARGING, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 133 */     return Mob.createMobAttributes()
/* 134 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 135 */       .add(Attributes.FOLLOW_RANGE, 100.0D)
/* 136 */       .add(Attributes.CAMERA_DISTANCE, 8.0D)
/* 137 */       .add(Attributes.FLYING_SPEED, 0.06D);
/*     */   }
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 142 */     return SoundSource.HOSTILE;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 147 */     return SoundEvents.GHAST_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 152 */     return SoundEvents.GHAST_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 157 */     return SoundEvents.GHAST_DEATH;
/*     */   }
/*     */   
/*     */   protected float getSoundVolume() {
/* 162 */     return 5.0F;
/*     */   }
/*     */   
/*     */   public static boolean checkGhastSpawnRules(EntityType<Ghast> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 166 */     return (level.getDifficulty() != Difficulty.PEACEFUL && random
/* 167 */       .nextInt(20) == 0 && 
/* 168 */       checkMobSpawnRules(type, level, spawnReason, pos, random));
/*     */   }
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/* 173 */     return 1;
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 178 */     super.addAdditionalSaveData(output);
/* 179 */     output.putByte("ExplosionPower", (byte)this.explosionPower);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 184 */     super.readAdditionalSaveData(input);
/* 185 */     this.explosionPower = input.getByteOr("ExplosionPower", (byte)1);
/*     */   }
/*     */   
/*     */   public boolean supportQuadLeashAsHolder() {
/* 190 */     return true;
/*     */   }
/*     */   
/*     */   public double leashElasticDistance() {
/* 195 */     return 10.0D;
/*     */   }
/*     */   
/*     */   public double leashSnapDistance() {
/* 200 */     return 16.0D;
/*     */   }
/*     */   
/*     */   public static class GhastMoveControl<T extends Mob> extends MoveControl<T> {
/*     */     private int floatDuration;
/*     */     
/*     */     private final boolean careful;
/*     */     
/*     */     private final BooleanSupplier shouldBeStopped;
/*     */     
/*     */     public GhastMoveControl(T ghast, boolean careful, BooleanSupplier shouldBeStopped) {
/* 209 */       super((Mob)ghast);
/* 210 */       this.careful = careful;
/* 211 */       this.shouldBeStopped = shouldBeStopped;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 216 */       if (this.shouldBeStopped.getAsBoolean()) {
/* 217 */         this.operation = MoveControl.Operation.WAIT;
/* 218 */         this.mob.stopInPlace();
/*     */       } 
/* 221 */       if (this.operation != MoveControl.Operation.MOVE_TO)
/*     */         return; 
/* 226 */       if (this.floatDuration-- <= 0) {
/* 227 */         this.floatDuration += this.mob.getRandom().nextInt(5) + 2;
/* 232 */         Vec3 travel = new Vec3(this.wantedX - this.mob.getX(), this.wantedY - this.mob.getY(), this.wantedZ - this.mob.getZ());
/* 235 */         if (canReach(travel)) {
/* 236 */           this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(travel.normalize().scale(this.mob.getAttributeValue(Attributes.FLYING_SPEED) * 5.0D / 3.0D)));
/*     */         } else {
/* 238 */           this.operation = MoveControl.Operation.WAIT;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean canReach(Vec3 travel) {
/* 244 */       AABB aabb = this.mob.getBoundingBox();
/* 245 */       AABB aabbAtDestination = aabb.move(travel);
/* 246 */       if (this.careful)
/* 247 */         for (BlockPos pos : BlockPos.betweenClosed(aabbAtDestination.inflate(1.0D))) {
/* 248 */           if (!blockTraversalPossible((BlockGetter)this.mob.level(), (Vec3)null, (Vec3)null, pos, false, false))
/* 249 */             return false; 
/*     */         }  
/* 253 */       boolean isInWater = this.mob.isInWater();
/* 254 */       boolean isInLava = this.mob.isInLava();
/* 255 */       Vec3 start = this.mob.position();
/* 256 */       Vec3 end = start.add(travel);
/* 258 */       return BlockGetter.forEachBlockIntersectedBetween(start, end, aabbAtDestination, (blockPos, i) -> aabb.intersects(blockPos) ? true : blockTraversalPossible((BlockGetter)this.mob.level(), start, end, blockPos, isInWater, isInLava));
/*     */     }
/*     */     
/*     */     private boolean blockTraversalPossible(BlockGetter level, Vec3 start, Vec3 end, BlockPos pos, boolean canPathThroughWater, boolean canPathThroughLava) {
/* 267 */       BlockState state = level.getBlockState(pos);
/* 268 */       if (state.isAir())
/* 269 */         return true; 
/* 271 */       boolean preciseBlockCollisions = (start != null && end != null);
/* 272 */       boolean pathNoCollisions = preciseBlockCollisions ? (!this.mob.collidedWithShapeMovingFrom(start, end, state.getCollisionShape(level, pos).move(new Vec3((Vec3i)pos)).toAabbs())) : state.getCollisionShape(level, pos).isEmpty();
/* 273 */       if (!this.careful)
/* 274 */         return pathNoCollisions; 
/* 276 */       if (state.is(BlockTags.HAPPY_GHAST_AVOIDS))
/* 277 */         return false; 
/* 279 */       FluidState fluidState = level.getFluidState(pos);
/* 280 */       if (!fluidState.isEmpty() && (!preciseBlockCollisions || this.mob.collidedWithFluid(fluidState, pos, start, end))) {
/* 281 */         if (fluidState.is(FluidTags.WATER))
/* 282 */           return canPathThroughWater; 
/* 284 */         if (fluidState.is(FluidTags.LAVA))
/* 285 */           return canPathThroughLava; 
/*     */       } 
/* 288 */       return pathNoCollisions;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RandomFloatAroundGoal extends Goal {
/*     */     private static final int MAX_ATTEMPTS = 64;
/*     */     
/*     */     private final Mob ghast;
/*     */     
/*     */     private final int distanceToBlocks;
/*     */     
/*     */     public RandomFloatAroundGoal(Mob ghast) {
/* 298 */       this(ghast, 0);
/*     */     }
/*     */     
/*     */     public RandomFloatAroundGoal(Mob ghast, int distanceToBlocks) {
/* 302 */       this.ghast = ghast;
/* 303 */       this.distanceToBlocks = distanceToBlocks;
/* 304 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 309 */       MoveControl moveControl = this.ghast.getMoveControl();
/* 310 */       if (!moveControl.hasWanted())
/* 311 */         return true; 
/* 314 */       double xd = moveControl.getWantedX() - this.ghast.getX();
/* 315 */       double yd = moveControl.getWantedY() - this.ghast.getY();
/* 316 */       double zd = moveControl.getWantedZ() - this.ghast.getZ();
/* 318 */       double dd = xd * xd + yd * yd + zd * zd;
/* 320 */       return (dd < 1.0D || dd > 3600.0D);
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 325 */       return false;
/*     */     }
/*     */     
/*     */     public void start() {
/* 330 */       Vec3 result = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
/* 331 */       this.ghast.getMoveControl().setWantedPosition(result.x(), result.y(), result.z(), 1.0D);
/*     */     }
/*     */     
/*     */     public static Vec3 getSuitableFlyToPosition(Mob mob, int distanceToBlocks) {
/* 335 */       Level level = mob.level();
/* 336 */       RandomSource random = mob.getRandom();
/* 337 */       Vec3 center = mob.position();
/* 338 */       Vec3 result = null;
/* 339 */       for (int i = 0; i < 64; i++) {
/* 340 */         result = chooseRandomPositionWithRestriction(mob, center, random);
/* 341 */         if (result != null && isGoodTarget(level, result, distanceToBlocks))
/* 342 */           return result; 
/*     */       } 
/* 345 */       if (result == null)
/* 346 */         result = chooseRandomPosition(center, random); 
/* 350 */       BlockPos pos = BlockPos.containing((Position)result);
/* 351 */       int heightY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, pos.getX(), pos.getZ());
/* 352 */       if (heightY < pos.getY() && heightY > level.getMinY())
/* 353 */         result = new Vec3(result.x(), mob.getY() - Math.abs(mob.getY() - result.y()), result.z()); 
/* 355 */       return result;
/*     */     }
/*     */     
/*     */     private static boolean isGoodTarget(Level level, Vec3 target, int distanceToBlocks) {
/* 359 */       if (distanceToBlocks <= 0)
/* 360 */         return true; 
/* 362 */       BlockPos pos = BlockPos.containing((Position)target);
/* 363 */       if (!level.getBlockState(pos).isAir())
/* 364 */         return false; 
/* 367 */       for (Direction dir : Direction.values()) {
/* 368 */         for (int i = 1; i < distanceToBlocks; i++) {
/* 369 */           BlockPos offset = pos.relative(dir, i);
/* 370 */           if (!level.getBlockState(offset).isAir())
/* 371 */             return true; 
/*     */         } 
/*     */       } 
/* 375 */       return false;
/*     */     }
/*     */     
/*     */     private static Vec3 chooseRandomPosition(Vec3 center, RandomSource random) {
/* 379 */       double xTarget = center.x() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 380 */       double yTarget = center.y() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 381 */       double zTarget = center.z() + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 382 */       return new Vec3(xTarget, yTarget, zTarget);
/*     */     }
/*     */     
/*     */     private static Vec3 chooseRandomPositionWithRestriction(Mob mob, Vec3 center, RandomSource random) {
/* 386 */       Vec3 target = chooseRandomPosition(center, random);
/* 387 */       if (mob.hasHome() && !mob.isWithinHome(target))
/* 388 */         return null; 
/* 390 */       return target;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GhastLookGoal extends Goal {
/*     */     private final Mob ghast;
/*     */     
/*     */     public GhastLookGoal(Mob ghast) {
/* 398 */       this.ghast = ghast;
/* 400 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 405 */       return true;
/*     */     }
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 410 */       return true;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 415 */       Ghast.faceMovementDirection(this.ghast);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void faceMovementDirection(Mob ghast) {
/* 420 */     if (ghast.getTarget() == null) {
/* 421 */       Vec3 movement = ghast.getDeltaMovement();
/* 422 */       ghast.setYRot(-((float)Mth.atan2(movement.x, movement.z)) * 57.295776F);
/* 423 */       ghast.yBodyRot = ghast.getYRot();
/*     */     } else {
/* 425 */       LivingEntity target = ghast.getTarget();
/* 427 */       double maxDist = 64.0D;
/* 428 */       if (target.distanceToSqr((Entity)ghast) < 4096.0D) {
/* 429 */         double xdd = target.getX() - ghast.getX();
/* 430 */         double zdd = target.getZ() - ghast.getZ();
/* 431 */         ghast.setYRot(-((float)Mth.atan2(xdd, zdd)) * 57.295776F);
/* 432 */         ghast.yBodyRot = ghast.getYRot();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class GhastShootFireballGoal extends Goal {
/*     */     private final Ghast ghast;
/*     */     
/*     */     public int chargeTime;
/*     */     
/*     */     public GhastShootFireballGoal(Ghast ghast) {
/* 442 */       this.ghast = ghast;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 447 */       return (this.ghast.getTarget() != null);
/*     */     }
/*     */     
/*     */     public void start() {
/* 452 */       this.chargeTime = 0;
/*     */     }
/*     */     
/*     */     public void stop() {
/* 457 */       this.ghast.setCharging(false);
/*     */     }
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 462 */       return true;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 467 */       LivingEntity target = this.ghast.getTarget();
/* 468 */       if (target == null)
/*     */         return; 
/* 472 */       double maxDist = 64.0D;
/* 473 */       if (target.distanceToSqr((Entity)this.ghast) < 4096.0D && this.ghast.hasLineOfSight((Entity)target)) {
/* 474 */         Level level = this.ghast.level();
/* 476 */         this.chargeTime++;
/* 477 */         if (this.chargeTime == 10 && !this.ghast.isSilent())
/* 478 */           level.levelEvent(null, 1015, this.ghast.blockPosition(), 0); 
/* 480 */         if (this.chargeTime == 20) {
/* 481 */           double d = 4.0D;
/* 482 */           Vec3 viewVector = this.ghast.getViewVector(1.0F);
/* 484 */           double xdd = target.getX() - this.ghast.getX() + viewVector.x * 4.0D;
/* 485 */           double ydd = target.getY(0.5D) - 0.5D + this.ghast.getY(0.5D);
/* 486 */           double zdd = target.getZ() - this.ghast.getZ() + viewVector.z * 4.0D;
/* 487 */           Vec3 direction = new Vec3(xdd, ydd, zdd);
/* 489 */           if (!this.ghast.isSilent())
/* 490 */             level.levelEvent(null, 1016, this.ghast.blockPosition(), 0); 
/* 492 */           LargeFireball entity = new LargeFireball(level, (LivingEntity)this.ghast, direction.normalize(), this.ghast.getExplosionPower());
/* 493 */           entity.setPos(this.ghast.getX() + viewVector.x * 4.0D, this.ghast.getY(0.5D) + 0.5D, entity.getZ() + viewVector.z * 4.0D);
/* 494 */           level.addFreshEntity((Entity)entity);
/* 495 */           this.chargeTime = -40;
/*     */         } 
/* 497 */       } else if (this.chargeTime > 0) {
/* 498 */         this.chargeTime--;
/*     */       } 
/* 500 */       this.ghast.setCharging((this.chargeTime > 10));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\Ghast.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */