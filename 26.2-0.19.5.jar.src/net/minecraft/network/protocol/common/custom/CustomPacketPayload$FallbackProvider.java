package net.minecraft.network.protocol.common.custom;

import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public interface FallbackProvider<B extends net.minecraft.network.FriendlyByteBuf> {
  StreamCodec<B, ? extends CustomPacketPayload> create(Identifier paramIdentifier);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\common\custom\CustomPacketPayload$FallbackProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */