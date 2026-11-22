package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collection;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.scores.ScoreHolder;

@FunctionalInterface
public interface Result {
  Collection<ScoreHolder> getNames(CommandSourceStack paramCommandSourceStack, Supplier<Collection<ScoreHolder>> paramSupplier) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\ScoreHolderArgument$Result.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */