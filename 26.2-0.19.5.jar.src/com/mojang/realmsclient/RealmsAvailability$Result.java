/*    */ package com.mojang.realmsclient;
/*    */ 
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ 
/*    */ public final class Result extends Record {
/*    */   private final RealmsAvailability.Type type;
/*    */   
/*    */   private final RealmsServiceException exception;
/*    */   
/*    */   public Result(RealmsAvailability.Type type, RealmsServiceException exception) {
/* 66 */     this.type = type;
/* 66 */     this.exception = exception;
/*    */   }
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/RealmsAvailability$Result;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/RealmsAvailability$Result;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/RealmsAvailability$Result;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/RealmsAvailability$Result;
/*    */   }
/*    */   
/*    */   public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/RealmsAvailability$Result;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #66	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/RealmsAvailability$Result;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   }
/*    */   
/*    */   public RealmsAvailability.Type type() {
/* 66 */     return this.type;
/*    */   }
/*    */   
/*    */   public RealmsServiceException exception() {
/* 66 */     return this.exception;
/*    */   }
/*    */   
/*    */   public Result(RealmsAvailability.Type type) {
/* 68 */     this(type, null);
/*    */   }
/*    */   
/*    */   public Result(RealmsServiceException exception) {
/* 72 */     this(RealmsAvailability.Type.UNEXPECTED_ERROR, exception);
/*    */   }
/*    */   
/*    */   public Screen createErrorScreen(Screen lastScreen) {
/* 76 */     switch (this.type.ordinal()) {
/*    */       default:
/* 76 */         throw new MatchException(null, null);
/*    */       case 0:
/*    */       
/*    */       case 1:
/*    */       
/*    */       case 2:
/*    */       
/*    */       case 3:
/*    */       
/*    */       case 4:
/*    */         break;
/*    */     } 
/* 76 */     return 
/*    */       
/* 81 */       (Screen)new RealmsGenericErrorScreen(Objects.<RealmsServiceException>requireNonNull(this.exception), lastScreen);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\RealmsAvailability$Result.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */