/*     */ package net.minecraft.world.entity.animal.dolphin;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.StructureTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.BreathAirGoal;
/*     */ import net.minecraft.world.entity.ai.goal.DolphinJumpGoal;
/*     */ import net.minecraft.world.entity.ai.goal.FollowPlayerRiddenEntityGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.animal.AgeableWaterCreature;
/*     */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.Guardian;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Dolphin extends AgeableWaterCreature {
/*  77 */   private static final EntityDataAccessor<Boolean> GOT_FISH = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  78 */   private static final EntityDataAccessor<Integer> MOISTNESS_LEVEL = SynchedEntityData.defineId(Dolphin.class, EntityDataSerializers.INT);
/*     */   
/*  80 */   private static final TargetingConditions SWIM_WITH_PLAYER_TARGETING = TargetingConditions.forNonCombat().range(10.0D).ignoreLineOfSight();
/*     */   
/*     */   public static final int TOTAL_AIR_SUPPLY = 4800;
/*     */   
/*     */   private static final int TOTAL_MOISTNESS_LEVEL = 2400;
/*     */   
/*     */   public static final Predicate<ItemEntity> ALLOWED_ITEMS;
/*     */   
/*     */   public static final float BABY_SCALE = 0.65F;
/*     */   
/*     */   static {
/*  84 */     ALLOWED_ITEMS = (e -> (!e.hasPickUpDelay() && e.isAlive() && e.isInWater()));
/*     */   }
/*     */   
/*  86 */   private static final EntityDimensions BABY_DIMENSIONS = EntityTypes.DOLPHIN.getDimensions()
/*  87 */     .scale(0.65F)
/*  88 */     .withEyeHeight(0.09375F)
/*  89 */     .withAttachments(EntityAttachments.builder()
/*  90 */       .attach(EntityAttachment.PASSENGER, 0.0F, 0.3125F, 0.0F));
/*     */   
/*     */   private static final boolean DEFAULT_GOT_FISH = false;
/*     */   
/*     */   private BlockPos treasurePos;
/*     */   
/*     */   public Dolphin(EntityType<? extends Dolphin> type, Level level) {
/*  97 */     super(type, level);
/*  99 */     this.moveControl = (MoveControl)new SmoothSwimmingMoveControl((Mob)this, 85, 10, 0.02F, 0.1F, true);
/* 100 */     this.lookControl = (LookControl)new SmoothSwimmingLookControl((Mob)this, 10);
/* 102 */     setCanPickUpLoot(true);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 107 */     setAirSupply(getMaxAirSupply());
/* 108 */     setXRot(0.0F);
/* 110 */     SpawnGroupData spawnGroupData = Objects.<SpawnGroupData>requireNonNullElseGet(groupData, () -> new AgeableMob.AgeableMobGroupData(0.1F));
/* 112 */     return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
/*     */   }
/*     */   
/*     */   public Dolphin getBreedOffspring(ServerLevel level, AgeableMob partner) {
/* 117 */     return (Dolphin)EntityTypes.DOLPHIN.create((Level)level, EntitySpawnReason.BREEDING);
/*     */   }
/*     */   
/*     */   public float getAgeScale() {
/* 122 */     return isBaby() ? 0.65F : 1.0F;
/*     */   }
/*     */   
/*     */   protected void handleAirSupply(int preTickAirSupply) {}
/*     */   
/*     */   public boolean gotFish() {
/* 132 */     return ((Boolean)this.entityData.get(GOT_FISH)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setGotFish(boolean gotFish) {
/* 136 */     this.entityData.set(GOT_FISH, Boolean.valueOf(gotFish));
/*     */   }
/*     */   
/*     */   public int getMoistnessLevel() {
/* 140 */     return ((Integer)this.entityData.get(MOISTNESS_LEVEL)).intValue();
/*     */   }
/*     */   
/*     */   public void setMoisntessLevel(int level) {
/* 144 */     this.entityData.set(MOISTNESS_LEVEL, Integer.valueOf(level));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 149 */     super.defineSynchedData(entityData);
/* 150 */     entityData.define(GOT_FISH, Boolean.valueOf(false));
/* 151 */     entityData.define(MOISTNESS_LEVEL, Integer.valueOf(2400));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 156 */     super.addAdditionalSaveData(output);
/* 158 */     output.putBoolean("GotFish", gotFish());
/* 159 */     output.putInt("Moistness", getMoistnessLevel());
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 164 */     super.readAdditionalSaveData(input);
/* 165 */     setGotFish(input.getBooleanOr("GotFish", false));
/* 166 */     setMoisntessLevel(input.getIntOr("Moistness", 2400));
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 171 */     this.goalSelector.addGoal(0, (Goal)new BreathAirGoal((PathfinderMob)this));
/* 172 */     this.goalSelector.addGoal(0, (Goal)new TryFindWaterGoal((PathfinderMob)this));
/* 173 */     this.goalSelector.addGoal(1, new DolphinSwimToTreasureGoal(this));
/* 174 */     this.goalSelector.addGoal(2, new DolphinSwimWithPlayerGoal(this, 4.0D));
/* 175 */     this.goalSelector.addGoal(4, (Goal)new RandomSwimmingGoal((PathfinderMob)this, 1.0D, 10));
/* 176 */     this.goalSelector.addGoal(4, (Goal)new RandomLookAroundGoal((Mob)this));
/* 177 */     this.goalSelector.addGoal(5, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/* 178 */     this.goalSelector.addGoal(5, (Goal)new DolphinJumpGoal(this, 10));
/* 179 */     this.goalSelector.addGoal(6, (Goal)new MeleeAttackGoal((PathfinderMob)this, 1.2000000476837158D, true));
/* 180 */     this.goalSelector.addGoal(8, new PlayWithItemsGoal());
/* 181 */     this.goalSelector.addGoal(8, (Goal)new FollowPlayerRiddenEntityGoal((PathfinderMob)this, AbstractBoat.class));
/* 182 */     this.goalSelector.addGoal(8, (Goal)new FollowPlayerRiddenEntityGoal((PathfinderMob)this, AbstractNautilus.class));
/* 183 */     this.goalSelector.addGoal(9, (Goal)new AvoidEntityGoal((PathfinderMob)this, Guardian.class, 8.0F, 1.0D, 1.0D));
/* 185 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Guardian.class })).setAlertOthers(new Class[0]));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 189 */     return Mob.createMobAttributes()
/* 190 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 191 */       .add(Attributes.MOVEMENT_SPEED, 1.2000000476837158D)
/* 192 */       .add(Attributes.ATTACK_DAMAGE, 3.0D);
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/* 197 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, level);
/*     */   }
/*     */   
/*     */   public void playAttackSound() {
/* 202 */     playSound(SoundEvents.DOLPHIN_ATTACK, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean canAttack(LivingEntity target) {
/* 207 */     return (!isBaby() && super.canAttack(target));
/*     */   }
/*     */   
/*     */   public int getMaxAirSupply() {
/* 212 */     return 4800;
/*     */   }
/*     */   
/*     */   protected int increaseAirSupply(int currentSupply) {
/* 217 */     return getMaxAirSupply();
/*     */   }
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 222 */     return 1;
/*     */   }
/*     */   
/*     */   public int getMaxHeadYRot() {
/* 227 */     return 1;
/*     */   }
/*     */   
/*     */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot slot) {
/* 232 */     return (slot == EquipmentSlot.MAINHAND && canPickUpLoot());
/*     */   }
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 237 */     if (getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
/* 238 */       ItemStack itemStack = entity.getItem();
/* 239 */       if (canHoldItem(itemStack)) {
/* 240 */         onItemPickup(entity);
/* 241 */         setItemSlot(EquipmentSlot.MAINHAND, itemStack);
/* 242 */         setGuaranteedDrop(EquipmentSlot.MAINHAND);
/* 243 */         take((Entity)entity, itemStack.getCount());
/* 244 */         entity.discard();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 251 */     super.tick();
/* 253 */     if (isNoAi()) {
/* 255 */       setAirSupply(getMaxAirSupply());
/*     */       return;
/*     */     } 
/* 259 */     if (isInWaterOrRain()) {
/* 260 */       setMoisntessLevel(2400);
/*     */     } else {
/* 262 */       setMoisntessLevel(getMoistnessLevel() - 1);
/* 264 */       if (getMoistnessLevel() <= 0)
/* 265 */         hurt(damageSources().dryOut(), 1.0F); 
/* 268 */       if (onGround()) {
/* 269 */         setDeltaMovement(getDeltaMovement().add(((this.random
/* 270 */               .nextFloat() * 2.0F - 1.0F) * 0.2F), 0.5D, ((this.random
/*     */               
/* 272 */               .nextFloat() * 2.0F - 1.0F) * 0.2F)));
/* 274 */         setYRot(this.random.nextFloat() * 360.0F);
/* 275 */         setOnGround(false);
/* 276 */         this.needsSync = true;
/*     */       } 
/*     */     } 
/* 280 */     if (level().isClientSide() && isInWater() && getDeltaMovement().lengthSqr() > 0.03D) {
/* 281 */       Vec3 viewVector = getViewVector(0.0F);
/* 282 */       float c = Mth.cos((getYRot() * 0.017453292F)) * 0.3F;
/* 283 */       float s = Mth.sin((getYRot() * 0.017453292F)) * 0.3F;
/* 284 */       float multiplier = 1.2F - this.random.nextFloat() * 0.7F;
/* 285 */       for (int i = 0; i < 2; i++) {
/* 286 */         level().addParticle((ParticleOptions)ParticleTypes.DOLPHIN, getX() - viewVector.x * multiplier + c, getY() - viewVector.y, getZ() - viewVector.z * multiplier + s, 0.0D, 0.0D, 0.0D);
/* 287 */         level().addParticle((ParticleOptions)ParticleTypes.DOLPHIN, getX() - viewVector.x * multiplier - c, getY() - viewVector.y, getZ() - viewVector.z * multiplier - s, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 294 */     if (id == 38) {
/* 295 */       addParticlesAroundSelf((ParticleOptions)ParticleTypes.HAPPY_VILLAGER);
/*     */     } else {
/* 297 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addParticlesAroundSelf(ParticleOptions particle) {
/* 302 */     for (int i = 0; i < 7; i++) {
/* 303 */       double xa = this.random.nextGaussian() * 0.01D;
/* 304 */       double ya = this.random.nextGaussian() * 0.01D;
/* 305 */       double za = this.random.nextGaussian() * 0.01D;
/* 306 */       level().addParticle(particle, getRandomX(1.0D), getRandomY() + 0.2D, getRandomZ(1.0D), xa, ya, za);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 312 */     ItemStack itemStack = player.getItemInHand(hand);
/* 314 */     if (!itemStack.isEmpty() && itemStack.is(ItemTags.FISHES)) {
/* 315 */       if (!level().isClientSide())
/* 316 */         playSound(SoundEvents.DOLPHIN_EAT, 1.0F, 1.0F); 
/* 319 */       if (canAgeUp()) {
/* 320 */         itemStack.consume(1, (LivingEntity)player);
/* 322 */         ageUp(getSpeedUpSecondsWhenFeeding(-this.age), true);
/*     */       } else {
/* 324 */         setGotFish(true);
/* 326 */         itemStack.consume(1, (LivingEntity)player);
/*     */       } 
/* 329 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 332 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 337 */     return SoundEvents.DOLPHIN_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 342 */     return SoundEvents.DOLPHIN_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 347 */     return isInWater() ? SoundEvents.DOLPHIN_AMBIENT_WATER : SoundEvents.DOLPHIN_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSplashSound() {
/* 352 */     return SoundEvents.DOLPHIN_SPLASH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 357 */     return SoundEvents.DOLPHIN_SWIM;
/*     */   }
/*     */   
/*     */   protected boolean closeToNextPos() {
/* 361 */     BlockPos target = getNavigation().getTargetPos();
/* 362 */     if (target != null)
/* 363 */       return target.closerToCenterThan((Position)position(), 12.0D); 
/* 365 */     return false;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 370 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 375 */     moveRelative(getSpeed(), input);
/* 376 */     move(MoverType.SELF, getDeltaMovement());
/* 378 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/* 380 */     if (getTarget() == null)
/* 381 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D)); 
/*     */   }
/*     */   
/*     */   public boolean canBeLeashed() {
/* 387 */     return true;
/*     */   }
/*     */   
/*     */   private class PlayWithItemsGoal extends Goal {
/*     */     private int cooldown;
/*     */     
/*     */     public PlayWithItemsGoal() {
/* 394 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 399 */       if (this.cooldown > Dolphin.this.tickCount)
/* 400 */         return false; 
/* 402 */       List<ItemEntity> items = Dolphin.this.level().getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/* 403 */       return (!items.isEmpty() || !Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty());
/*     */     }
/*     */     
/*     */     public void start() {
/* 408 */       List<ItemEntity> items = Dolphin.this.level().getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/* 409 */       if (!items.isEmpty()) {
/* 410 */         Dolphin.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D);
/* 411 */         Dolphin.this.playSound(SoundEvents.DOLPHIN_PLAY, 1.0F, 1.0F);
/*     */       } 
/* 413 */       this.cooldown = 0;
/*     */     }
/*     */     
/*     */     public void stop() {
/* 418 */       ItemStack itemStack = Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 419 */       if (!itemStack.isEmpty()) {
/* 420 */         drop(itemStack);
/* 421 */         Dolphin.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 422 */         this.cooldown = Dolphin.this.tickCount + Dolphin.this.random.nextInt(100);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void tick() {
/* 428 */       List<ItemEntity> items = Dolphin.this.level().getEntitiesOfClass(ItemEntity.class, Dolphin.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), Dolphin.ALLOWED_ITEMS);
/* 430 */       ItemStack itemStack = Dolphin.this.getItemBySlot(EquipmentSlot.MAINHAND);
/* 431 */       if (!itemStack.isEmpty()) {
/* 432 */         drop(itemStack);
/* 433 */         Dolphin.this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/* 434 */       } else if (!items.isEmpty()) {
/* 435 */         Dolphin.this.getNavigation().moveTo((Entity)items.get(0), 1.2000000476837158D);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void drop(ItemStack itemStack) {
/* 440 */       if (itemStack.isEmpty())
/*     */         return; 
/* 444 */       double yHandPos = Dolphin.this.getEyeY() - 0.30000001192092896D;
/* 445 */       ItemEntity thrownItem = new ItemEntity(Dolphin.this.level(), Dolphin.this.getX(), yHandPos, Dolphin.this.getZ(), itemStack);
/* 446 */       thrownItem.setPickUpDelay(40);
/* 448 */       thrownItem.setThrower((Entity)Dolphin.this);
/* 450 */       float pow = 0.3F;
/* 451 */       float dir = Dolphin.this.random.nextFloat() * 6.2831855F;
/* 452 */       float pow2 = 0.02F * Dolphin.this.random.nextFloat();
/* 453 */       thrownItem.setDeltaMovement((0.3F * 
/* 454 */           -Mth.sin((Dolphin.this.getYRot() * 0.017453292F)) * Mth.cos((Dolphin.this.getXRot() * 0.017453292F)) + Mth.cos(dir) * pow2), (0.3F * 
/* 455 */           Mth.sin((Dolphin.this.getXRot() * 0.017453292F)) * 1.5F), (0.3F * 
/* 456 */           Mth.cos((Dolphin.this.getYRot() * 0.017453292F)) * Mth.cos((Dolphin.this.getXRot() * 0.017453292F)) + Mth.sin(dir) * pow2));
/* 459 */       Dolphin.this.level().addFreshEntity((Entity)thrownItem);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DolphinSwimWithPlayerGoal extends Goal {
/*     */     private final Dolphin dolphin;
/*     */     
/*     */     private final double speedModifier;
/*     */     
/*     */     private Player player;
/*     */     
/*     */     public DolphinSwimWithPlayerGoal(Dolphin dolphin, double speedModifier) {
/* 469 */       this.dolphin = dolphin;
/* 470 */       this.speedModifier = speedModifier;
/* 471 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 476 */       this.player = getServerLevel((Entity)this.dolphin).getNearestPlayer(Dolphin.SWIM_WITH_PLAYER_TARGETING, (LivingEntity)this.dolphin);
/* 477 */       if (this.player == null)
/* 478 */         return false; 
/* 480 */       return (this.player.isSwimming() && this.dolphin.getTarget() != this.player);
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 485 */       return (this.player != null && this.player.isSwimming() && this.dolphin.distanceToSqr((Entity)this.player) < 256.0D);
/*     */     }
/*     */     
/*     */     public void start() {
/* 490 */       this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), (Entity)this.dolphin);
/*     */     }
/*     */     
/*     */     public void stop() {
/* 495 */       this.player = null;
/* 496 */       this.dolphin.getNavigation().stop();
/*     */     }
/*     */     
/*     */     public void tick() {
/* 501 */       this.dolphin.getLookControl().setLookAt((Entity)this.player, (this.dolphin.getMaxHeadYRot() + 20), this.dolphin.getMaxHeadXRot());
/* 502 */       if (this.dolphin.distanceToSqr((Entity)this.player) < 6.25D) {
/* 503 */         this.dolphin.getNavigation().stop();
/*     */       } else {
/* 505 */         this.dolphin.getNavigation().moveTo((Entity)this.player, this.speedModifier);
/*     */       } 
/* 508 */       if (this.player.isSwimming() && this.player.level().getRandom().nextInt(6) == 0)
/* 509 */         this.player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 100), (Entity)this.dolphin); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DolphinSwimToTreasureGoal extends Goal {
/*     */     private final Dolphin dolphin;
/*     */     
/*     */     private boolean stuck;
/*     */     
/*     */     public DolphinSwimToTreasureGoal(Dolphin dolphin) {
/* 519 */       this.dolphin = dolphin;
/* 520 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */     
/*     */     public boolean isInterruptable() {
/* 525 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 530 */       return (this.dolphin.gotFish() && this.dolphin.getAirSupply() >= 100);
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 535 */       BlockPos treasurePos = this.dolphin.treasurePos;
/* 536 */       if (treasurePos == null)
/* 537 */         return false; 
/* 539 */       return (!BlockPos.containing(treasurePos.getX(), this.dolphin.getY(), treasurePos.getZ()).closerToCenterThan((Position)this.dolphin.position(), 4.0D) && !this.stuck && this.dolphin.getAirSupply() >= 100);
/*     */     }
/*     */     
/*     */     public void start() {
/* 544 */       if (!(this.dolphin.level() instanceof ServerLevel))
/*     */         return; 
/* 547 */       ServerLevel level = (ServerLevel)this.dolphin.level();
/* 548 */       this.stuck = false;
/* 549 */       this.dolphin.getNavigation().stop();
/* 551 */       BlockPos dolphinPos = this.dolphin.blockPosition();
/* 553 */       BlockPos treasurePos = level.findNearestMapStructure(StructureTags.DOLPHIN_LOCATED, dolphinPos, 50, false);
/* 554 */       if (treasurePos != null) {
/* 555 */         this.dolphin.treasurePos = treasurePos;
/*     */       } else {
/* 558 */         this.stuck = true;
/*     */         return;
/*     */       } 
/* 562 */       level.broadcastEntityEvent((Entity)this.dolphin, (byte)38);
/*     */     }
/*     */     
/*     */     public void stop() {
/* 567 */       BlockPos treasurePos = this.dolphin.treasurePos;
/* 568 */       if (treasurePos == null || BlockPos.containing(treasurePos.getX(), this.dolphin.getY(), treasurePos.getZ()).closerToCenterThan((Position)this.dolphin.position(), 4.0D) || this.stuck)
/* 569 */         this.dolphin.setGotFish(false); 
/*     */     }
/*     */     
/*     */     public void tick() {
/* 575 */       if (this.dolphin.treasurePos == null)
/*     */         return; 
/* 579 */       Level level = this.dolphin.level();
/* 581 */       if (this.dolphin.closeToNextPos() || this.dolphin.getNavigation().isDone()) {
/* 582 */         Vec3 treasurePos = Vec3.atCenterOf((Vec3i)this.dolphin.treasurePos);
/* 583 */         Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.dolphin, 16, 1, treasurePos, 0.39269909262657166D);
/* 584 */         if (nextPos == null)
/* 585 */           nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.dolphin, 8, 4, treasurePos, 1.5707963705062866D); 
/* 588 */         if (nextPos != null) {
/* 589 */           BlockPos next = BlockPos.containing((Position)nextPos);
/* 590 */           if (!level.getFluidState(next).is(FluidTags.WATER) || !level.getBlockState(next).isPathfindable(PathComputationType.WATER))
/* 591 */             nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.dolphin, 8, 5, treasurePos, 1.5707963705062866D); 
/*     */         } 
/* 595 */         if (nextPos == null) {
/* 596 */           this.stuck = true;
/*     */           return;
/*     */         } 
/* 600 */         this.dolphin.getLookControl().setLookAt(nextPos.x, nextPos.y, nextPos.z, (this.dolphin.getMaxHeadYRot() + 20), this.dolphin.getMaxHeadXRot());
/* 601 */         this.dolphin.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, 1.3D);
/* 603 */         if (level.getRandom().nextInt(adjustedTickDelay(80)) == 0)
/* 604 */           level.broadcastEntityEvent((Entity)this.dolphin, (byte)38); 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\dolphin\Dolphin.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */