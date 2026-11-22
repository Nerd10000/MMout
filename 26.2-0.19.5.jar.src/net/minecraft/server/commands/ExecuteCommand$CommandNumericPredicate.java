package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
interface CommandNumericPredicate {
  int test(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\ExecuteCommand$CommandNumericPredicate.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */