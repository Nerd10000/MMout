/*     */ package net.minecraft.server.players;
/*     */ 
/*     */ import java.util.Date;
/*     */ 
/*     */ class GameProfileInfo {
/*     */   private final NameAndId nameAndId;
/*     */   
/*     */   private final Date expirationDate;
/*     */   
/*     */   private volatile long lastAccess;
/*     */   
/*     */   private GameProfileInfo(NameAndId nameAndId, Date expirationDate) {
/* 233 */     this.nameAndId = nameAndId;
/* 234 */     this.expirationDate = expirationDate;
/*     */   }
/*     */   
/*     */   public NameAndId nameAndId() {
/* 238 */     return this.nameAndId;
/*     */   }
/*     */   
/*     */   public Date expirationDate() {
/* 242 */     return this.expirationDate;
/*     */   }
/*     */   
/*     */   public void setLastAccess(long currentOperation) {
/* 246 */     this.lastAccess = currentOperation;
/*     */   }
/*     */   
/*     */   public long lastAccess() {
/* 250 */     return this.lastAccess;
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\players\CachedUserNameToIdResolver$GameProfileInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */