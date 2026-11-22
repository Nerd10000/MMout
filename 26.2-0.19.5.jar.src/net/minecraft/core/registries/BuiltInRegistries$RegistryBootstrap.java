package net.minecraft.core.registries;

import net.minecraft.core.Registry;

@FunctionalInterface
interface RegistryBootstrap<T> {
  Object run(Registry<T> paramRegistry);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\registries\BuiltInRegistries$RegistryBootstrap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */