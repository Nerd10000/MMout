/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ 
/*    */ public final class SumAggregation {
/* 63 */   private final AtomicLong sentBytes = new AtomicLong();
/*    */   
/* 64 */   private final AtomicInteger sentPackets = new AtomicInteger();
/*    */   
/* 65 */   private final AtomicLong receivedBytes = new AtomicLong();
/*    */   
/* 66 */   private final AtomicInteger receivedPackets = new AtomicInteger();
/*    */   
/*    */   private final NetworkSummaryEvent event;
/*    */   
/*    */   public SumAggregation(String remoteAddress) {
/* 70 */     this.event = new NetworkSummaryEvent(remoteAddress);
/* 71 */     this.event.begin();
/*    */   }
/*    */   
/*    */   public void trackSentPacket(int size) {
/* 75 */     this.sentPackets.incrementAndGet();
/* 76 */     this.sentBytes.addAndGet(size);
/*    */   }
/*    */   
/*    */   public void trackReceivedPacket(int size) {
/* 80 */     this.receivedPackets.incrementAndGet();
/* 81 */     this.receivedBytes.addAndGet(size);
/*    */   }
/*    */   
/*    */   public void commitEvent() {
/* 85 */     this.event.sentBytes = this.sentBytes.get();
/* 86 */     this.event.sentPackets = this.sentPackets.get();
/* 87 */     this.event.receivedBytes = this.receivedBytes.get();
/* 88 */     this.event.receivedPackets = this.receivedPackets.get();
/* 89 */     this.event.commit();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\event\NetworkSummaryEvent$SumAggregation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */