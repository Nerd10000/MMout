/*     */ package net.minecraft.world.entity.npc.villager;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiPredicate;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.SpawnUtil;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.clock.ClockManager;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ReputationEventHandler;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.ActivityData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.VillagerGoalPackages;
/*     */ import net.minecraft.world.entity.ai.gossip.GossipContainer;
/*     */ import net.minecraft.world.entity.ai.gossip.GossipType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.sensing.GolemSensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.ai.village.ReputationEventType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiType;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.Witch;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.raid.Raid;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.trading.MerchantOffer;
/*     */ import net.minecraft.world.item.trading.MerchantOffers;
/*     */ import net.minecraft.world.item.trading.TradeSet;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.timeline.Timeline;
/*     */ import net.minecraft.world.timeline.Timelines;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class Villager extends AbstractVillager implements VillagerDataHolder, ReputationEventHandler {
/*  99 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/* 101 */   private static final EntityDataAccessor<VillagerData> DATA_VILLAGER_DATA = SynchedEntityData.defineId(Villager.class, EntityDataSerializers.VILLAGER_DATA);
/*     */   
/* 102 */   private static final EntityDataAccessor<Boolean> DATA_VILLAGER_DATA_FINALIZED = SynchedEntityData.defineId(Villager.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   public static final int BREEDING_FOOD_THRESHOLD = 12;
/*     */   
/* 105 */   public static final Map<Item, Integer> FOOD_POINTS = (Map<Item, Integer>)ImmutableMap.of(Items.BREAD, 
/* 106 */       Integer.valueOf(4), Items.POTATO, 
/* 107 */       Integer.valueOf(1), Items.CARROT, 
/* 108 */       Integer.valueOf(1), Items.BEETROOT, 
/* 109 */       Integer.valueOf(1));
/*     */   
/*     */   private static final int MAX_GOSSIP_TOPICS = 10;
/*     */   
/*     */   private static final int GOSSIP_COOLDOWN = 1200;
/*     */   
/*     */   private static final int GOSSIP_DECAY_INTERVAL = 24000;
/*     */   
/*     */   private static final int HOW_FAR_AWAY_TO_TALK_TO_OTHER_VILLAGERS_ABOUT_GOLEMS = 10;
/*     */   
/*     */   private static final int HOW_MANY_VILLAGERS_NEED_TO_AGREE_TO_SPAWN_A_GOLEM = 5;
/*     */   
/*     */   private static final long TIME_SINCE_SLEEPING_FOR_GOLEM_SPAWNING = 24000L;
/*     */   
/*     */   @VisibleForTesting
/*     */   public static final float SPEED_MODIFIER = 0.5F;
/*     */   
/*     */   private static final int DEFAULT_XP = 0;
/*     */   
/*     */   private static final byte DEFAULT_FOOD_LEVEL = 0;
/*     */   
/*     */   private static final int DEFAULT_LAST_RESTOCK = 0;
/*     */   
/*     */   private static final int DEFAULT_LAST_GOSSIP_DECAY = 0;
/*     */   
/*     */   private static final int DEFAULT_RESTOCKS_TODAY = 0;
/*     */   
/*     */   private static final boolean DEFAULT_ASSIGN_PROFESSION_WHEN_SPAWNED = false;
/*     */   
/* 132 */   private static final EntityDimensions BABY_DIMENSIONS = EntityDimensions.scalable(0.49F, 0.98F).withEyeHeight(0.63F);
/*     */   
/*     */   private int updateMerchantTimer;
/*     */   
/*     */   private boolean increaseProfessionLevelOnUpdate;
/*     */   
/*     */   private Player lastTradedPlayer;
/*     */   
/* 138 */   private int foodLevel = 0;
/*     */   
/* 140 */   private final GossipContainer gossips = new GossipContainer();
/*     */   
/*     */   private long lastGossipTime;
/*     */   
/* 143 */   private long lastGossipDecayTime = 0L;
/*     */   
/* 145 */   private int villagerXp = 0;
/*     */   
/* 146 */   private long lastRestockGameTime = 0L;
/*     */   
/* 147 */   private int numberOfRestocksToday = 0;
/*     */   
/*     */   private long lastRestockCheckDay;
/*     */   
/*     */   private boolean assignProfessionWhenSpawned = false;
/*     */   
/*     */   private static final Brain.Provider<Villager> BRAIN_PROVIDER;
/*     */   
/*     */   public static final Map<MemoryModuleType<GlobalPos>, BiPredicate<Villager, Holder<PoiType>>> POI_MEMORIES;
/*     */   
/*     */   static {
/* 151 */     BRAIN_PROVIDER = Brain.provider(
/* 152 */         List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.NEAREST_BED, SensorType.HURT_BY, SensorType.VILLAGER_HOSTILES, SensorType.VILLAGER_BABIES, SensorType.SECONDARY_POIS, SensorType.GOLEM_DETECTED), body -> {
/*     */           Holder<VillagerProfession> profession = body.getVillagerData().profession();
/*     */           List<ActivityData<Villager>> activities = new ArrayList<>();
/*     */           if (body.isBaby()) {
/*     */             activities.add(ActivityData.create(Activity.PLAY, VillagerGoalPackages.getPlayPackage(0.5F)));
/*     */           } else {
/*     */             activities.add(ActivityData.create(Activity.WORK, VillagerGoalPackages.getWorkPackage(profession, 0.5F), (Set)ImmutableSet.of(Pair.of(MemoryModuleType.JOB_SITE, MemoryStatus.VALUE_PRESENT))));
/*     */           } 
/*     */           activities.add(ActivityData.create(Activity.CORE, VillagerGoalPackages.getCorePackage(profession, 0.5F)));
/*     */           activities.add(ActivityData.create(Activity.MEET, VillagerGoalPackages.getMeetPackage(0.5F), (Set)ImmutableSet.of(Pair.of(MemoryModuleType.MEETING_POINT, MemoryStatus.VALUE_PRESENT))));
/*     */           activities.add(ActivityData.create(Activity.REST, VillagerGoalPackages.getRestPackage(0.5F)));
/*     */           activities.add(ActivityData.create(Activity.IDLE, VillagerGoalPackages.getIdlePackage(0.5F)));
/*     */           activities.add(ActivityData.create(Activity.PANIC, VillagerGoalPackages.getPanicPackage(0.5F)));
/*     */           activities.add(ActivityData.create(Activity.PRE_RAID, VillagerGoalPackages.getPreRaidPackage(0.5F)));
/*     */           activities.add(ActivityData.create(Activity.RAID, VillagerGoalPackages.getRaidPackage(0.5F)));
/*     */           activities.add(ActivityData.create(Activity.HIDE, VillagerGoalPackages.getHidePackage(0.5F)));
/*     */           return activities;
/*     */         });
/* 183 */     POI_MEMORIES = (Map<MemoryModuleType<GlobalPos>, BiPredicate<Villager, Holder<PoiType>>>)ImmutableMap.of(MemoryModuleType.HOME, (villager, poiType) -> poiType.is(PoiTypes.HOME), MemoryModuleType.JOB_SITE, (villager, poiType) -> ((VillagerProfession)villager.getVillagerData().profession().value()).heldJobSite().test(poiType), MemoryModuleType.POTENTIAL_JOB_SITE, (villager, poiType) -> VillagerProfession.ALL_ACQUIRABLE_JOBS.test(poiType), MemoryModuleType.MEETING_POINT, (villager, poiType) -> poiType.is(PoiTypes.MEETING));
/*     */   }
/*     */   
/*     */   public Villager(EntityType<? extends Villager> entityType, Level level) {
/* 191 */     super((EntityType)entityType, level);
/* 192 */     getNavigation().setCanOpenDoors(true);
/* 193 */     getNavigation().setCanFloat(true);
/* 194 */     getNavigation().setRequiredPathLength(48.0F);
/* 195 */     setCanPickUpLoot(true);
/*     */   }
/*     */   
/*     */   public Brain<Villager> getBrain() {
/* 201 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected Brain<Villager> makeBrain(Brain.Packed packedBrain) {
/* 206 */     Brain<Villager> brain = BRAIN_PROVIDER.makeBrain((LivingEntity)this, packedBrain);
/* 207 */     registerBrainGoals(brain);
/* 208 */     return brain;
/*     */   }
/*     */   
/*     */   public void refreshBrain(ServerLevel level) {
/* 212 */     Brain<Villager> oldBrain = getBrain();
/* 213 */     oldBrain.stopAll(level, (LivingEntity)this);
/* 214 */     this.brain = BRAIN_PROVIDER.makeBrain((LivingEntity)this, oldBrain.pack());
/* 215 */     registerBrainGoals(getBrain());
/*     */   }
/*     */   
/*     */   private void registerBrainGoals(Brain<Villager> brain) {
/* 219 */     if (isBaby()) {
/* 220 */       brain.setSchedule(EnvironmentAttributes.BABY_VILLAGER_ACTIVITY);
/*     */     } else {
/* 222 */       brain.setSchedule(EnvironmentAttributes.VILLAGER_ACTIVITY);
/*     */     } 
/* 224 */     brain.updateActivityFromSchedule(level().environmentAttributes(), level().getGameTime(), position());
/*     */   }
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 229 */     super.ageBoundaryReached();
/* 230 */     if (level() instanceof ServerLevel)
/* 231 */       refreshBrain((ServerLevel)level()); 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 236 */     return Mob.createMobAttributes()
/* 237 */       .add(Attributes.MOVEMENT_SPEED, 0.5D);
/*     */   }
/*     */   
/*     */   public boolean assignProfessionWhenSpawned() {
/* 241 */     return this.assignProfessionWhenSpawned;
/*     */   }
/*     */   
/*     */   protected void customServerAiStep(ServerLevel level) {
/* 246 */     ProfilerFiller profiler = Profiler.get();
/* 247 */     profiler.push("villagerBrain");
/* 248 */     getBrain().tick(level, (LivingEntity)this);
/* 249 */     profiler.pop();
/* 251 */     if (this.assignProfessionWhenSpawned)
/* 252 */       this.assignProfessionWhenSpawned = false; 
/* 255 */     if (!isTrading() && this.updateMerchantTimer > 0) {
/* 256 */       this.updateMerchantTimer--;
/* 257 */       if (this.updateMerchantTimer <= 0) {
/* 258 */         if (this.increaseProfessionLevelOnUpdate) {
/* 259 */           increaseMerchantCareer(level);
/* 260 */           this.increaseProfessionLevelOnUpdate = false;
/*     */         } 
/* 262 */         addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 0));
/*     */       } 
/*     */     } 
/* 266 */     if (this.lastTradedPlayer != null) {
/* 267 */       level.onReputationEvent(ReputationEventType.TRADE, (Entity)this.lastTradedPlayer, this);
/* 268 */       level.broadcastEntityEvent((Entity)this, (byte)14);
/* 269 */       this.lastTradedPlayer = null;
/*     */     } 
/* 273 */     if (!isNoAi() && this.random.nextInt(100) == 0) {
/* 274 */       Raid raid = level.getRaidAt(blockPosition());
/* 275 */       if (raid != null && raid.isActive() && !raid.isOver())
/* 276 */         level.broadcastEntityEvent((Entity)this, (byte)42); 
/*     */     } 
/* 280 */     if (getVillagerData().profession().is(VillagerProfession.NONE) && isTrading())
/* 281 */       stopTrading(); 
/* 284 */     super.customServerAiStep(level);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 289 */     super.tick();
/* 291 */     if (getUnhappyCounter() > 0)
/* 292 */       setUnhappyCounter(getUnhappyCounter() - 1); 
/* 295 */     maybeDecayGossip();
/*     */   }
/*     */   
/*     */   public InteractionResult mobInteract(Player player, InteractionHand hand) {
/* 301 */     ItemStack itemStack = player.getItemInHand(hand);
/* 302 */     if (!itemStack.is(Items.VILLAGER_SPAWN_EGG) && isAlive() && !isTrading() && !isSleeping()) {
/* 303 */       if (isBaby()) {
/* 304 */         setUnhappy();
/* 306 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/* 309 */       if (!level().isClientSide()) {
/* 310 */         boolean noOffers = getOffers().isEmpty();
/* 313 */         if (hand == InteractionHand.MAIN_HAND) {
/* 314 */           if (noOffers)
/* 315 */             setUnhappy(); 
/* 317 */           player.awardStat(Stats.TALKED_TO_VILLAGER);
/*     */         } 
/* 320 */         if (noOffers)
/* 321 */           return (InteractionResult)InteractionResult.CONSUME; 
/* 325 */         startTrading(player);
/*     */       } 
/* 328 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 330 */     return super.mobInteract(player, hand);
/*     */   }
/*     */   
/*     */   private void setUnhappy() {
/* 334 */     setUnhappyCounter(40);
/* 335 */     if (!level().isClientSide())
/* 336 */       makeSound(SoundEvents.VILLAGER_NO); 
/*     */   }
/*     */   
/*     */   private void startTrading(Player player) {
/* 341 */     updateSpecialPrices(player);
/* 342 */     setTradingPlayer(player);
/* 343 */     openTradingScreen(player, getDisplayName(), getVillagerData().level());
/*     */   }
/*     */   
/*     */   public void setTradingPlayer(Player player) {
/* 348 */     boolean shouldStop = (getTradingPlayer() != null && player == null);
/* 349 */     super.setTradingPlayer(player);
/* 350 */     if (shouldStop)
/* 351 */       stopTrading(); 
/*     */   }
/*     */   
/*     */   protected void stopTrading() {
/* 357 */     super.stopTrading();
/* 358 */     resetSpecialPrices();
/*     */   }
/*     */   
/*     */   private void resetSpecialPrices() {
/* 362 */     if (level().isClientSide())
/*     */       return; 
/* 365 */     for (MerchantOffer offer : getOffers())
/* 366 */       offer.resetSpecialPriceDiff(); 
/*     */   }
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose pose) {
/* 372 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
/*     */   }
/*     */   
/*     */   public boolean canRestock() {
/* 377 */     return true;
/*     */   }
/*     */   
/*     */   public void restock() {
/* 381 */     updateDemand();
/* 382 */     for (MerchantOffer offer : getOffers())
/* 383 */       offer.resetUses(); 
/* 385 */     resendOffersToTradingPlayer();
/* 387 */     this.lastRestockGameTime = level().getGameTime();
/* 388 */     this.numberOfRestocksToday++;
/*     */   }
/*     */   
/*     */   private void resendOffersToTradingPlayer() {
/* 392 */     MerchantOffers offers = getOffers();
/* 393 */     Player tradingPlayer = getTradingPlayer();
/* 394 */     if (tradingPlayer != null && !offers.isEmpty())
/* 395 */       tradingPlayer.sendMerchantOffers(tradingPlayer.containerMenu.containerId, offers, getVillagerData().level(), getVillagerXp(), showProgressBar(), canRestock()); 
/*     */   }
/*     */   
/*     */   private boolean needsToRestock() {
/* 400 */     for (MerchantOffer offer : getOffers()) {
/* 401 */       if (offer.needsRestock())
/* 402 */         return true; 
/*     */     } 
/* 405 */     return false;
/*     */   }
/*     */   
/*     */   private boolean allowedToRestock() {
/* 410 */     return (this.numberOfRestocksToday == 0 || (this.numberOfRestocksToday < 2 && level().getGameTime() > this.lastRestockGameTime + 2400L));
/*     */   }
/*     */   
/*     */   public boolean shouldRestock(ServerLevel level) {
/* 414 */     long halfDayPassedTime = this.lastRestockGameTime + 12000L;
/* 415 */     long gameTime = level().getGameTime();
/* 416 */     boolean isNewDay = (gameTime > halfDayPassedTime);
/* 420 */     long currentDay = ((Integer)level().registryAccess().get(Timelines.OVERWORLD_DAY).map(timeline -> Integer.valueOf(((Timeline)timeline.value()).getPeriodCount((ClockManager)level.clockManager()))).orElse(Integer.valueOf(0))).intValue();
/* 421 */     int i = isNewDay | ((this.lastRestockCheckDay > 0L && currentDay > this.lastRestockCheckDay) ? 1 : 0);
/* 422 */     this.lastRestockCheckDay = currentDay;
/* 424 */     if (i != 0) {
/* 425 */       this.lastRestockGameTime = gameTime;
/* 426 */       resetNumberOfRestocks();
/*     */     } 
/* 429 */     return (allowedToRestock() && needsToRestock());
/*     */   }
/*     */   
/*     */   private void catchUpDemand() {
/* 434 */     int missedUpdates = 2 - this.numberOfRestocksToday;
/* 435 */     if (missedUpdates > 0)
/* 436 */       for (MerchantOffer offer : getOffers())
/* 437 */         offer.resetUses();  
/* 440 */     for (int i = 0; i < missedUpdates; i++)
/* 441 */       updateDemand(); 
/* 443 */     resendOffersToTradingPlayer();
/*     */   }
/*     */   
/*     */   private void updateDemand() {
/* 447 */     for (MerchantOffer offer : getOffers())
/* 448 */       offer.updateDemand(); 
/*     */   }
/*     */   
/*     */   private void updateSpecialPrices(Player player) {
/* 453 */     int reputation = getPlayerReputation(player);
/* 454 */     if (reputation != 0)
/* 455 */       for (MerchantOffer offer : getOffers())
/* 456 */         offer.addToSpecialPriceDiff(-Mth.floor(reputation * offer.getPriceMultiplier()));  
/* 460 */     if (player.hasEffect(MobEffects.HERO_OF_THE_VILLAGE)) {
/* 461 */       MobEffectInstance effect = player.getEffect(MobEffects.HERO_OF_THE_VILLAGE);
/* 462 */       int amplifier = effect.getAmplifier();
/* 463 */       for (MerchantOffer offer : getOffers()) {
/* 464 */         double modifier = 0.3D + 0.0625D * amplifier;
/* 465 */         int costReduction = (int)Math.floor(modifier * offer.getBaseCostA().getCount());
/* 466 */         offer.addToSpecialPriceDiff(-Math.max(costReduction, 1));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/* 473 */     super.defineSynchedData(entityData);
/* 474 */     entityData.define(DATA_VILLAGER_DATA, createDefaultVillagerData());
/* 475 */     entityData.define(DATA_VILLAGER_DATA_FINALIZED, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static VillagerData createDefaultVillagerData() {
/* 479 */     return new VillagerData((Holder<VillagerType>)BuiltInRegistries.VILLAGER_TYPE.getOrThrow(VillagerData.DEFAULT_TYPE), (Holder<VillagerProfession>)BuiltInRegistries.VILLAGER_PROFESSION.getOrThrow(VillagerProfession.NONE), 1);
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 484 */     super.addAdditionalSaveData(output);
/* 485 */     output.store("VillagerData", VillagerData.CODEC, getVillagerData());
/* 486 */     output.putBoolean("VillagerDataFinalized", getVillagerDataFinalized());
/* 488 */     output.putByte("FoodLevel", (byte)this.foodLevel);
/* 489 */     output.store("Gossips", GossipContainer.CODEC, this.gossips);
/* 490 */     output.putInt("Xp", this.villagerXp);
/* 491 */     output.putLong("LastRestock", this.lastRestockGameTime);
/* 492 */     output.putLong("LastGossipDecay", this.lastGossipDecayTime);
/* 493 */     output.putInt("RestocksToday", this.numberOfRestocksToday);
/* 494 */     if (this.assignProfessionWhenSpawned)
/* 495 */       output.putBoolean("AssignProfessionWhenSpawned", true); 
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 501 */     super.readAdditionalSaveData(input);
/* 503 */     Optional<VillagerData> villagerDataOptional = input.read("VillagerData", VillagerData.CODEC);
/* 504 */     if (input.getBooleanOr("VillagerDataFinalized", false) || villagerDataOptional.isPresent()) {
/* 505 */       setVillagerDataFinalized(true);
/* 506 */       VillagerData villagerData = villagerDataOptional.orElseGet(Villager::createDefaultVillagerData);
/* 507 */       this.entityData.set(DATA_VILLAGER_DATA, villagerData);
/*     */     } 
/* 510 */     this.foodLevel = input.getByteOr("FoodLevel", (byte)0);
/* 512 */     this.gossips.clear();
/* 513 */     Objects.requireNonNull(this.gossips);
/* 513 */     input.read("Gossips", GossipContainer.CODEC).ifPresent(this.gossips::putAll);
/* 515 */     this.villagerXp = input.getIntOr("Xp", 0);
/* 517 */     this.lastRestockGameTime = input.getLongOr("LastRestock", 0L);
/* 519 */     this.lastGossipDecayTime = input.getLongOr("LastGossipDecay", 0L);
/* 522 */     if (level() instanceof ServerLevel)
/* 523 */       refreshBrain((ServerLevel)level()); 
/* 526 */     this.numberOfRestocksToday = input.getIntOr("RestocksToday", 0);
/* 528 */     this.assignProfessionWhenSpawned = input.getBooleanOr("AssignProfessionWhenSpawned", false);
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 533 */     return false;
/*     */   }
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 538 */     if (isSleeping())
/* 539 */       return null; 
/* 542 */     if (isTrading())
/* 543 */       return SoundEvents.VILLAGER_TRADE; 
/* 545 */     return SoundEvents.VILLAGER_AMBIENT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource source) {
/* 550 */     return SoundEvents.VILLAGER_HURT;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 555 */     return SoundEvents.VILLAGER_DEATH;
/*     */   }
/*     */   
/*     */   public void playWorkSound() {
/* 559 */     makeSound(((VillagerProfession)getVillagerData().profession().value()).workSound());
/*     */   }
/*     */   
/*     */   public void setVillagerData(VillagerData data) {
/* 564 */     VillagerData currentData = getVillagerData();
/* 565 */     if (!currentData.profession().equals(data.profession()))
/* 566 */       this.offers = null; 
/* 569 */     this.entityData.set(DATA_VILLAGER_DATA, data);
/*     */   }
/*     */   
/*     */   public boolean getVillagerDataFinalized() {
/* 574 */     return ((Boolean)this.entityData.get(DATA_VILLAGER_DATA_FINALIZED)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setVillagerDataFinalized(boolean villagerDataFinalized) {
/* 579 */     this.entityData.set(DATA_VILLAGER_DATA_FINALIZED, Boolean.valueOf(villagerDataFinalized));
/*     */   }
/*     */   
/*     */   public VillagerData getVillagerData() {
/* 584 */     return (VillagerData)this.entityData.get(DATA_VILLAGER_DATA);
/*     */   }
/*     */   
/*     */   protected void rewardTradeXp(MerchantOffer offer) {
/* 589 */     int popXp = 3 + this.random.nextInt(4);
/* 591 */     this.villagerXp += offer.getXp();
/* 592 */     this.lastTradedPlayer = getTradingPlayer();
/* 594 */     if (shouldIncreaseLevel()) {
/* 595 */       this.updateMerchantTimer = 40;
/* 596 */       this.increaseProfessionLevelOnUpdate = true;
/* 597 */       popXp += 5;
/*     */     } 
/* 600 */     if (offer.shouldRewardExp())
/* 601 */       level().addFreshEntity((Entity)new ExperienceOrb(level(), getX(), getY() + 0.5D, getZ(), popXp)); 
/*     */   }
/*     */   
/*     */   public void setLastHurtByMob(LivingEntity hurtBy) {
/* 608 */     if (hurtBy != null && level() instanceof ServerLevel) {
/* 609 */       ((ServerLevel)level()).onReputationEvent(ReputationEventType.VILLAGER_HURT, (Entity)hurtBy, this);
/* 610 */       if (isAlive() && hurtBy instanceof Player)
/* 611 */         level().broadcastEntityEvent((Entity)this, (byte)13); 
/*     */     } 
/* 614 */     super.setLastHurtByMob(hurtBy);
/*     */   }
/*     */   
/*     */   public void die(DamageSource source) {
/* 619 */     LOGGER.info("Villager {} died, message: '{}'", this, source.getLocalizedDeathMessage((LivingEntity)this).getString());
/* 620 */     Entity murderer = source.getEntity();
/* 621 */     if (murderer != null)
/* 622 */       tellWitnessesThatIWasMurdered(murderer); 
/* 625 */     releaseAllPois();
/* 626 */     super.die(source);
/*     */   }
/*     */   
/*     */   private void releaseAllPois() {
/* 630 */     releasePoi(MemoryModuleType.HOME);
/* 631 */     releasePoi(MemoryModuleType.JOB_SITE);
/* 632 */     releasePoi(MemoryModuleType.POTENTIAL_JOB_SITE);
/* 633 */     releasePoi(MemoryModuleType.MEETING_POINT);
/*     */   }
/*     */   
/*     */   private void tellWitnessesThatIWasMurdered(Entity murderer) {
/*     */     ServerLevel serverLevel;
/* 637 */     Level level = level();
/* 637 */     if (level instanceof ServerLevel) {
/* 637 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/*     */       return;
/*     */     } 
/* 641 */     Optional<NearestVisibleLivingEntities> witnesses = this.brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/* 642 */     if (witnesses.isEmpty())
/*     */       return; 
/* 646 */     Objects.requireNonNull(ReputationEventHandler.class);
/* 646 */     ((NearestVisibleLivingEntities)witnesses.get()).findAll(ReputationEventHandler.class::isInstance)
/* 647 */       .forEach(witness -> serverLevel.onReputationEvent(ReputationEventType.VILLAGER_KILLED, murderer, (ReputationEventHandler)witness));
/*     */   }
/*     */   
/*     */   public void releasePoi(MemoryModuleType<GlobalPos> memoryType) {
/* 651 */     if (!(level() instanceof ServerLevel))
/*     */       return; 
/* 654 */     MinecraftServer server = ((ServerLevel)level()).getServer();
/* 655 */     this.brain.getMemory(memoryType).ifPresent(memory -> {
/*     */           ServerLevel poiLevel = server.getLevel(memory.dimension());
/*     */           if (poiLevel == null)
/*     */             return; 
/*     */           PoiManager poiManager = poiLevel.getPoiManager();
/*     */           Optional<Holder<PoiType>> type = poiManager.getType(memory.pos());
/*     */           BiPredicate<Villager, Holder<PoiType>> poiTypePredicate = POI_MEMORIES.get(memoryType);
/*     */           if (type.isPresent() && poiTypePredicate.test(this, type.get())) {
/*     */             poiManager.release(memory.pos());
/*     */             poiLevel.debugSynchronizers().updatePoi(memory.pos());
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean canBreed() {
/* 672 */     return (this.foodLevel + countFoodPointsInInventory() >= 12 && !isSleeping() && getAge() == 0);
/*     */   }
/*     */   
/*     */   private boolean hungry() {
/* 676 */     return (this.foodLevel < 12);
/*     */   }
/*     */   
/*     */   private void eatUntilFull() {
/* 680 */     if (!hungry() || countFoodPointsInInventory() == 0)
/*     */       return; 
/* 684 */     for (int slot = 0; slot < getInventory().getContainerSize(); slot++) {
/* 685 */       ItemStack itemStack = getInventory().getItem(slot);
/* 687 */       if (!itemStack.isEmpty()) {
/* 688 */         Integer value = FOOD_POINTS.get(itemStack.getItem());
/* 689 */         if (value != null) {
/* 690 */           int itemCount = itemStack.getCount();
/* 691 */           for (int count = itemCount; count > 0; count--) {
/* 692 */             this.foodLevel += value.intValue();
/* 693 */             getInventory().removeItem(slot, 1);
/* 695 */             if (!hungry())
/*     */               return; 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getPlayerReputation(Player player) {
/* 705 */     return this.gossips.getReputation(player.getUUID(), t -> true);
/*     */   }
/*     */   
/*     */   private void digestFood(int amount) {
/* 709 */     this.foodLevel -= amount;
/*     */   }
/*     */   
/*     */   public void eatAndDigestFood() {
/* 713 */     eatUntilFull();
/* 714 */     digestFood(12);
/*     */   }
/*     */   
/*     */   public void setOffers(MerchantOffers offers) {
/* 718 */     this.offers = offers;
/*     */   }
/*     */   
/*     */   private boolean shouldIncreaseLevel() {
/* 722 */     int currentLevel = getVillagerData().level();
/* 723 */     return (VillagerData.canLevelUp(currentLevel) && this.villagerXp >= VillagerData.getMaxXpPerLevel(currentLevel));
/*     */   }
/*     */   
/*     */   private void increaseMerchantCareer(ServerLevel level) {
/* 727 */     setVillagerData(getVillagerData().withLevel(getVillagerData().level() + 1));
/* 729 */     updateTrades(level);
/*     */   }
/*     */   
/*     */   protected Component getTypeName() {
/* 734 */     return ((VillagerProfession)getVillagerData().profession().value()).name();
/*     */   }
/*     */   
/*     */   public void handleEntityEvent(byte id) {
/* 739 */     if (id == 12) {
/* 740 */       addParticlesAroundSelf((ParticleOptions)ParticleTypes.HEART);
/* 741 */     } else if (id == 13) {
/* 742 */       addParticlesAroundSelf((ParticleOptions)ParticleTypes.ANGRY_VILLAGER);
/* 743 */     } else if (id == 14) {
/* 744 */       addParticlesAroundSelf((ParticleOptions)ParticleTypes.HAPPY_VILLAGER);
/* 745 */     } else if (id == 42) {
/* 746 */       addParticlesAroundSelf((ParticleOptions)ParticleTypes.SPLASH);
/*     */     } else {
/* 748 */       super.handleEntityEvent(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 754 */     if (spawnReason == EntitySpawnReason.BREEDING)
/* 755 */       setVillagerData(getVillagerData().withProfession((HolderGetter.Provider)level.registryAccess(), VillagerProfession.NONE)); 
/* 757 */     finalizeVillagerType(level, blockPosition());
/* 758 */     if (spawnReason == EntitySpawnReason.STRUCTURE)
/* 759 */       this.assignProfessionWhenSpawned = true; 
/* 762 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public Villager getBreedOffspring(ServerLevel level, AgeableMob partner) {
/*     */     Holder<VillagerType> type;
/* 768 */     double biomeRoll = this.random.nextDouble();
/* 769 */     if (biomeRoll < 0.5D) {
/* 770 */       Holder.Reference reference = level.registryAccess().getOrThrow(VillagerType.byBiome(level.getBiome(blockPosition())));
/* 771 */     } else if (biomeRoll < 0.75D) {
/* 772 */       type = getVillagerData().type();
/*     */     } else {
/* 774 */       type = ((Villager)partner).getVillagerData().type();
/*     */     } 
/* 777 */     Villager baby = new Villager(EntityTypes.VILLAGER, (Level)level);
/* 778 */     baby.setVillagerData(baby.getVillagerData().withType(type).withProfession((HolderGetter.Provider)level.registryAccess(), VillagerProfession.NONE));
/* 779 */     baby.setVillagerDataFinalized(true);
/* 780 */     return baby;
/*     */   }
/*     */   
/*     */   public void thunderHit(ServerLevel level, LightningBolt lightningBolt) {
/* 785 */     if (level.getDifficulty() != Difficulty.PEACEFUL) {
/* 786 */       LOGGER.info("Villager {} was struck by lightning {}.", this, lightningBolt);
/* 787 */       Witch witch = (Witch)convertTo(EntityTypes.WITCH, ConversionParams.single((Mob)this, false, false), w -> {
/*     */             w.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(w.blockPosition()), EntitySpawnReason.CONVERSION, null);
/*     */             w.setPersistenceRequired();
/*     */             releaseAllPois();
/*     */           });
/* 793 */       if (witch == null)
/* 794 */         super.thunderHit(level, lightningBolt); 
/*     */     } else {
/* 797 */       super.thunderHit(level, lightningBolt);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 803 */     InventoryCarrier.pickUpItem(level, (Mob)this, this, entity);
/*     */   }
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
/* 808 */     Item item = itemStack.getItem();
/* 809 */     return ((itemStack.is(ItemTags.VILLAGER_PICKS_UP) || ((VillagerProfession)getVillagerData().profession().value()).requestedItems().contains(item)) && getInventory().canAddItem(itemStack));
/*     */   }
/*     */   
/*     */   public boolean hasExcessFood() {
/* 813 */     return (countFoodPointsInInventory() >= 24);
/*     */   }
/*     */   
/*     */   public boolean wantsMoreFood() {
/* 817 */     return (countFoodPointsInInventory() < 12);
/*     */   }
/*     */   
/*     */   private int countFoodPointsInInventory() {
/* 821 */     SimpleContainer inventory = getInventory();
/* 822 */     return FOOD_POINTS.entrySet().stream().mapToInt(entry -> inventory.countItem((Item)entry.getKey()) * ((Integer)entry.getValue()).intValue()).sum();
/*     */   }
/*     */   
/*     */   public boolean hasFarmSeeds() {
/* 826 */     return getInventory().hasAnyMatching(item -> item.is(ItemTags.VILLAGER_PLANTABLE_SEEDS));
/*     */   }
/*     */   
/*     */   protected void updateTrades(ServerLevel level) {
/* 831 */     VillagerData data = getVillagerData();
/* 832 */     VillagerProfession profession = (VillagerProfession)data.profession().value();
/* 833 */     ResourceKey<TradeSet> trades = profession.getTrades(data.level());
/* 835 */     if (trades == null)
/*     */       return; 
/* 839 */     addOffersFromTradeSet(level, getOffers(), trades);
/* 840 */     if (SharedConstants.DEBUG_UNLOCK_ALL_TRADES && data.level() < 5)
/* 841 */       increaseMerchantCareer(level); 
/*     */   }
/*     */   
/*     */   public void gossip(ServerLevel level, Villager target, long timestamp) {
/* 846 */     if ((timestamp >= this.lastGossipTime && timestamp < this.lastGossipTime + 1200L) || (timestamp >= target.lastGossipTime && timestamp < target.lastGossipTime + 1200L))
/*     */       return; 
/* 852 */     this.gossips.transferFrom(target.gossips, this.random, 10);
/* 854 */     this.lastGossipTime = timestamp;
/* 855 */     target.lastGossipTime = timestamp;
/* 857 */     spawnGolemIfNeeded(level, timestamp, 5);
/*     */   }
/*     */   
/*     */   private void maybeDecayGossip() {
/* 861 */     long timestamp = level().getGameTime();
/* 863 */     if (this.lastGossipDecayTime == 0L) {
/* 864 */       this.lastGossipDecayTime = timestamp;
/*     */       return;
/*     */     } 
/* 868 */     if (timestamp < this.lastGossipDecayTime + 24000L)
/*     */       return; 
/* 872 */     this.gossips.decay();
/* 873 */     this.lastGossipDecayTime = timestamp;
/*     */   }
/*     */   
/*     */   public void spawnGolemIfNeeded(ServerLevel level, long timestamp, int villagersNeededToAgree) {
/* 877 */     if (!wantsToSpawnGolem(timestamp))
/*     */       return; 
/* 882 */     AABB villagerSearchBox = getBoundingBox().inflate(10.0D, 10.0D, 10.0D);
/* 884 */     List<Villager> nearbyVillagers = level.getEntitiesOfClass(Villager.class, villagerSearchBox);
/* 888 */     List<Villager> nearbyVillagersThatWantAGolem = nearbyVillagers.stream().filter(villager -> villager.wantsToSpawnGolem(timestamp)).limit(5L).toList();
/* 890 */     if (nearbyVillagersThatWantAGolem.size() < villagersNeededToAgree)
/*     */       return; 
/* 896 */     if (SpawnUtil.trySpawnMob(EntityTypes.IRON_GOLEM, EntitySpawnReason.MOB_SUMMONED, level, blockPosition(), 10, 8, 6, SpawnUtil.Strategy.LEGACY_IRON_GOLEM, false).isEmpty())
/*     */       return; 
/* 901 */     nearbyVillagers.forEach(GolemSensor::golemDetected);
/*     */   }
/*     */   
/*     */   public boolean wantsToSpawnGolem(long timestamp) {
/* 905 */     if (!golemSpawnConditionsMet(level().getGameTime()))
/* 906 */       return false; 
/* 908 */     return !this.brain.hasMemoryValue(MemoryModuleType.GOLEM_DETECTED_RECENTLY);
/*     */   }
/*     */   
/*     */   public void onReputationEventFrom(ReputationEventType type, Entity source) {
/* 913 */     if (type == ReputationEventType.ZOMBIE_VILLAGER_CURED) {
/* 914 */       this.gossips.add(source.getUUID(), GossipType.MAJOR_POSITIVE, 20);
/* 915 */       this.gossips.add(source.getUUID(), GossipType.MINOR_POSITIVE, 25);
/* 916 */     } else if (type == ReputationEventType.TRADE) {
/* 917 */       this.gossips.add(source.getUUID(), GossipType.TRADING, 2);
/* 918 */     } else if (type == ReputationEventType.VILLAGER_HURT) {
/* 919 */       this.gossips.add(source.getUUID(), GossipType.MINOR_NEGATIVE, 25);
/* 920 */     } else if (type == ReputationEventType.VILLAGER_KILLED) {
/* 921 */       this.gossips.add(source.getUUID(), GossipType.MAJOR_NEGATIVE, 25);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getVillagerXp() {
/* 927 */     return this.villagerXp;
/*     */   }
/*     */   
/*     */   public void setVillagerXp(int value) {
/* 931 */     this.villagerXp = value;
/*     */   }
/*     */   
/*     */   private void resetNumberOfRestocks() {
/* 935 */     catchUpDemand();
/* 936 */     this.numberOfRestocksToday = 0;
/*     */   }
/*     */   
/*     */   public GossipContainer getGossips() {
/* 940 */     return this.gossips;
/*     */   }
/*     */   
/*     */   public void setGossips(GossipContainer gossips) {
/* 944 */     this.gossips.putAll(gossips);
/*     */   }
/*     */   
/*     */   public void stopSleeping() {
/* 949 */     super.stopSleeping();
/* 950 */     this.brain.setMemory(MemoryModuleType.LAST_WOKEN, Long.valueOf(level().getGameTime()));
/*     */   }
/*     */   
/*     */   private boolean golemSpawnConditionsMet(long gameTime) {
/* 954 */     Optional<Long> sleepMemory = this.brain.getMemory(MemoryModuleType.LAST_SLEPT);
/* 955 */     return sleepMemory.filter(aLong -> (gameTime - aLong.longValue() < 24000L)).isPresent();
/*     */   }
/*     */   
/*     */   public <T> T get(DataComponentType<? extends T> type) {
/* 960 */     if (type == DataComponents.VILLAGER_VARIANT)
/* 961 */       return (T)castComponentValue(type, getVillagerData().type()); 
/* 964 */     return (T)super.get(type);
/*     */   }
/*     */   
/*     */   protected void applyImplicitComponents(DataComponentGetter components) {
/* 969 */     applyImplicitComponentIfPresent(components, DataComponents.VILLAGER_VARIANT);
/* 970 */     super.applyImplicitComponents(components);
/*     */   }
/*     */   
/*     */   protected <T> boolean applyImplicitComponent(DataComponentType<T> type, T value) {
/* 975 */     if (type == DataComponents.VILLAGER_VARIANT) {
/* 976 */       Holder<VillagerType> variant = (Holder<VillagerType>)castComponentValue(DataComponents.VILLAGER_VARIANT, value);
/* 977 */       setVillagerData(getVillagerData().withType(variant));
/* 978 */       setVillagerDataFinalized(true);
/* 979 */       return true;
/*     */     } 
/* 982 */     return super.applyImplicitComponent(type, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\npc\villager\Villager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */