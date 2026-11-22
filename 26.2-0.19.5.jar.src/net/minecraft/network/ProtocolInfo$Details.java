package net.minecraft.network;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.util.VisibleForDebug;

public interface Details {
  ConnectionProtocol id();
  
  PacketFlow flow();
  
  @VisibleForDebug
  void listPackets(PacketVisitor paramPacketVisitor);
  
  @FunctionalInterface
  public static interface PacketVisitor {
    void accept(PacketType<?> param2PacketType, int param2Int);
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\ProtocolInfo$Details.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */