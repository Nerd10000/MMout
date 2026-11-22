package net.minecraft.network.protocol.ping;

import net.minecraft.network.PacketListener;

public interface ClientPongPacketListener extends PacketListener {
  void handlePongResponse(ClientboundPongResponsePacket paramClientboundPongResponsePacket);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\ping\ClientPongPacketListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */