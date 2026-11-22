/*     */ package net.minecraft.world.entity.monster.warden;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientGamePacketListener;
/*     */ import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.GameEventTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffectUtil;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.DynamicGameEventListener;
/*     */ import net.minecraft.world.level.gameevent.EntityPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ 
/*     */ public class Warden extends Monster implements VibrationSystem {
/*     */   private static final int VIBRATION_COOLDOWN_TICKS = 40;
/*     */   
/*     */   private static final int TIME_TO_USE_MELEE_UNTIL_SONIC_BOOM = 200;
/*     */   
/*     */   private static final int MAX_HEALTH = 500;
/*     */   
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.3F;
/*     */   
/*     */   private static final float KNOCKBACK_RESISTANCE = 1.0F;
/*     */   
/*     */   private static final float ATTACK_KNOCKBACK = 1.5F;
/*     */   
/*     */   private static final int ATTACK_DAMAGE = 30;
/*     */   
/*     */   private static final int FOLLOW_RANGE = 24;
/*     */   
/*  92 */   private static final EntityDataAccessor<Integer> CLIENT_ANGER_LEVEL = SynchedEntityData.defineId(Warden.class, EntityDataSerializers.INT);
/*     */   
/*     */   private static final int DARKNESS_DISPLAY_LIMIT = 200;
/*     */   
/*     */   private static final int DARKNESS_DURATION = 260;
/*     */   
/*     */   private static final int DARKNESS_RADIUS = 20;
/*     */   
/*     */   private static final int DARKNESS_INTERVAL = 120;
/*     */   
/*     */   private static final int ANGERMANAGEMENT_TICK_DELAY = 20;
/*     */   
/*     */   private static final int DEFAULT_ANGER = 35;
/*     */   
/*     */   private static final int PROJECTILE_ANGER = 10;
/*     */   
/*     */   private static final int ON_HURT_ANGER_BOOST = 20;
/*     */   
/*     */   private static final int RECENT_PROJECTILE_TICK_THRESHOLD = 100;
/*     */   
/*     */   private static final int TOUCH_COOLDOWN_TICKS = 20;
/*     */   
/*     */   private static final int DIGGING_PARTICLES_AMOUNT = 30;
/*     */   
/*     */   private static final float DIGGING_PARTICLES_DURATION = 4.5F;
/*     */   
/*     */   private static final float DIGGING_PARTICLES_OFFSET = 0.7F;
/*     */   
/*     */   private static final int PROJECTILE_ANGER_DISTANCE = 30;
/*     */   
/* 112 */   private static final Brain.Provider<Warden> BRAIN_PROVIDER = Brain.provider(
/* 113 */       List.of(MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.RECENT_PROJECTILE, MemoryModuleType.TOUCH_COOLDOWN, MemoryModuleType.VIBRATION_COOLDOWN), 
/*     */       
/* 119 */       List.of(SensorType.NEAREST_PLAYERS, SensorType.WARDEN_ENTITY_SENSOR), WardenAi::getActivities);
/*     */   
/*     */   private int tendrilAnimation;
/*     */   
/*     */   private int tendrilAnimationO;
/*     */   
/*     */   private int heartAnimation;
/*     */   
/*     */   private int heartAnimationO;
/*     */   
/* 134 */   public final AnimationState roarAnimationState = new AnimationState();
/*     */   
/* 135 */   public final AnimationState sniffAnimationState = new AnimationState();
/*     */   
/* 136 */   public final AnimationState emergeAnimationState = new AnimationState();
/*     */   
/* 137 */   public final AnimationState diggingAnimationState = new AnimationState();
/*     */   
/* 138 */   public final AnimationState attackAnimationState = new AnimationState();
/*     */   
/* 139 */   public final AnimationState sonicBoomAnimationState = new AnimationState();
/*     */   
/*     */   private final DynamicGameEventListener<VibrationSystem.Listener> dynamicGameEventListener;
/*     */   
/*     */   private final VibrationSystem.User vibrationUser;
/*     */   
/*     */   private VibrationSystem.Data vibrationData;
/*     */   
/* 145 */   private AngerManagement angerManagement = new AngerManagement(this::canTargetEntity, Collections.emptyList());
/*     */   
/*     */   public Warden(EntityType<? extends Monster> type, Level level) {
/* 148 */     super(type, level);
/* 149 */     this.vibrationUser = new VibrationUser();
/* 150 */     this.vibrationData = new VibrationSystem.Data();
/* 151 */     this.dynamicGameEventListener = new DynamicGameEventListener((GameEventListener)new VibrationSystem.Listener(this));
/* 153 */     this.xpReward = 5;
/* 154 */     getNavigation().setCanFloat(true);
/* 156 */     setPathfindingMalus(PathType.UNPASSABLE_RAIL, 0.0F);
/* 157 */     setPathfindingMalus(PathType.DAMAGING, 8.0F);
/* 158 */     setPathfindingMalus(PathType.POWDER_SNOW, 8.0F);
/* 159 */     setPathfindingMalus(PathType.LAVA, 8.0F);
/* 160 */     setPathfindingMalus(PathType.FIRE, 0.0F);
/* 161 */     setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, 0.0F);
/*     */   }
/*     */   
/*     */   public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity serverEntity) {
/* 166 */     return (Packet<ClientGamePacketListener>)new ClientboundAddEntityPacket((Entity)this, serverEntity, hasPose(Pose.EMERGING) ? 1 : 0);
/*     */   }
/*     */   
/*     */   public void recreateFromPacket(ClientboundAddEntityPacket packet) {
/* 171 */     super.recreateFromPacket(packet);
/* 172 */     if (packet.getData() == 1)
/* 173 */       setPose(Pose.EMERGING); 
/*     */   }
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 179 */     return (super.checkSpawnObstruction(level) && level.noCollision((Entity)this, getType().getDimensions().makeBoundingBox(position())));
/*     */   }
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 184 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public boolean isInvulnerableTo(ServerLevel level, DamageSource source) {
/* 189 */     if (isDiggingOrEmerging() && !source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
/* 190 */       return true; 
/* 192 */     return super.isInvulnerableTo(level, source);
/*     */   }
/*     */   
/*     */   private boolean isDiggingOrEmerging() {
/* 196 */     return (hasPose(Pose.DIGGING) || hasPose(Pose.EMERGING));
/*     */   }
/*     */   
/*     */   protected boolean canRide(Entity vehicle) {
/* 201 */     return false;
/*     */   }
/*     */   
/*     */   public float getSecondsToDisableBlocking() {
/* 206 */     return 5.0F;
/*     */   }
/*     */   
/*     */   protected float nextStep() {
/* 212 */     return this.moveDist + 0.55F;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 216 */     return Monster.createMonsterAttributes()
/* 217 */       .add(Attributes.MAX_HEALTH, 500.0D)
/* 218 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 219 */       .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
/* 220 */       .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
/* 221 */       .add(Attributes.ATTACK_DAMAGE, 30.0D)
/* 222 */       .add(Attributes.FOLLOW_RANGE, 24.0D);
/*     */   }
/*     */   
/*     */   public boolean dampensVibrations() {
/* 227 */     return true;
/*     */   }
/*     */   
/*     */   protected float getSoundVolume() {
/* 233 */     return 4.0F;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 238 */     if (hasPose(Pose.ROARING) || isDiggingOrEmerging())
/* 239 */       return null; 
/* 242 */     return getAngerLevel().getAmbientSound();
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 247 */     return SoundEvents.WARDEN_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 252 */     return SoundEvents.WARDEN_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 257 */     playSound(SoundEvents.WARDEN_STEP, 10.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 262 */     level.broadcastEntityEvent((Entity)this, (byte)4);
/* 263 */     playSound(SoundEvents.WARDEN_ATTACK_IMPACT, 10.0F, getVoicePitch());
/* 265 */     SonicBoom.setCooldown((LivingEntity)this, 40);
/* 267 */     return super.doHurtTarget(level, target);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 272 */     super.defineSynchedData(entityData);
/* 274 */     entityData.define(CLIENT_ANGER_LEVEL, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public int getClientAngerLevel() {
/* 278 */     return ((Integer)this.entityData.get(CLIENT_ANGER_LEVEL)).intValue();
/*     */   }
/*     */   
/*     */   private void syncClientAngerLevel() {
/* 282 */     this.entityData.set(CLIENT_ANGER_LEVEL, Integer.valueOf(getActiveAnger()));
/*     */   }
/*     */   
/*     */   public void tick() {
/* 287 */     Level level = level();
/* 287 */     if (level instanceof ServerLevel) {
/* 287 */       ServerLevel serverLevel = (ServerLevel)level;
/* 288 */       VibrationSystem.Ticker.tick((Level)serverLevel, this.vibrationData, this.vibrationUser);
/* 290 */       if (isPersistenceRequired() || requiresCustomPersistence())
/* 291 */         WardenAi.setDigCooldown((LivingEntity)this); 
/*     */     } 
/* 295 */     super.tick();
/* 298 */     if (level().isClientSide()) {
/* 299 */       if (this.tickCount % getHeartBeatDelay() == 0) {
/* 300 */         this.heartAnimation = 10;
/* 301 */         if (!isSilent())
/* 302 */           level().playLocalSound(getX(), getY(), getZ(), SoundEvents.WARDEN_HEARTBEAT, getSoundSource(), 5.0F, getVoicePitch(), false); 
/*     */       } 
/* 306 */       this.tendrilAnimationO = this.tendrilAnimation;
/* 307 */       if (this.tendrilAnimation > 0)
/* 308 */         this.tendrilAnimation--; 
/* 311 */       this.heartAnimationO = this.heartAnimation;
/* 312 */       if (this.heartAnimation > 0)
/* 313 */         this.heartAnimation--; 
/* 316 */       switch (getPose()) {
/*     */         case EMERGING:
/* 317 */           clientDiggingParticles(this.emergeAnimationState);
/*     */           break;
/*     */         case DIGGING:
/* 318 */           clientDiggingParticles(this.diggingAnimationState);
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 325 */     ProfilerFiller profiler = Profiler.get();
/* 326 */     profiler.push("wardenBrain");
/* 327 */     getBrain().tick(level, (LivingEntity)this);
/* 328 */     profiler.pop();
/* 330 */     super.customServerAiStep(level);
/* 332 */     if ((this.tickCount + getId()) % 120 == 0)
/* 333 */       applyDarknessAround(level, position(), (Entity)this, 20); 
/* 336 */     if (this.tickCount % 20 == 0) {
/* 337 */       this.angerManagement.tick(level, this::canTargetEntity);
/* 338 */       syncClientAngerLevel();
/*     */     } 
/* 341 */     WardenAi.updateActivity(getBrain());
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 346 */     if (id == 4) {
/* 347 */       this.roarAnimationState.stop();
/* 348 */       this.attackAnimationState.start(this.tickCount);
/* 349 */     } else if (id == 61) {
/* 350 */       this.tendrilAnimation = 10;
/* 351 */     } else if (id == 62) {
/* 352 */       this.sonicBoomAnimationState.start(this.tickCount);
/*     */     } else {
/* 354 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getHeartBeatDelay() {
/* 359 */     float anger = getClientAngerLevel() / AngerLevel.ANGRY.getMinimumAnger();
/* 360 */     return 40 - Mth.floor(Mth.clamp(anger, 0.0F, 1.0F) * 30.0F);
/*     */   }
/*     */   
/*     */   public float getTendrilAnimation(float a) {
/* 365 */     return Mth.lerp(a, this.tendrilAnimationO, this.tendrilAnimation) / 10.0F;
/*     */   }
/*     */   
/*     */   public float getHeartAnimation(float a) {
/* 369 */     return Mth.lerp(a, this.heartAnimationO, this.heartAnimation) / 10.0F;
/*     */   }
/*     */   
/*     */   private void clientDiggingParticles(AnimationState state) {
/* 373 */     if ((float)state.getTimeInMillis(this.tickCount) < 4500.0F) {
/* 374 */       RandomSource random = getRandom();
/* 375 */       BlockState stateBelow = getBlockStateOn();
/* 377 */       if (stateBelow.getRenderShape() != RenderShape.INVISIBLE)
/* 378 */         for (int i = 0; i < 30; i++) {
/* 379 */           double xx = getX() + Mth.randomBetween(random, -0.7F, 0.7F);
/* 380 */           double yy = getY();
/* 381 */           double zz = getZ() + Mth.randomBetween(random, -0.7F, 0.7F);
/* 383 */           level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, stateBelow), xx, yy, zz, 0.0D, 0.0D, 0.0D);
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 391 */     if (DATA_POSE.equals(accessor))
/* 392 */       switch (getPose()) {
/*     */         case ROARING:
/* 393 */           this.roarAnimationState.start(this.tickCount);
/*     */           break;
/*     */         case SNIFFING:
/* 394 */           this.sniffAnimationState.start(this.tickCount);
/*     */           break;
/*     */         case EMERGING:
/* 395 */           this.emergeAnimationState.start(this.tickCount);
/*     */           break;
/*     */         case DIGGING:
/* 396 */           this.diggingAnimationState.start(this.tickCount);
/*     */           break;
/*     */       }  
/* 400 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   public boolean ignoreExplosion(Explosion explosion) {
/* 405 */     return isDiggingOrEmerging();
/*     */   }
/*     */   
/*     */   protected Brain<Warden> makeBrain(Brain.Packed packedBrain) {
/* 410 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Warden> getBrain() {
/* 416 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   public void updateDynamicGameEventListener(BiConsumer<DynamicGameEventListener<?>, ServerLevel> action) {
/* 421 */     Level level = level();
/* 421 */     if (level instanceof ServerLevel) {
/* 421 */       ServerLevel serverLevel = (ServerLevel)level;
/* 422 */       action.accept(this.dynamicGameEventListener, serverLevel);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean canAttack(LivingEntity target) {
/* 428 */     return canTargetEntity((Entity)target);
/*     */   }
/*     */   
/*     */   @Contract("null->false")
/*     */   public boolean canTargetEntity(Entity entity) {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: instanceof net/minecraft/world/entity/LivingEntity
/*     */     //   4: ifeq -> 98
/*     */     //   7: aload_1
/*     */     //   8: checkcast net/minecraft/world/entity/LivingEntity
/*     */     //   11: astore_2
/*     */     //   12: aload_0
/*     */     //   13: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   16: aload_1
/*     */     //   17: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   20: if_acmpne -> 98
/*     */     //   23: getstatic net/minecraft/world/entity/EntitySelector.NO_CREATIVE_OR_SPECTATOR : Ljava/util/function/Predicate;
/*     */     //   26: aload_1
/*     */     //   27: invokeinterface test : (Ljava/lang/Object;)Z
/*     */     //   32: ifeq -> 98
/*     */     //   35: aload_0
/*     */     //   36: aload_1
/*     */     //   37: invokevirtual isAlliedTo : (Lnet/minecraft/world/entity/Entity;)Z
/*     */     //   40: ifne -> 98
/*     */     //   43: aload_2
/*     */     //   44: getstatic net/minecraft/world/entity/EntityTypes.ARMOR_STAND : Lnet/minecraft/world/entity/EntityType;
/*     */     //   47: invokevirtual is : (Ljava/lang/Object;)Z
/*     */     //   50: ifne -> 98
/*     */     //   53: aload_2
/*     */     //   54: getstatic net/minecraft/world/entity/EntityTypes.WARDEN : Lnet/minecraft/world/entity/EntityType;
/*     */     //   57: invokevirtual is : (Ljava/lang/Object;)Z
/*     */     //   60: ifne -> 98
/*     */     //   63: aload_2
/*     */     //   64: invokevirtual isInvulnerable : ()Z
/*     */     //   67: ifne -> 98
/*     */     //   70: aload_2
/*     */     //   71: invokevirtual isDeadOrDying : ()Z
/*     */     //   74: ifne -> 98
/*     */     //   77: aload_0
/*     */     //   78: invokevirtual level : ()Lnet/minecraft/world/level/Level;
/*     */     //   81: invokevirtual getWorldBorder : ()Lnet/minecraft/world/level/border/WorldBorder;
/*     */     //   84: aload_2
/*     */     //   85: invokevirtual getBoundingBox : ()Lnet/minecraft/world/phys/AABB;
/*     */     //   88: invokevirtual isWithinBounds : (Lnet/minecraft/world/phys/AABB;)Z
/*     */     //   91: ifeq -> 98
/*     */     //   94: iconst_1
/*     */     //   95: goto -> 99
/*     */     //   98: iconst_0
/*     */     //   99: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #441	-> 0
/*     */     //   #433	-> 7
/*     */     //   #434	-> 13
/*     */     //   #435	-> 27
/*     */     //   #436	-> 37
/*     */     //   #437	-> 47
/*     */     //   #438	-> 57
/*     */     //   #439	-> 64
/*     */     //   #440	-> 71
/*     */     //   #441	-> 78
/*     */     //   #433	-> 99
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   12	86	2	livingEntity	Lnet/minecraft/world/entity/LivingEntity;
/*     */     //   0	100	0	this	Lnet/minecraft/world/entity/monster/warden/Warden;
/*     */     //   0	100	1	entity	Lnet/minecraft/world/entity/Entity;
/*     */   }
/*     */   
/*     */   public static void applyDarknessAround(ServerLevel level, Vec3 position, Entity source, int darknessRadius) {
/* 446 */     MobEffectInstance darkness = new MobEffectInstance(MobEffects.DARKNESS, 260, 0, false, false);
/* 447 */     MobEffectUtil.addEffectToPlayersAround(level, source, position, darknessRadius, darkness, 200);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 452 */     super.addAdditionalSaveData(output);
/* 454 */     output.store("anger", AngerManagement.codec(this::canTargetEntity), this.angerManagement);
/* 455 */     output.store("listener", VibrationSystem.Data.CODEC, this.vibrationData);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 460 */     super.readAdditionalSaveData(input);
/* 462 */     this
/* 463 */       .angerManagement = input.read("anger", AngerManagement.codec(this::canTargetEntity)).orElseGet(() -> new AngerManagement(this::canTargetEntity, Collections.emptyList()));
/* 464 */     syncClientAngerLevel();
/* 466 */     this.vibrationData = input.read("listener", VibrationSystem.Data.CODEC).orElseGet(net.minecraft.world.level.gameevent.vibrations.VibrationSystem.Data::new);
/*     */   }
/*     */   
/*     */   private void playListeningSound() {
/* 470 */     if (!hasPose(Pose.ROARING))
/* 471 */       playSound(getAngerLevel().getListeningSound(), 10.0F, getVoicePitch()); 
/*     */   }
/*     */   
/*     */   public AngerLevel getAngerLevel() {
/* 476 */     return AngerLevel.byAnger(getActiveAnger());
/*     */   }
/*     */   
/*     */   private int getActiveAnger() {
/* 480 */     return this.angerManagement.getActiveAnger((Entity)getTarget());
/*     */   }
/*     */   
/*     */   public void clearAnger(Entity entity) {
/* 484 */     this.angerManagement.clearAnger(entity);
/*     */   }
/*     */   
/*     */   public void increaseAngerAt(Entity entity) {
/* 488 */     increaseAngerAt(entity, 35, true);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void increaseAngerAt(Entity entity, int amount, boolean playSound) {
/* 493 */     if (!isNoAi() && canTargetEntity(entity)) {
/* 494 */       WardenAi.setDigCooldown((LivingEntity)this);
/* 496 */       boolean maybeSwitchTarget = !(getTarget() instanceof net.minecraft.world.entity.player.Player);
/* 498 */       int newAnger = this.angerManagement.increaseAnger(entity, amount);
/* 500 */       if (entity instanceof net.minecraft.world.entity.player.Player && maybeSwitchTarget && AngerLevel.byAnger(newAnger).isAngry())
/* 502 */         getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET); 
/* 505 */       if (playSound)
/* 506 */         playListeningSound(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Optional<LivingEntity> getEntityAngryAt() {
/* 512 */     if (getAngerLevel().isAngry())
/* 513 */       return this.angerManagement.getActiveEntity(); 
/* 515 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public LivingEntity getTarget() {
/* 520 */     return getTargetFromBrain();
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 525 */     return false;
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 530 */     getBrain().setMemoryWithExpiry(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 1200L);
/* 532 */     if (spawnReason == EntitySpawnReason.TRIGGERED) {
/* 533 */       setPose(Pose.EMERGING);
/* 534 */       getBrain().setMemoryWithExpiry(MemoryModuleType.IS_EMERGING, Unit.INSTANCE, WardenAi.EMERGE_DURATION);
/* 535 */       playSound(SoundEvents.WARDEN_AGITATED, 5.0F, 1.0F);
/*     */     } 
/* 538 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 543 */     boolean wasHurt = super.hurtServer(level, source, damage);
/* 544 */     if (!isNoAi() && !isDiggingOrEmerging()) {
/* 545 */       Entity attacker = source.getEntity();
/* 547 */       increaseAngerAt(attacker, AngerLevel.ANGRY.getMinimumAnger() + 20, false);
/* 550 */       if (this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty() && attacker instanceof LivingEntity) {
/* 551 */         LivingEntity livingAttacker = (LivingEntity)attacker;
/* 551 */         if (source
/* 552 */           .isDirect() || closerThan((Entity)livingAttacker, 5.0D))
/* 554 */           setAttackTarget(livingAttacker); 
/*     */       } 
/*     */     } 
/* 557 */     return wasHurt;
/*     */   }
/*     */   
/*     */   public void setAttackTarget(LivingEntity target) {
/* 561 */     getBrain().eraseMemory(MemoryModuleType.ROAR_TARGET);
/* 562 */     getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, target);
/* 563 */     getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 564 */     SonicBoom.setCooldown((LivingEntity)this, 200);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 569 */     EntityDimensions dimensions = super.getDefaultDimensions(pose);
/* 571 */     if (isDiggingOrEmerging())
/* 572 */       return EntityDimensions.fixed(dimensions.width(), 1.0F); 
/* 575 */     return dimensions;
/*     */   }
/*     */   
/*     */   public boolean isPushable() {
/* 580 */     return (!isDiggingOrEmerging() && super.isPushable());
/*     */   }
/*     */   
/*     */   protected void doPush(Entity entity) {
/* 585 */     if (!isNoAi() && !getBrain().hasMemoryValue(MemoryModuleType.TOUCH_COOLDOWN)) {
/* 586 */       getBrain().setMemoryWithExpiry(MemoryModuleType.TOUCH_COOLDOWN, Unit.INSTANCE, 20L);
/* 587 */       increaseAngerAt(entity);
/* 588 */       WardenAi.setDisturbanceLocation(this, entity.blockPosition());
/*     */     } 
/* 591 */     super.doPush(entity);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public AngerManagement getAngerManagement() {
/* 596 */     return this.angerManagement;
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 604 */     return (PathNavigation)new GroundPathNavigation(this, (Mob)this, level) {
/*     */         protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 607 */           this.nodeEvaluator = (NodeEvaluator)new WalkNodeEvaluator();
/* 608 */           return new PathFinder(this, this.nodeEvaluator, maxVisitedNodes) {
/*     */               protected float distance(Node from, Node to) {
/* 614 */                 return from.distanceToXZ(to);
/*     */               }
/*     */             };
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public VibrationSystem.Data getVibrationData() {
/* 623 */     return this.vibrationData;
/*     */   }
/*     */   
/*     */   public VibrationSystem.User getVibrationUser() {
/* 628 */     return this.vibrationUser;
/*     */   }
/*     */   
/*     */   private class VibrationUser implements VibrationSystem.User {
/*     */     private static final int GAME_EVENT_LISTENER_RANGE = 16;
/*     */     
/* 633 */     private final PositionSource positionSource = (PositionSource)new EntityPositionSource((Entity)Warden.this, Warden.this.getEyeHeight());
/*     */     
/*     */     public int getListenerRadius() {
/* 637 */       return 16;
/*     */     }
/*     */     
/*     */     public PositionSource getPositionSource() {
/* 642 */       return this.positionSource;
/*     */     }
/*     */     
/*     */     public TagKey<GameEvent> getListenableEvents() {
/* 647 */       return GameEventTags.WARDEN_CAN_LISTEN;
/*     */     }
/*     */     
/*     */     public boolean canTriggerAvoidVibration() {
/* 652 */       return true;
/*     */     }
/*     */     
/*     */     public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 657 */       if (Warden.this.isNoAi() || Warden.this
/* 658 */         .isDeadOrDying() || Warden.this
/* 659 */         .getBrain().hasMemoryValue(MemoryModuleType.VIBRATION_COOLDOWN) || Warden.this
/* 660 */         .isDiggingOrEmerging() || 
/* 661 */         !level.getWorldBorder().isWithinBounds(pos))
/* 663 */         return false; 
/* 666 */       Entity entity = context.sourceEntity();
/* 666 */       if (entity instanceof LivingEntity) {
/* 666 */         LivingEntity livingEntity = (LivingEntity)entity;
/* 666 */         if (Warden.this.canTargetEntity((Entity)livingEntity));
/* 666 */         return false;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void onReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, Entity sourceEntity, Entity projectileOwner, float receivingDistance) {
/* 671 */       if (Warden.this.isDeadOrDying())
/*     */         return; 
/* 675 */       Warden.this.brain.setMemoryWithExpiry(MemoryModuleType.VIBRATION_COOLDOWN, Unit.INSTANCE, 40L);
/* 677 */       level.broadcastEntityEvent((Entity)Warden.this, (byte)61);
/* 678 */       Warden.this.playSound(SoundEvents.WARDEN_TENDRIL_CLICKS, 5.0F, Warden.this.getVoicePitch());
/* 680 */       BlockPos suspiciousPos = pos;
/* 683 */       if (projectileOwner != null) {
/* 684 */         if (Warden.this.closerThan(projectileOwner, 30.0D))
/* 685 */           if (Warden.this.getBrain().hasMemoryValue(MemoryModuleType.RECENT_PROJECTILE)) {
/* 686 */             if (Warden.this.canTargetEntity(projectileOwner))
/* 687 */               suspiciousPos = projectileOwner.blockPosition(); 
/* 689 */             Warden.this.increaseAngerAt(projectileOwner);
/*     */           } else {
/* 691 */             Warden.this.increaseAngerAt(projectileOwner, 10, true);
/*     */           }  
/* 694 */         Warden.this.getBrain().setMemoryWithExpiry(MemoryModuleType.RECENT_PROJECTILE, Unit.INSTANCE, 100L);
/*     */       } else {
/* 696 */         Warden.this.increaseAngerAt(sourceEntity);
/*     */       } 
/* 699 */       if (!Warden.this.getAngerLevel().isAngry()) {
/* 701 */         Optional<LivingEntity> activeEntity = Warden.this.angerManagement.getActiveEntity();
/* 702 */         if (projectileOwner != null || activeEntity.isEmpty() || activeEntity.get() == sourceEntity)
/* 703 */           WardenAi.setDisturbanceLocation(Warden.this, suspiciousPos); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\warden\Warden.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */