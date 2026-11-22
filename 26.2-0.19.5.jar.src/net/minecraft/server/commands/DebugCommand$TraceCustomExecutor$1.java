/*     */ package net.minecraft.server.commands;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import net.minecraft.commands.CommandResultCallback;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.commands.ExecutionCommandSource;
/*     */ import net.minecraft.commands.execution.ExecutionContext;
/*     */ import net.minecraft.commands.execution.Frame;
/*     */ import net.minecraft.commands.execution.tasks.CallFunction;
/*     */ import net.minecraft.commands.functions.CommandFunction;
/*     */ import net.minecraft.commands.functions.InstantiatedFunction;
/*     */ 
/*     */ class null extends CallFunction<CommandSourceStack> {
/*     */   null(DebugCommand.TraceCustomExecutor this$0, InstantiatedFunction<CommandSourceStack> function, CommandResultCallback resultCallback, boolean returnParentFrame) {
/* 129 */     super(function, resultCallback, returnParentFrame);
/*     */   }
/*     */   
/*     */   public void execute(CommandSourceStack sender, ExecutionContext<CommandSourceStack> context, Frame frame) {
/* 132 */     output.println(function.id());
/* 133 */     super.execute((ExecutionCommandSource)sender, context, frame);
/*     */   }
/*     */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\server\commands\DebugCommand$TraceCustomExecutor$1.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */