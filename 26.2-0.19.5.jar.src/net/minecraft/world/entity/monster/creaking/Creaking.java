/*     */ package net.minecraft.world.entity.monster.creaking;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.BodyRotationControl;
/*     */ import net.minecraft.world.entity.ai.control.JumpControl;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.CreakingHeartBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.CreakingHeartBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.CreakingHeartState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.PathfindingContext;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Creaking extends Monster {
/*  60 */   private static final EntityDataAccessor<Boolean> CAN_MOVE = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  61 */   private static final EntityDataAccessor<Boolean> IS_ACTIVE = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  63 */   private static final EntityDataAccessor<Boolean> IS_TEARING_DOWN = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  65 */   private static final EntityDataAccessor<Optional<BlockPos>> HOME_POS = SynchedEntityData.defineId(Creaking.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);
/*     */   
/*  67 */   private static final Brain.Provider<Creaking> BRAIN_PROVIDER = Brain.provider(
/*  68 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS), CreakingAi::getActivities);
/*     */   
/*     */   private static final int ATTACK_ANIMATION_DURATION = 15;
/*     */   
/*     */   private static final int MAX_HEALTH = 1;
/*     */   
/*     */   private static final float ATTACK_DAMAGE = 3.0F;
/*     */   
/*     */   private static final float FOLLOW_RANGE = 32.0F;
/*     */   
/*     */   private static final float ACTIVATION_RANGE_SQ = 144.0F;
/*     */   
/*     */   public static final int ATTACK_INTERVAL = 40;
/*     */   
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.4F;
/*     */   
/*     */   public static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.3F;
/*     */   
/*     */   public static final int CREAKING_ORANGE = 16545810;
/*     */   
/*     */   public static final int CREAKING_GRAY = 6250335;
/*     */   
/*     */   public static final int INVULNERABILITY_ANIMATION_DURATION = 8;
/*     */   
/*     */   public static final int TWITCH_DEATH_DURATION = 45;
/*     */   
/*     */   private static final int MAX_PLAYER_STUCK_COUNTER = 4;
/*     */   
/*     */   private int attackAnimationRemainingTicks;
/*     */   
/*  97 */   public final AnimationState attackAnimationState = new AnimationState();
/*     */   
/*  98 */   public final AnimationState invulnerabilityAnimationState = new AnimationState();
/*     */   
/* 101 */   public final AnimationState deathAnimationState = new AnimationState();
/*     */   
/*     */   private int invulnerabilityAnimationRemainingTicks;
/*     */   
/*     */   private boolean eyesGlowing;
/*     */   
/*     */   private int nextFlickerTime;
/*     */   
/*     */   private int playerStuckCounter;
/*     */   
/*     */   public Creaking(EntityType<? extends Creaking> type, Level level) {
/* 110 */     super(type, level);
/* 112 */     this.lookControl = new CreakingLookControl(this);
/* 113 */     this.moveControl = new CreakingMoveControl<>(this);
/* 114 */     this.jumpControl = new CreakingJumpControl(this);
/* 116 */     GroundPathNavigation navigation = (GroundPathNavigation)getNavigation();
/* 117 */     navigation.setCanFloat(true);
/* 119 */     this.xpReward = 0;
/*     */   }
/*     */   
/*     */   public void setTransient(BlockPos pos) {
/* 123 */     setHomePos(pos);
/* 124 */     setPathfindingMalus(PathType.DAMAGING, 8.0F);
/* 125 */     setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
/* 126 */     setPathfindingMalus(PathType.LAVA, 8.0F);
/* 127 */     setPathfindingMalus(PathType.FIRE, 0.0F);
/* 128 */     setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, 0.0F);
/*     */   }
/*     */   
/*     */   public boolean isHeartBound() {
/* 132 */     return (getHomePos() != null);
/*     */   }
/*     */   
/*     */   protected BodyRotationControl createBodyControl() {
/* 137 */     return new CreakingBodyRotationControl(this);
/*     */   }
/*     */   
/*     */   protected Brain<Creaking> makeBrain(Brain.Packed packedBrain) {
/* 142 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 147 */     super.defineSynchedData(entityData);
/* 148 */     entityData.define(CAN_MOVE, Boolean.valueOf(true));
/* 149 */     entityData.define(IS_ACTIVE, Boolean.valueOf(false));
/* 150 */     entityData.define(IS_TEARING_DOWN, Boolean.valueOf(false));
/* 151 */     entityData.define(HOME_POS, Optional.empty());
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 155 */     return Monster.createMonsterAttributes()
/* 156 */       .add(Attributes.MAX_HEALTH, 1.0D)
/* 157 */       .add(Attributes.MOVEMENT_SPEED, 0.4000000059604645D)
/* 158 */       .add(Attributes.ATTACK_DAMAGE, 3.0D)
/* 159 */       .add(Attributes.FOLLOW_RANGE, 32.0D)
/* 160 */       .add(Attributes.STEP_HEIGHT, 1.0625D);
/*     */   }
/*     */   
/*     */   public boolean canMove() {
/* 164 */     return ((Boolean)this.entityData.get(CAN_MOVE)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 169 */     if (!(target instanceof LivingEntity))
/* 170 */       return false; 
/* 172 */     this.attackAnimationRemainingTicks = 15;
/* 173 */     level().broadcastEntityEvent((Entity)this, (byte)4);
/* 175 */     return super.doHurtTarget(level, target);
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 180 */     BlockPos homePos = getHomePos();
/* 181 */     if (homePos == null || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
/* 182 */       return super.hurtServer(level, source, damage); 
/* 185 */     if (isInvulnerableTo(level, source) || this.invulnerabilityAnimationRemainingTicks > 0 || isDeadOrDying())
/* 186 */       return false; 
/* 189 */     Player responsiblePlayer = blameSourceForDamage(source);
/* 191 */     Entity directEntity = source.getDirectEntity();
/* 192 */     if (!(directEntity instanceof LivingEntity) && !(directEntity instanceof net.minecraft.world.entity.projectile.Projectile) && responsiblePlayer == null)
/* 193 */       return false; 
/* 196 */     this.invulnerabilityAnimationRemainingTicks = 8;
/* 197 */     level().broadcastEntityEvent((Entity)this, (byte)66);
/* 198 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 200 */     BlockEntity blockEntity = level().getBlockEntity(homePos);
/* 200 */     if (blockEntity instanceof CreakingHeartBlockEntity) {
/* 200 */       CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity;
/* 201 */       if (creakingHeartBlockEntity.isProtector(this)) {
/* 202 */         if (responsiblePlayer != null)
/* 203 */           creakingHeartBlockEntity.creakingHurt(); 
/* 205 */         playHurtSound(source);
/*     */       } 
/*     */     } 
/* 208 */     return true;
/*     */   }
/*     */   
/*     */   public Player blameSourceForDamage(DamageSource source) {
/* 212 */     resolveMobResponsibleForDamage(source);
/* 213 */     return resolvePlayerResponsibleForDamage(source);
/*     */   }
/*     */   
/*     */   public boolean isPushable() {
/* 218 */     return (super.isPushable() && canMove());
/*     */   }
/*     */   
/*     */   public void push(double xa, double ya, double za) {
/* 223 */     if (!canMove())
/*     */       return; 
/* 226 */     super.push(xa, ya, za);
/*     */   }
/*     */   
/*     */   public Brain<Creaking> getBrain() {
/* 232 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 237 */     ProfilerFiller profiler = Profiler.get();
/* 238 */     profiler.push("creakingBrain");
/* 239 */     getBrain().tick((ServerLevel)level(), (LivingEntity)this);
/* 240 */     profiler.pop();
/* 241 */     CreakingAi.updateActivity(this);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 247 */     if (this.invulnerabilityAnimationRemainingTicks > 0)
/* 248 */       this.invulnerabilityAnimationRemainingTicks--; 
/* 250 */     if (this.attackAnimationRemainingTicks > 0)
/* 251 */       this.attackAnimationRemainingTicks--; 
/* 254 */     if (!level().isClientSide()) {
/* 255 */       boolean canMove = ((Boolean)this.entityData.get(CAN_MOVE)).booleanValue();
/* 256 */       boolean nowCanMove = checkCanMove();
/* 257 */       if (nowCanMove != canMove) {
/* 259 */         gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 260 */         if (nowCanMove) {
/* 261 */           makeSound(SoundEvents.CREAKING_UNFREEZE);
/*     */         } else {
/* 263 */           stopInPlace();
/* 264 */           makeSound(SoundEvents.CREAKING_FREEZE);
/*     */         } 
/*     */       } 
/* 267 */       this.entityData.set(CAN_MOVE, Boolean.valueOf(nowCanMove));
/*     */     } 
/* 269 */     super.aiStep();
/*     */   }
/*     */   
/*     */   public void tick() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   4: invokevirtual isClientSide : ()Z
/*     */     //   7: ifne -> 66
/*     */     //   10: aload_0
/*     */     //   11: invokevirtual getHomePos : ()Lnet/minecraft/core/BlockPos;
/*     */     //   14: astore_1
/*     */     //   15: aload_1
/*     */     //   16: ifnull -> 66
/*     */     //   19: aload_0
/*     */     //   20: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   23: aload_1
/*     */     //   24: invokevirtual getBlockEntity : (Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/entity/BlockEntity;
/*     */     //   27: astore #4
/*     */     //   29: aload #4
/*     */     //   31: instanceof net/minecraft/world/level/block/entity/CreakingHeartBlockEntity
/*     */     //   34: ifeq -> 55
/*     */     //   37: aload #4
/*     */     //   39: checkcast net/minecraft/world/level/block/entity/CreakingHeartBlockEntity
/*     */     //   42: astore_3
/*     */     //   43: aload_3
/*     */     //   44: aload_0
/*     */     //   45: invokevirtual isProtector : (Lnet/minecraft/world/entity/monster/creaking/Creaking;)Z
/*     */     //   48: ifeq -> 55
/*     */     //   51: iconst_1
/*     */     //   52: goto -> 56
/*     */     //   55: iconst_0
/*     */     //   56: istore_2
/*     */     //   57: iload_2
/*     */     //   58: ifne -> 66
/*     */     //   61: aload_0
/*     */     //   62: fconst_0
/*     */     //   63: invokevirtual setHealth : (F)V
/*     */     //   66: aload_0
/*     */     //   67: invokespecial tick : ()V
/*     */     //   70: aload_0
/*     */     //   71: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   74: invokevirtual isClientSide : ()Z
/*     */     //   77: ifeq -> 88
/*     */     //   80: aload_0
/*     */     //   81: invokevirtual setupAnimationStates : ()V
/*     */     //   84: aload_0
/*     */     //   85: invokevirtual checkEyeBlink : ()V
/*     */     //   88: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #274	-> 0
/*     */     //   #275	-> 10
/*     */     //   #276	-> 15
/*     */     //   #277	-> 19
/*     */     //   #278	-> 57
/*     */     //   #279	-> 61
/*     */     //   #284	-> 66
/*     */     //   #285	-> 70
/*     */     //   #286	-> 80
/*     */     //   #287	-> 84
/*     */     //   #289	-> 88
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   43	12	3	creakingHeartBlockEntity	Lnet/minecraft/world/level/block/entity/CreakingHeartBlockEntity;
/*     */     //   57	9	2	hasProtectionFromCreakingHeart	Z
/*     */     //   15	51	1	homePos	Lnet/minecraft/core/BlockPos;
/*     */     //   0	89	0	this	Lnet/minecraft/world/entity/monster/creaking/Creaking;
/*     */   }
/*     */   
/*     */   protected void tickDeath() {
/* 293 */     if (isHeartBound() && isTearingDown()) {
/* 294 */       this.deathTime++;
/* 295 */       if (!level().isClientSide() && this.deathTime > 45 && !isRemoved())
/* 296 */         tearDown(); 
/*     */     } else {
/* 299 */       super.tickDeath();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void updateWalkAnimation(float distance) {
/* 306 */     float targetSpeed = Math.min(distance * 25.0F, 3.0F);
/* 307 */     this.walkAnimation.update(targetSpeed, 0.4F, 1.0F);
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 312 */     this.attackAnimationState.animateWhen((this.attackAnimationRemainingTicks > 0), this.tickCount);
/* 313 */     this.invulnerabilityAnimationState.animateWhen((this.invulnerabilityAnimationRemainingTicks > 0), this.tickCount);
/* 314 */     this.deathAnimationState.animateWhen(isTearingDown(), this.tickCount);
/*     */   }
/*     */   
/*     */   public void tearDown() {
/* 318 */     Level level = level();
/* 318 */     if (level instanceof ServerLevel) {
/* 318 */       ServerLevel serverLevel = (ServerLevel)level;
/* 319 */       AABB box = getBoundingBox();
/* 321 */       Vec3 center = box.getCenter();
/* 322 */       double xSpread = box.getXsize() * 0.3D;
/* 323 */       double ySpread = box.getYsize() * 0.3D;
/* 324 */       double zSpread = box.getZsize() * 0.3D;
/* 325 */       serverLevel.sendParticles((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK_CRUMBLE, Blocks.PALE_OAK_WOOD.defaultBlockState()), center.x, center.y, center.z, 100, xSpread, ySpread, zSpread, 0.0D);
/* 326 */       serverLevel.sendParticles((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK_CRUMBLE, (BlockState)Blocks.CREAKING_HEART.defaultBlockState().setValue((Property)CreakingHeartBlock.STATE, (Comparable)CreakingHeartState.AWAKE)), center.x, center.y, center.z, 10, xSpread, ySpread, zSpread, 0.0D);
/*     */     } 
/* 328 */     makeSound(getDeathSound());
/* 329 */     remove(Entity.RemovalReason.DISCARDED);
/*     */   }
/*     */   
/*     */   public void creakingDeathEffects(DamageSource source) {
/* 333 */     blameSourceForDamage(source);
/* 334 */     die(source);
/* 335 */     makeSound(SoundEvents.CREAKING_TWITCH);
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 341 */     if (id == 66) {
/* 342 */       this.invulnerabilityAnimationRemainingTicks = 8;
/* 343 */       playHurtSound(damageSources().generic());
/* 344 */     } else if (id == 4) {
/* 345 */       this.attackAnimationRemainingTicks = 15;
/* 346 */       playAttackSound();
/*     */     } else {
/* 348 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean fireImmune() {
/* 354 */     return (isHeartBound() || super.fireImmune());
/*     */   }
/*     */   
/*     */   public boolean canUsePortal(boolean ignorePassenger) {
/* 359 */     return (!isHeartBound() && super.canUsePortal(ignorePassenger));
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 364 */     return (PathNavigation)new CreakingPathNavigation(this, level);
/*     */   }
/*     */   
/*     */   public boolean playerIsStuckInYou() {
/* 368 */     List<Player> players = this.brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(List.of());
/* 369 */     if (players.isEmpty()) {
/* 370 */       this.playerStuckCounter = 0;
/* 371 */       return false;
/*     */     } 
/* 373 */     AABB ownBox = getBoundingBox();
/* 374 */     for (Player player : players) {
/* 375 */       if (ownBox.contains(player.getEyePosition())) {
/* 376 */         this.playerStuckCounter++;
/* 377 */         return (this.playerStuckCounter > 4);
/*     */       } 
/*     */     } 
/* 380 */     this.playerStuckCounter = 0;
/* 381 */     return false;
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 386 */     super.readAdditionalSaveData(input);
/* 387 */     input.read("home_pos", BlockPos.CODEC).ifPresent(this::setTransient);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 392 */     super.addAdditionalSaveData(output);
/* 393 */     output.storeNullable("home_pos", BlockPos.CODEC, getHomePos());
/*     */   }
/*     */   
/*     */   public void setHomePos(BlockPos pos) {
/* 397 */     this.entityData.set(HOME_POS, Optional.of(pos));
/*     */   }
/*     */   
/*     */   public BlockPos getHomePos() {
/* 401 */     return ((Optional<BlockPos>)this.entityData.get(HOME_POS)).orElse(null);
/*     */   }
/*     */   
/*     */   public void setTearingDown() {
/* 405 */     this.entityData.set(IS_TEARING_DOWN, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   public boolean isTearingDown() {
/* 409 */     return ((Boolean)this.entityData.get(IS_TEARING_DOWN)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean hasGlowingEyes() {
/* 413 */     return this.eyesGlowing;
/*     */   }
/*     */   
/*     */   public void checkEyeBlink() {
/* 417 */     if (this.deathTime > this.nextFlickerTime) {
/* 418 */       this.nextFlickerTime = this.deathTime + getRandom().nextIntBetweenInclusive(this.eyesGlowing ? 2 : (this.deathTime / 4), this.eyesGlowing ? 8 : (this.deathTime / 2));
/* 419 */       this.eyesGlowing = !this.eyesGlowing;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void playAttackSound() {
/* 425 */     makeSound(SoundEvents.CREAKING_ATTACK);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 430 */     if (isActive())
/* 431 */       return null; 
/* 433 */     return SoundEvents.CREAKING_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 438 */     return isHeartBound() ? SoundEvents.CREAKING_SWAY : super.getHurtSound(source);
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 443 */     return SoundEvents.CREAKING_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 448 */     playSound(SoundEvents.CREAKING_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public LivingEntity getTarget() {
/* 453 */     return getTargetFromBrain();
/*     */   }
/*     */   
/*     */   public void knockback(double power, double xd, double zd, DamageSource source, float damage) {
/* 458 */     if (!canMove())
/*     */       return; 
/* 461 */     super.knockback(power, xd, zd, source, damage);
/*     */   }
/*     */   
/*     */   public boolean checkCanMove() {
/* 465 */     List<Player> players = this.brain.getMemory(MemoryModuleType.NEAREST_PLAYERS).orElse(List.of());
/* 467 */     boolean active = isActive();
/* 468 */     if (players.isEmpty()) {
/* 469 */       if (active)
/* 470 */         deactivate(); 
/* 472 */       return true;
/*     */     } 
/* 474 */     boolean hasPotentialTarget = false;
/* 475 */     for (Player player : players) {
/* 476 */       if (!canAttack((LivingEntity)player) || isAlliedTo((Entity)player))
/*     */         continue; 
/* 479 */       hasPotentialTarget = true;
/* 480 */       if (active && !LivingEntity.PLAYER_NOT_WEARING_DISGUISE_ITEM.test(player))
/*     */         continue; 
/* 484 */       if (isLookingAtMe((LivingEntity)player, 0.5D, false, true, new double[] { getEyeY(), getY() + 0.5D * getScale(), (getEyeY() + getY()) / 2.0D })) {
/* 485 */         if (active)
/* 486 */           return false; 
/* 488 */         if (player.distanceToSqr((Entity)this) < 144.0D) {
/* 489 */           activate(player);
/* 490 */           return false;
/*     */         } 
/*     */       } 
/*     */     } 
/* 494 */     if (!hasPotentialTarget && active)
/* 495 */       deactivate(); 
/* 497 */     return true;
/*     */   }
/*     */   
/*     */   public void activate(Player player) {
/* 501 */     getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, player);
/* 502 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 503 */     makeSound(SoundEvents.CREAKING_ACTIVATE);
/* 504 */     setIsActive(true);
/*     */   }
/*     */   
/*     */   public void deactivate() {
/* 508 */     getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 509 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 510 */     makeSound(SoundEvents.CREAKING_DEACTIVATE);
/* 511 */     setIsActive(false);
/*     */   }
/*     */   
/*     */   public void setIsActive(boolean active) {
/* 515 */     this.entityData.set(IS_ACTIVE, Boolean.valueOf(active));
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 519 */     return ((Boolean)this.entityData.get(IS_ACTIVE)).booleanValue();
/*     */   }
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 524 */     return 0.0F;
/*     */   }
/*     */   
/*     */   private class CreakingLookControl extends LookControl {
/*     */     public CreakingLookControl(Creaking creaking) {
/* 529 */       super((Mob)creaking);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 534 */       if (Creaking.this.canMove())
/* 535 */         super.tick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CreakingMoveControl<T extends Creaking> extends MoveControl<T> {
/*     */     public CreakingMoveControl(T creaking) {
/* 542 */       super((Mob)creaking);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 547 */       if (((Creaking)this.mob).canMove())
/* 548 */         super.tick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CreakingJumpControl extends JumpControl {
/*     */     public CreakingJumpControl(Creaking creaking) {
/* 555 */       super((Mob)creaking);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 560 */       if (Creaking.this.canMove()) {
/* 561 */         super.tick();
/*     */       } else {
/* 563 */         Creaking.this.setJumping(false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private class CreakingBodyRotationControl extends BodyRotationControl {
/*     */     public CreakingBodyRotationControl(Creaking creaking) {
/* 570 */       super((Mob)creaking);
/*     */     }
/*     */     
/*     */     public void clientTick() {
/* 575 */       if (Creaking.this.canMove())
/* 576 */         super.clientTick(); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class HomeNodeEvaluator extends WalkNodeEvaluator {
/*     */     private static final int MAX_DISTANCE_TO_HOME_SQ = 1024;
/*     */     
/*     */     public PathType getPathType(PathfindingContext context, int x, int y, int z) {
/* 587 */       BlockPos homePos = Creaking.this.getHomePos();
/* 589 */       if (homePos == null)
/* 590 */         return super.getPathType(context, x, y, z); 
/* 593 */       double homeDistance = homePos.distSqr(new Vec3i(x, y, z));
/* 594 */       if (homeDistance > 1024.0D && homeDistance >= homePos.distSqr((Vec3i)context.mobPosition()))
/* 595 */         return PathType.BLOCKED; 
/* 597 */       return super.getPathType(context, x, y, z);
/*     */     }
/*     */   }
/*     */   
/*     */   private class CreakingPathNavigation extends GroundPathNavigation {
/*     */     public CreakingPathNavigation(Creaking mob, Level level) {
/* 603 */       super((Mob)mob, level);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 608 */       if (Creaking.this.canMove())
/* 609 */         super.tick(); 
/*     */     }
/*     */     
/*     */     protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 615 */       this.nodeEvaluator = (NodeEvaluator)new Creaking.HomeNodeEvaluator();
/* 616 */       this.nodeEvaluator.setCanPassDoors(true);
/* 617 */       return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\creaking\Creaking.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */