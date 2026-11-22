package net.minecraft.resources;

import java.util.Map;

@FunctionalInterface
interface LoaderFactory {
  <T> RegistryLoadTask<T> create(RegistryDataLoader.RegistryData<T> paramRegistryData, Map<ResourceKey<?>, Exception> paramMap);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\resources\RegistryDataLoader$LoaderFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */