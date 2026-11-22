package net.minecraft.commands.synchronization;

import net.minecraft.commands.CommandBuildContext;

public interface Template<A extends com.mojang.brigadier.arguments.ArgumentType<?>> {
  A instantiate(CommandBuildContext paramCommandBuildContext);
  
  ArgumentTypeInfo<A, ?> type();
}


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\synchronization\ArgumentTypeInfo$Template.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */