/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import com.mojang.brigadier.context.CommandContext;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.arguments.CompoundTagArgument;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ 
/*     */ class null extends FunctionCommand.FunctionCustomExecutor {
/*     */   protected CompoundTag arguments(CommandContext<CommandSourceStack> context) {
/* 100 */     return CompoundTagArgument.getCompoundTag(context, "arguments");
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\FunctionCommand$4.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */