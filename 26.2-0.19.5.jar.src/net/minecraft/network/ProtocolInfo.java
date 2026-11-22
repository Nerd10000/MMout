package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.BundlerInfo;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.util.VisibleForDebug;

public interface ProtocolInfo<T extends PacketListener> {
  ConnectionProtocol id();
  
  PacketFlow flow();
  
  StreamCodec<ByteBuf, Packet<? super T>> codec();
  
  BundlerInfo bundlerInfo();
  
  public static interface DetailsProvider {
    ProtocolInfo.Details details();
  }
  
  public static interface Details {
    ConnectionProtocol id();
    
    PacketFlow flow();
    
    @VisibleForDebug
    void listPackets(PacketVisitor param1PacketVisitor);
    
    @FunctionalInterface
    public static interface PacketVisitor {
      void accept(PacketType<?> param2PacketType, int param2Int);
    }
  }
  
  @FunctionalInterface
  public static interface PacketVisitor {
    void accept(PacketType<?> param1PacketType, int param1Int);
  }
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\ProtocolInfo.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */