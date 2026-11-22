/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.mojang.realmsclient.exception.RealmsHttpException;
/*     */ 
/*     */ public class Delete extends Request<Request.Delete> {
/*     */   public Delete(String uri, int connectTimeout, int readTimeout) {
/* 203 */     super(uri, connectTimeout, readTimeout);
/*     */   }
/*     */   
/*     */   public Delete doConnect() {
/*     */     try {
/* 209 */       this.connection.setDoOutput(true);
/* 210 */       this.connection.setRequestMethod("DELETE");
/* 211 */       this.connection.connect();
/* 212 */       return this;
/* 213 */     } catch (Exception e) {
/* 214 */       throw new RealmsHttpException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\Request$Delete.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */