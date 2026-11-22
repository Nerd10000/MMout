package net.minecraft.server.commands.data;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
interface StringProcessor {
  String process(String paramString) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\data\DataCommands$StringProcessor.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */