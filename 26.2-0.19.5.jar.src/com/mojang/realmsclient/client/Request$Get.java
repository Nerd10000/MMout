/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.mojang.realmsclient.exception.RealmsHttpException;
/*     */ 
/*     */ public class Get extends Request<Request.Get> {
/*     */   public Get(String uri, int connectTimeout, int readTimeout) {
/* 221 */     super(uri, connectTimeout, readTimeout);
/*     */   }
/*     */   
/*     */   public Get doConnect() {
/*     */     try {
/* 227 */       this.connection.setDoInput(true);
/* 228 */       this.connection.setDoOutput(true);
/* 229 */       this.connection.setUseCaches(false);
/* 230 */       this.connection.setRequestMethod("GET");
/* 231 */       return this;
/* 232 */     } catch (Exception e) {
/* 233 */       throw new RealmsHttpException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\Request$Get.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */