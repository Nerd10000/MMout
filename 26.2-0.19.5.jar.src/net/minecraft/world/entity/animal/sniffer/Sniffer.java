/*     */ package net.minecraft.world.entity.animal.sniffer;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.Leashable;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Sniffer extends Animal {
/*     */   public enum State {
/*  79 */     IDLING(0),
/*  80 */     FEELING_HAPPY(1),
/*  81 */     SCENTING(2),
/*  82 */     SNIFFING(3),
/*  83 */     SEARCHING(4),
/*  84 */     DIGGING(5),
/*  85 */     RISING(6);
/*     */     
/*  88 */     public static final IntFunction<State> BY_ID = ByIdMap.continuous(State::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */     
/*  90 */     public static final StreamCodec<ByteBuf, State> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, State::id);
/*     */     
/*     */     private final int id;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     State(int id) {
/*  95 */       this.id = id;
/*     */     }
/*     */     
/*     */     public int id() {
/*  99 */       return this.id;
/*     */     }
/*     */   }
/*     */   
/* 103 */   private static final Brain.Provider<Sniffer> BRAIN_PROVIDER = Brain.provider(
/* 104 */       List.of(MemoryModuleType.SNIFFER_EXPLORED_POSITIONS), 
/*     */       
/* 107 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.NEAREST_PLAYERS, SensorType.FOOD_TEMPTATIONS), paramSniffer -> SnifferAi.getActivities());
/*     */   
/*     */   private static final int DIGGING_PARTICLES_DELAY_TICKS = 1700;
/*     */   
/*     */   private static final int DIGGING_PARTICLES_DURATION_TICKS = 6000;
/*     */   
/*     */   private static final int DIGGING_PARTICLES_AMOUNT = 30;
/*     */   
/*     */   private static final int DIGGING_DROP_SEED_OFFSET_TICKS = 120;
/*     */   
/*     */   private static final int SNIFFER_BABY_START_AGE = -48000;
/*     */   
/*     */   private static final float DIGGING_BB_HEIGHT_OFFSET = 0.4F;
/*     */   
/* 125 */   private static final EntityDimensions DIGGING_DIMENSIONS = EntityDimensions.scalable(EntityTypes.SNIFFER.getWidth(), EntityTypes.SNIFFER.getHeight() - 0.4F).withEyeHeight(0.81F);
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 128 */     return Animal.createAnimalAttributes()
/* 129 */       .add(Attributes.MOVEMENT_SPEED, 0.10000000149011612D)
/* 130 */       .add(Attributes.MAX_HEALTH, 14.0D);
/*     */   }
/*     */   
/* 134 */   private static final EntityDataAccessor<State> DATA_STATE = SynchedEntityData.defineId(Sniffer.class, EntityDataSerializers.SNIFFER_STATE);
/*     */   
/* 135 */   private static final EntityDataAccessor<Integer> DATA_DROP_SEED_AT_TICK = SynchedEntityData.defineId(Sniffer.class, EntityDataSerializers.INT);
/*     */   
/* 137 */   public final AnimationState feelingHappyAnimationState = new AnimationState();
/*     */   
/* 138 */   public final AnimationState scentingAnimationState = new AnimationState();
/*     */   
/* 139 */   public final AnimationState sniffingAnimationState = new AnimationState();
/*     */   
/* 140 */   public final AnimationState diggingAnimationState = new AnimationState();
/*     */   
/* 141 */   public final AnimationState risingAnimationState = new AnimationState();
/*     */   
/*     */   public Sniffer(EntityType<? extends Animal> type, Level level) {
/* 144 */     super(type, level);
/* 146 */     getNavigation().setCanFloat(true);
/* 147 */     setPathfindingMalus(PathType.WATER, -1.0F);
/* 148 */     setPathfindingMalus(PathType.ON_TOP_OF_POWDER_SNOW, -1.0F);
/* 149 */     setPathfindingMalus(PathType.DAMAGE_CAUTIOUS, -1.0F);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 154 */     super.defineSynchedData(entityData);
/* 155 */     entityData.define(DATA_STATE, State.IDLING);
/* 156 */     entityData.define(DATA_DROP_SEED_AT_TICK, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public void onPathfindingStart() {
/* 161 */     super.onPathfindingStart();
/* 165 */     if (isOnFire() || isInWater())
/* 166 */       setPathfindingMalus(PathType.WATER, 0.0F); 
/*     */   }
/*     */   
/*     */   public void onPathfindingDone() {
/* 172 */     setPathfindingMalus(PathType.WATER, -1.0F);
/*     */   }
/*     */   
/*     */   public int getBabyStartAge() {
/* 177 */     return -48000;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 182 */     if (getState() == State.DIGGING)
/* 183 */       return DIGGING_DIMENSIONS.scale(getAgeScale()); 
/* 185 */     return super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public boolean isSearching() {
/* 189 */     return (getState() == State.SEARCHING);
/*     */   }
/*     */   
/*     */   public boolean isTempted() {
/* 193 */     return ((Boolean)this.brain.getMemory(MemoryModuleType.IS_TEMPTED).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean canSniff() {
/* 197 */     return (!isTempted() && !isPanicking() && !isInWater() && !isInLove() && onGround() && !isPassenger() && !isLeashed());
/*     */   }
/*     */   
/*     */   public boolean canPlayDiggingSound() {
/* 201 */     return (getState() == State.DIGGING || getState() == State.SEARCHING);
/*     */   }
/*     */   
/*     */   private BlockPos getHeadBlock() {
/* 205 */     Vec3 position = getHeadPosition();
/* 208 */     return BlockPos.containing(position.x(), getY() + 0.20000000298023224D, position.z());
/*     */   }
/*     */   
/*     */   private Vec3 getHeadPosition() {
/* 213 */     return position().add(getForward().scale(2.25D));
/*     */   }
/*     */   
/*     */   public boolean supportQuadLeash() {
/* 218 */     return true;
/*     */   }
/*     */   
/*     */   public Vec3[] getQuadLeashOffsets() {
/* 223 */     return Leashable.createQuadLeashOffsets((Entity)this, -0.01D, 0.63D, 0.38D, 1.15D);
/*     */   }
/*     */   
/*     */   private State getState() {
/* 227 */     return (State)this.entityData.get(DATA_STATE);
/*     */   }
/*     */   
/*     */   private Sniffer setState(State state) {
/* 231 */     this.entityData.set(DATA_STATE, state);
/* 232 */     return this;
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 237 */     if (DATA_STATE.equals(accessor)) {
/* 238 */       State state = getState();
/* 240 */       resetAnimations();
/* 242 */       switch (state.ordinal()) {
/*     */         case 2:
/* 243 */           this.scentingAnimationState.startIfStopped(this.tickCount);
/*     */           break;
/*     */         case 3:
/* 244 */           this.sniffingAnimationState.startIfStopped(this.tickCount);
/*     */           break;
/*     */         case 5:
/* 245 */           this.diggingAnimationState.startIfStopped(this.tickCount);
/*     */           break;
/*     */         case 6:
/* 246 */           this.risingAnimationState.startIfStopped(this.tickCount);
/*     */           break;
/*     */         case 1:
/* 247 */           this.feelingHappyAnimationState.startIfStopped(this.tickCount);
/*     */           break;
/*     */       } 
/* 250 */       refreshDimensions();
/*     */     } 
/* 253 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   private void resetAnimations() {
/* 257 */     this.diggingAnimationState.stop();
/* 258 */     this.sniffingAnimationState.stop();
/* 259 */     this.risingAnimationState.stop();
/* 260 */     this.feelingHappyAnimationState.stop();
/* 261 */     this.scentingAnimationState.stop();
/*     */   }
/*     */   
/*     */   public Sniffer transitionTo(State state) {
/* 265 */     switch (state.ordinal()) {
/*     */       case 0:
/* 267 */         setState(State.IDLING);
/*     */         break;
/*     */       case 2:
/* 270 */         setState(State.SCENTING).onScentingStart();
/*     */         break;
/*     */       case 3:
/* 273 */         playSound(SoundEvents.SNIFFER_SNIFFING, 1.0F, 1.0F);
/* 274 */         setState(State.SNIFFING);
/*     */         break;
/*     */       case 4:
/* 277 */         setState(State.SEARCHING);
/*     */         break;
/*     */       case 5:
/* 280 */         setState(State.DIGGING).onDiggingStart();
/*     */         break;
/*     */       case 6:
/* 283 */         playSound(SoundEvents.SNIFFER_DIGGING_STOP, 1.0F, 1.0F);
/* 284 */         setState(State.RISING);
/*     */         break;
/*     */       case 1:
/* 287 */         playSound(SoundEvents.SNIFFER_HAPPY, 1.0F, 1.0F);
/* 288 */         setState(State.FEELING_HAPPY);
/*     */         break;
/*     */     } 
/* 291 */     return this;
/*     */   }
/*     */   
/*     */   private Sniffer onScentingStart() {
/* 295 */     playSound(SoundEvents.SNIFFER_SCENTING, 1.0F, isBaby() ? 1.3F : 1.0F);
/* 296 */     return this;
/*     */   }
/*     */   
/*     */   private Sniffer onDiggingStart() {
/* 301 */     this.entityData.set(DATA_DROP_SEED_AT_TICK, Integer.valueOf(this.tickCount + 120));
/* 304 */     level().broadcastEntityEvent((Entity)this, (byte)63);
/* 306 */     return this;
/*     */   }
/*     */   
/*     */   public Sniffer onDiggingComplete(boolean success) {
/* 310 */     if (success)
/* 311 */       storeExploredPosition(getOnPos()); 
/* 313 */     return this;
/*     */   }
/*     */   
/*     */   Optional<BlockPos> calculateDigPosition() {
/* 318 */     return 
/* 319 */       IntStream.range(0, 5)
/* 320 */       .mapToObj(idx -> LandRandomPos.getPos((PathfinderMob)this, 10 + 2 * idx, 3))
/* 321 */       .filter(Objects::nonNull)
/* 322 */       .map(BlockPos::containing)
/* 323 */       .filter(position -> level().getWorldBorder().isWithinBounds(position))
/* 324 */       .map(BlockPos::below)
/* 325 */       .filter(this::canDig)
/* 326 */       .findFirst();
/*     */   }
/*     */   
/*     */   boolean canDig() {
/* 331 */     return (!isPanicking() && !isTempted() && !isBaby() && !isInWater() && onGround() && !isPassenger() && canDig(getHeadBlock().below()));
/*     */   }
/*     */   
/*     */   private boolean canDig(BlockPos position) {
/* 335 */     return (level().getBlockState(position).is(BlockTags.SNIFFER_DIGGABLE_BLOCK) && 
/* 336 */       getExploredPositions().noneMatch(explored -> GlobalPos.of(level().dimension(), position).equals(explored)) && (
/* 337 */       (Boolean)Optional.<Path>ofNullable(getNavigation().createPath(position, 1)).map(Path::canReach).orElse(Boolean.valueOf(false))).booleanValue());
/*     */   }
/*     */   
/*     */   private void dropSeed() {
/* 341 */     Level level = level();
/* 341 */     if (level instanceof ServerLevel) {
/* 341 */       ServerLevel serverLevel = (ServerLevel)level;
/* 341 */       if (((Integer)this.entityData.get(DATA_DROP_SEED_AT_TICK)).intValue() == this.tickCount) {
/* 345 */         BlockPos head = getHeadBlock();
/* 346 */         dropFromGiftLootTable(serverLevel, BuiltInLootTables.SNIFFER_DIGGING, (l, itemStack) -> {
/*     */               ItemEntity entity = new ItemEntity(level(), head.getX(), head.getY(), head.getZ(), itemStack);
/*     */               entity.setDefaultPickUpDelay();
/*     */               l.addFreshEntity((Entity)entity);
/*     */             });
/* 352 */         playSound(SoundEvents.SNIFFER_DROP_SEED, 1.0F, 1.0F);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Sniffer emitDiggingParticles(AnimationState state) {
/* 358 */     boolean emit = (state.getTimeInMillis(this.tickCount) > 1700L && state.getTimeInMillis(this.tickCount) < 6000L);
/* 360 */     if (emit) {
/* 361 */       BlockPos head = getHeadBlock();
/* 362 */       BlockState stateBelow = level().getBlockState(head.below());
/* 364 */       if (stateBelow.getRenderShape() != RenderShape.INVISIBLE) {
/* 365 */         for (int i = 0; i < 30; i++) {
/* 366 */           Vec3 centered = Vec3.atCenterOf((Vec3i)head).add(0.0D, -0.6499999761581421D, 0.0D);
/* 368 */           level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, stateBelow), centered.x, centered.y, centered.z, 0.0D, 0.0D, 0.0D);
/*     */         } 
/* 372 */         if (this.tickCount % 10 == 0)
/* 373 */           level().playLocalSound(getX(), getY(), getZ(), stateBelow.getSoundType().getHitSound(), getSoundSource(), 0.5F, 0.5F, false); 
/*     */       } 
/*     */     } 
/* 379 */     if (this.tickCount % 10 == 0)
/* 380 */       level().gameEvent((Holder)GameEvent.ENTITY_ACTION, getHeadBlock(), GameEvent.Context.of((Entity)this)); 
/* 383 */     return this;
/*     */   }
/*     */   
/*     */   private Sniffer storeExploredPosition(BlockPos position) {
/* 387 */     List<GlobalPos> updated = (List<GlobalPos>)getExploredPositions().limit(20L).collect(Collectors.toList());
/* 389 */     updated.add(0, GlobalPos.of(level().dimension(), position));
/* 390 */     getBrain().setMemory(MemoryModuleType.SNIFFER_EXPLORED_POSITIONS, updated);
/* 391 */     return this;
/*     */   }
/*     */   
/*     */   private Stream<GlobalPos> getExploredPositions() {
/* 395 */     return getBrain().getMemory(MemoryModuleType.SNIFFER_EXPLORED_POSITIONS)
/* 396 */       .stream()
/* 397 */       .flatMap(Collection::stream);
/*     */   }
/*     */   
/*     */   public void jumpFromGround() {
/* 403 */     super.jumpFromGround();
/* 404 */     double speedModifier = this.moveControl.getSpeedModifier();
/* 405 */     if (speedModifier > 0.0D) {
/* 406 */       double current = getDeltaMovement().horizontalDistanceSqr();
/* 407 */       if (current < 0.01D)
/* 408 */         moveRelative(0.1F, new Vec3(0.0D, 0.0D, 1.0D)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
/* 415 */     ItemStack itemStack = new ItemStack((ItemLike)Items.SNIFFER_EGG);
/* 416 */     ItemEntity entity = new ItemEntity((Level)level, position().x(), position().y(), position().z(), itemStack);
/* 417 */     entity.setDefaultPickUpDelay();
/* 419 */     finalizeSpawnChildFromBreeding(level, partner, null);
/* 421 */     playSound(SoundEvents.SNIFFER_EGG_PLOP, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.5F);
/* 422 */     level.addFreshEntity((Entity)entity);
/*     */   }
/*     */   
/*     */   public void die(DamageSource source) {
/* 427 */     transitionTo(State.IDLING);
/* 428 */     super.die(source);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 433 */     switch (getState().ordinal()) {
/*     */       case 5:
/* 434 */         emitDiggingParticles(this.diggingAnimationState).dropSeed();
/*     */         break;
/*     */       case 4:
/* 435 */         playSearchingSound();
/*     */         break;
/*     */     } 
/* 437 */     super.tick();
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 442 */     ItemStack heldItem = player.getItemInHand(hand);
/* 443 */     boolean isFood = isFood(heldItem);
/* 445 */     InteractionResult interactionResult = super.mobInteract(player, hand);
/* 446 */     if (interactionResult.consumesAction() && isFood)
/* 447 */       playEatingSound(); 
/* 449 */     return interactionResult;
/*     */   }
/*     */   
/*     */   protected void playEatingSound() {
/* 454 */     level().playSound(null, (Entity)this, SoundEvents.SNIFFER_EAT, SoundSource.NEUTRAL, 1.0F, Mth.randomBetween(level().getRandom(), 0.8F, 1.2F));
/*     */   }
/*     */   
/*     */   private void playSearchingSound() {
/* 458 */     if (level().isClientSide() && this.tickCount % 20 == 0)
/* 459 */       level().playLocalSound(getX(), getY(), getZ(), SoundEvents.SNIFFER_SEARCHING, getSoundSource(), 1.0F, 1.0F, false); 
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 465 */     playSound(SoundEvents.SNIFFER_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 470 */     return Set.<State>of(State.DIGGING, State.SEARCHING).contains(getState()) ? null : SoundEvents.SNIFFER_IDLE;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 475 */     return SoundEvents.SNIFFER_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 480 */     return SoundEvents.SNIFFER_DEATH;
/*     */   }
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 485 */     return 50;
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 490 */     return (AgeableMob)EntityTypes.SNIFFER.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   public boolean canMate(Animal partner) {
/* 495 */     if (partner instanceof Sniffer) {
/* 495 */       Sniffer snifferPartner = (Sniffer)partner;
/* 496 */       Set<State> states = Set.of(State.IDLING, State.SCENTING, State.FEELING_HAPPY);
/* 497 */       return (states.contains(getState()) && states.contains(snifferPartner.getState()) && super.canMate(partner));
/*     */     } 
/* 500 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 505 */     return itemStack.is(ItemTags.SNIFFER_FOOD);
/*     */   }
/*     */   
/*     */   protected Brain<Sniffer> makeBrain(Brain.Packed packedBrain) {
/* 510 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Sniffer> getBrain() {
/* 516 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 521 */     ProfilerFiller profiler = Profiler.get();
/* 522 */     profiler.push("snifferBrain");
/* 523 */     getBrain().tick(level, (LivingEntity)this);
/* 525 */     profiler.popPush("snifferActivityUpdate");
/* 527 */     SnifferAi.updateActivity(this);
/* 528 */     profiler.pop();
/* 530 */     super.customServerAiStep(level);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\sniffer\Sniffer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */