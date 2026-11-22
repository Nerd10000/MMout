/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.Unit;
/*     */ import com.mojang.util.UndashedUuid;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.WorldVersion;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.User;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.main.GameConfig;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.packs.DownloadQueue;
/*     */ import net.minecraft.server.packs.FilePackResources;
/*     */ import net.minecraft.server.packs.PackLocationInfo;
/*     */ import net.minecraft.server.packs.PackSelectionConfig;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackSource;
/*     */ import net.minecraft.server.packs.repository.RepositorySource;
/*     */ import net.minecraft.util.HttpUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class DownloadedPackSource implements AutoCloseable {
/*  50 */   private static final Component SERVER_NAME = (Component)Component.translatable("resourcePack.server.name");
/*     */   
/*  52 */   private static final Pattern SHA1 = Pattern.compile("^[a-fA-F0-9]{40}$");
/*     */   
/*  54 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final RepositorySource EMPTY_SOURCE = result -> {
/*     */     
/*     */     };
/*     */   
/*  57 */   private static final PackSelectionConfig DOWNLOADED_PACK_SELECTION = new PackSelectionConfig(true, Pack.Position.TOP, true);
/*     */   
/*  63 */   private static final PackLoadFeedback LOG_ONLY_FEEDBACK = new PackLoadFeedback() {
/*     */       public void reportUpdate(UUID id, PackLoadFeedback.Update update) {
/*  66 */         DownloadedPackSource.LOGGER.debug("Downloaded pack {} changed state to {}", id, update);
/*     */       }
/*     */       
/*     */       public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult result) {
/*  71 */         DownloadedPackSource.LOGGER.debug("Downloaded pack {} finished with state {}", id, result);
/*     */       }
/*     */     };
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*  77 */   private RepositorySource packSource = EMPTY_SOURCE;
/*     */   
/*     */   private PackReloadConfig.Callbacks pendingReload;
/*     */   
/*     */   private final ServerPackManager manager;
/*     */   
/*     */   private final DownloadQueue downloadQueue;
/*     */   
/*  84 */   private PackSource packType = PackSource.SERVER;
/*     */   
/*  85 */   private PackLoadFeedback packFeedback = LOG_ONLY_FEEDBACK;
/*     */   
/*     */   private int packIdSerialNumber;
/*     */   
/*     */   public DownloadedPackSource(Minecraft minecraft, Path packCache, GameConfig.UserData user) {
/*  94 */     this.minecraft = minecraft;
/*     */     try {
/*  96 */       this.downloadQueue = new DownloadQueue(packCache);
/*  97 */     } catch (IOException e) {
/*  98 */       throw new UncheckedIOException("Failed to open download queue in directory " + String.valueOf(packCache), e);
/*     */     } 
/* 101 */     Objects.requireNonNull(minecraft);
/* 101 */     Executor executor = minecraft::schedule;
/* 102 */     this
/*     */       
/* 117 */       .manager = new ServerPackManager(createDownloader(this.downloadQueue, executor, user.user, user.proxy), new PackLoadFeedback() {
/*     */           public void reportUpdate(UUID id, PackLoadFeedback.Update result) {
/*     */             DownloadedPackSource.this.packFeedback.reportUpdate(id, result);
/*     */           }
/*     */           
/*     */           public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult result) {
/*     */             DownloadedPackSource.this.packFeedback.reportFinalResult(id, result);
/*     */           }
/* 117 */         }createReloadConfig(), createUpdateScheduler(executor), ServerPackManager.PackPromptStatus.PENDING);
/*     */   }
/*     */   
/*     */   private HttpUtil.DownloadProgressListener createDownloadNotifier(final int totalCount) {
/* 123 */     return new HttpUtil.DownloadProgressListener() {
/* 125 */         private final SystemToast.SystemToastId toastId = new SystemToast.SystemToastId();
/*     */         
/* 126 */         private Component title = (Component)Component.empty();
/*     */         
/* 127 */         private Component message = null;
/*     */         
/*     */         private int count;
/*     */         
/*     */         private int failCount;
/*     */         
/* 130 */         private OptionalLong totalBytes = OptionalLong.empty();
/*     */         
/*     */         private void updateToast() {
/* 133 */           DownloadedPackSource.this.minecraft.execute(() -> SystemToast.addOrUpdate(DownloadedPackSource.this.minecraft.gui.toastManager(), this.toastId, this.title, this.message));
/*     */         }
/*     */         
/*     */         private void updateProgress(long bytesSoFar) {
/* 137 */           if (this.totalBytes.isPresent()) {
/* 138 */             this.message = (Component)Component.translatable("download.pack.progress.percent", new Object[] { Long.valueOf(bytesSoFar * 100L / this.totalBytes.getAsLong()) });
/*     */           } else {
/* 140 */             this.message = (Component)Component.translatable("download.pack.progress.bytes", new Object[] { Unit.humanReadable(bytesSoFar) });
/*     */           } 
/* 142 */           updateToast();
/*     */         }
/*     */         
/*     */         public void requestStart() {
/* 147 */           this.count++;
/* 148 */           this.title = (Component)Component.translatable("download.pack.title", new Object[] { Integer.valueOf(this.count), Integer.valueOf(this.val$totalCount) });
/* 149 */           updateToast();
/* 150 */           DownloadedPackSource.LOGGER.debug("Starting pack {}/{} download", Integer.valueOf(this.count), Integer.valueOf(totalCount));
/*     */         }
/*     */         
/*     */         public void downloadStart(OptionalLong sizeBytes) {
/* 155 */           DownloadedPackSource.LOGGER.debug("File size = {} bytes", sizeBytes);
/* 156 */           this.totalBytes = sizeBytes;
/* 157 */           updateProgress(0L);
/*     */         }
/*     */         
/*     */         public void downloadedBytes(long bytesSoFar) {
/* 162 */           DownloadedPackSource.LOGGER.debug("Progress for pack {}: {} bytes", Integer.valueOf(this.count), Long.valueOf(bytesSoFar));
/* 163 */           updateProgress(bytesSoFar);
/*     */         }
/*     */         
/*     */         public void requestFinished(boolean success) {
/* 168 */           if (!success) {
/* 169 */             DownloadedPackSource.LOGGER.info("Pack {} failed to download", Integer.valueOf(this.count));
/* 170 */             this.failCount++;
/*     */           } else {
/* 172 */             DownloadedPackSource.LOGGER.debug("Download ended for pack {}", Integer.valueOf(this.count));
/*     */           } 
/* 175 */           if (this.count == totalCount)
/* 176 */             if (this.failCount > 0) {
/* 177 */               this.title = (Component)Component.translatable("download.pack.failed", new Object[] { Integer.valueOf(this.failCount), Integer.valueOf(this.val$totalCount) });
/* 178 */               this.message = null;
/* 179 */               updateToast();
/*     */             } else {
/* 181 */               SystemToast.forceHide(DownloadedPackSource.this.minecraft.gui.toastManager(), this.toastId);
/*     */             }  
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private PackDownloader createDownloader(final DownloadQueue downloadQueue, final Executor mainThreadExecutor, final User user, final Proxy proxy) {
/* 189 */     return new PackDownloader() {
/*     */         private static final int MAX_PACK_SIZE_BYTES = 262144000;
/*     */         
/* 192 */         private static final HashFunction CACHE_HASHING_FUNCTION = Hashing.sha1();
/*     */         
/*     */         private Map<String, String> createDownloadHeaders() {
/* 195 */           WorldVersion version = SharedConstants.getCurrentVersion();
/* 196 */           return Map.of("X-Minecraft-Username", user
/* 197 */               .getName(), "X-Minecraft-UUID", 
/* 198 */               UndashedUuid.toString(user.getProfileId()), "X-Minecraft-Version", version
/* 199 */               .name(), "X-Minecraft-Version-ID", version
/* 200 */               .id(), "X-Minecraft-Pack-Format", 
/* 201 */               String.valueOf(version.packVersion(PackType.CLIENT_RESOURCES)), "User-Agent", "Minecraft Java/" + version
/* 202 */               .name());
/*     */         }
/*     */         
/*     */         public void download(Map<UUID, DownloadQueue.DownloadRequest> requests, Consumer<DownloadQueue.BatchResult> output) {
/* 207 */           downloadQueue.downloadBatch(new DownloadQueue.BatchConfig(CACHE_HASHING_FUNCTION, 262144000, 
/*     */                 
/* 211 */                 createDownloadHeaders(), proxy, DownloadedPackSource.this
/*     */                 
/* 213 */                 .createDownloadNotifier(requests.size())), requests)
/*     */             
/* 216 */             .thenAcceptAsync(output, mainThreadExecutor);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private Runnable createUpdateScheduler(final Executor mainThreadExecutor) {
/* 222 */     return new Runnable() {
/*     */         private boolean scheduledInMainExecutor;
/*     */         
/*     */         private boolean hasUpdates;
/*     */         
/*     */         public void run() {
/* 228 */           this.hasUpdates = true;
/* 229 */           if (!this.scheduledInMainExecutor) {
/* 230 */             this.scheduledInMainExecutor = true;
/* 231 */             mainThreadExecutor.execute(this::runAllUpdates);
/*     */           } 
/*     */         }
/*     */         
/*     */         private void runAllUpdates() {
/* 236 */           while (this.hasUpdates) {
/* 237 */             this.hasUpdates = false;
/* 238 */             DownloadedPackSource.this.manager.tick();
/*     */           } 
/* 240 */           this.scheduledInMainExecutor = false;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private PackReloadConfig createReloadConfig() {
/* 246 */     return this::startReload;
/*     */   }
/*     */   
/*     */   private List<Pack> loadRequestedPacks(List<PackReloadConfig.IdAndPath> packsToLoad) {
/* 250 */     List<Pack> packs = new ArrayList<>(packsToLoad.size());
/* 252 */     for (PackReloadConfig.IdAndPath idAndPath : Lists.reverse(packsToLoad)) {
/* 253 */       String name = String.format(Locale.ROOT, "server/%08X/%s", new Object[] { Integer.valueOf(this.packIdSerialNumber++), idAndPath.id() });
/* 254 */       Path path = idAndPath.path();
/* 260 */       PackLocationInfo packLocationInfo = new PackLocationInfo(name, SERVER_NAME, this.packType, Optional.empty());
/* 263 */       FilePackResources.FileResourcesSupplier fileResourcesSupplier = new FilePackResources.FileResourcesSupplier(path);
/* 265 */       PackFormat currentPackVersion = SharedConstants.getCurrentVersion().packVersion(PackType.CLIENT_RESOURCES);
/* 266 */       Pack.Metadata metadata = Pack.readPackMetadata(packLocationInfo, (Pack.ResourcesSupplier)fileResourcesSupplier, currentPackVersion, PackType.CLIENT_RESOURCES);
/* 267 */       if (metadata == null) {
/* 268 */         LOGGER.warn("Invalid pack metadata in {}, ignoring all", path);
/* 269 */         return null;
/*     */       } 
/* 271 */       packs.add(new Pack(packLocationInfo, (Pack.ResourcesSupplier)fileResourcesSupplier, metadata, DOWNLOADED_PACK_SELECTION));
/*     */     } 
/* 274 */     return packs;
/*     */   }
/*     */   
/*     */   public RepositorySource createRepositorySource() {
/* 279 */     return output -> this.packSource.loadPacks(output);
/*     */   }
/*     */   
/*     */   private static RepositorySource configureSource(List<Pack> packs) {
/* 283 */     if (packs.isEmpty())
/* 284 */       return EMPTY_SOURCE; 
/* 286 */     Objects.requireNonNull(packs);
/* 286 */     return packs::forEach;
/*     */   }
/*     */   
/*     */   private void startReload(PackReloadConfig.Callbacks callbacks) {
/* 290 */     this.pendingReload = callbacks;
/* 291 */     List<PackReloadConfig.IdAndPath> normalPacks = callbacks.packsToLoad();
/* 292 */     List<Pack> packs = loadRequestedPacks(normalPacks);
/* 293 */     if (packs == null) {
/* 295 */       callbacks.onFailure(false);
/* 296 */       List<PackReloadConfig.IdAndPath> recoveryPacks = callbacks.packsToLoad();
/* 298 */       packs = loadRequestedPacks(recoveryPacks);
/* 299 */       if (packs == null) {
/* 300 */         LOGGER.warn("Double failure in loading server packs");
/* 301 */         packs = List.of();
/*     */       } 
/*     */     } 
/* 305 */     this.packSource = configureSource(packs);
/* 306 */     this.minecraft.reloadResourcePacks();
/*     */   }
/*     */   
/*     */   public void onRecovery() {
/* 310 */     if (this.pendingReload != null) {
/* 311 */       this.pendingReload.onFailure(false);
/* 312 */       List<Pack> packs = loadRequestedPacks(this.pendingReload.packsToLoad());
/* 313 */       if (packs == null) {
/* 314 */         LOGGER.warn("Double failure in loading server packs");
/* 315 */         packs = List.of();
/*     */       } 
/* 317 */       this.packSource = configureSource(packs);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onRecoveryFailure() {
/* 322 */     if (this.pendingReload != null) {
/* 323 */       this.pendingReload.onFailure(true);
/* 324 */       this.pendingReload = null;
/* 325 */       this.packSource = EMPTY_SOURCE;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onReloadSuccess() {
/* 331 */     if (this.pendingReload != null) {
/* 332 */       this.pendingReload.onSuccess();
/* 333 */       this.pendingReload = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static HashCode tryParseSha1Hash(String hash) {
/* 339 */     if (hash != null && SHA1.matcher(hash).matches())
/* 340 */       return HashCode.fromString(hash.toLowerCase(Locale.ROOT)); 
/* 342 */     return null;
/*     */   }
/*     */   
/*     */   public void pushPack(UUID id, URL url, String hash) {
/* 346 */     HashCode parsedHash = tryParseSha1Hash(hash);
/* 347 */     this.manager.pushPack(id, url, parsedHash);
/*     */   }
/*     */   
/*     */   public void pushLocalPack(UUID id, Path path) {
/* 351 */     this.manager.pushLocalPack(id, path);
/*     */   }
/*     */   
/*     */   public void popPack(UUID id) {
/* 355 */     this.manager.popPack(id);
/*     */   }
/*     */   
/*     */   public void popAll() {
/* 359 */     this.manager.popAll();
/*     */   }
/*     */   
/*     */   private static PackLoadFeedback createPackResponseSender(final Connection connection) {
/* 363 */     return new PackLoadFeedback() {
/*     */         public void reportUpdate(UUID id, PackLoadFeedback.Update result) {
/*     */           // Byte code:
/*     */           //   0: getstatic net/minecraft/client/resources/server/DownloadedPackSource.LOGGER : Lorg/slf4j/Logger;
/*     */           //   3: ldc 'Pack {} changed status to {}'
/*     */           //   5: aload_1
/*     */           //   6: aload_2
/*     */           //   7: invokeinterface debug : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
/*     */           //   12: getstatic net/minecraft/client/resources/server/DownloadedPackSource$8.$SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$Update : [I
/*     */           //   15: aload_2
/*     */           //   16: invokevirtual ordinal : ()I
/*     */           //   19: iaload
/*     */           //   20: lookupswitch default -> 48, 1 -> 58, 2 -> 64
/*     */           //   48: new java/lang/MatchException
/*     */           //   51: dup
/*     */           //   52: aconst_null
/*     */           //   53: aconst_null
/*     */           //   54: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */           //   57: athrow
/*     */           //   58: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.ACCEPTED : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   61: goto -> 67
/*     */           //   64: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.DOWNLOADED : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   67: astore_3
/*     */           //   68: aload_0
/*     */           //   69: getfield val$connection : Lnet/minecraft/network/Connection;
/*     */           //   72: new net/minecraft/network/protocol/common/ServerboundResourcePackPacket
/*     */           //   75: dup
/*     */           //   76: aload_1
/*     */           //   77: aload_3
/*     */           //   78: invokespecial <init> : (Ljava/util/UUID;Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;)V
/*     */           //   81: invokevirtual send : (Lnet/minecraft/network/protocol/Packet;)V
/*     */           //   84: return
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #366	-> 0
/*     */           //   #367	-> 12
/*     */           //   #368	-> 58
/*     */           //   #369	-> 64
/*     */           //   #367	-> 67
/*     */           //   #371	-> 68
/*     */           //   #372	-> 84
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	85	0	this	Lnet/minecraft/client/resources/server/DownloadedPackSource$6;
/*     */           //   0	85	1	id	Ljava/util/UUID;
/*     */           //   0	85	2	result	Lnet/minecraft/client/resources/server/PackLoadFeedback$Update;
/*     */           //   68	17	3	response	Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */         }
/*     */         
/*     */         public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult result) {
/*     */           // Byte code:
/*     */           //   0: getstatic net/minecraft/client/resources/server/DownloadedPackSource.LOGGER : Lorg/slf4j/Logger;
/*     */           //   3: ldc 'Pack {} changed status to {}'
/*     */           //   5: aload_1
/*     */           //   6: aload_2
/*     */           //   7: invokeinterface debug : (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
/*     */           //   12: getstatic net/minecraft/client/resources/server/DownloadedPackSource$8.$SwitchMap$net$minecraft$client$resources$server$PackLoadFeedback$FinalResult : [I
/*     */           //   15: aload_2
/*     */           //   16: invokevirtual ordinal : ()I
/*     */           //   19: iaload
/*     */           //   20: tableswitch default -> 56, 1 -> 66, 2 -> 72, 3 -> 78, 4 -> 84, 5 -> 90
/*     */           //   56: new java/lang/MatchException
/*     */           //   59: dup
/*     */           //   60: aconst_null
/*     */           //   61: aconst_null
/*     */           //   62: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */           //   65: athrow
/*     */           //   66: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.SUCCESSFULLY_LOADED : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   69: goto -> 93
/*     */           //   72: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.FAILED_DOWNLOAD : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   75: goto -> 93
/*     */           //   78: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.DECLINED : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   81: goto -> 93
/*     */           //   84: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.DISCARDED : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   87: goto -> 93
/*     */           //   90: getstatic net/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action.FAILED_RELOAD : Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */           //   93: astore_3
/*     */           //   94: aload_0
/*     */           //   95: getfield val$connection : Lnet/minecraft/network/Connection;
/*     */           //   98: new net/minecraft/network/protocol/common/ServerboundResourcePackPacket
/*     */           //   101: dup
/*     */           //   102: aload_1
/*     */           //   103: aload_3
/*     */           //   104: invokespecial <init> : (Ljava/util/UUID;Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;)V
/*     */           //   107: invokevirtual send : (Lnet/minecraft/network/protocol/Packet;)V
/*     */           //   110: return
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #376	-> 0
/*     */           //   #377	-> 12
/*     */           //   #378	-> 66
/*     */           //   #379	-> 72
/*     */           //   #380	-> 78
/*     */           //   #381	-> 84
/*     */           //   #382	-> 90
/*     */           //   #377	-> 93
/*     */           //   #384	-> 94
/*     */           //   #385	-> 110
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	111	0	this	Lnet/minecraft/client/resources/server/DownloadedPackSource$6;
/*     */           //   0	111	1	id	Ljava/util/UUID;
/*     */           //   0	111	2	result	Lnet/minecraft/client/resources/server/PackLoadFeedback$FinalResult;
/*     */           //   94	17	3	response	Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket$Action;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public void configureForServerControl(Connection connection, ServerPackManager.PackPromptStatus packPromptStatus) {
/* 390 */     this.packType = PackSource.SERVER;
/* 391 */     this.packFeedback = createPackResponseSender(connection);
/* 392 */     switch (packPromptStatus) {
/*     */       case ALLOWED:
/* 393 */         this.manager.allowServerPacks();
/*     */         break;
/*     */       case DECLINED:
/* 394 */         this.manager.rejectServerPacks();
/*     */         break;
/*     */       case PENDING:
/* 395 */         this.manager.resetPromptStatus();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void configureForLocalWorld() {
/* 400 */     this.packType = PackSource.WORLD;
/* 401 */     this.packFeedback = LOG_ONLY_FEEDBACK;
/* 402 */     this.manager.allowServerPacks();
/*     */   }
/*     */   
/*     */   public void allowServerPacks() {
/* 406 */     this.manager.allowServerPacks();
/*     */   }
/*     */   
/*     */   public void rejectServerPacks() {
/* 410 */     this.manager.rejectServerPacks();
/*     */   }
/*     */   
/*     */   public CompletableFuture<Void> waitForPackFeedback(final UUID packId) {
/* 414 */     final CompletableFuture<Void> result = new CompletableFuture<>();
/* 415 */     final PackLoadFeedback original = this.packFeedback;
/* 416 */     this.packFeedback = new PackLoadFeedback() {
/*     */         public void reportUpdate(UUID id, PackLoadFeedback.Update result) {
/* 419 */           original.reportUpdate(id, result);
/*     */         }
/*     */         
/*     */         public void reportFinalResult(UUID id, PackLoadFeedback.FinalResult status) {
/* 424 */           if (packId.equals(id)) {
/* 425 */             DownloadedPackSource.this.packFeedback = original;
/* 426 */             if (status == PackLoadFeedback.FinalResult.APPLIED) {
/* 427 */               result.complete(null);
/*     */             } else {
/* 429 */               result.completeExceptionally(new IllegalStateException("Failed to apply pack " + String.valueOf(id) + ", reason: " + String.valueOf(status)));
/*     */             } 
/*     */           } 
/* 432 */           original.reportFinalResult(id, status);
/*     */         }
/*     */       };
/* 435 */     return result;
/*     */   }
/*     */   
/*     */   public void cleanupAfterDisconnect() {
/* 439 */     this.manager.popAll();
/* 440 */     this.packFeedback = LOG_ONLY_FEEDBACK;
/* 441 */     this.manager.resetPromptStatus();
/*     */   }
/*     */   
/*     */   public void close() throws IOException {
/* 446 */     this.downloadQueue.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\DownloadedPackSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */