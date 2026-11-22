package net.minecraft.resources;

import java.util.Optional;
import net.minecraft.core.Registry;

public interface RegistryInfoLookup {
  <T> Optional<RegistryOps.RegistryInfo<T>> lookup(ResourceKey<? extends Registry<? extends T>> paramResourceKey);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryOps$RegistryInfoLookup.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */