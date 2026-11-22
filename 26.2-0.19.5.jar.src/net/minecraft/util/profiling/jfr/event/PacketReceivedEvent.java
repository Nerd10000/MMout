/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ 
/*    */ @Name("minecraft.PacketReceived")
/*    */ @Label("Network Packet Received")
/*    */ public class PacketReceivedEvent extends PacketEvent {
/*    */   public static final String NAME = "minecraft.PacketReceived";
/*    */   
/* 17 */   public static final EventType TYPE = EventType.getEventType((Class)PacketReceivedEvent.class);
/*    */   
/*    */   public PacketReceivedEvent(String protocolId, String packetDirection, String packetId, SocketAddress remoteAddress, int readableBytes) {
/* 20 */     super(protocolId, packetDirection, packetId, remoteAddress, readableBytes);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\event\PacketReceivedEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */