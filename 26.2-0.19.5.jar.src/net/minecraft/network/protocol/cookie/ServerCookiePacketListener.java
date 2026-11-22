package net.minecraft.network.protocol.cookie;

import net.minecraft.network.protocol.game.ServerPacketListener;

public interface ServerCookiePacketListener extends ServerPacketListener {
  void handleCookieResponse(ServerboundCookieResponsePacket paramServerboundCookieResponsePacket);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\cookie\ServerCookiePacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */