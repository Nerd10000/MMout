package net.minecraft.commands.functions;

import java.util.List;
import net.minecraft.commands.execution.UnboundEntryAction;
import net.minecraft.resources.Identifier;

public interface InstantiatedFunction<T> {
  Identifier id();
  
  List<UnboundEntryAction<T>> entries();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\functions\InstantiatedFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */