package net.minecraft.commands.execution;

import net.minecraft.resources.Identifier;

public interface TraceCallbacks extends AutoCloseable {
  void onCommand(int paramInt, String paramString);
  
  void onReturn(int paramInt1, String paramString, int paramInt2);
  
  void onError(String paramString);
  
  void onCall(int paramInt1, Identifier paramIdentifier, int paramInt2);
  
  void close();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\TraceCallbacks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */