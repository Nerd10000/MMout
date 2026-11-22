/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ 
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockAndLightGetter;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.TurtleEggBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Turtle extends Animal {
/*  69 */   private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  70 */   private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float BABY_SCALE = 0.3F;
/*     */   
/*  73 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.TURTLE.getDimensions()
/*  74 */     .withAttachments(EntityAttachments.builder()
/*  75 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityTypes.TURTLE.getHeight(), -0.25F))
/*     */     
/*  77 */     .scale(0.3F);
/*     */   
/*     */   private static final boolean DEFAULT_HAS_EGG = false;
/*     */   
/*     */   private int layEggCounter;
/*     */   
/*     */   public static final TargetingConditions.Selector BABY_ON_LAND_SELECTOR;
/*     */   
/*     */   static {
/*  83 */     BABY_ON_LAND_SELECTOR = ((target, level) -> (target.isBaby() && !target.isInWater()));
/*     */   }
/*     */   
/*  85 */   private BlockPos homePos = BlockPos.ZERO;
/*     */   
/*     */   private BlockPos travelPos;
/*     */   
/*     */   private boolean goingHome;
/*     */   
/*     */   public Turtle(EntityType<? extends Turtle> type, Level level) {
/*  90 */     super(type, level);
/*  92 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*  93 */     setPathfindingMalus(PathType.DOOR_IRON_CLOSED, -1.0F);
/*  94 */     setPathfindingMalus(PathType.DOOR_WOOD_CLOSED, -1.0F);
/*  95 */     setPathfindingMalus(PathType.DOOR_OPEN, -1.0F);
/*  96 */     this.moveControl = new TurtleMoveControl<>(this);
/*     */   }
/*     */   
/*     */   public void setHomePos(BlockPos pos) {
/* 100 */     this.homePos = pos;
/*     */   }
/*     */   
/*     */   public BlockPos getHomePos() {
/* 104 */     return this.homePos;
/*     */   }
/*     */   
/*     */   public boolean hasEgg() {
/* 108 */     return ((Boolean)this.entityData.get(HAS_EGG)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setHasEgg(boolean onOff) {
/* 112 */     this.entityData.set(HAS_EGG, Boolean.valueOf(onOff));
/*     */   }
/*     */   
/*     */   public boolean isLayingEgg() {
/* 116 */     return ((Boolean)this.entityData.get(LAYING_EGG)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setLayingEgg(boolean on) {
/* 120 */     this.layEggCounter = on ? 1 : 0;
/* 121 */     this.entityData.set(LAYING_EGG, Boolean.valueOf(on));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 126 */     super.defineSynchedData(entityData);
/* 127 */     entityData.define(HAS_EGG, Boolean.valueOf(false));
/* 128 */     entityData.define(LAYING_EGG, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 133 */     super.addAdditionalSaveData(output);
/* 135 */     output.store("home_pos", BlockPos.CODEC, this.homePos);
/* 137 */     output.putBoolean("has_egg", hasEgg());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 142 */     setHomePos(input.read("home_pos", BlockPos.CODEC).orElse(blockPosition()));
/* 144 */     super.readAdditionalSaveData(input);
/* 145 */     setHasEgg(input.getBooleanOr("has_egg", false));
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 150 */     setHomePos(blockPosition());
/* 151 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public static boolean checkTurtleSpawnRules(EntityType<Turtle> type, LevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 155 */     return (pos.getY() < level.getSeaLevel() + 4 && 
/* 156 */       TurtleEggBlock.onSand((BlockGetter)level, pos) && 
/* 157 */       isBrightEnoughToSpawn((BlockAndLightGetter)level, pos));
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 162 */     this.goalSelector.addGoal(0, (Goal)new TurtlePanicGoal(this, 1.2D));
/* 163 */     this.goalSelector.addGoal(1, (Goal)new TurtleBreedGoal(this, 1.0D));
/* 164 */     this.goalSelector.addGoal(1, (Goal)new TurtleLayEggGoal(this, 1.0D));
/* 165 */     this.goalSelector.addGoal(2, (Goal)new TemptGoal((PathfinderMob)this, 1.1D, i -> i.is(ItemTags.TURTLE_FOOD), false));
/* 166 */     this.goalSelector.addGoal(3, (Goal)new TurtleGoToWaterGoal(this, 1.0D));
/* 167 */     this.goalSelector.addGoal(4, new TurtleGoHomeGoal(this, 1.0D));
/* 168 */     this.goalSelector.addGoal(7, new TurtleTravelGoal(this, 1.0D));
/* 169 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 170 */     this.goalSelector.addGoal(9, (Goal)new TurtleRandomStrollGoal(this, 1.0D, 100));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 174 */     return Animal.createAnimalAttributes()
/* 175 */       .add(Attributes.MAX_HEALTH, 30.0D)
/* 176 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/* 177 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 182 */     return false;
/*     */   }
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 187 */     return 200;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 192 */     if (!isInWater() && onGround() && !isBaby())
/* 193 */       return SoundEvents.TURTLE_AMBIENT_LAND; 
/* 196 */     return super.getAmbientSound();
/*     */   }
/*     */   
/*     */   protected void playSwimSound(float volume) {
/* 201 */     super.playSwimSound(volume * 1.5F);
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 206 */     return SoundEvents.TURTLE_SWIM;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 211 */     if (isBaby())
/* 212 */       return SoundEvents.TURTLE_HURT_BABY; 
/* 214 */     return SoundEvents.TURTLE_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 219 */     if (isBaby())
/* 220 */       return SoundEvents.TURTLE_DEATH_BABY; 
/* 222 */     return SoundEvents.TURTLE_DEATH;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 227 */     SoundEvent sound = isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
/* 229 */     playSound(sound, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean canFallInLove() {
/* 234 */     return (super.canFallInLove() && !hasEgg());
/*     */   }
/*     */   
/*     */   protected float nextStep() {
/* 239 */     return this.moveDist + 0.15F;
/*     */   }
/*     */   
/*     */   public float getAgeScale() {
/* 244 */     return isBaby() ? 0.3F : 1.0F;
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 249 */     return (PathNavigation)new TurtlePathNavigation(this, level);
/*     */   }
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 254 */     return (AgeableMob)EntityTypes.TURTLE.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   public boolean isFood(ItemStack itemStack) {
/* 259 */     return itemStack.is(ItemTags.TURTLE_FOOD);
/*     */   }
/*     */   
/*     */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/* 264 */     if (!this.goingHome && level.getFluidState(pos).is(FluidTags.WATER))
/* 265 */       return 10.0F; 
/* 268 */     if (TurtleEggBlock.onSand((BlockGetter)level, pos))
/* 269 */       return 10.0F; 
/* 272 */     return level.getPathfindingCostFromLightLevels(pos);
/*     */   }
/*     */   
/*     */   public void aiStep() {
/* 277 */     super.aiStep();
/* 279 */     if (isAlive() && isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0) {
/* 280 */       BlockPos pos = blockPosition();
/* 281 */       if (TurtleEggBlock.onSand((BlockGetter)level(), pos)) {
/* 282 */         level().levelEvent(2001, pos, Block.getId(level().getBlockState(pos.below())));
/* 283 */         gameEvent((Holder)GameEvent.ENTITY_ACTION);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 290 */     super.ageBoundaryReached();
/* 292 */     if (!isBaby()) {
/* 292 */       Level level = level();
/* 292 */       if (level instanceof ServerLevel) {
/* 292 */         ServerLevel serverLevel = (ServerLevel)level;
/* 292 */         if (((Boolean)serverLevel.getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/* 293 */           dropFromGiftLootTable(serverLevel, BuiltInLootTables.TURTLE_GROW, this::spawnAtLocation); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 299 */     moveRelative(0.1F, input);
/* 300 */     move(MoverType.SELF, getDeltaMovement());
/* 302 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/* 303 */     if (getTarget() == null && (!this.goingHome || !this.homePos.closerToCenterThan((Position)position(), 20.0D)))
/* 304 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D)); 
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/* 310 */     return false;
/*     */   }
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/* 315 */     hurtServer(level, damageSources().lightningBolt(), Float.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 320 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   private static class TurtlePanicGoal extends PanicGoal {
/*     */     public TurtlePanicGoal(Turtle turtle, double speedModifier) {
/* 325 */       super((PathfinderMob)turtle, speedModifier);
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 330 */       if (!shouldPanic())
/* 331 */         return false; 
/* 334 */       BlockPos blockPos = lookForWater((BlockGetter)this.mob.level(), (Entity)this.mob, 7);
/* 335 */       if (blockPos != null) {
/* 336 */         this.posX = blockPos.getX();
/* 337 */         this.posY = blockPos.getY();
/* 338 */         this.posZ = blockPos.getZ();
/* 340 */         return true;
/*     */       } 
/* 343 */       return findRandomPosition();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleTravelGoal extends Goal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     private final double speedModifier;
/*     */     
/*     */     private boolean stuck;
/*     */     
/*     */     public TurtleTravelGoal(Turtle turtle, double speedModifier) {
/* 353 */       this.turtle = turtle;
/* 354 */       this.speedModifier = speedModifier;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 359 */       return (!this.turtle.goingHome && !this.turtle.hasEgg() && this.turtle.isInWater());
/*     */     }
/*     */     
/*     */     public void start() {
/* 364 */       int xzDist = 512;
/* 365 */       int yDist = 4;
/* 366 */       RandomSource random = this.turtle.random;
/* 367 */       int xt = random.nextInt(1025) - 512;
/* 368 */       int yt = random.nextInt(9) - 4;
/* 369 */       int zt = random.nextInt(1025) - 512;
/* 371 */       if (yt + this.turtle.getY() > (this.turtle.level().getSeaLevel() - 1))
/* 372 */         yt = 0; 
/* 374 */       this.turtle.travelPos = BlockPos.containing(xt + this.turtle.getX(), yt + this.turtle.getY(), zt + this.turtle.getZ());
/* 375 */       this.stuck = false;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 380 */       if (this.turtle.travelPos == null) {
/* 381 */         this.stuck = true;
/*     */         return;
/*     */       } 
/* 385 */       if (this.turtle.getNavigation().isDone()) {
/* 386 */         Vec3 targetPos = Vec3.atBottomCenterOf((Vec3i)this.turtle.travelPos);
/* 387 */         Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 3, targetPos, 0.3141592741012573D);
/* 388 */         if (nextPos == null)
/* 389 */           nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 8, 7, targetPos, 1.5707963705062866D); 
/* 393 */         if (nextPos != null) {
/* 394 */           int xc = Mth.floor(nextPos.x);
/* 395 */           int zc = Mth.floor(nextPos.z);
/* 396 */           int r = 34;
/* 397 */           if (!this.turtle.level().hasChunksAt(xc - 34, zc - 34, xc + 34, zc + 34))
/* 398 */             nextPos = null; 
/*     */         } 
/* 402 */         if (nextPos == null) {
/* 403 */           this.stuck = true;
/*     */           return;
/*     */         } 
/* 407 */         this.turtle.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 413 */       return (!this.turtle.getNavigation().isDone() && !this.stuck && !this.turtle.goingHome && !this.turtle.isInLove() && !this.turtle.hasEgg());
/*     */     }
/*     */     
/*     */     public void stop() {
/* 418 */       this.turtle.travelPos = null;
/* 419 */       super.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleGoHomeGoal extends Goal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     private final double speedModifier;
/*     */     
/*     */     private boolean stuck;
/*     */     
/*     */     private int closeToHomeTryTicks;
/*     */     
/*     */     private static final int GIVE_UP_TICKS = 600;
/*     */     
/*     */     public TurtleGoHomeGoal(Turtle turtle, double speedModifier) {
/* 431 */       this.turtle = turtle;
/* 432 */       this.speedModifier = speedModifier;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 437 */       if (this.turtle.isBaby())
/* 438 */         return false; 
/* 441 */       if (this.turtle.hasEgg())
/* 442 */         return true; 
/* 445 */       if (this.turtle.getRandom().nextInt(reducedTickDelay(700)) != 0)
/* 446 */         return false; 
/* 449 */       return !this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 64.0D);
/*     */     }
/*     */     
/*     */     public void start() {
/* 454 */       this.turtle.goingHome = true;
/* 455 */       this.stuck = false;
/* 456 */       this.closeToHomeTryTicks = 0;
/*     */     }
/*     */     
/*     */     public void stop() {
/* 461 */       this.turtle.goingHome = false;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 466 */       return (!this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 7.0D) && !this.stuck && this.closeToHomeTryTicks <= adjustedTickDelay(600));
/*     */     }
/*     */     
/*     */     public void tick() {
/* 471 */       BlockPos homePos = this.turtle.homePos;
/* 472 */       boolean closeToHome = homePos.closerToCenterThan((Position)this.turtle.position(), 16.0D);
/* 473 */       if (closeToHome)
/* 474 */         this.closeToHomeTryTicks++; 
/* 477 */       if (this.turtle.getNavigation().isDone()) {
/* 478 */         Vec3 homePosVec = Vec3.atBottomCenterOf((Vec3i)homePos);
/* 479 */         Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 3, homePosVec, 0.3141592741012573D);
/* 480 */         if (nextPos == null)
/* 481 */           nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 8, 7, homePosVec, 1.5707963705062866D); 
/* 484 */         if (nextPos != null && !closeToHome && !this.turtle.level().getBlockState(BlockPos.containing((Position)nextPos)).is(Blocks.WATER))
/* 486 */           nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 5, homePosVec, 1.5707963705062866D); 
/* 489 */         if (nextPos == null) {
/* 490 */           this.stuck = true;
/*     */           return;
/*     */         } 
/* 494 */         this.turtle.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleBreedGoal extends BreedGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     public TurtleBreedGoal(Turtle turtle, double speedModifier) {
/* 503 */       super(turtle, speedModifier);
/* 504 */       this.turtle = turtle;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 509 */       return (super.canUse() && !this.turtle.hasEgg());
/*     */     }
/*     */     
/*     */     protected void breed() {
/* 514 */       ServerPlayer loveCause = this.animal.getLoveCause();
/* 515 */       if (loveCause == null && this.partner.getLoveCause() != null)
/* 516 */         loveCause = this.partner.getLoveCause(); 
/* 519 */       if (loveCause != null) {
/* 520 */         loveCause.awardStat(Stats.ANIMALS_BRED);
/* 521 */         CriteriaTriggers.BRED_ANIMALS.trigger(loveCause, this.animal, this.partner, null);
/*     */       } 
/* 524 */       this.turtle.setHasEgg(true);
/* 525 */       this.animal.setAge(6000);
/* 526 */       this.partner.setAge(6000);
/* 527 */       this.animal.resetLove();
/* 528 */       this.partner.resetLove();
/* 530 */       RandomSource random = this.animal.getRandom();
/* 531 */       if (((Boolean)getServerLevel((Level)this.level).getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/* 532 */         this.level.addFreshEntity((Entity)new ExperienceOrb((Level)this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), random.nextInt(7) + 1)); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleLayEggGoal extends MoveToBlockGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     public TurtleLayEggGoal(Turtle turtle, double speedModifier) {
/* 541 */       super((PathfinderMob)turtle, speedModifier, 16);
/* 542 */       this.turtle = turtle;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 547 */       if (this.turtle.hasEgg() && this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 9.0D))
/* 548 */         return super.canUse(); 
/* 550 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 555 */       return (super.canContinueToUse() && this.turtle.hasEgg() && this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 9.0D));
/*     */     }
/*     */     
/*     */     public void tick() {
/* 560 */       super.tick();
/* 562 */       BlockPos turtlePos = this.turtle.blockPosition();
/* 563 */       if (!this.turtle.isInWater() && isReachedTarget()) {
/* 564 */         if (this.turtle.layEggCounter < 1) {
/* 565 */           this.turtle.setLayingEgg(true);
/* 566 */         } else if (this.turtle.layEggCounter > adjustedTickDelay(200)) {
/* 567 */           Level level = this.turtle.level();
/* 568 */           level.playSound(null, turtlePos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.getRandom().nextFloat() * 0.2F);
/* 569 */           BlockPos eggPos = this.blockPos.above();
/* 570 */           BlockState eggState = (BlockState)Blocks.TURTLE_EGG.defaultBlockState().setValue((Property)TurtleEggBlock.EGGS, Integer.valueOf(this.turtle.random.nextInt(4) + 1));
/* 571 */           level.setBlock(eggPos, eggState, 3);
/* 572 */           level.gameEvent((Holder)GameEvent.BLOCK_PLACE, eggPos, GameEvent.Context.of((Entity)this.turtle, eggState));
/* 573 */           this.turtle.setHasEgg(false);
/* 574 */           this.turtle.setLayingEgg(false);
/* 575 */           this.turtle.setInLoveTime(600);
/*     */         } 
/* 577 */         if (this.turtle.isLayingEgg())
/* 578 */           this.turtle.layEggCounter++; 
/*     */       } 
/*     */     }
/*     */     
/*     */     protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 585 */       if (!level.isEmptyBlock(pos.above()))
/* 586 */         return false; 
/* 589 */       return TurtleEggBlock.isSand((BlockGetter)level, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleRandomStrollGoal extends RandomStrollGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     private TurtleRandomStrollGoal(Turtle turtle, double speedModifier, int interval) {
/* 597 */       super((PathfinderMob)turtle, speedModifier, interval);
/* 598 */       this.turtle = turtle;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 603 */       if (!this.mob.isInWater() && !this.turtle.goingHome && !this.turtle.hasEgg())
/* 604 */         return super.canUse(); 
/* 607 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleGoToWaterGoal extends MoveToBlockGoal {
/*     */     private static final int GIVE_UP_TICKS = 1200;
/*     */     
/*     */     private final Turtle turtle;
/*     */     
/*     */     private TurtleGoToWaterGoal(Turtle turtle, double speedModifier) {
/* 617 */       super((PathfinderMob)turtle, turtle.isBaby() ? 2.0D : speedModifier, 24);
/* 618 */       this.turtle = turtle;
/* 619 */       this.verticalSearchStart = -1;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 624 */       return (!this.turtle.isInWater() && this.tryTicks <= 1200 && isValidTarget((LevelReader)this.turtle.level(), this.blockPos));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 629 */       if (this.turtle.isBaby() && !this.turtle.isInWater())
/* 630 */         return super.canUse(); 
/* 633 */       if (!this.turtle.goingHome && !this.turtle.isInWater() && !this.turtle.hasEgg())
/* 634 */         return super.canUse(); 
/* 637 */       return false;
/*     */     }
/*     */     
/*     */     public boolean shouldRecalculatePath() {
/* 642 */       return (this.tryTicks % 160 == 0);
/*     */     }
/*     */     
/*     */     protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 647 */       return level.getBlockState(pos).is(Blocks.WATER);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleMoveControl<T extends Turtle> extends MoveControl<T> {
/*     */     public TurtleMoveControl(T turtle) {
/* 654 */       super((Mob)turtle);
/*     */     }
/*     */     
/*     */     private void updateSpeed() {
/* 658 */       if (((Turtle)this.mob).isInWater()) {
/* 660 */         ((Turtle)this.mob).setDeltaMovement(((Turtle)this.mob).getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/* 662 */         if (!((Turtle)this.mob).getHomePos().closerToCenterThan((Position)((Turtle)this.mob).position(), 16.0D))
/* 663 */           ((Turtle)this.mob).setSpeed(Math.max(((Turtle)this.mob).getSpeed() / 2.0F, 0.08F)); 
/* 666 */         if (((Turtle)this.mob).isBaby())
/* 667 */           ((Turtle)this.mob).setSpeed(Math.max(((Turtle)this.mob).getSpeed() / 3.0F, 0.06F)); 
/* 669 */       } else if (((Turtle)this.mob).onGround()) {
/* 670 */         ((Turtle)this.mob).setSpeed(Math.max(((Turtle)this.mob).getSpeed() / 2.0F, 0.06F));
/*     */       } 
/*     */     }
/*     */     
/*     */     public void tick() {
/* 676 */       updateSpeed();
/* 678 */       if (this.operation != MoveControl.Operation.MOVE_TO || ((Turtle)this.mob).getNavigation().isDone()) {
/* 679 */         ((Turtle)this.mob).setSpeed(0.0F);
/*     */         return;
/*     */       } 
/* 683 */       double xd = this.wantedX - ((Turtle)this.mob).getX();
/* 684 */       double yd = this.wantedY - ((Turtle)this.mob).getY();
/* 685 */       double zd = this.wantedZ - ((Turtle)this.mob).getZ();
/* 686 */       double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
/* 687 */       if (dd < 9.999999747378752E-6D) {
/* 688 */         ((Turtle)this.mob).setSpeed(0.0F);
/*     */         return;
/*     */       } 
/* 692 */       yd /= dd;
/* 694 */       float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 695 */       ((Turtle)this.mob).setYRot(rotlerp(((Turtle)this.mob).getYRot(), yRotD, 90.0F));
/* 696 */       ((Turtle)this.mob).yBodyRot = ((Turtle)this.mob).getYRot();
/* 698 */       float targetSpeed = (float)(this.speedModifier * ((Turtle)this.mob).getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 699 */       ((Turtle)this.mob).setSpeed(Mth.lerp(0.125F, ((Turtle)this.mob).getSpeed(), targetSpeed));
/* 701 */       ((Turtle)this.mob).setDeltaMovement(((Turtle)this.mob).getDeltaMovement().add(0.0D, ((Turtle)this.mob).getSpeed() * yd * 0.1D, 0.0D));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtlePathNavigation extends AmphibiousPathNavigation {
/*     */     public TurtlePathNavigation(Turtle mob, Level level) {
/* 707 */       super((Mob)mob, level);
/*     */     }
/*     */     
/*     */     public boolean isStableDestination(BlockPos pos) {
/* 712 */       Mob mob = this.mob;
/* 712 */       if (mob instanceof Turtle) {
/* 712 */         Turtle turtle = (Turtle)mob;
/* 713 */         if (turtle.travelPos != null)
/* 714 */           return this.level.getBlockState(pos).is(Blocks.WATER); 
/*     */       } 
/* 718 */       return !this.level.getBlockState(pos.below()).isAir();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\turtle\Turtle.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */