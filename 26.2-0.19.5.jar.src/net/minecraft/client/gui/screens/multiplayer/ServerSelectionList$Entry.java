package net.minecraft.client.gui.screens.multiplayer;

import net.minecraft.client.gui.components.ObjectSelectionList;

public abstract class Entry extends ObjectSelectionList.Entry<ServerSelectionList.Entry> implements AutoCloseable {
  public void close() {}
  
  protected abstract boolean matches(Entry paramEntry);
  
  public abstract void join();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\client\gui\screens\multiplayer\ServerSelectionList$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */