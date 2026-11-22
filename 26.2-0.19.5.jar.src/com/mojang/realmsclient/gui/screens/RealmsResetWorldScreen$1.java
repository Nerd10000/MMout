/*     */ package com.mojang.realmsclient.gui.screens;
/*     */ 
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.WorldTemplatePaginatedList;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ 
/*     */ class null extends Thread {
/*     */   null(String name) {
/* 116 */     super(name);
/*     */   }
/*     */   
/*     */   public void run() {
/* 119 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     try {
/* 121 */       WorldTemplatePaginatedList templates = client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.NORMAL);
/* 122 */       WorldTemplatePaginatedList adventuremaps = client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.ADVENTUREMAP);
/* 123 */       WorldTemplatePaginatedList experiences = client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.EXPERIENCE);
/* 124 */       WorldTemplatePaginatedList inspirations = client.fetchWorldTemplates(1, 10, RealmsServer.WorldType.INSPIRATION);
/* 125 */       RealmsResetWorldScreen.access$000(RealmsResetWorldScreen.this).execute(() -> {
/*     */             RealmsResetWorldScreen.this.templates = templates;
/*     */             RealmsResetWorldScreen.this.adventuremaps = adventuremaps;
/*     */             RealmsResetWorldScreen.this.experiences = experiences;
/*     */             RealmsResetWorldScreen.this.inspirations = inspirations;
/*     */           });
/* 131 */     } catch (RealmsServiceException e) {
/* 132 */       RealmsResetWorldScreen.LOGGER.error("Couldn't fetch templates in reset world", (Throwable)e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsResetWorldScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */