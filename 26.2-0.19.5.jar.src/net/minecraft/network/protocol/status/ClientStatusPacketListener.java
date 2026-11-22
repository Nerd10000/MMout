/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import net.minecraft.network.ClientboundPacketListener;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.protocol.ping.ClientPongPacketListener;
/*    */ 
/*    */ public interface ClientStatusPacketListener extends ClientboundPacketListener, ClientPongPacketListener {
/*    */   default ConnectionProtocol protocol() {
/* 10 */     return ConnectionProtocol.STATUS;
/*    */   }
/*    */   
/*    */   void handleStatusResponse(ClientboundStatusResponsePacket paramClientboundStatusResponsePacket);
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\status\ClientStatusPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */