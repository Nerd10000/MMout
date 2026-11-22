package net.minecraft.commands;

import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.tree.ArgumentCommandNode;

@FunctionalInterface
public interface Output<S> {
  <T> void accept(CommandContextBuilder<S> paramCommandContextBuilder, ArgumentCommandNode<S, T> paramArgumentCommandNode, ParsedArgument<S, T> paramParsedArgument);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\ArgumentVisitor$Output.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */