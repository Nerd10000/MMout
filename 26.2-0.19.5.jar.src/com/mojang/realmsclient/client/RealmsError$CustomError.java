/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.mojang.realmsclient.exception.RealmsHttpException;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public final class CustomError extends Record implements RealmsError {
/*     */   private final int httpCode;
/*     */   
/*     */   private final Component payload;
/*     */   
/*     */   public CustomError(int httpCode, Component payload) {
/*  92 */     this.httpCode = httpCode;
/*  92 */     this.payload = payload;
/*     */   }
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/client/RealmsError$CustomError;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #92	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$CustomError;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/client/RealmsError$CustomError;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #92	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$CustomError;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/client/RealmsError$CustomError;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #92	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/realmsclient/client/RealmsError$CustomError;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public int httpCode() {
/*  92 */     return this.httpCode;
/*     */   }
/*     */   
/*     */   public Component payload() {
/*  92 */     return this.payload;
/*     */   }
/*     */   
/*  93 */   public static final CustomError SERVICE_BUSY = new CustomError(429, (Component)Component.translatable("mco.errorMessage.serviceBusy"));
/*     */   
/*  94 */   public static final Component RETRY_MESSAGE = (Component)Component.translatable("mco.errorMessage.retry");
/*     */   
/*     */   public static final String BODY_TAG = "<body>";
/*     */   
/*     */   public static final String CLOSING_BODY_TAG = "</body>";
/*     */   
/*     */   public static CustomError unknownCompatibilityResponse(String response) {
/*  99 */     return new CustomError(500, (Component)Component.translatable("mco.errorMessage.realmsService.unknownCompatibility", new Object[] { response }));
/*     */   }
/*     */   
/*     */   public static CustomError configurationError() {
/* 103 */     return new CustomError(500, (Component)Component.translatable("mco.errorMessage.realmsService.configurationError"));
/*     */   }
/*     */   
/*     */   public static CustomError connectivityError(RealmsHttpException exception) {
/* 107 */     return new CustomError(500, (Component)Component.translatable("mco.errorMessage.realmsService.connectivity", new Object[] { exception.getMessage() }));
/*     */   }
/*     */   
/*     */   public static CustomError retry(int statusCode) {
/* 111 */     return new CustomError(statusCode, RETRY_MESSAGE);
/*     */   }
/*     */   
/*     */   public static CustomError noPayload(int statusCode) {
/* 115 */     return new CustomError(statusCode, null);
/*     */   }
/*     */   
/*     */   public static CustomError htmlPayload(int statusCode, String payload) {
/* 119 */     int bodyStart = payload.indexOf("<body>");
/* 120 */     int bodyEnd = payload.indexOf("</body>");
/* 121 */     if (bodyStart >= 0 && bodyEnd > bodyStart)
/* 122 */       return new CustomError(statusCode, (Component)Component.literal(payload.substring(bodyStart + "<body>".length(), bodyEnd).trim())); 
/* 125 */     LOGGER.error("Got an error with an unreadable html body {}", payload);
/* 126 */     return new CustomError(statusCode, null);
/*     */   }
/*     */   
/*     */   public int errorCode() {
/* 131 */     return this.httpCode;
/*     */   }
/*     */   
/*     */   public Component errorMessage() {
/* 136 */     return (this.payload != null) ? this.payload : NO_MESSAGE;
/*     */   }
/*     */   
/*     */   public String logMessage() {
/* 141 */     if (this.payload != null)
/* 142 */       return String.format(Locale.ROOT, "Realms service error (%d) with message '%s'", new Object[] { Integer.valueOf(this.httpCode), this.payload.getString() }); 
/* 144 */     return String.format(Locale.ROOT, "Realms service error (%d) with no payload", new Object[] { Integer.valueOf(this.httpCode) });
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\RealmsError$CustomError.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */