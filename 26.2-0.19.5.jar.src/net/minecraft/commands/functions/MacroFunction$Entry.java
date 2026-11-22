package net.minecraft.commands.functions;

import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.minecraft.commands.FunctionInstantiationException;
import net.minecraft.commands.execution.UnboundEntryAction;
import net.minecraft.resources.Identifier;

interface Entry<T> {
  IntList parameters();
  
  UnboundEntryAction<T> instantiate(List<String> paramList, CommandDispatcher<T> paramCommandDispatcher, Identifier paramIdentifier) throws FunctionInstantiationException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\functions\MacroFunction$Entry.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */