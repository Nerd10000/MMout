/*      */ package net.minecraft.world.entity.animal.bee;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Position;
/*      */ import net.minecraft.core.Vec3i;
/*      */ import net.minecraft.core.particles.ParticleOptions;
/*      */ import net.minecraft.core.particles.ParticleTypes;
/*      */ import net.minecraft.network.syncher.EntityDataAccessor;
/*      */ import net.minecraft.network.syncher.EntityDataSerializers;
/*      */ import net.minecraft.network.syncher.SynchedEntityData;
/*      */ import net.minecraft.server.level.ServerLevel;
/*      */ import net.minecraft.sounds.SoundEvent;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.BlockTags;
/*      */ import net.minecraft.tags.ItemTags;
/*      */ import net.minecraft.tags.PoiTypeTags;
/*      */ import net.minecraft.tags.TagKey;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.TimeUtil;
/*      */ import net.minecraft.util.VisibleForDebug;
/*      */ import net.minecraft.util.debug.DebugBeeInfo;
/*      */ import net.minecraft.util.debug.DebugSubscriptions;
/*      */ import net.minecraft.util.debug.DebugValueSource;
/*      */ import net.minecraft.util.valueproviders.UniformInt;
/*      */ import net.minecraft.world.Difficulty;
/*      */ import net.minecraft.world.InteractionHand;
/*      */ import net.minecraft.world.InteractionResult;
/*      */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*      */ import net.minecraft.world.damagesource.DamageSource;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.AgeableMob;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EntityReference;
/*      */ import net.minecraft.world.entity.EntitySpawnReason;
/*      */ import net.minecraft.world.entity.EntityType;
/*      */ import net.minecraft.world.entity.EntityTypes;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.Mob;
/*      */ import net.minecraft.world.entity.NeutralMob;
/*      */ import net.minecraft.world.entity.PathfinderMob;
/*      */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.ai.control.FlyingMoveControl;
/*      */ import net.minecraft.world.entity.ai.control.LookControl;
/*      */ import net.minecraft.world.entity.ai.control.MoveControl;
/*      */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*      */ import net.minecraft.world.entity.ai.goal.FollowParentGoal;
/*      */ import net.minecraft.world.entity.ai.goal.Goal;
/*      */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*      */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*      */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*      */ import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
/*      */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*      */ import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
/*      */ import net.minecraft.world.entity.ai.util.AirRandomPos;
/*      */ import net.minecraft.world.entity.ai.util.HoverRandomPos;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*      */ import net.minecraft.world.entity.ai.village.poi.PoiRecord;
/*      */ import net.minecraft.world.entity.animal.Animal;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.item.BlockItem;
/*      */ import net.minecraft.world.item.Item;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.LevelReader;
/*      */ import net.minecraft.world.level.block.Block;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.BonemealableBlock;
/*      */ import net.minecraft.world.level.block.CropBlock;
/*      */ import net.minecraft.world.level.block.DoublePlantBlock;
/*      */ import net.minecraft.world.level.block.FlowerBlock;
/*      */ import net.minecraft.world.level.block.StemBlock;
/*      */ import net.minecraft.world.level.block.SweetBerryBushBlock;
/*      */ import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntity;
/*      */ import net.minecraft.world.level.block.entity.BlockEntityTypes;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*      */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.material.Fluid;
/*      */ import net.minecraft.world.level.pathfinder.Path;
/*      */ import net.minecraft.world.level.pathfinder.PathType;
/*      */ import net.minecraft.world.level.storage.ValueInput;
/*      */ import net.minecraft.world.level.storage.ValueOutput;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ 
/*      */ public class Bee extends Animal implements NeutralMob {
/*      */   public static final float FLAP_DEGREES_PER_TICK = 120.32113F;
/*      */   
/*  101 */   public static final int TICKS_PER_FLAP = Mth.ceil(1.4959966F);
/*      */   
/*  103 */   private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Bee.class, EntityDataSerializers.BYTE);
/*      */   
/*  104 */   private static final EntityDataAccessor<Long> DATA_ANGER_END_TIME = SynchedEntityData.defineId(Bee.class, EntityDataSerializers.LONG);
/*      */   
/*      */   private static final int FLAG_ROLL = 2;
/*      */   
/*      */   private static final int FLAG_HAS_STUNG = 4;
/*      */   
/*      */   private static final int FLAG_HAS_NECTAR = 8;
/*      */   
/*      */   private static final int STING_DEATH_COUNTDOWN = 1200;
/*      */   
/*      */   private static final int TICKS_BEFORE_GOING_TO_KNOWN_FLOWER = 600;
/*      */   
/*      */   private static final int TICKS_WITHOUT_NECTAR_BEFORE_GOING_HOME = 3600;
/*      */   
/*      */   private static final int MIN_ATTACK_DIST = 4;
/*      */   
/*      */   private static final int MAX_CROPS_GROWABLE = 10;
/*      */   
/*      */   private static final int POISON_SECONDS_NORMAL = 10;
/*      */   
/*      */   private static final int POISON_SECONDS_HARD = 18;
/*      */   
/*      */   private static final int TOO_FAR_DISTANCE = 48;
/*      */   
/*      */   private static final int HIVE_CLOSE_ENOUGH_DISTANCE = 2;
/*      */   
/*      */   private static final int RESTRICTED_WANDER_DISTANCE_REDUCTION = 24;
/*      */   
/*      */   private static final int DEFAULT_WANDER_DISTANCE_REDUCTION = 16;
/*      */   
/*      */   private static final int PATHFIND_TO_HIVE_WHEN_CLOSER_THAN = 16;
/*      */   
/*      */   private static final int HIVE_SEARCH_DISTANCE = 20;
/*      */   
/*      */   public static final String TAG_CROPS_GROWN_SINCE_POLLINATION = "CropsGrownSincePollination";
/*      */   
/*      */   public static final String TAG_CANNOT_ENTER_HIVE_TICKS = "CannotEnterHiveTicks";
/*      */   
/*      */   public static final String TAG_TICKS_SINCE_POLLINATION = "TicksSincePollination";
/*      */   
/*      */   public static final String TAG_HAS_STUNG = "HasStung";
/*      */   
/*      */   public static final String TAG_HAS_NECTAR = "HasNectar";
/*      */   
/*      */   public static final String TAG_FLOWER_POS = "flower_pos";
/*      */   
/*      */   public static final String TAG_HIVE_POS = "hive_pos";
/*      */   
/*      */   public static final boolean DEFAULT_HAS_NECTAR = false;
/*      */   
/*      */   private static final boolean DEFAULT_HAS_STUNG = false;
/*      */   
/*      */   private static final int DEFAULT_TICKS_SINCE_POLLINATION = 0;
/*      */   
/*      */   private static final int DEFAULT_CANNOT_ENTER_HIVE_TICKS = 0;
/*      */   
/*      */   private static final int DEFAULT_CROPS_GROWN_SINCE_POLLINATION = 0;
/*      */   
/*  145 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*      */   
/*      */   private EntityReference<LivingEntity> persistentAngerTarget;
/*      */   
/*      */   private float rollAmount;
/*      */   
/*      */   private float rollAmountO;
/*      */   
/*      */   private int timeSinceSting;
/*      */   
/*  153 */   private int ticksWithoutNectarSinceExitingHive = 0;
/*      */   
/*  156 */   private int stayOutOfHiveCountdown = 0;
/*      */   
/*  159 */   private int numCropsGrownSincePollination = 0;
/*      */   
/*      */   private static final int COOLDOWN_BEFORE_LOCATING_NEW_HIVE = 200;
/*      */   
/*      */   private int remainingCooldownBeforeLocatingNewHive;
/*      */   
/*      */   private static final int COOLDOWN_BEFORE_LOCATING_NEW_FLOWER = 200;
/*      */   
/*      */   private static final int MIN_FIND_FLOWER_RETRY_COOLDOWN = 20;
/*      */   
/*      */   private static final int MAX_FIND_FLOWER_RETRY_COOLDOWN = 60;
/*      */   
/*  171 */   private int remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
/*      */   
/*      */   private BlockPos savedFlowerPos;
/*      */   
/*      */   private BlockPos hivePos;
/*      */   
/*      */   private BeePollinateGoal beePollinateGoal;
/*      */   
/*      */   private BeeGoToHiveGoal goToHiveGoal;
/*      */   
/*      */   private BeeGoToKnownFlowerGoal goToKnownFlowerGoal;
/*      */   
/*      */   private int underWaterTicks;
/*      */   
/*      */   public Bee(EntityType<? extends Bee> type, Level level) {
/*  184 */     super(type, level);
/*  185 */     this.moveControl = (MoveControl)new FlyingMoveControl((Mob)this, 20, true);
/*  186 */     this.lookControl = new BeeLookControl((Mob)this);
/*  188 */     setPathfindingMalus(PathType.FIRE, -1.0F);
/*  189 */     setPathfindingMalus(PathType.WATER, -1.0F);
/*  190 */     setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
/*  191 */     setPathfindingMalus(PathType.COCOA, -1.0F);
/*  192 */     setPathfindingMalus(PathType.FENCE, -1.0F);
/*      */   }
/*      */   
/*      */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  197 */     super.defineSynchedData(entityData);
/*  198 */     entityData.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*  199 */     entityData.define(DATA_ANGER_END_TIME, Long.valueOf(-1L));
/*      */   }
/*      */   
/*      */   public float getWalkTargetValue(BlockPos pos, LevelReader level) {
/*  205 */     if (level.getBlockState(pos).isAir())
/*  206 */       return 10.0F; 
/*  208 */     return 0.0F;
/*      */   }
/*      */   
/*      */   protected void registerGoals() {
/*  213 */     this.goalSelector.addGoal(0, (Goal)new BeeAttackGoal((PathfinderMob)this, 1.399999976158142D, true));
/*  214 */     this.goalSelector.addGoal(1, new BeeEnterHiveGoal());
/*  215 */     this.goalSelector.addGoal(2, (Goal)new BreedGoal(this, 1.0D));
/*  216 */     this.goalSelector.addGoal(3, (Goal)new TemptGoal((PathfinderMob)this, 1.25D, i -> i.is(ItemTags.BEE_FOOD), false));
/*  218 */     this.goalSelector.addGoal(3, new ValidateHiveGoal());
/*  219 */     this.goalSelector.addGoal(3, new ValidateFlowerGoal());
/*  221 */     this.beePollinateGoal = new BeePollinateGoal();
/*  222 */     this.goalSelector.addGoal(4, this.beePollinateGoal);
/*  224 */     this.goalSelector.addGoal(5, (Goal)new FollowParentGoal(this, 1.25D));
/*  226 */     this.goalSelector.addGoal(5, new BeeLocateHiveGoal());
/*  228 */     this.goToHiveGoal = new BeeGoToHiveGoal();
/*  229 */     this.goalSelector.addGoal(5, this.goToHiveGoal);
/*  231 */     this.goToKnownFlowerGoal = new BeeGoToKnownFlowerGoal();
/*  232 */     this.goalSelector.addGoal(6, this.goToKnownFlowerGoal);
/*  234 */     this.goalSelector.addGoal(7, new BeeGrowCropGoal());
/*  235 */     this.goalSelector.addGoal(8, new BeeWanderGoal());
/*  236 */     this.goalSelector.addGoal(9, (Goal)new FloatGoal((Mob)this));
/*  238 */     this.targetSelector.addGoal(1, (Goal)(new BeeHurtByOtherGoal(this)).setAlertOthers(new Class[0]));
/*  239 */     this.targetSelector.addGoal(2, (Goal)new BeeBecomeAngryTargetGoal(this));
/*  240 */     this.targetSelector.addGoal(3, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, true));
/*      */   }
/*      */   
/*      */   protected void addAdditionalSaveData(ValueOutput output) {
/*  245 */     super.addAdditionalSaveData(output);
/*  247 */     output.storeNullable("hive_pos", BlockPos.CODEC, this.hivePos);
/*  248 */     output.storeNullable("flower_pos", BlockPos.CODEC, this.savedFlowerPos);
/*  249 */     output.putBoolean("HasNectar", hasNectar());
/*  250 */     output.putBoolean("HasStung", hasStung());
/*  251 */     output.putInt("TicksSincePollination", this.ticksWithoutNectarSinceExitingHive);
/*  252 */     output.putInt("CannotEnterHiveTicks", this.stayOutOfHiveCountdown);
/*  253 */     output.putInt("CropsGrownSincePollination", this.numCropsGrownSincePollination);
/*  255 */     addPersistentAngerSaveData(output);
/*      */   }
/*      */   
/*      */   protected void readAdditionalSaveData(ValueInput input) {
/*  260 */     super.readAdditionalSaveData(input);
/*  261 */     setHasNectar(input.getBooleanOr("HasNectar", false));
/*  262 */     setHasStung(input.getBooleanOr("HasStung", false));
/*  263 */     this.ticksWithoutNectarSinceExitingHive = input.getIntOr("TicksSincePollination", 0);
/*  264 */     this.stayOutOfHiveCountdown = input.getIntOr("CannotEnterHiveTicks", 0);
/*  265 */     this.numCropsGrownSincePollination = input.getIntOr("CropsGrownSincePollination", 0);
/*  267 */     this.hivePos = input.read("hive_pos", BlockPos.CODEC).orElse(null);
/*  268 */     this.savedFlowerPos = input.read("flower_pos", BlockPos.CODEC).orElse(null);
/*  270 */     readPersistentAngerSaveData(level(), input);
/*      */   }
/*      */   
/*      */   public boolean doHurtTarget(ServerLevel level, Entity target) {
/*  275 */     DamageSource damageSource = damageSources().sting((LivingEntity)this);
/*  276 */     boolean wasHurt = target.hurtServer(level, damageSource, (int)getAttributeValue(Attributes.ATTACK_DAMAGE));
/*  277 */     if (wasHurt) {
/*  278 */       EnchantmentHelper.doPostAttackEffects(level, target, damageSource);
/*  279 */       if (target instanceof LivingEntity) {
/*  279 */         LivingEntity livingTarget = (LivingEntity)target;
/*  280 */         livingTarget.setStingerCount(livingTarget.getStingerCount() + 1);
/*  281 */         int poisonTime = 0;
/*  282 */         if (level().getDifficulty() == Difficulty.NORMAL) {
/*  283 */           poisonTime = 10;
/*  284 */         } else if (level().getDifficulty() == Difficulty.HARD) {
/*  285 */           poisonTime = 18;
/*      */         } 
/*  288 */         if (poisonTime > 0)
/*  289 */           livingTarget.addEffect(new MobEffectInstance(MobEffects.POISON, poisonTime * 20, 0), (Entity)this); 
/*      */       } 
/*  292 */       setHasStung(true);
/*  293 */       stopBeingAngry();
/*  295 */       playSound(SoundEvents.BEE_STING, 1.0F, 1.0F);
/*      */     } 
/*  297 */     return wasHurt;
/*      */   }
/*      */   
/*      */   public void tick() {
/*  302 */     super.tick();
/*  305 */     if (hasNectar() && getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F)
/*  306 */       for (int i = 0; i < this.random.nextInt(2) + 1; i++)
/*  307 */         spawnFluidParticle(level(), getX() - 0.30000001192092896D, getX() + 0.30000001192092896D, getZ() - 0.30000001192092896D, getZ() + 0.30000001192092896D, getY(0.5D), (ParticleOptions)ParticleTypes.FALLING_NECTAR);  
/*  311 */     updateRollAmount();
/*      */   }
/*      */   
/*      */   private void spawnFluidParticle(Level level, double x1, double x2, double z1, double z2, double y, ParticleOptions dripParticle) {
/*  315 */     level.addParticle(dripParticle, Mth.lerp(level.getRandom().nextDouble(), x1, x2), y, Mth.lerp(level.getRandom().nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
/*      */   }
/*      */   
/*      */   private void pathfindRandomlyTowards(BlockPos targetPos) {
/*  319 */     Vec3 targetVec = Vec3.atBottomCenterOf((Vec3i)targetPos);
/*  320 */     int yAdjust = 0;
/*  321 */     BlockPos beePos = blockPosition();
/*  322 */     int yDelta = (int)targetVec.y - beePos.getY();
/*  323 */     if (yDelta > 2) {
/*  324 */       yAdjust = 4;
/*  325 */     } else if (yDelta < -2) {
/*  326 */       yAdjust = -4;
/*      */     } 
/*  329 */     int xzDist = 6;
/*  330 */     int yDist = 8;
/*  331 */     int dist = beePos.distManhattan((Vec3i)targetPos);
/*  332 */     if (dist < 15) {
/*  333 */       xzDist = dist / 2;
/*  334 */       yDist = dist / 2;
/*      */     } 
/*  337 */     Vec3 nextPosTowards = AirRandomPos.getPosTowards((PathfinderMob)this, xzDist, yDist, yAdjust, targetVec, 0.3141592741012573D);
/*  338 */     if (nextPosTowards == null)
/*      */       return; 
/*  342 */     this.navigation.setMaxVisitedNodesMultiplier(0.5F);
/*  343 */     this.navigation.moveTo(nextPosTowards.x, nextPosTowards.y, nextPosTowards.z, 1.0D);
/*      */   }
/*      */   
/*      */   public BlockPos getSavedFlowerPos() {
/*  347 */     return this.savedFlowerPos;
/*      */   }
/*      */   
/*      */   public boolean hasSavedFlowerPos() {
/*  351 */     return (this.savedFlowerPos != null);
/*      */   }
/*      */   
/*      */   public void setSavedFlowerPos(BlockPos savedFlowerPos) {
/*  355 */     this.savedFlowerPos = savedFlowerPos;
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public int getTravellingTicks() {
/*  360 */     return Math.max(this.goToHiveGoal.travellingTicks, this.goToKnownFlowerGoal.travellingTicks);
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public List<BlockPos> getBlacklistedHives() {
/*  365 */     return this.goToHiveGoal.blacklistedTargets;
/*      */   }
/*      */   
/*      */   private boolean isTiredOfLookingForNectar() {
/*  369 */     return (this.ticksWithoutNectarSinceExitingHive > 3600);
/*      */   }
/*      */   
/*      */   private void dropHive() {
/*  373 */     this.hivePos = null;
/*  374 */     this.remainingCooldownBeforeLocatingNewHive = 200;
/*      */   }
/*      */   
/*      */   private void dropFlower() {
/*  378 */     this.savedFlowerPos = null;
/*  379 */     this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(this.random, 20, 60);
/*      */   }
/*      */   
/*      */   private boolean wantsToEnterHive() {
/*  384 */     if (this.stayOutOfHiveCountdown > 0 || this.beePollinateGoal.isPollinating() || hasStung() || getTarget() != null)
/*  385 */       return false; 
/*  391 */     boolean wantsToEnterHive = (hasNectar() || isTiredOfLookingForNectar() || ((Boolean)level().environmentAttributes().getValue(EnvironmentAttributes.BEES_STAY_IN_HIVE, position())).booleanValue());
/*  394 */     return (wantsToEnterHive && !isHiveNearFire());
/*      */   }
/*      */   
/*      */   public void setStayOutOfHiveCountdown(int ticks) {
/*  398 */     this.stayOutOfHiveCountdown = ticks;
/*      */   }
/*      */   
/*      */   public float getRollAmount(float a) {
/*  402 */     return Mth.lerp(a, this.rollAmountO, this.rollAmount);
/*      */   }
/*      */   
/*      */   private void updateRollAmount() {
/*  406 */     this.rollAmountO = this.rollAmount;
/*  407 */     if (isRolling()) {
/*  408 */       this.rollAmount = Math.min(1.0F, this.rollAmount + 0.2F);
/*      */     } else {
/*  410 */       this.rollAmount = Math.max(0.0F, this.rollAmount - 0.24F);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void customServerAiStep(ServerLevel level) {
/*  416 */     boolean hasStung = hasStung();
/*  418 */     if (isInWater()) {
/*  419 */       this.underWaterTicks++;
/*      */     } else {
/*  421 */       this.underWaterTicks = 0;
/*      */     } 
/*  424 */     if (this.underWaterTicks > 20)
/*  425 */       hurtServer(level, damageSources().drown(), 1.0F); 
/*  428 */     if (hasStung) {
/*  429 */       this.timeSinceSting++;
/*  433 */       if (this.timeSinceSting % 5 == 0 && this.random.nextInt(Mth.clamp(1200 - this.timeSinceSting, 1, 1200)) == 0)
/*  434 */         hurtServer(level, damageSources().generic(), getHealth()); 
/*      */     } 
/*  438 */     if (!hasNectar())
/*  439 */       this.ticksWithoutNectarSinceExitingHive++; 
/*  442 */     updatePersistentAnger(level, false);
/*      */   }
/*      */   
/*      */   public void resetTicksWithoutNectarSinceExitingHive() {
/*  446 */     this.ticksWithoutNectarSinceExitingHive = 0;
/*      */   }
/*      */   
/*      */   private boolean isHiveNearFire() {
/*  450 */     BeehiveBlockEntity beehiveBlockEntity = getBeehiveBlockEntity();
/*  451 */     return (beehiveBlockEntity != null && beehiveBlockEntity.isFireNearby());
/*      */   }
/*      */   
/*      */   public long getPersistentAngerEndTime() {
/*  456 */     return ((Long)this.entityData.get(DATA_ANGER_END_TIME)).longValue();
/*      */   }
/*      */   
/*      */   public void setPersistentAngerEndTime(long endTime) {
/*  461 */     this.entityData.set(DATA_ANGER_END_TIME, Long.valueOf(endTime));
/*      */   }
/*      */   
/*      */   public EntityReference<LivingEntity> getPersistentAngerTarget() {
/*  466 */     return this.persistentAngerTarget;
/*      */   }
/*      */   
/*      */   public void setPersistentAngerTarget(EntityReference<LivingEntity> persistentAngerTarget) {
/*  471 */     this.persistentAngerTarget = persistentAngerTarget;
/*      */   }
/*      */   
/*      */   public void startPersistentAngerTimer() {
/*  476 */     setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
/*      */   }
/*      */   
/*      */   private boolean doesHiveHaveSpace(BlockPos hivePos) {
/*  480 */     BlockEntity blockEntity = level().getBlockEntity(hivePos);
/*  481 */     if (blockEntity instanceof BeehiveBlockEntity) {
/*  481 */       BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
/*  482 */       return !beehiveBlockEntity.isFull();
/*      */     } 
/*  484 */     return false;
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public boolean hasHive() {
/*  489 */     return (this.hivePos != null);
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public BlockPos getHivePos() {
/*  494 */     return this.hivePos;
/*      */   }
/*      */   
/*      */   private int getCropsGrownSincePollination() {
/*  498 */     return this.numCropsGrownSincePollination;
/*      */   }
/*      */   
/*      */   private void resetNumCropsGrownSincePollination() {
/*  502 */     this.numCropsGrownSincePollination = 0;
/*      */   }
/*      */   
/*      */   private void incrementNumCropsGrownSincePollination() {
/*  506 */     this.numCropsGrownSincePollination++;
/*      */   }
/*      */   
/*      */   public void aiStep() {
/*  511 */     super.aiStep();
/*  513 */     if (!level().isClientSide()) {
/*  514 */       if (this.stayOutOfHiveCountdown > 0)
/*  515 */         this.stayOutOfHiveCountdown--; 
/*  517 */       if (this.remainingCooldownBeforeLocatingNewHive > 0)
/*  518 */         this.remainingCooldownBeforeLocatingNewHive--; 
/*  520 */       if (this.remainingCooldownBeforeLocatingNewFlower > 0)
/*  521 */         this.remainingCooldownBeforeLocatingNewFlower--; 
/*  525 */       boolean shouldRoll = (isAngry() && !hasStung() && getTarget() != null && getTarget().distanceToSqr((Entity)this) < 4.0D);
/*  526 */       setRolling(shouldRoll);
/*  528 */       if (this.tickCount % 20 == 0)
/*  530 */         if (!isHiveValid())
/*  531 */           this.hivePos = null;  
/*      */     } 
/*      */   }
/*      */   
/*      */   private BeehiveBlockEntity getBeehiveBlockEntity() {
/*  538 */     if (this.hivePos == null)
/*  539 */       return null; 
/*  541 */     if (isTooFarAway(this.hivePos))
/*  542 */       return null; 
/*  544 */     return level().getBlockEntity(this.hivePos, BlockEntityTypes.BEEHIVE).orElse(null);
/*      */   }
/*      */   
/*      */   private boolean isHiveValid() {
/*  548 */     return (getBeehiveBlockEntity() != null);
/*      */   }
/*      */   
/*      */   public boolean hasNectar() {
/*  552 */     return getFlag(8);
/*      */   }
/*      */   
/*      */   private void setHasNectar(boolean hasNectar) {
/*  556 */     if (hasNectar)
/*  557 */       resetTicksWithoutNectarSinceExitingHive(); 
/*  559 */     setFlag(8, hasNectar);
/*      */   }
/*      */   
/*      */   public boolean hasStung() {
/*  563 */     return getFlag(4);
/*      */   }
/*      */   
/*      */   private void setHasStung(boolean hasStung) {
/*  567 */     setFlag(4, hasStung);
/*      */   }
/*      */   
/*      */   private boolean isRolling() {
/*  571 */     return getFlag(2);
/*      */   }
/*      */   
/*      */   private void setRolling(boolean rolling) {
/*  575 */     setFlag(2, rolling);
/*      */   }
/*      */   
/*      */   private boolean isTooFarAway(BlockPos targetPos) {
/*  579 */     return !closerThan(targetPos, 48);
/*      */   }
/*      */   
/*      */   private void setFlag(int flag, boolean value) {
/*  583 */     if (value) {
/*  584 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() | flag)));
/*      */     } else {
/*  586 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & (flag ^ 0xFFFFFFFF))));
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean getFlag(int flag) {
/*  591 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & flag) != 0);
/*      */   }
/*      */   
/*      */   public static AttributeSupplier.Builder createAttributes() {
/*  595 */     return Animal.createAnimalAttributes()
/*  596 */       .add(Attributes.MAX_HEALTH, 10.0D)
/*  597 */       .add(Attributes.FLYING_SPEED, 0.6000000238418579D)
/*  598 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/*  599 */       .add(Attributes.ATTACK_DAMAGE, 2.0D);
/*      */   }
/*      */   
/*      */   protected PathNavigation createNavigation(Level level) {
/*  604 */     FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation((Mob)this, level) {
/*      */         public boolean isStableDestination(BlockPos pos) {
/*  607 */           return !this.level.getBlockState(pos.below()).isAir();
/*      */         }
/*      */         
/*      */         public void tick() {
/*  612 */           if (Bee.this.beePollinateGoal.isPollinating())
/*      */             return; 
/*  616 */           super.tick();
/*      */         }
/*      */       };
/*  619 */     flyingPathNavigation.setCanOpenDoors(false);
/*  620 */     flyingPathNavigation.setCanFloat(false);
/*  621 */     flyingPathNavigation.setRequiredPathLength(48.0F);
/*  622 */     return (PathNavigation)flyingPathNavigation;
/*      */   }
/*      */   
/*      */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/*  627 */     ItemStack heldItem = player.getItemInHand(hand);
/*  628 */     if (isFood(heldItem)) {
/*  628 */       Item item = heldItem.getItem();
/*  628 */       if (item instanceof BlockItem) {
/*  628 */         BlockItem blockItem = (BlockItem)item;
/*  628 */         Block block = blockItem.getBlock();
/*  628 */         if (block instanceof FlowerBlock) {
/*  628 */           FlowerBlock flower = (FlowerBlock)block;
/*  629 */           MobEffectInstance effect = flower.getBeeInteractionEffect();
/*  630 */           if (effect != null) {
/*  631 */             usePlayerItem(player, hand, heldItem);
/*  632 */             if (!level().isClientSide())
/*  633 */               addEffect(effect); 
/*  635 */             return (InteractionResult)InteractionResult.SUCCESS;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  638 */     return super.mobInteract(player, hand);
/*      */   }
/*      */   
/*      */   public boolean isFood(ItemStack itemStack) {
/*  643 */     return itemStack.is(ItemTags.BEE_FOOD);
/*      */   }
/*      */   
/*      */   protected void playStepSound(BlockPos pos, BlockState blockState) {}
/*      */   
/*      */   protected SoundEvent getAmbientSound() {
/*  653 */     return null;
/*      */   }
/*      */   
/*      */   protected SoundEvent getHurtSound(DamageSource source) {
/*  658 */     return SoundEvents.BEE_HURT;
/*      */   }
/*      */   
/*      */   protected SoundEvent getDeathSound() {
/*  663 */     return SoundEvents.BEE_DEATH;
/*      */   }
/*      */   
/*      */   protected float getSoundVolume() {
/*  668 */     return 0.4F;
/*      */   }
/*      */   
/*      */   public Bee getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*  673 */     return (Bee)EntityTypes.BEE.create((Level)level, EntitySpawnReason.BREEDING);
/*      */   }
/*      */   
/*      */   protected void checkFallDamage(double ya, boolean onGround, BlockState onState, BlockPos pos) {}
/*      */   
/*      */   public boolean isFlapping() {
/*  685 */     return (isFlying() && this.tickCount % TICKS_PER_FLAP == 0);
/*      */   }
/*      */   
/*      */   public boolean isFlying() {
/*  689 */     return !onGround();
/*      */   }
/*      */   
/*      */   protected boolean omnidirectionalAirMover() {
/*  694 */     return true;
/*      */   }
/*      */   
/*      */   public void dropOffNectar() {
/*  698 */     setHasNectar(false);
/*  699 */     resetNumCropsGrownSincePollination();
/*      */   }
/*      */   
/*      */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/*  704 */     if (isInvulnerableTo(level, source))
/*  705 */       return false; 
/*  708 */     this.beePollinateGoal.stopPollinating();
/*  709 */     return super.hurtServer(level, source, damage);
/*      */   }
/*      */   
/*      */   protected void jumpInLiquid(TagKey<Fluid> type) {
/*  714 */     setDeltaMovement(getDeltaMovement().add(0.0D, 0.01D, 0.0D));
/*      */   }
/*      */   
/*      */   public Vec3 getLeashOffset() {
/*  719 */     return new Vec3(0.0D, (0.5F * getEyeHeight()), (getBbWidth() * 0.2F));
/*      */   }
/*      */   
/*      */   private boolean closerThan(BlockPos targetPos, int distance) {
/*  723 */     return targetPos.closerThan((Vec3i)blockPosition(), distance);
/*      */   }
/*      */   
/*      */   public void setHivePos(BlockPos hivePos) {
/*  727 */     this.hivePos = hivePos;
/*      */   }
/*      */   
/*      */   public static boolean attractsBees(BlockState state) {
/*  731 */     if (state.is(BlockTags.BEE_ATTRACTIVE)) {
/*  732 */       if (((Boolean)state.getValueOrElse((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(false))).booleanValue())
/*  733 */         return false; 
/*  735 */       if (state.is(Blocks.SUNFLOWER))
/*  736 */         return (state.getValue((Property)DoublePlantBlock.HALF) == DoubleBlockHalf.UPPER); 
/*  738 */       return true;
/*      */     } 
/*  740 */     return false;
/*      */   }
/*      */   
/*      */   public void registerDebugValues(ServerLevel level, DebugValueSource.Registration registration) {
/*  745 */     super.registerDebugValues(level, registration);
/*  746 */     registration.register(DebugSubscriptions.BEES, () -> new DebugBeeInfo(Optional.ofNullable(getHivePos()), Optional.ofNullable(getSavedFlowerPos()), getTravellingTicks(), getBlacklistedHives()));
/*      */   }
/*      */   
/*      */   private class BeeHurtByOtherGoal extends HurtByTargetGoal {
/*      */     public BeeHurtByOtherGoal(Bee bee) {
/*  756 */       super((PathfinderMob)bee, new Class[0]);
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/*  762 */       return (Bee.this.isAngry() && super.canContinueToUse());
/*      */     }
/*      */     
/*      */     protected void alertOther(Mob other, LivingEntity hurtByMob) {
/*  767 */       if (other instanceof Bee && this.mob.hasLineOfSight((Entity)hurtByMob))
/*  768 */         other.setTarget(hurtByMob); 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class BeeBecomeAngryTargetGoal extends NearestAttackableTargetGoal<Player> {
/*      */     public BeeBecomeAngryTargetGoal(Bee bee) {
/*  775 */       super((Mob)bee, Player.class, 10, true, false, bee::isAngryAt);
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  780 */       return (beeCanTarget() && super.canUse());
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/*  785 */       boolean beeCanTarget = beeCanTarget();
/*  786 */       if (!beeCanTarget || this.mob.getTarget() == null) {
/*  787 */         this.targetMob = null;
/*  788 */         return false;
/*      */       } 
/*  790 */       return super.canContinueToUse();
/*      */     }
/*      */     
/*      */     private boolean beeCanTarget() {
/*  794 */       Bee bee = (Bee)this.mob;
/*  795 */       return (bee.isAngry() && !bee.hasStung());
/*      */     }
/*      */   }
/*      */   
/*      */   private abstract class BaseBeeGoal extends Goal {
/*      */     public abstract boolean canBeeUse();
/*      */     
/*      */     public abstract boolean canBeeContinueToUse();
/*      */     
/*      */     public boolean canUse() {
/*  807 */       return (canBeeUse() && !Bee.this.isAngry());
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/*  812 */       return (canBeeContinueToUse() && !Bee.this.isAngry());
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeWanderGoal extends Goal {
/*      */     public BeeWanderGoal() {
/*  819 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/*  824 */       return (Bee.this.navigation.isDone() && Bee.this.random.nextInt(10) == 0);
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/*  829 */       return Bee.this.navigation.isInProgress();
/*      */     }
/*      */     
/*      */     public void start() {
/*  834 */       Vec3 targetPos = findPos();
/*  835 */       if (targetPos != null)
/*  836 */         Bee.this.navigation.moveTo(Bee.this.navigation.createPath(BlockPos.containing((Position)targetPos), 1), 1.0D); 
/*      */     }
/*      */     
/*      */     private Vec3 findPos() {
/*      */       Vec3 wanderDirection;
/*  843 */       if (Bee.this.isHiveValid() && !Bee.this.closerThan(Bee.this.hivePos, getWanderThreshold())) {
/*  845 */         Vec3 hivePosVec = Vec3.atCenterOf((Vec3i)Bee.this.hivePos);
/*  846 */         wanderDirection = hivePosVec.subtract(Bee.this.position()).normalize();
/*      */       } else {
/*  848 */         wanderDirection = Bee.this.getViewVector(0.0F);
/*      */       } 
/*  851 */       int xzDist = 8;
/*  852 */       Vec3 groundBasedPosition = HoverRandomPos.getPos((PathfinderMob)Bee.this, 8, 7, wanderDirection.x, wanderDirection.z, 1.5707964F, 3, 1);
/*  853 */       if (groundBasedPosition != null)
/*  854 */         return groundBasedPosition; 
/*  858 */       return AirAndWaterRandomPos.getPos((PathfinderMob)Bee.this, 8, 4, -2, wanderDirection.x, wanderDirection.z, 1.5707963705062866D);
/*      */     }
/*      */     
/*      */     private int getWanderThreshold() {
/*  863 */       int distanceReduction = (Bee.this.hasHive() || Bee.this.hasSavedFlowerPos()) ? 24 : 16;
/*  864 */       return 48 - distanceReduction;
/*      */     }
/*      */   }
/*      */   
/*      */   @VisibleForDebug
/*      */   public class BeeGoToHiveGoal extends BaseBeeGoal {
/*      */     public static final int MAX_TRAVELLING_TICKS = 2400;
/*      */     
/*      */     private int travellingTicks;
/*      */     
/*      */     private static final int MAX_BLACKLISTED_TARGETS = 3;
/*      */     
/*  876 */     private final List<BlockPos> blacklistedTargets = Lists.newArrayList();
/*      */     
/*      */     private Path lastPath;
/*      */     
/*      */     private static final int TICKS_BEFORE_HIVE_DROP = 60;
/*      */     
/*      */     private int ticksStuck;
/*      */     
/*      */     public BeeGoToHiveGoal() {
/*  884 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */     
/*      */     public boolean canBeeUse() {
/*  889 */       return (Bee.this.hivePos != null && 
/*  890 */         !Bee.this.isTooFarAway(Bee.this.hivePos) && 
/*  891 */         !Bee.this.hasHome() && Bee.this
/*  892 */         .wantsToEnterHive() && 
/*  893 */         !hasReachedTarget(Bee.this.hivePos) && Bee.this
/*  894 */         .level().getBlockState(Bee.this.hivePos).is(BlockTags.BEEHIVES));
/*      */     }
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/*  899 */       return canBeeUse();
/*      */     }
/*      */     
/*      */     public void start() {
/*  904 */       this.travellingTicks = 0;
/*  905 */       this.ticksStuck = 0;
/*  906 */       super.start();
/*      */     }
/*      */     
/*      */     public void stop() {
/*  911 */       this.travellingTicks = 0;
/*  912 */       this.ticksStuck = 0;
/*  913 */       Bee.this.navigation.stop();
/*  914 */       Bee.this.navigation.resetMaxVisitedNodesMultiplier();
/*      */     }
/*      */     
/*      */     public void tick() {
/*  919 */       if (Bee.this.hivePos == null)
/*      */         return; 
/*  923 */       this.travellingTicks++;
/*  925 */       if (this.travellingTicks > adjustedTickDelay(2400)) {
/*  927 */         dropAndBlacklistHive();
/*      */         return;
/*      */       } 
/*  931 */       if (Bee.this.navigation.isInProgress())
/*      */         return; 
/*  937 */       if (Bee.this.closerThan(Bee.this.hivePos, 16)) {
/*  939 */         boolean canReachAllTheWayToTarget = pathfindDirectlyTowards(Bee.this.hivePos);
/*  940 */         if (!canReachAllTheWayToTarget) {
/*  942 */           dropAndBlacklistHive();
/*  944 */         } else if (this.lastPath != null && Bee.this.navigation.getPath().sameAs(this.lastPath)) {
/*  950 */           this.ticksStuck++;
/*  952 */           if (this.ticksStuck > 60) {
/*  953 */             Bee.this.dropHive();
/*  954 */             this.ticksStuck = 0;
/*      */           } 
/*      */         } else {
/*  958 */           this.lastPath = Bee.this.navigation.getPath();
/*      */         } 
/*      */         return;
/*      */       } 
/*  964 */       if (Bee.this.isTooFarAway(Bee.this.hivePos)) {
/*  966 */         Bee.this.dropHive();
/*      */         return;
/*      */       } 
/*  973 */       Bee.this.pathfindRandomlyTowards(Bee.this.hivePos);
/*      */     }
/*      */     
/*      */     private boolean pathfindDirectlyTowards(BlockPos targetPos) {
/*  978 */       int closeEnough = Bee.this.closerThan(targetPos, 3) ? 1 : 2;
/*  979 */       Bee.this.navigation.setMaxVisitedNodesMultiplier(10.0F);
/*  980 */       Bee.this.navigation.moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), closeEnough, 1.0D);
/*  981 */       return (Bee.this.navigation.getPath() != null && Bee.this.navigation.getPath().canReach());
/*      */     }
/*      */     
/*      */     private boolean isTargetBlacklisted(BlockPos targetPos) {
/*  985 */       return this.blacklistedTargets.contains(targetPos);
/*      */     }
/*      */     
/*      */     private void blacklistTarget(BlockPos targetPos) {
/*  989 */       this.blacklistedTargets.add(targetPos);
/*  990 */       while (this.blacklistedTargets.size() > 3)
/*  991 */         this.blacklistedTargets.remove(0); 
/*      */     }
/*      */     
/*      */     private void clearBlacklist() {
/*  996 */       this.blacklistedTargets.clear();
/*      */     }
/*      */     
/*      */     private void dropAndBlacklistHive() {
/* 1000 */       if (Bee.this.hivePos != null)
/* 1001 */         blacklistTarget(Bee.this.hivePos); 
/* 1003 */       Bee.this.dropHive();
/*      */     }
/*      */     
/*      */     private boolean hasReachedTarget(BlockPos targetPos) {
/* 1007 */       if (Bee.this.closerThan(targetPos, 2))
/* 1008 */         return true; 
/* 1010 */       Path path = Bee.this.navigation.getPath();
/* 1011 */       return (path != null && path.getTarget().equals(targetPos) && path.canReach() && path.isDone());
/*      */     }
/*      */   }
/*      */   
/*      */   public class BeeGoToKnownFlowerGoal extends BaseBeeGoal {
/*      */     private static final int MAX_TRAVELLING_TICKS = 2400;
/*      */     
/*      */     private int travellingTicks;
/*      */     
/*      */     public BeeGoToKnownFlowerGoal() {
/* 1023 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */     
/*      */     public boolean canBeeUse() {
/* 1028 */       return (Bee.this.savedFlowerPos != null && 
/* 1029 */         !Bee.this.hasHome() && 
/* 1030 */         wantsToGoToKnownFlower() && 
/* 1031 */         !Bee.this.closerThan(Bee.this.savedFlowerPos, 2));
/*      */     }
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1036 */       return canBeeUse();
/*      */     }
/*      */     
/*      */     public void start() {
/* 1041 */       this.travellingTicks = 0;
/* 1042 */       super.start();
/*      */     }
/*      */     
/*      */     public void stop() {
/* 1047 */       this.travellingTicks = 0;
/* 1048 */       Bee.this.navigation.stop();
/* 1049 */       Bee.this.navigation.resetMaxVisitedNodesMultiplier();
/*      */     }
/*      */     
/*      */     public void tick() {
/* 1054 */       if (Bee.this.savedFlowerPos == null)
/*      */         return; 
/* 1057 */       this.travellingTicks++;
/* 1059 */       if (this.travellingTicks > adjustedTickDelay(2400)) {
/* 1061 */         Bee.this.dropFlower();
/*      */         return;
/*      */       } 
/* 1065 */       if (Bee.this.navigation.isInProgress())
/*      */         return; 
/* 1070 */       if (Bee.this.isTooFarAway(Bee.this.savedFlowerPos)) {
/* 1072 */         Bee.this.dropFlower();
/*      */         return;
/*      */       } 
/* 1079 */       Bee.this.pathfindRandomlyTowards(Bee.this.savedFlowerPos);
/*      */     }
/*      */     
/*      */     private boolean wantsToGoToKnownFlower() {
/* 1083 */       return (Bee.this.ticksWithoutNectarSinceExitingHive > 600);
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeLookControl extends LookControl {
/*      */     public BeeLookControl(Mob mob) {
/* 1089 */       super(mob);
/*      */     }
/*      */     
/*      */     public void tick() {
/* 1094 */       if (Bee.this.isAngry())
/*      */         return; 
/* 1097 */       super.tick();
/*      */     }
/*      */     
/*      */     protected boolean resetXRotOnTick() {
/* 1102 */       return !Bee.this.beePollinateGoal.isPollinating();
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeePollinateGoal extends BaseBeeGoal {
/*      */     private static final int MIN_POLLINATION_TICKS = 400;
/*      */     
/*      */     private static final double ARRIVAL_THRESHOLD = 0.1D;
/*      */     
/*      */     private static final int POSITION_CHANGE_CHANCE = 25;
/*      */     
/*      */     private static final float SPEED_MODIFIER = 0.35F;
/*      */     
/*      */     private static final float HOVER_HEIGHT_WITHIN_FLOWER = 0.6F;
/*      */     
/*      */     private static final float HOVER_POS_OFFSET = 0.33333334F;
/*      */     
/*      */     private static final int FLOWER_SEARCH_RADIUS = 5;
/*      */     
/*      */     private int successfulPollinatingTicks;
/*      */     
/*      */     private int lastSoundPlayedTick;
/*      */     
/*      */     private boolean pollinating;
/*      */     
/*      */     private Vec3 hoverPos;
/*      */     
/*      */     private int pollinatingTicks;
/*      */     
/*      */     private static final int MAX_POLLINATING_TICKS = 600;
/*      */     
/* 1126 */     private Long2LongOpenHashMap unreachableFlowerCache = new Long2LongOpenHashMap();
/*      */     
/*      */     public BeePollinateGoal() {
/* 1129 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*      */     }
/*      */     
/*      */     public boolean canBeeUse() {
/* 1134 */       if (Bee.this.remainingCooldownBeforeLocatingNewFlower > 0)
/* 1135 */         return false; 
/* 1138 */       if (Bee.this.hasNectar())
/* 1139 */         return false; 
/* 1141 */       if (Bee.this.level().isRaining())
/* 1142 */         return false; 
/* 1146 */       Optional<BlockPos> nearbyPos = findNearbyFlower();
/* 1147 */       if (nearbyPos.isPresent()) {
/* 1148 */         Bee.this.savedFlowerPos = nearbyPos.get();
/* 1150 */         Bee.this.navigation.moveTo(Bee.this.savedFlowerPos.getX() + 0.5D, Bee.this.savedFlowerPos.getY() + 0.5D, Bee.this.savedFlowerPos.getZ() + 0.5D, 1.2000000476837158D);
/* 1151 */         return true;
/*      */       } 
/* 1155 */       Bee.this.remainingCooldownBeforeLocatingNewFlower = Mth.nextInt(Bee.this.random, 20, 60);
/* 1156 */       return false;
/*      */     }
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1161 */       if (!this.pollinating)
/* 1162 */         return false; 
/* 1164 */       if (!Bee.this.hasSavedFlowerPos())
/* 1165 */         return false; 
/* 1167 */       if (Bee.this.level().isRaining())
/* 1168 */         return false; 
/* 1170 */       if (hasPollinatedLongEnough())
/* 1171 */         return (Bee.this.random.nextFloat() < 0.2F); 
/* 1173 */       return true;
/*      */     }
/*      */     
/*      */     private boolean hasPollinatedLongEnough() {
/* 1177 */       return (this.successfulPollinatingTicks > 400);
/*      */     }
/*      */     
/*      */     private boolean isPollinating() {
/* 1181 */       return this.pollinating;
/*      */     }
/*      */     
/*      */     private void stopPollinating() {
/* 1185 */       this.pollinating = false;
/*      */     }
/*      */     
/*      */     public void start() {
/* 1190 */       this.successfulPollinatingTicks = 0;
/* 1191 */       this.pollinatingTicks = 0;
/* 1192 */       this.lastSoundPlayedTick = 0;
/* 1193 */       this.pollinating = true;
/* 1194 */       Bee.this.resetTicksWithoutNectarSinceExitingHive();
/*      */     }
/*      */     
/*      */     public void stop() {
/* 1199 */       if (hasPollinatedLongEnough())
/* 1200 */         Bee.this.setHasNectar(true); 
/* 1202 */       this.pollinating = false;
/* 1203 */       Bee.this.navigation.stop();
/* 1205 */       Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */     }
/*      */     
/*      */     public boolean requiresUpdateEveryTick() {
/* 1210 */       return true;
/*      */     }
/*      */     
/*      */     public void tick() {
/* 1216 */       if (!Bee.this.hasSavedFlowerPos())
/*      */         return; 
/* 1220 */       this.pollinatingTicks++;
/* 1221 */       if (this.pollinatingTicks > 600) {
/* 1223 */         Bee.this.dropFlower();
/* 1224 */         this.pollinating = false;
/* 1225 */         Bee.this.remainingCooldownBeforeLocatingNewFlower = 200;
/*      */         return;
/*      */       } 
/* 1229 */       Vec3 flowerPos = Vec3.atBottomCenterOf((Vec3i)Bee.this.savedFlowerPos).add(0.0D, 0.6000000238418579D, 0.0D);
/* 1231 */       if (flowerPos.distanceTo(Bee.this.position()) > 1.0D) {
/* 1232 */         this.hoverPos = flowerPos;
/* 1233 */         setWantedPos();
/*      */         return;
/*      */       } 
/* 1237 */       if (this.hoverPos == null)
/* 1238 */         this.hoverPos = flowerPos; 
/* 1241 */       boolean arrivedAtHoverPos = (Bee.this.position().distanceTo(this.hoverPos) <= 0.1D);
/* 1242 */       boolean shouldSetWantedPos = true;
/* 1244 */       if (!arrivedAtHoverPos && this.pollinatingTicks > 600) {
/* 1246 */         Bee.this.dropFlower();
/*      */         return;
/*      */       } 
/* 1250 */       if (arrivedAtHoverPos) {
/* 1251 */         boolean shouldChangeHoverPositions = (Bee.this.random.nextInt(25) == 0);
/* 1252 */         if (shouldChangeHoverPositions) {
/* 1253 */           this.hoverPos = new Vec3(flowerPos.x() + getOffset(), flowerPos.y(), flowerPos.z() + getOffset());
/* 1255 */           Bee.this.navigation.stop();
/*      */         } else {
/* 1257 */           shouldSetWantedPos = false;
/*      */         } 
/* 1260 */         Bee.this.getLookControl().setLookAt(flowerPos.x(), flowerPos.y(), flowerPos.z());
/*      */       } 
/* 1263 */       if (shouldSetWantedPos)
/* 1264 */         setWantedPos(); 
/* 1267 */       this.successfulPollinatingTicks++;
/* 1269 */       if (Bee.this.random.nextFloat() < 0.05F && this.successfulPollinatingTicks > this.lastSoundPlayedTick + 60) {
/* 1270 */         this.lastSoundPlayedTick = this.successfulPollinatingTicks;
/* 1271 */         Bee.this.playSound(SoundEvents.BEE_POLLINATE, 1.0F, 1.0F);
/*      */       } 
/*      */     }
/*      */     
/*      */     private void setWantedPos() {
/* 1276 */       Bee.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.3499999940395355D);
/*      */     }
/*      */     
/*      */     private float getOffset() {
/* 1280 */       return (Bee.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
/*      */     }
/*      */     
/*      */     private Optional<BlockPos> findNearbyFlower() {
/* 1284 */       Iterable<BlockPos> closestNearbyFlowers = BlockPos.withinManhattan(Bee.this.blockPosition(), 5, 5, 5);
/* 1285 */       Long2LongOpenHashMap tempCache = new Long2LongOpenHashMap();
/* 1287 */       for (BlockPos pos : closestNearbyFlowers) {
/* 1289 */         long unreachableUntilTime = this.unreachableFlowerCache.getOrDefault(pos.asLong(), Long.MIN_VALUE);
/* 1290 */         if (Bee.this.level().getGameTime() < unreachableUntilTime) {
/* 1291 */           tempCache.put(pos.asLong(), unreachableUntilTime);
/*      */           continue;
/*      */         } 
/* 1294 */         if (Bee.attractsBees(Bee.this.level().getBlockState(pos))) {
/* 1295 */           Path path = Bee.this.navigation.createPath(pos, 1);
/* 1296 */           if (path != null && path.canReach())
/* 1297 */             return Optional.of(pos); 
/* 1299 */           tempCache.put(pos.asLong(), Bee.this.level().getGameTime() + 600L);
/*      */         } 
/*      */       } 
/* 1303 */       this.unreachableFlowerCache = tempCache;
/* 1304 */       return Optional.empty();
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeLocateHiveGoal extends BaseBeeGoal {
/*      */     public boolean canBeeUse() {
/* 1312 */       return (Bee.this.remainingCooldownBeforeLocatingNewHive == 0 && 
/* 1313 */         !Bee.this.hasHive() && Bee.this
/* 1314 */         .wantsToEnterHive());
/*      */     }
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1319 */       return false;
/*      */     }
/*      */     
/*      */     public void start() {
/* 1324 */       Bee.this.remainingCooldownBeforeLocatingNewHive = 200;
/* 1327 */       List<BlockPos> hivesWithSpace = findNearbyHivesWithSpace();
/* 1329 */       if (hivesWithSpace.isEmpty())
/*      */         return; 
/* 1335 */       for (BlockPos posToCheck : hivesWithSpace) {
/* 1336 */         if (!Bee.this.goToHiveGoal.isTargetBlacklisted(posToCheck)) {
/* 1338 */           Bee.this.hivePos = posToCheck;
/*      */           return;
/*      */         } 
/*      */       } 
/* 1346 */       Bee.this.goToHiveGoal.clearBlacklist();
/* 1347 */       Bee.this.hivePos = hivesWithSpace.get(0);
/*      */     }
/*      */     
/*      */     private List<BlockPos> findNearbyHivesWithSpace() {
/* 1351 */       BlockPos beePos = Bee.this.blockPosition();
/* 1352 */       PoiManager poiManager = ((ServerLevel)Bee.this.level()).getPoiManager();
/* 1353 */       Stream<PoiRecord> nearbyHives = poiManager.getInRange(p -> p.is(PoiTypeTags.BEE_HOME), beePos, 20, PoiManager.Occupancy.ANY);
/* 1355 */       Objects.requireNonNull(Bee.this);
/* 1355 */       return (List<BlockPos>)nearbyHives.map(PoiRecord::getPos).filter(Bee.this::doesHiveHaveSpace)
/* 1356 */         .sorted(Comparator.comparingDouble(pos -> pos.distSqr((Vec3i)beePos))).collect(Collectors.toList());
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeGrowCropGoal extends BaseBeeGoal {
/*      */     private static final int GROW_CHANCE = 30;
/*      */     
/*      */     public boolean canBeeUse() {
/* 1365 */       if (Bee.this.getCropsGrownSincePollination() >= 10)
/* 1366 */         return false; 
/* 1369 */       if (Bee.this.random.nextFloat() < 0.3F)
/* 1370 */         return false; 
/* 1374 */       return (Bee.this.hasNectar() && Bee.this.isHiveValid());
/*      */     }
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1379 */       return canBeeUse();
/*      */     }
/*      */     
/*      */     public void tick() {
/* 1384 */       if (Bee.this.random.nextInt(adjustedTickDelay(30)) != 0)
/*      */         return; 
/* 1388 */       for (int i = 1; i <= 2; i++) {
/* 1389 */         BlockPos belowPos = Bee.this.blockPosition().below(i);
/* 1390 */         BlockState belowState = Bee.this.level().getBlockState(belowPos);
/* 1391 */         Block belowBlock = belowState.getBlock();
/* 1392 */         BlockState growState = null;
/* 1393 */         if (belowState.is(BlockTags.BEE_GROWABLES)) {
/* 1394 */           if (belowBlock instanceof CropBlock) {
/* 1394 */             CropBlock cropBlockBelow = (CropBlock)belowBlock;
/* 1395 */             if (!cropBlockBelow.isMaxAge(belowState))
/* 1396 */               growState = cropBlockBelow.getStateForAge(cropBlockBelow.getAge(belowState) + 1); 
/* 1398 */           } else if (belowBlock instanceof StemBlock) {
/* 1399 */             int age = ((Integer)belowState.getValue((Property)StemBlock.AGE)).intValue();
/* 1400 */             if (age < 7)
/* 1401 */               growState = (BlockState)belowState.setValue((Property)StemBlock.AGE, Integer.valueOf(age + 1)); 
/* 1403 */           } else if (belowState.is(Blocks.SWEET_BERRY_BUSH)) {
/* 1404 */             int age = ((Integer)belowState.getValue((Property)SweetBerryBushBlock.AGE)).intValue();
/* 1405 */             if (age < 3)
/* 1406 */               growState = (BlockState)belowState.setValue((Property)SweetBerryBushBlock.AGE, Integer.valueOf(age + 1)); 
/* 1408 */           } else if (belowState.is(Blocks.CAVE_VINES) || belowState.is(Blocks.CAVE_VINES_PLANT)) {
/* 1409 */             BonemealableBlock bonemealableBlock = (BonemealableBlock)belowState.getBlock();
/* 1410 */             if (bonemealableBlock.isValidBonemealTarget((LevelReader)Bee.this.level(), belowPos, belowState)) {
/* 1411 */               bonemealableBlock.performBonemeal((ServerLevel)Bee.this.level(), Bee.this.random, belowPos, belowState);
/* 1412 */               growState = Bee.this.level().getBlockState(belowPos);
/*      */             } 
/*      */           } 
/* 1416 */           if (growState != null) {
/* 1417 */             Bee.this.level().levelEvent(2011, belowPos, 15);
/* 1418 */             Bee.this.level().setBlockAndUpdate(belowPos, growState);
/* 1419 */             Bee.this.incrementNumCropsGrownSincePollination();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeAttackGoal extends MeleeAttackGoal {
/*      */     public BeeAttackGoal(PathfinderMob mob, double speedModifier, boolean trackTarget) {
/* 1428 */       super(mob, speedModifier, trackTarget);
/*      */     }
/*      */     
/*      */     public boolean canUse() {
/* 1433 */       return (super.canUse() && Bee.this.isAngry() && !Bee.this.hasStung());
/*      */     }
/*      */     
/*      */     public boolean canContinueToUse() {
/* 1438 */       return (super.canContinueToUse() && Bee.this.isAngry() && !Bee.this.hasStung());
/*      */     }
/*      */   }
/*      */   
/*      */   private class BeeEnterHiveGoal extends BaseBeeGoal {
/*      */     public boolean canBeeUse() {
/* 1446 */       if (Bee.this.hivePos != null && Bee.this.wantsToEnterHive() && Bee.this.hivePos.closerToCenterThan((Position)Bee.this.position(), 2.0D)) {
/* 1447 */         BeehiveBlockEntity beehiveBlockEntity = Bee.this.getBeehiveBlockEntity();
/* 1448 */         if (beehiveBlockEntity != null)
/* 1449 */           if (beehiveBlockEntity.isFull()) {
/* 1450 */             Bee.this.hivePos = null;
/*      */           } else {
/* 1452 */             return true;
/*      */           }  
/*      */       } 
/* 1456 */       return false;
/*      */     }
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1461 */       return false;
/*      */     }
/*      */     
/*      */     public void start() {
/* 1466 */       BeehiveBlockEntity beehiveBlockEntity = Bee.this.getBeehiveBlockEntity();
/* 1467 */       if (beehiveBlockEntity != null)
/* 1468 */         beehiveBlockEntity.addOccupant(Bee.this); 
/*      */     }
/*      */   }
/*      */   
/*      */   private class ValidateFlowerGoal extends BaseBeeGoal {
/*      */     private final int validateFlowerCooldown;
/*      */     
/*      */     private long lastValidateTick;
/*      */     
/*      */     private ValidateFlowerGoal() {
/* 1474 */       this.validateFlowerCooldown = Mth.nextInt(Bee.this.random, 20, 40);
/* 1476 */       this.lastValidateTick = -1L;
/*      */     }
/*      */     
/*      */     public void start() {
/* 1481 */       if (Bee.this.savedFlowerPos != null && Bee.this.level().isLoaded(Bee.this.savedFlowerPos) && !isFlower(Bee.this.savedFlowerPos))
/* 1482 */         Bee.this.dropFlower(); 
/* 1484 */       this.lastValidateTick = Bee.this.level().getGameTime();
/*      */     }
/*      */     
/*      */     public boolean canBeeUse() {
/* 1489 */       return (Bee.this.level().getGameTime() > this.lastValidateTick + this.validateFlowerCooldown);
/*      */     }
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1494 */       return false;
/*      */     }
/*      */     
/*      */     private boolean isFlower(BlockPos flowerPos) {
/* 1498 */       return Bee.attractsBees(Bee.this.level().getBlockState(flowerPos));
/*      */     }
/*      */   }
/*      */   
/*      */   private class ValidateHiveGoal extends BaseBeeGoal {
/*      */     private final int VALIDATE_HIVE_COOLDOWN;
/*      */     
/*      */     private long lastValidateTick;
/*      */     
/*      */     private ValidateHiveGoal() {
/* 1503 */       this.VALIDATE_HIVE_COOLDOWN = Mth.nextInt(Bee.this.random, 20, 40);
/* 1505 */       this.lastValidateTick = -1L;
/*      */     }
/*      */     
/*      */     public void start() {
/* 1509 */       if (Bee.this.hivePos != null && Bee.this.level().isLoaded(Bee.this.hivePos) && !Bee.this.isHiveValid())
/* 1510 */         Bee.this.dropHive(); 
/* 1512 */       this.lastValidateTick = Bee.this.level().getGameTime();
/*      */     }
/*      */     
/*      */     public boolean canBeeUse() {
/* 1517 */       return (Bee.this.level().getGameTime() > this.lastValidateTick + this.VALIDATE_HIVE_COOLDOWN);
/*      */     }
/*      */     
/*      */     public boolean canBeeContinueToUse() {
/* 1522 */       return false;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\animal\bee\Bee.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */