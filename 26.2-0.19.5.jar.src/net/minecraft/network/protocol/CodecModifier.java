package net.minecraft.network.protocol;

import net.minecraft.network.codec.StreamCodec;

@FunctionalInterface
public interface CodecModifier<B, V, C> {
  StreamCodec<? super B, V> apply(StreamCodec<? super B, V> paramStreamCodec, C paramC);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\protocol\CodecModifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */