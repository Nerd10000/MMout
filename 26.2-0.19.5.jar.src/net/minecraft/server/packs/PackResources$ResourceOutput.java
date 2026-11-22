package net.minecraft.server.packs;

import java.io.InputStream;
import java.util.function.BiConsumer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.IoSupplier;

@FunctionalInterface
public interface ResourceOutput extends BiConsumer<Identifier, IoSupplier<InputStream>> {}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\PackResources$ResourceOutput.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */