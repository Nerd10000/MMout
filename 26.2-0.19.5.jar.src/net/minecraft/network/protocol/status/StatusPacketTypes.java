/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class StatusPacketTypes {
/*  9 */   public static final PacketType<ClientboundStatusResponsePacket> CLIENTBOUND_STATUS_RESPONSE = createClientbound("status_response");
/*    */   
/* 11 */   public static final PacketType<ServerboundStatusRequestPacket> SERVERBOUND_STATUS_REQUEST = createServerbound("status_request");
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ClientStatusPacketListener>> PacketType<T> createClientbound(String id) {
/* 14 */     return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ServerStatusPacketListener>> PacketType<T> createServerbound(String id) {
/* 18 */     return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\status\StatusPacketTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */