/*     */ package net.minecraft.world.entity.monster.zombie;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.damagesource.DamageSource;
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
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
/*     */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.npc.villager.AbstractVillager;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.Projectile;
/*     */ import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Drowned extends Zombie implements RangedAttackMob {
/*     */   public static final float NAUTILUS_SHELL_CHANCE = 0.03F;
/*     */   
/*     */   private static final float ZOMBIE_NAUTILUS_JOCKEY_CHANCE = 0.5F;
/*     */   
/*  72 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.49F, 0.98F)
/*  73 */     .withEyeHeight(0.775F)
/*  74 */     .withAttachments(EntityAttachments.builder()
/*  75 */       .attach(EntityAttachment.VEHICLE, 0.0F, 0.1875F, 0.0F));
/*     */   
/*     */   private boolean searchingForLand;
/*     */   
/*     */   public Drowned(EntityType<? extends Drowned> type, Level level) {
/*  80 */     super((EntityType)type, level);
/*  81 */     this.moveControl = new DrownedMoveControl<>(this);
/*  83 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  87 */     return Zombie.createAttributes()
/*  88 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */   
/*     */   protected PathNavigation createNavigation(Level level) {
/*  93 */     return (PathNavigation)new AmphibiousPathNavigation((Mob)this, level);
/*     */   }
/*     */   
/*     */   protected void addBehaviourGoals() {
/*  98 */     this.goalSelector.addGoal(1, new DrownedGoToWaterGoal((PathfinderMob)this, 1.0D));
/*  99 */     this.goalSelector.addGoal(2, (Goal)new DrownedTridentAttackGoal(this, 1.0D, 40, 10.0F));
/* 100 */     this.goalSelector.addGoal(2, (Goal)new DrownedAttackGoal(this, 1.0D, false));
/* 101 */     this.goalSelector.addGoal(5, (Goal)new DrownedGoToBeachGoal(this, 1.0D));
/* 102 */     this.goalSelector.addGoal(6, new DrownedSwimUpGoal(this, 1.0D, level().getSeaLevel()));
/* 103 */     this.goalSelector.addGoal(7, (Goal)new RandomStrollGoal((PathfinderMob)this, 1.0D));
/* 105 */     this.targetSelector.addGoal(1, (Goal)(new HurtByTargetGoal((PathfinderMob)this, new Class[] { Drowned.class })).setAlertOthers(new Class[] { ZombifiedPiglin.class }));
/* 106 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, (target, level) -> okTarget(target)));
/* 107 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractVillager.class, false));
/* 108 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, IronGolem.class, true));
/* 109 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Axolotl.class, true, false));
/* 110 */     this.targetSelector.addGoal(5, (Goal)new NearestAttackableTargetGoal((Mob)this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 115 */     groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/* 117 */     if (getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() && 
/* 118 */       level.getRandom().nextFloat() < 0.03F) {
/* 119 */       setItemSlot(EquipmentSlot.OFFHAND, new ItemStack((ItemLike)Items.NAUTILUS_SHELL));
/* 120 */       setGuaranteedDrop(EquipmentSlot.OFFHAND);
/*     */     } 
/* 124 */     if ((spawnReason == EntitySpawnReason.NATURAL || spawnReason == EntitySpawnReason.STRUCTURE) && 
/* 125 */       getMainHandItem().is(Items.TRIDENT) && level
/* 126 */       .getRandom().nextFloat() < 0.5F && 
/* 127 */       !isBaby() && 
/* 128 */       !level.getBiome(blockPosition()).is(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS)) {
/* 129 */       ZombieNautilus zombieNautilus = (ZombieNautilus)EntityTypes.ZOMBIE_NAUTILUS.create(level(), EntitySpawnReason.JOCKEY);
/* 130 */       if (zombieNautilus != null) {
/* 131 */         if (spawnReason == EntitySpawnReason.STRUCTURE)
/* 132 */           zombieNautilus.setPersistenceRequired(); 
/* 134 */         zombieNautilus.snapTo(getX(), getY(), getZ(), getYRot(), 0.0F);
/* 135 */         zombieNautilus.finalizeSpawn(level, difficulty, spawnReason, null);
/* 136 */         startRiding((Entity)zombieNautilus, false, false);
/* 137 */         level.addFreshEntity((Entity)zombieNautilus);
/*     */       } 
/*     */     } 
/* 141 */     return groupData;
/*     */   }
/*     */   
/*     */   public static boolean checkDrownedSpawnRules(EntityType<Drowned> type, ServerLevelAccessor level, EntitySpawnReason spawnReason, BlockPos pos, RandomSource random) {
/* 145 */     if (!level.getFluidState(pos.below()).is(FluidTags.WATER) && !EntitySpawnReason.isSpawner(spawnReason))
/* 146 */       return false; 
/* 149 */     Holder<Biome> biome = level.getBiome(pos);
/* 152 */     boolean canMonsterSpawn = (level.getDifficulty() != Difficulty.PEACEFUL && (EntitySpawnReason.ignoresLightRequirements(spawnReason) || isDarkEnoughToSpawn(level, pos, random)) && (EntitySpawnReason.isSpawner(spawnReason) || level.getFluidState(pos).is(FluidTags.WATER)));
/* 154 */     if (canMonsterSpawn && (EntitySpawnReason.isSpawner(spawnReason) || spawnReason == EntitySpawnReason.REINFORCEMENT))
/* 155 */       return true; 
/* 156 */     if (biome.is(BiomeTags.MORE_FREQUENT_DROWNED_SPAWNS))
/* 157 */       return (random.nextInt(15) == 0 && canMonsterSpawn); 
/* 159 */     return (random.nextInt(40) == 0 && isDeepEnoughToSpawn((LevelAccessor)level, pos) && canMonsterSpawn);
/*     */   }
/*     */   
/*     */   private static boolean isDeepEnoughToSpawn(LevelAccessor level, BlockPos pos) {
/* 164 */     return (pos.getY() < level.getSeaLevel() - 5);
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 169 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 174 */     if (isInWater())
/* 175 */       return SoundEvents.DROWNED_AMBIENT_WATER; 
/* 177 */     return SoundEvents.DROWNED_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 182 */     if (isInWater())
/* 183 */       return SoundEvents.DROWNED_HURT_WATER; 
/* 185 */     return SoundEvents.DROWNED_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 190 */     if (isInWater())
/* 191 */       return SoundEvents.DROWNED_DEATH_WATER; 
/* 193 */     return SoundEvents.DROWNED_DEATH;
/*     */   }
/*     */   
/*     */   protected SoundEvent getStepSound() {
/* 198 */     return SoundEvents.DROWNED_STEP;
/*     */   }
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 203 */     return SoundEvents.DROWNED_SWIM;
/*     */   }
/*     */   
/*     */   protected boolean canSpawnInLiquids() {
/* 208 */     return true;
/*     */   }
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
/* 213 */     if (random.nextFloat() > 0.9D) {
/* 214 */       int rand = random.nextInt(16);
/* 215 */       if (rand < 10) {
/* 216 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.TRIDENT));
/*     */       } else {
/* 218 */         setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.FISHING_ROD));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack newItemStack, ItemStack currentItemStack, EquipmentSlot slot) {
/* 225 */     if (currentItemStack.is(Items.NAUTILUS_SHELL))
/* 226 */       return false; 
/* 229 */     return super.canReplaceCurrentItem(newItemStack, currentItemStack, slot);
/*     */   }
/*     */   
/*     */   protected boolean convertsInWater() {
/* 234 */     return false;
/*     */   }
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader level) {
/* 239 */     return level.isUnobstructed((Entity)this);
/*     */   }
/*     */   
/*     */   public boolean okTarget(LivingEntity target) {
/* 243 */     if (target != null) {
/* 244 */       if (level().isBrightOutside() && !target.isInWater())
/* 245 */         return false; 
/* 248 */       return true;
/*     */     } 
/* 250 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 255 */     return !isSwimming();
/*     */   }
/*     */   
/*     */   public boolean wantsToSwim() {
/* 259 */     if (this.searchingForLand)
/* 260 */       return true; 
/* 263 */     LivingEntity target = getTarget();
/* 264 */     if (target != null && target.isInWater())
/* 265 */       return true; 
/* 268 */     return false;
/*     */   }
/*     */   
/*     */   protected void travelInWater(Vec3 input, double baseGravity, boolean isFalling, double oldY) {
/* 273 */     if (isUnderWater() && wantsToSwim()) {
/* 274 */       moveRelative(0.01F, input);
/* 275 */       move(MoverType.SELF, getDeltaMovement());
/* 277 */       setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */     } else {
/* 279 */       super.travelInWater(input, baseGravity, isFalling, oldY);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateSwimming() {
/* 285 */     if (!level().isClientSide())
/* 286 */       setSwimming((isEffectiveAi() && isUnderWater() && wantsToSwim())); 
/*     */   }
/*     */   
/*     */   public boolean isVisuallySwimming() {
/* 292 */     return (isSwimming() && !isPassenger());
/*     */   }
/*     */   
/*     */   protected boolean closeToNextPos() {
/* 296 */     Path path = getNavigation().getPath();
/* 297 */     if (path != null) {
/* 298 */       BlockPos pos = path.getTarget();
/* 299 */       if (pos != null) {
/* 300 */         double sqrDistToNextPos = distanceToSqr(pos.getX(), pos.getY(), pos.getZ());
/* 301 */         if (sqrDistToNextPos < 4.0D)
/* 302 */           return true; 
/*     */       } 
/*     */     } 
/* 306 */     return false;
/*     */   }
/*     */   
/*     */   public void performRangedAttack(LivingEntity target, float power) {
/* 311 */     ItemStack mainHandItem = getMainHandItem();
/* 312 */     ItemStack tridentItemStack = mainHandItem.is(Items.TRIDENT) ? mainHandItem : new ItemStack((ItemLike)Items.TRIDENT);
/* 313 */     ThrownTrident trident = new ThrownTrident(level(), (LivingEntity)this, tridentItemStack);
/* 315 */     double xd = target.getX() - getX();
/* 316 */     double yd = target.getY(0.3333333333333333D) - trident.getY();
/* 317 */     double zd = target.getZ() - getZ();
/* 318 */     double distanceToTarget = Math.sqrt(xd * xd + zd * zd);
/* 319 */     Level level = level();
/* 319 */     if (level instanceof ServerLevel) {
/* 319 */       ServerLevel serverLevel = (ServerLevel)level;
/* 320 */       Projectile.spawnProjectileUsingShoot((Projectile)trident, serverLevel, tridentItemStack, xd, yd + distanceToTarget * 0.20000000298023224D, zd, 1.6F, (14 - 
/*     */           
/* 324 */           level().getDifficulty().getId() * 4));
/*     */     } 
/* 326 */     playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
/*     */   }
/*     */   
/*     */   public TagKey<Item> getPreferredWeaponType() {
/* 331 */     return ItemTags.DROWNED_PREFERRED_WEAPONS;
/*     */   }
/*     */   
/*     */   public void setSearchingForLand(boolean searchingForLand) {
/* 335 */     this.searchingForLand = searchingForLand;
/*     */   }
/*     */   
/*     */   public boolean isSearchingForLand() {
/* 339 */     return this.searchingForLand;
/*     */   }
/*     */   
/*     */   private static class DrownedTridentAttackGoal extends RangedAttackGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedTridentAttackGoal(RangedAttackMob mob, double speedModifier, int attackInterval, float attackRadius) {
/* 346 */       super(mob, speedModifier, attackInterval, attackRadius);
/* 347 */       this.drowned = (Drowned)mob;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 352 */       return (super.canUse() && this.drowned.getMainHandItem().is(Items.TRIDENT));
/*     */     }
/*     */     
/*     */     public void start() {
/* 357 */       super.start();
/* 358 */       this.drowned.setAggressive(true);
/* 359 */       this.drowned.startUsingItem(InteractionHand.MAIN_HAND);
/*     */     }
/*     */     
/*     */     public void stop() {
/* 364 */       super.stop();
/* 365 */       this.drowned.stopUsingItem();
/* 366 */       this.drowned.setAggressive(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedSwimUpGoal extends Goal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     private final double speedModifier;
/*     */     
/*     */     private final int seaLevel;
/*     */     
/*     */     private boolean stuck;
/*     */     
/*     */     public DrownedSwimUpGoal(Drowned drowned, double speedModifier, int seaLevel) {
/* 377 */       this.drowned = drowned;
/* 378 */       this.speedModifier = speedModifier;
/* 379 */       this.seaLevel = seaLevel;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 384 */       return (!this.drowned.level().isBrightOutside() && this.drowned.isInWater() && this.drowned.getY() < (this.seaLevel - 2));
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 389 */       return (canUse() && !this.stuck);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 394 */       if (this.drowned.getY() < (this.seaLevel - 1) && (this.drowned.getNavigation().isDone() || this.drowned.closeToNextPos())) {
/* 396 */         Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.drowned, 4, 8, new Vec3(this.drowned.getX(), (this.seaLevel - 1), this.drowned.getZ()), 1.5707963705062866D);
/* 398 */         if (nextPos == null) {
/* 399 */           this.stuck = true;
/*     */           return;
/*     */         } 
/* 403 */         this.drowned.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void start() {
/* 409 */       this.drowned.setSearchingForLand(true);
/* 410 */       this.stuck = false;
/*     */     }
/*     */     
/*     */     public void stop() {
/* 415 */       this.drowned.setSearchingForLand(false);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedGoToBeachGoal extends MoveToBlockGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedGoToBeachGoal(Drowned drowned, double speedModifier) {
/* 424 */       super((PathfinderMob)drowned, speedModifier, 8, 2);
/* 425 */       this.drowned = drowned;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 430 */       return (super.canUse() && !this.drowned.level().isBrightOutside() && this.drowned.isInWater() && this.drowned.getY() >= (this.drowned.level().getSeaLevel() - 3));
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 435 */       return super.canContinueToUse();
/*     */     }
/*     */     
/*     */     protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 440 */       BlockPos above = pos.above();
/* 441 */       if (!level.isEmptyBlock(above) || !level.isEmptyBlock(above.above()))
/* 442 */         return false; 
/* 445 */       return level.getBlockState(pos).entityCanStandOn((BlockGetter)level, pos, (Entity)this.drowned);
/*     */     }
/*     */     
/*     */     public void start() {
/* 450 */       this.drowned.setSearchingForLand(false);
/* 451 */       super.start();
/*     */     }
/*     */     
/*     */     public void stop() {
/* 456 */       super.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedGoToWaterGoal extends Goal {
/*     */     private final PathfinderMob mob;
/*     */     
/*     */     private double wantedX;
/*     */     
/*     */     private double wantedY;
/*     */     
/*     */     private double wantedZ;
/*     */     
/*     */     private final double speedModifier;
/*     */     
/*     */     private final Level level;
/*     */     
/*     */     public DrownedGoToWaterGoal(PathfinderMob mob, double speedModifier) {
/* 469 */       this.mob = mob;
/* 470 */       this.speedModifier = speedModifier;
/* 471 */       this.level = mob.level();
/* 472 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 477 */       if (!this.level.isBrightOutside())
/* 478 */         return false; 
/* 480 */       if (this.mob.isInWater())
/* 481 */         return false; 
/* 484 */       Vec3 pos = getWaterPos();
/* 485 */       if (pos == null)
/* 486 */         return false; 
/* 488 */       this.wantedX = pos.x;
/* 489 */       this.wantedY = pos.y;
/* 490 */       this.wantedZ = pos.z;
/* 491 */       return true;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 496 */       return !this.mob.getNavigation().isDone();
/*     */     }
/*     */     
/*     */     public void start() {
/* 501 */       this.mob.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
/*     */     }
/*     */     
/*     */     private Vec3 getWaterPos() {
/* 505 */       RandomSource random = this.mob.getRandom();
/* 506 */       BlockPos pos = this.mob.blockPosition();
/* 508 */       for (int i = 0; i < 10; i++) {
/* 509 */         BlockPos randomPos = pos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8), random.nextInt(20) - 10);
/* 511 */         if (this.level.getBlockState(randomPos).is(Blocks.WATER))
/* 512 */           return Vec3.atBottomCenterOf((Vec3i)randomPos); 
/*     */       } 
/* 515 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedAttackGoal extends ZombieAttackGoal {
/*     */     private final Drowned drowned;
/*     */     
/*     */     public DrownedAttackGoal(Drowned drowned, double speedModifier, boolean trackTarget) {
/* 523 */       super(drowned, speedModifier, trackTarget);
/* 524 */       this.drowned = drowned;
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 529 */       return (super.canUse() && this.drowned.okTarget(this.drowned.getTarget()));
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 534 */       return (super.canContinueToUse() && this.drowned.okTarget(this.drowned.getTarget()));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DrownedMoveControl<T extends Drowned> extends MoveControl<T> {
/*     */     public DrownedMoveControl(T drowned) {
/* 541 */       super((Mob)drowned);
/*     */     }
/*     */     
/*     */     public void tick() {
/* 546 */       LivingEntity target = ((Drowned)this.mob).getTarget();
/* 547 */       if (((Drowned)this.mob).wantsToSwim() && ((Drowned)this.mob).isInWater()) {
/* 548 */         if ((target != null && target.getY() > ((Drowned)this.mob).getY()) || ((Drowned)this.mob).isSearchingForLand())
/* 550 */           ((Drowned)this.mob).setDeltaMovement(((Drowned)this.mob).getDeltaMovement().add(0.0D, 0.002D, 0.0D)); 
/* 553 */         if (this.operation != MoveControl.Operation.MOVE_TO || ((Drowned)this.mob).getNavigation().isDone()) {
/* 554 */           ((Drowned)this.mob).setSpeed(0.0F);
/*     */           return;
/*     */         } 
/* 558 */         double xd = this.wantedX - ((Drowned)this.mob).getX();
/* 559 */         double yd = this.wantedY - ((Drowned)this.mob).getY();
/* 560 */         double zd = this.wantedZ - ((Drowned)this.mob).getZ();
/* 561 */         double dd = Math.sqrt(xd * xd + yd * yd + zd * zd);
/* 562 */         yd /= dd;
/* 564 */         float yRotD = (float)(Mth.atan2(zd, xd) * 57.2957763671875D) - 90.0F;
/* 565 */         ((Drowned)this.mob).setYRot(rotlerp(((Drowned)this.mob).getYRot(), yRotD, 90.0F));
/* 566 */         ((Drowned)this.mob).yBodyRot = ((Drowned)this.mob).getYRot();
/* 568 */         float targetSpeed = (float)(this.speedModifier * ((Drowned)this.mob).getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 569 */         float newSpeed = Mth.lerp(0.125F, ((Drowned)this.mob).getSpeed(), targetSpeed);
/* 570 */         ((Drowned)this.mob).setSpeed(newSpeed);
/* 571 */         ((Drowned)this.mob).setDeltaMovement(((Drowned)this.mob).getDeltaMovement().add(newSpeed * xd * 0.005D, newSpeed * yd * 0.1D, newSpeed * zd * 0.005D));
/*     */       } else {
/* 577 */         if (!((Drowned)this.mob).onGround())
/* 578 */           ((Drowned)this.mob).setDeltaMovement(((Drowned)this.mob).getDeltaMovement().add(0.0D, -0.008D, 0.0D)); 
/* 580 */         super.tick();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public void rideTick() {
/* 587 */     super.rideTick();
/* 589 */     Entity entity = getControlledVehicle();
/* 589 */     if (entity instanceof PathfinderMob) {
/* 589 */       PathfinderMob pathfinderMob = (PathfinderMob)entity;
/* 590 */       this.yBodyRot = pathfinderMob.yBodyRot;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 596 */     if (itemStack.is(ItemTags.SPEARS))
/* 597 */       return false; 
/* 599 */     return super.wantsToPickUp(level, itemStack);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\monster\zombie\Drowned.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */