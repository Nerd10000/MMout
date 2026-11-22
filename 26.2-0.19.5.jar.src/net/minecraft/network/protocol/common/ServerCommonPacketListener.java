package net.minecraft.network.protocol.common;

import net.minecraft.network.protocol.cookie.ServerCookiePacketListener;

public interface ServerCommonPacketListener extends ServerCookiePacketListener {
  void handleKeepAlive(ServerboundKeepAlivePacket paramServerboundKeepAlivePacket);
  
  void handlePong(ServerboundPongPacket paramServerboundPongPacket);
  
  void handleCustomPayload(ServerboundCustomPayloadPacket paramServerboundCustomPayloadPacket);
  
  void handleResourcePackResponse(ServerboundResourcePackPacket paramServerboundResourcePackPacket);
  
  void handleClientInformation(ServerboundClientInformationPacket paramServerboundClientInformationPacket);
  
  void handleCustomClickAction(ServerboundCustomClickActionPacket paramServerboundCustomClickActionPacket);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ServerCommonPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */