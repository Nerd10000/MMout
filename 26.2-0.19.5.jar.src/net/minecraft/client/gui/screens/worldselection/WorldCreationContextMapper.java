package net.minecraft.client.gui.screens.worldselection;

import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;

@FunctionalInterface
public interface WorldCreationContextMapper {
  WorldCreationContext apply(ReloadableServerResources paramReloadableServerResources, LayeredRegistryAccess<RegistryLayer> paramLayeredRegistryAccess, DataPackReloadCookie paramDataPackReloadCookie);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldCreationContextMapper.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */