package net.minecraft.network.protocol.game;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface Writer {
  void write(RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf, ClientboundPlayerInfoUpdatePacket.Entry paramEntry);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoUpdatePacket$Action$Writer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */