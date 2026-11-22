/*     */ package net.minecraft.world.level.chunk.storage;
/*     */ 
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.nbt.NbtUtils;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class SimpleRegionStorage implements AutoCloseable {
/*     */   private final IOWorker worker;
/*     */   
/*     */   private final DataFixer fixerUpper;
/*     */   
/*     */   private final DataFixTypes dataFixType;
/*     */   
/*     */   public SimpleRegionStorage(RegionStorageInfo info, Path folder, DataFixer fixerUpper, boolean syncWrites, DataFixTypes dataFixType) {
/*  29 */     this.fixerUpper = fixerUpper;
/*  30 */     this.dataFixType = dataFixType;
/*  31 */     this.worker = new IOWorker(info, folder, syncWrites);
/*     */   }
/*     */   
/*     */   public boolean isOldChunkAround(ChunkPos pos, int range) {
/*  35 */     return this.worker.isOldChunkAround(pos, range);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Optional<CompoundTag>> read(ChunkPos pos) {
/*  39 */     return this.worker.loadAsync(pos);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> write(ChunkPos pos, CompoundTag value) {
/*  43 */     return write(pos, () -> value);
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> write(ChunkPos pos, Supplier<CompoundTag> supplier) {
/*  47 */     return this.worker.store(pos, supplier);
/*     */   }
/*     */   
/*     */   public CompoundTag upgradeChunkTag(CompoundTag chunkTag, int defaultVersion, CompoundTag dataFixContextTag, int targetVersion) {
/*  51 */     int version = NbtUtils.getDataVersion(chunkTag, defaultVersion);
/*  52 */     if (version >= targetVersion)
/*  53 */       return chunkTag; 
/*     */     try {
/*  58 */       injectDatafixingContext(chunkTag, dataFixContextTag);
/*  59 */       chunkTag = this.dataFixType.update(this.fixerUpper, chunkTag, version, targetVersion);
/*  60 */       removeDatafixingContext(chunkTag);
/*  63 */       NbtUtils.addDataVersion(chunkTag, targetVersion);
/*  65 */       return chunkTag;
/*  66 */     } catch (Exception e) {
/*  67 */       CrashReport report = CrashReport.forThrowable(e, "Updated chunk");
/*  68 */       CrashReportCategory details = report.addCategory("Updated chunk details");
/*  69 */       details.setDetail("Data version", Integer.valueOf(version));
/*  70 */       details.setDetail("Target version", Integer.valueOf(targetVersion));
/*  71 */       throw new ReportedException(report);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompoundTag upgradeChunkTag(CompoundTag chunkTag, int defaultVersion) {
/*  76 */     return upgradeChunkTag(chunkTag, defaultVersion, null, SharedConstants.getCurrentVersion().dataVersion().version());
/*     */   }
/*     */   
/*     */   public Dynamic<Tag> upgradeChunkTag(Dynamic<Tag> chunkTag, int defaultVersion) {
/*  80 */     return new Dynamic(chunkTag.getOps(), upgradeChunkTag((CompoundTag)chunkTag.getValue(), defaultVersion, null, SharedConstants.getCurrentVersion().dataVersion().version()));
/*     */   }
/*     */   
/*     */   public static void injectDatafixingContext(CompoundTag chunkTag, CompoundTag contextTag) {
/*  84 */     if (contextTag != null)
/*  85 */       chunkTag.put("__context", (Tag)contextTag); 
/*     */   }
/*     */   
/*     */   private static void removeDatafixingContext(CompoundTag chunkTag) {
/*  90 */     chunkTag.remove("__context");
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> synchronize(boolean flush) {
/*  94 */     return this.worker.synchronize(flush);
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/*  99 */     this.worker.close();
/*     */   }
/*     */   
/*     */   public ChunkScanAccess chunkScanner() {
/* 103 */     return this.worker;
/*     */   }
/*     */   
/*     */   public RegionStorageInfo storageInfo() {
/* 107 */     return this.worker.storageInfo();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\storage\SimpleRegionStorage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */