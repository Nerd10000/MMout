package net.minecraft.server.commands;

import net.minecraft.resources.Identifier;

public interface Callbacks<T> {
  void signalResult(T paramT, Identifier paramIdentifier, int paramInt);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\FunctionCommand$Callbacks.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */