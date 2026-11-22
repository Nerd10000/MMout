/*     */ package net.minecraft.util.filefix;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.datafixers.DataFixerBuilder;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.OpenOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.StandardOpenOption;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtAccounter;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.CopyOnWriteFileSystem;
/*     */ import net.minecraft.util.filefix.virtualfilesystem.FileMove;
/*     */ import net.minecraft.util.worldupdate.UpgradeProgress;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ import org.apache.commons.io.file.PathUtils;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FileFixerUpper {
/*     */   private static final int FILE_FIXER_INTRODUCTION_VERSION = 4772;
/*     */   
/*  52 */   private static final Gson GSON = (new Gson()).newBuilder().setPrettyPrinting().create();
/*     */   
/*     */   private static final String FILE_FIX_DIRECTORY_NAME = "filefix";
/*     */   
/*     */   private static final String NEW_WORLD_TEMP_NAME = "new_world";
/*     */   
/*     */   private static final String UPGRADE_IN_PROGRESS_NAME = "upgrade_in_progress.json";
/*     */   
/*  56 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final DataFixerBuilder.Result dataFixer;
/*     */   
/*     */   private final List<FileFix> fileFixes;
/*     */   
/*     */   private final int latestFileFixerVersion;
/*     */   
/*     */   public FileFixerUpper(DataFixerBuilder.Result dataFixer, List<FileFix> fileFixes, int latestFileFixerVersion) {
/*  62 */     this.dataFixer = dataFixer;
/*  63 */     this.fileFixes = List.copyOf(fileFixes);
/*  64 */     this.latestFileFixerVersion = latestFileFixerVersion;
/*     */   }
/*     */   
/*     */   public static int worldVersionToFileFixerVersion(int levelDataVersion) {
/*  68 */     if (levelDataVersion < 4772)
/*  70 */       return 0; 
/*  72 */     return levelDataVersion;
/*     */   }
/*     */   
/*     */   public boolean requiresFileFixing(int levelDataVersion) {
/*  76 */     return (worldVersionToFileFixerVersion(levelDataVersion) < this.latestFileFixerVersion);
/*     */   }
/*     */   
/*     */   public Dynamic<?> fix(LevelStorageSource.LevelStorageAccess worldAccess, Dynamic<?> levelDataTag, UpgradeProgress upgradeProgress) throws FileFixException {
/*  80 */     return fix(worldAccess, levelDataTag, upgradeProgress, SharedConstants.getCurrentVersion().dataVersion().version());
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public Dynamic<?> fix(LevelStorageSource.LevelStorageAccess worldAccess, Dynamic<?> levelDataTag, UpgradeProgress upgradeProgress, int toVersion) throws FileFixException {
/*  85 */     int loadedVersion = NbtUtils.getDataVersion(levelDataTag);
/*  86 */     if (requiresFileFixing(loadedVersion)) {
/*     */       List<FileMove> moves;
/*  87 */       LOGGER.info("Starting upgrade for world \"{}\"", worldAccess.getLevelId());
/*  88 */       Path worldFolder = worldAccess.getLevelDirectory().path();
/*  89 */       Path fileFixDirectory = worldFolder.resolve("filefix");
/*  90 */       Path tempWorld = fileFixDirectory.resolve("new_world");
/*     */       try {
/*  93 */         moves = startOrContinueFileFixing(upgradeProgress, toVersion, worldFolder, tempWorld, fileFixDirectory, loadedVersion);
/*  94 */       } catch (IOException e) {
/*  95 */         throw new AbortedFileFixException(e);
/*     */       } 
/*     */       try {
/*  98 */         swapInFixedWorld(worldAccess, moves, fileFixDirectory, tempWorld);
/*  99 */       } catch (AbortedFileFixException e) {
/* 100 */         if (e.notRevertedMoves().isEmpty())
/* 102 */           cleanup(fileFixDirectory); 
/* 104 */         throw e;
/*     */       } 
/*     */       try {
/* 107 */         levelDataTag = worldAccess.getUnfixedDataTag(false);
/* 108 */       } catch (IOException e) {
/* 109 */         throw new UncheckedIOException(e);
/*     */       } 
/* 111 */       loadedVersion = NbtUtils.getDataVersion(levelDataTag);
/*     */     } 
/* 113 */     Dynamic<?> fixedLevelDataTag = DataFixTypes.LEVEL.updateToCurrentVersion(this.dataFixer.fixer(), levelDataTag, loadedVersion);
/* 114 */     return addVersionsToLevelData(fixedLevelDataTag, toVersion);
/*     */   }
/*     */   
/*     */   private List<FileMove> startOrContinueFileFixing(UpgradeProgress upgradeProgress, int toVersion, Path worldFolder, Path tempWorld, Path fileFixDirectory, int loadedVersion) throws IOException {
/*     */     List<FileMove> moves;
/* 118 */     Path upgradeInProgressFile = fileFixDirectory.resolve("upgrade_in_progress.json");
/* 120 */     if (Files.exists(upgradeInProgressFile, new java.nio.file.LinkOption[0])) {
/* 121 */       LOGGER.warn("Found previously interrupted world upgrade, attempting to continue it");
/* 122 */       moves = readMoves(worldFolder, tempWorld, upgradeInProgressFile);
/*     */     } else {
/* 124 */       if (Files.exists(fileFixDirectory, new java.nio.file.LinkOption[0]))
/* 125 */         deleteDirectory(fileFixDirectory); 
/*     */       try {
/* 128 */         Files.createDirectory(fileFixDirectory, (FileAttribute<?>[])new FileAttribute[0]);
/* 129 */         moves = applyFileFixersOnCow(upgradeProgress, loadedVersion, toVersion, worldFolder, fileFixDirectory, tempWorld);
/* 130 */       } catch (Exception e) {
/* 131 */         cleanup(fileFixDirectory);
/* 132 */         throw e;
/*     */       } 
/*     */     } 
/* 135 */     return moves;
/*     */   }
/*     */   
/*     */   private static void deleteDirectory(Path directory) throws IOException {
/* 143 */     Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
/*     */           public FileVisitResult visitFile(Path realPath, BasicFileAttributes attrs) {
/*     */             try {
/* 147 */               Files.deleteIfExists(realPath);
/* 148 */             } catch (IOException iOException) {}
/* 150 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */           
/*     */           public FileVisitResult postVisitDirectory(Path realPath, IOException e) {
/*     */             try {
/* 156 */               Files.deleteIfExists(realPath);
/* 157 */             } catch (IOException iOException) {}
/* 159 */             return FileVisitResult.CONTINUE;
/*     */           }
/*     */         });
/* 162 */     if (Files.exists(directory, new java.nio.file.LinkOption[0]))
/* 163 */       PathUtils.deleteDirectory(directory); 
/*     */   }
/*     */   
/*     */   private static void cleanup(Path fileFixDirectory) {
/*     */     try {
/* 169 */       deleteDirectory(fileFixDirectory);
/* 170 */     } catch (Exception ex) {
/* 171 */       LOGGER.error("Failed to clean up", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<FileMove> applyFileFixersOnCow(UpgradeProgress upgradeProgress, int loadedVersion, int toVersion, Path worldFolder, Path fileFixDirectory, Path tempWorld) throws IOException {
/* 176 */     Objects.requireNonNull(fileFixDirectory);
/* 176 */     CopyOnWriteFileSystem fs = CopyOnWriteFileSystem.create(worldFolder.getFileName().toString(), worldFolder, fileFixDirectory.resolve("cow"), fileFixDirectory::equals);
/*     */     try {
/* 177 */       applyFileFixers(upgradeProgress, loadedVersion, toVersion, (Path)fs.rootPath());
/* 179 */       CopyOnWriteFileSystem.Moves moves = fs.collectMoveOperations(tempWorld);
/* 181 */       Files.createDirectory(tempWorld, (FileAttribute<?>[])new FileAttribute[0]);
/* 182 */       CopyOnWriteFileSystem.createDirectories(moves.directories());
/* 183 */       CopyOnWriteFileSystem.moveFiles(moves.copiedFiles());
/* 185 */       List<FileMove> list = moves.preexistingFiles();
/* 186 */       if (fs != null)
/* 186 */         fs.close(); 
/*     */       return list;
/*     */     } catch (Throwable throwable) {
/*     */       if (fs != null)
/*     */         try {
/*     */           fs.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void applyFileFixers(UpgradeProgress upgradeProgress, int loadedVersion, int toVersion, Path basePath) throws IOException {
/* 191 */     List<FileFix> applicableFixers = getApplicableFixers(loadedVersion, toVersion);
/* 192 */     upgradeProgress.setType(UpgradeProgress.Type.FILES);
/* 193 */     countFiles(applicableFixers, upgradeProgress);
/* 194 */     upgradeProgress.setStatus(UpgradeProgress.Status.UPGRADING);
/* 195 */     upgradeProgress.setApplicableFixerAmount(applicableFixers.size());
/* 197 */     for (FileFix fileFix : applicableFixers) {
/* 198 */       upgradeProgress.incrementRunningFileFixer();
/* 199 */       fileFix.runFixOperations(basePath, upgradeProgress);
/*     */     } 
/* 201 */     writeUpdatedLevelData(basePath, toVersion);
/* 202 */     Files.deleteIfExists(basePath.resolve("level.dat_old"));
/* 203 */     Files.deleteIfExists(basePath.resolve("session.lock"));
/*     */   }
/*     */   
/*     */   private List<FileFix> getApplicableFixers(int fromVersion, int toVersion) {
/* 207 */     int fileFixerFromVersion = worldVersionToFileFixerVersion(fromVersion);
/* 208 */     return this.fileFixes.stream()
/* 209 */       .filter(fileFix -> (fileFix.getVersion() > fileFixerFromVersion && fileFix.getVersion() <= toVersion))
/* 210 */       .toList();
/*     */   }
/*     */   
/*     */   private void countFiles(List<FileFix> applicableFixers, UpgradeProgress upgradeProgress) {
/* 214 */     upgradeProgress.setStatus(UpgradeProgress.Status.COUNTING);
/* 215 */     int totalFiles = 0;
/* 216 */     for (FileFix fileFix : applicableFixers)
/* 217 */       totalFiles += fileFix.countFileOperations(); 
/* 219 */     upgradeProgress.addTotalFileFixOperations(totalFiles);
/*     */   }
/*     */   
/*     */   private void writeUpdatedLevelData(Path worldFolder, int toVersion) throws IOException {
/* 223 */     Path levelDatPath = worldFolder.resolve(LevelResource.LEVEL_DATA_FILE.id());
/* 224 */     CompoundTag unfixedLevelDat = NbtIo.readCompressed(levelDatPath, NbtAccounter.defaultQuota());
/* 225 */     CompoundTag unfixedDataTag = unfixedLevelDat.getCompoundOrEmpty("Data");
/* 226 */     int dataVersion = NbtUtils.getDataVersion(unfixedDataTag);
/* 228 */     Dynamic<?> fixed = DataFixTypes.LEVEL.update(this.dataFixer.fixer(), new Dynamic((DynamicOps)NbtOps.INSTANCE, unfixedDataTag), dataVersion, toVersion);
/* 229 */     fixed = addVersionsToLevelData(fixed, toVersion);
/* 231 */     Dynamic<?> dynamic = fixed.emptyMap().set("Data", fixed);
/* 232 */     NbtIo.writeCompressed((CompoundTag)dynamic.convert((DynamicOps)NbtOps.INSTANCE).getValue(), levelDatPath);
/*     */   }
/*     */   
/*     */   private static Dynamic<?> addVersionsToLevelData(Dynamic<?> fixed, int toVersion) {
/* 236 */     fixed = NbtUtils.addDataVersion(fixed, toVersion);
/* 237 */     fixed = PrimaryLevelData.writeLastPlayed(fixed);
/* 238 */     fixed = PrimaryLevelData.writeVersionTag(fixed);
/* 239 */     return fixed;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   protected static void swapInFixedWorld(LevelStorageSource.LevelStorageAccess worldAccess, List<FileMove> moves, Path fileFixDirectory, Path tempWorld) throws FileFixException {
/*     */     Path tempWorldTopLevel, oldWorldFolder;
/*     */     FileSystemCapabilities fileSystemCapabilities;
/* 244 */     Path worldFolder = worldAccess.getLevelDirectory().path();
/* 245 */     Path savesDirectory = worldFolder.getParent();
/* 246 */     String worldName = worldFolder.getFileName().toString();
/*     */     try {
/* 251 */       fileSystemCapabilities = detectFileSystemCapabilities(fileFixDirectory);
/* 252 */       tempWorldTopLevel = savesDirectory.resolve(FileUtil.findAvailableName(savesDirectory, worldName + " upgraded", ""));
/* 253 */       oldWorldFolder = savesDirectory.resolve(FileUtil.findAvailableName(savesDirectory, worldName + " OUTDATED", ""));
/* 254 */     } catch (Exception e) {
/* 255 */       throw new AbortedFileFixException(e);
/*     */     } 
/* 257 */     if (!fileSystemCapabilities.atomicMove())
/* 258 */       throw new AtomicMoveNotSupportedFileFixException(fileSystemCapabilities); 
/* 260 */     CopyOption[] moveOptions = fileSystemCapabilities.getMoveOptions();
/* 261 */     LOGGER.info("File system capabilities: {}", fileSystemCapabilities);
/* 262 */     Path movesFile = fileFixDirectory.resolve("upgrade_in_progress.json");
/*     */     try {
/* 264 */       if (fileSystemCapabilities.hardLinks()) {
/* 265 */         CopyOnWriteFileSystem.hardLinkFiles(moves);
/*     */       } else {
/* 267 */         writeMoves(moves, worldFolder, tempWorld, movesFile);
/*     */       } 
/* 269 */     } catch (Exception e) {
/* 270 */       throw new AbortedFileFixException(e, List.of(), fileSystemCapabilities);
/*     */     } 
/* 273 */     LOGGER.info("Applying file structure changes for world \"{}\"", worldAccess.getLevelId());
/* 274 */     if (fileSystemCapabilities.hardLinks()) {
/*     */       try {
/* 276 */         LOGGER.info("Moving new hardlinked world to top level");
/* 277 */         Files.move(tempWorld, tempWorldTopLevel, moveOptions);
/* 278 */       } catch (Exception e) {
/* 279 */         LOGGER.error("Encountered error trying to move world folder:", e);
/* 280 */         throw new AbortedFileFixException(e, List.of(), fileSystemCapabilities);
/*     */       } 
/*     */     } else {
/*     */       try {
/* 284 */         LOGGER.info("Moving files into new file structure");
/* 285 */         CopyOnWriteFileSystem.moveFilesWithRetry(moves, moveOptions);
/* 286 */         LOGGER.info("Moving new world to top level");
/* 287 */         Files.move(tempWorld, tempWorldTopLevel, moveOptions);
/* 288 */       } catch (Exception e) {
/* 289 */         LOGGER.error("Encountered error while trying to create new world folder:", e);
/* 290 */         List<FileMove> failedMoves = CopyOnWriteFileSystem.tryRevertMoves(moves, moveOptions);
/* 291 */         if (failedMoves.isEmpty())
/*     */           try {
/* 293 */             Files.deleteIfExists(movesFile);
/* 294 */           } catch (IOException e2) {
/* 295 */             LOGGER.warn("Failed to delete {}", movesFile, e);
/*     */           }  
/* 298 */         throw new AbortedFileFixException(e, failedMoves, fileSystemCapabilities);
/*     */       } 
/* 300 */       LOGGER.info("Complete move");
/*     */       try {
/* 302 */         Files.deleteIfExists(movesFile);
/* 303 */       } catch (IOException e) {
/* 304 */         LOGGER.warn("Failed to delete {}", movesFile, e);
/*     */       } 
/*     */     } 
/* 307 */     LOGGER.info("Start cleanup");
/*     */     try {
/* 310 */       Files.deleteIfExists(worldFolder.resolve("level.dat"));
/* 311 */       Files.deleteIfExists(worldFolder.resolve("level.dat_old"));
/* 312 */     } catch (Exception e) {
/* 313 */       LOGGER.warn("Failed to delete outdated level.dat files: ", e);
/*     */     } 
/* 315 */     MutableBoolean succeeded = new MutableBoolean();
/*     */     try {
/* 317 */       worldAccess.releaseTemporarilyAndRun(() -> {
/*     */             LOGGER.info("Moving out old world folder");
/*     */             try {
/*     */               Files.move(worldFolder, oldWorldFolder, moveOptions);
/* 321 */             } catch (Exception e) {
/*     */               LOGGER.warn("Failed to move outdated world folder out of the way; will try to delete instead: ", e);
/*     */               try {
/*     */                 deleteDirectory(worldFolder);
/* 326 */               } catch (Exception e2) {
/*     */                 LOGGER.warn("Failed to delete outdated world folder: ", e);
/*     */                 throw new FailedCleanupFileFixException(e, tempWorldTopLevel.getFileName().toString(), fileSystemCapabilities);
/*     */               } 
/*     */             } 
/*     */             LOGGER.info("Moving in new world folder");
/*     */             try {
/*     */               Files.move(tempWorldTopLevel, worldFolder, moveOptions);
/* 334 */             } catch (Exception e) {
/*     */               LOGGER.warn("Failed to move in new world folder: ", e);
/*     */               throw new FailedCleanupFileFixException(e, tempWorldTopLevel.getFileName().toString(), fileSystemCapabilities);
/*     */             } 
/*     */             succeeded.setTrue();
/*     */           });
/* 340 */     } catch (IOException e) {
/* 341 */       Path newWorldFolder = succeeded.isTrue() ? worldFolder : tempWorldTopLevel;
/* 342 */       throw new FailedCleanupFileFixException(e, newWorldFolder.getFileName().toString(), fileSystemCapabilities);
/*     */     } 
/* 345 */     LOGGER.info("Done applying file structure changes for world \"{}\". Cleaning up outdated data...", worldAccess.getLevelId());
/*     */     try {
/* 347 */       if (Files.exists(oldWorldFolder, new java.nio.file.LinkOption[0]))
/* 348 */         deleteDirectory(oldWorldFolder); 
/* 350 */     } catch (Exception e) {
/* 351 */       LOGGER.warn("Failed to clean up old world folder", e);
/*     */     } 
/* 353 */     LOGGER.info("Upgrade done for world \"{}\"", worldAccess.getLevelId());
/*     */   }
/*     */   
/*     */   public static final class UpgradeInProgress extends Record {
/*     */     private final List<FileMove> moves;
/*     */     
/*     */     public UpgradeInProgress(List<FileMove> moves) {
/* 356 */       this.moves = moves;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #356	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #356	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #356	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/filefix/FileFixerUpper$UpgradeInProgress;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public List<FileMove> moves() {
/* 356 */       return this.moves;
/*     */     }
/*     */     
/*     */     public static Codec<UpgradeInProgress> codec(Path fromDirectory, Path toDirectory) {
/* 360 */       return RecordCodecBuilder.create(i -> i.group((App)FileMove.moveCodec(fromDirectory, toDirectory).listOf().fieldOf("moves").forGetter(UpgradeInProgress::moves)).apply((Applicative)i, UpgradeInProgress::new));
/*     */     }
/*     */   }
/*     */   
/*     */   private static void writeMoves(List<FileMove> moves, Path fromDirectory, Path toDirectory, Path filePath) throws IOException {
/* 368 */     Codec<UpgradeInProgress> codec = UpgradeInProgress.codec(fromDirectory, toDirectory);
/* 369 */     JsonElement json = (JsonElement)codec.encodeStart((DynamicOps)JsonOps.INSTANCE, new UpgradeInProgress(moves)).getOrThrow();
/* 370 */     Files.writeString(filePath, GSON.toJson(json), new OpenOption[] { StandardOpenOption.DSYNC, StandardOpenOption.CREATE, StandardOpenOption.WRITE });
/*     */   }
/*     */   
/*     */   private static List<FileMove> readMoves(Path fromDirectory, Path toDirectory, Path filePath) throws IOException {
/* 374 */     JsonObject json = (JsonObject)GSON.fromJson(Files.readString(filePath), JsonObject.class);
/* 375 */     Codec<UpgradeInProgress> codec = UpgradeInProgress.codec(fromDirectory, toDirectory);
/* 376 */     return ((UpgradeInProgress)((Pair)codec.decode((DynamicOps)JsonOps.INSTANCE, json).getOrThrow()).getFirst()).moves;
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 380 */     public final List<FileFix> fileFixes = new ArrayList<>();
/*     */     
/*     */     private final int currentVersion;
/*     */     
/*     */     private int latestFileFixerVersion;
/*     */     
/* 383 */     private final List<Schema> knownSchemas = new ArrayList<>();
/*     */     
/*     */     public Builder(int currentVersion) {
/* 386 */       this.currentVersion = currentVersion;
/*     */     }
/*     */     
/*     */     public void addFixer(FileFix fileFix) {
/* 390 */       if (!this.knownSchemas.contains(fileFix.getSchema()))
/* 391 */         throw new IllegalArgumentException("Tried to add file fixer with unknown schema. Add it through FileFixerUpper#addSchema instead"); 
/* 393 */       int fileFixVersion = fileFix.getVersion();
/* 394 */       if (fileFix.getVersion() > this.currentVersion)
/* 395 */         throw new IllegalArgumentException(String.format(Locale.ROOT, "Tried to add too recent file fix for version: %s. The data version of the game is: %s", new Object[] { Integer.valueOf(fileFixVersion), Integer.valueOf(this.currentVersion) })); 
/* 397 */       if (!this.fileFixes.isEmpty()) {
/* 398 */         FileFix last = this.fileFixes.getLast();
/* 399 */         if (last.getVersion() > fileFixVersion)
/* 400 */           throw new IllegalArgumentException(String.format(Locale.ROOT, "Tried to add too recent file fix for version: %s. The most recent file fix version is %s", new Object[] { Integer.valueOf(fileFixVersion), Integer.valueOf(last.getVersion()) })); 
/*     */       } 
/* 403 */       this.fileFixes.add(fileFix);
/*     */     }
/*     */     
/*     */     public Schema addSchema(DataFixerBuilder fixerUpper, int version, BiFunction<Integer, Schema, Schema> factory) {
/* 407 */       this.latestFileFixerVersion = Math.max(version, this.latestFileFixerVersion);
/* 408 */       Schema schema = fixerUpper.addSchema(version, factory);
/* 409 */       this.knownSchemas.add(schema);
/* 410 */       return schema;
/*     */     }
/*     */     
/*     */     public FileFixerUpper build(DataFixerBuilder.Result dataFixer) {
/* 414 */       return new FileFixerUpper(dataFixer, this.fileFixes, this.latestFileFixerVersion);
/*     */     }
/*     */   }
/*     */   
/*     */   public static FileSystemCapabilities detectFileSystemCapabilities(Path dir) throws IOException {
/* 419 */     return new FileSystemCapabilities(supportsAtomicMove(dir), supportsHardLinks(dir));
/*     */   }
/*     */   
/*     */   private static boolean supportsAtomicMove(Path dir) throws IOException {
/* 423 */     Path sourceFile = dir.resolve(UUID.randomUUID().toString());
/* 424 */     Path targetFile = dir.resolve(UUID.randomUUID().toString());
/*     */     try {
/*     */     
/*     */     } finally {
/* 434 */       Files.deleteIfExists(sourceFile);
/* 435 */       Files.deleteIfExists(targetFile);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean supportsHardLinks(Path dir) throws IOException {
/* 440 */     Path sourceFile = dir.resolve(UUID.randomUUID().toString());
/* 441 */     Path targetFile = dir.resolve(UUID.randomUUID().toString());
/*     */     try {
/*     */     
/*     */     } finally {
/* 451 */       Files.deleteIfExists(sourceFile);
/* 452 */       Files.deleteIfExists(targetFile);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\filefix\FileFixerUpper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */