package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.players.NameAndId;

@FunctionalInterface
public interface Result {
  Collection<NameAndId> getNames(CommandSourceStack paramCommandSourceStack) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\GameProfileArgument$Result.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */