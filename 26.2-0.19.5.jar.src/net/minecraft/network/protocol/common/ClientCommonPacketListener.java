package net.minecraft.network.protocol.common;

import net.minecraft.network.protocol.cookie.ClientCookiePacketListener;

public interface ClientCommonPacketListener extends ClientCookiePacketListener {
  void handleKeepAlive(ClientboundKeepAlivePacket paramClientboundKeepAlivePacket);
  
  void handlePing(ClientboundPingPacket paramClientboundPingPacket);
  
  void handleCustomPayload(ClientboundCustomPayloadPacket paramClientboundCustomPayloadPacket);
  
  void handleDisconnect(ClientboundDisconnectPacket paramClientboundDisconnectPacket);
  
  void handleResourcePackPush(ClientboundResourcePackPushPacket paramClientboundResourcePackPushPacket);
  
  void handleResourcePackPop(ClientboundResourcePackPopPacket paramClientboundResourcePackPopPacket);
  
  void handleUpdateTags(ClientboundUpdateTagsPacket paramClientboundUpdateTagsPacket);
  
  void handleStoreCookie(ClientboundStoreCookiePacket paramClientboundStoreCookiePacket);
  
  void handleTransfer(ClientboundTransferPacket paramClientboundTransferPacket);
  
  void handleCustomReportDetails(ClientboundCustomReportDetailsPacket paramClientboundCustomReportDetailsPacket);
  
  void handleServerLinks(ClientboundServerLinksPacket paramClientboundServerLinksPacket);
  
  void handleClearDialog(ClientboundClearDialogPacket paramClientboundClearDialogPacket);
  
  void handleShowDialog(ClientboundShowDialogPacket paramClientboundShowDialogPacket);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\ClientCommonPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */