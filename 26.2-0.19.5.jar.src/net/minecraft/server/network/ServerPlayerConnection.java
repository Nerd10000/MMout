package net.minecraft.server.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;

public interface ServerPlayerConnection {
  ServerPlayer getPlayer();
  
  void send(Packet<?> paramPacket);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\network\ServerPlayerConnection.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */