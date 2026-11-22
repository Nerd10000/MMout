package net.minecraft.commands.arguments.selector.options;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;

@FunctionalInterface
public interface Modifier {
  void handle(EntitySelectorParser paramEntitySelectorParser) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\arguments\selector\options\EntitySelectorOptions$Modifier.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */