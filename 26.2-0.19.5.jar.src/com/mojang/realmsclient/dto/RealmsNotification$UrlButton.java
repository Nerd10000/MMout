/*     */ package com.mojang.realmsclient.dto;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.realmsclient.util.JsonUtils;
/*     */ 
/*     */ final class UrlButton extends Record {
/*     */   private final String url;
/*     */   
/*     */   private final RealmsText urlText;
/*     */   
/*     */   private static final String URL = "url";
/*     */   
/*     */   private static final String URL_TEXT = "urlText";
/*     */   
/*     */   private UrlButton(String url, RealmsText urlText) {
/* 185 */     this.url = url;
/* 185 */     this.urlText = urlText;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #185	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #185	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #185	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsNotification$UrlButton;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public String url() {
/* 185 */     return this.url;
/*     */   }
/*     */   
/*     */   public RealmsText urlText() {
/* 185 */     return this.urlText;
/*     */   }
/*     */   
/*     */   public static UrlButton parse(JsonObject jsonObject) {
/* 190 */     String url = JsonUtils.getRequiredString("url", jsonObject);
/* 191 */     RealmsText urlText = (RealmsText)JsonUtils.getRequired("urlText", jsonObject, RealmsText::parse);
/* 192 */     return new UrlButton(url, urlText);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\dto\RealmsNotification$UrlButton.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */