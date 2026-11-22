package net.minecraft.network.protocol.game;

import java.util.UUID;
import net.minecraft.network.RegistryFriendlyByteBuf;

interface Operation {
  ClientboundBossEventPacket.OperationType getType();
  
  void dispatch(UUID paramUUID, ClientboundBossEventPacket.Handler paramHandler);
  
  void write(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundBossEventPacket$Operation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */