/*    */ package com.mojang.realmsclient.exception;
/*    */ 
/*    */ import com.mojang.realmsclient.client.RealmsError;
/*    */ 
/*    */ public class RealmsServiceException extends Exception {
/*    */   public final RealmsError realmsError;
/*    */   
/*    */   public RealmsServiceException(RealmsError error) {
/*  9 */     this.realmsError = error;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 14 */     return this.realmsError.logMessage();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\exception\RealmsServiceException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */