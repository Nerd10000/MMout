package net.minecraft.core;

import net.minecraft.data.worldgen.BootstrapContext;

@FunctionalInterface
public interface RegistryBootstrap<T> {
  void run(BootstrapContext<T> paramBootstrapContext);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\core\RegistrySetBuilder$RegistryBootstrap.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */