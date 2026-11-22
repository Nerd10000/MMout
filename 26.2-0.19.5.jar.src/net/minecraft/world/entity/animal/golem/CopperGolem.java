/*     */ package net.minecraft.world.entity.animal.golem;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
/*     */ import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CopperGolem extends AbstractGolem implements ContainerUser, Shearable {
/*     */   private static final long IGNORE_WEATHERING_TICK = -2L;
/*     */   
/*     */   private static final long UNSET_WEATHERING_TICK = -1L;
/*     */   
/*     */   private static final int WEATHERING_TICK_FROM = 504000;
/*     */   
/*     */   private static final int WEATHERING_TICK_TO = 552000;
/*     */   
/*     */   private static final int SPIN_ANIMATION_MIN_COOLDOWN = 200;
/*     */   
/*     */   private static final int SPIN_ANIMATION_MAX_COOLDOWN = 240;
/*     */   
/*     */   private static final float SPIN_SOUND_TIME_INTERVAL_OFFSET = 10.0F;
/*     */   
/*     */   private static final float TURN_TO_STATUE_CHANCE = 0.0058F;
/*     */   
/*     */   private static final int SPAWN_COOLDOWN_MIN = 60;
/*     */   
/*     */   private static final int SPAWN_COOLDOWN_MAX = 100;
/*     */   
/*  74 */   private static final Brain.Provider<CopperGolem> BRAIN_PROVIDER = Brain.provider(
/*  75 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY), paramCopperGolem -> CopperGolemAi.getActivities());
/*     */   
/*  82 */   private static final EntityDataAccessor<WeatheringCopper.WeatherState> DATA_WEATHER_STATE = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.WEATHERING_COPPER_STATE);
/*     */   
/*  83 */   private static final EntityDataAccessor<CopperGolemState> COPPER_GOLEM_STATE = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.COPPER_GOLEM_STATE);
/*     */   
/*     */   private BlockPos openedChestPos;
/*     */   
/*     */   private UUID lastLightningBoltUUID;
/*     */   
/*  87 */   private long nextWeatheringTick = -1L;
/*     */   
/*  88 */   private int idleAnimationStartTick = 0;
/*     */   
/*  90 */   private final AnimationState idleAnimationState = new AnimationState();
/*     */   
/*  91 */   private final AnimationState interactionGetItemAnimationState = new AnimationState();
/*     */   
/*  92 */   private final AnimationState interactionGetNoItemAnimationState = new AnimationState();
/*     */   
/*  93 */   private final AnimationState interactionDropItemAnimationState = new AnimationState();
/*     */   
/*  94 */   private final AnimationState interactionDropNoItemAnimationState = new AnimationState();
/*     */   
/*  96 */   public static final EquipmentSlot EQUIPMENT_SLOT_ANTENNA = EquipmentSlot.SADDLE;
/*     */   
/*     */   public CopperGolem(EntityType<? extends AbstractGolem> type, Level level) {
/*  99 */     super(type, level);
/* 100 */     getNavigation().setRequiredPathLength(48.0F);
/* 101 */     getNavigation().setCanOpenDoors(true);
/* 102 */     setPersistenceRequired();
/* 103 */     setState(CopperGolemState.IDLE);
/* 104 */     setPathfindingMalus(PathType.FIRE_IN_NEIGHBOR, 16.0F);
/* 105 */     setPathfindingMalus(PathType.DAMAGING_IN_NEIGHBOR, 16.0F);
/* 106 */     setPathfindingMalus(PathType.FIRE, -1.0F);
/* 107 */     getBrain().setMemory(MemoryModuleType.TRANSPORT_ITEMS_COOLDOWN_TICKS, Integer.valueOf(getRandom().nextInt(60, 100)));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 111 */     return Mob.createMobAttributes()
/* 112 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D)
/* 113 */       .add(Attributes.STEP_HEIGHT, 1.0D)
/* 114 */       .add(Attributes.MAX_HEALTH, 12.0D);
/*     */   }
/*     */   
/*     */   public CopperGolemState getState() {
/* 118 */     return (CopperGolemState)this.entityData.get(COPPER_GOLEM_STATE);
/*     */   }
/*     */   
/*     */   public void setState(CopperGolemState state) {
/* 122 */     this.entityData.set(COPPER_GOLEM_STATE, state);
/*     */   }
/*     */   
/*     */   public WeatheringCopper.WeatherState getWeatherState() {
/* 126 */     return (WeatheringCopper.WeatherState)this.entityData.get(DATA_WEATHER_STATE);
/*     */   }
/*     */   
/*     */   public void setWeatherState(WeatheringCopper.WeatherState state) {
/* 130 */     this.entityData.set(DATA_WEATHER_STATE, state);
/*     */   }
/*     */   
/*     */   public void setOpenedChestPos(BlockPos openedChestPos) {
/* 134 */     this.openedChestPos = openedChestPos;
/*     */   }
/*     */   
/*     */   public void clearOpenedChestPos() {
/* 138 */     this.openedChestPos = null;
/*     */   }
/*     */   
/*     */   public AnimationState getIdleAnimationState() {
/* 142 */     return this.idleAnimationState;
/*     */   }
/*     */   
/*     */   public AnimationState getInteractionGetItemAnimationState() {
/* 146 */     return this.interactionGetItemAnimationState;
/*     */   }
/*     */   
/*     */   public AnimationState getInteractionGetNoItemAnimationState() {
/* 150 */     return this.interactionGetNoItemAnimationState;
/*     */   }
/*     */   
/*     */   public AnimationState getInteractionDropItemAnimationState() {
/* 154 */     return this.interactionDropItemAnimationState;
/*     */   }
/*     */   
/*     */   public AnimationState getInteractionDropNoItemAnimationState() {
/* 158 */     return this.interactionDropNoItemAnimationState;
/*     */   }
/*     */   
/*     */   protected Brain<CopperGolem> makeBrain(Brain.Packed packedBrain) {
/* 163 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<CopperGolem> getBrain() {
/* 169 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 174 */     super.defineSynchedData(entityData);
/* 175 */     entityData.define(DATA_WEATHER_STATE, WeatheringCopper.WeatherState.UNAFFECTED);
/* 176 */     entityData.define(COPPER_GOLEM_STATE, CopperGolemState.IDLE);
/*     */   }
/*     */   
/*     */   public void addAdditionalSaveData(ValueOutput output) {
/* 181 */     super.addAdditionalSaveData(output);
/* 182 */     output.putLong("next_weather_age", this.nextWeatheringTick);
/* 183 */     output.store("weather_state", WeatheringCopper.WeatherState.CODEC, getWeatherState());
/*     */   }
/*     */   
/*     */   public void readAdditionalSaveData(ValueInput input) {
/* 188 */     super.readAdditionalSaveData(input);
/* 189 */     this.nextWeatheringTick = input.getLongOr("next_weather_age", -1L);
/* 190 */     setWeatherState(input.read("weather_state", WeatheringCopper.WeatherState.CODEC).orElse(WeatheringCopper.WeatherState.UNAFFECTED));
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 195 */     ProfilerFiller profiler = Profiler.get();
/* 196 */     profiler.push("copperGolemBrain");
/* 197 */     getBrain().tick(level, (LivingEntity)this);
/* 198 */     profiler.pop();
/* 200 */     profiler.push("copperGolemActivityUpdate");
/* 201 */     CopperGolemAi.updateActivity(this);
/* 202 */     profiler.pop();
/* 204 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 209 */     super.tick();
/* 210 */     if (level().isClientSide()) {
/* 211 */       if (!isNoAi())
/* 212 */         setupAnimationStates(); 
/*     */     } else {
/* 215 */       updateWeathering((ServerLevel)level(), level().getRandom(), level().getGameTime());
/*     */     } 
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 221 */     ItemStack itemStack = player.getItemInHand(hand);
/* 222 */     if (itemStack.isEmpty()) {
/* 223 */       ItemStack equippedItem = getMainHandItem();
/* 224 */       if (!equippedItem.isEmpty()) {
/* 225 */         BehaviorUtils.throwItem((LivingEntity)this, equippedItem, player.position());
/* 226 */         setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
/* 227 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 231 */     Level level = level();
/* 232 */     if (itemStack.is(Items.SHEARS) && readyForShearing()) {
/* 233 */       if (level instanceof ServerLevel) {
/* 233 */         ServerLevel serverLevel = (ServerLevel)level;
/* 234 */         shear(serverLevel, SoundSource.PLAYERS, itemStack);
/* 235 */         gameEvent((Holder)GameEvent.SHEAR, (Entity)player);
/* 236 */         itemStack.hurtAndBreak(1, (LivingEntity)player, hand);
/*     */       } 
/* 238 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 241 */     if (level.isClientSide())
/* 242 */       return (InteractionResult)InteractionResult.PASS; 
/* 245 */     if (itemStack.is(Items.HONEYCOMB) && 
/* 246 */       this.nextWeatheringTick != -2L) {
/* 247 */       level.levelEvent((Entity)this, 3003, blockPosition(), 0);
/* 249 */       this.nextWeatheringTick = -2L;
/* 250 */       usePlayerItem(player, hand, itemStack);
/* 251 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/* 255 */     if (itemStack.is(ItemTags.AXES) && 
/* 256 */       this.nextWeatheringTick == -2L) {
/* 257 */       level.playSound(null, (Entity)this, SoundEvents.AXE_SCRAPE, getSoundSource(), 1.0F, 1.0F);
/* 258 */       level.levelEvent((Entity)this, 3004, blockPosition(), 0);
/* 260 */       this.nextWeatheringTick = -1L;
/* 261 */       itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/* 262 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/* 266 */     if (itemStack.is(ItemTags.AXES)) {
/* 267 */       WeatheringCopper.WeatherState weatherState = getWeatherState();
/* 268 */       if (weatherState != WeatheringCopper.WeatherState.UNAFFECTED) {
/* 269 */         level.playSound(null, (Entity)this, SoundEvents.AXE_SCRAPE, getSoundSource(), 1.0F, 1.0F);
/* 270 */         level.levelEvent((Entity)this, 3005, blockPosition(), 0);
/* 272 */         this.nextWeatheringTick = -1L;
/* 273 */         this.entityData.set(DATA_WEATHER_STATE, weatherState.previous(), true);
/* 274 */         itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
/* 275 */         return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */       } 
/*     */     } 
/* 279 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   private void updateWeathering(ServerLevel level, RandomSource random, long gameTime) {
/* 283 */     if (this.nextWeatheringTick == -2L)
/*     */       return; 
/* 286 */     if (this.nextWeatheringTick == -1L) {
/* 287 */       this.nextWeatheringTick = gameTime + random.nextIntBetweenInclusive(504000, 552000);
/*     */       return;
/*     */     } 
/* 290 */     WeatheringCopper.WeatherState weatherState = (WeatheringCopper.WeatherState)this.entityData.get(DATA_WEATHER_STATE);
/* 291 */     boolean isFullyOxidized = weatherState.equals(WeatheringCopper.WeatherState.OXIDIZED);
/* 292 */     if (gameTime >= this.nextWeatheringTick && !isFullyOxidized) {
/* 293 */       WeatheringCopper.WeatherState newState = weatherState.next();
/* 294 */       boolean isNewStateFullyOxidized = newState.equals(WeatheringCopper.WeatherState.OXIDIZED);
/* 295 */       setWeatherState(newState);
/* 296 */       this.nextWeatheringTick = isNewStateFullyOxidized ? 0L : (this.nextWeatheringTick + random.nextIntBetweenInclusive(504000, 552000));
/*     */     } 
/* 299 */     if (isFullyOxidized && 
/* 300 */       canTurnToStatue((Level)level))
/* 301 */       turnToStatue(level); 
/*     */   }
/*     */   
/*     */   private boolean canTurnToStatue(Level level) {
/* 307 */     return (level.getBlockState(blockPosition()).isAir() && level.getRandom().nextFloat() <= 0.0058F);
/*     */   }
/*     */   
/*     */   private void turnToStatue(ServerLevel level) {
/* 311 */     BlockPos pos = blockPosition();
/* 312 */     level.setBlock(pos, (BlockState)((BlockState)((Block)Blocks.COPPER_GOLEM_STATUE.weathering().oxidized()).defaultBlockState()
/* 313 */         .setValue((Property)CopperGolemStatueBlock.POSE, (Comparable)CopperGolemStatueBlock.Pose.values()[this.random.nextInt(0, (CopperGolemStatueBlock.Pose.values()).length)]))
/* 314 */         .setValue((Property)CopperGolemStatueBlock.FACING, (Comparable)Direction.fromYRot(getYRot())), 3);
/* 315 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 315 */     if (blockEntity instanceof CopperGolemStatueBlockEntity) {
/* 315 */       CopperGolemStatueBlockEntity copperGolemStatueBlockEntity = (CopperGolemStatueBlockEntity)blockEntity;
/* 316 */       copperGolemStatueBlockEntity.createStatue(this);
/* 317 */       dropPreservedEquipment(level);
/* 318 */       discard();
/* 319 */       playSound(SoundEvents.COPPER_GOLEM_BECOME_STATUE);
/* 321 */       if (isLeashed())
/* 322 */         if (((Boolean)level.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 323 */           dropLeash();
/*     */         } else {
/* 325 */           removeLeash();
/*     */         }  
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 332 */     switch (getState()) {
/*     */       case IDLE:
/* 334 */         this.interactionGetNoItemAnimationState.stop();
/* 335 */         this.interactionGetItemAnimationState.stop();
/* 336 */         this.interactionDropItemAnimationState.stop();
/* 337 */         this.interactionDropNoItemAnimationState.stop();
/* 338 */         if (this.idleAnimationStartTick == this.tickCount) {
/* 339 */           this.idleAnimationState.start(this.tickCount);
/* 340 */         } else if (this.idleAnimationStartTick == 0) {
/* 341 */           this.idleAnimationStartTick = this.tickCount + this.random.nextInt(200, 240);
/*     */         } 
/* 344 */         if (this.tickCount == this.idleAnimationStartTick + 10.0F) {
/* 345 */           playHeadSpinSound();
/* 346 */           this.idleAnimationStartTick = 0;
/*     */         } 
/*     */         break;
/*     */       case GETTING_ITEM:
/* 350 */         this.idleAnimationState.stop();
/* 351 */         this.idleAnimationStartTick = 0;
/* 352 */         this.interactionGetNoItemAnimationState.stop();
/* 353 */         this.interactionDropItemAnimationState.stop();
/* 354 */         this.interactionDropNoItemAnimationState.stop();
/* 355 */         this.interactionGetItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */       case GETTING_NO_ITEM:
/* 358 */         this.idleAnimationState.stop();
/* 359 */         this.idleAnimationStartTick = 0;
/* 360 */         this.interactionGetItemAnimationState.stop();
/* 361 */         this.interactionDropNoItemAnimationState.stop();
/* 362 */         this.interactionDropItemAnimationState.stop();
/* 363 */         this.interactionGetNoItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */       case DROPPING_ITEM:
/* 366 */         this.idleAnimationState.stop();
/* 367 */         this.idleAnimationStartTick = 0;
/* 368 */         this.interactionGetItemAnimationState.stop();
/* 369 */         this.interactionGetNoItemAnimationState.stop();
/* 370 */         this.interactionDropNoItemAnimationState.stop();
/* 371 */         this.interactionDropItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */       case DROPPING_NO_ITEM:
/* 374 */         this.idleAnimationState.stop();
/* 375 */         this.idleAnimationStartTick = 0;
/* 376 */         this.interactionGetItemAnimationState.stop();
/* 377 */         this.interactionGetNoItemAnimationState.stop();
/* 378 */         this.interactionDropItemAnimationState.stop();
/* 379 */         this.interactionDropNoItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void spawn(WeatheringCopper.WeatherState weatherState) {
/* 385 */     setWeatherState(weatherState);
/* 386 */     playSpawnSound();
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 391 */     playSpawnSound();
/* 392 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public void playSpawnSound() {
/* 396 */     playSound(SoundEvents.COPPER_GOLEM_SPAWN);
/*     */   }
/*     */   
/*     */   private void playHeadSpinSound() {
/* 400 */     if (!isSilent())
/* 401 */       level().playLocalSound(getX(), getY(), getZ(), getSpinHeadSound(), getSoundSource(), 1.0F, 1.0F, false); 
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 407 */     return CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).hurtSound();
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 412 */     return CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).deathSound();
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 417 */     playSound(CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).stepSound(), 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private SoundEvent getSpinHeadSound() {
/* 421 */     return CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).spinHeadSound();
/*     */   }
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 426 */     return new Vec3(0.0D, (0.75F * getEyeHeight()), 0.0D);
/*     */   }
/*     */   
/*     */   public boolean hasContainerOpen(ContainerOpenersCounter container, BlockPos blockPos) {
/* 431 */     if (this.openedChestPos == null)
/* 432 */       return false; 
/* 434 */     BlockState blockState = level().getBlockState(this.openedChestPos);
/* 435 */     return (this.openedChestPos.equals(blockPos) || (blockState.getBlock() instanceof ChestBlock && blockState
/* 436 */       .getValue((Property)ChestBlock.TYPE) != ChestType.SINGLE && 
/* 437 */       ChestBlock.getConnectedBlockPos(this.openedChestPos, blockState).equals(blockPos)));
/*     */   }
/*     */   
/*     */   public double getContainerInteractionRange() {
/* 442 */     return 3.0D;
/*     */   }
/*     */   
/*     */   public void shear(ServerLevel level, SoundSource soundSource, ItemStack tool) {
/* 447 */     level.playSound(null, (Entity)this, SoundEvents.COPPER_GOLEM_SHEAR, soundSource, 1.0F, 1.0F);
/* 448 */     ItemStack itemStack = getItemBySlot(EQUIPMENT_SLOT_ANTENNA);
/* 449 */     setItemSlot(EQUIPMENT_SLOT_ANTENNA, ItemStack.EMPTY);
/* 450 */     spawnAtLocation(level, itemStack, 1.5F);
/*     */   }
/*     */   
/*     */   public boolean readyForShearing() {
/* 455 */     return getItemBySlot(EQUIPMENT_SLOT_ANTENNA).is(ItemTags.SHEARABLE_FROM_COPPER_GOLEM);
/*     */   }
/*     */   
/*     */   protected void dropEquipment(ServerLevel level) {
/* 460 */     super.dropEquipment(level);
/* 461 */     dropPreservedEquipment(level);
/*     */   }
/*     */   
/*     */   protected void actuallyHurt(ServerLevel level, DamageSource source, float dmg) {
/* 466 */     super.actuallyHurt(level, source, dmg);
/* 467 */     setState(CopperGolemState.IDLE);
/*     */   }
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/* 472 */     super.thunderHit(level, lightningBolt);
/* 474 */     UUID lightningBoltUUID = lightningBolt.getUUID();
/* 475 */     if (!lightningBoltUUID.equals(this.lastLightningBoltUUID)) {
/* 476 */       this.lastLightningBoltUUID = lightningBoltUUID;
/* 477 */       WeatheringCopper.WeatherState weatherState = getWeatherState();
/* 478 */       if (weatherState != WeatheringCopper.WeatherState.UNAFFECTED) {
/* 479 */         this.nextWeatheringTick = -1L;
/* 480 */         this.entityData.set(DATA_WEATHER_STATE, weatherState.previous(), true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\golem\CopperGolem.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */