/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.SpecialDates;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.SpearUseGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.npc.villager.Villager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Zombie extends Monster {
/*  75 */   private static final Identifier SPEED_MODIFIER_BABY_ID = Identifier.withDefaultNamespace("baby");
/*     */   
/*  76 */   private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_ID, 0.5D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
/*     */   
/*  77 */   private static final Identifier REINFORCEMENT_CALLER_CHARGE_ID = Identifier.withDefaultNamespace("reinforcement_caller_charge");
/*     */   
/*  78 */   private static final AttributeModifier ZOMBIE_REINFORCEMENT_CALLEE_CHARGE = new AttributeModifier(Identifier.withDefaultNamespace("reinforcement_callee_charge"), -0.05000000074505806D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*  79 */   private static final Identifier LEADER_ZOMBIE_BONUS_ID = Identifier.withDefaultNamespace("leader_zombie_bonus");
/*     */   
/*  80 */   private static final Identifier ZOMBIE_RANDOM_SPAWN_BONUS_ID = Identifier.withDefaultNamespace("zombie_random_spawn_bonus");
/*     */   
/*  82 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  83 */   private static final EntityDataAccessor<Integer> DATA_SPECIAL_TYPE_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.INT);
/*     */   
/*  84 */   private static final EntityDataAccessor<Boolean> DATA_DROWNED_CONVERSION_ID = SynchedEntityData.defineId(Zombie.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final float ZOMBIE_LEADER_CHANCE = 0.05F;
/*     */   
/*     */   public static final int REINFORCEMENT_ATTEMPTS = 50;
/*     */   
/*     */   public static final int REINFORCEMENT_RANGE_MAX = 40;
/*     */   
/*     */   public static final int REINFORCEMENT_RANGE_MIN = 7;
/*     */   
/*     */   private static final int NOT_CONVERTING = -1;
/*     */   
/*  91 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.49F, 0.98F)
/*  92 */     .withEyeHeight(0.775F)
/*  93 */     .withAttachments(EntityAttachments.builder()
/*  94 */       .attach(EntityAttachment.VEHICLE, 0.0F, 0.1875F, 0.0F));
/*     */   
/*     */   private static final float BREAK_DOOR_CHANCE = 0.1F;
/*     */   
/*     */   private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE;
/*     */   
/*     */   private static final boolean DEFAULT_BABY = false;
/*     */   
/*     */   private static final boolean DEFAULT_CAN_BREAK_DOORS = false;
/*     */   
/*     */   private static final int DEFAULT_IN_WATER_TIME = 0;
/*     */   
/*     */   static {
/*  97 */     DOOR_BREAKING_PREDICATE = (d -> (d == Difficulty.HARD));
/*     */   }
/*     */   
/* 101 */   private final BreakDoorGoal breakDoorGoal = new BreakDoorGoal((Mob)this, DOOR_BREAKING_PREDICATE);
/*     */   
/*     */   private boolean canBreakDoors = false;
/*     */   
/* 104 */   private int inWaterTime = 0;
/*     */   
/*     */   private int conversionTime;
/*     */   
/*     */   public Zombie(EntityType<? extends Zombie> type, Level level) {
/* 108 */     super(type, level);
/*     */   }
/*     */   
/*     */   public Zombie(Level level) {
/* 112 */     this(EntityTypes.ZOMBIE, level);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/* 117 */     this.goalSelector.addGoal(4, (Goal)new ZombieAttackTurtleEggGoal((PathfinderMob)this, 1.0D, 3));
/* 118 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 119 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/* 121 */     addBehaviourGoals();
/*     */   }
/*     */   
/*     */   protected void addBehaviourGoals() {
/* 125 */     this.goalSelector.addGoal(2, (Goal)new SpearUseGoal(this, 1.0D, 1.0D, 10.0F, 2.0F));
/* 126 */     this.goalSelector.addGoal(3, (Goal)new ZombieAttackGoal(this, 1.0D, false));
/* 127 */     this.goalSelector.addGoal(6, (Goal)new MoveThroughVillageGoal((PathfinderMob)this, 1.0D, true, 4, this::canBreakDoors));
/* 128 */     this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal((PathfinderMob)this, 1.0D));
/* 130 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[0])).setAlertOthers(new Class[] { ZombifiedPiglin.class }));
/* 131 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, true));
/* 132 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false));
/* 133 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/* 134 */     this.targetSelector.addGoal(5, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 138 */     return Monster.createMonsterAttributes()
/* 139 */       .add(Attributes.FOLLOW_RANGE, 35.0D)
/* 140 */       .add(Attributes.MOVEMENT_SPEED, 0.23000000417232513D)
/* 141 */       .add(Attributes.ATTACK_DAMAGE, 3.0D)
/* 142 */       .add(Attributes.ARMOR, 2.0D)
/* 143 */       .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 148 */     super.defineSynchedData(entityData);
/* 150 */     entityData.define(DATA_BABY_ID, Boolean.valueOf(false));
/* 151 */     entityData.define(DATA_SPECIAL_TYPE_ID, Integer.valueOf(0));
/* 152 */     entityData.define(DATA_DROWNED_CONVERSION_ID, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean isUnderWaterConverting() {
/* 156 */     return ((Boolean)getEntityData().get(DATA_DROWNED_CONVERSION_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean canBreakDoors() {
/* 160 */     return this.canBreakDoors;
/*     */   }
/*     */   
/*     */   public void setCanBreakDoors(boolean canBreakDoors) {
/* 164 */     if (this.navigation.canNavigateGround()) {
/* 165 */       if (this.canBreakDoors != canBreakDoors) {
/* 166 */         this.canBreakDoors = canBreakDoors;
/* 167 */         this.navigation.setCanOpenDoors(canBreakDoors);
/* 169 */         if (canBreakDoors) {
/* 170 */           this.goalSelector.addGoal(1, (Goal)this.breakDoorGoal);
/*     */         } else {
/* 172 */           this.goalSelector.removeGoal((Goal)this.breakDoorGoal);
/*     */         } 
/*     */       } 
/* 176 */     } else if (this.canBreakDoors) {
/* 177 */       this.goalSelector.removeGoal((Goal)this.breakDoorGoal);
/* 178 */       this.canBreakDoors = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isBaby() {
/* 185 */     return ((Boolean)getEntityData().get(DATA_BABY_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   protected int getBaseExperienceReward(ServerLevel level) {
/* 190 */     if (isBaby())
/* 191 */       this.xpReward = (int)(this.xpReward * 2.5D); 
/* 194 */     return super.getBaseExperienceReward(level);
/*     */   }
/*     */   
/*     */   public void setBaby(boolean baby) {
/* 199 */     getEntityData().set(DATA_BABY_ID, Boolean.valueOf(baby));
/* 201 */     if (level() != null && !level().isClientSide()) {
/* 202 */       AttributeInstance speed = getAttribute(Attributes.MOVEMENT_SPEED);
/* 203 */       speed.removeModifier(SPEED_MODIFIER_BABY_ID);
/* 204 */       if (baby)
/* 205 */         speed.addTransientModifier(SPEED_MODIFIER_BABY); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
/* 212 */     if (DATA_BABY_ID.equals(accessor))
/* 213 */       refreshDimensions(); 
/* 216 */     super.onSyncedDataUpdated(accessor);
/*     */   }
/*     */   
/*     */   protected boolean convertsInWater() {
/* 220 */     return true;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 225 */     Level level = level();
/* 225 */     if (level instanceof ServerLevel) {
/* 225 */       ServerLevel serverLevel = (ServerLevel)level;
/* 225 */       if (isAlive() && !isNoAi())
/* 226 */         if (isUnderWaterConverting()) {
/* 227 */           this.conversionTime--;
/* 229 */           if (this.conversionTime < 0)
/* 230 */             doUnderWaterConversion(serverLevel); 
/* 232 */         } else if (convertsInWater()) {
/* 233 */           if (isEyeInFluid(FluidTags.WATER)) {
/* 234 */             this.inWaterTime++;
/* 236 */             if (this.inWaterTime >= 600)
/* 237 */               startUnderWaterConversion(300); 
/*     */           } else {
/* 240 */             this.inWaterTime = -1;
/*     */           } 
/*     */         }  
/*     */     } 
/* 245 */     super.tick();
/*     */   }
/*     */   
/*     */   private void startUnderWaterConversion(int time) {
/* 249 */     this.conversionTime = time;
/* 250 */     getEntityData().set(DATA_DROWNED_CONVERSION_ID, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */   protected void doUnderWaterConversion(ServerLevel level) {
/* 255 */     convertToZombieType(level, EntityTypes.DROWNED);
/* 256 */     if (!isSilent())
/* 257 */       level.levelEvent(null, 1040, blockPosition(), 0); 
/*     */   }
/*     */   
/*     */   protected void convertToZombieType(ServerLevel level, EntityType<? extends Zombie> zombieType) {
/* 262 */     convertTo(zombieType, ConversionParams.single((Mob)this, true, true), newZombie -> newZombie.handleAttributes(level.getCurrentDifficultyAt(newZombie.blockPosition()).getSpecialMultiplier(), EntitySpawnReason.CONVERSION));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public boolean convertVillagerToZombieVillager(ServerLevel level, Villager villager) {
/* 269 */     ZombieVillager zombieVillager = (ZombieVillager)villager.convertTo(EntityTypes.ZOMBIE_VILLAGER, ConversionParams.single((Mob)villager, true, true), zombie -> {
/*     */           zombie.setVillagerDataFinalized(villager.getVillagerDataFinalized());
/*     */           zombie.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(zombie.blockPosition()), EntitySpawnReason.CONVERSION, new ZombieGroupData(false, true));
/*     */           zombie.setVillagerData(villager.getVillagerData());
/*     */           zombie.setGossips(villager.getGossips().copy());
/*     */           zombie.setTradeOffers(villager.getOffers().copy());
/*     */           zombie.setVillagerXp(villager.getVillagerXp());
/*     */           if (!isSilent())
/*     */             level.levelEvent(null, 1026, blockPosition(), 0); 
/*     */         });
/* 282 */     return (zombieVillager != null);
/*     */   }
/*     */   
/*     */   protected boolean isSunSensitive() {
/* 286 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 291 */     if (!super.hurtServer(level, source, damage))
/* 292 */       return false; 
/* 295 */     LivingEntity target = getTarget();
/* 296 */     if (target == null && source.getEntity() instanceof LivingEntity)
/* 297 */       target = (LivingEntity)source.getEntity(); 
/* 300 */     if (target != null && level.getDifficulty() == Difficulty.HARD && this.random.nextFloat() < getAttributeValue(Attributes.SPAWN_REINFORCEMENTS_CHANCE) && level.isSpawningMonsters()) {
/* 301 */       int x = Mth.floor(getX());
/* 302 */       int y = Mth.floor(getY());
/* 303 */       int z = Mth.floor(getZ());
/* 304 */       EntityType<? extends Zombie> type = getType();
/* 305 */       Zombie reinforcement = (Zombie)type.create((Level)level, EntitySpawnReason.REINFORCEMENT);
/* 306 */       if (reinforcement == null)
/* 307 */         return true; 
/* 309 */       for (int i = 0; i < 50; i++) {
/* 310 */         int xt = x + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
/* 311 */         int yt = y + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
/* 312 */         int zt = z + Mth.nextInt(this.random, 7, 40) * Mth.nextInt(this.random, -1, 1);
/* 314 */         BlockPos spawnPos = new BlockPos(xt, yt, zt);
/* 316 */         if (SpawnPlacements.isSpawnPositionOk(type, (LevelReader)level, spawnPos) && 
/* 317 */           SpawnPlacements.checkSpawnRules(type, (ServerLevelAccessor)level, EntitySpawnReason.REINFORCEMENT, spawnPos, level.getRandom())) {
/* 319 */           reinforcement.setPos(xt, yt, zt);
/* 322 */           if (!level.hasNearbyAlivePlayer(xt, yt, zt, 7.0D) && level.isUnobstructed((Entity)reinforcement) && level.noCollision((Entity)reinforcement) && (reinforcement.canSpawnInLiquids() || !level.containsAnyLiquid(reinforcement.getBoundingBox()))) {
/* 323 */             reinforcement.setTarget(target);
/* 324 */             reinforcement.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(reinforcement.blockPosition()), EntitySpawnReason.REINFORCEMENT, (SpawnGroupData)null);
/* 325 */             level.addFreshEntityWithPassengers((Entity)reinforcement);
/* 327 */             AttributeInstance attribute = getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
/* 328 */             AttributeModifier modifier = attribute.getModifier(REINFORCEMENT_CALLER_CHARGE_ID);
/* 329 */             double existingAmount = (modifier != null) ? modifier.amount() : 0.0D;
/* 330 */             attribute.removeModifier(REINFORCEMENT_CALLER_CHARGE_ID);
/* 331 */             attribute.addPermanentModifier(new AttributeModifier(REINFORCEMENT_CALLER_CHARGE_ID, existingAmount - 0.05D, AttributeModifier.Operation.ADD_VALUE));
/* 332 */             reinforcement.getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addPermanentModifier(ZOMBIE_REINFORCEMENT_CALLEE_CHARGE);
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 339 */     return true;
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/* 344 */     boolean result = super.doHurtTarget(level, target);
/* 346 */     if (result) {
/* 347 */       float difficulty = level.getCurrentDifficultyAt(blockPosition()).getEffectiveDifficulty();
/* 350 */       if (getMainHandItem().isEmpty() && 
/* 351 */         isOnFire() && this.random.nextFloat() < difficulty * 0.3F)
/* 352 */         target.igniteForSeconds((2 * (int)difficulty)); 
/*     */     } 
/* 357 */     return result;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 362 */     return SoundEvents.ZOMBIE_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 367 */     return SoundEvents.ZOMBIE_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 372 */     return SoundEvents.ZOMBIE_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getStepSound() {
/* 376 */     return SoundEvents.ZOMBIE_STEP;
/*     */   }
/*     */   
/*     */   protected void playStepSound(BlockPos pos, BlockState blockState) {
/* 381 */     playSound(getStepSound(), 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   public EntityType<? extends Zombie> getType() {
/* 386 */     return super.getType();
/*     */   }
/*     */   
/*     */   protected boolean canSpawnInLiquids() {
/* 390 */     return false;
/*     */   }
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 395 */     super.populateDefaultEquipmentSlots(random, difficulty);
/* 397 */     if (random.nextFloat() < ((level().getDifficulty() == Difficulty.HARD) ? 0.05F : 0.01F)) {
/* 398 */       int rand = random.nextInt(6);
/* 399 */       if (rand == 0) {
/* 400 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SWORD));
/* 401 */       } else if (rand == 1) {
/* 402 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SPEAR));
/*     */       } else {
/* 404 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.IRON_SHOVEL));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 411 */     super.addAdditionalSaveData(output);
/* 413 */     output.putBoolean("IsBaby", isBaby());
/* 414 */     output.putBoolean("CanBreakDoors", canBreakDoors());
/* 416 */     output.putInt("InWaterTime", isInWater() ? this.inWaterTime : -1);
/* 417 */     output.putInt("DrownedConversionTime", isUnderWaterConverting() ? this.conversionTime : -1);
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 422 */     super.readAdditionalSaveData(input);
/* 424 */     setBaby(input.getBooleanOr("IsBaby", false));
/* 425 */     setCanBreakDoors(input.getBooleanOr("CanBreakDoors", false));
/* 427 */     this.inWaterTime = input.getIntOr("InWaterTime", 0);
/* 429 */     int conversionTime = input.getIntOr("DrownedConversionTime", -1);
/* 430 */     if (conversionTime != -1) {
/* 431 */       startUnderWaterConversion(conversionTime);
/*     */     } else {
/* 433 */       getEntityData().set(DATA_DROWNED_CONVERSION_ID, Boolean.valueOf(false));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean killedEntity(ServerLevel level, LivingEntity entity, DamageSource source) {
/* 439 */     boolean perished = super.killedEntity(level, entity, source);
/* 441 */     if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && entity instanceof Villager) {
/* 441 */       Villager villager = (Villager)entity;
/* 442 */       if (level.getDifficulty() != Difficulty.HARD && this.random.nextBoolean())
/* 443 */         return perished; 
/* 446 */       if (convertVillagerToZombieVillager(level, villager))
/* 447 */         perished = false; 
/*     */     } 
/* 450 */     return perished;
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 455 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public boolean canHoldItem(ItemStack itemStack) {
/* 460 */     if (itemStack.is(ItemTags.EGGS) && isBaby() && isPassenger())
/* 461 */       return false; 
/* 463 */     return super.canHoldItem(itemStack);
/*     */   }
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 468 */     if (itemStack.is(Items.GLOW_INK_SAC))
/* 469 */       return false; 
/* 471 */     return super.wantsToPickUp(level, itemStack);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 476 */     RandomSource random = level.getRandom();
/* 478 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/* 479 */     float difficultyModifier = difficulty.getSpecialMultiplier();
/* 481 */     if (spawnReason != EntitySpawnReason.CONVERSION)
/* 482 */       setCanPickUpLoot((random.nextFloat() < 0.55F * difficultyModifier)); 
/* 485 */     if (groupData == null)
/* 486 */       groupData = new ZombieGroupData(getSpawnAsBabyOdds(random), true); 
/* 489 */     if (groupData instanceof ZombieGroupData) {
/* 489 */       ZombieGroupData zombieData = (ZombieGroupData)groupData;
/* 491 */       if (zombieData.isBaby) {
/* 492 */         setBaby(true);
/* 494 */         if (zombieData.canSpawnJockey)
/* 495 */           if (random.nextFloat() < 0.05D) {
/* 496 */             List<Chicken> chickens = level.getEntitiesOfClass(Chicken.class, getBoundingBox().inflate(5.0D, 3.0D, 5.0D), EntitySelector.ENTITY_NOT_BEING_RIDDEN);
/* 498 */             if (!chickens.isEmpty()) {
/* 499 */               Chicken chicken = chickens.get(0);
/* 500 */               chicken.setChickenJockey(true);
/* 501 */               startRiding((Entity)chicken, false, false);
/*     */             } 
/* 503 */           } else if (random.nextFloat() < 0.05D) {
/* 504 */             Chicken chicken = (Chicken)EntityTypes.CHICKEN.create(level(), EntitySpawnReason.JOCKEY);
/* 505 */             if (chicken != null) {
/* 506 */               chicken.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 507 */               chicken.finalizeSpawn(level, difficulty, EntitySpawnReason.JOCKEY, null);
/* 508 */               chicken.setChickenJockey(true);
/* 509 */               startRiding((Entity)chicken, false, false);
/* 513 */               level.addFreshEntity((Entity)chicken);
/*     */             } 
/*     */           }  
/*     */       } 
/* 519 */       setCanBreakDoors((random.nextFloat() < difficultyModifier * 0.1F));
/* 521 */       if (spawnReason != EntitySpawnReason.CONVERSION) {
/* 522 */         populateDefaultEquipmentSlots(random, difficulty);
/* 523 */         populateDefaultEquipmentEnchantments(level, random, difficulty);
/*     */       } 
/*     */     } 
/* 527 */     if (getItemBySlot(EquipmentSlot.HEAD).isEmpty() && 
/* 528 */       SpecialDates.isHalloween() && random.nextFloat() < 0.25F) {
/* 530 */       setItemSlot(EquipmentSlot.HEAD, new ItemStack((random.nextFloat() < 0.1F) ? (ItemLike)Blocks.JACK_O_LANTERN : (ItemLike)Blocks.CARVED_PUMPKIN));
/* 531 */       setDropChance(EquipmentSlot.HEAD, 0.0F);
/*     */     } 
/* 535 */     handleAttributes(difficultyModifier, spawnReason);
/* 537 */     return groupData;
/*     */   }
/*     */   
/*     */   protected void onOffspringSpawnedFromEgg(Player spawner, Mob offspring) {
/* 542 */     Level level = level();
/* 542 */     if (level instanceof ServerLevel) {
/* 542 */       ServerLevel serverLevel = (ServerLevel)level;
/* 543 */       float difficultyModifier = serverLevel.getCurrentDifficultyAt(offspring.blockPosition()).getSpecialMultiplier();
/* 544 */       offspring.setCanPickUpLoot((this.random.nextFloat() < 0.55F * difficultyModifier));
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setInWaterTime(int inWaterTime) {
/* 550 */     this.inWaterTime = inWaterTime;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setConversionTime(int conversionTime) {
/* 555 */     this.conversionTime = conversionTime;
/*     */   }
/*     */   
/*     */   public static boolean getSpawnAsBabyOdds(RandomSource random) {
/* 559 */     return (random.nextFloat() < 0.05F);
/*     */   }
/*     */   
/*     */   protected void handleAttributes(float difficultyModifier, EntitySpawnReason spawnReason) {
/* 563 */     randomizeReinforcementsChance();
/* 564 */     getAttribute(Attributes.KNOCKBACK_RESISTANCE).addOrReplacePermanentModifier(new AttributeModifier(RANDOM_SPAWN_BONUS_ID, this.random.nextDouble() * 0.05000000074505806D, AttributeModifier.Operation.ADD_VALUE));
/* 565 */     double followRangeModifier = this.random.nextDouble() * 1.5D * difficultyModifier;
/* 566 */     if (followRangeModifier > 1.0D)
/* 567 */       getAttribute(Attributes.FOLLOW_RANGE).addOrReplacePermanentModifier(new AttributeModifier(ZOMBIE_RANDOM_SPAWN_BONUS_ID, followRangeModifier, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)); 
/* 570 */     if (this.random.nextFloat() < difficultyModifier * 0.05F) {
/* 571 */       getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, this.random.nextDouble() * 0.25D + 0.5D, AttributeModifier.Operation.ADD_VALUE));
/* 572 */       getAttribute(Attributes.MAX_HEALTH).addOrReplacePermanentModifier(new AttributeModifier(LEADER_ZOMBIE_BONUS_ID, this.random.nextDouble() * 3.0D + 1.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
/* 573 */       if (spawnReason != EntitySpawnReason.CONVERSION && spawnReason != EntitySpawnReason.LOAD && spawnReason != EntitySpawnReason.DIMENSION_TRAVEL)
/* 574 */         setHealth(getMaxHealth()); 
/* 576 */       setCanBreakDoors(true);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void randomizeReinforcementsChance() {
/* 581 */     getAttribute(Attributes.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.random.nextDouble() * 0.10000000149011612D);
/*     */   }
/*     */   
/*     */   public static class ZombieGroupData implements SpawnGroupData {
/*     */     public final boolean isBaby;
/*     */     
/*     */     public final boolean canSpawnJockey;
/*     */     
/*     */     public ZombieGroupData(boolean baby, boolean canSpawnJockey) {
/* 589 */       this.isBaby = baby;
/* 590 */       this.canSpawnJockey = canSpawnJockey;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ZombieAttackTurtleEggGoal extends RemoveBlockGoal {
/*     */     public ZombieAttackTurtleEggGoal(PathfinderMob mob, double speedModifier, int verticalSearchRange) {
/* 596 */       super(Blocks.TURTLE_EGG, mob, speedModifier, verticalSearchRange);
/*     */     }
/*     */     
/*     */     public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
/* 601 */       level.playSound(null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + Zombie.this.random.nextFloat() * 0.2F);
/*     */     }
/*     */     
/*     */     public void playBreakSound(Level level, BlockPos pos) {
/* 606 */       level.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.getRandom().nextFloat() * 0.2F);
/*     */     }
/*     */     
/*     */     public double acceptedDistance() {
/* 611 */       return 1.14D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Zombie.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */