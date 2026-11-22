/*     */ package net.minecraft.client.resources.server;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ class null implements PackReloadConfig.Callbacks {
/*     */   public void onSuccess() {
/* 359 */     for (ServerPackManager.ServerPackData pack : packsToLoad) {
/* 360 */       pack.activationStatus = ServerPackManager.ActivationStatus.ACTIVE;
/* 361 */       if (pack.removalReason == null)
/* 362 */         ServerPackManager.this.packLoadFeedback.reportFinalResult(pack.id, PackLoadFeedback.FinalResult.APPLIED); 
/*     */     } 
/* 365 */     for (ServerPackManager.ServerPackData pack : packsToUnload)
/* 366 */       pack.activationStatus = ServerPackManager.ActivationStatus.INACTIVE; 
/* 368 */     ServerPackManager.this.registerForUpdate();
/*     */   }
/*     */   
/*     */   public void onFailure(boolean isRecovery) {
/* 373 */     if (!isRecovery) {
/* 375 */       packsToLoad.clear();
/* 376 */       for (ServerPackManager.ServerPackData pack : ServerPackManager.this.packs) {
/* 377 */         switch (pack.activationStatus.ordinal()) {
/*     */           case 2:
/* 378 */             packsToLoad.add(pack);
/*     */           case 1:
/* 380 */             pack.activationStatus = ServerPackManager.ActivationStatus.INACTIVE;
/* 381 */             pack.setRemovalReasonIfNotSet(ServerPackManager.RemovalReason.ACTIVATION_FAILED);
/*     */           case 0:
/* 383 */             pack.setRemovalReasonIfNotSet(ServerPackManager.RemovalReason.DISCARDED);
/*     */         } 
/*     */       } 
/* 386 */       ServerPackManager.this.registerForUpdate();
/*     */     } else {
/* 389 */       for (ServerPackManager.ServerPackData pack : ServerPackManager.this.packs) {
/* 390 */         if (pack.activationStatus == ServerPackManager.ActivationStatus.PENDING)
/* 391 */           pack.activationStatus = ServerPackManager.ActivationStatus.INACTIVE; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<PackReloadConfig.IdAndPath> packsToLoad() {
/* 399 */     return packsToLoad.stream().map(pack -> new PackReloadConfig.IdAndPath(pack.id, pack.path)).toList();
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\ServerPackManager$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */