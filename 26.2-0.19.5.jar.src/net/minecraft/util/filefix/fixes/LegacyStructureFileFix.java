/*     */ package net.minecraft.util.filefix.fixes;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.OptionalDynamic;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ChunkMap;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.datafix.fixes.References;
/*     */ import net.minecraft.util.filefix.CanceledFileFixException;
/*     */ import net.minecraft.util.filefix.FileFix;
/*     */ import net.minecraft.util.filefix.access.ChunkNbt;
/*     */ import net.minecraft.util.filefix.access.CompressedNbt;
/*     */ import net.minecraft.util.filefix.access.FileAccess;
/*     */ import net.minecraft.util.filefix.access.FileAccessProvider;
/*     */ import net.minecraft.util.filefix.access.FileRelation;
/*     */ import net.minecraft.util.filefix.access.FileResourceTypes;
/*     */ import net.minecraft.util.filefix.access.LevelDat;
/*     */ import net.minecraft.util.filefix.access.SavedDataNbt;
/*     */ import net.minecraft.util.filefix.operations.ModifyContent;
/*     */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ 
/*     */ public class LegacyStructureFileFix extends FileFix {
/*     */   public static final int STRUCTURE_RANGE = 8;
/*     */   
/*  52 */   public static final List<String> OVERWORLD_LEGACY_STRUCTURES = List.of("Monument", "Stronghold", "Mineshaft", "Temple", "Mansion");
/*     */   
/*     */   public static final Map<String, String> LEGACY_TO_CURRENT_MAP;
/*     */   
/*     */   static {
/*  60 */     LEGACY_TO_CURRENT_MAP = (Map<String, String>)Util.make(Maps.newHashMap(), map -> {
/*     */           map.put("Iglu", "Igloo");
/*     */           map.put("TeDP", "Desert_Pyramid");
/*     */           map.put("TeJP", "Jungle_Pyramid");
/*     */           map.put("TeSH", "Swamp_Hut");
/*     */         });
/*     */   }
/*     */   
/*  67 */   public static final List<String> NETHER_LEGACY_STRUCTURES = List.of("Fortress");
/*     */   
/*  68 */   public static final List<String> END_LEGACY_STRUCTURES = List.of("EndCity");
/*     */   
/*  69 */   private static final ResourceKey<Level> OVERWORLD_KEY = ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("overworld"));
/*     */   
/*  70 */   private static final ResourceKey<Level> NETHER_KEY = ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("the_nether"));
/*     */   
/*  71 */   private static final ResourceKey<Level> END_KEY = ResourceKey.create(Registries.DIMENSION, Identifier.withDefaultNamespace("the_end"));
/*     */   
/*     */   public LegacyStructureFileFix(Schema schema) {
/*  74 */     super(schema);
/*     */   }
/*     */   
/*     */   public static final class LegacyStructureData extends Record {
/*     */     private final Map<String, Dynamic<?>> starts;
/*     */     
/*     */     private final Map<String, LongList> indexes;
/*     */     
/*     */     public LegacyStructureData(Map<String, Dynamic<?>> starts, Map<String, LongList> indexes) {
/*  77 */       this.starts = starts;
/*  77 */       this.indexes = indexes;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #77	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$LegacyStructureData;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Map<String, Dynamic<?>> starts() {
/*  77 */       return this.starts;
/*     */     }
/*     */     
/*     */     public Map<String, LongList> indexes() {
/*  77 */       return this.indexes;
/*     */     }
/*     */     
/*     */     public LegacyStructureData() {
/*  82 */       this(new HashMap<>(), new HashMap<>());
/*     */     }
/*     */     
/*     */     public void addStart(String id, Dynamic<Tag> data) {
/*  86 */       this.starts.put(id, data);
/*     */     }
/*     */     
/*     */     public void addIndex(String id, long sourcePos) {
/*  90 */       ((LongList)this.indexes.computeIfAbsent(id, l -> new LongArrayList())).add(sourcePos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class DimensionFixEntry extends Record {
/*     */     private final ResourceKey<Level> dimensionKey;
/*     */     
/*     */     private final List<FileAccess<SavedDataNbt>> structureFileAccess;
/*     */     
/*     */     private final FileAccess<ChunkNbt> chunkFileAccess;
/*     */     
/*     */     private final Long2ObjectOpenHashMap<LegacyStructureFileFix.LegacyStructureData> structures;
/*     */     
/*     */     private DimensionFixEntry(ResourceKey<Level> dimensionKey, List<FileAccess<SavedDataNbt>> structureFileAccess, FileAccess<ChunkNbt> chunkFileAccess, Long2ObjectOpenHashMap<LegacyStructureFileFix.LegacyStructureData> structures) {
/*  94 */       this.dimensionKey = dimensionKey;
/*  94 */       this.structureFileAccess = structureFileAccess;
/*  94 */       this.chunkFileAccess = chunkFileAccess;
/*  94 */       this.structures = structures;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #94	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/filefix/fixes/LegacyStructureFileFix$DimensionFixEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public ResourceKey<Level> dimensionKey() {
/*  94 */       return this.dimensionKey;
/*     */     }
/*     */     
/*     */     public List<FileAccess<SavedDataNbt>> structureFileAccess() {
/*  94 */       return this.structureFileAccess;
/*     */     }
/*     */     
/*     */     public FileAccess<ChunkNbt> chunkFileAccess() {
/*  94 */       return this.chunkFileAccess;
/*     */     }
/*     */     
/*     */     public Long2ObjectOpenHashMap<LegacyStructureFileFix.LegacyStructureData> structures() {
/*  94 */       return this.structures;
/*     */     }
/*     */   }
/*     */   
/*     */   public void makeFixer() {
/* 103 */     addFileContentFix(files -> {
/*     */           List<FileAccess<SavedDataNbt>> overworldStructureData = OVERWORLD_LEGACY_STRUCTURES.stream().map(()).toList();
/*     */           RegionStorageInfo overworldInfo = new RegionStorageInfo("overworld", OVERWORLD_KEY, "chunk");
/*     */           List<FileAccess<SavedDataNbt>> netherStructureData = NETHER_LEGACY_STRUCTURES.stream().map(()).toList();
/*     */           RegionStorageInfo netherInfo = new RegionStorageInfo("the_nether", NETHER_KEY, "chunk");
/*     */           List<FileAccess<SavedDataNbt>> endStructureData = END_LEGACY_STRUCTURES.stream().map(()).toList();
/*     */           RegionStorageInfo endInfo = new RegionStorageInfo("the_end", END_KEY, "chunk");
/*     */           FileAccess<LevelDat> levelDat = files.getFileAccess(FileResourceTypes.LEVEL_DAT, FileRelation.ORIGIN.forFile("level.dat"));
/*     */           FileAccess<ChunkNbt> overworldChunks = files.getFileAccess(FileResourceTypes.chunk(DataFixTypes.CHUNK, overworldInfo), FileRelation.OLD_OVERWORLD.resolve(FileRelation.REGION));
/*     */           FileAccess<ChunkNbt> netherChunks = files.getFileAccess(FileResourceTypes.chunk(DataFixTypes.CHUNK, netherInfo), FileRelation.OLD_NETHER.resolve(FileRelation.REGION));
/*     */           FileAccess<ChunkNbt> endChunks = files.getFileAccess(FileResourceTypes.chunk(DataFixTypes.CHUNK, endInfo), FileRelation.OLD_END.resolve(FileRelation.REGION));
/*     */           return ();
/*     */         });
/*     */   }
/*     */   
/*     */   private static void extractAndStoreLegacyStructureData(Dynamic<Tag> levelData, List<DimensionFixEntry> dimensionFixEntries, UpgradeProgress upgradeProgress) throws IOException {
/* 132 */     upgradeProgress.setStatus(UpgradeProgress.Status.COUNTING);
/* 133 */     for (DimensionFixEntry dimensionFixEntry : dimensionFixEntries) {
/* 134 */       Long2ObjectOpenHashMap<LegacyStructureData> structures = dimensionFixEntry.structures;
/* 135 */       for (FileAccess<SavedDataNbt> structureDataFileAccess : dimensionFixEntry.structureFileAccess) {
/* 136 */         SavedDataNbt targetFile = (SavedDataNbt)structureDataFileAccess.getOnlyFile();
/* 137 */         Optional<Dynamic<Tag>> structureData = targetFile.read();
/* 138 */         if (structureData.isEmpty())
/*     */           continue; 
/* 141 */         extractLegacyStructureData(structureData.get(), (Long2ObjectMap<LegacyStructureData>)structures);
/*     */       } 
/* 143 */       upgradeProgress.addTotalFileFixOperations(structures.size());
/*     */     } 
/* 145 */     upgradeProgress.setStatus(UpgradeProgress.Status.UPGRADING);
/* 146 */     for (DimensionFixEntry dimensionFixEntry : dimensionFixEntries) {
/*     */       String chunkGeneratorType;
/* 147 */       ResourceKey<Level> dimensionKey = dimensionFixEntry.dimensionKey;
/* 148 */       ChunkNbt chunkNbt = (ChunkNbt)dimensionFixEntry.chunkFileAccess.getOnlyFile();
/* 150 */       if (dimensionKey == OVERWORLD_KEY) {
/* 151 */         String generatorName = levelData.get("generatorName").asString("buffet");
/* 152 */         switch (generatorName) {
/*     */           case "flat":
/*     */           
/*     */           case "debug_all_block_states":
/*     */           
/*     */           default:
/*     */             break;
/*     */         } 
/* 152 */         chunkGeneratorType = 
/*     */           
/* 155 */           "minecraft:noise";
/*     */       } else {
/* 158 */         chunkGeneratorType = "minecraft:noise";
/*     */       } 
/* 160 */       Optional<Identifier> generatorIdentifier = Optional.ofNullable(Identifier.tryParse(chunkGeneratorType));
/* 161 */       CompoundTag dataFixContext = ChunkMap.getChunkDataFixContextTag(dimensionKey, generatorIdentifier);
/* 162 */       storeLegacyStructureDataToChunks((Long2ObjectMap<LegacyStructureData>)dimensionFixEntry.structures, chunkNbt, dataFixContext, upgradeProgress);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static FileAccess<SavedDataNbt> getLegacyStructureData(FileAccessProvider files, String structureId) {
/* 167 */     return files.getFileAccess(FileResourceTypes.savedData(References.SAVED_DATA_STRUCTURE_FEATURE_INDICES, CompressedNbt.MissingSeverity.MINOR), FileRelation.DATA.forFile(structureId + ".dat"));
/*     */   }
/*     */   
/*     */   private static void extractLegacyStructureData(Dynamic<Tag> structureData, Long2ObjectMap<LegacyStructureData> extractedDataContainer) {
/* 171 */     OptionalDynamic<Tag> features = structureData.get("Features");
/* 172 */     Map<Dynamic<Tag>, Dynamic<Tag>> map = features.asMap(Function.identity(), Function.identity());
/* 173 */     for (Iterator<Dynamic<Tag>> iterator = map.values().iterator(); iterator.hasNext(); ) {
/* 173 */       Dynamic<Tag> value = iterator.next();
/* 174 */       long pos = ChunkPos.pack(value.get("ChunkX").asInt(0), value.get("ChunkZ").asInt(0));
/* 175 */       List<Dynamic<Tag>> childList = value.get("Children").asList(Function.identity());
/* 176 */       if (!childList.isEmpty()) {
/* 178 */         Objects.requireNonNull(LEGACY_TO_CURRENT_MAP);
/* 178 */         Optional<String> id = ((Dynamic)childList.getFirst()).get("id").asString().result().map(LEGACY_TO_CURRENT_MAP::get);
/* 179 */         if (id.isPresent())
/* 180 */           value = value.set("id", value.createString(id.get())); 
/*     */       } 
/* 183 */       Dynamic<Tag> finalValue = value;
/* 185 */       value.get("id").asString().ifSuccess(id -> {
/*     */             ((LegacyStructureData)extractedDataContainer.computeIfAbsent(pos, ())).addStart(id, finalValue);
/*     */             for (int neighborX = ChunkPos.getX(pos) - 8; neighborX <= ChunkPos.getX(pos) + 8; neighborX++) {
/*     */               for (int neighborZ = ChunkPos.getZ(pos) - 8; neighborZ <= ChunkPos.getZ(pos) + 8; neighborZ++)
/*     */                 ((LegacyStructureData)extractedDataContainer.computeIfAbsent(ChunkPos.pack(neighborX, neighborZ), ())).addIndex(id, pos); 
/*     */             } 
/*     */           });
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void storeLegacyStructureDataToChunks(Long2ObjectMap<LegacyStructureData> structures, ChunkNbt chunksAccess, CompoundTag dataFixContext, UpgradeProgress upgradeProgress) {
/* 209 */     List<Long2ObjectMap.Entry<LegacyStructureData>> entries = structures.long2ObjectEntrySet().stream().sorted(Comparator.comparingLong(entry -> ChunkPos.pack(ChunkPos.getRegionX(entry.getLongKey()), ChunkPos.getRegionZ(entry.getLongKey())))).toList();
/* 213 */     IncrementalFutureSequence futures = new IncrementalFutureSequence(8);
/* 214 */     for (Long2ObjectMap.Entry<LegacyStructureData> entry : entries) {
/* 215 */       if (upgradeProgress.isCanceled())
/* 216 */         throw new CanceledFileFixException(); 
/* 218 */       long pos = entry.getLongKey();
/* 219 */       LegacyStructureData legacyData = (LegacyStructureData)entry.getValue();
/* 221 */       int finished = futures.push(chunksAccess.updateChunk(ChunkPos.unpack(pos), dataFixContext, tag -> {
/*     */               CompoundTag levelTag = tag.getCompoundOrEmpty("Level");
/*     */               CompoundTag structureTag = levelTag.getCompoundOrEmpty("Structures");
/*     */               CompoundTag startTag = structureTag.getCompoundOrEmpty("Starts");
/*     */               CompoundTag referencesTag = structureTag.getCompoundOrEmpty("References");
/*     */               legacyData.starts().forEach(());
/*     */               legacyData.indexes().forEach(());
/*     */               structureTag.put("Starts", (Tag)startTag);
/*     */               structureTag.put("References", (Tag)referencesTag);
/*     */               levelTag.put("Structures", (Tag)structureTag);
/*     */               tag.put("Level", (Tag)levelTag);
/*     */               return tag;
/*     */             }));
/* 239 */       upgradeProgress.incrementFinishedOperationsBy(finished);
/*     */     } 
/* 242 */     upgradeProgress.incrementFinishedOperationsBy(futures.waitForAll());
/*     */   }
/*     */   
/*     */   private static class IncrementalFutureSequence {
/*     */     private final int maxConcurrency;
/*     */     
/*     */     private final List<CompletableFuture<?>> futures;
/*     */     
/*     */     public IncrementalFutureSequence(int maxConcurrency) {
/* 250 */       this.maxConcurrency = maxConcurrency;
/* 251 */       this.futures = new ArrayList<>(maxConcurrency);
/*     */     }
/*     */     
/*     */     public int push(CompletableFuture<?> future) {
/* 255 */       int finished = 0;
/* 256 */       if (this.futures.size() >= this.maxConcurrency)
/* 257 */         finished += waitOnAny(); 
/* 259 */       this.futures.add(future);
/* 260 */       return finished;
/*     */     }
/*     */     
/*     */     private int waitOnAny() {
/* 264 */       int oldSize = this.futures.size();
/* 265 */       CompletableFuture.anyOf((CompletableFuture<?>[])this.futures.toArray(x$0 -> new CompletableFuture[x$0])).join();
/* 266 */       this.futures.removeIf(CompletableFuture::isDone);
/* 267 */       return oldSize - this.futures.size();
/*     */     }
/*     */     
/*     */     public int waitForAll() {
/* 271 */       int oldSize = this.futures.size();
/* 272 */       CompletableFuture.allOf((CompletableFuture<?>[])this.futures.toArray(x$0 -> new CompletableFuture[x$0])).join();
/* 273 */       this.futures.clear();
/* 274 */       return oldSize;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\fixes\LegacyStructureFileFix.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */