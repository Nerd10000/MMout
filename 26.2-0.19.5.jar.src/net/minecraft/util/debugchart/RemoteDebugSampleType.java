/*    */ package net.minecraft.util.debugchart;
/*    */ 
/*    */ import net.minecraft.util.debug.DebugSubscription;
/*    */ import net.minecraft.util.debug.DebugSubscriptions;
/*    */ 
/*    */ public enum RemoteDebugSampleType {
/*  7 */   TICK_TIME(DebugSubscriptions.DEDICATED_SERVER_TICK_TIME);
/*    */   
/*    */   private final DebugSubscription<?> subscription;
/*    */   
/*    */   RemoteDebugSampleType(DebugSubscription<?> subscription) {
/* 13 */     this.subscription = subscription;
/*    */   }
/*    */   
/*    */   public DebugSubscription<?> subscription() {
/* 17 */     return this.subscription;
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debugchart\RemoteDebugSampleType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */