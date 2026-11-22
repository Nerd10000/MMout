package net.minecraft.server.commands.data;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.function.Function;
import net.minecraft.commands.CommandSourceStack;

public interface DataProvider {
  DataAccessor access(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException;
  
  ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> paramArgumentBuilder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> paramFunction);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\data\DataCommands$DataProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */