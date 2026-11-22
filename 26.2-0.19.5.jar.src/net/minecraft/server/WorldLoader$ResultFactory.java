package net.minecraft.server;

import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.packs.resources.CloseableResourceManager;

@FunctionalInterface
public interface ResultFactory<D, R> {
  R create(CloseableResourceManager paramCloseableResourceManager, ReloadableServerResources paramReloadableServerResources, LayeredRegistryAccess<RegistryLayer> paramLayeredRegistryAccess, D paramD);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\WorldLoader$ResultFactory.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */