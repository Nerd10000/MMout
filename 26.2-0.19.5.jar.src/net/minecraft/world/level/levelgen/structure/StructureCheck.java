/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2BooleanMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2BooleanOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.IntTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.StreamTagVisitor;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.visitors.CollectFields;
/*     */ import net.minecraft.nbt.visitors.FieldSelector;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelHeightAccessor;
/*     */ import net.minecraft.world.level.biome.BiomeSource;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.chunk.storage.ChunkScanAccess;
/*     */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*     */ import net.minecraft.world.level.levelgen.RandomState;
/*     */ import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
/*     */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class StructureCheck {
/*  44 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int NO_STRUCTURE = -1;
/*     */   
/*     */   private final ChunkScanAccess storageAccess;
/*     */   
/*     */   private final RegistryAccess registryAccess;
/*     */   
/*     */   private final StructureTemplateManager structureTemplateManager;
/*     */   
/*     */   private final ResourceKey<Level> dimension;
/*     */   
/*     */   private final ChunkGenerator chunkGenerator;
/*     */   
/*     */   private final RandomState randomState;
/*     */   
/*     */   private final LevelHeightAccessor heightAccessor;
/*     */   
/*     */   private final BiomeSource biomeSource;
/*     */   
/*     */   private final long seed;
/*     */   
/*     */   private final DataFixer fixerUpper;
/*     */   
/*  59 */   private final Long2ObjectMap<Object2IntMap<Structure>> loadedChunks = (Long2ObjectMap<Object2IntMap<Structure>>)new Long2ObjectOpenHashMap();
/*     */   
/*  60 */   private final Map<Structure, Long2BooleanMap> featureChecks = new HashMap<>();
/*     */   
/*     */   public StructureCheck(ChunkScanAccess storageAccess, RegistryAccess registryAccess, StructureTemplateManager structureTemplateManager, ResourceKey<Level> dimension, ChunkGenerator chunkGenerator, RandomState randomState, LevelHeightAccessor heightAccessor, BiomeSource biomeSource, long seed, DataFixer fixerUpper) {
/*  63 */     this.storageAccess = storageAccess;
/*  64 */     this.registryAccess = registryAccess;
/*  65 */     this.structureTemplateManager = structureTemplateManager;
/*  66 */     this.dimension = dimension;
/*  67 */     this.chunkGenerator = chunkGenerator;
/*  68 */     this.randomState = randomState;
/*  69 */     this.heightAccessor = heightAccessor;
/*  70 */     this.biomeSource = biomeSource;
/*  71 */     this.seed = seed;
/*  72 */     this.fixerUpper = fixerUpper;
/*     */   }
/*     */   
/*     */   public StructureCheckResult checkStart(ChunkPos pos, Structure structure, StructurePlacement placement, boolean requireUnreferenced) {
/*  76 */     long posKey = pos.pack();
/*  77 */     Object2IntMap<Structure> cachedResult = (Object2IntMap<Structure>)this.loadedChunks.get(posKey);
/*  78 */     if (cachedResult != null)
/*  79 */       return checkStructureInfo(cachedResult, structure, requireUnreferenced); 
/*  83 */     StructureCheckResult storageCheckResult = tryLoadFromStorage(pos, structure, requireUnreferenced, posKey);
/*  84 */     if (storageCheckResult != null)
/*  86 */       return storageCheckResult; 
/*  94 */     if (!placement.applyAdditionalChunkRestrictions(pos.x(), pos.z(), this.seed))
/*  95 */       return StructureCheckResult.START_NOT_PRESENT; 
/*  99 */     boolean isFeatureChunk = ((Long2BooleanMap)this.featureChecks.computeIfAbsent(structure, k -> new Long2BooleanOpenHashMap())).computeIfAbsent(posKey, k -> canCreateStructure(pos, structure));
/* 103 */     if (!isFeatureChunk)
/* 105 */       return StructureCheckResult.START_NOT_PRESENT; 
/* 109 */     return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */   }
/*     */   
/*     */   private boolean canCreateStructure(ChunkPos pos, Structure structure) {
/* 113 */     Objects.requireNonNull(structure.biomes());
/* 113 */     return structure.findValidGenerationPoint(new Structure.GenerationContext(this.registryAccess, this.chunkGenerator, this.biomeSource, this.randomState, this.structureTemplateManager, this.seed, pos, this.heightAccessor, structure.biomes()::contains)).isPresent();
/*     */   }
/*     */   
/*     */   private StructureCheckResult tryLoadFromStorage(ChunkPos pos, Structure structure, boolean requireUnreferenced, long posKey) {
/*     */     CompoundTag chunkTag, fixedChunkTag;
/* 118 */     CollectFields collectFields = new CollectFields(new FieldSelector[] { new FieldSelector(IntTag.TYPE, "DataVersion"), new FieldSelector("Level", "Structures", CompoundTag.TYPE, "Starts"), new FieldSelector("structures", CompoundTag.TYPE, "starts") });
/*     */     try {
/* 125 */       this.storageAccess.scanChunk(pos, (StreamTagVisitor)collectFields).join();
/* 126 */     } catch (Exception e) {
/* 127 */       LOGGER.warn("Failed to read chunk {}", pos, e);
/* 128 */       return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */     } 
/* 130 */     Tag result = collectFields.getResult();
/* 131 */     if (result instanceof CompoundTag) {
/* 131 */       chunkTag = (CompoundTag)result;
/*     */     } else {
/* 133 */       return null;
/*     */     } 
/* 136 */     int version = NbtUtils.getDataVersion(chunkTag);
/* 138 */     SimpleRegionStorage.injectDatafixingContext(chunkTag, ChunkMap.getChunkDataFixContextTag(this.dimension, this.chunkGenerator.getTypeNameForDataFixer()));
/*     */     try {
/* 142 */       fixedChunkTag = DataFixTypes.CHUNK.updateToCurrentVersion(this.fixerUpper, chunkTag, version);
/* 143 */     } catch (Exception e) {
/* 144 */       LOGGER.warn("Failed to partially datafix chunk {}", pos, e);
/* 146 */       return StructureCheckResult.CHUNK_LOAD_NEEDED;
/*     */     } 
/* 149 */     Object2IntMap<Structure> knownStarts = loadStructures(fixedChunkTag);
/* 150 */     if (knownStarts == null)
/* 152 */       return null; 
/* 155 */     storeFullResults(posKey, knownStarts);
/* 156 */     return checkStructureInfo(knownStarts, structure, requireUnreferenced);
/*     */   }
/*     */   
/*     */   private Object2IntMap<Structure> loadStructures(CompoundTag chunkTag) {
/* 161 */     Optional<CompoundTag> maybeStartsTag = chunkTag.getCompound("structures").flatMap(tag -> tag.getCompound("starts"));
/* 162 */     if (maybeStartsTag.isEmpty())
/* 163 */       return null; 
/* 166 */     CompoundTag startsTag = maybeStartsTag.get();
/* 167 */     if (startsTag.isEmpty())
/* 168 */       return Object2IntMaps.emptyMap(); 
/* 171 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 172 */     Registry<Structure> structuresRegistry = this.registryAccess.lookupOrThrow(Registries.STRUCTURE);
/* 173 */     startsTag.forEach((key, tag) -> {
/*     */           Identifier id = Identifier.tryParse(key);
/*     */           if (id == null)
/*     */             return; 
/*     */           Structure foundFeature = (Structure)structuresRegistry.getValue(id);
/*     */           if (foundFeature == null)
/*     */             return; 
/*     */           tag.asCompound().ifPresent(());
/*     */         });
/* 191 */     return (Object2IntMap<Structure>)object2IntOpenHashMap;
/*     */   }
/*     */   
/*     */   private static Object2IntMap<Structure> deduplicateEmptyMap(Object2IntMap<Structure> map) {
/* 195 */     return map.isEmpty() ? Object2IntMaps.emptyMap() : map;
/*     */   }
/*     */   
/*     */   private StructureCheckResult checkStructureInfo(Object2IntMap<Structure> cachedResult, Structure structure, boolean requireUnreferenced) {
/* 199 */     int referenceCount = cachedResult.getOrDefault(structure, -1);
/* 201 */     return (referenceCount != -1 && (!requireUnreferenced || referenceCount == 0)) ? StructureCheckResult.START_PRESENT : StructureCheckResult.START_NOT_PRESENT;
/*     */   }
/*     */   
/*     */   public void onStructureLoad(ChunkPos pos, Map<Structure, StructureStart> starts) {
/* 205 */     long posKey = pos.pack();
/* 207 */     Object2IntOpenHashMap object2IntOpenHashMap = new Object2IntOpenHashMap();
/* 208 */     starts.forEach((structure, structureStart) -> {
/*     */           if (structureStart.isValid())
/*     */             startsToReferences.put(structure, structureStart.getReferences()); 
/*     */         });
/* 213 */     storeFullResults(posKey, (Object2IntMap<Structure>)object2IntOpenHashMap);
/*     */   }
/*     */   
/*     */   private void storeFullResults(long posKey, Object2IntMap<Structure> starts) {
/* 217 */     this.loadedChunks.put(posKey, deduplicateEmptyMap(starts));
/* 220 */     this.featureChecks.values().forEach(m -> m.remove(posKey));
/*     */   }
/*     */   
/*     */   public void incrementReference(ChunkPos chunkPos, Structure structure) {
/* 224 */     this.loadedChunks.compute(chunkPos.pack(), (key, counts) -> {
/*     */           Object2IntOpenHashMap object2IntOpenHashMap;
/*     */           if (counts == null || counts.isEmpty())
/*     */             object2IntOpenHashMap = new Object2IntOpenHashMap(); 
/*     */           object2IntOpenHashMap.computeInt(structure, ());
/*     */           return (Object2IntMap)object2IntOpenHashMap;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\levelgen\structure\StructureCheck.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */