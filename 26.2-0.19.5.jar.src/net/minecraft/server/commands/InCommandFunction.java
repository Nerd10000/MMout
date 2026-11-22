package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface InCommandFunction<T, R> {
  R apply(T paramT) throws CommandSyntaxException;
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\InCommandFunction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */