package net.minecraft.server.commands.data;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.List;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@FunctionalInterface
interface DataManipulator {
  int modify(CommandContext<CommandSourceStack> paramCommandContext, CompoundTag paramCompoundTag, NbtPathArgument.NbtPath paramNbtPath, List<Tag> paramList) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\data\DataCommands$DataManipulator.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */