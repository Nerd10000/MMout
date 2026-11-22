/*    */ package com.mojang.realmsclient.client;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.locale.Language;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class ErrorWithJsonPayload extends Record implements RealmsError {
/*    */   private final int httpCode;
/*    */   
/*    */   private final int code;
/*    */   
/*    */   private final String reason;
/*    */   
/*    */   private final String message;
/*    */   
/*    */   public ErrorWithJsonPayload(int httpCode, int code, String reason, String message) {
/* 27 */     this.httpCode = httpCode;
/* 27 */     this.code = code;
/* 27 */     this.reason = reason;
/* 27 */     this.message = message;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #27	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithJsonPayload;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int httpCode() {
/* 27 */     return this.httpCode;
/*    */   }
/*    */   
/*    */   public int code() {
/* 27 */     return this.code;
/*    */   }
/*    */   
/*    */   public String reason() {
/* 27 */     return this.reason;
/*    */   }
/*    */   
/*    */   public String message() {
/* 27 */     return this.message;
/*    */   }
/*    */   
/*    */   public int errorCode() {
/* 30 */     return this.code;
/*    */   }
/*    */   
/*    */   public Component errorMessage() {
/* 35 */     String codeTranslationKey = "mco.errorMessage." + this.code;
/* 36 */     if (Language.getInstance().has(codeTranslationKey))
/* 37 */       return (Component)Component.translatable(codeTranslationKey); 
/* 40 */     if (this.reason != null) {
/* 41 */       String reasonTranslationKey = "mco.errorReason." + this.reason;
/* 42 */       if (Language.getInstance().has(reasonTranslationKey))
/* 43 */         return (Component)Component.translatable(reasonTranslationKey); 
/*    */     } 
/* 47 */     return (this.message != null) ? (Component)Component.literal(this.message) : NO_MESSAGE;
/*    */   }
/*    */   
/*    */   public String logMessage() {
/* 52 */     return String.format(Locale.ROOT, "Realms service error (%d/%d/%s) with message '%s'", new Object[] { Integer.valueOf(this.httpCode), Integer.valueOf(this.code), this.reason, this.message });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\RealmsError$ErrorWithJsonPayload.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */