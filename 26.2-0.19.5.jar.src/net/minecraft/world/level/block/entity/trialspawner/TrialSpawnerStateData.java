/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntitySpawnRequest;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BaseSpawner;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ 
/*     */ public class TrialSpawnerStateData {
/*     */   private static final String TAG_SPAWN_DATA = "spawn_data";
/*     */   
/*     */   private static final String TAG_NEXT_MOB_SPAWNS_AT = "next_mob_spawns_at";
/*     */   
/*     */   private static final int DELAY_BETWEEN_PLAYER_SCANS = 20;
/*     */   
/*     */   private static final int TRIAL_OMEN_PER_BAD_OMEN_LEVEL = 18000;
/*     */   
/*  51 */   final Set<UUID> detectedPlayers = new HashSet<>();
/*     */   
/*  53 */   final Set<UUID> currentMobs = new HashSet<>();
/*     */   
/*     */   long cooldownEndsAt;
/*     */   
/*     */   long nextMobSpawnsAt;
/*     */   
/*     */   int totalMobsSpawned;
/*     */   
/*  62 */   Optional<SpawnData> nextSpawnData = Optional.empty();
/*     */   
/*  64 */   Optional<ResourceKey<LootTable>> ejectingLootTable = Optional.empty();
/*     */   
/*     */   private Entity displayEntity;
/*     */   
/*     */   private WeightedList<ItemStack> dispensing;
/*     */   
/*     */   double spin;
/*     */   
/*     */   double oSpin;
/*     */   
/*     */   public Packed pack() {
/*  73 */     return new Packed(
/*  74 */         Set.copyOf(this.detectedPlayers), 
/*  75 */         Set.copyOf(this.currentMobs), this.cooldownEndsAt, this.nextMobSpawnsAt, this.totalMobsSpawned, this.nextSpawnData, this.ejectingLootTable);
/*     */   }
/*     */   
/*     */   public void apply(Packed packed) {
/*  85 */     this.detectedPlayers.clear();
/*  86 */     this.detectedPlayers.addAll(packed.detectedPlayers);
/*  88 */     this.currentMobs.clear();
/*  89 */     this.currentMobs.addAll(packed.currentMobs);
/*  91 */     this.cooldownEndsAt = packed.cooldownEndsAt;
/*  92 */     this.nextMobSpawnsAt = packed.nextMobSpawnsAt;
/*  93 */     this.totalMobsSpawned = packed.totalMobsSpawned;
/*  94 */     this.nextSpawnData = packed.nextSpawnData;
/*  95 */     this.ejectingLootTable = packed.ejectingLootTable;
/*     */   }
/*     */   
/*     */   public void reset() {
/*  99 */     this.currentMobs.clear();
/* 100 */     this.nextSpawnData = Optional.empty();
/* 101 */     resetStatistics();
/*     */   }
/*     */   
/*     */   public void resetStatistics() {
/* 105 */     this.detectedPlayers.clear();
/* 106 */     this.totalMobsSpawned = 0;
/* 107 */     this.nextMobSpawnsAt = 0L;
/* 108 */     this.cooldownEndsAt = 0L;
/*     */   }
/*     */   
/*     */   public boolean hasMobToSpawn(TrialSpawner trialSpawner, RandomSource random) {
/* 112 */     boolean hasNextMobToSpawn = getOrCreateNextSpawnData(trialSpawner, random).getEntityToSpawn().getString("id").isPresent();
/* 113 */     return (hasNextMobToSpawn || !trialSpawner.activeConfig().spawnPotentialsDefinition().isEmpty());
/*     */   }
/*     */   
/*     */   public boolean hasFinishedSpawningAllMobs(TrialSpawnerConfig config, int additionalPlayers) {
/* 117 */     return (this.totalMobsSpawned >= config.calculateTargetTotalMobs(additionalPlayers));
/*     */   }
/*     */   
/*     */   public boolean haveAllCurrentMobsDied() {
/* 121 */     return this.currentMobs.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean isReadyToSpawnNextMob(ServerLevel serverLevel, TrialSpawnerConfig config, int additionalPlayers) {
/* 125 */     return (serverLevel.getGameTime() >= this.nextMobSpawnsAt && this.currentMobs.size() < config.calculateTargetSimultaneousMobs(additionalPlayers));
/*     */   }
/*     */   
/*     */   public int countAdditionalPlayers(BlockPos pos) {
/* 129 */     if (this.detectedPlayers.isEmpty())
/* 130 */       Util.logAndPauseIfInIde("Trial Spawner at " + String.valueOf(pos) + " has no detected players"); 
/* 132 */     return Math.max(0, this.detectedPlayers.size() - 1);
/*     */   }
/*     */   
/*     */   public void tryDetectPlayers(ServerLevel level, BlockPos pos, TrialSpawner trialSpawner) {
/* 136 */     boolean becameOminous, isThrottled = ((pos.asLong() + level.getGameTime()) % 20L != 0L);
/* 137 */     if (isThrottled)
/*     */       return; 
/* 141 */     if (trialSpawner.getState().equals(TrialSpawnerState.COOLDOWN) && trialSpawner.isOminous())
/*     */       return; 
/* 145 */     List<UUID> inLineOfSightPlayers = trialSpawner.getPlayerDetector().detect(level, trialSpawner.getEntitySelector(), pos, trialSpawner.getRequiredPlayerRange(), true);
/* 148 */     if (trialSpawner.isOminous() || inLineOfSightPlayers.isEmpty()) {
/* 149 */       becameOminous = false;
/*     */     } else {
/* 151 */       Optional<Pair<Player, Holder<MobEffect>>> playerWithOminousEffect = findPlayerWithOminousEffect(level, inLineOfSightPlayers);
/* 152 */       playerWithOminousEffect.ifPresent(playerAndEffect -> {
/*     */             Player player = (Player)playerAndEffect.getFirst();
/*     */             if (playerAndEffect.getSecond() == MobEffects.BAD_OMEN)
/*     */               transformBadOmenIntoTrialOmen(player); 
/*     */             level.levelEvent(3020, BlockPos.containing((Position)player.getEyePosition()), 0);
/*     */             trialSpawner.applyOminous(level, pos);
/*     */           });
/* 160 */       becameOminous = playerWithOminousEffect.isPresent();
/*     */     } 
/* 163 */     if (trialSpawner.getState().equals(TrialSpawnerState.COOLDOWN) && !becameOminous)
/*     */       return; 
/* 167 */     boolean isSearchingForFirstPlayer = (trialSpawner.getStateData()).detectedPlayers.isEmpty();
/* 170 */     List<UUID> foundPlayers = isSearchingForFirstPlayer ? inLineOfSightPlayers : trialSpawner.getPlayerDetector().detect(level, trialSpawner.getEntitySelector(), pos, trialSpawner.getRequiredPlayerRange(), false);
/* 172 */     if (this.detectedPlayers.addAll(foundPlayers)) {
/* 174 */       this.nextMobSpawnsAt = Math.max(level.getGameTime() + 40L, this.nextMobSpawnsAt);
/* 176 */       if (!becameOminous) {
/* 177 */         int event = trialSpawner.isOminous() ? 3019 : 3013;
/* 178 */         level.levelEvent(event, pos, this.detectedPlayers.size());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<Pair<Player, Holder<MobEffect>>> findPlayerWithOminousEffect(ServerLevel level, List<UUID> inLineOfSightPlayers) {
/* 184 */     Player playerWithBadOmen = null;
/* 186 */     for (UUID playerUuid : inLineOfSightPlayers) {
/* 187 */       Player player = level.getPlayerByUUID(playerUuid);
/* 188 */       if (player == null)
/*     */         continue; 
/* 192 */       Holder<MobEffect> trialOmen = MobEffects.TRIAL_OMEN;
/* 193 */       if (player.hasEffect(trialOmen))
/* 194 */         return Optional.of(Pair.of(player, trialOmen)); 
/* 197 */       if (player.hasEffect(MobEffects.BAD_OMEN))
/* 198 */         playerWithBadOmen = player; 
/*     */     } 
/* 202 */     return Optional.<Player>ofNullable(playerWithBadOmen).map(player -> Pair.of(player, MobEffects.BAD_OMEN));
/*     */   }
/*     */   
/*     */   public void resetAfterBecomingOminous(TrialSpawner trialSpawner, ServerLevel level) {
/* 206 */     Objects.requireNonNull(level);
/* 206 */     this.currentMobs.stream().map(level::getEntity).forEach(entity -> {
/*     */           if (entity == null)
/*     */             return; 
/*     */           level.levelEvent(3012, entity.blockPosition(), TrialSpawner.FlameParticle.NORMAL.encode());
/*     */           if (entity instanceof Mob) {
/*     */             Mob mob = (Mob)entity;
/*     */             mob.dropPreservedEquipment(level);
/*     */           } 
/*     */           entity.remove(Entity.RemovalReason.DISCARDED);
/*     */         });
/* 218 */     if (!trialSpawner.ominousConfig().spawnPotentialsDefinition().isEmpty())
/* 219 */       this.nextSpawnData = Optional.empty(); 
/* 222 */     this.totalMobsSpawned = 0;
/* 223 */     this.currentMobs.clear();
/* 224 */     this.nextMobSpawnsAt = level.getGameTime() + trialSpawner.ominousConfig().ticksBetweenSpawn();
/* 225 */     trialSpawner.markUpdated();
/* 226 */     this.cooldownEndsAt = level.getGameTime() + trialSpawner.ominousConfig().ticksBetweenItemSpawners();
/*     */   }
/*     */   
/*     */   private static void transformBadOmenIntoTrialOmen(Player player) {
/* 230 */     MobEffectInstance badOmen = player.getEffect(MobEffects.BAD_OMEN);
/* 231 */     if (badOmen == null)
/*     */       return; 
/* 235 */     int amplifier = badOmen.getAmplifier() + 1;
/* 236 */     int duration = 18000 * amplifier;
/* 237 */     player.removeEffect(MobEffects.BAD_OMEN);
/* 238 */     player.addEffect(new MobEffectInstance(MobEffects.TRIAL_OMEN, duration, 0));
/*     */   }
/*     */   
/*     */   public boolean isReadyToOpenShutter(ServerLevel serverLevel, float delayBeforeOpen, int targetCooldownLength) {
/* 242 */     long cooldownStartedAt = this.cooldownEndsAt - targetCooldownLength;
/* 243 */     return ((float)serverLevel.getGameTime() >= (float)cooldownStartedAt + delayBeforeOpen);
/*     */   }
/*     */   
/*     */   public boolean isReadyToEjectItems(ServerLevel serverLevel, float timeBetweenEjections, int targetCooldownLength) {
/* 247 */     long cooldownStartedAt = this.cooldownEndsAt - targetCooldownLength;
/* 248 */     return ((float)(serverLevel.getGameTime() - cooldownStartedAt) % timeBetweenEjections == 0.0F);
/*     */   }
/*     */   
/*     */   public boolean isCooldownFinished(ServerLevel serverLevel) {
/* 252 */     return (serverLevel.getGameTime() >= this.cooldownEndsAt);
/*     */   }
/*     */   
/*     */   protected SpawnData getOrCreateNextSpawnData(TrialSpawner trialSpawner, RandomSource random) {
/* 256 */     if (this.nextSpawnData.isPresent())
/* 257 */       return this.nextSpawnData.get(); 
/* 259 */     WeightedList<SpawnData> spawnPotentials = trialSpawner.activeConfig().spawnPotentialsDefinition();
/* 260 */     Optional<SpawnData> selected = spawnPotentials.isEmpty() ? this.nextSpawnData : spawnPotentials.getRandom(random);
/* 261 */     this.nextSpawnData = Optional.of(selected.orElseGet(SpawnData::new));
/* 262 */     trialSpawner.markUpdated();
/* 263 */     return this.nextSpawnData.get();
/*     */   }
/*     */   
/*     */   public Entity getOrCreateDisplayEntity(TrialSpawner trialSpawner, Level level, TrialSpawnerState state) {
/* 267 */     if (!state.hasSpinningMob())
/* 268 */       return null; 
/* 271 */     if (this.displayEntity == null) {
/* 272 */       CompoundTag entityToSpawn = getOrCreateNextSpawnData(trialSpawner, level.getRandom()).getEntityToSpawn();
/* 273 */       if (entityToSpawn.getString("id").isPresent())
/* 274 */         this.displayEntity = EntityType.loadEntityRecursive(entityToSpawn, level, new EntitySpawnRequest(EntitySpawnReason.TRIAL_SPAWNER, true), BaseSpawner.SET_DISPLAY_ENTITY_ID); 
/*     */     } 
/* 278 */     return this.displayEntity;
/*     */   }
/*     */   
/*     */   public CompoundTag getUpdateTag(TrialSpawnerState state) {
/* 282 */     CompoundTag tag = new CompoundTag();
/* 284 */     if (state == TrialSpawnerState.ACTIVE)
/* 285 */       tag.putLong("next_mob_spawns_at", this.nextMobSpawnsAt); 
/* 288 */     this.nextSpawnData.ifPresent(spawnData -> tag.store("spawn_data", SpawnData.CODEC, spawnData));
/* 292 */     return tag;
/*     */   }
/*     */   
/*     */   public double getSpin() {
/* 296 */     return this.spin;
/*     */   }
/*     */   
/*     */   public double getOSpin() {
/* 300 */     return this.oSpin;
/*     */   }
/*     */   
/*     */   public WeightedList<ItemStack> getDispensingItems(ServerLevel level, TrialSpawnerConfig config, BlockPos pos) {
/* 304 */     if (this.dispensing != null)
/* 305 */       return this.dispensing; 
/* 308 */     LootTable lootTable = level.getServer().reloadableRegistries().getLootTable(config.itemsToDropWhenOminous());
/* 309 */     LootParams params = (new LootParams.Builder(level)).create(LootContextParamSets.EMPTY);
/* 311 */     long simplePositionalSeed = lowResolutionPosition(level, pos);
/* 312 */     ObjectArrayList<ItemStack> lootDrops = lootTable.getRandomItems(params, simplePositionalSeed);
/* 314 */     if (lootDrops.isEmpty())
/* 315 */       return WeightedList.of(); 
/* 318 */     WeightedList.Builder<ItemStack> builder = WeightedList.builder();
/* 320 */     for (ObjectListIterator<ItemStack> objectListIterator = lootDrops.iterator(); objectListIterator.hasNext(); ) {
/* 320 */       ItemStack drop = objectListIterator.next();
/* 321 */       builder.add(drop.copyWithCount(1), drop.getCount());
/*     */     } 
/* 324 */     this.dispensing = builder.build();
/* 325 */     return this.dispensing;
/*     */   }
/*     */   
/*     */   private static long lowResolutionPosition(ServerLevel level, BlockPos pos) {
/* 334 */     BlockPos lowResolutionPosition = new BlockPos(Mth.floor(pos.getX() / 30.0F), Mth.floor(pos.getY() / 20.0F), Mth.floor(pos.getZ() / 30.0F));
/* 337 */     return level.getSeed() + lowResolutionPosition.asLong();
/*     */   }
/*     */   
/*     */   public static final class Packed extends Record {
/*     */     private final Set<UUID> detectedPlayers;
/*     */     
/*     */     private final Set<UUID> currentMobs;
/*     */     
/*     */     private final long cooldownEndsAt;
/*     */     
/*     */     private final long nextMobSpawnsAt;
/*     */     
/*     */     private final int totalMobsSpawned;
/*     */     
/*     */     private final Optional<SpawnData> nextSpawnData;
/*     */     
/*     */     private final Optional<ResourceKey<LootTable>> ejectingLootTable;
/*     */     
/*     */     public static final MapCodec<Packed> MAP_CODEC;
/*     */     
/*     */     public Packed(Set<UUID> detectedPlayers, Set<UUID> currentMobs, long cooldownEndsAt, long nextMobSpawnsAt, int totalMobsSpawned, Optional<SpawnData> nextSpawnData, Optional<ResourceKey<LootTable>> ejectingLootTable) {
/* 340 */       this.detectedPlayers = detectedPlayers;
/* 340 */       this.currentMobs = currentMobs;
/* 340 */       this.cooldownEndsAt = cooldownEndsAt;
/* 340 */       this.nextMobSpawnsAt = nextMobSpawnsAt;
/* 340 */       this.totalMobsSpawned = totalMobsSpawned;
/* 340 */       this.nextSpawnData = nextSpawnData;
/* 340 */       this.ejectingLootTable = ejectingLootTable;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #340	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #340	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #340	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/block/entity/trialspawner/TrialSpawnerStateData$Packed;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Set<UUID> detectedPlayers() {
/* 340 */       return this.detectedPlayers;
/*     */     }
/*     */     
/*     */     public Set<UUID> currentMobs() {
/* 340 */       return this.currentMobs;
/*     */     }
/*     */     
/*     */     public long cooldownEndsAt() {
/* 340 */       return this.cooldownEndsAt;
/*     */     }
/*     */     
/*     */     public long nextMobSpawnsAt() {
/* 340 */       return this.nextMobSpawnsAt;
/*     */     }
/*     */     
/*     */     public int totalMobsSpawned() {
/* 340 */       return this.totalMobsSpawned;
/*     */     }
/*     */     
/*     */     public Optional<SpawnData> nextSpawnData() {
/* 340 */       return this.nextSpawnData;
/*     */     }
/*     */     
/*     */     public Optional<ResourceKey<LootTable>> ejectingLootTable() {
/* 340 */       return this.ejectingLootTable;
/*     */     }
/*     */     
/*     */     static {
/* 349 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)UUIDUtil.CODEC_SET.lenientOptionalFieldOf("registered_players", Set.of()).forGetter(Packed::detectedPlayers), (App)UUIDUtil.CODEC_SET.lenientOptionalFieldOf("current_mobs", Set.of()).forGetter(Packed::currentMobs), (App)Codec.LONG.lenientOptionalFieldOf("cooldown_ends_at", Long.valueOf(0L)).forGetter(Packed::cooldownEndsAt), (App)Codec.LONG.lenientOptionalFieldOf("next_mob_spawns_at", Long.valueOf(0L)).forGetter(Packed::nextMobSpawnsAt), (App)Codec.intRange(0, 2147483647).lenientOptionalFieldOf("total_mobs_spawned", Integer.valueOf(0)).forGetter(Packed::totalMobsSpawned), (App)SpawnData.CODEC.lenientOptionalFieldOf("spawn_data").forGetter(Packed::nextSpawnData), (App)LootTable.KEY_CODEC.lenientOptionalFieldOf("ejecting_loot_table").forGetter(Packed::ejectingLootTable)).apply((Applicative)i, Packed::new));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerStateData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */