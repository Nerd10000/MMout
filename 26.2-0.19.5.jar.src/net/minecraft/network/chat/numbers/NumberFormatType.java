package net.minecraft.network.chat.numbers;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface NumberFormatType<T extends NumberFormat> {
  MapCodec<T> mapCodec();
  
  StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\network\chat\numbers\NumberFormatType.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */