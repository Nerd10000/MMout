/*    */ package net.minecraft;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DefaultUncaughtExceptionHandlerWithName implements Thread.UncaughtExceptionHandler {
/*    */   private final Logger logger;
/*    */   
/*    */   public DefaultUncaughtExceptionHandlerWithName(Logger logger) {
/*  9 */     this.logger = logger;
/*    */   }
/*    */   
/*    */   public void uncaughtException(Thread t, Throwable e) {
/* 14 */     this.logger.error("Caught previously unhandled exception :");
/* 15 */     this.logger.error(t.getName(), e);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\DefaultUncaughtExceptionHandlerWithName.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */