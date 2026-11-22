/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import com.mojang.brigadier.context.ContextChain;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ 
/*    */ public abstract class WithErrorHandling<T extends ExecutionCommandSource<T>> implements CustomCommandExecutor<T> {
/*    */   public final void run(T sender, ContextChain<T> currentStep, ChainModifiers modifiers, ExecutionControl<T> output) {
/*    */     try {
/* 24 */       runGuarded(sender, currentStep, modifiers, output);
/* 25 */     } catch (CommandSyntaxException e) {
/* 26 */       onError(e, sender, modifiers, output.tracer());
/* 27 */       sender.callback().onFailure();
/*    */     } 
/*    */   }
/*    */   
/*    */   protected void onError(CommandSyntaxException e, T sender, ChainModifiers modifiers, TraceCallbacks tracer) {
/* 32 */     sender.handleError(e, modifiers.isForked(), tracer);
/*    */   }
/*    */   
/*    */   protected abstract void runGuarded(T paramT, ContextChain<T> paramContextChain, ChainModifiers paramChainModifiers, ExecutionControl<T> paramExecutionControl) throws CommandSyntaxException;
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\CustomCommandExecutor$WithErrorHandling.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */