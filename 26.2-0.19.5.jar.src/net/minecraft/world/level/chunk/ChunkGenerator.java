/*     */ package net.minecraft.world.level.chunk;
/*     */ 
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.ints.IntArraySet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.random.WeightedList;
/*     */ import net.minecraft.world.entity.MobCategory;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.NoiseColumn;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.biome.Biome;
/*     */ import net.minecraft.world.level.biome.BiomeGenerationSettings;
/*     */ import net.minecraft.world.level.biome.BiomeManager;
/*     */ import net.minecraft.world.level.biome.BiomeResolver;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.biome.FeatureSorter;
/*     */ import net.minecraft.world.level.biome.MobSpawnSettings;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.GenerationStep;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.LegacyRandomSource;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.RandomSupport;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.feature.FeatureCountTracker;
/*     */ import net.minecraft.world.level.levelgen.placement.PlacedFeature;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.Structure;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureCheckResult;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSet;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
/*     */ import net.minecraft.world.level.levelgen.structure.StructureStart;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.ConcentricRingsStructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ 
/*     */ public abstract class ChunkGenerator {
/*  83 */   public static final Codec<ChunkGenerator> CODEC = BuiltInRegistries.CHUNK_GENERATOR.byNameCodec().dispatchStable(ChunkGenerator::codec, Function.identity());
/*     */   
/*     */   protected final BiomeSource biomeSource;
/*     */   
/*     */   private final Supplier<List<FeatureSorter.StepFeatureData>> featuresPerStep;
/*     */   
/*     */   private final Function<Holder<Biome>, BiomeGenerationSettings> generationSettingsGetter;
/*     */   
/*     */   public ChunkGenerator(BiomeSource biomeSource) {
/*  94 */     this(biomeSource, biome -> ((Biome)biome.value()).getGenerationSettings());
/*     */   }
/*     */   
/*     */   public ChunkGenerator(BiomeSource biomeSource, Function<Holder<Biome>, BiomeGenerationSettings> generationSettingsGetter) {
/*  98 */     this.biomeSource = biomeSource;
/*  99 */     this.generationSettingsGetter = generationSettingsGetter;
/* 101 */     this.featuresPerStep = (Supplier<List<FeatureSorter.StepFeatureData>>)Suppliers.memoize(() -> FeatureSorter.buildFeaturesPerStep(List.copyOf(biomeSource.possibleBiomes()), (), true));
/*     */   }
/*     */   
/*     */   public void validate() {
/* 105 */     this.featuresPerStep.get();
/*     */   }
/*     */   
/*     */   public ChunkGeneratorStructureState createState(HolderLookup<StructureSet> structureSets, RandomState randomState, long legacyLevelSeed) {
/* 111 */     return ChunkGeneratorStructureState.createForNormal(randomState, legacyLevelSeed, this.biomeSource, structureSets);
/*     */   }
/*     */   
/*     */   public Optional<Identifier> getTypeNameForDataFixer() {
/* 115 */     return BuiltInRegistries.CHUNK_GENERATOR.getResourceKey(codec()).map(ResourceKey::identifier);
/*     */   }
/*     */   
/*     */   public CompletableFuture<ChunkAccess> createBiomes(RandomState randomState, Blender blender, StructureManager structureManager, ChunkAccess protoChunk) {
/* 120 */     return CompletableFuture.supplyAsync(() -> {
/*     */           protoChunk.fillBiomesFromNoise((BiomeResolver)this.biomeSource, randomState.sampler());
/*     */           return protoChunk;
/* 123 */         }Util.backgroundExecutor().forName("init_biomes"));
/*     */   }
/*     */   
/*     */   public Pair<BlockPos, Holder<Structure>> findNearestMapStructure(ServerLevel level, HolderSet<Structure> wantedStructures, BlockPos pos, int maxSearchRadius, boolean createReference) {
/* 129 */     if (SharedConstants.DEBUG_DISABLE_FEATURES)
/* 130 */       return null; 
/* 135 */     if (!level.getServer().getWorldGenSettings().options().generateStructures())
/* 136 */       return null; 
/* 139 */     ChunkGeneratorStructureState generatorState = level.getChunkSource().getGeneratorState();
/* 140 */     Object2ObjectArrayMap<StructurePlacement, Set<Holder<Structure>>> object2ObjectArrayMap = new Object2ObjectArrayMap();
/* 141 */     for (Holder<Structure> structure : wantedStructures) {
/* 142 */       for (StructurePlacement placement : generatorState.getPlacementsForStructure(structure))
/* 143 */         ((Set<Holder<Structure>>)object2ObjectArrayMap.computeIfAbsent(placement, p -> new ObjectArraySet())).add(structure); 
/*     */     } 
/* 147 */     if (object2ObjectArrayMap.isEmpty())
/* 148 */       return null; 
/* 151 */     Pair<BlockPos, Holder<Structure>> nearest = null;
/* 152 */     double distanceSqr = Double.MAX_VALUE;
/* 153 */     StructureManager structureManager = level.structureManager();
/* 154 */     List<Map.Entry<StructurePlacement, Set<Holder<Structure>>>> randomSpreadEntries = new ArrayList<>(object2ObjectArrayMap.size());
/* 155 */     for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : object2ObjectArrayMap.entrySet()) {
/* 156 */       StructurePlacement placement = entry.getKey();
/* 157 */       if (placement instanceof ConcentricRingsStructurePlacement) {
/* 157 */         ConcentricRingsStructurePlacement rings = (ConcentricRingsStructurePlacement)placement;
/* 158 */         Pair<BlockPos, Holder<Structure>> generating = getNearestGeneratedStructure(entry.getValue(), level, structureManager, pos, createReference, rings);
/* 159 */         if (generating != null) {
/* 160 */           BlockPos structurePos = (BlockPos)generating.getFirst();
/* 161 */           double newDistanceSqr = pos.distSqr((Vec3i)structurePos);
/* 162 */           if (newDistanceSqr < distanceSqr) {
/* 163 */             distanceSqr = newDistanceSqr;
/* 164 */             nearest = generating;
/*     */           } 
/*     */         } 
/*     */         continue;
/*     */       } 
/* 167 */       if (placement instanceof RandomSpreadStructurePlacement)
/* 168 */         randomSpreadEntries.add(entry); 
/*     */     } 
/* 172 */     if (!randomSpreadEntries.isEmpty()) {
/* 173 */       int chunkOriginX = SectionPos.blockToSectionCoord(pos.getX());
/* 174 */       int chunkOriginZ = SectionPos.blockToSectionCoord(pos.getZ());
/* 177 */       for (int radius = 0; radius <= maxSearchRadius; radius++) {
/* 178 */         boolean foundSomething = false;
/* 179 */         for (Map.Entry<StructurePlacement, Set<Holder<Structure>>> entry : randomSpreadEntries) {
/* 180 */           RandomSpreadStructurePlacement randomPlacement = (RandomSpreadStructurePlacement)entry.getKey();
/* 181 */           Pair<BlockPos, Holder<Structure>> structurePos = getNearestGeneratedStructure(entry.getValue(), (LevelReader)level, structureManager, chunkOriginX, chunkOriginZ, radius, createReference, generatorState.getLevelSeed(), randomPlacement);
/* 182 */           if (structurePos != null) {
/* 183 */             foundSomething = true;
/* 184 */             double newDistanceSqr = pos.distSqr((Vec3i)structurePos.getFirst());
/* 185 */             if (newDistanceSqr < distanceSqr) {
/* 186 */               distanceSqr = newDistanceSqr;
/* 187 */               nearest = structurePos;
/*     */             } 
/*     */           } 
/*     */         } 
/* 192 */         if (foundSomething)
/* 193 */           return nearest; 
/*     */       } 
/*     */     } 
/* 197 */     return nearest;
/*     */   }
/*     */   
/*     */   private Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> structures, ServerLevel level, StructureManager structureManager, BlockPos pos, boolean createReference, ConcentricRingsStructurePlacement rings) {
/* 201 */     List<ChunkPos> positions = level.getChunkSource().getGeneratorState().getRingPositionsFor(rings);
/* 202 */     if (positions == null)
/* 203 */       throw new IllegalStateException("Somehow tried to find structures for a placement that doesn't exist"); 
/* 205 */     Pair<BlockPos, Holder<Structure>> closestPos = null;
/* 206 */     double closest = Double.MAX_VALUE;
/* 207 */     BlockPos.MutableBlockPos structurePos = new BlockPos.MutableBlockPos();
/* 208 */     for (ChunkPos chunkPos : positions) {
/* 209 */       structurePos.set(SectionPos.sectionToBlockCoord(chunkPos.x(), 8), 32, SectionPos.sectionToBlockCoord(chunkPos.z(), 8));
/* 210 */       double distSqr = structurePos.distSqr((Vec3i)pos);
/* 211 */       boolean isClosest = (closestPos == null || distSqr < closest);
/* 212 */       if (isClosest) {
/* 213 */         Pair<BlockPos, Holder<Structure>> generating = getStructureGeneratingAt(structures, (LevelReader)level, structureManager, createReference, (StructurePlacement)rings, chunkPos);
/* 214 */         if (generating != null) {
/* 215 */           closestPos = generating;
/* 216 */           closest = distSqr;
/*     */         } 
/*     */       } 
/*     */     } 
/* 220 */     return closestPos;
/*     */   }
/*     */   
/*     */   private static Pair<BlockPos, Holder<Structure>> getNearestGeneratedStructure(Set<Holder<Structure>> structures, LevelReader level, StructureManager structureManager, int chunkOriginX, int chunkOriginZ, int radius, boolean createReference, long seed, RandomSpreadStructurePlacement config) {
/* 224 */     int spacing = config.spacing();
/* 226 */     for (int x = -radius; x <= radius; x++) {
/* 227 */       boolean xEdge = (x == -radius || x == radius);
/* 228 */       for (int z = -radius; z <= radius; z++) {
/* 229 */         boolean zEdge = (z == -radius || z == radius);
/* 230 */         if (xEdge || zEdge) {
/* 234 */           int sectorX = chunkOriginX + spacing * x;
/* 235 */           int sectorZ = chunkOriginZ + spacing * z;
/* 237 */           ChunkPos chunkTarget = config.getPotentialStructureChunk(seed, sectorX, sectorZ);
/* 239 */           Pair<BlockPos, Holder<Structure>> generating = getStructureGeneratingAt(structures, level, structureManager, createReference, (StructurePlacement)config, chunkTarget);
/* 240 */           if (generating != null)
/* 241 */             return generating; 
/*     */         } 
/*     */       } 
/*     */     } 
/* 246 */     return null;
/*     */   }
/*     */   
/*     */   private static Pair<BlockPos, Holder<Structure>> getStructureGeneratingAt(Set<Holder<Structure>> structures, LevelReader level, StructureManager structureManager, boolean createReference, StructurePlacement config, ChunkPos chunkTarget) {
/* 250 */     for (Holder<Structure> structure : structures) {
/* 251 */       StructureCheckResult fastCheckResult = structureManager.checkStructurePresence(chunkTarget, (Structure)structure.value(), config, createReference);
/* 253 */       if (fastCheckResult == StructureCheckResult.START_NOT_PRESENT)
/*     */         continue; 
/* 255 */       if (!createReference && fastCheckResult == StructureCheckResult.START_PRESENT)
/* 256 */         return Pair.of(config.getLocatePos(chunkTarget), structure); 
/* 259 */       ChunkAccess chunk = level.getChunk(chunkTarget.x(), chunkTarget.z(), ChunkStatus.STRUCTURE_STARTS);
/* 260 */       StructureStart start = structureManager.getStartForStructure(SectionPos.bottomOf(chunk), (Structure)structure.value(), chunk);
/* 261 */       if (start != null && start.isValid() && (
/* 262 */         !createReference || tryAddReference(structureManager, start)))
/* 263 */         return Pair.of(config.getLocatePos(start.getChunkPos()), structure); 
/*     */     } 
/* 267 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean tryAddReference(StructureManager manager, StructureStart start) {
/* 271 */     if (start.canBeReferenced()) {
/* 272 */       manager.addReference(start);
/* 273 */       return true;
/*     */     } 
/* 275 */     return false;
/*     */   }
/*     */   
/*     */   public void applyBiomeDecoration(WorldGenLevel level, ChunkAccess chunk, StructureManager structureManager) {
/* 279 */     ChunkPos centerPos = chunk.getPos();
/* 281 */     if (SharedConstants.debugVoidTerrain(centerPos))
/*     */       return; 
/* 285 */     SectionPos sectionPos = SectionPos.of(centerPos, level.getMinSectionY());
/* 286 */     BlockPos origin = sectionPos.origin();
/* 288 */     Registry<Structure> structuresRegistry = level.registryAccess().lookupOrThrow(Registries.STRUCTURE);
/* 289 */     Map<Integer, List<Structure>> structuresByStep = (Map<Integer, List<Structure>>)structuresRegistry.stream().collect(Collectors.groupingBy(structure -> Integer.valueOf(structure.step().ordinal())));
/* 291 */     List<FeatureSorter.StepFeatureData> featureList = this.featuresPerStep.get();
/* 293 */     WorldgenRandom random = new WorldgenRandom((RandomSource)new XoroshiroRandomSource(RandomSupport.generateUniqueSeed()));
/* 294 */     long decorationSeed = random.setDecorationSeed(level.getSeed(), origin.getX(), origin.getZ());
/* 296 */     ObjectArraySet objectArraySet = new ObjectArraySet();
/* 297 */     ChunkPos.rangeClosed(sectionPos.chunk(), 1).forEach(chunkPos -> {
/*     */           ChunkAccess chunkInRange = level.getChunk(chunkPos.x(), chunkPos.z());
/*     */           for (LevelChunkSection section : chunkInRange.getSections()) {
/*     */             Objects.requireNonNull(possibleBiomes);
/*     */             section.getBiomes().getAll(possibleBiomes::add);
/*     */           } 
/*     */         });
/* 304 */     objectArraySet.retainAll(this.biomeSource.possibleBiomes());
/* 305 */     int featureStepCount = featureList.size();
/*     */     try {
/* 308 */       Registry<PlacedFeature> featureRegistry = level.registryAccess().lookupOrThrow(Registries.PLACED_FEATURE);
/* 310 */       int generationSteps = Math.max((GenerationStep.Decoration.values()).length, featureStepCount);
/* 311 */       for (int stepIndex = 0; stepIndex < generationSteps; stepIndex++) {
/* 312 */         int index = 0;
/* 313 */         if (structureManager.shouldGenerateStructures()) {
/* 314 */           List<Structure> structures = structuresByStep.getOrDefault(Integer.valueOf(stepIndex), Collections.emptyList());
/* 315 */           for (Structure structure : structures) {
/* 316 */             random.setFeatureSeed(decorationSeed, index, stepIndex);
/* 318 */             Supplier<String> currentlyGenerating = () -> {
/*     */                 Objects.requireNonNull(structure);
/*     */                 return structuresRegistry.getResourceKey(structure).map(Object::toString).orElseGet(structure::toString);
/*     */               };
/*     */             try {
/* 320 */               level.setCurrentlyGenerating(currentlyGenerating);
/* 322 */               structureManager.startsForStructure(sectionPos, structure).forEach(start -> start.placeInChunk(level, structureManager, this, (RandomSource)random, getWritableArea(chunk), centerPos));
/* 325 */             } catch (Exception e) {
/* 326 */               CrashReport report = CrashReport.forThrowable(e, "Feature placement");
/* 328 */               Objects.requireNonNull(currentlyGenerating);
/* 328 */               report.addCategory("Feature").setDetail("Description", currentlyGenerating::get);
/* 329 */               throw new ReportedException(report);
/*     */             } 
/* 331 */             index++;
/*     */           } 
/*     */         } 
/* 334 */         if (stepIndex < featureStepCount) {
/* 335 */           IntArraySet intArraySet = new IntArraySet();
/* 336 */           for (Holder<Biome> biome : (Iterable<Holder<Biome>>)objectArraySet) {
/* 338 */             List<HolderSet<PlacedFeature>> featuresInBiome = ((BiomeGenerationSettings)this.generationSettingsGetter.apply(biome)).features();
/* 339 */             if (stepIndex >= featuresInBiome.size())
/*     */               continue; 
/* 342 */             HolderSet<PlacedFeature> featuresInBiomeThisStep = featuresInBiome.get(stepIndex);
/* 343 */             FeatureSorter.StepFeatureData stepFeatureData1 = featureList.get(stepIndex);
/* 344 */             featuresInBiomeThisStep.stream().map(Holder::value).forEach(feature -> possibleFeaturesThisStep.add(stepFeatureData.indexMapping().applyAsInt(feature)));
/*     */           } 
/* 347 */           int numberOfFeaturesInStep = intArraySet.size();
/* 348 */           int[] indexArray = intArraySet.toIntArray();
/* 349 */           Arrays.sort(indexArray);
/* 351 */           FeatureSorter.StepFeatureData stepFeatureData = featureList.get(stepIndex);
/* 352 */           for (int featureIndex = 0; featureIndex < numberOfFeaturesInStep; featureIndex++) {
/* 353 */             int globalIndexOfFeature = indexArray[featureIndex];
/* 354 */             PlacedFeature feature = stepFeatureData.features().get(globalIndexOfFeature);
/* 356 */             Supplier<String> currentlyGenerating = () -> {
/*     */                 Objects.requireNonNull(feature);
/*     */                 return featureRegistry.getResourceKey(feature).map(Object::toString).orElseGet(feature::toString);
/*     */               };
/* 357 */             random.setFeatureSeed(decorationSeed, globalIndexOfFeature, stepIndex);
/*     */             try {
/* 359 */               level.setCurrentlyGenerating(currentlyGenerating);
/* 360 */               feature.placeWithBiomeCheck(level, this, (RandomSource)random, origin);
/* 361 */             } catch (Exception e) {
/* 362 */               CrashReport report = CrashReport.forThrowable(e, "Feature placement");
/* 364 */               Objects.requireNonNull(currentlyGenerating);
/* 364 */               report.addCategory("Feature").setDetail("Description", currentlyGenerating::get);
/* 365 */               throw new ReportedException(report);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 370 */       level.setCurrentlyGenerating(null);
/* 371 */       if (SharedConstants.DEBUG_FEATURE_COUNT)
/* 372 */         FeatureCountTracker.chunkDecorated(level.getLevel()); 
/* 374 */     } catch (Exception e) {
/* 375 */       CrashReport report = CrashReport.forThrowable(e, "Biome decoration");
/* 376 */       report.addCategory("Generation")
/* 377 */         .setDetail("CenterX", Integer.valueOf(centerPos.x()))
/* 378 */         .setDetail("CenterZ", Integer.valueOf(centerPos.z()))
/* 379 */         .setDetail("Decoration Seed", Long.valueOf(decorationSeed));
/* 380 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static BoundingBox getWritableArea(ChunkAccess chunk) {
/* 385 */     ChunkPos chunkPos = chunk.getPos();
/* 386 */     int targetBlockX = chunkPos.getMinBlockX();
/* 387 */     int targetBlockZ = chunkPos.getMinBlockZ();
/* 389 */     LevelHeightAccessor heightAccessor = chunk.getHeightAccessorForGeneration();
/* 390 */     int minY = heightAccessor.getMinY() + 1;
/* 391 */     int maxY = heightAccessor.getMaxY();
/* 393 */     return new BoundingBox(targetBlockX, minY, targetBlockZ, targetBlockX + 15, maxY, targetBlockZ + 15);
/*     */   }
/*     */   
/*     */   public int getSpawnHeight(LevelHeightAccessor heightAccessor) {
/* 402 */     return 64;
/*     */   }
/*     */   
/*     */   public BiomeSource getBiomeSource() {
/* 406 */     return this.biomeSource;
/*     */   }
/*     */   
/*     */   public WeightedList<MobSpawnSettings.SpawnerData> getMobsAt(Holder<Biome> biome, StructureManager structureManager, MobCategory mobCategory, BlockPos pos) {
/* 412 */     Map<Structure, LongSet> structures = structureManager.getAllStructuresAt(pos);
/* 414 */     for (Map.Entry<Structure, LongSet> entry : structures.entrySet()) {
/* 415 */       Structure structure = entry.getKey();
/* 416 */       StructureSpawnOverride override = (StructureSpawnOverride)structure.spawnOverrides().get(mobCategory);
/* 417 */       if (override == null)
/*     */         continue; 
/* 421 */       MutableBoolean inOverrideBox = new MutableBoolean(false);
/* 424 */       Predicate<StructureStart> check = (override.boundingBox() == StructureSpawnOverride.BoundingBoxType.PIECE) ? (start -> structureManager.structureHasPieceAt(pos, start)) : (start -> start.getBoundingBox().isInside((Vec3i)pos));
/* 426 */       structureManager.fillStartsForStructure(structure, entry.getValue(), start -> {
/*     */             if (inOverrideBox.isFalse() && check.test(start))
/*     */               inOverrideBox.setTrue(); 
/*     */           });
/* 431 */       if (inOverrideBox.isTrue())
/* 432 */         return override.spawns(); 
/*     */     } 
/* 436 */     return ((Biome)biome.value()).getMobSettings().getMobs(mobCategory);
/*     */   }
/*     */   
/*     */   public void createStructures(RegistryAccess registryAccess, ChunkGeneratorStructureState state, StructureManager structureManager, ChunkAccess centerChunk, StructureTemplateManager structureTemplateManager, ResourceKey<Level> level) {
/* 440 */     if (SharedConstants.DEBUG_DISABLE_STRUCTURES)
/*     */       return; 
/* 444 */     ChunkPos sourceChunkPos = centerChunk.getPos();
/* 445 */     SectionPos sectionPos = SectionPos.bottomOf(centerChunk);
/* 447 */     RandomState randomState = state.randomState();
/* 449 */     state.possibleStructureSets().forEach(set -> {
/*     */           StructurePlacement featurePlacement = ((StructureSet)set.value()).placement();
/*     */           List<StructureSet.StructureSelectionEntry> structures = ((StructureSet)set.value()).structures();
/*     */           for (StructureSet.StructureSelectionEntry structure : structures) {
/*     */             StructureStart existingStart = structureManager.getStartForStructure(sectionPos, (Structure)structure.structure().value(), centerChunk);
/*     */             if (existingStart != null && existingStart.isValid())
/*     */               return; 
/*     */           } 
/*     */           if (!featurePlacement.isStructureChunk(state, sourceChunkPos.x(), sourceChunkPos.z()))
/*     */             return; 
/*     */           if (structures.size() == 1) {
/*     */             tryGenerateStructure(structures.get(0), structureManager, registryAccess, randomState, structureTemplateManager, state.getLevelSeed(), centerChunk, sourceChunkPos, sectionPos, level);
/*     */             return;
/*     */           } 
/*     */           ArrayList<StructureSet.StructureSelectionEntry> options = new ArrayList<>(structures.size());
/*     */           options.addAll(structures);
/*     */           WorldgenRandom random = new WorldgenRandom((RandomSource)new LegacyRandomSource(0L));
/*     */           random.setLargeFeatureSeed(state.getLevelSeed(), sourceChunkPos.x(), sourceChunkPos.z());
/*     */           int total = 0;
/*     */           for (StructureSet.StructureSelectionEntry option : options)
/*     */             total += option.weight(); 
/*     */           while (!options.isEmpty()) {
/*     */             int choice = random.nextInt(total);
/*     */             int index = 0;
/*     */             for (StructureSet.StructureSelectionEntry option : options) {
/*     */               choice -= option.weight();
/*     */               if (choice < 0)
/*     */                 break; 
/*     */               index++;
/*     */             } 
/*     */             StructureSet.StructureSelectionEntry selected = options.get(index);
/*     */             if (tryGenerateStructure(selected, structureManager, registryAccess, randomState, structureTemplateManager, state.getLevelSeed(), centerChunk, sourceChunkPos, sectionPos, level))
/*     */               return; 
/*     */             options.remove(index);
/*     */             total -= selected.weight();
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private boolean tryGenerateStructure(StructureSet.StructureSelectionEntry selected, StructureManager structureManager, RegistryAccess registryAccess, RandomState randomState, StructureTemplateManager structureTemplateManager, long seed, ChunkAccess centerChunk, ChunkPos sourceChunkPos, SectionPos sectionPos, ResourceKey<Level> level) {
/* 508 */     Structure structure = (Structure)selected.structure().value();
/* 509 */     int references = fetchReferences(structureManager, centerChunk, sectionPos, structure);
/* 512 */     HolderSet<Biome> biomeAllowedForStructure = structure.biomes();
/* 513 */     Objects.requireNonNull(biomeAllowedForStructure);
/* 513 */     Predicate<Holder<Biome>> biomePredicate = biomeAllowedForStructure::contains;
/* 514 */     StructureStart start = structure.generate(selected.structure(), level, registryAccess, this, this.biomeSource, randomState, structureTemplateManager, seed, sourceChunkPos, references, (LevelHeightAccessor)centerChunk, biomePredicate);
/* 515 */     if (start.isValid()) {
/* 516 */       structureManager.setStartForStructure(sectionPos, structure, start, centerChunk);
/* 517 */       return true;
/*     */     } 
/* 519 */     return false;
/*     */   }
/*     */   
/*     */   private static int fetchReferences(StructureManager structureManager, ChunkAccess centerChunk, SectionPos sectionPos, Structure structure) {
/* 524 */     StructureStart prevEntry = structureManager.getStartForStructure(sectionPos, structure, centerChunk);
/* 525 */     return (prevEntry != null) ? prevEntry.getReferences() : 0;
/*     */   }
/*     */   
/*     */   public void createReferences(WorldGenLevel level, StructureManager structureManager, ChunkAccess centerChunk) {
/* 529 */     int range = 8;
/* 530 */     ChunkPos chunkPos = centerChunk.getPos();
/* 531 */     int targetX = chunkPos.x();
/* 532 */     int targetZ = chunkPos.z();
/* 533 */     int targetBlockX = chunkPos.getMinBlockX();
/* 534 */     int targetBlockZ = chunkPos.getMinBlockZ();
/* 536 */     SectionPos pos = SectionPos.bottomOf(centerChunk);
/* 538 */     for (int sourceX = targetX - 8; sourceX <= targetX + 8; sourceX++) {
/* 539 */       for (int sourceZ = targetZ - 8; sourceZ <= targetZ + 8; sourceZ++) {
/* 540 */         long sourceChunkKey = ChunkPos.pack(sourceX, sourceZ);
/* 542 */         for (StructureStart start : level.getChunk(sourceX, sourceZ).getAllStarts().values()) {
/*     */           try {
/* 544 */             if (start.isValid() && start.getBoundingBox().intersects(targetBlockX, targetBlockZ, targetBlockX + 15, targetBlockZ + 15))
/* 545 */               structureManager.addReferenceForStructure(pos, start.getStructure(), sourceChunkKey, centerChunk); 
/* 547 */           } catch (Exception e) {
/* 548 */             CrashReport report = CrashReport.forThrowable(e, "Generating structure reference");
/* 549 */             CrashReportCategory structure = report.addCategory("Structure");
/* 550 */             Optional<? extends Registry<Structure>> configuredStructuresRegistry = level.registryAccess().lookup(Registries.STRUCTURE);
/* 551 */             structure.setDetail("Id", () -> (String)configuredStructuresRegistry.map(()).orElse("UNKNOWN"));
/* 552 */             structure.setDetail("Name", () -> BuiltInRegistries.STRUCTURE_TYPE.getKey(start.getStructure().type()).toString());
/* 553 */             structure.setDetail("Class", () -> start.getStructure().getClass().getCanonicalName());
/* 554 */             throw new ReportedException(report);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getFirstFreeHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) {
/* 573 */     return getBaseHeight(x, z, type, heightAccessor, randomState);
/*     */   }
/*     */   
/*     */   public int getFirstOccupiedHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor heightAccessor, RandomState randomState) {
/* 577 */     return getBaseHeight(x, z, type, heightAccessor, randomState) - 1;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public BiomeGenerationSettings getBiomeGenerationSettings(Holder<Biome> biome) {
/* 587 */     return this.generationSettingsGetter.apply(biome);
/*     */   }
/*     */   
/*     */   protected abstract MapCodec<? extends ChunkGenerator> codec();
/*     */   
/*     */   public abstract void applyCarvers(WorldGenRegion paramWorldGenRegion, long paramLong, RandomState paramRandomState, BiomeManager paramBiomeManager, StructureManager paramStructureManager, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract void buildSurface(WorldGenRegion paramWorldGenRegion, StructureManager paramStructureManager, RandomState paramRandomState, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract void spawnOriginalMobs(WorldGenRegion paramWorldGenRegion);
/*     */   
/*     */   public abstract int getGenDepth();
/*     */   
/*     */   public abstract CompletableFuture<ChunkAccess> fillFromNoise(Blender paramBlender, RandomState paramRandomState, StructureManager paramStructureManager, ChunkAccess paramChunkAccess);
/*     */   
/*     */   public abstract int getSeaLevel();
/*     */   
/*     */   public abstract int getMinY();
/*     */   
/*     */   public abstract int getBaseHeight(int paramInt1, int paramInt2, Heightmap.Types paramTypes, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState);
/*     */   
/*     */   public abstract NoiseColumn getBaseColumn(int paramInt1, int paramInt2, LevelHeightAccessor paramLevelHeightAccessor, RandomState paramRandomState);
/*     */   
/*     */   public abstract void addDebugScreenInfo(List<String> paramList, RandomState paramRandomState, BlockPos paramBlockPos);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\ChunkGenerator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */