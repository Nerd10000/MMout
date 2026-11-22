/*     */ package net.minecraft.util.worldupdate;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2FloatMap;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2FloatMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Reference2FloatOpenHashMap;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.datafix.DataFixTypes;
/*     */ import net.minecraft.world.level.Level;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class UpgradeProgress {
/*  21 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private volatile boolean finished;
/*     */   
/*  23 */   private final FileFixStats totalFileFixStats = new FileFixStats();
/*     */   
/*  24 */   private final FileFixStats typeFileFixStats = new FileFixStats();
/*     */   
/*  25 */   private final FileFixStats runningFileFixerStats = new FileFixStats();
/*     */   
/*     */   private volatile float totalProgress;
/*     */   
/*  27 */   private final AtomicInteger totalChunks = new AtomicInteger();
/*     */   
/*  28 */   private final AtomicInteger converted = new AtomicInteger();
/*     */   
/*  29 */   private final AtomicInteger skipped = new AtomicInteger();
/*     */   
/*  30 */   private final Reference2FloatMap<ResourceKey<Level>> progressMap = Reference2FloatMaps.synchronize((Reference2FloatMap)new Reference2FloatOpenHashMap());
/*     */   
/*     */   private volatile boolean canceled = false;
/*     */   
/*     */   private volatile DataFixTypes dataFixType;
/*     */   
/*  33 */   private volatile Status status = Status.COUNTING;
/*     */   
/*     */   private volatile Type type;
/*     */   
/*  35 */   private final AtomicLong lastLoggedProgressTime = new AtomicLong();
/*     */   
/*     */   public enum Status {
/*  38 */     COUNTING, FAILED, FINISHED, UPGRADING;
/*     */   }
/*     */   
/*     */   public enum Type implements StringRepresentable {
/*  42 */     FILES("files"),
/*  43 */     LEGACY_STRUCTURES("legacy_structures"),
/*  44 */     REGIONS("regions");
/*     */     
/*     */     private final String id;
/*     */     
/*     */     private final Component label;
/*     */     
/*     */     Type(String id) {
/*  51 */       this.id = id;
/*  52 */       this.label = (Component)Component.translatable("upgradeWorld.progress.type." + getSerializedName());
/*     */     }
/*     */     
/*     */     public String getSerializedName() {
/*  57 */       return this.id;
/*     */     }
/*     */     
/*     */     public Component label() {
/*  61 */       return this.label;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FileFixStats {
/*  66 */     private final AtomicInteger finishedOperations = new AtomicInteger();
/*     */     
/*  67 */     private final AtomicInteger totalOperations = new AtomicInteger();
/*     */     
/*     */     public int finishedOperations() {
/*  70 */       return this.finishedOperations.get();
/*     */     }
/*     */     
/*     */     public int totalOperations() {
/*  74 */       return this.totalOperations.get();
/*     */     }
/*     */     
/*     */     public void reset() {
/*  78 */       this.finishedOperations.set(0);
/*  79 */       this.totalOperations.set(0);
/*     */     }
/*     */     
/*     */     public float getProgress() {
/*  83 */       if (totalOperations() == 0)
/*  84 */         return 0.0F; 
/*  86 */       return Mth.clamp(finishedOperations() / totalOperations(), 0.0F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isFinished() {
/*  91 */     return this.finished;
/*     */   }
/*     */   
/*     */   public void setFinished(boolean finished) {
/*  95 */     this.finished = finished;
/*     */   }
/*     */   
/*     */   public FileFixStats getTotalFileFixStats() {
/*  99 */     return this.totalFileFixStats;
/*     */   }
/*     */   
/*     */   public FileFixStats getTypeFileFixStats() {
/* 103 */     return this.typeFileFixStats;
/*     */   }
/*     */   
/*     */   public FileFixStats getRunningFileFixerStats() {
/* 107 */     return this.runningFileFixerStats;
/*     */   }
/*     */   
/*     */   public void addTotalFileFixOperations(int additionalFileFixOperations) {
/* 111 */     this.totalFileFixStats.totalOperations.addAndGet(additionalFileFixOperations);
/* 112 */     this.typeFileFixStats.totalOperations.addAndGet(additionalFileFixOperations);
/*     */   }
/*     */   
/*     */   public float getTotalProgress() {
/* 116 */     return this.totalProgress;
/*     */   }
/*     */   
/*     */   public void setTotalProgress(float totalProgress) {
/* 120 */     this.totalProgress = totalProgress;
/*     */   }
/*     */   
/*     */   public int getTotalChunks() {
/* 124 */     return this.totalChunks.get();
/*     */   }
/*     */   
/*     */   public void addTotalChunks(int additionalTotalChunks) {
/* 128 */     this.totalChunks.addAndGet(additionalTotalChunks);
/*     */   }
/*     */   
/*     */   public int getConverted() {
/* 132 */     return this.converted.get();
/*     */   }
/*     */   
/*     */   public void setDimensionProgress(ResourceKey<Level> dimensionKey, float currentProgress) {
/* 136 */     this.progressMap.put(dimensionKey, currentProgress);
/*     */   }
/*     */   
/*     */   public float getDimensionProgress(ResourceKey<Level> dimensionKey) {
/* 140 */     return this.progressMap.getFloat(dimensionKey);
/*     */   }
/*     */   
/*     */   public void incrementConverted() {
/* 144 */     this.converted.incrementAndGet();
/*     */   }
/*     */   
/*     */   public int getSkipped() {
/* 148 */     return this.skipped.get();
/*     */   }
/*     */   
/*     */   public void incrementSkipped() {
/* 152 */     this.skipped.incrementAndGet();
/*     */   }
/*     */   
/*     */   public void incrementFinishedOperations() {
/* 156 */     incrementFinishedOperationsBy(1);
/*     */   }
/*     */   
/*     */   public void incrementFinishedOperationsBy(int count) {
/* 160 */     this.totalFileFixStats.finishedOperations.addAndGet(count);
/* 161 */     this.typeFileFixStats.finishedOperations.addAndGet(count);
/* 162 */     logProgress();
/*     */   }
/*     */   
/*     */   public void setCanceled() {
/* 166 */     this.canceled = true;
/*     */   }
/*     */   
/*     */   public boolean isCanceled() {
/* 170 */     return this.canceled;
/*     */   }
/*     */   
/*     */   public Status getStatus() {
/* 174 */     return this.status;
/*     */   }
/*     */   
/*     */   public void setStatus(Status status) {
/* 178 */     this.status = status;
/*     */   }
/*     */   
/*     */   public DataFixTypes getDataFixType() {
/* 182 */     return this.dataFixType;
/*     */   }
/*     */   
/*     */   public void setType(Type type) {
/* 186 */     this.type = type;
/* 187 */     this.typeFileFixStats.reset();
/*     */   }
/*     */   
/*     */   public Type getType() {
/* 191 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setApplicableFixerAmount(int amount) {
/* 195 */     this.runningFileFixerStats.totalOperations.set(amount);
/*     */   }
/*     */   
/*     */   public void incrementRunningFileFixer() {
/* 199 */     this.runningFileFixerStats.finishedOperations.incrementAndGet();
/*     */   }
/*     */   
/*     */   public void reset(DataFixTypes dataFixType) {
/* 203 */     this.totalFileFixStats.reset();
/* 204 */     this.typeFileFixStats.reset();
/* 205 */     this.totalChunks.set(0);
/* 206 */     this.converted.set(0);
/* 207 */     this.skipped.set(0);
/* 208 */     this.dataFixType = dataFixType;
/*     */   }
/*     */   
/*     */   public void logProgress() {
/* 212 */     long now = Util.getMillis();
/* 213 */     if (now >= this.lastLoggedProgressTime.get() + 1000L) {
/* 214 */       float progress = this.totalFileFixStats.finishedOperations() / this.totalFileFixStats.totalOperations();
/* 215 */       this.lastLoggedProgressTime.set(now);
/* 216 */       LOGGER.info("Upgrading progress: {}%", Integer.valueOf((int)(progress * 100.0F)));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static class Noop extends UpgradeProgress {
/*     */     public void setFinished(boolean finished) {}
/*     */     
/*     */     public void addTotalFileFixOperations(int additionalFileFixOperations) {}
/*     */     
/*     */     public void setTotalProgress(float totalProgress) {}
/*     */     
/*     */     public void addTotalChunks(int additionalTotalChunks) {}
/*     */     
/*     */     public void setDimensionProgress(ResourceKey<Level> dimensionKey, float currentProgress) {}
/*     */     
/*     */     public void incrementConverted() {}
/*     */     
/*     */     public void incrementSkipped() {}
/*     */     
/*     */     public void incrementFinishedOperations() {}
/*     */     
/*     */     public void incrementFinishedOperationsBy(int count) {}
/*     */     
/*     */     public void setCanceled() {}
/*     */     
/*     */     public void setStatus(UpgradeProgress.Status status) {}
/*     */     
/*     */     public void setApplicableFixerAmount(int amount) {}
/*     */     
/*     */     public void incrementRunningFileFixer() {}
/*     */     
/*     */     public void reset(DataFixTypes dataFixType) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\worldupdate\UpgradeProgress.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */