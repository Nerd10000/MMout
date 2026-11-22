/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileVisitResult;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.SimpleFileVisitor;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.time.Instant;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtIo;
/*     */ import net.minecraft.nbt.NbtOps;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.DirectoryLock;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.dimension.DimensionType;
/*     */ import org.apache.commons.io.function.IORunnable;
/*     */ 
/*     */ public class LevelStorageAccess implements AutoCloseable {
/*     */   private DirectoryLock lock;
/*     */   
/*     */   private final LevelStorageSource.LevelDirectory levelDirectory;
/*     */   
/*     */   private final String levelId;
/*     */   
/* 403 */   private final Map<LevelResource, Path> resources = Maps.newHashMap();
/*     */   
/*     */   private LevelStorageAccess(String levelId, Path path) throws IOException {
/* 406 */     this.levelId = levelId;
/* 407 */     this.levelDirectory = new LevelStorageSource.LevelDirectory(path);
/* 408 */     createLock();
/*     */   }
/*     */   
/*     */   private void createLock() throws IOException {
/* 412 */     this.lock = DirectoryLock.create(this.levelDirectory.path);
/*     */   }
/*     */   
/*     */   public void releaseTemporarilyAndRun(IORunnable runnable) throws IOException {
/* 416 */     close();
/*     */     try {
/* 418 */       runnable.run();
/*     */     } finally {
/* 420 */       createLock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public long estimateDiskSpace() {
/*     */     try {
/* 426 */       return Files.getFileStore(this.levelDirectory.path).getUsableSpace();
/* 427 */     } catch (Exception ignored) {
/* 429 */       return Long.MAX_VALUE;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean checkForLowDiskSpace() {
/* 434 */     return (estimateDiskSpace() < 67108864L);
/*     */   }
/*     */   
/*     */   public void safeClose() {
/*     */     try {
/* 439 */       close();
/* 440 */     } catch (IOException e) {
/* 441 */       LevelStorageSource.LOGGER.warn("Failed to unlock access to level {}", getLevelId(), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public LevelStorageSource parent() {
/* 446 */     return LevelStorageSource.this;
/*     */   }
/*     */   
/*     */   public LevelStorageSource.LevelDirectory getLevelDirectory() {
/* 450 */     return this.levelDirectory;
/*     */   }
/*     */   
/*     */   public String getLevelId() {
/* 454 */     return this.levelId;
/*     */   }
/*     */   
/*     */   public Path getLevelPath(LevelResource resource) {
/* 458 */     Objects.requireNonNull(this.levelDirectory);
/* 458 */     return this.resources.computeIfAbsent(resource, this.levelDirectory::resourcePath);
/*     */   }
/*     */   
/*     */   public Path getDimensionPath(ResourceKey<Level> name) {
/* 462 */     return DimensionType.getStorageFolder(name, this.levelDirectory.path());
/*     */   }
/*     */   
/*     */   private void checkLock() {
/* 466 */     if (!this.lock.isValid())
/* 467 */       throw new IllegalStateException("Lock is no longer valid"); 
/*     */   }
/*     */   
/*     */   public PlayerDataStorage createPlayerStorage() {
/* 472 */     checkLock();
/* 473 */     return new PlayerDataStorage(this, LevelStorageSource.this.fixerUpper);
/*     */   }
/*     */   
/*     */   public void collectIssues(boolean useFallback) throws IOException {
/* 477 */     checkLock();
/* 479 */     Dynamic<?> unfixedDataTag = getUnfixedDataTag(useFallback);
/* 480 */     int dataVersion = NbtUtils.getDataVersion(unfixedDataTag);
/* 481 */     Dynamic<?> fixedDataTag = DataFixTypes.LEVEL.updateToCurrentVersion(LevelStorageSource.this.fixerUpper, unfixedDataTag, dataVersion);
/* 482 */     LevelStorageSource.this.makeLevelSummary(fixedDataTag, this.levelDirectory, false, dataVersion);
/*     */   }
/*     */   
/*     */   public LevelSummary fixAndGetSummary() throws IOException {
/* 486 */     checkLock();
/* 487 */     return fixAndGetSummaryFromTag(getUnfixedDataTag(false));
/*     */   }
/*     */   
/*     */   public LevelSummary fixAndGetSummaryFromTag(Dynamic<?> dataTag) {
/* 491 */     checkLock();
/* 492 */     int dataVersion = NbtUtils.getDataVersion(dataTag);
/* 493 */     Dynamic<?> dataTagFixed = DataFixTypes.LEVEL_SUMMARY.updateToCurrentVersion(LevelStorageSource.this.fixerUpper, dataTag, dataVersion);
/* 494 */     return LevelStorageSource.this.makeLevelSummary(dataTagFixed, this.levelDirectory, false, dataVersion);
/*     */   }
/*     */   
/*     */   public Dynamic<?> getUnfixedDataTagWithFallback() throws IOException {
/*     */     Dynamic<?> unfixedDataTag;
/*     */     try {
/* 500 */       unfixedDataTag = getUnfixedDataTag(false);
/* 501 */     } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 502 */       LevelStorageSource.LOGGER.warn("Failed to load world data from {}", this.levelDirectory.dataFile(), e);
/* 503 */       LevelStorageSource.LOGGER.info("Attempting to use fallback {}", this.levelDirectory.oldDataFile());
/* 504 */       unfixedDataTag = getUnfixedDataTag(true);
/* 505 */       restoreLevelDataFromOld();
/*     */     } 
/* 507 */     return unfixedDataTag;
/*     */   }
/*     */   
/*     */   public Dynamic<?> getUnfixedDataTag(boolean useFallback) throws IOException {
/* 511 */     checkLock();
/* 512 */     Path dataFile = getDataFile(useFallback);
/* 513 */     CompoundTag root = LevelStorageSource.readLevelDataTagRaw(dataFile);
/* 514 */     return new Dynamic((DynamicOps)NbtOps.INSTANCE, root.getCompoundOrEmpty("Data"));
/*     */   }
/*     */   
/*     */   private Path getDataFile(boolean useFallback) {
/* 518 */     return useFallback ? this.levelDirectory.oldDataFile() : this.levelDirectory.dataFile();
/*     */   }
/*     */   
/*     */   public void saveDataTag(WorldData levelData) {
/* 522 */     saveDataTag(levelData, null);
/*     */   }
/*     */   
/*     */   public void saveDataTag(WorldData levelData, UUID singleplayerUUID) {
/* 526 */     CompoundTag dataTag = levelData.createTag(singleplayerUUID);
/* 528 */     CompoundTag root = new CompoundTag();
/* 529 */     root.put("Data", (Tag)dataTag);
/* 531 */     saveLevelData(root);
/*     */   }
/*     */   
/*     */   public void saveLevelData(Dynamic<?> tag) {
/* 535 */     Tag genericTag = (Tag)tag.convert((DynamicOps)NbtOps.INSTANCE).getValue();
/* 536 */     CompoundTag root = new CompoundTag();
/* 537 */     root.put("Data", genericTag);
/* 539 */     saveLevelData(root);
/*     */   }
/*     */   
/*     */   private void saveLevelData(CompoundTag root) {
/* 543 */     Path worldDir = this.levelDirectory.path();
/*     */     try {
/* 545 */       Path dataFile = Files.createTempFile(worldDir, "level", ".dat", (FileAttribute<?>[])new FileAttribute[0]);
/* 546 */       NbtIo.writeCompressed(root, dataFile);
/* 548 */       Path oldDataFile = this.levelDirectory.oldDataFile();
/* 549 */       Path currentFile = this.levelDirectory.dataFile();
/* 550 */       Util.safeReplaceFile(currentFile, dataFile, oldDataFile);
/* 551 */     } catch (Exception e) {
/* 552 */       LevelStorageSource.LOGGER.error("Failed to save level {}", worldDir, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Optional<Path> getIconFile() {
/* 557 */     if (!this.lock.isValid())
/* 558 */       return Optional.empty(); 
/* 560 */     return Optional.of(this.levelDirectory.iconFile());
/*     */   }
/*     */   
/*     */   public void deleteLevel() throws IOException {
/* 564 */     checkLock();
/* 566 */     final Path lockPath = this.levelDirectory.lockFile();
/* 568 */     LevelStorageSource.LOGGER.info("Deleting level {}", this.levelId);
/* 569 */     for (int attempt = 1; attempt <= 5; attempt++) {
/* 570 */       LevelStorageSource.LOGGER.info("Attempt {}...", Integer.valueOf(attempt));
/*     */       try {
/* 573 */         Files.walkFileTree(this.levelDirectory.path(), new SimpleFileVisitor<Path>() {
/*     */               public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
/* 576 */                 if (!file.equals(lockPath)) {
/* 577 */                   LevelStorageSource.LOGGER.debug("Deleting {}", file);
/* 581 */                   Files.deleteIfExists(file);
/*     */                 } 
/* 583 */                 return FileVisitResult.CONTINUE;
/*     */               }
/*     */               
/*     */               public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
/* 588 */                 if (exc != null)
/* 589 */                   throw exc; 
/* 592 */                 if (dir.equals(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path())) {
/* 594 */                   LevelStorageSource.LevelStorageAccess.this.lock.close();
/* 595 */                   Files.deleteIfExists(lockPath);
/*     */                 } 
/* 597 */                 Files.deleteIfExists(dir);
/* 598 */                 return FileVisitResult.CONTINUE;
/*     */               }
/*     */             });
/*     */         break;
/* 602 */       } catch (IOException e) {
/* 603 */         if (attempt < 5) {
/* 604 */           LevelStorageSource.LOGGER.warn("Failed to delete {}", this.levelDirectory.path(), e);
/*     */           try {
/* 606 */             Thread.sleep(500L);
/* 607 */           } catch (InterruptedException interruptedException) {}
/*     */         } else {
/* 610 */           throw e;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renameLevel(String newName) throws IOException {
/* 617 */     modifyLevelDataWithoutDatafix(tag -> tag.putString("LevelName", newName.trim()));
/*     */   }
/*     */   
/*     */   public void renameAndDropPlayer(String newName) throws IOException {
/* 621 */     modifyLevelDataWithoutDatafix(tag -> {
/*     */           tag.putString("LevelName", newName.trim());
/*     */           tag.remove("singleplayer_uuid");
/*     */         });
/*     */   }
/*     */   
/*     */   private void modifyLevelDataWithoutDatafix(Consumer<CompoundTag> updater) throws IOException {
/* 628 */     checkLock();
/* 630 */     CompoundTag root = LevelStorageSource.readLevelDataTagRaw(this.levelDirectory.dataFile());
/* 631 */     updater.accept(root.getCompoundOrEmpty("Data"));
/* 632 */     saveLevelData(root);
/*     */   }
/*     */   
/*     */   public long makeWorldBackup() throws IOException {
/* 636 */     checkLock();
/* 637 */     String zipFilePrefix = FileNameDateFormatter.FORMATTER.format(ZonedDateTime.now()) + "_" + FileNameDateFormatter.FORMATTER.format(ZonedDateTime.now());
/* 639 */     Path root = LevelStorageSource.this.getBackupPath();
/*     */     try {
/* 641 */       FileUtil.createDirectoriesSafe(root);
/* 642 */     } catch (IOException e) {
/* 643 */       throw new RuntimeException(e);
/*     */     } 
/* 645 */     Path zipFilePath = root.resolve(FileUtil.findAvailableName(root, zipFilePrefix, ".zip"));
/* 647 */     final ZipOutputStream stream = new ZipOutputStream(new BufferedOutputStream(Files.newOutputStream(zipFilePath, new java.nio.file.OpenOption[0])));
/*     */     try {
/* 648 */       final Path rootPath = Paths.get(this.levelId, new String[0]);
/* 650 */       Files.walkFileTree(this.levelDirectory.path(), new SimpleFileVisitor<Path>() {
/*     */             public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
/* 653 */               if (path.endsWith("session.lock"))
/* 654 */                 return FileVisitResult.CONTINUE; 
/* 656 */               String entryPath = rootPath.resolve(LevelStorageSource.LevelStorageAccess.this.levelDirectory.path().relativize(path)).toString().replace('\\', '/');
/* 657 */               ZipEntry entry = new ZipEntry(entryPath);
/* 658 */               stream.putNextEntry(entry);
/* 659 */               Files.asByteSource(path.toFile()).copyTo(stream);
/* 660 */               stream.closeEntry();
/* 661 */               return FileVisitResult.CONTINUE;
/*     */             }
/*     */           });
/* 664 */       stream.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         stream.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/* 666 */     return Files.size(zipFilePath);
/*     */   }
/*     */   
/*     */   public boolean hasWorldData() {
/* 670 */     return (Files.exists(this.levelDirectory.dataFile(), new java.nio.file.LinkOption[0]) || Files.exists(this.levelDirectory.oldDataFile(), new java.nio.file.LinkOption[0]));
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 675 */     this.lock.close();
/*     */   }
/*     */   
/*     */   public boolean restoreLevelDataFromOld() {
/* 679 */     return Util.safeReplaceOrMoveFile(this.levelDirectory.dataFile(), this.levelDirectory.oldDataFile(), this.levelDirectory.corruptedDataFile(ZonedDateTime.now()), true);
/*     */   }
/*     */   
/*     */   public Instant getFileModificationTime(boolean fallback) {
/* 683 */     return LevelStorageSource.getFileModificationTime(getDataFile(fallback));
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelStorageSource$LevelStorageAccess.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */