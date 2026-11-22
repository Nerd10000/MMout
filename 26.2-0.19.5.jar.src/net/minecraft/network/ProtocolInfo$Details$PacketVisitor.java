package net.minecraft.network;

import net.minecraft.network.protocol.PacketType;

@FunctionalInterface
public interface PacketVisitor {
  void accept(PacketType<?> paramPacketType, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\ProtocolInfo$Details$PacketVisitor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */