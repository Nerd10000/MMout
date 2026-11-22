package net.minecraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.HolderLookup;

public interface RegistryContextSwapper {
  <T> DataResult<T> swapTo(Codec<T> paramCodec, T paramT, HolderLookup.Provider paramProvider);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\RegistryContextSwapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */