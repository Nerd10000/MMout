/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ class null implements FunctionCommand.Callbacks<CommandSourceStack> {
/*     */   public void signalResult(CommandSourceStack originalSource, Identifier id, int newValue) {
/* 160 */     originalSource.sendSuccess(() -> Component.translatable("commands.function.result", new Object[] { Component.translationArg(id), Integer.valueOf(newValue) }), true);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\FunctionCommand$5.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */