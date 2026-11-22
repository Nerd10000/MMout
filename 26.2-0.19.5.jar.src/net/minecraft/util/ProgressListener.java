package net.minecraft.util;

import net.minecraft.network.chat.Component;

public interface ProgressListener {
  void progressStartNoAbort(Component paramComponent);
  
  void progressStart(Component paramComponent);
  
  void progressStage(Component paramComponent);
  
  void progressStagePercentage(int paramInt);
  
  void stop();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraf\\util\ProgressListener.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */