/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.io.IOException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Instant;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.UUIDUtil;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.eventlog.JsonEventLog;
/*     */ import net.minecraft.util.thread.ConsecutiveExecutor;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DownloadQueue implements AutoCloseable {
/*  34 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int MAX_KEPT_PACKS = 20;
/*     */   
/*     */   private final Path cacheDir;
/*     */   
/*     */   private final JsonEventLog<LogEntry> eventLog;
/*     */   
/*     */   private static final class FileInfoEntry extends Record {
/*     */     private final String name;
/*     */     
/*     */     private final long size;
/*     */     
/*     */     public static final Codec<FileInfoEntry> CODEC;
/*     */     
/*     */     private FileInfoEntry(String name, long size) {
/*  38 */       this.name = name;
/*  38 */       this.size = size;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #38	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$FileInfoEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String name() {
/*  38 */       return this.name;
/*     */     }
/*     */     
/*     */     public long size() {
/*  38 */       return this.size;
/*     */     }
/*     */     
/*     */     static {
/*  42 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.fieldOf("name").forGetter(FileInfoEntry::name), (App)Codec.LONG.fieldOf("size").forGetter(FileInfoEntry::size)).apply((Applicative)i, FileInfoEntry::new));
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LogEntry extends Record {
/*     */     private final UUID id;
/*     */     
/*     */     private final String url;
/*     */     
/*     */     private final Instant time;
/*     */     
/*     */     private final Optional<String> hash;
/*     */     
/*     */     private final Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo;
/*     */     
/*     */     public static final Codec<LogEntry> CODEC;
/*     */     
/*     */     private LogEntry(UUID id, String url, Instant time, Optional<String> hash, Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo) {
/*  48 */       this.id = id;
/*  48 */       this.url = url;
/*  48 */       this.time = time;
/*  48 */       this.hash = hash;
/*  48 */       this.errorOrFileInfo = errorOrFileInfo;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$LogEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$LogEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public UUID id() {
/*  48 */       return this.id;
/*     */     }
/*     */     
/*     */     public String url() {
/*  48 */       return this.url;
/*     */     }
/*     */     
/*     */     public Instant time() {
/*  48 */       return this.time;
/*     */     }
/*     */     
/*     */     public Optional<String> hash() {
/*  48 */       return this.hash;
/*     */     }
/*     */     
/*     */     public Either<String, DownloadQueue.FileInfoEntry> errorOrFileInfo() {
/*  48 */       return this.errorOrFileInfo;
/*     */     }
/*     */     
/*     */     static {
/*  55 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)UUIDUtil.STRING_CODEC.fieldOf("id").forGetter(LogEntry::id), (App)Codec.STRING.fieldOf("url").forGetter(LogEntry::url), (App)ExtraCodecs.INSTANT_ISO8601.fieldOf("time").forGetter(LogEntry::time), (App)Codec.STRING.optionalFieldOf("hash").forGetter(LogEntry::hash), (App)Codec.mapEither(Codec.STRING.fieldOf("error"), DownloadQueue.FileInfoEntry.CODEC.fieldOf("file")).forGetter(LogEntry::errorOrFileInfo)).apply((Applicative)i, LogEntry::new));
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class BatchResult extends Record {
/*     */     private final Map<UUID, Path> downloaded;
/*     */     
/*     */     private final Set<UUID> failed;
/*     */     
/*     */     public Set<UUID> failed() {
/*  64 */       return this.failed;
/*     */     }
/*     */     
/*     */     public Map<UUID, Path> downloaded() {
/*  64 */       return this.downloaded;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$BatchResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchResult;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$BatchResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchResult;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$BatchResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #64	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchResult;
/*     */     }
/*     */     
/*     */     public BatchResult(Map<UUID, Path> downloaded, Set<UUID> failed) {
/*  64 */       this.downloaded = downloaded;
/*  64 */       this.failed = failed;
/*     */     }
/*     */     
/*     */     public BatchResult() {
/*  69 */       this(new HashMap<>(), new HashSet<>());
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class DownloadRequest extends Record {
/*     */     private final URL url;
/*     */     
/*     */     private final HashCode hash;
/*     */     
/*     */     public DownloadRequest(URL url, HashCode hash) {
/*  76 */       this.url = url;
/*  76 */       this.hash = hash;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #76	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$DownloadRequest;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public URL url() {
/*  76 */       return this.url;
/*     */     }
/*     */     
/*     */     public HashCode hash() {
/*  76 */       return this.hash;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class BatchConfig extends Record {
/*     */     private final HashFunction hashFunction;
/*     */     
/*     */     private final int maxSize;
/*     */     
/*     */     private final Map<String, String> headers;
/*     */     
/*     */     private final Proxy proxy;
/*     */     
/*     */     private final HttpUtil.DownloadProgressListener listener;
/*     */     
/*     */     public BatchConfig(HashFunction hashFunction, int maxSize, Map<String, String> headers, Proxy proxy, HttpUtil.DownloadProgressListener listener) {
/*  81 */       this.hashFunction = hashFunction;
/*  81 */       this.maxSize = maxSize;
/*  81 */       this.headers = headers;
/*  81 */       this.proxy = proxy;
/*  81 */       this.listener = listener;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #81	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/DownloadQueue$BatchConfig;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public HashFunction hashFunction() {
/*  81 */       return this.hashFunction;
/*     */     }
/*     */     
/*     */     public int maxSize() {
/*  81 */       return this.maxSize;
/*     */     }
/*     */     
/*     */     public Map<String, String> headers() {
/*  81 */       return this.headers;
/*     */     }
/*     */     
/*     */     public Proxy proxy() {
/*  81 */       return this.proxy;
/*     */     }
/*     */     
/*     */     public HttpUtil.DownloadProgressListener listener() {
/*  81 */       return this.listener;
/*     */     }
/*     */   }
/*     */   
/*  91 */   private final ConsecutiveExecutor tasks = new ConsecutiveExecutor((Executor)Util.nonCriticalIoPool(), "download-queue");
/*     */   
/*     */   public DownloadQueue(Path cacheDir) throws IOException {
/*  94 */     this.cacheDir = cacheDir;
/*  95 */     FileUtil.createDirectoriesSafe(cacheDir);
/*  96 */     this.eventLog = JsonEventLog.open(LogEntry.CODEC, cacheDir.resolve("log.json"));
/*  98 */     DownloadCacheCleaner.vacuumCacheDir(cacheDir, 20);
/*     */   }
/*     */   
/*     */   private BatchResult runDownload(BatchConfig config, Map<UUID, DownloadRequest> requests) {
/* 102 */     BatchResult result = new BatchResult();
/* 103 */     requests.forEach((id, request) -> {
/*     */           Path targetDir = this.cacheDir.resolve(id.toString());
/*     */           Path downloadedFile = null;
/*     */           try {
/*     */             downloadedFile = HttpUtil.downloadFile(targetDir, request.url, config.headers, config.hashFunction, request.hash, config.maxSize, config.proxy, config.listener);
/*     */             result.downloaded.put(id, downloadedFile);
/* 109 */           } catch (Exception e) {
/*     */             LOGGER.error("Failed to download {}", request.url, e);
/*     */             result.failed.add(id);
/*     */           } 
/*     */           try {
/*     */             this.eventLog.write(new LogEntry(id, request.url.toString(), Instant.now(), Optional.<HashCode>ofNullable(request.hash).map(HashCode::toString), (downloadedFile != null) ? getFileInfo(downloadedFile) : Either.left("download_failed")));
/* 121 */           } catch (Exception e) {
/*     */             LOGGER.error("Failed to log download of {}", request.url, e);
/*     */           } 
/*     */         });
/* 125 */     return result;
/*     */   }
/*     */   
/*     */   private Either<String, FileInfoEntry> getFileInfo(Path downloadedFile) {
/*     */     try {
/* 130 */       long size = Files.size(downloadedFile);
/* 131 */       Path relativePath = this.cacheDir.relativize(downloadedFile);
/* 132 */       return Either.right(new FileInfoEntry(relativePath.toString(), size));
/* 133 */     } catch (IOException e) {
/* 134 */       LOGGER.error("Failed to get file size of {}", downloadedFile, e);
/* 135 */       return Either.left("no_access");
/*     */     } 
/*     */   }
/*     */   
/*     */   public CompletableFuture<BatchResult> downloadBatch(BatchConfig config, Map<UUID, DownloadRequest> requests) {
/* 140 */     Objects.requireNonNull(this.tasks);
/* 140 */     return CompletableFuture.supplyAsync(() -> runDownload(config, requests), this.tasks::schedule);
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 145 */     this.tasks.close();
/* 146 */     this.eventLog.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\DownloadQueue.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */