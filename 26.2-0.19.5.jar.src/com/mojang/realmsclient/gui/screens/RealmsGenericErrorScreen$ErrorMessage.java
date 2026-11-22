/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ 
/*    */ import com.mojang.realmsclient.client.RealmsError;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ final class ErrorMessage extends Record {
/*    */   private final Component title;
/*    */   
/*    */   private final Component detail;
/*    */   
/*    */   private ErrorMessage(Component title, Component detail) {
/* 69 */     this.title = title;
/* 69 */     this.detail = detail;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #69	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/gui/screens/RealmsGenericErrorScreen$ErrorMessage;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public Component title() {
/* 69 */     return this.title;
/*    */   }
/*    */   
/*    */   public Component detail() {
/* 69 */     return this.detail;
/*    */   }
/*    */   
/*    */   private static ErrorMessage forServiceError(RealmsServiceException realmsServiceException) {
/* 71 */     RealmsError errorDetails = realmsServiceException.realmsError;
/* 72 */     return new ErrorMessage((Component)Component.translatable("mco.errorMessage.realmsService.realmsError", new Object[] { Integer.valueOf(errorDetails.errorCode()) }), errorDetails.errorMessage());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\screens\RealmsGenericErrorScreen$ErrorMessage.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */