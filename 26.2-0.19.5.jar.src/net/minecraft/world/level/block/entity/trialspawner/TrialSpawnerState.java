/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.OminousItemSpawner;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ 
/*     */ public enum TrialSpawnerState implements StringRepresentable {
/*  35 */   INACTIVE("inactive", 0, ParticleEmission.NONE, -1.0D, false),
/*  36 */   WAITING_FOR_PLAYERS("waiting_for_players", 4, ParticleEmission.SMALL_FLAMES, 200.0D, true),
/*  37 */   ACTIVE("active", 8, ParticleEmission.FLAMES_AND_SMOKE, 1000.0D, true),
/*  38 */   WAITING_FOR_REWARD_EJECTION("waiting_for_reward_ejection", 8, ParticleEmission.SMALL_FLAMES, -1.0D, false),
/*  39 */   EJECTING_REWARD("ejecting_reward", 8, ParticleEmission.SMALL_FLAMES, -1.0D, false),
/*  40 */   COOLDOWN("cooldown", 0, ParticleEmission.SMOKE_INSIDE_AND_TOP_FACE, -1.0D, false);
/*     */   
/*     */   private static final float DELAY_BEFORE_EJECT_AFTER_KILLING_LAST_MOB = 40.0F;
/*     */   
/*     */   private static final int TIME_BETWEEN_EACH_EJECTION;
/*     */   
/*     */   private final String name;
/*     */   
/*     */   private final int lightLevel;
/*     */   
/*     */   private final double spinningMobSpeed;
/*     */   
/*     */   private final ParticleEmission particleEmission;
/*     */   
/*     */   private final boolean isCapableOfSpawning;
/*     */   
/*     */   static {
/*  43 */     TIME_BETWEEN_EACH_EJECTION = Mth.floor(30.0F);
/*     */   }
/*     */   
/*     */   TrialSpawnerState(String name, int lightLevel, ParticleEmission particleEmission, double spinningMobSpeed, boolean isCapableOfSpawning) {
/*  51 */     this.name = name;
/*  52 */     this.lightLevel = lightLevel;
/*  53 */     this.particleEmission = particleEmission;
/*  54 */     this.spinningMobSpeed = spinningMobSpeed;
/*  55 */     this.isCapableOfSpawning = isCapableOfSpawning;
/*     */   }
/*     */   
/*     */   TrialSpawnerState tickAndGetNext(BlockPos spawnerPos, TrialSpawner trialSpawner, ServerLevel serverLevel) {
/*     */     int additionalPlayers;
/*  60 */     TrialSpawnerStateData data = trialSpawner.getStateData();
/*  61 */     TrialSpawnerConfig config = trialSpawner.activeConfig();
/*  63 */     RandomSource random = serverLevel.getRandom();
/*  64 */     switch (ordinal()) {
/*     */       default:
/*  64 */         throw new MatchException(null, null);
/*     */       case 0:
/*  64 */         return 
/*  65 */           (data.getOrCreateDisplayEntity(trialSpawner, (Level)serverLevel, WAITING_FOR_PLAYERS) == null) ? 
/*  66 */           this : 
/*  67 */           WAITING_FOR_PLAYERS;
/*     */       case 1:
/*  70 */         data.resetStatistics();
/*  76 */         data.tryDetectPlayers(serverLevel, spawnerPos, trialSpawner);
/*  77 */         return !trialSpawner.canSpawnInLevel(serverLevel) ? this : (!data.hasMobToSpawn(trialSpawner, random) ? INACTIVE : (data.detectedPlayers.isEmpty() ? 
/*  78 */           this : 
/*  79 */           ACTIVE));
/*     */       case 2:
/*  83 */         data.resetStatistics();
/*  91 */         additionalPlayers = data.countAdditionalPlayers(spawnerPos);
/*  92 */         data.tryDetectPlayers(serverLevel, spawnerPos, trialSpawner);
/*  94 */         if (trialSpawner.isOminous())
/*  95 */           spawnOminousOminousItemSpawner(serverLevel, spawnerPos, trialSpawner); 
/*  98 */         if (data.hasFinishedSpawningAllMobs(config, additionalPlayers)) {
/*  99 */           if (data.haveAllCurrentMobsDied()) {
/* 100 */             data.cooldownEndsAt = serverLevel.getGameTime() + trialSpawner.getTargetCooldownLength();
/* 101 */             data.totalMobsSpawned = 0;
/* 102 */             data.nextMobSpawnsAt = 0L;
/*     */           } 
/* 105 */         } else if (data.isReadyToSpawnNextMob(serverLevel, config, additionalPlayers)) {
/* 106 */           trialSpawner.spawnMob(serverLevel, spawnerPos).ifPresent(entityId -> {
/*     */                 data.currentMobs.add(entityId);
/*     */                 data.totalMobsSpawned++;
/*     */                 data.nextMobSpawnsAt = serverLevel.getGameTime() + config.ticksBetweenSpawn();
/*     */                 config.spawnPotentialsDefinition().getRandom(random).ifPresent(());
/*     */               });
/*     */         } 
/* 118 */         return !trialSpawner.canSpawnInLevel(serverLevel) ? WAITING_FOR_PLAYERS : (!data.hasMobToSpawn(trialSpawner, random) ? INACTIVE : this);
/*     */       case 3:
/* 122 */         serverLevel.playSound(null, spawnerPos, SoundEvents.TRIAL_SPAWNER_OPEN_SHUTTER, SoundSource.BLOCKS);
/* 123 */         return data.isReadyToOpenShutter(serverLevel, 40.0F, trialSpawner.getTargetCooldownLength()) ? EJECTING_REWARD : 
/*     */           
/* 125 */           this;
/*     */       case 4:
/* 133 */         serverLevel.playSound(null, spawnerPos, SoundEvents.TRIAL_SPAWNER_CLOSE_SHUTTER, SoundSource.BLOCKS);
/* 134 */         data.ejectingLootTable = Optional.empty();
/* 138 */         if (data.ejectingLootTable.isEmpty())
/* 139 */           data.ejectingLootTable = config.lootTablesToEject().getRandom(random); 
/* 142 */         data.ejectingLootTable.ifPresent(lootTable -> trialSpawner.ejectReward(serverLevel, spawnerPos, lootTable));
/* 143 */         data.detectedPlayers.remove(data.detectedPlayers.iterator().next());
/* 144 */         return !data.isReadyToEjectItems(serverLevel, TIME_BETWEEN_EACH_EJECTION, trialSpawner.getTargetCooldownLength()) ? this : (data.detectedPlayers.isEmpty() ? COOLDOWN : this);
/*     */       case 5:
/*     */         break;
/*     */     } 
/* 147 */     data.tryDetectPlayers(serverLevel, spawnerPos, trialSpawner);
/* 150 */     data.totalMobsSpawned = 0;
/* 151 */     data.nextMobSpawnsAt = 0L;
/* 154 */     trialSpawner.removeOminous(serverLevel, spawnerPos);
/* 155 */     data.reset();
/* 156 */     return !data.detectedPlayers.isEmpty() ? ACTIVE : (data.isCooldownFinished(serverLevel) ? WAITING_FOR_PLAYERS : 
/*     */       
/* 158 */       this);
/*     */   }
/*     */   
/*     */   private void spawnOminousOminousItemSpawner(ServerLevel level, BlockPos trialSpawnerPos, TrialSpawner trialSpawner) {
/* 164 */     TrialSpawnerStateData data = trialSpawner.getStateData();
/* 165 */     TrialSpawnerConfig config = trialSpawner.activeConfig();
/* 167 */     ItemStack itemToDispense = data.getDispensingItems(level, config, trialSpawnerPos).getRandom(level.getRandom()).orElse(ItemStack.EMPTY);
/* 168 */     if (itemToDispense.isEmpty())
/*     */       return; 
/* 172 */     if (timeToSpawnItemSpawner(level, data))
/* 173 */       calculatePositionToSpawnSpawner(level, trialSpawnerPos, trialSpawner, data).ifPresent(pos -> {
/*     */             OminousItemSpawner itemSpawner = OminousItemSpawner.create((Level)level, itemToDispense);
/*     */             itemSpawner.snapTo(pos);
/*     */             level.addFreshEntity((Entity)itemSpawner);
/*     */             float pitch = (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F;
/*     */             level.playSound(null, BlockPos.containing((Position)pos), SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM_BEGIN, SoundSource.BLOCKS, 1.0F, pitch);
/*     */             data.cooldownEndsAt = level.getGameTime() + trialSpawner.ominousConfig().ticksBetweenItemSpawners();
/*     */           }); 
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> calculatePositionToSpawnSpawner(ServerLevel level, BlockPos trialSpawnerPos, TrialSpawner trialSpawner, TrialSpawnerStateData data) {
/* 187 */     Objects.requireNonNull(level);
/* 193 */     List<Player> nearbyPlayers = data.detectedPlayers.stream().map(level::getPlayerByUUID).filter(Objects::nonNull).filter(player -> (!player.isCreative() && !player.isSpectator() && player.isAlive() && player.distanceToSqr(Vec3.atCenterOf((Vec3i)trialSpawnerPos)) <= Mth.square(trialSpawner.getRequiredPlayerRange()))).toList();
/* 195 */     if (nearbyPlayers.isEmpty())
/* 196 */       return Optional.empty(); 
/* 199 */     Entity entity = selectEntityToSpawnItemAbove(nearbyPlayers, data.currentMobs, trialSpawner, trialSpawnerPos, level);
/* 201 */     if (entity == null)
/* 202 */       return Optional.empty(); 
/* 205 */     return calculatePositionAbove(entity, level);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> calculatePositionAbove(Entity entityToSpawnItemAbove, ServerLevel level) {
/* 209 */     Vec3 entityPos = entityToSpawnItemAbove.position();
/* 210 */     Vec3 trySpawnPos = entityPos.relative(Direction.UP, (entityToSpawnItemAbove.getBbHeight() + 2.0F + level.getRandom().nextInt(4)));
/* 211 */     BlockHitResult hitResult = level.clip(new ClipContext(entityPos, trySpawnPos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, CollisionContext.empty()));
/* 212 */     Vec3 down = Vec3.atCenterOf((Vec3i)hitResult.getBlockPos()).relative(Direction.DOWN, 1.0D);
/* 213 */     BlockPos blockPosDown = BlockPos.containing((Position)down);
/* 214 */     if (!level.getBlockState(blockPosDown).getCollisionShape((BlockGetter)level, blockPosDown).isEmpty())
/* 215 */       return Optional.empty(); 
/* 217 */     return Optional.of(down);
/*     */   }
/*     */   
/*     */   private static Entity selectEntityToSpawnItemAbove(List<Player> nearbyPlayers, Set<UUID> mobIds, TrialSpawner trialSpawner, BlockPos spawnerPos, ServerLevel level) {
/* 222 */     Objects.requireNonNull(level);
/* 224 */     Stream<Entity> nearbyMobs = mobIds.stream().map(level::getEntity).filter(Objects::nonNull).filter(target -> (target.isAlive() && target.distanceToSqr(Vec3.atCenterOf((Vec3i)spawnerPos)) <= Mth.square(trialSpawner.getRequiredPlayerRange())));
/* 226 */     RandomSource random = level.getRandom();
/* 227 */     List<? extends Entity> eligibleEntities = random.nextBoolean() ? nearbyMobs.toList() : (List)nearbyPlayers;
/* 229 */     if (eligibleEntities.isEmpty())
/* 230 */       return null; 
/* 233 */     if (eligibleEntities.size() == 1)
/* 234 */       return eligibleEntities.getFirst(); 
/* 237 */     return (Entity)Util.getRandom(eligibleEntities, random);
/*     */   }
/*     */   
/*     */   private boolean timeToSpawnItemSpawner(ServerLevel serverLevel, TrialSpawnerStateData data) {
/* 241 */     return (serverLevel.getGameTime() >= data.cooldownEndsAt);
/*     */   }
/*     */   
/*     */   public int lightLevel() {
/* 245 */     return this.lightLevel;
/*     */   }
/*     */   
/*     */   public double spinningMobSpeed() {
/* 249 */     return this.spinningMobSpeed;
/*     */   }
/*     */   
/*     */   public boolean hasSpinningMob() {
/* 253 */     return (this.spinningMobSpeed >= 0.0D);
/*     */   }
/*     */   
/*     */   public boolean isCapableOfSpawning() {
/* 257 */     return this.isCapableOfSpawning;
/*     */   }
/*     */   
/*     */   public void emitParticles(Level level, BlockPos blockPos, boolean isOminous) {
/* 261 */     this.particleEmission.emit(level, level.getRandom(), blockPos, isOminous);
/*     */   }
/*     */   
/*     */   public String getSerializedName() {
/* 266 */     return this.name;
/*     */   }
/*     */   
/*     */   private static class LightLevel {
/*     */     private static final int UNLIT = 0;
/*     */     
/*     */     private static final int HALF_LIT = 4;
/*     */     
/*     */     private static final int LIT = 8;
/*     */   }
/*     */   
/*     */   private static class SpinningMob {
/*     */     private static final double NONE = -1.0D;
/*     */     
/*     */     private static final double SLOW = 200.0D;
/*     */     
/*     */     private static final double FAST = 1000.0D;
/*     */   }
/*     */   
/*     */   private static interface ParticleEmission {
/*     */     public static final ParticleEmission NONE = (level, random, pos, isOminous) -> {
/*     */       
/*     */       };
/*     */     
/*     */     public static final ParticleEmission SMALL_FLAMES;
/*     */     
/*     */     public static final ParticleEmission FLAMES_AND_SMOKE;
/*     */     
/*     */     public static final ParticleEmission SMOKE_INSIDE_AND_TOP_FACE;
/*     */     
/*     */     static {
/* 283 */       SMALL_FLAMES = ((level, random, pos, isOminous) -> {
/*     */           if (random.nextInt(2) == 0) {
/*     */             Vec3 vec = Vec3.atCenterOf((Vec3i)pos).offsetRandom(random, 0.9F);
/*     */             addParticle(isOminous ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMALL_FLAME, vec, level);
/*     */           } 
/*     */         });
/* 289 */       FLAMES_AND_SMOKE = ((level, random, pos, isOminous) -> {
/*     */           Vec3 vec = Vec3.atCenterOf((Vec3i)pos).offsetRandom(random, 1.0F);
/*     */           addParticle(ParticleTypes.SMOKE, vec, level);
/*     */           addParticle(isOminous ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, vec, level);
/*     */         });
/* 294 */       SMOKE_INSIDE_AND_TOP_FACE = ((level, random, pos, isOminous) -> {
/*     */           Vec3 vec = Vec3.atCenterOf((Vec3i)pos).offsetRandom(random, 0.9F);
/*     */           if (random.nextInt(3) == 0)
/*     */             addParticle(ParticleTypes.SMOKE, vec, level); 
/*     */           if (level.getGameTime() % 20L == 0L) {
/*     */             Vec3 topFaceVec = Vec3.atCenterOf((Vec3i)pos).add(0.0D, 0.5D, 0.0D);
/*     */             int smokeCount = level.getRandom().nextInt(4) + 20;
/*     */             for (int i = 0; i < smokeCount; i++)
/*     */               addParticle(ParticleTypes.SMOKE, topFaceVec, level); 
/*     */           } 
/*     */         });
/*     */     }
/*     */     
/*     */     private static void addParticle(SimpleParticleType smoke, Vec3 vec, Level level) {
/* 310 */       level.addParticle((ParticleOptions)smoke, vec.x(), vec.y(), vec.z(), 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */     
/*     */     void emit(Level param1Level, RandomSource param1RandomSource, BlockPos param1BlockPos, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\block\entity\trialspawner\TrialSpawnerState.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */