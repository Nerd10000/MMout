/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.QuartPos;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.SpawnPlacements;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.structures.NetherFortressStructure;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.LevelData;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class NaturalSpawner {
/*  59 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MIN_SPAWN_DISTANCE = 24;
/*     */   
/*     */   public static final int SPAWN_DISTANCE_CHUNK = 8;
/*     */   
/*     */   public static final int SPAWN_DISTANCE_BLOCK = 128;
/*     */   
/*  64 */   public static final int INSCRIBED_SQUARE_SPAWN_DISTANCE_CHUNK = Mth.floor(8.0F / Mth.SQRT_OF_TWO);
/*     */   
/*  65 */   private static final int MAGIC_NUMBER = (int)Math.pow(17.0D, 2.0D);
/*     */   
/*     */   private static final MobCategory[] SPAWNING_CATEGORIES;
/*     */   
/*     */   static {
/*  66 */     SPAWNING_CATEGORIES = (MobCategory[])Stream.<MobCategory>of(MobCategory.values()).filter(c -> (c != MobCategory.MISC)).toArray(x$0 -> new MobCategory[x$0]);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface ChunkGetter {
/*     */     void query(long param1Long, Consumer<LevelChunk> param1Consumer);
/*     */   }
/*     */   
/*     */   public static class SpawnState {
/*     */     private final int spawnableChunkCount;
/*     */     
/*     */     private final Object2IntOpenHashMap<MobCategory> mobCategoryCounts;
/*     */     
/*     */     private final PotentialCalculator spawnPotential;
/*     */     
/*     */     private final Object2IntMap<MobCategory> unmodifiableMobCategoryCounts;
/*     */     
/*     */     private final LocalMobCapCalculator localMobCapCalculator;
/*     */     
/*     */     private BlockPos lastCheckedPos;
/*     */     
/*     */     private EntityType<?> lastCheckedType;
/*     */     
/*     */     private double lastCharge;
/*     */     
/*     */     private SpawnState(int spawnableChunkCount, Object2IntOpenHashMap<MobCategory> mobCategoryCounts, PotentialCalculator spawnPotential, LocalMobCapCalculator localMobCapCalculator) {
/*  80 */       this.spawnableChunkCount = spawnableChunkCount;
/*  81 */       this.mobCategoryCounts = mobCategoryCounts;
/*  82 */       this.spawnPotential = spawnPotential;
/*  83 */       this.localMobCapCalculator = localMobCapCalculator;
/*  84 */       this.unmodifiableMobCategoryCounts = Object2IntMaps.unmodifiable((Object2IntMap)mobCategoryCounts);
/*     */     }
/*     */     
/*     */     private boolean canSpawn(EntityType<?> type, BlockPos testPos, ChunkAccess chunk) {
/*  88 */       this.lastCheckedPos = testPos;
/*  89 */       this.lastCheckedType = type;
/*  91 */       MobSpawnSettings.MobSpawnCost mobSpawnCost = NaturalSpawner.getRoughBiome(testPos, chunk).getMobSettings().getMobSpawnCost(type);
/*  92 */       if (mobSpawnCost == null) {
/*  93 */         this.lastCharge = 0.0D;
/*  94 */         return true;
/*     */       } 
/*  96 */       double charge = mobSpawnCost.charge();
/*  97 */       this.lastCharge = charge;
/*  98 */       double energyChange = this.spawnPotential.getPotentialEnergyChange(testPos, charge);
/*  99 */       return (energyChange <= mobSpawnCost.energyBudget());
/*     */     }
/*     */     
/*     */     private void afterSpawn(Mob mob, ChunkAccess chunk) {
/*     */       double charge;
/* 103 */       EntityType<?> type = mob.getType();
/* 105 */       BlockPos pos = mob.blockPosition();
/* 106 */       if (pos.equals(this.lastCheckedPos) && type == this.lastCheckedType) {
/* 107 */         charge = this.lastCharge;
/*     */       } else {
/* 110 */         MobSpawnSettings.MobSpawnCost mobSpawnCost = NaturalSpawner.getRoughBiome(pos, chunk).getMobSettings().getMobSpawnCost(type);
/* 111 */         if (mobSpawnCost != null) {
/* 112 */           charge = mobSpawnCost.charge();
/*     */         } else {
/* 114 */           charge = 0.0D;
/*     */         } 
/*     */       } 
/* 117 */       this.spawnPotential.addCharge(pos, charge);
/* 118 */       MobCategory category = type.getCategory();
/* 119 */       this.mobCategoryCounts.addTo(category, 1);
/* 120 */       this.localMobCapCalculator.addMob(ChunkPos.containing(pos), category);
/*     */     }
/*     */     
/*     */     public int getSpawnableChunkCount() {
/* 124 */       return this.spawnableChunkCount;
/*     */     }
/*     */     
/*     */     public Object2IntMap<MobCategory> getMobCategoryCounts() {
/* 128 */       return this.unmodifiableMobCategoryCounts;
/*     */     }
/*     */     
/*     */     private boolean canSpawnForCategoryGlobal(MobCategory mobCategory) {
/* 132 */       int maxMobCount = mobCategory.getMaxInstancesPerChunk() * this.spawnableChunkCount / NaturalSpawner.MAGIC_NUMBER;
/* 133 */       return (this.mobCategoryCounts.getInt(mobCategory) < maxMobCount);
/*     */     }
/*     */     
/*     */     private boolean canSpawnForCategoryLocal(MobCategory mobCategory, ChunkPos chunkPos) {
/* 137 */       return (this.localMobCapCalculator.canSpawn(mobCategory, chunkPos) || SharedConstants.DEBUG_IGNORE_LOCAL_MOB_CAP);
/*     */     }
/*     */   }
/*     */   
/*     */   public static SpawnState createState(int spawnableChunkCount, Iterable<Entity> entities, ChunkGetter chunkGetter, LocalMobCapCalculator localMobCapCalculator) {
/* 160 */     PotentialCalculator spawnPotential = new PotentialCalculator();
/* 161 */     Object2IntOpenHashMap<MobCategory> mobCounts = new Object2IntOpenHashMap();
/* 163 */     for (Iterator<Entity> iterator = entities.iterator(); iterator.hasNext(); ) {
/* 163 */       Entity entity = iterator.next();
/* 164 */       if (entity instanceof Mob) {
/* 164 */         Mob mob = (Mob)entity;
/* 164 */         if (mob.isPersistenceRequired() || mob.requiresCustomPersistence())
/*     */           continue; 
/*     */       } 
/* 167 */       MobCategory category = entity.getType().getCategory();
/* 168 */       if (category == MobCategory.MISC)
/*     */         continue; 
/* 172 */       BlockPos pos = entity.blockPosition();
/* 174 */       chunkGetter.query(ChunkPos.pack(pos), chunk -> {
/*     */             MobSpawnSettings.MobSpawnCost mobSpawnCost = getRoughBiome(pos, (ChunkAccess)chunk).getMobSettings().getMobSpawnCost(entity.getType());
/*     */             if (mobSpawnCost != null)
/*     */               spawnPotential.addCharge(entity.blockPosition(), mobSpawnCost.charge()); 
/*     */             if (entity instanceof Mob)
/*     */               localMobCapCalculator.addMob(chunk.getPos(), category); 
/*     */             mobCounts.addTo(category, 1);
/*     */           });
/*     */     } 
/* 186 */     return new SpawnState(spawnableChunkCount, mobCounts, spawnPotential, localMobCapCalculator);
/*     */   }
/*     */   
/*     */   private static Biome getRoughBiome(BlockPos pos, ChunkAccess chunk) {
/* 191 */     return (Biome)chunk.getNoiseBiome(QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(pos.getY()), QuartPos.fromBlock(pos.getZ())).value();
/*     */   }
/*     */   
/*     */   public static List<MobCategory> getFilteredSpawningCategories(SpawnState state, boolean spawnEnemies, boolean spawnPersistent) {
/* 195 */     List<MobCategory> spawningCategories = new ArrayList<>(SPAWNING_CATEGORIES.length);
/* 196 */     for (MobCategory mobCategory : SPAWNING_CATEGORIES) {
/* 197 */       if ((spawnEnemies || mobCategory.isFriendly()) && (spawnPersistent || 
/* 198 */         !mobCategory.isPersistent()) && state
/* 199 */         .canSpawnForCategoryGlobal(mobCategory))
/* 201 */         spawningCategories.add(mobCategory); 
/*     */     } 
/* 204 */     return spawningCategories;
/*     */   }
/*     */   
/*     */   public static void spawnForChunk(ServerLevel level, LevelChunk chunk, SpawnState state, List<MobCategory> spawningCategories) {
/* 208 */     ProfilerFiller profiler = Profiler.get();
/* 209 */     profiler.push("spawner");
/* 210 */     for (MobCategory mobCategory : spawningCategories) {
/* 211 */       if (state.canSpawnForCategoryLocal(mobCategory, chunk.getPos())) {
/* 212 */         Objects.requireNonNull(state);
/* 212 */         Objects.requireNonNull(state);
/* 212 */         spawnCategoryForChunk(mobCategory, level, chunk, state::canSpawn, state::afterSpawn);
/*     */       } 
/*     */     } 
/* 215 */     profiler.pop();
/*     */   }
/*     */   
/*     */   public static void spawnCategoryForChunk(MobCategory mobCategory, ServerLevel level, LevelChunk chunk, SpawnPredicate extraTest, AfterSpawnCallback spawnCallback) {
/* 219 */     BlockPos start = getRandomPosWithin((Level)level, chunk);
/* 221 */     if (start.getY() < level.getMinY() + 1)
/*     */       return; 
/* 224 */     spawnCategoryForPosition(mobCategory, level, (ChunkAccess)chunk, start, extraTest, spawnCallback);
/*     */   }
/*     */   
/*     */   @VisibleForDebug
/*     */   public static void spawnCategoryForPosition(MobCategory mobCategory, ServerLevel level, BlockPos start) {
/* 229 */     spawnCategoryForPosition(mobCategory, level, level.getChunk(start), start, (type, chunk, pos) -> true, (mob, chunk) -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   public static void spawnCategoryForPosition(MobCategory mobCategory, ServerLevel level, ChunkAccess chunk, BlockPos start, SpawnPredicate extraTest, AfterSpawnCallback spawnCallback) {
/* 233 */     StructureManager structureManager = level.structureManager();
/* 234 */     ChunkGenerator generator = level.getChunkSource().getGenerator();
/* 235 */     int yStart = start.getY();
/* 237 */     BlockState state = chunk.getBlockState(start);
/* 238 */     if (state.isRedstoneConductor((BlockGetter)chunk, start))
/*     */       return; 
/* 242 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 243 */     int clusterSize = 0;
/* 245 */     for (int groupCount = 0; groupCount < 3; groupCount++) {
/* 246 */       int x = start.getX();
/* 247 */       int z = start.getZ();
/* 248 */       int ss = 6;
/* 250 */       MobSpawnSettings.SpawnerData currentSpawnData = null;
/* 251 */       SpawnGroupData groupData = null;
/* 253 */       int max = Mth.ceil(level.random.nextFloat() * 4.0F);
/* 254 */       int groupSize = 0;
/* 257 */       for (int ll = 0; ll < max; ll++) {
/* 258 */         x += level.random.nextInt(6) - level.random.nextInt(6);
/* 259 */         z += level.random.nextInt(6) - level.random.nextInt(6);
/* 261 */         pos.set(x, yStart, z);
/* 263 */         double xx = x + 0.5D;
/* 264 */         double zz = z + 0.5D;
/* 266 */         Player nearestPlayer = level.getNearestPlayer(xx, yStart, zz, -1.0D, false);
/* 267 */         if (nearestPlayer != null) {
/* 271 */           double nearestPlayerDistanceSqr = nearestPlayer.distanceToSqr(xx, yStart, zz);
/* 272 */           if (isRightDistanceToPlayerAndSpawnPoint(level, chunk, pos, nearestPlayerDistanceSqr)) {
/* 276 */             if (currentSpawnData == null) {
/* 277 */               Optional<MobSpawnSettings.SpawnerData> nextSpawnData = getRandomSpawnMobAt(level, structureManager, generator, mobCategory, level.random, (BlockPos)pos);
/* 278 */               if (nextSpawnData.isEmpty())
/*     */                 break; 
/* 281 */               currentSpawnData = nextSpawnData.get();
/* 284 */               max = currentSpawnData.minCount() + level.random.nextInt(1 + currentSpawnData.maxCount() - currentSpawnData.minCount());
/*     */             } 
/* 287 */             if (isValidSpawnPostitionForType(level, mobCategory, structureManager, generator, currentSpawnData, pos, nearestPlayerDistanceSqr))
/* 291 */               if (extraTest.test(currentSpawnData.type(), (BlockPos)pos, chunk)) {
/* 296 */                 Mob mob = getMobForSpawn(level, currentSpawnData.type());
/* 297 */                 if (mob == null)
/*     */                   return; 
/* 301 */                 mob.snapTo(xx, yStart, zz, level.random.nextFloat() * 360.0F, 0.0F);
/* 303 */                 if (isValidPositionForMob(level, mob, nearestPlayerDistanceSqr)) {
/* 307 */                   groupData = mob.finalizeSpawn((ServerLevelAccessor)level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.NATURAL, groupData);
/* 309 */                   clusterSize++;
/* 310 */                   groupSize++;
/* 311 */                   level.addFreshEntityWithPassengers((Entity)mob);
/* 312 */                   spawnCallback.run(mob, chunk);
/* 314 */                   if (clusterSize >= mob.getMaxSpawnClusterSize())
/*     */                     return; 
/* 317 */                   if (mob.isMaxGroupSizeReached(groupSize))
/*     */                     break; 
/*     */                 } 
/*     */               }  
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isRightDistanceToPlayerAndSpawnPoint(ServerLevel level, ChunkAccess chunk, BlockPos.MutableBlockPos pos, double nearestPlayerDistanceSqr) {
/* 325 */     if (nearestPlayerDistanceSqr <= 576.0D)
/* 326 */       return false; 
/* 328 */     LevelData.RespawnData respawnData = level.getRespawnData();
/* 329 */     if (respawnData.dimension() == level.dimension() && respawnData.pos().closerToCenterThan((Position)new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D), 24.0D))
/* 330 */       return false; 
/* 333 */     ChunkPos chunkPos = ChunkPos.containing((BlockPos)pos);
/* 334 */     return (Objects.equals(chunkPos, chunk.getPos()) || level.canSpawnEntitiesInChunk(chunkPos));
/*     */   }
/*     */   
/*     */   private static boolean isValidSpawnPostitionForType(ServerLevel level, MobCategory mobCategory, StructureManager structureManager, ChunkGenerator generator, MobSpawnSettings.SpawnerData currentSpawnData, BlockPos.MutableBlockPos pos, double nearestPlayerDistanceSqr) {
/* 338 */     EntityType<?> type = currentSpawnData.type();
/* 340 */     if (type.getCategory() == MobCategory.MISC)
/* 341 */       return false; 
/* 344 */     if (!type.canSpawnFarFromPlayer() && nearestPlayerDistanceSqr > (type.getCategory().getDespawnDistance() * type.getCategory().getDespawnDistance()))
/* 345 */       return false; 
/* 348 */     if (!type.canSummon() || !canSpawnMobAt(level, structureManager, generator, mobCategory, currentSpawnData, (BlockPos)pos))
/* 349 */       return false; 
/* 352 */     if (!SpawnPlacements.isSpawnPositionOk(type, (LevelReader)level, (BlockPos)pos))
/* 353 */       return false; 
/* 355 */     if (!SpawnPlacements.checkSpawnRules(type, (ServerLevelAccessor)level, EntitySpawnReason.NATURAL, (BlockPos)pos, level.random))
/* 356 */       return false; 
/* 358 */     if (!level.noCollision(type.getSpawnAABB(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D)))
/* 359 */       return false; 
/* 361 */     return true;
/*     */   }
/*     */   
/*     */   private static Mob getMobForSpawn(ServerLevel level, EntityType<?> type) {
/*     */     try {
/* 366 */       Entity entity = type.create((Level)level, EntitySpawnReason.NATURAL);
/* 366 */       if (entity instanceof Mob) {
/* 366 */         Mob mob = (Mob)entity;
/* 367 */         return mob;
/*     */       } 
/* 369 */       LOGGER.warn("Can't spawn entity of type: {}", BuiltInRegistries.ENTITY_TYPE.getKey(type));
/* 370 */     } catch (Exception e) {
/* 371 */       LOGGER.warn("Failed to create mob", e);
/*     */     } 
/* 373 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean isValidPositionForMob(ServerLevel level, Mob mob, double nearestPlayerDistanceSqr) {
/* 377 */     if (nearestPlayerDistanceSqr > (mob.getType().getCategory().getDespawnDistance() * mob.getType().getCategory().getDespawnDistance()) && mob.removeWhenFarAway(nearestPlayerDistanceSqr))
/* 378 */       return false; 
/* 380 */     return (mob.checkSpawnRules((LevelAccessor)level, EntitySpawnReason.NATURAL) && mob.checkSpawnObstruction((LevelReader)level));
/*     */   }
/*     */   
/*     */   private static Optional<MobSpawnSettings.SpawnerData> getRandomSpawnMobAt(ServerLevel level, StructureManager structureManager, ChunkGenerator generator, MobCategory mobCategory, RandomSource random, BlockPos pos) {
/* 384 */     Holder<Biome> biome = level.getBiome(pos);
/* 386 */     if (mobCategory == MobCategory.WATER_AMBIENT && biome.is(BiomeTags.REDUCED_WATER_AMBIENT_SPAWNS) && random.nextFloat() < 0.98F)
/* 387 */       return Optional.empty(); 
/* 389 */     return mobsAt(level, structureManager, generator, mobCategory, pos, biome).getRandom(random);
/*     */   }
/*     */   
/*     */   private static boolean canSpawnMobAt(ServerLevel level, StructureManager structureManager, ChunkGenerator generator, MobCategory mobCategory, MobSpawnSettings.SpawnerData spawnerData, BlockPos pos) {
/* 393 */     return mobsAt(level, structureManager, generator, mobCategory, pos, null).contains(spawnerData);
/*     */   }
/*     */   
/*     */   private static WeightedList<MobSpawnSettings.SpawnerData> mobsAt(ServerLevel level, StructureManager structureManager, ChunkGenerator generator, MobCategory mobCategory, BlockPos pos, Holder<Biome> biome) {
/* 398 */     if (isInNetherFortressBounds(pos, level, mobCategory, structureManager))
/* 399 */       return NetherFortressStructure.FORTRESS_ENEMIES; 
/* 401 */     return generator.getMobsAt((biome != null) ? biome : level.getBiome(pos), structureManager, mobCategory, pos);
/*     */   }
/*     */   
/*     */   public static boolean isInNetherFortressBounds(BlockPos pos, ServerLevel level, MobCategory category, StructureManager structureManager) {
/* 406 */     if (category != MobCategory.MONSTER || !level.getBlockState(pos.below()).is(Blocks.NETHER_BRICKS))
/* 407 */       return false; 
/* 409 */     Structure fortress = (Structure)structureManager.registryAccess().lookupOrThrow(Registries.STRUCTURE).getValue(BuiltinStructures.FORTRESS);
/* 410 */     if (fortress == null)
/* 411 */       return false; 
/* 413 */     return structureManager.getStructureAt(pos, fortress).isValid();
/*     */   }
/*     */   
/*     */   private static BlockPos getRandomPosWithin(Level level, LevelChunk chunk) {
/* 417 */     ChunkPos pos = chunk.getPos();
/* 418 */     int x = pos.getMinBlockX() + level.random.nextInt(16);
/* 419 */     int z = pos.getMinBlockZ() + level.random.nextInt(16);
/* 421 */     int topEmptyY = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) + 1;
/* 422 */     int y = Mth.randomBetweenInclusive(level.random, level.getMinY(), topEmptyY);
/* 424 */     return new BlockPos(x, y, z);
/*     */   }
/*     */   
/*     */   public static boolean isValidEmptySpawnBlock(BlockGetter level, BlockPos pos, BlockState blockState, FluidState fluidState, EntityType<?> type) {
/* 429 */     if (blockState.isCollisionShapeFullBlock(level, pos))
/* 430 */       return false; 
/* 433 */     if (blockState.isSignalSource())
/* 434 */       return false; 
/* 437 */     if (!fluidState.isEmpty())
/* 438 */       return false; 
/* 441 */     if (blockState.is(BlockTags.PREVENT_MOB_SPAWNING_INSIDE))
/* 442 */       return false; 
/* 445 */     if (type.isBlockDangerous(blockState))
/* 446 */       return false; 
/* 448 */     return true;
/*     */   }
/*     */   
/*     */   public static void spawnMobsForChunkGeneration(ServerLevelAccessor level, Holder<Biome> biome, ChunkPos chunkPos, RandomSource random) {
/* 452 */     MobSpawnSettings mobSettings = ((Biome)biome.value()).getMobSettings();
/* 453 */     WeightedList<MobSpawnSettings.SpawnerData> mobs = mobSettings.getMobs(MobCategory.CREATURE);
/* 454 */     if (mobs.isEmpty() || !((Boolean)level.getLevel().getGameRules().get(GameRules.SPAWN_MOBS)).booleanValue())
/*     */       return; 
/* 458 */     int xo = chunkPos.getMinBlockX();
/* 459 */     int zo = chunkPos.getMinBlockZ();
/* 462 */     while (random.nextFloat() < mobSettings.getCreatureProbability()) {
/* 463 */       Optional<MobSpawnSettings.SpawnerData> nextSpawnerData = mobs.getRandom(random);
/* 464 */       if (nextSpawnerData.isEmpty())
/*     */         continue; 
/* 467 */       MobSpawnSettings.SpawnerData spawnerData = nextSpawnerData.get();
/* 469 */       int count = spawnerData.minCount() + random.nextInt(1 + spawnerData.maxCount() - spawnerData.minCount());
/* 470 */       SpawnGroupData groupSpawnData = null;
/* 472 */       int x = xo + random.nextInt(16);
/* 473 */       int z = zo + random.nextInt(16);
/* 474 */       int startX = x;
/* 475 */       int startZ = z;
/* 477 */       for (int i = 0; i < count; i++) {
/* 478 */         boolean success = false;
/* 479 */         for (int attempts = 0; !success && attempts < 4; attempts++) {
/* 482 */           BlockPos pos = getTopNonCollidingPos(level, spawnerData.type(), x, z);
/* 483 */           if (spawnerData.type().canSummon() && SpawnPlacements.isSpawnPositionOk(spawnerData.type(), level, pos)) {
/*     */             Entity entity;
/* 484 */             float width = spawnerData.type().getWidth();
/* 485 */             double fx = Mth.clamp(x, xo + width, xo + 16.0D - width);
/* 486 */             double fz = Mth.clamp(z, zo + width, zo + 16.0D - width);
/* 488 */             if (!level.noCollision(spawnerData.type().getSpawnAABB(fx, pos.getY(), fz)))
/*     */               continue; 
/* 492 */             if (!SpawnPlacements.checkSpawnRules(spawnerData.type(), level, EntitySpawnReason.CHUNK_GENERATION, BlockPos.containing(fx, pos.getY(), fz), level.getRandom()))
/*     */               continue; 
/*     */             try {
/* 498 */               entity = spawnerData.type().create((Level)level.getLevel(), EntitySpawnReason.NATURAL);
/* 499 */             } catch (Exception e) {
/* 500 */               LOGGER.warn("Failed to create mob", e);
/*     */               continue;
/*     */             } 
/* 504 */             if (entity == null)
/*     */               continue; 
/* 508 */             entity.snapTo(fx, pos.getY(), fz, random.nextFloat() * 360.0F, 0.0F);
/* 510 */             if (entity instanceof Mob) {
/* 510 */               Mob mob = (Mob)entity;
/* 511 */               if (mob.checkSpawnRules(level, EntitySpawnReason.CHUNK_GENERATION) && mob.checkSpawnObstruction(level)) {
/* 512 */                 groupSpawnData = mob.finalizeSpawn(level, level.getCurrentDifficultyAt(mob.blockPosition()), EntitySpawnReason.CHUNK_GENERATION, groupSpawnData);
/* 513 */                 level.addFreshEntityWithPassengers((Entity)mob);
/* 514 */                 success = true;
/*     */               } 
/*     */             } 
/*     */           } 
/* 519 */           x += random.nextInt(5) - random.nextInt(5);
/* 520 */           z += random.nextInt(5) - random.nextInt(5);
/* 521 */           while (x < xo || x >= xo + 16 || z < zo || z >= zo + 16) {
/* 522 */             x = startX + random.nextInt(5) - random.nextInt(5);
/* 523 */             z = startZ + random.nextInt(5) - random.nextInt(5);
/*     */           } 
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BlockPos getTopNonCollidingPos(LevelReader level, EntityType<?> type, int x, int z) {
/* 531 */     int levelHeight = level.getHeight(SpawnPlacements.getHeightmapType(type), x, z);
/* 532 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(x, levelHeight, z);
/* 534 */     if (level.dimensionType().hasCeiling()) {
/*     */       do {
/* 537 */         pos.move(Direction.DOWN);
/* 538 */       } while (!level.getBlockState((BlockPos)pos).isAir());
/*     */       do {
/* 540 */         pos.move(Direction.DOWN);
/* 541 */       } while (level.getBlockState((BlockPos)pos).isAir() && pos.getY() > level.getMinY());
/*     */     } 
/* 544 */     return SpawnPlacements.getPlacementType(type).adjustSpawnPosition(level, pos.immutable());
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SpawnPredicate {
/*     */     boolean test(EntityType<?> param1EntityType, BlockPos param1BlockPos, ChunkAccess param1ChunkAccess);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface AfterSpawnCallback {
/*     */     void run(Mob param1Mob, ChunkAccess param1ChunkAccess);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\NaturalSpawner.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */