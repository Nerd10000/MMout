/*     */ package net.minecraft.world.entity.raid;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function12;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.advancements.triggers.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderGetter;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponentPatch;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ClientboundSoundPacket;
/*     */ import net.minecraft.server.level.ServerBossEvent;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.BossEvent;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EntityTypes;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.SpawnPlacementType;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemStackTemplate;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.Rarity;
/*     */ import net.minecraft.world.item.component.TooltipDisplay;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.entity.BannerPattern;
/*     */ import net.minecraft.world.level.block.entity.BannerPatternLayers;
/*     */ import net.minecraft.world.level.block.entity.BannerPatterns;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Raid {
/*  69 */   public static final SpawnPlacementType RAVAGER_SPAWN_PLACEMENT_TYPE = SpawnPlacements.getPlacementType(EntityTypes.RAVAGER);
/*     */   
/*     */   public static final MapCodec<Raid> MAP_CODEC;
/*     */   
/*     */   private static final int ALLOW_SPAWNING_WITHIN_VILLAGE_SECONDS_THRESHOLD = 7;
/*     */   
/*     */   private static final int SECTION_RADIUS_FOR_FINDING_NEW_VILLAGE_CENTER = 2;
/*     */   
/*     */   private static final int VILLAGE_SEARCH_RADIUS = 32;
/*     */   
/*     */   private static final int RAID_TIMEOUT_TICKS = 48000;
/*     */   
/*     */   private static final int NUM_SPAWN_ATTEMPTS = 5;
/*     */   
/*     */   private enum RaidStatus implements StringRepresentable {
/*  72 */     ONGOING("ongoing"),
/*  73 */     VICTORY("victory"),
/*  74 */     LOSS("loss"),
/*  75 */     STOPPED("stopped");
/*     */     
/*  77 */     public static final Codec<RaidStatus> CODEC = (Codec<RaidStatus>)StringRepresentable.fromEnum(RaidStatus::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     RaidStatus(String name) {
/*  82 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/*  87 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   private enum RaiderType {
/*  92 */     VINDICATOR((String)EntityTypes.VINDICATOR, new int[] { 0, 0, 2, 0, 1, 4, 2, 5 }),
/*  93 */     EVOKER((String)EntityTypes.EVOKER, new int[] { 0, 0, 0, 0, 0, 1, 1, 2 }),
/*  95 */     PILLAGER((String)EntityTypes.PILLAGER, new int[] { 0, 4, 3, 3, 4, 4, 4, 2 }),
/*  96 */     WITCH((String)EntityTypes.WITCH, new int[] { 0, 0, 0, 0, 3, 0, 0, 1 }),
/*  97 */     RAVAGER((String)EntityTypes.RAVAGER, new int[] { 0, 0, 0, 1, 0, 1, 0, 2 });
/*     */     
/* 100 */     private static final RaiderType[] VALUES = values();
/*     */     
/*     */     private final EntityType<? extends Raider> entityType;
/*     */     
/*     */     private final int[] spawnsPerWaveBeforeBonus;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     RaiderType(EntityType<? extends Raider> entityType, int[] spawnsPerWaveBeforeBonus) {
/* 106 */       this.entityType = entityType;
/* 107 */       this.spawnsPerWaveBeforeBonus = spawnsPerWaveBeforeBonus;
/*     */     }
/*     */   }
/*     */   
/*     */   static {
/* 111 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.BOOL.fieldOf("started").forGetter(()), (App)Codec.BOOL.fieldOf("active").forGetter(()), (App)Codec.LONG.fieldOf("ticks_active").forGetter(()), (App)Codec.INT.fieldOf("raid_omen_level").forGetter(()), (App)Codec.INT.fieldOf("groups_spawned").forGetter(()), (App)Codec.INT.fieldOf("cooldown_ticks").forGetter(()), (App)Codec.INT.fieldOf("post_raid_ticks").forGetter(()), (App)Codec.FLOAT.fieldOf("total_health").forGetter(()), (App)Codec.INT.fieldOf("group_count").forGetter(()), (App)RaidStatus.CODEC.fieldOf("status").forGetter(()), (App)BlockPos.CODEC.fieldOf("center").forGetter(()), (App)UUIDUtil.CODEC_SET.fieldOf("heroes_of_the_village").forGetter(())).apply((Applicative)i, Raid::new));
/*     */   }
/*     */   
/* 132 */   private static final Component OMINOUS_BANNER_PATTERN_NAME = (Component)Component.translatable("block.minecraft.ominous_banner");
/*     */   
/*     */   private static final String RAIDERS_REMAINING = "event.minecraft.raid.raiders_remaining";
/*     */   
/*     */   public static final int VILLAGE_RADIUS_BUFFER = 16;
/*     */   
/*     */   private static final int POST_RAID_TICK_LIMIT = 40;
/*     */   
/*     */   private static final int DEFAULT_PRE_RAID_TICKS = 300;
/*     */   
/*     */   public static final int MAX_NO_ACTION_TIME = 2400;
/*     */   
/*     */   public static final int MAX_CELEBRATION_TICKS = 600;
/*     */   
/*     */   private static final int OUTSIDE_RAID_BOUNDS_TIMEOUT = 30;
/*     */   
/*     */   public static final int DEFAULT_MAX_RAID_OMEN_LEVEL = 5;
/*     */   
/*     */   private static final int LOW_MOB_THRESHOLD = 2;
/*     */   
/* 144 */   private static final Component RAID_NAME_COMPONENT = (Component)Component.translatable("event.minecraft.raid");
/*     */   
/* 145 */   private static final Component RAID_BAR_VICTORY_COMPONENT = (Component)Component.translatable("event.minecraft.raid.victory.full");
/*     */   
/* 146 */   private static final Component RAID_BAR_DEFEAT_COMPONENT = (Component)Component.translatable("event.minecraft.raid.defeat.full");
/*     */   
/*     */   private static final int HERO_OF_THE_VILLAGE_DURATION = 48000;
/*     */   
/*     */   private static final int VALID_RAID_RADIUS = 96;
/*     */   
/*     */   public static final int VALID_RAID_RADIUS_SQR = 9216;
/*     */   
/*     */   public static final int RAID_REMOVAL_THRESHOLD_SQR = 12544;
/*     */   
/* 155 */   private final Map<Integer, Raider> groupToLeaderMap = Maps.newHashMap();
/*     */   
/* 156 */   private final Map<Integer, Set<Raider>> groupRaiderMap = Maps.newHashMap();
/*     */   
/* 158 */   private final Set<UUID> heroesOfTheVillage = Sets.newHashSet();
/*     */   
/*     */   private long ticksActive;
/*     */   
/*     */   private BlockPos center;
/*     */   
/*     */   private boolean started;
/*     */   
/*     */   private float totalHealth;
/*     */   
/*     */   private int raidOmenLevel;
/*     */   
/*     */   private boolean active;
/*     */   
/*     */   private int groupsSpawned;
/*     */   
/*     */   private int postRaidTicks;
/*     */   
/*     */   private int raidCooldownTicks;
/*     */   
/* 169 */   private final RandomSource random = RandomSource.create();
/*     */   
/* 170 */   private final ServerBossEvent raidEvent = new ServerBossEvent(Mth.createInsecureUUID(this.random), RAID_NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
/*     */   
/*     */   private final int numGroups;
/*     */   
/*     */   private RaidStatus status;
/*     */   
/*     */   private int celebrationTicks;
/*     */   
/* 174 */   private Optional<BlockPos> waveSpawnPos = Optional.empty();
/*     */   
/*     */   public Raid(BlockPos center, Difficulty difficulty) {
/* 177 */     this.active = true;
/* 178 */     this.raidCooldownTicks = 300;
/* 179 */     this.raidEvent.setProgress(0.0F);
/* 180 */     this.center = center;
/* 181 */     this.numGroups = getNumGroups(difficulty);
/* 182 */     this.status = RaidStatus.ONGOING;
/*     */   }
/*     */   
/*     */   private Raid(boolean started, boolean active, long ticksActive, int raidOmenLevel, int groupsSpawned, int raidCooldownTicks, int postRaidTicks, float totalHealth, int numGroups, RaidStatus status, BlockPos center, Set<UUID> heroesOfTheVillage) {
/* 186 */     this.started = started;
/* 187 */     this.active = active;
/* 188 */     this.ticksActive = ticksActive;
/* 189 */     this.raidOmenLevel = raidOmenLevel;
/* 190 */     this.groupsSpawned = groupsSpawned;
/* 191 */     this.raidCooldownTicks = raidCooldownTicks;
/* 192 */     this.postRaidTicks = postRaidTicks;
/* 193 */     this.totalHealth = totalHealth;
/* 194 */     this.center = center;
/* 195 */     this.numGroups = numGroups;
/* 196 */     this.status = status;
/* 197 */     this.heroesOfTheVillage.addAll(heroesOfTheVillage);
/*     */   }
/*     */   
/*     */   public boolean isOver() {
/* 201 */     return (isVictory() || isLoss());
/*     */   }
/*     */   
/*     */   public boolean isBetweenWaves() {
/* 205 */     return (hasFirstWaveSpawned() && getTotalRaidersAlive() == 0 && this.raidCooldownTicks > 0);
/*     */   }
/*     */   
/*     */   public boolean hasFirstWaveSpawned() {
/* 209 */     return (this.groupsSpawned > 0);
/*     */   }
/*     */   
/*     */   public boolean isStopped() {
/* 213 */     return (this.status == RaidStatus.STOPPED);
/*     */   }
/*     */   
/*     */   public boolean isVictory() {
/* 217 */     return (this.status == RaidStatus.VICTORY);
/*     */   }
/*     */   
/*     */   public boolean isLoss() {
/* 221 */     return (this.status == RaidStatus.LOSS);
/*     */   }
/*     */   
/*     */   public float getTotalHealth() {
/* 225 */     return this.totalHealth;
/*     */   }
/*     */   
/*     */   public Set<Raider> getAllRaiders() {
/* 229 */     Set<Raider> raiders = Sets.newHashSet();
/* 230 */     for (Set<Raider> raiderSet : this.groupRaiderMap.values())
/* 231 */       raiders.addAll(raiderSet); 
/* 233 */     return raiders;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 237 */     return this.started;
/*     */   }
/*     */   
/*     */   public int getGroupsSpawned() {
/* 241 */     return this.groupsSpawned;
/*     */   }
/*     */   
/*     */   private Predicate<ServerPlayer> validPlayer() {
/* 246 */     return player -> {
/*     */         BlockPos pos = player.blockPosition();
/* 248 */         return (player.isAlive() && player.level().getRaidAt(pos) == this);
/*     */       };
/*     */   }
/*     */   
/*     */   private void updatePlayers(ServerLevel level) {
/* 253 */     Set<ServerPlayer> currentPlayersInRaid = Sets.newHashSet(this.raidEvent.getPlayers());
/* 254 */     List<ServerPlayer> newPlayersInRaid = level.getPlayers(validPlayer());
/* 256 */     for (ServerPlayer player : newPlayersInRaid) {
/* 257 */       if (!currentPlayersInRaid.contains(player))
/* 258 */         this.raidEvent.addPlayer(player); 
/*     */     } 
/* 262 */     for (ServerPlayer player : currentPlayersInRaid) {
/* 263 */       if (!newPlayersInRaid.contains(player))
/* 264 */         this.raidEvent.removePlayer(player); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getMaxRaidOmenLevel() {
/* 270 */     return 5;
/*     */   }
/*     */   
/*     */   public int getRaidOmenLevel() {
/* 274 */     return this.raidOmenLevel;
/*     */   }
/*     */   
/*     */   public void setRaidOmenLevel(int raidOmenLevel) {
/* 278 */     this.raidOmenLevel = raidOmenLevel;
/*     */   }
/*     */   
/*     */   public boolean absorbRaidOmen(ServerPlayer player) {
/* 282 */     MobEffectInstance effect = player.getEffect(MobEffects.RAID_OMEN);
/* 283 */     if (effect == null)
/* 284 */       return false; 
/* 287 */     this.raidOmenLevel += effect.getAmplifier() + 1;
/* 288 */     this.raidOmenLevel = Mth.clamp(this.raidOmenLevel, 0, getMaxRaidOmenLevel());
/* 290 */     if (!hasFirstWaveSpawned()) {
/* 291 */       player.awardStat(Stats.RAID_TRIGGER);
/* 292 */       CriteriaTriggers.RAID_OMEN.trigger(player);
/*     */     } 
/* 295 */     return true;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 299 */     this.active = false;
/* 300 */     this.raidEvent.removeAllPlayers();
/* 301 */     this.status = RaidStatus.STOPPED;
/*     */   }
/*     */   
/*     */   public void tick(ServerLevel level) {
/* 305 */     if (isStopped())
/*     */       return; 
/* 309 */     if (this.status == RaidStatus.ONGOING) {
/* 310 */       boolean oldActive = this.active;
/* 311 */       this.active = level.hasChunkAt(this.center);
/* 313 */       if (level.getDifficulty() == Difficulty.PEACEFUL) {
/* 314 */         stop();
/*     */         return;
/*     */       } 
/* 318 */       if (oldActive != this.active)
/* 319 */         this.raidEvent.setVisible(this.active); 
/* 323 */       if (!this.active)
/*     */         return; 
/* 327 */       if (!level.isVillage(this.center))
/* 330 */         moveRaidCenterToNearbyVillageSection(level); 
/* 334 */       if (!level.isVillage(this.center))
/* 336 */         if (this.groupsSpawned > 0) {
/* 337 */           this.status = RaidStatus.LOSS;
/*     */         } else {
/* 339 */           stop();
/*     */         }  
/* 343 */       this.ticksActive++;
/* 345 */       if (this.ticksActive >= 48000L) {
/* 346 */         stop();
/*     */         return;
/*     */       } 
/* 350 */       int raidersAlive = getTotalRaidersAlive();
/* 353 */       if (raidersAlive == 0 && hasMoreWaves())
/* 354 */         if (this.raidCooldownTicks > 0) {
/* 355 */           boolean hasCachedWaveSpawnPos = this.waveSpawnPos.isPresent();
/* 356 */           boolean shouldTryToFindSpawnPos = (!hasCachedWaveSpawnPos && this.raidCooldownTicks % 5 == 0);
/* 359 */           if (hasCachedWaveSpawnPos && !level.isPositionEntityTicking(this.waveSpawnPos.get()))
/* 360 */             shouldTryToFindSpawnPos = true; 
/* 364 */           if (shouldTryToFindSpawnPos)
/* 365 */             this.waveSpawnPos = getValidSpawnPos(level); 
/* 368 */           if (this.raidCooldownTicks == 300 || this.raidCooldownTicks % 20 == 0)
/* 369 */             updatePlayers(level); 
/* 371 */           this.raidCooldownTicks--;
/* 372 */           this.raidEvent.setProgress(Mth.clamp((300 - this.raidCooldownTicks) / 300.0F, 0.0F, 1.0F));
/* 373 */         } else if (this.raidCooldownTicks == 0 && this.groupsSpawned > 0) {
/* 374 */           this.raidCooldownTicks = 300;
/* 375 */           this.raidEvent.setName(RAID_NAME_COMPONENT);
/*     */           return;
/*     */         }  
/* 381 */       if (this.ticksActive % 20L == 0L) {
/* 382 */         updatePlayers(level);
/* 383 */         updateRaiders(level);
/* 385 */         if (raidersAlive > 0) {
/* 387 */           if (raidersAlive <= 2) {
/* 388 */             this.raidEvent.setName((Component)RAID_NAME_COMPONENT.copy().append(" - ").append((Component)Component.translatable("event.minecraft.raid.raiders_remaining", new Object[] { Integer.valueOf(raidersAlive) })));
/*     */           } else {
/* 390 */             this.raidEvent.setName(RAID_NAME_COMPONENT);
/*     */           } 
/*     */         } else {
/* 393 */           this.raidEvent.setName(RAID_NAME_COMPONENT);
/*     */         } 
/*     */       } 
/* 397 */       if (SharedConstants.DEBUG_RAIDS)
/* 398 */         this.raidEvent.setName((Component)RAID_NAME_COMPONENT.copy().append(" wave: ").append("" + this.groupsSpawned).append(CommonComponents.SPACE).append("Raiders alive: ").append("" + getTotalRaidersAlive())
/* 399 */             .append(CommonComponents.SPACE).append("" + getHealthOfLivingRaiders()).append(" / ").append("" + this.totalHealth).append(" Is bonus? ").append("" + ((hasBonusWave() && hasSpawnedBonusWave()) ? 1 : 0))
/* 400 */             .append(" Status: ").append(this.status.getSerializedName())); 
/* 403 */       boolean soundPlayed = false;
/* 404 */       int attempt = 0;
/* 405 */       while (shouldSpawnGroup()) {
/* 407 */         BlockPos spawnPos = this.waveSpawnPos.orElseGet(() -> findRandomSpawnPos(level, 20));
/* 408 */         if (spawnPos != null) {
/* 409 */           this.started = true;
/* 410 */           spawnGroup(level, spawnPos);
/* 411 */           if (!soundPlayed) {
/* 412 */             playSound(level, spawnPos);
/* 413 */             soundPlayed = true;
/*     */           } 
/*     */         } else {
/* 416 */           attempt++;
/*     */         } 
/* 419 */         if (attempt > 5) {
/* 420 */           stop();
/*     */           break;
/*     */         } 
/*     */       } 
/* 426 */       if (isStarted() && !hasMoreWaves() && raidersAlive == 0)
/* 427 */         if (this.postRaidTicks < 40) {
/* 428 */           this.postRaidTicks++;
/*     */         } else {
/* 430 */           this.status = RaidStatus.VICTORY;
/* 431 */           for (UUID heroUUID : this.heroesOfTheVillage) {
/* 432 */             Entity entity = level.getEntity(heroUUID);
/* 433 */             if (entity instanceof LivingEntity) {
/* 433 */               LivingEntity hero = (LivingEntity)entity;
/* 433 */               if (!entity.isSpectator()) {
/* 434 */                 hero.addEffect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 48000, this.raidOmenLevel - 1, false, false, true));
/* 435 */                 if (hero instanceof ServerPlayer) {
/* 435 */                   ServerPlayer playerHero = (ServerPlayer)hero;
/* 436 */                   playerHero.awardStat(Stats.RAID_WIN);
/* 437 */                   CriteriaTriggers.RAID_WIN.trigger(playerHero);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         }  
/* 444 */       setDirty(level);
/* 445 */     } else if (isOver()) {
/* 446 */       this.celebrationTicks++;
/* 447 */       if (this.celebrationTicks >= 600) {
/* 448 */         stop();
/*     */         return;
/*     */       } 
/* 453 */       if (this.celebrationTicks % 20 == 0) {
/* 454 */         updatePlayers(level);
/* 455 */         this.raidEvent.setVisible(true);
/* 456 */         if (isVictory()) {
/* 457 */           this.raidEvent.setProgress(0.0F);
/* 458 */           this.raidEvent.setName(RAID_BAR_VICTORY_COMPONENT);
/*     */         } else {
/* 460 */           this.raidEvent.setName(RAID_BAR_DEFEAT_COMPONENT);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void moveRaidCenterToNearbyVillageSection(ServerLevel level) {
/* 467 */     Stream<SectionPos> sectionsToSearchForVillage = SectionPos.cube(SectionPos.of(this.center), 2);
/* 470 */     Objects.requireNonNull(level);
/* 470 */     sectionsToSearchForVillage.filter(level::isVillage)
/* 471 */       .map(SectionPos::center)
/* 472 */       .min(Comparator.comparingDouble(pos -> pos.distSqr((Vec3i)this.center)))
/* 473 */       .ifPresent(this::setCenter);
/*     */   }
/*     */   
/*     */   private Optional<BlockPos> getValidSpawnPos(ServerLevel level) {
/* 477 */     BlockPos spawnPos = findRandomSpawnPos(level, 8);
/* 478 */     if (spawnPos != null)
/* 479 */       return Optional.of(spawnPos); 
/* 481 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private boolean hasMoreWaves() {
/* 485 */     if (hasBonusWave())
/* 486 */       return !hasSpawnedBonusWave(); 
/* 488 */     return !isFinalWave();
/*     */   }
/*     */   
/*     */   private boolean isFinalWave() {
/* 492 */     return (getGroupsSpawned() == this.numGroups);
/*     */   }
/*     */   
/*     */   private boolean hasBonusWave() {
/* 496 */     return (this.raidOmenLevel > 1);
/*     */   }
/*     */   
/*     */   private boolean hasSpawnedBonusWave() {
/* 500 */     return (getGroupsSpawned() > this.numGroups);
/*     */   }
/*     */   
/*     */   private boolean shouldSpawnBonusGroup() {
/* 504 */     return (isFinalWave() && getTotalRaidersAlive() == 0 && hasBonusWave());
/*     */   }
/*     */   
/*     */   private void updateRaiders(ServerLevel level) {
/* 508 */     Iterator<Set<Raider>> raiders = this.groupRaiderMap.values().iterator();
/* 509 */     Set<Raider> toRemove = Sets.newHashSet();
/* 511 */     while (raiders.hasNext()) {
/* 512 */       Set<Raider> raiderSet = raiders.next();
/* 513 */       for (Raider raider : raiderSet) {
/* 514 */         BlockPos raiderPos = raider.blockPosition();
/* 518 */         if (raider.isRemoved() || raider.level().dimension() != level.dimension() || this.center.distSqr((Vec3i)raiderPos) >= 12544.0D) {
/* 519 */           toRemove.add(raider);
/*     */           continue;
/*     */         } 
/* 521 */         if (raider.tickCount <= 600)
/*     */           continue; 
/* 526 */         if (level.getEntity(raider.getUUID()) == null)
/* 527 */           toRemove.add(raider); 
/* 531 */         if (!level.isVillage(raiderPos) && raider.getNoActionTime() > 2400)
/* 532 */           raider.setTicksOutsideRaid(raider.getTicksOutsideRaid() + 1); 
/* 535 */         if (raider.getTicksOutsideRaid() >= 30)
/* 536 */           toRemove.add(raider); 
/*     */       } 
/*     */     } 
/* 541 */     for (Raider raider : toRemove) {
/* 542 */       removeFromRaid(level, raider, true);
/* 543 */       if (raider.isPatrolLeader())
/* 544 */         removeLeader(raider.getWave()); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void playSound(ServerLevel level, BlockPos soundOrigin) {
/* 550 */     float distAway = 13.0F;
/* 551 */     int range = 64;
/* 553 */     Collection<ServerPlayer> playersInRaid = this.raidEvent.getPlayers();
/* 554 */     long seed = this.random.nextLong();
/* 555 */     for (ServerPlayer player : level.players()) {
/* 556 */       Vec3 playerLoc = player.position();
/* 557 */       Vec3 raidLoc = Vec3.atCenterOf((Vec3i)soundOrigin);
/* 558 */       double distBtwn = Math.sqrt((raidLoc.x - playerLoc.x) * (raidLoc.x - playerLoc.x) + (raidLoc.z - playerLoc.z) * (raidLoc.z - playerLoc.z));
/* 560 */       double x3 = playerLoc.x + 13.0D / distBtwn * (raidLoc.x - playerLoc.x);
/* 561 */       double z3 = playerLoc.z + 13.0D / distBtwn * (raidLoc.z - playerLoc.z);
/* 563 */       if (distBtwn <= 64.0D || playersInRaid.contains(player))
/* 564 */         player.connection.send((Packet)new ClientboundSoundPacket((Holder)SoundEvents.RAID_HORN, SoundSource.NEUTRAL, x3, player.getY(), z3, 64.0F, 1.0F, seed)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void spawnGroup(ServerLevel level, BlockPos pos) {
/* 570 */     boolean leaderSet = false;
/* 571 */     int groupNumber = this.groupsSpawned + 1;
/* 572 */     this.totalHealth = 0.0F;
/* 573 */     DifficultyInstance difficulty = level.getCurrentDifficultyAt(pos);
/* 574 */     boolean isBonusGroup = shouldSpawnBonusGroup();
/* 576 */     for (RaiderType raiderType : RaiderType.VALUES) {
/* 577 */       int numSpawns = getDefaultNumSpawns(raiderType, groupNumber, isBonusGroup) + getPotentialBonusSpawns(raiderType, this.random, groupNumber, difficulty, isBonusGroup);
/* 578 */       int ravagersSpawned = 0;
/* 580 */       for (int i = 0; i < numSpawns; i++) {
/* 581 */         Raider raider = (Raider)raiderType.entityType.create((Level)level, EntitySpawnReason.EVENT);
/* 582 */         if (raider == null)
/*     */           break; 
/* 586 */         if (!leaderSet && raider.canBeLeader()) {
/* 587 */           raider.setPatrolLeader(true);
/* 588 */           setLeader(groupNumber, raider);
/* 589 */           leaderSet = true;
/*     */         } 
/* 592 */         joinRaid(level, groupNumber, raider, pos, false);
/* 594 */         if (raiderType.entityType == EntityTypes.RAVAGER) {
/* 595 */           Raider ridingRaider = null;
/* 596 */           if (groupNumber == getNumGroups(Difficulty.NORMAL)) {
/* 597 */             ridingRaider = (Raider)EntityTypes.PILLAGER.create((Level)level, EntitySpawnReason.EVENT);
/* 598 */           } else if (groupNumber >= getNumGroups(Difficulty.HARD)) {
/* 600 */             if (ravagersSpawned == 0) {
/* 601 */               ridingRaider = (Raider)EntityTypes.EVOKER.create((Level)level, EntitySpawnReason.EVENT);
/*     */             } else {
/* 603 */               ridingRaider = (Raider)EntityTypes.VINDICATOR.create((Level)level, EntitySpawnReason.EVENT);
/*     */             } 
/*     */           } 
/* 606 */           ravagersSpawned++;
/* 608 */           if (ridingRaider != null) {
/* 609 */             joinRaid(level, groupNumber, ridingRaider, pos, false);
/* 610 */             ridingRaider.snapTo(pos, 0.0F, 0.0F);
/* 611 */             ridingRaider.startRiding((Entity)raider, false, false);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 617 */     this.waveSpawnPos = Optional.empty();
/* 618 */     this.groupsSpawned++;
/* 619 */     updateBossbar();
/* 620 */     setDirty(level);
/*     */   }
/*     */   
/*     */   public void joinRaid(ServerLevel level, int groupNumber, Raider raider, BlockPos pos, boolean exists) {
/* 624 */     boolean added = addWaveMob(level, groupNumber, raider);
/* 626 */     if (added) {
/* 627 */       raider.setCurrentRaid(this);
/* 628 */       raider.setWave(groupNumber);
/* 629 */       raider.setCanJoinRaid(true);
/* 630 */       raider.setTicksOutsideRaid(0);
/* 632 */       if (!exists && pos != null) {
/* 633 */         raider.setPos(pos.getX() + 0.5D, pos.getY() + 1.0D, pos.getZ() + 0.5D);
/* 634 */         raider.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(pos), EntitySpawnReason.EVENT, (SpawnGroupData)null);
/* 635 */         raider.applyRaidBuffs(level, groupNumber, false);
/* 636 */         raider.setOnGround(true);
/* 637 */         level.addFreshEntityWithPassengers((Entity)raider);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateBossbar() {
/* 643 */     this.raidEvent.setProgress(Mth.clamp(getHealthOfLivingRaiders() / this.totalHealth, 0.0F, 1.0F));
/*     */   }
/*     */   
/*     */   public float getHealthOfLivingRaiders() {
/* 647 */     float health = 0.0F;
/* 648 */     for (Set<Raider> raiders : this.groupRaiderMap.values()) {
/* 649 */       for (Raider raider : raiders)
/* 650 */         health += raider.getHealth(); 
/*     */     } 
/* 653 */     return health;
/*     */   }
/*     */   
/*     */   private boolean shouldSpawnGroup() {
/* 657 */     return (this.raidCooldownTicks == 0 && (this.groupsSpawned < this.numGroups || shouldSpawnBonusGroup()) && getTotalRaidersAlive() == 0);
/*     */   }
/*     */   
/*     */   public int getTotalRaidersAlive() {
/* 661 */     return this.groupRaiderMap.values().stream().mapToInt(Set::size).sum();
/*     */   }
/*     */   
/*     */   public void removeFromRaid(ServerLevel level, Raider raider, boolean removeFromTotalHealth) {
/* 665 */     Set<Raider> raiders = this.groupRaiderMap.get(Integer.valueOf(raider.getWave()));
/* 666 */     if (raiders != null) {
/* 667 */       boolean couldRemove = raiders.remove(raider);
/* 668 */       if (couldRemove) {
/* 671 */         if (removeFromTotalHealth)
/* 672 */           this.totalHealth -= raider.getHealth(); 
/* 674 */         raider.setCurrentRaid((Raid)null);
/* 675 */         updateBossbar();
/* 676 */         setDirty(level);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setDirty(ServerLevel level) {
/* 682 */     level.getRaids().setDirty();
/*     */   }
/*     */   
/*     */   public static DataComponentPatch getBannerComponentPatch(HolderGetter<BannerPattern> patternGetter) {
/* 686 */     DataComponentPatch.Builder builder = DataComponentPatch.builder();
/* 696 */     BannerPatternLayers patterns = (new BannerPatternLayers.Builder()).addIfRegistered(patternGetter, BannerPatterns.RHOMBUS_MIDDLE, DyeColor.CYAN).addIfRegistered(patternGetter, BannerPatterns.STRIPE_BOTTOM, DyeColor.LIGHT_GRAY).addIfRegistered(patternGetter, BannerPatterns.STRIPE_CENTER, DyeColor.GRAY).addIfRegistered(patternGetter, BannerPatterns.BORDER, DyeColor.LIGHT_GRAY).addIfRegistered(patternGetter, BannerPatterns.STRIPE_MIDDLE, DyeColor.BLACK).addIfRegistered(patternGetter, BannerPatterns.HALF_HORIZONTAL, DyeColor.LIGHT_GRAY).addIfRegistered(patternGetter, BannerPatterns.CIRCLE_MIDDLE, DyeColor.LIGHT_GRAY).addIfRegistered(patternGetter, BannerPatterns.BORDER, DyeColor.BLACK).build();
/* 698 */     builder.set(DataComponents.BANNER_PATTERNS, patterns);
/* 699 */     builder.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.BANNER_PATTERNS, true));
/* 700 */     builder.set(DataComponents.ITEM_NAME, OMINOUS_BANNER_PATTERN_NAME);
/* 701 */     builder.set(DataComponents.RARITY, Rarity.UNCOMMON);
/* 703 */     return builder.build();
/*     */   }
/*     */   
/*     */   public static ItemStackTemplate getOminousBannerTemplate(HolderGetter<BannerPattern> patternGetter) {
/* 707 */     return new ItemStackTemplate((Item)Items.BANNER.white(), getBannerComponentPatch(patternGetter));
/*     */   }
/*     */   
/*     */   public static ItemStack getOminousBannerInstance(HolderGetter<BannerPattern> patternGetter) {
/* 711 */     return getOminousBannerTemplate(patternGetter).create();
/*     */   }
/*     */   
/*     */   public Raider getLeader(int wave) {
/* 715 */     return this.groupToLeaderMap.get(Integer.valueOf(wave));
/*     */   }
/*     */   
/*     */   private BlockPos findRandomSpawnPos(ServerLevel level, int maxTries) {
/* 719 */     int secondsRemaining = this.raidCooldownTicks / 20;
/* 722 */     float howFar = 0.22F * secondsRemaining - 0.24F;
/* 727 */     BlockPos.MutableBlockPos spawnPos = new BlockPos.MutableBlockPos();
/* 728 */     float startAngle = this.random.nextFloat() * 6.2831855F;
/* 730 */     for (int i = 0; i < maxTries; i++) {
/* 731 */       float angle = startAngle + 3.1415927F * i / 8.0F;
/* 732 */       int spawnX = this.center.getX() + Mth.floor(Mth.cos(angle) * 32.0F * howFar) + this.random.nextInt(3) * Mth.floor(howFar);
/* 733 */       int spawnZ = this.center.getZ() + Mth.floor(Mth.sin(angle) * 32.0F * howFar) + this.random.nextInt(3) * Mth.floor(howFar);
/* 734 */       int spawnY = level.getHeight(Heightmap.Types.WORLD_SURFACE, spawnX, spawnZ);
/* 737 */       if (Mth.abs(spawnY - this.center.getY()) <= 96) {
/* 741 */         spawnPos.set(spawnX, spawnY, spawnZ);
/* 745 */         if (!level.isVillage((BlockPos)spawnPos) || secondsRemaining <= 7) {
/* 750 */           int delta = 10;
/* 751 */           if (level.hasChunksAt(spawnPos.getX() - 10, spawnPos.getZ() - 10, spawnPos.getX() + 10, spawnPos.getZ() + 10))
/* 757 */             if (level.isPositionEntityTicking((BlockPos)spawnPos))
/* 762 */               if (RAVAGER_SPAWN_PLACEMENT_TYPE.isSpawnPositionOk((LevelReader)level, (BlockPos)spawnPos, EntityTypes.RAVAGER) || (level
/* 763 */                 .getBlockState(spawnPos.below()).is(Blocks.SNOW) && level.getBlockState((BlockPos)spawnPos).isAir()))
/* 765 */                 return (BlockPos)spawnPos;   
/*     */         } 
/*     */       } 
/*     */     } 
/* 769 */     return null;
/*     */   }
/*     */   
/*     */   private boolean addWaveMob(ServerLevel level, int wave, Raider raider) {
/* 773 */     return addWaveMob(level, wave, raider, true);
/*     */   }
/*     */   
/*     */   public boolean addWaveMob(ServerLevel level, int wave, Raider raider, boolean updateHealth) {
/* 777 */     this.groupRaiderMap.computeIfAbsent(Integer.valueOf(wave), v -> Sets.newHashSet());
/* 778 */     Set<Raider> raiders = this.groupRaiderMap.get(Integer.valueOf(wave));
/* 779 */     Raider existingCopy = null;
/* 783 */     for (Raider r : raiders) {
/* 784 */       if (r.getUUID().equals(raider.getUUID())) {
/* 785 */         existingCopy = r;
/*     */         break;
/*     */       } 
/*     */     } 
/* 790 */     if (existingCopy != null) {
/* 791 */       raiders.remove(existingCopy);
/* 792 */       raiders.add(raider);
/*     */     } 
/* 795 */     raiders.add(raider);
/* 796 */     if (updateHealth)
/* 797 */       this.totalHealth += raider.getHealth(); 
/* 799 */     updateBossbar();
/* 800 */     setDirty(level);
/* 801 */     return true;
/*     */   }
/*     */   
/*     */   public void setLeader(int wave, Raider raider) {
/* 805 */     this.groupToLeaderMap.put(Integer.valueOf(wave), raider);
/* 806 */     raider.setItemSlot(EquipmentSlot.HEAD, getOminousBannerInstance((HolderGetter<BannerPattern>)raider.registryAccess().lookupOrThrow(Registries.BANNER_PATTERN)));
/* 807 */     raider.setDropChance(EquipmentSlot.HEAD, 2.0F);
/*     */   }
/*     */   
/*     */   public void removeLeader(int wave) {
/* 811 */     this.groupToLeaderMap.remove(Integer.valueOf(wave));
/*     */   }
/*     */   
/*     */   public BlockPos getCenter() {
/* 815 */     return this.center;
/*     */   }
/*     */   
/*     */   private void setCenter(BlockPos center) {
/* 819 */     this.center = center;
/*     */   }
/*     */   
/*     */   private int getDefaultNumSpawns(RaiderType type, int wav, boolean isBonusWave) {
/* 823 */     return isBonusWave ? type.spawnsPerWaveBeforeBonus[this.numGroups] : type.spawnsPerWaveBeforeBonus[wav];
/*     */   }
/*     */   
/*     */   private int getPotentialBonusSpawns(RaiderType type, RandomSource random, int wav, DifficultyInstance difficultyInstance, boolean isBonusWave) {
/*     */     int bonusSpawns;
/* 828 */     Difficulty difficulty = difficultyInstance.getDifficulty();
/* 829 */     boolean isEasy = (difficulty == Difficulty.EASY);
/* 830 */     boolean isNormal = (difficulty == Difficulty.NORMAL);
/* 832 */     switch (type.ordinal()) {
/*     */       case 3:
/* 835 */         if (!isEasy && wav > 2 && wav != 4) {
/* 836 */           int i = 1;
/*     */           break;
/*     */         } 
/* 839 */         return 0;
/*     */       case 0:
/*     */       case 2:
/* 843 */         if (isEasy) {
/* 844 */           int i = random.nextInt(2);
/*     */           break;
/*     */         } 
/* 845 */         if (isNormal) {
/* 846 */           int i = 1;
/*     */           break;
/*     */         } 
/* 848 */         bonusSpawns = 2;
/*     */         break;
/*     */       case 4:
/* 851 */         bonusSpawns = (!isEasy && isBonusWave) ? 1 : 0;
/*     */         break;
/*     */       default:
/* 853 */         return 0;
/*     */     } 
/* 857 */     return (bonusSpawns > 0) ? random.nextInt(bonusSpawns + 1) : 0;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 861 */     return this.active;
/*     */   }
/*     */   
/*     */   public int getNumGroups(Difficulty difficulty) {
/* 865 */     switch (difficulty) {
/*     */       default:
/* 865 */         throw new MatchException(null, null);
/*     */       case PEACEFUL:
/*     */       
/*     */       case EASY:
/*     */       
/*     */       case NORMAL:
/*     */       
/*     */       case HARD:
/*     */         break;
/*     */     } 
/* 865 */     return 
/*     */       
/* 869 */       7;
/*     */   }
/*     */   
/*     */   public float getEnchantOdds() {
/* 874 */     int raidOmenLvl = getRaidOmenLevel();
/* 875 */     if (raidOmenLvl == 2)
/* 876 */       return 0.1F; 
/* 878 */     if (raidOmenLvl == 3)
/* 879 */       return 0.25F; 
/* 881 */     if (raidOmenLvl == 4)
/* 882 */       return 0.5F; 
/* 884 */     if (raidOmenLvl == 5)
/* 885 */       return 0.75F; 
/* 887 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public void addHeroOfTheVillage(Entity killer) {
/* 891 */     this.heroesOfTheVillage.add(killer.getUUID());
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\entity\raid\Raid.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */