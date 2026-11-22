package net.minecraft.core.component;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

@FunctionalInterface
interface CodecGetter {
  <T> StreamCodec<? super RegistryFriendlyByteBuf, T> apply(DataComponentType<T> paramDataComponentType);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\component\DataComponentPatch$CodecGetter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */