package net.minecraft.network.protocol.cookie;

import net.minecraft.network.ClientboundPacketListener;

public interface ClientCookiePacketListener extends ClientboundPacketListener {
  void handleRequestCookie(ClientboundCookieRequestPacket paramClientboundCookieRequestPacket);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\cookie\ClientCookiePacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */