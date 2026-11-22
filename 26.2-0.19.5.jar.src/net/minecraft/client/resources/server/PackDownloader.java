package net.minecraft.client.resources.server;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.server.packs.DownloadQueue;

public interface PackDownloader {
  void download(Map<UUID, DownloadQueue.DownloadRequest> paramMap, Consumer<DownloadQueue.BatchResult> paramConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\resources\server\PackDownloader.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */