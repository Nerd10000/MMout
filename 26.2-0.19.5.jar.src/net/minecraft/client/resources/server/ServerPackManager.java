/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import com.google.common.hash.HashCode;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.server.packs.DownloadQueue;
/*     */ 
/*     */ public class ServerPackManager {
/*     */   private final PackDownloader downloader;
/*     */   
/*     */   private final PackLoadFeedback packLoadFeedback;
/*     */   
/*     */   private final PackReloadConfig reloadConfig;
/*     */   
/*     */   private final Runnable updateRequest;
/*     */   
/*     */   private PackPromptStatus packPromptStatus;
/*     */   
/*  49 */   private final List<ServerPackData> packs = new ArrayList<>();
/*     */   
/*     */   public ServerPackManager(PackDownloader downloader, PackLoadFeedback packLoadFeedback, PackReloadConfig reloadConfig, Runnable updateRequest, PackPromptStatus packPromptStatus) {
/*  52 */     this.downloader = downloader;
/*  53 */     this.packLoadFeedback = packLoadFeedback;
/*  54 */     this.reloadConfig = reloadConfig;
/*  55 */     this.updateRequest = updateRequest;
/*  56 */     this.packPromptStatus = packPromptStatus;
/*     */   }
/*     */   
/*     */   private void registerForUpdate() {
/*  60 */     this.updateRequest.run();
/*     */   }
/*     */   
/*     */   public enum PackPromptStatus {
/*  64 */     PENDING, ALLOWED, DECLINED;
/*     */   }
/*     */   
/*     */   private enum PackDownloadStatus {
/*  70 */     REQUESTED, PENDING, DONE;
/*     */   }
/*     */   
/*     */   private enum RemovalReason {
/*  76 */     DOWNLOAD_FAILED((String)PackLoadFeedback.FinalResult.DOWNLOAD_FAILED),
/*  77 */     ACTIVATION_FAILED((String)PackLoadFeedback.FinalResult.ACTIVATION_FAILED),
/*  78 */     DECLINED((String)PackLoadFeedback.FinalResult.DECLINED),
/*  79 */     DISCARDED((String)PackLoadFeedback.FinalResult.DISCARDED),
/*  80 */     SERVER_REMOVED(null),
/*  81 */     SERVER_REPLACED(null);
/*     */     
/*     */     private final PackLoadFeedback.FinalResult serverResponse;
/*     */     
/*     */     RemovalReason(PackLoadFeedback.FinalResult serverResponse) {
/*  87 */       this.serverResponse = serverResponse;
/*     */     }
/*     */   }
/*     */   
/*     */   private enum ActivationStatus {
/*  92 */     INACTIVE, PENDING, ACTIVE;
/*     */   }
/*     */   
/*     */   private static class ServerPackData {
/*     */     private final UUID id;
/*     */     
/*     */     private final URL url;
/*     */     
/*     */     private final HashCode hash;
/*     */     
/*     */     private Path path;
/*     */     
/*     */     private ServerPackManager.RemovalReason removalReason;
/*     */     
/* 103 */     private ServerPackManager.PackDownloadStatus downloadStatus = ServerPackManager.PackDownloadStatus.REQUESTED;
/*     */     
/* 104 */     private ServerPackManager.ActivationStatus activationStatus = ServerPackManager.ActivationStatus.INACTIVE;
/*     */     
/*     */     private boolean promptAccepted;
/*     */     
/*     */     private ServerPackData(UUID id, URL url, HashCode hash) {
/* 108 */       this.id = id;
/* 109 */       this.url = url;
/* 110 */       this.hash = hash;
/*     */     }
/*     */     
/*     */     public void setRemovalReasonIfNotSet(ServerPackManager.RemovalReason removalReason) {
/* 114 */       if (this.removalReason == null)
/* 115 */         this.removalReason = removalReason; 
/*     */     }
/*     */     
/*     */     public boolean isRemoved() {
/* 120 */       return (this.removalReason != null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void markExistingPacksAsRemoved(UUID id) {
/* 126 */     for (ServerPackData pack : this.packs) {
/* 127 */       if (pack.id.equals(id))
/* 128 */         pack.setRemovalReasonIfNotSet(RemovalReason.SERVER_REPLACED); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void pushPack(UUID id, URL url, HashCode hash) {
/* 134 */     if (this.packPromptStatus == PackPromptStatus.DECLINED) {
/* 135 */       this.packLoadFeedback.reportFinalResult(id, PackLoadFeedback.FinalResult.DECLINED);
/*     */       return;
/*     */     } 
/* 139 */     pushNewPack(id, new ServerPackData(id, url, hash));
/*     */   }
/*     */   
/*     */   public void pushLocalPack(UUID id, Path path) {
/*     */     URL url;
/* 143 */     if (this.packPromptStatus == PackPromptStatus.DECLINED) {
/* 144 */       this.packLoadFeedback.reportFinalResult(id, PackLoadFeedback.FinalResult.DECLINED);
/*     */       return;
/*     */     } 
/*     */     try {
/* 150 */       url = path.toUri().toURL();
/* 151 */     } catch (MalformedURLException e) {
/* 152 */       throw new IllegalStateException("Can't convert path to URL " + String.valueOf(path), e);
/*     */     } 
/* 155 */     ServerPackData pack = new ServerPackData(id, url, null);
/* 156 */     pack.downloadStatus = PackDownloadStatus.DONE;
/* 157 */     pack.path = path;
/* 158 */     pushNewPack(id, pack);
/*     */   }
/*     */   
/*     */   private void pushNewPack(UUID id, ServerPackData pack) {
/* 163 */     markExistingPacksAsRemoved(id);
/* 165 */     this.packs.add(pack);
/* 167 */     if (this.packPromptStatus == PackPromptStatus.ALLOWED)
/* 168 */       acceptPack(pack); 
/* 171 */     registerForUpdate();
/*     */   }
/*     */   
/*     */   private void acceptPack(ServerPackData pack) {
/* 175 */     this.packLoadFeedback.reportUpdate(pack.id, PackLoadFeedback.Update.ACCEPTED);
/* 176 */     pack.promptAccepted = true;
/*     */   }
/*     */   
/*     */   private ServerPackData findPackInfo(UUID id) {
/* 180 */     for (ServerPackData pack : this.packs) {
/* 181 */       if (!pack.isRemoved() && pack.id.equals(id))
/* 182 */         return pack; 
/*     */     } 
/* 185 */     return null;
/*     */   }
/*     */   
/*     */   public void popPack(UUID id) {
/* 189 */     ServerPackData packInfo = findPackInfo(id);
/* 190 */     if (packInfo != null) {
/* 191 */       packInfo.setRemovalReasonIfNotSet(RemovalReason.SERVER_REMOVED);
/* 192 */       registerForUpdate();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void popAll() {
/* 197 */     for (ServerPackData pack : this.packs)
/* 198 */       pack.setRemovalReasonIfNotSet(RemovalReason.SERVER_REMOVED); 
/* 200 */     registerForUpdate();
/*     */   }
/*     */   
/*     */   public void allowServerPacks() {
/* 204 */     this.packPromptStatus = PackPromptStatus.ALLOWED;
/* 206 */     for (ServerPackData pack : this.packs) {
/* 207 */       if (!pack.promptAccepted && !pack.isRemoved())
/* 208 */         acceptPack(pack); 
/*     */     } 
/* 212 */     registerForUpdate();
/*     */   }
/*     */   
/*     */   public void rejectServerPacks() {
/* 216 */     this.packPromptStatus = PackPromptStatus.DECLINED;
/* 217 */     for (ServerPackData pack : this.packs) {
/* 218 */       if (!pack.promptAccepted)
/* 219 */         pack.setRemovalReasonIfNotSet(RemovalReason.DECLINED); 
/*     */     } 
/* 222 */     registerForUpdate();
/*     */   }
/*     */   
/*     */   public void resetPromptStatus() {
/* 226 */     this.packPromptStatus = PackPromptStatus.PENDING;
/*     */   }
/*     */   
/*     */   public void tick() {
/* 231 */     boolean downloadsPending = updateDownloads();
/* 232 */     if (!downloadsPending)
/* 233 */       triggerReloadIfNeeded(); 
/* 236 */     cleanupRemovedPacks();
/*     */   }
/*     */   
/*     */   private void cleanupRemovedPacks() {
/* 240 */     this.packs.removeIf(data -> {
/*     */           if (data.activationStatus != ActivationStatus.INACTIVE)
/*     */             return false; 
/*     */           if (data.removalReason != null) {
/*     */             PackLoadFeedback.FinalResult response = data.removalReason.serverResponse;
/*     */             if (response != null)
/*     */               this.packLoadFeedback.reportFinalResult(data.id, response); 
/*     */             return true;
/*     */           } 
/*     */           return false;
/*     */         });
/*     */   }
/*     */   
/*     */   private void onDownload(Collection<ServerPackData> data, DownloadQueue.BatchResult result) {
/* 259 */     if (!result.failed().isEmpty())
/* 261 */       for (ServerPackData pack : this.packs) {
/* 262 */         if (pack.activationStatus != ActivationStatus.ACTIVE) {
/* 263 */           if (result.failed().contains(pack.id)) {
/* 264 */             pack.setRemovalReasonIfNotSet(RemovalReason.DOWNLOAD_FAILED);
/*     */             continue;
/*     */           } 
/* 266 */           pack.setRemovalReasonIfNotSet(RemovalReason.DISCARDED);
/*     */         } 
/*     */       }  
/* 272 */     for (ServerPackData pack : data) {
/* 273 */       Path packFile = (Path)result.downloaded().get(pack.id);
/* 274 */       if (packFile != null) {
/* 275 */         pack.downloadStatus = PackDownloadStatus.DONE;
/* 276 */         pack.path = packFile;
/* 278 */         if (!pack.isRemoved())
/* 279 */           this.packLoadFeedback.reportUpdate(pack.id, PackLoadFeedback.Update.DOWNLOADED); 
/*     */       } 
/*     */     } 
/* 283 */     registerForUpdate();
/*     */   }
/*     */   
/*     */   private boolean updateDownloads() {
/* 290 */     List<ServerPackData> downloadPacks = new ArrayList<>();
/* 291 */     boolean downloadsInProgress = false;
/* 292 */     for (ServerPackData pack : this.packs) {
/* 293 */       if (pack.isRemoved() || !pack.promptAccepted)
/*     */         continue; 
/* 297 */       if (pack.downloadStatus != PackDownloadStatus.DONE)
/* 298 */         downloadsInProgress = true; 
/* 301 */       if (pack.downloadStatus == PackDownloadStatus.REQUESTED) {
/* 302 */         pack.downloadStatus = PackDownloadStatus.PENDING;
/* 303 */         downloadPacks.add(pack);
/*     */       } 
/*     */     } 
/* 307 */     if (!downloadPacks.isEmpty()) {
/* 308 */       Map<UUID, DownloadQueue.DownloadRequest> downloadRequests = new HashMap<>();
/* 309 */       for (ServerPackData pack : downloadPacks)
/* 310 */         downloadRequests.put(pack.id, new DownloadQueue.DownloadRequest(pack.url, pack.hash)); 
/* 312 */       this.downloader.download(downloadRequests, result -> onDownload(downloadPacks, result));
/*     */     } 
/* 314 */     return downloadsInProgress;
/*     */   }
/*     */   
/*     */   private void triggerReloadIfNeeded() {
/* 318 */     boolean needsReload = false;
/* 319 */     final List<ServerPackData> packsToLoad = new ArrayList<>();
/* 320 */     final List<ServerPackData> packsToUnload = new ArrayList<>();
/* 322 */     for (ServerPackData pack : this.packs) {
/* 323 */       if (pack.activationStatus == ActivationStatus.PENDING)
/*     */         return; 
/* 328 */       boolean shouldBeActive = (pack.promptAccepted && pack.downloadStatus == PackDownloadStatus.DONE && !pack.isRemoved());
/* 329 */       if (shouldBeActive && pack.activationStatus == ActivationStatus.INACTIVE) {
/* 330 */         packsToLoad.add(pack);
/* 331 */         needsReload = true;
/*     */       } 
/* 334 */       if (pack.activationStatus == ActivationStatus.ACTIVE) {
/* 335 */         if (!shouldBeActive) {
/* 336 */           needsReload = true;
/* 337 */           packsToUnload.add(pack);
/*     */           continue;
/*     */         } 
/* 340 */         packsToLoad.add(pack);
/*     */       } 
/*     */     } 
/* 345 */     if (needsReload) {
/* 346 */       for (ServerPackData pack : packsToLoad) {
/* 347 */         if (pack.activationStatus != ActivationStatus.ACTIVE)
/* 348 */           pack.activationStatus = ActivationStatus.PENDING; 
/*     */       } 
/* 352 */       for (ServerPackData pack : packsToUnload)
/* 353 */         pack.activationStatus = ActivationStatus.PENDING; 
/* 356 */       this.reloadConfig.scheduleReload(new PackReloadConfig.Callbacks() {
/*     */             public void onSuccess() {
/* 359 */               for (ServerPackManager.ServerPackData pack : packsToLoad) {
/* 360 */                 pack.activationStatus = ServerPackManager.ActivationStatus.ACTIVE;
/* 361 */                 if (pack.removalReason == null)
/* 362 */                   ServerPackManager.this.packLoadFeedback.reportFinalResult(pack.id, PackLoadFeedback.FinalResult.APPLIED); 
/*     */               } 
/* 365 */               for (ServerPackManager.ServerPackData pack : packsToUnload)
/* 366 */                 pack.activationStatus = ServerPackManager.ActivationStatus.INACTIVE; 
/* 368 */               ServerPackManager.this.registerForUpdate();
/*     */             }
/*     */             
/*     */             public void onFailure(boolean isRecovery) {
/* 373 */               if (!isRecovery) {
/* 375 */                 packsToLoad.clear();
/* 376 */                 for (ServerPackManager.ServerPackData pack : ServerPackManager.this.packs) {
/* 377 */                   switch (pack.activationStatus.ordinal()) {
/*     */                     case 2:
/* 378 */                       packsToLoad.add(pack);
/*     */                     case 1:
/* 380 */                       pack.activationStatus = ServerPackManager.ActivationStatus.INACTIVE;
/* 381 */                       pack.setRemovalReasonIfNotSet(ServerPackManager.RemovalReason.ACTIVATION_FAILED);
/*     */                     case 0:
/* 383 */                       pack.setRemovalReasonIfNotSet(ServerPackManager.RemovalReason.DISCARDED);
/*     */                   } 
/*     */                 } 
/* 386 */                 ServerPackManager.this.registerForUpdate();
/*     */               } else {
/* 389 */                 for (ServerPackManager.ServerPackData pack : ServerPackManager.this.packs) {
/* 390 */                   if (pack.activationStatus == ServerPackManager.ActivationStatus.PENDING)
/* 391 */                     pack.activationStatus = ServerPackManager.ActivationStatus.INACTIVE; 
/*     */                 } 
/*     */               } 
/*     */             }
/*     */             
/*     */             public List<PackReloadConfig.IdAndPath> packsToLoad() {
/* 399 */               return packsToLoad.stream().map(pack -> new PackReloadConfig.IdAndPath(pack.id, pack.path)).toList();
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\ServerPackManager.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */