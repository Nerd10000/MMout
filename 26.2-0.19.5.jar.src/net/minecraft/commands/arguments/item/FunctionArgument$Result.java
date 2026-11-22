package net.minecraft.commands.arguments.item;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.functions.CommandFunction;
import net.minecraft.resources.Identifier;

public interface Result {
  Collection<CommandFunction<CommandSourceStack>> create(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException;
  
  Pair<Identifier, Either<CommandFunction<CommandSourceStack>, Collection<CommandFunction<CommandSourceStack>>>> unwrap(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException;
  
  Pair<Identifier, Collection<CommandFunction<CommandSourceStack>>> unwrapToCollection(CommandContext<CommandSourceStack> paramCommandContext) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\item\FunctionArgument$Result.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */