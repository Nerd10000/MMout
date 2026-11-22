package net.minecraft.util;

import java.util.OptionalLong;

public interface DownloadProgressListener {
  void requestStart();
  
  void downloadStart(OptionalLong paramOptionalLong);
  
  void downloadedBytes(long paramLong);
  
  void requestFinished(boolean paramBoolean);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\HttpUtil$DownloadProgressListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */