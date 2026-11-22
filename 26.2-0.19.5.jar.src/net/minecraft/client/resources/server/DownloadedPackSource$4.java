/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.mojang.util.UndashedUuid;
/*     */ import java.net.Proxy;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.WorldVersion;
/*     */ import net.minecraft.client.User;
/*     */ import net.minecraft.server.packs.DownloadQueue;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ 
/*     */ class null implements PackDownloader {
/*     */   private static final int MAX_PACK_SIZE_BYTES = 262144000;
/*     */   
/* 192 */   private static final HashFunction CACHE_HASHING_FUNCTION = Hashing.sha1();
/*     */   
/*     */   private Map<String, String> createDownloadHeaders() {
/* 195 */     WorldVersion version = SharedConstants.getCurrentVersion();
/* 196 */     return Map.of("X-Minecraft-Username", user
/* 197 */         .getName(), "X-Minecraft-UUID", 
/* 198 */         UndashedUuid.toString(user.getProfileId()), "X-Minecraft-Version", version
/* 199 */         .name(), "X-Minecraft-Version-ID", version
/* 200 */         .id(), "X-Minecraft-Pack-Format", 
/* 201 */         String.valueOf(version.packVersion(PackType.CLIENT_RESOURCES)), "User-Agent", "Minecraft Java/" + version
/* 202 */         .name());
/*     */   }
/*     */   
/*     */   public void download(Map<UUID, DownloadQueue.DownloadRequest> requests, Consumer<DownloadQueue.BatchResult> output) {
/* 207 */     downloadQueue.downloadBatch(new DownloadQueue.BatchConfig(CACHE_HASHING_FUNCTION, 262144000, 
/*     */           
/* 211 */           createDownloadHeaders(), proxy, DownloadedPackSource.this
/*     */           
/* 213 */           .createDownloadNotifier(requests.size())), requests)
/*     */       
/* 216 */       .thenAcceptAsync(output, mainThreadExecutor);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\DownloadedPackSource$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */