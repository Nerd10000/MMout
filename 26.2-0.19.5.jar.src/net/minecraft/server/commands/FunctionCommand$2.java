/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.arguments.NbtPathArgument;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.server.commands.data.DataCommands;
/*    */ 
/*    */ class null extends FunctionCommand.FunctionCustomExecutor {
/*    */   protected CompoundTag arguments(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
/* 76 */     return FunctionCommand.getArgumentTag(NbtPathArgument.getPath(context, "path"), provider.access(context));
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\FunctionCommand$2.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */