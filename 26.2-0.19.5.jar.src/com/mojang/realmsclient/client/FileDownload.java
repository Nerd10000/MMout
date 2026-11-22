/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.google.common.io.Files;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.dto.WorldDownload;
/*     */ import com.mojang.realmsclient.exception.RealmsDefaultUncaughtExceptionHandler;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsDownloadLatestWorldScreen;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.URI;
/*     */ import java.net.http.HttpClient;
/*     */ import java.net.http.HttpRequest;
/*     */ import java.net.http.HttpResponse;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
/*     */ import java.util.Locale;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.validation.ContentValidationException;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
/*     */ import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
/*     */ import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.apache.commons.io.output.CountingOutputStream;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FileDownload {
/*  51 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private volatile boolean cancelled;
/*     */   
/*     */   private volatile boolean finished;
/*     */   
/*     */   private volatile boolean error;
/*     */   
/*     */   private volatile boolean extracting;
/*     */   
/*     */   private volatile File tempFile;
/*     */   
/*     */   private volatile File resourcePackPath;
/*     */   
/*     */   private volatile CompletableFuture<?> pendingRequest;
/*     */   
/*     */   private Thread currentThread;
/*     */   
/*     */   private <T> T joinCancellableRequest(CompletableFuture<T> pendingRequest) throws Throwable {
/*  65 */     this.pendingRequest = pendingRequest;
/*  66 */     if (this.cancelled) {
/*  67 */       pendingRequest.cancel(true);
/*  68 */       return null;
/*     */     } 
/*     */     try {
/*  72 */       return pendingRequest.join();
/*  73 */     } catch (CompletionException e) {
/*  74 */       throw e.getCause();
/*  76 */     } catch (CancellationException e) {
/*  77 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static HttpClient createClient() {
/*  82 */     return HttpClient.newBuilder().executor((Executor)Util.nonCriticalIoPool()).connectTimeout(Duration.ofMinutes(2L)).build();
/*     */   }
/*     */   
/*     */   private static HttpRequest.Builder createRequest(String downloadLink) {
/*  86 */     return HttpRequest.newBuilder(URI.create(downloadLink)).timeout(Duration.ofMinutes(2L));
/*     */   }
/*     */   
/*     */   @CheckReturnValue
/*     */   public static OptionalLong contentLength(String downloadLink) {
/*     */     try {
/*  91 */       HttpClient client = createClient();
/*     */       try {
/*  92 */         HttpResponse<Void> response = client.send(
/*  93 */             createRequest(downloadLink).HEAD().build(), 
/*  94 */             HttpResponse.BodyHandlers.discarding());
/*  96 */         OptionalLong optionalLong = response.headers().firstValueAsLong("Content-Length");
/*  97 */         if (client != null)
/*  97 */           client.close(); 
/*     */         return optionalLong;
/*     */       } catch (Throwable throwable) {
/*     */         if (client != null)
/*     */           try {
/*     */             client.close();
/*     */           } catch (Throwable throwable1) {
/*     */             throwable.addSuppressed(throwable1);
/*     */           }  
/*     */         throw throwable;
/*     */       } 
/*  97 */     } catch (Exception e) {
/*  98 */       LOGGER.error("Unable to get content length for download");
/*  99 */       return OptionalLong.empty();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void download(WorldDownload worldDownload, String worldName, RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, LevelStorageSource levelStorageSource) {
/* 104 */     if (this.currentThread != null)
/*     */       return; 
/* 108 */     this.currentThread = new Thread(() -> {
/*     */           HttpClient client = createClient();
/*     */           try {
/*     */             try {
/*     */               this.tempFile = File.createTempFile("backup", ".tar.gz");
/*     */               download(downloadStatus, client, worldDownload.downloadLink(), this.tempFile);
/*     */               finishWorldDownload(worldName.trim(), this.tempFile, levelStorageSource, downloadStatus);
/* 114 */             } catch (Exception e) {
/*     */               LOGGER.error("Caught exception while downloading world", e);
/*     */               this.error = true;
/*     */             } finally {
/*     */               this.pendingRequest = null;
/*     */               if (this.tempFile != null)
/*     */                 this.tempFile.delete(); 
/*     */               this.tempFile = null;
/*     */             } 
/*     */             if (this.error) {
/*     */               if (client != null)
/*     */                 client.close(); 
/*     */               return;
/*     */             } 
/*     */             String resourcePackLink = worldDownload.resourcePackUrl();
/*     */             if (!resourcePackLink.isEmpty() && !worldDownload.resourcePackHash().isEmpty())
/*     */               try {
/*     */                 this.tempFile = File.createTempFile("resources", ".tar.gz");
/*     */                 download(downloadStatus, client, resourcePackLink, this.tempFile);
/*     */                 finishResourcePackDownload(downloadStatus, this.tempFile, worldDownload);
/* 135 */               } catch (Exception e) {
/*     */                 LOGGER.error("Caught exception while downloading resource pack", e);
/*     */                 this.error = true;
/*     */               } finally {
/*     */                 this.pendingRequest = null;
/*     */                 if (this.tempFile != null)
/*     */                   this.tempFile.delete(); 
/*     */                 this.tempFile = null;
/*     */               }  
/*     */             this.finished = true;
/*     */             if (client != null)
/*     */               client.close(); 
/*     */           } catch (Throwable t$) {
/*     */             if (client != null)
/*     */               try {
/*     */                 client.close();
/*     */               } catch (Throwable x2) {
/*     */                 t$.addSuppressed(x2);
/*     */               }  
/*     */             throw t$;
/*     */           } 
/*     */         }"Realms world download");
/* 150 */     this.currentThread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new RealmsDefaultUncaughtExceptionHandler(LOGGER));
/* 151 */     this.currentThread.start();
/*     */   }
/*     */   
/*     */   private void download(RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, HttpClient client, String url, File target) throws IOException {
/*     */     HttpResponse<InputStream> response;
/* 155 */     HttpRequest request = createRequest(url).GET().build();
/*     */     try {
/* 159 */       response = joinCancellableRequest(client.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream()));
/* 160 */     } catch (Error e) {
/* 161 */       throw e;
/* 162 */     } catch (Throwable e) {
/* 163 */       LOGGER.error("Failed to download {}", url, e);
/* 164 */       this.error = true;
/*     */       return;
/*     */     } 
/* 167 */     if (response == null || this.cancelled)
/*     */       return; 
/* 171 */     if (response.statusCode() != 200) {
/* 172 */       this.error = true;
/*     */       return;
/*     */     } 
/* 176 */     downloadStatus.totalBytes = response.headers().firstValueAsLong("Content-Length").orElse(0L);
/* 179 */     InputStream is = response.body();
/*     */     try {
/* 180 */       OutputStream os = new FileOutputStream(target);
/*     */       try {
/* 182 */         is.transferTo((OutputStream)new DownloadCountingOutputStream(os, downloadStatus));
/* 183 */         os.close();
/*     */       } catch (Throwable throwable) {
/*     */         try {
/*     */           os.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         } 
/*     */         throw throwable;
/*     */       } 
/* 183 */       if (is != null)
/* 183 */         is.close(); 
/*     */     } catch (Throwable throwable) {
/*     */       if (is != null)
/*     */         try {
/*     */           is.close();
/*     */         } catch (Throwable throwable1) {
/*     */           throwable.addSuppressed(throwable1);
/*     */         }  
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 187 */     if (this.tempFile != null) {
/* 188 */       this.tempFile.delete();
/* 189 */       this.tempFile = null;
/*     */     } 
/* 192 */     this.cancelled = true;
/* 193 */     CompletableFuture<?> pendingRequest = this.pendingRequest;
/* 194 */     if (pendingRequest != null)
/* 195 */       pendingRequest.cancel(true); 
/*     */   }
/*     */   
/*     */   public boolean isFinished() {
/* 200 */     return this.finished;
/*     */   }
/*     */   
/*     */   public boolean isError() {
/* 204 */     return this.error;
/*     */   }
/*     */   
/*     */   public boolean isExtracting() {
/* 208 */     return this.extracting;
/*     */   }
/*     */   
/* 212 */   private static final String[] INVALID_FILE_NAMES = new String[] { 
/* 212 */       "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4", 
/* 212 */       "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", 
/* 212 */       "LPT6", "LPT7", "LPT8", "LPT9" };
/*     */   
/*     */   public static String findAvailableFolderName(String folder) {
/* 217 */     folder = folder.replaceAll("[\\./\"]", "_");
/* 219 */     for (String invalidName : INVALID_FILE_NAMES) {
/* 220 */       if (folder.equalsIgnoreCase(invalidName))
/* 221 */         folder = "_" + folder + "_"; 
/*     */     } 
/* 225 */     return folder;
/*     */   }
/*     */   
/*     */   private void untarGzipArchive(String name, File file, LevelStorageSource levelStorageSource) throws IOException {
/*     */     String finalName;
/* 229 */     Pattern namePattern = Pattern.compile(".*-([0-9]+)$");
/* 232 */     int number = 1;
/* 234 */     for (char replacer : SharedConstants.ILLEGAL_FILE_CHARACTERS)
/* 235 */       name = name.replace(replacer, '_'); 
/* 238 */     if (StringUtils.isEmpty(name))
/* 239 */       name = "Realm"; 
/* 242 */     name = findAvailableFolderName(name);
/*     */     try {
/* 245 */       for (LevelStorageSource.LevelDirectory level : levelStorageSource.findLevelCandidates()) {
/* 246 */         String levelId = level.directoryName();
/* 247 */         if (levelId.toLowerCase(Locale.ROOT).startsWith(name.toLowerCase(Locale.ROOT))) {
/* 248 */           Matcher matcher = namePattern.matcher(levelId);
/* 249 */           if (matcher.matches()) {
/* 250 */             int parsedNumber = Integer.parseInt(matcher.group(1));
/* 251 */             if (parsedNumber > number)
/* 252 */               number = parsedNumber; 
/*     */             continue;
/*     */           } 
/* 255 */           number++;
/*     */         } 
/*     */       } 
/* 259 */     } catch (Exception e) {
/* 260 */       LOGGER.error("Error getting level list", e);
/* 261 */       this.error = true;
/*     */       return;
/*     */     } 
/* 265 */     if (!levelStorageSource.isNewLevelIdAcceptable(name) || number > 1) {
/* 266 */       finalName = name + name;
/* 268 */       if (!levelStorageSource.isNewLevelIdAcceptable(finalName)) {
/* 269 */         boolean foundName = false;
/* 271 */         while (!foundName) {
/* 272 */           number++;
/* 273 */           finalName = name + name;
/* 275 */           if (levelStorageSource.isNewLevelIdAcceptable(finalName))
/* 276 */             foundName = true; 
/*     */         } 
/*     */       } 
/*     */     } else {
/* 281 */       finalName = name;
/*     */     } 
/* 284 */     TarArchiveInputStream tarIn = null;
/* 285 */     Path worldPath = Minecraft.getInstance().getLevelSource().getLevelPath(finalName).normalize();
/*     */     try {
/* 287 */       FileUtil.createDirectoriesSafe(worldPath);
/* 289 */       tarIn = new TarArchiveInputStream((InputStream)new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(file))));
/* 291 */       TarArchiveEntry tarEntry = tarIn.getNextTarEntry();
/* 292 */       while (tarEntry != null) {
/* 293 */         Path destPath = worldPath.resolve(Path.of("world", new String[0]).relativize(Path.of(tarEntry.getName(), new String[0]))).normalize();
/* 295 */         if (!destPath.startsWith(worldPath)) {
/* 296 */           LOGGER.warn("Unexpected entry in Realms world download: {}", tarEntry.getName());
/* 297 */           tarEntry = tarIn.getNextTarEntry();
/*     */           continue;
/*     */         } 
/* 300 */         if (tarEntry.isDirectory()) {
/* 301 */           FileUtil.createDirectoriesSafe(destPath);
/*     */         } else {
/* 303 */           Path parent = destPath.getParent();
/* 304 */           if (parent != null)
/* 305 */             FileUtil.createDirectoriesSafe(parent); 
/* 307 */           FileOutputStream output = new FileOutputStream(destPath.toFile());
/*     */           try {
/* 308 */             IOUtils.copy((InputStream)tarIn, output);
/* 309 */             output.close();
/*     */           } catch (Throwable throwable) {
/*     */             try {
/*     */               output.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             } 
/*     */             throw throwable;
/*     */           } 
/*     */         } 
/* 312 */         tarEntry = tarIn.getNextTarEntry();
/*     */       } 
/* 314 */     } catch (Exception e) {
/* 315 */       LOGGER.error("Error extracting world", e);
/* 316 */       this.error = true;
/*     */     } finally {
/* 318 */       if (tarIn != null)
/* 319 */         tarIn.close(); 
/* 322 */       if (file != null)
/* 323 */         file.delete(); 
/*     */       try {
/* 326 */         LevelStorageSource.LevelStorageAccess access = levelStorageSource.validateAndCreateAccess(finalName);
/*     */         try {
/* 327 */           access.renameAndDropPlayer(finalName);
/* 328 */           if (access != null)
/* 328 */             access.close(); 
/*     */         } catch (Throwable throwable) {
/*     */           if (access != null)
/*     */             try {
/*     */               access.close();
/*     */             } catch (Throwable throwable1) {
/*     */               throwable.addSuppressed(throwable1);
/*     */             }  
/*     */           throw throwable;
/*     */         } 
/* 328 */       } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 329 */         LOGGER.error("Failed to modify unpacked realms level {}", finalName, e);
/* 330 */       } catch (ContentValidationException e) {
/* 331 */         LOGGER.warn("Failed to download file", (Throwable)e);
/*     */       } 
/* 334 */       this.resourcePackPath = worldPath.resolve(LevelResource.MAP_RESOURCE_FILE.id()).toFile();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void finishWorldDownload(String worldName, File tempFile, LevelStorageSource levelStorageSource, RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus) {
/* 339 */     if (downloadStatus.bytesWritten >= downloadStatus.totalBytes && !this.cancelled && !this.error)
/*     */       try {
/* 341 */         this.extracting = true;
/* 342 */         untarGzipArchive(worldName, tempFile, levelStorageSource);
/* 343 */       } catch (IOException e) {
/* 344 */         LOGGER.error("Error extracting archive", e);
/* 345 */         this.error = true;
/*     */       }  
/*     */   }
/*     */   
/*     */   private void finishResourcePackDownload(RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus, File tempFile, WorldDownload worldDownload) {
/* 351 */     if (downloadStatus.bytesWritten >= downloadStatus.totalBytes && !this.cancelled)
/*     */       try {
/* 354 */         String actualHash = Hashing.sha1().hashBytes(Files.toByteArray(tempFile)).toString();
/* 356 */         if (actualHash.equals(worldDownload.resourcePackHash())) {
/* 357 */           FileUtils.copyFile(tempFile, this.resourcePackPath);
/* 358 */           this.finished = true;
/*     */         } else {
/* 360 */           LOGGER.error("Resourcepack had wrong hash (expected {}, found {}). Deleting it.", worldDownload.resourcePackHash(), actualHash);
/* 361 */           FileUtils.deleteQuietly(tempFile);
/* 362 */           this.error = true;
/*     */         } 
/* 364 */       } catch (IOException e) {
/* 365 */         LOGGER.error("Error copying resourcepack file: {}", e.getMessage());
/* 366 */         this.error = true;
/*     */       }  
/*     */   }
/*     */   
/*     */   private static class DownloadCountingOutputStream extends CountingOutputStream {
/*     */     private final RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus;
/*     */     
/*     */     public DownloadCountingOutputStream(OutputStream out, RealmsDownloadLatestWorldScreen.DownloadStatus downloadStatus) {
/* 375 */       super(out);
/* 376 */       this.downloadStatus = downloadStatus;
/*     */     }
/*     */     
/*     */     protected void afterWrite(int n) throws IOException {
/* 381 */       super.afterWrite(n);
/* 382 */       this.downloadStatus.bytesWritten = getByteCount();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\FileDownload.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */