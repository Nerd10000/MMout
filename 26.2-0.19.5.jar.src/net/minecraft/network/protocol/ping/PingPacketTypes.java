/*    */ package net.minecraft.network.protocol.ping;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class PingPacketTypes {
/*  9 */   public static final PacketType<ClientboundPongResponsePacket> CLIENTBOUND_PONG_RESPONSE = createClientbound("pong_response");
/*    */   
/* 11 */   public static final PacketType<ServerboundPingRequestPacket> SERVERBOUND_PING_REQUEST = createServerbound("ping_request");
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ClientPongPacketListener>> PacketType<T> createClientbound(String id) {
/* 14 */     return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ServerPingPacketListener>> PacketType<T> createServerbound(String id) {
/* 18 */     return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\ping\PingPacketTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */