package net.minecraft.world.level.chunk;

import com.mojang.serialization.DataResult;

public interface Unpacker<T, C extends PalettedContainerRO<T>> {
  DataResult<C> read(Strategy<T> paramStrategy, PalettedContainerRO.PackedData<T> paramPackedData);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\world\level\chunk\PalettedContainerRO$Unpacker.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */