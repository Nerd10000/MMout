package net.minecraft.data.structures;

import net.minecraft.nbt.CompoundTag;

@FunctionalInterface
public interface Filter {
  CompoundTag apply(String paramString, CompoundTag paramCompoundTag);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\data\structures\SnbtToNbt$Filter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */