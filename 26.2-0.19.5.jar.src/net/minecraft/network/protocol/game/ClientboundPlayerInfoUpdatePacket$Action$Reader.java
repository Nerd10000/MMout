package net.minecraft.network.protocol.game;

import net.minecraft.network.RegistryFriendlyByteBuf;

public interface Reader {
  void read(ClientboundPlayerInfoUpdatePacket.EntryBuilder paramEntryBuilder, RegistryFriendlyByteBuf paramRegistryFriendlyByteBuf);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\game\ClientboundPlayerInfoUpdatePacket$Action$Reader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */