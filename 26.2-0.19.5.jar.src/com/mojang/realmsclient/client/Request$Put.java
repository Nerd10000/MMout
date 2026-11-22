/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.mojang.realmsclient.exception.RealmsHttpException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ public class Put extends Request<Request.Put> {
/*     */   private final String content;
/*     */   
/*     */   public Put(String uri, String content, int connectTimeout, int readTimeout) {
/* 242 */     super(uri, connectTimeout, readTimeout);
/* 243 */     this.content = content;
/*     */   }
/*     */   
/*     */   public Put doConnect() {
/*     */     try {
/* 249 */       if (this.content != null)
/* 250 */         this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8"); 
/* 253 */       this.connection.setDoOutput(true);
/* 254 */       this.connection.setDoInput(true);
/* 255 */       this.connection.setRequestMethod("PUT");
/* 256 */       OutputStream out = this.connection.getOutputStream();
/* 257 */       OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
/* 258 */       writer.write(this.content);
/* 259 */       writer.close();
/* 260 */       out.flush();
/* 261 */       return this;
/* 262 */     } catch (Exception e) {
/* 263 */       throw new RealmsHttpException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\Request$Put.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */