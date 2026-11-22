package net.minecraft.commands.arguments.blocks;

import java.util.function.Predicate;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

public interface Result extends Predicate<BlockInWorld> {
  boolean requiresNbt();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\blocks\BlockPredicateArgument$Result.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */