/*   */ package net.minecraft.network.protocol.handshake;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.game.ServerPacketListener;
/*   */ 
/*   */ public interface ServerHandshakePacketListener extends ServerPacketListener {
/*   */   default ConnectionProtocol protocol() {
/* 9 */     return ConnectionProtocol.HANDSHAKING;
/*   */   }
/*   */   
/*   */   void handleIntention(ClientIntentionPacket paramClientIntentionPacket);
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\handshake\ServerHandshakePacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */