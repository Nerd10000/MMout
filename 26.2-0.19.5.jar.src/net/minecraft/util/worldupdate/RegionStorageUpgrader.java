/*     */ package net.minecraft.util.worldupdate;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.chunk.storage.RecreatingSimpleRegionStorage;
/*     */ import net.minecraft.world.level.chunk.storage.RegionFile;
/*     */ import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
/*     */ import net.minecraft.world.level.chunk.storage.SimpleRegionStorage;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RegionStorageUpgrader {
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final String NEW_DIRECTORY_PREFIX = "new_";
/*     */   
/*  40 */   private static final Pattern REGEX = Pattern.compile("^r\\.(-?[0-9]+)\\.(-?[0-9]+)\\.mca$");
/*     */   
/*     */   private final DataFixer dataFixer;
/*     */   
/*     */   private final UpgradeProgress upgradeProgress;
/*     */   
/*     */   private final String type;
/*     */   
/*     */   private final String folderName;
/*     */   
/*     */   protected CompletableFuture<Void> previousWriteFuture;
/*     */   
/*     */   protected final DataFixTypes dataFixType;
/*     */   
/*     */   protected final int defaultVersion;
/*     */   
/*     */   private final boolean recreateRegionFiles;
/*     */   
/*     */   private ResourceKey<Level> dimensionKey;
/*     */   
/*     */   private SimpleRegionStorage storage;
/*     */   
/*     */   private List<FileToUpgrade> files;
/*     */   
/*     */   private final int startIndex;
/*     */   
/*     */   private final CompoundTag dataFixContextTag;
/*     */   
/*     */   private final Int2ObjectMap<TagModifier> tagModifiers;
/*     */   
/*     */   protected RegionStorageUpgrader(DataFixer dataFixer, DataFixTypes dataFixType, String type, String folderName, int defaultVersion, boolean recreateRegionFiles, UpgradeProgress upgradeProgress, int startIndex, CompoundTag dataFixContextTag, Int2ObjectMap<TagModifier> tagModifiers) {
/*  68 */     this.dataFixer = dataFixer;
/*  69 */     this.dataFixType = dataFixType;
/*  70 */     this.type = type;
/*  71 */     this.folderName = folderName;
/*  72 */     this.recreateRegionFiles = recreateRegionFiles;
/*  73 */     this.defaultVersion = defaultVersion;
/*  74 */     this.upgradeProgress = upgradeProgress;
/*  75 */     this.startIndex = startIndex;
/*  76 */     this.dataFixContextTag = dataFixContextTag;
/*  77 */     this.tagModifiers = tagModifiers;
/*     */   }
/*     */   
/*     */   public void init(ResourceKey<Level> dimensionKey, LevelStorageSource.LevelStorageAccess levelStorage) {
/*  81 */     RegionStorageInfo info = new RegionStorageInfo(levelStorage.getLevelId(), dimensionKey, this.type);
/*  82 */     Path regionFolder = levelStorage.getDimensionPath(dimensionKey).resolve(this.folderName);
/*  83 */     this.dimensionKey = dimensionKey;
/*  84 */     this.storage = createStorage(info, regionFolder);
/*  85 */     this.files = getFilesToProcess(info, regionFolder);
/*     */   }
/*     */   
/*     */   public void upgrade() {
/*  89 */     if (this.dimensionKey == null || this.storage == null || this.files == null)
/*  90 */       throw new IllegalStateException("RegionStorageUpgrader has not been initialized"); 
/*  92 */     if (this.files.isEmpty())
/*     */       return; 
/*  95 */     float totalSize = this.upgradeProgress.getTotalFileFixStats().totalOperations();
/*  97 */     this.upgradeProgress.setStatus(UpgradeProgress.Status.UPGRADING);
/*  99 */     ListIterator<FileToUpgrade> iterator = this.files.listIterator();
/* 100 */     while (!this.upgradeProgress.isCanceled()) {
/* 101 */       boolean worked = false;
/* 103 */       float totalProgress = 0.0F;
/* 104 */       if (iterator.hasNext()) {
/* 105 */         FileToUpgrade fileToUpgrade = iterator.next();
/* 106 */         boolean converted = true;
/* 107 */         for (ChunkPos chunkPos : fileToUpgrade.chunksToUpgrade()) {
/* 108 */           converted = (converted && processOnePosition(this.storage, chunkPos));
/* 109 */           worked = true;
/*     */         } 
/* 111 */         if (this.recreateRegionFiles)
/* 112 */           if (converted) {
/* 113 */             onFileFinished(fileToUpgrade.file());
/*     */           } else {
/* 115 */             LOGGER.error("Failed to convert region file {}", fileToUpgrade.file().getPath());
/*     */           }  
/*     */       } 
/* 120 */       int nextIndex = iterator.nextIndex();
/* 121 */       float currentDimensionProgress = nextIndex / totalSize;
/* 122 */       float currentTotalProgress = (this.startIndex + nextIndex) / totalSize;
/* 123 */       this.upgradeProgress.setDimensionProgress(this.dimensionKey, currentDimensionProgress);
/* 124 */       totalProgress += currentTotalProgress;
/* 125 */       this.upgradeProgress.setTotalProgress(totalProgress);
/* 127 */       if (!worked)
/*     */         break; 
/*     */     } 
/* 132 */     this.upgradeProgress.setStatus(UpgradeProgress.Status.FINISHED);
/*     */     try {
/* 135 */       this.storage.close();
/* 136 */     } catch (Exception e) {
/* 137 */       LOGGER.error("Error upgrading chunk", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected final SimpleRegionStorage createStorage(RegionStorageInfo info, Path regionFolder) {
/* 142 */     return this.recreateRegionFiles ? 
/* 143 */       (SimpleRegionStorage)new RecreatingSimpleRegionStorage(info
/* 144 */         .withTypeSuffix("source"), regionFolder, info
/* 145 */         .withTypeSuffix("target"), resolveRecreateDirectory(regionFolder), this.dataFixer, true, this.dataFixType) : 
/*     */       
/* 150 */       new SimpleRegionStorage(info, regionFolder, this.dataFixer, true, this.dataFixType);
/*     */   }
/*     */   
/*     */   private List<FileToUpgrade> getFilesToProcess(RegionStorageInfo info, Path regionFolder) {
/* 154 */     List<FileToUpgrade> filesToUpgrade = getAllChunkPositions(info, regionFolder);
/* 155 */     this.upgradeProgress.addTotalFileFixOperations(filesToUpgrade.size());
/* 156 */     this.upgradeProgress.addTotalChunks(filesToUpgrade.stream().mapToInt(fileToUpgrade -> fileToUpgrade.chunksToUpgrade().size()).sum());
/* 157 */     return filesToUpgrade;
/*     */   }
/*     */   
/*     */   public int fileAmount() {
/* 161 */     if (this.files == null)
/* 162 */       return 0; 
/* 164 */     return this.files.size();
/*     */   }
/*     */   
/*     */   private static List<FileToUpgrade> getAllChunkPositions(RegionStorageInfo info, Path regionFolder) {
/* 169 */     File[] files = regionFolder.toFile().listFiles((dir, name) -> name.endsWith(".mca"));
/* 171 */     if (files == null)
/* 172 */       return List.of(); 
/* 175 */     List<FileToUpgrade> regionFileChunks = Lists.newArrayList();
/* 176 */     for (File regionFile : files) {
/* 177 */       Matcher regex = REGEX.matcher(regionFile.getName());
/* 178 */       if (regex.matches()) {
/* 182 */         int xOffset = Integer.parseInt(regex.group(1)) << 5;
/* 183 */         int zOffset = Integer.parseInt(regex.group(2)) << 5;
/* 185 */         List<ChunkPos> chunkPositions = Lists.newArrayList();
/*     */         try {
/* 186 */           RegionFile regionSource = new RegionFile(info, regionFile.toPath(), regionFolder, true);
/*     */           try {
/* 187 */             for (int x = 0; x < 32; x++) {
/* 188 */               for (int z = 0; z < 32; z++) {
/* 189 */                 ChunkPos pos = new ChunkPos(x + xOffset, z + zOffset);
/* 190 */                 if (regionSource.doesChunkExist(pos))
/* 191 */                   chunkPositions.add(pos); 
/*     */               } 
/*     */             } 
/* 195 */             if (!chunkPositions.isEmpty())
/* 196 */               regionFileChunks.add(new FileToUpgrade(regionSource, chunkPositions)); 
/* 198 */             regionSource.close();
/*     */           } catch (Throwable throwable) {
/*     */             try {
/*     */               regionSource.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             } 
/*     */             throw throwable;
/*     */           } 
/* 198 */         } catch (Throwable t) {
/* 199 */           LOGGER.error("Failed to read chunks from region file {}", regionFile.toPath(), t);
/*     */         } 
/*     */       } 
/*     */     } 
/* 202 */     return regionFileChunks;
/*     */   }
/*     */   
/*     */   private boolean processOnePosition(SimpleRegionStorage storage, ChunkPos pos) {
/* 206 */     boolean converted = false;
/*     */     try {
/* 208 */       converted = tryProcessOnePosition(storage, pos);
/* 209 */     } catch (ReportedException|java.util.concurrent.CompletionException e) {
/* 210 */       Throwable cause = e.getCause();
/* 211 */       if (cause instanceof IOException) {
/* 212 */         LOGGER.error("Error upgrading chunk {}", pos, cause);
/*     */       } else {
/* 214 */         throw e;
/*     */       } 
/*     */     } 
/* 217 */     if (converted) {
/* 218 */       this.upgradeProgress.incrementConverted();
/*     */     } else {
/* 220 */       this.upgradeProgress.incrementSkipped();
/*     */     } 
/* 222 */     return converted;
/*     */   }
/*     */   
/*     */   protected boolean tryProcessOnePosition(SimpleRegionStorage storage, ChunkPos pos) {
/* 226 */     CompoundTag chunkTag = ((Optional<CompoundTag>)storage.read(pos).join()).orElse(null);
/* 227 */     if (chunkTag != null) {
/* 228 */       int version = NbtUtils.getDataVersion(chunkTag);
/* 229 */       int latestVersion = SharedConstants.getCurrentVersion().dataVersion().version();
/* 231 */       boolean changed = false;
/* 232 */       for (ObjectIterator<Int2ObjectMap.Entry<TagModifier>> objectIterator = this.tagModifiers.int2ObjectEntrySet().iterator(); objectIterator.hasNext(); ) {
/* 232 */         Int2ObjectMap.Entry<TagModifier> tagFixer = objectIterator.next();
/* 233 */         int neededVersion = tagFixer.getIntKey();
/* 234 */         chunkTag = upgradeTag(storage, chunkTag, neededVersion);
/* 235 */         changed |= ((TagModifier)tagFixer.getValue()).modifyTagAfterFix(pos, chunkTag);
/*     */       } 
/* 237 */       CompoundTag upgradedTag = upgradeTag(storage, chunkTag, latestVersion);
/* 238 */       int i = changed | ((version < latestVersion) ? 1 : 0);
/* 240 */       if (i != 0 || this.recreateRegionFiles) {
/* 241 */         if (this.previousWriteFuture != null)
/* 242 */           this.previousWriteFuture.join(); 
/* 244 */         this.previousWriteFuture = storage.write(pos, upgradedTag);
/* 245 */         return true;
/*     */       } 
/*     */     } 
/* 248 */     return false;
/*     */   }
/*     */   
/*     */   protected CompoundTag upgradeTag(SimpleRegionStorage storage, CompoundTag chunkTag, int targetVersion) {
/* 252 */     return storage.upgradeChunkTag(chunkTag, this.defaultVersion, this.dataFixContextTag, targetVersion);
/*     */   }
/*     */   
/*     */   private void onFileFinished(RegionFile regionFile) {
/* 256 */     if (!this.recreateRegionFiles)
/*     */       return; 
/* 259 */     if (this.previousWriteFuture != null)
/* 260 */       this.previousWriteFuture.join(); 
/* 262 */     Path filePath = regionFile.getPath();
/* 263 */     Path directoryPath = filePath.getParent();
/* 264 */     Path newFilePath = resolveRecreateDirectory(directoryPath).resolve(filePath.getFileName().toString());
/*     */     try {
/* 267 */       if (newFilePath.toFile().exists()) {
/* 268 */         Files.delete(filePath);
/* 269 */         Files.move(newFilePath, filePath, new java.nio.file.CopyOption[0]);
/*     */       } else {
/* 271 */         LOGGER.error("Failed to replace an old region file. New file {} does not exist.", newFilePath);
/*     */       } 
/* 273 */     } catch (IOException e) {
/* 274 */       LOGGER.error("Failed to replace an old region file", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static Path resolveRecreateDirectory(Path directoryPath) {
/* 279 */     return directoryPath.resolveSibling("new_" + directoryPath.getFileName().toString());
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface TagModifier {
/*     */     boolean modifyTagAfterFix(ChunkPos param1ChunkPos, CompoundTag param1CompoundTag);
/*     */   }
/*     */   
/*     */   public static class Builder {
/*     */     private final DataFixer dataFixer;
/*     */     
/*     */     private DataFixTypes dataFixType;
/*     */     
/*     */     private String type;
/*     */     
/*     */     private String folderName;
/*     */     
/* 287 */     private int defaultVersion = -1;
/*     */     
/*     */     private boolean recreateRegionFiles;
/*     */     
/* 289 */     private UpgradeProgress upgradeProgress = new UpgradeProgress.Noop();
/*     */     
/*     */     private CompoundTag dataFixContextTag;
/*     */     
/* 291 */     private Int2ObjectAVLTreeMap<RegionStorageUpgrader.TagModifier> tagModifiers = new Int2ObjectAVLTreeMap();
/*     */     
/*     */     public Builder(DataFixer dataFixer) {
/* 294 */       this.dataFixer = dataFixer;
/*     */     }
/*     */     
/*     */     public Builder setDataFixType(DataFixTypes dataFixType) {
/* 298 */       this.dataFixType = dataFixType;
/* 299 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setTypeAndFolderName(String name) {
/* 303 */       return setType(name).setFolderName(name);
/*     */     }
/*     */     
/*     */     public Builder setType(String type) {
/* 307 */       this.type = type;
/* 308 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setFolderName(String folderName) {
/* 312 */       this.folderName = folderName;
/* 313 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDefaultVersion(int defaultVersion) {
/* 317 */       this.defaultVersion = defaultVersion;
/* 318 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setRecreateRegionFiles(boolean recreateRegionFiles) {
/* 322 */       this.recreateRegionFiles = recreateRegionFiles;
/* 323 */       return this;
/*     */     }
/*     */     
/*     */     public Builder trackProgress(UpgradeProgress upgradeProgress) {
/* 327 */       this.upgradeProgress = upgradeProgress;
/* 328 */       return this;
/*     */     }
/*     */     
/*     */     public Builder setDataFixContextTag(CompoundTag dataFixContextTag) {
/* 332 */       this.dataFixContextTag = dataFixContextTag;
/* 333 */       return this;
/*     */     }
/*     */     
/*     */     public Builder addTagModifier(int version, RegionStorageUpgrader.TagModifier tagModifier) {
/* 337 */       if (this.tagModifiers.containsKey(version))
/* 338 */         throw new IllegalStateException("Can't add two fixers for the same data version"); 
/* 340 */       this.tagModifiers.put(version, tagModifier);
/* 341 */       return this;
/*     */     }
/*     */     
/*     */     private Builder setTagModifiers(Int2ObjectAVLTreeMap<RegionStorageUpgrader.TagModifier> tagModifiers) {
/* 345 */       this.tagModifiers = tagModifiers;
/* 346 */       return this;
/*     */     }
/*     */     
/*     */     public Builder copy() {
/* 350 */       return (new Builder(this.dataFixer))
/* 351 */         .setDataFixType(this.dataFixType)
/* 352 */         .setType(this.type)
/* 353 */         .setFolderName(this.folderName)
/* 354 */         .setDefaultVersion(this.defaultVersion)
/* 355 */         .setRecreateRegionFiles(this.recreateRegionFiles)
/* 356 */         .trackProgress(this.upgradeProgress)
/* 357 */         .setDataFixContextTag(this.dataFixContextTag)
/* 358 */         .setTagModifiers(this.tagModifiers.clone());
/*     */     }
/*     */     
/*     */     public RegionStorageUpgrader build(int previousCopiesFileAmounts) {
/* 362 */       return new RegionStorageUpgrader(this.dataFixer, 
/*     */           
/* 364 */           Objects.<DataFixTypes>requireNonNull(this.dataFixType), 
/* 365 */           Objects.<String>requireNonNull(this.type), 
/* 366 */           Objects.<String>requireNonNull(this.folderName), this.defaultVersion, this.recreateRegionFiles, this.upgradeProgress, previousCopiesFileAmounts, this.dataFixContextTag, (Int2ObjectMap<RegionStorageUpgrader.TagModifier>)this.tagModifiers);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\RegionStorageUpgrader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */