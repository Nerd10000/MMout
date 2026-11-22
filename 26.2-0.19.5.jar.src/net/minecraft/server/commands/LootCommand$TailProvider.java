package net.minecraft.server.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

@FunctionalInterface
interface TailProvider {
  ArgumentBuilder<CommandSourceStack, ?> construct(ArgumentBuilder<CommandSourceStack, ?> paramArgumentBuilder, LootCommand.DropConsumer paramDropConsumer);
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\LootCommand$TailProvider.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */