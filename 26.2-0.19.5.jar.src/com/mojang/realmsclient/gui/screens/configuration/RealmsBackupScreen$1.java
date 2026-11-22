/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.Backup;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import java.util.List;
/*     */ 
/*     */ class null extends Thread {
/*     */   null(String name) {
/* 111 */     super(name);
/*     */   }
/*     */   
/*     */   public void run() {
/* 114 */     RealmsClient client = RealmsClient.getOrCreate();
/*     */     try {
/* 116 */       List<Backup> backups = client.backupsFor(RealmsBackupScreen.this.serverData.id).backups();
/* 117 */       RealmsBackupScreen.access$000(RealmsBackupScreen.this).execute(() -> {
/*     */             RealmsBackupScreen.this.backups = backups;
/*     */             RealmsBackupScreen.this.noBackups = RealmsBackupScreen.this.backups.isEmpty();
/*     */             if (!RealmsBackupScreen.this.noBackups && RealmsBackupScreen.this.downloadButton != null)
/*     */               RealmsBackupScreen.this.downloadButton.active = true; 
/*     */             if (RealmsBackupScreen.this.backupList != null)
/*     */               RealmsBackupScreen.this.backupList.replaceEntries(RealmsBackupScreen.this.backups.stream().map(()).toList()); 
/*     */           });
/* 127 */     } catch (RealmsServiceException e) {
/* 128 */       RealmsBackupScreen.LOGGER.error("Couldn't request backups", (Throwable)e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\configuration\RealmsBackupScreen$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */