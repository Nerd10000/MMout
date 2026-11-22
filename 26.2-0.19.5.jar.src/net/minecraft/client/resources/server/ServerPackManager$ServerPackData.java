/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import com.google.common.hash.HashCode;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.util.UUID;
/*     */ 
/*     */ class ServerPackData {
/*     */   private final UUID id;
/*     */   
/*     */   private final URL url;
/*     */   
/*     */   private final HashCode hash;
/*     */   
/*     */   private Path path;
/*     */   
/*     */   private ServerPackManager.RemovalReason removalReason;
/*     */   
/* 103 */   private ServerPackManager.PackDownloadStatus downloadStatus = ServerPackManager.PackDownloadStatus.REQUESTED;
/*     */   
/* 104 */   private ServerPackManager.ActivationStatus activationStatus = ServerPackManager.ActivationStatus.INACTIVE;
/*     */   
/*     */   private boolean promptAccepted;
/*     */   
/*     */   private ServerPackData(UUID id, URL url, HashCode hash) {
/* 108 */     this.id = id;
/* 109 */     this.url = url;
/* 110 */     this.hash = hash;
/*     */   }
/*     */   
/*     */   public void setRemovalReasonIfNotSet(ServerPackManager.RemovalReason removalReason) {
/* 114 */     if (this.removalReason == null)
/* 115 */       this.removalReason = removalReason; 
/*     */   }
/*     */   
/*     */   public boolean isRemoved() {
/* 120 */     return (this.removalReason != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\ServerPackManager$ServerPackData.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */