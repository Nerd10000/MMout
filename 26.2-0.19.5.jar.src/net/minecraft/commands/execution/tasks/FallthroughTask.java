/*    */ package net.minecraft.commands.execution.tasks;
/*    */ 
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.EntryAction;
/*    */ import net.minecraft.commands.execution.ExecutionContext;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ 
/*    */ public class FallthroughTask<T extends ExecutionCommandSource<T>> implements EntryAction<T> {
/*  9 */   private static final FallthroughTask<? extends ExecutionCommandSource<?>> INSTANCE = new FallthroughTask();
/*    */   
/*    */   public static <T extends ExecutionCommandSource<T>> EntryAction<T> instance() {
/* 14 */     return (EntryAction)INSTANCE;
/*    */   }
/*    */   
/*    */   public void execute(ExecutionContext<T> context, Frame frame) {
/* 19 */     frame.returnFailure();
/* 22 */     frame.discard();
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\execution\tasks\FallthroughTask.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */