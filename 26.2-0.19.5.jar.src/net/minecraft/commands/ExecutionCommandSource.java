/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.ResultConsumer;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.execution.TraceCallbacks;
/*    */ import net.minecraft.server.permissions.PermissionSetSupplier;
/*    */ 
/*    */ public interface ExecutionCommandSource<T extends ExecutionCommandSource<T>> extends PermissionSetSupplier {
/*    */   T withCallback(CommandResultCallback paramCommandResultCallback);
/*    */   
/*    */   CommandResultCallback callback();
/*    */   
/*    */   default T clearCallbacks() {
/* 18 */     return withCallback(CommandResultCallback.EMPTY);
/*    */   }
/*    */   
/*    */   CommandDispatcher<T> dispatcher();
/*    */   
/*    */   void handleError(CommandExceptionType paramCommandExceptionType, Message paramMessage, boolean paramBoolean, TraceCallbacks paramTraceCallbacks);
/*    */   
/*    */   boolean isSilent();
/*    */   
/*    */   default void handleError(CommandSyntaxException e, boolean forked, TraceCallbacks tracer) {
/* 31 */     handleError(e.getType(), e.getRawMessage(), forked, tracer);
/*    */   }
/*    */   
/*    */   static <T extends ExecutionCommandSource<T>> ResultConsumer<T> resultConsumer() {
/* 36 */     return (context, success, result) -> ((ExecutionCommandSource)context.getSource()).callback().onResult(success, result);
/*    */   }
/*    */ }


/* Location:              C:\Users\karol\AppData\Roaming\ModrinthApp\meta\versions\26.2-0.19.5\26.2-0.19.5.jar!\net\minecraft\commands\ExecutionCommandSource.class
 * Java compiler version: 25 (69.0)
 * JD-Core Version:       1.1.3
 */