package net.minecraft.commands.arguments;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.scores.ScoreAccess;

@FunctionalInterface
public interface Operation {
  void apply(ScoreAccess paramScoreAccess1, ScoreAccess paramScoreAccess2) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\OperationArgument$Operation.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */