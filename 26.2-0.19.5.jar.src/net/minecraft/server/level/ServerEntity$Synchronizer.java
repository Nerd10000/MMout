package net.minecraft.server.level;

import java.util.function.Predicate;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

public interface Synchronizer {
  void sendToTrackingPlayers(Packet<? super ClientGamePacketListener> paramPacket);
  
  void sendToTrackingPlayersAndSelf(Packet<? super ClientGamePacketListener> paramPacket);
  
  void sendToTrackingPlayersFiltered(Packet<? super ClientGamePacketListener> paramPacket, Predicate<ServerPlayer> paramPredicate);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\level\ServerEntity$Synchronizer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */