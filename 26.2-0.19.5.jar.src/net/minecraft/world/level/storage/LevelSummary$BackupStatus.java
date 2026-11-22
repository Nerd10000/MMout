/*     */ package net.minecraft.world.level.storage;
/*     */ 
/*     */ public enum BackupStatus {
/* 217 */   NONE(false, false, ""),
/* 218 */   DOWNGRADE(true, true, "downgrade"),
/* 219 */   UPGRADE_TO_SNAPSHOT(true, false, "snapshot"),
/* 220 */   FILE_FIXING_REQUIRED(true, false, "file_fixing_required");
/*     */   
/*     */   private final boolean shouldBackup;
/*     */   
/*     */   private final boolean severe;
/*     */   
/*     */   private final String translationKey;
/*     */   
/*     */   BackupStatus(boolean shouldBackup, boolean severe, String translationKey) {
/* 227 */     this.shouldBackup = shouldBackup;
/* 228 */     this.severe = severe;
/* 229 */     this.translationKey = translationKey;
/*     */   }
/*     */   
/*     */   public boolean shouldBackup() {
/* 233 */     return this.shouldBackup;
/*     */   }
/*     */   
/*     */   public boolean isSevere() {
/* 237 */     return this.severe;
/*     */   }
/*     */   
/*     */   public String getTranslationKey() {
/* 241 */     return this.translationKey;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\storage\LevelSummary$BackupStatus.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */