package net.minecraft.client.gui.components.debug;

import java.util.Collection;
import net.minecraft.resources.Identifier;

public interface DebugScreenDisplayer {
  void addPriorityLine(String paramString);
  
  void addLine(String paramString);
  
  void addToGroup(Identifier paramIdentifier, Collection<String> paramCollection);
  
  void addToGroup(Identifier paramIdentifier, String paramString);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\components\debug\DebugScreenDisplayer.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */