/*    */ package net.minecraft.util.debugchart;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ClientboundDebugSamplePacket;
/*    */ import net.minecraft.util.debug.ServerDebugSubscribers;
/*    */ 
/*    */ public class RemoteSampleLogger extends AbstractSampleLogger {
/*    */   private final ServerDebugSubscribers subscribers;
/*    */   
/*    */   private final RemoteDebugSampleType sampleType;
/*    */   
/*    */   public RemoteSampleLogger(int dimensions, ServerDebugSubscribers subscribers, RemoteDebugSampleType sampleType) {
/* 11 */     this(dimensions, subscribers, sampleType, new long[dimensions]);
/*    */   }
/*    */   
/*    */   public RemoteSampleLogger(int dimensions, ServerDebugSubscribers subscribers, RemoteDebugSampleType sampleType, long[] defaults) {
/* 15 */     super(dimensions, defaults);
/* 16 */     this.subscribers = subscribers;
/* 17 */     this.sampleType = sampleType;
/*    */   }
/*    */   
/*    */   protected void useSample() {
/* 22 */     if (this.subscribers.hasAnySubscriberFor(this.sampleType.subscription()))
/* 23 */       this.subscribers.broadcastToAll(this.sampleType.subscription(), (Packet)new ClientboundDebugSamplePacket((long[])this.sample.clone(), this.sampleType)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\debugchart\RemoteSampleLogger.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */