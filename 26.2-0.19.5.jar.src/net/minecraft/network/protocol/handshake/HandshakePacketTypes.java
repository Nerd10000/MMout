/*    */ package net.minecraft.network.protocol.handshake;
/*    */ 
/*    */ import net.minecraft.network.protocol.PacketFlow;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class HandshakePacketTypes {
/*  9 */   public static final PacketType<ClientIntentionPacket> CLIENT_INTENTION = createServerbound("intention");
/*    */   
/*    */   private static <T extends net.minecraft.network.protocol.Packet<ServerHandshakePacketListener>> PacketType<T> createServerbound(String id) {
/* 12 */     return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\handshake\HandshakePacketTypes.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */