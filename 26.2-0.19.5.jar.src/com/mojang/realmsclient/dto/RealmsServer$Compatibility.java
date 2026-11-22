/*     */ package com.mojang.realmsclient.dto;
/*     */ 
/*     */ public enum Compatibility {
/* 352 */   UNVERIFIABLE, INCOMPATIBLE, RELEASE_TYPE_INCOMPATIBLE, NEEDS_DOWNGRADE, NEEDS_UPGRADE, COMPATIBLE;
/*     */   
/*     */   public boolean isCompatible() {
/* 361 */     return (this == COMPATIBLE);
/*     */   }
/*     */   
/*     */   public boolean needsUpgrade() {
/* 365 */     return (this == NEEDS_UPGRADE);
/*     */   }
/*     */   
/*     */   public boolean needsDowngrade() {
/* 369 */     return (this == NEEDS_DOWNGRADE);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsServer$Compatibility.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */