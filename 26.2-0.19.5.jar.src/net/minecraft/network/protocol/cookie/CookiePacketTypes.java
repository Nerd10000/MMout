/*    */ package net.minecraft.network.protocol.cookie;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class CookiePacketTypes {
/*  9 */   public static final PacketType<ClientboundCookieRequestPacket> CLIENTBOUND_COOKIE_REQUEST = createClientbound("cookie_request");
/*    */   
/* 11 */   public static final PacketType<ServerboundCookieResponsePacket> SERVERBOUND_COOKIE_RESPONSE = createServerbound("cookie_response");
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ClientCookiePacketListener>> PacketType<T> createClientbound(String id) {
/* 14 */     return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ServerCookiePacketListener>> PacketType<T> createServerbound(String id) {
/* 18 */     return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\cookie\CookiePacketTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */