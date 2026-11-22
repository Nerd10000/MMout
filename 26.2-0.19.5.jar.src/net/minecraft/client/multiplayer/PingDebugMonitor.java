/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*    */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*    */ 
/*    */ public class PingDebugMonitor {
/*    */   private final ClientPacketListener connection;
/*    */   
/*    */   private final LocalSampleLogger delayTimer;
/*    */   
/*    */   public PingDebugMonitor(ClientPacketListener connection, LocalSampleLogger delayTimer) {
/* 13 */     this.connection = connection;
/* 14 */     this.delayTimer = delayTimer;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     this.connection.send((Packet<?>)new ServerboundPingRequestPacket(Util.getMillis()));
/*    */   }
/*    */   
/*    */   public void onPongReceived(ClientboundPongResponsePacket packet) {
/* 22 */     this.delayTimer.logSample(Util.getMillis() - packet.time());
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\multiplayer\PingDebugMonitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */