/*    */ package net.minecraft.network.protocol.handshake;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*    */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*    */ 
/*    */ public class HandshakeProtocols {
/*    */   public static final SimpleUnboundProtocol<ServerHandshakePacketListener, FriendlyByteBuf> SERVERBOUND_TEMPLATE;
/*    */   
/*    */   static {
/* 10 */     SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.HANDSHAKING, builder -> builder.addPacket(HandshakePacketTypes.CLIENT_INTENTION, ClientIntentionPacket.STREAM_CODEC));
/*    */   }
/*    */   
/* 14 */   public static final ProtocolInfo<ServerHandshakePacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\handshake\HandshakeProtocols.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */