/*   */ package net.minecraft.commands.execution;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface UnboundEntryAction<T> {
/*   */   void execute(T paramT, ExecutionContext<T> paramExecutionContext, Frame paramFrame);
/*   */   
/*   */   default EntryAction<T> bind(T sender) {
/* 8 */     return (context, frame) -> execute((T)sender, context, frame);
/*   */   }
/*   */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\UnboundEntryAction.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */