/*   */ package net.minecraft.network.protocol.login;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.cookie.ClientCookiePacketListener;
/*   */ 
/*   */ public interface ClientLoginPacketListener extends ClientCookiePacketListener {
/*   */   default ConnectionProtocol protocol() {
/* 9 */     return ConnectionProtocol.LOGIN;
/*   */   }
/*   */   
/*   */   void handleHello(ClientboundHelloPacket paramClientboundHelloPacket);
/*   */   
/*   */   void handleLoginFinished(ClientboundLoginFinishedPacket paramClientboundLoginFinishedPacket);
/*   */   
/*   */   void handleDisconnect(ClientboundLoginDisconnectPacket paramClientboundLoginDisconnectPacket);
/*   */   
/*   */   void handleCompression(ClientboundLoginCompressionPacket paramClientboundLoginCompressionPacket);
/*   */   
/*   */   void handleCustomQuery(ClientboundCustomQueryPacket paramClientboundCustomQueryPacket);
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\login\ClientLoginPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */