/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ 
/*    */ @Name("minecraft.PacketSent")
/*    */ @Label("Network Packet Sent")
/*    */ public class PacketSentEvent extends PacketEvent {
/*    */   public static final String NAME = "minecraft.PacketSent";
/*    */   
/* 17 */   public static final EventType TYPE = EventType.getEventType((Class)PacketSentEvent.class);
/*    */   
/*    */   public PacketSentEvent(String protocolId, String packetDirection, String packetId, SocketAddress remoteAddress, int writtenBytes) {
/* 20 */     super(protocolId, packetDirection, packetId, remoteAddress, writtenBytes);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\profiling\jfr\event\PacketSentEvent.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */