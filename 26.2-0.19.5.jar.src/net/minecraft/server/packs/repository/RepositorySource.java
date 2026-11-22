package net.minecraft.server.packs.repository;

import java.util.function.Consumer;

@FunctionalInterface
public interface RepositorySource {
  void loadPacks(Consumer<Pack> paramConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\packs\repository\RepositorySource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */