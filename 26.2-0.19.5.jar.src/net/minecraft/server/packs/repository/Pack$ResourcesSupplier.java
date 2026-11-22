package net.minecraft.server.packs.repository;

import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;

public interface ResourcesSupplier {
  PackResources openPrimary(PackLocationInfo paramPackLocationInfo);
  
  PackResources openFull(PackLocationInfo paramPackLocationInfo, Pack.Metadata paramMetadata);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\repository\Pack$ResourcesSupplier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */