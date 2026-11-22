/*    */ package com.mojang.realmsclient.client;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public final class AuthenticationError extends Record implements RealmsError {
/*    */   private final String message;
/*    */   
/*    */   public static final int ERROR_CODE = 401;
/*    */   
/*    */   public AuthenticationError(String message) {
/* 73 */     this.message = message;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #73	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/client/RealmsError$AuthenticationError;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public String message() {
/* 73 */     return this.message;
/*    */   }
/*    */   
/*    */   public int errorCode() {
/* 78 */     return 401;
/*    */   }
/*    */   
/*    */   public Component errorMessage() {
/* 83 */     return (Component)Component.literal(this.message);
/*    */   }
/*    */   
/*    */   public String logMessage() {
/* 88 */     return String.format(Locale.ROOT, "Realms authentication error with message '%s'", new Object[] { this.message });
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\client\RealmsError$AuthenticationError.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */