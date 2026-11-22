/*     */ package com.mojang.realmsclient.client;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.exception.RealmsHttpException;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.GsonHelper;
/*     */ import net.minecraft.util.LenientJsonParser;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public interface RealmsError {
/*  17 */   public static final Component NO_MESSAGE = (Component)Component.translatable("mco.errorMessage.noDetails");
/*     */   
/*  19 */   public static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   int errorCode();
/*     */   
/*     */   Component errorMessage();
/*     */   
/*     */   String logMessage();
/*     */   
/*     */   public static final class ErrorWithJsonPayload extends Record implements RealmsError {
/*     */     private final int httpCode;
/*     */     
/*     */     private final int code;
/*     */     
/*     */     private final String reason;
/*     */     
/*     */     private final String message;
/*     */     
/*     */     public ErrorWithJsonPayload(int httpCode, int code, String reason, String message) {
/*  27 */       this.httpCode = httpCode;
/*  27 */       this.code = code;
/*  27 */       this.reason = reason;
/*  27 */       this.message = message;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #27	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int httpCode() {
/*  27 */       return this.httpCode;
/*     */     }
/*     */     
/*     */     public int code() {
/*  27 */       return this.code;
/*     */     }
/*     */     
/*     */     public String reason() {
/*  27 */       return this.reason;
/*     */     }
/*     */     
/*     */     public String message() {
/*  27 */       return this.message;
/*     */     }
/*     */     
/*     */     public int errorCode() {
/*  30 */       return this.code;
/*     */     }
/*     */     
/*     */     public Component errorMessage() {
/*  35 */       String codeTranslationKey = "mco.errorMessage." + this.code;
/*  36 */       if (Language.getInstance().has(codeTranslationKey))
/*  37 */         return (Component)Component.translatable(codeTranslationKey); 
/*  40 */       if (this.reason != null) {
/*  41 */         String reasonTranslationKey = "mco.errorReason." + this.reason;
/*  42 */         if (Language.getInstance().has(reasonTranslationKey))
/*  43 */           return (Component)Component.translatable(reasonTranslationKey); 
/*     */       } 
/*  47 */       return (this.message != null) ? (Component)Component.literal(this.message) : NO_MESSAGE;
/*     */     }
/*     */     
/*     */     public String logMessage() {
/*  52 */       return String.format(Locale.ROOT, "Realms service error (%d/%d/%s) with message '%s'", new Object[] { Integer.valueOf(this.httpCode), Integer.valueOf(this.code), this.reason, this.message });
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ErrorWithRawPayload extends Record implements RealmsError {
/*     */     private final int httpCode;
/*     */     
/*     */     private final String payload;
/*     */     
/*     */     public ErrorWithRawPayload(int httpCode, String payload) {
/*  56 */       this.httpCode = httpCode;
/*  56 */       this.payload = payload;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #56	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #56	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #56	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int httpCode() {
/*  56 */       return this.httpCode;
/*     */     }
/*     */     
/*     */     public String payload() {
/*  56 */       return this.payload;
/*     */     }
/*     */     
/*     */     public int errorCode() {
/*  59 */       return this.httpCode;
/*     */     }
/*     */     
/*     */     public Component errorMessage() {
/*  64 */       return (Component)Component.literal(this.payload);
/*     */     }
/*     */     
/*     */     public String logMessage() {
/*  69 */       return String.format(Locale.ROOT, "Realms service error (%d) with raw payload '%s'", new Object[] { Integer.valueOf(this.httpCode), this.payload });
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class AuthenticationError extends Record implements RealmsError {
/*     */     private final String message;
/*     */     
/*     */     public static final int ERROR_CODE = 401;
/*     */     
/*     */     public AuthenticationError(String message) {
/*  73 */       this.message = message;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #73	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #73	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #73	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public String message() {
/*  73 */       return this.message;
/*     */     }
/*     */     
/*     */     public int errorCode() {
/*  78 */       return 401;
/*     */     }
/*     */     
/*     */     public Component errorMessage() {
/*  83 */       return (Component)Component.literal(this.message);
/*     */     }
/*     */     
/*     */     public String logMessage() {
/*  88 */       return String.format(Locale.ROOT, "Realms authentication error with message '%s'", new Object[] { this.message });
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class CustomError extends Record implements RealmsError {
/*     */     private final int httpCode;
/*     */     
/*     */     private final Component payload;
/*     */     
/*     */     public CustomError(int httpCode, Component payload) {
/*  92 */       this.httpCode = httpCode;
/*  92 */       this.payload = payload;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/client/RealmsError$CustomError;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$CustomError;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/client/RealmsError$CustomError;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$CustomError;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/client/RealmsError$CustomError;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #92	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/client/RealmsError$CustomError;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public int httpCode() {
/*  92 */       return this.httpCode;
/*     */     }
/*     */     
/*     */     public Component payload() {
/*  92 */       return this.payload;
/*     */     }
/*     */     
/*  93 */     public static final CustomError SERVICE_BUSY = new CustomError(429, (Component)Component.translatable("mco.errorMessage.serviceBusy"));
/*     */     
/*  94 */     public static final Component RETRY_MESSAGE = (Component)Component.translatable("mco.errorMessage.retry");
/*     */     
/*     */     public static final String BODY_TAG = "<body>";
/*     */     
/*     */     public static final String CLOSING_BODY_TAG = "</body>";
/*     */     
/*     */     public static CustomError unknownCompatibilityResponse(String response) {
/*  99 */       return new CustomError(500, (Component)Component.translatable("mco.errorMessage.realmsService.unknownCompatibility", new Object[] { response }));
/*     */     }
/*     */     
/*     */     public static CustomError configurationError() {
/* 103 */       return new CustomError(500, (Component)Component.translatable("mco.errorMessage.realmsService.configurationError"));
/*     */     }
/*     */     
/*     */     public static CustomError connectivityError(RealmsHttpException exception) {
/* 107 */       return new CustomError(500, (Component)Component.translatable("mco.errorMessage.realmsService.connectivity", new Object[] { exception.getMessage() }));
/*     */     }
/*     */     
/*     */     public static CustomError retry(int statusCode) {
/* 111 */       return new CustomError(statusCode, RETRY_MESSAGE);
/*     */     }
/*     */     
/*     */     public static CustomError noPayload(int statusCode) {
/* 115 */       return new CustomError(statusCode, null);
/*     */     }
/*     */     
/*     */     public static CustomError htmlPayload(int statusCode, String payload) {
/* 119 */       int bodyStart = payload.indexOf("<body>");
/* 120 */       int bodyEnd = payload.indexOf("</body>");
/* 121 */       if (bodyStart >= 0 && bodyEnd > bodyStart)
/* 122 */         return new CustomError(statusCode, (Component)Component.literal(payload.substring(bodyStart + "<body>".length(), bodyEnd).trim())); 
/* 125 */       LOGGER.error("Got an error with an unreadable html body {}", payload);
/* 126 */       return new CustomError(statusCode, null);
/*     */     }
/*     */     
/*     */     public int errorCode() {
/* 131 */       return this.httpCode;
/*     */     }
/*     */     
/*     */     public Component errorMessage() {
/* 136 */       return (this.payload != null) ? this.payload : NO_MESSAGE;
/*     */     }
/*     */     
/*     */     public String logMessage() {
/* 141 */       if (this.payload != null)
/* 142 */         return String.format(Locale.ROOT, "Realms service error (%d) with message '%s'", new Object[] { Integer.valueOf(this.httpCode), this.payload.getString() }); 
/* 144 */       return String.format(Locale.ROOT, "Realms service error (%d) with no payload", new Object[] { Integer.valueOf(this.httpCode) });
/*     */     }
/*     */   }
/*     */   
/*     */   static RealmsError parse(int httpCode, String payload) {
/* 150 */     if (httpCode == 429)
/* 152 */       return CustomError.SERVICE_BUSY; 
/* 155 */     if (Strings.isNullOrEmpty(payload))
/* 156 */       return CustomError.noPayload(httpCode); 
/*     */     try {
/* 167 */       JsonObject object = LenientJsonParser.parse(payload).getAsJsonObject();
/* 168 */       String errorReason = GsonHelper.getAsString(object, "reason", null);
/* 169 */       String errorMessage = GsonHelper.getAsString(object, "errorMsg", null);
/* 170 */       int errorCode = GsonHelper.getAsInt(object, "errorCode", -1);
/* 171 */       if (errorMessage != null || errorReason != null || errorCode != -1)
/* 172 */         return new ErrorWithJsonPayload(httpCode, 
/*     */             
/* 174 */             (errorCode != -1) ? errorCode : httpCode, errorReason, errorMessage); 
/* 180 */     } catch (Exception e) {
/* 181 */       LOGGER.error("Could not parse RealmsError", e);
/*     */     } 
/* 184 */     return new ErrorWithRawPayload(httpCode, payload);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\RealmsError.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */