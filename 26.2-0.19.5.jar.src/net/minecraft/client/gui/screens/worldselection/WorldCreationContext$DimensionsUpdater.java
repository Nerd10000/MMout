package net.minecraft.client.gui.screens.worldselection;

import java.util.function.BiFunction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.levelgen.WorldDimensions;

@FunctionalInterface
public interface DimensionsUpdater extends BiFunction<RegistryAccess.Frozen, WorldDimensions, WorldDimensions> {}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\worldselection\WorldCreationContext$DimensionsUpdater.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */