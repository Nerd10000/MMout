/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
/*     */ import net.minecraft.world.entity.variant.SpawnContext;
/*     */ import net.minecraft.world.entity.variant.VariantUtils;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.Node;
/*     */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*     */ import net.minecraft.world.level.pathfinder.PathFinder;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.PathfindingContext;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Frog extends Animal {
/*  74 */   private static final Brain.Provider<Frog> BRAIN_PROVIDER = Brain.provider(
/*  75 */       List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FROG_ATTACKABLES, SensorType.FROG_TEMPTATIONS, SensorType.IS_IN_WATER), paramFrog -> FrogAi.getActivities());
/*     */   
/*  85 */   private static final EntityDataAccessor<Holder<FrogVariant>> DATA_VARIANT_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.FROG_VARIANT);
/*     */   
/*  86 */   private static final EntityDataAccessor<OptionalInt> DATA_TONGUE_TARGET_ID = SynchedEntityData.defineId(Frog.class, EntityDataSerializers.OPTIONAL_UNSIGNED_INT);
/*     */   
/*     */   private static final int FROG_FALL_DAMAGE_REDUCTION = 5;
/*     */   
/*  89 */   private static final ResourceKey<FrogVariant> DEFAULT_VARIANT = FrogVariants.TEMPERATE;
/*     */   
/*  91 */   public final AnimationState jumpAnimationState = new AnimationState();
/*     */   
/*  92 */   public final AnimationState croakAnimationState = new AnimationState();
/*     */   
/*  93 */   public final AnimationState tongueAnimationState = new AnimationState();
/*     */   
/*  94 */   public final AnimationState swimIdleAnimationState = new AnimationState();
/*     */   
/*     */   public Frog(EntityType<? extends Animal> type, Level level) {
/*  97 */     super(type, level);
/*  98 */     this.lookControl = new FrogLookControl((Mob)this);
/* 100 */     setPathfindingMalus(PathType.WATER, 4.0F);
/* 101 */     setPathfindingMalus(PathType.TRAPDOOR, -1.0F);
/* 102 */     this.moveControl = (MoveControl)new SmoothSwimmingMoveControl((Mob)this, 85, 10, 0.02F, 0.1F, true);
/*     */   }
/*     */   
/*     */   protected Brain<Frog> makeBrain(Brain.Packed packedBrain) {
/* 107 */     return BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/*     */   }
/*     */   
/*     */   public Brain<Frog> getBrain() {
/* 113 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 118 */     super.defineSynchedData(entityData);
/* 119 */     Registry<FrogVariant> variants = registryAccess().lookupOrThrow(Registries.FROG_VARIANT);
/* 120 */     entityData.define(DATA_VARIANT_ID, VariantUtils.getDefaultOrAny(registryAccess(), DEFAULT_VARIANT));
/* 121 */     entityData.define(DATA_TONGUE_TARGET_ID, OptionalInt.empty());
/*     */   }
/*     */   
/*     */   public void eraseTongueTarget() {
/* 125 */     this.entityData.set(DATA_TONGUE_TARGET_ID, OptionalInt.empty());
/*     */   }
/*     */   
/*     */   public Optional<Entity> getTongueTarget() {
/* 130 */     Objects.requireNonNull(level());
/* 130 */     return ((OptionalInt)this.entityData.get(DATA_TONGUE_TARGET_ID)).stream().<Entity>mapToObj(level()::getEntity)
/* 131 */       .filter(Objects::nonNull)
/* 132 */       .findFirst();
/*     */   }
/*     */   
/*     */   public void setTongueTarget(Entity target) {
/* 136 */     this.entityData.set(DATA_TONGUE_TARGET_ID, OptionalInt.of(target.getId()));
/*     */   }
/*     */   
/*     */   public int getHeadRotSpeed() {
/* 141 */     return 35;
/*     */   }
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 146 */     return 5;
/*     */   }
/*     */   
/*     */   public Holder<FrogVariant> getVariant() {
/* 150 */     return (Holder<FrogVariant>)this.entityData.get(DATA_VARIANT_ID);
/*     */   }
/*     */   
/*     */   private void setVariant(Holder<FrogVariant> variant) {
/* 154 */     this.entityData.set(DATA_VARIANT_ID, variant);
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 159 */     if (type == DataComponents.FROG_VARIANT)
/* 160 */       return (T)castComponentValue(type, getVariant()); 
/* 163 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 168 */     applyImplicitComponentIfPresent(components, DataComponents.FROG_VARIANT);
/* 169 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 174 */     if (type == DataComponents.FROG_VARIANT) {
/* 175 */       setVariant((Holder<FrogVariant>)castComponentValue(DataComponents.FROG_VARIANT, value));
/* 176 */       return true;
/*     */     } 
/* 179 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 184 */     super.addAdditionalSaveData(output);
/* 185 */     VariantUtils.writeVariant(output, getVariant());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 190 */     super.readAdditionalSaveData(input);
/* 191 */     VariantUtils.readVariant(input, Registries.FROG_VARIANT).ifPresent(this::setVariant);
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 196 */     ProfilerFiller profiler = Profiler.get();
/* 197 */     profiler.push("frogBrain");
/* 198 */     getBrain().tick(level, (LivingEntity)this);
/* 199 */     profiler.pop();
/* 201 */     profiler.push("frogActivityUpdate");
/* 202 */     FrogAi.updateActivity(this);
/* 203 */     profiler.pop();
/* 205 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 210 */     if (level().isClientSide())
/* 211 */       this.swimIdleAnimationState.animateWhen((isInWater() && !this.walkAnimation.isMoving()), this.tickCount); 
/* 214 */     super.tick();
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 219 */     if (DATA_POSE.equals(accessor)) {
/* 220 */       Pose pose = getPose();
/* 222 */       if (pose == Pose.LONG_JUMPING) {
/* 223 */         this.jumpAnimationState.start(this.tickCount);
/*     */       } else {
/* 225 */         this.jumpAnimationState.stop();
/*     */       } 
/* 228 */       if (pose == Pose.CROAKING) {
/* 229 */         this.croakAnimationState.start(this.tickCount);
/*     */       } else {
/* 231 */         this.croakAnimationState.stop();
/*     */       } 
/* 234 */       if (pose == Pose.USING_TONGUE) {
/* 235 */         this.tongueAnimationState.start(this.tickCount);
/*     */       } else {
/* 237 */         this.tongueAnimationState.stop();
/*     */       } 
/*     */     } 
/* 240 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   protected void updateWalkAnimation(float distance) {
/*     */     float targetSpeed;
/* 246 */     if (this.jumpAnimationState.isStarted()) {
/* 247 */       targetSpeed = 0.0F;
/*     */     } else {
/* 249 */       targetSpeed = Math.min(distance * 25.0F, 1.0F);
/*     */     } 
/* 251 */     this.walkAnimation.update(targetSpeed, 0.4F, isBaby() ? 3.0F : 1.0F);
/*     */   }
/*     */   
/*     */   public void playEatingSound() {
/* 256 */     level().playSound(null, (Entity)this, SoundEvents.FROG_EAT, SoundSource.NEUTRAL, 2.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 261 */     Frog frog = (Frog)EntityTypes.FROG.create((Level)level, EntitySpawnReason.BREEDING);
/* 262 */     if (frog != null)
/* 263 */       FrogAi.initMemories(frog, level.getRandom()); 
/* 266 */     return (AgeableMob)frog;
/*     */   }
/*     */   
/*     */   protected boolean canBeABaby() {
/* 271 */     return false;
/*     */   }
/*     */   
/*     */   public void spawnChildFromBreeding(ServerLevel level, Animal partner) {
/* 277 */     finalizeSpawnChildFromBreeding(level, partner, null);
/* 279 */     getBrain().setMemory(MemoryModuleType.IS_PREGNANT, Unit.INSTANCE);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 284 */     VariantUtils.selectVariantToSpawn(SpawnContext.create(level, blockPosition()), Registries.FROG_VARIANT).ifPresent(this::setVariant);
/* 286 */     FrogAi.initMemories(this, level.getRandom());
/* 288 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   private class FrogLookControl extends LookControl {
/*     */     public FrogLookControl(Mob mob) {
/* 293 */       super(mob);
/*     */     }
/*     */     
/*     */     protected boolean resetXRotOnTick() {
/* 298 */       return Frog.this.getTongueTarget().isEmpty();
/*     */     }
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 303 */     return Animal.createAnimalAttributes()
/* 304 */       .add(Attributes.MOVEMENT_SPEED, 1.0D)
/* 305 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 306 */       .add(Attributes.ATTACK_DAMAGE, 10.0D)
/* 307 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 312 */     return SoundEvents.FROG_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 317 */     return SoundEvents.FROG_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 322 */     return SoundEvents.FROG_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 327 */     playSound(SoundEvents.FROG_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 332 */     return false;
/*     */   }
/*     */   
/*     */   protected int calculateFallDamage(double fallDistance, float damageModifier) {
/* 337 */     return super.calculateFallDamage(fallDistance, damageModifier) - 5;
/*     */   }
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 342 */     moveRelative(getSpeed(), input);
/* 343 */     move(MoverType.SELF, getDeltaMovement());
/* 344 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */   }
/*     */   
/*     */   public static boolean canEat(LivingEntity entity) {
/* 349 */     if (entity instanceof AbstractCubeMob) {
/* 349 */       AbstractCubeMob cubeMob = (AbstractCubeMob)entity;
/* 349 */       if (cubeMob.getSize() != 1)
/* 350 */         return false; 
/*     */     } 
/* 352 */     return entity.is(EntityTypeTags.FROG_FOOD);
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 357 */     return (PathNavigation)new FrogPathNavigation(this, level);
/*     */   }
/*     */   
/*     */   public LivingEntity getTarget() {
/* 362 */     return getTargetFromBrain();
/*     */   }
/*     */   
/*     */   private static class FrogPathNavigation extends AmphibiousPathNavigation {
/*     */     public FrogPathNavigation(Frog mob, Level level) {
/* 367 */       super((Mob)mob, level);
/*     */     }
/*     */     
/*     */     public boolean canCutCorner(PathType pathType) {
/* 372 */       return (pathType != PathType.WATER_BORDER && super.canCutCorner(pathType));
/*     */     }
/*     */     
/*     */     protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 377 */       this.nodeEvaluator = (NodeEvaluator)new Frog.FrogNodeEvaluator(true);
/* 378 */       return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FrogNodeEvaluator extends AmphibiousNodeEvaluator {
/* 383 */     private final BlockPos.MutableBlockPos belowPos = new BlockPos.MutableBlockPos();
/*     */     
/*     */     public FrogNodeEvaluator(boolean prefersShallowSwimming) {
/* 386 */       super(prefersShallowSwimming);
/*     */     }
/*     */     
/*     */     public Node getStart() {
/* 391 */       if (!this.mob.isInWater())
/* 392 */         return super.getStart(); 
/* 394 */       return getStartNode(new BlockPos(Mth.floor((this.mob.getBoundingBox()).minX), Mth.floor((this.mob.getBoundingBox()).minY), Mth.floor((this.mob.getBoundingBox()).minZ)));
/*     */     }
/*     */     
/*     */     public PathType getPathType(PathfindingContext context, int x, int y, int z) {
/* 399 */       this.belowPos.set(x, y - 1, z);
/* 401 */       BlockState belowState = context.getBlockState((BlockPos)this.belowPos);
/* 402 */       if (belowState.is(BlockTags.FROG_PREFER_JUMP_TO))
/* 403 */         return PathType.OPEN; 
/* 406 */       return super.getPathType(context, x, y, z);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 412 */     return itemStack.is(ItemTags.FROG_FOOD);
/*     */   }
/*     */   
/*     */   public static boolean checkFrogSpawnRules(EntityType<? extends Animal> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 416 */     return (level.getBlockState(pos.below()).is(BlockTags.FROGS_SPAWNABLE_ON) && 
/* 417 */       isBrightEnoughToSpawn((BlockAndLightGetter)level, pos));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\frog\Frog.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */