/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.PathfindToRaidGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiManager;
/*     */ import net.minecraft.world.entity.ai.village.poi.PoiTypes;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.PatrollingMonster;
/*     */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class Raider extends PatrollingMonster {
/*  49 */   protected static final EntityDataAccessor<Boolean> IS_CELEBRATING = SynchedEntityData.defineId(Raider.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final Predicate<ItemEntity> ALLOWED_ITEMS;
/*     */   
/*     */   private static final int DEFAULT_WAVE = 0;
/*     */   
/*     */   private static final boolean DEFAULT_CAN_JOIN_RAID = false;
/*     */   
/*     */   protected Raid raid;
/*     */   
/*     */   static {
/*  51 */     ALLOWED_ITEMS = (e -> (!e.hasPickUpDelay() && e.isAlive() && ItemStack.matches(e.getItem(), Raid.getOminousBannerInstance((HolderGetter<BannerPattern>)e.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)))));
/*     */   }
/*     */   
/*  59 */   private int wave = 0;
/*     */   
/*     */   private boolean canJoinRaid = false;
/*     */   
/*     */   private int ticksOutsideRaid;
/*     */   
/*     */   protected Raider(EntityType<? extends Raider> type, Level level) {
/*  64 */     super(type, level);
/*     */   }
/*     */   
/*     */   protected void registerGoals() {
/*  69 */     super.registerGoals();
/*  70 */     this.goalSelector.addGoal(1, new ObtainRaidLeaderBannerGoal<>(this));
/*  71 */     this.goalSelector.addGoal(3, (Goal)new PathfindToRaidGoal(this));
/*  72 */     this.goalSelector.addGoal(4, new RaiderMoveThroughVillageGoal(this, 1.0499999523162842D, 1));
/*  73 */     this.goalSelector.addGoal(5, new RaiderCelebration(this));
/*     */   }
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder entityData) {
/*  78 */     super.defineSynchedData(entityData);
/*  80 */     entityData.define(IS_CELEBRATING, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean canJoinRaid() {
/*  86 */     return this.canJoinRaid;
/*     */   }
/*     */   
/*     */   public void setCanJoinRaid(boolean canJoinRaid) {
/*  90 */     this.canJoinRaid = canJoinRaid;
/*     */   }
/*     */   
/*     */   public void aiStep() {
/*  98 */     Level level = level();
/*  98 */     if (level instanceof ServerLevel) {
/*  98 */       ServerLevel serverLevel = (ServerLevel)level;
/*  98 */       if (isAlive()) {
/*  99 */         Raid currentRaid = getCurrentRaid();
/* 100 */         if (canJoinRaid())
/* 101 */           if (currentRaid == null) {
/* 102 */             if (level().getGameTime() % 20L == 0L) {
/* 103 */               Raid nearbyRaid = serverLevel.getRaidAt(blockPosition());
/* 104 */               if (nearbyRaid != null && Raids.canJoinRaid(this))
/* 105 */                 nearbyRaid.joinRaid(serverLevel, nearbyRaid.getGroupsSpawned(), this, null, true); 
/*     */             } 
/*     */           } else {
/* 110 */             LivingEntity target = getTarget();
/* 111 */             if (target != null && (target.is(EntityTypes.PLAYER) || target.is(EntityTypes.IRON_GOLEM)))
/* 112 */               this.noActionTime = 0; 
/*     */           }  
/*     */       } 
/*     */     } 
/* 117 */     super.aiStep();
/*     */   }
/*     */   
/*     */   protected void updateNoActionTime() {
/* 123 */     this.noActionTime += 2;
/*     */   }
/*     */   
/*     */   public void die(DamageSource source) {
/* 128 */     Level level = level();
/* 128 */     if (level instanceof ServerLevel) {
/* 128 */       ServerLevel serverLevel = (ServerLevel)level;
/* 129 */       Entity killer = source.getEntity();
/* 130 */       Raid raidWhenKilled = getCurrentRaid();
/* 131 */       if (raidWhenKilled != null) {
/* 132 */         if (isPatrolLeader())
/* 133 */           raidWhenKilled.removeLeader(getWave()); 
/* 136 */         if (killer != null && killer.is(EntityTypes.PLAYER))
/* 137 */           raidWhenKilled.addHeroOfTheVillage(killer); 
/* 140 */         raidWhenKilled.removeFromRaid(serverLevel, this, false);
/*     */       } 
/*     */     } 
/* 144 */     super.die(source);
/*     */   }
/*     */   
/*     */   public boolean canJoinPatrol() {
/* 149 */     return !hasActiveRaid();
/*     */   }
/*     */   
/*     */   public void setCurrentRaid(Raid raid) {
/* 153 */     this.raid = raid;
/*     */   }
/*     */   
/*     */   public Raid getCurrentRaid() {
/* 157 */     return this.raid;
/*     */   }
/*     */   
/*     */   public boolean isCaptain() {
/* 161 */     ItemStack banner = getItemBySlot(EquipmentSlot.HEAD);
/* 162 */     boolean hasCaptainBanner = (!banner.isEmpty() && ItemStack.matches(banner, Raid.getOminousBannerInstance((HolderGetter<BannerPattern>)registryAccess().lookupOrThrow(Registries.BANNER_PATTERN))));
/* 163 */     boolean patrolLeader = isPatrolLeader();
/* 165 */     return (hasCaptainBanner && patrolLeader);
/*     */   }
/*     */   
/*     */   public boolean hasRaid() {
/*     */     ServerLevel serverLevel;
/* 169 */     Level level = level();
/* 169 */     if (level instanceof ServerLevel) {
/* 169 */       serverLevel = (ServerLevel)level;
/*     */     } else {
/* 170 */       return false;
/*     */     } 
/* 172 */     return (getCurrentRaid() != null || serverLevel.getRaidAt(blockPosition()) != null);
/*     */   }
/*     */   
/*     */   public boolean hasActiveRaid() {
/* 176 */     return (getCurrentRaid() != null && getCurrentRaid().isActive());
/*     */   }
/*     */   
/*     */   public void setWave(int wave) {
/* 180 */     this.wave = wave;
/*     */   }
/*     */   
/*     */   public int getWave() {
/* 184 */     return this.wave;
/*     */   }
/*     */   
/*     */   public boolean isCelebrating() {
/* 188 */     return ((Boolean)this.entityData.get(IS_CELEBRATING)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setCelebrating(boolean celebrating) {
/* 192 */     this.entityData.set(IS_CELEBRATING, Boolean.valueOf(celebrating));
/*     */   }
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/* 197 */     super.addAdditionalSaveData(output);
/* 198 */     output.putInt("Wave", this.wave);
/* 199 */     output.putBoolean("CanJoinRaid", this.canJoinRaid);
/* 200 */     if (this.raid != null) {
/* 200 */       Level level = level();
/* 200 */       if (level instanceof ServerLevel) {
/* 200 */         ServerLevel serverLevel = (ServerLevel)level;
/* 201 */         serverLevel.getRaids().getId(this.raid).ifPresent(id -> output.putInt("RaidId", id));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/* 207 */     super.readAdditionalSaveData(input);
/* 208 */     this.wave = input.getIntOr("Wave", 0);
/* 209 */     this.canJoinRaid = input.getBooleanOr("CanJoinRaid", false);
/* 210 */     Level level = level();
/* 210 */     if (level instanceof ServerLevel) {
/* 210 */       ServerLevel serverLevel = (ServerLevel)level;
/* 211 */       input.getInt("RaidId").ifPresent(raidId -> {
/*     */             this.raid = level.getRaids().get(raidId.intValue());
/*     */             if (this.raid != null) {
/*     */               this.raid.addWaveMob(level, this.wave, this, false);
/*     */               if (isPatrolLeader())
/*     */                 this.raid.setLeader(this.wave, this); 
/*     */             } 
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void pickUpItem(ServerLevel level, ItemEntity entity) {
/* 226 */     ItemStack itemStack = entity.getItem();
/* 227 */     boolean hasRaidLeader = (hasActiveRaid() && getCurrentRaid().getLeader(getWave()) != null);
/* 230 */     if (hasActiveRaid() && !hasRaidLeader && ItemStack.matches(itemStack, Raid.getOminousBannerInstance((HolderGetter<BannerPattern>)registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)))) {
/* 231 */       EquipmentSlot slot = EquipmentSlot.HEAD;
/* 232 */       ItemStack current = getItemBySlot(slot);
/* 233 */       double dropChance = getDropChances().byEquipment(slot);
/* 234 */       if (!current.isEmpty() && Math.max(this.random.nextFloat() - 0.1F, 0.0F) < dropChance)
/* 235 */         spawnAtLocation(level, current); 
/* 237 */       onItemPickup(entity);
/* 238 */       setItemSlot(slot, itemStack);
/* 239 */       take((Entity)entity, itemStack.getCount());
/* 240 */       entity.discard();
/* 241 */       getCurrentRaid().setLeader(getWave(), this);
/* 242 */       setPatrolLeader(true);
/*     */     } else {
/* 244 */       super.pickUpItem(level, entity);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean removeWhenFarAway(double distSqr) {
/* 250 */     if (getCurrentRaid() == null)
/* 251 */       return super.removeWhenFarAway(distSqr); 
/* 253 */     return false;
/*     */   }
/*     */   
/*     */   public boolean requiresCustomPersistence() {
/* 258 */     return (super.requiresCustomPersistence() || getCurrentRaid() != null);
/*     */   }
/*     */   
/*     */   public int getTicksOutsideRaid() {
/* 262 */     return this.ticksOutsideRaid;
/*     */   }
/*     */   
/*     */   public void setTicksOutsideRaid(int ticksOutsideRaid) {
/* 266 */     this.ticksOutsideRaid = ticksOutsideRaid;
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
/* 271 */     if (hasActiveRaid())
/* 272 */       getCurrentRaid().updateBossbar(); 
/* 274 */     return super.hurtServer(level, source, damage);
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/* 280 */     setCanJoinRaid((!is(EntityTypes.WITCH) || spawnReason != EntitySpawnReason.NATURAL));
/* 282 */     return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
/*     */   }
/*     */   
/*     */   public abstract void applyRaidBuffs(ServerLevel paramServerLevel, int paramInt, boolean paramBoolean);
/*     */   
/*     */   public abstract SoundEvent getCelebrateSound();
/*     */   
/*     */   public class ObtainRaidLeaderBannerGoal<T extends Raider> extends Goal {
/*     */     private final T mob;
/*     */     
/* 290 */     private Int2LongOpenHashMap unreachableBannerCache = new Int2LongOpenHashMap();
/*     */     
/*     */     private Path pathToBanner;
/*     */     
/*     */     private ItemEntity pursuedBannerItemEntity;
/*     */     
/*     */     public ObtainRaidLeaderBannerGoal(T mob) {
/* 295 */       this.mob = mob;
/* 296 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 301 */       if (cannotPickUpBanner())
/* 302 */         return false; 
/* 305 */       Int2LongOpenHashMap tempCache = new Int2LongOpenHashMap();
/* 306 */       double followRange = Raider.this.getAttributeValue(Attributes.FOLLOW_RANGE);
/* 307 */       List<ItemEntity> items = this.mob.level().getEntitiesOfClass(ItemEntity.class, this.mob.getBoundingBox().inflate(followRange, 8.0D, followRange), Raider.ALLOWED_ITEMS);
/* 309 */       for (ItemEntity banner : items) {
/* 310 */         long unreachableUntilTime = this.unreachableBannerCache.getOrDefault(banner.getId(), Long.MIN_VALUE);
/* 311 */         if (Raider.this.level().getGameTime() < unreachableUntilTime) {
/* 312 */           tempCache.put(banner.getId(), unreachableUntilTime);
/*     */           continue;
/*     */         } 
/* 316 */         Path path = this.mob.getNavigation().createPath((Entity)banner, 1);
/* 317 */         if (path != null && path.canReach()) {
/* 318 */           this.pathToBanner = path;
/* 319 */           this.pursuedBannerItemEntity = banner;
/* 320 */           return true;
/*     */         } 
/* 322 */         tempCache.put(banner.getId(), Raider.this.level().getGameTime() + 600L);
/*     */       } 
/* 326 */       this.unreachableBannerCache = tempCache;
/* 328 */       return false;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 333 */       if (this.pursuedBannerItemEntity == null || this.pathToBanner == null)
/* 334 */         return false; 
/* 336 */       if (this.pursuedBannerItemEntity.isRemoved())
/* 337 */         return false; 
/* 339 */       if (this.pathToBanner.isDone())
/* 340 */         return false; 
/* 342 */       if (cannotPickUpBanner())
/* 343 */         return false; 
/* 345 */       return true;
/*     */     }
/*     */     
/*     */     private boolean cannotPickUpBanner() {
/* 349 */       if (!this.mob.hasActiveRaid())
/* 350 */         return true; 
/* 352 */       if (this.mob.getCurrentRaid().isOver())
/* 353 */         return true; 
/* 355 */       if (!this.mob.canBeLeader())
/* 356 */         return true; 
/* 358 */       if (ItemStack.matches(this.mob.getItemBySlot(EquipmentSlot.HEAD), Raid.getOminousBannerInstance((HolderGetter<BannerPattern>)this.mob.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN))))
/* 359 */         return true; 
/* 361 */       Raider leader = Raider.this.raid.getLeader(this.mob.getWave());
/* 362 */       if (leader != null && leader.isAlive())
/* 363 */         return true; 
/* 365 */       return false;
/*     */     }
/*     */     
/*     */     public void start() {
/* 370 */       this.mob.getNavigation().moveTo(this.pathToBanner, 1.149999976158142D);
/*     */     }
/*     */     
/*     */     public void stop() {
/* 375 */       this.pathToBanner = null;
/* 376 */       this.pursuedBannerItemEntity = null;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 381 */       if (this.pursuedBannerItemEntity != null && this.pursuedBannerItemEntity.closerThan((Entity)this.mob, 1.414D))
/* 382 */         this.mob.pickUpItem(getServerLevel(Raider.this.level()), this.pursuedBannerItemEntity); 
/*     */     }
/*     */   }
/*     */   
/*     */   public class RaiderCelebration extends Goal {
/*     */     private final Raider mob;
/*     */     
/*     */     public RaiderCelebration(Raider mob) {
/* 391 */       this.mob = mob;
/* 392 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 397 */       Raid currentRaid = this.mob.getCurrentRaid();
/* 398 */       return (this.mob.isAlive() && this.mob.getTarget() == null && currentRaid != null && currentRaid.isLoss());
/*     */     }
/*     */     
/*     */     public void start() {
/* 403 */       this.mob.setCelebrating(true);
/* 404 */       super.start();
/*     */     }
/*     */     
/*     */     public void stop() {
/* 409 */       this.mob.setCelebrating(false);
/* 410 */       super.stop();
/*     */     }
/*     */     
/*     */     public void tick() {
/* 415 */       if (!this.mob.isSilent() && this.mob.random.nextInt(adjustedTickDelay(100)) == 0)
/* 416 */         Raider.this.makeSound(Raider.this.getCelebrateSound()); 
/* 419 */       if (!this.mob.isPassenger() && this.mob.random.nextInt(adjustedTickDelay(50)) == 0)
/* 420 */         this.mob.getJumpControl().jump(); 
/* 423 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class HoldGroundAttackGoal extends Goal {
/*     */     private final Raider mob;
/*     */     
/*     */     private final float hostileRadiusSqr;
/*     */     
/* 430 */     public final TargetingConditions shoutTargeting = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight().ignoreInvisibilityTesting();
/*     */     
/*     */     public HoldGroundAttackGoal(AbstractIllager mob, float hostileRadius) {
/* 433 */       this.mob = (Raider)mob;
/* 434 */       this.hostileRadiusSqr = hostileRadius * hostileRadius;
/* 435 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 441 */       LivingEntity lastHurtByMob = this.mob.getLastHurtByMob();
/* 442 */       return (this.mob.getCurrentRaid() == null && this.mob.isPatrolling() && this.mob.getTarget() != null && !this.mob.isAggressive() && (lastHurtByMob == null || !lastHurtByMob.is(EntityTypes.PLAYER)));
/*     */     }
/*     */     
/*     */     public void start() {
/* 447 */       super.start();
/* 448 */       this.mob.getNavigation().stop();
/* 450 */       List<Raider> nearbyEntities = getServerLevel((Entity)this.mob).getNearbyEntities(Raider.class, this.shoutTargeting, (LivingEntity)this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/* 451 */       for (Raider entity : nearbyEntities)
/* 452 */         entity.setTarget(this.mob.getTarget()); 
/*     */     }
/*     */     
/*     */     public void stop() {
/* 458 */       super.stop();
/* 460 */       LivingEntity target = this.mob.getTarget();
/* 461 */       if (target != null) {
/* 462 */         List<Raider> nearbyEntities = getServerLevel((Entity)this.mob).getNearbyEntities(Raider.class, this.shoutTargeting, (LivingEntity)this.mob, this.mob.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
/* 463 */         for (Raider entity : nearbyEntities) {
/* 464 */           entity.setTarget(target);
/* 465 */           entity.setAggressive(true);
/*     */         } 
/* 467 */         this.mob.setAggressive(true);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 473 */       return true;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 478 */       LivingEntity target = this.mob.getTarget();
/* 479 */       if (target == null)
/*     */         return; 
/* 483 */       if (this.mob.distanceToSqr((Entity)target) > this.hostileRadiusSqr) {
/* 484 */         this.mob.getLookControl().setLookAt((Entity)target, 30.0F, 30.0F);
/* 486 */         if (this.mob.random.nextInt(50) == 0)
/* 487 */           this.mob.playAmbientSound(); 
/*     */       } else {
/* 490 */         this.mob.setAggressive(true);
/*     */       } 
/* 493 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class RaiderMoveThroughVillageGoal extends Goal {
/*     */     private final Raider raider;
/*     */     
/*     */     private final double speedModifier;
/*     */     
/*     */     private BlockPos poiPos;
/*     */     
/* 501 */     private final List<BlockPos> visited = Lists.newArrayList();
/*     */     
/*     */     private final int distanceToPoi;
/*     */     
/*     */     private boolean stuck;
/*     */     
/*     */     public RaiderMoveThroughVillageGoal(Raider mob, double speedModifier, int distanceToPoi) {
/* 506 */       this.raider = mob;
/* 507 */       this.speedModifier = speedModifier;
/* 508 */       this.distanceToPoi = distanceToPoi;
/* 509 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/* 514 */       updateVisited();
/* 515 */       return (isValidRaid() && hasSuitablePoi() && this.raider.getTarget() == null);
/*     */     }
/*     */     
/*     */     private boolean isValidRaid() {
/* 519 */       return (this.raider.hasActiveRaid() && !this.raider.getCurrentRaid().isOver());
/*     */     }
/*     */     
/*     */     private boolean hasSuitablePoi() {
/* 523 */       ServerLevel level = (ServerLevel)this.raider.level();
/* 524 */       BlockPos pos = this.raider.blockPosition();
/* 525 */       Optional<BlockPos> homePos = level.getPoiManager().getRandom(p -> p.is(PoiTypes.HOME), this::hasNotVisited, PoiManager.Occupancy.ANY, pos, 48, this.raider.random);
/* 526 */       if (homePos.isEmpty())
/* 527 */         return false; 
/* 530 */       this.poiPos = ((BlockPos)homePos.get()).immutable();
/* 532 */       return true;
/*     */     }
/*     */     
/*     */     public boolean canContinueToUse() {
/* 537 */       if (this.raider.getNavigation().isDone())
/* 538 */         return false; 
/* 540 */       return (this.raider.getTarget() == null && !this.poiPos.closerToCenterThan((Position)this.raider.position(), (this.raider.getBbWidth() + this.distanceToPoi)) && !this.stuck);
/*     */     }
/*     */     
/*     */     public void stop() {
/* 545 */       if (this.poiPos.closerToCenterThan((Position)this.raider.position(), this.distanceToPoi))
/* 546 */         this.visited.add(this.poiPos); 
/*     */     }
/*     */     
/*     */     public void start() {
/* 552 */       super.start();
/* 553 */       this.raider.setNoActionTime(0);
/* 554 */       this.raider.getNavigation().moveTo(this.poiPos.getX(), this.poiPos.getY(), this.poiPos.getZ(), this.speedModifier);
/* 555 */       this.stuck = false;
/*     */     }
/*     */     
/*     */     public void tick() {
/* 560 */       if (this.raider.getNavigation().isDone()) {
/* 561 */         Vec3 poiVec = Vec3.atBottomCenterOf((Vec3i)this.poiPos);
/* 562 */         Vec3 nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.raider, 16, 7, poiVec, 0.3141592741012573D);
/* 563 */         if (nextPos == null)
/* 564 */           nextPos = DefaultRandomPos.getPosTowards((PathfinderMob)this.raider, 8, 7, poiVec, 1.5707963705062866D); 
/* 567 */         if (nextPos == null) {
/* 568 */           this.stuck = true;
/*     */           return;
/*     */         } 
/* 572 */         this.raider.getNavigation().moveTo(nextPos.x, nextPos.y, nextPos.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean hasNotVisited(BlockPos poi) {
/* 577 */       for (BlockPos visitedPoi : this.visited) {
/* 578 */         if (Objects.equals(poi, visitedPoi))
/* 579 */           return false; 
/*     */       } 
/* 582 */       return true;
/*     */     }
/*     */     
/*     */     private void updateVisited() {
/* 586 */       if (this.visited.size() > 2)
/* 587 */         this.visited.remove(0); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\raid\Raider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */