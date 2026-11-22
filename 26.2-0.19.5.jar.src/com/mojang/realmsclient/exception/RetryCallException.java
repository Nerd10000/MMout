/*    */ package com.mojang.realmsclient.exception;
/*    */ 
/*    */ import com.mojang.realmsclient.client.RealmsError;
/*    */ 
/*    */ public class RetryCallException extends RealmsServiceException {
/*    */   public static final int DEFAULT_DELAY = 5;
/*    */   
/*    */   public final int delaySeconds;
/*    */   
/*    */   public RetryCallException(int delaySeconds, int statusCode) {
/* 11 */     super((RealmsError)RealmsError.CustomError.retry(statusCode));
/* 13 */     if (delaySeconds < 0 || delaySeconds > 120) {
/* 14 */       this.delaySeconds = 5;
/*    */     } else {
/* 16 */       this.delaySeconds = delaySeconds;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\exception\RetryCallException.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */