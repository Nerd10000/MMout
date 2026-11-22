/*     */ package net.minecraft.server.network;
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface IgnoreStrategy {
/*     */   public static final IgnoreStrategy NEVER_IGNORE = (message, removedCharCount) -> false;
/*     */   
/*     */   public static final IgnoreStrategy IGNORE_FULLY_FILTERED;
/*     */   
/*     */   static {
/* 232 */     IGNORE_FULLY_FILTERED = ((message, removedCharCount) -> (message.length() == removedCharCount));
/*     */   }
/*     */   
/*     */   static IgnoreStrategy ignoreOverThreshold(int threshold) {
/* 235 */     return (message, removedCharCount) -> (removedCharCount >= threshold);
/*     */   }
/*     */   
/*     */   static IgnoreStrategy select(int hashesToDrop) {
/* 239 */     switch (hashesToDrop) {
/*     */       case -1:
/*     */       
/*     */       case 0:
/*     */       
/*     */     } 
/* 239 */     return 
/*     */       
/* 242 */       ignoreOverThreshold(hashesToDrop);
/*     */   }
/*     */   
/*     */   boolean shouldIgnore(String paramString, int paramInt);
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerTextFilter$IgnoreStrategy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */