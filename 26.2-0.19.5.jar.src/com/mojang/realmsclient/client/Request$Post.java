/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.mojang.realmsclient.exception.RealmsHttpException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ public class Post extends Request<Request.Post> {
/*     */   private final String content;
/*     */   
/*     */   public Post(String uri, String content, int connectTimeout, int readTimeout) {
/* 272 */     super(uri, connectTimeout, readTimeout);
/* 273 */     this.content = content;
/*     */   }
/*     */   
/*     */   public Post doConnect() {
/*     */     try {
/* 279 */       if (this.content != null)
/* 280 */         this.connection.setRequestProperty("Content-Type", "application/json; charset=utf-8"); 
/* 283 */       this.connection.setDoInput(true);
/* 284 */       this.connection.setDoOutput(true);
/* 285 */       this.connection.setUseCaches(false);
/* 286 */       this.connection.setRequestMethod("POST");
/* 287 */       OutputStream out = this.connection.getOutputStream();
/* 288 */       OutputStreamWriter writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);
/* 289 */       writer.write(this.content);
/* 290 */       writer.close();
/* 291 */       out.flush();
/* 292 */       return this;
/* 293 */     } catch (Exception e) {
/* 294 */       throw new RealmsHttpException(e.getMessage(), e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\Request$Post.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */