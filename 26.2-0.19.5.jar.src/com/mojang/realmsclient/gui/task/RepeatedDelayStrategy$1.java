/*    */ package com.mojang.realmsclient.gui.task;
/*    */ 
/*    */ class null implements RepeatedDelayStrategy {
/*    */   public long delayCyclesAfterSuccess() {
/* 10 */     return 1L;
/*    */   }
/*    */   
/*    */   public long delayCyclesAfterFailure() {
/* 15 */     return 1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\com\mojang\realmsclient\gui\task\RepeatedDelayStrategy$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */