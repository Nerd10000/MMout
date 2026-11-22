/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import com.mojang.brigadier.Command;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ 
/*    */ public interface CommandAdapter<T> extends CustomCommandExecutor<T>, Command<T> {
/*    */   default int run(CommandContext<T> context) throws CommandSyntaxException {
/* 16 */     throw new UnsupportedOperationException("This function should not run");
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\CustomCommandExecutor$CommandAdapter.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */