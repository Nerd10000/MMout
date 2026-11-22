/*    */ package com.mojang.realmsclient.client;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class ErrorWithRawPayload extends Record implements RealmsError {
/*    */   private final int httpCode;
/*    */   
/*    */   private final String payload;
/*    */   
/*    */   public ErrorWithRawPayload(int httpCode, String payload) {
/* 56 */     this.httpCode = httpCode;
/* 56 */     this.payload = payload;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #56	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/client/RealmsError$ErrorWithRawPayload;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public int httpCode() {
/* 56 */     return this.httpCode;
/*    */   }
/*    */   
/*    */   public String payload() {
/* 56 */     return this.payload;
/*    */   }
/*    */   
/*    */   public int errorCode() {
/* 59 */     return this.httpCode;
/*    */   }
/*    */   
/*    */   public Component errorMessage() {
/* 64 */     return (Component)Component.literal(this.payload);
/*    */   }
/*    */   
/*    */   public String logMessage() {
/* 69 */     return String.format(Locale.ROOT, "Realms service error (%d) with raw payload '%s'", new Object[] { Integer.valueOf(this.httpCode), this.payload });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\RealmsError$ErrorWithRawPayload.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */