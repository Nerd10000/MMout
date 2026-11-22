package net.minecraft.network.protocol;

import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import net.minecraft.network.ProtocolInfo;

public interface UnboundProtocol<T extends net.minecraft.network.PacketListener, B extends ByteBuf, C> extends ProtocolInfo.DetailsProvider {
  ProtocolInfo<T> bind(Function<ByteBuf, B> paramFunction, C paramC);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\UnboundProtocol.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */