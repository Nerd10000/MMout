/*    */ package net.minecraft.network;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*    */ 
/*    */ public class BandwidthDebugMonitor {
/*  8 */   private final AtomicInteger bytesReceived = new AtomicInteger();
/*    */   
/*    */   private final LocalSampleLogger bandwidthLogger;
/*    */   
/*    */   public BandwidthDebugMonitor(LocalSampleLogger bandwidthLogger) {
/* 12 */     this.bandwidthLogger = bandwidthLogger;
/*    */   }
/*    */   
/*    */   public void onReceive(int bytes) {
/* 16 */     this.bytesReceived.getAndAdd(bytes);
/*    */   }
/*    */   
/*    */   public void tick() {
/* 20 */     this.bandwidthLogger.logSample(this.bytesReceived.getAndSet(0));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\BandwidthDebugMonitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */