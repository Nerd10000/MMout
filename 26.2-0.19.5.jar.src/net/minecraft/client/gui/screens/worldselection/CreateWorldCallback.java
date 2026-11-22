package net.minecraft.client.gui.screens.worldselection;

import java.nio.file.Path;
import java.util.Optional;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.storage.LevelDataAndDimensions;

@FunctionalInterface
public interface CreateWorldCallback {
  boolean create(CreateWorldScreen paramCreateWorldScreen, LayeredRegistryAccess<RegistryLayer> paramLayeredRegistryAccess, LevelDataAndDimensions.WorldDataAndGenSettings paramWorldDataAndGenSettings, Optional<GameRules> paramOptional, Path paramPath);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\CreateWorldCallback.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */