package net.minecraft.server.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.numbers.NumberFormat;

@FunctionalInterface
public interface NumberFormatCommandExecutor {
  int run(CommandContext<CommandSourceStack> paramCommandContext, NumberFormat paramNumberFormat) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\ScoreboardCommand$NumberFormatCommandExecutor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */