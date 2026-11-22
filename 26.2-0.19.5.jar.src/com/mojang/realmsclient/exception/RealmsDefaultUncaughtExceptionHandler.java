/*    */ package com.mojang.realmsclient.exception;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RealmsDefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
/*    */   private final Logger logger;
/*    */   
/*    */   public RealmsDefaultUncaughtExceptionHandler(Logger logger) {
/*  9 */     this.logger = logger;
/*    */   }
/*    */   
/*    */   public void uncaughtException(Thread t, Throwable e) {
/* 14 */     this.logger.error("Caught previously unhandled exception", e);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\exception\RealmsDefaultUncaughtExceptionHandler.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */