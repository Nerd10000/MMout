/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.PathMatcher;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.time.Instant;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtAccounter;
/*     */ import net.minecraft.nbt.NbtFormatException;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.StreamTagVisitor;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.nbt.visitors.FieldSelector;
/*     */ import net.minecraft.nbt.visitors.SkipFields;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.WorldLoader;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.util.DirectoryLock;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.MemoryReserve;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.datafix.DataFixers;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.saveddata.SavedData;
/*     */ import net.minecraft.world.level.saveddata.SavedDataType;
/*     */ import net.minecraft.world.level.validation.ContentValidationException;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
/*     */ import net.minecraft.world.level.validation.PathAllowList;
/*     */ import org.apache.commons.io.function.IORunnable;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LevelStorageSource {
/*  93 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final String TAG_DATA = "Data";
/*     */   
/*     */   private static final PathMatcher NO_SYMLINKS_ALLOWED = path -> false;
/*     */   
/*     */   public static final String ALLOWED_SYMLINKS_CONFIG_NAME = "allowed_symlinks.txt";
/*     */   
/*     */   private static final int DISK_SPACE_WARNING_THRESHOLD = 67108864;
/*     */   
/*  98 */   private static final Component LOAD_FOLDER_ACCESS_MESSAGE = (Component)Component.translatable("selectWorld.load_folder_access");
/*     */   
/*     */   private final Path baseDir;
/*     */   
/*     */   private final Path backupDir;
/*     */   
/*     */   private final DataFixer fixerUpper;
/*     */   
/*     */   private final DirectoryValidator worldDirValidator;
/*     */   
/*     */   public LevelStorageSource(Path baseDir, Path backupDir, DirectoryValidator worldDirValidator, DataFixer fixerUpper) {
/* 107 */     this.fixerUpper = fixerUpper;
/*     */     try {
/* 109 */       FileUtil.createDirectoriesSafe(baseDir);
/* 110 */     } catch (IOException e) {
/* 111 */       throw new UncheckedIOException(e);
/*     */     } 
/* 113 */     this.baseDir = baseDir;
/* 114 */     this.backupDir = backupDir;
/* 116 */     this.worldDirValidator = worldDirValidator;
/*     */   }
/*     */   
/*     */   public static DirectoryValidator parseValidator(Path configPath) {
/* 120 */     if (Files.exists(configPath, new java.nio.file.LinkOption[0]))
/*     */       try {
/* 121 */         BufferedReader reader = Files.newBufferedReader(configPath);
/*     */         try {
/* 122 */           DirectoryValidator directoryValidator = new DirectoryValidator((PathMatcher)PathAllowList.readPlain(reader));
/* 123 */           if (reader != null)
/* 123 */             reader.close(); 
/*     */           return directoryValidator;
/*     */         } catch (Throwable throwable) {
/*     */           if (reader != null)
/*     */             try {
/*     */               reader.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 123 */       } catch (Exception e) {
/* 124 */         LOGGER.error("Failed to parse {}, disallowing all symbolic links", "allowed_symlinks.txt", e);
/*     */       }  
/* 128 */     return new DirectoryValidator(NO_SYMLINKS_ALLOWED);
/*     */   }
/*     */   
/*     */   public static LevelStorageSource createDefault(Path path) {
/* 132 */     DirectoryValidator validator = parseValidator(path.resolve("allowed_symlinks.txt"));
/* 133 */     return new LevelStorageSource(path, path.resolve("../backups"), validator, DataFixers.getDataFixer());
/*     */   }
/*     */   
/*     */   public static WorldDataConfiguration readDataConfig(Dynamic<?> levelData) {
/* 137 */     Objects.requireNonNull(LOGGER);
/* 137 */     return WorldDataConfiguration.CODEC.parse(levelData).resultOrPartial(LOGGER::error).orElse(WorldDataConfiguration.DEFAULT);
/*     */   }
/*     */   
/*     */   public static WorldLoader.PackConfig getPackConfig(Dynamic<?> levelDataTag, PackRepository packRepository, boolean safeMode) {
/* 141 */     return new WorldLoader.PackConfig(packRepository, readDataConfig(levelDataTag), safeMode, false);
/*     */   }
/*     */   
/*     */   public static LevelDataAndDimensions getLevelDataAndDimensions(LevelStorageAccess worldAccess, Dynamic<?> levelDataTag, WorldDataConfiguration dataConfiguration, Registry<LevelStem> datapackDimensions, HolderLookup.Provider registryAccess) {
/* 145 */     if (DataFixers.getFileFixer().requiresFileFixing(NbtUtils.getDataVersion(levelDataTag)))
/* 146 */       throw new IllegalStateException("Cannot get level data without file fixing first"); 
/* 148 */     Dynamic<?> dataTag = RegistryOps.injectRegistryContext(levelDataTag, registryAccess);
/* 149 */     WorldGenSettings worldGenSettings = (WorldGenSettings)readExistingSavedData(worldAccess, registryAccess, WorldGenSettings.TYPE).mapOrElse(Function.identity(), error -> {
/*     */           LOGGER.error("Unable to read or access the world gen settings file! Falling back to the default settings with a random world seed. {}", error.message());
/*     */           return new WorldGenSettings(WorldOptions.defaultWithRandomSeed(), new WorldDimensions(datapackDimensions));
/*     */         });
/* 153 */     LevelSettings settings = LevelSettings.parse(dataTag, dataConfiguration);
/* 154 */     WorldDimensions.Complete dimensions = worldGenSettings.dimensions().bake(datapackDimensions);
/* 155 */     Lifecycle lifecycle = dimensions.lifecycle().add(registryAccess.allRegistriesLifecycle());
/* 157 */     PrimaryLevelData worldData = PrimaryLevelData.parse(dataTag, settings, dimensions.specialWorldProperty(), lifecycle);
/* 158 */     return LevelDataAndDimensions.create(worldData, worldGenSettings, dimensions);
/*     */   }
/*     */   
/*     */   public static <T extends SavedData> DataResult<T> readExistingSavedData(LevelStorageAccess access, HolderLookup.Provider registryAccess, SavedDataType<T> savedDataType) {
/*     */     CompoundTag fileContents;
/* 162 */     Path dataLocation = savedDataType.id().withSuffix(".dat").resolveAgainst(access.getLevelPath(LevelResource.DATA));
/*     */     try {
/* 165 */       fileContents = NbtIo.readCompressed(dataLocation, NbtAccounter.unlimitedHeap());
/* 166 */     } catch (IOException e) {
/* 167 */       Objects.requireNonNull(e);
/* 167 */       return DataResult.error(e::getMessage);
/*     */     } 
/* 169 */     return savedDataType.codec().parse((DynamicOps)RegistryOps.create((DynamicOps)NbtOps.INSTANCE, registryAccess), fileContents.getCompoundOrEmpty("data"));
/*     */   }
/*     */   
/*     */   public static void writeGameRules(WorldData worldData, Path worldFolder, GameRules gameRules) throws IOException {
/* 173 */     Codec<GameRules> codec = GameRules.codec(worldData.enabledFeatures());
/* 174 */     writeSavedData(worldFolder, (DynamicOps<Tag>)NbtOps.INSTANCE, GameRuleMap.TYPE, codec, gameRules);
/*     */   }
/*     */   
/*     */   public static void writeWorldGenSettings(RegistryAccess registryAccess, Path worldFolder, WorldGenSettings worldGenSettings) throws IOException {
/* 178 */     RegistryOps<Tag> ops = RegistryOps.create((DynamicOps)NbtOps.INSTANCE, (HolderLookup.Provider)registryAccess);
/* 179 */     writeSavedData(worldFolder, (DynamicOps<Tag>)ops, WorldGenSettings.TYPE, WorldGenSettings.CODEC, worldGenSettings);
/*     */   }
/*     */   
/*     */   private static <T> void writeSavedData(Path worldFolder, DynamicOps<Tag> ops, SavedDataType<?> type, Codec<T> codec, T data) throws IOException {
/* 183 */     Tag encoded = (Tag)codec.encodeStart(ops, data).getOrThrow();
/* 184 */     CompoundTag fullTag = new CompoundTag();
/* 185 */     fullTag.put("data", encoded);
/* 186 */     NbtUtils.addCurrentDataVersion(fullTag);
/* 187 */     Path path = type.id().withSuffix(".dat").resolveAgainst(worldFolder.resolve("data"));
/* 188 */     FileUtil.createDirectoriesSafe(path.getParent());
/* 189 */     NbtIo.writeCompressed(fullTag, path);
/*     */   }
/*     */   
/*     */   public String getName() {
/* 193 */     return "Anvil";
/*     */   }
/*     */   
/*     */   public LevelCandidates findLevelCandidates() throws LevelStorageException {
/* 197 */     if (!Files.exists(this.baseDir, new java.nio.file.LinkOption[0])) {
/*     */       try {
/* 199 */         Files.createDirectory(this.baseDir, (FileAttribute<?>[])new FileAttribute[0]);
/* 200 */       } catch (IOException e) {
/* 201 */         throw new LevelStorageException(LOAD_FOLDER_ACCESS_MESSAGE, e);
/*     */       } 
/* 203 */     } else if (!Files.isDirectory(this.baseDir, new java.nio.file.LinkOption[0])) {
/* 204 */       throw new LevelStorageException(LOAD_FOLDER_ACCESS_MESSAGE);
/*     */     } 
/*     */     try {
/* 207 */       Stream<Path> paths = Files.list(this.baseDir);
/*     */       try {
/* 211 */         List<LevelDirectory> candidates = paths.filter(x$0 -> Files.isDirectory(x$0, new java.nio.file.LinkOption[0])).map(LevelDirectory::new).filter(directory -> (Files.isRegularFile(directory.dataFile(), new java.nio.file.LinkOption[0]) || Files.isRegularFile(directory.oldDataFile(), new java.nio.file.LinkOption[0]))).toList();
/* 213 */         LevelCandidates levelCandidates = new LevelCandidates(candidates);
/* 214 */         if (paths != null)
/* 214 */           paths.close(); 
/*     */         return levelCandidates;
/*     */       } catch (Throwable throwable) {
/*     */         if (paths != null)
/*     */           try {
/*     */             paths.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/* 214 */     } catch (IOException e) {
/* 215 */       throw new LevelStorageException(LOAD_FOLDER_ACCESS_MESSAGE);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<List<LevelSummary>> loadLevelSummaries(LevelCandidates candidates) {
/* 220 */     List<CompletableFuture<LevelSummary>> futures = new ArrayList<>(candidates.levels.size());
/* 221 */     for (Iterator<LevelDirectory> iterator = candidates.levels.iterator(); iterator.hasNext(); ) {
/* 221 */       LevelDirectory level = iterator.next();
/* 222 */       futures.add(CompletableFuture.supplyAsync(() -> {
/*     */               boolean locked;
/*     */               try {
/*     */                 locked = DirectoryLock.isLocked(level.path());
/* 226 */               } catch (Exception e) {
/*     */                 LOGGER.warn("Failed to read {} lock", level.path(), e);
/*     */                 return null;
/*     */               } 
/*     */               try {
/*     */                 return readLevelSummary(level, locked);
/* 233 */               } catch (OutOfMemoryError e) {
/*     */                 MemoryReserve.release();
/*     */                 String detailedMessage = "Ran out of memory trying to read summary of world folder \"" + level.directoryName() + "\"";
/*     */                 LOGGER.error(LogUtils.FATAL_MARKER, detailedMessage);
/*     */                 OutOfMemoryError detailedException = new OutOfMemoryError("Ran out of memory reading level data");
/*     */                 detailedException.initCause(e);
/*     */                 CrashReport crashReport = CrashReport.forThrowable(detailedException, detailedMessage);
/*     */                 CrashReportCategory worldDetails = crashReport.addCategory("World details");
/*     */                 worldDetails.setDetail("Folder Name", level.directoryName());
/*     */                 try {
/*     */                   long size = Files.size(level.dataFile());
/*     */                   worldDetails.setDetail("level.dat size", Long.valueOf(size));
/* 248 */                 } catch (IOException ex) {
/*     */                   worldDetails.setDetailError("level.dat size", ex);
/*     */                 } 
/*     */                 throw new ReportedException(crashReport);
/*     */               } 
/* 254 */             }Util.backgroundExecutor().forName("loadLevelSummaries")));
/*     */     } 
/* 257 */     return Util.sequenceFailFastAndCancel(futures)
/* 258 */       .thenApply(levels -> levels.stream().filter(Objects::nonNull).sorted().toList());
/*     */   }
/*     */   
/*     */   private int getStorageVersion() {
/* 265 */     return 19133;
/*     */   }
/*     */   
/*     */   private static CompoundTag readLevelDataTagRaw(Path dataFile) throws IOException {
/* 269 */     return NbtIo.readCompressed(dataFile, NbtAccounter.uncompressedQuota());
/*     */   }
/*     */   
/*     */   private LevelSummary readLevelSummary(LevelDirectory level, boolean locked) {
/* 273 */     Path dataFile = level.dataFile();
/* 274 */     if (Files.exists(dataFile, new java.nio.file.LinkOption[0]))
/*     */       try {
/* 276 */         if (Files.isSymbolicLink(dataFile)) {
/* 277 */           List<ForbiddenSymlinkInfo> issues = this.worldDirValidator.validateSymlink(dataFile);
/* 278 */           if (!issues.isEmpty()) {
/* 279 */             LOGGER.warn("{}", ContentValidationException.getMessage(dataFile, issues));
/* 280 */             return new LevelSummary.SymlinkLevelSummary(level.directoryName(), level.iconFile());
/*     */           } 
/*     */         } 
/* 283 */         Tag result = readLightweightData(dataFile);
/* 284 */         if (result instanceof CompoundTag) {
/* 284 */           CompoundTag root = (CompoundTag)result;
/* 285 */           CompoundTag tag = root.getCompoundOrEmpty("Data");
/* 286 */           int dataVersion = NbtUtils.getDataVersion(tag);
/* 287 */           Dynamic<?> updated = DataFixTypes.LEVEL_SUMMARY.updateToCurrentVersion(this.fixerUpper, new Dynamic((DynamicOps)NbtOps.INSTANCE, tag), dataVersion);
/* 288 */           return makeLevelSummary(updated, level, locked, dataVersion);
/*     */         } 
/* 290 */         LOGGER.warn("Invalid root tag in {}", dataFile);
/* 292 */       } catch (Exception e) {
/* 293 */         LOGGER.error("Exception reading {}", dataFile, e);
/*     */       }  
/* 296 */     return new LevelSummary.CorruptedLevelSummary(level.directoryName(), level.iconFile(), getFileModificationTime(level));
/*     */   }
/*     */   
/*     */   private static long getFileModificationTime(LevelDirectory level) {
/* 300 */     Instant timeStamp = getFileModificationTime(level.dataFile());
/* 301 */     if (timeStamp == null)
/* 302 */       timeStamp = getFileModificationTime(level.oldDataFile()); 
/* 304 */     return (timeStamp == null) ? -1L : timeStamp.toEpochMilli();
/*     */   }
/*     */   
/*     */   private static Instant getFileModificationTime(Path path) {
/*     */     try {
/* 309 */       return Files.getLastModifiedTime(path, new java.nio.file.LinkOption[0]).toInstant();
/* 310 */     } catch (IOException iOException) {
/* 312 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private LevelSummary makeLevelSummary(Dynamic<?> dataTag, LevelDirectory levelDirectory, boolean locked, int dataVersion) {
/* 316 */     LevelVersion levelVersion = LevelVersion.parse(dataTag);
/* 317 */     int levelDataVersion = levelVersion.levelDataVersion();
/* 318 */     if (levelDataVersion == 19132 || levelDataVersion == 19133) {
/* 319 */       boolean requiresManualConversion = (levelDataVersion != getStorageVersion());
/* 320 */       boolean requiresFileFixing = DataFixers.getFileFixer().requiresFileFixing(dataVersion);
/* 321 */       Path icon = levelDirectory.iconFile();
/* 322 */       WorldDataConfiguration dataConfiguration = readDataConfig(dataTag);
/* 323 */       LevelSettings settings = LevelSettings.parse(dataTag, dataConfiguration);
/* 324 */       FeatureFlagSet enabledFeatureFlags = parseFeatureFlagsFromSummary(dataTag);
/* 325 */       boolean experimental = FeatureFlags.isExperimental(enabledFeatureFlags);
/* 326 */       return new LevelSummary(settings, levelVersion, levelDirectory.directoryName(), requiresManualConversion, requiresFileFixing, locked, experimental, icon);
/*     */     } 
/* 328 */     throw new NbtFormatException("Unknown data version: " + Integer.toHexString(levelDataVersion));
/*     */   }
/*     */   
/*     */   private static FeatureFlagSet parseFeatureFlagsFromSummary(Dynamic<?> tag) {
/* 336 */     Set<Identifier> enabledFlags = (Set<Identifier>)tag.get("enabled_features").asStream().flatMap(entry -> entry.asString().result().map(Identifier::tryParse).stream()).collect(Collectors.toSet());
/* 338 */     return FeatureFlags.REGISTRY.fromNames(enabledFlags, unknownId -> {
/*     */         
/*     */         });
/*     */   }
/*     */   
/*     */   private static Tag readLightweightData(Path dataFile) throws IOException {
/* 342 */     SkipFields parser = new SkipFields(new FieldSelector[] { new FieldSelector("Data", CompoundTag.TYPE, "Player"), new FieldSelector("Data", CompoundTag.TYPE, "WorldGenSettings") });
/* 346 */     NbtIo.parseCompressed(dataFile, (StreamTagVisitor)parser, NbtAccounter.uncompressedQuota());
/* 347 */     return parser.getResult();
/*     */   }
/*     */   
/*     */   public boolean isNewLevelIdAcceptable(String levelId) {
/*     */     try {
/* 352 */       Path fullPath = getLevelPath(levelId);
/* 353 */       Files.createDirectory(fullPath, (FileAttribute<?>[])new FileAttribute[0]);
/* 354 */       Files.deleteIfExists(fullPath);
/* 355 */       return true;
/* 356 */     } catch (IOException e) {
/* 357 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean levelExists(String levelId) {
/*     */     try {
/* 363 */       return Files.isDirectory(getLevelPath(levelId), new java.nio.file.LinkOption[0]);
/* 364 */     } catch (InvalidPathException e) {
/* 365 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Path getLevelPath(String levelId) {
/* 370 */     return this.baseDir.resolve(levelId);
/*     */   }
/*     */   
/*     */   public Path getBaseDir() {
/* 374 */     return this.baseDir;
/*     */   }
/*     */   
/*     */   public Path getBackupPath() {
/* 378 */     return this.backupDir;
/*     */   }
/*     */   
/*     */   public LevelStorageAccess validateAndCreateAccess(String levelId) throws IOException, ContentValidationException {
/* 382 */     Path levelPath = getLevelPath(levelId);
/* 383 */     List<ForbiddenSymlinkInfo> validationResults = this.worldDirValidator.validateDirectory(levelPath, true);
/* 384 */     if (!validationResults.isEmpty())
/* 385 */       throw new ContentValidationException(levelPath, validationResults); 
/* 387 */     return new LevelStorageAccess(levelId, levelPath);
/*     */   }
/*     */   
/*     */   public LevelStorageAccess createAccess(String levelId) throws IOException {
/* 391 */     Path levelPath = getLevelPath(levelId);
/* 392 */     return new LevelStorageAccess(levelId, levelPath);
/*     */   }
/*     */   
/*     */   public DirectoryValidator getWorldDirValidator() {
/* 396 */     return this.worldDirValidator;
/*     */   }
/*     */   
/*     */   public class LevelStorageAccess implements AutoCloseable {
/*     */     private DirectoryLock lock;
/*     */     
/*     */     private final LevelStorageSource.LevelDirectory levelDirectory;
/*     */     
/*     */     private final String levelId;
/*     */     
/* 403 */     private final Map<LevelResource, Path> resources = Maps.newHashMap();
/*     */     
/*     */     private LevelStorageAccess(String levelId, Path path) throws IOException {
/* 406 */       this.levelId = levelId;
/* 407 */       this.levelDirectory = new LevelStorageSource.LevelDirectory(path);
/* 408 */       createLock();
/*     */     }
/*     */     
/*     */     private void createLock() throws IOException {
/* 412 */       this.lock = DirectoryLock.create(this.levelDirectory.path);
/*     */     }
/*     */     
/*     */     public void releaseTemporarilyAndRun(IORunnable runnable) throws IOException {
/* 416 */       close();
/*     */       try {
/* 418 */         runnable.run();
/*     */       } finally {
/* 420 */         createLock();
/*     */       } 
/*     */     }
/*     */     
/*     */     public long estimateDiskSpace() {
/*     */       try {
/* 426 */         return Files.getFileStore(this.levelDirectory.path).getUsableSpace();
/* 427 */       } catch (Exception ignored) {
/* 429 */         return Long.MAX_VALUE;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean checkForLowDiskSpace() {
/* 434 */       return (estimateDiskSpace() < 67108864L);
/*     */     }
/*     */     
/*     */     public void safeClose() {
/*     */       try {
/* 439 */         close();
/* 440 */       } catch (IOException e) {
/* 441 */         LevelStorageSource.LOGGER.warn("Failed to unlock access to level {}", getLevelId(), e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public LevelStorageSource parent() {
/* 446 */       return LevelStorageSource.this;
/*     */     }
/*     */     
/*     */     public LevelStorageSource.LevelDirectory getLevelDirectory() {
/* 450 */       return this.levelDirectory;
/*     */     }
/*     */     
/*     */     public String getLevelId() {
/* 454 */       return this.levelId;
/*     */     }
/*     */     
/*     */     public Path getLevelPath(LevelResource resource) {
/* 458 */       Objects.requireNonNull(this.levelDirectory);
/* 458 */       return this.resources.computeIfAbsent(resource, this.levelDirectory::resourcePath);
/*     */     }
/*     */     
/*     */     public Path getDimensionPath(ResourceKey<Level> name) {
/* 462 */       return DimensionType.getStorageFolder(name, this.levelDirectory.path());
/*     */     }
/*     */     
/*     */     private void checkLock() {
/* 466 */       if (!this.lock.isValid())
/* 467 */         throw new IllegalStateException("Lock is no longer valid"); 
/*     */     }
/*     */     
/*     */     public PlayerDataStorage createPlayerStorage() {
/* 472 */       checkLock();
/* 473 */       return new PlayerDataStorage(this, LevelStorageSource.this.fixerUpper);
/*     */     }
/*     */     
/*     */     public void collectIssues(boolean useFallback) throws IOException {
/* 477 */       checkLock();
/* 479 */       Dynamic<?> unfixedDataTag = getUnfixedDataTag(useFallback);
/* 480 */       int dataVersion = NbtUtils.getDataVersion(unfixedDataTag);
/* 481 */       Dynamic<?> fixedDataTag = DataFixTypes.LEVEL.updateToCurrentVersion(LevelStorageSource.this.fixerUpper, unfixedDataTag, dataVersion);
/* 482 */       LevelStorageSource.this.makeLevelSummary(fixedDataTag, this.levelDirectory, false, dataVersion);
/*     */     }
/*     */     
/*     */     public LevelSummary fixAndGetSummary() throws IOException {
/* 486 */       checkLock();
/* 487 */       return fixAndGetSummaryFromTag(getUnfixedDataTag(false));
/*     */     }
/*     */     
/*     */     public LevelSummary fixAndGetSummaryFromTag(Dynamic<?> dataTag) {
/* 491 */       checkLock();
/* 492 */       int dataVersion = NbtUtils.getDataVersion(dataTag);
/* 493 */       Dynamic<?> dataTagFixed = DataFixTypes.LEVEL_SUMMARY.updateToCurrentVersion(LevelStorageSource.this.fixerUpper, dataTag, dataVersion);
/* 494 */       return LevelStorageSource.this.makeLevelSummary(dataTagFixed, this.levelDirectory, false, dataVersion);
/*     */     }
/*     */     
/*     */     public Dynamic<?> getUnfixedDataTagWithFallback() throws IOException {
/*     */       Dynamic<?> unfixedDataTag;
/*     */       try {
/* 500 */         unfixedDataTag = getUnfixedDataTag(false);
/* 501 */       } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 502 */         LevelStorageSource.LOGGER.warn("Failed to load world data from {}", this.levelDirectory.dataFile(), e);
/* 503 */         LevelStorageSource.LOGGER.info("Attempting to use fallback {}", this.levelDirectory.oldDataFile());
/* 504 */         unfixedDataTag = getUnfixedDataTag(true);
/* 505 */         restoreLevelDataFromOld();
/*     */       } 
/* 507 */       return unfixedDataTag;
/*     */     }
/*     */     
/*     */     public Dynamic<?> getUnfixedDataTag(boolean useFallback) throws IOException {
/* 511 */       checkLock();
/* 512 */       Path dataFile = getDataFile(useFallback);
/* 513 */       CompoundTag root = LevelStorageSource.readLevelDataTagRaw(dataFile);
/* 514 */       return new Dynamic((DynamicOps)NbtOps.INSTANCE, root.getCompoundOrEmpty("Data"));
/*     */     }
/*     */     
/*     */     private Path getDataFile(boolean useFallback) {
/* 518 */       return useFallback ? this.levelDirectory.oldDataFile() : this.levelDirectory.dataFile();
/*     */     }
/*     */     
/*     */     public void saveDataTag(WorldData levelData) {
/* 522 */       saveDataTag(levelData, null);
/*     */     }
/*     */     
/*     */     public void saveDataTag(WorldData levelData, UUID singleplayerUUID) {
/* 526 */       CompoundTag dataTag = levelData.createTag(singleplayerUUID);
/* 528 */       CompoundTag root = new CompoundTag();
/* 529 */       root.put("Data", (Tag)dataTag);
/* 531 */       saveLevelData(root);
/*     */     }
/*     */     
/*     */     public void saveLevelData(Dynamic<?> tag) {
/* 535 */       Tag genericTag = (Tag)tag.convert((DynamicOps)NbtOps.INSTANCE).getValue();
/* 536 */       CompoundTag root = new CompoundTag();
/* 537 */       root.put("Data", genericTag);
/* 539 */       saveLevelData(root);
/*     */     }
/*     */     
/*     */     private void saveLevelData(CompoundTag root) {
/* 543 */       Path worldDir = this.levelDirectory.path();
/*     */       try {
/* 545 */         Path dataFile = Files.createTempFile(worldDir, "level", ".dat", (FileAttribute<?>[])new FileAttribute[0]);
/* 546 */         NbtIo.writeCompressed(root, dataFile);
/* 548 */         Path oldDataFile = this.levelDirectory.oldDataFile();
/* 549 */         Path currentFile = this.levelDirectory.dataFile();
/* 550 */         Util.safeReplaceFile(currentFile, dataFile, oldDataFile);
/* 551 */       } catch (Exception e) {
/* 552 */         LevelStorageSource.LOGGER.error("Failed to save level {}", worldDir, e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Optional<Path> getIconFile() {
/* 557 */       if (!this.lock.isValid())
/* 558 */         return Optional.empty(); 
/* 560 */       return Optional.of(this.levelDirectory.iconFile());
/*     */     }
/*     */     
/*     */     public void deleteLevel() throws IOException {
/* 564 */       checkLock();
/* 566 */       final Path lockPath = this.levelDirectory.lockFile();
/* 568 */       LevelStorageSource.LOGGER.info("Deleting level {}", this.levelId);
/* 569 */       for (int attempt = 1; attempt <= 5; attempt++) {
/* 570 */         LevelStorageSource.LOGGER.info("Attempt {}...", Integer.valueOf(attempt));
/*     */         try {
/* 573 */           Files.walkFileTree(this.levelDirectory.path(), new SimpleFileVisitor<Path>() {
/*     */                 public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 576 */                   if (!file.equals(lockPath)) {
/* 577 */                     LevelStorageSource.LOGGER.debug("Deleting {}", file);
/* 581 */                     Files.deleteIfExists(file);
/*     */                   } 
/* 583 */                   return FileVisitResult.CONTINUE;
/*     */                 }
/*     */                 
/*     */                 public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/* 588 */                   if (exc != null)
/* 589 */                     throw exc; 
/* 592 */                   if (dir.equals(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path())) {
/* 594 */                     LevelStorageSource.LevelStorageAccess.this.lock.close();
/* 595 */                     Files.deleteIfExists(lockPath);
/*     */                   } 
/* 597 */                   Files.deleteIfExists(dir);
/* 598 */                   return FileVisitResult.CONTINUE;
/*     */                 }
/*     */               });
/*     */           break;
/* 602 */         } catch (IOException e) {
/* 603 */           if (attempt < 5) {
/* 604 */             LevelStorageSource.LOGGER.warn("Failed to delete {}", this.levelDirectory.path(), e);
/*     */             try {
/* 606 */               Thread.sleep(500L);
/* 607 */             } catch (InterruptedException interruptedException) {}
/*     */           } else {
/* 610 */             throw e;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     public void renameLevel(String newName) throws IOException {
/* 617 */       modifyLevelDataWithoutDatafix(tag -> tag.putString("LevelName", newName.trim()));
/*     */     }
/*     */     
/*     */     public void renameAndDropPlayer(String newName) throws IOException {
/* 621 */       modifyLevelDataWithoutDatafix(tag -> {
/*     */             tag.putString("LevelName", newName.trim());
/*     */             tag.remove("singleplayer_uuid");
/*     */           });
/*     */     }
/*     */     
/*     */     private void modifyLevelDataWithoutDatafix(Consumer<CompoundTag> updater) throws IOException {
/* 628 */       checkLock();
/* 630 */       CompoundTag root = LevelStorageSource.readLevelDataTagRaw(this.levelDirectory.dataFile());
/* 631 */       updater.accept(root.getCompoundOrEmpty("Data"));
/* 632 */       saveLevelData(root);
/*     */     }
/*     */     
/*     */     public long makeWorldBackup() throws IOException {
/* 636 */       checkLock();
/* 637 */       String zipFilePrefix = FileNameDateFormatter.FORMATTER.format(ZonedDateTime.now()) + "_" + FileNameDateFormatter.FORMATTER.format(ZonedDateTime.now());
/* 639 */       Path root = LevelStorageSource.this.getBackupPath();
/*     */       try {
/* 641 */         FileUtil.createDirectoriesSafe(root);
/* 642 */       } catch (IOException e) {
/* 643 */         throw new RuntimeException(e);
/*     */       } 
/* 645 */       Path zipFilePath = root.resolve(FileUtil.findAvailableName(root, zipFilePrefix, ".zip"));
/* 647 */       final ZipOutputStream stream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(zipFilePath, new java.nio.file.OpenOption[0])));
/*     */       try {
/* 648 */         final Path rootPath = Paths.get(this.levelId, new String[0]);
/* 650 */         Files.walkFileTree(this.levelDirectory.path(), new SimpleFileVisitor<Path>() {
/*     */               public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
/* 653 */                 if (path.endsWith("session.lock"))
/* 654 */                   return FileVisitResult.CONTINUE; 
/* 656 */                 String entryPath = rootPath.resolve(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path().relativize(path)).toString().replace('\\', '/');
/* 657 */                 ZipEntry entry = new ZipEntry(entryPath);
/* 658 */                 stream.putNextEntry(entry);
/* 659 */                 Files.asByteSource(path.toFile()).copyTo(stream);
/* 660 */                 stream.closeEntry();
/* 661 */                 return FileVisitResult.CONTINUE;
/*     */               }
/*     */             });
/* 664 */         stream.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           stream.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/* 666 */       return Files.size(zipFilePath);
/*     */     }
/*     */     
/*     */     public boolean hasWorldData() {
/* 670 */       return (Files.exists(this.levelDirectory.dataFile(), new java.nio.file.LinkOption[0]) || Files.exists(this.levelDirectory.oldDataFile(), new java.nio.file.LinkOption[0]));
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 675 */       this.lock.close();
/*     */     }
/*     */     
/*     */     public boolean restoreLevelDataFromOld() {
/* 679 */       return Util.safeReplaceOrMoveFile(this.levelDirectory.dataFile(), this.levelDirectory.oldDataFile(), this.levelDirectory.corruptedDataFile(ZonedDateTime.now()), true);
/*     */     }
/*     */     
/*     */     public Instant getFileModificationTime(boolean fallback) {
/* 683 */       return LevelStorageSource.getFileModificationTime(getDataFile(fallback));
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends SimpleFileVisitor<Path> {
/*     */     public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/*     */       if (!file.equals(lockPath)) {
/*     */         LevelStorageSource.LOGGER.debug("Deleting {}", file);
/*     */         Files.deleteIfExists(file);
/*     */       } 
/*     */       return FileVisitResult.CONTINUE;
/*     */     }
/*     */     
/*     */     public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/*     */       if (exc != null)
/*     */         throw exc; 
/*     */       if (dir.equals(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path())) {
/*     */         LevelStorageSource.LevelStorageAccess.this.lock.close();
/*     */         Files.deleteIfExists(lockPath);
/*     */       } 
/*     */       Files.deleteIfExists(dir);
/*     */       return FileVisitResult.CONTINUE;
/*     */     }
/*     */   }
/*     */   
/*     */   class null extends SimpleFileVisitor<Path> {
/*     */     public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
/*     */       if (path.endsWith("session.lock"))
/*     */         return FileVisitResult.CONTINUE; 
/*     */       String entryPath = rootPath.resolve(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path().relativize(path)).toString().replace('\\', '/');
/*     */       ZipEntry entry = new ZipEntry(entryPath);
/*     */       stream.putNextEntry(entry);
/*     */       Files.asByteSource(path.toFile()).copyTo(stream);
/*     */       stream.closeEntry();
/*     */       return FileVisitResult.CONTINUE;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class LevelCandidates extends Record implements Iterable<LevelDirectory> {
/*     */     private final List<LevelStorageSource.LevelDirectory> levels;
/*     */     
/*     */     public LevelCandidates(List<LevelStorageSource.LevelDirectory> levels) {
/* 687 */       this.levels = levels;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #687	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #687	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #687	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelCandidates;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<LevelStorageSource.LevelDirectory> levels() {
/* 687 */       return this.levels;
/*     */     }
/*     */     
/*     */     public boolean isEmpty() {
/* 689 */       return this.levels.isEmpty();
/*     */     }
/*     */     
/*     */     public Iterator<LevelStorageSource.LevelDirectory> iterator() {
/* 694 */       return this.levels.iterator();
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class LevelDirectory extends Record {
/*     */     private final Path path;
/*     */     
/*     */     public LevelDirectory(Path path) {
/* 698 */       this.path = path;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #698	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #698	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #698	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/LevelStorageSource$LevelDirectory;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public Path path() {
/* 698 */       return this.path;
/*     */     }
/*     */     
/*     */     public String directoryName() {
/* 700 */       return this.path.getFileName().toString();
/*     */     }
/*     */     
/*     */     public Path dataFile() {
/* 704 */       return resourcePath(LevelResource.LEVEL_DATA_FILE);
/*     */     }
/*     */     
/*     */     public Path oldDataFile() {
/* 708 */       return resourcePath(LevelResource.OLD_LEVEL_DATA_FILE);
/*     */     }
/*     */     
/*     */     public Path corruptedDataFile(ZonedDateTime time) {
/* 712 */       return this.path.resolve(LevelResource.LEVEL_DATA_FILE.id() + "_corrupted_" + LevelResource.LEVEL_DATA_FILE.id());
/*     */     }
/*     */     
/*     */     public Path rawDataFile(ZonedDateTime time) {
/* 716 */       return this.path.resolve(LevelResource.LEVEL_DATA_FILE.id() + "_raw_" + LevelResource.LEVEL_DATA_FILE.id());
/*     */     }
/*     */     
/*     */     public Path iconFile() {
/* 720 */       return resourcePath(LevelResource.ICON_FILE);
/*     */     }
/*     */     
/*     */     public Path lockFile() {
/* 724 */       return resourcePath(LevelResource.LOCK_FILE);
/*     */     }
/*     */     
/*     */     public Path resourcePath(LevelResource resource) {
/* 728 */       return this.path.resolve(resource.id());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelStorageSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */