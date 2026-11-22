/*   */ package net.minecraft.network.protocol.login;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.cookie.ServerCookiePacketListener;
/*   */ 
/*   */ public interface ServerLoginPacketListener extends ServerCookiePacketListener {
/*   */   default ConnectionProtocol protocol() {
/* 9 */     return ConnectionProtocol.LOGIN;
/*   */   }
/*   */   
/*   */   void handleHello(ServerboundHelloPacket paramServerboundHelloPacket);
/*   */   
/*   */   void handleKey(ServerboundKeyPacket paramServerboundKeyPacket);
/*   */   
/*   */   void handleCustomQueryPacket(ServerboundCustomQueryAnswerPacket paramServerboundCustomQueryAnswerPacket);
/*   */   
/*   */   void handleLoginAcknowledgement(ServerboundLoginAcknowledgedPacket paramServerboundLoginAcknowledgedPacket);
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\login\ServerLoginPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */