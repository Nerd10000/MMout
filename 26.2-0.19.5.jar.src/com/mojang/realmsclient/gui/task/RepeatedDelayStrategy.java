/*    */ package com.mojang.realmsclient.gui.task;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public interface RepeatedDelayStrategy {
/*  7 */   public static final RepeatedDelayStrategy CONSTANT = new RepeatedDelayStrategy() {
/*    */       public long delayCyclesAfterSuccess() {
/* 10 */         return 1L;
/*    */       }
/*    */       
/*    */       public long delayCyclesAfterFailure() {
/* 15 */         return 1L;
/*    */       }
/*    */     };
/*    */   
/*    */   long delayCyclesAfterSuccess();
/*    */   
/*    */   long delayCyclesAfterFailure();
/*    */   
/*    */   static RepeatedDelayStrategy exponentialBackoff(final int maxBackoff) {
/* 24 */     return new RepeatedDelayStrategy() {
/* 25 */         private static final Logger LOGGER = LogUtils.getLogger();
/*    */         
/*    */         private int failureCount;
/*    */         
/*    */         public long delayCyclesAfterSuccess() {
/* 31 */           this.failureCount = 0;
/* 32 */           return 1L;
/*    */         }
/*    */         
/*    */         public long delayCyclesAfterFailure() {
/* 37 */           this.failureCount++;
/* 38 */           long expandedDelay = Math.min(1L << this.failureCount, maxBackoff);
/* 39 */           LOGGER.debug("Skipping for {} extra cycles", Long.valueOf(expandedDelay));
/* 40 */           return expandedDelay;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\task\RepeatedDelayStrategy.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */