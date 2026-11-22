package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.font.GlyphProvider;
import java.io.IOException;
import net.minecraft.server.packs.resources.ResourceManager;

public interface Loader {
  GlyphProvider load(ResourceManager paramResourceManager) throws IOException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\font\providers\GlyphProviderDefinition$Loader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */